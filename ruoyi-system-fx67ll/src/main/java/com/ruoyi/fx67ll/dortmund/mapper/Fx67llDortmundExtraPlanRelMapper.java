package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraPlanRel;

/**
 * 外快盈亏与方案计划关系Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundExtraPlanRelMapper {
    /**
     * 查询外快盈亏与方案计划关系
     *
     * @param relId 关系唯一标识
     * @return 外快盈亏与方案计划关系
     */
    public Fx67llDortmundExtraPlanRel selectFx67llDortmundExtraPlanRelByRelId(Long relId);

    /**
     * 查询外快盈亏与方案计划关系列表
     *
     * @param fx67llDortmundExtraPlanRel 外快盈亏与方案计划关系
     * @return 外快盈亏与方案计划关系集合
     */
    public List<Fx67llDortmundExtraPlanRel> selectFx67llDortmundExtraPlanRelList(Fx67llDortmundExtraPlanRel fx67llDortmundExtraPlanRel);

    /**
     * 新增外快盈亏与方案计划关系
     *
     * @param fx67llDortmundExtraPlanRel 外快盈亏与方案计划关系
     * @return 结果
     */
    public int insertFx67llDortmundExtraPlanRel(Fx67llDortmundExtraPlanRel fx67llDortmundExtraPlanRel);

    /**
     * 修改外快盈亏与方案计划关系
     *
     * @param fx67llDortmundExtraPlanRel 外快盈亏与方案计划关系
     * @return 结果
     */
    public int updateFx67llDortmundExtraPlanRel(Fx67llDortmundExtraPlanRel fx67llDortmundExtraPlanRel);

    /**
     * 删除外快盈亏与方案计划关系
     *
     * @param relId 关系唯一标识
     * @return 结果
     */
    public int deleteFx67llDortmundExtraPlanRelByRelId(Long relId);

    /**
     * 批量删除外快盈亏与方案计划关系
     *
     * @param relIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundExtraPlanRelByRelIds(Long[] relIds);

    /**
     * 按外快盈亏删除方案计划关系
     *
     * @param extraId 外快盈亏ID
     * @return 结果
     */
    public int deleteFx67llDortmundExtraPlanRelByExtraId(Long extraId);
}
