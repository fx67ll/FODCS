package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 方案计划对象 fx67ll_dortmund_plan
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 方案计划唯一标识（主键）
     */
    private Long planId;

    /**
     * 正文
     */
    @Excel(name = "正文")
    private String planContent;

    /**
     * 方案预计本金（手工修正值；空表示使用关联组合自动汇总的预计本金）
     */
    @Excel(name = "方案预计本金")
    private BigDecimal planExpectedPrincipal;

    /**
     * 方案预计倍率（空表示使用自动计算值；有值时必须大于0）
     */
    @Excel(name = "方案预计倍率")
    private BigDecimal planExpectedOdds;

    /**
     * 方案预计回报（空表示使用自动计算值；有值时必须大于等于0）
     */
    @Excel(name = "方案预计回报")
    private BigDecimal planExpectedReturn;

    /**
     * 方案实际回报（仅由用户手工登记，必填；允许为0）
     */
    @Excel(name = "方案实际回报")
    private BigDecimal planActualReturn;

    /**
     * 方案实际倍率（仅由用户手工登记，必填；允许为0）
     */
    @Excel(name = "方案实际倍率")
    private BigDecimal planActualOdds;

    /**
     * 方案实际本金（仅由用户手工登记，必填；服务端不按关联组合派生）
     */
    @Excel(name = "方案实际本金")
    private BigDecimal planActualPrincipal;

    /**
     * 已选择的采用回报组合主键集合（非表字段）
     */
    private List<Long> combinationIds;

    /**
     * 关联回报组合的赛果核验汇总状态（Y 全部成功，N 存在失败，null 未核验，非表字段）
     */
    private String combinationVerificationStatus;

    /**
     * 按关联组合自动计算的预计本金
     */
    @Excel(name = "自动预计本金")
    private BigDecimal calculatedExpectedPrincipal;

    /**
     * 按关联组合自动计算的预计倍率
     */
    @Excel(name = "自动预计倍率")
    private BigDecimal calculatedExpectedOdds;

    /**
     * 按关联组合自动计算的预计回报
     */
    @Excel(name = "自动预计回报")
    private BigDecimal calculatedExpectedReturn;

    /**
     * 最近一次成功保存的方案计划摘要正文
     */
    private String summaryContent;

    /**
     * 摘要更新标记（Y-需要刷新，N-与当前数据一致）
     */
    private String summaryUpdateFlag;

    /**
     * 最近一次成功保存摘要正文的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date summaryUpdateTime;

    /**
     * 有效预计本金（非表字段，手工预计本金优先）
     */
    @Excel(name = "有效预计本金")
    private BigDecimal effectiveExpectedPrincipal;

    /**
     * 有效预计倍率（非表字段，手工预计倍率优先）
     */
    @Excel(name = "有效预计倍率")
    private BigDecimal effectiveExpectedOdds;

    /**
     * 有效预计回报（非表字段，手工预计回报优先）
     */
    @Excel(name = "有效预计回报")
    private BigDecimal effectiveExpectedReturn;

    /**
     * 其他说明
     */
    @Excel(name = "其他说明")
    private String planRemark;

    /**
     * 已关联比赛ID集合（非表字段，与 newMatches 合并去重后写入关系表）
     */
    private Long[] matchIds;

    /**
     * 查询用关联比赛ID（非表字段，列表按单场比赛过滤关联方案计划，与 matchIds 提交字段区分）
     */
    private Long matchId;

    /**
     * 查询用关联回报组合ID（非表字段，列表按组合过滤关联方案计划）
     */
    private Long combinationId;

    /**
     * 当场新建比赛集合（非表字段，只能引用已有赛季和球队，不能带 newSeason 等嵌套字段）
     */
    private List<Fx67llDortmundBasicMatch> newMatches;

    /**
     * 关联比赛列表（非表字段，查询时只读返回）
     */
    private List<Fx67llDortmundBasicMatch> matches;

    /**
     * 关联比赛数量（非表字段，列表只读派生值）
     */
    private Integer matchCount;

    /**
     * 逻辑删除标志（字典码：0-存在，2-已删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 创建开始时间
     */
    private String beginCreateTime;

    /**
     * 创建结束时间
     */
    private String endCreateTime;

    /**
     * 更新开始时间
     */
    private String beginUpdateTime;

    /**
     * 更新结束时间
     */
    private String endUpdateTime;

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanExpectedPrincipal(BigDecimal planExpectedPrincipal) {
        this.planExpectedPrincipal = planExpectedPrincipal;
    }

    public BigDecimal getPlanExpectedPrincipal() {
        return planExpectedPrincipal;
    }

    public void setPlanExpectedOdds(BigDecimal planExpectedOdds) {
        this.planExpectedOdds = planExpectedOdds;
    }

    public BigDecimal getPlanExpectedOdds() {
        return planExpectedOdds;
    }

    public void setPlanExpectedReturn(BigDecimal planExpectedReturn) {
        this.planExpectedReturn = planExpectedReturn;
    }

    public BigDecimal getPlanExpectedReturn() {
        return planExpectedReturn;
    }

    public void setPlanActualReturn(BigDecimal planActualReturn) {
        this.planActualReturn = planActualReturn;
    }

    public BigDecimal getPlanActualReturn() {
        return planActualReturn;
    }

    public void setPlanActualOdds(BigDecimal planActualOdds) {
        this.planActualOdds = planActualOdds;
    }

    public BigDecimal getPlanActualOdds() {
        return planActualOdds;
    }

    public void setPlanActualPrincipal(BigDecimal planActualPrincipal) {
        this.planActualPrincipal = planActualPrincipal;
    }

    public BigDecimal getPlanActualPrincipal() {
        return planActualPrincipal;
    }

    public List<Long> getCombinationIds() {
        return combinationIds;
    }

    public void setCombinationIds(List<Long> combinationIds) {
        this.combinationIds = combinationIds;
    }

    public String getCombinationVerificationStatus() {
        return combinationVerificationStatus;
    }

    public void setCombinationVerificationStatus(String combinationVerificationStatus) {
        this.combinationVerificationStatus = combinationVerificationStatus;
    }

    public BigDecimal getCalculatedExpectedPrincipal() {
        return calculatedExpectedPrincipal;
    }

    public void setCalculatedExpectedPrincipal(BigDecimal calculatedExpectedPrincipal) {
        this.calculatedExpectedPrincipal = calculatedExpectedPrincipal;
    }

    public BigDecimal getCalculatedExpectedOdds() {
        return calculatedExpectedOdds;
    }

    public void setCalculatedExpectedOdds(BigDecimal calculatedExpectedOdds) {
        this.calculatedExpectedOdds = calculatedExpectedOdds;
    }

    public BigDecimal getCalculatedExpectedReturn() {
        return calculatedExpectedReturn;
    }

    public void setCalculatedExpectedReturn(BigDecimal calculatedExpectedReturn) {
        this.calculatedExpectedReturn = calculatedExpectedReturn;
    }

    public BigDecimal getEffectiveExpectedPrincipal() {
        return effectiveExpectedPrincipal;
    }

    public void setEffectiveExpectedPrincipal(BigDecimal effectiveExpectedPrincipal) {
        this.effectiveExpectedPrincipal = effectiveExpectedPrincipal;
    }

    public String getSummaryContent() {
        return summaryContent;
    }

    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }

    public String getSummaryUpdateFlag() {
        return summaryUpdateFlag;
    }

    public void setSummaryUpdateFlag(String summaryUpdateFlag) {
        this.summaryUpdateFlag = summaryUpdateFlag;
    }

    public Date getSummaryUpdateTime() {
        return summaryUpdateTime;
    }

    public void setSummaryUpdateTime(Date summaryUpdateTime) {
        this.summaryUpdateTime = summaryUpdateTime;
    }

    public BigDecimal getEffectiveExpectedOdds() {
        return effectiveExpectedOdds;
    }

    public void setEffectiveExpectedOdds(BigDecimal effectiveExpectedOdds) {
        this.effectiveExpectedOdds = effectiveExpectedOdds;
    }

    public BigDecimal getEffectiveExpectedReturn() {
        return effectiveExpectedReturn;
    }

    public void setEffectiveExpectedReturn(BigDecimal effectiveExpectedReturn) {
        this.effectiveExpectedReturn = effectiveExpectedReturn;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public Long[] getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(Long[] matchIds) {
        this.matchIds = matchIds;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public List<Fx67llDortmundBasicMatch> getNewMatches() {
        return newMatches;
    }

    public void setNewMatches(List<Fx67llDortmundBasicMatch> newMatches) {
        this.newMatches = newMatches;
    }

    public List<Fx67llDortmundBasicMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<Fx67llDortmundBasicMatch> matches) {
        this.matches = matches;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
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

    public String getBeginUpdateTime() {
        return beginUpdateTime;
    }

    public void setBeginUpdateTime(String beginUpdateTime) {
        this.beginUpdateTime = beginUpdateTime;
    }

    public String getEndUpdateTime() {
        return endUpdateTime;
    }

    public void setEndUpdateTime(String endUpdateTime) {
        this.endUpdateTime = endUpdateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("planId", getPlanId())
                .append("planContent", getPlanContent())
                .append("planExpectedPrincipal", getPlanExpectedPrincipal())
                .append("planExpectedOdds", getPlanExpectedOdds())
                .append("planExpectedReturn", getPlanExpectedReturn())
                .append("planActualReturn", getPlanActualReturn())
                .append("planActualOdds", getPlanActualOdds())
                .append("planActualPrincipal", getPlanActualPrincipal())
                .append("combinationIds", getCombinationIds())
                .append("combinationVerificationStatus", getCombinationVerificationStatus())
                .append("calculatedExpectedPrincipal", getCalculatedExpectedPrincipal())
                .append("calculatedExpectedOdds", getCalculatedExpectedOdds())
                .append("calculatedExpectedReturn", getCalculatedExpectedReturn())
                .append("summaryContent", getSummaryContent())
                .append("summaryUpdateFlag", getSummaryUpdateFlag())
                .append("summaryUpdateTime", getSummaryUpdateTime())
                .append("effectiveExpectedPrincipal", getEffectiveExpectedPrincipal())
                .append("effectiveExpectedOdds", getEffectiveExpectedOdds())
                .append("effectiveExpectedReturn", getEffectiveExpectedReturn())
                .append("planRemark", getPlanRemark())
                .append("matchIds", getMatchIds())
                .append("newMatches", getNewMatches())
                .append("matches", getMatches())
                .append("matchCount", getMatchCount())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
