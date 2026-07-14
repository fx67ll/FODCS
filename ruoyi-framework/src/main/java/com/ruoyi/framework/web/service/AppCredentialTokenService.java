package com.ruoyi.framework.web.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * APP 第三方凭据令牌服务（阶段二·4.5）
 *
 * 一次性凭据令牌存 Redis，特点：
 * - 一次性：用后即删，不可重放
 * - 短时效：5 分钟过期（比登录令牌长，因前端要拿密文换明文再调第三方）
 * - 绑定用户 + 设备指纹：签发时记录，使用时校验，防令牌被拿到别处用
 * - 强随机：32 字节，防爆破
 *
 * 令牌对应的明文凭据（AK/SK）也存 Redis，使用时取出返回前端，用完即删。
 * 密文在前端与令牌分离传输：credentialForApp 返回密文 + 令牌，decryptForApp 用令牌换明文。
 *
 * @author fx67ll
 */
@Component
public class AppCredentialTokenService
{
    /** Redis key 前缀：第三方凭据令牌 */
    private static final String TOKEN_KEY_PREFIX = "app:cred:token:";

    /** 令牌有效期（秒），5 分钟 */
    private static final long TOKEN_EXPIRE_SECONDS = 5 * 60;

    @Autowired
    private RedisCache redisCache;

    /**
     * 签发一次性凭据令牌，绑定用户与设备指纹，存储明文凭据
     *
     * @param credentialType 凭据类型（如 lotteryReward）
     * @param credential     明文凭据（JSON 字符串，含 AK/SK 等）
     * @param userId         签发用户 ID
     * @param fingerprint    设备指纹
     * @return 一次性凭据令牌
     */
    public String issueToken(String credentialType, String credential, Long userId, String fingerprint)
    {
        String token = IdUtils.fastSimpleUUID();
        CredentialPayload payload = new CredentialPayload(credentialType, credential, userId, fingerprint);
        redisCache.setCacheObject(TOKEN_KEY_PREFIX + token, payload,
                (int) TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 校验并消费令牌（一次性，用后即删）
     *
     * @param token       凭据令牌
     * @param userId      当前用户 ID（须与签发时一致）
     * @param fingerprint 设备指纹（须与签发时一致）
     * @return 凭据负载，令牌不存在/用户或指纹不匹配返回 null
     */
    public CredentialPayload consumeToken(String token, Long userId, String fingerprint)
    {
        if (token == null || token.isEmpty())
        {
            return null;
        }
        String key = TOKEN_KEY_PREFIX + token;
        CredentialPayload payload = redisCache.getCacheObject(key);
        // 用后即删（一次性）
        redisCache.deleteObject(key);
        if (payload == null)
        {
            return null;
        }
        // 校验用户与设备指纹匹配
        if (userId == null || !userId.equals(payload.getUserId()))
        {
            return null;
        }
        if (fingerprint == null || !fingerprint.equals(payload.getFingerprint()))
        {
            return null;
        }
        return payload;
    }

    /**
     * 凭据负载：类型 + 明文凭据 + 用户 + 指纹
     *
     * 注意：需无参构造 + setter，供 fastjson2 反序列化（Redis 存取）。
     */
    public static class CredentialPayload
    {
        /** 凭据类型（如 lotteryReward） */
        private String credentialType;
        /** 明文凭据（JSON 字符串） */
        private String credential;
        /** 签发用户 ID */
        private Long userId;
        /** 设备指纹 */
        private String fingerprint;

        public CredentialPayload()
        {
        }

        public CredentialPayload(String credentialType, String credential, Long userId, String fingerprint)
        {
            this.credentialType = credentialType;
            this.credential = credential;
            this.userId = userId;
            this.fingerprint = fingerprint;
        }

        public String getCredentialType()
        {
            return credentialType;
        }

        public void setCredentialType(String credentialType)
        {
            this.credentialType = credentialType;
        }

        public String getCredential()
        {
            return credential;
        }

        public void setCredential(String credential)
        {
            this.credential = credential;
        }

        public Long getUserId()
        {
            return userId;
        }

        public void setUserId(Long userId)
        {
            this.userId = userId;
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
