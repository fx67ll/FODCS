package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 统一信息点对象 fx67ll_dortmund_info_point
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundInfoPoint extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 信息点唯一标识（主键）
     */
    private Long infoPointId;

    /**
     * 信息点类型（T-赛季球队信息点，M-比赛临时信息点；创建后不可修改）
     */
    @Excel(name = "信息点类型", readConverterExp = "T=赛季球队信息点，M=比赛临时信息点")
    private String infoPointType;

    /**
     * T类型所属赛季ID（外键，关联fx67ll_dortmund_season.season_id；创建后不可修改；M类型为空）
     */
    @Excel(name = "T类型所属赛季ID")
    private Long seasonId;

    /**
     * 赛季名称（关联查询）
     */
    @Excel(name = "赛季名称")
    private String seasonName;

    /**
     * M类型所属比赛ID（外键，关联fx67ll_dortmund_match.match_id；创建后不可修改；T类型为空）
     */
    @Excel(name = "M类型所属比赛ID")
    private Long matchId;

    /**
     * 比赛开球时间（关联查询）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "比赛开球时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date matchTime;

    /**
     * 主队名称（关联查询）
     */
    @Excel(name = "主队名称")
    private String homeTeamName;

    /**
     * 客队名称（关联查询）
     */
    @Excel(name = "客队名称")
    private String awayTeamName;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String pointTitle;

    /**
     * 客观信息内容
     */
    @Excel(name = "客观信息内容")
    private String pointContent;

    /**
     * 信息来源名称
     */
    @Excel(name = "信息来源名称")
    private String sourceName;

    /**
     * 来源地址
     */
    @Excel(name = "来源地址")
    private String sourceUrl;

    /**
     * 来源发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "来源发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sourcePublishTime;

    /**
     * 采集或录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "采集或录入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date collectTime;

    /**
     * 生效时间（空表示该端不限制）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "生效时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveStartTime;

    /**
     * 失效时间（空表示该端不限制）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "失效时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveEndTime;

    /**
     * 信息点状态（0-启用，1-停用，复用sys_normal_disable）
     */
    @Excel(name = "信息点状态", readConverterExp = "0=启用，1=停用")
    private String pointStatus;

    /**
     * 是否可公开（0-不可公开，1-可公开，字典fx67ll_dortmund_allow_public）
     */
    @Excel(name = "是否可公开", readConverterExp = "0=不可公开，1=可公开")
    private String allowPublic;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String pointRemark;

    /**
     * 关联球队ID集合（非表字段，仅T类型创建和编辑时提交）
     */
    private Long[] teamIds;

    /**
     * 球队关系列表（非表字段，查询时只读返回）
     */
    private List<Fx67llDortmundInfoPointTeamRel> teamRels;

    /**
     * 有效标记（非表字段，有效性查询只读返回）
     */
    private Boolean valid;

    /**
     * 排除原因（非表字段，有效性查询只读返回，入选时为空）
     */
    private String excludeReason;

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

    public void setInfoPointId(Long infoPointId) {
        this.infoPointId = infoPointId;
    }

    public Long getInfoPointId() {
        return infoPointId;
    }

    public void setInfoPointType(String infoPointType) {
        this.infoPointType = infoPointType;
    }

    public String getInfoPointType() {
        return infoPointType;
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

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public void setPointTitle(String pointTitle) {
        this.pointTitle = pointTitle;
    }

    public String getPointTitle() {
        return pointTitle;
    }

    public void setPointContent(String pointContent) {
        this.pointContent = pointContent;
    }

    public String getPointContent() {
        return pointContent;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public Date getSourcePublishTime() {
        return sourcePublishTime;
    }

    public void setSourcePublishTime(Date sourcePublishTime) {
        this.sourcePublishTime = sourcePublishTime;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public void setPointStatus(String pointStatus) {
        this.pointStatus = pointStatus;
    }

    public String getPointStatus() {
        return pointStatus;
    }

    public void setAllowPublic(String allowPublic) {
        this.allowPublic = allowPublic;
    }

    public String getAllowPublic() {
        return allowPublic;
    }

    public void setPointRemark(String pointRemark) {
        this.pointRemark = pointRemark;
    }

    public String getPointRemark() {
        return pointRemark;
    }

    public Long[] getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(Long[] teamIds) {
        this.teamIds = teamIds;
    }

    public List<Fx67llDortmundInfoPointTeamRel> getTeamRels() {
        return teamRels;
    }

    public void setTeamRels(List<Fx67llDortmundInfoPointTeamRel> teamRels) {
        this.teamRels = teamRels;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getExcludeReason() {
        return excludeReason;
    }

    public void setExcludeReason(String excludeReason) {
        this.excludeReason = excludeReason;
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
                .append("infoPointId", getInfoPointId())
                .append("infoPointType", getInfoPointType())
                .append("seasonId", getSeasonId())
                .append("seasonName", getSeasonName())
                .append("matchId", getMatchId())
                .append("matchTime", getMatchTime())
                .append("homeTeamName", getHomeTeamName())
                .append("awayTeamName", getAwayTeamName())
                .append("pointTitle", getPointTitle())
                .append("pointContent", getPointContent())
                .append("sourceName", getSourceName())
                .append("sourceUrl", getSourceUrl())
                .append("sourcePublishTime", getSourcePublishTime())
                .append("collectTime", getCollectTime())
                .append("effectiveStartTime", getEffectiveStartTime())
                .append("effectiveEndTime", getEffectiveEndTime())
                .append("pointStatus", getPointStatus())
                .append("allowPublic", getAllowPublic())
                .append("pointRemark", getPointRemark())
                .append("teamIds", getTeamIds())
                .append("teamRels", getTeamRels())
                .append("valid", getValid())
                .append("excludeReason", getExcludeReason())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("userId", getUserId())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
