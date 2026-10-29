package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 方案计划对象 fx67ll_dortmund_plan
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public class Fx67llDortmundPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 方案计划唯一标识（主键）
     */
    private Long planId;

    /**
     * 正文
     */
    @Excel(name = "正文")
    private String planContent;

    /**
     * 录入倍率（空表示凭证没有该项；有值时必须大于0）
     */
    @Excel(name = "录入倍率")
    private BigDecimal planRate;

    /**
     * 录入金额（空表示凭证没有该项；有值时必须大于等于0）
     */
    @Excel(name = "录入金额")
    private BigDecimal planAmount;

    /**
     * 其他说明
     */
    @Excel(name = "其他说明")
    private String planRemark;

    /**
     * 图片对象键集合（非表字段，接口提交与返回均为数组，最多两个相对对象键）
     * 不传（null）表示图片保持不变；空数组表示全部移除；非空数组表示按提交集合替换
     */
    private String[] planImageObjectKeys;

    /**
     * 图片对象键逗号分隔串（表字段，由服务层从数组拼接写入并拆分读出，不直接出现在接口中）
     */
    @JsonIgnore
    @Excel(name = "图片对象键")
    private String planImageObjectKeysStr;

    /**
     * 已关联比赛ID集合（非表字段，与 newMatches 合并去重后写入关系表）
     */
    private Long[] matchIds;

    /**
     * 当场新建比赛集合（非表字段，只能引用已有赛季和球队，不能带 newSeason 等嵌套字段）
     */
    private List<Fx67llDortmundBasicMatch> newMatches;

    /**
     * 关联比赛列表（非表字段，查询时只读返回）
     */
    private List<Fx67llDortmundBasicMatch> matches;

    /**
     * 关联比赛数量（非表字段，列表只读派生值）
     */
    private Integer matchCount;

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

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanRate(BigDecimal planRate) {
        this.planRate = planRate;
    }

    public BigDecimal getPlanRate() {
        return planRate;
    }

    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public String[] getPlanImageObjectKeys() {
        return planImageObjectKeys;
    }

    public void setPlanImageObjectKeys(String[] planImageObjectKeys) {
        this.planImageObjectKeys = planImageObjectKeys;
    }

    public String getPlanImageObjectKeysStr() {
        return planImageObjectKeysStr;
    }

    public void setPlanImageObjectKeysStr(String planImageObjectKeysStr) {
        this.planImageObjectKeysStr = planImageObjectKeysStr;
    }

    public Long[] getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(Long[] matchIds) {
        this.matchIds = matchIds;
    }

    public List<Fx67llDortmundBasicMatch> getNewMatches() {
        return newMatches;
    }

    public void setNewMatches(List<Fx67llDortmundBasicMatch> newMatches) {
        this.newMatches = newMatches;
    }

    public List<Fx67llDortmundBasicMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<Fx67llDortmundBasicMatch> matches) {
        this.matches = matches;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
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
                .append("planId", getPlanId())
                .append("planContent", getPlanContent())
                .append("planRate", getPlanRate())
                .append("planAmount", getPlanAmount())
                .append("planRemark", getPlanRemark())
                .append("planImageObjectKeys", getPlanImageObjectKeys())
                .append("planImageObjectKeysStr", getPlanImageObjectKeysStr())
                .append("matchIds", getMatchIds())
                .append("newMatches", getNewMatches())
                .append("matches", getMatches())
                .append("matchCount", getMatchCount())
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
