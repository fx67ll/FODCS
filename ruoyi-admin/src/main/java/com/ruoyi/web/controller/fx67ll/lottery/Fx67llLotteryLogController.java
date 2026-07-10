package com.ruoyi.web.controller.fx67ll.lottery;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryHistory;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryTotalReward;
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

    // ==================== 公共处理方法开始 ====================

    /**
     * 校验是否为 fx67ll 本人
     */
    private boolean isFx67llSelf() {
        return SecurityUtils.getUsername().equals("fx67ll");
    }

    /**
     * 获取标准化的 AjaxResult 权限错误返回
     */
    private AjaxResult getForbiddenAjaxResult() {
        return error("内部测试功能，仅限 fx67ll 本人使用！");
    }

    /**
     * 获取标准化的 TableDataInfo 权限错误返回
     */
    private TableDataInfo getForbiddenTableDataInfo() {
        TableDataInfo errorRsp = new TableDataInfo();
        errorRsp.setCode(HttpStatus.FORBIDDEN);
        errorRsp.setMsg("内部测试功能，仅限 fx67ll 本人使用！");
        return errorRsp;
    }

    // ==================== 公共处理方法结束 ====================

    /**
     * 查询每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLotteryLog fx67llLotteryLog) {
        if (!isFx67llSelf()) {
            return getForbiddenTableDataInfo();
        }
        startPage();
        List<Fx67llLotteryLog> list = fx67llLotteryLogService.selectFx67llLotteryLogList(fx67llLotteryLog);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 查询每日号码记录列表
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:list')")
    @GetMapping("/getLotteryLogListForApp")
    public TableDataInfo getLotteryLogListForApp(Fx67llLotteryLog fx67llLotteryLog) {
        startPageForApp();
        List<Fx67llLotteryLog> list = fx67llLotteryLogService.selectFx67llLotteryLogListByUserId(fx67llLotteryLog);
        return getDataTable(list);
    }


    /**
     * 导出每日号码记录列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:export')")
    @Log(title = "导出每日号码记录列表", businessType = BusinessType.EXPORT)
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
        if (!isFx67llSelf()) {
            return getForbiddenAjaxResult();
        }
        return success(fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(lotteryId));
    }

    /**
     * 提供给 APP 获取每日号码记录详细信息
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:query')")
    @GetMapping(value = "/getLotteryLogInfoForApp/{lotteryId}")
    public AjaxResult getLotteryLogInfoForApp(@PathVariable("lotteryId") Long lotteryId) {
        if (fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(lotteryId).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止查询！");
        } else {
            return success(fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(lotteryId));
        }
    }

    /**
     * 新增每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:add')")
    @Log(title = "新增每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        if (!isFx67llSelf()) {
            return getForbiddenAjaxResult();
        }
        return toAjax(fx67llLotteryLogService.insertFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 提供给 APP 新增每日号码记录
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:add')")
    @Log(title = "提供给 APP 新增每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping("/addLotteryLogForApp")
    public AjaxResult addLotteryLogForApp(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        return toAjax(fx67llLotteryLogService.insertFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 修改每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:edit')")
    @Log(title = "修改每日号码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        if (!isFx67llSelf()) {
            return getForbiddenAjaxResult();
        }
        return toAjax(fx67llLotteryLogService.updateFx67llLotteryLog(fx67llLotteryLog));
    }

    /**
     * 提供给 APP 修改每日号码记录
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:edit')")
    @Log(title = "提供给 APP 修改每日号码记录", businessType = BusinessType.UPDATE)
    @PutMapping("/editLotteryLogForApp")
    public AjaxResult editLotteryLogForApp(@RequestBody Fx67llLotteryLog fx67llLotteryLog) {
        if (fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(fx67llLotteryLog.getLotteryId()).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止修改！");
        } else {
            return toAjax(fx67llLotteryLogService.updateFx67llLotteryLog(fx67llLotteryLog));
        }
    }

    /**
     * 删除每日号码记录
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:remove')")
    @Log(title = "删除每日号码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{lotteryIds}")
    public AjaxResult remove(@PathVariable Long[] lotteryIds) {
        if (!isFx67llSelf()) {
            return getForbiddenAjaxResult();
        }
        return toAjax(fx67llLotteryLogService.deleteFx67llLotteryLogByLotteryIds(lotteryIds));
    }

    /**
     * 合并同期号、同类型的每日号码记录
     * <p>
     * 将一组同期号、同类型的记录在后台事务内合并为一条全新记录，并删除合并前的旧数据。
     * 所有合并逻辑（去重、求和、新增、删除）均在后台一个事务中完成，前端只需传入待合并的主键集合，
     * 避免前端多次异步调用造成的脏数据错乱问题。
     * <p>
     * 入参：待合并记录主键数组（至少 2 条）
     * 返回：合并后新生成记录的主键
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:merge')")
    @Log(title = "合并同期号同类型每日号码记录", businessType = BusinessType.UPDATE)
    @PostMapping("/merge")
    public AjaxResult merge(@RequestBody Long[] lotteryIds) {
        if (!isFx67llSelf()) {
            return getForbiddenAjaxResult();
        }
        Long mergedLotteryId = fx67llLotteryLogService.mergeFx67llLotteryLogs(lotteryIds);
        return AjaxResult.success("合并成功", mergedLotteryId);
    }

    /**
     * 提供给 APP 合并同期号、同类型的每日号码记录
     * <p>
     * 与 Web 端 /merge 接口逻辑一致，区别在于不做 fx67ll 本人限制，
     * 而是在 Service 层校验所有待合并记录均为当前登录用户本人创建。
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:merge')")
    @Log(title = "提供给 APP 合并同期号同类型每日号码记录", businessType = BusinessType.UPDATE)
    @PostMapping("/mergeLotteryLogForApp")
    public AjaxResult mergeLotteryLogForApp(@RequestBody Long[] lotteryIds) {
        Long mergedLotteryId = fx67llLotteryLogService.mergeFx67llLotteryLogs(lotteryIds);
        return AjaxResult.success("合并成功", mergedLotteryId);
    }

    /**
     * 提供给 APP 批量新增每日号码记录
     * <p>
     * 用于"周五一键三连"等需要一次性生成多条不同类型记录的场景。
     * 前端串行准备好各条记录数据后，一次请求调用本接口，后台一个事务内按入参顺序依次写入，
     * 每条记录 createTime 递增 1 秒以保证保存顺序稳定，任一失败全部回滚，
     * 替代前端多次异步调用 addLog 造成的顺序不可控与原子性缺失问题。
     * <p>
     * 入参：待新增记录集合（顺序即保存顺序，如排列三→排列五→七星彩）
     * 返回：新增记录的主键集合
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:add')")
    @Log(title = "提供给 APP 批量新增每日号码记录", businessType = BusinessType.INSERT)
    @PostMapping("/batchAddLotteryLogForApp")
    public AjaxResult batchAddLotteryLogForApp(@RequestBody List<Fx67llLotteryLog> logList) {
        List<Long> lotteryIds = fx67llLotteryLogService.batchInsertFx67llLotteryLogs(logList);
        return AjaxResult.success("保存成功", lotteryIds);
    }

    /**
     * 提供给 APP 删除每日号码记录
     */
    // 如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
    // @PreAuthorize("@ss.hasPermi('lottery:log:remove')")
    @Log(title = "提供给 APP 删除每日号码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteLogByIdForApp/{lotteryId}")
    public AjaxResult deleteLogByIdForApp(@PathVariable Long lotteryId) {
        if (fx67llLotteryLogService.selectFx67llLotteryLogByLotteryId(lotteryId).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止删除！");
        } else {
            return toAjax(fx67llLotteryLogService.deleteFx67llLotteryLogByLotteryIdForApp(lotteryId));
        }
    }

    /**
     * 提供给 APP 查询历史号码记录中奖数据统计
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:total')")
    @GetMapping("/getLotteryTotalReward")
    public TableDataInfo getLotteryTotalReward(Fx67llLotteryLog fx67llLotteryLog) {
        if (!isFx67llSelf()) {
            return getForbiddenTableDataInfo();
        }
        startPageForApp();
        fx67llLotteryLog.setUserId(SecurityUtils.getUserId());
        List<Fx67llLotteryTotalReward> list = fx67llLotteryLogService.selectFx67llLotteryTotalReward(fx67llLotteryLog);
        return getDataTable(list);
    }

    /**
     * 历史号码出现频率统计
     */
    @PreAuthorize("@ss.hasPermi('lottery:log:statistics')")
    @GetMapping("/getLotteryHistoryStatistics")
    public TableDataInfo getLotteryHistoryStatistics() {
        if (!isFx67llSelf()) {
            return getForbiddenTableDataInfo();
        }
        List<Fx67llLotteryHistory> list = fx67llLotteryLogService.selectFx67llLotteryLogHistoryStatistics();
        return getDataTable(list);
    }
}