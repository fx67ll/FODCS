package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外快盈亏与方案计划关系对象 fx67ll_dortmund_extra_plan_rel
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundExtraPlanRel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 关系唯一标识（主键）
     */
    private Long relId;

    /**
     * 外快盈亏ID（外键，关联fx67ll_dortmund_extra.extra_id）
     */
    private Long extraId;

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

    public void setExtraId(Long extraId) {
        this.extraId = extraId;
    }

    public Long getExtraId() {
        return extraId;
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
                .append("extraId", getExtraId())
                .append("planId", getPlanId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
