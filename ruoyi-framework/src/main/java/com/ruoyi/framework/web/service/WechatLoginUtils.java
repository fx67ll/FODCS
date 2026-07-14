package com.ruoyi.framework.web.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.config.SecretKeyConfig;

/**
 * 微信小程序登录工具（阶段二·4.4）
 *
 * 后端调用微信 jscode2session 接口，用小程序 AppID/AppSecret 换取 openid。
 * 由后端发起请求（不经小程序前端），免配 request 合法域名。
 *
 * openid 由微信服务器背书、不可伪造，用于主账号一键登录的身份校验。
 *
 * 主账号账密从 yml 读取（member.main.username/password，服务端持有，前端不接触），
 * 游客账密也从 yml 读取（member.guest.username/password）。
 *
 * @author fx67ll
 */
@Component
public class WechatLoginUtils
{
    private static final Logger log = LoggerFactory.getLogger(WechatLoginUtils.class);

    /** 微信 jscode2session 接口地址 */
    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    /**
     * 用 wx.login 的 code 换取 openid
     *
     * @param wxCode 小程序 wx.login() 返回的 code
     * @return openid，失败返回 null
     */
    public String getOpenid(String wxCode)
    {
        if (wxCode == null || wxCode.isEmpty())
        {
            return null;
        }
        String appid = secretKeyConfig.getWechat().getMiniapp().getAppid();
        String secret = secretKeyConfig.getWechat().getMiniapp().getSecret();
        if (appid == null || appid.isEmpty() || secret == null || secret.isEmpty()
                || appid.startsWith("待填写"))
        {
            log.warn("[微信登录] 小程序 AppID/AppSecret 未配置，无法换取 openid");
            return null;
        }
        String url = JSCODE2SESSION_URL + "?appid=" + appid + "&secret=" + secret
                + "&js_code=" + wxCode + "&grant_type=authorization_code";
        try
        {
            String result = HttpUtils.sendGet(url);
            JSONObject json = JSON.parseObject(result);
            if (json == null)
            {
                log.error("[微信登录] jscode2session 返回空，code={}", wxCode);
                return null;
            }
            // 微信返回 errcode 非 0 表示出错
            Integer errcode = json.getInteger("errcode");
            if (errcode != null && errcode != 0)
            {
                log.error("[微信登录] jscode2session 失败，errcode={} errmsg={}", errcode, json.getString("errmsg"));
                return null;
            }
            return json.getString("openid");
        }
        catch (Exception e)
        {
            log.error("[微信登录] 调用 jscode2session 异常，code={}", wxCode, e);
            return null;
        }
    }

    /**
     * 判断 openid 是否在主账号白名单内
     *
     * @param openid 待校验的 openid
     * @return true 在白名单内
     */
    public boolean isOpenidAllowed(String openid)
    {
        if (openid == null || openid.isEmpty())
        {
            return false;
        }
        return secretKeyConfig.getMember().getMain().getOpenids().contains(openid);
    }

    /**
     * 获取主账号登录用户名（从 yml member.main.username 读取，服务端持有）
     */
    public String getMainUsername()
    {
        return secretKeyConfig.getMember().getMain().getUsername();
    }

    /**
     * 获取主账号登录密码（从 yml member.main.password 读取，服务端持有）
     */
    public String getMainPassword()
    {
        return secretKeyConfig.getMember().getMain().getPassword();
    }

    /**
     * 获取游客登录用户名（服务端持有，不下发前端）
     */
    public String getGuestUsername()
    {
        return secretKeyConfig.getMember().getGuest().getUsername();
    }

    /**
     * 获取游客登录密码（服务端持有，不下发前端）
     */
    public String getGuestPassword()
    {
        return secretKeyConfig.getMember().getGuest().getPassword();
    }
}
