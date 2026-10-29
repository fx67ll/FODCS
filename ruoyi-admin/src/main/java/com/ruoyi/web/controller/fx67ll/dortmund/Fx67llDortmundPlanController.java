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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlan;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundPlanService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 方案计划Controller
 *
 * @author fx67ll
 * @date 2026-10-29
 */
@RestController
@RequestMapping("/dortmund/plan")
public class Fx67llDortmundPlanController extends BaseController {
    @Autowired
    private IFx67llDortmundPlanService fx67llDortmundPlanService;

    /**
     * 查询方案计划列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundPlan fx67llDortmundPlan) {
        startPage();
        List<Fx67llDortmundPlan> list = fx67llDortmundPlanService.selectFx67llDortmundPlanList(fx67llDortmundPlan);
        return getDataTable(list);
    }

    /**
     * 导出方案计划列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:export')")
    @Log(title = "方案计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundPlan fx67llDortmundPlan) {
        List<Fx67llDortmundPlan> list = fx67llDortmundPlanService.selectFx67llDortmundPlanList(fx67llDortmundPlan);
        ExcelUtil<Fx67llDortmundPlan> util = new ExcelUtil<Fx67llDortmundPlan>(Fx67llDortmundPlan.class);
        util.exportExcel(response, list, "方案计划数据");
    }

    /**
     * 获取方案计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:query')")
    @GetMapping(value = "/{planId}")
    public AjaxResult getInfo(@PathVariable("planId") Long planId) {
        return success(fx67llDortmundPlanService.selectFx67llDortmundPlanByPlanId(planId));
    }

    /**
     * 新增方案计划
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:add')")
    @Log(title = "方案计划", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundPlan fx67llDortmundPlan) {
        return toAjax(fx67llDortmundPlanService.insertFx67llDortmundPlan(fx67llDortmundPlan));
    }

    /**
     * 修改方案计划
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:edit')")
    @Log(title = "方案计划", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundPlan fx67llDortmundPlan) {
        return toAjax(fx67llDortmundPlanService.updateFx67llDortmundPlan(fx67llDortmundPlan));
    }

    /**
     * 删除方案计划
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:remove')")
    @Log(title = "方案计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{planIds}")
    public AjaxResult remove(@PathVariable Long[] planIds) {
        return toAjax(fx67llDortmundPlanService.deleteFx67llDortmundPlanByPlanIds(planIds));
    }

    /**
     * 上传方案计划图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:plan:image:upload')")
    @Log(title = "方案计划图片", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/image/upload")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String objectKey = fx67llDortmundPlanService.uploadPlanImage(file);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("objectKey", objectKey);
        return ajax;
    }
}
