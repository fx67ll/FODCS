package com.ruoyi.fx67ll.secret.service.impl;

import java.util.List;

import com.ruoyi.common.config.SecretKeyConfig;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.secret.mapper.Fx67llSecretKeyMapper;
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;
import com.ruoyi.fx67ll.secret.service.IFx67llSecretKeyService;

/**
 * 秘钥配置Service业务层处理
 *
 * 加解密职责下沉到后端：insert/update 时用 dbEncKey 加密 secret_value 入库，
 * 查询时解密返回明文（仅服务端解密，不下发密钥给前端）。
 * cryptoSaltKey 那条存的是盐值明文而非密文，加密/解密均跳过。
 *
 * @author fx67ll
 * @date 2023-08-30
 */
@Service
public class Fx67llSecretKeyServiceImpl implements IFx67llSecretKeyService {
    /**
     * 旧版前端盐值键名，迁移期间保留，其值是盐值明文不参与加解密
     */
    private static final String CRYPTO_SALT_KEY = "cryptoSaltKey";

    @Autowired
    private Fx67llSecretKeyMapper fx67llSecretKeyMapper;

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    /**
     * 查询秘钥配置
     *
     * @param secretId 秘钥配置主键
     * @return 秘钥配置（secret_value 已解密为明文）
     */
    @Override
    public Fx67llSecretKey selectFx67llSecretKeyBySecretId(Long secretId) {
        Fx67llSecretKey secretKey = fx67llSecretKeyMapper.selectFx67llSecretKeyBySecretId(secretId);
        decryptValue(secretKey);
        return secretKey;
    }

    /**
     * 查询秘钥配置列表
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置集合（secret_value 已解密为明文）
     */
    @Override
    public List<Fx67llSecretKey> selectFx67llSecretKeyList(Fx67llSecretKey fx67llSecretKey) {
        List<Fx67llSecretKey> list = fx67llSecretKeyMapper.selectFx67llSecretKeyList(fx67llSecretKey);
        decryptValues(list);
        return list;
    }

    /**
     * 查询秘钥配置列表（管理端用，secret_value 直接返回数据库密文，不下发明文）
     *
     * 列表只展示密文串（无意义 Base64），防抓包批量泄露明文。
     * 明文仅在点击修改/查看时通过 transferKey 解密流程获取。
     * cryptoSaltKey 保留明文（吉祥物密钥，不删）。
     */
    @Override
    public List<Fx67llSecretKey> selectFx67llSecretKeyListMasked(Fx67llSecretKey fx67llSecretKey) {
        List<Fx67llSecretKey> list = fx67llSecretKeyMapper.selectFx67llSecretKeyList(fx67llSecretKey);
        if (list == null || list.isEmpty()) {
            return list;
        }
        for (Fx67llSecretKey secretKey : list) {
            maskValue(secretKey);
        }
        return list;
    }

    /**
     * 提供给 APP 查询秘钥配置列表
     *
     * 注：此方法返回的 secret_value 解密与否由上层 Controller（getSecretKeyConfigForApp）
     * 的鉴权分级逻辑控制，这里仅做原始查询。敏感键的脱敏/拦截在 Controller 层处理。
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置集合
     */
    @Override
    public List<Fx67llSecretKey> selectFx67llSecretKeyListForApp(Fx67llSecretKey fx67llSecretKey) {
        // 禁止获取cryptoSaltKey的值
        if (fx67llSecretKey.getSecretKey() == null || fx67llSecretKey.getSecretKey().equals(CRYPTO_SALT_KEY)) {
            return fx67llSecretKeyMapper.selectFx67llSecretKeyListForNull();
        } else {
            return fx67llSecretKeyMapper.selectFx67llSecretKeyList(fx67llSecretKey);
        }
    }

    /**
     * 按 secret_key 精确匹配查询单条配置（APP 接口专用），secret_value 已解密为明文。
     * cryptoSaltKey 一律返回 null（盐值不下发前端）。
     *
     * @param secretKey 秘钥键
     * @return 秘钥配置（含明文 secret_value），不存在或为 cryptoSaltKey 返回 null
     */
    @Override
    public Fx67llSecretKey selectFx67llSecretKeyBySecretKeyForApp(String secretKey) {
        if (secretKey == null || secretKey.isEmpty() || CRYPTO_SALT_KEY.equals(secretKey)) {
            return null;
        }
        Fx67llSecretKey secretKeyRecord = fx67llSecretKeyMapper.selectFx67llSecretKeyBySecretKey(secretKey);
        decryptValue(secretKeyRecord);
        return secretKeyRecord;
    }

