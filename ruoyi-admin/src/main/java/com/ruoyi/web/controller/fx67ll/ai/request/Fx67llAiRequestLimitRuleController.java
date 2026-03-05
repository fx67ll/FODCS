package com.ruoyi.web.controller.fx67ll.ai.request;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLimitRule;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestLimitRuleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/request/rule")
public class Fx67llAiRequestLimitRuleController extends BaseController {
    @Autowired
    private IFx67llAiRequestLimitRuleService Fx67llAiRequestLimitRuleService;

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llAiRequestLimitRule> list = Fx67llAiRequestLimitRuleService.selectFx67llAiRequestLimitRuleList(fx67LlAiRequestLimitRule);
            return getDataTable(list);
        } else {
            List<Fx67llAiRequestLimitRule> list = Fx67llAiRequestLimitRuleService.selectFx67llAiRequestLimitRuleListByUserId(fx67LlAiRequestLimitRule);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:export')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        List<Fx67llAiRequestLimitRule> list = Fx67llAiRequestLimitRuleService.selectFx67llAiRequestLimitRuleList(fx67LlAiRequestLimitRule);
        ExcelUtil<Fx67llAiRequestLimitRule> util = new ExcelUtil<Fx67llAiRequestLimitRule>(Fx67llAiRequestLimitRule.class);
        util.exportExcel(response, list, "AI Prompt 限流/熔断规则（适配Sentinel框架）数据");
    }

    /**
     * 获取AI Prompt 限流/熔断规则（适配Sentinel框架）详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:query')")
    @GetMapping(value = "/{limitRuleId}")
    public AjaxResult getInfo(@PathVariable("limitRuleId") Long limitRuleId) {
        return success(Fx67llAiRequestLimitRuleService.selectFx67llAiRequestLimitRuleByLimitRuleId(limitRuleId));
    }

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:add')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        return toAjax(Fx67llAiRequestLimitRuleService.insertFx67llAiRequestLimitRule(fx67LlAiRequestLimitRule));
    }

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:edit')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        return toAjax(Fx67llAiRequestLimitRuleService.updateFx67llAiRequestLimitRule(fx67LlAiRequestLimitRule));
    }

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:request:rule:remove')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.DELETE)
    @DeleteMapping("/{limitRuleIds}")
    public AjaxResult remove(@PathVariable Long[] limitRuleIds) {
        return toAjax(Fx67llAiRequestLimitRuleService.deleteFx67llAiRequestLimitRuleByLimitRuleIds(limitRuleIds));
    }
}
