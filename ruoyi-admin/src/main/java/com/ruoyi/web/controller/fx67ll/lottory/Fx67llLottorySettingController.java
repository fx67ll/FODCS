package com.ruoyi.web.controller.fx67ll.lottory;

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
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottorySetting;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottorySettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 固定追号配置Controller
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottory/setting")
public class Fx67llLottorySettingController extends BaseController
{
    @Autowired
    private IFx67llLottorySettingService fx67llLottorySettingService;

    /**
     * 查询固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLottorySetting fx67llLottorySetting)
    {
        startPage();
        List<Fx67llLottorySetting> list = fx67llLottorySettingService.selectFx67llLottorySettingList(fx67llLottorySetting);
        return getDataTable(list);
    }

    /**
     * 导出固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:export')")
    @Log(title = "固定追号配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLottorySetting fx67llLottorySetting)
    {
        List<Fx67llLottorySetting> list = fx67llLottorySettingService.selectFx67llLottorySettingList(fx67llLottorySetting);
        ExcelUtil<Fx67llLottorySetting> util = new ExcelUtil<Fx67llLottorySetting>(Fx67llLottorySetting.class);
        util.exportExcel(response, list, "固定追号配置数据");
    }

    /**
     * 获取固定追号配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(fx67llLottorySettingService.selectFx67llLottorySettingByUserId(userId));
    }

    /**
     * 新增固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:add')")
    @Log(title = "固定追号配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLottorySetting fx67llLottorySetting)
    {
        return toAjax(fx67llLottorySettingService.insertFx67llLottorySetting(fx67llLottorySetting));
    }

    /**
     * 修改固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:edit')")
    @Log(title = "固定追号配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLottorySetting fx67llLottorySetting)
    {
        return toAjax(fx67llLottorySettingService.updateFx67llLottorySetting(fx67llLottorySetting));
    }

    /**
     * 删除固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:setting:remove')")
    @Log(title = "固定追号配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(fx67llLottorySettingService.deleteFx67llLottorySettingByUserIds(userIds));
    }
}
