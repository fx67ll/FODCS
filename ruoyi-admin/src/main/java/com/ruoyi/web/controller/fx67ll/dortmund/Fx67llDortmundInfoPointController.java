package com.ruoyi.web.controller.fx67ll.dortmund;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 统一信息点Controller
 *
 * @author fx67ll
 * @date 2026-10-29
 */
@RestController
@RequestMapping("/dortmund/info/point")
public class Fx67llDortmundInfoPointController extends BaseController {
    @Autowired
    private IFx67llDortmundInfoPointService fx67llDortmundInfoPointService;

    /**
     * 查询统一信息点列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        startPage();
        List<Fx67llDortmundInfoPoint> list = fx67llDortmundInfoPointService.selectFx67llDortmundInfoPointList(fx67llDortmundInfoPoint);
        return getDataTable(list);
    }

    /**
     * 获取统一信息点详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:query')")
    @GetMapping(value = "/{infoPointId}")
    public AjaxResult getInfo(@PathVariable("infoPointId") Long infoPointId) {
        return success(fx67llDortmundInfoPointService.selectFx67llDortmundInfoPointByInfoPointId(infoPointId));
    }

    /**
     * 新增统一信息点
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:add')")
    @Log(title = "信息点", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        return toAjax(fx67llDortmundInfoPointService.insertFx67llDortmundInfoPoint(fx67llDortmundInfoPoint));
    }

    /**
     * 修改统一信息点
     * <p>
     * 类型、赛季、比赛归属和状态不可通过本接口修改
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:edit')")
    @Log(title = "信息点", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        return toAjax(fx67llDortmundInfoPointService.updateFx67llDortmundInfoPoint(fx67llDortmundInfoPoint));
    }

    /**
     * 修改信息点状态
     * <p>
     * 轻量动作对象：信息点主键 + 目标状态，条件更新，影响行数不是 1 时按已处理、状态变化或记录不存在返回
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:changeStatus')")
    @Log(title = "信息点状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        return toAjax(fx67llDortmundInfoPointService.changeFx67llDortmundInfoPointStatus(fx67llDortmundInfoPoint));
    }

    /**
     * 查询比赛的有效信息点
     * <p>
     * 返回该比赛当前会进入后续分析的信息点与被排除信息点，每行含有效标记与排除原因
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:query')")
    @GetMapping("/valid/{matchId}")
    public AjaxResult valid(@PathVariable("matchId") Long matchId) {
        return success(fx67llDortmundInfoPointService.selectFx67llDortmundInfoPointValidListByMatchId(matchId));
    }
}
