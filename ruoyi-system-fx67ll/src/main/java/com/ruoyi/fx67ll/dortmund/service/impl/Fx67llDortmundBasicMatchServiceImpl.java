package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatchExcel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundMatchMultiplierImport;
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
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundOddsService;
import com.ruoyi.fx67ll.dortmund.support.DortmundDataScope;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;
import com.ruoyi.fx67ll.dortmund.support.DortmundWeightCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    /** 比赛 JSON 单次导入上限，避免超大请求在单事务内长时间占用连接。 */
    private static final int MAX_IMPORT_ROWS = 1000;

    /** 自动生成比赛编码的随机源；仅用于补足日期后的六位随机段。 */
    private static final SecureRandom MATCH_CODE_RANDOM = new SecureRandom();

    /** 单场比赛一次导入的倍率记录上限，避免异常 JSON 放大事务和校验开销。 */
    private static final int MAX_MULTIPLIERS_PER_MATCH = 5;

    /** 自动生成倍率编码的随机源；与手工新增保持相同的可读编码结构。 */
    private static final SecureRandom MULTIPLIER_CODE_RANDOM = new SecureRandom();

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

    /** 倍率记录通过现有服务完成字段校验、当前版本切换和审计字段填充。 */
    @Autowired
    @Lazy
    private IFx67llDortmundOddsService fx67llDortmundOddsService;

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
     * 按当前用户数据范围查询比赛当前有效倍率版本。
     * <p>先校验比赛归属，再读取唯一有效倍率；没有有效版本时返回 null，
     * 多条有效版本仍视为数据异常并阻断，避免移动端展示不确定的冻结数据。</p>
     *
     * @param matchId 比赛记录主键
     * @return 当前有效倍率记录；未维护时返回 null
     */
    @Override
    public Fx67llDortmundOdds selectActiveOddsForCurrentUser(Long matchId) {
        requireMatchAccessible(matchId);
        int activeCount = fx67llDortmundOddsMapper.countActiveOddsByMatchId(matchId);
        if (activeCount > 1) {
            throw new ServiceException("该比赛存在多条有效倍率记录，请先调整至仅一条有效倍率！");
        }
        if (activeCount == 0) {
            return null;
        }
        Fx67llDortmundOdds odds = fx67llDortmundOddsMapper.selectActiveFx67llDortmundOddsByMatchId(matchId);
        // 比赛归属与倍率归属必须同时可访问，防止异常归档数据把他人倍率带到当前用户详情中。
        DortmundDataScope.requireAccessible(odds == null ? null : odds.getUserId(), "有效倍率记录不存在或没有访问权限！");
        return odds;
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
     * APP 端只更新常规赛果和倍率赛果。
     * <p>
     * 移动端不允许通过本接口修改比赛编排字段，避免表单不完整时覆盖赛季、主客队或开赛时间。
     * 两个赛果均按 Web 端相同规则校验和标准化；任一结果变化时，将关联方案标记为摘要待刷新，
     * 并把引用该比赛的回报组合核验结论重置为未核验，保证下游统计不会继续使用过期事实。
     * </p>
     *
     * @param matchId     比赛记录主键
     * @param matchResult 常规赛果，可为空表示清除
     * @param oddsResult  倍率赛果，可为空表示清除
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMatchResultsForApp(Long matchId, String matchResult, String oddsResult) {
        if (matchId == null) {
            throw new ServiceException("比赛主键不能为空！");
        }
        Fx67llDortmundBasicMatch oldMatch = requireMatchAccessible(matchId);
        String normalizedMatchResult = DortmundFieldValidator.validateEnum(
                matchResult, "比赛结果", false, "0", "1", "2");
        String normalizedOddsResult = normalizeOddsResult(oddsResult);
        boolean resultChanged = !Objects.equals(oldMatch.getMatchResult(), normalizedMatchResult)
                || !Objects.equals(oldMatch.getOddsResult(), normalizedOddsResult);

        int rows = fx67llDortmundBasicMatchMapper.updateMatchResultsForApp(
                matchId, normalizedMatchResult, normalizedOddsResult, SecurityUtils.getUsername());
        if (rows > 0 && resultChanged) {
            fx67llDortmundPlanMapper.markSummaryUpdateByMatchId(matchId);
            fx67llDortmundReturnCombinationMapper.resetVerificationByMatchId(
                    matchId, SecurityUtils.getUsername());
        }
        return rows;
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
     * 批量导入确认后的 JSON 比赛条目。
     * <p>先解析全部条目再写库，一次性返回所有序号错误，避免事务中部分成功造成用户误以为导入完整。
     * 赛季、球队及其他可见比赛均在当前登录用户的数据范围内解析。</p>
     *
     * @param rows 前端预览并勾选的业务交换行
     * @return 导入成功条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importFx67llDortmundBasicMatchJson(List<Fx67llDortmundBasicMatchExcel> rows) {
        return importFx67llDortmundBasicMatchRows(rows);
    }

    /**
     * 批量导入既有比赛的赛果和倍率记录。
     * <p>matchCode 是唯一定位依据；未出现、null 或空白的赛果字段保持原值，当前接口不提供清空能力。
     * 倍率记录复用倍率服务执行六类数值、权重和当前有效版本校验，任一失败整批回滚。</p>
     *
     * @param rows 用户确认写入的比赛结果条目
     * @return 导入成功条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importFx67llDortmundBasicMatchResultJson(List<Fx67llDortmundBasicMatchExcel> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new ServiceException("导入文件中没有可处理的数据！");
        }
        if (rows.size() > MAX_IMPORT_ROWS) {
            throw new ServiceException("单次最多导入" + MAX_IMPORT_ROWS + "条比赛结果！");
        }

        List<Fx67llDortmundBasicMatch> accessibleMatches = selectFx67llDortmundBasicMatchListForCurrentUser(
                new Fx67llDortmundBasicMatch());
        Map<String, Fx67llDortmundBasicMatch> matchByCode = new HashMap<>();
        for (Fx67llDortmundBasicMatch match : accessibleMatches) {
            String code = trim(match.getMatchCode());
            if (code != null) {
                matchByCode.put(code, match);
            }
        }

        List<String> errors = new ArrayList<>();
        List<PreparedResultImport> prepared = new ArrayList<>();
        Set<String> importCodes = new HashSet<>();
        for (int index = 0; index < rows.size(); index++) {
            int rowNumber = index + 1;
            Fx67llDortmundBasicMatchExcel row = rows.get(index);
            try {
                if (row == null) {
                    throw new ServiceException("数据行不能为空");
                }
                String matchCode = trim(row.getMatchCode());
                if (matchCode == null) {
                    throw new ServiceException("比赛编码不能为空");
                }
                if (!importCodes.add(matchCode)) {
                    throw new ServiceException("同一文件内比赛编码“" + matchCode + "”重复");
                }
                Fx67llDortmundBasicMatch match = matchByCode.get(matchCode);
                if (match == null) {
                    throw new ServiceException("未找到当前账号可维护的比赛编码“" + matchCode + "”");
                }

                boolean matchResultProvided = trim(row.getMatchResult()) != null;
                String rawMultiplierResult = trim(row.getOddsResult()) != null
                        ? row.getOddsResult() : row.getMultiplierResult();
                boolean multiplierResultProvided = trim(rawMultiplierResult) != null;
                String normalizedMatchResult = matchResultProvided
                        ? DortmundFieldValidator.validateEnum(row.getMatchResult(), "比赛结果", false,
                                "0", "1", "2")
                        : null;
                String normalizedMultiplierResult = multiplierResultProvided
                        ? normalizeOddsResult(rawMultiplierResult) : null;
                List<Fx67llDortmundMatchMultiplierImport> multipliers = rows.get(index).getMultipliers() == null
                        ? Collections.emptyList() : rows.get(index).getMultipliers();
                checkImportMultiplierCount(multipliers);

                boolean resultChanged = (matchResultProvided
                        && !Objects.equals(match.getMatchResult(), normalizedMatchResult))
                        || (multiplierResultProvided
                                && !Objects.equals(match.getOddsResult(), normalizedMultiplierResult));
                if (!resultChanged && multipliers.isEmpty()) {
                    throw new ServiceException("赛果没有变化，且未提供倍率记录");
                }
                prepared.add(new PreparedResultImport(match, rowNumber, matchResultProvided,
                        normalizedMatchResult, multiplierResultProvided, normalizedMultiplierResult,
                        multipliers, resultChanged));
            } catch (ServiceException e) {
                errors.add("第" + rowNumber + "条：" + e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            throw new ServiceException("比赛结果导入校验失败：" + String.join("；", errors));
        }

        int imported = 0;
        for (PreparedResultImport row : prepared) {
            int updated = 0;
            if (row.matchResultProvided || row.multiplierResultProvided) {
                updated = fx67llDortmundBasicMatchMapper.updateMatchResultsForImport(
                        row.match.getMatchId(), row.matchResult, row.multiplierResult,
                        SecurityUtils.getUsername());
            }
            if (updated > 0 && row.resultChanged) {
                fx67llDortmundPlanMapper.markSummaryUpdateByMatchId(row.match.getMatchId());
                fx67llDortmundReturnCombinationMapper.resetVerificationByMatchId(
                        row.match.getMatchId(), SecurityUtils.getUsername());
            }
            insertImportedMultipliers(row.match, row.multipliers, row.rowNumber);
            imported++;
        }
        return imported;
    }

    /**
     * 校验导入倍率记录数量，避免异常 JSON 在单事务内无限放大。
     */
    private void checkImportMultiplierCount(List<Fx67llDortmundMatchMultiplierImport> multipliers) {
        if (multipliers == null || multipliers.isEmpty()) {
            return;
        }
        if (multipliers.size() > MAX_MULTIPLIERS_PER_MATCH) {
            throw new ServiceException("单场比赛最多导入" + MAX_MULTIPLIERS_PER_MATCH + "条倍率记录");
        }
        for (Fx67llDortmundMatchMultiplierImport multiplier : multipliers) {
            if (multiplier == null) {
                throw new ServiceException("倍率记录不能为空");
            }
        }
    }

    /**
     * 将一组导入倍率记录转换为现有倍率服务可校验的业务对象并写入。
     */
    private void insertImportedMultipliers(Fx67llDortmundBasicMatch match,
            List<Fx67llDortmundMatchMultiplierImport> multipliers, int rowNumber) {
        if (multipliers == null || multipliers.isEmpty()) {
            return;
        }
        for (int index = 0; index < multipliers.size(); index++) {
            Fx67llDortmundMatchMultiplierImport source = multipliers.get(index);
            try {
                fx67llDortmundOddsService.insertFx67llDortmundOdds(
                        buildImportedOdds(source, match.getMatchId(), index));
            } catch (ServiceException e) {
                throw new ServiceException("第" + rowNumber + "条比赛的第" + (index + 1)
                        + "条倍率记录无效：" + e.getMessage());
            }
        }
    }

    /**
     * 构造导入倍率对象；编码、来源时间和有效状态缺失时使用安全默认值。
     */
    private Fx67llDortmundOdds buildImportedOdds(Fx67llDortmundMatchMultiplierImport source,
            Long matchId, int index) {
        Date now = DateUtils.getNowDate();
        Date sourceTime = source.getSourceTime() == null ? now : source.getSourceTime();
        String multiplierCode = trim(source.getMultiplierCode());
        if (multiplierCode == null) {
            multiplierCode = generateImportMultiplierCode(sourceTime);
        }

        Fx67llDortmundOdds odds = new Fx67llDortmundOdds();
        odds.setMatchId(matchId);
        odds.setOddsCode(multiplierCode);
        odds.setSourceTime(sourceTime);
        odds.setHandicapValue(source.getHandicapValue());
        odds.setHomeWinOdds(source.getHomeWinMultiplier());
        odds.setDrawOdds(source.getDrawMultiplier());
        odds.setAwayWinOdds(source.getAwayWinMultiplier());
        odds.setHandicapHomeWinOdds(source.getHandicapHomeWinMultiplier());
        odds.setHandicapDrawOdds(source.getHandicapDrawMultiplier());
        odds.setHandicapAwayWinOdds(source.getHandicapAwayWinMultiplier());
        odds.setHomeWinWeight(source.getHomeWinWeight());
        odds.setDrawWeight(source.getDrawWeight());
        odds.setAwayWinWeight(source.getAwayWinWeight());
        odds.setHandicapHomeWinWeight(source.getHandicapHomeWinWeight());
        odds.setHandicapDrawWeight(source.getHandicapDrawWeight());
        odds.setHandicapAwayWinWeight(source.getHandicapAwayWinWeight());
        odds.setActiveFlag(normalizeImportActiveFlag(source.getActiveFlag(), index));
        odds.setOddsRemark(source.getMultiplierRemark());
        return odds;
    }

    /**
     * 缺省时仅首条导入记录作为当前有效版本，其余保留为历史版本。
     */
    private String normalizeImportActiveFlag(String value, int index) {
        String normalized = trim(value);
        if (normalized == null) {
            return index == 0 ? "Y" : "N";
        }
        String upper = normalized.toUpperCase(Locale.ROOT);
        if ("Y".equals(upper) || "1".equals(upper) || "TRUE".equals(upper)) {
            return "Y";
        }
        if ("N".equals(upper) || "0".equals(upper) || "FALSE".equals(upper)) {
            return "N";
        }
        throw new ServiceException("倍率记录有效状态只能为 Y 或 N");
    }

    /**
     * 生成不超过 23 个字符的导入倍率编码。
     */
    private String generateImportMultiplierCode(Date sourceTime) {
        String datePart = DateUtils.parseDateToStr("yyyyMMdd", sourceTime);
        return "ODDS-" + datePart + "-" + String.format("%06d", MULTIPLIER_CODE_RANDOM.nextInt(1000000));
    }

    /**
     * 比赛导入公共实现。
     *
     * @param rows 待导入行
     * @return 导入成功条数
     */
    private int importFx67llDortmundBasicMatchRows(List<Fx67llDortmundBasicMatchExcel> rows) {
        if (rows == null || rows.isEmpty()) {
            throw new ServiceException("导入文件中没有可处理的数据！");
        }
        if (rows.size() > MAX_IMPORT_ROWS) {
            throw new ServiceException("单次最多导入" + MAX_IMPORT_ROWS + "条比赛记录！");
        }

        // 导入引用与列表、详情共用同一数据范围：普通用户不能借 JSON 导入绑定他人赛季或球队。
        List<Fx67llDortmundBasicSeason> seasons = fx67llDortmundBasicSeasonService
                .selectFx67llDortmundBasicSeasonListForCurrentUser(new Fx67llDortmundBasicSeason());
        List<Fx67llDortmundBasicTeam> teams = fx67llDortmundBasicTeamService
                .selectFx67llDortmundBasicTeamListForCurrentUser(new Fx67llDortmundBasicTeam());
        // 比赛编码和“赛季 + 主客队 + 开赛时间”在数据库均为全局唯一，
        // 因此重复校验必须读取全量比赛，不能只检查当前用户自己的数据。
        List<Fx67llDortmundBasicMatch> existingMatches = fx67llDortmundBasicMatchMapper
                .selectFx67llDortmundBasicMatchList(new Fx67llDortmundBasicMatch());

        Set<String> existingCodes = new HashSet<>();
        Set<String> existingBusinessKeys = new HashSet<>();
        for (Fx67llDortmundBasicMatch match : existingMatches) {
            if (match.getMatchCode() != null) {
                existingCodes.add(match.getMatchCode().trim());
            }
            existingBusinessKeys.add(buildMatchBusinessKey(match.getSeasonId(), match.getHomeTeamId(),
                    match.getAwayTeamId(), match.getMatchTime()));
        }

        List<String> errors = new ArrayList<>();
        List<PreparedMatchImport> prepared = new ArrayList<>();
        Set<String> importCodes = new HashSet<>();
        Set<String> importBusinessKeys = new HashSet<>();
        for (int index = 0; index < rows.size(); index++) {
            int rowNumber = index + 1;
            Fx67llDortmundBasicMatchExcel row = rows.get(index);
            try {
                Fx67llDortmundBasicMatch match = resolveMatchImportRow(row, seasons, teams,
                        existingCodes, importCodes);
                String businessKey = buildMatchBusinessKey(match.getSeasonId(), match.getHomeTeamId(),
                        match.getAwayTeamId(), match.getMatchTime());
                if (existingBusinessKeys.contains(businessKey)) {
                    throw new ServiceException("该赛季、主客队和开赛时间已存在比赛，请勿重复导入");
                }
                if (!importBusinessKeys.add(businessKey)) {
                    throw new ServiceException("同一文件内存在重复的赛季、主客队和开赛时间");
                }
                List<Fx67llDortmundMatchMultiplierImport> multipliers = row.getMultipliers() == null
                        ? Collections.emptyList() : row.getMultipliers();
                checkImportMultiplierCount(multipliers);
                prepared.add(new PreparedMatchImport(match, multipliers, rowNumber));
            } catch (ServiceException e) {
                errors.add("第" + rowNumber + "条：" + e.getMessage());
            }
        }
        if (!errors.isEmpty()) {
            throw new ServiceException("比赛导入校验失败：" + String.join("；", errors));
        }

        int imported = 0;
        for (PreparedMatchImport row : prepared) {
            int inserted = insertFx67llDortmundBasicMatch(row.match);
            imported += inserted;
            if (inserted > 0) {
                insertImportedMultipliers(row.match, row.multipliers, row.rowNumber);
            }
        }
        return imported;
    }

    /**
     * 将一条 JSON 比赛数据解析为可写入的比赛对象。
     *
     * @param row JSON 比赛条目
     * @param seasons 当前用户可见赛季
     * @param teams 当前用户可见球队
     * @param existingCodes 数据库已有比赛编码
     * @param importCodes 当前文件已占用比赛编码
     * @return 待写入比赛
     */
    private Fx67llDortmundBasicMatch resolveMatchImportRow(Fx67llDortmundBasicMatchExcel row,
            List<Fx67llDortmundBasicSeason> seasons, List<Fx67llDortmundBasicTeam> teams,
            Set<String> existingCodes, Set<String> importCodes) {
        if (row == null) {
            throw new ServiceException("数据行不能为空");
        }
        String seasonName = trim(row.getSeasonName());
        if (seasonName == null) {
            throw new ServiceException("赛季名称不能为空");
        }
        if (row.getMatchTime() == null) {
            throw new ServiceException("比赛开球时间不能为空");
        }

        Fx67llDortmundBasicSeason season = findSeasonByName(seasons, seasonName);
        Fx67llDortmundBasicTeam homeTeam = findTeam(teams, row.getHomeTeamCode(), row.getHomeTeamName(), "主队");
        Fx67llDortmundBasicTeam awayTeam = findTeam(teams, row.getAwayTeamCode(), row.getAwayTeamName(), "客队");
        if (Objects.equals(homeTeam.getTeamId(), awayTeam.getTeamId())) {
            throw new ServiceException("主队和客队不能是同一支球队");
        }

        String matchCode = trim(row.getMatchCode());
        if (matchCode == null) {
            matchCode = generateImportMatchCode(row.getMatchTime(), existingCodes, importCodes);
        } else if (existingCodes.contains(matchCode) || importCodes.contains(matchCode)) {
            throw new ServiceException("比赛编码“" + matchCode + "”已存在");
        }
        importCodes.add(matchCode);

        Fx67llDortmundBasicMatch match = new Fx67llDortmundBasicMatch();
        match.setMatchCode(matchCode);
        match.setSeasonId(season.getSeasonId());
        match.setHomeTeamId(homeTeam.getTeamId());
        match.setAwayTeamId(awayTeam.getTeamId());
        match.setMatchTime(row.getMatchTime());
        match.setMatchVenue(trim(row.getMatchVenue()) == null ? homeTeam.getTeamVenue() : trim(row.getMatchVenue()));
        match.setMatchStatus(trim(row.getMatchStatus()) == null ? "0" : trim(row.getMatchStatus()));
        match.setMatchResult(trim(row.getMatchResult()));
        match.setOddsResult(trim(row.getOddsResult() != null ? row.getOddsResult() : row.getMultiplierResult()));
        match.setMatchRemark(trim(row.getMatchRemark()));
        return match;
    }

    /**
     * 按完整名称查找当前用户可见赛季。
     */
    private Fx67llDortmundBasicSeason findSeasonByName(List<Fx67llDortmundBasicSeason> seasons, String seasonName) {
        Fx67llDortmundBasicSeason matched = null;
        if (seasons != null) {
            for (Fx67llDortmundBasicSeason season : seasons) {
                if (seasonName.equals(trim(season.getSeasonName()))) {
                    if (matched != null) {
                        throw new ServiceException("赛季名称“" + seasonName + "”不唯一，请先处理重复赛季");
                    }
                    matched = season;
                }
            }
        }
        if (matched == null) {
            throw new ServiceException("未找到赛季“" + seasonName + "”");
        }
        return matched;
    }

    /**
     * 优先按球队编码精确匹配，编码为空时按名称唯一匹配。
     */
    private Fx67llDortmundBasicTeam findTeam(List<Fx67llDortmundBasicTeam> teams, String teamCode,
            String teamName, String label) {
        String code = trim(teamCode);
        String name = trim(teamName);
        if (code == null && name == null) {
            throw new ServiceException(label + "编码或名称不能为空");
        }
        Fx67llDortmundBasicTeam matched = null;
        if (teams != null) {
            for (Fx67llDortmundBasicTeam team : teams) {
                boolean matchedRow = code != null
                        ? code.equals(trim(team.getTeamCode()))
                        : name.equals(trim(team.getTeamName())) || name.equals(trim(team.getTeamNameShort()))
                                || name.equals(trim(team.getTeamNameEn()));
                if (!matchedRow) {
                    continue;
                }
                if (matched != null && !Objects.equals(matched.getTeamId(), team.getTeamId())) {
                    throw new ServiceException(label + "“" + (code == null ? name : code) + "”匹配到多支球队，请改用唯一编码");
                }
                matched = team;
            }
        }
        if (matched == null) {
            throw new ServiceException("未找到" + label + "“" + (code == null ? name : code) + "”");
        }
        return matched;
    }

    /**
     * 生成全局唯一且不超过 23 个字符的比赛编码。
     */
    private String generateImportMatchCode(java.util.Date matchTime, Set<String> existingCodes, Set<String> importCodes) {
        String datePart = DateUtils.parseDateToStr("yyyyMMdd", matchTime);
        for (int attempt = 0; attempt < 20; attempt++) {
            String candidate = "MATCH-" + datePart + "-" + String.format("%06d", MATCH_CODE_RANDOM.nextInt(1000000));
            if (!existingCodes.contains(candidate) && !importCodes.contains(candidate)) {
                return candidate;
            }
        }
        throw new ServiceException("自动生成比赛编码失败，请在文件中手工填写唯一编码");
    }

    /**
     * 构造比赛业务唯一键，用于在导入前检查文件内和数据库内重复。
     */
    private String buildMatchBusinessKey(Long seasonId, Long homeTeamId, Long awayTeamId, java.util.Date matchTime) {
        return (seasonId == null ? "" : seasonId) + "|" + (homeTeamId == null ? "" : homeTeamId) + "|"
                + (awayTeamId == null ? "" : awayTeamId) + "|"
                + (matchTime == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", matchTime));
    }

    /**
     * 去除首尾空白，空串统一转换为 null。
     */
    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
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

    /**
     * 比赛记录导入的预校验结果，保留条目序号用于关联倍率写入错误。
     */
    private static class PreparedMatchImport {
        private final Fx67llDortmundBasicMatch match;
        private final List<Fx67llDortmundMatchMultiplierImport> multipliers;
        private final int rowNumber;

        private PreparedMatchImport(Fx67llDortmundBasicMatch match,
                List<Fx67llDortmundMatchMultiplierImport> multipliers, int rowNumber) {
            this.match = match;
            this.multipliers = multipliers;
            this.rowNumber = rowNumber;
        }
    }

    /**
     * 比赛结果导入的预校验结果；null 赛果表示保持原值。
     */
    private static class PreparedResultImport {
        private final Fx67llDortmundBasicMatch match;
        private final int rowNumber;
        private final boolean matchResultProvided;
        private final String matchResult;
        private final boolean multiplierResultProvided;
        private final String multiplierResult;
        private final List<Fx67llDortmundMatchMultiplierImport> multipliers;
        private final boolean resultChanged;

        private PreparedResultImport(Fx67llDortmundBasicMatch match, int rowNumber,
                boolean matchResultProvided, String matchResult, boolean multiplierResultProvided,
                String multiplierResult, List<Fx67llDortmundMatchMultiplierImport> multipliers,
                boolean resultChanged) {
            this.match = match;
            this.rowNumber = rowNumber;
            this.matchResultProvided = matchResultProvided;
            this.matchResult = matchResult;
            this.multiplierResultProvided = multiplierResultProvided;
            this.multiplierResult = multiplierResult;
            this.multipliers = multipliers;
            this.resultChanged = resultChanged;
        }
    }
}
