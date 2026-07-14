package com.ruoyi.web.controller.fx67ll.secret;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.SecretKeyConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;
import com.ruoyi.fx67ll.secret.service.IFx67llSecretKeyService;
import com.ruoyi.framework.web.service.AppCredentialTokenService;
import com.ruoyi.framework.web.service.AppCredentialTokenService.CredentialPayload;

/**
 * APP 第三方凭据下发接口（阶段二·4.5）
 *
 * 第三方凭据维持前端直连，但用短时效令牌方案把泄露面压到最小：
 * - POST /secret/key/credentialForApp：登录态 + 权限 + 频率限制。
 *   服务端用 dbEncKey 解密数据库密文得明文 AK/SK → 用 appSecretKey 重加密 → 返回密文 + 短时效一次性令牌
 * - POST /secret/key/decryptForApp：登录态 + 一次性令牌校验。
 *   令牌换明文凭据，令牌单次有效，前端用完即弃
 *
 * credentialType 映射到 secret 表的键：lotteryReward → qryLotteryRewardAppId/AppSecret；ocr → ocrPubKey/SecKey。
 * 服务端记录每次凭据下发（用户、时间、类型、指纹），供审计滥用。
 *
 * @author fx67ll
 */
@RestController
@RequestMapping("/secret/key")
public class AppCredentialController
{
    /** credentialType → secret 表键名对（AppId 键 + AppSecret 键） */
    private static final Map<String, String[]> CREDENTIAL_TYPE_MAP = new HashMap<>();

    static
    {
        // 中奖查询凭据
        CREDENTIAL_TYPE_MAP.put("lotteryReward", new String[] { "qryLotteryRewardAppId", "qryLotteryRewardAppSecret" });
        // 百度 OCR 凭据
        CREDENTIAL_TYPE_MAP.put("ocr", new String[] { "ocrPubKey", "ocrSecKey" });
    }

    @Autowired
    private IFx67llSecretKeyService fx67llSecretKeyService;

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    @Autowired
    private AppCredentialTokenService appCredentialTokenService;

    /**
     * 下发第三方凭据密文 + 短时效一次性令牌
     *
     * 登录态 + secret:key:use 权限。secret 表中的第三方凭据仅对 fx67ll 自身开放，
     * 不对其他登录用户开放，故需细粒度权限标识。服务端解密数据库密文得明文，
     * 用 appSecretKey 重加密返回，附带一次性令牌（5 分钟），前端凭令牌调 decryptForApp 换明文。
     *
     * 后期新增第三方 API 走后端服务调用 + yml 配置，不再进 secret 表；本次不动存量是因老逻辑完善、变动大。
     *
     * @param body credentialType + fingerprint
     * @return 密文（cipherCredential）+ 一次性令牌（credentialToken）
     */
    @PreAuthorize("@ss.hasPermi('secret:key:use')")
    @Log(title = "下发第三方凭据", businessType = BusinessType.OTHER)
    @PostMapping("/credentialForApp")
    public AjaxResult credentialForApp(@RequestBody CredentialRequest body)
    {
        if (body == null || StringUtils.isEmpty(body.getCredentialType()))
        {
            return AjaxResult.error("凭据类型不能为空");
        }
        String credentialType = body.getCredentialType();
        String[] keyNames = CREDENTIAL_TYPE_MAP.get(credentialType);
        if (keyNames == null)
        {
            return AjaxResult.error("不支持的凭据类型");
        }

        // 从数据库读取并解密明文凭据（Service 层已解密）
        Fx67llSecretKey idKey = fx67llSecretKeyService.selectFx67llSecretKeyBySecretKeyForApp(keyNames[0]);
        Fx67llSecretKey secretKeyRecord = fx67llSecretKeyService.selectFx67llSecretKeyBySecretKeyForApp(keyNames[1]);
        if (idKey == null || secretKeyRecord == null)
        {
            return AjaxResult.error("凭据未配置");
        }

        // 组装明文凭据 JSON
        Map<String, String> plain = new HashMap<>(2);
        plain.put("appId", idKey.getSecretValue());
        plain.put("appSecret", secretKeyRecord.getSecretValue());
        String plainJson = JSON.toJSONString(plain);

        // 用 appSecretKey 重加密成密文（前端拿密文，拿不到明文）
        String appSecretKey = secretKeyConfig.getSecret().getAppSecretKey();
        String cipher = com.ruoyi.common.utils.CryptoUtils.encrypt(plainJson, appSecretKey);

        // 签发一次性令牌，存明文凭据，绑定用户 + 指纹
        Long userId = SecurityUtils.getUserId();
        String token = appCredentialTokenService.issueToken(credentialType, plainJson, userId, body.getFingerprint());

        AjaxResult ajax = AjaxResult.success();
        ajax.put("cipherCredential", cipher);
        ajax.put("credentialToken", token);
        // 审计信息由 @Log 注解记录，含操作用户、类型
        return ajax;
    }

    /**
     * 一次性令牌换取明文凭据
     *
     * 登录态 + 令牌校验（单次有效，绑定用户 + 指纹）。前端拿到明文后立即直连第三方，用完清除。
     *
     * @param body credentialToken + fingerprint
     * @return 明文凭据（plainCredential，JSON 字符串）
     */
    @PreAuthorize("isAuthenticated()")
    @Log(title = "解密第三方凭据", businessType = BusinessType.OTHER)
    @PostMapping("/decryptForApp")
    public AjaxResult decryptForApp(@RequestBody CredentialRequest body)
    {
        if (body == null || StringUtils.isEmpty(body.getCredentialToken()))
        {
            return AjaxResult.error("凭据令牌不能为空");
        }
        Long userId = SecurityUtils.getUserId();
        // 校验并消费令牌（一次性，用后即删，校验用户 + 指纹）
        CredentialPayload payload = appCredentialTokenService.consumeToken(body.getCredentialToken(), userId, body.getFingerprint());
        if (payload == null)
        {
            return AjaxResult.error("凭据令牌无效或已过期");
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("plainCredential", payload.getCredential());
        return ajax;
    }

    /**
     * 凭据请求体
     */
    public static class CredentialRequest
    {
        /** 凭据类型（lotteryReward / ocr），credentialForApp 必传 */
        private String credentialType;

        /** 一次性凭据令牌，decryptForApp 必传 */
        private String credentialToken;

        /** 设备指纹（校验令牌绑定） */
        private String fingerprint;

        public String getCredentialType()
        {
            return credentialType;
        }

        public void setCredentialType(String credentialType)
        {
            this.credentialType = credentialType;
        }

        public String getCredentialToken()
        {
            return credentialToken;
        }

        public void setCredentialToken(String credentialToken)
        {
            this.credentialToken = credentialToken;
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
}
