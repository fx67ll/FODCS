package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundRoundPlanRel;

/**
 * 数值轮次与方案计划关系Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundRoundPlanRelMapper {
    /**
     * 查询数值轮次与方案计划关系
     *
     * @param relId 关系唯一标识
     * @return 数值轮次与方案计划关系
     */
    public Fx67llDortmundRoundPlanRel selectFx67llDortmundRoundPlanRelByRelId(Long relId);

    /**
     * 查询数值轮次与方案计划关系列表
     *
     * @param fx67llDortmundRoundPlanRel 数值轮次与方案计划关系
     * @return 数值轮次与方案计划关系集合
     */
    public List<Fx67llDortmundRoundPlanRel> selectFx67llDortmundRoundPlanRelList(Fx67llDortmundRoundPlanRel fx67llDortmundRoundPlanRel);

    /**
     * 新增数值轮次与方案计划关系
     *
     * @param fx67llDortmundRoundPlanRel 数值轮次与方案计划关系
     * @return 结果
     */
    public int insertFx67llDortmundRoundPlanRel(Fx67llDortmundRoundPlanRel fx67llDortmundRoundPlanRel);

    /**
     * 修改数值轮次与方案计划关系
     *
     * @param fx67llDortmundRoundPlanRel 数值轮次与方案计划关系
     * @return 结果
     */
    public int updateFx67llDortmundRoundPlanRel(Fx67llDortmundRoundPlanRel fx67llDortmundRoundPlanRel);

    /**
     * 删除数值轮次与方案计划关系
     *
     * @param relId 关系唯一标识
     * @return 结果
     */
    public int deleteFx67llDortmundRoundPlanRelByRelId(Long relId);

    /**
     * 批量删除数值轮次与方案计划关系
     *
     * @param relIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundRoundPlanRelByRelIds(Long[] relIds);

    /**
     * 按数值轮次删除方案计划关系
     *
     * @param roundId 数值模拟轮次ID
     * @return 结果
     */
    public int deleteFx67llDortmundRoundPlanRelByRoundId(Long roundId);

    /**
     * 按所属版本删除该版本全部轮次的方案计划关系
     *
     * @param versionId 数值模拟版本ID
     * @return 结果
     */
    public int deleteFx67llDortmundRoundPlanRelByVersionId(Long versionId);
}
