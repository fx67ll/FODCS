package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外快盈亏数值模拟版本记录对象 fx67ll_dortmund_extra_simulate_version
 *
 * @author fx67ll
 * @date 2026-09-11
 */
public class Fx67llDortmundExtraSimulateVersion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数值模拟版本记录主键
     */
    private Long versionId;

    /**
     * 版本序号（同一用户内递增）
     */
    private Long versionNo;

    /**
     * 模拟模式（full全量滚动 half半量滚动）
     */
    private String simMode;

    /**
     * 初始数值
     */
    private String initialValue;

    /**
     * 每轮系数
     */
    private String coefficient;

    /**
     * 目标数值
     */
    private String targetValue;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建开始时间
     */
    private String beginCreateTime;

    /**
     * 创建结束时间
     */
    private String endCreateTime;

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setSimMode(String simMode) {
        this.simMode = simMode;
    }

    public String getSimMode() {
        return simMode;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public String getTargetValue() {
        return targetValue;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("versionId", getVersionId())
                .append("versionNo", getVersionNo())
                .append("simMode", getSimMode())
                .append("initialValue", getInitialValue())
                .append("coefficient", getCoefficient())
                .append("targetValue", getTargetValue())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
