package com.ruoyi.web.controller.fx67ll.dortmund;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointExcel;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import com.ruoyi.fx67ll.dortmund.util.DortmundExportUtils;
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
    /** 信息点导出字段白名单，T/M 类型均使用同一业务表。 */
    private static final String[] EXPORT_COLUMNS = {
            "infoPointId", "infoPointType", "seasonId", "matchId", "pointTitle", "pointContent",
            "sourceName", "sourceUrl", "sourcePublishTime", "collectTime", "effectiveStartTime",
            "effectiveEndTime", "pointStatus", "allowPublic", "pointRemark", "userId",
            "createBy", "createTime", "updateBy", "updateTime"
    };

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
     * 导出统一信息点。
     * <p>T 类型会补齐有效球队编码，确保导出的文件可以直接修改后重新导入。</p>
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:list')")
    @Log(title = "信息点导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundInfoPoint query,
            @RequestParam(value = "exportType", required = false, defaultValue = "excel") String exportType)
            throws IOException {
        List<Fx67llDortmundInfoPoint> list =
                fx67llDortmundInfoPointService.selectFx67llDortmundInfoPointExportList(query);
        if (DortmundExportUtils.isStructured(exportType)) {
            DortmundExportUtils.export(response, "dortmund-info-point", exportType,
                    "fx67ll_dortmund_info_point", EXPORT_COLUMNS, list);
            return;
        }
        List<Fx67llDortmundInfoPointExcel> exportList = new java.util.ArrayList<Fx67llDortmundInfoPointExcel>();
        for (Fx67llDortmundInfoPoint item : list) {
            exportList.add(new Fx67llDortmundInfoPointExcel(item));
        }
        ExcelUtil<Fx67llDortmundInfoPointExcel> util =
                new ExcelUtil<Fx67llDortmundInfoPointExcel>(Fx67llDortmundInfoPointExcel.class);
        util.exportExcel(response, exportList, "信息点数据");
    }

    /**
     * 批量导入前端预览并确认的标准 JSON 信息点。
     * <p>
     * T 类型使用赛季名称和球队编码建立关系，M 类型使用比赛编码归属比赛；
     * 服务层先解析并校验全部条目，再统一写入，任一条失败整批回滚并返回带条目序号的原因。
     * Excel 仅保留导出能力，信息点导入只接受 JSON。
     * </p>
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:add')")
    @Log(title = "信息点 JSON 导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importJson")
    public AjaxResult importJson(@RequestBody List<Fx67llDortmundInfoPointExcel> rows) {
        int imported = fx67llDortmundInfoPointService.importFx67llDortmundInfoPointJson(rows);
        return success("信息点导入成功，共 " + imported + " 条");
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
     * 删除统一信息点
     */
    @PreAuthorize("@ss.hasPermi('dortmund:info:point:remove')")
    @Log(title = "统一信息点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoPointIds}")
    public AjaxResult remove(@PathVariable Long[] infoPointIds) {
        return toAjax(fx67llDortmundInfoPointService.deleteFx67llDortmundInfoPointByInfoPointIds(infoPointIds));
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
