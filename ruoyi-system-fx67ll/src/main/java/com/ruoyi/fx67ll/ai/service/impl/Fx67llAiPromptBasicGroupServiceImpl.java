package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiPromptBasicGroupMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicGroup;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicGroupService;

/**
 * AI Prompt模板分组Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiPromptBasicGroupServiceImpl implements IFx67llAiPromptBasicGroupService {
    @Autowired
    private Fx67llAiPromptBasicGroupMapper fx67llAiPromptBasicGroupMapper;

    /**
     * 查询AI Prompt模板分组
     *
     * @param groupId AI Prompt模板分组主键
     * @return AI Prompt模板分组
     */
    @Override
    public Fx67llAiPromptBasicGroup selectFx67llAiPromptBasicGroupByGroupId(Long groupId) {
        return fx67llAiPromptBasicGroupMapper.selectFx67llAiPromptBasicGroupByGroupId(groupId);
    }

    /**
     * 查询AI Prompt模板分组列表
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return AI Prompt模板分组
     */
    @Override
    public List<Fx67llAiPromptBasicGroup> selectFx67llAiPromptBasicGroupList(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        return fx67llAiPromptBasicGroupMapper.selectFx67llAiPromptBasicGroupList(fx67llAiPromptBasicGroup);
    }

    /**
     * 通过 UserId 查询AI Prompt模板分组列表
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return AI Prompt模板分组
     */
    @Override
    public List<Fx67llAiPromptBasicGroup> selectFx67llAiPromptBasicGroupListByUserId(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        fx67llAiPromptBasicGroup.setUserId(SecurityUtils.getUserId());
        return fx67llAiPromptBasicGroupMapper.selectFx67llAiPromptBasicGroupList(fx67llAiPromptBasicGroup);
    }

    /**
     * 新增AI Prompt模板分组
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return 结果
     */
    @Override
    public int insertFx67llAiPromptBasicGroup(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        fx67llAiPromptBasicGroup.setUserId(SecurityUtils.getUserId());
        fx67llAiPromptBasicGroup.setCreateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicGroup.setCreateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicGroupMapper.insertFx67llAiPromptBasicGroup(fx67llAiPromptBasicGroup);
    }

    /**
     * 修改AI Prompt模板分组
     *
     * @param fx67llAiPromptBasicGroup AI Prompt模板分组
     * @return 结果
     */
    @Override
    public int updateFx67llAiPromptBasicGroup(Fx67llAiPromptBasicGroup fx67llAiPromptBasicGroup) {
        fx67llAiPromptBasicGroup.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicGroup.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicGroupMapper.updateFx67llAiPromptBasicGroup(fx67llAiPromptBasicGroup);
    }

    /**
     * 批量删除AI Prompt模板分组
     *
     * @param groupIds 需要删除的AI Prompt模板分组主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicGroupByGroupIds(Long[] groupIds) {
        return fx67llAiPromptBasicGroupMapper.deleteFx67llAiPromptBasicGroupByGroupIds(groupIds);
    }

    /**
     * 删除AI Prompt模板分组信息
     *
     * @param groupId AI Prompt模板分组主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicGroupByGroupId(Long groupId) {
        return fx67llAiPromptBasicGroupMapper.deleteFx67llAiPromptBasicGroupByGroupId(groupId);
    }
}
