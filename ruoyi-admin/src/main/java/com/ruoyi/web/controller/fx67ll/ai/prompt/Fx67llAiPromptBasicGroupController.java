package com.ruoyi.web.controller.fx67ll.ai.prompt;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicGroup;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt模板分组Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/prompt/group")
public class Fx67llAiPromptBasicGroupController extends BaseController {
    @Autowired
    private IFx67llAiPromptBasicGroupService fx67llAiPromptBasicGroupService;

    /**
     * 查询AI Prompt模板分组列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llAiPromptBasicGroup> list = fx67llAiPromptBasicGroupService.selectFx67llAiPromptBasicGroupList(fx67llAiPromptBasicGroup);
            return getDataTable(list);
        } else {
            List<Fx67llAiPromptBasicGroup> list = fx67llAiPromptBasicGroupService.selectFx67llAiPromptBasicGroupListByUserId(fx67llAiPromptBasicGroup);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt模板分组列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:export')")
    @Log(title = "AI Prompt模板分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        List<Fx67llAiPromptBasicGroup> list = fx67llAiPromptBasicGroupService.selectFx67llAiPromptBasicGroupList(fx67llAiPromptBasicGroup);
        ExcelUtil<Fx67llAiPromptBasicGroup> util = new ExcelUtil<Fx67llAiPromptBasicGroup>(Fx67llAiPromptBasicGroup.class);
        util.exportExcel(response, list, "AI Prompt模板分组数据");
    }

    /**
     * 获取AI Prompt模板分组详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:query')")
    @GetMapping(value = "/{groupId}")
    public AjaxResult getInfo(@PathVariable("groupId") Long groupId) {
        return success(fx67llAiPromptBasicGroupService.selectFx67llAiPromptBasicGroupByGroupId(groupId));
    }

    /**
     * 新增AI Prompt模板分组
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:add')")
    @Log(title = "AI Prompt模板分组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        return toAjax(fx67llAiPromptBasicGroupService.insertFx67llAiPromptBasicGroup(fx67llAiPromptBasicGroup));
    }

    /**
     * 修改AI Prompt模板分组
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:edit')")
    @Log(title = "AI Prompt模板分组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        return toAjax(fx67llAiPromptBasicGroupService.updateFx67llAiPromptBasicGroup(fx67llAiPromptBasicGroup));
    }

    /**
     * 删除AI Prompt模板分组
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:group:remove')")
    @Log(title = "AI Prompt模板分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds) {
        return toAjax(fx67llAiPromptBasicGroupService.deleteFx67llAiPromptBasicGroupByGroupIds(groupIds));
    }
}
