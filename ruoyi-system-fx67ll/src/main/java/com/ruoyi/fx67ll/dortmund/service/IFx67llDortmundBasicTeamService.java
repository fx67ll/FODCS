package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;

/**
 * 球队管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundBasicTeamService 
{
    /**
     * 查询球队管理
     * 
     * @param teamId 球队管理主键
     * @return 球队管理
     */
    public Fx67llDortmundBasicTeam selectFx67llDortmundBasicTeamByTeamId(Long teamId);

    /**
     * 查询球队管理列表
     * 
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理集合
     */
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamList(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

    /**
     * 通过 UserId 查询球队管理列表
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理集合
     */
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamListByUserId(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

    /**
     * 新增球队管理
     * 
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    public int insertFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

    /**
     * 修改球队管理
     * 
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    public int updateFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

    /**
     * 批量删除球队管理
     * 
     * @param teamIds 需要删除的球队管理主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicTeamByTeamIds(Long[] teamIds);

    /**
     * 删除球队管理信息
     * 
     * @param teamId 球队管理主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicTeamByTeamId(Long teamId);
}
