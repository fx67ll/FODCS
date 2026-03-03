package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicSeasonMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;

/**
 * 赛季管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundBasicSeasonServiceImpl implements IFx67llDortmundBasicSeasonService {
    @Autowired
    private Fx67llDortmundBasicSeasonMapper fx67llDortmundBasicSeasonMapper;

    /**
     * 查询赛季管理
     *
     * @param seasonId 赛季管理主键
     * @return 赛季管理
     */
    @Override
    public Fx67llDortmundBasicSeason selectFx67llDortmundBasicSeasonBySeasonId(Long seasonId) {
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonBySeasonId(seasonId);
    }

    /**
     * 查询赛季管理列表
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理
     */
    @Override
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonList(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
    }

    /**
     * 通过 UserId 查询赛季管理列表
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理
     */
    @Override
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonListByUserId(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        fx67llDortmundBasicSeason.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
    }

    /**
     * 新增赛季管理
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        fx67llDortmundBasicSeason.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicSeason.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicSeason.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicSeasonMapper.insertFx67llDortmundBasicSeason(fx67llDortmundBasicSeason);
    }

    /**
     * 修改赛季管理
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        fx67llDortmundBasicSeason.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicSeason.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicSeasonMapper.updateFx67llDortmundBasicSeason(fx67llDortmundBasicSeason);
    }

    /**
     * 批量删除赛季管理
     *
     * @param seasonIds 需要删除的赛季管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicSeasonBySeasonIds(Long[] seasonIds) {
        return fx67llDortmundBasicSeasonMapper.deleteFx67llDortmundBasicSeasonBySeasonIds(seasonIds);
    }

    /**
     * 删除赛季管理信息
     *
     * @param seasonId 赛季管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicSeasonBySeasonId(Long seasonId) {
        return fx67llDortmundBasicSeasonMapper.deleteFx67llDortmundBasicSeasonBySeasonId(seasonId);
    }
}
