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

    /** 参与AI分析的比赛池，限1-23场；每条候选仍最多8场。 */
    private List<Long> matchIds;

    /** 用户偏好编码；只作为AI候选结构提示，不参与金额、倍率或偏差计算。 */
    private String preferenceCode;

    /** 受控偏好维度；可省略并由预设编码补全，四项必须同时提供或同时省略。 */
    private Fx67llDortmundReturnPreferenceDimensions preferenceDimensions;

    /** 用户临时补充的业务目标；后端预定义目标由服务端生成，二者独立保存 */
    private List<String> supplementaryObjectives;

    /** 客户端幂等请求号，同一用户重复提交时返回同一批次 */
    private String clientRequestId;

    /**
     * 是否隐藏真实倍率。
     * <p>null 或 true 时向AI发送固定占位文本，false 时发送真实倍率；该标记只冻结在请求快照中，
     * 不新增数据库字段，也不影响后端从快照读取真实倍率完成金额复算。</p>
     */
    private Boolean hideOdds;

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

    public String getPreferenceCode() {
        return preferenceCode;
    }

    public void setPreferenceCode(String preferenceCode) {
        this.preferenceCode = preferenceCode;
    }

    public Fx67llDortmundReturnPreferenceDimensions getPreferenceDimensions() {
        return preferenceDimensions;
    }

    public void setPreferenceDimensions(Fx67llDortmundReturnPreferenceDimensions preferenceDimensions) {
        this.preferenceDimensions = preferenceDimensions;
    }

    public List<String> getSupplementaryObjectives() {
        return supplementaryObjectives;
    }

    public void setSupplementaryObjectives(List<String> supplementaryObjectives) {
        this.supplementaryObjectives = supplementaryObjectives;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }

    public Boolean getHideOdds() {
        return hideOdds;
    }

    public void setHideOdds(Boolean hideOdds) {
        this.hideOdds = hideOdds;
    }
}
