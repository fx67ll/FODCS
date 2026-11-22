package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;

/**
 * 赛季管理Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundBasicSeasonService 
{
    /**
     * 查询赛季管理
     * 
     * @param seasonId 赛季管理主键
     * @return 赛季管理
     */
    public Fx67llDortmundBasicSeason selectFx67llDortmundBasicSeasonBySeasonId(Long seasonId);

    /**
     * 按当前用户数据范围查询赛季详情。
     * <p>普通用户仅能读取本人赛季；全部数据权限角色可读取全量赛季。</p>
     *
     * @param seasonId 赛季管理主键
     * @return 当前用户可访问的赛季详情
     */
    public Fx67llDortmundBasicSeason selectFx67llDortmundBasicSeasonDetailForCurrentUser(Long seasonId);

    /**
     * 查询赛季管理列表
     * 
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理集合
     */
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonList(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

    /**
     * 按当前用户数据范围查询赛季管理列表。
     * <p>普通用户强制按本人 user_id 过滤；全部数据权限角色不强行收窄归属范围。</p>
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 当前用户可访问的赛季管理集合
     */
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonListForCurrentUser(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

    /**
     * 新增赛季管理
     * 
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    public int insertFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

    /**
     * 修改赛季管理
     * 
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    public int updateFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

    /**
     * 批量删除赛季管理
     * 
     * @param seasonIds 需要删除的赛季管理主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicSeasonBySeasonIds(Long[] seasonIds);

    /**
     * 删除赛季管理信息
     * 
     * @param seasonId 赛季管理主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicSeasonBySeasonId(Long seasonId);
}
