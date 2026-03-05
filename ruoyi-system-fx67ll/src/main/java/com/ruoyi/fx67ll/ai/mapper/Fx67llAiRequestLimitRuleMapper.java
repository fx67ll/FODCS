package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLimitRule;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiRequestLimitRuleMapper {
    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    public Fx67llAiRequestLimitRule selectFx67llAiRequestLimitRuleByLimitRuleId(Long limitRuleId);

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）集合
     */
    public List<Fx67llAiRequestLimitRule> selectFx67llAiRequestLimitRuleList(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule);

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    public int insertFx67llAiRequestLimitRule(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule);

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    public int updateFx67llAiRequestLimitRule(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule);

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    public int deleteFx67llAiRequestLimitRuleByLimitRuleId(Long limitRuleId);

    /**
     * 批量删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiRequestLimitRuleByLimitRuleIds(Long[] limitRuleIds);
}
