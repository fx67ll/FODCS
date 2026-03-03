package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundMatchAnalysisMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchAnalysis;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundMatchAnalysisService;

/**
 * 比赛AI分析原始结果Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundMatchAnalysisServiceImpl implements IFx67llDortmundMatchAnalysisService {
    @Autowired
    private Fx67llDortmundMatchAnalysisMapper fx67llDortmundMatchAnalysisMapper;

    /**
     * 查询比赛AI分析原始结果
     *
     * @param analysisId 比赛AI分析原始结果主键
     * @return 比赛AI分析原始结果
     */
    @Override
    public Fx67llDortmundMatchAnalysis selectFx67llDortmundMatchAnalysisByAnalysisId(Long analysisId) {
        return fx67llDortmundMatchAnalysisMapper.selectFx67llDortmundMatchAnalysisByAnalysisId(analysisId);
    }

    /**
     * 查询比赛AI分析原始结果列表
     *
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 比赛AI分析原始结果
     */
    @Override
    public List<Fx67llDortmundMatchAnalysis> selectFx67llDortmundMatchAnalysisList(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        return fx67llDortmundMatchAnalysisMapper.selectFx67llDortmundMatchAnalysisList(fx67llDortmundMatchAnalysis);
    }

    /**
     * 通过 UserId 查询比赛AI分析原始结果列表
     *
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 比赛AI分析原始结果
     */
    @Override
    public List<Fx67llDortmundMatchAnalysis> selectFx67llDortmundMatchAnalysisListByUserId(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        fx67llDortmundMatchAnalysis.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundMatchAnalysisMapper.selectFx67llDortmundMatchAnalysisList(fx67llDortmundMatchAnalysis);
    }

    /**
     * 新增比赛AI分析原始结果
     *
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundMatchAnalysis(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        fx67llDortmundMatchAnalysis.setUserId(SecurityUtils.getUserId());
        fx67llDortmundMatchAnalysis.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundMatchAnalysis.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundMatchAnalysisMapper.insertFx67llDortmundMatchAnalysis(fx67llDortmundMatchAnalysis);
    }

    /**
     * 修改比赛AI分析原始结果
     *
     * @param fx67llDortmundMatchAnalysis 比赛AI分析原始结果
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundMatchAnalysis(Fx67llDortmundMatchAnalysis fx67llDortmundMatchAnalysis) {
        fx67llDortmundMatchAnalysis.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundMatchAnalysis.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundMatchAnalysisMapper.updateFx67llDortmundMatchAnalysis(fx67llDortmundMatchAnalysis);
    }

    /**
     * 批量删除比赛AI分析原始结果
     *
     * @param analysisIds 需要删除的比赛AI分析原始结果主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundMatchAnalysisByAnalysisIds(Long[] analysisIds) {
        return fx67llDortmundMatchAnalysisMapper.deleteFx67llDortmundMatchAnalysisByAnalysisIds(analysisIds);
    }

    /**
     * 删除比赛AI分析原始结果信息
     *
     * @param analysisId 比赛AI分析原始结果主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundMatchAnalysisByAnalysisId(Long analysisId) {
        return fx67llDortmundMatchAnalysisMapper.deleteFx67llDortmundMatchAnalysisByAnalysisId(analysisId);
    }
}
