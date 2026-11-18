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

    /** 客户端幂等请求号（同一用户内唯一） */
    private String clientRequestId;

    /** 当前异步任务租约持有者 */
    private String workerId;

    /** 租约到期时间，过期后允许其他节点接管 */
    private Date leaseExpireTime;

    /** 已尝试次数（含当前执行） */
    private Integer attemptCount;

    /** 最大尝试次数 */
    private Integer maxAttempts;

    /** 下次可重试时间 */
    private Date nextRetryTime;

    /** 结果来源（A-自动AI，M-人工AI处理） */
    private String resultSource;

    /** 人工导入操作人 */
    private String manualImportBy;

    /** 人工导入完成时间 */
    private Date manualImportTime;

    /** 业务快照数据截止时间（受理批次时冻结） */
    private Date dataAsOf;

    /** 【通用AI接入点】通用AI场景编码；场景编码稳定，不随供应商、模型或模板变化 */
    private String aiSceneCode;

    /** 【通用AI接入点】AI标准化响应契约版本 */
    private String aiContractVersion;

    /** 【通用AI接入点】证据规划调用的供应商无关引用，不建外键 */
    private String aiPlanRunRef;

    /** 【通用AI接入点】最终分析调用的供应商无关引用，不建外键 */
    private String aiAnalysisRunRef;

    /** 【通用AI接入点】证据规划调用的业务输入快照，仅含业务数据 */
    private String aiPlanInputSnapshot;

    /** 【通用AI接入点】最终分析调用的业务输入快照，仅含业务数据 */
    private String aiAnalysisInputSnapshot;

    /** 【通用AI接入点】供应商无关的调用阶段审计数组（阶段、场景、契约版本、请求号与耗时） */
    private String aiCallRefs;

    /** 【通用AI接入点】标准化AI结果快照（仅business内容，不含供应商治理元数据） */
    private String aiResultSnapshot;

    /** 自动AI证据流程状态（N-不适用，P-规划或检索中，R-检索完成，S-证据已固化，F-失败） */
    private String aiEvidenceStatus;

    /** 自动AI证据检索计划快照，仅供审计与后续通用AI模块复用 */
    private String aiEvidencePlan;

    /** 自动AI证据规划或检索的受控错误摘要 */
    private String aiEvidenceError;

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

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Date getLeaseExpireTime() {
        return leaseExpireTime;
    }

    public void setLeaseExpireTime(Date leaseExpireTime) {
        this.leaseExpireTime = leaseExpireTime;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public String getResultSource() {
        return resultSource;
    }

    public void setResultSource(String resultSource) {
        this.resultSource = resultSource;
    }

    public String getManualImportBy() {
        return manualImportBy;
    }

    public void setManualImportBy(String manualImportBy) {
        this.manualImportBy = manualImportBy;
    }

    public Date getManualImportTime() {
        return manualImportTime;
    }

    public void setManualImportTime(Date manualImportTime) {
        this.manualImportTime = manualImportTime;
    }

    public Date getDataAsOf() {
        return dataAsOf;
    }

    public void setDataAsOf(Date dataAsOf) {
        this.dataAsOf = dataAsOf;
    }

    public String getAiSceneCode() {
        return aiSceneCode;
    }

    public void setAiSceneCode(String aiSceneCode) {
        this.aiSceneCode = aiSceneCode;
    }

    public String getAiContractVersion() {
        return aiContractVersion;
    }

    public void setAiContractVersion(String aiContractVersion) {
        this.aiContractVersion = aiContractVersion;
    }

    public String getAiPlanRunRef() {
        return aiPlanRunRef;
    }

    public void setAiPlanRunRef(String aiPlanRunRef) {
        this.aiPlanRunRef = aiPlanRunRef;
    }

    public String getAiAnalysisRunRef() {
        return aiAnalysisRunRef;
    }

    public void setAiAnalysisRunRef(String aiAnalysisRunRef) {
        this.aiAnalysisRunRef = aiAnalysisRunRef;
    }

    public String getAiPlanInputSnapshot() {
        return aiPlanInputSnapshot;
    }

    public void setAiPlanInputSnapshot(String aiPlanInputSnapshot) {
        this.aiPlanInputSnapshot = aiPlanInputSnapshot;
    }

    public String getAiAnalysisInputSnapshot() {
        return aiAnalysisInputSnapshot;
    }

    public void setAiAnalysisInputSnapshot(String aiAnalysisInputSnapshot) {
        this.aiAnalysisInputSnapshot = aiAnalysisInputSnapshot;
    }

    public String getAiCallRefs() {
        return aiCallRefs;
    }

    public void setAiCallRefs(String aiCallRefs) {
        this.aiCallRefs = aiCallRefs;
    }

    public String getAiResultSnapshot() {
        return aiResultSnapshot;
    }

    public void setAiResultSnapshot(String aiResultSnapshot) {
        this.aiResultSnapshot = aiResultSnapshot;
    }

    public String getAiEvidenceStatus() {
        return aiEvidenceStatus;
    }

    public void setAiEvidenceStatus(String aiEvidenceStatus) {
        this.aiEvidenceStatus = aiEvidenceStatus;
    }

    public String getAiEvidencePlan() {
        return aiEvidencePlan;
    }

    public void setAiEvidencePlan(String aiEvidencePlan) {
        this.aiEvidencePlan = aiEvidencePlan;
    }

    public String getAiEvidenceError() {
        return aiEvidenceError;
    }

    public void setAiEvidenceError(String aiEvidenceError) {
        this.aiEvidenceError = aiEvidenceError;
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
                .append("clientRequestId", getClientRequestId())
                .append("workerId", getWorkerId())
                .append("leaseExpireTime", getLeaseExpireTime())
                .append("attemptCount", getAttemptCount())
                .append("maxAttempts", getMaxAttempts())
                .append("nextRetryTime", getNextRetryTime())
                .append("resultSource", getResultSource())
                .append("manualImportBy", getManualImportBy())
                .append("manualImportTime", getManualImportTime())
                .append("dataAsOf", getDataAsOf())
                .append("aiSceneCode", getAiSceneCode())
                .append("aiContractVersion", getAiContractVersion())
                .append("aiPlanRunRef", getAiPlanRunRef())
                .append("aiAnalysisRunRef", getAiAnalysisRunRef())
                .append("aiCallRefs", getAiCallRefs())
                .append("aiEvidenceStatus", getAiEvidenceStatus())
                .append("aiEvidenceError", getAiEvidenceError())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
