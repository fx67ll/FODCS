package com.ruoyi.fx67ll.punch.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 打卡记录工时统计对象
 *
 * @author fx67ll
 * @date 2023-12-29
 */
public class Fx67llPunchLogLost {
    private static final long serialVersionUID = 1L;

    /**
     * 打卡人用户名
     */
    private String punchUser;

    /**
     * 打卡时长统计的月份
     */
    private String punchMonth;

    /**
     * 当月缺卡记录的日期
     */
    private String punchDay;

    /**
     * 当月缺卡记录的类型
     */
    private String lostPunchType;


    public String getPunchUser() {
        return punchUser;
    }

    public void setPunchUser(String punchUser) {
        this.punchUser = punchUser;
    }

    public String getPunchMonth() {
        return punchMonth;
    }

    public void setPunchMonth(String punchMonth) {
        this.punchMonth = punchMonth;
    }

    public String getPunchDay() {
        return punchDay;
    }

    public void setPunchDay(String punchDay) {
        this.punchDay = punchDay;
    }

    public String getLostPunchType() {
        return lostPunchType;
    }

    public void setLostPunchType(String lostPunchType) {
        this.lostPunchType = lostPunchType;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("punchUser", getPunchUser())
                .append("punchMonth", getPunchMonth())
                .append("punchDay", getPunchDay())
                .append("lostPunchType", getLostPunchType())
                .toString();
    }
}
