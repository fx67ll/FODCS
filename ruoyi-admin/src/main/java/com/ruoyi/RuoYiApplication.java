package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// 开启 Spring 定时任务支持，用于公开服务状态大盘的脱敏采集任务（@Scheduled，10min 一次，不进 Quartz，零 DB 开销）
@EnableScheduling
public class RuoYiApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("fx67ll-One-Data-Center-Server start successfully~  d=====(￣▽￣*)b 顶  \n" +
                "    ___  __  __     __     ____    _       _     \n" +
                "   | __| \\ \\/ /    / /    |__  |  | |     | |    \n" +
                "   | _|   >  <    / _ \\     / /   | |__   | |__  \n" +
                "  _|_|_  /_/\\_\\   \\___/   _/_/_   |____|  |____| \n" +
                "_| \"\"\" |_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"|_|\"\"\"\"\"| \n" +
                "\"`-0-4-'\"`-0-4-'\"`-0-0-'\"`-0-0-'\"`-1-0-'\"`-2-3-' \n");
    }
}
