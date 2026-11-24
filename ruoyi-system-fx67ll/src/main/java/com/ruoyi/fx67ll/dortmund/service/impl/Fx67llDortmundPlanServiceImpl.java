package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraPlanRel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlan;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanReturnRel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanSummary;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationItem;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlanMatchRel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundRoundPlanRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraPlanRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMatchRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundRoundPlanRelMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanReturnRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnGenerationMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;

/**
 * 方案计划Service业务层处理
 * <p>
 * 第1.5阶段方案以“已采用回报组合”为材料来源：自动预计本金取各组合所属生成批次基础金额之和，
 * 自动预计回报取组合预计回报之和。
 * 预计本金与预计回报可手工修正，留空时回退到服务端自动值；预计倍率只接受手工输入。
 * 实际本金、实际倍率与实际回报只接受用户手工输入且为必填项，服务端不会按组合派生或覆盖。
 * 组合关系变化会清空三项手工预计值并强制摘要失效；关系替换、摘要刷新和删除均在同一事务内完成。
 *
 * @author fx67ll
 * @date 2026-10-29
 */
@Service
public class Fx67llDortmundPlanServiceImpl implements IFx67llDortmundPlanService {
    /**
     * 正文最大长度
     */
    private static final int CONTENT_MAX_LENGTH = DortmundModuleConfig.Field.PLAN_CONTENT;

    /**
     * 说明最大长度
     */
    private static final int REMARK_MAX_LENGTH = DortmundModuleConfig.Field.PLAN_REMARK;

    /**
     * 方案预计倍率、实际倍率及自动派生倍率的统一上限。
     */
    private static final BigDecimal MAX_PLAN_RATE = DortmundModuleConfig.Content.MAX_PLAN_RATE;

    /**
     * 方案预计回报、实际回报及自动派生回报的统一上限。
     */
    private static final BigDecimal MAX_PLAN_AMOUNT = DortmundModuleConfig.Content.MAX_PLAN_AMOUNT;

    @Autowired
    private Fx67llDortmundPlanMapper fx67llDortmundPlanMapper;

    @Autowired
    private Fx67llDortmundPlanReturnRelMapper fx67llDortmundPlanReturnRelMapper;

    @Autowired
    private Fx67llDortmundReturnGenerationMapper fx67llDortmundReturnGenerationMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationMapper fx67llDortmundReturnCombinationMapper;

    @Autowired
    private IFx67llDortmundReturnCombinationService fx67llDortmundReturnCombinationService;

    @Autowired
    private Fx67llDortmundPlanMatchRelMapper fx67llDortmundPlanMatchRelMapper;

    @Autowired
    private Fx67llDortmundExtraPlanRelMapper fx67llDortmundExtraPlanRelMapper;

    @Autowired
    private Fx67llDortmundRoundPlanRelMapper fx67llDortmundRoundPlanRelMapper;

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    /**
     * 查询方案计划
     * <p>
     * 仅返回存在、未删除且在当前用户数据范围内的方案计划，其余情况抛出受控业务异常
     *
     * @param planId 方案计划主键
     * @return 方案计划
     */
    @Override
    public Fx67llDortmundPlan selectFx67llDortmundPlanByPlanId(Long planId) {
        Fx67llDortmundPlan plan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(planId);
        if (plan == null || !"0".equals(plan.getDelFlag()) || !checkPlanDataScope(plan.getUserId())) {
            throw new ServiceException("方案计划不存在或没有访问权限！");
        }
        fillMatchesAndCount(plan);
        fillPhase15Fields(plan);
        return plan;
    }

    /**
     * 查询方案计划列表
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 方案计划集合
     */
    @Override
    public List<Fx67llDortmundPlan> selectFx67llDortmundPlanList(Fx67llDortmundPlan fx67llDortmundPlan) {
        if (fx67llDortmundPlan == null) {
            fx67llDortmundPlan = new Fx67llDortmundPlan();
        }
        if (!hasAllDataScope()) {
            fx67llDortmundPlan.setUserId(SecurityUtils.getUserId());
        }
        fx67llDortmundPlan.setDelFlag("0");
        List<Fx67llDortmundPlan> list = fx67llDortmundPlanMapper.selectFx67llDortmundPlanList(fx67llDortmundPlan);
        fillPlanListDetails(list);
        return list;
    }

