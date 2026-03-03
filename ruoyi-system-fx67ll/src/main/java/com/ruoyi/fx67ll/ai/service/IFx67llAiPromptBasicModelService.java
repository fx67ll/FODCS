package com.ruoyi.fx67ll.ai.service;

import java.util.List;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicModel;

/**
 * AI Prompt模型配置Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llAiPromptBasicModelService 
{
    /**
     * 查询AI Prompt模型配置
     * 
     * @param modelId AI Prompt模型配置主键
     * @return AI Prompt模型配置
     */
    public Fx67llAiPromptBasicModel selectFx67llAiPromptBasicModelByModelId(Long modelId);

    /**
     * 查询AI Prompt模型配置列表
     * 
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return AI Prompt模型配置集合
     */
    public List<Fx67llAiPromptBasicModel> selectFx67llAiPromptBasicModelList(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel);

    /**
     * 通过 UserId 查询AI Prompt模型配置列表
     *
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return AI Prompt模型配置集合
     */
    public List<Fx67llAiPromptBasicModel> selectFx67llAiPromptBasicModelListByUserId(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel);

    /**
     * 新增AI Prompt模型配置
     * 
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return 结果
     */
    public int insertFx67llAiPromptBasicModel(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel);

    /**
     * 修改AI Prompt模型配置
     * 
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return 结果
     */
    public int updateFx67llAiPromptBasicModel(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel);

    /**
     * 批量删除AI Prompt模型配置
     * 
     * @param modelIds 需要删除的AI Prompt模型配置主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicModelByModelIds(Long[] modelIds);

    /**
     * 删除AI Prompt模型配置信息
     * 
     * @param modelId AI Prompt模型配置主键
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicModelByModelId(Long modelId);
}
