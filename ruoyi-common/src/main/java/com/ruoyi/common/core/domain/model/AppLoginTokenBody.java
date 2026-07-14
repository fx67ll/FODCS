package com.ruoyi.common.core.domain.model;

/**
 * APP 一键登录令牌签发请求体（阶段二·4.4）
 *
 * 前端调 /auth/app/loginToken 时传入：
 * - loginType=main：必传 wxCode（小程序 wx.login 的 code），后端换 openid 校验白名单
 * - loginType=guest：传设备指纹，不绑 openid
 *
 * @author fx67ll
 */
public class AppLoginTokenBody
{
    /** 登录类型 main（主账号，仅小程序）/ guest（游客） */
    private String loginType;

    /** 小程序 wx.login() 返回的 code（loginType=main 时必传） */
    private String wxCode;

    /** 设备指纹（loginType=guest 时必传；main 时建议一并传入绑定令牌） */
    private String fingerprint;

    public String getLoginType()
    {
        return loginType;
    }

    public void setLoginType(String loginType)
    {
        this.loginType = loginType;
    }

    public String getWxCode()
    {
        return wxCode;
    }

    public void setWxCode(String wxCode)
    {
        this.wxCode = wxCode;
    }

    public String getFingerprint()
    {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint)
    {
        this.fingerprint = fingerprint;
    }
}
