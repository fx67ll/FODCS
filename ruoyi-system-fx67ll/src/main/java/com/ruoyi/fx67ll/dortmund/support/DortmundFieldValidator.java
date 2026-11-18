package com.ruoyi.fx67ll.dortmund.support;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.ruoyi.common.exception.ServiceException;

/**
 * Dortmund 业务字段统一校验工具
 * <p>
 * 校验规则严格对齐 fx67ll_freedom_2026.sql 中的字段长度、数值精度与枚举约束。
 * 文本统一按 Unicode 字符数校验并在落库前去除首尾空格，避免前端校验通过后仍触发数据库截断或类型异常。
 *
 * @author fx67ll
 */
public final class DortmundFieldValidator {
    /**
     * 普通十进制数字格式，不接受科学计数法、正号和仅含小数点的文本
     */
    private static final Pattern DECIMAL_TEXT_PATTERN = Pattern.compile("^-?\\d+(?:\\.\\d+)?$");

    private DortmundFieldValidator() {
    }

    /**
     * 校验必填文本并返回去除首尾空格后的值
     *
     * @param value     待校验文本
     * @param label     字段名称
     * @param maxLength 数据库允许的最大字符数
     * @return 去除首尾空格后的文本
     */
    public static String requireText(String value, String label, int maxLength) {
        return normalizeText(value, label, maxLength, true);
    }

    /**
     * 校验选填文本并返回去除首尾空格后的值，空白文本统一转为 null
     *
     * @param value     待校验文本
     * @param label     字段名称
     * @param maxLength 数据库允许的最大字符数
     * @return 去除首尾空格后的文本或 null
     */
    public static String optionalText(String value, String label, int maxLength) {
        return normalizeText(value, label, maxLength, false);
    }

    /**
     * 校验枚举值并返回去除首尾空格后的值
     *
     * @param value    待校验值
     * @param label    字段名称
     * @param required 是否必填
     * @param options  允许的枚举值
     * @return 标准化后的枚举值或 null
     */
    public static String validateEnum(String value, String label, boolean required, String... options) {
        String normalized = normalizeText(value, label, 16, required);
        if (normalized != null && !Arrays.asList(options).contains(normalized)) {
            throw new ServiceException(label + "取值不正确，允许值为：" + String.join("/", options) + "！");
        }
        return normalized;
    }

    /**
     * 校验整数取值范围
     *
     * @param value    待校验值
     * @param label    字段名称
     * @param min      允许的最小值
     * @param max      允许的最大值
     * @param required 是否必填
     */
    public static void validateInteger(Integer value, String label, int min, int max, boolean required) {
        if (value == null) {
            if (required) {
                throw new ServiceException(label + "不能为空！");
            }
            return;
        }
        if (value < min || value > max) {
            throw new ServiceException(label + "必须在" + min + "到" + max + "之间！");
        }
    }

    /**
     * 校验长整数取值范围
     *
     * @param value    待校验值
     * @param label    字段名称
     * @param min      允许的最小值
     * @param max      允许的最大值
     * @param required 是否必填
     */
    public static void validateLong(Long value, String label, long min, long max, boolean required) {
        if (value == null) {
            if (required) {
                throw new ServiceException(label + "不能为空！");
            }
            return;
        }
        if (value < min || value > max) {
            throw new ServiceException(label + "必须在" + min + "到" + max + "之间！");
        }
    }

