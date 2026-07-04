package com.ruoyi.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }

    /**
     * 获取已上架通知公告列表
     */
    @GetMapping("/public/list")
    public TableDataInfo publicList(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectPublicNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息（仅已上架）
     */
    @GetMapping(value = "/public/{noticeId}")
    public AjaxResult publicGetInfo(@PathVariable Long noticeId) {
        SysNotice notice = noticeService.selectNoticeById(noticeId);
        if (notice == null || !"0".equals(notice.getStatus())) {
            return AjaxResult.success(new SysNotice());
        }
        return success(notice);
    }

    /**
     * 获取最新一条已上架公告
     */
    @GetMapping(value = "/public/latest")
    public AjaxResult publicLatest() {
        return success(noticeService.selectLatestNotice());
    }

    /**
     * 提供给 APP 查询通知公告列表（管理员视角，含所有状态，仅限 fx67ll）
     */
    @PreAuthorize("@ss.hasRole('fx67ll')")
    @GetMapping("/getNoticeListForApp")
    public TableDataInfo getNoticeListForApp(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 提供给 APP 获取通知公告详细信息（仅限 fx67ll）
     */
    @PreAuthorize("@ss.hasRole('fx67ll')")
    @GetMapping(value = "/getNoticeInfoForApp/{noticeId}")
    public AjaxResult getNoticeInfoForApp(@PathVariable("noticeId") Long noticeId) {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 提供给 APP 新增通知公告（仅限 fx67ll）
     */
    @PreAuthorize("@ss.hasRole('fx67ll')")
    @Log(title = "提供给 APP 新增通知公告", businessType = BusinessType.INSERT)
    @PostMapping("/addNoticeForApp")
    public AjaxResult addNoticeForApp(@RequestBody SysNotice notice) {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 提供给 APP 修改通知公告（仅限 fx67ll）
     */
    @PreAuthorize("@ss.hasRole('fx67ll')")
    @Log(title = "提供给 APP 修改通知公告", businessType = BusinessType.UPDATE)
    @PutMapping("/editNoticeForApp")
    public AjaxResult editNoticeForApp(@RequestBody SysNotice notice) {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 提供给 APP 删除通知公告（仅限 fx67ll）
     */
    @PreAuthorize("@ss.hasRole('fx67ll')")
    @Log(title = "提供给 APP 删除通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteNoticeByIdForApp/{noticeIds}")
    public AjaxResult deleteNoticeByIdForApp(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
