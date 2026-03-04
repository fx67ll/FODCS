package com.ruoyi.web.controller.fx67ll.ai.request;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiMonthlyLog;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiYearlyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiYearlyLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI 调用请求年统计日志Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/request/yearly/log")
public class Fx67llAiRequestApiYearlyLogController extends BaseController {
    @Autowired
    private IFx67llAiRequestApiYearlyLogService fx67llAiRequestApiYearlyLogService;

    /**
     * 查询AI 调用请求年统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llAiRequestApiYearlyLog> list = fx67llAiRequestApiYearlyLogService.selectFx67llAiRequestApiYearlyLogList(fx67llAiRequestApiYearlyLog);
            return getDataTable(list);
        } else {
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setRows(new ArrayList<>());
            tableDataInfo.setTotal(0);
            tableDataInfo.setMsg("没有权限查询！");
            return tableDataInfo;
        }
    }

    /**
     * 导出AI 调用请求年统计日志列表
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:export')")
    @Log(title = "AI 调用请求年统计日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        List<Fx67llAiRequestApiYearlyLog> list = fx67llAiRequestApiYearlyLogService.selectFx67llAiRequestApiYearlyLogList(fx67llAiRequestApiYearlyLog);
        ExcelUtil<Fx67llAiRequestApiYearlyLog> util = new ExcelUtil<Fx67llAiRequestApiYearlyLog>(Fx67llAiRequestApiYearlyLog.class);
        util.exportExcel(response, list, "AI 调用请求年统计日志数据");
    }

    /**
     * 获取AI 调用请求年统计日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:query')")
    @GetMapping(value = "/{yearlyLogYear}")
    public AjaxResult getInfo(@PathVariable("yearlyLogYear") String yearlyLogYear) {
        return success(fx67llAiRequestApiYearlyLogService.selectFx67llAiRequestApiYearlyLogByYearlyLogYear(yearlyLogYear));
    }

    /**
     * 新增AI 调用请求年统计日志
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:add')")
    @Log(title = "AI 调用请求年统计日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        return toAjax(fx67llAiRequestApiYearlyLogService.insertFx67llAiRequestApiYearlyLog(fx67llAiRequestApiYearlyLog));
    }

    /**
     * 修改AI 调用请求年统计日志
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:edit')")
    @Log(title = "AI 调用请求年统计日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        return toAjax(fx67llAiRequestApiYearlyLogService.updateFx67llAiRequestApiYearlyLog(fx67llAiRequestApiYearlyLog));
    }

    /**
     * 删除AI 调用请求年统计日志
     */
    @PreAuthorize("@ss.hasPermi('ai:request:yearly:log:remove')")
    @Log(title = "AI 调用请求年统计日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{yearlyLogYears}")
    public AjaxResult remove(@PathVariable String[] yearlyLogYears) {
        return toAjax(fx67llAiRequestApiYearlyLogService.deleteFx67llAiRequestApiYearlyLogByYearlyLogYears(yearlyLogYears));
    }
}
