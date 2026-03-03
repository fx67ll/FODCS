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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicModel;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicModelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt模型配置Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/model")
public class Fx67llAiPromptBasicModelController extends BaseController {
    @Autowired
    private IFx67llAiPromptBasicModelService fx67llAiPromptBasicModelService;

    /**
     * 查询AI Prompt模型配置列表
     */
    @PreAuthorize("@ss.hasPermi('ai:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llAiPromptBasicModel> list = fx67llAiPromptBasicModelService.selectFx67llAiPromptBasicModelList(fx67llAiPromptBasicModel);
            return getDataTable(list);
        } else {
            List<Fx67llAiPromptBasicModel> list = fx67llAiPromptBasicModelService.selectFx67llAiPromptBasicModelListByUserId(fx67llAiPromptBasicModel);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt模型配置列表
     */
    @PreAuthorize("@ss.hasPermi('ai:model:export')")
    @Log(title = "AI Prompt模型配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        List<Fx67llAiPromptBasicModel> list = fx67llAiPromptBasicModelService.selectFx67llAiPromptBasicModelList(fx67llAiPromptBasicModel);
        ExcelUtil<Fx67llAiPromptBasicModel> util = new ExcelUtil<Fx67llAiPromptBasicModel>(Fx67llAiPromptBasicModel.class);
        util.exportExcel(response, list, "AI Prompt模型配置数据");
    }

    /**
     * 获取AI Prompt模型配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId) {
        return success(fx67llAiPromptBasicModelService.selectFx67llAiPromptBasicModelByModelId(modelId));
    }

    /**
     * 新增AI Prompt模型配置
     */
    @PreAuthorize("@ss.hasPermi('ai:model:add')")
    @Log(title = "AI Prompt模型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        return toAjax(fx67llAiPromptBasicModelService.insertFx67llAiPromptBasicModel(fx67llAiPromptBasicModel));
    }

    /**
     * 修改AI Prompt模型配置
     */
    @PreAuthorize("@ss.hasPermi('ai:model:edit')")
    @Log(title = "AI Prompt模型配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        return toAjax(fx67llAiPromptBasicModelService.updateFx67llAiPromptBasicModel(fx67llAiPromptBasicModel));
    }

    /**
     * 删除AI Prompt模型配置
     */
    @PreAuthorize("@ss.hasPermi('ai:model:remove')")
    @Log(title = "AI Prompt模型配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable Long[] modelIds) {
        return toAjax(fx67llAiPromptBasicModelService.deleteFx67llAiPromptBasicModelByModelIds(modelIds));
    }
}
