package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiPromptBasicSceneMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicScene;
import com.ruoyi.fx67ll.ai.service.IFx67llAiPromptBasicSceneService;

/**
 * AI Prompt场景管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiPromptBasicSceneServiceImpl implements IFx67llAiPromptBasicSceneService {
    @Autowired
    private Fx67llAiPromptBasicSceneMapper fx67llAiPromptBasicSceneMapper;

    /**
     * 查询AI Prompt场景管理
     *
     * @param sceneId AI Prompt场景管理主键
     * @return AI Prompt场景管理
     */
    @Override
    public Fx67llAiPromptBasicScene selectFx67llAiPromptBasicSceneBySceneId(Long sceneId) {
        return fx67llAiPromptBasicSceneMapper.selectFx67llAiPromptBasicSceneBySceneId(sceneId);
    }

    /**
     * 查询AI Prompt场景管理列表
     *
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return AI Prompt场景管理
     */
    @Override
    public List<Fx67llAiPromptBasicScene> selectFx67llAiPromptBasicSceneList(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        return fx67llAiPromptBasicSceneMapper.selectFx67llAiPromptBasicSceneList(fx67llAiPromptBasicScene);
    }

    /**
     * 通过 UserId 查询AI Prompt场景管理列表
     *
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return AI Prompt场景管理
     */
    @Override
    public List<Fx67llAiPromptBasicScene> selectFx67llAiPromptBasicSceneListByUserId(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        fx67llAiPromptBasicScene.setUserId(SecurityUtils.getUserId());
        return fx67llAiPromptBasicSceneMapper.selectFx67llAiPromptBasicSceneList(fx67llAiPromptBasicScene);
    }

    /**
     * 新增AI Prompt场景管理
     *
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return 结果
     */
    @Override
    public int insertFx67llAiPromptBasicScene(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        fx67llAiPromptBasicScene.setUserId(SecurityUtils.getUserId());
        fx67llAiPromptBasicScene.setCreateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicScene.setCreateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicSceneMapper.insertFx67llAiPromptBasicScene(fx67llAiPromptBasicScene);
    }

    /**
     * 修改AI Prompt场景管理
     *
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return 结果
     */
    @Override
    public int updateFx67llAiPromptBasicScene(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene) {
        fx67llAiPromptBasicScene.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiPromptBasicScene.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiPromptBasicSceneMapper.updateFx67llAiPromptBasicScene(fx67llAiPromptBasicScene);
    }

    /**
     * 批量删除AI Prompt场景管理
     *
     * @param sceneIds 需要删除的AI Prompt场景管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicSceneBySceneIds(Long[] sceneIds) {
        return fx67llAiPromptBasicSceneMapper.deleteFx67llAiPromptBasicSceneBySceneIds(sceneIds);
    }

    /**
     * 删除AI Prompt场景管理信息
     *
     * @param sceneId AI Prompt场景管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiPromptBasicSceneBySceneId(Long sceneId) {
        return fx67llAiPromptBasicSceneMapper.deleteFx67llAiPromptBasicSceneBySceneId(sceneId);
    }
}
