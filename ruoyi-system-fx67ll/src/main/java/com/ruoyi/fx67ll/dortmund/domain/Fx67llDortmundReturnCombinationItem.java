package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    /** 用户提交时的原始手工权重快照（0.0000-1.0000），允许为空；空值在AI输入和appliedWeight中按1.0000解释 */
    private BigDecimal manualWeightSnapshot;

    /**
     * AI评估权重（0.0000-1.0000）
     * <p>
     * 仅用于记录AI对本次比赛选择类型的评估权重，便于复核候选组合的生成依据；
     * 不参与组合倍率、预计回报、偏差金额或方案金额等任何金额计算。
     */
    private BigDecimal appliedWeight;

    /** 明细排序 */
    private Integer itemSort;

    /** 逐场选择理由快照；不参与任何金额计算 */
    private String selectionReason;

    /** 完整权重推导说明，仅用于自动AI复核 */
    private String weightRationale;

    /** 本选择引用的候选证据因子编码JSON数组 */
    private String evidenceRefs;

    /** 已考虑但未占主导的反向证据因子编码JSON数组 */
    private String counterEvidenceRefs;

    /** 比赛摘要 */
    private String matchSummary;

    /** 主队名称（非表字段，由组合明细联表补齐，供方案预览直接展示） */
    private String homeTeamName;

    /** 客队名称（非表字段，由组合明细联表补齐，供方案预览直接展示） */
    private String awayTeamName;

    /** 比赛开球时间（非表字段，由组合明细联表补齐） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date matchTime;

    /** 人工登记的比赛结果（非表字段，由组合明细联表补齐，用于结果页展示；核验使用 oddsResult） */
    private String matchResult;

    /** 人工登记的倍率赛果（非表字段，逗号分隔；回报组合核验只读取该字段） */
    private String oddsResult;

    /** 球队主场（非表字段，由组合明细联表补齐） */
    private String matchVenue;

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
        // AI评估权重仅用于生成解释和历史复核，不是金额系数。
        return appliedWeight;
    }

    public void setAppliedWeight(BigDecimal appliedWeight) {
        // AI评估权重仅用于生成解释和历史复核，不是金额系数。
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

    public String getWeightRationale() {
        return weightRationale;
    }

    public void setWeightRationale(String weightRationale) {
        this.weightRationale = weightRationale;
    }

    public String getEvidenceRefs() {
        return evidenceRefs;
    }

    public void setEvidenceRefs(String evidenceRefs) {
        this.evidenceRefs = evidenceRefs;
    }

    public String getCounterEvidenceRefs() {
        return counterEvidenceRefs;
    }

    public void setCounterEvidenceRefs(String counterEvidenceRefs) {
        this.counterEvidenceRefs = counterEvidenceRefs;
    }

    public String getMatchSummary() {
        return matchSummary;
    }

    public void setMatchSummary(String matchSummary) {
        this.matchSummary = matchSummary;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getOddsResult() {
        return oddsResult;
    }

    public void setOddsResult(String oddsResult) {
        this.oddsResult = oddsResult;
    }

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
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
                // appliedWeight: AI评估权重，不参与任何金额计算。
                .append("appliedWeight", getAppliedWeight())
                .append("itemSort", getItemSort())
                .append("selectionReason", getSelectionReason())
                .append("weightRationale", getWeightRationale())
                .append("evidenceRefs", getEvidenceRefs())
                .append("counterEvidenceRefs", getCounterEvidenceRefs())
                .append("homeTeamName", getHomeTeamName())
                .append("awayTeamName", getAwayTeamName())
                .append("matchTime", getMatchTime())
                .append("matchResult", getMatchResult())
                .append("oddsResult", getOddsResult())
                .append("matchVenue", getMatchVenue())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
