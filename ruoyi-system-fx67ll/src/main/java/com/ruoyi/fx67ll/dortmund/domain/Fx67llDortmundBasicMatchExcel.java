package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 比赛记录业务交换对象。
 * <p>
 * 同时承载标准 JSON 导入和 Excel 导出：不使用数据库主键作为交换字段，赛季按名称、
 * 主客队按编码（名称仅作兜底）解析，并由服务层继续执行用户数据范围和唯一性校验。
 * Excel 仅用于导出查看，比赛记录导入只接受 JSON。
 *
 * @author fx67ll
 * @date 2026-11-25
 */
public class Fx67llDortmundBasicMatchExcel {
    /** 比赛编码；留空时由服务端按比赛时间自动生成 */
    @Excel(name = "比赛编码", width = 24, prompt = "选填；留空时自动生成 MATCH-yyyyMMdd-XXXXXX")
    private String matchCode;

    /** 赛季名称，需与当前用户可见赛季完全一致 */
    @Excel(name = "赛季名称", width = 30, prompt = "必填；填写赛季管理中的完整赛季名称")
    private String seasonName;

    /** 主队编码，优先按编码精确匹配 */
    @Excel(name = "主队编码", width = 20, prompt = "选填；编码缺失时按球队名称唯一匹配")
    private String homeTeamCode;

    /** 主队名称，用于导出阅读和编码缺失时的唯一名称兜底 */
    @Excel(name = "主队名称", width = 24)
    private String homeTeamName;

    /** 客队编码，优先按编码精确匹配 */
    @Excel(name = "客队编码", width = 20, prompt = "选填；编码缺失时按球队名称唯一匹配")
    private String awayTeamCode;

    /** 客队名称，用于导出阅读和编码缺失时的唯一名称兜底 */
    @Excel(name = "客队名称", width = 24)
    private String awayTeamName;

    /** 比赛开球时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "比赛开球时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss",
            prompt = "必填；格式 yyyy-MM-dd HH:mm:ss")
    private Date matchTime;

    /** 球队主场；留空时自动使用主队主场 */
    @Excel(name = "球队主场", width = 30, prompt = "选填；留空时自动带出主队主场")
    private String matchVenue;

    /** 比赛资料状态：0-正常，1-关闭 */
    @Excel(name = "比赛状态", readConverterExp = "0=正常,1=关闭", prompt = "选填；默认正常")
    private String matchStatus;

    /** 常规比赛结果：0-主队胜，1-平局，2-客队胜 */
    @Excel(name = "比赛结果", readConverterExp = "0=主队胜,1=平局,2=客队胜", prompt = "选填")
    private String matchResult;

    /** 倍率赛果，多个编码使用英文逗号分隔：HW/DR/AW/HH/HD/HA */
    @Excel(name = "倍率赛果", width = 24,
            prompt = "选填；多个值用英文逗号分隔，可选 HW/DR/AW/HH/HD/HA")
    private String oddsResult;

    /** 比赛业务备注 */
    @Excel(name = "比赛备注", width = 40)
    private String matchRemark;

    public Fx67llDortmundBasicMatchExcel() {
    }

    /**
     * 从比赛列表行构建导出对象。
     *
     * @param source 比赛列表行
     */
    public Fx67llDortmundBasicMatchExcel(Fx67llDortmundBasicMatch source) {
        if (source == null) {
            return;
        }
        this.matchCode = source.getMatchCode();
        this.seasonName = source.getSeasonName();
        this.homeTeamCode = source.getHomeTeamCode();
        this.homeTeamName = source.getHomeTeamName();
        this.awayTeamCode = source.getAwayTeamCode();
        this.awayTeamName = source.getAwayTeamName();
        this.matchTime = source.getMatchTime();
        this.matchVenue = source.getMatchVenue();
        this.matchStatus = source.getMatchStatus();
        this.matchResult = source.getMatchResult();
        this.oddsResult = source.getOddsResult();
        this.matchRemark = source.getMatchRemark();
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getHomeTeamCode() {
        return homeTeamCode;
    }

    public void setHomeTeamCode(String homeTeamCode) {
        this.homeTeamCode = homeTeamCode;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamCode() {
        return awayTeamCode;
    }

    public void setAwayTeamCode(String awayTeamCode) {
        this.awayTeamCode = awayTeamCode;
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

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
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

    public String getMatchRemark() {
        return matchRemark;
    }

    public void setMatchRemark(String matchRemark) {
        this.matchRemark = matchRemark;
    }
}
