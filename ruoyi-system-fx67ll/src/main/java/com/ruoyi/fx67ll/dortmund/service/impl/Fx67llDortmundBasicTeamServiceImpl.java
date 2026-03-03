package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
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
        return fx67llDortmundBasicTeamMapper.deleteFx67llDortmundBasicTeamByTeamId(teamId);
    }
}
