package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundMatchScoreMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchScore;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundMatchScoreService;

/**
 * 比赛标准化评分Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundMatchScoreServiceImpl implements IFx67llDortmundMatchScoreService {
    @Autowired
    private Fx67llDortmundMatchScoreMapper fx67llDortmundMatchScoreMapper;

    /**
     * 查询比赛标准化评分
     *
     * @param scoreId 比赛标准化评分主键
     * @return 比赛标准化评分
     */
    @Override
    public Fx67llDortmundMatchScore selectFx67llDortmundMatchScoreByScoreId(Long scoreId) {
        return fx67llDortmundMatchScoreMapper.selectFx67llDortmundMatchScoreByScoreId(scoreId);
    }

    /**
     * 查询比赛标准化评分列表
     *
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 比赛标准化评分
     */
    @Override
    public List<Fx67llDortmundMatchScore> selectFx67llDortmundMatchScoreList(Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        return fx67llDortmundMatchScoreMapper.selectFx67llDortmundMatchScoreList(fx67llDortmundMatchScore);
    }

    /**
     * 通过 UserId 查询比赛标准化评分列表
     *
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 比赛标准化评分
     */
    @Override
    public List<Fx67llDortmundMatchScore> selectFx67llDortmundMatchScoreListByUserId(Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        fx67llDortmundMatchScore.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundMatchScoreMapper.selectFx67llDortmundMatchScoreList(fx67llDortmundMatchScore);
    }

    /**
     * 新增比赛标准化评分
     *
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundMatchScore(Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        fx67llDortmundMatchScore.setUserId(SecurityUtils.getUserId());
        fx67llDortmundMatchScore.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundMatchScore.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundMatchScoreMapper.insertFx67llDortmundMatchScore(fx67llDortmundMatchScore);
    }

    /**
     * 修改比赛标准化评分
     *
     * @param fx67llDortmundMatchScore 比赛标准化评分
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundMatchScore(Fx67llDortmundMatchScore fx67llDortmundMatchScore) {
        fx67llDortmundMatchScore.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundMatchScore.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundMatchScoreMapper.updateFx67llDortmundMatchScore(fx67llDortmundMatchScore);
    }

    /**
     * 批量删除比赛标准化评分
     *
     * @param scoreIds 需要删除的比赛标准化评分主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundMatchScoreByScoreIds(Long[] scoreIds) {
        return fx67llDortmundMatchScoreMapper.deleteFx67llDortmundMatchScoreByScoreIds(scoreIds);
    }

    /**
     * 删除比赛标准化评分信息
     *
     * @param scoreId 比赛标准化评分主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundMatchScoreByScoreId(Long scoreId) {
        return fx67llDortmundMatchScoreMapper.deleteFx67llDortmundMatchScoreByScoreId(scoreId);
    }
}
