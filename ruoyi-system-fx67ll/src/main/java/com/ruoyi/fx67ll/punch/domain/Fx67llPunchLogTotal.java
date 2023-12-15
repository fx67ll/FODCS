package com.ruoyi.fx67ll.punch.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 打卡记录工时统计对象
 *
 * @author fx67ll
 * @date 2023-12-29
 */
public class Fx67llPunchLogTotal {
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
     * 当月打卡总工时
     */
    private Double totalWorkHours;
    private Double totalWorkMinutes;
    private Double totalWorkSeconds;

    /**
     * 当月日均工时
     */
    private Integer totalPunchDays;
    private Integer totalWorkDays;
    private Double workHoursPerDay;

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

    public Double getTotalWorkHours() {
        return totalWorkHours;
    }

    public void setTotalWorkHours(Double totalWorkHours) {
        this.totalWorkHours = totalWorkHours;
    }

    public Double getTotalWorkMinutes() {
        return totalWorkMinutes;
    }

    public void setTotalWorkMinutes(Double totalWorkMinutes) {
        this.totalWorkMinutes = totalWorkMinutes;
    }

    public Double getTotalWorkSeconds() {
        return totalWorkSeconds;
    }

    public void setTotalWorkSeconds(Double totalWorkSeconds) {
        this.totalWorkSeconds = totalWorkSeconds;
    }

    public Integer getTotalPunchDays() {
        return totalPunchDays;
    }

    public void setTotalPunchDays(Integer totalPunchDays) {
        this.totalPunchDays = totalPunchDays;
    }

    public Integer getTotalWorkDays() {
        return totalWorkDays;
    }

    public void setTotalWorkDays(Integer totalWorkDays) {
        this.totalWorkDays = totalWorkDays;
    }

    public Double getWorkHoursPerDay() {
        return workHoursPerDay;
    }

    public void setWorkHoursPerDay(Double workHoursPerDay) {
        this.workHoursPerDay = workHoursPerDay;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("punchUser", getPunchUser())
                .append("punchMonth", getPunchMonth())
                .append("totalWorkHours", getTotalWorkHours())
                .append("totalWorkMinutes", getTotalWorkMinutes())
                .append("totalWorkSeconds", getTotalWorkSeconds())
                .append("totalPunchDays", getTotalPunchDays())
                .append("totalWorkDays", getTotalWorkDays())
                .append("workHoursPerDay", getWorkHoursPerDay())
                .toString();
    }
}
