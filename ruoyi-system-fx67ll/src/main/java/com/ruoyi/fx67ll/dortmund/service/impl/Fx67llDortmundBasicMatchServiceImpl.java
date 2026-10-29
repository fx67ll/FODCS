package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanMatchRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMatchRelMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private IFx67llDortmundBasicSeasonService fx67llDortmundBasicSeasonService;

    @Autowired
    private IFx67llDortmundBasicTeamService fx67llDortmundBasicTeamService;

    @Autowired
    private Fx67llDortmundInfoPointMapper fx67llDortmundInfoPointMapper;

    @Autowired
    private Fx67llDortmundPlanMatchRelMapper fx67llDortmundPlanMatchRelMapper;

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
     * <p>
     * AI 分析次数只由服务端维护，客户端提交的值一律忽略；
     * 支持通过 newSeason、newHomeTeam、newAwayTeam 当场新建赛季和球队，与比赛同一事务写入，任一步失败全部回滚
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        resolveNewSeasonAndTeams(fx67llDortmundBasicMatch);
        checkMatchFields(fx67llDortmundBasicMatch);
        fx67llDortmundBasicMatch.setAnalysisCount(null);
        fx67llDortmundBasicMatch.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicMatch.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setCreateTime(DateUtils.getNowDate());
        try {
            return fx67llDortmundBasicMatchMapper.insertFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("该赛季、主队、客队和开赛时间已存在对应比赛，请改选已有比赛！");
        }
    }

    /**
     * 修改比赛记录
     * <p>
     * AI 分析次数只由服务端维护，客户端提交的值一律忽略；
     * newSeason、newHomeTeam、newAwayTeam 只允许出现在新增请求中，修改时携带即拒绝
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        if (fx67llDortmundBasicMatch.getMatchId() == null) {
            throw new ServiceException("比赛主键不能为空！");
        }
        if (fx67llDortmundBasicMatch.getNewSeason() != null || fx67llDortmundBasicMatch.getNewHomeTeam() != null
                || fx67llDortmundBasicMatch.getNewAwayTeam() != null) {
            throw new ServiceException("修改比赛不支持当场新建赛季或球队，请改选已有记录！");
        }
        checkMatchFields(fx67llDortmundBasicMatch);
        fx67llDortmundBasicMatch.setAnalysisCount(null);
        fx67llDortmundBasicMatch.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setUpdateTime(DateUtils.getNowDate());
        try {
            return fx67llDortmundBasicMatchMapper.updateFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("该赛季、主队、客队和开赛时间已存在对应比赛，请改选已有比赛！");
        }
    }

    /**
     * 批量删除比赛记录
     *
     * @param matchIds 需要删除的比赛记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicMatchByMatchIds(Long[] matchIds) {
        if (matchIds == null || matchIds.length == 0) {
            return 0;
        }
        for (Long matchId : matchIds) {
            checkMatchReferenceBeforeDelete(matchId);
        }
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
        checkMatchReferenceBeforeDelete(matchId);
        return fx67llDortmundBasicMatchMapper.deleteFx67llDortmundBasicMatchByMatchId(matchId);
    }

    /**
     * 处理当场新建的赛季和球队，将其主键回填到比赛记录
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     */
    private void resolveNewSeasonAndTeams(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        Fx67llDortmundBasicSeason newSeason = fx67llDortmundBasicMatch.getNewSeason();
        if (newSeason != null) {
            if (fx67llDortmundBasicMatch.getSeasonId() != null) {
                throw new ServiceException("不能同时选择已有赛季和当场新建赛季！");
            }
            fx67llDortmundBasicSeasonService.insertFx67llDortmundBasicSeason(newSeason);
            fx67llDortmundBasicMatch.setSeasonId(newSeason.getSeasonId());
        }
        Fx67llDortmundBasicTeam newHomeTeam = fx67llDortmundBasicMatch.getNewHomeTeam();
        if (newHomeTeam != null) {
            if (fx67llDortmundBasicMatch.getHomeTeamId() != null) {
                throw new ServiceException("不能同时选择已有主队和当场新建主队！");
            }
            fx67llDortmundBasicTeamService.insertFx67llDortmundBasicTeam(newHomeTeam);
            fx67llDortmundBasicMatch.setHomeTeamId(newHomeTeam.getTeamId());
        }
        Fx67llDortmundBasicTeam newAwayTeam = fx67llDortmundBasicMatch.getNewAwayTeam();
        if (newAwayTeam != null) {
            if (fx67llDortmundBasicMatch.getAwayTeamId() != null) {
                throw new ServiceException("不能同时选择已有客队和当场新建客队！");
            }
            fx67llDortmundBasicTeamService.insertFx67llDortmundBasicTeam(newAwayTeam);
            fx67llDortmundBasicMatch.setAwayTeamId(newAwayTeam.getTeamId());
        }
    }

    /**
     * 校验比赛归属与主客队字段
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     */
    private void checkMatchFields(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        if (fx67llDortmundBasicMatch.getSeasonId() == null) {
            throw new ServiceException("比赛所属赛季不能为空！");
        }
        if (fx67llDortmundBasicMatch.getHomeTeamId() == null || fx67llDortmundBasicMatch.getAwayTeamId() == null) {
            throw new ServiceException("比赛主队和客队不能为空！");
        }
        if (fx67llDortmundBasicMatch.getHomeTeamId().equals(fx67llDortmundBasicMatch.getAwayTeamId())) {
            throw new ServiceException("比赛主队和客队不能相同！");
        }
        if (fx67llDortmundBasicMatch.getMatchTime() == null) {
            throw new ServiceException("比赛开球时间不能为空！");
        }
        if (fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonBySeasonId(fx67llDortmundBasicMatch.getSeasonId()) == null) {
            throw new ServiceException("所选赛季不存在，请重新选择！");
        }
        if (fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(fx67llDortmundBasicMatch.getHomeTeamId()) == null) {
            throw new ServiceException("所选主队不存在，请重新选择！");
        }
        if (fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(fx67llDortmundBasicMatch.getAwayTeamId()) == null) {
            throw new ServiceException("所选客队不存在，请重新选择！");
        }
    }

    /**
     * 删除前校验比赛是否被信息点或方案计划引用，被引用时返回统一"已被引用"错误
     *
     * @param matchId 比赛记录主键
     */
    private void checkMatchReferenceBeforeDelete(Long matchId) {
        Fx67llDortmundInfoPoint infoPointQuery = new Fx67llDortmundInfoPoint();
        infoPointQuery.setMatchId(matchId);
        List<Fx67llDortmundInfoPoint> infoPointList = fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointList(infoPointQuery);
        if (infoPointList != null && !infoPointList.isEmpty()) {
            throw new ServiceException("比赛已被信息点引用，不能删除");
        }
        Fx67llDortmundPlanMatchRel relQuery = new Fx67llDortmundPlanMatchRel();
        relQuery.setMatchId(matchId);
        List<Fx67llDortmundPlanMatchRel> relList = fx67llDortmundPlanMatchRelMapper.selectFx67llDortmundPlanMatchRelList(relQuery);
        if (relList != null && !relList.isEmpty()) {
            throw new ServiceException("比赛已被方案计划引用，不能删除");
        }
    }
}
