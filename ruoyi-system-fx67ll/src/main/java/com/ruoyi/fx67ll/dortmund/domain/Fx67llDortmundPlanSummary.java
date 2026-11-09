package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

/**
 * 方案计划摘要响应对象
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundPlanSummary {
    /** 方案计划主键 */
    private Long planId;

    /** 已保存摘要正文 */
    private String content;

    /** 摘要生成时间 */
    private Date generatedTime;

    /** 摘要更新标记 */
    private String summaryUpdateFlag;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(Date generatedTime) {
        this.generatedTime = generatedTime;
    }

    public String getSummaryUpdateFlag() {
        return summaryUpdateFlag;
    }

    public void setSummaryUpdateFlag(String summaryUpdateFlag) {
        this.summaryUpdateFlag = summaryUpdateFlag;
    }
}
