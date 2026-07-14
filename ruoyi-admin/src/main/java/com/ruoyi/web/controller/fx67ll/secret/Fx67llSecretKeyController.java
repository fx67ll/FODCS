package com.ruoyi.web.controller.fx67ll.secret;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.TransferCryptoUtils;
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;
import com.ruoyi.fx67ll.secret.service.IFx67llSecretKeyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.web.service.TransferKeyService;

/**
 * 秘钥配置Controller
 *
 * @author fx67ll
 * @date 2023-08-30
 */
@RestController
@RequestMapping("/secret/key")
public class Fx67llSecretKeyController extends BaseController {
    /**
     * 非敏感键白名单：功能开关等，匿名可返回明文
     */
    private static final Set<String> NON_SENSITIVE_KEYS = new HashSet<>(Arrays.asList("isNeedWaiKuai"));

    @Autowired
    private IFx67llSecretKeyService fx67llSecretKeyService;

    @Autowired
    private TransferKeyService transferKeyService;

    /**
     * 查询秘钥配置列表
     *
     * secret_value 直接返回数据库密文（不下发明文），防抓包批量泄露。
     * 明文仅在点击修改/查看时通过 transferKey 解密流程获取。
     * cryptoSaltKey 保留明文（吉祥物密钥，迁移期间可见）。
     */
    @PreAuthorize("@ss.hasPermi('secret:key:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llSecretKey fx67llSecretKey) {
        startPage();
        List<Fx67llSecretKey> list = fx67llSecretKeyService.selectFx67llSecretKeyListMasked(fx67llSecretKey);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 查询秘钥配置（鉴权分级，v2.7 + v3.4 收紧）
     *
     * 按 secretKey 粒度分级，精确匹配（不再 LIKE 模糊匹配）：
     * - 非敏感键（isNeedWaiKuai 等功能开关）：匿名返回明文
     * - 其余敏感键（含 loginName/loginPassword、OCR AK/SK、第三方 AppId/AppSecret 等）：要求登录态才返回，匿名返回空
     *
     * cryptoSaltKey 一律不返回（吉祥物密钥不下发）。
     *
     * v3.4 收紧：loginName/loginPassword 从"登录前置键匿名返回"降为"敏感键要求登录态"。
     * 阶段三 FODCA 登录页改一键登录后，loginName/loginPassword 匿名通道已无调用方，收紧不影响功能。
     */
    @GetMapping("/getSecretKeyConfigForApp")
    public TableDataInfo getSecretKeyConfigForApp(Fx67llSecretKey fx67llSecretKey) {
        String secretKey = fx67llSecretKey.getSecretKey();
        // 无 secretKey 或为 cryptoSaltKey：返回空
        if (secretKey == null || secretKey.isEmpty() || "cryptoSaltKey".equals(secretKey)) {
            return getDataTable(new ArrayList<>());
        }
        // 非敏感键：匿名可取
        if (NON_SENSITIVE_KEYS.contains(secretKey)) {
            Fx67llSecretKey record = fx67llSecretKeyService.selectFx67llSecretKeyBySecretKeyForApp(secretKey);
            return getDataTable(record == null ? new ArrayList<>() : Collections.singletonList(record));
        }
        // 其余敏感键：要求登录态，匿名返回空
        if (!isLoggedIn()) {
            return getDataTable(new ArrayList<>());
        }
        Fx67llSecretKey record = fx67llSecretKeyService.selectFx67llSecretKeyBySecretKeyForApp(secretKey);
        return getDataTable(record == null ? new ArrayList<>() : Collections.singletonList(record));
    }

    /**
     * 判断当前请求是否已登录（未登录不抛异常，返回 false）
     */
    private boolean isLoggedIn() {
        try {
            return SecurityUtils.getLoginUser() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 导出秘钥配置列表
     */
    @PreAuthorize("@ss.hasPermi('secret:key:export')")
    @Log(title = "导出秘钥配置列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llSecretKey fx67llSecretKey) {
        List<Fx67llSecretKey> list = fx67llSecretKeyService.selectFx67llSecretKeyList(fx67llSecretKey);
        // 导出脱敏（阶段五·4.10）：secret_value 导出为 ***，cryptoSaltKey 保留明文（吉祥物）
        for (Fx67llSecretKey item : list) {
            if (item.getSecretValue() != null && !"cryptoSaltKey".equals(item.getSecretKey())) {
                item.setSecretValue("***");
            }
        }
        ExcelUtil<Fx67llSecretKey> util = new ExcelUtil<Fx67llSecretKey>(Fx67llSecretKey.class);
        util.exportExcel(response, list, "秘钥配置数据");
    }

    /**
     * 签发传输密钥 transferKey（阶段四·4.17，按需签发短时效 5 分钟）
     *
     * 管理端秘钥查看/修改前调用，拿到 transferKey 后用于传输加解密。
     * transferKey 仅用于传输（AES-CBC），与数据库加密密钥 dbEncKey（AES-GCM）算法隔离。
     */
    @PreAuthorize("@ss.hasPermi('secret:key:edit')")
    @GetMapping("/transferKey")
    public AjaxResult issueTransferKey() {
        String transferKey = transferKeyService.issue();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("transferKey", transferKey);
        return ajax;
    }

    /**
     * 获取秘钥配置详细信息（管理端，secret_value 用 transferKey 加密返回，不返回明文）
     *
     * 前端需先调 /transferKey 拿到 transferKey，再用其解密返回的 cipherValue。
     * cryptoSaltKey 不加密（盐值明文可见）。
     */
    @PreAuthorize("@ss.hasPermi('secret:key:query')")
    @GetMapping(value = "/{secretId}")
    public AjaxResult getInfo(@PathVariable("secretId") Long secretId) {
        Fx67llSecretKey record = fx67llSecretKeyService.selectFx67llSecretKeyBySecretId(secretId);
        if (record == null) {
            return success(null);
        }
        // cryptoSaltKey 不加密（盐值明文）
        if (!"cryptoSaltKey".equals(record.getSecretKey()) && record.getSecretValue() != null) {
            String transferKey = transferKeyService.getTransferKey();
            if (transferKey == null) {
                return AjaxResult.error("传输密钥已过期，请重新签发");
            }
            String cipherValue = TransferCryptoUtils.encrypt(record.getSecretValue(), transferKey);
            record.setSecretValue(cipherValue);
        }
        return success(record);
    }

    /**
     * 新增秘钥配置（管理端，secret_value 为 transferKey 加密密文，后端解密后用 dbEncKey 加密入库）
     *
     * cryptoSaltKey 不加密（明文入库）。
     */
    @PreAuthorize("@ss.hasPermi('secret:key:add')")
    @Log(title = "新增秘钥配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llSecretKey fx67llSecretKey) {
        String plainValue = decryptTransferValue(fx67llSecretKey);
        if (plainValue == null) {
            return AjaxResult.error("传输密钥已过期或密文无效，请重新签发");
        }
        fx67llSecretKey.setSecretValue(plainValue);
        return toAjax(fx67llSecretKeyService.insertFx67llSecretKey(fx67llSecretKey));
    }

    /**
     * 修改秘钥配置（管理端，secret_value 为 transferKey 加密密文，后端解密后用 dbEncKey 加密入库）
     *
     * cryptoSaltKey 不加密（明文入库）。
     */
    @PreAuthorize("@ss.hasPermi('secret:key:edit')")
    @Log(title = "修改秘钥配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llSecretKey fx67llSecretKey) {
        String plainValue = decryptTransferValue(fx67llSecretKey);
        if (plainValue == null) {
            return AjaxResult.error("传输密钥已过期或密文无效，请重新签发");
        }
        fx67llSecretKey.setSecretValue(plainValue);
        return toAjax(fx67llSecretKeyService.updateFx67llSecretKey(fx67llSecretKey));
    }

    /**
     * 用 transferKey 解密 secret_value（cryptoSaltKey 跳过，直接返回原值）
     *
     * @return 明文，解密失败返回 null
     */
    private String decryptTransferValue(Fx67llSecretKey fx67llSecretKey) {
        if (fx67llSecretKey == null || fx67llSecretKey.getSecretValue() == null) {
            return null;
        }
        // cryptoSaltKey 明文不入库加密
        if ("cryptoSaltKey".equals(fx67llSecretKey.getSecretKey())) {
            return fx67llSecretKey.getSecretValue();
        }
        String transferKey = transferKeyService.getTransferKey();
        if (transferKey == null) {
            return null;
        }
        try {
            return TransferCryptoUtils.decrypt(fx67llSecretKey.getSecretValue(), transferKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除秘钥配置
     */
    @PreAuthorize("@ss.hasPermi('secret:key:remove')")
    @Log(title = "删除秘钥配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{secretIds}")
    public AjaxResult remove(@PathVariable Long[] secretIds) {
        if (hasForbiddenDeleteValue(secretIds, 1)) {
            return warn("禁止删除配置项：cryptoSaltKey! ");
        } else {
            return toAjax(fx67llSecretKeyService.deleteFx67llSecretKeyBySecretIds(secretIds));
        }
    }

    /**
     * 判断是否含有不允许删除
     */
    private static boolean hasForbiddenDeleteValue(Long[] array, long targetValue) {
        for (Long element : array) {
            if (element != null && element.equals(targetValue)) {
                return true;
            }
        }
        return false;
    }

}
