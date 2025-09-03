package com.ruoyi.web.controller.fx67ll.note;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLog;
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
import com.ruoyi.fx67ll.note.domain.Fx67llNoteLog;
import com.ruoyi.fx67ll.note.service.IFx67llNoteLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 富文本记录Controller
 *
 * @author ruoyi
 * @date 2025-08-26
 */
@RestController
@RequestMapping("/note/log")
public class Fx67llNoteLogController extends BaseController {
    @Autowired
    private IFx67llNoteLogService fx67llNoteLogService;

    /**
     * 查询富文本记录列表
     */
    @PreAuthorize("@ss.hasPermi('note:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llNoteLog fx67llNoteLog) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llNoteLog> list = fx67llNoteLogService.selectFx67llNoteLogList(fx67llNoteLog);
            return getDataTable(list);
        } else {
            List<Fx67llNoteLog> list = fx67llNoteLogService.selectFx67llNoteLogListByUserId(fx67llNoteLog);
            return getDataTable(list);
        }
    }

    /**
     * 提供给 APP 查询富文本记录列表
     */
    @PreAuthorize("@ss.hasPermi('note:log:list')")
    @GetMapping("/getNoteLogListForApp")
    public TableDataInfo getNoteLogListForApp(Fx67llNoteLog fx67llNoteLog) {
        startPage();
        List<Fx67llNoteLog> list = fx67llNoteLogService.selectFx67llNoteLogListByUserId(fx67llNoteLog);
        return getDataTable(list);
    }


    /**
     * 导出富文本记录列表
     */
    @PreAuthorize("@ss.hasPermi('note:log:export')")
    @Log(title = "导出富文本记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llNoteLog fx67llNoteLog) {
        List<Fx67llNoteLog> list = fx67llNoteLogService.selectFx67llNoteLogList(fx67llNoteLog);
        ExcelUtil<Fx67llNoteLog> util = new ExcelUtil<Fx67llNoteLog>(Fx67llNoteLog.class);
        util.exportExcel(response, list, "富文本记录数据");
    }

    /**
     * 获取富文本记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('note:log:query')")
    @GetMapping(value = "/{noteId}")
    public AjaxResult getInfo(@PathVariable("noteId") Long noteId) {
        return success(fx67llNoteLogService.selectFx67llNoteLogByNoteId(noteId));
    }

    /**
     * 新增富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:add')")
    @Log(title = "新增富文本记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llNoteLog fx67llNoteLog) {
        return toAjax(fx67llNoteLogService.insertFx67llNoteLog(fx67llNoteLog));
    }

    /**
     * 提供给 APP 新增富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:add')")
    @Log(title = "提供给 APP 新增富文本记录", businessType = BusinessType.INSERT)
    @PostMapping("/addNoteLogForApp")
    public AjaxResult addNoteLogForApp(@RequestBody Fx67llNoteLog fx67llNoteLog) {
        return toAjax(fx67llNoteLogService.insertFx67llNoteLog(fx67llNoteLog));
    }

    /**
     * 修改富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:edit')")
    @Log(title = "修改富文本记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llNoteLog fx67llNoteLog) {
        return toAjax(fx67llNoteLogService.updateFx67llNoteLog(fx67llNoteLog));
    }

    /**
     * 提供给 APP 修改富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:edit')")
    @Log(title = "提供给 APP 修改富文本记录", businessType = BusinessType.UPDATE)
    @PutMapping("/editNoteLogForApp")
    public AjaxResult editNoteLogForApp(@RequestBody Fx67llNoteLog fx67llNoteLog) {
        if (fx67llNoteLogService.selectFx67llNoteLogByNoteId(fx67llNoteLog.getNoteId()).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止修改！");
        } else {
            return toAjax(fx67llNoteLogService.updateFx67llNoteLog(fx67llNoteLog));
        }
    }

    /**
     * 删除富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:remove')")
    @Log(title = "删除富文本记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noteIds}")
    public AjaxResult remove(@PathVariable Long[] noteIds) {
        return toAjax(fx67llNoteLogService.deleteFx67llNoteLogByNoteIds(noteIds));
    }

    /**
     * 提供给 APP 删除富文本记录
     */
    @PreAuthorize("@ss.hasPermi('note:log:remove')")
    @Log(title = "提供给 APP 删除富文本记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteNoteLogByIdForApp/{noteIds}")
    public AjaxResult deleteNoteLogByIdForApp(@PathVariable Long noteId) {
        if (fx67llNoteLogService.selectFx67llNoteLogByNoteId(noteId).getUserId() != SecurityUtils.getUserId()) {
            return warn("此记录非本人创建，禁止删除！");
        } else {
            return toAjax(fx67llNoteLogService.deleteFx67llNoteLogByNoteId(noteId));
        }
    }
}
