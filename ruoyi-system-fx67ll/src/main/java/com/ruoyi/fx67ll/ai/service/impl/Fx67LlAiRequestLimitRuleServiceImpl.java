package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiRequestLimitRuleMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLimitRule;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestLimitRuleService;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67LlAiRequestLimitRuleServiceImpl implements IFx67llAiRequestLimitRuleService {
    @Autowired
    private Fx67llAiRequestLimitRuleMapper fx67LlAiRequestLimitRuleMapper;

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public Fx67llAiRequestLimitRule selectFx67llAiRequestLimitRuleByLimitRuleId(Long limitRuleId) {
        return fx67LlAiRequestLimitRuleMapper.selectFx67llAiRequestLimitRuleByLimitRuleId(limitRuleId);
    }

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public List<Fx67llAiRequestLimitRule> selectFx67llAiRequestLimitRuleList(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        return fx67LlAiRequestLimitRuleMapper.selectFx67llAiRequestLimitRuleList(fx67LlAiRequestLimitRule);
    }

    /**
     * 通过 UserId 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public List<Fx67llAiRequestLimitRule> selectFx67llAiRequestLimitRuleListByUserId(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        fx67LlAiRequestLimitRule.setUserId(SecurityUtils.getUserId());
        return fx67LlAiRequestLimitRuleMapper.selectFx67llAiRequestLimitRuleList(fx67LlAiRequestLimitRule);
    }

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    @Override
    public int insertFx67llAiRequestLimitRule(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        fx67LlAiRequestLimitRule.setUserId(SecurityUtils.getUserId());
        fx67LlAiRequestLimitRule.setCreateBy(SecurityUtils.getUsername());
        fx67LlAiRequestLimitRule.setCreateTime(DateUtils.getNowDate());
        return fx67LlAiRequestLimitRuleMapper.insertFx67llAiRequestLimitRule(fx67LlAiRequestLimitRule);
    }

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67LlAiRequestLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    @Override
    public int updateFx67llAiRequestLimitRule(Fx67llAiRequestLimitRule fx67LlAiRequestLimitRule) {
        fx67LlAiRequestLimitRule.setUpdateBy(SecurityUtils.getUsername());
        fx67LlAiRequestLimitRule.setUpdateTime(DateUtils.getNowDate());
        return fx67LlAiRequestLimitRuleMapper.updateFx67llAiRequestLimitRule(fx67LlAiRequestLimitRule);
    }

    /**
     * 批量删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleIds 需要删除的AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestLimitRuleByLimitRuleIds(Long[] limitRuleIds) {
        return fx67LlAiRequestLimitRuleMapper.deleteFx67llAiRequestLimitRuleByLimitRuleIds(limitRuleIds);
    }

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）信息
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestLimitRuleByLimitRuleId(Long limitRuleId) {
        return fx67LlAiRequestLimitRuleMapper.deleteFx67llAiRequestLimitRuleByLimitRuleId(limitRuleId);
    }
}
