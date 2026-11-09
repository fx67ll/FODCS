package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.ruoyi.common.annotation.Excel;

/**
 * Dortmund倍率记录导出响应对象
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundOddsExportResponse {
    @Excel(name = "倍率记录编码")
    private String oddsCode;

    @Excel(name = "比赛编码")
    private String matchCode;

    @Excel(name = "比赛摘要")
    private String matchSummary;

    @Excel(name = "倍率来源时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sourceTime;

    @Excel(name = "让分值")
    private BigDecimal handicapValue;

    @Excel(name = "主胜倍率")
    private BigDecimal homeWinOdds;

    @Excel(name = "主胜权重")
    private BigDecimal homeWinWeight;

    @Excel(name = "平局倍率")
    private BigDecimal drawOdds;

    @Excel(name = "平局权重")
    private BigDecimal drawWeight;

    @Excel(name = "客胜倍率")
    private BigDecimal awayWinOdds;

    @Excel(name = "客胜权重")
    private BigDecimal awayWinWeight;

    @Excel(name = "让分主胜倍率")
    private BigDecimal handicapHomeWinOdds;

    @Excel(name = "让分主胜权重")
    private BigDecimal handicapHomeWinWeight;

    @Excel(name = "让分平局倍率")
    private BigDecimal handicapDrawOdds;

    @Excel(name = "让分平局权重")
    private BigDecimal handicapDrawWeight;

    @Excel(name = "让分客胜倍率")
    private BigDecimal handicapAwayWinOdds;

    @Excel(name = "让分客胜权重")
    private BigDecimal handicapAwayWinWeight;

    @Excel(name = "是否有效")
    private String activeFlag;

    @Excel(name = "说明")
    private String oddsRemark;

    @Excel(name = "创建时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Fx67llDortmundOddsExportResponse(Fx67llDortmundOdds source) {
        this.oddsCode = source.getOddsCode();
        this.matchCode = source.getMatchCode();
        this.matchSummary = source.getMatchSummary();
        this.sourceTime = source.getSourceTime();
        this.handicapValue = source.getHandicapValue();
        this.homeWinOdds = source.getHomeWinOdds();
        this.homeWinWeight = source.getHomeWinWeight();
        this.drawOdds = source.getDrawOdds();
        this.drawWeight = source.getDrawWeight();
        this.awayWinOdds = source.getAwayWinOdds();
        this.awayWinWeight = source.getAwayWinWeight();
        this.handicapHomeWinOdds = source.getHandicapHomeWinOdds();
        this.handicapHomeWinWeight = source.getHandicapHomeWinWeight();
        this.handicapDrawOdds = source.getHandicapDrawOdds();
        this.handicapDrawWeight = source.getHandicapDrawWeight();
        this.handicapAwayWinOdds = source.getHandicapAwayWinOdds();
        this.handicapAwayWinWeight = source.getHandicapAwayWinWeight();
        this.activeFlag = source.getActiveFlag();
        this.oddsRemark = source.getOddsRemark();
        this.createTime = source.getCreateTime();
    }
}
