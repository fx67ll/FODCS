package com.ruoyi.fx67ll.ai.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI 调用请求日统计日志对象 fx67ll_ai_request_daily_log
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiRequestApiDailyLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 统计日期（yyyy-MM-dd，分区键）
     */
    private Date dailyLogDate;

    /**
     * 关联模板ID（外键，直接调用模型时为空）
     */
    @Excel(name = "关联模板ID", readConverterExp = "外=键，直接调用模型时为空")
    private Long promptId;

    /**
     * 模板名称（非数据库字段，用于列表展示）
     */
    @Excel(name = "模板名称")
    private String promptName;

    /**
     * 所属分组ID（外键，关联fx67ll_ai_prompt_group.group_id，强制约束模板与分组的归属关系）
     */
    @Excel(name = "所属分组ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_group.group_id，强制约束模板与分组的归属关系")
    private Long groupId;

    /** 分组名称（非数据库字段，用于列表展示） */
    @Excel(name = "分组名称")
    private String groupName;

    /**
     * 关联场景ID（外键，直接调用模型时为空）
     */
    @Excel(name = "关联场景ID", readConverterExp = "外=键，直接调用模型时为空")
    private Long sceneId;

    /**
     * 场景名称（非数据库字段，用于列表展示）
     */
    @Excel(name = "场景名称")
    private String sceneName;

    /**
     * 调用模型ID（外键，关联fx67ll_ai_prompt_model.model_id）
     */
    @Excel(name = "调用模型ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_model.model_id")
    private Long modelId;

    /**
     * 模型名称（非数据库字段，用于列表展示）
     */
    @Excel(name = "模型名称")
    private String modelName;

    /**
     * 统计周期内API总调用次数（含所有状态）
     */
    @Excel(name = "统计周期内API总调用次数", readConverterExp = "含=所有状态")
    private Long totalRequests;

    /**
     * 统计周期内业务失败调用次数
     */
    @Excel(name = "统计周期内业务失败调用次数")
    private Long failRequests;

    /**
     * 统计周期内限流拦截调用次数
     */
    @Excel(name = "统计周期内限流拦截调用次数")
    private Long limitRequests;

    /**
     * 统计周期内熔断拦截调用次数
     */
    @Excel(name = "统计周期内熔断拦截调用次数")
    private Long circuitRequests;

    /**
     * 统计周期内总输入Token消耗量
     */
    @Excel(name = "统计周期内总输入Token消耗量")
    private Long totalPromptTokens;

    /**
     * 统计周期内总输出Token消耗量
     */
    @Excel(name = "统计周期内总输出Token消耗量")
    private Long totalCompletionTokens;

    /**
     * 统计周期内总预估费用（元）
     */
    @Excel(name = "统计周期内总预估费用", readConverterExp = "元=")
    private BigDecimal totalCost;

    /**
     * 统计周期内平均请求耗时（毫秒，总耗时/成功请求数）
     */
    @Excel(name = "统计周期内平均请求耗时", readConverterExp = "毫=秒，总耗时/成功请求数")
    private Long avgDurationMs;

    public void setDailyLogDate(Date dailyLogDate) {
        this.dailyLogDate = dailyLogDate;
    }

    public Date getDailyLogDate() {
        return dailyLogDate;
    }

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

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getSceneId() {
        return sceneId;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setTotalRequests(Long totalRequests) {
        this.totalRequests = totalRequests;
    }

    public Long getTotalRequests() {
        return totalRequests;
    }

    public void setFailRequests(Long failRequests) {
        this.failRequests = failRequests;
    }

    public Long getFailRequests() {
        return failRequests;
    }

    public void setLimitRequests(Long limitRequests) {
        this.limitRequests = limitRequests;
    }

    public Long getLimitRequests() {
        return limitRequests;
    }

    public void setCircuitRequests(Long circuitRequests) {
        this.circuitRequests = circuitRequests;
    }

    public Long getCircuitRequests() {
        return circuitRequests;
    }

    public void setTotalPromptTokens(Long totalPromptTokens) {
        this.totalPromptTokens = totalPromptTokens;
    }

    public Long getTotalPromptTokens() {
        return totalPromptTokens;
    }

    public void setTotalCompletionTokens(Long totalCompletionTokens) {
        this.totalCompletionTokens = totalCompletionTokens;
    }

    public Long getTotalCompletionTokens() {
        return totalCompletionTokens;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setAvgDurationMs(Long avgDurationMs) {
        this.avgDurationMs = avgDurationMs;
    }

    public Long getAvgDurationMs() {
        return avgDurationMs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dailyLogDate", getDailyLogDate())
                .append("promptId", getPromptId())
                .append("promptName", getPromptName())
                .append("groupId", getGroupId())
                .append("groupName", getGroupName())
                .append("sceneId", getSceneId())
                .append("sceneName", getSceneName())
                .append("modelId", getModelId())
                .append("modelName", getModelName())
                .append("totalRequests", getTotalRequests())
                .append("failRequests", getFailRequests())
                .append("limitRequests", getLimitRequests())
                .append("circuitRequests", getCircuitRequests())
                .append("totalPromptTokens", getTotalPromptTokens())
                .append("totalCompletionTokens", getTotalCompletionTokens())
                .append("totalCost", getTotalCost())
                .append("avgDurationMs", getAvgDurationMs())
                .toString();
    }
}
