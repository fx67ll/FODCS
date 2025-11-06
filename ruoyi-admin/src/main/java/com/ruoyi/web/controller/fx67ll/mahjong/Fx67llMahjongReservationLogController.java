package com.ruoyi.web.controller.fx67ll.mahjong;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLogExt;
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
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongReservationLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 麻将室预约记录Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/mahjong/reservation/log")
public class Fx67llMahjongReservationLogController extends BaseController {
    @Autowired
    private IFx67llMahjongReservationLogService fx67llMahjongReservationLogService;

    /**
     * 查询麻将室预约记录列表
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llMahjongReservationLog> list = fx67llMahjongReservationLogService.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
            return getDataTable(list);
        } else {
            List<Fx67llMahjongReservationLog> list = fx67llMahjongReservationLogService.selectFx67llMahjongReservationLogListByUserId(fx67llMahjongReservationLog);
            return getDataTable(list);
        }
    }

    /**
     * 提供给 APP 查询麻将室预约记录列表
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:list')")
    @PostMapping("/getReservationLogListForApp")
    public TableDataInfo getReservationLogListForApp(@RequestBody Fx67llMahjongReservationLogExt fx67llMahjongReservationLogExt) {
        if(!fx67llMahjongReservationLogExt.getIsNeedAll()){
            fx67llMahjongReservationLogExt.setUserId(SecurityUtils.getUserId());
        }
        startPage();
        List<Fx67llMahjongReservationLogExt> list = fx67llMahjongReservationLogService.selectReservationLogForApp(fx67llMahjongReservationLogExt);
        return getDataTable(list);
    }

    /**
     * 导出麻将室预约记录列表
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:export')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        List<Fx67llMahjongReservationLog> list = fx67llMahjongReservationLogService.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
        ExcelUtil<Fx67llMahjongReservationLog> util = new ExcelUtil<Fx67llMahjongReservationLog>(Fx67llMahjongReservationLog.class);
        util.exportExcel(response, list, "麻将室预约记录数据");
    }

    /**
     * 获取麻将室预约记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:query')")
    @GetMapping(value = "/{mahjongReservationLogId}")
    public AjaxResult getInfo(@PathVariable("mahjongReservationLogId") Long mahjongReservationLogId) {
        return success(fx67llMahjongReservationLogService.selectFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId));
    }

    /**
     * 新增麻将室预约记录
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:add')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        return toAjax(fx67llMahjongReservationLogService.insertFx67llMahjongReservationLog(fx67llMahjongReservationLog));
    }

    /**
     * 提供给 APP 新增麻将室预约记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:add')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addReservationLogForApp")
    public AjaxResult addReservationLogForApp(@RequestBody Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        return toAjax(fx67llMahjongReservationLogService.insertFx67llMahjongReservationLog(fx67llMahjongReservationLog));
    }

    /**
     * 修改麻将室预约记录
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:edit')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        return toAjax(fx67llMahjongReservationLogService.updateFx67llMahjongReservationLog(fx67llMahjongReservationLog));
    }

    /**
     * 提供给 APP 修改麻将室预约记录
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:edit')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/editReservationLogForApp")
    public AjaxResult editReservationLogForApp(@RequestBody Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        return toAjax(fx67llMahjongReservationLogService.updateFx67llMahjongReservationLog(fx67llMahjongReservationLog));
    }

    /**
     * 删除麻将室预约记录
     */
    @PreAuthorize("@ss.hasPermi('mahjong:reservation:log:remove')")
    @Log(title = "麻将室预约记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{mahjongReservationLogIds}")
    public AjaxResult remove(@PathVariable Long[] mahjongReservationLogIds) {
        return toAjax(fx67llMahjongReservationLogService.deleteFx67llMahjongReservationLogByMahjongReservationLogIds(mahjongReservationLogIds));
    }
}
