package com.ruoyi.fx67ll.ai.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI Prompt 限流/熔断规则（适配Sentinel框架）对象 fx67ll_ai_request_limit_rule
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiRequestLimitRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 规则唯一标识（主键）
     */
    private Long limitRuleId;

    /**
     * 规则作用维度（字典码：1-模型，2-模板，3-场景，4-分组）
     */
    @Excel(name = "规则作用维度", readConverterExp = "字=典码：1-模型，2-模板，3-场景，4-分组")
    private String limitRuleDimension;

    /**
     * 规则作用目标ID（对应维度的业务ID，如model_id、prompt_id）
     */
    @Excel(name = "规则作用目标ID", readConverterExp = "对=应维度的业务ID，如model_id、prompt_id")
    private Long limitRuleTargetId;

    /**
     * 规则类型（字典码：1-流量控制，2-熔断保护）
     */
    @Excel(name = "规则类型", readConverterExp = "字=典码：1-流量控制，2-熔断保护")
    private String limitRuleType;

    /**
     * 流控模式（字典码：D-直接拒绝，A-关联控制，L-链路流控，仅流控规则有效）
     */
    @Excel(name = "流控模式", readConverterExp = "字=典码：D-直接拒绝，A-关联控制，L-链路流控，仅流控规则有效")
    private String flowControlMode;

    /**
     * 流控效果（字典码：F-快速失败，W-预热启动，Q-匀速排队，仅流控规则有效）
     */
    @Excel(name = "流控效果", readConverterExp = "字=典码：F-快速失败，W-预热启动，Q-匀速排队，仅流控规则有效")
    private String flowControlEffect;

    /**
     * 流控指标类型（字典码：Q-QPS阈值，C-并发线程数，仅流控规则有效）
     */
    @Excel(name = "流控指标类型", readConverterExp = "字=典码：Q-QPS阈值，C-并发线程数，仅流控规则有效")
    private String flowRuleType;

    /**
     * 流控阈值（QPS或并发数，保留2位小数）
     */
    @Excel(name = "流控阈值", readConverterExp = "Q=PS或并发数，保留2位小数")
    private BigDecimal flowThreshold;

    /**
     * 熔断策略（字典码：S-慢调用比例，E-异常比例，N-异常数，仅熔断规则有效）
     */
    @Excel(name = "熔断策略", readConverterExp = "字=典码：S-慢调用比例，E-异常比例，N-异常数，仅熔断规则有效")
    private String circuitStrategy;

    /**
     * 熔断触发阈值（慢调用/异常比例：0-1；异常数：正整数）
     */
    @Excel(name = "熔断触发阈值", readConverterExp = "慢=调用/异常比例：0-1；异常数：正整数")
    private BigDecimal circuitThreshold;

    /**
     * 慢调用判定阈值（毫秒，仅慢调用熔断策略有效）
     */
    @Excel(name = "慢调用判定阈值", readConverterExp = "毫=秒，仅慢调用熔断策略有效")
    private Integer circuitGrade;

    /**
     * 熔断统计窗口时长（毫秒，默认10秒）
     */
    @Excel(name = "熔断统计窗口时长", readConverterExp = "毫=秒，默认10秒")
    private Integer circuitWindow;

    /**
     * 熔断恢复超时时间（毫秒，默认5秒后尝试半开）
     */
    @Excel(name = "熔断恢复超时时间", readConverterExp = "毫=秒，默认5秒后尝试半开")
    private Integer circuitTimeout;

    /**
     * 规则启用状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "规则启用状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String limitRuleStatus;

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

    public void setLimitRuleId(Long limitRuleId) {
        this.limitRuleId = limitRuleId;
    }

    public Long getLimitRuleId() {
        return limitRuleId;
    }

    public void setLimitRuleDimension(String limitRuleDimension) {
        this.limitRuleDimension = limitRuleDimension;
    }

    public String getLimitRuleDimension() {
        return limitRuleDimension;
    }

    public void setLimitRuleTargetId(Long limitRuleTargetId) {
        this.limitRuleTargetId = limitRuleTargetId;
    }

    public Long getLimitRuleTargetId() {
        return limitRuleTargetId;
    }

    public void setLimitRuleType(String limitRuleType) {
        this.limitRuleType = limitRuleType;
    }

    public String getLimitRuleType() {
        return limitRuleType;
    }

    public void setFlowControlMode(String flowControlMode) {
        this.flowControlMode = flowControlMode;
    }

    public String getFlowControlMode() {
        return flowControlMode;
    }

    public void setFlowControlEffect(String flowControlEffect) {
        this.flowControlEffect = flowControlEffect;
    }

    public String getFlowControlEffect() {
        return flowControlEffect;
    }

    public void setFlowRuleType(String flowRuleType) {
        this.flowRuleType = flowRuleType;
    }

    public String getFlowRuleType() {
        return flowRuleType;
    }

    public void setFlowThreshold(BigDecimal flowThreshold) {
        this.flowThreshold = flowThreshold;
    }

    public BigDecimal getFlowThreshold() {
        return flowThreshold;
    }

    public void setCircuitStrategy(String circuitStrategy) {
        this.circuitStrategy = circuitStrategy;
    }

    public String getCircuitStrategy() {
        return circuitStrategy;
    }

    public void setCircuitThreshold(BigDecimal circuitThreshold) {
        this.circuitThreshold = circuitThreshold;
    }

    public BigDecimal getCircuitThreshold() {
        return circuitThreshold;
    }

    public void setCircuitGrade(Integer circuitGrade) {
        this.circuitGrade = circuitGrade;
    }

    public Integer getCircuitGrade() {
        return circuitGrade;
    }

    public void setCircuitWindow(Integer circuitWindow) {
        this.circuitWindow = circuitWindow;
    }

    public Integer getCircuitWindow() {
        return circuitWindow;
    }

    public void setCircuitTimeout(Integer circuitTimeout) {
        this.circuitTimeout = circuitTimeout;
    }

    public Integer getCircuitTimeout() {
        return circuitTimeout;
    }

    public void setLimitRuleStatus(String limitRuleStatus) {
        this.limitRuleStatus = limitRuleStatus;
    }

    public String getLimitRuleStatus() {
        return limitRuleStatus;
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
                .append("limitRuleId", getLimitRuleId())
                .append("limitRuleDimension", getLimitRuleDimension())
                .append("limitRuleTargetId", getLimitRuleTargetId())
                .append("limitRuleType", getLimitRuleType())
                .append("flowControlMode", getFlowControlMode())
                .append("flowControlEffect", getFlowControlEffect())
                .append("flowRuleType", getFlowRuleType())
                .append("flowThreshold", getFlowThreshold())
                .append("circuitStrategy", getCircuitStrategy())
                .append("circuitThreshold", getCircuitThreshold())
                .append("circuitGrade", getCircuitGrade())
                .append("circuitWindow", getCircuitWindow())
                .append("circuitTimeout", getCircuitTimeout())
                .append("limitRuleStatus", getLimitRuleStatus())
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
