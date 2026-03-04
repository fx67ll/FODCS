package com.ruoyi.web.controller.fx67ll.dortmund;

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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchAnalysis;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundMatchAnalysisService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 比赛AI分析原始结果Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/dortmund/match/analysis")
public class Fx67llDortmundMatchAnalysisController extends BaseController {
    @Autowired
    private IFx67llDortmundMatchAnalysisService fx67llDortmundMatchAnalysisService;

    /**
     * 查询比赛AI分析原始结果列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") ) {
            List<Fx67llDortmundMatchAnalysis> list = fx67llDortmundMatchAnalysisService.selectFx67llDortmundMatchAnalysisList(fx67llDortmundMatchAnalysis);
            return getDataTable(list);
        } else {
            List<Fx67llDortmundMatchAnalysis> list = fx67llDortmundMatchAnalysisService.selectFx67llDortmundMatchAnalysisListByUserId(fx67llDortmundMatchAnalysis);
            return getDataTable(list);
        }
    }

    /**
     * 导出比赛AI分析原始结果列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:export')")
    @Log(title = "比赛AI分析原始结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        List<Fx67llDortmundMatchAnalysis> list = fx67llDortmundMatchAnalysisService.selectFx67llDortmundMatchAnalysisList(fx67llDortmundMatchAnalysis);
        ExcelUtil<Fx67llDortmundMatchAnalysis> util = new ExcelUtil<Fx67llDortmundMatchAnalysis>(Fx67llDortmundMatchAnalysis.class);
        util.exportExcel(response, list, "比赛AI分析原始结果数据");
    }

    /**
     * 获取比赛AI分析原始结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:query')")
    @GetMapping(value = "/{analysisId}")
    public AjaxResult getInfo(@PathVariable("analysisId") Long analysisId) {
        return success(fx67llDortmundMatchAnalysisService.selectFx67llDortmundMatchAnalysisByAnalysisId(analysisId));
    }

    /**
     * 新增比赛AI分析原始结果
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:add')")
    @Log(title = "比赛AI分析原始结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        return toAjax(fx67llDortmundMatchAnalysisService.insertFx67llDortmundMatchAnalysis(fx67llDortmundMatchAnalysis));
    }

    /**
     * 修改比赛AI分析原始结果
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:edit')")
    @Log(title = "比赛AI分析原始结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        return toAjax(fx67llDortmundMatchAnalysisService.updateFx67llDortmundMatchAnalysis(fx67llDortmundMatchAnalysis));
    }

    /**
     * 删除比赛AI分析原始结果
     */
    @PreAuthorize("@ss.hasPermi('dortmund:analysis:remove')")
    @Log(title = "比赛AI分析原始结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{analysisIds}")
    public AjaxResult remove(@PathVariable Long[] analysisIds) {
        return toAjax(fx67llDortmundMatchAnalysisService.deleteFx67llDortmundMatchAnalysisByAnalysisIds(analysisIds));
    }
}