    /**
     * 新增方案计划
     * <p>
     * 文字、比赛关系、本次新建比赛、允许保存的对象键同一事务写入，任一步失败全部回滚
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan) {
        if (fx67llDortmundPlan.getCombinationIds() != null) {
            return insertPhase15Plan(fx67llDortmundPlan);
        }
        checkPlanFields(fx67llDortmundPlan);
        List<Fx67llDortmundBasicMatch> resolvedMatches = resolveMatches(fx67llDortmundPlan, null);
        fx67llDortmundPlan.setUserId(SecurityUtils.getUserId());
        fx67llDortmundPlan.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundPlan.setCreateTime(DateUtils.getNowDate());
        fx67llDortmundPlan.setDelFlag("0");
        int rows = fx67llDortmundPlanMapper.insertFx67llDortmundPlan(fx67llDortmundPlan);
        if (rows > 0) {
            insertMatchRels(fx67llDortmundPlan.getPlanId(), resolvedMatches);
        }
        return rows;
    }

    /**
     * 修改方案计划
     * <p>
     * 已关联、后来被关闭的比赛保持原关联不动，仅拒绝新选关闭比赛；保存后至少仍有一场比赛
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan) {
        if (fx67llDortmundPlan.getCombinationIds() != null) {
            return updatePhase15Plan(fx67llDortmundPlan);
        }
        if (fx67llDortmundPlan.getPlanId() == null) {
            throw new ServiceException("方案计划主键不能为空！");
        }
        Fx67llDortmundPlan oldPlan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(fx67llDortmundPlan.getPlanId());
        if (oldPlan == null || !"0".equals(oldPlan.getDelFlag())) {
            throw new ServiceException("方案计划不存在或已删除！");
        }
        if (!checkPlanDataScope(oldPlan.getUserId())) {
            throw new ServiceException("没有权限修改该方案计划！");
        }
        checkPlanFields(fx67llDortmundPlan);
        List<Long> keptMatchIds = selectPlanMatchIds(fx67llDortmundPlan.getPlanId());
        List<Fx67llDortmundBasicMatch> resolvedMatches = resolveMatches(fx67llDortmundPlan, keptMatchIds);
        if (resolvedMatches.isEmpty()) {
            throw new ServiceException("方案计划保存后至少要关联一场比赛！");
        }
        fx67llDortmundPlan.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundPlan.setUpdateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundPlanMapper.updateFx67llDortmundPlan(fx67llDortmundPlan);
        if (rows > 0) {
            fx67llDortmundPlanMatchRelMapper.deleteFx67llDortmundPlanMatchRelByPlanId(fx67llDortmundPlan.getPlanId());
            insertMatchRels(fx67llDortmundPlan.getPlanId(), resolvedMatches);
        }
        return rows;
    }

    /**
     * 批量删除方案计划
     *
     * @param planIds 需要删除的方案计划主键集合
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFx67llDortmundPlanByPlanIds(Long[] planIds) {
        if (planIds == null || planIds.length == 0) {
            return 0;
        }
        int rows = 0;
        for (Long planId : planIds) {
            rows += deleteFx67llDortmundPlanByPlanId(planId);
        }
        return rows;
    }

    /**
     * 删除方案计划信息
     * <p>
     * 逻辑删除方案计划，同一事务删除它的比赛关系行并清理只属于它的图片文件
     *
     * @param planId 方案计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFx67llDortmundPlanByPlanId(Long planId) {
        Fx67llDortmundPlan oldPlan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(planId);
        if (oldPlan == null || !"0".equals(oldPlan.getDelFlag())) {
            throw new ServiceException("方案计划不存在或已删除！");
        }
        if (!checkPlanDataScope(oldPlan.getUserId())) {
            throw new ServiceException("没有权限删除该方案计划！");
        }
        // 先确认没有被外快盈亏或数值轮次引用，避免删除后留下孤儿关系。
        checkPlanDeleteReference(planId);
        Fx67llDortmundPlan plan = new Fx67llDortmundPlan();
        plan.setPlanId(planId);
        plan.setDelFlag("2");
        plan.setUpdateBy(SecurityUtils.getUsername());
        plan.setUpdateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundPlanMapper.updateFx67llDortmundPlan(plan);
        if (rows > 0) {
            fx67llDortmundPlanMatchRelMapper.deleteFx67llDortmundPlanMatchRelByPlanId(planId);
            fx67llDortmundPlanReturnRelMapper.deleteByPlanId(planId);
        }
        return rows;
    }

    /**
     * 查询方案计划已保存摘要
     *
     * @param planId 方案计划主键
     * @return 方案计划摘要
     */
    @Override
    public Fx67llDortmundPlanSummary selectPlanSummary(Long planId) {
        return toPlanSummary(loadPlanForPhase15(planId));
    }

    /**
     * 刷新并保存方案计划摘要
     *
     * @param planId 方案计划主键
     * @return 方案计划摘要
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Fx67llDortmundPlanSummary refreshPlanSummary(Long planId) {
        Fx67llDortmundPlan plan = loadPlanForPhase15(planId, true);
        String content = formatPlanSummary(plan, loadPlanCombinations(planId));
        plan.setSummaryContent(content);
        // 摘要刷新成功后才置N；材料字段变化会由更新流程重新置Y。
        plan.setSummaryUpdateFlag("N");
        plan.setSummaryUpdateTime(DateUtils.getNowDate());
        plan.setUpdateBy(SecurityUtils.getUsername());
        plan.setUpdateTime(DateUtils.getNowDate());
        fx67llDortmundPlanMapper.updateFx67llDortmundPlanSummary(plan);
        return toPlanSummary(plan);
    }

    /**
     * 校验方案计划基础字段
     *
     * @param fx67llDortmundPlan 方案计划
     */
    private void checkPlanFields(Fx67llDortmundPlan fx67llDortmundPlan) {
        fx67llDortmundPlan.setPlanContent(DortmundFieldValidator.requireText(
                fx67llDortmundPlan.getPlanContent(), "方案计划正文", CONTENT_MAX_LENGTH));
        fx67llDortmundPlan.setPlanRemark(DortmundFieldValidator.optionalText(
                fx67llDortmundPlan.getPlanRemark(), "方案计划说明", REMARK_MAX_LENGTH));
        // 三项预计字段选填，留空表示采用关联组合自动汇总值；三项实际字段为必填的手工登记值。
        validatePlanAmount(fx67llDortmundPlan.getPlanExpectedPrincipal(), "方案预计本金");
        validatePlanRate(fx67llDortmundPlan.getPlanExpectedOdds(), "方案预计倍率");
        validatePlanAmount(fx67llDortmundPlan.getPlanExpectedReturn(), "方案预计回报");
        validateRequiredPlanAmount(fx67llDortmundPlan.getPlanActualPrincipal(), "方案实际本金");
        validateRequiredNonNegativeRate(fx67llDortmundPlan.getPlanActualOdds(), "方案实际倍率");
        validateRequiredPlanAmount(fx67llDortmundPlan.getPlanActualReturn(), "方案实际回报");
    }

