package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnAsyncMonitor;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import org.apache.ibatis.annotations.Param;

/**
 * 回报组合生成批次Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundReturnGenerationMapper {
    public Fx67llDortmundReturnGeneration selectFx67llDortmundReturnGenerationByGenerationId(Long generationId);

    public List<Fx67llDortmundReturnGeneration> selectFx67llDortmundReturnGenerationList(
            Fx67llDortmundReturnGeneration fx67llDortmundReturnGeneration);

    public int insertFx67llDortmundReturnGeneration(
            Fx67llDortmundReturnGeneration fx67llDortmundReturnGeneration);

    public int updateFx67llDortmundReturnGeneration(
            Fx67llDortmundReturnGeneration fx67llDortmundReturnGeneration);

    /**
     * 按用户与客户端请求号查询已有批次，用于异步提交幂等。
     */
    public Fx67llDortmundReturnGeneration selectByClientRequestId(@Param("userId") Long userId,
            @Param("clientRequestId") String clientRequestId);

    /**
     * 查询当前可被worker领取的批次主键。
     */
    public List<Long> selectPendingGenerationIds(@Param("limit") int limit);

    /**
     * 使用条件更新原子领取批次，MySQL 5.7不依赖SKIP LOCKED。
     */
    public int claimGeneration(@Param("generationId") Long generationId, @Param("workerId") String workerId,
            @Param("leaseSeconds") int leaseSeconds, @Param("maxAttempts") int maxAttempts);

    /**
     * 锁定批次行，供成功结果短事务做worker/status二次校验。
     */
    public Fx67llDortmundReturnGeneration selectGenerationForUpdate(@Param("generationId") Long generationId);

    /**
     * 成功完成批次，仅租约持有者且仍处理中时生效。
     */
    public int completeGenerationSuccess(@Param("generationId") Long generationId,
            @Param("workerId") String workerId, @Param("generatedCount") int generatedCount,
            @Param("remark") String remark, @Param("updateBy") String updateBy,
            @Param("aiResultSnapshot") String aiResultSnapshot,
            @Param("aiAnalysisRunRef") String aiAnalysisRunRef,
            @Param("aiAnalysisInputSnapshot") String aiAnalysisInputSnapshot,
            @Param("aiCallRefs") String aiCallRefs);

    /**
     * 人工导入成功完成批次；允许从待人工导入、遗留失败或已完成的人工来源批次更新，防止自动成功结果被覆盖。
     */
    public int completeManualGenerationSuccess(@Param("generationId") Long generationId,
            @Param("generatedCount") int generatedCount, @Param("remark") String remark,
            @Param("updateBy") String updateBy, @Param("aiResultSnapshot") String aiResultSnapshot,
            @Param("aiAnalysisInputSnapshot") String aiAnalysisInputSnapshot);

    /**
     * 释放租约并安排有限重试。
     */
    public int releaseGenerationForRetry(@Param("generationId") Long generationId,
            @Param("workerId") String workerId, @Param("remark") String remark,
            @Param("retrySeconds") int retrySeconds, @Param("updateBy") String updateBy);

    /**
     * 终态失败，仅租约持有者且仍处理中时生效。
     */
    public int failGeneration(@Param("generationId") Long generationId, @Param("workerId") String workerId,
            @Param("remark") String remark, @Param("updateBy") String updateBy);

    /**
     * 回收租约已过期且尝试次数达到上限的批次，防止worker崩溃后永久停留在处理中。
     */
    public int failExhaustedGeneration(@Param("generationId") Long generationId, @Param("remark") String remark);

    /**
     * 当前worker开始证据规划或检索，重置上一次尝试的证据状态。
     */
    public int startEvidenceWork(@Param("generationId") Long generationId, @Param("workerId") String workerId);

    /**
     * 在规划调用前保存规划业务输入快照，保证供应商调用失败时仍可审计。
     */
    public int savePlanInputSnapshot(@Param("generationId") Long generationId,
            @Param("workerId") String workerId, @Param("planInputSnapshot") String planInputSnapshot);

    /**
     * 保存自动AI检索计划输出、规划调用引用和供应商无关调用审计数组。
     */
    public int saveEvidencePlan(@Param("generationId") Long generationId, @Param("workerId") String workerId,
            @Param("planJson") String planJson, @Param("planRunRef") String planRunRef,
            @Param("aiCallRefs") String aiCallRefs);

    /**
     * 在最终分析调用前保存分析业务输入快照。
     */
    public int saveAnalysisInputSnapshot(@Param("generationId") Long generationId,
            @Param("workerId") String workerId, @Param("analysisInputSnapshot") String analysisInputSnapshot);

    /**
     * 更新当前worker的证据流程状态与受控错误摘要。
     */
    public int updateEvidenceStatus(@Param("generationId") Long generationId,
            @Param("workerId") String workerId, @Param("status") String status,
            @Param("error") String error);

    /**
     * 查询轻量异步任务聚合指标，不读取任何大文本快照。
     */
    public Fx67llDortmundReturnAsyncMonitor selectAsyncTaskSummary();

    /**
     * 查询当前处理中任务，供监控面板展示；不返回请求、证据和AI结果快照。
     */
    public List<Fx67llDortmundReturnGeneration> selectRunningAsyncTasks(@Param("limit") int limit);

    /**
     * 查询失败任务和租约过期的处理中任务，供管理员定位异常。
     */
    public List<Fx67llDortmundReturnGeneration> selectAbnormalAsyncTasks(@Param("limit") int limit);

    /**
     * 检查任务是否仍由当前worker持有且处于处理中，用于停止后的协作式中断。
     */
    public int countOwnedProcessingGeneration(@Param("generationId") Long generationId,
            @Param("workerId") String workerId);

    /**
     * 管理员手动停止处理中任务；清空租约后worker的后续条件写入将自动失效。
     */
    public int adminStopGeneration(@Param("generationId") Long generationId,
            @Param("remark") String remark, @Param("updateBy") String updateBy);

    /**
     * 将租约过期且尝试次数耗尽的处理中任务转为终态失败。
     */
    public int markExhaustedAsyncTasksFailed(@Param("remark") String remark);

    /**
     * 统计指定时间前结束的失败任务。
     */
    public int countFailedGenerationsBefore(@Param("before") java.util.Date before);

    /**
     * 统计指定时间前结束且没有组合或证据依赖、可安全删除的失败任务。
     */
    public int countCleanupEligibleGenerations(@Param("before") java.util.Date before);

    /**
     * 查询可安全删除的失败任务主键，单次受数量上限约束。
     */
    public List<Long> selectCleanupEligibleGenerationIds(@Param("before") java.util.Date before,
            @Param("limit") int limit);

    /**
     * 删除仍满足失败终态、时间阈值且无组合/证据依赖的任务，防止检查后被并发引用。
     */
    public int deleteCleanupEligibleGeneration(@Param("generationId") Long generationId,
            @Param("before") java.util.Date before);
}
