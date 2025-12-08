package com.ruoyi.fx67ll.lottery.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 每日号码记录对象 fx67ll_lottery_log
 *
 * @author fx67ll
 * @date 2023-08-10
 */
public class Fx67llLotteryLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 号码日志主键
     */
    private Long lotteryId;

    /**
     * 当日购买号码
     */
    @Excel(name = "彩票期号")
    private String dateCode;

    /**
     * 当日购买号码
     */
    @Excel(name = "当日购买号码")
    private String recordNumber;

    /**
     * 当日固定追号
     */
    @Excel(name = "当日固定追号")
    private String chaseNumber;

    /**
     * 当日中奖号码
     */
    @Excel(name = "当日中奖号码")
    private String winningNumber;

    /**
     * 是否中奖（Y是 N否）
     */
    @Excel(name = "是否中奖", readConverterExp = "Y=是,N=否")
    private String isWin;

    /**
     * 中奖金额
     */
    @Excel(name = "中奖金额")
    private String winningPrice;

    /**
     * 当日购买的彩票类型（1大乐透 2双色球）
     */
    @Excel(name = "当日购买的彩票类型", readConverterExp = "1=大乐透,2=双色球")
    private Integer numberType;

    /**
     * 星期几（1周一 2周二 3周三 4周四 5周五 6周六 7周日）
     */
    @Excel(name = "星期几", readConverterExp = "1=周一,2=周二,3=周三,4=周四,5=周五,6=周六,7=周日")
    private Integer weekType;

    /**
     * 是否有追加购买（Y是 N否）
     */
    @Excel(name = "是否有追加购买", readConverterExp = "Y=是,N=否")
    private String hasMorePurchases;

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
     * 额外查询用字段：创建开始时间
     */
    private String beginCreateTime;

    /**
     * 额外查询用字段：创建结束时间
     */
    private String endCreateTime;

    /**
     * 额外查询用字段：更新开始时间
     */
    private String beginUpdateTime;

    /**
     * 额外查询用字段：更新结束时间
     */
    private String endUpdateTime;

    /**
     * 额外查询用字段：用于查询是否已经查询过中奖号码的记录（Y=只查有中奖号码记录数据，N=只查无中奖号码记录数据，null=不限）
     */
    private String hasWinningNumber;

    /**
     * 额外查询用字段：用于查询是否记录了期号
     */
    private String hasDateCode;

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    public String getDateCode() {
        return dateCode;
    }

    public void setLotteryId(Long lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Long getLotteryId() {
        return lotteryId;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setChaseNumber(String chaseNumber) {
        this.chaseNumber = chaseNumber;
    }

    public String getChaseNumber() {
        return chaseNumber;
    }

    public void setWinningNumber(String winningNumber) {
        this.winningNumber = winningNumber;
    }

    public String getWinningNumber() {
        return winningNumber;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    public String getIsWin() {
        return isWin;
    }

    public void setWinningPrice(String winningPrice) {
        this.winningPrice = winningPrice;
    }

    public String getWinningPrice() {
        return winningPrice;
    }

    public void setNumberType(Integer numberType) {
        this.numberType = numberType;
    }

    public Integer getNumberType() {
        return numberType;
    }

    public void setWeekType(Integer weekType) {
        this.weekType = weekType;
    }

    public Integer getWeekType() {
        return weekType;
    }

    public void setHasMorePurchases(String hasMorePurchases) {
        this.hasMorePurchases = hasMorePurchases;
    }

    public String getHasMorePurchases() {
        return hasMorePurchases;
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

    public String getHasWinningNumber() {
        return hasWinningNumber;
    }

    public void setHasWinningNumber(String hasWinningNumber) {
        this.hasWinningNumber = hasWinningNumber;
    }

    public String getHasDateCode() {
        return hasDateCode;
    }

    public void setHasDateCode(String hasDateCode) {
        this.hasDateCode = hasDateCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("lotteryId", getLotteryId())
                .append("dateCode", getDateCode())
                .append("recordNumber", getRecordNumber())
                .append("chaseNumber", getChaseNumber())
                .append("winningNumber", getWinningNumber())
                .append("isWin", getIsWin())
                .append("winningPrice", getWinningPrice())
                .append("numberType", getNumberType())
                .append("weekType", getWeekType())
                .append("hasMorePurchases", getHasMorePurchases())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .append("hasWinningNumber", getHasWinningNumber())
                .append("hasDateCode", getHasDateCode())
                .toString();
    }
}