    /**
     * 新增第1.5阶段方案：组合集合是自动预计值的事实源，自动预计本金和预计回报写回计划，
     * 三项实际字段保留请求中的手工值，比赛关系由组合明细派生。
     */
    private int insertPhase15Plan(Fx67llDortmundPlan plan) {
        checkPlanFields(plan);
        PlanCombinationStats stats = collectCombinationStats(plan.getCombinationIds(), null);
        Date now = DateUtils.getNowDate();
        plan.setUserId(SecurityUtils.getUserId());
        plan.setCreateBy(SecurityUtils.getUsername());
        plan.setCreateTime(now);
        plan.setDelFlag("0");
        // 自动预计值只由组合统计写入；实际本金、实际回报与实际倍率始终采用用户手工输入值。
        plan.setCalculatedExpectedPrincipal(stats.expectedPrincipal);
        plan.setCalculatedExpectedReturn(stats.expectedReturn);
        validatePlanAmount(plan.getCalculatedExpectedPrincipal(), "方案自动预计本金");
        validatePlanAmount(plan.getCalculatedExpectedReturn(), "方案自动预计回报");
        // 新方案还没有摘要正文，标记为Y等待用户刷新。
        plan.setSummaryUpdateFlag("Y");
        int rows = fx67llDortmundPlanMapper.insertFx67llDortmundPlan(plan);
        if (rows > 0) {
            replacePhase15Relations(plan.getPlanId(), plan.getCombinationIds(), stats.matchIds);
        }
        return rows;
    }

    /**
     * 修改第1.5阶段方案：先锁定计划行；组合集合变化时清空三项手工预计值并强制摘要重新生成，
     * 三项实际字段属于用户手工登记结果，不随组合变化清空。
     */
    private int updatePhase15Plan(Fx67llDortmundPlan plan) {
        if (plan.getPlanId() == null) {
            throw new ServiceException("方案计划主键不能为空！");
        }
        Fx67llDortmundPlan oldPlan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanIdForUpdate(plan.getPlanId());
        if (oldPlan == null || !"0".equals(oldPlan.getDelFlag()) || !checkPlanDataScope(oldPlan.getUserId())) {
            throw new ServiceException("方案计划不存在或没有访问权限！");
        }
        checkPlanFields(plan);
        PlanCombinationStats stats = collectCombinationStats(plan.getCombinationIds(), plan.getPlanId());
        List<Long> oldCombinationIds = selectPlanCombinationIds(plan.getPlanId());
        if (!sameIds(oldCombinationIds, plan.getCombinationIds())) {
            // 组合集合已变化，原手工预计本金/倍率/回报不再对应同一组组合，先清空再由服务端重新派生。
            // 实际本金、实际回报与实际倍率是用户手工登记值，不随组合重算而清空。
            plan.setPlanExpectedPrincipal(null);
            plan.setPlanExpectedOdds(null);
            plan.setPlanExpectedReturn(null);
        }
        // 自动预计值只由组合统计写入；实际本金、实际回报与实际倍率始终保存用户手工输入值。
        plan.setCalculatedExpectedPrincipal(stats.expectedPrincipal);
        plan.setCalculatedExpectedReturn(stats.expectedReturn);
        validatePlanAmount(plan.getCalculatedExpectedPrincipal(), "方案自动预计本金");
        validatePlanAmount(plan.getCalculatedExpectedReturn(), "方案自动预计回报");
        // 只有摘要正文仍与材料字段一致时才允许保留N，否则强制Y。
        plan.setSummaryUpdateFlag(resolveSummaryFlag(plan, oldPlan, oldCombinationIds));
        plan.setUpdateBy(SecurityUtils.getUsername());
        plan.setUpdateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundPlanMapper.updateFx67llDortmundPlanPhase15(plan);
        if (rows > 0) {
            replacePhase15Relations(plan.getPlanId(), plan.getCombinationIds(), stats.matchIds);
        }
        return rows;
    }

