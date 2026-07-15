package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端密钥相关配置
 *
 * 绑定 application.yml 中 app.secret 与 wechat / member 段配置。
 * 这些值仅服务端持有，永不下发前端。
 *
 * @author fx67ll
 */
@Component
@ConfigurationProperties(prefix = "app")
public class SecretKeyConfig
{
    /** 加密相关密钥（dbEncKey 加密数据库 secret_value，appSecretKey 加密下发给前端的凭据） */
    private Secret secret = new Secret();

    /** 微信小程序配置（主账号一键登录调 jscode2session 用） */
    private Wechat wechat = new Wechat();

    /** 主账号身份白名单（openid 列表） */
    private Member member = new Member();

    public Secret getSecret()
    {
        return secret;
    }

    public void setSecret(Secret secret)
    {
        this.secret = secret;
    }

    public Wechat getWechat()
    {
        return wechat;
    }

    public void setWechat(Wechat wechat)
    {
        this.wechat = wechat;
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

    /** 加密密钥配置 */
    public static class Secret
    {
        /** 加密数据库中存储的 secret_value（DB_ENC_KEY，Base64 编码 256 位） */
        private String dbEncKey;

        /** 加密下发给前端的第三方凭据（APP_SECRET_KEY，Base64 编码 256 位） */
        private String appSecretKey;

        public String getDbEncKey()
        {
            return dbEncKey;
        }

        public void setDbEncKey(String dbEncKey)
        {
            this.dbEncKey = dbEncKey;
        }

        public String getAppSecretKey()
        {
            return appSecretKey;
        }

        public void setAppSecretKey(String appSecretKey)
        {
            this.appSecretKey = appSecretKey;
        }
    }

    /** 微信小程序配置 */
    public static class Wechat
    {
        private Miniapp miniapp = new Miniapp();

        public Miniapp getMiniapp()
        {
            return miniapp;
        }

        public void setMiniapp(Miniapp miniapp)
        {
            this.miniapp = miniapp;
        }

        public static class Miniapp
        {
            /** 小程序 AppID */
            private String appid;

            /** 小程序 AppSecret（敏感） */
            private String secret;

            public String getAppid()
            {
                return appid;
            }

            public void setAppid(String appid)
            {
                this.appid = appid;
            }

            public String getSecret()
            {
                return secret;
            }

            public void setSecret(String secret)
            {
                this.secret = secret;
            }
        }
    }

    /** 主账号身份白名单 */
    public static class Member
    {
        private Main main = new Main();

        private Guest guest = new Guest();

        public Main getMain()
        {
            return main;
        }

        public void setMain(Main main)
        {
            this.main = main;
        }

        public Guest getGuest()
        {
            return guest;
        }

        public void setGuest(Guest guest)
        {
            this.guest = guest;
        }

        public static class Main
        {
            /** 主账号微信 openid 白名单（数组，换微信号时可临时并存） */
            private List<String> openids = new ArrayList<>();

            /** 主账号登录用户名（服务端持有，不下发前端；一键登录免密签发，不存密码） */
            private String username;

            public List<String> getOpenids()
            {
                return openids;
            }

            public void setOpenids(List<String> openids)
            {
                this.openids = openids;
            }

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }
        }

        /** 游客账号配置（服务端持有，免密签发不存密码） */
        public static class Guest
        {
            /** 游客登录用户名 */
            private String username = "user";

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }
        }
    }
}
