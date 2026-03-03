package com.ruoyi.fx67ll.dortmund.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 比赛AI分析原始结果对象 fx67ll_dortmund_match_analysis
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llDortmundMatchAnalysis extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分析唯一标识（主键）
     */
    private Long analysisId;

    /**
     * 关联比赛ID（外键，关联fx67ll_dortmund_match.match_id）
     */
    @Excel(name = "关联比赛ID", readConverterExp = "外=键，关联fx67ll_dortmund_match.match_id")
    private Long matchId;

    /**
     * 使用的模板ID（外键，关联fx67ll_ai_prompt_template.prompt_id，自定义分析时为空）
     */
    @Excel(name = "使用的模板ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_template.prompt_id，自定义分析时为空")
    private Long promptId;

    /**
     * 使用的模型ID（外键，关联fx67ll_ai_prompt_model.model_id）
     */
    @Excel(name = "使用的模型ID", readConverterExp = "外=键，关联fx67ll_ai_prompt_model.model_id")
    private Long modelId;

    /**
     * AI调用日志关联码（格式：request_log_id|request_time，用于手动关联fx67ll_ai_request_log表）
     */
    @Excel(name = "AI调用日志关联码", readConverterExp = "格=式：request_log_id|request_time，用于手动关联fx67ll_ai_request_log表")
    private String requestLogCode;

    /**
     * 分析类型（字典码：0-模板分析，1-自定义文本分析）
     */
    @Excel(name = "分析类型", readConverterExp = "字=典码：0-模板分析，1-自定义文本分析")
    private String analysisType;

    /**
     * 最终请求Prompt（含渲染后的球队/比赛数据，自定义分析时为用户输入文本）
     */
    @Excel(name = "最终请求Prompt", readConverterExp = "含=渲染后的球队/比赛数据，自定义分析时为用户输入文本")
    private String rawPrompt;

    /**
     * AI原始响应内容（JSON格式字符串）
     */
    @Excel(name = "AI原始响应内容", readConverterExp = "J=SON格式字符串")
    private String rawAiResponse;

    /**
     * 分析业务备注
     */
    @Excel(name = "分析业务备注")
    private String analysisRemark;

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

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setPromptId(Long promptId) {
        this.promptId = promptId;
    }

    public Long getPromptId() {
        return promptId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setRequestLogCode(String requestLogCode) {
        this.requestLogCode = requestLogCode;
    }

    public String getRequestLogCode() {
        return requestLogCode;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setRawPrompt(String rawPrompt) {
        this.rawPrompt = rawPrompt;
    }

    public String getRawPrompt() {
        return rawPrompt;
    }

    public void setRawAiResponse(String rawAiResponse) {
        this.rawAiResponse = rawAiResponse;
    }

    public String getRawAiResponse() {
        return rawAiResponse;
    }

    public void setAnalysisRemark(String analysisRemark) {
        this.analysisRemark = analysisRemark;
    }

    public String getAnalysisRemark() {
        return analysisRemark;
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
                .append("analysisId", getAnalysisId())
                .append("matchId", getMatchId())
                .append("promptId", getPromptId())
                .append("modelId", getModelId())
                .append("requestLogCode", getRequestLogCode())
                .append("analysisType", getAnalysisType())
                .append("rawPrompt", getRawPrompt())
                .append("rawAiResponse", getRawAiResponse())
                .append("analysisRemark", getAnalysisRemark())
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
