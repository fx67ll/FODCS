package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公开服务状态控制器（面向游客，免登录只读）
 *
 * 自包含采集与脱敏，独立于现有 /monitor/server、/server/* 接口，只回脱敏聚合数据：
 * 在线服务状态、稳定运行天数、累计拦截次数。绝不返回 IP/路径/版本/阈值。
 * 采集任务每 60 秒一次，结果写内存缓存，接口只读缓存，零实时采集开销。
 *
 * 调度说明：当前用 Spring @Scheduled，由 RuoYiApplication 的 @EnableScheduling 驱动。
 * 若后续启用若依 Quartz 做定时任务统一管理，应把本类采集改造为 sys_job 任务（删 @Scheduled，由 Quartz 调度 collect）。
 */
@RestController
@RequestMapping("/public/status")
public class PublicStatusController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(PublicStatusController.class);

    private static final String LOG_PREFIX = "[PublicStatus] ";

    // ==================== 命令绝对路径（防路径劫持，与 Fail2BanController 一致） ====================
    private static final String SUDO_CMD = "/usr/bin/sudo";
    private static final String FAIL2BAN_CLIENT = "/usr/bin/fail2ban-client";
    private static final String WHICH_CMD = "/usr/bin/which";

    // 命令执行超时时间（秒）
    private static final int COMMAND_TIMEOUT = 5;

    // ==================== 正则常量 ====================
    // 监狱列表行：Jail list: sshd, recidive
    private static final Pattern JAIL_LIST_PATTERN = Pattern.compile("Jail list:\\s*(.+)");
    // 单监狱 total failed 行
    private static final Pattern TOTAL_FAILED_PATTERN = Pattern.compile("Total failed:\\s*(\\d+)");

    // ==================== 监控服务清单（仅暴露名称与布尔状态，全部脱敏） ====================
    private static final String[] MONITORED_SERVICES = {"fail2ban", "tomcat", "jenkins"};

    // 各服务进程探活关键字：用 ps aux 查进程判断是否在线
    // 不用 systemctl is-active，因为 tomcat/jenkins 是 startup.sh 或 WAR 方式启动、非 systemd 服务，
    // systemctl 永远判为 inactive，会导致状态大盘误报为离线（与 TomcatController 进程关键字探活保持一致）
    private static final Map<String, String> SERVICE_PROCESS_KEYWORD = new LinkedHashMap<>();
    static {
        SERVICE_PROCESS_KEYWORD.put("fail2ban", "fail2ban-server");
        SERVICE_PROCESS_KEYWORD.put("tomcat", "apache-tomcat");
        // jenkins 不在此注册：它作为 Tomcat 的 webapp 部署，与 Tomcat 共用同一个 JVM，
        // ps aux 里只有 apache-tomcat 这条 java 进程，没有独立的 jenkins 进程，关键字无法匹配。
        // 故 jenkins 状态直接跟随 tomcat（见 collectServiceStatus 中 JENKINS_KEY 分支）。
    }

    // jenkins 跟随 tomcat 的在线判定：jenkins 部在 tomcat webapps 下，tomcat 在线即视为 jenkins 在线
    private static final String JENKINS_FOLLOW_KEY = "tomcat";

    // 异步读取进程输出流，避免缓冲区满导致进程阻塞
    private static final ThreadPoolExecutor STREAM_EXECUTOR = new ThreadPoolExecutor(
            1, 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(8),
            r -> {
                Thread t = new Thread(r, "public-status-stream");
                t.setDaemon(true);
                return t;
            });

    // 脱敏状态缓存，采集任务单写、对外接口多读
    private volatile Map<String, Object> statusCache = null;

    /**
     * 应用启动后立即采集一次，避免首次访问大盘空白
     */
    @PostConstruct
    public void init() {
        try {
            collect();
        } catch (Exception e) {
            log.warn(LOG_PREFIX + "启动期采集失败，将等待首次定时任务重试：{}", e.getMessage());
        }
    }

    /**
     * 定时采集任务：每 60 秒一次（fixedDelay 天然防重叠）
     * 采集本身轻量，60 秒既能保证数据新鲜度，开销也可忽略
     */
    @Scheduled(fixedDelay = 60 * 1000L, initialDelay = 60 * 1000L)
    public void scheduledCollect() {
        try {
            collect();
        } catch (Exception e) {
            // 采集失败保留上次缓存，仅记录日志，不重试
            log.warn(LOG_PREFIX + "定时采集异常，保留上次缓存：{}", e.getMessage());
        }
    }

    /**
     * 执行一次采集并刷新缓存
     * 单指标失败仅降级该指标，不影响整体；public 便于后续改造为 Quartz 任务
     */
    public void collect() {
        Map<String, Object> data = new LinkedHashMap<>();

        // 各服务在线状态（仅布尔 + 展示名）
        Map<String, Boolean> serviceStatus = collectServiceStatus();
        data.put("services", serviceStatus);

        // 在线服务数
        long onlineCount = serviceStatus.values().stream().filter(Boolean::booleanValue).count();
        data.put("onlineServiceCount", onlineCount);
        data.put("totalServiceCount", (long) serviceStatus.size());

        // 累计拦截攻击次数（仅计数，不给任何 IP）
        data.put("totalBlockedAttempts", collectTotalBlockedAttempts());

        // 防护是否启用（仅布尔，不暴露阈值）
        data.put("fail2banEnabled", serviceStatus.getOrDefault("fail2ban", Boolean.FALSE));

        // ==================== 服务器运行指标（全部脱敏，不含 IP/路径/版本/阈值） ====================
        // 操作系统运行时间（读取 /proc/uptime 换算，至少 1 天）—— 与 JVM uptime 区分开，反映整机开机时长
        data.put("osUptimeDays", collectOsUptimeDays());

        // CPU/内存/磁盘/负载指标
        CpuMemoryStat cm = collectCpuMemory();
        data.put("cpuUsage", cm.cpuUsage);           // CPU 使用率百分比（0-100）
        data.put("memoryUsage", cm.memoryUsage);      // 内存使用率百分比（0-100）
        data.put("memoryUsedGB", cm.memoryUsedGB);    // 已用内存（GB，1 位小数）
        data.put("memoryTotalGB", cm.memoryTotalGB);  // 总内存（GB，1 位小数）
        data.put("diskUsage", collectDiskUsage());    // 根分区磁盘使用率百分比（0-100）

        // 系统负载（1/5/15 分钟平均负载，归一化为相对核数的小数，不暴露核数）
        double[] loadAvg = collectLoadAverage();
        data.put("load1", loadAvg[0]);
        data.put("load5", loadAvg[1]);
        data.put("load15", loadAvg[2]);

        // ==================== JVM 运行时长（应用进程运行总小时数，与 osUptimeDays 整机开机天数区分） ====================
        // 用最小单位"小时"而非"天"：刚部署完也能如实反映"本次启动运行了多久"，不会因不足1天被兜底成1天。
        // 前端按 ≥24 小时换算为天展示，<24 小时直接显示小时。
        data.put("uptimeHours", collectUptimeHours());

        // 缓存刷新时间（前端"更新于 Xmin 前"展示用）
        data.put("lastUpdateTime", System.currentTimeMillis());

        this.statusCache = data;
        log.info(LOG_PREFIX + "采集完成：在线服务 {}/{}，OS运行 {} 天，CPU {}%，内存 {}%，磁盘 {}%，累计拦截 {} 次",
                onlineCount, serviceStatus.size(), data.get("osUptimeDays"),
                data.get("cpuUsage"), data.get("memoryUsage"), data.get("diskUsage"),
                data.get("totalBlockedAttempts"));
    }

    /**
     * 获取脱敏服务状态总览（游客免登录）
     * 只读内存缓存，零采集成本；缓存未就绪返回 initializing 占位
     */
    @Anonymous
    @GetMapping("/overview")
    public AjaxResult overview() {
        return AjaxResult.success(getStatusSnapshot());
    }

    /**
     * 获取脱敏状态快照，缓存未就绪时返回初始化中占位
     */
    private Map<String, Object> getStatusSnapshot() {
        Map<String, Object> snapshot = this.statusCache;
        if (snapshot == null) {
            Map<String, Object> placeholder = new LinkedHashMap<>();
            placeholder.put("initializing", true);
            placeholder.put("lastUpdateTime", 0L);
            return placeholder;
        }
        return snapshot;
    }

    // ==================== 各指标采集 ====================

    /**
     * 采集各服务在线状态
     * - fail2ban/tomcat：ps aux 进程关键字探活（不用 systemctl is-active，非 systemd 启动会误判 inactive）
     * - jenkins：作为 Tomcat 的 webapp 部署，与 Tomcat 共用 JVM，无独立进程，直接跟随 tomcat 在线状态
     */
    private Map<String, Boolean> collectServiceStatus() {
        Map<String, Boolean> result = new LinkedHashMap<>();
        // 一次 ps aux，避免每个服务各查一次
        String psOutput = executeCommand(new String[]{"/bin/ps", "aux"});
        for (String service : MONITORED_SERVICES) {
            boolean online = false;
            try {
                String keyword = SERVICE_PROCESS_KEYWORD.get(service);
                if (keyword == null) {
                    // 无进程关键字的服务（如 jenkins）跟随指定服务（tomcat）的在线状态
                    online = result.getOrDefault(JENKINS_FOLLOW_KEY, Boolean.FALSE);
                } else {
                    online = isProcessRunning(psOutput, keyword);
                }
            } catch (Exception e) {
                log.debug(LOG_PREFIX + "探活服务 {} 失败：{}", service, e.getMessage());
            }
            result.put(service, online);
        }
        return result;
    }

    /**
     * 判断进程是否在运行：在 ps aux 输出中查找关键字，排除 grep 自身行
     */
    private boolean isProcessRunning(String psOutput, String keyword) {
        if (psOutput == null || psOutput.isEmpty() || keyword == null || keyword.isEmpty()) {
            return false;
        }
        for (String line : psOutput.split("\n")) {
            if (line.contains(keyword) && !line.contains("grep")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 采集应用进程运行总小时数（JVM uptime 换算，至少 1 小时）
     * 用小时而非天：刚部署完也能如实反映"本次启动运行了多久"，不足1天不会被兜底成1天。
     * 前端按 ≥24 小时换算为天展示，<24 小时直接显示小时。
     */
    private long collectUptimeHours() {
        try {
            long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
            return Math.max(1L, (uptimeMs / (60L * 60 * 1000)));
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "采集运行小时数失败：{}", e.getMessage());
            return 1L;
        }
    }

    /**
     * 采集操作系统运行天数（读 /proc/uptime 第一列秒数换算，至少 1 天）
     * /proc/uptime 不含敏感信息，仅整机开机时长
     */
    private long collectOsUptimeDays() {
        try {
            String content = readFile("/proc/uptime");
            if (content == null || content.isEmpty()) {
                return 1L;
            }
            String uptimeSecondsStr = content.trim().split("\\s+")[0];
            double uptimeSeconds = Double.parseDouble(uptimeSecondsStr);
            long days = (long) (uptimeSeconds / (24 * 60 * 60));
            return Math.max(1L, days);
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "采集OS运行天数失败：{}", e.getMessage());
            return 1L;
        }
    }

    /**
     * CPU 与内存使用率（com.sun.management.OperatingSystemMXBean）
     * - cpuUsage：近一次采样的系统 CPU 使用率（0-100），首次调用可能返回负值，降级为 0
     * - memoryUsage：物理内存使用率（0-100）
     * - memoryUsedGB/memoryTotalGB：保留 1 位小数
     */
    private CpuMemoryStat collectCpuMemory() {
        CpuMemoryStat stat = new CpuMemoryStat();
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double cpuUsage = osBean.getSystemCpuLoad() * 100;
            stat.cpuUsage = round1(cpuUsage >= 0 ? cpuUsage : 0);
            long totalBytes = osBean.getTotalPhysicalMemorySize();
            long freeBytes = osBean.getFreePhysicalMemorySize();
            long usedBytes = totalBytes - freeBytes;
            stat.memoryTotalGB = round1(totalBytes / (1024.0 * 1024 * 1024));
            stat.memoryUsedGB = round1(usedBytes / (1024.0 * 1024 * 1024));
            stat.memoryUsage = round1(totalBytes > 0 ? (usedBytes * 100.0 / totalBytes) : 0);
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "采集CPU/内存失败：{}", e.getMessage());
        }
        return stat;
    }

    /**
     * 采集根分区磁盘使用率（java.io.File.getUsableSpace，仅暴露百分比，不含挂载路径）
     */
    private double collectDiskUsage() {
        try {
            File root = new File("/");
            long total = root.getTotalSpace();
            long usable = root.getUsableSpace();
            long used = total - usable;
            return round1(total > 0 ? (used * 100.0 / total) : 0);
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "采集磁盘使用率失败：{}", e.getMessage());
            return 0;
        }
    }

    /**
     * 采集系统平均负载（/proc/loadavg 1/5/15 分钟）
     * 数值含义为相对 CPU 核数的负载比，但此处不暴露核数，仅展示原始小数。
     * 保留 2 位小数：/proc/loadavg 原生即 2 位小数，空闲时常见 0.01/0.02，
     * 若只保留 1 位会被四舍五入成 0.0，让用户误以为采集异常。
     */
    private double[] collectLoadAverage() {
        double[] load = new double[]{0, 0, 0};
        try {
            String content = readFile("/proc/loadavg");
            if (content == null || content.isEmpty()) {
                return load;
            }
            String[] parts = content.trim().split("\\s+");
            load[0] = round2(Double.parseDouble(parts[0]));
            load[1] = round2(Double.parseDouble(parts[1]));
            load[2] = round2(Double.parseDouble(parts[2]));
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "采集系统负载失败：{}", e.getMessage());
        }
        return load;
    }

    /**
     * 读小文件为字符串（/proc 下的虚拟文件，单次读取）
     */
    private String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 保留 1 位小数
     */
    private double round1(double value) {
        return Math.round(value * 10) / 10.0;
    }

    /**
     * 保留 2 位小数（用于系统负载等需要精细刻度的指标）
     */
    private double round2(double value) {
        return Math.round(value * 100) / 100.0;
    }

    /**
     * CPU/内存采集结果载体
     */
    private static class CpuMemoryStat {
        double cpuUsage = 0;
        double memoryUsage = 0;
        double memoryUsedGB = 0;
        double memoryTotalGB = 0;
    }

    /**
     * 采集 Fail2Ban 累计拦截次数（各监狱 total failed 之和，不返回任何 IP）
     * fail2ban.sqlite3 仅存当前封禁记录、不含累计计数，故用 fail2ban-client
     */
    private long collectTotalBlockedAttempts() {
        try {
            // 前置检查：fail2ban-client 是否可用
            String which = executeCommand(new String[]{WHICH_CMD, FAIL2BAN_CLIENT});
            if (which == null || which.trim().isEmpty()) {
                return 0L;
            }

            String statusOutput = executeCommand(new String[]{SUDO_CMD, FAIL2BAN_CLIENT, "status"});
            if (statusOutput == null || statusOutput.isEmpty()) {
                return 0L;
            }

            Matcher jailMatcher = JAIL_LIST_PATTERN.matcher(statusOutput);
            if (!jailMatcher.find()) {
                return 0L;
            }

            String[] jails = jailMatcher.group(1).split(",");
            long total = 0L;
            for (String jail : jails) {
                String jailName = jail.trim();
                if (jailName.isEmpty()) {
                    continue;
                }
                String jailStatus = executeCommand(new String[]{SUDO_CMD, FAIL2BAN_CLIENT, "status", jailName});
                if (jailStatus == null) {
                    continue;
                }
                Matcher failedMatcher = TOTAL_FAILED_PATTERN.matcher(jailStatus);
                if (failedMatcher.find()) {
                    total += Long.parseLong(failedMatcher.group(1));
                }
            }
            return total;
        } catch (Exception e) {
            log.warn(LOG_PREFIX + "采集累计拦截次数异常，降级为 0：{}", e.getMessage());
            return 0L;
        }
    }

    // ==================== 进程执行工具（数组命令防注入） ====================

    /**
     * 执行系统命令并返回标准输出（合并错误流），失败或超时返回 null
     */
    private String executeCommand(String[] command) {
        Process process = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            process = pb.start();

            Process finalProcess = process;
            Future<String> outputFuture = STREAM_EXECUTOR.submit(() -> readStream(finalProcess.getInputStream()));

            boolean finished = process.waitFor(COMMAND_TIMEOUT, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.debug(LOG_PREFIX + "命令执行超时：{}", String.join(" ", command));
                return null;
            }

            String output = outputFuture.get();
            int exitCode = process.exitValue();
            // 退出码 0 返回输出，非 0（如 is-active 对 inactive 返回 3）降级为 null
            return exitCode == 0 ? output : null;
        } catch (Exception e) {
            log.debug(LOG_PREFIX + "执行命令异常：{}，异常：{}", String.join(" ", command), e.getMessage());
            return null;
        } finally {
            destroyProcess(process);
        }
    }

    /**
     * 读取输入流为字符串
     */
    private String readStream(InputStream inputStream) throws Exception {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * 安全销毁进程，防止僵尸进程
     */
    private void destroyProcess(Process process) {
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
        }
    }
}
