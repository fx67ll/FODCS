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
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryChase;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotteryChaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 固定追号配置Controller
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottery/chase")
public class Fx67llLotteryChaseController extends BaseController
{
    @Autowired
    private IFx67llLotteryChaseService fx67llLotteryChaseService;

    /**
     * 查询固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLotteryChase fx67llLotteryChase)
    {
        startPage();
        List<Fx67llLotteryChase> list = fx67llLotteryChaseService.selectFx67llLotteryChaseList(fx67llLotteryChase);
        return getDataTable(list);
    }

    /**
     * 导出固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:export')")
    @Log(title = "固定追号配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLotteryChase fx67llLotteryChase)
    {
        List<Fx67llLotteryChase> list = fx67llLotteryChaseService.selectFx67llLotteryChaseList(fx67llLotteryChase);
        ExcelUtil<Fx67llLotteryChase> util = new ExcelUtil<Fx67llLotteryChase>(Fx67llLotteryChase.class);
        util.exportExcel(response, list, "固定追号配置数据");
    }

    /**
     * 获取固定追号配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:query')")
    @GetMapping(value = "/{chaseId}")
    public AjaxResult getInfo(@PathVariable("chaseId") Long chaseId)
    {
        return success(fx67llLotteryChaseService.selectFx67llLotteryChaseByChaseId(chaseId));
    }

    /**
     * 新增固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:add')")
    @Log(title = "固定追号配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLotteryChase fx67llLotteryChase)
    {
        return toAjax(fx67llLotteryChaseService.insertFx67llLotteryChase(fx67llLotteryChase));
    }

    /**
     * 修改固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:edit')")
    @Log(title = "固定追号配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLotteryChase fx67llLotteryChase)
    {
        return toAjax(fx67llLotteryChaseService.updateFx67llLotteryChase(fx67llLotteryChase));
    }

    /**
     * 删除固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottery:chase:remove')")
    @Log(title = "固定追号配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{chaseIds}")
    public AjaxResult remove(@PathVariable Long[] chaseIds)
    {
        return toAjax(fx67llLotteryChaseService.deleteFx67llLotteryChaseByChaseIds(chaseIds));
    }
}