    /**
     * 汇总方案关联组合的派生指标。
     * <p>
     * 自动预计本金=各组合所属生成批次基础金额之和；自动预计回报=全部组合预计回报之和；
     * 组合按主键升序加行锁，避免并发关联时死锁；只允许当前用户可见、已采用且未被其他计划引用的组合。
     */
    private PlanCombinationStats collectCombinationStats(List<Long> combinationIds, Long planId) {
        PlanCombinationStats stats = new PlanCombinationStats();
        if (combinationIds == null || combinationIds.isEmpty()) {
            return stats;
        }
        LinkedHashSet<Long> uniqueIds = new LinkedHashSet<>(combinationIds);
        if (uniqueIds.size() != combinationIds.size()) {
            throw new ServiceException("方案计划不能关联重复的回报组合！");
        }
        BigDecimal totalReturn = BigDecimal.ZERO;
        BigDecimal totalInput = BigDecimal.ZERO;
        List<Long> lockIds = new ArrayList<>(uniqueIds);
        if (lockIds.contains(null)) {
            throw new ServiceException("回报组合主键不能为空！");
        }
        Collections.sort(lockIds);
        for (Long combinationId : lockIds) {
            if (combinationId == null) {
                throw new ServiceException("回报组合主键不能为空！");
            }
            if (fx67llDortmundReturnCombinationMapper
                    .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(combinationId) == null) {
                throw new ServiceException("回报组合不存在！");
            }
            Fx67llDortmundReturnCombination combination = fx67llDortmundReturnCombinationService.selectCombinationById(combinationId);
            if (combination == null || !"Y".equals(combination.getAdoptedFlag())) {
                throw new ServiceException("只能关联当前用户可访问且已采用的回报组合！");
            }
            Long relationPlanId = fx67llDortmundPlanReturnRelMapper.selectPlanIdByCombinationId(combinationId);
            if (relationPlanId != null && !relationPlanId.equals(planId)) {
                throw new ServiceException("回报组合已被其他方案计划引用！");
            }
            Fx67llDortmundReturnGeneration generation = fx67llDortmundReturnGenerationMapper
                    .selectFx67llDortmundReturnGenerationByGenerationId(combination.getGenerationId());
            if (generation == null || generation.getInputAmount() == null || generation.getInputAmount().signum() <= 0) {
                throw new ServiceException("所选回报组合的生成批次数据不正确！");
            }
            totalReturn = totalReturn.add(defaultAmount(combination.getEstimatedReturn()));
            totalInput = totalInput.add(generation.getInputAmount());
            for (Fx67llDortmundReturnCombinationItem item : combination.getMatches()) {
                if (item.getMatchId() != null) {
                    stats.matchIds.add(item.getMatchId());
                }
            }
        }
        stats.expectedPrincipal = totalInput.setScale(
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_FRACTION_DIGITS, RoundingMode.HALF_UP);
        stats.expectedReturn = totalReturn.setScale(
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_FRACTION_DIGITS, RoundingMode.HALF_UP);
        return stats;
    }

    /**
     * 按提交顺序重建计划-组合和计划-比赛关系；比赛关系取组合明细并集，调用方负责保证事务。
     */
    private void replacePhase15Relations(Long planId, List<Long> combinationIds, LinkedHashSet<Long> matchIds) {
        fx67llDortmundPlanReturnRelMapper.deleteByPlanId(planId);
        fx67llDortmundPlanMatchRelMapper.deleteFx67llDortmundPlanMatchRelByPlanId(planId);
        // 展示排序将0视为未配置；方案内组合关系从1开始，保持用户选择顺序。
        int sort = 1;
        try {
            if (combinationIds != null) {
                for (Long combinationId : combinationIds) {
                    Fx67llDortmundPlanReturnRel relation = new Fx67llDortmundPlanReturnRel();
                    relation.setPlanId(planId);
                    relation.setCombinationId(combinationId);
                    relation.setRelSort(sort++);
                    relation.setCreateBy(SecurityUtils.getUsername());
                    relation.setCreateTime(DateUtils.getNowDate());
                    fx67llDortmundPlanReturnRelMapper.insertFx67llDortmundPlanReturnRel(relation);
                }
            }
        } catch (DuplicateKeyException e) {
            throw new ServiceException("回报组合已被其他方案计划引用，请刷新后重试！");
        }
        for (Long matchId : matchIds) {
            Fx67llDortmundPlanMatchRel relation = new Fx67llDortmundPlanMatchRel();
            relation.setPlanId(planId);
            relation.setMatchId(matchId);
            relation.setCreateBy(SecurityUtils.getUsername());
            relation.setCreateTime(DateUtils.getNowDate());
            fx67llDortmundPlanMatchRelMapper.insertFx67llDortmundPlanMatchRel(relation);
        }
    }

    private List<Long> selectPlanCombinationIds(Long planId) {
        List<Long> ids = new ArrayList<>();
        for (Fx67llDortmundPlanReturnRel relation : fx67llDortmundPlanReturnRelMapper.selectListByPlanId(planId)) {
            ids.add(relation.getCombinationId());
        }
        return ids;
    }

    private List<Fx67llDortmundReturnCombination> loadPlanCombinations(Long planId) {
        List<Fx67llDortmundReturnCombination> combinations = new ArrayList<>();
        for (Long combinationId : selectPlanCombinationIds(planId)) {
            Fx67llDortmundReturnCombination combination = fx67llDortmundReturnCombinationService.selectCombinationById(combinationId);
            if (combination != null) {
                combinations.add(combination);
            }
        }
        return combinations;
    }

    private Fx67llDortmundPlan loadPlanForPhase15(Long planId) {
        return loadPlanForPhase15(planId, false);
    }

    private Fx67llDortmundPlan loadPlanForPhase15(Long planId, boolean forUpdate) {
        Fx67llDortmundPlan plan = forUpdate
                ? fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanIdForUpdate(planId)
                : fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(planId);
        if (plan == null || !"0".equals(plan.getDelFlag()) || !checkPlanDataScope(plan.getUserId())) {
            throw new ServiceException("方案计划不存在或没有访问权限！");
        }
        fillPhase15Fields(plan);
        return plan;
    }

    private void fillPhase15Fields(Fx67llDortmundPlan plan) {
        // 回读真实关系，避免前端提交的组合集合与数据库当前状态不一致。
        List<Long> combinationIds = selectPlanCombinationIds(plan.getPlanId());
        plan.setCombinationIds(combinationIds);
        fillPlanCombinationVerification(plan, combinationIds, loadCombinationVerificationStatuses(combinationIds));
        // 预计倍率由用户手工填写；手工预计本金/回报为空时回退到组合派生值。
        plan.setEffectiveExpectedPrincipal(plan.getPlanExpectedPrincipal() == null
                ? plan.getCalculatedExpectedPrincipal() : plan.getPlanExpectedPrincipal());
        plan.setEffectiveExpectedOdds(plan.getPlanExpectedOdds());
        plan.setEffectiveExpectedReturn(plan.getPlanExpectedReturn() == null
                ? plan.getCalculatedExpectedReturn() : plan.getPlanExpectedReturn());
    }

