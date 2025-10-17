package com.ruoyi.fx67ll.mahjong.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 麻将室预约记录对象 fx67ll_mahjong_reservation_log
 *
 * @author ruoyi
 * @date 2025-10-16
 */
public class Fx67llMahjongReservationLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 麻将室预约记录主键
     */
    private Long mahjongReservationLogId;

    /**
     * 预约用户主键
     */
    @Excel(name = "预约用户主键")
    private Long userId;

    /**
     * 预约用户名
     */
    @Excel(name = "预约用户名")
    private String createBy;

    /**
     * 麻将室主键
     */
    @Excel(name = "麻将室主键")
    private Long mahjongRoomId;

    /**
     * 麻将室名称
     */
    @Excel(name = "麻将室名称")
    private String mahjongRoomName;


    /**
     * 预约开始时间（含日期和小时）
     */
    @Excel(name = "预约开始时间", readConverterExp = "含=日期和小时")
    private Date reservationStartTime;

    /**
     * 预约结束时间（含日期和小时）
     */
    @Excel(name = "预约结束时间", readConverterExp = "含=日期和小时")
    private Date reservationEndTime;

    /**
     * 预约联系方式（电话）
     */
    @Excel(name = "预约联系方式", readConverterExp = "电=话")
    private String reservationContact;

    /**
     * 预留：费用金额（未来存储实际费用）
     */
    @Excel(name = "预留：费用金额", readConverterExp = "未=来存储实际费用")
    private BigDecimal reservationAmount;

    /**
     * 预约状态（0正常 1取消 2完成）
     */
    @Excel(name = "预约状态", readConverterExp = "0=正常,1=取消,2=完成")
    private String reservationStatus;

    /**
     * 麻将室预约记录备注
     */
    @Excel(name = "麻将室预约记录备注")
    private String reservationRemark;

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

    public void setMahjongReservationLogId(Long mahjongReservationLogId) {
        this.mahjongReservationLogId = mahjongReservationLogId;
    }

    public Long getMahjongReservationLogId() {
        return mahjongReservationLogId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setMahjongRoomId(Long mahjongRoomId) {
        this.mahjongRoomId = mahjongRoomId;
    }

    public Long getMahjongRoomId() {
        return mahjongRoomId;
    }

    public void setMahjongRoomName(String mahjongRoomName) {
        this.mahjongRoomName = mahjongRoomName;
    }

    public String getMahjongRoomName() {
        return mahjongRoomName;
    }

    public void setReservationStartTime(Date reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public Date getReservationStartTime() {
        return reservationStartTime;
    }

    public void setReservationEndTime(Date reservationEndTime) {
        this.reservationEndTime = reservationEndTime;
    }

    public Date getReservationEndTime() {
        return reservationEndTime;
    }

    public void setReservationContact(String reservationContact) {
        this.reservationContact = reservationContact;
    }

    public String getReservationContact() {
        return reservationContact;
    }

    public void setReservationAmount(BigDecimal reservationAmount) {
        this.reservationAmount = reservationAmount;
    }

    public BigDecimal getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationRemark(String reservationRemark) {
        this.reservationRemark = reservationRemark;
    }

    public String getReservationRemark() {
        return reservationRemark;
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
                .append("mahjongReservationLogId", getMahjongReservationLogId())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("mahjongRoomId", getMahjongRoomId())
                .append("mahjongRoomName", getMahjongRoomName())
                .append("reservationStartTime", getReservationStartTime())
                .append("reservationEndTime", getReservationEndTime())
                .append("reservationContact", getReservationContact())
                .append("reservationAmount", getReservationAmount())
                .append("reservationStatus", getReservationStatus())
                .append("reservationRemark", getReservationRemark())
                .append("delFlag", getDelFlag())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
