package com.ruoyi.fx67ll.dortmund.ai;

import java.util.List;

/**
 * 回报组合自动证据检索端口。
 * <p>
 * 仅自动AI模式使用。端口只返回未受信任的检索候选，后续清洗、去重、哈希、证据编码、冻结和落库
 * 均由业务服务完成；人工AI任务不会调用该端口。
 * <p>
 * 【通用AI接入点】通用AI模块提供搜索工具时，以本端口作为供应商适配边界；查询覆盖、结果上限、
 * 内容清洗、URL约束、证据冻结和引用校验仍由回报组合Service负责。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public interface ReturnEvidenceRetrievalPort {
    /**
     * 当前部署是否具备可用的检索服务。
     */
    boolean isConfigured();

    /**
     * 执行单条有界检索。
     *
     * @param query      已由服务层校验的查询及其有界提示
     * @param maxResults 最大返回条数
     * @return 未受信任的原始检索结果
     */
    List<RetrievalResult> retrieve(ReturnCombinationAiAnalysisPort.EvidenceQuery query, int maxResults);

    /**
     * 检索结果业务字段
     */
    class RetrievalResult {
        private String title;

        private String url;

        private String snippet;

        private String sourceName;

        private String publishedAt;

        /** 检索网关提供的供应商无关来源层级，可为空 */
        private String sourceTier;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSnippet() {
            return snippet;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSourceTier() {
            return sourceTier;
        }

        public void setSourceTier(String sourceTier) {
            this.sourceTier = sourceTier;
        }
    }
}
