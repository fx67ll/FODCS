package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointTeamRel;

/**
 * 信息点球队关系Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundInfoPointTeamRelMapper {
    /**
     * 查询信息点球队关系
     *
     * @param relationId 关联唯一标识
     * @return 信息点球队关系
     */
    public Fx67llDortmundInfoPointTeamRel selectFx67llDortmundInfoPointTeamRelByRelationId(Long relationId);

    /**
     * 查询信息点球队关系列表
     *
     * @param fx67llDortmundInfoPointTeamRel 信息点球队关系
     * @return 信息点球队关系集合
     */
    public List<Fx67llDortmundInfoPointTeamRel> selectFx67llDortmundInfoPointTeamRelList(Fx67llDortmundInfoPointTeamRel fx67llDortmundInfoPointTeamRel);

    /**
     * 新增信息点球队关系
     *
     * @param fx67llDortmundInfoPointTeamRel 信息点球队关系
     * @return 结果
     */
    public int insertFx67llDortmundInfoPointTeamRel(Fx67llDortmundInfoPointTeamRel fx67llDortmundInfoPointTeamRel);

    /**
     * 修改信息点球队关系
     *
     * @param fx67llDortmundInfoPointTeamRel 信息点球队关系
     * @return 结果
     */
    public int updateFx67llDortmundInfoPointTeamRel(Fx67llDortmundInfoPointTeamRel fx67llDortmundInfoPointTeamRel);
}
