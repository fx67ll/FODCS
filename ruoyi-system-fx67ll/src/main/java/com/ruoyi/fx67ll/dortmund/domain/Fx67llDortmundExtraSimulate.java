package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外快盈亏数值模拟轮次记录对象 fx67ll_dortmund_extra_simulate
 *
 * @author fx67ll
 * @date 2026-09-10
 */
public class Fx67llDortmundExtraSimulate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 数值模拟轮次记录主键
     */
    private Long simId;

    /**
     * 模拟模式（full全量滚动 half半量滚动）
     */
    private String simMode;

    /**
     * 轮次序号（连续序列内序号，新序列从1开始）
     */
    private Long roundNo;

    /**
     * 本轮开始数值
     */
    private String startValue;

    /**
     * 本轮参与滚动数值
     */
    private String joinValue;

    /**
     * 本轮是否达成（Y是 N否）
     */
    private String isHit;

    /**
     * 本轮实际回收数值（不必然等于参与数值乘系数，支持部分回本）
     */
    private String returnValue;

    /**
     * 本轮结束数值
     */
    private String endValue;

    /**
     * 本轮数值变动倍数
     */
    private String roundChange;

    /**
     * 本轮使用系数
     */
    private String coefficient;

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

    public void setSimId(Long simId) {
        this.simId = simId;
    }

    public Long getSimId() {
        return simId;
    }

    public void setSimMode(String simMode) {
        this.simMode = simMode;
    }

    public String getSimMode() {
        return simMode;
    }

    public void setRoundNo(Long roundNo) {
        this.roundNo = roundNo;
    }

    public Long getRoundNo() {
        return roundNo;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setJoinValue(String joinValue) {
        this.joinValue = joinValue;
    }

    public String getJoinValue() {
        return joinValue;
    }

    public void setIsHit(String isHit) {
        this.isHit = isHit;
    }

    public String getIsHit() {
        return isHit;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setRoundChange(String roundChange) {
        this.roundChange = roundChange;
    }

    public String getRoundChange() {
        return roundChange;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getCoefficient() {
        return coefficient;
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
                .append("simId", getSimId())
                .append("simMode", getSimMode())
                .append("roundNo", getRoundNo())
                .append("startValue", getStartValue())
                .append("joinValue", getJoinValue())
                .append("isHit", getIsHit())
                .append("returnValue", getReturnValue())
                .append("endValue", getEndValue())
                .append("roundChange", getRoundChange())
                .append("coefficient", getCoefficient())
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
