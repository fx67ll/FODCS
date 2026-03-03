package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicGroup;

/**
 * AI Prompt模板分组Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiPromptBasicGroupMapper {
    /**
     * 查询AI Prompt模板分组
     *
     * @param groupId AI Prompt模板分组主键
     * @return AI Prompt模板分组
     */
    public Fx67llAiPromptBasicGroup selectFx67llAiPromptBasicGroupByGroupId(Long groupId);

    /**
     * 查询AI Prompt模板分组列表
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return AI Prompt模板分组集合
     */
    public List<Fx67llAiPromptBasicGroup> selectFx67llAiPromptBasicGroupList(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup);

    /**
     * 新增AI Prompt模板分组
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return 结果
     */
    public int insertFx67llAiPromptBasicGroup(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup);

    /**
     * 修改AI Prompt模板分组
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return 结果
     */
    public int updateFx67llAiPromptBasicGroup(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup);

    /**
     * 删除AI Prompt模板分组
     *
     * @param groupId AI Prompt模板分组主键
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicGroupByGroupId(Long groupId);

    /**
     * 批量删除AI Prompt模板分组
     *
     * @param groupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicGroupByGroupIds(Long[] groupIds);
}
