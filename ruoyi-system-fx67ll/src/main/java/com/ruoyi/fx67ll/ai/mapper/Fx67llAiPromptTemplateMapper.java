package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptTemplate;

/**
 * AI Prompt模板管理Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiPromptTemplateMapper {
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
     * 删除AI Prompt模板管理
     *
     * @param promptId AI Prompt模板管理主键
     * @return 结果
     */
    public int deleteFx67llAiPromptTemplateByPromptId(Long promptId);

    /**
     * 批量删除AI Prompt模板管理
     *
     * @param promptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptTemplateByPromptIds(Long[] promptIds);
}
