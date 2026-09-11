package com.ruoyi.web.controller.fx67ll.dortmund;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateVersionService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外快盈亏数值模拟版本记录Controller
 *
 * @author fx67ll
 * @date 2026-09-11
 */
@RestController
@RequestMapping("/dortmund/extra/simulate/version")
public class Fx67llDortmundExtraSimulateVersionController extends BaseController {
    @Autowired
    private IFx67llDortmundExtraSimulateVersionService fx67llDortmundExtraSimulateVersionService;

    /**
     * 查询外快盈亏数值模拟版本记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        startPage();
        List<Fx67llDortmundExtraSimulateVersion> list = fx67llDortmundExtraSimulateVersionService.selectFx67llDortmundExtraSimulateVersionList(fx67llDortmundExtraSimulateVersion);
        return getDataTable(list);
    }

    /**
     * 新增外快盈亏数值模拟版本记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:add')")
    @Log(title = "新增外快盈亏数值模拟版本记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        return toAjax(fx67llDortmundExtraSimulateVersionService.insertFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion));
    }

    /**
     * 修改外快盈亏数值模拟版本记录（用于版本启用停用）
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:edit')")
    @Log(title = "修改外快盈亏数值模拟版本记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        return toAjax(fx67llDortmundExtraSimulateVersionService.updateFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion));
    }

    /**
     * 提供给 APP 查询外快盈亏数值模拟版本记录列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('dortmund:extra:list')")
    @GetMapping("/getSimulateVersionListForApp")
    public TableDataInfo getSimulateVersionListForApp(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        startPageForApp();
        List<Fx67llDortmundExtraSimulateVersion> list = fx67llDortmundExtraSimulateVersionService.selectFx67llDortmundExtraSimulateVersionListByUserId(fx67llDortmundExtraSimulateVersion);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 新增外快盈亏数值模拟版本记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('dortmund:extra:add')")
    @Log(title = "提供给 APP 新增外快盈亏数值模拟版本记录", businessType = BusinessType.INSERT)
    @PostMapping("/addSimulateVersionForApp")
    public AjaxResult addSimulateVersionForApp(@RequestBody Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        return toAjax(fx67llDortmundExtraSimulateVersionService.insertFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion));
    }
}
