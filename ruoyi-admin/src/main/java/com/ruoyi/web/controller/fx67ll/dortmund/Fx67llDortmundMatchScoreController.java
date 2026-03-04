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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchScore;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundMatchScoreService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 比赛标准化评分Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/dortmund/match/score")
public class Fx67llDortmundMatchScoreController extends BaseController {
    @Autowired
    private IFx67llDortmundMatchScoreService fx67llDortmundMatchScoreService;

    /**
     * 查询比赛标准化评分列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") ) {
            List<Fx67llDortmundMatchScore> list = fx67llDortmundMatchScoreService.selectFx67llDortmundMatchScoreList(fx67llDortmundMatchScore);
            return getDataTable(list);
        } else {
            List<Fx67llDortmundMatchScore> list = fx67llDortmundMatchScoreService.selectFx67llDortmundMatchScoreListByUserId(fx67llDortmundMatchScore);
            return getDataTable(list);
        }
    }

    /**
     * 导出比赛标准化评分列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:export')")
    @Log(title = "比赛标准化评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        List<Fx67llDortmundMatchScore> list = fx67llDortmundMatchScoreService.selectFx67llDortmundMatchScoreList(fx67llDortmundMatchScore);
        ExcelUtil<Fx67llDortmundMatchScore> util = new ExcelUtil<Fx67llDortmundMatchScore>(Fx67llDortmundMatchScore.class);
        util.exportExcel(response, list, "比赛标准化评分数据");
    }

    /**
     * 获取比赛标准化评分详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:query')")
    @GetMapping(value = "/{scoreId}")
    public AjaxResult getInfo(@PathVariable("scoreId") Long scoreId) {
        return success(fx67llDortmundMatchScoreService.selectFx67llDortmundMatchScoreByScoreId(scoreId));
    }

    /**
     * 新增比赛标准化评分
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:add')")
    @Log(title = "比赛标准化评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        return toAjax(fx67llDortmundMatchScoreService.insertFx67llDortmundMatchScore(fx67llDortmundMatchScore));
    }

    /**
     * 修改比赛标准化评分
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:edit')")
    @Log(title = "比赛标准化评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        return toAjax(fx67llDortmundMatchScoreService.updateFx67llDortmundMatchScore(fx67llDortmundMatchScore));
    }

    /**
     * 删除比赛标准化评分
     */
    @PreAuthorize("@ss.hasPermi('dortmund:score:remove')")
    @Log(title = "比赛标准化评分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scoreIds}")
    public AjaxResult remove(@PathVariable Long[] scoreIds) {
        return toAjax(fx67llDortmundMatchScoreService.deleteFx67llDortmundMatchScoreByScoreIds(scoreIds));
    }
}
