package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * 回报组合生成请求对象
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundReturnGenerateRequest {
    /** 生成模式 */
    private String generationMode;

    /** 基础金额 */
    private BigDecimal inputAmount;

    /** 目标金额 */
    private BigDecimal targetAmount;

    /** 比赛主键集合 */
    private List<Long> matchIds;

    /** 客户端幂等请求号，同一用户重复提交时返回同一批次 */
    private String clientRequestId;

    public String getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(String generationMode) {
        this.generationMode = generationMode;
    }

    public BigDecimal getInputAmount() {
        return inputAmount;
    }

    public void setInputAmount(BigDecimal inputAmount) {
        this.inputAmount = inputAmount;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public List<Long> getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(List<Long> matchIds) {
        this.matchIds = matchIds;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }
}
