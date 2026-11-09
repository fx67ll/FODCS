package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
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
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMatchRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundRoundPlanRelMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanReturnRelMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnGenerationMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;

/**
 * 方案计划Service业务层处理
 *
 * @author fx67ll
 * @date 2026-10-29
 */
@Service
public class Fx67llDortmundPlanServiceImpl implements IFx67llDortmundPlanService {
    private static final Logger log = LoggerFactory.getLogger(Fx67llDortmundPlanServiceImpl.class);

    /**
     * 正文最大长度
     */
    private static final int CONTENT_MAX_LENGTH = 1023;

    /**
     * 说明最大长度
     */
    private static final int REMARK_MAX_LENGTH = 1023;

    /**
     * 图片对象键签发记录的 Redis 键前缀，完整键为 前缀 + 用户ID + 对象键
     */
    private static final String PLAN_IMAGE_ISSUED_KEY_PREFIX = "dortmund:plan:image:issued:";

    /**
     * 图片对象键签发记录的过期时间（小时）
     */
    private static final int PLAN_IMAGE_ISSUED_TTL_HOURS = 24;

    /**
     * 单条方案计划允许保存的图片对象键数量上限
     */
    private static final int PLAN_IMAGE_MAX_COUNT = 2;

    /**
     * 单个图片对象键的最大长度
     */
    private static final int PLAN_IMAGE_KEY_MAX_LENGTH = 1023;

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
    private RedisCache redisCache;

    /**
     * 查询方案计划
     * <p>
     * 仅返回存在、未删除且在当前用户数据范围内的方案计划，其余情况一律返回 null
     *
     * @param planId 方案计划主键
     * @return 方案计划
     */
    @Override
    public Fx67llDortmundPlan selectFx67llDortmundPlanByPlanId(Long planId) {
        Fx67llDortmundPlan plan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(planId);
        if (plan == null || !"0".equals(plan.getDelFlag()) || !checkPlanDataScope(plan.getUserId())) {
            return null;
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
        for (Fx67llDortmundPlan plan : list) {
            fillMatchesAndCount(plan);
            fillPhase15Fields(plan);
        }
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
     * 上传方案计划图片
     * <p>
     * 复用 RuoYi 现有上传实现（随机文件名、现有存储路径、大小/扩展名/MIME/文件头校验），返回相对对象键；
     * 上传成功后写入 24 小时签发记录，保存方案计划时校验归属并一次性消费
     *
     * @param file 上传的图片文件
     * @return 相对对象键
     */
    public String uploadPlanImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("上传图片不能为空！");
        }
        String objectKey = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
        redisCache.setCacheObject(PLAN_IMAGE_ISSUED_KEY_PREFIX + SecurityUtils.getUserId() + ":" + objectKey,
                SecurityUtils.getUsername(), PLAN_IMAGE_ISSUED_TTL_HOURS, TimeUnit.HOURS);
        return objectKey;
    }

    /**
     * 查询可关联的已采用回报组合
     *
     * @return 已采用回报组合集合
     */
    @Override
    public List<Fx67llDortmundReturnCombination> selectAdoptedReturnCombinationOptions() {
        Fx67llDortmundReturnCombination query = new Fx67llDortmundReturnCombination();
        query.setAdoptedFlag("Y");
        return fx67llDortmundReturnCombinationService.selectCombinationList(query);
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
        Fx67llDortmundPlan plan = loadPlanForPhase15(planId);
        String content = formatPlanSummary(plan, loadPlanCombinations(planId));
        plan.setSummaryContent(content);
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
        if (fx67llDortmundPlan.getPlanContent() == null || fx67llDortmundPlan.getPlanContent().trim().isEmpty()) {
            throw new ServiceException("方案计划正文不能为空！");
        }
        if (fx67llDortmundPlan.getPlanContent().length() > CONTENT_MAX_LENGTH) {
            throw new ServiceException("方案计划正文不能超过1023个字符！");
        }
        if (fx67llDortmundPlan.getPlanRemark() != null && fx67llDortmundPlan.getPlanRemark().length() > REMARK_MAX_LENGTH) {
            throw new ServiceException("方案计划说明不能超过1023个字符！");
        }
        if (fx67llDortmundPlan.getPlanRate() != null && fx67llDortmundPlan.getPlanRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("方案计划倍率有值时必须大于0！");
        }
        if (fx67llDortmundPlan.getPlanAmount() != null && fx67llDortmundPlan.getPlanAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("方案计划金额有值时必须大于等于0！");
        }
    }

