package com.ruoyi.web.controller.fx67ll.ai.prompt;

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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptTemplate;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt模板管理Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/prompt/template")
public class Fx67llAiPromptTemplateController extends BaseController {
    @Autowired
    private IFx67llAiPromptTemplateService fx67llAiPromptTemplateService;

    /**
     * 查询AI Prompt模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llAiPromptTemplate> list = fx67llAiPromptTemplateService.selectFx67llAiPromptTemplateList(fx67llAiPromptTemplate);
            return getDataTable(list);
        } else {
            List<Fx67llAiPromptTemplate> list = fx67llAiPromptTemplateService.selectFx67llAiPromptTemplateListByUserId(fx67llAiPromptTemplate);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:export')")
    @Log(title = "AI Prompt模板管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        List<Fx67llAiPromptTemplate> list = fx67llAiPromptTemplateService.selectFx67llAiPromptTemplateList(fx67llAiPromptTemplate);
        ExcelUtil<Fx67llAiPromptTemplate> util = new ExcelUtil<Fx67llAiPromptTemplate>(Fx67llAiPromptTemplate.class);
        util.exportExcel(response, list, "AI Prompt模板管理数据");
    }

    /**
     * 获取AI Prompt模板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:query')")
    @GetMapping(value = "/{promptId}")
    public AjaxResult getInfo(@PathVariable("promptId") Long promptId) {
        return success(fx67llAiPromptTemplateService.selectFx67llAiPromptTemplateByPromptId(promptId));
    }

    /**
     * 新增AI Prompt模板管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:add')")
    @Log(title = "AI Prompt模板管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        return toAjax(fx67llAiPromptTemplateService.insertFx67llAiPromptTemplate(fx67llAiPromptTemplate));
    }

    /**
     * 修改AI Prompt模板管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:edit')")
    @Log(title = "AI Prompt模板管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        return toAjax(fx67llAiPromptTemplateService.updateFx67llAiPromptTemplate(fx67llAiPromptTemplate));
    }

    /**
     * 删除AI Prompt模板管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:template:remove')")
    @Log(title = "AI Prompt模板管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{promptIds}")
    public AjaxResult remove(@PathVariable Long[] promptIds) {
        return toAjax(fx67llAiPromptTemplateService.deleteFx67llAiPromptTemplateByPromptIds(promptIds));
    }
}
