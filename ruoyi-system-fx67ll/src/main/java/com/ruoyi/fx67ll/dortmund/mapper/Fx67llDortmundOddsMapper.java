package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;

/**
 * Dortmund倍率记录Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundOddsMapper {
    public Fx67llDortmundOdds selectFx67llDortmundOddsByOddsId(Long oddsId);

    public List<Fx67llDortmundOdds> selectFx67llDortmundOddsList(Fx67llDortmundOdds fx67llDortmundOdds);

    public Fx67llDortmundOdds selectActiveFx67llDortmundOddsByMatchId(Long matchId);

    public int countActiveOddsByMatchId(Long matchId);

    public Long selectMatchIdForUpdate(Long matchId);

    public int insertFx67llDortmundOdds(Fx67llDortmundOdds fx67llDortmundOdds);

    public int updateFx67llDortmundOdds(Fx67llDortmundOdds fx67llDortmundOdds);

    public int deactivateOddsByMatchId(Fx67llDortmundOdds fx67llDortmundOdds);

    public int activateOtherOddsByMatchId(Fx67llDortmundOdds fx67llDortmundOdds);

    public int activateFx67llDortmundOdds(Fx67llDortmundOdds fx67llDortmundOdds);

    public int deleteFx67llDortmundOddsByOddsIds(Long[] oddsIds);

    public int countCombinationItemsByOddsId(Long oddsId);

    public int countOddsByMatchId(Long matchId);
}
