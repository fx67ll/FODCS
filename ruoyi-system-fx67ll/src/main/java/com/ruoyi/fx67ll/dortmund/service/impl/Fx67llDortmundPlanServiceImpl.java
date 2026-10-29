package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundPlanMapper;

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
        fillPlanImageKeys(plan);
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
        if (!hasAllDataScope()) {
            fx67llDortmundPlan.setUserId(SecurityUtils.getUserId());
        }
        fx67llDortmundPlan.setDelFlag("0");
        List<Fx67llDortmundPlan> list = fx67llDortmundPlanMapper.selectFx67llDortmundPlanList(fx67llDortmundPlan);
        for (Fx67llDortmundPlan plan : list) {
            fillMatchesAndCount(plan);
            fillPlanImageKeys(plan);
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
        checkPlanFields(fx67llDortmundPlan);
        List<Fx67llDortmundBasicMatch> resolvedMatches = resolveMatches(fx67llDortmundPlan, null);
        List<String> imageKeys = resolvePlanImageKeys(fx67llDortmundPlan, null);
        fx67llDortmundPlan.setUserId(SecurityUtils.getUserId());
        fx67llDortmundPlan.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundPlan.setCreateTime(DateUtils.getNowDate());
        fx67llDortmundPlan.setDelFlag("0");
        String imageKeysStr = imageKeys == null || imageKeys.isEmpty() ? null : joinPlanImageKeys(imageKeys);
        fx67llDortmundPlan.setPlanImageObjectKeysStr(imageKeysStr);
        int rows = fx67llDortmundPlanMapper.insertFx67llDortmundPlan(fx67llDortmundPlan);
        if (rows > 0) {
            insertMatchRels(fx67llDortmundPlan.getPlanId(), resolvedMatches);
            consumeIssuedPlanImageKeys(imageKeys);
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
        List<String> currentImageKeys = splitPlanImageKeys(oldPlan.getPlanImageObjectKeysStr());
        List<String> imageKeys = resolvePlanImageKeys(fx67llDortmundPlan, currentImageKeys);
        fx67llDortmundPlan.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundPlan.setUpdateTime(DateUtils.getNowDate());
        fx67llDortmundPlan.setPlanImageObjectKeysStr(imageKeys == null ? null : joinPlanImageKeys(imageKeys));
        int rows = fx67llDortmundPlanMapper.updateFx67llDortmundPlan(fx67llDortmundPlan);
        if (rows > 0) {
            fx67llDortmundPlanMatchRelMapper.deleteFx67llDortmundPlanMatchRelByPlanId(fx67llDortmundPlan.getPlanId());
            insertMatchRels(fx67llDortmundPlan.getPlanId(), resolvedMatches);
            if (imageKeys != null) {
                consumeIssuedPlanImageKeys(imageKeys);
                List<String> removedImageKeys = new ArrayList<>();
                for (String currentImageKey : currentImageKeys) {
                    if (!imageKeys.contains(currentImageKey)) {
                        removedImageKeys.add(currentImageKey);
                    }
                }
                deletePlanImageFiles(removedImageKeys);
            }
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
            deletePlanImageFiles(splitPlanImageKeys(oldPlan.getPlanImageObjectKeysStr()));
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
    @Override
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
     * 校验方案计划基础字段
     *
     * @param fx67llDortmundPlan 方案计划
     */
    private void checkPlanFields(Fx67llDortmundPlan fx67llDortmundPlan) {
        if (fx67llDortmundPlan.getPlanContent() == null || fx67llDortmundPlan.getPlanContent().trim().isEmpty()) {
            throw new ServiceException("方案计划正文不能为空！");
        }
        if (fx67llDortmundPlan.getPlanContent().length() > CONTENT_MAX_LENGTH) {
            throw new ServiceException("方案计划正文不能超过 1023 个字符！");
        }
        if (fx67llDortmundPlan.getPlanRemark() != null && fx67llDortmundPlan.getPlanRemark().length() > REMARK_MAX_LENGTH) {
            throw new ServiceException("方案计划说明不能超过 1023 个字符！");
        }
        if (fx67llDortmundPlan.getPlanRate() != null && fx67llDortmundPlan.getPlanRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("方案计划倍率有值时必须大于 0！");
        }
        if (fx67llDortmundPlan.getPlanAmount() != null && fx67llDortmundPlan.getPlanAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("方案计划金额有值时必须大于等于 0！");
        }
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
