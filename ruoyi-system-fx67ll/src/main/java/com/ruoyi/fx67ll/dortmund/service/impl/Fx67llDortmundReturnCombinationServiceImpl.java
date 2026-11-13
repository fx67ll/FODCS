package com.ruoyi.fx67ll.dortmund.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.fx67ll.dortmund.ai.ReturnCombinationAiAnalysisPort;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationItem;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImageCleanupResult;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationItemMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnGenerationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnImageMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundOddsService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;

/**
 * Dortmund回报组合Service业务层处理
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@Service
public class Fx67llDortmundReturnCombinationServiceImpl implements IFx67llDortmundReturnCombinationService {
    private static final Logger log = LoggerFactory.getLogger(Fx67llDortmundReturnCombinationServiceImpl.class);

    private static final String STATUS_PROCESSING = "0";
    private static final String STATUS_SUCCESS = "1";
    private static final String STATUS_FAILED = "2";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String IMAGE_TEMP = "T";
    private static final String IMAGE_BOUND = "B";
    private static final String IMAGE_DELETE_PENDING = "D";
    private static final String IMAGE_QUARANTINED = "Q";
    private static final BigDecimal MAX_BUSINESS_AMOUNT = new BigDecimal("999999999999.99");
    private static final BigDecimal MAX_COMBINATION_ODDS = new BigDecimal("999999999999.999999");
    private static final Set<String> SORT_FIELDS = new HashSet<>();
    private static final Set<String> SELECTION_TYPES = new HashSet<>();
    static {
        Collections.addAll(SELECTION_TYPES, "HW", "DR", "AW", "HH", "HD", "HA");
        Collections.addAll(SORT_FIELDS, "combinedOdds", "estimatedReturn", "deviationAmount",
                "matchCount", "reviewTime", "createTime");
    }

    @Autowired
    private Fx67llDortmundReturnGenerationMapper fx67llDortmundReturnGenerationMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationMapper fx67llDortmundReturnCombinationMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationItemMapper fx67llDortmundReturnCombinationItemMapper;

    @Autowired
    private Fx67llDortmundReturnImageMapper fx67llDortmundReturnImageMapper;

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    @Autowired
    private IFx67llDortmundOddsService fx67llDortmundOddsService;

    @Autowired
    private ReturnCombinationAiAnalysisPort returnCombinationAiAnalysisPort;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Value("${dortmund.return.generation.lease-seconds:300}")
    private int generationLeaseSeconds;

    @Value("${dortmund.return.generation.max-attempts:3}")
    private int generationMaxAttempts;

    @Value("${dortmund.return.generation.dispatch-batch-size:5}")
    private int generationDispatchBatchSize;

    @Value("${dortmund.return.generation.retry-delay-seconds:60}")
    private int generationRetryDelaySeconds;

    @Value("${dortmund.return.image.temp-hours:24}")
    private int imageTempHours;

    @Value("${dortmund.return.image.cleanup-batch-size:50}")
    private int imageCleanupBatchSize;

    @Value("${dortmund.return.image.delete-retry-limit:5}")
    private int imageDeleteRetryLimit;

    private final String workerId = "return-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

    /**
     * 生成回报组合
     *
     * @param request 回报组合生成请求
     * @return 已受理的生成批次；候选组合由异步worker落库后通过批次详情查询
     */
    @Override
    public Fx67llDortmundReturnGeneration generate(Fx67llDortmundReturnGenerateRequest request) {
        validateRequest(request);
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        if (!blank(request.getClientRequestId())) {
            Fx67llDortmundReturnGeneration existing = fx67llDortmundReturnGenerationMapper
                    .selectByClientRequestId(userId, request.getClientRequestId());
            if (existing != null) {
                existing.setRequestSnapshot(null);
                existing.setCombinations(new ArrayList<>());
                return existing;
            }
        }
        List<Fx67llDortmundOdds> oddsList = loadRequestOdds(request.getMatchIds());
        Fx67llDortmundReturnGeneration generation = new Fx67llDortmundReturnGeneration();
        Date now = DateUtils.getNowDate();
        generation.setGenerationCode(createCode("G"));
        generation.setGenerationMode(request.getGenerationMode());
        generation.setInputAmount(request.getInputAmount());
        generation.setTargetAmount(request.getTargetAmount());
        generation.setGenerationStatus(STATUS_PROCESSING);
        generation.setGenerationRemark("异步分析已受理，等待worker处理。");
        generation.setGeneratedCount(0);
        generation.setStartedTime(now);
        generation.setUserId(userId);
        generation.setClientRequestId(blank(request.getClientRequestId())
                ? UUID.randomUUID().toString().replace("-", "") : limit(request.getClientRequestId(), 64));
        generation.setAttemptCount(0);
        generation.setMaxAttempts(generationMaxAttempts);
        generation.setNextRetryTime(now);
        generation.setCreateBy(username);
        generation.setUpdateBy(username);
        generation.setCreateTime(now);
        generation.setUpdateTime(now);
        generation.setRequestSnapshot(buildSnapshot(request, oddsList));
        try {
            fx67llDortmundReturnGenerationMapper.insertFx67llDortmundReturnGeneration(generation);
            generation.setRequestSnapshot(null);
            generation.setCombinations(new ArrayList<>());
            return generation;
        } catch (DuplicateKeyException e) {
            Fx67llDortmundReturnGeneration existing = fx67llDortmundReturnGenerationMapper
                    .selectByClientRequestId(userId, generation.getClientRequestId());
            if (existing != null) {
                existing.setRequestSnapshot(null);
                existing.setCombinations(new ArrayList<>());
                return existing;
            }
            throw new ServiceException("回报组合生成请求重复，请刷新后重试！");
        } catch (Exception e) {
            log.error("受理回报组合异步生成失败", e);
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException("回报组合生成请求受理失败，请稍后重试！");
        }
    }

    /**
     * 异步生成调度器。
     * <p>
     * MySQL 5.7没有SKIP LOCKED，因此先查候选再以条件UPDATE原子领取；多节点同时扫描时，
     * 只有成功把worker_id和lease_expire_time写入的节点可以执行。
     */
    @Override
    @Scheduled(fixedDelayString = "${dortmund.return.generation.dispatch-interval-ms:15000}",
            initialDelayString = "${dortmund.return.generation.dispatch-initial-delay-ms:5000}")
    public void dispatchPendingGenerations() {
        List<Long> pendingIds;
        try {
            pendingIds = fx67llDortmundReturnGenerationMapper.selectPendingGenerationIds(
                    Math.max(generationDispatchBatchSize, 1));
        } catch (Exception e) {
            log.error("查询待执行回报组合批次失败", e);
            return;
        }
        for (Long generationId : pendingIds) {
            try {
                int claimed = fx67llDortmundReturnGenerationMapper.claimGeneration(generationId, workerId,
                        Math.max(generationLeaseSeconds, 30), Math.max(generationMaxAttempts, 1));
                if (claimed == 1) {
                    processClaimedGeneration(generationId);
                } else {
                    int exhausted = fx67llDortmundReturnGenerationMapper.failExhaustedGeneration(generationId,
                            "异步生成租约已过期且达到最大尝试次数，系统已终止本次批次。");
                }
            } catch (Exception e) {
                log.error("领取或执行回报组合批次失败，生成批次ID：{}", generationId, e);
            }
        }
    }

    private void processClaimedGeneration(Long generationId) {
        Fx67llDortmundReturnGeneration generation = fx67llDortmundReturnGenerationMapper
                .selectFx67llDortmundReturnGenerationByGenerationId(generationId);
        if (generation == null || !STATUS_PROCESSING.equals(generation.getGenerationStatus())
                || !workerId.equals(generation.getWorkerId())) {
            return;
        }
        try {
            GenerationSnapshot snapshot = parseGenerationSnapshot(generation.getRequestSnapshot());
            Fx67llDortmundReturnGenerateRequest request = snapshot.request;
            List<Fx67llDortmundOdds> oddsList = snapshot.odds;
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput aiInput =
                    new ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput();
            aiInput.setGenerationMode(request.getGenerationMode());
            aiInput.setInputAmount(request.getInputAmount());
            aiInput.setTargetAmount(request.getTargetAmount());
            aiInput.setOdds(buildAiOddsList(oddsList, request.getGenerationMode()));
            aiInput.setBusinessObjectives(buildBusinessObjectives(request));
            List<PreparedCombination> prepared = prepareCombinations(
                    returnCombinationAiAnalysisPort.analyze(aiInput), request, oddsList);
            if (prepared.isEmpty()) {
                throw new ServiceException("AI未生成可用的回报组合！");
            }
            persistSuccess(generation, prepared);
        } catch (Exception e) {
            handleGenerationFailure(generation, e);
        }
    }

    private GenerationSnapshot parseGenerationSnapshot(String snapshot) {
        if (blank(snapshot)) {
            throw new ServiceException("生成批次缺少业务输入快照！");
        }
        JSONObject value = JSONObject.parseObject(snapshot);
        Fx67llDortmundReturnGenerateRequest request = new Fx67llDortmundReturnGenerateRequest();
        request.setGenerationMode(value.getString("generationMode"));
        request.setInputAmount(value.getBigDecimal("inputAmount"));
        request.setTargetAmount(value.getBigDecimal("targetAmount"));
        request.setMatchIds(value.getJSONArray("matchIds").toJavaList(Long.class));
        validateRequest(request);
        JSONArray oddsValues = value.getJSONArray("odds");
        if (oddsValues == null || oddsValues.isEmpty()) {
            throw new ServiceException("生成批次缺少提交时的odds快照！");
        }
        List<Fx67llDortmundOdds> oddsList = oddsValues.toJavaList(Fx67llDortmundOdds.class);
        Set<Long> requestedMatchIds = new HashSet<>(request.getMatchIds());
        Set<Long> snapshotMatchIds = new HashSet<>();
        for (Fx67llDortmundOdds odds : oddsList) {
            if (odds == null || odds.getMatchId() == null || !requestedMatchIds.contains(odds.getMatchId())
                    || !snapshotMatchIds.add(odds.getMatchId())) {
                throw new ServiceException("生成批次odds快照与比赛列表不一致！");
            }
        }
        if (snapshotMatchIds.size() != requestedMatchIds.size()) {
            throw new ServiceException("生成批次odds快照缺少比赛！");
        }
        return new GenerationSnapshot(request, oddsList);
    }

    private List<String> buildBusinessObjectives(Fx67llDortmundReturnGenerateRequest request) {
        List<String> objectives = new ArrayList<>();
        objectives.add("仅从输入odds中给出的比赛和选择类型生成候选，不得虚构比赛、赔率或选择类型。");
        objectives.add("每条候选必须覆盖1至8场不重复比赛，候选之间要去重；避免对同一风险重复下注。");
        objectives.add("预计回报应尽量接近目标金额，但不得为了贴近目标而违反赔率快照或选择约束。");
        objectives.add("结合matchSummary中的球队、开赛时间，以及独立字段中的让球值和有效odds判断合理性，并输出简短中文理由。");
        if ("S".equals(request.getGenerationMode())) {
            objectives.add("Subjective Mode必须尊重输入中的手工权重：高权重代表用户更认可的选择，不得改写成AI权重。");
        } else {
            objectives.add("AI Mode由AI为每个选择给出0至100的appliedWeight；该值只表示评估权重，不是金额或投注比例。");
        }
        objectives.add("appliedWeight只用于说明AI评估倾向，禁止用它计算组合倍率、预计回报、偏差或任何金额。");
        return objectives;
    }

    private void handleGenerationFailure(Fx67llDortmundReturnGeneration generation, Exception error) {
        String message = error == null || blank(error.getMessage())
                ? "回报组合异步生成失败，请稍后重试！" : limit(error.getMessage(), 900);
        int attempt = generation.getAttemptCount() == null ? 1 : generation.getAttemptCount();
        int maxAttempts = generation.getMaxAttempts() == null ? generationMaxAttempts : generation.getMaxAttempts();
        try {
            if (attempt < maxAttempts) {
                String remark = "第" + attempt + "次执行失败，系统将自动重试：" + message;
                fx67llDortmundReturnGenerationMapper.releaseGenerationForRetry(generation.getGenerationId(), workerId,
                        limit(remark, 1023), Math.max(generationRetryDelaySeconds, 1), generation.getCreateBy());
                return;
            }
            String remark = "异步生成最终失败，已保留受控原因：" + message;
            fx67llDortmundReturnGenerationMapper.failGeneration(generation.getGenerationId(), workerId,
                    limit(remark, 1023), generation.getCreateBy());
            log.error("回报组合异步生成最终失败，生成批次ID：{}，尝试次数：{}", generation.getGenerationId(), attempt,
                    error);
        } catch (Exception updateError) {
            log.error("更新回报组合异步批次失败状态异常，生成批次ID：{}", generation.getGenerationId(), updateError);
        }
    }

    /**
     * 查询回报组合生成批次列表
     *
     * @param query 生成批次查询条件
     * @return 生成批次集合
     */
    @Override
    public List<Fx67llDortmundReturnGeneration> selectGenerationList(Fx67llDortmundReturnGeneration query) {
        if (query == null) {
            query = new Fx67llDortmundReturnGeneration();
        }
        query.setBeginCreateTime(normalizeDateBoundary(query.getBeginCreateTime(), true));
        query.setEndCreateTime(normalizeDateBoundary(query.getEndCreateTime(), false));
        if (!hasAllDataScope()) {
            query.setUserId(SecurityUtils.getUserId());
        }
        List<Fx67llDortmundReturnGeneration> result = fx67llDortmundReturnGenerationMapper
                .selectFx67llDortmundReturnGenerationList(query);
        for (Fx67llDortmundReturnGeneration value : result) {
            value.setRequestSnapshot(null);
        }
        return result;
    }

    /**
     * 查询回报组合生成批次详情
     *
     * @param generationId 生成批次主键
     * @return 生成批次详情
     */
    @Override
    public Fx67llDortmundReturnGeneration selectGenerationById(Long generationId, String sortField, String sortOrder) {
        Fx67llDortmundReturnGeneration value = fx67llDortmundReturnGenerationMapper
                .selectFx67llDortmundReturnGenerationByGenerationId(generationId);
        if (value == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(value.getUserId()))) {
            return null;
        }
        value.setCombinations(selectCombinationListByGeneration(generationId, sortField, sortOrder));
        return value;
    }

    /**
     * 查询回报组合列表
     *
     * @param query 回报组合查询条件
     * @return 回报组合集合
     */
    @Override
    public List<Fx67llDortmundReturnCombination> selectCombinationList(Fx67llDortmundReturnCombination query) {
        if (query == null) {
            query = new Fx67llDortmundReturnCombination();
        }
        query.setBeginCreateTime(normalizeDateBoundary(query.getBeginCreateTime(), true));
        query.setEndCreateTime(normalizeDateBoundary(query.getEndCreateTime(), false));
        normalizeSort(query);
        if (!hasAllDataScope()) {
            query.setUserId(SecurityUtils.getUserId());
        }
        List<Fx67llDortmundReturnCombination> result = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationList(query);
        for (Fx67llDortmundReturnCombination value : result) {
            value.setCombinationRemark(null);
            value.setMatches(null);
            value.setImages(null);
        }
        return result;
    }

    /**
     * 查询回报组合详情
     *
     * @param combinationId 回报组合主键
     * @return 回报组合详情
     */
    @Override
    public Fx67llDortmundReturnCombination selectCombinationById(Long combinationId) {
        Fx67llDortmundReturnCombination value = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationByCombinationId(combinationId);
        if (value == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(value.getUserId()))) {
            return null;
        }
        value.setPlanLinked(
                fx67llDortmundReturnCombinationMapper.countPlanRelationsByCombinationId(combinationId) > 0);
        if (hasPermission("dortmund:return:image:query")) {
            value.setImages(fx67llDortmundReturnImageMapper.selectActiveImagesByCombinationId(combinationId));
        } else {
            value.setImages(null);
        }
        List<Fx67llDortmundReturnCombinationItem> items = fx67llDortmundReturnCombinationItemMapper
                .selectItemsByCombinationId(combinationId);
        value.setMatches(items);
        if (!hasPermission("dortmund:return:remark:query")) {
            value.setCombinationRemark(null);
            if (value.getMatches() != null) {
                for (Fx67llDortmundReturnCombinationItem item : value.getMatches()) {
                    item.setSelectionReason(null);
                }
            }
        } else {
            fillSelectionReasons(combinationId, value.getCombinationRemark(), items);
        }
        return value;
    }

    private void normalizeSort(Fx67llDortmundReturnCombination query) {
        if (blank(query.getSortField()) || !SORT_FIELDS.contains(query.getSortField())) {
            query.setSortField("createTime");
        }
        if (!"asc".equalsIgnoreCase(query.getSortOrder())) {
            query.setSortOrder("desc");
        } else {
            query.setSortOrder("asc");
        }
    }

    /**
     * 修改回报组合采用状态
     *
     * @param combinationId 回报组合主键
     * @param adoptedFlag 是否采用
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAdoptedFlag(Long combinationId, String adoptedFlag) {
        Fx67llDortmundReturnCombination value = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(combinationId);
        if (value == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(value.getUserId()))) {
            throw new ServiceException("回报组合不存在或没有访问权限！");
        }
        if (!YES.equals(adoptedFlag) && !NO.equals(adoptedFlag)) {
            throw new ServiceException("回报组合采用状态不正确！");
        }
        if (NO.equals(adoptedFlag)
                && fx67llDortmundReturnCombinationMapper.countPlanRelationsByCombinationId(combinationId) > 0) {
            throw new ServiceException("回报组合已被方案计划引用，不能改为不采用！");
        }
        if (NO.equals(adoptedFlag)
                && fx67llDortmundReturnImageMapper.countActiveImagesByCombinationId(combinationId) > 0) {
            throw new ServiceException("Combination images must be removed before unadopting.");
        }
        value.setAdoptedFlag(adoptedFlag);
        value.setReviewBy(SecurityUtils.getUsername());
        value.setReviewTime(DateUtils.getNowDate());
        value.setUpdateBy(SecurityUtils.getUsername());
        value.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundReturnCombinationMapper.updateAdoptedFlag(value);
    }

    /**
     * 批量采用回报组合。
     * <p>
     * 所有组合必须在同一事务内通过数据范围校验，避免部分成功造成前端勾选状态与数据库不一致。
     *
     * @param combinationIds 组合主键集合
     * @return 实际由未采用改为已采用的条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAdopt(List<Long> combinationIds) {
        if (combinationIds == null || combinationIds.isEmpty()) {
            throw new ServiceException("请至少选择一条回报组合！");
        }
        LinkedHashSet<Long> uniqueIds = new LinkedHashSet<>(combinationIds);
        if (uniqueIds.size() != combinationIds.size() || uniqueIds.size() > 100 || uniqueIds.contains(null)) {
            throw new ServiceException("批量采用最多支持100条不重复的回报组合！");
        }
        Date now = DateUtils.getNowDate();
        String username = SecurityUtils.getUsername();
        int updated = 0;
        List<Long> orderedIds = new ArrayList<>(uniqueIds);
        Collections.sort(orderedIds);
        for (Long combinationId : orderedIds) {
            Fx67llDortmundReturnCombination value = fx67llDortmundReturnCombinationMapper
                    .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(combinationId);
            if (value == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(value.getUserId()))) {
                throw new ServiceException("回报组合不存在或没有访问权限！");
            }
            if (YES.equals(value.getAdoptedFlag())) {
                continue;
            }
            value.setAdoptedFlag(YES);
            value.setReviewBy(username);
            value.setReviewTime(now);
            value.setUpdateBy(username);
            value.setUpdateTime(now);
            updated += fx67llDortmundReturnCombinationMapper.updateAdoptedFlag(value);
        }
        return updated;
    }

    /**
     * 为已采用回报组合上传图片。
     *
     * @param combinationId 回报组合主键
     * @param file          上传的图片文件
     * @return 已绑定图片登记
     */
    @Override
    public Fx67llDortmundReturnImage uploadCombinationImage(Long combinationId, MultipartFile file)
            throws Exception {
        if (combinationId == null) {
            throw new ServiceException("回报组合主键不能为空！");
        }
        if (file == null || file.isEmpty()) {
            throw new ServiceException("上传图片不能为空！");
        }
        Fx67llDortmundReturnCombination initial = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationByCombinationId(combinationId);
        assertImageMutable(initial, combinationId);

        String objectKey = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
        String username = SecurityUtils.getUsername();
        Date now = DateUtils.getNowDate();
        Fx67llDortmundReturnImage image = new Fx67llDortmundReturnImage();
        image.setObjectKey(objectKey);
        image.setOriginalName(limit(file.getOriginalFilename(), 255));
        image.setContentType(limit(file.getContentType(), 128));
        image.setFileSize(file.getSize());
        image.setUserId(initial.getUserId());
        image.setImageState(IMAGE_TEMP);
        image.setExpireTime(new Date(now.getTime() + Math.max(imageTempHours, 1) * 3600_000L));
        image.setDeleteAttempts(0);
        image.setCreateBy(username);
        image.setCreateTime(now);
        image.setUpdateBy(username);
        image.setUpdateTime(now);
        try {
            fx67llDortmundReturnImageMapper.insertImage(image);
        } catch (Exception e) {
            deleteImageFileQuietly(objectKey);
            log.error("登记回报组合图片失败，已回滚磁盘文件，对象键：{}", objectKey, e);
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException("回报组合图片登记失败，请重新上传！");
        }

        try {
            TransactionTemplate transaction = new TransactionTemplate(transactionManager);
            Boolean bound = transaction.execute(status -> {
                Fx67llDortmundReturnCombination locked = fx67llDortmundReturnCombinationMapper
                        .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(combinationId);
                assertImageMutable(locked, combinationId);
                int activeCount = fx67llDortmundReturnImageMapper
                        .countActiveImagesByCombinationId(combinationId);
                if (activeCount >= 3) {
                    throw new ServiceException("每条回报组合最多只能保留三张有效图片！");
                }
                int rows = fx67llDortmundReturnImageMapper.bindToCombination(
                        image.getImageId(), combinationId, locked.getUserId());
                if (rows != 1) {
                    throw new ServiceException("回报组合图片绑定失败，请刷新后重试！");
                }
                return Boolean.TRUE;
            });
            if (!Boolean.TRUE.equals(bound)) {
                throw new ServiceException("回报组合图片绑定失败，请刷新后重试！");
            }
        } catch (Exception e) {
            compensateFailedUpload(image.getImageId());
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException("回报组合图片绑定失败，请刷新后重试！");
        }

        image.setCombinationId(combinationId);
        image.setImageState(IMAGE_BOUND);
        image.setExpireTime(null);
        image.setNextRetryTime(null);
        return image;
    }

    /**
     * 删除已采用回报组合的图片。登记先转待删除，文件删除成功后移除登记；文件失败保留重试记录。
     *
     * @param combinationId 回报组合主键
     * @param imageId       图片登记主键
     * @return 结果
     */
    @Override
    public int removeCombinationImage(Long combinationId, Long imageId) {
        if (combinationId == null || imageId == null) {
            throw new ServiceException("回报组合主键和图片主键不能为空！");
        }
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        Fx67llDortmundReturnImage image = transaction.execute(status -> {
            Fx67llDortmundReturnCombination locked = fx67llDortmundReturnCombinationMapper
                    .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(combinationId);
            assertImageMutable(locked, combinationId);
            Fx67llDortmundReturnImage lockedImage = fx67llDortmundReturnImageMapper
                    .selectByImageIdForUpdate(imageId);
            if (lockedImage == null || !combinationId.equals(lockedImage.getCombinationId())
                    || !IMAGE_BOUND.equals(lockedImage.getImageState())
                    || !locked.getUserId().equals(lockedImage.getUserId())) {
                throw new ServiceException("回报组合图片不存在、已删除或不属于当前组合！");
            }
            if (fx67llDortmundReturnImageMapper.markDeletePending(
                    imageId, "用户删除回报组合图片") != 1) {
                throw new ServiceException("回报组合图片状态已变化，请刷新后重试！");
            }
            return lockedImage;
        });
        if (image == null) {
            throw new ServiceException("回报组合图片删除失败，请刷新后重试！");
        }
        try {
            deleteImageFile(image.getObjectKey());
            if (fx67llDortmundReturnImageMapper.deleteImage(imageId) != 1) {
                log.warn("回报组合图片文件已删除，登记将在后台重试回收，图片ID：{}", imageId);
            }
        } catch (Exception e) {
            boolean quarantine = image.getDeleteAttempts() != null
                    && image.getDeleteAttempts() + 1 >= Math.max(imageDeleteRetryLimit, 1);
            fx67llDortmundReturnImageMapper.markDeleteFailed(
                    imageId, limit(e.getMessage(), 500), quarantine);
            log.warn("回报组合图片文件删除失败，已进入待重试状态，图片ID：{}", imageId, e);
        }
        return 1;
    }

    @Override
    public List<Fx67llDortmundReturnImage> selectImagesByCombinationId(Long combinationId) {
        Fx67llDortmundReturnCombination combination = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationByCombinationId(combinationId);
        if (combination == null
                || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(combination.getUserId()))) {
            throw new ServiceException("回报组合不存在或没有访问权限！");
        }
        return fx67llDortmundReturnImageMapper.selectActiveImagesByCombinationId(combinationId);
    }

    /**
     * 查询指定日期范围内的孤儿图片。
     * <p>
     * 返回过期T、关联失效B和到期D/Q；有效B不属于孤儿。
     * 非全量数据权限用户只能查看自己的图片。
     */
    @Override
    public List<Fx67llDortmundReturnImage> selectOrphanImageList(Fx67llDortmundReturnImage query) {
        Fx67llDortmundReturnImage value = query == null ? new Fx67llDortmundReturnImage() : query;
        LocalDate begin = parseOrphanDate(value.getBeginCreateTime(), "开始日期");
        LocalDate end = parseOrphanDate(value.getEndCreateTime(), "结束日期");
        if (begin.isAfter(end)) {
            throw new ServiceException("孤儿图片查询开始日期不能晚于结束日期！");
        }
        if (ChronoUnit.DAYS.between(begin, end) > 366) {
            throw new ServiceException("孤儿图片查询时间跨度不能超过366天！");
        }
        value.setBeginCreateTime(begin + " 00:00:00");
        value.setEndCreateTime(end + " 23:59:59");
        if (!hasAllDataScope()) {
            value.setUserId(SecurityUtils.getUserId());
        }
        return fx67llDortmundReturnImageMapper.selectOrphanImages(value);
    }

    /**
     * 逐项删除孤儿图片。
     * <p>
     * 每项独立处理并返回结果，单张文件删除失败不会阻断其他图片；失败记录会保留为D/Q并由后台扫描重试。
     */
    @Override
    public Fx67llDortmundReturnImageCleanupResult deleteOrphanImages(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            throw new ServiceException("请至少选择一张孤儿图片！");
        }
        LinkedHashSet<Long> uniqueIds = new LinkedHashSet<>(imageIds);
        if (uniqueIds.size() != imageIds.size() || uniqueIds.size() > 100 || uniqueIds.contains(null)) {
            throw new ServiceException("单次最多删除100张不重复的孤儿图片！");
        }
        Fx67llDortmundReturnImageCleanupResult result = new Fx67llDortmundReturnImageCleanupResult();
        result.setRequestedCount(uniqueIds.size());
        for (Long imageId : uniqueIds) {
            String failedObjectKey = deleteOrphanImage(imageId, true);
            if (failedObjectKey == null) {
                result.setDeletedCount(result.getDeletedCount() + 1);
            } else {
                result.setFailedCount(result.getFailedCount() + 1);
                result.getFailedObjectKeys().add(failedObjectKey);
            }
        }
        return result;
    }

    /**
     * 后台孤儿图片回收扫描。每次只处理有限批次，失败图片按next_retry_time延后重试。
     */
    @Scheduled(fixedDelayString = "${dortmund.return.image.cleanup-interval-ms:600000}",
            initialDelayString = "${dortmund.return.image.cleanup-initial-delay-ms:60000}")
    public void cleanupOrphanImages() {
        int limit = Math.max(imageCleanupBatchSize, 1);
        List<Long> imageIds;
        try {
            imageIds = fx67llDortmundReturnImageMapper.selectCleanupCandidateIds(limit,
                    Math.max(imageDeleteRetryLimit, 1));
        } catch (Exception e) {
            log.error("查询待回收孤儿图片失败", e);
            return;
        }
        int deleted = 0;
        for (Long imageId : imageIds) {
            try {
                if (deleteOrphanImage(imageId, false) == null) {
                    deleted++;
                }
            } catch (Exception e) {
                log.error("回收孤儿图片异常，图片ID：{}", imageId, e);
            }
        }
        if (!imageIds.isEmpty()) {
            log.info("孤儿图片回收扫描完成，候选：{}，删除：{}", imageIds.size(), deleted);
        }
    }

    private String deleteOrphanImage(Long imageId, boolean enforceScope) {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        return transaction.execute(status -> {
            Fx67llDortmundReturnImage image = fx67llDortmundReturnImageMapper.selectByImageIdForUpdate(imageId);
            if (image == null || !isOrphanCandidate(image)) {
                return "#" + imageId;
            }
            if (enforceScope && !hasAllDataScope() && !SecurityUtils.getUserId().equals(image.getUserId())) {
                return image.getObjectKey();
            }
            if (fx67llDortmundReturnImageMapper.markDeletePending(
                    imageId, "孤儿图片回收") != 1) {
                return image.getObjectKey();
            }
            try {
                deleteImageFile(image.getObjectKey());
                int rows = fx67llDortmundReturnImageMapper.deleteImage(imageId);
                return rows == 1 ? null : image.getObjectKey();
            } catch (Exception e) {
                boolean quarantine = image.getDeleteAttempts() != null
                        && image.getDeleteAttempts() + 1 >= Math.max(imageDeleteRetryLimit, 1);
                fx67llDortmundReturnImageMapper.markDeleteFailed(imageId, limit(e.getMessage(), 500), quarantine);
                log.warn("删除孤儿图片失败，图片ID：{}，对象键：{}", imageId, image.getObjectKey(), e);
                return image.getObjectKey();
            }
        });
    }

    private boolean isOrphanCandidate(Fx67llDortmundReturnImage image) {
        Date now = DateUtils.getNowDate();
        if (IMAGE_TEMP.equals(image.getImageState())) {
            return image.getExpireTime() != null && !image.getExpireTime().after(now);
        }
        if (IMAGE_DELETE_PENDING.equals(image.getImageState())
                || IMAGE_QUARANTINED.equals(image.getImageState())) {
            return image.getNextRetryTime() == null || !image.getNextRetryTime().after(now);
        }
        if (IMAGE_BOUND.equals(image.getImageState())) {
            if (image.getCombinationId() == null) {
                return true;
            }
            Fx67llDortmundReturnCombination combination = fx67llDortmundReturnCombinationMapper
                    .selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(
                            image.getCombinationId());
            return combination == null || !YES.equals(combination.getAdoptedFlag());
        }
        return false;
    }

    private void assertImageMutable(Fx67llDortmundReturnCombination combination, Long combinationId) {
        if (combination == null
                || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(combination.getUserId()))) {
            throw new ServiceException("回报组合不存在或没有访问权限！");
        }
        if (!YES.equals(combination.getAdoptedFlag())) {
            throw new ServiceException("仅已采用回报组合可以维护图片！");
        }
        if (fx67llDortmundReturnCombinationMapper.countPlanRelationsByCombinationId(combinationId) > 0) {
            throw new ServiceException("回报组合已被方案计划引用，图片只读；请先解除计划关联！");
        }
    }

    private void compensateFailedUpload(Long imageId) {
        if (imageId == null) {
            return;
        }
        String objectKey;
        try {
            TransactionTemplate transaction = new TransactionTemplate(transactionManager);
            objectKey = transaction.execute(status -> {
                Fx67llDortmundReturnImage image = fx67llDortmundReturnImageMapper
                        .selectByImageIdForUpdate(imageId);
                if (image == null || !IMAGE_TEMP.equals(image.getImageState())
                        || image.getCombinationId() != null) {
                    return null;
                }
                fx67llDortmundReturnImageMapper.markDeletePending(imageId, "图片绑定失败补偿");
                return image.getObjectKey();
            });
        } catch (Exception e) {
            log.error("回报组合图片绑定失败后的登记补偿异常，图片ID：{}", imageId, e);
            return;
        }
        if (objectKey == null) {
            return;
        }
        try {
            deleteImageFile(objectKey);
            fx67llDortmundReturnImageMapper.deleteImage(imageId);
        } catch (Exception e) {
            Fx67llDortmundReturnImage image = fx67llDortmundReturnImageMapper
                    .selectByImageIdForUpdate(imageId);
            boolean quarantine = image != null && image.getDeleteAttempts() != null
                    && image.getDeleteAttempts() + 1 >= Math.max(imageDeleteRetryLimit, 1);
            fx67llDortmundReturnImageMapper.markDeleteFailed(
                    imageId, limit(e.getMessage(), 500), quarantine);
            log.warn("回报组合图片绑定失败后文件补偿未完成，图片ID：{}", imageId, e);
        }
    }

    private void deleteImageFileQuietly(String objectKey) {
        try {
            deleteImageFile(objectKey);
        } catch (Exception e) {
            log.warn("补偿删除图片文件失败，对象键：{}", objectKey, e);
        }
    }

    private List<PreparedCombination> prepareCombinations(
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output,
            Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> oddsList) {
        if (output == null || output.getCandidates() == null) {
            return Collections.emptyList();
        }
        Map<Long, Fx67llDortmundOdds> oddsMap = new HashMap<>();
        for (Fx67llDortmundOdds odds : oddsList) {
            oddsMap.put(odds.getMatchId(), odds);
        }
        Map<String, PreparedCombination> unique = new LinkedHashMap<>();
        for (ReturnCombinationAiAnalysisPort.Candidate candidate : output.getCandidates()) {
            PreparedCombination prepared = prepareOne(candidate, request, oddsMap);
            if (prepared != null) {
                unique.putIfAbsent(prepared.signature, prepared);
            }
        }
        List<PreparedCombination> result = new ArrayList<>(unique.values());
        result.sort(Comparator.comparing(value -> value.combination.getDeviationAmount().abs()));
        return result.size() > 10 ? new ArrayList<>(result.subList(0, 10)) : result;
    }

    private PreparedCombination prepareOne(ReturnCombinationAiAnalysisPort.Candidate candidate,
            Fx67llDortmundReturnGenerateRequest request, Map<Long, Fx67llDortmundOdds> oddsMap) {
        if (candidate == null || blank(candidate.getTitle()) || candidate.getTitle().length() > 233
                || candidate.getSelections() == null || candidate.getSelections().isEmpty()
                || candidate.getSelections().size() > 8) {
            return null;
        }
        Set<Long> seenMatches = new LinkedHashSet<>();
        List<Fx67llDortmundReturnCombinationItem> items = new ArrayList<>();
        BigDecimal combined = BigDecimal.ONE;
        List<String> signatureParts = new ArrayList<>();
        int sort = 0;
        for (ReturnCombinationAiAnalysisPort.Selection selection : candidate.getSelections()) {
            if (selection == null || selection.getMatchId() == null || !seenMatches.add(selection.getMatchId())
                    || !oddsMap.containsKey(selection.getMatchId())
                    || !SELECTION_TYPES.contains(selection.getSelectionType())) {
                return null;
            }
            Fx67llDortmundOdds odds = oddsMap.get(selection.getMatchId());
            // 手工权重按原值保存，空值保持NULL，不能用默认100覆盖用户的“未填写”语义。
            BigDecimal manualWeight = rawManualWeight(odds, selection.getSelectionType());
            // AI评估权重：仅用于解释选择倾向；后续金额计算只使用oddsValue，不读取appliedWeight。
            BigDecimal appliedWeight = "S".equals(request.getGenerationMode())
                    ? effectiveAiWeight(manualWeight, request.getGenerationMode()) : selection.getAppliedWeight();
            if (appliedWeight == null || appliedWeight.signum() < 0
                    || appliedWeight.compareTo(new BigDecimal("100")) > 0) {
                return null;
            }
            BigDecimal oddsValue = oddsValue(odds, selection.getSelectionType());
            combined = combined.multiply(oddsValue);
            Fx67llDortmundReturnCombinationItem item = new Fx67llDortmundReturnCombinationItem();
            item.setMatchId(selection.getMatchId());
            item.setOddsId(odds.getOddsId());
            item.setMatchCode(odds.getMatchCode());
            item.setSelectionType(selection.getSelectionType());
            item.setOddsValueSnapshot(oddsValue);
            item.setManualWeightSnapshot(manualWeight);
            // AI评估权重仅保存用于复核，不能进入组合倍率、预计回报或金额公式。
            item.setAppliedWeight(appliedWeight);
            item.setItemSort(sort++);
            // 逐场选择理由快照仅用于复核，不参与任何金额计算。
            item.setSelectionReason(limit(selection.getReason(), 1023));
            items.add(item);
            signatureParts.add(selection.getMatchId() + ":" + selection.getSelectionType());
        }
        Collections.sort(signatureParts);
        BigDecimal estimated = request.getInputAmount().multiply(combined).setScale(2, RoundingMode.HALF_UP);
        if (combined.compareTo(MAX_COMBINATION_ODDS) > 0) {
            throw new ServiceException("组合倍率超出可计算范围，请减少比赛场次或调整赔率！");
        }
        if (estimated.compareTo(MAX_BUSINESS_AMOUNT) > 0) {
            throw new ServiceException("预计回报超出可计算范围，请调整基础金额或比赛组合！");
        }
        Fx67llDortmundReturnCombination combination = new Fx67llDortmundReturnCombination();
        combination.setCombinationCode(createCode("C"));
        combination.setCombinationTitle(candidate.getTitle().trim());
        combination.setCombinationReason(limit(candidate.getReason(), 2047));
        combination.setCombinationRemark(candidate.getAnalysisContent());
        combination.setCombinedOdds(combined.setScale(6, RoundingMode.HALF_UP));
        combination.setEstimatedReturn(estimated);
        combination.setDeviationAmount(estimated.subtract(request.getTargetAmount()).setScale(2, RoundingMode.HALF_UP));
        combination.setAdoptedFlag(NO);
        combination.setMatches(items);
        PreparedCombination result = new PreparedCombination();
        result.signature = String.join(";", signatureParts);
        result.combination = combination;
        result.items = items;
        return result;
    }

    /**
     * 固化异步生成结果。
     * <p>
     * worker不依赖SecurityUtils：审计用户取自批次创建人；事务内锁定批次并再次校验worker/status，
     * 防止租约过期后新旧worker同时写入。批次固化不读取、绑定或修改任何组合图片。
     */
    private void persistSuccess(Fx67llDortmundReturnGeneration generation, List<PreparedCombination> prepared) {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        Integer completed = transaction.execute(status -> {
            Fx67llDortmundReturnGeneration locked = fx67llDortmundReturnGenerationMapper
                    .selectGenerationForUpdate(generation.getGenerationId());
            if (locked == null || !STATUS_PROCESSING.equals(locked.getGenerationStatus())
                    || !workerId.equals(locked.getWorkerId())) {
                throw new ServiceException("生成批次租约已失效，本次结果不再落库！");
            }
            String username = blank(locked.getCreateBy()) ? "system" : locked.getCreateBy();
            Date now = DateUtils.getNowDate();
            for (PreparedCombination value : prepared) {
                Fx67llDortmundReturnCombination combination = value.combination;
                combination.setGenerationId(locked.getGenerationId());
                combination.setUserId(locked.getUserId());
                combination.setCreateBy(username);
                combination.setUpdateBy(username);
                combination.setCreateTime(now);
                combination.setUpdateTime(now);
                fx67llDortmundReturnCombinationMapper.insertFx67llDortmundReturnCombination(combination);
                for (Fx67llDortmundReturnCombinationItem item : value.items) {
                    item.setCombinationId(combination.getCombinationId());
                    item.setCreateBy(username);
                    item.setCreateTime(now);
                    fx67llDortmundReturnCombinationItemMapper.insertFx67llDortmundReturnCombinationItem(item);
                }
            }
            int rows = fx67llDortmundReturnGenerationMapper.completeGenerationSuccess(
                    locked.getGenerationId(), workerId, prepared.size(), "生成成功。", username);
            if (rows != 1) {
                throw new ServiceException("生成批次完成状态写入失败！");
            }
            return rows;
        });
        if (completed == null || completed != 1) {
            throw new ServiceException("生成批次结果固化失败！");
        }
    }

    private List<Fx67llDortmundReturnCombination> selectCombinationListByGeneration(Long generationId,
            String sortField, String sortOrder) {
        Fx67llDortmundReturnCombination query = new Fx67llDortmundReturnCombination();
        query.setGenerationId(generationId);
        query.setSortField(sortField);
        query.setSortOrder(sortOrder);
        return selectCombinationList(query);
    }

    private List<Fx67llDortmundOdds> loadRequestOdds(List<Long> matchIds) {
        List<Fx67llDortmundOdds> result = new ArrayList<>();
        for (Long matchId : new LinkedHashSet<>(matchIds)) {
            Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchService
                    .selectFx67llDortmundBasicMatchByMatchId(matchId);
            if (match == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(match.getUserId()))
                    || !"0".equals(match.getMatchStatus())) {
                throw new ServiceException("所选比赛不存在、已关闭或没有访问权限！");
            }
            Fx67llDortmundOdds odds = fx67llDortmundOddsService.selectActiveFx67llDortmundOddsByMatchId(matchId);
            if (odds == null) {
                throw new ServiceException("所选比赛没有当前有效倍率记录！");
            }
            odds.setMatchCode(match.getMatchCode());
            odds.setMatchSummary(buildMatchSummary(match));
            result.add(odds);
        }
        return result;
    }

    private void validateRequest(Fx67llDortmundReturnGenerateRequest request) {
        if (request == null || (!"S".equals(request.getGenerationMode())
                && !"A".equals(request.getGenerationMode()))) {
            throw new ServiceException("回报组合生成模式不正确！");
        }
        if (request.getInputAmount() == null || request.getInputAmount().signum() <= 0
                || request.getTargetAmount() == null || request.getTargetAmount().signum() <= 0) {
            throw new ServiceException("基础金额和目标金额必须大于0！");
        }
        if (request.getInputAmount().compareTo(MAX_BUSINESS_AMOUNT) > 0
                || request.getTargetAmount().compareTo(MAX_BUSINESS_AMOUNT) > 0
                || request.getInputAmount().stripTrailingZeros().scale() > 2
                || request.getTargetAmount().stripTrailingZeros().scale() > 2) {
            throw new ServiceException("基础金额和目标金额最多保留两位小数且不能超过999999999999.99！");
        }
        if (request.getMatchIds() == null || request.getMatchIds().size() < 1
                || request.getMatchIds().size() > 8 || request.getMatchIds().contains(null)
                || new HashSet<>(request.getMatchIds()).size() != request.getMatchIds().size()) {
            throw new ServiceException("比赛列表必须包含1至8场不重复的比赛！");
        }
    }

    private BigDecimal oddsValue(Fx67llDortmundOdds odds, String type) {
        switch (type) {
            case "HW":
                return odds.getHomeWinOdds();
            case "DR":
                return odds.getDrawOdds();
            case "AW":
                return odds.getAwayWinOdds();
            case "HH":
                return odds.getHandicapHomeWinOdds();
            case "HD":
                return odds.getHandicapDrawOdds();
            default:
                return odds.getHandicapAwayWinOdds();
        }
    }

    private BigDecimal rawManualWeight(Fx67llDortmundOdds odds, String type) {
        BigDecimal value;
        switch (type) {
            case "HW":
                value = odds.getHomeWinWeight();
                break;
            case "DR":
                value = odds.getDrawWeight();
                break;
            case "AW":
                value = odds.getAwayWinWeight();
                break;
            case "HH":
                value = odds.getHandicapHomeWinWeight();
                break;
            case "HD":
                value = odds.getHandicapDrawWeight();
                break;
            default:
                value = odds.getHandicapAwayWinWeight();
        }
        return value;
    }

    private String buildSnapshot(Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> odds) {
        JSONObject value = new JSONObject();
        value.put("generationMode", request.getGenerationMode());
        value.put("inputAmount", request.getInputAmount());
        value.put("targetAmount", request.getTargetAmount());
        value.put("matchIds", request.getMatchIds());
        value.put("odds", copyOddsList(odds));
        return value.toJSONString();
    }

    private List<Fx67llDortmundOdds> buildAiOddsList(List<Fx67llDortmundOdds> oddsList, String generationMode) {
        List<Fx67llDortmundOdds> result = copyOddsList(oddsList);
        if ("S".equals(generationMode)) {
            for (Fx67llDortmundOdds target : result) {
                target.setHomeWinWeight(effectiveAiWeight(target.getHomeWinWeight(), generationMode));
                target.setDrawWeight(effectiveAiWeight(target.getDrawWeight(), generationMode));
                target.setAwayWinWeight(effectiveAiWeight(target.getAwayWinWeight(), generationMode));
                target.setHandicapHomeWinWeight(effectiveAiWeight(target.getHandicapHomeWinWeight(), generationMode));
                target.setHandicapDrawWeight(effectiveAiWeight(target.getHandicapDrawWeight(), generationMode));
                target.setHandicapAwayWinWeight(effectiveAiWeight(target.getHandicapAwayWinWeight(), generationMode));
            }
        }
        return result;
    }

    private List<Fx67llDortmundOdds> copyOddsList(List<Fx67llDortmundOdds> oddsList) {
        List<Fx67llDortmundOdds> result = new ArrayList<>();
        for (Fx67llDortmundOdds source : oddsList) {
            Fx67llDortmundOdds target = new Fx67llDortmundOdds();
            target.setOddsId(source.getOddsId());
            target.setMatchId(source.getMatchId());
            target.setMatchCode(source.getMatchCode());
            target.setMatchSummary(source.getMatchSummary());
            target.setOddsCode(source.getOddsCode());
            target.setSourceTime(source.getSourceTime());
            target.setHandicapValue(source.getHandicapValue());
            target.setHomeWinOdds(source.getHomeWinOdds());
            target.setDrawOdds(source.getDrawOdds());
            target.setAwayWinOdds(source.getAwayWinOdds());
            target.setHandicapHomeWinOdds(source.getHandicapHomeWinOdds());
            target.setHandicapDrawOdds(source.getHandicapDrawOdds());
            target.setHandicapAwayWinOdds(source.getHandicapAwayWinOdds());
            target.setHomeWinWeight(source.getHomeWinWeight());
            target.setDrawWeight(source.getDrawWeight());
            target.setAwayWinWeight(source.getAwayWinWeight());
            target.setHandicapHomeWinWeight(source.getHandicapHomeWinWeight());
            target.setHandicapDrawWeight(source.getHandicapDrawWeight());
            target.setHandicapAwayWinWeight(source.getHandicapAwayWinWeight());
            result.add(target);
        }
        return result;
    }

    private BigDecimal effectiveAiWeight(BigDecimal weight, String generationMode) {
        return "S".equals(generationMode) && weight == null ? new BigDecimal("100") : weight;
    }

    /**
     * 用历史分析内容兜底回填逐场理由。
     * <p>
     * 新版明细已直接保存selection_reason；仅当该字段为空时，才从本候选的
     * combination_remark.selections中按比赛和选择类型恢复，避免覆盖已固化快照。
     */
    private void fillSelectionReasons(Long combinationId, String combinationRemark,
            List<Fx67llDortmundReturnCombinationItem> items) {
        if (blank(combinationRemark) || items == null || items.isEmpty()) {
            return;
        }
        try {
            JSONArray selections = JSONObject.parseObject(combinationRemark).getJSONArray("selections");
            if (selections == null || selections.isEmpty()) {
                return;
            }
            Map<String, String> reasons = new HashMap<>();
            for (int i = 0; i < selections.size(); i++) {
                JSONObject selection = selections.getJSONObject(i);
                if (selection != null && selection.getLong("matchId") != null
                        && !blank(selection.getString("reason"))) {
                    reasons.put(selection.getLong("matchId") + ":" + selection.getString("selectionType"),
                            selection.getString("reason"));
                }
            }
            for (Fx67llDortmundReturnCombinationItem item : items) {
                if (blank(item.getSelectionReason())) {
                    item.setSelectionReason(reasons.get(item.getMatchId() + ":" + item.getSelectionType()));
                }
            }
        } catch (Exception e) {
            log.warn("回报组合明细理由解析失败，组合ID：{}", combinationId);
        }
    }

    private String buildMatchSummary(Fx67llDortmundBasicMatch match) {
        String homeTeamName = blank(match.getHomeTeamName()) ? "" : match.getHomeTeamName();
        String awayTeamName = blank(match.getAwayTeamName()) ? "" : match.getAwayTeamName();
        String matchTime = match.getMatchTime() == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", match.getMatchTime());
        String summary = (homeTeamName + " vs " + awayTeamName).trim();
        return blank(matchTime) ? summary : summary + " " + matchTime;
    }

    private LocalDate parseOrphanDate(String value, String label) {
        if (blank(value)) {
            throw new ServiceException("孤儿图片查询" + label + "不能为空！");
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (Exception e) {
            throw new ServiceException("孤儿图片查询" + label + "格式不正确，应为yyyy-MM-dd！");
        }
    }

    private String normalizeDateBoundary(String value, boolean start) {
        if (blank(value)) {
            return value;
        }
        String trimmed = value.trim();
        return trimmed.length() > 10 ? trimmed : trimmed + (start ? " 00:00:00" : " 23:59:59");
    }

    private void validateImageObjectKey(String objectKey) {
        if (objectKey == null) {
            throw new ServiceException("图片对象键格式不正确！");
        }
        if (blank(objectKey) || objectKey.length() > 512 || objectKey.contains("..")
                || !objectKey.startsWith(Constants.RESOURCE_PREFIX + "/")) {
            throw new ServiceException("图片对象键格式不正确！");
        }
    }

    private void deleteImageFile(String objectKey) {
        validateImageObjectKey(objectKey);
        String filePath = RuoYiConfig.getProfile() + objectKey.substring(Constants.RESOURCE_PREFIX.length());
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (!FileUtils.deleteFile(filePath)) {
            throw new ServiceException("图片文件删除失败，请稍后重试！");
        }
    }

    private String createCode(String prefix) {
        return DateUtils.dateTimeNow("yyyyMMddHHmmss") + prefix
                + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }

    private String limit(String value, int length) {
        return value == null || value.length() <= length ? value : value.substring(0, length);
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

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

    private boolean hasPermission(String permission) {
        return SecurityUtils.getLoginUser().getPermissions().contains(permission)
                || SecurityUtils.getLoginUser().getPermissions().contains("*:*:*");
    }

    private static class GenerationSnapshot {
        private final Fx67llDortmundReturnGenerateRequest request;
        private final List<Fx67llDortmundOdds> odds;

        private GenerationSnapshot(Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> odds) {
            this.request = request;
            this.odds = odds;
        }
    }

    private static class PreparedCombination {
        private String signature;
        private Fx67llDortmundReturnCombination combination;
        private List<Fx67llDortmundReturnCombinationItem> items;
    }
}
