package com.ruoyi.web.controller.fx67ll.punch;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
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
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLog;
import com.ruoyi.fx67ll.punch.service.IFx67llPunchLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 打卡记录Controller
 *
 * @author fx67ll
 * @date 2023-11-29
 */
@RestController
@RequestMapping("/punch/log")
public class Fx67llPunchLogController extends BaseController {
    @Autowired
    private IFx67llPunchLogService fx67llPunchLogService;

    /**
     * 查询打卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('punch:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llPunchLog fx67llPunchLog) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llPunchLog> list = fx67llPunchLogService.selectFx67llPunchLogList(fx67llPunchLog);
            return getDataTable(list);
        } else {
            List<Fx67llPunchLog> list = fx67llPunchLogService.selectFx67llPunchLogListByUserId(fx67llPunchLog);
            return getDataTable(list);
        }
    }

    /**
     * 提供给 APP 查询打卡记录列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('punch:log:list')")
    @GetMapping("/getPunchLogListForApp")
    public TableDataInfo getPunchLogListForApp(Fx67llPunchLog fx67llPunchLog) {
        startPageForApp();
        List<Fx67llPunchLog> list = fx67llPunchLogService.selectFx67llPunchLogListByUserId(fx67llPunchLog);
        return getDataTable(list);
    }

    /**
     * 导出打卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('punch:log:export')")
    @Log(title = "导出打卡记录列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llPunchLog fx67llPunchLog) {
        List<Fx67llPunchLog> list = fx67llPunchLogService.selectFx67llPunchLogList(fx67llPunchLog);
        ExcelUtil<Fx67llPunchLog> util = new ExcelUtil<Fx67llPunchLog>(Fx67llPunchLog.class);
        util.exportExcel(response, list, "打卡记录数据");
    }

    /**
     * 获取打卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('punch:log:query')")
    @GetMapping(value = "/{punchId}")
    public AjaxResult getInfo(@PathVariable("punchId") Long punchId) {
        return success(fx67llPunchLogService.selectFx67llPunchLogByPunchId(punchId));
    }

    /**
     * 提供给 APP 获取打卡记录详细信息
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('punch:log:query')")
    @GetMapping(value = "/getPunchLogInfoForApp/{punchId}")
    public AjaxResult getPunchLogInfoForApp(@PathVariable("punchId") Long punchId) {
        if (fx67llPunchLogService.selectFx67llPunchLogByPunchId(punchId).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止查询！");
        } else {
            return success(fx67llPunchLogService.selectFx67llPunchLogByPunchId(punchId));
        }
    }

    /**
     * 新增打卡记录
     */
    @PreAuthorize("@ss.hasPermi('punch:log:add')")
    @Log(title = "新增打卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llPunchLog fx67llPunchLog) {
        return toAjax(fx67llPunchLogService.insertFx67llPunchLog(fx67llPunchLog));
    }

    /**
     * 提供给 APP 新增每日号码记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('punch:log:add')")
    @Log(title = "提供给 APP 新增每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping("/addPunchLogForApp")
    public AjaxResult addPunchLogForApp(@RequestBody Fx67llPunchLog fx67llPunchLog) {
        return toAjax(fx67llPunchLogService.insertFx67llPunchLog(fx67llPunchLog));
    }

    /**
     * 修改打卡记录
     */
    @PreAuthorize("@ss.hasPermi('punch:log:edit')")
    @Log(title = "修改打卡记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llPunchLog fx67llPunchLog) {
        return toAjax(fx67llPunchLogService.updateFx67llPunchLog(fx67llPunchLog));
    }

    /**
     * 提供给 APP 修改打卡记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('punch:log:edit')")
    @Log(title = "提供给 APP 修改打卡记录", businessType = BusinessType.UPDATE)
    @PutMapping("/editPunchLogForApp")
    public AjaxResult editPunchLogForApp(@RequestBody Fx67llPunchLog fx67llPunchLog) {
        if (fx67llPunchLogService.selectFx67llPunchLogByPunchId(fx67llPunchLog.getPunchId()).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止修改！");
        } else {
            return toAjax(fx67llPunchLogService.updateFx67llPunchLog(fx67llPunchLog));
        }
    }

    /**
     * 删除打卡记录
     */
    @PreAuthorize("@ss.hasPermi('punch:log:remove')")
    @Log(title = "删除打卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{punchIds}")
    public AjaxResult remove(@PathVariable Long[] punchIds) {
        return toAjax(fx67llPunchLogService.deleteFx67llPunchLogByPunchIds(punchIds));
    }

    /**
     * 提供给 APP 删除打卡记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('punch:log:remove')")
    @Log(title = "提供给 APP 删除打卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/deletePunchLogByIdForApp/{punchId}")
    public AjaxResult deletePunchLogByIdForApp(@PathVariable Long punchId) {
        if (fx67llPunchLogService.selectFx67llPunchLogByPunchId(punchId).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止删除！");
        } else {
            return toAjax(fx67llPunchLogService.deleteFx67llPunchLogByPunchIdForApp(punchId));
        }
    }

}
