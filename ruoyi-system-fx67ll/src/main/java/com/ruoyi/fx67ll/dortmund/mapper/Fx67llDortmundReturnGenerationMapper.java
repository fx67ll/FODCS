package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

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
            @Param("remark") String remark, @Param("updateBy") String updateBy);

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
}
