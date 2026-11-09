package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;

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
}
