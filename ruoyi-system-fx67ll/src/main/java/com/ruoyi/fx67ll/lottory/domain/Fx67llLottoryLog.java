package com.ruoyi.fx67ll.lottory.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 每日号码记录对象 fx67ll_lottory_log
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public class Fx67llLottoryLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 号码日志主键 */
    private Long lottoryId;

    /** 当日购买号码 */
    @Excel(name = "当日购买号码")
    private String recordNumber;

    /** 当日固定追号 */
    @Excel(name = "当日固定追号")
    private String chaseNumber;

    /** 当日中奖号码 */
    @Excel(name = "当日中奖号码")
    private String winningNumber;

    /** 当日购买的彩票类型（1大乐透 2双色球） */
    @Excel(name = "当日购买的彩票类型", readConverterExp = "1=大乐透,2=双色球")
    private Integer numberType;

    /** 星期几（1周一 2周二 3周三 4周四 5周五 6周六 7周日） */
    @Excel(name = "星期几", readConverterExp = "1=周一,2=周二,3=周三,4=周四,5=周五,6=周六,7=周日")
    private Integer weekType;

    /** 是否有追加购买（Y是 N否） */
    @Excel(name = "是否有追加购买", readConverterExp = "Y=是,N=否")
    private String hasMorePurchases;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    private Long userId;

    public void setLottoryId(Long lottoryId) 
    {
        this.lottoryId = lottoryId;
    }

    public Long getLottoryId() 
    {
        return lottoryId;
    }
    public void setRecordNumber(String recordNumber) 
    {
        this.recordNumber = recordNumber;
    }

    public String getRecordNumber() 
    {
        return recordNumber;
    }
    public void setChaseNumber(String chaseNumber) 
    {
        this.chaseNumber = chaseNumber;
    }

    public String getChaseNumber() 
    {
        return chaseNumber;
    }
    public void setWinningNumber(String winningNumber) 
    {
        this.winningNumber = winningNumber;
    }

    public String getWinningNumber() 
    {
        return winningNumber;
    }
    public void setNumberType(Integer numberType) 
    {
        this.numberType = numberType;
    }

    public Integer getNumberType() 
    {
        return numberType;
    }
    public void setWeekType(Integer weekType) 
    {
        this.weekType = weekType;
    }

    public Integer getWeekType() 
    {
        return weekType;
    }
    public void setHasMorePurchases(String hasMorePurchases) 
    {
        this.hasMorePurchases = hasMorePurchases;
    }

    public String getHasMorePurchases() 
    {
        return hasMorePurchases;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("lottoryId", getLottoryId())
            .append("recordNumber", getRecordNumber())
            .append("chaseNumber", getChaseNumber())
            .append("winningNumber", getWinningNumber())
            .append("numberType", getNumberType())
            .append("weekType", getWeekType())
            .append("hasMorePurchases", getHasMorePurchases())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