    /**
     * 新增秘钥配置
     *
     * secret_value 用 dbEncKey 加密后入库；cryptoSaltKey 跳过加密（盐值明文）。
     * 应用层校验 secret_key 唯一性（varchar(1023) 无法加唯一索引，改为查询查重）。
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    @Override
    public int insertFx67llSecretKey(Fx67llSecretKey fx67llSecretKey) {
        checkSecretKeyUnique(fx67llSecretKey);
        encryptValue(fx67llSecretKey);
        return fx67llSecretKeyMapper.insertFx67llSecretKey(fx67llSecretKey);
    }

    /**
     * 修改秘钥配置
     *
     * secret_value 用 dbEncKey 加密后更新；cryptoSaltKey 跳过加密（盐值明文）。
     * 应用层校验 secret_key 唯一性（排除自身）。
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    @Override
    public int updateFx67llSecretKey(Fx67llSecretKey fx67llSecretKey) {
        checkSecretKeyUnique(fx67llSecretKey);
        encryptValue(fx67llSecretKey);
        return fx67llSecretKeyMapper.updateFx67llSecretKey(fx67llSecretKey);
    }

    /**
     * 应用层校验 secret_key 唯一性（阶段五·4.9）
     *
     * varchar(1023) 受 MySQL 索引长度限制无法加唯一索引，改为查询查重。
     * 新增时校验全局唯一；修改时校验排除自身（secret_id 不同但 secret_key 相同则冲突）。
     *
     * @param fx67llSecretKey 秘钥配置
     */
    private void checkSecretKeyUnique(Fx67llSecretKey fx67llSecretKey) {
        if (fx67llSecretKey == null || fx67llSecretKey.getSecretKey() == null
                || fx67llSecretKey.getSecretKey().isEmpty()) {
            return;
        }
        Fx67llSecretKey existing = fx67llSecretKeyMapper.selectFx67llSecretKeyBySecretKey(fx67llSecretKey.getSecretKey());
        if (existing != null) {
            // 修改时排除自身（secret_id 相同说明是改自己的其他字段，不算冲突）
            if (fx67llSecretKey.getSecretId() != null
                    && fx67llSecretKey.getSecretId().equals(existing.getSecretId())) {
                return;
            }
            throw new ServiceException("秘钥键'" + fx67llSecretKey.getSecretKey() + "'已存在，不允许重复");
        }
    }

    /**
     * 批量删除秘钥配置
     *
     * @param secretIds 需要删除的秘钥配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llSecretKeyBySecretIds(Long[] secretIds) {
        return fx67llSecretKeyMapper.deleteFx67llSecretKeyBySecretIds(secretIds);
    }

    /**
     * 删除秘钥配置信息
     *
     * @param secretId 秘钥配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llSecretKeyBySecretId(Long secretId) {
        return fx67llSecretKeyMapper.deleteFx67llSecretKeyBySecretId(secretId);
    }

    /**
     * 加密单条记录的 secret_value（cryptoSaltKey 跳过）
     */
    private void encryptValue(Fx67llSecretKey secretKey) {
        if (secretKey == null || secretKey.getSecretValue() == null) {
            return;
        }
        if (CRYPTO_SALT_KEY.equals(secretKey.getSecretKey())) {
            return;
        }
        secretKey.setSecretValue(CryptoUtils.encrypt(secretKey.getSecretValue(), getDbEncKey()));
    }

    /**
     * 解密单条记录的 secret_value（cryptoSaltKey 跳过；空记录跳过）
     */
    private void decryptValue(Fx67llSecretKey secretKey) {
        if (secretKey == null || secretKey.getSecretValue() == null) {
            return;
        }
        if (CRYPTO_SALT_KEY.equals(secretKey.getSecretKey())) {
            return;
        }
        secretKey.setSecretValue(CryptoUtils.decrypt(secretKey.getSecretValue(), getDbEncKey()));
    }

    /**
     * 解密列表中每条记录的 secret_value
     */
    private void decryptValues(List<Fx67llSecretKey> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (Fx67llSecretKey secretKey : list) {
            decryptValue(secretKey);
        }
    }

    /**
     * 列表展示用：secret_value 直接返回数据库密文（不解密、不脱敏），cryptoSaltKey 跳过保留明文。
     *
     * 列表只展示密文串（无意义的 Base64），不下发明文，也不泄露末尾几位。
     * 明文仅在点击修改/查看时通过 transferKey 解密流程获取。
     */
    private void maskValue(Fx67llSecretKey secretKey) {
        if (secretKey == null || secretKey.getSecretValue() == null) {
            return;
        }
        // cryptoSaltKey 保留明文（迁移期间可见，阶段五删除该记录）
        // 其他键：直接返回数据库密文，不解密、不脱敏
    }

    /**
     * 获取数据库加密密钥（DB_ENC_KEY）
     */
    private String getDbEncKey() {
        return secretKeyConfig.getSecret().getDbEncKey();
    }
}

