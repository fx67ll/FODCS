package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;

/**
 * 比赛记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llDortmundBasicMatchMapper 
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
     * 删除比赛记录
     * 
     * @param matchId 比赛记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchId(Long matchId);

    /**
     * 批量删除比赛记录
     * 
     * @param matchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchIds(Long[] matchIds);
}
