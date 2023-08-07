package com.ruoyi.fx67ll.lottery.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 固定追号配置对象 fx67ll_lottery_chase
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public class Fx67llLotteryChase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 固定追号主键 */
    private Long chaseId;

    /** 每日固定追号 */
    @Excel(name = "每日固定追号")
    private String chaseNumber;

    /** 固定追号的彩票类型（1大乐透 2双色球） */
    @Excel(name = "固定追号的彩票类型", readConverterExp = "1=大乐透,2=双色球")
    private Integer numberType;

    /** 星期几的固定追号（1周一 2周二 3周三 4周四 5周五 6周六 7周日） */
    @Excel(name = "星期几的固定追号", readConverterExp = "1=周一,2=周二,3=周三,4=周四,5=周五,6=周六,7=周日")
    private Integer weekType;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    private Long userId;

    public void setChaseId(Long chaseId) 
    {
        this.chaseId = chaseId;
    }

    public Long getChaseId() 
    {
        return chaseId;
    }
    public void setChaseNumber(String chaseNumber) 
    {
        this.chaseNumber = chaseNumber;
    }

    public String getChaseNumber() 
    {
        return chaseNumber;
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
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
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
            .append("chaseId", getChaseId())
            .append("chaseNumber", getChaseNumber())
            .append("numberType", getNumberType())
            .append("weekType", getWeekType())
            .append("sort", getSort())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
