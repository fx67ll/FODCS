package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 方案计划与比赛关系对象 fx67ll_dortmund_plan_match_rel
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundPlanMatchRel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关系唯一标识（主键）
     */
    private Long relId;

    /**
     * 方案计划ID（外键，关联fx67ll_dortmund_plan.plan_id）
     */
    private Long planId;

    /**
     * 比赛ID（外键，关联fx67ll_dortmund_match.match_id）
     */
    private Long matchId;

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getRelId() {
        return relId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("relId", getRelId())
                .append("planId", getPlanId())
                .append("matchId", getMatchId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
