package com.ruoyi.fx67ll.punch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 打卡记录对象 fx67ll_punch_log
 *
 * @author fx67ll
 * @date 2023-11-29
 */
public class Fx67llPunchLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 打卡记录主键
     */
    private Long punchId;

    /**
     * 打卡类型（1代表上班 2代表下班）
     */
    @Excel(name = "打卡类型", readConverterExp = "1=代表上班,2=代表下班")
    private String punchType;

    /**
     * 打卡记录备注
     */
    @Excel(name = "打卡记录备注")
    private String punchRemark;

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

    /**
     * 更新开始时间
     */
    private String beginUpdateTime;

    /**
     * 更新结束时间
     */
    private String endUpdateTime;

    /**
     * 打卡统计月份
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String punchMonth;

    public void setPunchId(Long punchId) {
        this.punchId = punchId;
    }

    public Long getPunchId() {
        return punchId;
    }

    public void setPunchType(String punchType) {
        this.punchType = punchType;
    }

    public String getPunchType() {
        return punchType;
    }

    public void setPunchRemark(String punchRemark) {
        this.punchRemark = punchRemark;
    }

    public String getPunchRemark() {
        return punchRemark;
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

    public String getPunchMonth() {
        return punchMonth;
    }

    public void setPunchMonth(String punchMonth) {
        this.punchMonth = punchMonth;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("punchId", getPunchId())
                .append("punchType", getPunchType())
                .append("punchRemark", getPunchRemark())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
