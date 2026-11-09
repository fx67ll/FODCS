package com.ruoyi.web.controller.fx67ll.dortmund;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOddsExportResponse;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundOddsService;

/**
 * Dortmund倍率记录Controller
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@RestController
@RequestMapping("/dortmund/odds")
public class Fx67llDortmundOddsController extends BaseController {
    @Autowired
    private IFx67llDortmundOddsService fx67llDortmundOddsService;

    /**
     * 查询倍率记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundOdds query) {
        startPage();
        return getDataTable(fx67llDortmundOddsService.selectFx67llDortmundOddsList(query));
    }

    /**
     * 获取倍率记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:query')")
    @GetMapping("/{oddsId}")
    public AjaxResult getInfo(@PathVariable("oddsId") Long oddsId) {
        return success(fx67llDortmundOddsService.selectFx67llDortmundOddsByOddsId(oddsId));
    }

    /**
     * 获取比赛当前有效倍率记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:query')")
    @GetMapping("/active/{matchId}")
    public AjaxResult active(@PathVariable("matchId") Long matchId) {
        return success(fx67llDortmundOddsService.selectActiveFx67llDortmundOddsByMatchId(matchId));
    }

    /**
     * 新增倍率记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:add')")
    @Log(title = "Dortmund倍率记录", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundOdds odds) {
        return toAjax(fx67llDortmundOddsService.insertFx67llDortmundOdds(odds));
    }

    /**
     * 修改倍率记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:edit')")
    @Log(title = "Dortmund倍率记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundOdds odds) {
        return toAjax(fx67llDortmundOddsService.updateFx67llDortmundOdds(odds));
    }

    /**
     * 切换比赛当前有效倍率记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:activate')")
    @Log(title = "Dortmund倍率记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{oddsId}/activate")
    public AjaxResult activate(@PathVariable("oddsId") Long oddsId) {
        return toAjax(fx67llDortmundOddsService.activateFx67llDortmundOdds(oddsId));
    }

    /**
     * 删除倍率记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:remove')")
    @Log(title = "Dortmund倍率记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{oddsIds}")
    public AjaxResult remove(@PathVariable Long[] oddsIds) {
        return toAjax(fx67llDortmundOddsService.deleteFx67llDortmundOddsByOddsIds(oddsIds));
    }

    /**
     * 导出倍率记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:odds:export')")
    @Log(title = "Dortmund倍率记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundOdds query) {
        List<Fx67llDortmundOdds> list = fx67llDortmundOddsService.selectFx67llDortmundOddsList(query);
        List<Fx67llDortmundOddsExportResponse> exportList = new java.util.ArrayList<>();
        for (Fx67llDortmundOdds item : list) {
            exportList.add(new Fx67llDortmundOddsExportResponse(item));
        }
        ExcelUtil<Fx67llDortmundOddsExportResponse> util = new ExcelUtil<Fx67llDortmundOddsExportResponse>(Fx67llDortmundOddsExportResponse.class);
        util.exportExcel(response, exportList, "Dortmund odds data");
    }
}
