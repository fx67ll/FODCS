package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

/**
 * 回报组合人工AI结果导入结果。
 *
 * @author fx67ll
 * @date 2026-11-16
 */
public class Fx67llDortmundReturnManualImportResult {
    /** 批次主键 */
    private Long generationId;

    /** 批次编码 */
    private String generationCode;

    /** 本次写入组合数 */
    private Integer generatedCount;

    /** 结果来源（A-自动AI，M-人工AI处理） */
    private String resultSource;

    /** 完成时间 */
    private Date finishedTime;

    public Long getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Long generationId) {
        this.generationId = generationId;
    }

    public String getGenerationCode() {
        return generationCode;
    }

    public void setGenerationCode(String generationCode) {
        this.generationCode = generationCode;
    }

    public Integer getGeneratedCount() {
        return generatedCount;
    }

    public void setGeneratedCount(Integer generatedCount) {
        this.generatedCount = generatedCount;
    }

    public String getResultSource() {
        return resultSource;
    }

    public void setResultSource(String resultSource) {
        this.resultSource = resultSource;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }
}
