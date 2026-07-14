package com.ruoyi.common.core.domain.model;

/**
 * APP 一键登录请求体（阶段二·4.4）
 *
 * 前端调 /auth/app/oneClickLogin 时传入登录令牌 + 设备指纹，换取 JWT。
 *
 * @author fx67ll
 */
public class AppOneClickLoginBody
{
    /** 一次性登录令牌（由 /auth/app/loginToken 派发） */
    private String loginToken;

    /** 设备指纹（须与签发令牌时一致） */
    private String fingerprint;

    public String getLoginToken()
    {
        return loginToken;
    }

    public void setLoginToken(String loginToken)
    {
        this.loginToken = loginToken;
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
