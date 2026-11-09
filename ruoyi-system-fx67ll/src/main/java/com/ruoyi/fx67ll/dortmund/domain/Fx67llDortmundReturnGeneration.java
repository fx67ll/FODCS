package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 回报组合生成批次对象 fx67ll_dortmund_return_generation
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundReturnGeneration extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 生成批次主键 */
    private Long generationId;

    /** 生成批次编码 */
    private String generationCode;

    /** 生成模式 */
    private String generationMode;

    /** 基础金额 */
    private BigDecimal inputAmount;

    /** 目标金额 */
    private BigDecimal targetAmount;

    /** 业务请求快照 */
    private String requestSnapshot;

    /** 生成状态 */
    private String generationStatus;

    /** 受控结果说明 */
    private String generationRemark;

    /** 实际生成数量 */
    private Integer generatedCount;

    /** 开始时间 */
    private Date startedTime;

    /** 完成时间 */
    private Date finishedTime;

    /** 数据归属用户主键 */
    private Long userId;

    /** 回报组合集合 */
    private List<Fx67llDortmundReturnCombination> combinations;

    /** 创建开始时间 */
    private String beginCreateTime;

    /** 创建结束时间 */
    private String endCreateTime;

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

    public String getRequestSnapshot() {
        return requestSnapshot;
    }

    public void setRequestSnapshot(String requestSnapshot) {
        this.requestSnapshot = requestSnapshot;
    }

    public String getGenerationStatus() {
        return generationStatus;
    }

    public void setGenerationStatus(String generationStatus) {
        this.generationStatus = generationStatus;
    }

    public String getGenerationRemark() {
        return generationRemark;
    }

    public void setGenerationRemark(String generationRemark) {
        this.generationRemark = generationRemark;
    }

    public Integer getGeneratedCount() {
        return generatedCount;
    }

    public void setGeneratedCount(Integer generatedCount) {
        this.generatedCount = generatedCount;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Fx67llDortmundReturnCombination> getCombinations() {
        return combinations;
    }

    public void setCombinations(List<Fx67llDortmundReturnCombination> combinations) {
        this.combinations = combinations;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("generationId", getGenerationId())
                .append("generationCode", getGenerationCode())
                .append("generationMode", getGenerationMode())
                .append("inputAmount", getInputAmount())
                .append("targetAmount", getTargetAmount())
                .append("generationStatus", getGenerationStatus())
                .append("generationRemark", getGenerationRemark())
                .append("generatedCount", getGeneratedCount())
                .append("startedTime", getStartedTime())
                .append("finishedTime", getFinishedTime())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
