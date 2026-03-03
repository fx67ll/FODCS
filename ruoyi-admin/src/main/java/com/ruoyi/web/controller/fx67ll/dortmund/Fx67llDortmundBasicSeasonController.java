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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 赛季管理Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/dortmund/season")
public class Fx67llDortmundBasicSeasonController extends BaseController {
    @Autowired
    private IFx67llDortmundBasicSeasonService fx67llDortmundBasicSeasonService;

    /**
     * 查询赛季管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:season:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llDortmundBasicSeason> list = fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
            return getDataTable(list);
        } else {
            List<Fx67llDortmundBasicSeason> list = fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonListByUserId(fx67llDortmundBasicSeason);
            return getDataTable(list);
        }
    }

    /**
     * 导出赛季管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:season:export')")
    @Log(title = "赛季管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        List<Fx67llDortmundBasicSeason> list = fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
        ExcelUtil<Fx67llDortmundBasicSeason> util = new ExcelUtil<Fx67llDortmundBasicSeason>(Fx67llDortmundBasicSeason.class);
        util.exportExcel(response, list, "赛季管理数据");
    }

    /**
     * 获取赛季管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:season:query')")
    @GetMapping(value = "/{seasonId}")
    public AjaxResult getInfo(@PathVariable("seasonId") Long seasonId) {
        return success(fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonBySeasonId(seasonId));
    }

    /**
     * 新增赛季管理
     */
    @PreAuthorize("@ss.hasPermi('system:season:add')")
    @Log(title = "赛季管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        return toAjax(fx67llDortmundBasicSeasonService.insertFx67llDortmundBasicSeason(fx67llDortmundBasicSeason));
    }

    /**
     * 修改赛季管理
     */
    @PreAuthorize("@ss.hasPermi('system:season:edit')")
    @Log(title = "赛季管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        return toAjax(fx67llDortmundBasicSeasonService.updateFx67llDortmundBasicSeason(fx67llDortmundBasicSeason));
    }

    /**
     * 删除赛季管理
     */
    @PreAuthorize("@ss.hasPermi('system:season:remove')")
    @Log(title = "赛季管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{seasonIds}")
    public AjaxResult remove(@PathVariable Long[] seasonIds) {
        return toAjax(fx67llDortmundBasicSeasonService.deleteFx67llDortmundBasicSeasonBySeasonIds(seasonIds));
    }
}
