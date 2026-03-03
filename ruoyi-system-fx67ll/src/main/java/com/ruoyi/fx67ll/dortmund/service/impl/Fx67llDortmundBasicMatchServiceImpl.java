package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;

/**
 * 比赛记录Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundBasicMatchServiceImpl implements IFx67llDortmundBasicMatchService {
    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    /**
     * 查询比赛记录
     *
     * @param matchId 比赛记录主键
     * @return 比赛记录
     */
    @Override
    public Fx67llDortmundBasicMatch selectFx67llDortmundBasicMatchByMatchId(Long matchId) {
        return fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchByMatchId(matchId);
    }

    /**
     * 查询比赛记录列表
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 比赛记录
     */
    @Override
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchList(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        return fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(fx67llDortmundBasicMatch);
    }

    /**
     * 通过 UserId 查询比赛记录列表
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 比赛记录
     */
    @Override
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchListByUserId(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        fx67llDortmundBasicMatch.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(fx67llDortmundBasicMatch);
    }

    /**
     * 新增比赛记录
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        fx67llDortmundBasicMatch.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicMatch.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicMatchMapper.insertFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
    }

    /**
     * 修改比赛记录
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        fx67llDortmundBasicMatch.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicMatchMapper.updateFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
    }

    /**
     * 批量删除比赛记录
     *
     * @param matchIds 需要删除的比赛记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicMatchByMatchIds(Long[] matchIds) {
        return fx67llDortmundBasicMatchMapper.deleteFx67llDortmundBasicMatchByMatchIds(matchIds);
    }

    /**
     * 删除比赛记录信息
     *
     * @param matchId 比赛记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicMatchByMatchId(Long matchId) {
        return fx67llDortmundBasicMatchMapper.deleteFx67llDortmundBasicMatchByMatchId(matchId);
    }
}
