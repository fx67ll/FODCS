package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 比赛记录对象 fx67ll_dortmund_match
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llDortmundBasicMatch extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 比赛唯一标识（主键）
     */
    private Long matchId;

    /**
     * 比赛唯一业务编码（规则：season_code + match_time + home_team_code + away_team_code）
     */
    @Excel(name = "比赛唯一业务编码", readConverterExp = "规=则：season_code,+=,m=atch_time,+=,h=ome_team_code,+=,a=way_team_code")
    private String matchCode;

    /**
     * 所属赛季ID（外键，关联fx67ll_dortmund_season.season_id）
     */
    @Excel(name = "所属赛季ID", readConverterExp = "外=键，关联fx67ll_dortmund_season.season_id")
    private Long seasonId;

    /**
     * 赛季名称（关联查询）
     */
    @Excel(name = "赛季名称")
    private String seasonName;

    /**
     * 主队球队ID（外键，关联fx67ll_dortmund_team.team_id）
     */
    @Excel(name = "主队球队ID", readConverterExp = "外=键，关联fx67ll_dortmund_team.team_id")
    private Long homeTeamId;

    /**
     * 主队名称（关联查询）
     */
    @Excel(name = "主队名称")
    private String homeTeamName;

    /**
     * 客队球队ID（外键，关联fx67ll_dortmund_team.team_id）
     */
    @Excel(name = "客队球队ID", readConverterExp = "外=键，关联fx67ll_dortmund_team.team_id")
    private Long awayTeamId;

    /**
     * 客队名称（关联查询）
     */
    @Excel(name = "客队名称")
    private String awayTeamName;

    /**
     * 比赛开球时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "比赛开球时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date matchTime;

    /**
     * 比赛举办场地名称
     */
    @Excel(name = "比赛举办场地名称")
    private String matchVenue;

    /**
     * 比赛业务备注（如轮次、特殊说明）
     */
    @Excel(name = "比赛业务备注", readConverterExp = "如=轮次、特殊说明")
    private String matchRemark;

    /**
     * 比赛状态（字典码：0-未开始，1-进行中，2-已结束）
     */
    @Excel(name = "比赛状态", readConverterExp = "字=典码：0-未开始，1-进行中，2-已结束")
    private String matchStatus;

    /**
     * AI分析次数（统计该比赛已生成的分析报告数量）
     */
    @Excel(name = "AI分析次数", readConverterExp = "统=计该比赛已生成的分析报告数量")
    private Integer analysisCount;

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

    /**
     * 比赛开始时间查询参数
     */
    private String beginMatchTime;

    /**
     * 比赛结束时间查询参数
     */
    private String endMatchTime;


    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }


    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchVenue(String matchVenue) {
        this.matchVenue = matchVenue;
    }

    public String getMatchVenue() {
        return matchVenue;
    }

    public void setMatchRemark(String matchRemark) {
        this.matchRemark = matchRemark;
    }

    public String getMatchRemark() {
        return matchRemark;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setAnalysisCount(Integer analysisCount) {
        this.analysisCount = analysisCount;
    }

    public Integer getAnalysisCount() {
        return analysisCount;
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

    public String getBeginMatchTime() {
        return beginMatchTime;
    }

    public void setBeginMatchTime(String beginMatchTime) {
        this.beginMatchTime = beginMatchTime;
    }

    public String getEndMatchTime() {
        return endMatchTime;
    }

    public void setEndMatchTime(String endMatchTime) {
        this.endMatchTime = endMatchTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("matchId", getMatchId())
                .append("matchCode", getMatchCode())
                .append("seasonId", getSeasonId())
                .append("seasonName", getSeasonName())
                .append("homeTeamId", getHomeTeamId())
                .append("homeTeamName", getHomeTeamName())
                .append("awayTeamId", getAwayTeamId())
                .append("awayTeamName", getAwayTeamName())
                .append("matchTime", getMatchTime())
                .append("matchVenue", getMatchVenue())
                .append("matchRemark", getMatchRemark())
                .append("matchStatus", getMatchStatus())
                .append("analysisCount", getAnalysisCount())
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
                .append("beginMatchTime", getBeginMatchTime())
                .append("endMatchTime", getEndMatchTime())
                .toString();
    }
}