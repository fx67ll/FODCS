package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanMatchRel;

/**
 * 方案计划与比赛关系Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundPlanMatchRelMapper {
    /**
     * 查询方案计划与比赛关系
     *
     * @param relId 关系唯一标识
     * @return 方案计划与比赛关系
     */
    public Fx67llDortmundPlanMatchRel selectFx67llDortmundPlanMatchRelByRelId(Long relId);

    /**
     * 查询方案计划与比赛关系列表
     *
     * @param fx67llDortmundPlanMatchRel 方案计划与比赛关系
     * @return 方案计划与比赛关系集合
     */
    public List<Fx67llDortmundPlanMatchRel> selectFx67llDortmundPlanMatchRelList(Fx67llDortmundPlanMatchRel fx67llDortmundPlanMatchRel);

    /**
     * 新增方案计划与比赛关系
     *
     * @param fx67llDortmundPlanMatchRel 方案计划与比赛关系
     * @return 结果
     */
    public int insertFx67llDortmundPlanMatchRel(Fx67llDortmundPlanMatchRel fx67llDortmundPlanMatchRel);

    /**
     * 修改方案计划与比赛关系
     *
     * @param fx67llDortmundPlanMatchRel 方案计划与比赛关系
     * @return 结果
     */
    public int updateFx67llDortmundPlanMatchRel(Fx67llDortmundPlanMatchRel fx67llDortmundPlanMatchRel);

    /**
     * 删除方案计划与比赛关系
     *
     * @param relId 关系唯一标识
     * @return 结果
     */
    public int deleteFx67llDortmundPlanMatchRelByRelId(Long relId);

    /**
     * 批量删除方案计划与比赛关系
     *
     * @param relIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundPlanMatchRelByRelIds(Long[] relIds);

    /**
     * 按方案计划删除比赛关系
     *
     * @param planId 方案计划ID
     * @return 结果
     */
    public int deleteFx67llDortmundPlanMatchRelByPlanId(Long planId);
}