    private Fx67llDortmundPlanSummary toPlanSummary(Fx67llDortmundPlan plan) {
        Fx67llDortmundPlanSummary summary = new Fx67llDortmundPlanSummary();
        summary.setPlanId(plan.getPlanId());
        summary.setContent(plan.getSummaryContent());
        summary.setGeneratedTime(plan.getSummaryUpdateTime());
        summary.setSummaryUpdateFlag(plan.getSummaryUpdateFlag() == null ? "Y" : plan.getSummaryUpdateFlag());
        return summary;
    }

    private String formatPlanSummary(Fx67llDortmundPlan plan, List<Fx67llDortmundReturnCombination> combinations) {
        StringBuilder builder = new StringBuilder();
        builder.append("方案计划：").append(plan.getPlanContent()).append('\n');
        builder.append("预计本金：").append(plan.getEffectiveExpectedPrincipal()).append('\n');
        builder.append("预计倍率：").append(plan.getEffectiveExpectedOdds()).append('\n');
        builder.append("预计回报：").append(plan.getEffectiveExpectedReturn()).append('\n');
        builder.append("实际本金：").append(plan.getPlanActualPrincipal()).append('\n');
        builder.append("实际倍率：").append(plan.getPlanActualOdds()).append('\n');
        builder.append("实际回报：").append(plan.getPlanActualReturn()).append('\n');
        builder.append("回报组合数：").append(combinations.size()).append('\n');
        for (int i = 0; i < combinations.size(); i++) {
            Fx67llDortmundReturnCombination combination = combinations.get(i);
            builder.append(i + 1).append(". ").append(combination.getCombinationTitle())
                    .append("｜组合倍率：").append(combination.getCombinedOdds())
                    .append("｜预计回报：").append(combination.getEstimatedReturn())
                    .append("｜比赛数：").append(combination.getMatchCount()).append('\n');
        }
        if (plan.getPlanRemark() != null && !plan.getPlanRemark().trim().isEmpty()) {
            builder.append("说明：").append(plan.getPlanRemark());
        } else if (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n') {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private boolean sameIds(List<Long> left, List<Long> right) {
        return new LinkedHashSet<>(left == null ? new ArrayList<Long>() : left)
                .equals(new LinkedHashSet<>(right == null ? new ArrayList<Long>() : right));
    }

    /**
     * 解析摘要更新标记。
     * <p>
     * Y表示摘要已过期、需要刷新；N表示摘要正文仍与当前计划数据一致。新增计划和数据有变化的修改一律强制Y，
     * 只有摘要正文存在、组合关系及摘要所包含的业务字段均未变化时，才允许用户显式提交N。
     */
    private String resolveSummaryFlag(Fx67llDortmundPlan plan, Fx67llDortmundPlan oldPlan,
            List<Long> oldCombinationIds) {
        String requested = plan.getSummaryUpdateFlag();
        if (requested == null || requested.trim().isEmpty()) {
            return "Y";
        }
        if (!"Y".equals(requested) && !"N".equals(requested)) {
            throw new ServiceException("摘要更新标记只能为Y或N！");
        }
        if ("Y".equals(requested)) {
            return "Y";
        }
        boolean materialChanged = !Objects.equals(plan.getPlanContent(), oldPlan.getPlanContent())
                || !sameAmount(plan.getPlanExpectedPrincipal(), oldPlan.getPlanExpectedPrincipal())
                || !sameAmount(plan.getPlanExpectedOdds(), oldPlan.getPlanExpectedOdds())
                || !sameAmount(plan.getPlanExpectedReturn(), oldPlan.getPlanExpectedReturn())
                || !sameAmount(plan.getPlanActualPrincipal(), oldPlan.getPlanActualPrincipal())
                || !sameAmount(plan.getPlanActualOdds(), oldPlan.getPlanActualOdds())
                || !sameAmount(plan.getPlanActualReturn(), oldPlan.getPlanActualReturn())
                || !Objects.equals(plan.getPlanRemark(), oldPlan.getPlanRemark())
                || !sameIds(oldCombinationIds, plan.getCombinationIds());
        if (!"N".equals(oldPlan.getSummaryUpdateFlag()) || oldPlan.getSummaryUpdateTime() == null) {
            return "Y";
        }
        if (materialChanged || oldPlan.getSummaryContent() == null || oldPlan.getSummaryContent().trim().isEmpty()) {
            return "Y";
        }
        return "N";
    }

    private boolean sameAmount(BigDecimal left, BigDecimal right) {
        if (left == null || right == null) {
            return left == right;
        }
        return left.compareTo(right) == 0;
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private void validatePlanRate(BigDecimal value, String label) {
        DortmundFieldValidator.validateDecimal(value, label, false, false, null, BigDecimal.ZERO,
                MAX_PLAN_RATE,
                DortmundModuleConfig.Numeric.PLAN_RATE_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.PLAN_RATE_FRACTION_DIGITS);
    }

    private void validatePlanAmount(BigDecimal value, String label) {
        DortmundFieldValidator.validateDecimal(value, label, false, false, BigDecimal.ZERO, null,
                MAX_PLAN_AMOUNT,
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_FRACTION_DIGITS);
    }

    /**
     * 校验必填的实际金额字段。
     */
    private void validateRequiredPlanAmount(BigDecimal value, String label) {
        DortmundFieldValidator.validateDecimal(value, label, true, false, BigDecimal.ZERO, null,
                MAX_PLAN_AMOUNT,
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.PLAN_AMOUNT_FRACTION_DIGITS);
    }

    /**
     * 校验必填的非负实际倍率；允许为0，表示方案没有产生返还。
     */
    private void validateRequiredNonNegativeRate(BigDecimal value, String label) {
        DortmundFieldValidator.validateDecimal(value, label, true, false, BigDecimal.ZERO, null,
                MAX_PLAN_RATE,
                DortmundModuleConfig.Numeric.PLAN_RATE_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.PLAN_RATE_FRACTION_DIGITS);
    }

    private static class PlanCombinationStats {
        /** 关联组合所属生成批次基础金额之和，即自动预计本金。 */
        private BigDecimal expectedPrincipal;
        /** 关联组合预计回报之和，即自动预计回报。 */
        private BigDecimal expectedReturn;
        private final LinkedHashSet<Long> matchIds = new LinkedHashSet<>();
    }

    /**
     * 解析方案计划的比赛集合：matchIds 引用已有正常比赛，newMatches 当场新建，合并去重后至少一场
     * <p>
     * 修改时已关联、后来被关闭的比赛保持原关联不动，仅拒绝新选关闭比赛
     *
     * @param fx67llDortmundPlan 方案计划
     * @param keptMatchIds       保留的已关联比赛集合（新增时为空）
     * @return 解析后的比赛集合
     */
    private List<Fx67llDortmundBasicMatch> resolveMatches(Fx67llDortmundPlan fx67llDortmundPlan, List<Long> keptMatchIds) {
        List<Fx67llDortmundBasicMatch> resolvedMatches = new ArrayList<>();
        LinkedHashSet<Long> matchIdSet = new LinkedHashSet<>();
        if (keptMatchIds != null) {
            matchIdSet.addAll(keptMatchIds);
        }
        Long[] matchIds = fx67llDortmundPlan.getMatchIds();
        if (matchIds != null) {
            for (Long matchId : matchIds) {
                Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchByMatchId(matchId);
                if (match == null) {
                    throw new ServiceException("所选比赛不存在，请重新选择！");
                }
                if (!"0".equals(match.getMatchStatus()) && (keptMatchIds == null || !keptMatchIds.contains(matchId))) {
                    throw new ServiceException("已关闭的比赛不能进入方案计划，请改选正常比赛！");
                }
                if (matchIdSet.add(matchId)) {
                    resolvedMatches.add(match);
                }
            }
        }
        List<Fx67llDortmundBasicMatch> newMatches = fx67llDortmundPlan.getNewMatches();
        if (newMatches != null) {
            for (Fx67llDortmundBasicMatch newMatch : newMatches) {
                checkNewMatchFields(newMatch);
                newMatch.setAnalysisCount(null);
                try {
                    fx67llDortmundBasicMatchService.insertFx67llDortmundBasicMatch(newMatch);
                } catch (ServiceException e) {
                    throw e;
                }
                if (matchIdSet.add(newMatch.getMatchId())) {
                    resolvedMatches.add(newMatch);
                }
            }
        }
        if (resolvedMatches.isEmpty()) {
            throw new ServiceException("方案计划至少要关联一场比赛！");
        }
        return resolvedMatches;
    }

    /**
     * 校验当场新建比赛的字段：只能引用已有赛季和球队，资料状态必须为正常
     *
     * @param newMatch 当场新建比赛
     */
    private void checkNewMatchFields(Fx67llDortmundBasicMatch newMatch) {
        if (newMatch.getNewSeason() != null || newMatch.getNewHomeTeam() != null || newMatch.getNewAwayTeam() != null) {
            throw new ServiceException("方案计划内新建比赛只能引用已有赛季和球队，不能当场新建！");
        }
        if (newMatch.getMatchStatus() == null || !"0".equals(newMatch.getMatchStatus())) {
            throw new ServiceException("方案计划内新建比赛的资料状态必须为正常！");
        }
    }

    /**
     * 批量插入方案计划与比赛关系行
     *
     * @param planId          方案计划主键
     * @param resolvedMatches 解析后的比赛集合
     */
    private void insertMatchRels(Long planId, List<Fx67llDortmundBasicMatch> resolvedMatches) {
        for (Fx67llDortmundBasicMatch match : resolvedMatches) {
            Fx67llDortmundPlanMatchRel rel = new Fx67llDortmundPlanMatchRel();
            rel.setPlanId(planId);
            rel.setMatchId(match.getMatchId());
            rel.setCreateBy(SecurityUtils.getUsername());
            rel.setCreateTime(DateUtils.getNowDate());
            fx67llDortmundPlanMatchRelMapper.insertFx67llDortmundPlanMatchRel(rel);
        }
    }

    /**
     * 查询方案计划当前已关联的比赛ID集合
     *
     * @param planId 方案计划主键
     * @return 比赛ID集合
     */
    private List<Long> selectPlanMatchIds(Long planId) {
        List<Long> matchIds = new ArrayList<>();
        Fx67llDortmundPlanMatchRel relQuery = new Fx67llDortmundPlanMatchRel();
        relQuery.setPlanId(planId);
        List<Fx67llDortmundPlanMatchRel> relList = fx67llDortmundPlanMatchRelMapper.selectFx67llDortmundPlanMatchRelList(relQuery);
        if (relList != null) {
            for (Fx67llDortmundPlanMatchRel rel : relList) {
                matchIds.add(rel.getMatchId());
            }
        }
        return matchIds;
    }

    /**
     * 填充方案计划的关联比赛与数量（只读展示）
     *
     * @param plan 方案计划
     */
    private void fillMatchesAndCount(Fx67llDortmundPlan plan) {
        List<Long> matchIds = selectPlanMatchIds(plan.getPlanId());
        List<Fx67llDortmundBasicMatch> matches = new ArrayList<>();
        for (Long matchId : matchIds) {
            Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchByMatchId(matchId);
            if (match != null) {
                matches.add(match);
            }
        }
        plan.setMatches(matches);
        plan.setMatchCount(matches.size());
    }

    /**
     * 批量补全方案计划列表的关联比赛与回报组合，避免逐行查询
     *
     * @param plans 当前页方案计划
     */
    private void fillPlanListDetails(List<Fx67llDortmundPlan> plans) {
        if (plans == null || plans.isEmpty()) {
            return;
        }
        List<Long> planIds = new ArrayList<>();
        Map<Long, List<Long>> matchIdsByPlan = new LinkedHashMap<>();
        Map<Long, List<Fx67llDortmundBasicMatch>> matchesByPlan = new LinkedHashMap<>();
        Map<Long, List<Long>> combinationIdsByPlan = new LinkedHashMap<>();
        for (Fx67llDortmundPlan plan : plans) {
            if (plan.getPlanId() == null) {
                continue;
            }
            planIds.add(plan.getPlanId());
            matchIdsByPlan.put(plan.getPlanId(), new ArrayList<>());
            matchesByPlan.put(plan.getPlanId(), new ArrayList<>());
            combinationIdsByPlan.put(plan.getPlanId(), new ArrayList<>());
        }
        if (planIds.isEmpty()) {
            return;
        }

        List<Fx67llDortmundPlanMatchRel> matchRelations =
                fx67llDortmundPlanMatchRelMapper.selectFx67llDortmundPlanMatchRelListByPlanIds(planIds);
        LinkedHashSet<Long> matchIds = new LinkedHashSet<>();
        if (matchRelations != null) {
            for (Fx67llDortmundPlanMatchRel relation : matchRelations) {
                List<Long> planMatchIds = matchIdsByPlan.get(relation.getPlanId());
                if (planMatchIds != null && relation.getMatchId() != null) {
                    planMatchIds.add(relation.getMatchId());
                    matchIds.add(relation.getMatchId());
                }
            }
        }

        Map<Long, Fx67llDortmundBasicMatch> matchById = new HashMap<>();
        if (!matchIds.isEmpty()) {
            List<Fx67llDortmundBasicMatch> matches =
                    fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchListByIds(new ArrayList<>(matchIds));
            if (matches != null) {
                for (Fx67llDortmundBasicMatch match : matches) {
                    matchById.put(match.getMatchId(), match);
                }
            }
        }
        for (Map.Entry<Long, List<Long>> entry : matchIdsByPlan.entrySet()) {
            List<Fx67llDortmundBasicMatch> planMatches = matchesByPlan.get(entry.getKey());
            for (Long matchId : entry.getValue()) {
                Fx67llDortmundBasicMatch match = matchById.get(matchId);
                if (match != null) {
                    planMatches.add(match);
                }
            }
        }

        List<Fx67llDortmundPlanReturnRel> returnRelations =
                fx67llDortmundPlanReturnRelMapper.selectListByPlanIds(planIds);
        if (returnRelations != null) {
            for (Fx67llDortmundPlanReturnRel relation : returnRelations) {
                List<Long> planCombinationIds = combinationIdsByPlan.get(relation.getPlanId());
                if (planCombinationIds != null && relation.getCombinationId() != null) {
                    planCombinationIds.add(relation.getCombinationId());
                }
            }
        }

        LinkedHashSet<Long> allCombinationIds = new LinkedHashSet<>();
        for (List<Long> planCombinationIds : combinationIdsByPlan.values()) {
            allCombinationIds.addAll(planCombinationIds);
        }
        Map<Long, String> verificationStatusByCombination =
                loadCombinationVerificationStatuses(new ArrayList<>(allCombinationIds));
        for (Fx67llDortmundPlan plan : plans) {
            if (plan.getPlanId() == null) {
                continue;
            }
            List<Fx67llDortmundBasicMatch> matches = matchesByPlan.get(plan.getPlanId());
            plan.setMatches(matches == null ? new ArrayList<>() : matches);
            plan.setMatchCount(matches == null ? 0 : matches.size());
            List<Long> combinationIds = combinationIdsByPlan.get(plan.getPlanId());
            plan.setCombinationIds(combinationIds == null ? new ArrayList<>() : combinationIds);
            fillPlanCombinationVerification(plan, combinationIds, verificationStatusByCombination);
            // 预计倍率由用户手工填写；手工预计本金/回报为空时回退到组合派生值。
            plan.setEffectiveExpectedPrincipal(plan.getPlanExpectedPrincipal() == null
                    ? plan.getCalculatedExpectedPrincipal() : plan.getPlanExpectedPrincipal());
            plan.setEffectiveExpectedOdds(plan.getPlanExpectedOdds());
            plan.setEffectiveExpectedReturn(plan.getPlanExpectedReturn() == null
                    ? plan.getCalculatedExpectedReturn() : plan.getPlanExpectedReturn());
        }
    }

    /**
     * 一次查询当前页或当前详情涉及的全部组合核验状态，避免逐方案查询。
     *
     * @param combinationIds 回报组合主键集合
     * @return 组合主键到核验状态的映射
     */
    private Map<Long, String> loadCombinationVerificationStatuses(List<Long> combinationIds) {
        Map<Long, String> statusMap = new HashMap<>();
        if (combinationIds == null || combinationIds.isEmpty()) {
            return statusMap;
        }
        List<Fx67llDortmundReturnCombination> statuses =
                fx67llDortmundReturnCombinationMapper.selectVerificationStatusesByIds(combinationIds);
        if (statuses != null) {
            for (Fx67llDortmundReturnCombination status : statuses) {
                if (status.getCombinationId() != null) {
                    statusMap.put(status.getCombinationId(), status.getVerificationStatus());
                }
            }
        }
        return statusMap;
    }

    /**
     * 汇总方案关联组合的核验状态：任一失败即为失败；全部成功才是成功；任一未核验或缺失保持未核验。
     * 未关联组合的方案返回 null，前端不展示核验标签。
     *
     * @param combinationIds 方案关联组合主键
     * @param statusMap      组合核验状态映射
     * @return Y、N 或 null（null 同时表示无关联组合或仍为未核验，前端结合组合关系区分展示）
     */
    private String resolvePlanVerificationStatus(List<Long> combinationIds, Map<Long, String> statusMap) {
        if (combinationIds == null || combinationIds.isEmpty()) {
            return null;
        }
        boolean allSuccess = true;
        boolean anyFailed = false;
        for (Long combinationId : combinationIds) {
            String status = statusMap == null ? null : statusMap.get(combinationId);
            if (DortmundModuleConfig.Contract.VERIFICATION_FAILED.equals(status)) {
                anyFailed = true;
            }
            if (!DortmundModuleConfig.Contract.VERIFICATION_SUCCESS.equals(status)) {
                allSuccess = false;
            }
        }
        if (anyFailed) {
            return DortmundModuleConfig.Contract.VERIFICATION_FAILED;
        }
        return allSuccess ? DortmundModuleConfig.Contract.VERIFICATION_SUCCESS
                : DortmundModuleConfig.Contract.VERIFICATION_UNVERIFIED;
    }

    /**
     * 汇总并写入方案关联组合的核验数量和总体状态。
     * 成功、失败均以组合当前核验结论为准，其余情况（含状态缺失）统一计入未核验，
     * 保证前端三项统计之和始终等于方案实际关联组合数。
     *
     * @param plan            方案计划
     * @param combinationIds  关联组合主键集合
     * @param statusMap       组合核验状态映射
     */
    private void fillPlanCombinationVerification(Fx67llDortmundPlan plan, List<Long> combinationIds,
                                                 Map<Long, String> statusMap) {
        int successCount = 0;
        int failedCount = 0;
        int unverifiedCount = 0;
        if (combinationIds != null) {
            for (Long combinationId : combinationIds) {
                String status = statusMap == null ? null : statusMap.get(combinationId);
                if (DortmundModuleConfig.Contract.VERIFICATION_SUCCESS.equals(status)) {
                    successCount++;
                } else if (DortmundModuleConfig.Contract.VERIFICATION_FAILED.equals(status)) {
                    failedCount++;
                } else {
                    unverifiedCount++;
                }
            }
        }
        plan.setCombinationSuccessCount(successCount);
        plan.setCombinationFailedCount(failedCount);
        plan.setCombinationUnverifiedCount(unverifiedCount);
        plan.setCombinationVerificationStatus(resolvePlanVerificationStatus(combinationIds, statusMap));
    }

    /**
     * 删除前校验方案计划是否被外快盈亏或数值轮次关系引用
     *
     * @param planId 方案计划主键
     */
    private void checkPlanDeleteReference(Long planId) {
        Fx67llDortmundExtraPlanRel extraRelQuery = new Fx67llDortmundExtraPlanRel();
        extraRelQuery.setPlanId(planId);
        List<Fx67llDortmundExtraPlanRel> extraRelList = fx67llDortmundExtraPlanRelMapper.selectFx67llDortmundExtraPlanRelList(extraRelQuery);
        if (extraRelList != null && !extraRelList.isEmpty()) {
            throw new ServiceException("方案计划已被外快盈亏引用，不能删除；请在外快盈亏编辑中解除关联或删除该记录");
        }
        Fx67llDortmundRoundPlanRel roundRelQuery = new Fx67llDortmundRoundPlanRel();
        roundRelQuery.setPlanId(planId);
        List<Fx67llDortmundRoundPlanRel> roundRelList = fx67llDortmundRoundPlanRelMapper.selectFx67llDortmundRoundPlanRelList(roundRelQuery);
        if (roundRelList != null && !roundRelList.isEmpty()) {
            throw new ServiceException("方案计划已被数值轮次引用，不能删除；请作废所属版本或删除对应的上一轮");
        }
    }

    /**
     * 判断当前用户是否拥有"全部数据"权限角色
     *
     * @return 是否全部数据权限
     */
    private boolean hasAllDataScope() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (roles != null) {
            for (SysRole role : roles) {
                if ("1".equals(role.getDataScope())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验当前用户对方案计划的数据范围
     *
     * @param planUserId 方案计划归属用户ID
     * @return 是否可见
     */
    private boolean checkPlanDataScope(Long planUserId) {
        return hasAllDataScope() || SecurityUtils.getUserId().equals(planUserId);
    }

}
