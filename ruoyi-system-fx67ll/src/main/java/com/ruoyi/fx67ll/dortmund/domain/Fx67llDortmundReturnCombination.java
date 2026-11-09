package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 回报组合对象 fx67ll_dortmund_return_combination
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundReturnCombination extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 回报组合主键 */
    private Long combinationId;

    /** 生成批次主键 */
    private Long generationId;

    /** 生成批次编码 */
    private String generationCode;

    /** 生成模式 */
    private String generationMode;

    /** 生成批次状态 */
    private String generationStatus;

    /** 回报组合编码 */
    private String combinationCode;

    /** 回报组合标题 */
    private String combinationTitle;

    /** 回报组合理由 */
    private String combinationReason;

    /** 组合倍率 */
    private BigDecimal combinedOdds;

    /** 预计回报 */
    private BigDecimal estimatedReturn;

    /** 与目标金额的偏差 */
    private BigDecimal deviationAmount;

    /** 是否采用 */
    private String adoptedFlag;

    /** 最近审核人 */
    private String reviewBy;

    /** 最近审核时间 */
    private Date reviewTime;

    /** 组合图片对象键分隔串（表字段，不直接出现在接口中） */
    @JsonIgnore
    private String combinationImageObjectKeys;

    /** 组合图片对象键集合（非表字段） */
    private String[] imageObjectKeys;

    /** AI分析业务输出 */
    private String combinationRemark;

    /** 数据归属用户主键 */
    private Long userId;

    /** 比赛数量 */
    private Integer matchCount;

    /** 组合明细集合 */
    private List<Fx67llDortmundReturnCombinationItem> matches;

    /** 创建开始时间 */
    private String beginCreateTime;

    /** 创建结束时间 */
    private String endCreateTime;

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public Long getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Long generationId) {
        this.generationId = generationId;
    }

    public String getGenerationCode() {
        return generationCode;
    }

    public void setGenerationCode(String generationCode) {
        this.generationCode = generationCode;
    }

    public String getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(String generationMode) {
        this.generationMode = generationMode;
    }

    public String getGenerationStatus() {
        return generationStatus;
    }

    public void setGenerationStatus(String generationStatus) {
        this.generationStatus = generationStatus;
    }

    public String getCombinationCode() {
        return combinationCode;
    }

    public void setCombinationCode(String combinationCode) {
        this.combinationCode = combinationCode;
    }

    public String getCombinationTitle() {
        return combinationTitle;
    }

    public void setCombinationTitle(String combinationTitle) {
        this.combinationTitle = combinationTitle;
    }

    public String getCombinationReason() {
        return combinationReason;
    }

    public void setCombinationReason(String combinationReason) {
        this.combinationReason = combinationReason;
    }

    public BigDecimal getCombinedOdds() {
        return combinedOdds;
    }

    public void setCombinedOdds(BigDecimal combinedOdds) {
        this.combinedOdds = combinedOdds;
    }

    public BigDecimal getEstimatedReturn() {
        return estimatedReturn;
    }

    public void setEstimatedReturn(BigDecimal estimatedReturn) {
        this.estimatedReturn = estimatedReturn;
    }

    public BigDecimal getDeviationAmount() {
        return deviationAmount;
    }

    public void setDeviationAmount(BigDecimal deviationAmount) {
        this.deviationAmount = deviationAmount;
    }

    public String getAdoptedFlag() {
        return adoptedFlag;
    }

    public void setAdoptedFlag(String adoptedFlag) {
        this.adoptedFlag = adoptedFlag;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getCombinationImageObjectKeys() {
        return combinationImageObjectKeys;
    }

    public void setCombinationImageObjectKeys(String combinationImageObjectKeys) {
        this.combinationImageObjectKeys = combinationImageObjectKeys;
    }

    public String[] getImageObjectKeys() {
        return imageObjectKeys;
    }

    public void setImageObjectKeys(String[] imageObjectKeys) {
        this.imageObjectKeys = imageObjectKeys;
    }

    public String getCombinationRemark() {
        return combinationRemark;
    }

    public void setCombinationRemark(String combinationRemark) {
        this.combinationRemark = combinationRemark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public List<Fx67llDortmundReturnCombinationItem> getMatches() {
        return matches;
    }

    public void setMatches(List<Fx67llDortmundReturnCombinationItem> matches) {
        this.matches = matches;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("combinationId", getCombinationId())
                .append("generationId", getGenerationId())
                .append("generationCode", getGenerationCode())
                .append("generationMode", getGenerationMode())
                .append("generationStatus", getGenerationStatus())
                .append("combinationCode", getCombinationCode())
                .append("combinationTitle", getCombinationTitle())
                .append("combinationReason", getCombinationReason())
                .append("combinedOdds", getCombinedOdds())
                .append("estimatedReturn", getEstimatedReturn())
                .append("deviationAmount", getDeviationAmount())
                .append("adoptedFlag", getAdoptedFlag())
                .append("reviewBy", getReviewBy())
                .append("reviewTime", getReviewTime())
                .append("imageObjectKeys", getImageObjectKeys())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
