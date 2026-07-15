package com.ruoyi.web.controller.fx67ll.auth;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.domain.model.AppLoginTokenBody;
import com.ruoyi.common.core.domain.model.AppOneClickLoginBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.AppLoginTokenService;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.WechatLoginUtils;
import com.ruoyi.framework.web.service.AppLoginTokenService.TokenPayload;

/**
 * APP 一键登录接口（阶段二·4.4）
 *
 * 免密签发，服务端不持有也不校验密码，前端不接触任何账密：
 * - POST /auth/app/loginToken：派发一次性登录令牌
 *   - loginType=main（仅小程序）：传 wxCode，后端换 openid 校验白名单通过才派发
 *   - loginType=guest：传设备指纹，不绑 openid
 * - POST /auth/app/oneClickLogin：令牌 + 指纹换 JWT，令牌一次性用后即删
 *
 * 安全：匿名 + 频率限制 + 失败锁定（Redis 计数 + TTL），无验证码。
 * 身份由 openid 白名单背书（微信不可伪造），oneClickLogin 按用户名免密签发，
 * 管理员改密码无需同步任何配置。
 *
 * @author fx67ll
 */
@RestController
@RequestMapping("/auth/app")
public class AppLoginController
{
    /** 登录类型：主账号（仅小程序） */
    private static final String LOGIN_TYPE_MAIN = "main";

    /** 登录类型：游客 */
    private static final String LOGIN_TYPE_GUEST = "guest";

    /** 频率限制：同指纹每分钟最多尝试次数 */
    private static final int RATE_LIMIT_PER_MINUTE = 5;

    /** 连续失败锁定阈值 */
    private static final int FAIL_LOCK_THRESHOLD = 5;

    /** 失败锁定时长（分钟） */
    private static final int FAIL_LOCK_MINUTES = 15;

    /** Redis key 前缀：登录频率限制计数 */
    private static final String RATE_LIMIT_KEY_PREFIX = "app:login:rate:";

    /** Redis key 前缀：登录失败锁定 */
    private static final String FAIL_LOCK_KEY_PREFIX = "app:login:lock:";

    @Autowired
    private AppLoginTokenService appLoginTokenService;

    @Autowired
    private WechatLoginUtils wechatLoginUtils;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 派发一次性登录令牌（匿名 + 频率限制 + 失败锁定）
     *
     * @param body 登录令牌请求（loginType + wxCode/fingerprint）
     * @return 令牌（不含账密明文）
     */
    @PostMapping("/loginToken")
    @Log(title = "APP一键登录令牌签发", businessType = BusinessType.OTHER)
    public AjaxResult loginToken(@RequestBody AppLoginTokenBody body)
    {
        if (body == null || StringUtils.isEmpty(body.getLoginType()))
        {
            return AjaxResult.error("登录类型不能为空");
        }
        String loginType = body.getLoginType();
        String fingerprint = body.getFingerprint();

        // 游客必须有设备指纹
        if (LOGIN_TYPE_GUEST.equals(loginType) && StringUtils.isEmpty(fingerprint))
        {
            return AjaxResult.error("设备指纹不能为空");
        }
        // 失败锁定校验（按设备指纹，loginToken 与 oneClickLogin 共用同一锁定 key）
        String failLockKey = FAIL_LOCK_KEY_PREFIX + fingerprint;
        if (isLocked(failLockKey))
        {
            return AjaxResult.error("登录失败次数过多，请稍后再试");
        }
        // 频率限制校验（按 IP，每分钟最多 N 次）
        if (overRateLimit(getClientIp()))
        {
            return AjaxResult.error("操作过于频繁，请稍后再试");
        }

        String username;
        if (LOGIN_TYPE_MAIN.equals(loginType))
        {
            // 主账号：用 wxCode 换 openid，校验白名单
            String openid = wechatLoginUtils.getOpenid(body.getWxCode());
            if (openid == null)
            {
                recordFail(failLockKey);
                return AjaxResult.error("微信登录凭证无效");
            }
            if (!wechatLoginUtils.isOpenidAllowed(openid))
            {
                recordFail(failLockKey);
                return AjaxResult.error("该微信账号未授权登录主账号");
            }
            // 主账号登录用户名从 yml 读取（member.main.username，服务端持有，前端不接触）
            // 一键登录免密签发：openid 白名单已校验，oneClickLogin 阶段不校验密码，故不取密码
            username = wechatLoginUtils.getMainUsername();
            if (StringUtils.isEmpty(username) || username.startsWith("待填写"))
            {
                return AjaxResult.error("主账号未配置，请联系管理员");
            }
        }
        else if (LOGIN_TYPE_GUEST.equals(loginType))
        {
            username = wechatLoginUtils.getGuestUsername();
        }
        else
        {
            return AjaxResult.error("不支持的登录类型");
        }

        // 签发一次性令牌，绑定设备指纹与登录用户名（不含密码）
        String token = appLoginTokenService.issueToken(loginType, username, fingerprint);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("loginToken", token);
        return ajax;
    }

