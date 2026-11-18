package com.ruoyi.fx67ll.dortmund.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 回报组合人工AI标准交换模板。
 * <p>
 * 模板与具体批次无关，只提供可复制的任务骨架与结果骨架；实际任务仍须使用
 * manual-task接口返回的已冻结业务输入，导入结果必须与当前批次taskId一致。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public class Fx67llDortmundReturnManualTemplate {
    /** 任务模板版本 */
    private String schemaVersion;

    /** 稳定任务类型 */
    private String taskType;

    /** 使用说明 */
    private String instruction;

    /** 可复制的任务JSON模板 */
    private String taskTemplate;

    /** 可复制的标准化结果JSON模板 */
    private String resultTemplate;

    /** 人工AI工作流步骤说明 */
    private List<String> workflowSteps = new ArrayList<>();

    /** 导出任务字段说明 */
    private List<Fx67llDortmundReturnManualTemplateField> taskFields = new ArrayList<>();

    /** 导入结果字段说明 */
    private List<Fx67llDortmundReturnManualTemplateField> resultFields = new ArrayList<>();

    /** 第三方AI处理提示词 */
    private String exportPrompt;

    /** 第三方AI结果修复提示词 */
    private String importPrompt;

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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTaskTemplate() {
        return taskTemplate;
    }

    public void setTaskTemplate(String taskTemplate) {
        this.taskTemplate = taskTemplate;
    }

    public String getResultTemplate() {
        return resultTemplate;
    }

    public void setResultTemplate(String resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public List<String> getWorkflowSteps() {
        return workflowSteps;
    }

    public void setWorkflowSteps(List<String> workflowSteps) {
        this.workflowSteps = workflowSteps;
    }

    public List<Fx67llDortmundReturnManualTemplateField> getTaskFields() {
        return taskFields;
    }

    public void setTaskFields(List<Fx67llDortmundReturnManualTemplateField> taskFields) {
        this.taskFields = taskFields;
    }

    public List<Fx67llDortmundReturnManualTemplateField> getResultFields() {
        return resultFields;
    }

    public void setResultFields(List<Fx67llDortmundReturnManualTemplateField> resultFields) {
        this.resultFields = resultFields;
    }

    public String getExportPrompt() {
        return exportPrompt;
    }

    public void setExportPrompt(String exportPrompt) {
        this.exportPrompt = exportPrompt;
    }

    public String getImportPrompt() {
        return importPrompt;
    }

    public void setImportPrompt(String importPrompt) {
        this.importPrompt = importPrompt;
    }
}
