package com.ruoyi.fx67ll.dortmund.ai;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AI消息中的倍率视图。
 * <p>
 * 该对象只用于模型输入。默认将六类真实倍率统一替换为占位文本，避免模型把倍率数值当作预测依据；
 * 用户显式关闭隐藏倍率时，六类字段改为发送真实数值，仅作为市场参考。无论是否隐藏，
 * 真实倍率都保留在生成批次快照中，候选生成或人工导入后由后端从快照查询并完成金额复算。
 * </p>
 *
 * @author fx67ll
 */
public class ReturnCombinationAiOdds {

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

    /** 让分值；只描述盘口结构，不属于真实倍率数值 */
    private BigDecimal handicapValue;

    /** 主胜倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值；为空表示该场无对应选择 */
    private Object homeWinOdds;

    /** 主胜权重；S模式为用户权威权重，A模式保留空值或参考值 */
    private BigDecimal homeWinWeight;

    /** 平局倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值；为空表示该场无对应选择 */
    private Object drawOdds;

    /** 平局权重 */
    private BigDecimal drawWeight;

    /** 客胜倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值；为空表示该场无对应选择 */
    private Object awayWinOdds;

    /** 客胜权重 */
    private BigDecimal awayWinWeight;
    /** 让分主胜倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值 */
    private Object handicapHomeWinOdds;

    /** 让分主胜权重 */
    private BigDecimal handicapHomeWinWeight;

    /** 让分平局倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值 */
    private Object handicapDrawOdds;

    /** 让分平局权重 */
    private BigDecimal handicapDrawWeight;

    /** 让分客胜倍率；隐藏模式下为占位文本，关闭隐藏后为真实数值 */
    private Object handicapAwayWinOdds;

    /** 让分客胜权重 */
    private BigDecimal handicapAwayWinWeight;

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

    public Object getHomeWinOdds() {
        return homeWinOdds;
    }

    public void setHomeWinOdds(Object homeWinOdds) {
        this.homeWinOdds = homeWinOdds;
    }

    public BigDecimal getHomeWinWeight() {
        return homeWinWeight;
    }

    public void setHomeWinWeight(BigDecimal homeWinWeight) {
        this.homeWinWeight = homeWinWeight;
    }
    public Object getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(Object drawOdds) {
        this.drawOdds = drawOdds;
    }

    public BigDecimal getDrawWeight() {
        return drawWeight;
    }

    public void setDrawWeight(BigDecimal drawWeight) {
        this.drawWeight = drawWeight;
    }

    public Object getAwayWinOdds() {
        return awayWinOdds;
    }

    public void setAwayWinOdds(Object awayWinOdds) {
        this.awayWinOdds = awayWinOdds;
    }

    public BigDecimal getAwayWinWeight() {
        return awayWinWeight;
    }

    public void setAwayWinWeight(BigDecimal awayWinWeight) {
        this.awayWinWeight = awayWinWeight;
    }
    public Object getHandicapHomeWinOdds() {
        return handicapHomeWinOdds;
    }

    public void setHandicapHomeWinOdds(Object handicapHomeWinOdds) {
        this.handicapHomeWinOdds = handicapHomeWinOdds;
    }

    public BigDecimal getHandicapHomeWinWeight() {
        return handicapHomeWinWeight;
    }

    public void setHandicapHomeWinWeight(BigDecimal handicapHomeWinWeight) {
        this.handicapHomeWinWeight = handicapHomeWinWeight;
    }

    public Object getHandicapDrawOdds() {
        return handicapDrawOdds;
    }

    public void setHandicapDrawOdds(Object handicapDrawOdds) {
        this.handicapDrawOdds = handicapDrawOdds;
    }
    public BigDecimal getHandicapDrawWeight() {
        return handicapDrawWeight;
    }

    public void setHandicapDrawWeight(BigDecimal handicapDrawWeight) {
        this.handicapDrawWeight = handicapDrawWeight;
    }

    public Object getHandicapAwayWinOdds() {
        return handicapAwayWinOdds;
    }

    public void setHandicapAwayWinOdds(Object handicapAwayWinOdds) {
        this.handicapAwayWinOdds = handicapAwayWinOdds;
    }

    public BigDecimal getHandicapAwayWinWeight() {
        return handicapAwayWinWeight;
    }

    public void setHandicapAwayWinWeight(BigDecimal handicapAwayWinWeight) {
        this.handicapAwayWinWeight = handicapAwayWinWeight;
    }
}
