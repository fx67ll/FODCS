package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
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
     * 按主键集合批量查询比赛（用于方案计划列表关联比赛补全）
     *
     * @param matchIds 比赛主键集合
     * @return 比赛集合
     */
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchListByIds(@Param("matchIds") List<Long> matchIds);

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
     * 仅更新比赛常规赛果和倍率赛果，供 APP 移动端赛果补录使用。
     * <p>显式写入空值以支持撤销误录；比赛编排字段保持不变。</p>
     *
     * @param matchId     比赛记录主键
     * @param matchResult 常规赛果
     * @param oddsResult  倍率赛果
     * @param updateBy    更新人
     * @return 结果
     */
    public int updateMatchResultsForApp(@Param("matchId") Long matchId,
            @Param("matchResult") String matchResult, @Param("oddsResult") String oddsResult,
            @Param("updateBy") String updateBy);

    /**
     * 导入比赛结果时仅更新请求中明确提供的赛果字段。
     * <p>null 表示保留原值，空字符串表示清除误录；其他编排字段保持不变。</p>
     *
     * @param matchId     比赛主键
     * @param matchResult 常规赛果；null 表示不修改
     * @param oddsResult  倍率赛果；null 表示不修改
     * @param updateBy    更新人
     * @return 结果
     */
    public int updateMatchResultsForImport(@Param("matchId") Long matchId,
            @Param("matchResult") String matchResult, @Param("oddsResult") String oddsResult,
            @Param("updateBy") String updateBy);

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
