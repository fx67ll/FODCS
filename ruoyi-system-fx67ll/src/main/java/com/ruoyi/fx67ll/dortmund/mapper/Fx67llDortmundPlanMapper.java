package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlan;

/**
 * 方案计划Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundPlanMapper {
    /**
     * 查询方案计划
     *
     * @param planId 方案计划主键
     * @return 方案计划
     */
    public Fx67llDortmundPlan selectFx67llDortmundPlanByPlanId(Long planId);

    /**
     * 查询方案计划列表
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 方案计划集合
     */
    public List<Fx67llDortmundPlan> selectFx67llDortmundPlanList(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 新增方案计划
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int insertFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 修改方案计划
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int updateFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 修改第1.5阶段方案计划业务字段
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int updateFx67llDortmundPlanPhase15(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 修改方案计划摘要
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int updateFx67llDortmundPlanSummary(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 按比赛主键标记关联方案计划摘要需要更新
     *
     * @param matchId 比赛主键
     * @return 结果
     */
    public int markSummaryUpdateByMatchId(Long matchId);

    /**
     * 删除方案计划
     *
     * @param planId 方案计划主键
     * @return 结果
     */
    public int deleteFx67llDortmundPlanByPlanId(Long planId);

    /**
     * 批量删除方案计划
     *
     * @param planIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundPlanByPlanIds(Long[] planIds);
}
