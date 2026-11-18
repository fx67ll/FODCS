package com.ruoyi.fx67ll.dortmund.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * 回报组合AI业务上下文。
 * <p>
 * 该对象只承载提交时冻结的比赛、球队和信息点业务事实，不包含供应商、模型、Prompt、Token、
 * 费用或调用链治理元数据。上下文随业务快照一起固化，自动AI、人工AI任务和后续通用AI模块
 * 均消费同一结构。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public class ReturnCombinationAiBusinessContext {
    /** 业务上下文结构版本 */
    public static final String CONTEXT_VERSION = "1.0";

    /** 业务上下文版本 */
    private String contextVersion = CONTEXT_VERSION;

    /** 参与分析的比赛上下文 */
    private List<MatchContext> matches = new ArrayList<>();

    public String getContextVersion() {
        return contextVersion;
    }

    public void setContextVersion(String contextVersion) {
        this.contextVersion = contextVersion;
    }

    public List<MatchContext> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchContext> matches) {
        this.matches = matches;
    }

    /**
     * 单场比赛上下文
     */
    public static class MatchContext {
        private Long matchId;

        private String matchCode;

        private String matchTime;

        private Long seasonId;

        private String seasonName;

        private TeamContext homeTeam;

        private TeamContext awayTeam;

        /** 比赛临时信息点（M类型） */
        private List<InfoPointContext> matchInfoPoints = new ArrayList<>();

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

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public Long getSeasonId() {
            return seasonId;
        }

        public void setSeasonId(Long seasonId) {
            this.seasonId = seasonId;
        }

        public String getSeasonName() {
            return seasonName;
        }

        public void setSeasonName(String seasonName) {
            this.seasonName = seasonName;
        }

        public TeamContext getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(TeamContext homeTeam) {
            this.homeTeam = homeTeam;
        }

        public TeamContext getAwayTeam() {
            return awayTeam;
        }

        public void setAwayTeam(TeamContext awayTeam) {
            this.awayTeam = awayTeam;
        }

        public List<InfoPointContext> getMatchInfoPoints() {
            return matchInfoPoints;
        }

        public void setMatchInfoPoints(List<InfoPointContext> matchInfoPoints) {
            this.matchInfoPoints = matchInfoPoints;
        }
    }

    /**
     * 球队上下文
     */
    public static class TeamContext {
        private Long teamId;

        private String teamName;

        /** 原始球队标签列表，保留标签原文，不映射固定字典 */
        private List<String> teamTags = new ArrayList<>();

        /** 赛季球队信息点（T类型） */
        private List<InfoPointContext> seasonInfoPoints = new ArrayList<>();

        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public List<String> getTeamTags() {
            return teamTags;
        }

        public void setTeamTags(List<String> teamTags) {
            this.teamTags = teamTags;
        }

        public List<InfoPointContext> getSeasonInfoPoints() {
            return seasonInfoPoints;
        }

        public void setSeasonInfoPoints(List<InfoPointContext> seasonInfoPoints) {
            this.seasonInfoPoints = seasonInfoPoints;
        }
    }

    /**
     * 信息点上下文
     */
    public static class InfoPointContext {
        private Long infoPointId;

        private String infoPointType;

        private String pointTitle;

        private String pointContent;

        private String sourceName;

        private String sourceUrl;

        private String sourcePublishTime;

        private String collectTime;

        private String effectiveStartTime;

        private String effectiveEndTime;

        /** 信息点当前有效关联的球队主键 */
        private List<Long> teamIds = new ArrayList<>();

        public Long getInfoPointId() {
            return infoPointId;
        }

        public void setInfoPointId(Long infoPointId) {
            this.infoPointId = infoPointId;
        }

        public String getInfoPointType() {
            return infoPointType;
        }

        public void setInfoPointType(String infoPointType) {
            this.infoPointType = infoPointType;
        }

        public String getPointTitle() {
            return pointTitle;
        }

        public void setPointTitle(String pointTitle) {
            this.pointTitle = pointTitle;
        }

        public String getPointContent() {
            return pointContent;
        }

        public void setPointContent(String pointContent) {
            this.pointContent = pointContent;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getSourcePublishTime() {
            return sourcePublishTime;
        }

        public void setSourcePublishTime(String sourcePublishTime) {
            this.sourcePublishTime = sourcePublishTime;
        }

        public String getCollectTime() {
            return collectTime;
        }

        public void setCollectTime(String collectTime) {
            this.collectTime = collectTime;
        }

        public String getEffectiveStartTime() {
            return effectiveStartTime;
        }

        public void setEffectiveStartTime(String effectiveStartTime) {
            this.effectiveStartTime = effectiveStartTime;
        }

        public String getEffectiveEndTime() {
            return effectiveEndTime;
        }

        public void setEffectiveEndTime(String effectiveEndTime) {
            this.effectiveEndTime = effectiveEndTime;
        }

        public List<Long> getTeamIds() {
            return teamIds;
        }

        public void setTeamIds(List<Long> teamIds) {
            this.teamIds = teamIds;
        }
    }
}
