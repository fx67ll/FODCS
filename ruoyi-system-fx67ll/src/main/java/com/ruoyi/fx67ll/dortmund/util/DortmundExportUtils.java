package com.ruoyi.fx67ll.dortmund.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.ruoyi.common.exception.ServiceException;

/**
 * Dortmund模块结构化导出工具。
 * <p>
 * 列表导出统一支持 Excel、JSON 和插入 SQL。JSON 与 SQL 复用 Controller 已按当前用户数据范围
 * 和筛选条件查询出的同一批业务对象，不再次拼接查询条件，避免导出范围与页面不一致。
 * SQL 仅输出显式声明的业务表字段，不依据 Java 字段名反射推断表结构，也不包含页面派生字段。
 *
 * @author fx67ll
 * @date 2026-11-25
 */
public final class DortmundExportUtils {
    /** Excel 由各 Controller 继续复用既有 ExcelUtil 与导出 DTO。 */
    public static final String TYPE_EXCEL = "excel";
    /** JSON 使用可读的 camelCase 业务字段名。 */
    public static final String TYPE_JSON = "json";
    /** SQL 使用 MySQL 可执行的单行 INSERT 语句。 */
    public static final String TYPE_SQL = "sql";

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TABLE_NAME_PATTERN = "[A-Za-z][A-Za-z0-9_]*";
    private static final String COLUMN_NAME_PATTERN = "[A-Za-z][A-Za-z0-9]*";
    private static final TimeZone EXPORT_TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");

    private DortmundExportUtils() {
    }

    /**
     * 判断导出类型是否由本工具处理。
     */
    public static boolean isStructured(String exportType) {
        String normalized = normalizeType(exportType);
        return TYPE_JSON.equals(normalized) || TYPE_SQL.equals(normalized);
    }

    /**
     * 按指定类型输出结构化文件。
     *
     * @param response HTTP 响应
     * @param filePrefix 文件名前缀，仅使用稳定 ASCII 业务名
     * @param exportType json 或 sql
     * @param tableName 插入 SQL 对应的业务表名
     * @param columns Java 业务字段名数组，SQL 自动转换为下划线列名
     * @param items 当前筛选和数据范围内待导出的业务对象
     */
    public static void export(HttpServletResponse response, String filePrefix, String exportType,
            String tableName, String[] columns, List<?> items) throws IOException {
        String normalized = normalizeType(exportType);
        if (!isStructured(normalized)) {
            throw new ServiceException("不支持的导出类型：" + exportType);
        }
        validateIdentifier(tableName, TABLE_NAME_PATTERN, "导出表名");
        if (columns == null || columns.length == 0) {
            throw new ServiceException("导出字段配置不能为空！");
        }
        for (String column : columns) {
            validateIdentifier(column, COLUMN_NAME_PATTERN, "导出字段名");
        }

        List<Map<String, Object>> rows = buildRows(columns, items);
        String content;
        String extension;
        String contentType;
        if (TYPE_SQL.equals(normalized)) {
            content = buildSql(tableName, columns, rows);
            extension = ".sql";
            contentType = "application/sql;charset=UTF-8";
        } else {
            // PrettyFormat 便于人工核对，同时保持标准 JSON 数组结构，不输出 NaN 等非标准值。
            content = JSON.toJSONString(rows, JSONWriter.Feature.PrettyFormat);
            extension = ".json";
            contentType = "application/json;charset=UTF-8";
        }

        String fileName = safeFileName(filePrefix) + "-" + timestamp() + extension;
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replace("+", "%20");
        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(contentType);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + encodedFileName);
        response.getOutputStream().write(content.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    /**
     * 将业务对象按显式字段顺序转换为导出行。
     */
    private static List<Map<String, Object>> buildRows(String[] columns, List<?> items) {
        List<Map<String, Object>> rows = new ArrayList<>();
        if (items == null) {
            return rows;
        }
        for (Object item : items) {
            if (item == null) {
                continue;
            }
            Map<String, Object> row = new LinkedHashMap<>();
            for (String column : columns) {
                row.put(column, normalizeValue(readProperty(item, column)));
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 生成带业务来源说明的确定性插入 SQL。
     */
    private static String buildSql(String tableName, String[] columns, List<Map<String, Object>> rows) {
        StringBuilder sql = new StringBuilder();
        sql.append("-- Dortmund模块业务数据导出\n");
        sql.append("-- 导出时间：").append(timestamp()).append("\n");
        sql.append("-- 数据范围：当前登录用户可见数据及导出时的页面筛选条件\n");
        sql.append("-- 说明：每条 INSERT 仅包含显式映射的业务字段，不包含页面派生字段\n\n");
        for (Map<String, Object> row : rows) {
            sql.append("INSERT INTO `").append(tableName).append("` (");
            for (int index = 0; index < columns.length; index++) {
                if (index > 0) {
                    sql.append(", ");
                }
                sql.append('`').append(toSnakeCase(columns[index])).append('`');
            }
            sql.append(") VALUES (");
            for (int index = 0; index < columns.length; index++) {
                if (index > 0) {
                    sql.append(", ");
                }
                sql.append(toSqlValue(row.get(columns[index])));
            }
            sql.append(");\n");
        }
        return sql.toString();
    }

    private static Object readProperty(Object bean, String propertyName) {
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
                if (propertyName.equals(descriptor.getName()) && descriptor.getReadMethod() != null) {
                    return descriptor.getReadMethod().invoke(bean);
                }
            }
        } catch (Exception exception) {
            throw new ServiceException("读取导出字段“" + propertyName + "”失败！");
        }
        throw new ServiceException("导出对象缺少业务字段“" + propertyName + "”！");
    }

    private static Object normalizeValue(Object value) {
        if (value instanceof Date) {
            return formatDate((Date) value);
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if (value instanceof BigInteger) {
            return value.toString();
        }
        return value;
    }

    private static String toSqlValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        String text = String.valueOf(value);
        text = text.replace("\\", "\\\\")
                .replace("\u0000", "\\0")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("'", "''");
        return "'" + text + "'";
    }

    private static String toSnakeCase(String value) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (Character.isUpperCase(current)) {
                if (index > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(current));
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private static String normalizeType(String exportType) {
        String normalized = exportType == null ? "" : exportType.trim().toLowerCase();
        return normalized.isEmpty() ? TYPE_EXCEL : normalized;
    }

    private static void validateIdentifier(String value, String pattern, String label) {
        if (value == null || !value.matches(pattern)) {
            throw new ServiceException(label + "格式不正确！");
        }
        if (value.length() > 64) {
            throw new ServiceException(label + "长度超出限制！");
        }
    }

    private static String safeFileName(String filePrefix) {
        String value = filePrefix == null ? "dortmund-export" : filePrefix.trim();
        if (value.isEmpty() || !value.matches("[A-Za-z0-9_-]+")) {
            return "dortmund-export";
        }
        return value;
    }

    private static String timestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(EXPORT_TIME_ZONE);
        return formatter.format(new Date());
    }

    private static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        formatter.setTimeZone(EXPORT_TIME_ZONE);
        return formatter.format(date);
    }
}
