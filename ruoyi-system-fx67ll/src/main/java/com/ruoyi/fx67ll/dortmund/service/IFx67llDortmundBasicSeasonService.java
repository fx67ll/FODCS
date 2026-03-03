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
     * 查询赛季管理列表
     * 
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理集合
     */
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonList(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

    /**
     * 通过 UserId 查询赛季管理列表
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理集合
     */
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonListByUserId(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason);

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
