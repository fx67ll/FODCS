package com.ruoyi.framework.web.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * APP 一键登录令牌服务（阶段二·4.4）
 *
 * 一次性登录令牌存 Redis，特点：
 * - 一次性：用后即删，不可重放
 * - 短时效：60 秒过期
 * - 绑定设备指纹：签发时记录指纹，使用时校验匹配，防令牌被拿到别处用
 * - 强随机：32 字节（IdUtils.fastSimpleUUID），防爆破
 *
 * 令牌对应的账密信息（loginType + username + password）也存 Redis，使用时取出走登录，
 * 明文账密不经过前端。
 *
 * @author fx67ll
 */
@Component
public class AppLoginTokenService
{
    /** Redis key 前缀：一键登录令牌 */
    private static final String TOKEN_KEY_PREFIX = "app:login:token:";

    /** 令牌有效期（秒），60 秒 */
    private static final long TOKEN_EXPIRE_SECONDS = 60;

    @Autowired
    private RedisCache redisCache;

    /**
     * 签发一次性登录令牌，绑定设备指纹与账密信息
     *
     * @param loginType  登录类型（main / guest）
     * @param username   账密-用户名（服务端持有，不下发前端）
     * @param password   账密-密码（服务端持有，不下发前端）
     * @param fingerprint 设备指纹
     * @return 一次性登录令牌
     */
    public String issueToken(String loginType, String username, String password, String fingerprint)
    {
        String token = IdUtils.fastSimpleUUID();
        TokenPayload payload = new TokenPayload(loginType, username, password, fingerprint);
        redisCache.setCacheObject(TOKEN_KEY_PREFIX + token, payload,
                (int) TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 校验并消费令牌（一次性，用后即删）
     *
     * @param token       登录令牌
     * @param fingerprint 设备指纹（须与签发时一致）
     * @return 令牌负载（含账密），令牌不存在或指纹不匹配返回 null
     */
    public TokenPayload consumeToken(String token, String fingerprint)
    {
        if (token == null || token.isEmpty())
        {
            return null;
        }
        String key = TOKEN_KEY_PREFIX + token;
        TokenPayload payload = redisCache.getCacheObject(key);
        // 用后即删（一次性）
        redisCache.deleteObject(key);
        if (payload == null)
        {
            return null;
        }
        // 校验设备指纹匹配
        if (fingerprint == null || !fingerprint.equals(payload.getFingerprint()))
        {
            return null;
        }
        return payload;
    }

    /**
     * 令牌负载：登录类型 + 账密 + 设备指纹
     *
     * 注意：需无参构造 + setter，供 fastjson2 反序列化（Redis 存取）。
     */
    public static class TokenPayload
    {
        /** 登录类型 main / guest */
        private String loginType;
        /** 用户名（服务端持有） */
        private String username;
        /** 密码（服务端持有） */
        private String password;
        /** 设备指纹 */
        private String fingerprint;

        public TokenPayload()
        {
        }

        public TokenPayload(String loginType, String username, String password, String fingerprint)
        {
            this.loginType = loginType;
            this.username = username;
            this.password = password;
            this.fingerprint = fingerprint;
        }

        public String getLoginType()
        {
            return loginType;
        }

        public void setLoginType(String loginType)
        {
            this.loginType = loginType;
        }

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public String getFingerprint()
        {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint)
        {
            this.fingerprint = fingerprint;
        }
    }
}
