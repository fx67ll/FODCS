package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiPromptLimitRuleMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptLimitRule;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptLimitRuleService;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiPromptLimitRuleServiceImpl implements IFx67llAiPromptLimitRuleService {
    @Autowired
    private Fx67llAiPromptLimitRuleMapper fx67llAiPromptLimitRuleMapper;

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public Fx67llAiPromptLimitRule selectFx67llAiPromptLimitRuleByLimitRuleId(Long limitRuleId) {
        return fx67llAiPromptLimitRuleMapper.selectFx67llAiPromptLimitRuleByLimitRuleId(limitRuleId);
    }

    /**
     * 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public List<Fx67llAiPromptLimitRule> selectFx67llAiPromptLimitRuleList(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        return fx67llAiPromptLimitRuleMapper.selectFx67llAiPromptLimitRuleList(fx67llAiPromptLimitRule);
    }

    /**
     * 通过 UserId 查询AI Prompt 限流/熔断规则（适配Sentinel框架）列表
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return AI Prompt 限流/熔断规则（适配Sentinel框架）
     */
    @Override
    public List<Fx67llAiPromptLimitRule> selectFx67llAiPromptLimitRuleListByUserId(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        fx67llAiPromptLimitRule.setUserId(SecurityUtils.getUserId());
        return fx67llAiPromptLimitRuleMapper.selectFx67llAiPromptLimitRuleList(fx67llAiPromptLimitRule);
    }

    /**
     * 新增AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    @Override
    public int insertFx67llAiPromptLimitRule(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        fx67llAiPromptLimitRule.setUserId(SecurityUtils.getUserId());
        fx67llAiPromptLimitRule.setCreateBy(SecurityUtils.getUsername());
        fx67llAiPromptLimitRule.setCreateTime(DateUtils.getNowDate());
        return fx67llAiPromptLimitRuleMapper.insertFx67llAiPromptLimitRule(fx67llAiPromptLimitRule);
    }

    /**
     * 修改AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param fx67llAiPromptLimitRule AI Prompt 限流/熔断规则（适配Sentinel框架）
     * @return 结果
     */
    @Override
    public int updateFx67llAiPromptLimitRule(Fx67llAiPromptLimitRule fx67llAiPromptLimitRule) {
        fx67llAiPromptLimitRule.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiPromptLimitRule.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiPromptLimitRuleMapper.updateFx67llAiPromptLimitRule(fx67llAiPromptLimitRule);
    }

    /**
     * 批量删除AI Prompt 限流/熔断规则（适配Sentinel框架）
     *
     * @param limitRuleIds 需要删除的AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptLimitRuleByLimitRuleIds(Long[] limitRuleIds) {
        return fx67llAiPromptLimitRuleMapper.deleteFx67llAiPromptLimitRuleByLimitRuleIds(limitRuleIds);
    }

    /**
     * 删除AI Prompt 限流/熔断规则（适配Sentinel框架）信息
     *
     * @param limitRuleId AI Prompt 限流/熔断规则（适配Sentinel框架）主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptLimitRuleByLimitRuleId(Long limitRuleId) {
        return fx67llAiPromptLimitRuleMapper.deleteFx67llAiPromptLimitRuleByLimitRuleId(limitRuleId);
    }
}
