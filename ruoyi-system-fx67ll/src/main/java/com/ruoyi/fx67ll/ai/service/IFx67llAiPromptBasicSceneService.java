package com.ruoyi.fx67ll.ai.service;

import java.util.List;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiPromptBasicScene;

/**
 * AI Prompt场景管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llAiPromptBasicSceneService 
{
    /**
     * 查询AI Prompt场景管理
     * 
     * @param sceneId AI Prompt场景管理主键
     * @return AI Prompt场景管理
     */
    public Fx67llAiPromptBasicScene selectFx67llAiPromptBasicSceneBySceneId(Long sceneId);

    /**
     * 查询AI Prompt场景管理列表
     * 
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return AI Prompt场景管理集合
     */
    public List<Fx67llAiPromptBasicScene> selectFx67llAiPromptBasicSceneList(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene);

    /**
     * 通过 UserId 查询AI Prompt场景管理列表
     *
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return AI Prompt场景管理集合
     */
    public List<Fx67llAiPromptBasicScene> selectFx67llAiPromptBasicSceneListByUserId(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene);

    /**
     * 新增AI Prompt场景管理
     * 
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return 结果
     */
    public int insertFx67llAiPromptBasicScene(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene);

    /**
     * 修改AI Prompt场景管理
     * 
     * @param fx67llAiPromptBasicScene AI Prompt场景管理
     * @return 结果
     */
    public int updateFx67llAiPromptBasicScene(Fx67llAiPromptBasicScene fx67llAiPromptBasicScene);

    /**
     * 批量删除AI Prompt场景管理
     * 
     * @param sceneIds 需要删除的AI Prompt场景管理主键集合
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicSceneBySceneIds(Long[] sceneIds);

    /**
     * 删除AI Prompt场景管理信息
     * 
     * @param sceneId AI Prompt场景管理主键
     * @return 结果
     */
    public int deleteFx67llAiPromptBasicSceneBySceneId(Long sceneId);
}
