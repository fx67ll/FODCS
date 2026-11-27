package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

/**
 * 异步任务异常清理结果。
 *
 * @author fx67ll
 * @date 2026-11-27
 */
public class Fx67llDortmundReturnAsyncCleanupResult {
    /** 本次转为终态失败的过期处理中任务数 */
    private int staleMarkedFailedCount;

    /** 实际删除的失败任务数 */
    private int deletedCount;

    /** 超期但因组合/证据依赖或单次清理上限而保留的失败任务数 */
    private int skippedDependencyCount;

    /** 尚未超过保留期的失败任务数 */
    private int retainedCount;

    /** 清理后仍存在的失败任务数 */
    private int remainingFailedCount;

    /** 失败任务清理截止时间 */
    private Date cleanupBefore;

    public int getStaleMarkedFailedCount() {
        return staleMarkedFailedCount;
    }

    public void setStaleMarkedFailedCount(int staleMarkedFailedCount) {
        this.staleMarkedFailedCount = staleMarkedFailedCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public int getSkippedDependencyCount() {
        return skippedDependencyCount;
    }

    public void setSkippedDependencyCount(int skippedDependencyCount) {
        this.skippedDependencyCount = skippedDependencyCount;
    }

    public int getRetainedCount() {
        return retainedCount;
    }

    public void setRetainedCount(int retainedCount) {
        this.retainedCount = retainedCount;
    }

    public int getRemainingFailedCount() {
        return remainingFailedCount;
    }

    public void setRemainingFailedCount(int remainingFailedCount) {
        this.remainingFailedCount = remainingFailedCount;
    }

    public Date getCleanupBefore() {
        return cleanupBefore;
    }

    public void setCleanupBefore(Date cleanupBefore) {
        this.cleanupBefore = cleanupBefore;
    }
}
