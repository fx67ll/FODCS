package com.ruoyi.fx67ll.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI Prompt模板分组对象 fx67ll_ai_prompt_group
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiPromptBasicGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分组唯一标识（主键）
     */
    private Long groupId;

    /**
     * 分组业务编码（唯一，用于程序逻辑关联）
     */
    @Excel(name = "分组业务编码", readConverterExp = "唯=一，用于程序逻辑关联")
    private String groupCode;

    /**
     * 分组业务名称（用于前端展示）
     */
    @Excel(name = "分组业务名称", readConverterExp = "用=于前端展示")
    private String groupName;

    /**
     * 分组启用状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "分组启用状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String groupStatus;

    /**
     * 分组展示排序（升序排列，数值越小越靠前）
     */
    @Excel(name = "分组展示排序", readConverterExp = "升=序排列，数值越小越靠前")
    private Integer groupSort;

    /**
     * 分组业务备注（说明分组的用途、范围）
     */
    @Excel(name = "分组业务备注", readConverterExp = "说=明分组的用途、范围")
    private String groupRemark;

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

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupSort(Integer groupSort) {
        this.groupSort = groupSort;
    }

    public Integer getGroupSort() {
        return groupSort;
    }

    public void setGroupRemark(String groupRemark) {
        this.groupRemark = groupRemark;
    }

    public String getGroupRemark() {
        return groupRemark;
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
                .append("groupId", getGroupId())
                .append("groupCode", getGroupCode())
                .append("groupName", getGroupName())
                .append("groupStatus", getGroupStatus())
                .append("groupSort", getGroupSort())
                .append("groupRemark", getGroupRemark())
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
