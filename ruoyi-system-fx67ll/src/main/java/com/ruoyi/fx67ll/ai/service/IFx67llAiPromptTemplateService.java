package com.ruoyi.fx67ll.ai.service;

import java.util.List;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptTemplate;

/**
 * AI Prompt模板管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llAiPromptTemplateService 
{
    /**
     * 查询AI Prompt模板管理
     * 
     * @param promptId AI Prompt模板管理主键
     * @return AI Prompt模板管理
     */
    public Fx67llAiPromptTemplate selectFx67llAiPromptTemplateByPromptId(Long promptId);

    /**
     * 查询AI Prompt模板管理列表
     * 
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return AI Prompt模板管理集合
     */
    public List<Fx67llAiPromptTemplate> selectFx67llAiPromptTemplateList(Fx67llAiPromptTemplate fx67llAiPromptTemplate);

    /**
     * 通过 UserId 查询AI Prompt模板管理列表
     *
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return AI Prompt模板管理集合
     */
    public List<Fx67llAiPromptTemplate> selectFx67llAiPromptTemplateListByUserId(Fx67llAiPromptTemplate fx67llAiPromptTemplate);

    /**
     * 新增AI Prompt模板管理
     * 
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return 结果
     */
    public int insertFx67llAiPromptTemplate(Fx67llAiPromptTemplate fx67llAiPromptTemplate);

    /**
     * 修改AI Prompt模板管理
     * 
     * @param fx67llAiPromptTemplate AI Prompt模板管理
     * @return 结果
     */
    public int updateFx67llAiPromptTemplate(Fx67llAiPromptTemplate fx67llAiPromptTemplate);

    /**
     * 批量删除AI Prompt模板管理
     * 
     * @param promptIds 需要删除的AI Prompt模板管理主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptTemplateByPromptIds(Long[] promptIds);

    /**
     * 删除AI Prompt模板管理信息
     * 
     * @param promptId AI Prompt模板管理主键
     * @return 结果
     */
    public int deleteFx67llAiPromptTemplateByPromptId(Long promptId);
}