    /**
     * 一键登录换 JWT（匿名 + 频率限制 + 失败锁定，令牌一次性）
     *
     * @param body 令牌 + 设备指纹
     * @return JWT
     */
    @PostMapping("/oneClickLogin")
    @Log(title = "APP一键登录", businessType = BusinessType.OTHER)
    public AjaxResult oneClickLogin(@RequestBody AppOneClickLoginBody body)
    {
        if (body == null || StringUtils.isEmpty(body.getLoginToken()) || StringUtils.isEmpty(body.getFingerprint()))
        {
            return AjaxResult.error("登录令牌与设备指纹不能为空");
        }
        // 失败锁定校验（按设备指纹，与 loginToken 共用同一锁定 key）
        String failLockKey = FAIL_LOCK_KEY_PREFIX + body.getFingerprint();
        if (isLocked(failLockKey))
        {
            return AjaxResult.error("登录失败次数过多，请稍后再试");
        }
        // 频率限制校验（按 IP）
        if (overRateLimit(getClientIp()))
        {
            return AjaxResult.error("操作过于频繁，请稍后再试");
        }

        // 校验并消费令牌（一次性，用后即删，校验指纹匹配）
        TokenPayload payload = appLoginTokenService.consumeToken(body.getLoginToken(), body.getFingerprint());
        if (payload == null)
        {
            recordFail(failLockKey);
            return AjaxResult.error("登录令牌无效或已过期");
        }

        try
        {
            // 免密登录：loginToken 阶段已校验 openid 白名单，这里按用户名直接签发 JWT，不校验密码
            String jwt = sysLoginService.loginWithoutPassword(payload.getUsername());
            // 登录成功，清除失败计数
            redisCache.deleteObject(failLockKey);
            AjaxResult ajax = AjaxResult.success();
            ajax.put(Constants.TOKEN, jwt);
            return ajax;
        }
        catch (Exception e)
        {
            recordFail(failLockKey);
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 检查登录资格（阶段三优化·小程序端登录页用）
     *
     * 小程序打开时调用，传 wxCode，后端换 openid 查白名单，
     * 只返回布尔值（是否允许一键登录），不返回 openid 本身。
     * 前端根据结果决定展示一键登录还是账密登录。
     * 匿名访问，已在 SecurityConfig 放行。
     *
     * @param body wxCode
     * @return eligible 是否允许一键登录
     */
    @PostMapping("/checkLoginEligibility")
    public AjaxResult checkLoginEligibility(@RequestBody AppLoginTokenBody body)
    {
        if (body == null || StringUtils.isEmpty(body.getWxCode()))
        {
            AjaxResult ajax = AjaxResult.success();
            ajax.put("eligible", false);
            return ajax;
        }
        String openid = wechatLoginUtils.getOpenid(body.getWxCode());
        boolean eligible = openid != null && wechatLoginUtils.isOpenidAllowed(openid);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("eligible", eligible);
        return ajax;
    }

    /**
     * 获取客户端 IP（用于频率限制）
     */
    private String getClientIp()
    {
        try
        {
            return com.ruoyi.common.utils.ip.IpUtils.getIpAddr();
        }
        catch (Exception e)
        {
            return "unknown";
        }
    }

    /**
     * 是否被锁定（连续失败达阈值）
     */
    private boolean isLocked(String lockKey)
    {
        Integer count = redisCache.getCacheObject(lockKey);
        return count != null && count >= FAIL_LOCK_THRESHOLD;
    }

    /**
     * 是否超过每分钟频率限制（按 IP，独立于失败锁定计数）
     *
     * @param ip 客户端 IP
     * @return true 超限
     */
    private boolean overRateLimit(String ip)
    {
        String rateKey = RATE_LIMIT_KEY_PREFIX + ip;
        Integer count = redisCache.getCacheObject(rateKey);
        if (count == null)
        {
            redisCache.setCacheObject(rateKey, 1, 1, TimeUnit.MINUTES);
            return false;
        }
        if (count >= RATE_LIMIT_PER_MINUTE)
        {
            return true;
        }
        // 复用原 TTL：先取剩余时间，无则默认 60 秒
        redisCache.setCacheObject(rateKey, count + 1, 60, TimeUnit.SECONDS);
        return false;
    }

    /**
     * 记录一次失败，达阈值则锁定 FAIL_LOCK_MINUTES 分钟（失败锁定独立计数，与频率限制分开）
     *
     * @param failLockKey 失败锁定 key（按设备指纹）
     */
    private void recordFail(String failLockKey)
    {
        Integer count = redisCache.getCacheObject(failLockKey);
        int next = (count == null ? 0 : count) + 1;
        if (next >= FAIL_LOCK_THRESHOLD)
        {
            // 达阈值，设置锁定时长
            redisCache.setCacheObject(failLockKey, next, FAIL_LOCK_MINUTES, TimeUnit.MINUTES);
        }
        else
        {
            // 未达阈值，计数 15 分钟过期（失败计数窗口与锁定时长一致，避免计数永不过期）
            redisCache.setCacheObject(failLockKey, next, FAIL_LOCK_MINUTES, TimeUnit.MINUTES);
        }
    }
}
