package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraSimulateMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateVersionService;

/**
 * 外快盈亏数值模拟轮次记录Service业务层处理
 *
 * @author fx67ll
 * @date 2026-09-10
 */
@Service
public class Fx67llDortmundExtraSimulateServiceImpl implements IFx67llDortmundExtraSimulateService {
    @Autowired
    private Fx67llDortmundExtraSimulateMapper fx67llDortmundExtraSimulateMapper;

    @Autowired
    private IFx67llDortmundExtraSimulateVersionService fx67llDortmundExtraSimulateVersionService;

    /**
     * 查询外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    @Override
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulateBySimId(Long simId) {
        return fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateBySimId(simId);
    }

    /**
     * 查询外快盈亏数值模拟轮次记录列表
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录
     */
    @Override
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateList(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        return fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateList(fx67llDortmundExtraSimulate);
    }

    /**
     * 查询外快盈亏数值模拟轮次记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateListByUserId(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        fx67llDortmundExtraSimulate.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateList(fx67llDortmundExtraSimulate);
    }

    /**
     * 新增外快盈亏数值模拟轮次记录（轮次为负时强制作废所属版本）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundExtraSimulate(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        fx67llDortmundExtraSimulate.setUserId(SecurityUtils.getUserId());
        fx67llDortmundExtraSimulate.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulate.setCreateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundExtraSimulateMapper.insertFx67llDortmundExtraSimulate(fx67llDortmundExtraSimulate);
        if (rows > 0 && fx67llDortmundExtraSimulate.getRoundNo() != null && fx67llDortmundExtraSimulate.getRoundNo() < 0) {
            // 轮次为负，强制作废所属版本（软删除，作废后不在列表中展示）
            Fx67llDortmundExtraSimulateVersion invalidateVersion = new Fx67llDortmundExtraSimulateVersion();
            invalidateVersion.setVersionId(fx67llDortmundExtraSimulate.getVersionId());
            fx67llDortmundExtraSimulateVersionService.invalidateFx67llDortmundExtraSimulateVersion(invalidateVersion);
        }
        return rows;
    }

    /**
     * 删除外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundExtraSimulateBySimId(Long simId) {
        return fx67llDortmundExtraSimulateMapper.deleteFx67llDortmundExtraSimulateBySimId(simId);
    }
}
