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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 球队管理Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/dortmund/team")
public class Fx67llDortmundBasicTeamController extends BaseController {
    @Autowired
    private IFx67llDortmundBasicTeamService fx67llDortmundBasicTeamService;

    /**
     * 查询球队管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llDortmundBasicTeam> list = fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
            return getDataTable(list);
        } else {
            List<Fx67llDortmundBasicTeam> list = fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamListByUserId(fx67llDortmundBasicTeam);
            return getDataTable(list);
        }
    }

    /**
     * 导出球队管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:team:export')")
    @Log(title = "球队管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        List<Fx67llDortmundBasicTeam> list = fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
        ExcelUtil<Fx67llDortmundBasicTeam> util = new ExcelUtil<Fx67llDortmundBasicTeam>(Fx67llDortmundBasicTeam.class);
        util.exportExcel(response, list, "球队管理数据");
    }

    /**
     * 获取球队管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:team:query')")
    @GetMapping(value = "/{teamId}")
    public AjaxResult getInfo(@PathVariable("teamId") Long teamId) {
        return success(fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(teamId));
    }

    /**
     * 新增球队管理
     */
    @PreAuthorize("@ss.hasPermi('system:team:add')")
    @Log(title = "球队管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        return toAjax(fx67llDortmundBasicTeamService.insertFx67llDortmundBasicTeam(fx67llDortmundBasicTeam));
    }

    /**
     * 修改球队管理
     */
    @PreAuthorize("@ss.hasPermi('system:team:edit')")
    @Log(title = "球队管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        return toAjax(fx67llDortmundBasicTeamService.updateFx67llDortmundBasicTeam(fx67llDortmundBasicTeam));
    }

    /**
     * 删除球队管理
     */
    @PreAuthorize("@ss.hasPermi('system:team:remove')")
    @Log(title = "球队管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{teamIds}")
    public AjaxResult remove(@PathVariable Long[] teamIds) {
        return toAjax(fx67llDortmundBasicTeamService.deleteFx67llDortmundBasicTeamByTeamIds(teamIds));
    }
}
