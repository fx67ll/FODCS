package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 球队管理对象 fx67ll_dortmund_team
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llDortmundBasicTeam extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 球队唯一标识（主键）
     */
    private Long teamId;

    /**
     * 球队业务编码（唯一，如DORTMUND、BAYERN）
     */
    @Excel(name = "球队业务编码", readConverterExp = "唯=一，如DORTMUND、BAYERN")
    private String teamCode;

    /**
     * 球队全称（如多特蒙德足球俱乐部）
     */
    @Excel(name = "球队全称", readConverterExp = "如=多特蒙德足球俱乐部")
    private String teamName;

    /**
     * 球队简称或昵称（如我横、大黄蜂）
     */
    @Excel(name = "球队简称或昵称", readConverterExp = "如=我横、大黄蜂")
    private String teamNameShort;

    /**
     * 球队英文全称（如Borussia Dortmund）
     */
    @Excel(name = "球队英文全称", readConverterExp = "如=Borussia,D=ortmund")
    private String teamNameEn;

    /**
     * 球队Logo图片URL地址
     */
    @Excel(name = "球队Logo图片URL地址")
    private String teamLogoUrl;

    /**
     * 球队主场（如伊杜纳信号公园球场）
     */
    @Excel(name = "球队主场", readConverterExp = "如=伊杜纳信号公园球场")
    private String teamVenue;

    /**
     * 球队所属国家/地区（如德国、英格兰）
     */
    @Excel(name = "球队所属国家/地区", readConverterExp = "如=德国、英格兰")
    private String teamCountry;

    /**
     * 球队标签（如主场龙、客场虫）
     */
    @Excel(name = "球队标签", readConverterExp = "如=主场龙、客场虫")
    private String teamTag;

    /**
     * 球队状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "球队状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String teamStatus;

    /**
     * 球队展示排序（升序排列，数值越小越靠前）
     */
    @Excel(name = "球队展示排序", readConverterExp = "升=序排列，数值越小越靠前")
    private Integer teamSort;

    /**
     * 球队业务备注
     */
    @Excel(name = "球队业务备注")
    private String teamRemark;

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

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamNameShort(String teamNameShort) {
        this.teamNameShort = teamNameShort;
    }

    public String getTeamNameShort() {
        return teamNameShort;
    }

    public void setTeamNameEn(String teamNameEn) {
        this.teamNameEn = teamNameEn;
    }

    public String getTeamNameEn() {
        return teamNameEn;
    }

    public void setTeamLogoUrl(String teamLogoUrl) {
        this.teamLogoUrl = teamLogoUrl;
    }

    public String getTeamLogoUrl() {
        return teamLogoUrl;
    }

    public void setTeamVenue(String teamVenue) {
        this.teamVenue = teamVenue;
    }

    public String getTeamVenue() {
        return teamVenue;
    }

    public void setTeamCountry(String teamCountry) {
        this.teamCountry = teamCountry;
    }

    public String getTeamCountry() {
        return teamCountry;
    }

    public void setTeamTag(String teamTag) {
        this.teamTag = teamTag;
    }

    public String getTeamTag() {
        return teamTag;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamSort(Integer teamSort) {
        this.teamSort = teamSort;
    }

    public Integer getTeamSort() {
        return teamSort;
    }

    public void setTeamRemark(String teamRemark) {
        this.teamRemark = teamRemark;
    }

    public String getTeamRemark() {
        return teamRemark;
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
                .append("teamId", getTeamId())
                .append("teamCode", getTeamCode())
                .append("teamName", getTeamName())
                .append("teamNameShort", getTeamNameShort())
                .append("teamNameEn", getTeamNameEn())
                .append("teamLogoUrl", getTeamLogoUrl())
                .append("teamVenue", getTeamVenue())
                .append("teamCountry", getTeamCountry())
                .append("teamTag", getTeamTag())
                .append("teamStatus", getTeamStatus())
                .append("teamSort", getTeamSort())
                .append("teamRemark", getTeamRemark())
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
