package com.ruoyi.fx67ll.dortmund.domain;

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
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

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
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
