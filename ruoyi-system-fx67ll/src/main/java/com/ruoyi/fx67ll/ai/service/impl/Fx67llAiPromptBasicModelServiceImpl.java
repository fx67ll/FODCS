package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiPromptBasicModelMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicModel;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicModelService;

/**
 * AI Prompt模型配置Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiPromptBasicModelServiceImpl implements IFx67llAiPromptBasicModelService {
    @Autowired
    private Fx67llAiPromptBasicModelMapper fx67llAiPromptBasicModelMapper;

    /**
     * 查询AI Prompt模型配置
     *
     * @param modelId AI Prompt模型配置主键
     * @return AI Prompt模型配置
     */
    @Override
    public Fx67llAiPromptBasicModel selectFx67llAiPromptBasicModelByModelId(Long modelId) {
        return fx67llAiPromptBasicModelMapper.selectFx67llAiPromptBasicModelByModelId(modelId);
    }

    /**
     * 查询AI Prompt模型配置列表
     *
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return AI Prompt模型配置
     */
    @Override
    public List<Fx67llAiPromptBasicModel> selectFx67llAiPromptBasicModelList(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        return fx67llAiPromptBasicModelMapper.selectFx67llAiPromptBasicModelList(fx67llAiPromptBasicModel);
    }

    /**
     * 通过 UserId 查询AI Prompt模型配置列表
     *
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return AI Prompt模型配置
     */
    @Override
    public List<Fx67llAiPromptBasicModel> selectFx67llAiPromptBasicModelListByUserId(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        fx67llAiPromptBasicModel.setUserId(SecurityUtils.getUserId());
        return fx67llAiPromptBasicModelMapper.selectFx67llAiPromptBasicModelList(fx67llAiPromptBasicModel);
    }

    /**
     * 新增AI Prompt模型配置
     *
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return 结果
     */
    @Override
    public int insertFx67llAiPromptBasicModel(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        fx67llAiPromptBasicModel.setUserId(SecurityUtils.getUserId());
        fx67llAiPromptBasicModel.setCreateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicModel.setCreateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicModelMapper.insertFx67llAiPromptBasicModel(fx67llAiPromptBasicModel);
    }

    /**
     * 修改AI Prompt模型配置
     *
     * @param fx67llAiPromptBasicModel AI Prompt模型配置
     * @return 结果
     */
    @Override
    public int updateFx67llAiPromptBasicModel(Fx67llAiPromptBasicModel fx67llAiPromptBasicModel) {
        fx67llAiPromptBasicModel.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicModel.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicModelMapper.updateFx67llAiPromptBasicModel(fx67llAiPromptBasicModel);
    }

    /**
     * 批量删除AI Prompt模型配置
     *
     * @param modelIds 需要删除的AI Prompt模型配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicModelByModelIds(Long[] modelIds) {
        return fx67llAiPromptBasicModelMapper.deleteFx67llAiPromptBasicModelByModelIds(modelIds);
    }

    /**
     * 删除AI Prompt模型配置信息
     *
     * @param modelId AI Prompt模型配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicModelByModelId(Long modelId) {
        return fx67llAiPromptBasicModelMapper.deleteFx67llAiPromptBasicModelByModelId(modelId);
    }
}
