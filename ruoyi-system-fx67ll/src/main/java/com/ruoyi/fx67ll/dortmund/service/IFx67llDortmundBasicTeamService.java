package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundTeamOptions;

/**
 * 球队管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundBasicTeamService 
{
    /**
     * 获取球队主场与标签选项。
     * <p>球队表去重值会与内置目录合并，供前端编辑器和选择器使用。</p>
     *
     * @return 合并后的选项
     */
    public Fx67llDortmundTeamOptions selectFx67llDortmundTeamOptions();

    /**
     * 查询球队管理
     * 
     * @param teamId 球队管理主键
     * @return 球队管理
     */
    public Fx67llDortmundBasicTeam selectFx67llDortmundBasicTeamByTeamId(Long teamId);

    /**
     * 按当前用户数据范围查询球队详情。
     * <p>普通用户仅能读取本人球队；全部数据权限角色可读取全量球队。</p>
     *
     * @param teamId 球队管理主键
     * @return 当前用户可访问的球队详情
     */
    public Fx67llDortmundBasicTeam selectFx67llDortmundBasicTeamDetailForCurrentUser(Long teamId);

    /**
     * 查询球队管理列表
     * 
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理集合
     */
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamList(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

    /**
     * 按当前用户数据范围查询球队管理列表。
     * <p>普通用户强制按本人 user_id 过滤；全部数据权限角色不强行收窄归属范围。</p>
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 当前用户可访问的球队管理集合
     */
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamListForCurrentUser(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam);

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
