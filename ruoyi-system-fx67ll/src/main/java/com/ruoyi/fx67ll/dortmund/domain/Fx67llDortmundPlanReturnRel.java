package com.ruoyi.fx67ll.dortmund.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 方案计划与回报组合关系对象 fx67ll_dortmund_plan_return_rel
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundPlanReturnRel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 关系主键 */
    private Long relId;

    /** 方案计划主键 */
    private Long planId;

    /** 回报组合主键 */
    private Long combinationId;

    /** 关系排序 */
    private Integer relSort;

    public Long getRelId() {
        return relId;
    }

    public void setRelId(Long relId) {
        this.relId = relId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public Integer getRelSort() {
        return relSort;
    }

    public void setRelSort(Integer relSort) {
        this.relSort = relSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("relId", getRelId())
                .append("planId", getPlanId())
                .append("combinationId", getCombinationId())
                .append("relSort", getRelSort())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
