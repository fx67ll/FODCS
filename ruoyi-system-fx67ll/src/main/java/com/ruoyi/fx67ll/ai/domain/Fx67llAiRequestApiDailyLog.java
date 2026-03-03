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
     * 统计维度：模型ID（-1表示全模型汇总）
     */
    private Long modelId;

    /**
     * 统计维度：场景ID（-1表示全场景汇总）
     */
    private Long sceneId;

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

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setSceneId(Long sceneId) {
        this.sceneId = sceneId;
    }

    public Long getSceneId() {
        return sceneId;
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
                .append("modelId", getModelId())
                .append("sceneId", getSceneId())
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
