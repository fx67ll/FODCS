package com.ruoyi.fx67ll.secret.service;

import java.util.List;

import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;

/**
 * 秘钥配置Service接口
 *
 * @author fx67ll
 * @date 2023-08-30
 */
public interface IFx67llSecretKeyService {
    /**
     * 查询秘钥配置
     *
     * @param secretId 秘钥配置主键
     * @return 秘钥配置
     */
    public Fx67llSecretKey selectFx67llSecretKeyBySecretId(Long secretId);

    /**
     * 查询秘钥配置列表
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置集合
     */
    public List<Fx67llSecretKey> selectFx67llSecretKeyList(Fx67llSecretKey fx67llSecretKey);

    /**
     * 查询秘钥配置列表（管理端用，secret_value 脱敏，不返回明文/密文）
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置集合（secret_value 为脱敏值 ***末4位）
     */
    public List<Fx67llSecretKey> selectFx67llSecretKeyListMasked(Fx67llSecretKey fx67llSecretKey);

    /**
     * 提供给 APP 查询秘钥配置列表
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置集合
     */
    public List<Fx67llSecretKey> selectFx67llSecretKeyListForApp(Fx67llSecretKey fx67llSecretKey);

    /**
     * 按 secret_key 精确匹配查询单条配置（APP 接口专用），secret_value 已解密为明文。
     * cryptoSaltKey 一律返回 null（盐值不下发）。
     *
     * @param secretKey 秘钥键
     * @return 秘钥配置（含明文 secret_value），不存在返回 null
     */
    public Fx67llSecretKey selectFx67llSecretKeyBySecretKeyForApp(String secretKey);

    /**
     * 新增秘钥配置
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    public int insertFx67llSecretKey(Fx67llSecretKey fx67llSecretKey);

    /**
     * 修改秘钥配置
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    public int updateFx67llSecretKey(Fx67llSecretKey fx67llSecretKey);

    /**
     * 批量删除秘钥配置
     *
     * @param secretIds 需要删除的秘钥配置主键集合
     * @return 结果
     */
    public int deleteFx67llSecretKeyBySecretIds(Long[] secretIds);

    /**
     * 删除秘钥配置信息
     *
     * @param secretId 秘钥配置主键
     * @return 结果
     */
    public int deleteFx67llSecretKeyBySecretId(Long secretId);
}
