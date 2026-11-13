package com.ruoyi.fx67ll.dortmund.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 孤儿图片清理结果。
 *
 * @author fx67ll
 * @date 2026-11-12
 */
public class Fx67llDortmundReturnImageCleanupResult {
    private int requestedCount;

    private int deletedCount;

    private int failedCount;

    private List<String> failedObjectKeys = new ArrayList<>();

    public int getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount = requestedCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public List<String> getFailedObjectKeys() {
        return failedObjectKeys;
    }

    public void setFailedObjectKeys(List<String> failedObjectKeys) {
        this.failedObjectKeys = failedObjectKeys;
    }
}
