package com.ruoyi.web.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fail2ban配置类
 * 从/home/ruoyi/.env文件加载IP白名单等配置
 * 配置与代码分离，修改无需重新打包
 * 支持两种白名单格式：单个独立IP、CIDR网段（如 114.222.252.0/24）
 */
@Component
public class Fail2BanConfig {
    private static final Logger log = LoggerFactory.getLogger(Fail2BanConfig.class);

    /** 日志统一前缀，便于在日志文件中快速识别Fail2Ban功能相关日志 */
    private static final String LOG_PREFIX = "[Fail2Ban] ";

    // 配置文件路径
    private static final String CONFIG_FILE_PATH = "/home/ruoyi/.env";

    // 配置项名称
    private static final String ALLOWED_IPS_KEY = "FAIL2BAN_ALLOWED_IPS";

    // 允许执行危险操作的IP/网段白名单集合（支持纯IP、CIDR网段字符串混合存储）
    private List<String> allowedIps = new ArrayList<>();

    /**
     * 服务启动时自动加载配置
     * 读取.env配置文件，解析FAIL2BAN_ALLOWED_IPS字段，拆分并缓存所有白名单IP/网段
     */
    @PostConstruct
    public void loadConfig() {
        File configFile = new File(CONFIG_FILE_PATH);

        // 如果配置文件不存在，使用空白名单（默认禁止所有操作）
        if (!configFile.exists()) {
            log.warn(LOG_PREFIX + "配置文件不存在：{}，将禁止所有封禁/解封操作", CONFIG_FILE_PATH);
            // 清空旧缓存，防止旧配置残留
            allowedIps.clear();
            return;
        }

        // 检查文件权限并读取配置
        if (configFile.canRead()) {
            // 先清空历史白名单缓存
            allowedIps.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // 跳过注释行(#开头)与空行
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }

                    // 解析键值对，仅分割第一个等号，避免值内包含=符号报错
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2 && ALLOWED_IPS_KEY.equals(parts[0].trim())) {
                        String ipListStr = parts[1].trim();
                        if (!ipListStr.isEmpty()) {
                            // 按逗号分割多IP/网段
                            String[] ips = ipListStr.split(",");
                            for (String ip : ips) {
                                String trimmedIp = ip.trim();
                                if (!trimmedIp.isEmpty()) {
                                    allowedIps.add(trimmedIp);
                                    // 启动时预校验IP/CIDR格式，非法网段打印警告日志
                                    validateIpOrCidrFormat(trimmedIp);
                                }
                            }
                        }
                        log.info(LOG_PREFIX + "成功加载IP白名单（支持CIDR网段）：{}", allowedIps);
                        break;
                    }
                }
            } catch (Exception e) {
                log.error(LOG_PREFIX + "读取配置文件异常", e);
                allowedIps.clear();
            }
        } else {
            log.error(LOG_PREFIX + "没有读取权限，配置文件：{}", CONFIG_FILE_PATH);
            allowedIps.clear();
        }
    }

    /**
     * 获取允许执行危险操作的IP/网段白名单原始字符串集合
     *
     * @return 白名单列表，包含纯IP或CIDR网段
     */
    public List<String> getAllowedIps() {
        return allowedIps;
    }

    /**
     * 检查请求IP是否在白名单内
     * 兼容两种规则：1.完整IP精确匹配  2.CIDR网段子网匹配（如114.222.252.0/24）
     *
     * @param ip 客户端来访公网IP
     * @return true=放行，false=禁止操作
     */
    public boolean isIpAllowed(String ip) {
        // 空IP或白名单为空直接拦截
        if (ip == null || ip.trim().length() == 0 || allowedIps.isEmpty()) {
            return false;
        }
        // 遍历所有白名单规则，任意一条匹配即放行
        for (String rule : allowedIps) {
            if (isIpMatchRule(ip, rule)) {
                return true;
            }
        }
        return false;
    }

    // ====================== 内部工具私有方法 ======================

    /**
     * 校验单条白名单IP/CIDR格式合法性，非法格式输出警告日志
     *
     * @param item 单条IP或CIDR网段字符串
     */
    private void validateIpOrCidrFormat(String item) {
        try {
            if (item.contains("/")) {
                // CIDR网段格式：ip/掩码
                String[] cidrArr = item.split("/");
                if (cidrArr.length != 2) {
                    log.warn(LOG_PREFIX + "白名单网段格式非法，不符合ip/mask规范：{}", item);
                    return;
                }
                int mask = Integer.parseInt(cidrArr[1]);
                if (mask < 0 || mask > 32) {
                    log.warn(LOG_PREFIX + "白名单网段掩码超出0-32范围：{}", item);
                    return;
                }
                // 校验IP部分是否合法IPv4
                ipToBigInt(cidrArr[0]);
            } else {
                // 普通独立IP，直接校验
                ipToBigInt(item);
            }
        } catch (NumberFormatException e) {
            log.warn(LOG_PREFIX + "白名单网段掩码不是数字：{}", item);
        } catch (UnknownHostException e) {
            log.warn(LOG_PREFIX + "白名单包含非法IP地址：{}", item);
        } catch (Exception e) {
            log.warn(LOG_PREFIX + "白名单规则解析异常 {}：{}", item, e.getMessage());
        }
    }

    /**
     * 判断单个IP是否匹配单条白名单规则（纯IP精确匹配 / CIDR网段包含匹配）
     *
     * @param targetIp 客户端真实IP
     * @param rule     白名单规则（纯IP / CIDR网段）
     * @return 是否匹配
     */
    private boolean isIpMatchRule(String targetIp, String rule) {
        try {
            // 规则不带/，走精确字符串匹配
            if (!rule.contains("/")) {
                return targetIp.equals(rule);
            }
            // CIDR网段逻辑
            String[] cidrParts = rule.split("/");
            String netIp = cidrParts[0];
            int prefix = Integer.parseInt(cidrParts[1]);

            BigInteger targetNum = ipToBigInt(targetIp);
            BigInteger netNum = ipToBigInt(netIp);
            // 32位IPv4掩码
            BigInteger fullMask = BigInteger.valueOf(0xFFFFFFFFL);
            BigInteger mask = fullMask.shiftLeft(32 - prefix);

            BigInteger targetSubnet = targetNum.and(mask);
            BigInteger ruleSubnet = netNum.and(mask);
            return targetSubnet.equals(ruleSubnet);
        } catch (Exception e) {
            // 解析异常降级为精确匹配，防止直接阻断所有请求
            log.debug(LOG_PREFIX + "网段匹配解析失败，降级精确比对 ip={}, rule={}", targetIp, rule, e);
            return targetIp.equals(rule);
        }
    }

    /**
     * IPv4字符串转无符号32位大整数，用于子网掩码位运算判断
     *
     * @param ipStr IPv4地址字符串，例：114.222.252.50
     * @return 对应数字
     * @throws UnknownHostException IP格式不合法抛出
     */
    private BigInteger ipToBigInt(String ipStr) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipStr);
        byte[] bytes = address.getAddress();
        BigInteger num = BigInteger.ZERO;
        for (byte b : bytes) {
            num = num.shiftLeft(8).or(BigInteger.valueOf(b & 0xFF));
        }
        return num;
    }
}