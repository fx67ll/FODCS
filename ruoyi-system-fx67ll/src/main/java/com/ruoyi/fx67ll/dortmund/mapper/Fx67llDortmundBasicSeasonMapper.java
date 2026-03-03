package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;

/**
 * 赛季管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llDortmundBasicSeasonMapper 
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
     * 删除赛季管理
     * 
     * @param seasonId 赛季管理主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicSeasonBySeasonId(Long seasonId);

    /**
     * 批量删除赛季管理
     * 
     * @param seasonIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicSeasonBySeasonIds(Long[] seasonIds);
}
