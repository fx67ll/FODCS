package com.ruoyi.web.controller.fx67ll.ai.prompt;

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
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptLimitRule;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptLimitRuleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Controller
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@RestController
@RequestMapping("/ai/rule")
public class Fx67llAiPromptLimitRuleController extends BaseController {
    @Autowired
    private IFx67llAiPromptLimitRuleService fx67llAiPromptLimitRuleService;

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll") || SecurityUtils.getUsername().equals("chaoshen")) {
            List<Fx67llAiPromptLimitRule> list = fx67llAiPromptLimitRuleService.selectFx67llAiPromptLimitRuleList(fx67llAiPromptLimitRule);
            return getDataTable(list);
        } else {
            List<Fx67llAiPromptLimitRule> list = fx67llAiPromptLimitRuleService.selectFx67llAiPromptLimitRuleListByUserId(fx67llAiPromptLimitRule);
            return getDataTable(list);
        }
    }

    /**
     * 导出AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:export')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        List<Fx67llAiPromptLimitRule> list = fx67llAiPromptLimitRuleService.selectFx67llAiPromptLimitRuleList(fx67llAiPromptLimitRule);
        ExcelUtil<Fx67llAiPromptLimitRule> util = new ExcelUtil<Fx67llAiPromptLimitRule>(Fx67llAiPromptLimitRule.class);
        util.exportExcel(response, list, "AI Prompt 限流/熔断规则（适配Sentinel框架）数据");
    }

    /**
     * 获取AI Prompt 限流/熔断规则（适配Sentinel框架）详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:query')")
    @GetMapping(value = "/{limitRuleId}")
    public AjaxResult getInfo(@PathVariable("limitRuleId") Long limitRuleId) {
        return success(fx67llAiPromptLimitRuleService.selectFx67llAiPromptLimitRuleByLimitRuleId(limitRuleId));
    }

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:add')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        return toAjax(fx67llAiPromptLimitRuleService.insertFx67llAiPromptLimitRule(fx67llAiPromptLimitRule));
    }

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:edit')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        return toAjax(fx67llAiPromptLimitRuleService.updateFx67llAiPromptLimitRule(fx67llAiPromptLimitRule));
    }

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @PreAuthorize("@ss.hasPermi('ai:rule:remove')")
    @Log(title = "AI Prompt 限流/熔断规则（适配Sentinel框架）", businessType = BusinessType.DELETE)
    @DeleteMapping("/{limitRuleIds}")
    public AjaxResult remove(@PathVariable Long[] limitRuleIds) {
        return toAjax(fx67llAiPromptLimitRuleService.deleteFx67llAiPromptLimitRuleByLimitRuleIds(limitRuleIds));
    }
}
