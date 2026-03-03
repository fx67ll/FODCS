package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;

/**
 * 比赛记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundBasicMatchService 
{
    /**
     * 查询比赛记录
     * 
     * @param matchId 比赛记录主键
     * @return 比赛记录
     */
    public Fx67llDortmundBasicMatch selectFx67llDortmundBasicMatchByMatchId(Long matchId);

    /**
     * 查询比赛记录列表
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 比赛记录集合
     */
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchList(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 通过 UserId 查询比赛记录列表
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 比赛记录集合
     */
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchListByUserId(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 新增比赛记录
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    public int insertFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 修改比赛记录
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    public int updateFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 批量删除比赛记录
     * 
     * @param matchIds 需要删除的比赛记录主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchIds(Long[] matchIds);

    /**
     * 删除比赛记录信息
     * 
     * @param matchId 比赛记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchId(Long matchId);
}
