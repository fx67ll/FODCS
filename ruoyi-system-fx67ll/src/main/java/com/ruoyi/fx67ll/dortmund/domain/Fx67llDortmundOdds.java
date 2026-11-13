package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Dortmund倍率记录对象 fx67ll_dortmund_odds
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundOdds extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 倍率记录主键 */
    private Long oddsId;

    /** 比赛主键 */
    private Long matchId;

    /** 比赛编码 */
    private String matchCode;

    /** 比赛摘要 */
    private String matchSummary;

    /** 倍率记录编码 */
    private String oddsCode;

    /** 倍率来源时间 */
    private Date sourceTime;

    /** 让分值 */
    private BigDecimal handicapValue;

    /** 主胜倍率 */
    private BigDecimal homeWinOdds;

    /** 主胜权重 */
    private BigDecimal homeWinWeight;

    /** 平局倍率 */
    private BigDecimal drawOdds;

    /** 平局权重 */
    private BigDecimal drawWeight;

    /** 客胜倍率 */
    private BigDecimal awayWinOdds;

    /** 客胜权重 */
    private BigDecimal awayWinWeight;

    /** 让分主胜倍率 */
    private BigDecimal handicapHomeWinOdds;

    /** 让分主胜权重 */
    private BigDecimal handicapHomeWinWeight;

    /** 让分平局倍率 */
    private BigDecimal handicapDrawOdds;

    /** 让分平局权重 */
    private BigDecimal handicapDrawWeight;

    /** 让分客胜倍率 */
    private BigDecimal handicapAwayWinOdds;

    /** 让分客胜权重 */
    private BigDecimal handicapAwayWinWeight;

    /** 是否为当前有效记录 */
    private String activeFlag;

    /** 倍率记录说明 */
    private String oddsRemark;

    /** 数据归属用户主键 */
    private Long userId;

    /** 删除标志 */
    private String delFlag;

    /** 倍率来源开始时间 */
    private String beginSourceTime;

    /** 倍率来源结束时间 */
    private String endSourceTime;

    /** 创建开始时间 */
    private String beginCreateTime;

    /** 创建结束时间 */
    private String endCreateTime;

    /** 更新开始时间 */
    private String beginUpdateTime;

    /** 更新结束时间 */
    private String endUpdateTime;

    /** 同一事务内新增的比赛 */
    private Fx67llDortmundBasicMatch newMatch;

    public Long getOddsId() {
        return oddsId;
    }

    public void setOddsId(Long oddsId) {
        this.oddsId = oddsId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getMatchSummary() {
        return matchSummary;
    }

    public void setMatchSummary(String matchSummary) {
        this.matchSummary = matchSummary;
    }

    public String getOddsCode() {
        return oddsCode;
    }

    public void setOddsCode(String oddsCode) {
        this.oddsCode = oddsCode;
    }

    public Date getSourceTime() {
        return sourceTime;
    }

    public void setSourceTime(Date sourceTime) {
        this.sourceTime = sourceTime;
    }

    public BigDecimal getHandicapValue() {
        return handicapValue;
    }

    public void setHandicapValue(BigDecimal handicapValue) {
        this.handicapValue = handicapValue;
    }

    public BigDecimal getHomeWinOdds() {
        return homeWinOdds;
    }

    public void setHomeWinOdds(BigDecimal homeWinOdds) {
        this.homeWinOdds = homeWinOdds;
    }

    public BigDecimal getHomeWinWeight() {
        return homeWinWeight;
    }

    public void setHomeWinWeight(BigDecimal homeWinWeight) {
        this.homeWinWeight = homeWinWeight;
    }

    public BigDecimal getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(BigDecimal drawOdds) {
        this.drawOdds = drawOdds;
    }

    public BigDecimal getDrawWeight() {
        return drawWeight;
    }

    public void setDrawWeight(BigDecimal drawWeight) {
        this.drawWeight = drawWeight;
    }

    public BigDecimal getAwayWinOdds() {
        return awayWinOdds;
    }

    public void setAwayWinOdds(BigDecimal awayWinOdds) {
        this.awayWinOdds = awayWinOdds;
    }

    public BigDecimal getAwayWinWeight() {
        return awayWinWeight;
    }

    public void setAwayWinWeight(BigDecimal awayWinWeight) {
        this.awayWinWeight = awayWinWeight;
    }

    public BigDecimal getHandicapHomeWinOdds() {
        return handicapHomeWinOdds;
    }

    public void setHandicapHomeWinOdds(BigDecimal handicapHomeWinOdds) {
        this.handicapHomeWinOdds = handicapHomeWinOdds;
    }

    public BigDecimal getHandicapHomeWinWeight() {
        return handicapHomeWinWeight;
    }

    public void setHandicapHomeWinWeight(BigDecimal handicapHomeWinWeight) {
        this.handicapHomeWinWeight = handicapHomeWinWeight;
    }

    public BigDecimal getHandicapDrawOdds() {
        return handicapDrawOdds;
    }

    public void setHandicapDrawOdds(BigDecimal handicapDrawOdds) {
        this.handicapDrawOdds = handicapDrawOdds;
    }

    public BigDecimal getHandicapDrawWeight() {
        return handicapDrawWeight;
    }

    public void setHandicapDrawWeight(BigDecimal handicapDrawWeight) {
        this.handicapDrawWeight = handicapDrawWeight;
    }

    public BigDecimal getHandicapAwayWinOdds() {
        return handicapAwayWinOdds;
    }

    public void setHandicapAwayWinOdds(BigDecimal handicapAwayWinOdds) {
        this.handicapAwayWinOdds = handicapAwayWinOdds;
    }

    public BigDecimal getHandicapAwayWinWeight() {
        return handicapAwayWinWeight;
    }

    public void setHandicapAwayWinWeight(BigDecimal handicapAwayWinWeight) {
        this.handicapAwayWinWeight = handicapAwayWinWeight;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getOddsRemark() {
        return oddsRemark;
    }

    public void setOddsRemark(String oddsRemark) {
        this.oddsRemark = oddsRemark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getBeginSourceTime() {
        return beginSourceTime;
    }

    public void setBeginSourceTime(String beginSourceTime) {
        this.beginSourceTime = beginSourceTime;
    }

    public String getEndSourceTime() {
        return endSourceTime;
    }

    public void setEndSourceTime(String endSourceTime) {
        this.endSourceTime = endSourceTime;
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

    public Fx67llDortmundBasicMatch getNewMatch() {
        return newMatch;
    }

    public void setNewMatch(Fx67llDortmundBasicMatch newMatch) {
        this.newMatch = newMatch;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("oddsId", getOddsId())
                .append("matchId", getMatchId())
                .append("matchCode", getMatchCode())
                .append("matchSummary", getMatchSummary())
                .append("oddsCode", getOddsCode())
                .append("sourceTime", getSourceTime())
                .append("handicapValue", getHandicapValue())
                .append("homeWinOdds", getHomeWinOdds())
                .append("homeWinWeight", getHomeWinWeight())
                .append("drawOdds", getDrawOdds())
                .append("drawWeight", getDrawWeight())
                .append("awayWinOdds", getAwayWinOdds())
                .append("awayWinWeight", getAwayWinWeight())
                .append("handicapHomeWinOdds", getHandicapHomeWinOdds())
                .append("handicapHomeWinWeight", getHandicapHomeWinWeight())
                .append("handicapDrawOdds", getHandicapDrawOdds())
                .append("handicapDrawWeight", getHandicapDrawWeight())
                .append("handicapAwayWinOdds", getHandicapAwayWinOdds())
                .append("handicapAwayWinWeight", getHandicapAwayWinWeight())
                .append("activeFlag", getActiveFlag())
                .append("oddsRemark", getOddsRemark())
                .append("userId", getUserId())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
