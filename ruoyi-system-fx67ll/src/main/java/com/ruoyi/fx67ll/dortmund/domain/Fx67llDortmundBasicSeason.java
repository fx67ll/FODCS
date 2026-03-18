package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 赛季管理对象 fx67ll_dortmund_season
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llDortmundBasicSeason extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 赛季唯一标识（主键）
     */
    private Long seasonId;

    /**
     * 赛季业务编码（唯一，如2025-2026_Bundesliga）
     */
    @Excel(name = "赛季业务编码", readConverterExp = "唯=一，如2025-2026_Bundesliga")
    private String seasonCode;

    /**
     * 赛季业务名称（如2025-2026赛季德甲联赛）
     */
    @Excel(name = "赛季业务名称", readConverterExp = "如=2025-2026赛季德甲联赛")
    private String seasonName;

    /**
     * 赛季开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "赛季开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date seasonStartDate;

    /**
     * 赛季结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "赛季结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date seasonEndDate;

    /**
     * 查询条件：赛季开始日期 >= 此日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date seasonStartQueryDate;

    /**
     * 查询条件：赛季结束日期 <= 此日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date seasonEndQueryDate;

    /**
     * 赛季状态（字典码：0-进行中，1-已结束，2-未开始）
     */
    @Excel(name = "赛季状态", readConverterExp = "字=典码：0-进行中，1-已结束，2-未开始")
    private String seasonStatus;

    /**
     * 赛季展示排序（升序排列，数值越小越靠前）
     */
    @Excel(name = "赛季展示排序", readConverterExp = "升=序排列，数值越小越靠前")
    private Integer seasonSort;

    /**
     * 赛季业务备注（说明赛事级别、规则等）
     */
    @Excel(name = "赛季业务备注", readConverterExp = "说=明赛事级别、规则等")
    private String seasonRemark;

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
     * 是否需要排序标志（Y-按sort排序，其他情况按create_time倒序）
     */
    private String isNeedSort;

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

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonCode(String seasonCode) {
        this.seasonCode = seasonCode;
    }

    public String getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonStartDate(Date seasonStartDate) {
        this.seasonStartDate = seasonStartDate;
    }

    public Date getSeasonStartDate() {
        return seasonStartDate;
    }

    public void setSeasonEndDate(Date seasonEndDate) {
        this.seasonEndDate = seasonEndDate;
    }

    public Date getSeasonEndDate() {
        return seasonEndDate;
    }

    public void setSeasonStatus(String seasonStatus) {
        this.seasonStatus = seasonStatus;
    }

    public String getSeasonStatus() {
        return seasonStatus;
    }

    public void setSeasonSort(Integer seasonSort) {
        this.seasonSort = seasonSort;
    }

    public Integer getSeasonSort() {
        return seasonSort;
    }

    public void setSeasonRemark(String seasonRemark) {
        this.seasonRemark = seasonRemark;
    }

    public String getSeasonRemark() {
        return seasonRemark;
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

    public String getIsNeedSort() {
        return isNeedSort;
    }

    public void setIsNeedSort(String isNeedSort) {
        this.isNeedSort = isNeedSort;
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
                .append("seasonId", getSeasonId())
                .append("seasonCode", getSeasonCode())
                .append("seasonName", getSeasonName())
                .append("seasonStartDate", getSeasonStartDate())
                .append("seasonEndDate", getSeasonEndDate())
                .append("seasonStatus", getSeasonStatus())
                .append("seasonSort", getSeasonSort())
                .append("seasonRemark", getSeasonRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("isNeedSort", getIsNeedSort())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
