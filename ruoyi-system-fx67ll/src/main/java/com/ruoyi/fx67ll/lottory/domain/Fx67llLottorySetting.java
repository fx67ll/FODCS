package com.ruoyi.fx67ll.lottory.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 固定追号配置对象 fx67ll_lottory_setting
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public class Fx67llLottorySetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 个人彩票生成配置 */
    @Excel(name = "个人彩票生成配置")
    private String chaseNumber;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setChaseNumber(String chaseNumber) 
    {
        this.chaseNumber = chaseNumber;
    }

    public String getChaseNumber() 
    {
        return chaseNumber;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("chaseNumber", getChaseNumber())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
