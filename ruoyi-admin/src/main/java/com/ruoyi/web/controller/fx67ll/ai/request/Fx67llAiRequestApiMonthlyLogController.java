package com.ruoyi.web.controller.fx67ll.ai.request;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicGroup;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiMonthlyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiMonthlyLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI 调用请求月统计日志Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/system/log")
public class Fx67llAiRequestApiMonthlyLogController extends BaseController {
    @Autowired
    private IFx67llAiRequestApiMonthlyLogService fx67llAiRequestApiMonthlyLogService;

    /**
     * 查询AI 调用请求月统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        startPage();
        List<Fx67llAiRequestApiMonthlyLog> list = fx67llAiRequestApiMonthlyLogService.selectFx67llAiRequestApiMonthlyLogList(fx67llAiRequestApiMonthlyLog);
        return getDataTable(list);
    }

    /**
     * 导出AI 调用请求月统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "AI 调用请求月统计日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        List<Fx67llAiRequestApiMonthlyLog> list = fx67llAiRequestApiMonthlyLogService.selectFx67llAiRequestApiMonthlyLogList(fx67llAiRequestApiMonthlyLog);
        ExcelUtil<Fx67llAiRequestApiMonthlyLog> util = new ExcelUtil<Fx67llAiRequestApiMonthlyLog>(Fx67llAiRequestApiMonthlyLog.class);
        util.exportExcel(response, list, "AI 调用请求月统计日志数据");
    }

    /**
     * 获取AI 调用请求月统计日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{monthlyLogMonth}")
    public AjaxResult getInfo(@PathVariable("monthlyLogMonth") String monthlyLogMonth) {
        return success(fx67llAiRequestApiMonthlyLogService.selectFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(monthlyLogMonth));
    }

    /**
     * 新增AI 调用请求月统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "AI 调用请求月统计日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        return toAjax(fx67llAiRequestApiMonthlyLogService.insertFx67llAiRequestApiMonthlyLog(fx67llAiRequestApiMonthlyLog));
    }

    /**
     * 修改AI 调用请求月统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "AI 调用请求月统计日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        return toAjax(fx67llAiRequestApiMonthlyLogService.updateFx67llAiRequestApiMonthlyLog(fx67llAiRequestApiMonthlyLog));
    }

    /**
     * 删除AI 调用请求月统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "AI 调用请求月统计日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{monthlyLogMonths}")
    public AjaxResult remove(@PathVariable String[] monthlyLogMonths) {
        return toAjax(fx67llAiRequestApiMonthlyLogService.deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonths(monthlyLogMonths));
    }
}
