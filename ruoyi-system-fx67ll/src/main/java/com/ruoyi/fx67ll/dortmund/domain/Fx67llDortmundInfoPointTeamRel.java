package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 信息点球队关系对象 fx67ll_dortmund_info_point_team_rel
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundInfoPointTeamRel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关联唯一标识（主键）
     */
    private Long relationId;

    /**
     * 信息点ID（必须指向T类型，由服务层校验；外键关联fx67ll_dortmund_info_point.info_point_id）
     */
    private Long infoPointId;

    /**
     * 球队ID（外键，关联fx67ll_dortmund_team.team_id）
     */
    private Long teamId;

    /**
     * 关联状态（0-有效，1-停用，字典fx67ll_dortmund_relation_status）
     */
    private String relationStatus;

    /**
     * 球队名称（关联查询）
     */
    private String teamName;

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setInfoPointId(Long infoPointId) {
        this.infoPointId = infoPointId;
    }

    public Long getInfoPointId() {
        return infoPointId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public String getRelationStatus() {
        return relationStatus;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("relationId", getRelationId())
                .append("infoPointId", getInfoPointId())
                .append("teamId", getTeamId())
                .append("relationStatus", getRelationStatus())
                .append("teamName", getTeamName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
