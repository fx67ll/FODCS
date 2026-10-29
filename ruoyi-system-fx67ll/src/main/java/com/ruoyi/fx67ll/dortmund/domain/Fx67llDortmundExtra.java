package com.ruoyi.fx67ll.dortmund.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外快盈亏记录对象 fx67ll_dortmund_extra
 *
 * @author fx67ll
 * @date 2023-09-14
 */
public class Fx67llDortmundExtra extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 外快记录主键
     */
    private Long extraId;

    /**
     * 当前外快总金额
     */
    @Excel(name = "当前外快总金额")
    private String extraMoney;

    /**
     * 是否盈利（Y是 N否）
     */
    @Excel(name = "是否盈利", readConverterExp = "Y=是,N=否")
    private String isWin;

    /**
     * 外快盈亏金额
     */
    @Excel(name = "外快盈亏金额")
    private String winMoney;

    /**
     * 当前投入本金
     */
    @Excel(name = "当前投入本金")
    private String seedMoney;

    /**
     * 已经落袋为安的盈利金额
     */
    @Excel(name = "已经落袋为安的盈利金额")
    private String saveMoney;

    /**
     * 目标金额
     */
    @Excel(name = "目标金额")
    private String targetMoney;

    /**
     * 外快盈亏备注
     */
    @Excel(name = "外快盈亏备注")
    private String extraRemark;

    /**
     * 已关联方案计划ID集合（非表字段，创建和编辑时可选提交）
     */
    private Long[] planIds;

    /**
     * 当场新建方案计划集合（非表字段，创建和编辑时可选提交，元素只用已有比赛，不能带图片对象键）
     */
    private List<Fx67llDortmundPlan> newPlans;

    /**
     * 图片对象键集合（非表字段，仅用于检测并拒绝请求中携带的图片对象键，外快盈亏不保存图片）
     */
    private String[] planImageObjectKeys;

    /**
     * 关联方案计划摘要（非表字段，详情只读返回，仅对拥有方案计划查询权限的用户填充）
     */
    private List<Fx67llDortmundPlan> plans;

    /**
     * 不可见关联方案计划数量（非表字段，详情只读返回）
     */
    private Integer hiddenPlanCount;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 关联方案计划数量（非表字段，列表只读派生值）
     */
    private Integer planCount;

    /**
     * 关联方案计划ID（非表字段，列表过滤条件，按方案计划查关联的外快记录）
     */
    private Long planId;

    /**
     * 是否关联方案计划（非表字段，列表过滤条件，1-已关联 0-未关联）
     */
    private String hasPlan;

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

    public void setExtraId(Long extraId) {
        this.extraId = extraId;
    }

    public Long getExtraId() {
        return extraId;
    }

    public void setExtraMoney(String extraMoney) {
        this.extraMoney = extraMoney;
    }

    public String getExtraMoney() {
        return extraMoney;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    public String getIsWin() {
        return isWin;
    }

    public void setWinMoney(String winMoney) {
        this.winMoney = winMoney;
    }

    public String getWinMoney() {
        return winMoney;
    }

    public void setSeedMoney(String seedMoney) {
        this.seedMoney = seedMoney;
    }

    public String getSeedMoney() {
        return seedMoney;
    }

    public void setTargetMoney(String targetMoney) {
        this.targetMoney = targetMoney;
    }

    public void setSaveMoney(String saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getSaveMoney() {
        return saveMoney;
    }

    public String getTargetMoney() {
        return targetMoney;
    }

    public void setExtraRemark(String extraRemark) {
        this.extraRemark = extraRemark;
    }

    public String getExtraRemark() {
        return extraRemark;
    }

    public Long[] getPlanIds() {
        return planIds;
    }

    public void setPlanIds(Long[] planIds) {
        this.planIds = planIds;
    }

    public List<Fx67llDortmundPlan> getNewPlans() {
        return newPlans;
    }

    public void setNewPlans(List<Fx67llDortmundPlan> newPlans) {
        this.newPlans = newPlans;
    }

    public String[] getPlanImageObjectKeys() {
        return planImageObjectKeys;
    }

    public void setPlanImageObjectKeys(String[] planImageObjectKeys) {
        this.planImageObjectKeys = planImageObjectKeys;
    }

    public List<Fx67llDortmundPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<Fx67llDortmundPlan> plans) {
        this.plans = plans;
    }

    public Integer getHiddenPlanCount() {
        return hiddenPlanCount;
    }

    public void setHiddenPlanCount(Integer hiddenPlanCount) {
        this.hiddenPlanCount = hiddenPlanCount;
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

    public Integer getPlanCount() {
        return planCount;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getHasPlan() {
        return hasPlan;
    }

    public void setHasPlan(String hasPlan) {
        this.hasPlan = hasPlan;
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
                .append("extraId", getExtraId())
                .append("extraMoney", getExtraMoney())
                .append("isWin", getIsWin())
                .append("winMoney", getWinMoney())
                .append("seedMoney", getSeedMoney())
                .append("saveMoney", getSaveMoney())
                .append("targetMoney", getTargetMoney())
                .append("extraRemark", getExtraRemark())
                .append("planIds", getPlanIds())
                .append("newPlans", getNewPlans())
                .append("planImageObjectKeys", getPlanImageObjectKeys())
                .append("plans", getPlans())
                .append("hiddenPlanCount", getHiddenPlanCount())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("planCount", getPlanCount())
                .append("planId", getPlanId())
                .append("hasPlan", getHasPlan())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
