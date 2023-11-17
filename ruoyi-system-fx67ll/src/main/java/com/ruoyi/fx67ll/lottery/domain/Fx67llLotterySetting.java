package com.ruoyi.fx67ll.lottery.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 固定追号配置对象 fx67ll_lottery_setting
 *
 * @author fx67ll
 * @date 2023-08-07
 */
public class Fx67llLotterySetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 生成配置主键
     */
    private Long settingId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 个人彩票生成配置
     */
    @Excel(name = "个人彩票生成配置")
    private String lotterySetting;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

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

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setLotterySetting(String lotterySetting) {
        this.lotterySetting = lotterySetting;
    }

    public String getLotterySetting() {
        return lotterySetting;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
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
                .append("settingId", getSettingId())
                .append("userId", getUserId())
                .append("lotterySetting", getLotterySetting())
                .append("delFlag", getDelFlag())
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
