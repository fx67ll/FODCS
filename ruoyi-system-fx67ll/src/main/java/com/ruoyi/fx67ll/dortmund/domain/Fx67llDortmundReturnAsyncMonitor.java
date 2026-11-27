package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;
import java.util.List;

/**
 * Dortmund 回报组合异步任务监控快照。
 * <p>聚合数据只读取任务状态与耗时字段，不加载请求快照、证据计划或AI结果等大字段，避免监控刷新本身放大数据库开销。</p>
 *
 * @author fx67ll
 * @date 2026-11-27
 */
public class Fx67llDortmundReturnAsyncMonitor {
    /** 任务总数 */
    private Long totalCount;

    /** 处理中任务数 */
    private Long runningCount;

    /** 成功任务数 */
    private Long successCount;

    /** 失败任务数 */
    private Long failedCount;

    /** 待人工导入任务数 */
    private Long manualPendingCount;

    /** 租约已过期的处理中任务数 */
    private Long staleRunningCount;

    /** 处于重试等待窗口的任务数 */
    private Long retryWaitingCount;

    /** 最近24小时受理任务数 */
    private Long recent24hCount;

    /** 已完成任务平均耗时（秒，含排队时间） */
    private Double averageDurationSeconds;

    /** 已完成任务最大耗时（秒，含排队时间） */
    private Long maxDurationSeconds;

    /** 快照生成时间 */
    private Date generatedAt;

    /** 当前处理中任务 */
    private List<Fx67llDortmundReturnGeneration> runningTasks;

    /** 异常任务：失败与租约过期的处理中任务 */
    private List<Fx67llDortmundReturnGeneration> abnormalTasks;

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Long runningCount) {
        this.runningCount = runningCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getManualPendingCount() {
        return manualPendingCount;
    }

    public void setManualPendingCount(Long manualPendingCount) {
        this.manualPendingCount = manualPendingCount;
    }

    public Long getStaleRunningCount() {
        return staleRunningCount;
    }

    public void setStaleRunningCount(Long staleRunningCount) {
        this.staleRunningCount = staleRunningCount;
    }

    public Long getRetryWaitingCount() {
        return retryWaitingCount;
    }

    public void setRetryWaitingCount(Long retryWaitingCount) {
        this.retryWaitingCount = retryWaitingCount;
    }

    public Long getRecent24hCount() {
        return recent24hCount;
    }

    public void setRecent24hCount(Long recent24hCount) {
        this.recent24hCount = recent24hCount;
    }

    public Double getAverageDurationSeconds() {
        return averageDurationSeconds;
    }

    public void setAverageDurationSeconds(Double averageDurationSeconds) {
        this.averageDurationSeconds = averageDurationSeconds;
    }

    public Long getMaxDurationSeconds() {
        return maxDurationSeconds;
    }

    public void setMaxDurationSeconds(Long maxDurationSeconds) {
        this.maxDurationSeconds = maxDurationSeconds;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<Fx67llDortmundReturnGeneration> getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(List<Fx67llDortmundReturnGeneration> runningTasks) {
        this.runningTasks = runningTasks;
    }

    public List<Fx67llDortmundReturnGeneration> getAbnormalTasks() {
        return abnormalTasks;
    }

    public void setAbnormalTasks(List<Fx67llDortmundReturnGeneration> abnormalTasks) {
        this.abnormalTasks = abnormalTasks;
    }
}
