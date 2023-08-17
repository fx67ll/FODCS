package com.ruoyi.web.controller.fx67ll.dortmund;

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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外快盈亏记录Controller
 *
 * @author fx67ll
 * @date 2023-08-17
 */
@RestController
@RequestMapping("/dortmund/extra")
public class Fx67llDortmundExtraController extends BaseController {
    @Autowired
    private IFx67llDortmundExtraService fx67llDortmundExtraService;

    /**
     * 查询外快盈亏记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundExtra fx67llDortmundExtra) {
        startPage();
        List<Fx67llDortmundExtra> list = fx67llDortmundExtraService.selectFx67llDortmundExtraList(fx67llDortmundExtra);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 查询外快盈亏记录列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('dortmund:extra:list')")
    @GetMapping("/getExtraListForApp")
    public TableDataInfo getExtraListForApp(Fx67llDortmundExtra fx67llDortmundExtra) {
        startPage();
        List<Fx67llDortmundExtra> list = fx67llDortmundExtraService.selectFx67llDortmundExtraList(fx67llDortmundExtra);
        return getDataTable(list);
    }

    /**
     * 导出外快盈亏记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:export')")
    @Log(title = "外快盈亏记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundExtra fx67llDortmundExtra) {
        List<Fx67llDortmundExtra> list = fx67llDortmundExtraService.selectFx67llDortmundExtraList(fx67llDortmundExtra);
        ExcelUtil<Fx67llDortmundExtra> util = new ExcelUtil<Fx67llDortmundExtra>(Fx67llDortmundExtra.class);
        util.exportExcel(response, list, "外快盈亏记录数据");
    }

    /**
     * 获取外快盈亏记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:query')")
    @GetMapping(value = "/{extraId}")
    public AjaxResult getInfo(@PathVariable("extraId") Long extraId) {
        return success(fx67llDortmundExtraService.selectFx67llDortmundExtraByExtraId(extraId));
    }

    /**
     * 新增外快盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:add')")
    @Log(title = "外快盈亏记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundExtra fx67llDortmundExtra) {
        return toAjax(fx67llDortmundExtraService.insertFx67llDortmundExtra(fx67llDortmundExtra));
    }

    /**
     * 提供给 APP 新增外快盈亏记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('dortmund:extra:add')")
    @Log(title = "外快盈亏记录", businessType = BusinessType.INSERT)
    @PostMapping("/addExtraForApp")
    public AjaxResult addExtraForApp(@RequestBody Fx67llDortmundExtra fx67llDortmundExtra) {
        return toAjax(fx67llDortmundExtraService.insertFx67llDortmundExtra(fx67llDortmundExtra));
    }

    /**
     * 修改外快盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:edit')")
    @Log(title = "外快盈亏记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundExtra fx67llDortmundExtra) {
        return toAjax(fx67llDortmundExtraService.updateFx67llDortmundExtra(fx67llDortmundExtra));
    }

    /**
     * 删除外快盈亏记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:remove')")
    @Log(title = "外快盈亏记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{extraIds}")
    public AjaxResult remove(@PathVariable Long[] extraIds) {
        return toAjax(fx67llDortmundExtraService.deleteFx67llDortmundExtraByExtraIds(extraIds));
    }

    /**
     * 提供给 APP 删除外快盈亏记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('dortmund:extra:remove')")
    @Log(title = "每日号码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteExtraByIdForApp/{extraId}")
    public AjaxResult deleteExtraByIdForApp(@PathVariable Long extraId) {
        return toAjax(fx67llDortmundExtraService.deleteFx67llDortmundExtraByExtraId(extraId));
    }
}
