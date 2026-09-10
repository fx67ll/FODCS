package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;

/**
 * 外快盈亏数值模拟轮次记录Service接口
 *
 * @author fx67ll
 * @date 2026-09-10
 */
public interface IFx67llDortmundExtraSimulateService {
    /**
     * 查询外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulateBySimId(Long simId);

    /**
     * 查询外快盈亏数值模拟轮次记录列表
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录集合
     */
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateList(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate);

    /**
     * 新增外快盈亏数值模拟轮次记录
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 结果
     */
    public int insertFx67llDortmundExtraSimulate(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate);

    /**
     * 删除外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundExtraSimulateBySimId(Long simId);
}
