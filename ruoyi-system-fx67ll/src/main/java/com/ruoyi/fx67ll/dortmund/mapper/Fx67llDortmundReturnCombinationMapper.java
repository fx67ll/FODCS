package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;

/**
 * 回报组合Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundReturnCombinationMapper {
    public Fx67llDortmundReturnCombination selectFx67llDortmundReturnCombinationByCombinationId(Long combinationId);

    public Fx67llDortmundReturnCombination selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(
            Long combinationId);

    public List<Fx67llDortmundReturnCombination> selectFx67llDortmundReturnCombinationList(
            Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    public int insertFx67llDortmundReturnCombination(
            Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    public int updateAdoptedFlag(Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    public int countPlanRelationsByCombinationId(Long combinationId);
}
