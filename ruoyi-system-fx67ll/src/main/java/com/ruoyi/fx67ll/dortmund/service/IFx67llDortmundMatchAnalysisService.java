package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchAnalysis;

/**
 * 比赛AI分析原始结果Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundMatchAnalysisService 
{
    /**
     * 查询比赛AI分析原始结果
     * 
     * @param analysisId 比赛AI分析原始结果主键
     * @return 比赛AI分析原始结果
     */
    public Fx67llDortmundMatchAnalysis selectFx67llDortmundMatchAnalysisByAnalysisId(Long analysisId);

    /**
     * 查询比赛AI分析原始结果列表
     * 
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 比赛AI分析原始结果集合
     */
    public List<Fx67llDortmundMatchAnalysis> selectFx67llDortmundMatchAnalysisList(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis);

    /**
     * 通过 UserId 查询比赛AI分析原始结果列表
     *
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 比赛AI分析原始结果集合
     */
    public List<Fx67llDortmundMatchAnalysis> selectFx67llDortmundMatchAnalysisListByUserId(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis);

    /**
     * 新增比赛AI分析原始结果
     * 
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 结果
     */
    public int insertFx67llDortmundMatchAnalysis(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis);

    /**
     * 修改比赛AI分析原始结果
     * 
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 结果
     */
    public int updateFx67llDortmundMatchAnalysis(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis);

    /**
     * 批量删除比赛AI分析原始结果
     * 
     * @param analysisIds 需要删除的比赛AI分析原始结果主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundMatchAnalysisByAnalysisIds(Long[] analysisIds);

    /**
     * 删除比赛AI分析原始结果信息
     * 
     * @param analysisId 比赛AI分析原始结果主键
     * @return 结果
     */
    public int deleteFx67llDortmundMatchAnalysisByAnalysisId(Long analysisId);
}
