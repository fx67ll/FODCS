package com.ruoyi.web.controller.fx67ll.secret;

import java.util.ArrayList;
import java.util.List;
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
import com.ruoyi.fx67ll.secret.domain.Fx67llSecretKey;
import com.ruoyi.fx67ll.secret.service.IFx67llSecretKeyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 秘钥配置Controller
 *
 * @author fx67ll
 * @date 2023-08-30
 */
@RestController
@RequestMapping("/secret/key")
public class Fx67llSecretKeyController extends BaseController {
    @Autowired
    private IFx67llSecretKeyService fx67llSecretKeyService;

    /**
     * 查询秘钥配置列表
     */
    @PreAuthorize("@ss.hasPermi('secret:key:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llSecretKey fx67llSecretKey) {
        startPage();
        List<Fx67llSecretKey> list = fx67llSecretKeyService.selectFx67llSecretKeyList(fx67llSecretKey);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 查询秘钥配置列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('secret:key:list')")
    @GetMapping("/getSecretKeyConfigForApp")
    public TableDataInfo getSecretKeyConfigForApp(Fx67llSecretKey fx67llSecretKey) {
//        禁止查询参数为"cryptoSaltKey"的配置信息
        if (fx67llSecretKey.getSecretKey() == null || fx67llSecretKey.getSecretKey().equals("cryptoSaltKey")) {
            return getDataTable(new ArrayList<>());
        } else {
            startPage();
            List<Fx67llSecretKey> list = fx67llSecretKeyService.selectFx67llSecretKeyListForApp(fx67llSecretKey);
            return getDataTable(list);
        }
    }

    /**
     * 导出秘钥配置列表
     */
    @PreAuthorize("@ss.hasPermi('secret:key:export')")
    @Log(title = "秘钥配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llSecretKey fx67llSecretKey) {
        List<Fx67llSecretKey> list = fx67llSecretKeyService.selectFx67llSecretKeyList(fx67llSecretKey);
        ExcelUtil<Fx67llSecretKey> util = new ExcelUtil<Fx67llSecretKey>(Fx67llSecretKey.class);
        util.exportExcel(response, list, "秘钥配置数据");
    }

    /**
     * 获取秘钥配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('secret:key:query')")
    @GetMapping(value = "/{secretId}")
    public AjaxResult getInfo(@PathVariable("secretId") Long secretId) {
        return success(fx67llSecretKeyService.selectFx67llSecretKeyBySecretId(secretId));
    }

    /**
     * 新增秘钥配置
     */
    @PreAuthorize("@ss.hasPermi('secret:key:add')")
    @Log(title = "秘钥配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llSecretKey fx67llSecretKey) {
        return toAjax(fx67llSecretKeyService.insertFx67llSecretKey(fx67llSecretKey));
    }

    /**
     * 修改秘钥配置
     */
    @PreAuthorize("@ss.hasPermi('secret:key:edit')")
    @Log(title = "秘钥配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llSecretKey fx67llSecretKey) {
        return toAjax(fx67llSecretKeyService.updateFx67llSecretKey(fx67llSecretKey));
    }

    /**
     * 删除秘钥配置
     */
    @PreAuthorize("@ss.hasPermi('secret:key:remove')")
    @Log(title = "秘钥配置", businessType = BusinessType.DELETE)
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
