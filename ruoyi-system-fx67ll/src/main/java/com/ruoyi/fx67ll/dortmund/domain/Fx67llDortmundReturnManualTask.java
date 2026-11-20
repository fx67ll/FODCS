package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.fx67ll.dortmund.ai.ReturnCombinationAiAnalysisPort;

/**
 * 回报组合人工AI处理任务。
 * <p>
 * 该对象只包含业务输入、业务处理提示词和标准输出契约，不包含供应商、模型、
 * Token、费用或调用链路等治理元数据。
 *
 * @author fx67ll
 * @date 2026-11-16
 */
public class Fx67llDortmundReturnManualTask {
    /** 交换契约版本 */
    private String schemaVersion;

    /** 稳定任务类型 */
    private String taskType;

    /** 生成批次主键 */
    private Long generationId;

    /** 生成批次编码，同时作为taskId */
    private String taskId;

    /** 生成模式 */
    private String generationMode;

    /** 导出时间 */
    private Date generatedAt;

    /** 已固化的业务输入快照 */
    private ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput input;

    /** 给第三方AI的操作说明 */
    private String instruction;

    /** 给第三方AI的完整处理提示词，随任务一同导出 */
    private String exportPrompt;

    /** 标准响应契约与示例 */
    private JSONObject responseContract;

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Long getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Long generationId) {
        this.generationId = generationId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(String generationMode) {
        this.generationMode = generationMode;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput getInput() {
        return input;
    }

    public void setInput(ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput input) {
        this.input = input;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getExportPrompt() {
        return exportPrompt;
    }

    public void setExportPrompt(String exportPrompt) {
        this.exportPrompt = exportPrompt;
    }

    public JSONObject getResponseContract() {
        return responseContract;
    }

    public void setResponseContract(JSONObject responseContract) {
        this.responseContract = responseContract;
    }
}
