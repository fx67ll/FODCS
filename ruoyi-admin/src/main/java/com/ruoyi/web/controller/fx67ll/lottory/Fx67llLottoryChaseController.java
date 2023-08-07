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
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryChase;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottoryChaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 固定追号配置Controller
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/lottory/chase")
public class Fx67llLottoryChaseController extends BaseController
{
    @Autowired
    private IFx67llLottoryChaseService fx67llLottoryChaseService;

    /**
     * 查询固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llLottoryChase fx67llLottoryChase)
    {
        startPage();
        List<Fx67llLottoryChase> list = fx67llLottoryChaseService.selectFx67llLottoryChaseList(fx67llLottoryChase);
        return getDataTable(list);
    }

    /**
     * 导出固定追号配置列表
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:export')")
    @Log(title = "固定追号配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llLottoryChase fx67llLottoryChase)
    {
        List<Fx67llLottoryChase> list = fx67llLottoryChaseService.selectFx67llLottoryChaseList(fx67llLottoryChase);
        ExcelUtil<Fx67llLottoryChase> util = new ExcelUtil<Fx67llLottoryChase>(Fx67llLottoryChase.class);
        util.exportExcel(response, list, "固定追号配置数据");
    }

    /**
     * 获取固定追号配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:query')")
    @GetMapping(value = "/{chaseId}")
    public AjaxResult getInfo(@PathVariable("chaseId") Long chaseId)
    {
        return success(fx67llLottoryChaseService.selectFx67llLottoryChaseByChaseId(chaseId));
    }

    /**
     * 新增固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:add')")
    @Log(title = "固定追号配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llLottoryChase fx67llLottoryChase)
    {
        return toAjax(fx67llLottoryChaseService.insertFx67llLottoryChase(fx67llLottoryChase));
    }

    /**
     * 修改固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:edit')")
    @Log(title = "固定追号配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llLottoryChase fx67llLottoryChase)
    {
        return toAjax(fx67llLottoryChaseService.updateFx67llLottoryChase(fx67llLottoryChase));
    }

    /**
     * 删除固定追号配置
     */
    @PreAuthorize("@ss.hasPermi('lottory:chase:remove')")
    @Log(title = "固定追号配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{chaseIds}")
    public AjaxResult remove(@PathVariable Long[] chaseIds)
    {
        return toAjax(fx67llLottoryChaseService.deleteFx67llLottoryChaseByChaseIds(chaseIds));
    }
}
