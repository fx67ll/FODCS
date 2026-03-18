package com.ruoyi.fx67ll.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI Prompt场景管理对象 fx67ll_ai_prompt_scene
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiPromptBasicScene extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 场景唯一标识（主键）
     */
    private Long sceneId;

    /**
     * 场景业务编码（唯一，用于程序逻辑关联）
     */
    @Excel(name = "场景业务编码", readConverterExp = "唯=一，用于程序逻辑关联")
    private String sceneCode;

    /**
     * 场景业务名称（用于前端展示）
     */
    @Excel(name = "场景业务名称", readConverterExp = "用=于前端展示")
    private String sceneName;

    /**
     * 场景业务描述（说明场景的业务背景、应用范围）
     */
    @Excel(name = "场景业务描述", readConverterExp = "说=明场景的业务背景、应用范围")
    private String sceneDesc;

    /**
     * 场景启用状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "场景启用状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String sceneStatus;

    /**
     * 场景展示排序（升序排列，数值越小越靠前）
     */
    @Excel(name = "场景展示排序", readConverterExp = "升=序排列，数值越小越靠前")
    private Integer sceneSort;

    /**
     * 场景扩展备注
     */
    @Excel(name = "场景扩展备注")
    private String sceneRemark;

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
     * 是否需要排序标志（Y-按sort排序，其他情况按create_time倒序）
     */
    private String isNeedSort;

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

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneCode(String sceneCode) {
        this.sceneCode = sceneCode;
    }

    public String getSceneCode() {
        return sceneCode;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneStatus(String sceneStatus) {
        this.sceneStatus = sceneStatus;
    }

    public String getSceneStatus() {
        return sceneStatus;
    }

    public void setSceneSort(Integer sceneSort) {
        this.sceneSort = sceneSort;
    }

    public Integer getSceneSort() {
        return sceneSort;
    }

    public void setSceneRemark(String sceneRemark) {
        this.sceneRemark = sceneRemark;
    }

    public String getSceneRemark() {
        return sceneRemark;
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

    public String getIsNeedSort() {
        return isNeedSort;
    }

    public void setIsNeedSort(String isNeedSort) {
        this.isNeedSort = isNeedSort;
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
                .append("sceneId", getSceneId())
                .append("sceneCode", getSceneCode())
                .append("sceneName", getSceneName())
                .append("sceneDesc", getSceneDesc())
                .append("sceneStatus", getSceneStatus())
                .append("sceneSort", getSceneSort())
                .append("sceneRemark", getSceneRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("isNeedSort", getIsNeedSort())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
