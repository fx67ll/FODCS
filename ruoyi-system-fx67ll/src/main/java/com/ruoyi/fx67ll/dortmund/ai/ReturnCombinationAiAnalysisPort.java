package com.ruoyi.fx67ll.dortmund.ai;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;

/**
 * Dortmund回报组合AI分析业务边界
 * <p>
 * 仅传递业务输入和业务输出，不持久化供应商、模型、Prompt、Token、费用或调用链路等AI元数据
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface ReturnCombinationAiAnalysisPort {
    /**
     * 执行回报组合AI分析
     *
     * @param input 已校验的业务输入
     * @return 业务可消费的AI分析输出
     */
    ReturnCombinationAiOutput analyze(ReturnCombinationAiInput input);

    /**
     * AI分析业务输入
     */
    class ReturnCombinationAiInput {
        private String generationMode;

        private BigDecimal inputAmount;

        private BigDecimal targetAmount;

        private List<Fx67llDortmundOdds> odds;

        /** 本次调用的显式业务目标，供未来通用AI编排复用 */
        private List<String> businessObjectives;

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
    }

    /**
     * AI分析业务输出
     */
    class ReturnCombinationAiOutput {
        private List<Candidate> candidates;

        private String analysisContent;

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
    }

    /**
     * AI候选组合
     */
    class Candidate {
        private String title;

        private String reason;

        private String analysisContent;

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

        public String getAnalysisContent() {
            return analysisContent;
        }

        public void setAnalysisContent(String analysisContent) {
            this.analysisContent = analysisContent;
        }

        public List<Selection> getSelections() {
            return selections;
        }

        public void setSelections(List<Selection> selections) {
            this.selections = selections;
        }
    }

    /**
     * AI候选比赛选择
     */
    class Selection {
        private Long matchId;

        private String selectionType;

        /**
         * AI评估权重（0-100），仅用于AI选型解释和历史复核。
         * 不参与金额计算，不是投注比例或金额系数。
         */
        private BigDecimal appliedWeight;

        private String reason;

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
    }
}
