package com.ruoyi.framework.web.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.TransferCryptoUtils;

/**
 * 传输密钥 transferKey 服务（阶段四·4.17，按需签发短时效）
 *
 * 管理端秘钥查看/修改时，前端先调签发接口拿 transferKey（绑定当前用户、5 分钟短时效），
 * 再用 transferKey 加解密传输。后端管理端加解密接口从 Redis 取 transferKey 解密。
 *
 * 不在登录时下发，改为按需签发，更短时效、更收敛。
 *
 * transferKey 仅用于传输加密（AES-CBC），与数据库加密密钥 dbEncKey（AES-GCM）算法隔离、互不混用。
 *
 * @author fx67ll
 */
@Component
public class TransferKeyService
{
    /** Redis key 前缀：传输密钥（按用户 ID 关联） */
    private static final String TRANSFER_KEY_PREFIX = "transfer:key:user:";

    /** transferKey 有效期（分钟），5 分钟 */
    private static final long EXPIRE_MINUTES = 5;

    @Autowired
    private RedisCache redisCache;

    /**
     * 按需签发 transferKey，绑定当前登录用户，存 Redis（5 分钟）
     *
     * @return transferKey（Base64）
     */
    public String issue()
    {
        Long userId = SecurityUtils.getUserId();
        String transferKey = TransferCryptoUtils.generateKey();
        redisCache.setCacheObject(TRANSFER_KEY_PREFIX + userId, transferKey, (int) EXPIRE_MINUTES, TimeUnit.MINUTES);
        return transferKey;
    }

    /**
     * 取当前用户的 transferKey（管理端加解密传输用）
     *
     * @return transferKey，不存在/已过期返回 null
     */
    public String getTransferKey()
    {
        Long userId = SecurityUtils.getUserId();
        return redisCache.getCacheObject(TRANSFER_KEY_PREFIX + userId);
    }
}
