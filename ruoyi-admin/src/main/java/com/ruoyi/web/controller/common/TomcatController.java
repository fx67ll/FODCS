package com.ruoyi.web.controller.common;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.*;

/**
 * Tomcat操作控制器（适配若依框架）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/server/tomcat")
public class TomcatController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TomcatController.class);

    // Tomcat安装目录（建议配置在application.yml中，通过@Value注入）
    private static final String TOMCAT_BIN_PATH = "/usr/soft/install/apache-tomcat-9.0.7/bin";
    // 命令执行超时时间（秒）
    private static final int COMMAND_TIMEOUT = 30;
    // Tomcat进程关键字，用于查询状态
    private static final String TOMCAT_PROCESS_KEYWORD = "apache-tomcat-9.0.7";
    // 线程池用于异步读取流（避免阻塞）
    private static final ExecutorService STREAM_EXECUTOR = Executors.newFixedThreadPool(2);

    /**
     * 启动Tomcat
     */
    @PreAuthorize("@ss.hasPermi('system:tomcat:operate')")
    @Log(title = "Tomcat操作", businessType = BusinessType.OTHER)
    @PostMapping("/start")
    public AjaxResult startTomcat() {
        // 启动前检查状态
        AjaxResult statusResult = getTomcatStatus();
        if (statusResult.get("data").equals("运行中")) {
            return AjaxResult.warn("Tomcat已处于运行状态，无需重复启动");
        }
        return executeCommand("startup.sh", "Tomcat启动");
    }

    /**
     * 停止Tomcat
     */
    @PreAuthorize("@ss.hasPermi('system:tomcat:operate')")
    @Log(title = "Tomcat操作", businessType = BusinessType.OTHER)
    @PostMapping("/stop")
    public AjaxResult stopTomcat() {
        return executeCommand("shutdown.sh", "Tomcat停止");
    }

    /**
     * 查询Tomcat状态
     */
    @PreAuthorize("@ss.hasPermi('system:tomcat:view')")
    @GetMapping("/status")
    public AjaxResult getTomcatStatus() {
        Process process = null;
        BufferedReader reader = null;
        try {
            // 执行ps命令查找Tomcat进程
            ProcessBuilder pb = new ProcessBuilder("ps", "aux");
            pb.redirectErrorStream(true);
            process = pb.start();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line;
            boolean isRunning = false;

            // 查找包含Tomcat关键字的进程
            while ((line = reader.readLine()) != null) {
                if (line.contains(TOMCAT_PROCESS_KEYWORD) && !line.contains("grep")) {
                    isRunning = true;
                    break;
                }
            }

            // 等待命令完成
            process.waitFor(5, TimeUnit.SECONDS);

            String status = isRunning ? "运行中" : "已停止";
            log.info("Tomcat当前状态：{}", status);
            return AjaxResult.success("查询Tomcat状态成功", status);
        } catch (Exception e) {
            log.error("查询Tomcat状态失败：", e);
            return AjaxResult.error("查询Tomcat状态失败：" + e.getMessage());
        } finally {
            closeReader(reader);
            destroyProcess(process);
        }
    }

    /**
     * 执行Shell命令通用方法（优化版）
     * 解决流阻塞、权限检查、资源释放问题
     */
    private AjaxResult executeCommand(String scriptName, String operationDesc) {
        // 1. 检查Tomcat bin目录是否存在
        File tomcatBinDir = new File(TOMCAT_BIN_PATH);
        if (!tomcatBinDir.exists() || !tomcatBinDir.isDirectory()) {
            String errorMsg = operationDesc + "失败：Tomcat bin目录不存在或不是目录，路径：" + TOMCAT_BIN_PATH;
            log.error(errorMsg);
            return AjaxResult.error(errorMsg);
        }

        // 2. 检查脚本文件是否存在且可读
        File scriptFile = new File(tomcatBinDir, scriptName);
        if (!scriptFile.exists() || !scriptFile.isFile()) {
            String errorMsg = operationDesc + "失败：脚本文件不存在，路径：" + scriptFile.getAbsolutePath();
            log.error(errorMsg);
            return AjaxResult.error(errorMsg);
        }
        if (!Files.isReadable(Paths.get(scriptFile.getAbsolutePath()))) {
            String errorMsg = operationDesc + "失败：没有脚本读取权限，路径：" + scriptFile.getAbsolutePath();
            log.error(errorMsg);
            return AjaxResult.error(errorMsg);
        }

        Process process = null;
        try {
            // 3. 构建命令：用sh执行脚本（避免依赖脚本的可执行权限）
            ProcessBuilder pb = new ProcessBuilder("sh", scriptName);
            pb.directory(tomcatBinDir); // 设置工作目录为tomcat bin目录
            pb.redirectErrorStream(true); // 合并错误流到输入流，统一处理

            // 4. 启动进程
            log.info("开始{}，执行脚本：{}", operationDesc, scriptFile.getAbsolutePath());
            process = pb.start();

            // 5. 异步读取输出流（关键：避免缓冲区满导致进程阻塞）
            Process finalProcess = process;
            Future<String> outputFuture = STREAM_EXECUTOR.submit(() -> readStream(finalProcess.getInputStream()));

            // 6. 等待命令执行完成（带超时）
            boolean isFinished = process.waitFor(COMMAND_TIMEOUT, TimeUnit.SECONDS);
            if (!isFinished) {
                process.destroyForcibly(); // 超时强制销毁进程
                String errorMsg = operationDesc + "超时（" + COMMAND_TIMEOUT + "秒），已强制终止";
                log.error(errorMsg);
                return AjaxResult.error(errorMsg);
            }

            // 7. 获取脚本输出和退出码
            String output = outputFuture.get(); // 获取异步读取的输出
            int exitCode = process.exitValue();

            // 8. 根据退出码判断结果
            if (exitCode == 0) {
                log.info("{}成功，输出：{}", operationDesc, output);
                return AjaxResult.success(operationDesc + "成功", output);
            } else {
                log.error("{}失败，退出码：{}，输出：{}", operationDesc, exitCode, output);
                return AjaxResult.error(operationDesc + "失败（退出码：" + exitCode + "），详情：" + output);
            }

        } catch (IOException e) {
            String errorMsg = operationDesc + "失败：IO异常";
            log.error(errorMsg, e);
            return AjaxResult.error(errorMsg + "，原因：" + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            String errorMsg = operationDesc + "被中断";
            log.error(errorMsg, e);
            return AjaxResult.error(errorMsg);
        } catch (ExecutionException e) {
            String errorMsg = operationDesc + "失败：流读取异常";
            log.error(errorMsg, e);
            return AjaxResult.error(errorMsg + "，原因：" + e.getMessage());
        } finally {
            // 确保进程销毁和资源释放
            destroyProcess(process);
        }
    }

    /**
     * 读取输入流内容（工具方法）
     */
    private String readStream(InputStream inputStream) throws IOException {
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
     * 安全关闭BufferedReader（工具方法）
     */
    private void closeReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                log.error("关闭流失败", e);
            }
        }
    }

    /**
     * 安全销毁进程（工具方法）
     */
    private void destroyProcess(Process process) {
        if (process != null && process.isAlive()) {
            process.destroyForcibly(); // 强制销毁避免残留
            log.info("进程已强制销毁");
        }
    }
}