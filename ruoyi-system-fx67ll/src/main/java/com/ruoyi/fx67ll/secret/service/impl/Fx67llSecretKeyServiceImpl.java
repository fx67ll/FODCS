package com.ruoyi.fx67ll.secret.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.secret.mapper.Fx67llSecretKeyMapper;
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;
import com.ruoyi.fx67ll.secret.service.IFx67llSecretKeyService;

/**
 * 秘钥配置Service业务层处理
 *
 * @author fx67ll
 * @date 2023-08-30
 */
@Service
public class Fx67llSecretKeyServiceImpl implements IFx67llSecretKeyService {
    @Autowired
    private Fx67llSecretKeyMapper fx67llSecretKeyMapper;

    /**
     * 查询秘钥配置
     *
     * @param secretId 秘钥配置主键
     * @return 秘钥配置
     */
    @Override
    public Fx67llSecretKey selectFx67llSecretKeyBySecretId(Long secretId) {
        return fx67llSecretKeyMapper.selectFx67llSecretKeyBySecretId(secretId);
    }

    /**
     * 查询秘钥配置列表
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置
     */
    @Override
    public List<Fx67llSecretKey> selectFx67llSecretKeyList(Fx67llSecretKey fx67llSecretKey) {
        return fx67llSecretKeyMapper.selectFx67llSecretKeyList(fx67llSecretKey);
    }

    /**
     * 查询秘钥配置列表
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 秘钥配置
     */
    @Override
    public List<Fx67llSecretKey> selectFx67llSecretKeyListForApp(Fx67llSecretKey fx67llSecretKey) {
        // 禁止获取cryptoSaltKey的值
        if (fx67llSecretKey.getSecretKey() == null || fx67llSecretKey.getSecretKey().equals("cryptoSaltKey")) {
            return fx67llSecretKeyMapper.selectFx67llSecretKeyListForNull();
        } else {
            return fx67llSecretKeyMapper.selectFx67llSecretKeyList(fx67llSecretKey);
        }
    }

    /**
     * 新增秘钥配置
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    @Override
    public int insertFx67llSecretKey(Fx67llSecretKey fx67llSecretKey) {
        return fx67llSecretKeyMapper.insertFx67llSecretKey(fx67llSecretKey);
    }

    /**
     * 修改秘钥配置
     *
     * @param fx67llSecretKey 秘钥配置
     * @return 结果
     */
    @Override
    public int updateFx67llSecretKey(Fx67llSecretKey fx67llSecretKey) {
        return fx67llSecretKeyMapper.updateFx67llSecretKey(fx67llSecretKey);
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
}
