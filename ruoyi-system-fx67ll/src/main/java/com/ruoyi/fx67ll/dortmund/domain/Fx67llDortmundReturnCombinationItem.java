package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 回报组合明细对象 fx67ll_dortmund_return_combination_item
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundReturnCombinationItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 组合明细主键 */
    private Long itemId;

    /** 回报组合主键 */
    private Long combinationId;

    /** 比赛主键 */
    private Long matchId;

    /** 倍率记录主键 */
    private Long oddsId;

    /** 比赛编码 */
    private String matchCode;

    /** 选择类型 */
    private String selectionType;

    /** 倍率值快照 */
    private BigDecimal oddsValueSnapshot;

    /** 手工权重快照 */
    private BigDecimal manualWeightSnapshot;

    /** 本次应用权重 */
    private BigDecimal appliedWeight;

    /** 明细排序 */
    private Integer itemSort;

    /** 选择理由 */
    private String selectionReason;

    /** 比赛摘要 */
    private String matchSummary;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getOddsId() {
        return oddsId;
    }

    public void setOddsId(Long oddsId) {
        this.oddsId = oddsId;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public BigDecimal getOddsValueSnapshot() {
        return oddsValueSnapshot;
    }

    public BigDecimal getOddsValue() {
        return oddsValueSnapshot;
    }

    public void setOddsValueSnapshot(BigDecimal oddsValueSnapshot) {
        this.oddsValueSnapshot = oddsValueSnapshot;
    }

    public BigDecimal getManualWeightSnapshot() {
        return manualWeightSnapshot;
    }

    public void setManualWeightSnapshot(BigDecimal manualWeightSnapshot) {
        this.manualWeightSnapshot = manualWeightSnapshot;
    }

    public BigDecimal getAppliedWeight() {
        return appliedWeight;
    }

    public void setAppliedWeight(BigDecimal appliedWeight) {
        this.appliedWeight = appliedWeight;
    }

    public Integer getItemSort() {
        return itemSort;
    }

    public void setItemSort(Integer itemSort) {
        this.itemSort = itemSort;
    }

    public String getSelectionReason() {
        return selectionReason;
    }

    public void setSelectionReason(String selectionReason) {
        this.selectionReason = selectionReason;
    }

    public String getMatchSummary() {
        return matchSummary;
    }

    public void setMatchSummary(String matchSummary) {
        this.matchSummary = matchSummary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("itemId", getItemId())
                .append("combinationId", getCombinationId())
                .append("matchId", getMatchId())
                .append("oddsId", getOddsId())
                .append("matchCode", getMatchCode())
                .append("selectionType", getSelectionType())
                .append("oddsValueSnapshot", getOddsValueSnapshot())
                .append("manualWeightSnapshot", getManualWeightSnapshot())
                .append("appliedWeight", getAppliedWeight())
                .append("itemSort", getItemSort())
                .append("selectionReason", getSelectionReason())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
