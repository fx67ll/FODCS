package com.ruoyi.fx67ll.dortmund.domain;

import com.ruoyi.common.annotation.Excel;

/**
 * 回报组合批次导出对象，不包含请求快照和AI治理信息。
 *
 * @author fx67ll
 * @date 2026-11-12
 */
public class Fx67llDortmundReturnGenerationExportResponse {
    @Excel(name = "批次编码")
    private String generationCode;

    @Excel(name = "生成模式", readConverterExp = "S=Subjective Mode,A=AI Mode")
    private String generationMode;

    @Excel(name = "状态", readConverterExp = "0=处理中,1=成功,2=失败")
    private String generationStatus;

    @Excel(name = "基础金额")
    private String inputAmount;

    @Excel(name = "目标金额")
    private String targetAmount;

    @Excel(name = "组合数")
    private Integer generatedCount;

    @Excel(name = "开始时间")
    private String startedTime;

    @Excel(name = "完成时间")
    private String finishedTime;

    @Excel(name = "受控备注")
    private String generationRemark;

    public Fx67llDortmundReturnGenerationExportResponse() {
    }

    public Fx67llDortmundReturnGenerationExportResponse(Fx67llDortmundReturnGeneration source,
            String startedTime, String finishedTime) {
        this.generationCode = source.getGenerationCode();
        this.generationMode = source.getGenerationMode();
        this.generationStatus = source.getGenerationStatus();
        this.inputAmount = source.getInputAmount() == null ? null : source.getInputAmount().toPlainString();
        this.targetAmount = source.getTargetAmount() == null ? null : source.getTargetAmount().toPlainString();
        this.generatedCount = source.getGeneratedCount();
        this.startedTime = startedTime;
        this.finishedTime = finishedTime;
        this.generationRemark = source.getGenerationRemark();
    }

    public String getGenerationCode() {
        return generationCode;
    }

    public String getGenerationMode() {
        return generationMode;
    }

    public String getGenerationStatus() {
        return generationStatus;
    }

    public String getInputAmount() {
        return inputAmount;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public Integer getGeneratedCount() {
        return generatedCount;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public String getGenerationRemark() {
        return generationRemark;
    }
}
