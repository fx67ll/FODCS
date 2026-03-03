package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiPromptTemplateMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptTemplate;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptTemplateService;

/**
 * AI Prompt模板管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiPromptTemplateServiceImpl implements IFx67llAiPromptTemplateService {
    @Autowired
    private Fx67llAiPromptTemplateMapper fx67llAiPromptTemplateMapper;

    /**
     * 查询AI Prompt模板管理
     *
     * @param promptId AI Prompt模板管理主键
     * @return AI Prompt模板管理
     */
    @Override
    public Fx67llAiPromptTemplate selectFx67llAiPromptTemplateByPromptId(Long promptId) {
        return fx67llAiPromptTemplateMapper.selectFx67llAiPromptTemplateByPromptId(promptId);
    }

    /**
     * 查询AI Prompt模板管理列表
     *
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return AI Prompt模板管理
     */
    @Override
    public List<Fx67llAiPromptTemplate> selectFx67llAiPromptTemplateList(Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        return fx67llAiPromptTemplateMapper.selectFx67llAiPromptTemplateList(fx67llAiPromptTemplate);
    }

    /**
     * 通过 UserId 查询AI Prompt模板管理列表
     *
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return AI Prompt模板管理
     */
    @Override
    public List<Fx67llAiPromptTemplate> selectFx67llAiPromptTemplateListByUserId(Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        fx67llAiPromptTemplate.setUserId(SecurityUtils.getUserId());
        return fx67llAiPromptTemplateMapper.selectFx67llAiPromptTemplateList(fx67llAiPromptTemplate);
    }

    /**
     * 新增AI Prompt模板管理
     *
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return 结果
     */
    @Override
    public int insertFx67llAiPromptTemplate(Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        fx67llAiPromptTemplate.setUserId(SecurityUtils.getUserId());
        fx67llAiPromptTemplate.setCreateBy(SecurityUtils.getUsername());
        fx67llAiPromptTemplate.setCreateTime(DateUtils.getNowDate());
        return fx67llAiPromptTemplateMapper.insertFx67llAiPromptTemplate(fx67llAiPromptTemplate);
    }

    /**
     * 修改AI Prompt模板管理
     *
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return 结果
     */
    @Override
    public int updateFx67llAiPromptTemplate(Fx67llAiPromptTemplate fx67llAiPromptTemplate) {
        fx67llAiPromptTemplate.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiPromptTemplate.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiPromptTemplateMapper.updateFx67llAiPromptTemplate(fx67llAiPromptTemplate);
    }

    /**
     * 批量删除AI Prompt模板管理
     *
     * @param promptIds 需要删除的AI Prompt模板管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptTemplateByPromptIds(Long[] promptIds) {
        return fx67llAiPromptTemplateMapper.deleteFx67llAiPromptTemplateByPromptIds(promptIds);
    }

    /**
     * 删除AI Prompt模板管理信息
     *
     * @param promptId AI Prompt模板管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptTemplateByPromptId(Long promptId) {
        return fx67llAiPromptTemplateMapper.deleteFx67llAiPromptTemplateByPromptId(promptId);
    }
}
