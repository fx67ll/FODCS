package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnEvidence;
import org.apache.ibatis.annotations.Param;

/**
 * 回报组合证据Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public interface Fx67llDortmundReturnEvidenceMapper {
    /**
     * 批量固化批次证据；仅自动AI成功事务调用。
     */
    int insertBatch(@Param("values") List<Fx67llDortmundReturnEvidence> values);

    /**
     * 按批次查询冻结证据，供审计与后续通用AI模块复用。
     */
    List<Fx67llDortmundReturnEvidence> selectByGenerationId(Long generationId);
}
