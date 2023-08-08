package com.ruoyi.web.controller.fx67ll.lottery;

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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotterySetting;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotterySettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 固定追号配置Controller
 *
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottery/setting")
public class Fx67llLotterySettingController extends BaseController {
    @Autowired
    private IFx67llLotterySettingService fx67llLotterySettingService;

    /**
     * 查询固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLotterySetting fx67llLotterySetting) {
        startPage();
        List<Fx67llLotterySetting> list = fx67llLotterySettingService.selectFx67llLotterySettingList(fx67llLotterySetting);
        return getDataTable(list);
    }

    /**
     * 导出固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:export')")
    @Log(title = "固定追号配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLotterySetting fx67llLotterySetting) {
        List<Fx67llLotterySetting> list = fx67llLotterySettingService.selectFx67llLotterySettingList(fx67llLotterySetting);
        ExcelUtil<Fx67llLotterySetting> util = new ExcelUtil<Fx67llLotterySetting>(Fx67llLotterySetting.class);
        util.exportExcel(response, list, "固定追号配置数据");
    }

    /**
     * 获取固定追号配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:query')")
    @GetMapping(value = "/{settingId}")
    public AjaxResult getInfo(@PathVariable("settingId") Long settingId) {
        return success(fx67llLotterySettingService.selectFx67llLotterySettingBySettingId(settingId));
    }

    /**
     * 提供给APP获取固定追号配置详细信息
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('lottery:setting:query')")
    @GetMapping(value = "/getLotterySettingConfigForApp/{settingId}")
    public AjaxResult getLotterySettingConfigForApp(@PathVariable("settingId") Long settingId) {
        return success(fx67llLotterySettingService.selectFx67llLotterySettingBySettingId(settingId));
    }

    /**
     * 新增固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:add')")
    @Log(title = "固定追号配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLotterySetting fx67llLotterySetting) {
        return toAjax(fx67llLotterySettingService.insertFx67llLotterySetting(fx67llLotterySetting));
    }

    /**
     * 修改固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:edit')")
    @Log(title = "固定追号配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLotterySetting fx67llLotterySetting) {
        return toAjax(fx67llLotterySettingService.updateFx67llLotterySetting(fx67llLotterySetting));
    }

    /**
     * 提供给APP修改固定追号配置
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('lottery:setting:edit')")
    @Log(title = "固定追号配置", businessType = BusinessType.UPDATE)
    @PutMapping("/editLotterySettingConfigForApp")
    public AjaxResult editLotterySettingConfigForApp(@RequestBody Fx67llLotterySetting fx67llLotterySetting) {
        return toAjax(fx67llLotterySettingService.updateFx67llLotterySetting(fx67llLotterySetting));
    }

    /**
     * 删除固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:setting:remove')")
    @Log(title = "固定追号配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{settingIds}")
    public AjaxResult remove(@PathVariable Long[] settingIds) {
        return toAjax(fx67llLotterySettingService.deleteFx67llLotterySettingBySettingIds(settingIds));
    }
}
