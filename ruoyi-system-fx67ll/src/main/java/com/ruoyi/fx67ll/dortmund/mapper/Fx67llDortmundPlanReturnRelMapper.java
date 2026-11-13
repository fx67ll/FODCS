package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanReturnRel;

/**
 * 方案计划与回报组合关系Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundPlanReturnRelMapper {
    /**
     * 查询方案计划关联的回报组合关系
     *
     * @param planId 方案计划主键
     * @return 回报组合关系集合
     */
    public List<Fx67llDortmundPlanReturnRel> selectListByPlanId(Long planId);

    /**
     * 按方案计划主键集合批量查询回报组合关系
     *
     * @param planIds 方案计划主键集合
     * @return 回报组合关系集合
     */
    public List<Fx67llDortmundPlanReturnRel> selectListByPlanIds(@Param("planIds") List<Long> planIds);

    /**
     * 查询回报组合已关联的方案计划主键
     *
     * @param combinationId 回报组合主键
     * @return 方案计划主键
     */
    public Long selectPlanIdByCombinationId(Long combinationId);

    /**
     * 新增方案计划与回报组合关系
     *
     * @param fx67llDortmundPlanReturnRel 方案计划与回报组合关系
     * @return 结果
     */
    public int insertFx67llDortmundPlanReturnRel(Fx67llDortmundPlanReturnRel fx67llDortmundPlanReturnRel);

    /**
     * 删除方案计划关联的回报组合关系
     *
     * @param planId 方案计划主键
     * @return 结果
     */
    public int deleteByPlanId(Long planId);
}
