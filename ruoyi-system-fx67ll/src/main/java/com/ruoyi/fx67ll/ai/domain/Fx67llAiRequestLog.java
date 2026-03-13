package com.ruoyi.fx67ll.ai.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI 调用请求日志对象 fx67ll_ai_request_log
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiRequestLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 日志唯一标识（分区表主键，与request_time组成复合聚集索引）
     */
    private Long requestLogId;

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

    /**
     * 分组名称（非数据库字段，用于列表展示）
     */
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
     * 请求完整内容（含最终渲染后的Prompt文本）
     */
    @Excel(name = "请求完整内容", readConverterExp = "含=最终渲染后的Prompt文本")
    private String requestContent;

    /**
     * 响应完整内容（大文本存储AI返回结果）
     */
    @Excel(name = "响应完整内容", readConverterExp = "大=文本存储AI返回结果")
    private String responseContent;

    /**
     * 输入Token消耗量（Prompt部分）
     */
    @Excel(name = "输入Token消耗量", readConverterExp = "P=rompt部分")
    private Long promptTokens;

    /**
     * 输出Token消耗量（Completion部分）
     */
    @Excel(name = "输出Token消耗量", readConverterExp = "C=ompletion部分")
    private Long completionTokens;

    /**
     * 总Token消耗量（输入+输出）
     */
    @Excel(name = "总Token消耗量", readConverterExp = "输=入+输出")
    private Long totalTokens;

    /**
     * 本次调用预估费用（元，基于单价和Token数计算）
     */
    @Excel(name = "本次调用预估费用", readConverterExp = "元=，基于单价和Token数计算")
    private BigDecimal cost;

    /**
     * 请求耗时（毫秒，从发送请求到接收响应的总时长）
     */
    @Excel(name = "请求耗时", readConverterExp = "毫=秒，从发送请求到接收响应的总时长")
    private Long durationMs;

    /**
     * HTTP响应状态码（如200、400、500）
     */
    @Excel(name = "HTTP响应状态码", readConverterExp = "如=200、400、500")
    private Integer httpStatus;

    /**
     * 调用业务状态（字典码：00-成功，01-业务失败，02-限流拦截，03-熔断拦截）
     */
    @Excel(name = "调用业务状态", readConverterExp = "字=典码：00-成功，01-业务失败，02-限流拦截，03-熔断拦截")
    private String callStatus;

    /**
     * 错误堆栈信息（调用失败时存储）
     */
    @Excel(name = "错误堆栈信息", readConverterExp = "调=用失败时存储")
    private String errorMsg;

    /**
     * 调用者客户端IP地址
     */
    @Excel(name = "调用者客户端IP地址")
    private String callerIp;

    /**
     * 请求发起时间（分区键，精确到秒）
     */
    private Date requestTime;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 请求发起开始时间
     */
    private String beginRequestTime;

    /**
     * 请求发起结束时间
     */
    private String endRequestTime;

    public void setRequestLogId(Long requestLogId) {
        this.requestLogId = requestLogId;
    }

    public Long getRequestLogId() {
        return requestLogId;
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

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setPromptTokens(Long promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Long getPromptTokens() {
        return promptTokens;
    }

    public void setCompletionTokens(Long completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Long getCompletionTokens() {
        return completionTokens;
    }

    public void setTotalTokens(Long totalTokens) {
        this.totalTokens = totalTokens;
    }

    public Long getTotalTokens() {
        return totalTokens;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setCallerIp(String callerIp) {
        this.callerIp = callerIp;
    }

    public String getCallerIp() {
        return callerIp;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBeginRequestTime() {
        return beginRequestTime;
    }

    public void setBeginRequestTime(String beginRequestTime) {
        this.beginRequestTime = beginRequestTime;
    }

    public String getEndRequestTime() {
        return endRequestTime;
    }

    public void setEndRequestTime(String endRequestTime) {
        this.endRequestTime = endRequestTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("requestLogId", getRequestLogId())
                .append("promptId", getPromptId())
                .append("promptName", getPromptName())
                .append("groupId", getGroupId())
                .append("groupName", getGroupName())
                .append("sceneId", getSceneId())
                .append("sceneName", getSceneName())
                .append("modelId", getModelId())
                .append("modelName", getModelName())
                .append("requestContent", getRequestContent())
                .append("responseContent", getResponseContent())
                .append("promptTokens", getPromptTokens())
                .append("completionTokens", getCompletionTokens())
                .append("totalTokens", getTotalTokens())
                .append("cost", getCost())
                .append("durationMs", getDurationMs())
                .append("httpStatus", getHttpStatus())
                .append("callStatus", getCallStatus())
                .append("errorMsg", getErrorMsg())
                .append("callerIp", getCallerIp())
                .append("requestTime", getRequestTime())
                .append("createBy", getCreateBy())
                .append("userId", getUserId())
                .append("beginRequestTime", getBeginRequestTime())
                .append("endRequestTime", getEndRequestTime())
                .toString();
    }
}
