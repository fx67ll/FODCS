package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanMatchRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundOddsMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMatchRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import com.ruoyi.fx67ll.dortmund.support.DortmundDataScope;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;
import com.ruoyi.fx67ll.dortmund.support.DortmundWeightCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 比赛记录Service业务层处理
 * <p>
 * 比赛是赛季、主客队、信息点和倍率的聚合根。新增可在同一事务内创建赛季/球队/首条有效倍率/信息点；
 * 修改禁止改变比赛编码或嵌套新建，赛果变化会使关联方案摘要失效。
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
    private IFx67llDortmundInfoPointService fx67llDortmundInfoPointService;

    @Autowired
    private Fx67llDortmundInfoPointMapper fx67llDortmundInfoPointMapper;

    @Autowired
    private Fx67llDortmundPlanMatchRelMapper fx67llDortmundPlanMatchRelMapper;

    @Autowired
    private Fx67llDortmundOddsMapper fx67llDortmundOddsMapper;

    @Autowired
    private Fx67llDortmundPlanMapper fx67llDortmundPlanMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationMapper fx67llDortmundReturnCombinationMapper;

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
     * 按当前用户数据范围查询比赛详情。
     * <p>对象不存在和无权访问统一返回受控提示，避免通过详情接口推断其他用户的数据。</p>
     *
     * @param matchId 比赛记录主键
     * @return 当前用户可访问的比赛
     */
    @Override
    public Fx67llDortmundBasicMatch selectFx67llDortmundBasicMatchDetailForCurrentUser(Long matchId) {
        return requireMatchAccessible(matchId);
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
     * 按当前用户数据范围查询比赛记录列表。
     * <p>普通用户强制按本人 user_id 过滤，全部数据权限角色保留其显式筛选条件。</p>
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 当前用户可访问的比赛集合
     */
    @Override
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchListForCurrentUser(
            Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        Long ownerUserId = DortmundDataScope.ownerFilterForCurrentUser();
        if (ownerUserId != null) {
            fx67llDortmundBasicMatch.setUserId(ownerUserId);
        }
        return fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(fx67llDortmundBasicMatch);
    }

    /**
     * 新增比赛记录
     * <p>
     * AI 分析次数只由服务端维护，客户端提交的值一律忽略；
     * 支持通过 newSeason、newHomeTeam、newAwayTeam 当场新建赛季和球队，与比赛同一事务写入，任一步失败全部回滚；
     * 支持通过 newOdds 同一事务创建首条有效倍率记录
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        if (fx67llDortmundBasicMatch.getNewOdds() != null
                && fx67llDortmundBasicMatch.getNewOdds().getNewMatch() != null) {
            throw new ServiceException("新倍率记录不能继续嵌套新增比赛！");
        }
        checkMatchScalarFields(fx67llDortmundBasicMatch);
        resolveNewSeasonAndTeams(fx67llDortmundBasicMatch);
        checkMatchFields(fx67llDortmundBasicMatch);
        fx67llDortmundBasicMatch.setAnalysisCount(null);
        fx67llDortmundBasicMatch.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicMatch.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setCreateTime(DateUtils.getNowDate());
        fx67llDortmundBasicMatch.setDelFlag("0");
        try {
            int rows = fx67llDortmundBasicMatchMapper.insertFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
            if (rows > 0) {
                if (fx67llDortmundBasicMatch.getNewOdds() != null) {
                    insertInitialOdds(fx67llDortmundBasicMatch.getNewOdds(), fx67llDortmundBasicMatch.getMatchId());
                }
                insertNewInfoPoints(fx67llDortmundBasicMatch);
            }
            return rows;
        } catch (DuplicateKeyException e) {
            throw new ServiceException("比赛编码已存在，或该赛季、主队、客队和开赛时间已存在对应比赛，请改选已有比赛！");
        }
    }

    /**
     * 修改比赛记录
     * <p>
     * AI 分析次数只由服务端维护，客户端提交的值一律忽略；
     * 比赛编码创建后不允许修改；
     * newSeason、newHomeTeam、newAwayTeam、newOdds 只允许出现在新增请求中，修改时携带即拒绝
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
                || fx67llDortmundBasicMatch.getNewAwayTeam() != null || fx67llDortmundBasicMatch.getNewOdds() != null
                || fx67llDortmundBasicMatch.getNewInfoPoints() != null) {
            throw new ServiceException("修改比赛不支持当场新建赛季、球队或倍率记录，请改选已有记录！");
        }
        Fx67llDortmundBasicMatch oldMatch = fx67llDortmundBasicMatchMapper
                .selectFx67llDortmundBasicMatchByMatchId(fx67llDortmundBasicMatch.getMatchId());
        if (oldMatch == null) {
            throw new ServiceException("比赛记录不存在或没有访问权限！");
        }
        DortmundDataScope.requireAccessible(oldMatch.getUserId(), "比赛记录不存在或没有访问权限！");
        String matchCode = fx67llDortmundBasicMatch.getMatchCode();
        if (matchCode == null || matchCode.trim().isEmpty()) {
            fx67llDortmundBasicMatch.setMatchCode(oldMatch.getMatchCode());
        } else if (!Objects.equals(oldMatch.getMatchCode(), matchCode.trim())) {
            throw new ServiceException("比赛编码创建后不允许修改！");
        } else {
            fx67llDortmundBasicMatch.setMatchCode(matchCode.trim());
        }
        checkMatchFields(fx67llDortmundBasicMatch);
        // 常规赛果和倍率赛果都属于赛果事实；任一变化都需要失效摘要与历史核验结论。
        boolean matchResultChanged = !Objects.equals(oldMatch.getMatchResult(), fx67llDortmundBasicMatch.getMatchResult())
                || !Objects.equals(oldMatch.getOddsResult(), fx67llDortmundBasicMatch.getOddsResult());
        fx67llDortmundBasicMatch.setAnalysisCount(null);
        // 归属与创建审计只允许由服务端维护，防止客户端通过修改请求转移数据或篡改创建信息。
        fx67llDortmundBasicMatch.setUserId(oldMatch.getUserId());
        fx67llDortmundBasicMatch.setCreateBy(oldMatch.getCreateBy());
        fx67llDortmundBasicMatch.setCreateTime(oldMatch.getCreateTime());
        fx67llDortmundBasicMatch.setDelFlag(oldMatch.getDelFlag());
        fx67llDortmundBasicMatch.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicMatch.setUpdateTime(DateUtils.getNowDate());
        try {
            int rows = fx67llDortmundBasicMatchMapper.updateFx67llDortmundBasicMatch(fx67llDortmundBasicMatch);
            if (rows > 0 && matchResultChanged) {
                fx67llDortmundPlanMapper.markSummaryUpdateByMatchId(fx67llDortmundBasicMatch.getMatchId());
                // 赛果被补录或修正后，引用该比赛的历史核验结论不再可信，统一回到未核验等待人工复核。
                fx67llDortmundReturnCombinationMapper.resetVerificationByMatchId(
                        fx67llDortmundBasicMatch.getMatchId(), SecurityUtils.getUsername());
            }
            return rows;
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
        // 先校验整批归属，再检查引用，确保任一无权记录都不会触发部分删除。
        for (Long matchId : matchIds) {
            requireMatchAccessible(matchId);
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
        requireMatchAccessible(matchId);
        checkMatchReferenceBeforeDelete(matchId);
        return fx67llDortmundBasicMatchMapper.deleteFx67llDortmundBasicMatchByMatchId(matchId);
    }

    /**
     * 读取并校验比赛归属。
     *
     * @param matchId 比赛记录主键
     * @return 当前用户可维护的比赛
     */
    private Fx67llDortmundBasicMatch requireMatchAccessible(Long matchId) {
        Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchMapper
                .selectFx67llDortmundBasicMatchByMatchId(matchId);
        if (match == null) {
            throw new ServiceException("比赛记录不存在或没有访问权限！");
        }
        DortmundDataScope.requireAccessible(match.getUserId(), "比赛记录不存在或没有访问权限！");
        return match;
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
        checkMatchScalarFields(fx67llDortmundBasicMatch);
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
        Fx67llDortmundBasicSeason season = fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonBySeasonId(
                fx67llDortmundBasicMatch.getSeasonId());
        if (season == null) {
            throw new ServiceException("所选赛季不存在，请重新选择！");
        }
        DortmundDataScope.requireAccessible(season.getUserId(), "所选赛季不存在或没有访问权限，请重新选择！");
        Fx67llDortmundBasicTeam homeTeam = fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(
                fx67llDortmundBasicMatch.getHomeTeamId());
        if (homeTeam == null) {
            throw new ServiceException("所选主队不存在，请重新选择！");
        }
        DortmundDataScope.requireAccessible(homeTeam.getUserId(), "所选主队不存在或没有访问权限，请重新选择！");
        Fx67llDortmundBasicTeam awayTeam = fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(
                fx67llDortmundBasicMatch.getAwayTeamId());
        if (awayTeam == null) {
            throw new ServiceException("所选客队不存在，请重新选择！");
        }
        DortmundDataScope.requireAccessible(awayTeam.getUserId(), "所选客队不存在或没有访问权限，请重新选择！");
    }

    /**
     * 校验比赛自身的文本、枚举和时间字段，不访问关联表
     * <p>
     * 当场新建赛季或球队前先执行本方法，避免父级字段不合法时仍先写入关联数据。
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     */
    private void checkMatchScalarFields(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        fx67llDortmundBasicMatch.setMatchCode(DortmundFieldValidator.requireText(
                fx67llDortmundBasicMatch.getMatchCode(), "比赛编码", DortmundModuleConfig.Field.MATCH_CODE));
        fx67llDortmundBasicMatch.setMatchVenue(DortmundFieldValidator.optionalText(
                fx67llDortmundBasicMatch.getMatchVenue(), "比赛场地", DortmundModuleConfig.Field.MATCH_VENUE));
        fx67llDortmundBasicMatch.setMatchStatus(DortmundFieldValidator.validateEnum(
                fx67llDortmundBasicMatch.getMatchStatus(), "比赛状态", true, "0", "1"));
        // 比赛结果只保留常规胜平负；让球等倍率命中结果写入独立的 odds_result 字段。
        fx67llDortmundBasicMatch.setMatchResult(DortmundFieldValidator.validateEnum(
                fx67llDortmundBasicMatch.getMatchResult(), "比赛结果", false,
                "0", "1", "2"));
        fx67llDortmundBasicMatch.setOddsResult(normalizeOddsResult(fx67llDortmundBasicMatch.getOddsResult()));
        fx67llDortmundBasicMatch.setMatchRemark(DortmundFieldValidator.optionalText(
                fx67llDortmundBasicMatch.getMatchRemark(), "比赛备注", DortmundModuleConfig.Field.MATCH_REMARK));
        if (fx67llDortmundBasicMatch.getMatchTime() == null) {
            throw new ServiceException("比赛开球时间不能为空！");
        }
    }

    /**
     * 标准化倍率赛果。
     * <p>前端多选后以逗号串提交；服务端兼容中文逗号、大小写和重复值，并按固定类型集合校验，
     * 避免非法编码进入核验链路。没有有效令牌时按未登记处理。</p>
     *
     * @param value 原始倍率赛果文本
     * @return 标准英文逗号串或 null
     */
    private String normalizeOddsResult(String value) {
        String normalized = DortmundFieldValidator.optionalText(
                value, "倍率赛果", DortmundModuleConfig.Field.MATCH_ODDS_RESULT);
        if (normalized == null) {
            return null;
        }
        Set<String> tokens = new LinkedHashSet<>();
        for (String rawToken : normalized.split(DortmundModuleConfig.Match.ODDS_RESULT_SPLIT_REGEX)) {
            String token = rawToken.trim().toUpperCase(Locale.ROOT);
            if (token.isEmpty()) {
                continue;
            }
            if (!DortmundModuleConfig.Match.ODDS_RESULT_TYPES.contains(token)) {
                throw new ServiceException("倍率赛果取值不正确，允许值为：HW/DR/AW/HH/HD/HA！");
            }
            tokens.add(token);
        }
        return tokens.isEmpty() ? null
                : String.join(DortmundModuleConfig.Match.ODDS_RESULT_SEPARATOR, tokens);
    }

    /**
     * 创建比赛时保存首条有效倍率记录
     *
     * @param fx67llDortmundOdds 倍率记录
     * @param matchId 比赛记录主键
     */
    private void insertInitialOdds(Fx67llDortmundOdds fx67llDortmundOdds, Long matchId) {
        if (fx67llDortmundOdds.getNewMatch() != null) {
            throw new ServiceException("新倍率记录不能继续嵌套新增比赛！");
        }
        checkInitialOddsFields(fx67llDortmundOdds);
        fx67llDortmundOdds.setMatchId(matchId);
        fx67llDortmundOdds.setOddsCode(fx67llDortmundOdds.getOddsCode().trim());
        fx67llDortmundOdds.setActiveFlag("Y");
        fx67llDortmundOdds.setUserId(SecurityUtils.getUserId());
        fx67llDortmundOdds.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundOdds.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundOdds.setCreateTime(DateUtils.getNowDate());
        fx67llDortmundOdds.setUpdateTime(DateUtils.getNowDate());
        fx67llDortmundOdds.setDelFlag("0");
        try {
            fx67llDortmundOddsMapper.insertFx67llDortmundOdds(fx67llDortmundOdds);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("倍率记录编码已存在！");
        }
    }

    /**
     * 创建比赛时同步写入关联的比赛临时信息点
     *
     * @param fx67llDortmundBasicMatch 比赛记录对象
     */
    private void insertNewInfoPoints(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch) {
        List<Fx67llDortmundInfoPoint> newInfoPoints = fx67llDortmundBasicMatch.getNewInfoPoints();
        if (newInfoPoints == null || newInfoPoints.isEmpty()) {
            return;
        }
        for (Fx67llDortmundInfoPoint infoPoint : newInfoPoints) {
            infoPoint.setInfoPointType("M");
            infoPoint.setMatchId(fx67llDortmundBasicMatch.getMatchId());
            infoPoint.setTeamIds(new Long[0]);
            fx67llDortmundInfoPointService.insertFx67llDortmundInfoPoint(infoPoint);
        }
    }

    private void checkInitialOddsFields(Fx67llDortmundOdds fx67llDortmundOdds) {
        fx67llDortmundOdds.setOddsCode(DortmundFieldValidator.requireText(
                fx67llDortmundOdds.getOddsCode(), "倍率记录编码", DortmundModuleConfig.Field.ODDS_CODE));
        if (fx67llDortmundOdds.getSourceTime() == null) {
            throw new ServiceException("倍率来源时间不能为空！");
        }
        DortmundFieldValidator.validateDecimal(fx67llDortmundOdds.getHandicapValue(), "让分值",
                true, true, null, null, DortmundModuleConfig.Numeric.MAX_HANDICAP_VALUE,
                DortmundModuleConfig.Numeric.HANDICAP_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.HANDICAP_FRACTION_DIGITS);
        checkOddsValue(fx67llDortmundOdds.getHomeWinOdds(), "主胜倍率");
        checkOddsValue(fx67llDortmundOdds.getDrawOdds(), "平局倍率");
        checkOddsValue(fx67llDortmundOdds.getAwayWinOdds(), "客胜倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapHomeWinOdds(), "让分主胜倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapDrawOdds(), "让分平局倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapAwayWinOdds(), "让分客胜倍率");
        checkWeight(fx67llDortmundOdds.getHomeWinWeight(), "主胜权重");
        checkWeight(fx67llDortmundOdds.getDrawWeight(), "平局权重");
        checkWeight(fx67llDortmundOdds.getAwayWinWeight(), "客胜权重");
        checkWeight(fx67llDortmundOdds.getHandicapHomeWinWeight(), "让分主胜权重");
        checkWeight(fx67llDortmundOdds.getHandicapDrawWeight(), "让分平局权重");
        checkWeight(fx67llDortmundOdds.getHandicapAwayWinWeight(), "让分客胜权重");
        fx67llDortmundOdds.setOddsRemark(DortmundFieldValidator.optionalText(
                fx67llDortmundOdds.getOddsRemark(), "倍率记录说明",
                DortmundModuleConfig.Field.ODDS_REMARK));
    }

    private void checkOddsValue(BigDecimal value, String label) {
        if (value == null || value.signum() <= 0) {
            throw new ServiceException(label + "必须大于0！");
        }
        DortmundFieldValidator.validateDecimal(value, label, true, false, null, BigDecimal.ZERO,
                DortmundModuleConfig.Numeric.MAX_ODDS_VALUE,
                DortmundModuleConfig.Numeric.ODDS_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.ODDS_FRACTION_DIGITS);
    }

    private void checkWeight(BigDecimal value, String label) {
        if (value != null) {
            DortmundWeightCodec.toUnits(value, label);
        }
    }

    /**
     * 删除前校验比赛是否被倍率、信息点或方案计划引用，被引用时返回统一“已被引用”错误
     *
     * @param matchId 比赛记录主键
     */
    private void checkMatchReferenceBeforeDelete(Long matchId) {
        if (fx67llDortmundOddsMapper.countOddsByMatchId(matchId) > 0) {
            throw new ServiceException("比赛已被倍率记录引用，不能删除");
        }
        Fx67llDortmundInfoPoint infoPointQuery = new Fx67llDortmundInfoPoint();
        infoPointQuery.setMatchId(matchId);
        List<Fx67llDortmundInfoPoint> infoPointList = fx67llDortmundInfoPointMapper
                .selectFx67llDortmundInfoPointList(infoPointQuery);
        if (infoPointList != null && !infoPointList.isEmpty()) {
            throw new ServiceException("比赛已被信息点引用，不能删除");
        }
        Fx67llDortmundPlanMatchRel relQuery = new Fx67llDortmundPlanMatchRel();
        relQuery.setMatchId(matchId);
        List<Fx67llDortmundPlanMatchRel> relList = fx67llDortmundPlanMatchRelMapper
                .selectFx67llDortmundPlanMatchRelList(relQuery);
        if (relList != null && !relList.isEmpty()) {
            throw new ServiceException("比赛已被方案计划引用，不能删除");
        }
    }
}
