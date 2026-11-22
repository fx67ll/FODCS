package com.ruoyi.fx67ll.dortmund.domain;

/**
 * 回报组合赛果核验结果。
 * <p>
 * 单条核验时 combinationId 与 verificationStatus 表示最终状态；批量核验时通过分项计数反馈
 * 成功、失败、未核验和跳过的数量，便于前端一次性说明整批处理结果。
 *
 * @author fx67ll
 * @date 2026-11-22
 */
public class Fx67llDortmundReturnVerificationResult {
    /** 单条核验对应的回报组合主键；批量核验时为空 */
    private Long combinationId;

    /** 单条核验后的最终状态（Y/N，null 表示未核验）；批量核验时为空 */
    private String verificationStatus;

    /** 本次请求涉及的组合数量 */
    private int requestedCount;

    /** 核验成功数量 */
    private int successCount;

    /** 核验失败数量 */
    private int failedCount;

    /** 因组合内比赛缺少赛果而保持未核验的数量 */
    private int unverifiedCount;

    /** 因未采用而在批量核验中跳过的数量 */
    private int skippedCount;

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public int getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount = requestedCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getUnverifiedCount() {
        return unverifiedCount;
    }

    public void setUnverifiedCount(int unverifiedCount) {
        this.unverifiedCount = unverifiedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }
}
