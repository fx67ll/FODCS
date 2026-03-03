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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI 调用请求日志Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/system/log")
public class Fx67llAiRequestLogController extends BaseController {
    @Autowired
    private IFx67llAiRequestLogService fx67llAiRequestLogService;

    /**
     * 查询AI 调用请求日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiRequestLog fx67llAiRequestLog) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llAiRequestLog> list = fx67llAiRequestLogService.selectFx67llAiRequestLogList(fx67llAiRequestLog);
            return getDataTable(list);
        } else {
            List<Fx67llAiRequestLog> list = fx67llAiRequestLogService.selectFx67llAiRequestLogListByUserId(fx67llAiRequestLog);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI 调用请求日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "AI 调用请求日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiRequestLog fx67llAiRequestLog) {
        List<Fx67llAiRequestLog> list = fx67llAiRequestLogService.selectFx67llAiRequestLogList(fx67llAiRequestLog);
        ExcelUtil<Fx67llAiRequestLog> util = new ExcelUtil<Fx67llAiRequestLog>(Fx67llAiRequestLog.class);
        util.exportExcel(response, list, "AI 调用请求日志数据");
    }

    /**
     * 获取AI 调用请求日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{requestLogId}")
    public AjaxResult getInfo(@PathVariable("requestLogId") Long requestLogId) {
        return success(fx67llAiRequestLogService.selectFx67llAiRequestLogByRequestLogId(requestLogId));
    }

    /**
     * 新增AI 调用请求日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "AI 调用请求日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiRequestLog fx67llAiRequestLog) {
        return toAjax(fx67llAiRequestLogService.insertFx67llAiRequestLog(fx67llAiRequestLog));
    }

    /**
     * 修改AI 调用请求日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "AI 调用请求日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiRequestLog fx67llAiRequestLog) {
        return toAjax(fx67llAiRequestLogService.updateFx67llAiRequestLog(fx67llAiRequestLog));
    }

    /**
     * 删除AI 调用请求日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "AI 调用请求日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{requestLogIds}")
    public AjaxResult remove(@PathVariable Long[] requestLogIds) {
        return toAjax(fx67llAiRequestLogService.deleteFx67llAiRequestLogByRequestLogIds(requestLogIds));
    }
}
