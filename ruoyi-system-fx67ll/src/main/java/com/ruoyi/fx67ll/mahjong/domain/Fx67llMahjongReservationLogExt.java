package com.ruoyi.fx67ll.mahjong.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 麻将室预约记录扩展对象（含用户表关联字段）
 * 用于返回“预约记录+用户名+用户联系方式”的组合数据
 *
 * @author fx67ll
 * @date 2025-10-31
 */
public class Fx67llMahjongReservationLogExt extends Fx67llMahjongReservationLog {
    private static final long serialVersionUID = 1L;

    /**
     * 预约用户姓名（关联sys_user表user_name）
     */
    private String userName;

    /**
     * 预约用户联系方式（关联sys_user表phonenumber，假设用户表联系方式字段为phonenumber）
     */
    private String phonenumber;

    // Getter & Setter
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    // 重写toString，包含父类字段和新增字段
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .appendSuper(super.toString()) // 继承父类所有字段的toString
                .append("userName", getUserName())
                .append("phonenumber", getPhonenumber())
                .toString();
    }
}