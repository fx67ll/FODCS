package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptLimitRule;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiPromptLimitRuleMapper {
    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    public Fx67llAiPromptLimitRule selectFx67llAiPromptLimitRuleByLimitRuleId(Long limitRuleId);

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）集合
     */
    public List<Fx67llAiPromptLimitRule> selectFx67llAiPromptLimitRuleList(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule);

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    public int insertFx67llAiPromptLimitRule(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule);

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    public int updateFx67llAiPromptLimitRule(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule);

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    public int deleteFx67llAiPromptLimitRuleByLimitRuleId(Long limitRuleId);

    /**
     * 批量删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptLimitRuleByLimitRuleIds(Long[] limitRuleIds);
}
