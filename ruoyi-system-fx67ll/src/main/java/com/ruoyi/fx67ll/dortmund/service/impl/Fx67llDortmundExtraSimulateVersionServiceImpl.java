package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraSimulateVersionMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateVersionService;

/**
 * 外快盈亏数值模拟版本记录Service业务层处理
 *
 * @author fx67ll
 * @date 2026-09-11
 */
@Service
public class Fx67llDortmundExtraSimulateVersionServiceImpl implements IFx67llDortmundExtraSimulateVersionService {
    @Autowired
    private Fx67llDortmundExtraSimulateVersionMapper fx67llDortmundExtraSimulateVersionMapper;

    /**
     * 查询外快盈亏数值模拟版本记录列表
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionList(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionList(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 查询外快盈亏数值模拟版本记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionListByUserId(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        fx67llDortmundExtraSimulateVersion.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionList(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 新增外快盈亏数值模拟版本记录（版本序号按用户自动递增）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        Long maxVersionNo = fx67llDortmundExtraSimulateVersionMapper.selectMaxVersionNoByUserId(SecurityUtils.getUserId());
        fx67llDortmundExtraSimulateVersion.setVersionNo(maxVersionNo == null ? 1L : maxVersionNo + 1);
        fx67llDortmundExtraSimulateVersion.setUserId(SecurityUtils.getUserId());
        fx67llDortmundExtraSimulateVersion.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulateVersion.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraSimulateVersionMapper.insertFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 修改外快盈亏数值模拟版本记录（用于版本启用停用）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        fx67llDortmundExtraSimulateVersion.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulateVersion.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraSimulateVersionMapper.updateFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 根据版本ID查询外快盈亏数值模拟版本记录
     *
     * @param versionId 版本ID
     * @return 外快盈亏数值模拟版本记录
     */
    @Override
    public Fx67llDortmundExtraSimulateVersion selectFx67llDortmundExtraSimulateVersionById(Long versionId) {
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionById(versionId);
    }
}
