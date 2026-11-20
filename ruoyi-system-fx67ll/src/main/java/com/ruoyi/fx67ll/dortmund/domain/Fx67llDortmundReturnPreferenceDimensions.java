package com.ruoyi.fx67ll.dortmund.domain;

/**
 * 回报组合受控偏好维度。
 * <p>
 * 四个字段必须同时提供或同时省略。服务端负责白名单校验、预设归一化和冲突判断，
 * 前端只能提交模块配置中定义的编码，不得提交阈值、权重或金额公式。
 *
 * @author fx67ll
 * @date 2026-11-20
 */
public class Fx67llDortmundReturnPreferenceDimensions {
    /** 风险水平：LOW、BALANCED、HIGH。 */
    private String riskLevel;

    /** 组合结构：SINGLE、SHORT、BALANCED、LONG。 */
    private String structure;

    /** 投入意图：CONSERVATIVE、BALANCED、AGGRESSIVE；实际投入仍以inputAmount为准。 */
    private String stakeIntent;

    /** 回报意图：TARGET_SEEKING、BALANCED、MAX_RETURN。 */
    private String returnIntent;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getStakeIntent() {
        return stakeIntent;
    }

    public void setStakeIntent(String stakeIntent) {
        this.stakeIntent = stakeIntent;
    }

    public String getReturnIntent() {
        return returnIntent;
    }

    public void setReturnIntent(String returnIntent) {
        this.returnIntent = returnIntent;
    }
}
