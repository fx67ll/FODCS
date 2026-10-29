package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数值轮次与方案计划关系对象 fx67ll_dortmund_round_plan_rel
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundRoundPlanRel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关系唯一标识（主键）
     */
    private Long relId;

    /**
     * 数值模拟轮次ID（外键，关联fx67ll_dortmund_extra_simulate.sim_id）
     */
    private Long roundId;

    /**
     * 方案计划ID（外键，关联fx67ll_dortmund_plan.plan_id）
     */
    private Long planId;

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getRelId() {
        return relId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("relId", getRelId())
                .append("roundId", getRoundId())
                .append("planId", getPlanId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
