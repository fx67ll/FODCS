package com.ruoyi.web.controller.fx67ll.ai.prompt;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicGroup;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicModel;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicScene;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicSceneService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt场景管理Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/prompt/scene")
public class Fx67llAiPromptBasicSceneController extends BaseController {
    @Autowired
    private IFx67llAiPromptBasicSceneService fx67llAiPromptBasicSceneService;

    /**
     * 查询AI Prompt场景管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llAiPromptBasicScene> list = fx67llAiPromptBasicSceneService.selectFx67llAiPromptBasicSceneList(fx67llAiPromptBasicScene);
            return getDataTable(list);
        } else {
            List<Fx67llAiPromptBasicScene> list = fx67llAiPromptBasicSceneService.selectFx67llAiPromptBasicSceneListByUserId(fx67llAiPromptBasicScene);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt场景管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:export')")
    @Log(title = "AI Prompt场景管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        List<Fx67llAiPromptBasicScene> list = fx67llAiPromptBasicSceneService.selectFx67llAiPromptBasicSceneList(fx67llAiPromptBasicScene);
        ExcelUtil<Fx67llAiPromptBasicScene> util = new ExcelUtil<Fx67llAiPromptBasicScene>(Fx67llAiPromptBasicScene.class);
        util.exportExcel(response, list, "AI Prompt场景管理数据");
    }

    /**
     * 获取AI Prompt场景管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:query')")
    @GetMapping(value = "/{sceneId}")
    public AjaxResult getInfo(@PathVariable("sceneId") Long sceneId) {
        return success(fx67llAiPromptBasicSceneService.selectFx67llAiPromptBasicSceneBySceneId(sceneId));
    }

    /**
     * 新增AI Prompt场景管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:add')")
    @Log(title = "AI Prompt场景管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        return toAjax(fx67llAiPromptBasicSceneService.insertFx67llAiPromptBasicScene(fx67llAiPromptBasicScene));
    }

    /**
     * 修改AI Prompt场景管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:edit')")
    @Log(title = "AI Prompt场景管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        return toAjax(fx67llAiPromptBasicSceneService.updateFx67llAiPromptBasicScene(fx67llAiPromptBasicScene));
    }

    /**
     * 删除AI Prompt场景管理
     */
    @PreAuthorize("@ss.hasPermi('ai:prompt:scene:remove')")
    @Log(title = "AI Prompt场景管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sceneIds}")
    public AjaxResult remove(@PathVariable Long[] sceneIds) {
        return toAjax(fx67llAiPromptBasicSceneService.deleteFx67llAiPromptBasicSceneBySceneIds(sceneIds));
    }
}
