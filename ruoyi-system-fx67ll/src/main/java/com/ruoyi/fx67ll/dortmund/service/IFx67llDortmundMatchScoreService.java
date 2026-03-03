package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchScore;

/**
 * 比赛标准化评分Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundMatchScoreService 
{
    /**
     * 查询比赛标准化评分
     * 
     * @param scoreId 比赛标准化评分主键
     * @return 比赛标准化评分
     */
    public Fx67llDortmundMatchScore selectFx67llDortmundMatchScoreByScoreId(Long scoreId);

    /**
     * 查询比赛标准化评分列表
     * 
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 比赛标准化评分集合
     */
    public List<Fx67llDortmundMatchScore> selectFx67llDortmundMatchScoreList(Fx67llDortmundMatchScore fx67llDortmundMatchScore);

    /**
     * 通过 UserId 查询比赛标准化评分列表
     *
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 比赛标准化评分集合
     */
    public List<Fx67llDortmundMatchScore> selectFx67llDortmundMatchScoreListByUserId(Fx67llDortmundMatchScore fx67llDortmundMatchScore);

    /**
     * 新增比赛标准化评分
     * 
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 结果
     */
    public int insertFx67llDortmundMatchScore(Fx67llDortmundMatchScore fx67llDortmundMatchScore);

    /**
     * 修改比赛标准化评分
     * 
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 结果
     */
    public int updateFx67llDortmundMatchScore(Fx67llDortmundMatchScore fx67llDortmundMatchScore);

    /**
     * 批量删除比赛标准化评分
     * 
     * @param scoreIds 需要删除的比赛标准化评分主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundMatchScoreByScoreIds(Long[] scoreIds);

    /**
     * 删除比赛标准化评分信息
     * 
     * @param scoreId 比赛标准化评分主键
     * @return 结果
     */
    public int deleteFx67llDortmundMatchScoreByScoreId(Long scoreId);
}
