package com.ruoyi.fx67ll.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI Prompt模板管理对象 fx67ll_ai_prompt_template
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiPromptTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模板唯一标识（主键）
     */
    private Long promptId;

    /**
     * 模板业务名称
     */
    @Excel(name = "模板业务名称")
    private String promptName;

    /**
     * 所属分组ID（外键，关联fx67ll_ai_prompt_group.group_id，强制约束模板与分组的归属关系）
     */
    @Excel(name = "所属分组ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_group.group_id，强制约束模板与分组的归属关系")
    private Long groupId;

    /**
     * 所属场景ID（外键，关联fx67ll_ai_prompt_scene.scene_id，定义模板的业务应用场景）
     */
    @Excel(name = "所属场景ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_scene.scene_id，定义模板的业务应用场景")
    private Long sceneId;

    /**
     * 默认绑定模型ID（外键，关联fx67ll_ai_prompt_model.model_id，指定模板调用的AI模型）
     */
    @Excel(name = "默认绑定模型ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_model.model_id，指定模板调用的AI模型")
    private Long modelId;

    /**
     * Prompt模板主体内容（含变量占位符，如{{team_name}}）
     */
    @Excel(name = "Prompt模板主体内容", readConverterExp = "含=变量占位符，如{{team_name}}")
    private String promptContent;

    /**
     * 模板变量元数据配置（JSON格式，定义变量名、类型、校验规则、示例值等）
     */
    @Excel(name = "模板变量元数据配置", readConverterExp = "J=SON格式，定义变量名、类型、校验规则、示例值等")
    private String promptVariableConfig;

    /**
     * 模型调用参数覆盖配置（JSON格式，优先级高于模型表默认参数）
     */
    @Excel(name = "模型调用参数覆盖配置", readConverterExp = "J=SON格式，优先级高于模型表默认参数")
    private String promptCustomConfigParams;

    /**
     * 模板启用状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "模板启用状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String promptStatus;

    /**
     * 模板业务备注（说明使用场景、注意事项等）
     */
    @Excel(name = "模板业务备注", readConverterExp = "说=明使用场景、注意事项等")
    private String promptRemark;

    /**
     * 逻辑删除标志（字典码：0-存在，2-已删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 创建开始时间
     */
    private String beginCreateTime;

    /**
     * 创建结束时间
     */
    private String endCreateTime;

    /**
     * 更新开始时间
     */
    private String beginUpdateTime;

    /**
     * 更新结束时间
     */
    private String endUpdateTime;

    public void setPromptId(Long promptId) {
        this.promptId = promptId;
    }

    public Long getPromptId() {
        return promptId;
    }

    public void setPromptName(String promptName) {
        this.promptName = promptName;
    }

    public String getPromptName() {
        return promptName;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setPromptContent(String promptContent) {
        this.promptContent = promptContent;
    }

    public String getPromptContent() {
        return promptContent;
    }

    public void setPromptVariableConfig(String promptVariableConfig) {
        this.promptVariableConfig = promptVariableConfig;
    }

    public String getPromptVariableConfig() {
        return promptVariableConfig;
    }

    public void setPromptCustomConfigParams(String promptCustomConfigParams) {
        this.promptCustomConfigParams = promptCustomConfigParams;
    }

    public String getPromptCustomConfigParams() {
        return promptCustomConfigParams;
    }

    public void setPromptStatus(String promptStatus) {
        this.promptStatus = promptStatus;
    }

    public String getPromptStatus() {
        return promptStatus;
    }

    public void setPromptRemark(String promptRemark) {
        this.promptRemark = promptRemark;
    }

    public String getPromptRemark() {
        return promptRemark;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getBeginUpdateTime() {
        return beginUpdateTime;
    }

    public void setBeginUpdateTime(String beginUpdateTime) {
        this.beginUpdateTime = beginUpdateTime;
    }

    public String getEndUpdateTime() {
        return endUpdateTime;
    }

    public void setEndUpdateTime(String endUpdateTime) {
        this.endUpdateTime = endUpdateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("promptId", getPromptId())
                .append("promptName", getPromptName())
                .append("groupId", getGroupId())
                .append("sceneId", getSceneId())
                .append("modelId", getModelId())
                .append("promptContent", getPromptContent())
                .append("promptVariableConfig", getPromptVariableConfig())
                .append("promptCustomConfigParams", getPromptCustomConfigParams())
                .append("promptStatus", getPromptStatus())
                .append("promptRemark", getPromptRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
