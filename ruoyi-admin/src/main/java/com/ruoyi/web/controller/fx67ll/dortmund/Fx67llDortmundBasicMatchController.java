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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 比赛记录Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/dortmund/match")
public class Fx67llDortmundBasicMatchController extends BaseController {
    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    /**
     * 查询比赛记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") ) {
            List<Fx67llDortmundBasicMatch> list = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchList(fx67llDortmundBasicMatch);
            return getDataTable(list);
        } else {
            List<Fx67llDortmundBasicMatch> list = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchListByUserId(fx67llDortmundBasicMatch);
            return getDataTable(list);
        }
    }

    /**
     * 导出比赛记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:export')")
    @Log(title = "比赛记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        List<Fx67llDortmundBasicMatch> list = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchList(fx67llDortmundBasicMatch);
        ExcelUtil<Fx67llDortmundBasicMatch> util = new ExcelUtil<Fx67llDortmundBasicMatch>(Fx67llDortmundBasicMatch.class);
        util.exportExcel(response, list, "比赛记录数据");
    }

    /**
     * 获取比赛记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:query')")
    @GetMapping(value = "/{matchId}")
    public AjaxResult getInfo(@PathVariable("matchId") Long matchId) {
        return success(fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchByMatchId(matchId));
    }

    /**
     * 新增比赛记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:add')")
    @Log(title = "比赛记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        return toAjax(fx67llDortmundBasicMatchService.insertFx67llDortmundBasicMatch(fx67llDortmundBasicMatch));
    }

    /**
     * 修改比赛记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:edit')")
    @Log(title = "比赛记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        return toAjax(fx67llDortmundBasicMatchService.updateFx67llDortmundBasicMatch(fx67llDortmundBasicMatch));
    }

    /**
     * 删除比赛记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:remove')")
    @Log(title = "比赛记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{matchIds}")
    public AjaxResult remove(@PathVariable Long[] matchIds) {
        return toAjax(fx67llDortmundBasicMatchService.deleteFx67llDortmundBasicMatchByMatchIds(matchIds));
    }
}
