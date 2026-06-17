package com.ruoyi.web.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Fail2ban配置类
 * 从/home/ruoyi/.env文件加载IP白名单等配置
 * 配置与代码分离，修改无需重新打包
 */
@Component
public class Fail2BanConfig {
    private static final Logger log = LoggerFactory.getLogger(Fail2BanConfig.class);

    // 配置文件路径
    private static final String CONFIG_FILE_PATH = "/home/ruoyi/.env";

    // 配置项名称
    private static final String ALLOWED_IPS_KEY = "FAIL2BAN_ALLOWED_IPS";

    // 允许执行危险操作的IP白名单
    private List<String> allowedIps = new ArrayList<>();

    /**
     * 服务启动时自动加载配置
     */
    @PostConstruct
    public void loadConfig() {
        File configFile = new File(CONFIG_FILE_PATH);

        // 如果配置文件不存在，使用空白名单（默认禁止所有操作）
        if (!configFile.exists()) {
            log.warn("Fail2ban配置文件不存在：{}，将禁止所有封禁/解封操作", CONFIG_FILE_PATH);
            return;
        }

        // 检查文件权限并读取配置
        if (configFile.canRead()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // 跳过注释和空行
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }

                    // 解析键值对
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2 && ALLOWED_IPS_KEY.equals(parts[0].trim())) {
                        String ipListStr = parts[1].trim();
                        if (!ipListStr.isEmpty()) {
                            // 分割IP列表，去除前后空格
                            String[] ips = ipListStr.split(",");
                            for (String ip : ips) {
                                String trimmedIp = ip.trim();
                                if (!trimmedIp.isEmpty()) {
                                    allowedIps.add(trimmedIp);
                                }
                            }
                        }
                        log.info("成功加载Fail2ban IP白名单：{}", allowedIps);
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("读取Fail2ban配置文件失败", e);
            }
        } else {
            log.error("没有权限读取Fail2ban配置文件：{}", CONFIG_FILE_PATH);
        }
    }

    /**
     * 获取允许执行危险操作的IP白名单
     */
    public List<String> getAllowedIps() {
        return allowedIps;
    }

    /**
     * 检查IP是否在白名单中
     */
    public boolean isIpAllowed(String ip) {
        return allowedIps.contains(ip);
    }
}