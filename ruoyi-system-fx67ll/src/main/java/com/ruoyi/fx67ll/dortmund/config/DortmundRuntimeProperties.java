package com.ruoyi.fx67ll.dortmund.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dortmund 模块类型化运行配置。
 * <p>
 * 字段初始值引用 {@link DortmundModuleConfig}，部署方仍可通过环境变量覆盖。启动时会把低于业务
 * 安全下限的覆盖值抬回安全值并告警，避免配置成功但运行时预算不足。供应商地址、密钥和模型名
 * 只从部署环境读取，不写入代码常量。
 *
 * @author fx67ll
 * @date 2026-11-20
 */
@Component
@ConfigurationProperties(prefix = "dortmund")
public class DortmundRuntimeProperties {
    private static final Logger log = LoggerFactory.getLogger(DortmundRuntimeProperties.class);

    private Ai ai = new Ai();

    private ReturnSettings returnSettings = new ReturnSettings();

    public Ai getAi() {
        return ai;
    }

    public void setAi(Ai ai) {
        this.ai = ai;
    }

    /**
     * 绑定 dortmund.return。Java 不能声明名为 return 的字段，因此以 getReturn 暴露属性名。
     */
    public ReturnSettings getReturn() {
        return returnSettings;
    }

    public void setReturn(ReturnSettings returnSettings) {
        this.returnSettings = returnSettings;
    }

    /** 供调度器 SpEL 读取，避免表达式中出现 Java 关键字。 */
    public ReturnSettings getReturnSettings() {
        return returnSettings;
    }

    /** 启动时统一兜底和告警，确保运行预算不低于串行最坏路径。 */
    @PostConstruct
    public void enforceSafeBounds() {
        if (DortmundModuleConfig.Return.MAX_COMBINATION_MATCHES
                > DortmundModuleConfig.Return.MAX_ANALYSIS_MATCHES) {
            throw new IllegalStateException("单候选腿数不能超过分析比赛池上限");
        }
        if (DortmundModuleConfig.Retrieval.MAX_QUERIES
                < DortmundModuleConfig.Return.MAX_ANALYSIS_MATCHES) {
            throw new IllegalStateException("证据检索查询上限不能低于分析比赛池上限");
        }
        ai.deepseek.normalize();
        ai.retrieval.normalize();
        returnSettings.generation.normalize(DortmundModuleConfig.Retrieval.MAX_QUERIES,
                ai.retrieval.timeoutSeconds);
        returnSettings.image.normalize();
    }

    /** AI 调用配置。 */
    public static class Ai {
        private DeepSeek deepseek = new DeepSeek();
        private Retrieval retrieval = new Retrieval();

        public DeepSeek getDeepseek() {
            return deepseek;
        }

        public void setDeepseek(DeepSeek deepseek) {
            this.deepseek = deepseek;
        }

        public Retrieval getRetrieval() {
            return retrieval;
        }

        public void setRetrieval(Retrieval retrieval) {
            this.retrieval = retrieval;
        }
    }

    /** DeepSeek 供应商适配器配置；敏感字段默认为空。 */
    public static class DeepSeek {
        private String url;
        private String apiKey;
        private String model;
        private int maxTokens = DortmundModuleConfig.Ai.ANALYSIS_MAX_TOKENS;
        private int plannerMaxTokens = DortmundModuleConfig.Ai.PLANNER_MAX_TOKENS;
        private int connectTimeoutSeconds = DortmundModuleConfig.Ai.CONNECT_TIMEOUT_SECONDS;
        private int readTimeoutSeconds = DortmundModuleConfig.Ai.DEFAULT_READ_TIMEOUT_SECONDS;
        private int analysisTimeoutSeconds = DortmundModuleConfig.Ai.ANALYSIS_TIMEOUT_SECONDS;
        private int planTimeoutSeconds = DortmundModuleConfig.Ai.PLAN_TIMEOUT_SECONDS;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
        }

        public int getPlannerMaxTokens() {
            return plannerMaxTokens;
        }

        public void setPlannerMaxTokens(int plannerMaxTokens) {
            this.plannerMaxTokens = plannerMaxTokens;
        }

        public int getConnectTimeoutSeconds() {
            return connectTimeoutSeconds;
        }

