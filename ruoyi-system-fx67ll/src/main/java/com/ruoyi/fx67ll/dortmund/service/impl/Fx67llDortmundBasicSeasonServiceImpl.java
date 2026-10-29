package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
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

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

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
        checkSeasonStartAndEndDate(fx67llDortmundBasicSeason);
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
        if (fx67llDortmundBasicSeason.getSeasonId() == null) {
            throw new ServiceException("赛季主键不能为空！");
        }
        checkSeasonStartAndEndDate(fx67llDortmundBasicSeason);
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
        if (seasonIds == null || seasonIds.length == 0) {
            return 0;
        }
        for (Long seasonId : seasonIds) {
            checkSeasonReferenceBeforeDelete(seasonId);
        }
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
        checkSeasonReferenceBeforeDelete(seasonId);
        return fx67llDortmundBasicSeasonMapper.deleteFx67llDortmundBasicSeasonBySeasonId(seasonId);
    }

    /**
     * 校验赛季开始日期不得晚于结束日期
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     */
    private void checkSeasonStartAndEndDate(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        if (fx67llDortmundBasicSeason.getSeasonStartDate() != null && fx67llDortmundBasicSeason.getSeasonEndDate() != null
                && fx67llDortmundBasicSeason.getSeasonStartDate().after(fx67llDortmundBasicSeason.getSeasonEndDate())) {
            throw new ServiceException("赛季开始日期不得晚于结束日期！");
        }
    }

    /**
     * 删除前校验赛季是否被比赛或信息点引用，被引用时返回统一"已被引用"错误
     *
     * @param seasonId 赛季管理主键
     */
    private void checkSeasonReferenceBeforeDelete(Long seasonId) {
        Fx67llDortmundBasicMatch matchQuery = new Fx67llDortmundBasicMatch();
        matchQuery.setSeasonId(seasonId);
        List<Fx67llDortmundBasicMatch> matchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (matchList != null && !matchList.isEmpty()) {
            throw new ServiceException("赛季已被比赛引用，不能删除；可改用结束赛季");
        }
    }
}
