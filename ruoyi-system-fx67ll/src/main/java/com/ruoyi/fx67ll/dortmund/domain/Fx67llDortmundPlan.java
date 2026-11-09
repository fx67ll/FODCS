package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
     * 录入倍率（空表示凭证没有该项；有值时必须大于0）
     */
    @Excel(name = "录入倍率")
    private BigDecimal planRate;

    /**
     * 录入金额（空表示凭证没有该项；有值时必须大于等于0）
     */
    @Excel(name = "录入金额")
    private BigDecimal planAmount;

    /**
     * 已选择的采用回报组合主键集合（非表字段）
     */
    private List<Long> combinationIds;

    /**
     * 按关联组合自动计算的倍率
     */
    @Excel(name = "自动倍率")
    private BigDecimal calculatedPlanRate;

    /**
     * 按关联组合自动计算的金额
     */
    @Excel(name = "自动金额")
    private BigDecimal calculatedPlanAmount;

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
     * 有效倍率（非表字段，手工倍率优先）
     */
    @Excel(name = "有效倍率")
    private BigDecimal effectivePlanRate;

    /**
     * 有效金额（非表字段，手工金额优先）
     */
    @Excel(name = "有效金额")
    private BigDecimal effectivePlanAmount;

    /**
     * 其他说明
     */
    @Excel(name = "其他说明")
    private String planRemark;

    /**
     * 第一阶段方案计划图片对象键集合（第1.5阶段不再使用，仅保留代码兼容）
     */
    @JsonIgnore
    private String[] planImageObjectKeys;

    /**
     * 第一阶段方案计划图片对象键分隔串（第1.5阶段不再映射数据库）
     */
    @JsonIgnore
    private String planImageObjectKeysStr;

    /**
     * 已关联比赛ID集合（非表字段，与 newMatches 合并去重后写入关系表）
     */
    private Long[] matchIds;

    /**
     * 查询用关联比赛ID（非表字段，列表按单场比赛过滤关联方案计划，与 matchIds 提交字段区分）
     */
    private Long matchId;

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

    public void setPlanRate(BigDecimal planRate) {
        this.planRate = planRate;
    }

    public BigDecimal getPlanRate() {
        return planRate;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public List<Long> getCombinationIds() {
        return combinationIds;
    }

    public void setCombinationIds(List<Long> combinationIds) {
        this.combinationIds = combinationIds;
    }

    public BigDecimal getCalculatedPlanRate() {
        return calculatedPlanRate;
    }

    public void setCalculatedPlanRate(BigDecimal calculatedPlanRate) {
        this.calculatedPlanRate = calculatedPlanRate;
    }

    public BigDecimal getCalculatedPlanAmount() {
        return calculatedPlanAmount;
    }

    public void setCalculatedPlanAmount(BigDecimal calculatedPlanAmount) {
        this.calculatedPlanAmount = calculatedPlanAmount;
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

    public BigDecimal getEffectivePlanRate() {
        return effectivePlanRate;
    }

    public void setEffectivePlanRate(BigDecimal effectivePlanRate) {
        this.effectivePlanRate = effectivePlanRate;
    }

    public BigDecimal getEffectivePlanAmount() {
        return effectivePlanAmount;
    }

    public void setEffectivePlanAmount(BigDecimal effectivePlanAmount) {
        this.effectivePlanAmount = effectivePlanAmount;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public String[] getPlanImageObjectKeys() {
        return planImageObjectKeys;
    }

    public void setPlanImageObjectKeys(String[] planImageObjectKeys) {
        this.planImageObjectKeys = planImageObjectKeys;
    }

    public String getPlanImageObjectKeysStr() {
        return planImageObjectKeysStr;
    }

    public void setPlanImageObjectKeysStr(String planImageObjectKeysStr) {
        this.planImageObjectKeysStr = planImageObjectKeysStr;
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
                .append("planRate", getPlanRate())
                .append("planAmount", getPlanAmount())
                .append("combinationIds", getCombinationIds())
                .append("calculatedPlanRate", getCalculatedPlanRate())
                .append("calculatedPlanAmount", getCalculatedPlanAmount())
                .append("summaryContent", getSummaryContent())
                .append("summaryUpdateFlag", getSummaryUpdateFlag())
                .append("summaryUpdateTime", getSummaryUpdateTime())
                .append("effectivePlanRate", getEffectivePlanRate())
                .append("effectivePlanAmount", getEffectivePlanAmount())
                .append("planRemark", getPlanRemark())
                .append("planImageObjectKeys", getPlanImageObjectKeys())
                .append("planImageObjectKeysStr", getPlanImageObjectKeysStr())
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