        public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
        }

        public int getReadTimeoutSeconds() {
            return readTimeoutSeconds;
        }

        public void setReadTimeoutSeconds(int readTimeoutSeconds) {
            this.readTimeoutSeconds = readTimeoutSeconds;
        }

        public int getAnalysisTimeoutSeconds() {
            return analysisTimeoutSeconds;
        }

        public void setAnalysisTimeoutSeconds(int analysisTimeoutSeconds) {
            this.analysisTimeoutSeconds = analysisTimeoutSeconds;
        }

        public int getPlanTimeoutSeconds() {
            return planTimeoutSeconds;
        }

        public void setPlanTimeoutSeconds(int planTimeoutSeconds) {
            this.planTimeoutSeconds = planTimeoutSeconds;
        }

        private void normalize() {
            if (maxTokens <= 0) {
                maxTokens = DortmundModuleConfig.Ai.ANALYSIS_MAX_TOKENS;
            }
            if (plannerMaxTokens <= 0) {
                plannerMaxTokens = Math.min(maxTokens, DortmundModuleConfig.Ai.PLANNER_MAX_TOKENS);
            }
            if (connectTimeoutSeconds <= 0) {
                connectTimeoutSeconds = DortmundModuleConfig.Ai.CONNECT_TIMEOUT_SECONDS;
            }
            if (readTimeoutSeconds <= 0) {
                readTimeoutSeconds = DortmundModuleConfig.Ai.DEFAULT_READ_TIMEOUT_SECONDS;
            }
            if (analysisTimeoutSeconds <= 0) {
                analysisTimeoutSeconds = DortmundModuleConfig.Ai.ANALYSIS_TIMEOUT_SECONDS;
            }
            if (planTimeoutSeconds <= 0) {
                planTimeoutSeconds = DortmundModuleConfig.Ai.PLAN_TIMEOUT_SECONDS;
            }
        }
    }

    /** 证据检索网关配置。 */
    public static class Retrieval {
        private String url;
        private String apiKey;
        private int maxResults = DortmundModuleConfig.Retrieval.MAX_RESULTS_PER_QUERY;
        private int timeoutSeconds = DortmundModuleConfig.Retrieval.DEFAULT_TIMEOUT_SECONDS;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public int getMaxResults() {
            return maxResults;
        }

        public void setMaxResults(int maxResults) {
            this.maxResults = maxResults;
        }

        public int getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        private void normalize() {
            if (maxResults <= 0) {
                maxResults = DortmundModuleConfig.Retrieval.MAX_RESULTS_PER_QUERY;
            }
            if (timeoutSeconds < DortmundModuleConfig.Retrieval.MIN_TIMEOUT_SECONDS) {
                timeoutSeconds = DortmundModuleConfig.Retrieval.MIN_TIMEOUT_SECONDS;
            }
        }
    }

    /** 回报组合生成与图片运行配置。 */
    public static class ReturnSettings {
        private Generation generation = new Generation();
        private Image image = new Image();

        public Generation getGeneration() {
            return generation;
        }

        public void setGeneration(Generation generation) {
            this.generation = generation;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }

    /** 异步生成租约、扫描和重试配置。 */
    public static class Generation {
        private int leaseSeconds = DortmundModuleConfig.Worker.DEFAULT_LEASE_SECONDS;
        private int maxAttempts = DortmundModuleConfig.Worker.MAX_ATTEMPTS;
        private int retryDelaySeconds = DortmundModuleConfig.Worker.RETRY_DELAY_SECONDS;
        private int dispatchBatchSize = DortmundModuleConfig.Worker.DISPATCH_BATCH_SIZE;
        private long dispatchIntervalMs = DortmundModuleConfig.Worker.DISPATCH_INTERVAL_MILLIS;
        private long dispatchInitialDelayMs = DortmundModuleConfig.Worker.DISPATCH_INITIAL_DELAY_MILLIS;

        public int getLeaseSeconds() {
            return leaseSeconds;
        }

        public void setLeaseSeconds(int leaseSeconds) {
            this.leaseSeconds = leaseSeconds;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public int getRetryDelaySeconds() {
            return retryDelaySeconds;
        }

        public void setRetryDelaySeconds(int retryDelaySeconds) {
            this.retryDelaySeconds = retryDelaySeconds;
        }

        public int getDispatchBatchSize() {
            return dispatchBatchSize;
        }

        public void setDispatchBatchSize(int dispatchBatchSize) {
            this.dispatchBatchSize = dispatchBatchSize;
        }

        public long getDispatchIntervalMs() {
            return dispatchIntervalMs;
        }

        public void setDispatchIntervalMs(long dispatchIntervalMs) {
            this.dispatchIntervalMs = dispatchIntervalMs;
        }

        public long getDispatchInitialDelayMs() {
            return dispatchInitialDelayMs;
        }

        public void setDispatchInitialDelayMs(long dispatchInitialDelayMs) {
            this.dispatchInitialDelayMs = dispatchInitialDelayMs;
        }

        private void normalize(int maxQueries, int retrievalTimeoutSeconds) {
            int minimumLease = DortmundModuleConfig.Ai.PLAN_TIMEOUT_SECONDS
                    + Math.max(maxQueries, DortmundModuleConfig.Return.MAX_ANALYSIS_MATCHES)
                    * retrievalTimeoutSeconds
                    + DortmundModuleConfig.Ai.ANALYSIS_TIMEOUT_SECONDS
                    + DortmundModuleConfig.Worker.LEASE_MARGIN_SECONDS;
            if (leaseSeconds < minimumLease) {
                log.warn("dortmund.return.generation.lease-seconds={}低于最小串行预算{}秒，运行时使用最小值。",
                        leaseSeconds, minimumLease);
                leaseSeconds = minimumLease;
            }
            maxAttempts = Math.max(maxAttempts, 1);
            retryDelaySeconds = Math.max(retryDelaySeconds, 1);
            dispatchBatchSize = Math.max(dispatchBatchSize, 1);
            dispatchIntervalMs = Math.max(dispatchIntervalMs, 1000L);
            dispatchInitialDelayMs = Math.max(dispatchInitialDelayMs, 0L);
        }
    }

    /** 回报组合图片有效期与清理配置。 */
    public static class Image {
        private int tempHours = DortmundModuleConfig.Worker.IMAGE_TEMP_HOURS;
        private int cleanupBatchSize = DortmundModuleConfig.Worker.IMAGE_CLEANUP_BATCH_SIZE;
        private int deleteRetryLimit = DortmundModuleConfig.Worker.IMAGE_DELETE_RETRY_LIMIT;
        private long cleanupIntervalMs = DortmundModuleConfig.Worker.IMAGE_CLEANUP_INTERVAL_MILLIS;
        private long cleanupInitialDelayMs = DortmundModuleConfig.Worker.IMAGE_CLEANUP_INITIAL_DELAY_MILLIS;

        public int getTempHours() {
            return tempHours;
        }

        public void setTempHours(int tempHours) {
            this.tempHours = tempHours;
        }

        public int getCleanupBatchSize() {
            return cleanupBatchSize;
        }

        public void setCleanupBatchSize(int cleanupBatchSize) {
            this.cleanupBatchSize = cleanupBatchSize;
        }

        public int getDeleteRetryLimit() {
            return deleteRetryLimit;
        }

        public void setDeleteRetryLimit(int deleteRetryLimit) {
            this.deleteRetryLimit = deleteRetryLimit;
        }

        public long getCleanupIntervalMs() {
            return cleanupIntervalMs;
        }

        public void setCleanupIntervalMs(long cleanupIntervalMs) {
            this.cleanupIntervalMs = cleanupIntervalMs;
        }

        public long getCleanupInitialDelayMs() {
            return cleanupInitialDelayMs;
        }

        public void setCleanupInitialDelayMs(long cleanupInitialDelayMs) {
            this.cleanupInitialDelayMs = cleanupInitialDelayMs;
        }

        private void normalize() {
            tempHours = Math.max(tempHours, 1);
            cleanupBatchSize = Math.max(cleanupBatchSize, 1);
            deleteRetryLimit = Math.max(deleteRetryLimit, 1);
            cleanupIntervalMs = Math.max(cleanupIntervalMs, 1000L);
            cleanupInitialDelayMs = Math.max(cleanupInitialDelayMs, 0L);
        }
    }
}
