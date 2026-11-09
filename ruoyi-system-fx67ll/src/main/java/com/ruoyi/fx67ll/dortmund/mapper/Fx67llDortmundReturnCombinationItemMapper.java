package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationItem;

/**
 * 回报组合明细Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundReturnCombinationItemMapper {
    public List<Fx67llDortmundReturnCombinationItem> selectItemsByCombinationId(Long combinationId);

    public int insertFx67llDortmundReturnCombinationItem(
            Fx67llDortmundReturnCombinationItem fx67llDortmundReturnCombinationItem);
}
