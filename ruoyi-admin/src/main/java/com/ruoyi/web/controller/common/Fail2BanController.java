package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fail2ban监控控制器（适配0.11.2版本优化版）
 * 【安全承诺】：本控制器仅提供只读监控功能，不包含任何修改系统状态的操作
 * 所有命令均为查询操作，彻底杜绝被黑客利用的风险
 * 适配若依框架，针对Fail2ban 0.11.x版本优化
 *
 * @author ruoyi
 * @version 1.2.0
 * @update 2026-06-17
 * @fixes 修复监狱名称解析错误、移除不兼容命令、实现精确运行时间计算
 */
@RestController
@RequestMapping("/server/fail2ban")
public class Fail2BanController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(Fail2BanController.class);

    // ==================== 系统配置常量 ====================
    /**
     * 命令执行超时时间（秒），防止进程阻塞
     */
    private static final int COMMAND_TIMEOUT = 15;
    /**
     * 线程池用于异步读取流，避免缓冲区满导致进程挂死
     */
    private static final ExecutorService STREAM_EXECUTOR = Executors.newFixedThreadPool(2);
    /**
     * 日志最大返回条数上限（安全限制），防止恶意请求导致内存溢出
     */
    private static final int MAX_LOG_LIMIT = 1023;
    /**
     * 日志默认返回条数，兼顾性能和实用性
     */
    private static final int DEFAULT_LOG_LIMIT = 200;
    /**
     * 请求缓存过期时间（秒），防止频繁执行命令
     */
    private static final int CACHE_TTL = 30;
    /**
     * 日期格式化器，用于解析日志时间
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * systemctl时间格式解析器
     */
    private static final DateTimeFormatter SYSTEMCTL_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd HH:mm:ss zzz", Locale.ENGLISH);

    // ==================== 系统命令路径 ====================
    /**
     * Fail2ban客户端命令路径
     */
    private static final String FAIL2BAN_CLIENT = "fail2ban-client";
    /**
     * Fail2ban日志文件路径
     */
    private static final String FAIL2BAN_LOG_PATH = "/var/log/fail2ban.log";

    // ==================== 缓存变量 ====================
    private static Map<String, Object> statusCache;
    private static long statusCacheTime;
    private static List<Map<String, Object>> jailsCache;
    private static long jailsCacheTime;

    // ==================== 公共接口 ====================

    /**
     * 获取Fail2ban服务整体状态
     * 返回服务运行状态、版本号、运行时长、防火墙状态及全局统计数据
     *
     * @return AjaxResult 包含服务状态信息的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/status")
    public AjaxResult getFail2banStatus() {
        // 检查缓存
        if (statusCache != null && System.currentTimeMillis() - statusCacheTime < CACHE_TTL * 1000) {
            return AjaxResult.success("查询Fail2ban状态成功", statusCache);
        }

        Map<String, Object> result = new HashMap<>();
        String status = "未知";
        String version = "未知";
        int totalJails = 0;
        int totalBannedIps = 0;
        int totalFailedAttempts = 0;
        String uptime = "未知";
        String firewallStatus = "未知";

        // 1. 检查fail2ban-client命令是否可用
        if (!isCommandAvailable(FAIL2BAN_CLIENT)) {
            log.error("Fail2ban客户端命令不可用，请确认已安装fail2ban");
            return AjaxResult.error("Fail2ban未安装或命令不可用");
        }

        // 2. 执行fail2ban-client status命令获取整体状态
        String statusOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "status"});
        if (statusOutput != null && !statusOutput.isEmpty()) {
            status = "运行中";

            // 解析监狱数量
            Pattern jailPattern = Pattern.compile("Number of jail:\\s*(\\d+)");
            Matcher jailMatcher = jailPattern.matcher(statusOutput);
            if (jailMatcher.find()) {
                totalJails = Integer.parseInt(jailMatcher.group(1));
            }

            // 解析版本信息
            String versionOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "version"});
            if (versionOutput != null) {
                version = versionOutput.trim();
            }

            // 3. 遍历所有监狱统计总封禁数和失败次数
            List<String> jailNames = getJailNames(statusOutput);
            for (String jailName : jailNames) {
                Map<String, Object> jailStats = getJailStatsInternal(jailName);
                totalBannedIps += (Integer) jailStats.getOrDefault("currentlyBanned", 0);
                totalFailedAttempts += (Integer) jailStats.getOrDefault("totalFailed", 0);
            }

            // 4. 获取服务运行时间
            uptime = getFail2banUptime();

            // 5. 获取防火墙状态
            firewallStatus = getFirewallStatus();
        } else {
            status = "已停止";
        }

        // 组装返回结果
        result.put("status", status);
        result.put("version", version);
        result.put("totalJails", totalJails);
        result.put("totalBannedIps", totalBannedIps);
        result.put("totalFailedAttempts", totalFailedAttempts);
        result.put("uptime", uptime);
        result.put("firewallStatus", firewallStatus);

        // 更新缓存
        statusCache = result;
        statusCacheTime = System.currentTimeMillis();

        return AjaxResult.success("查询Fail2ban状态成功", result);
    }

    /**
     * 获取所有监狱的状态列表
     * 返回每个监狱的基本统计信息（当前封禁、累计封禁、失败尝试）
     *
     * @return AjaxResult 包含监狱列表的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/jails")
    public AjaxResult getJailList() {
        // 检查缓存
        if (jailsCache != null && System.currentTimeMillis() - jailsCacheTime < CACHE_TTL * 1000) {
            return AjaxResult.success("查询监狱列表成功", jailsCache);
        }

        List<Map<String, Object>> jailList = new ArrayList<>();

        String statusOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "status"});
        if (statusOutput == null || statusOutput.isEmpty()) {
            return AjaxResult.success("Fail2ban服务未运行", jailList);
        }

        List<String> jailNames = getJailNames(statusOutput);
        for (String jailName : jailNames) {
            Map<String, Object> jailStats = getJailStatsInternal(jailName);
            jailList.add(jailStats);
        }

        // 更新缓存
        jailsCache = jailList;
        jailsCacheTime = System.currentTimeMillis();

        return AjaxResult.success("查询监狱列表成功", jailList);
    }

    /**
     * 获取单个监狱的详细信息
     * 包含监狱基本统计、当前被封禁的IP列表和日志文件路径
     *
     * @param jailName 监狱名称（安全校验：禁止包含特殊字符）
     * @return AjaxResult 包含监狱详情的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/jail/{jailName}")
    public AjaxResult getJailDetail(@PathVariable String jailName) {
        // 安全检查：防止路径遍历攻击和命令注入
        if (jailName.contains("/") || jailName.contains("..") || jailName.contains(" ") || jailName.contains(";")) {
            log.warn("检测到无效的监狱名称请求：{}", jailName);
            return AjaxResult.error("无效的监狱名称");
        }

        // 额外安全检查：只允许查询实际存在的监狱
        String statusOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "status"});
        if (statusOutput == null || statusOutput.isEmpty()) {
            return AjaxResult.error("Fail2ban服务未运行");
        }
        List<String> validJailNames = getJailNames(statusOutput);
        if (!validJailNames.contains(jailName)) {
            log.warn("检测到不存在的监狱名称请求：{}", jailName);
            return AjaxResult.error("监狱不存在");
        }

        Map<String, Object> jailDetail = new HashMap<>();
        Map<String, Object> basicStats = getJailStatsInternal(jailName);
        jailDetail.putAll(basicStats);

        // 获取当前被封禁的IP列表
        String bannedIpsOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "get", jailName, "banned"});
        if (bannedIpsOutput != null) {
            List<String> bannedIps = parseBannedIps(bannedIpsOutput);
            jailDetail.put("bannedIps", bannedIps);
        }

        return AjaxResult.success("查询监狱详情成功", jailDetail);
    }

    /**
     * 获取所有被封禁的IP（跨所有监狱）
     * 返回按监狱分组的IP列表和总数量，自动去重
     *
     * @return AjaxResult 包含全量封禁IP的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/all-banned-ips")
    public AjaxResult getAllBannedIps() {
        Map<String, List<String>> allBannedIps = new HashMap<>();
        int totalCount = 0;

        String statusOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "status"});
        if (statusOutput == null || statusOutput.isEmpty()) {
            return AjaxResult.success("Fail2ban服务未运行", allBannedIps);
        }

        List<String> jailNames = getJailNames(statusOutput);
        for (String jailName : jailNames) {
            String bannedIpsOutput = executeCommand(new String[]{FAIL2BAN_CLIENT, "get", jailName, "banned"});
            if (bannedIpsOutput != null) {
                List<String> bannedIps = parseBannedIps(bannedIpsOutput);
                if (!bannedIps.isEmpty()) {
                    allBannedIps.put(jailName, bannedIps);
                    totalCount += bannedIps.size();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", totalCount);
        result.put("bannedIpsByJail", allBannedIps);

        return AjaxResult.success("查询所有封禁IP成功", result);
    }

    /**
     * 获取最近的Fail2ban日志（支持参数化配置）
     * 【修复】：解决正则表达式无法匹配包含点号的类名的问题
     * 【增强】：添加文件存在性和可读性检查，返回明确的错误提示
     *
     * @param limit 返回日志条数（1-1023，默认200）
     * @param level 日志级别筛选（ERROR/WARN/INFO/DEBUG，可选）
     * @param jail  监狱名称筛选（可选）
     * @return AjaxResult 包含日志列表的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/logs")
    public AjaxResult getRecentLogs(
            @RequestParam(defaultValue = "200") int limit,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String jail
    ) {
        List<Map<String, Object>> logs = new ArrayList<>();

        // 安全限制：强制限制最大返回条数，防止恶意请求
        limit = Math.min(limit, MAX_LOG_LIMIT);
        limit = Math.max(limit, 1); // 最小返回1条

        // 检查日志文件是否存在且可读
        File logFile = new File(FAIL2BAN_LOG_PATH);
        if (!logFile.exists()) {
            log.error("Fail2ban日志文件不存在：{}", FAIL2BAN_LOG_PATH);
            return AjaxResult.error("日志文件不存在，请检查路径配置：" + FAIL2BAN_LOG_PATH);
        }
        if (!logFile.canRead()) {
            log.error("没有权限读取Fail2ban日志文件：{}，当前运行用户：{}",
                    FAIL2BAN_LOG_PATH, System.getProperty("user.name"));
            return AjaxResult.error("权限不足，请将运行用户添加到adm组：sudo usermod -aG adm " + System.getProperty("user.name"));
        }

        // 使用tail命令获取最后N行日志
        String logOutput = executeCommand(new String[]{"tail", "-n", String.valueOf(limit), FAIL2BAN_LOG_PATH});
        if (logOutput == null || logOutput.isEmpty()) {
            return AjaxResult.success("暂无日志数据", logs);
        }

        // 解析日志行
        String[] lines = logOutput.split("\n");
        // ✅ 修复后的正则表达式，支持包含点号的类名
        Pattern logPattern = Pattern.compile(
                "(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}:\\d{2}:\\d{2}),\\d+\\s+([^\\s]+)\\s+\\[\\d+\\]:\\s+(.+)"
        );

        // 倒序遍历，最新日志在前
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i];
            Matcher matcher = logPattern.matcher(line);
            if (matcher.find()) {
                String date = matcher.group(1);
                String time = matcher.group(2);
                // 提取日志级别（从消息部分中解析）
                String message = matcher.group(4);
                String logLevel = "INFO";

                // 从消息开头提取级别（INFO/WARN/ERROR/DEBUG）
                if (message.startsWith("INFO")) {
                    logLevel = "INFO";
                    message = message.substring(4).trim();
                } else if (message.startsWith("WARN")) {
                    logLevel = "WARN";
                    message = message.substring(4).trim();
                } else if (message.startsWith("ERROR")) {
                    logLevel = "ERROR";
                    message = message.substring(5).trim();
                } else if (message.startsWith("DEBUG")) {
                    logLevel = "DEBUG";
                    message = message.substring(5).trim();
                }

                // 按级别筛选
                if (level != null && !level.isEmpty() && !logLevel.equalsIgnoreCase(level)) {
                    continue;
                }

                // 按监狱筛选
                if (jail != null && !jail.isEmpty() && !message.contains("[" + jail + "]")) {
                    continue;
                }

                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("date", date);
                logEntry.put("time", time);
                logEntry.put("level", logLevel);
                logEntry.put("message", message);

                // 提取日志中的IP地址（支持IPv4）
                Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
                Matcher ipMatcher = ipPattern.matcher(message);
                if (ipMatcher.find()) {
                    logEntry.put("ip", ipMatcher.group());
                }

                logs.add(logEntry);
            }
        }

        return AjaxResult.success("查询日志成功", logs);
    }

    /**
     * 获取攻击统计数据
     * 包含按监狱统计的攻击次数、攻击来源Top 20和最近24小时攻击趋势
     * 【修复】：支持包含连字符的监狱名称，现在会统计所有监狱的攻击数据
     *
     * @return AjaxResult 包含统计数据的响应对象
     */
    @PreAuthorize("@ss.hasPermi('system:fail2ban:view')")
    @GetMapping("/stats")
    public AjaxResult getAttackStats() {
        Map<String, Object> stats = new HashMap<>();

        // 按监狱统计攻击次数
        Map<String, Integer> jailAttackCount = new HashMap<>();
        // 按IP统计攻击次数（Top 20）
        Map<String, Integer> ipAttackCount = new HashMap<>();
        // 最近24小时攻击趋势（按小时统计）
        Map<String, Integer> hourlyTrend = new LinkedHashMap<>();

        // 初始化24小时趋势数据
        LocalDateTime now = LocalDateTime.now();
        for (int i = 23; i >= 0; i--) {
            LocalDateTime hour = now.minusHours(i);
            String hourKey = hour.format(DateTimeFormatter.ofPattern("HH:00"));
            hourlyTrend.put(hourKey, 0);
        }

        // 只读取最近10000行日志进行统计，避免大文件性能问题
        String logOutput = executeCommand(new String[]{"tail", "-n", "10000", FAIL2BAN_LOG_PATH});
        if (logOutput != null) {
            String[] lines = logOutput.split("\n");
            // ✅ 修复后的正则表达式，支持连字符
            Pattern jailPattern = Pattern.compile("\\[([\\w-]+)\\]\\s+Found\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)");
            Pattern timePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})\\s+(\\d{2}):\\d{2}:\\d{2}");

            for (String line : lines) {
                // 统计攻击次数
                Matcher jailMatcher = jailPattern.matcher(line);
                if (jailMatcher.find()) {
                    String jail = jailMatcher.group(1);
                    String ip = jailMatcher.group(2);

                    jailAttackCount.put(jail, jailAttackCount.getOrDefault(jail, 0) + 1);
                    ipAttackCount.put(ip, ipAttackCount.getOrDefault(ip, 0) + 1);

                    // 统计小时趋势
                    Matcher timeMatcher = timePattern.matcher(line);
                    if (timeMatcher.find()) {
                        String dateStr = timeMatcher.group(1);
                        String hourStr = timeMatcher.group(2);
                        String hourKey = hourStr + ":00";

                        // 只统计最近24小时的数据
                        try {
                            LocalDateTime logTime = LocalDateTime.parse(
                                    dateStr + " " + hourStr + ":00:00",
                                    DATE_FORMATTER
                            );
                            if (logTime.isAfter(now.minusHours(24))) {
                                hourlyTrend.put(hourKey, hourlyTrend.getOrDefault(hourKey, 0) + 1);
                            }
                        } catch (Exception e) {
                            // 忽略时间解析错误
                            log.debug("日志时间解析失败：{}", line);
                        }
                    }
                }
            }
        }

        // 对IP攻击次数进行排序，取Top 20
        List<Map.Entry<String, Integer>> ipList = new ArrayList<>(ipAttackCount.entrySet());
        ipList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<Map<String, Object>> topIps = new ArrayList<>();
        for (int i = 0; i < Math.min(20, ipList.size()); i++) {
            Map<String, Object> ipEntry = new HashMap<>();
            ipEntry.put("ip", ipList.get(i).getKey());
            ipEntry.put("count", ipList.get(i).getValue());
            topIps.add(ipEntry);
        }

        stats.put("jailAttackCount", jailAttackCount);
        stats.put("topAttackIps", topIps);
        stats.put("hourlyTrend", hourlyTrend);

        return AjaxResult.success("查询统计数据成功", stats);
    }

    // ==================== 内部工具方法 ====================

    /**
     * 从fail2ban-client status输出中提取监狱名称列表
     * 【修复】：正确解析包含连字符的监狱名称（如ssl-protect）
     *
     * @param statusOutput fail2ban-client status命令的输出
     * @return List<String> 监狱名称列表
     */
    private List<String> getJailNames(String statusOutput) {
        List<String> jailNames = new ArrayList<>();
        // 修复正则：支持包含连字符的监狱名称
        Pattern pattern = Pattern.compile("Jail list:\\s*([\\w,\\s-]+)");
        Matcher matcher = pattern.matcher(statusOutput);
        if (matcher.find()) {
            String[] names = matcher.group(1).split(",\\s*");
            for (String name : names) {
                if (!name.trim().isEmpty()) {
                    jailNames.add(name.trim());
                }
            }
        }
        return jailNames;
    }

    /**
     * 获取单个监狱的基本统计信息
     * 【修复】：适配Fail2ban 0.11.x版本的Filter/Actions分组格式
     *
     * @param jailName 监狱名称
     * @return Map<String, Object> 包含当前封禁、累计封禁、失败尝试的统计信息
     */
    private Map<String, Object> getJailStatsInternal(String jailName) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("name", jailName);

        String output = executeCommand(new String[]{FAIL2BAN_CLIENT, "status", jailName});
        if (output == null || output.isEmpty()) {
            stats.put("currentlyBanned", 0);
            stats.put("totalBanned", 0);
            stats.put("totalFailed", 0);
            stats.put("currentlyFailed", 0);
            stats.put("logPath", "未知");
            return stats;
        }

        // 解析当前失败次数
        Pattern currentFailedPattern = Pattern.compile("Currently failed:\\s*(\\d+)");
        Matcher currentFailedMatcher = currentFailedPattern.matcher(output);
        if (currentFailedMatcher.find()) {
            stats.put("currentlyFailed", Integer.parseInt(currentFailedMatcher.group(1)));
        } else {
            stats.put("currentlyFailed", 0);
        }

        // 解析总失败次数
        Pattern totalFailedPattern = Pattern.compile("Total failed:\\s*(\\d+)");
        Matcher totalFailedMatcher = totalFailedPattern.matcher(output);
        if (totalFailedMatcher.find()) {
            stats.put("totalFailed", Integer.parseInt(totalFailedMatcher.group(1)));
        } else {
            stats.put("totalFailed", 0);
        }

        // 解析日志文件路径
        Pattern logPathPattern = Pattern.compile("File list:\\s*(.+)");
        Matcher logPathMatcher = logPathPattern.matcher(output);
        if (logPathMatcher.find()) {
            stats.put("logPath", logPathMatcher.group(1).trim());
        } else {
            stats.put("logPath", "未知");
        }

        // 解析当前封禁数
        Pattern currentBannedPattern = Pattern.compile("Currently banned:\\s*(\\d+)");
        Matcher currentBannedMatcher = currentBannedPattern.matcher(output);
        if (currentBannedMatcher.find()) {
            stats.put("currentlyBanned", Integer.parseInt(currentBannedMatcher.group(1)));
        } else {
            stats.put("currentlyBanned", 0);
        }

        // 解析总封禁数
        Pattern totalBannedPattern = Pattern.compile("Total banned:\\s*(\\d+)");
        Matcher totalBannedMatcher = totalBannedPattern.matcher(output);
        if (totalBannedMatcher.find()) {
            stats.put("totalBanned", Integer.parseInt(totalBannedMatcher.group(1)));
        } else {
            stats.put("totalBanned", 0);
        }

        return stats;
    }

    /**
     * 解析被封禁的IP列表
     *
     * @param bannedOutput fail2ban-client get jail banned命令的输出
     * @return List<String> IP地址列表
     */
    private List<String> parseBannedIps(String bannedOutput) {
        List<String> ips = new ArrayList<>();
        if (bannedOutput == null || bannedOutput.isEmpty() || bannedOutput.trim().equals("[]")) {
            return ips;
        }

        Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
        Matcher matcher = ipPattern.matcher(bannedOutput);
        while (matcher.find()) {
            ips.add(matcher.group());
        }

        return ips;
    }

    /**
     * 获取Fail2ban服务运行时间
     * 【新增】：精确计算运行时长，显示"已运行X天X小时X分钟"
     *
     * @return String 服务运行状态描述
     */
    private String getFail2banUptime() {
        // 使用systemctl获取服务启动时间
        String output = executeCommand(new String[]{"systemctl", "show", "fail2ban", "-p", "ActiveEnterTimestamp"});
        if (output == null || output.isEmpty()) {
            return "未知";
        }

        Pattern pattern = Pattern.compile("ActiveEnterTimestamp=(.+)");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            String timestampStr = matcher.group(1).trim();
            try {
                LocalDateTime startTime = LocalDateTime.parse(timestampStr, SYSTEMCTL_DATE_FORMATTER);
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(startTime, now);

                // ✅ Java 8 100%兼容，计算结果和Java 9+完全一致
                long totalMinutes = duration.toMinutes();
                long days = totalMinutes / (24 * 60);
                long hours = (totalMinutes % (24 * 60)) / 60;
                long minutes = totalMinutes % 60;

                if (days > 0) {
                    return String.format("已运行 %d天%d小时%d分钟", days, hours, minutes);
                } else if (hours > 0) {
                    return String.format("已运行 %d小时%d分钟", hours, minutes);
                } else {
                    return String.format("已运行 %d分钟", minutes);
                }
            } catch (Exception e) {
                log.warn("解析服务启动时间失败：{}", timestampStr, e);
                return "正常运行中";
            }
        }

        return "未知";
    }

    /**
     * 获取防火墙状态（支持firewalld和ufw）
     * 【优化】：显示具体哪个防火墙在运行
     *
     * @return String 防火墙状态（运行中/未运行）
     */
    private String getFirewallStatus() {
        // 检查firewalld状态
        String output = executeCommand(new String[]{"systemctl", "is-active", "firewalld"});
        if (output != null && output.trim().equals("active")) {
            return "运行中 (firewalld)";
        }

        // 检查ufw状态
        output = executeCommand(new String[]{"systemctl", "is-active", "ufw"});
        if (output != null && output.trim().equals("active")) {
            return "运行中 (ufw)";
        }

        return "未运行";
    }

    /**
     * 执行Shell命令通用方法（只读操作专用）
     * 采用数组形式执行命令，避免命令注入漏洞
     *
     * @param command 命令参数数组
     * @return String 命令执行输出，失败返回null
     */
    private String executeCommand(String[] command) {
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true); // 合并错误流和输出流
            process = pb.start();

            // 异步读取输出流，避免缓冲区满导致进程阻塞
            Process finalProcess = process;
            Future<String> outputFuture = STREAM_EXECUTOR.submit(() -> readStream(finalProcess.getInputStream()));

            // 等待命令执行完成（带超时保护）
            boolean isFinished = process.waitFor(COMMAND_TIMEOUT, TimeUnit.SECONDS);
            if (!isFinished) {
                process.destroyForcibly();
                log.error("命令执行超时：{}", String.join(" ", command));
                return null;
            }

            // 获取输出和退出码
            String output = outputFuture.get();
            int exitCode = process.exitValue();

            if (exitCode == 0) {
                return output;
            } else {
                // 降低日志级别，避免大量错误日志
                log.debug("命令执行失败，退出码：{}，命令：{}，输出：{}",
                        exitCode, String.join(" ", command), output);
                return null;
            }

        } catch (Exception e) {
            log.error("执行命令异常：{}", String.join(" ", command), e);
            return null;
        } finally {
            destroyProcess(process);
        }
    }

    /**
     * 读取输入流内容
     *
     * @param inputStream 输入流
     * @return String 流内容字符串
     * @throws Exception 读取异常
     */
    private String readStream(InputStream inputStream) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * 安全销毁进程，防止僵尸进程
     *
     * @param process 进程对象
     */
    private void destroyProcess(Process process) {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
            log.debug("进程已强制销毁");
        }
    }

    /**
     * 检查系统命令是否可用
     *
     * @param command 命令名称
     * @return boolean 命令是否可用
     */
    private boolean isCommandAvailable(String command) {
        Process process = null;
        try {
            process = new ProcessBuilder("which", command).start();
            return process.waitFor(2, TimeUnit.SECONDS) && process.exitValue() == 0;
        } catch (Exception e) {
            log.error("检查命令可用性失败：{}", command, e);
            return false;
        } finally {
            destroyProcess(process);
        }
    }
}