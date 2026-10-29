package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointTeamRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointTeamRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicTeamMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;

/**
 * 球队管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundBasicTeamServiceImpl implements IFx67llDortmundBasicTeamService {
    @Autowired
    private Fx67llDortmundBasicTeamMapper fx67llDortmundBasicTeamMapper;

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    @Autowired
    private Fx67llDortmundInfoPointTeamRelMapper fx67llDortmundInfoPointTeamRelMapper;

    /**
     * 查询球队管理
     *
     * @param teamId 球队管理主键
     * @return 球队管理
     */
    @Override
    public Fx67llDortmundBasicTeam selectFx67llDortmundBasicTeamByTeamId(Long teamId) {
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamByTeamId(teamId);
    }

    /**
     * 查询球队管理列表
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理
     */
    @Override
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamList(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
    }

    /**
     * 通过 UserId 查询球队管理列表
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理
     */
    @Override
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamListByUserId(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        fx67llDortmundBasicTeam.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
    }

    /**
     * 新增球队管理
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        fx67llDortmundBasicTeam.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicTeam.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicTeam.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicTeamMapper.insertFx67llDortmundBasicTeam(fx67llDortmundBasicTeam);
    }

    /**
     * 修改球队管理
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        if (fx67llDortmundBasicTeam.getTeamId() == null) {
            throw new ServiceException("球队主键不能为空！");
        }
        fx67llDortmundBasicTeam.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicTeam.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicTeamMapper.updateFx67llDortmundBasicTeam(fx67llDortmundBasicTeam);
    }

    /**
     * 批量删除球队管理
     *
     * @param teamIds 需要删除的球队管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicTeamByTeamIds(Long[] teamIds) {
        if (teamIds == null || teamIds.length == 0) {
            return 0;
        }
        for (Long teamId : teamIds) {
            checkTeamReferenceBeforeDelete(teamId);
        }
        return fx67llDortmundBasicTeamMapper.deleteFx67llDortmundBasicTeamByTeamIds(teamIds);
    }

    /**
     * 删除球队管理信息
     *
     * @param teamId 球队管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicTeamByTeamId(Long teamId) {
        checkTeamReferenceBeforeDelete(teamId);
        return fx67llDortmundBasicTeamMapper.deleteFx67llDortmundBasicTeamByTeamId(teamId);
    }

    /**
     * 删除前校验球队是否被比赛或信息点关系引用，被引用时返回统一"已被引用"错误
     *
     * @param teamId 球队管理主键
     */
    private void checkTeamReferenceBeforeDelete(Long teamId) {
        Fx67llDortmundBasicMatch matchQuery = new Fx67llDortmundBasicMatch();
        matchQuery.setHomeTeamId(teamId);
        List<Fx67llDortmundBasicMatch> homeMatchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (homeMatchList != null && !homeMatchList.isEmpty()) {
            throw new ServiceException("球队已被比赛引用，不能删除；可改用停用");
        }
        matchQuery.setHomeTeamId(null);
        matchQuery.setAwayTeamId(teamId);
        List<Fx67llDortmundBasicMatch> awayMatchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (awayMatchList != null && !awayMatchList.isEmpty()) {
            throw new ServiceException("球队已被比赛引用，不能删除；可改用停用");
        }
        Fx67llDortmundInfoPointTeamRel relQuery = new Fx67llDortmundInfoPointTeamRel();
        relQuery.setTeamId(teamId);
        List<Fx67llDortmundInfoPointTeamRel> relList = fx67llDortmundInfoPointTeamRelMapper.selectFx67llDortmundInfoPointTeamRelList(relQuery);
        if (relList != null && !relList.isEmpty()) {
            throw new ServiceException("球队已被信息点引用，不能删除；可改用停用");
        }
    }
}
