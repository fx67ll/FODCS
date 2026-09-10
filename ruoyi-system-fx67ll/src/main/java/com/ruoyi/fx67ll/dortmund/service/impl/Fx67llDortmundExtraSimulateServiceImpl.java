package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraSimulateMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateService;

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
     * 新增外快盈亏数值模拟轮次记录
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundExtraSimulate(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        fx67llDortmundExtraSimulate.setUserId(SecurityUtils.getUserId());
        fx67llDortmundExtraSimulate.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulate.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraSimulateMapper.insertFx67llDortmundExtraSimulate(fx67llDortmundExtraSimulate);
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