    private int insertPhase15Plan(Fx67llDortmundPlan plan) {
        checkPlanFields(plan);
        PlanCombinationStats stats = collectCombinationStats(plan.getCombinationIds(), null);
        Date now = DateUtils.getNowDate();
        plan.setUserId(SecurityUtils.getUserId());
        plan.setCreateBy(SecurityUtils.getUsername());
        plan.setCreateTime(now);
        plan.setDelFlag("0");
        plan.setCalculatedPlanRate(stats.rate);
        plan.setCalculatedPlanAmount(stats.amount);
        plan.setSummaryUpdateFlag("Y");
        int rows = fx67llDortmundPlanMapper.insertFx67llDortmundPlan(plan);
        if (rows > 0) {
            replacePhase15Relations(plan.getPlanId(), plan.getCombinationIds(), stats.matchIds);
        }
        return rows;
    }

    private int updatePhase15Plan(Fx67llDortmundPlan plan) {
        if (plan.getPlanId() == null) {
            throw new ServiceException("方案计划主键不能为空！");
        }
        Fx67llDortmundPlan oldPlan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(plan.getPlanId());
        if (oldPlan == null || !"0".equals(oldPlan.getDelFlag()) || !checkPlanDataScope(oldPlan.getUserId())) {
            throw new ServiceException("方案计划不存在或没有访问权限！");
        }
        checkPlanFields(plan);
        PlanCombinationStats stats = collectCombinationStats(plan.getCombinationIds(), plan.getPlanId());
        if (!sameIds(selectPlanCombinationIds(plan.getPlanId()), plan.getCombinationIds())) {
            plan.setPlanRate(null);
            plan.setPlanAmount(null);
        }
        plan.setCalculatedPlanRate(stats.rate);
        plan.setCalculatedPlanAmount(stats.amount);
        plan.setSummaryUpdateFlag("Y");
        plan.setUpdateBy(SecurityUtils.getUsername());
        plan.setUpdateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundPlanMapper.updateFx67llDortmundPlanPhase15(plan);
        if (rows > 0) {
            replacePhase15Relations(plan.getPlanId(), plan.getCombinationIds(), stats.matchIds);
        }
        return rows;
    }

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
        stats.amount = totalReturn.setScale(2, RoundingMode.HALF_UP);
        if (totalInput.signum() > 0) {
            stats.rate = totalReturn.divide(totalInput, 4, RoundingMode.HALF_UP);
        }
        return stats;
    }

    private void replacePhase15Relations(Long planId, List<Long> combinationIds, LinkedHashSet<Long> matchIds) {
        fx67llDortmundPlanReturnRelMapper.deleteByPlanId(planId);
        fx67llDortmundPlanMatchRelMapper.deleteFx67llDortmundPlanMatchRelByPlanId(planId);
        int sort = 0;
        try {
            for (Long combinationId : combinationIds) {
                Fx67llDortmundPlanReturnRel relation = new Fx67llDortmundPlanReturnRel();
                relation.setPlanId(planId);
                relation.setCombinationId(combinationId);
                relation.setRelSort(sort++);
                relation.setCreateBy(SecurityUtils.getUsername());
                relation.setCreateTime(DateUtils.getNowDate());
                fx67llDortmundPlanReturnRelMapper.insertFx67llDortmundPlanReturnRel(relation);
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
        Fx67llDortmundPlan plan = fx67llDortmundPlanMapper.selectFx67llDortmundPlanByPlanId(planId);
        if (plan == null || !"0".equals(plan.getDelFlag()) || !checkPlanDataScope(plan.getUserId())) {
            throw new ServiceException("方案计划不存在或没有访问权限！");
        }
        fillPhase15Fields(plan);
        return plan;
    }

    private void fillPhase15Fields(Fx67llDortmundPlan plan) {
        plan.setCombinationIds(selectPlanCombinationIds(plan.getPlanId()));
        plan.setEffectivePlanRate(plan.getPlanRate() == null ? plan.getCalculatedPlanRate() : plan.getPlanRate());
        plan.setEffectivePlanAmount(plan.getPlanAmount() == null ? plan.getCalculatedPlanAmount() : plan.getPlanAmount());
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
        builder.append("有效倍率：").append(plan.getEffectivePlanRate()).append('\n');
        builder.append("有效金额：").append(plan.getEffectivePlanAmount()).append('\n');
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

    private BigDecimal defaultAmount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static class PlanCombinationStats {
        private BigDecimal rate;
        private BigDecimal amount;
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
     * 解析方案计划请求中的图片对象键集合
     * <p>
     * 不传（null）表示图片保持不变；无上传权限时非空提交整体拒绝、空提交视为未提交；
     * 每个对象键必须在当前计划已保存的集合中或命中当前用户的签发记录，去重后最多两个
     *
     * @param fx67llDortmundPlan 方案计划
     * @param currentKeys        当前计划已保存的图片对象键集合（新增时为空）
     * @return 解析后的图片对象键集合，null 表示请求未提交图片
     */
    private List<String> resolvePlanImageKeys(Fx67llDortmundPlan fx67llDortmundPlan, List<String> currentKeys) {
        String[] requestKeys = fx67llDortmundPlan.getPlanImageObjectKeys();
        if (!hasPermission("dortmund:plan:image:upload")) {
            if (requestKeys != null && requestKeys.length > 0) {
                throw new ServiceException("没有方案计划图片上传权限，不能提交图片对象键！");
            }
            return null;
        }
        if (requestKeys == null) {
            return null;
        }
        LinkedHashSet<String> keySet = new LinkedHashSet<>();
        for (String requestKey : requestKeys) {
            String objectKey = requestKey == null ? "" : requestKey.trim();
            if (objectKey.isEmpty()) {
                throw new ServiceException("图片对象键不能为空！");
            }
            if (objectKey.length() > PLAN_IMAGE_KEY_MAX_LENGTH) {
                throw new ServiceException("单个图片对象键长度不能超过 1023 个字符！");
            }
            if (!objectKey.startsWith(Constants.RESOURCE_PREFIX + "/")) {
                throw new ServiceException("图片对象键格式非法！");
            }
            keySet.add(objectKey);
        }
        if (keySet.size() > PLAN_IMAGE_MAX_COUNT) {
            throw new ServiceException("方案计划最多只能保存 2 个图片对象键！");
        }
        List<String> imageKeys = new ArrayList<>(keySet);
        for (String objectKey : imageKeys) {
            boolean onCurrentPlan = currentKeys != null && currentKeys.contains(objectKey);
            String issuedKey = PLAN_IMAGE_ISSUED_KEY_PREFIX + SecurityUtils.getUserId() + ":" + objectKey;
            if (!onCurrentPlan && redisCache.getCacheObject(issuedKey) == null) {
                throw new ServiceException("图片对象键未上传或已失效，请重新上传！");
            }
        }
        return imageKeys;
    }

    /**
     * 拼接图片对象键为逗号分隔串（空集合拼接为空串，用于清空表字段）
     *
     * @param imageKeys 图片对象键集合
     * @return 逗号分隔串
     */
    private String joinPlanImageKeys(List<String> imageKeys) {
        return String.join(",", imageKeys);
    }

    /**
     * 拆分表字段中的图片对象键逗号分隔串
     *
     * @param keysStr 图片对象键逗号分隔串
     * @return 图片对象键集合
     */
    private List<String> splitPlanImageKeys(String keysStr) {
        List<String> imageKeys = new ArrayList<>();
        if (keysStr != null && !keysStr.trim().isEmpty()) {
            for (String key : keysStr.split(",")) {
                String objectKey = key.trim();
                if (!objectKey.isEmpty()) {
                    imageKeys.add(objectKey);
                }
            }
        }
        return imageKeys;
    }

    /**
     * 保存成功后一次性消费图片对象键的签发记录
     *
     * @param imageKeys 本次保存落库的图片对象键集合
     */
    private void consumeIssuedPlanImageKeys(List<String> imageKeys) {
        if (imageKeys == null || imageKeys.isEmpty()) {
            return;
        }
        for (String objectKey : imageKeys) {
            redisCache.deleteObject(PLAN_IMAGE_ISSUED_KEY_PREFIX + SecurityUtils.getUserId() + ":" + objectKey);
        }
    }

    /**
     * 删除已落库的图片对象键对应的本地文件
     * <p>
     * 删除失败仅记录待清理日志，不影响业务事务
     *
     * @param imageKeys 待删除文件对应的图片对象键集合
     */
    private void deletePlanImageFiles(List<String> imageKeys) {
        if (imageKeys == null || imageKeys.isEmpty()) {
            return;
        }
        for (String objectKey : imageKeys) {
            try {
                String localPath = RuoYiConfig.getProfile() + objectKey.substring(Constants.RESOURCE_PREFIX.length());
                if (!FileUtils.deleteFile(localPath)) {
                    log.warn("方案计划图片文件待清理（文件不存在或删除失败）：{}", localPath);
                }
            } catch (Exception e) {
                log.warn("方案计划图片文件删除异常，待清理：" + objectKey, e);
            }
        }
    }

    /**
     * 按权限填充方案计划的图片对象键数组
     * <p>
     * 仅有图片查询权限的用户能拿到对象键，其余用户数组与导出字段均置空
     *
     * @param plan 方案计划
     */
    private void fillPlanImageKeys(Fx67llDortmundPlan plan) {
        if (hasPermission("dortmund:plan:image:query")) {
            plan.setPlanImageObjectKeys(splitPlanImageKeys(plan.getPlanImageObjectKeysStr()).toArray(new String[0]));
        } else {
            plan.setPlanImageObjectKeys(null);
            plan.setPlanImageObjectKeysStr(null);
        }
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

    /**
     * 判断当前用户是否拥有指定功能权限
     *
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    private boolean hasPermission(String permission) {
        return SecurityUtils.getLoginUser().getPermissions().contains(permission)
                || SecurityUtils.getLoginUser().getPermissions().contains("*:*:*");
    }
}
