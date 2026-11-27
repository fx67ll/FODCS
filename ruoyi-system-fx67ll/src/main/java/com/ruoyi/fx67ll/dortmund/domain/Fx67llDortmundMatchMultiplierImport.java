package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 比赛 JSON 导入附带的倍率记录交换对象。
 * <p>
 * 外部 AI 只提交业务倍率字段，平台编码由服务端生成或校验；六类倍率必须完整，
 * 权重可留空并按 1.0000 参与后续计算。该对象不直接暴露数据库主键和归属字段。</p>
 *
 * @author fx67ll
 * @date 2026-11-27
 */
public class Fx67llDortmundMatchMultiplierImport {
    /** 倍率记录编码；留空时由服务端生成 ODDS-yyyyMMdd-XXXXXX */
    @JsonAlias("oddsCode")
    private String multiplierCode;

    /** 倍率来源时间；留空时使用导入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sourceTime;

    /** 让分值 */
    private BigDecimal handicapValue;

    /** 主胜倍率 */
    @JsonAlias("homeWinOdds")
    private BigDecimal homeWinMultiplier;

    /** 主胜权重，范围 0.0000-1.0000 */
    private BigDecimal homeWinWeight;

    /** 平局倍率 */
    @JsonAlias("drawOdds")
    private BigDecimal drawMultiplier;

    /** 平局权重，范围 0.0000-1.0000 */
    private BigDecimal drawWeight;

    /** 客胜倍率 */
    @JsonAlias("awayWinOdds")
    private BigDecimal awayWinMultiplier;

    /** 客胜权重，范围 0.0000-1.0000 */
    private BigDecimal awayWinWeight;

    /** 让分主胜倍率 */
    @JsonAlias("handicapHomeWinOdds")
    private BigDecimal handicapHomeWinMultiplier;

    /** 让分主胜权重，范围 0.0000-1.0000 */
    private BigDecimal handicapHomeWinWeight;

    /** 让分平局倍率 */
    @JsonAlias("handicapDrawOdds")
    private BigDecimal handicapDrawMultiplier;

    /** 让分平局权重，范围 0.0000-1.0000 */
    private BigDecimal handicapDrawWeight;

    /** 让分客胜倍率 */
    @JsonAlias("handicapAwayWinOdds")
    private BigDecimal handicapAwayWinMultiplier;

    /** 让分客胜权重，范围 0.0000-1.0000 */
    private BigDecimal handicapAwayWinWeight;

    /** 是否为当前有效版本；Y-有效，N-历史版本；留空默认 Y */
    private String activeFlag;

    /** 倍率记录说明 */
    @JsonAlias("oddsRemark")
    private String multiplierRemark;

    public String getMultiplierCode() {
        return multiplierCode;
    }

    public void setMultiplierCode(String multiplierCode) {
        this.multiplierCode = multiplierCode;
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

    public BigDecimal getHomeWinMultiplier() {
        return homeWinMultiplier;
    }

    public void setHomeWinMultiplier(BigDecimal homeWinMultiplier) {
        this.homeWinMultiplier = homeWinMultiplier;
    }

    public BigDecimal getHomeWinWeight() {
        return homeWinWeight;
    }

    public void setHomeWinWeight(BigDecimal homeWinWeight) {
        this.homeWinWeight = homeWinWeight;
    }

    public BigDecimal getDrawMultiplier() {
        return drawMultiplier;
    }

    public void setDrawMultiplier(BigDecimal drawMultiplier) {
        this.drawMultiplier = drawMultiplier;
    }

    public BigDecimal getDrawWeight() {
        return drawWeight;
    }

    public void setDrawWeight(BigDecimal drawWeight) {
        this.drawWeight = drawWeight;
    }

    public BigDecimal getAwayWinMultiplier() {
        return awayWinMultiplier;
    }

    public void setAwayWinMultiplier(BigDecimal awayWinMultiplier) {
        this.awayWinMultiplier = awayWinMultiplier;
    }

    public BigDecimal getAwayWinWeight() {
        return awayWinWeight;
    }

    public void setAwayWinWeight(BigDecimal awayWinWeight) {
        this.awayWinWeight = awayWinWeight;
    }

    public BigDecimal getHandicapHomeWinMultiplier() {
        return handicapHomeWinMultiplier;
    }

    public void setHandicapHomeWinMultiplier(BigDecimal handicapHomeWinMultiplier) {
        this.handicapHomeWinMultiplier = handicapHomeWinMultiplier;
    }

    public BigDecimal getHandicapHomeWinWeight() {
        return handicapHomeWinWeight;
    }

    public void setHandicapHomeWinWeight(BigDecimal handicapHomeWinWeight) {
        this.handicapHomeWinWeight = handicapHomeWinWeight;
    }

    public BigDecimal getHandicapDrawMultiplier() {
        return handicapDrawMultiplier;
    }

    public void setHandicapDrawMultiplier(BigDecimal handicapDrawMultiplier) {
        this.handicapDrawMultiplier = handicapDrawMultiplier;
    }

    public BigDecimal getHandicapDrawWeight() {
        return handicapDrawWeight;
    }

    public void setHandicapDrawWeight(BigDecimal handicapDrawWeight) {
        this.handicapDrawWeight = handicapDrawWeight;
    }

    public BigDecimal getHandicapAwayWinMultiplier() {
        return handicapAwayWinMultiplier;
    }

    public void setHandicapAwayWinMultiplier(BigDecimal handicapAwayWinMultiplier) {
        this.handicapAwayWinMultiplier = handicapAwayWinMultiplier;
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

    public String getMultiplierRemark() {
        return multiplierRemark;
    }

    public void setMultiplierRemark(String multiplierRemark) {
        this.multiplierRemark = multiplierRemark;
    }
}