    /**
     * 校验普通十进制文本，返回去除首尾空格后的原始文本
     * <p>
     * 支持同时限制总字符数、整数位、小数位、正负号及业务取值范围，
     * 与前端 decimalRule 和数据库字段精度保持一致。
     *
     * @param value             待校验文本
     * @param label             字段名称
     * @param required          是否必填
     * @param allowNegative     是否允许负数
     * @param maxIntegerDigits  最大整数位数
     * @param maxFractionDigits 最大小数位数
     * @param maxTextLength     最大文本长度
     * @param minInclusive      最小值（包含）
     * @param minExclusive      最小值（不包含）
     * @param maxInclusive      最大值（包含）
     * @return 去除首尾空格后的文本或 null
     */
    public static String validateDecimalText(String value, String label, boolean required, boolean allowNegative,
            int maxIntegerDigits, int maxFractionDigits, int maxTextLength, BigDecimal minInclusive,
            BigDecimal minExclusive, BigDecimal maxInclusive) {
        String text = normalizeText(value, label, maxTextLength, required);
        if (text == null) {
            return null;
        }
        if (!DECIMAL_TEXT_PATTERN.matcher(text).matches()) {
            throw new ServiceException(label + "必须为普通十进制数字，不能使用科学计数法！");
        }
        if (!allowNegative && text.charAt(0) == '-') {
            throw new ServiceException(label + "不能为负数！");
        }
        String unsigned = text.charAt(0) == '-' ? text.substring(1) : text;
        String[] parts = unsigned.split("\\.", -1);
        String integerDigits = parts[0].replaceFirst("^0+", "");
        if (integerDigits.length() > maxIntegerDigits) {
            throw new ServiceException(label + "整数位不能超过" + maxIntegerDigits + "位！");
        }
        String fractionDigits = parts.length > 1 ? parts[1].replaceFirst("0+$", "") : "";
        if (fractionDigits.length() > maxFractionDigits) {
            throw new ServiceException(label + "小数位不能超过" + maxFractionDigits + "位！");
        }
        BigDecimal numeric = new BigDecimal(text);
        validateDecimalRange(numeric, label, minInclusive, minExclusive, maxInclusive);
        return text;
    }

    /**
     * 校验 BigDecimal 的精度、符号与取值范围
     *
     * @param value             待校验值
     * @param label             字段名称
     * @param required          是否必填
     * @param allowNegative     是否允许负数
     * @param minInclusive      最小值（包含）
     * @param minExclusive      最小值（不包含）
     * @param maxInclusive      最大值（包含）
     * @param maxIntegerDigits  最大整数位数
     * @param maxFractionDigits 最大小数位数
     */
    public static void validateDecimal(BigDecimal value, String label, boolean required, boolean allowNegative,
            BigDecimal minInclusive, BigDecimal minExclusive, BigDecimal maxInclusive,
            int maxIntegerDigits, int maxFractionDigits) {
        if (value == null) {
            if (required) {
                throw new ServiceException(label + "不能为空！");
            }
            return;
        }
        if (!allowNegative && value.signum() < 0) {
            throw new ServiceException(label + "不能为负数！");
        }
        int fractionDigits = Math.max(value.stripTrailingZeros().scale(), 0);
        if (fractionDigits > maxFractionDigits) {
            throw new ServiceException(label + "小数位不能超过" + maxFractionDigits + "位！");
        }
        BigDecimal integerPart = value.abs().setScale(0, RoundingMode.DOWN);
        if (integerPart.compareTo(BigDecimal.TEN.pow(maxIntegerDigits)) >= 0) {
            throw new ServiceException(label + "整数位不能超过" + maxIntegerDigits + "位！");
        }
        validateDecimalRange(value, label, minInclusive, minExclusive, maxInclusive);
    }

    private static String normalizeText(String value, String label, int maxLength, boolean required) {
        if (value == null || value.trim().isEmpty()) {
            if (required) {
                throw new ServiceException(label + "不能为空！");
            }
            return null;
        }
        String normalized = value.trim();
        if (normalized.codePointCount(0, normalized.length()) > maxLength) {
            throw new ServiceException(label + "不能超过" + maxLength + "个字符！");
        }
        return normalized;
    }

    private static void validateDecimalRange(BigDecimal value, String label, BigDecimal minInclusive,
            BigDecimal minExclusive, BigDecimal maxInclusive) {
        if (minInclusive != null && value.compareTo(minInclusive) < 0) {
            throw new ServiceException(label + "不能小于" + minInclusive.stripTrailingZeros().toPlainString() + "！");
        }
        if (minExclusive != null && value.compareTo(minExclusive) <= 0) {
            throw new ServiceException(label + "必须大于" + minExclusive.stripTrailingZeros().toPlainString() + "！");
        }
        if (maxInclusive != null && value.compareTo(maxInclusive) > 0) {
            throw new ServiceException(label + "不能大于" + maxInclusive.stripTrailingZeros().toPlainString() + "！");
        }
    }
}
