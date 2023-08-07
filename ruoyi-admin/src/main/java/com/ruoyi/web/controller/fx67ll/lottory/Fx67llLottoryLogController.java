package com.ruoyi.fx67ll.lottory.controller;

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
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryLog;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottoryLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 每日号码记录Controller
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottory/log")
public class Fx67llLottoryLogController extends BaseController
{
    @Autowired
    private IFx67llLottoryLogService fx67llLottoryLogService;

    /**
     * 查询每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLottoryLog fx67llLottoryLog)
    {
        startPage();
        List<Fx67llLottoryLog> list = fx67llLottoryLogService.selectFx67llLottoryLogList(fx67llLottoryLog);
        return getDataTable(list);
    }

    /**
     * 导出每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:export')")
    @Log(title = "每日号码记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLottoryLog fx67llLottoryLog)
    {
        List<Fx67llLottoryLog> list = fx67llLottoryLogService.selectFx67llLottoryLogList(fx67llLottoryLog);
        ExcelUtil<Fx67llLottoryLog> util = new ExcelUtil<Fx67llLottoryLog>(Fx67llLottoryLog.class);
        util.exportExcel(response, list, "每日号码记录数据");
    }

    /**
     * 获取每日号码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:query')")
    @GetMapping(value = "/{lottoryId}")
    public AjaxResult getInfo(@PathVariable("lottoryId") Long lottoryId)
    {
        return success(fx67llLottoryLogService.selectFx67llLottoryLogByLottoryId(lottoryId));
    }

    /**
     * 新增每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:add')")
    @Log(title = "每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLottoryLog fx67llLottoryLog)
    {
        return toAjax(fx67llLottoryLogService.insertFx67llLottoryLog(fx67llLottoryLog));
    }

    /**
     * 修改每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:edit')")
    @Log(title = "每日号码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLottoryLog fx67llLottoryLog)
    {
        return toAjax(fx67llLottoryLogService.updateFx67llLottoryLog(fx67llLottoryLog));
    }

    /**
     * 删除每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottory:log:remove')")
    @Log(title = "每日号码记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{lottoryIds}")
    public AjaxResult remove(@PathVariable Long[] lottoryIds)
    {
        return toAjax(fx67llLottoryLogService.deleteFx67llLottoryLogByLottoryIds(lottoryIds));
    }
}
