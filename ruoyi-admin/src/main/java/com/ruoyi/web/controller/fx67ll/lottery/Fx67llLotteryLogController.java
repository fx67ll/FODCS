package com.ruoyi.web.controller.fx67ll.lottery;

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
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotteryLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 每日号码记录Controller
 *
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottery/log")
public class Fx67llLotteryLogController extends BaseController {
    @Autowired
    private IFx67llLotteryLogService fx67llLotteryLogService;

    /**
     * 查询每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLotteryLog fx67llLotteryLog) {
        startPage();
        List<Fx67llLotteryLog> list = fx67llLotteryLogService.selectFx67llLotteryLogList(fx67llLotteryLog);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 查询每日号码记录列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('lottery:log:list')")
    @GetMapping("/getLotteryLogListForApp")
    public TableDataInfo getLotteryLogListForApp(Fx67llLotteryLog fx67llLotteryLog) {
        startPage();
        List<Fx67llLotteryLog> list = fx67llLotteryLogService.selectFx67llLotteryLogListByUserId(fx67llLotteryLog);
        return getDataTable(list);
    }


    /**
     * 导出每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:export')")
    @Log(title = "每日号码记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLotteryLog fx67llLotteryLog) {
        List<Fx67llLotteryLog> list = fx67llLotteryLogService.selectFx67llLotteryLogList(fx67llLotteryLog);
        ExcelUtil<Fx67llLotteryLog> util = new ExcelUtil<Fx67llLotteryLog>(Fx67llLotteryLog.class);
        util.exportExcel(response, list, "每日号码记录数据");
    }

    /**
     * 获取每日号码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:query')")
    @GetMapping(value = "/{lotteryId}")
    public AjaxResult getInfo(@PathVariable("lotteryId") Long lotteryId) {
        return success(fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(lotteryId));
    }

    /**
     * 新增每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:add')")
    @Log(title = "每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        return toAjax(fx67llLotteryLogService.insertFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 提供给 APP 新增每日号码记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('lottery:log:add')")
    @Log(title = "每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping("/addLotteryLogForApp")
    public AjaxResult addLotteryLogForApp(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        return toAjax(fx67llLotteryLogService.insertFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 修改每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:edit')")
    @Log(title = "每日号码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        return toAjax(fx67llLotteryLogService.updateFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 删除每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:remove')")
    @Log(title = "每日号码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{lotteryIds}")
    public AjaxResult remove(@PathVariable Long[] lotteryIds) {
        return toAjax(fx67llLotteryLogService.deleteFx67llLotteryLogByLotteryIds(lotteryIds));
    }

    /**
     * 提供给 APP 删除每日号码记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('lottery:log:remove')")
    @Log(title = "每日号码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteLogByIdForApp/{lotteryId}")
    public AjaxResult deleteLogByIdForApp(@PathVariable Long lotteryId) {
        return toAjax(fx67llLotteryLogService.deleteFx67llLotteryLogByLotteryIdForApp(lotteryId));
    }

}
