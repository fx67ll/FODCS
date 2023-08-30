package com.ruoyi.fx67ll.secret.mapper;

import java.util.List;

import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;

/**
 * 秘钥配置Mapper接口
 *
 * @author fx67ll
 * @date 2023-08-30
 */
public interface Fx67llSecretKeyMapper {
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
     * 提供给 APP 查询秘钥配置列表
     *
     * @return 秘钥配置集合
     */
    public List<Fx67llSecretKey> selectFx67llSecretKeyListForNull();

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
     * 删除秘钥配置
     *
     * @param secretId 秘钥配置主键
     * @return 结果
     */
    public int deleteFx67llSecretKeyBySecretId(Long secretId);

    /**
     * 批量删除秘钥配置
     *
     * @param secretIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llSecretKeyBySecretIds(Long[] secretIds);

}
