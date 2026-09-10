package com.ruoyi.web.controller.fx67ll.dortmund;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外快盈亏数值模拟轮次记录Controller
 *
 * @author fx67ll
 * @date 2026-09-10
 */
@RestController
@RequestMapping("/dortmund/extra/simulate")
public class Fx67llDortmundExtraSimulateController extends BaseController {
    @Autowired
    private IFx67llDortmundExtraSimulateService fx67llDortmundExtraSimulateService;

    /**
     * 查询外快盈亏数值模拟轮次记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        startPage();
        List<Fx67llDortmundExtraSimulate> list = fx67llDortmundExtraSimulateService.selectFx67llDortmundExtraSimulateList(fx67llDortmundExtraSimulate);
        return getDataTable(list);
    }

    /**
     * 新增外快盈亏数值模拟轮次记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:add')")
    @Log(title = "新增外快盈亏数值模拟轮次记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        return toAjax(fx67llDortmundExtraSimulateService.insertFx67llDortmundExtraSimulate(fx67llDortmundExtraSimulate));
    }

    /**
     * 删除外快盈亏数值模拟轮次记录
     */
    @PreAuthorize("@ss.hasPermi('dortmund:extra:remove')")
    @Log(title = "删除外快盈亏数值模拟轮次记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{simId}")
    public AjaxResult remove(@PathVariable Long simId) {
        return toAjax(fx67llDortmundExtraSimulateService.deleteFx67llDortmundExtraSimulateBySimId(simId));
    }
}
