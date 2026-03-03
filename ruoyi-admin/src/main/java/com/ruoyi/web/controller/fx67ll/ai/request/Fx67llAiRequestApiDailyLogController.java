package com.ruoyi.web.controller.fx67ll.ai.request;

import java.util.Date;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiDailyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiDailyLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI 调用请求日统计日志Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/system/log")
public class Fx67llAiRequestApiDailyLogController extends BaseController {
    @Autowired
    private IFx67llAiRequestApiDailyLogService fx67llAiRequestApiDailyLogService;

    /**
     * 查询AI 调用请求日统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        startPage();
        List<Fx67llAiRequestApiDailyLog> list = fx67llAiRequestApiDailyLogService.selectFx67llAiRequestApiDailyLogList(fx67llAiRequestApiDailyLog);
        return getDataTable(list);
    }

    /**
     * 导出AI 调用请求日统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "AI 调用请求日统计日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        List<Fx67llAiRequestApiDailyLog> list = fx67llAiRequestApiDailyLogService.selectFx67llAiRequestApiDailyLogList(fx67llAiRequestApiDailyLog);
        ExcelUtil<Fx67llAiRequestApiDailyLog> util = new ExcelUtil<Fx67llAiRequestApiDailyLog>(Fx67llAiRequestApiDailyLog.class);
        util.exportExcel(response, list, "AI 调用请求日统计日志数据");
    }

    /**
     * 获取AI 调用请求日统计日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{dailyLogDate}")
    public AjaxResult getInfo(@PathVariable("dailyLogDate") Date dailyLogDate) {
        return success(fx67llAiRequestApiDailyLogService.selectFx67llAiRequestApiDailyLogByDailyLogDate(dailyLogDate));
    }

    /**
     * 新增AI 调用请求日统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "AI 调用请求日统计日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        return toAjax(fx67llAiRequestApiDailyLogService.insertFx67llAiRequestApiDailyLog(fx67llAiRequestApiDailyLog));
    }

    /**
     * 修改AI 调用请求日统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "AI 调用请求日统计日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        return toAjax(fx67llAiRequestApiDailyLogService.updateFx67llAiRequestApiDailyLog(fx67llAiRequestApiDailyLog));
    }

    /**
     * 删除AI 调用请求日统计日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "AI 调用请求日统计日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dailyLogDates}")
    public AjaxResult remove(@PathVariable Date[] dailyLogDates) {
        return toAjax(fx67llAiRequestApiDailyLogService.deleteFx67llAiRequestApiDailyLogByDailyLogDates(dailyLogDates));
    }
}
