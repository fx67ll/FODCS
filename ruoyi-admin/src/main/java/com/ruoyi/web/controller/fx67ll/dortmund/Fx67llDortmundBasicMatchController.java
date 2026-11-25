package com.ruoyi.web.controller.fx67ll.dortmund;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatchExcel;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.util.DortmundExportUtils;
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
    /** 比赛导出字段白名单；JSON 保留业务字段，SQL 映射为比赛表列。 */
    private static final String[] EXPORT_COLUMNS = {
            "matchId", "matchCode", "seasonId", "homeTeamId", "awayTeamId", "matchTime", "matchVenue",
            "matchStatus", "analysisCount", "matchResult", "oddsResult", "matchRemark", "userId",
            "createBy", "createTime", "updateBy", "updateTime", "delFlag"
    };

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    /**
     * 查询比赛记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        startPage();
        List<Fx67llDortmundBasicMatch> list = fx67llDortmundBasicMatchService
                .selectFx67llDortmundBasicMatchListForCurrentUser(fx67llDortmundBasicMatch);
        return getDataTable(list);
    }

    /**
     * 导出比赛记录列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:export')")
    @Log(title = "比赛记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundBasicMatch fx67llDortmundBasicMatch,
            @RequestParam(value = "exportType", required = false, defaultValue = "excel") String exportType)
            throws IOException {
        List<Fx67llDortmundBasicMatch> list = fx67llDortmundBasicMatchService
                .selectFx67llDortmundBasicMatchListForCurrentUser(fx67llDortmundBasicMatch);
        if (DortmundExportUtils.isStructured(exportType)) {
            DortmundExportUtils.export(response, "dortmund-match", exportType,
                    "fx67ll_dortmund_match", EXPORT_COLUMNS, list);
            return;
        }
        // Excel 导出使用业务交换对象：赛季、球队展示名称/编码，便于人工核对和整理。
        List<Fx67llDortmundBasicMatchExcel> exportList = new java.util.ArrayList<Fx67llDortmundBasicMatchExcel>();
        for (Fx67llDortmundBasicMatch item : list) {
            exportList.add(new Fx67llDortmundBasicMatchExcel(item));
        }
        ExcelUtil<Fx67llDortmundBasicMatchExcel> util =
                new ExcelUtil<Fx67llDortmundBasicMatchExcel>(Fx67llDortmundBasicMatchExcel.class);
        util.exportExcel(response, exportList, "比赛记录数据");
    }

    /**
     * 批量导入前端预览并确认插入的标准 JSON 比赛数据。
     * <p>请求体只接受业务交换字段；赛季、球队、比赛编码和比赛时间仍由服务层统一校验，
     * 任一条失败整批回滚，并返回可直接定位到预览序号的错误提示。</p>
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:add')")
    @Log(title = "比赛记录 JSON 导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importJson")
    public AjaxResult importJson(@RequestBody List<Fx67llDortmundBasicMatchExcel> rows) {
        int imported = fx67llDortmundBasicMatchService.importFx67llDortmundBasicMatchJson(rows);
        return success("比赛记录导入成功，共 " + imported + " 条");
    }

    /**
     * 获取比赛记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:match:query')")
    @GetMapping(value = "/{matchId}")
    public AjaxResult getInfo(@PathVariable("matchId") Long matchId) {
        return success(fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchDetailForCurrentUser(matchId));
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
     * APP 查询比赛历史卡片列表。
     * <p>沿用当前登录用户的数据范围，不暴露管理端菜单权限，分页参数由 startPageForApp 解析。</p>
     */
    @GetMapping("/getMatchListForApp")
    public TableDataInfo getMatchListForApp(Fx67llDortmundBasicMatch query) {
        startPageForApp();
        return getDataTable(fx67llDortmundBasicMatchService
                .selectFx67llDortmundBasicMatchListForCurrentUser(query));
    }

    /**
     * APP 查询比赛详情，包含主客队、赛季和赛果信息。
     */
    @GetMapping("/getMatchInfoForApp/{matchId}")
    public AjaxResult getMatchInfoForApp(@PathVariable("matchId") Long matchId) {
        return success(fx67llDortmundBasicMatchService
                .selectFx67llDortmundBasicMatchDetailForCurrentUser(matchId));
    }

    /**
     * APP 补录或修正比赛赛果。
     * <p>只接受 matchId、matchResult、oddsResult 的业务语义，其他编排字段不会被写入。</p>
     */
    @Log(title = "APP 比赛赛果补录", businessType = BusinessType.UPDATE)
    @PostMapping("/updateMatchResultForApp")
    public AjaxResult updateMatchResultForApp(@RequestBody Fx67llDortmundBasicMatch payload) {
        if (payload == null) {
            throw new ServiceException("比赛赛果参数不能为空！");
        }
        return toAjax(fx67llDortmundBasicMatchService.updateMatchResultsForApp(
                payload.getMatchId(), payload.getMatchResult(), payload.getOddsResult()));
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
