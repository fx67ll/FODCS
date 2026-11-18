package com.ruoyi.fx67ll.dortmund.ai;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;

/**
 * Dortmund回报组合AI分析业务边界
 * <p>
 * 仅传递业务输入、业务输出与供应商无关的调用审计信息，不持久化供应商、模型、Prompt、Token或费用等治理元数据
 * <p>
 * 【通用AI接入点】本端口是回报组合业务与模型编排之间唯一允许的依赖边界。通用AI模块接入时，
 * 只替换本端口实现；业务输入、证据编码、响应契约和落库事务不得随供应商变化。
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface ReturnCombinationAiAnalysisPort {
    /**
     * 执行回报组合AI分析
     *
     * @param input 已校验的业务输入
     * @param invocation 通用AI调用契约
     * @return 业务可消费的AI分析输出
     */
    ReturnCombinationAiOutput analyze(ReturnCombinationAiInput input, ReturnCombinationAiInvocation invocation);

    /**
     * 由AI自主识别权重论证所需的信息缺口并生成受控检索计划。
     * <p>
     * 仅自动AI模式调用；人工AI任务不调用本方法。后续通用AI模块可在此编排规划模型与搜索工具，
     * 但检索结果的冻结、证据编码和候选校验仍由回报组合业务边界负责。
     *
     * @param input 已校验的业务输入
     * @param invocation 通用AI调用契约
     * @return 有界、可审计的证据检索计划
     */
    ReturnCombinationEvidencePlan planEvidence(ReturnCombinationAiInput input,
            ReturnCombinationAiInvocation invocation);

    /**
     * 通用AI稳定调用契约。
     * <p>
     * 【通用AI接入点】operation、sceneCode、contractVersion、businessKey、requestId、timeoutMillis和
     * toolPolicy由回报组合业务层生成；通用AI实现只消费本对象，不得要求业务层传入供应商请求体。
     */
    class ReturnCombinationAiInvocation {
        /** 调用阶段：PLAN或ANALYZE */
        private String operation;

        /** 稳定场景编码 */
        private String sceneCode;

        /** 自动AI标准化响应契约版本 */
        private String contractVersion;

        /** 业务幂等键，使用生成批次编码 */
        private String businessKey;

        /** 通用调用审计请求号 */
        private String requestId;

        /** 单次调用超时预算 */
        private Long timeoutMillis;

        /** 供应商无关工具策略 */
        private ToolPolicy toolPolicy;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getSceneCode() {
            return sceneCode;
        }

        public void setSceneCode(String sceneCode) {
            this.sceneCode = sceneCode;
        }

        public String getContractVersion() {
            return contractVersion;
        }

        public void setContractVersion(String contractVersion) {
            this.contractVersion = contractVersion;
        }

        public String getBusinessKey() {
            return businessKey;
        }

        public void setBusinessKey(String businessKey) {
            this.businessKey = businessKey;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Long getTimeoutMillis() {
            return timeoutMillis;
        }

        public void setTimeoutMillis(Long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
        }

        public ToolPolicy getToolPolicy() {
            return toolPolicy;
        }

        public void setToolPolicy(ToolPolicy toolPolicy) {
            this.toolPolicy = toolPolicy;
        }
    }

    /**
     * 通用AI工具策略，不包含搜索供应商凭据或具体实现。
     */
    class ToolPolicy {
        private Boolean searchAllowed;

        private Integer maxQueries;

        private Integer maxResultsPerQuery;

        private List<String> blockedDomains;

        public Boolean getSearchAllowed() {
            return searchAllowed;
        }

        public void setSearchAllowed(Boolean searchAllowed) {
            this.searchAllowed = searchAllowed;
        }

        public Integer getMaxQueries() {
            return maxQueries;
        }

        public void setMaxQueries(Integer maxQueries) {
            this.maxQueries = maxQueries;
        }

        public Integer getMaxResultsPerQuery() {
            return maxResultsPerQuery;
        }

        public void setMaxResultsPerQuery(Integer maxResultsPerQuery) {
            this.maxResultsPerQuery = maxResultsPerQuery;
        }

        public List<String> getBlockedDomains() {
            return blockedDomains;
        }

        public void setBlockedDomains(List<String> blockedDomains) {
            this.blockedDomains = blockedDomains;
        }
    }

    /**
     * AI分析业务输入
     */
    class ReturnCombinationAiInput {
        private String generationMode;

        /** 业务快照数据截止时间 */
        private String dataAsOf;

        private BigDecimal inputAmount;

        private BigDecimal targetAmount;

        private List<Fx67llDortmundOdds> odds;

        /** 本次调用的显式业务目标，供未来通用AI编排复用 */
        private List<String> businessObjectives;

        /** 用户临时补充的业务目标；与后端预定义目标并列生效，不覆盖businessObjectives */
        private List<String> supplementaryObjectives;

        /** 提交时冻结的比赛、球队、球队标签和信息点上下文 */
        private ReturnCombinationAiBusinessContext businessContext;

        /** 系统冻结的已提交证据目录，evidenceCode供AI在证据链中引用 */
        private List<EvidenceContext> evidenceCatalog;

        /** 自动AI模式由系统受控检索并冻结的补充证据；主观模式与人工模式为空 */
        private List<EvidenceContext> retrievedEvidence;

        public String getGenerationMode() {
            return generationMode;
        }

        public void setGenerationMode(String generationMode) {
            this.generationMode = generationMode;
        }

        public String getDataAsOf() {
            return dataAsOf;
        }

        public void setDataAsOf(String dataAsOf) {
            this.dataAsOf = dataAsOf;
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

        public List<Fx67llDortmundOdds> getOdds() {
            return odds;
        }

        public void setOdds(List<Fx67llDortmundOdds> odds) {
            this.odds = odds;
        }

        public List<String> getBusinessObjectives() {
            return businessObjectives;
        }

        public void setBusinessObjectives(List<String> businessObjectives) {
            this.businessObjectives = businessObjectives;
        }

        public List<String> getSupplementaryObjectives() {
            return supplementaryObjectives;
        }

        public void setSupplementaryObjectives(List<String> supplementaryObjectives) {
            this.supplementaryObjectives = supplementaryObjectives;
        }

        public ReturnCombinationAiBusinessContext getBusinessContext() {
            return businessContext;
        }

        public void setBusinessContext(ReturnCombinationAiBusinessContext businessContext) {
            this.businessContext = businessContext;
        }

        public List<EvidenceContext> getEvidenceCatalog() {
            return evidenceCatalog;
        }

        public void setEvidenceCatalog(List<EvidenceContext> evidenceCatalog) {
            this.evidenceCatalog = evidenceCatalog;
        }

        public List<EvidenceContext> getRetrievedEvidence() {
            return retrievedEvidence;
        }

        public void setRetrievedEvidence(List<EvidenceContext> retrievedEvidence) {
            this.retrievedEvidence = retrievedEvidence;
        }
    }

    /**
     * AI证据检索计划
     */
    class ReturnCombinationEvidencePlan {
        private String planningSummary;

        private String aiRunRef;

        private List<EvidenceQuery> queries;

        public String getPlanningSummary() {
            return planningSummary;
        }

        public void setPlanningSummary(String planningSummary) {
            this.planningSummary = planningSummary;
        }

        public String getAiRunRef() {
            return aiRunRef;
        }

        public void setAiRunRef(String aiRunRef) {
            this.aiRunRef = aiRunRef;
        }

        public List<EvidenceQuery> getQueries() {
            return queries;
        }

        public void setQueries(List<EvidenceQuery> queries) {
            this.queries = queries;
        }
    }

    /**
     * 单条有界证据检索查询
     */
    class EvidenceQuery {
        private Long matchId;

        private String query;

        private String purpose;

        private String reason;

        /** 检索时效提示（天） */
        private Integer recencyDays;

        /** 检索语言区域提示 */
        private String locale;

        /** 检索地理范围提示 */
        private String region;

        /** 优先来源层级提示，由业务层白名单校验 */
        private List<String> preferredSourceTiers;

        public Long getMatchId() {
            return matchId;
        }

        public void setMatchId(Long matchId) {
            this.matchId = matchId;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getRecencyDays() {
            return recencyDays;
        }

        public void setRecencyDays(Integer recencyDays) {
            this.recencyDays = recencyDays;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public List<String> getPreferredSourceTiers() {
            return preferredSourceTiers;
        }

        public void setPreferredSourceTiers(List<String> preferredSourceTiers) {
            this.preferredSourceTiers = preferredSourceTiers;
        }
    }

    /**
     * 冻结证据上下文，仅承载业务事实，不包含供应商治理元数据
     */
    class EvidenceContext {
        private String evidenceCode;

        private Long matchId;

        private String evidenceType;

        private String title;

        private String content;

        private String sourceName;

        private String sourceUrl;

        private String publishedTime;

        private String query;

        /** 来源层级：OFFICIAL/MAJOR_MEDIA/SPECIALIST/AGGREGATOR/UNKNOWN */
        private String sourceTier;

        /** 新鲜度：CURRENT/RECENT/STALE/UNKNOWN */
        private String freshnessClass;

        public String getEvidenceCode() {
            return evidenceCode;
        }

        public void setEvidenceCode(String evidenceCode) {
            this.evidenceCode = evidenceCode;
        }

        public Long getMatchId() {
            return matchId;
        }

        public void setMatchId(Long matchId) {
            this.matchId = matchId;
        }

        public String getEvidenceType() {
            return evidenceType;
        }

        public void setEvidenceType(String evidenceType) {
            this.evidenceType = evidenceType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getPublishedTime() {
            return publishedTime;
        }

        public void setPublishedTime(String publishedTime) {
            this.publishedTime = publishedTime;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getSourceTier() {
            return sourceTier;
        }

        public void setSourceTier(String sourceTier) {
            this.sourceTier = sourceTier;
        }

        public String getFreshnessClass() {
            return freshnessClass;
        }

        public void setFreshnessClass(String freshnessClass) {
            this.freshnessClass = freshnessClass;
        }
    }

    /**
     * AI分析业务输出
     */
    class ReturnCombinationAiOutput {
        private List<Candidate> candidates;

        private String analysisContent;

        /** 供应商无关的AI调用链路引用；由适配器可选回填，不承载模型或费用等治理元数据 */
        private String aiRunRef;

        public List<Candidate> getCandidates() {
            return candidates;
        }

        public void setCandidates(List<Candidate> candidates) {
            this.candidates = candidates;
        }

        public String getAnalysisContent() {
            return analysisContent;
        }

        public void setAnalysisContent(String analysisContent) {
            this.analysisContent = analysisContent;
        }

        public String getAiRunRef() {
            return aiRunRef;
        }

        public void setAiRunRef(String aiRunRef) {
            this.aiRunRef = aiRunRef;
        }
    }

    /**
     * AI候选组合
     */
    class Candidate {
        private String title;

        private String reason;

        /** 候选置信度（0-100），仅用于复核和展示，不参与金额计算 */
        private Integer confidence;

        /** 候选风险说明，仅用于复核和展示 */
        private String riskNotes;

        private String analysisContent;

        /** 候选级完整证据链；自动AI必填，人工AI兼容路径为空 */
        private List<EvidenceFactor> evidenceChain;

        private List<Selection> selections;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getConfidence() {
            return confidence;
        }

        public void setConfidence(Integer confidence) {
            this.confidence = confidence;
        }

        public String getRiskNotes() {
            return riskNotes;
        }

        public void setRiskNotes(String riskNotes) {
            this.riskNotes = riskNotes;
        }

        public String getAnalysisContent() {
            return analysisContent;
        }

        public void setAnalysisContent(String analysisContent) {
            this.analysisContent = analysisContent;
        }

        public List<EvidenceFactor> getEvidenceChain() {
            return evidenceChain;
        }

        public void setEvidenceChain(List<EvidenceFactor> evidenceChain) {
            this.evidenceChain = evidenceChain;
        }

        public List<Selection> getSelections() {
            return selections;
        }

        public void setSelections(List<Selection> selections) {
            this.selections = selections;
        }
    }

    /**
     * 候选证据链因子
     */
    class EvidenceFactor {
        private String factorCode;

        private Long matchId;

        private String factorType;

        /** SUPPLIED、RETRIEVAL或MANUAL_WEIGHT */
        private String sourceType;

        private String sourceRef;

        private String observation;

        private String impact;

        /** 冲突组编码，同一组内允许同时出现支持与反对因子 */
        private String conflictGroup;

        private BigDecimal weightImpact;

        private Integer confidence;

        public String getFactorCode() {
            return factorCode;
        }

        public void setFactorCode(String factorCode) {
            this.factorCode = factorCode;
        }

        public Long getMatchId() {
            return matchId;
        }

        public void setMatchId(Long matchId) {
            this.matchId = matchId;
        }

        public String getFactorType() {
            return factorType;
        }

        public void setFactorType(String factorType) {
            this.factorType = factorType;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSourceRef() {
            return sourceRef;
        }

        public void setSourceRef(String sourceRef) {
            this.sourceRef = sourceRef;
        }

        public String getObservation() {
            return observation;
        }

        public void setObservation(String observation) {
            this.observation = observation;
        }

        public String getImpact() {
            return impact;
        }

        public void setImpact(String impact) {
            this.impact = impact;
        }

        public String getConflictGroup() {
            return conflictGroup;
        }

        public void setConflictGroup(String conflictGroup) {
            this.conflictGroup = conflictGroup;
        }

        public BigDecimal getWeightImpact() {
            return weightImpact;
        }

        public void setWeightImpact(BigDecimal weightImpact) {
            this.weightImpact = weightImpact;
        }

        public Integer getConfidence() {
            return confidence;
        }

        public void setConfidence(Integer confidence) {
            this.confidence = confidence;
        }
    }

    /**
     * AI候选比赛选择
     */
    class Selection {
        private Long matchId;

        private String selectionType;

        /**
         * AI评估权重（0.0000-1.0000），仅用于AI选型解释和历史复核。
         * 不参与金额计算，不是投注比例或金额系数。
         */
        private BigDecimal appliedWeight;

        private String reason;

        /** 完整权重推导说明，自动AI必填 */
        private String weightRationale;

        /** 本选择引用的证据链因子编码 */
        private List<String> evidenceRefs;

        /** 已考虑但未占主导的反向证据因子编码 */
        private List<String> counterEvidenceRefs;

        public Long getMatchId() {
            return matchId;
        }

        public void setMatchId(Long matchId) {
            this.matchId = matchId;
        }

        public String getSelectionType() {
            return selectionType;
        }

        public void setSelectionType(String selectionType) {
            this.selectionType = selectionType;
        }

        public BigDecimal getAppliedWeight() {
            // AI评估权重仅供生成解释，不进入金额公式。
            return appliedWeight;
        }

        public void setAppliedWeight(BigDecimal appliedWeight) {
            // AI评估权重仅供生成解释，不进入金额公式。
            this.appliedWeight = appliedWeight;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getWeightRationale() {
            return weightRationale;
        }

        public void setWeightRationale(String weightRationale) {
            this.weightRationale = weightRationale;
        }

        public List<String> getEvidenceRefs() {
            return evidenceRefs;
        }

        public void setEvidenceRefs(List<String> evidenceRefs) {
            this.evidenceRefs = evidenceRefs;
        }

        public List<String> getCounterEvidenceRefs() {
            return counterEvidenceRefs;
        }

        public void setCounterEvidenceRefs(List<String> counterEvidenceRefs) {
            this.counterEvidenceRefs = counterEvidenceRefs;
        }
    }
}
