package com.ruoyi.fx67ll.dortmund.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson2.JSON;
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
import com.ruoyi.fx67ll.dortmund.ai.ReturnCombinationAiBusinessContext;
import com.ruoyi.fx67ll.dortmund.ai.ReturnCombinationAiResponseParser;
import com.ruoyi.fx67ll.dortmund.ai.ReturnEvidenceRetrievalPort;
import com.ruoyi.fx67ll.dortmund.ai.EvidenceSourceTaxonomy;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointTeamRel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationItem;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnEvidence;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualImportRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualImportResult;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTask;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTemplate;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTemplateField;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImageCleanupResult;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationItemMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnGenerationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnEvidenceMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnImageMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundOddsService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;
import com.ruoyi.fx67ll.dortmund.support.DortmundWeightCodec;

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
    private static final String STATUS_MANUAL_PENDING = "3";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String IMAGE_TEMP = "T";
    private static final String IMAGE_BOUND = "B";
    private static final String IMAGE_DELETE_PENDING = "D";
    private static final String IMAGE_QUARANTINED = "Q";
    private static final String RESULT_SOURCE_AUTO = "A";
    private static final String RESULT_SOURCE_MANUAL = "M";
    private static final String SNAPSHOT_VERSION = "2.1";
    private static final String AI_SCENE_CODE = ReturnCombinationAiResponseParser.TASK_TYPE;
    private static final String AUTO_AI_CONTRACT_VERSION = ReturnCombinationAiResponseParser.AUTO_SCHEMA_VERSION;
    private static final String MANUAL_AI_CONTRACT_VERSION = ReturnCombinationAiResponseParser.MANUAL_SCHEMA_VERSION;
    private static final String EVIDENCE_STATUS_NOT_APPLICABLE = "N";
    private static final String EVIDENCE_STATUS_PLANNING = "P";
    private static final String EVIDENCE_STATUS_READY = "R";
    private static final String EVIDENCE_STATUS_FAILED = "F";
    private static final String EVIDENCE_ORIGIN_SUPPLIED = "S";
    private static final String EVIDENCE_ORIGIN_RETRIEVED = "R";
    private static final String EVIDENCE_SOURCE_SUPPLIED = "SUPPLIED";
    private static final String EVIDENCE_SOURCE_RETRIEVAL = "RETRIEVAL";
    private static final String EVIDENCE_SOURCE_MANUAL_WEIGHT = "MANUAL_WEIGHT";
    private static final int MAX_RETRIEVAL_QUERIES = 8;
    private static final int MAX_RETRIEVAL_RESULTS_PER_QUERY = 4;
    private static final int MAX_EVIDENCE_FACTORS = 20;
    private static final int MAX_EVIDENCE_OBSERVATION_LENGTH = 1000;
    private static final int MAX_WEIGHT_RATIONALE_LENGTH = 4000;
    private static final int MAX_CONFLICT_GROUP_LENGTH = 64;
    private static final int MAX_RISK_NOTES_LENGTH = 2000;
    private static final int MAX_COUNTER_EVIDENCE_REFS = 20;
    private static final long AI_PLAN_TIMEOUT_MILLIS = 60_000L;
    private static final long AI_ANALYSIS_TIMEOUT_MILLIS = 180_000L;
    private static final String AI_OPERATION_PLAN = "PLAN";
    private static final String AI_OPERATION_ANALYZE = "ANALYZE";
    private static final int MAX_SUPPLEMENTARY_OBJECTIVES = 5;
    private static final int MAX_SUPPLEMENTARY_OBJECTIVE_LENGTH = 200;
    private static final int MAX_SUPPLEMENTARY_OBJECTIVES_TOTAL_LENGTH = 1000;
    private static final int MAX_MANUAL_RESPONSE_BYTES = 2 * 1024 * 1024;
    private static final BigDecimal MAX_BUSINESS_AMOUNT = new BigDecimal("999999999999.99");
    private static final BigDecimal MAX_COMBINATION_ODDS = new BigDecimal("999999999999.999999");
    private static final Set<String> SORT_FIELDS = new HashSet<>();
    private static final Set<String> SELECTION_TYPES = new HashSet<>();
    private static final Set<String> EVIDENCE_SOURCE_TYPES = new HashSet<>();
    static {
        Collections.addAll(SELECTION_TYPES, "HW", "DR", "AW", "HH", "HD", "HA");
        Collections.addAll(EVIDENCE_SOURCE_TYPES, EVIDENCE_SOURCE_SUPPLIED,
                EVIDENCE_SOURCE_RETRIEVAL, EVIDENCE_SOURCE_MANUAL_WEIGHT);
        Collections.addAll(SORT_FIELDS, "combinedOdds", "estimatedReturn", "deviationAmount",
                "matchCount", "reviewTime", "aiConfidence", "createTime");
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
    private Fx67llDortmundReturnEvidenceMapper fx67llDortmundReturnEvidenceMapper;

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    @Autowired
    private IFx67llDortmundBasicTeamService fx67llDortmundBasicTeamService;

    @Autowired
    private IFx67llDortmundInfoPointService fx67llDortmundInfoPointService;

    @Autowired
    private IFx67llDortmundOddsService fx67llDortmundOddsService;

    /** 【通用AI接入点】业务服务只依赖分析端口；替换通用AI适配器不修改快照、解析和落库逻辑。 */
    @Autowired
    private ReturnCombinationAiAnalysisPort returnCombinationAiAnalysisPort;

    @Autowired
    private ReturnCombinationAiResponseParser returnCombinationAiResponseParser;

    /** 【通用AI接入点】通用AI搜索工具通过该端口接入；覆盖校验、证据冻结和引用校验保留在业务侧。 */
    @Autowired
    private ReturnEvidenceRetrievalPort returnEvidenceRetrievalPort;

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
                requireResultSource(existing, RESULT_SOURCE_AUTO);
                existing.setRequestSnapshot(null);
                existing.setCombinations(new ArrayList<>());
                return existing;
            }
        }
        List<Fx67llDortmundOdds> oddsList = loadRequestOdds(request.getMatchIds());
        ReturnCombinationAiBusinessContext businessContext = buildBusinessContext(request.getMatchIds());
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
        generation.setDataAsOf(now);
        generation.setUserId(userId);
        generation.setClientRequestId(blank(request.getClientRequestId())
                ? UUID.randomUUID().toString().replace("-", "") : limit(request.getClientRequestId(), 64));
        generation.setAttemptCount(0);
        generation.setMaxAttempts(generationMaxAttempts);
        generation.setNextRetryTime(now);
        generation.setResultSource(RESULT_SOURCE_AUTO);
        generation.setAiSceneCode(AI_SCENE_CODE);
        generation.setAiContractVersion(AUTO_AI_CONTRACT_VERSION);
        generation.setAiEvidenceStatus(EVIDENCE_STATUS_NOT_APPLICABLE);
        generation.setCreateBy(username);
        generation.setUpdateBy(username);
        generation.setCreateTime(now);
        generation.setUpdateTime(now);
        generation.setRequestSnapshot(buildSnapshot(request, oddsList, businessContext, now));
        try {
            fx67llDortmundReturnGenerationMapper.insertFx67llDortmundReturnGeneration(generation);
            generation.setRequestSnapshot(null);
            generation.setCombinations(new ArrayList<>());
            return generation;
        } catch (DuplicateKeyException e) {
            Fx67llDortmundReturnGeneration existing = fx67llDortmundReturnGenerationMapper
                    .selectByClientRequestId(userId, generation.getClientRequestId());
            if (existing != null) {
                requireResultSource(existing, RESULT_SOURCE_AUTO);
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
     * 创建人工AI处理批次。
     * <p>
     * 人工路径与自动路径共享提交参数和业务快照，但批次直接进入待人工导入状态，
     * 不进入自动worker队列。用户下载任务并交给第三方AI后，再通过导入接口完成批次。
     */
    @Override
    public Fx67llDortmundReturnManualTask createManualTask(Fx67llDortmundReturnGenerateRequest request) {
        validateRequest(request);
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        if (!blank(request.getClientRequestId())) {
            Fx67llDortmundReturnGeneration existing = fx67llDortmundReturnGenerationMapper
                    .selectByClientRequestId(userId, request.getClientRequestId());
            if (existing != null) {
                requireResultSource(existing, RESULT_SOURCE_MANUAL);
                return buildManualTask(existing.getGenerationId());
            }
        }
        List<Fx67llDortmundOdds> oddsList = loadRequestOdds(request.getMatchIds());
        ReturnCombinationAiBusinessContext businessContext = buildBusinessContext(request.getMatchIds());
        Date now = DateUtils.getNowDate();
        Fx67llDortmundReturnGeneration generation = new Fx67llDortmundReturnGeneration();
        generation.setGenerationCode(createCode("G"));
        generation.setGenerationMode(request.getGenerationMode());
        generation.setInputAmount(request.getInputAmount());
        generation.setTargetAmount(request.getTargetAmount());
        generation.setGenerationStatus(STATUS_MANUAL_PENDING);
        generation.setGenerationRemark("人工AI任务已创建，等待导入第三方AI标准化结果。");
        generation.setGeneratedCount(0);
        generation.setStartedTime(now);
        generation.setDataAsOf(now);
        generation.setUserId(userId);
        generation.setClientRequestId(blank(request.getClientRequestId())
                ? UUID.randomUUID().toString().replace("-", "") : limit(request.getClientRequestId(), 64));
        generation.setAttemptCount(0);
        generation.setMaxAttempts(0);
        generation.setResultSource(RESULT_SOURCE_MANUAL);
        generation.setAiSceneCode(AI_SCENE_CODE);
        generation.setAiContractVersion(MANUAL_AI_CONTRACT_VERSION);
        generation.setAiEvidenceStatus(EVIDENCE_STATUS_NOT_APPLICABLE);
        generation.setCreateBy(username);
        generation.setUpdateBy(username);
        generation.setCreateTime(now);
        generation.setUpdateTime(now);
        generation.setRequestSnapshot(buildSnapshot(request, oddsList, businessContext, now));
        try {
            fx67llDortmundReturnGenerationMapper.insertFx67llDortmundReturnGeneration(generation);
            return buildManualTask(generation.getGenerationId());
        } catch (DuplicateKeyException e) {
            Fx67llDortmundReturnGeneration existing = fx67llDortmundReturnGenerationMapper
                    .selectByClientRequestId(userId, generation.getClientRequestId());
            if (existing != null) {
                requireResultSource(existing, RESULT_SOURCE_MANUAL);
                return buildManualTask(existing.getGenerationId());
            }
            throw new ServiceException("人工AI处理请求重复，请刷新后重试！");
        } catch (Exception e) {
            log.error("创建人工AI处理任务失败", e);
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException("人工AI处理任务创建失败，请稍后重试！");
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

    /**
     * 构建人工AI处理任务。
     * <p>
     * 任务只复用已固化的业务快照，不重新查询比赛或赔率。待人工导入和遗留失败批次均可导出，
     * 保证第三方AI处理期间原始业务条件变化不会改变任务输入。
     */
    @Override
    public Fx67llDortmundReturnManualTask buildManualTask(Long generationId) {
        Fx67llDortmundReturnGeneration generation = requireManualTaskGeneration(generationId);
        GenerationSnapshot snapshot = parseGenerationSnapshot(generation.getRequestSnapshot());
        Fx67llDortmundReturnManualTask task = new Fx67llDortmundReturnManualTask();
        task.setSchemaVersion(MANUAL_AI_CONTRACT_VERSION);
        task.setTaskType(ReturnCombinationAiResponseParser.TASK_TYPE);
        task.setGenerationId(generation.getGenerationId());
        task.setTaskId(generation.getGenerationCode());
        task.setGenerationMode(generation.getGenerationMode());
        task.setGeneratedAt(DateUtils.getNowDate());
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput manualInput = buildAiInput(snapshot);
        manualInput.setBusinessObjectives(buildManualBusinessObjectives(snapshot.request));
        manualInput.setEvidenceCatalog(Collections.emptyList());
        manualInput.setRetrievedEvidence(Collections.emptyList());
        task.setInput(manualInput);
        task.setInstruction("请把input作为唯一业务数据源，严格按responseContract返回单个JSON对象。"
                + "input.businessObjectives是后端预定义业务目标，input.supplementaryObjectives是用户补充业务目标，"
                + "两者都是强制约束，补充目标不得覆盖、替换或弱化预定义目标。"
                + "input.businessContext中的球队标签是原始开放标签列表，请结合球队和比赛语境自行解释，"
                + "不得假设固定标签字典；赛季球队信息点和比赛临时信息点仅作为证据，不得虚构或补写。"
                + "人工任务不执行平台内证据检索，第三方AI如需扩展信息请在平台外自行处理，"
                + "但不得把未经核实的扩展信息伪装为input中的业务事实。"
                + "不得虚构比赛、球队、标签、信息点、赔率或选择类型；不得返回供应商、模型、Token或费用信息。");
        task.setResponseContract(buildManualResponseContract(generation, snapshot));
        return task;
    }

    /**
     * 构建人工AI标准交换模板。
     * <p>
     * 模板只描述交换骨架，不包含任何批次的真实业务数据；实际任务通过manual-task接口导出。
     * 两个模板都可直接复制给第三方AI，用于明确任务输入与回填结果的最小结构。
     */
    @Override
    public Fx67llDortmundReturnManualTemplate buildManualTemplate() {
        Fx67llDortmundReturnManualTemplate template = new Fx67llDortmundReturnManualTemplate();
        template.setSchemaVersion(MANUAL_AI_CONTRACT_VERSION);
        template.setTaskType(ReturnCombinationAiResponseParser.TASK_TYPE);
        template.setInstruction("任务模板用于说明人工AI导出结构，实际导出请使用当前批次的manual-task数据；"
                + "结果模板用于说明回填结构，导入前必须把taskId替换为当前批次编码，"
                + "并确保analysisContent、candidates和selections均为真实分析结果。");
        template.setWorkflowSteps(Arrays.asList(
                "步骤1：在当前批次导出处理任务JSON，业务快照在导出前已冻结。",
                "步骤2：把任务JSON与“第三方AI处理提示词”一起交给外部AI，由外部AI完成信息补充、比赛选择和权重判断。",
                "步骤3：只把符合1.0结果契约的单个JSON对象粘贴或上传回系统，平台重新校验、复算、去重并落库。"));

        JSONObject resultExample = new JSONObject();
        resultExample.put("schemaVersion", MANUAL_AI_CONTRACT_VERSION);
        resultExample.put("taskType", ReturnCombinationAiResponseParser.TASK_TYPE);
        resultExample.put("taskId", "{{taskId}}");
        resultExample.put("analysisContent", "中文分析摘要：说明候选组合如何满足预定义与补充业务目标。");
        JSONArray resultCandidates = new JSONArray();
        JSONObject resultCandidate = new JSONObject();
        resultCandidate.put("title", "候选组合标题");
        resultCandidate.put("reason", "候选组合的简要选择理由");
        resultCandidate.put("confidence", 78);
        resultCandidate.put("riskNotes", "候选风险与信息缺口说明，可省略。");
        JSONArray resultSelections = new JSONArray();
        JSONObject resultSelection = new JSONObject();
        resultSelection.put("matchId", 0);
        resultSelection.put("selectionType", "HW");
        resultSelection.put("appliedWeight", 0.8000);
        resultSelection.put("reason", "该场比赛的选择理由");
        resultSelections.add(resultSelection);
        resultCandidate.put("selections", resultSelections);
        resultCandidates.add(resultCandidate);
        resultExample.put("candidates", resultCandidates);

        JSONObject task = new JSONObject();
        task.put("schemaVersion", MANUAL_AI_CONTRACT_VERSION);
        task.put("taskType", ReturnCombinationAiResponseParser.TASK_TYPE);
        task.put("generationId", "{{generationId}}");
        task.put("taskId", "{{taskId}}");
        task.put("generationMode", "S或A");
        task.put("generatedAt", "{{generatedAt}}");
        JSONObject input = new JSONObject();
        input.put("generationMode", "S或A");
        input.put("dataAsOf", "{{dataAsOf}}");
        input.put("inputAmount", 100);
        input.put("targetAmount", 500);
        input.put("odds", new JSONArray());
        input.put("businessObjectives", new JSONArray());
        input.put("supplementaryObjectives", new JSONArray());
        JSONObject businessContext = new JSONObject();
        businessContext.put("contextVersion", "1.0");
        businessContext.put("matches", new JSONArray());
        input.put("businessContext", businessContext);
        input.put("evidenceCatalog", new JSONArray());
        input.put("retrievedEvidence", new JSONArray());
        task.put("input", input);
        task.put("instruction", "将{{taskId}}替换为当前批次编码；以input为唯一业务事实源，"
                + "严格按responseContract返回单个JSON对象，不得返回供应商、模型、Token或费用信息。");
        JSONObject responseContract = new JSONObject();
        responseContract.put("schemaVersion", MANUAL_AI_CONTRACT_VERSION);
        responseContract.put("taskType", ReturnCombinationAiResponseParser.TASK_TYPE);
        responseContract.put("taskId", "{{taskId}}");
        JSONArray requiredFields = new JSONArray();
        requiredFields.add("schemaVersion");
        requiredFields.add("taskType");
        requiredFields.add("taskId");
        requiredFields.add("analysisContent");
        requiredFields.add("candidates");
        responseContract.put("requiredTopLevelFields", requiredFields);
        responseContract.put("candidateRules", buildManualCandidateRules());
        responseContract.put("selectionTypes", buildManualSelectionTypes());
        responseContract.put("outputExample", resultExample);
        task.put("responseContract", responseContract);

        template.setTaskTemplate(JSON.toJSONString(task));
        template.setResultTemplate(JSON.toJSONString(resultExample));
        template.setTaskFields(buildManualTaskFields());
        template.setResultFields(buildManualResultFields());
        template.setExportPrompt(buildManualExportPrompt());
        template.setImportPrompt(buildManualImportPrompt());
        return template;
    }

    /**
     * 构建人工任务导出字段说明。
     *
     * @return 按任务、业务输入、赔率、上下文、证据和契约分组的字段清单
     */
    private List<Fx67llDortmundReturnManualTemplateField> buildManualTaskFields() {
        List<Fx67llDortmundReturnManualTemplateField> fields = new ArrayList<>();
        addManualField(fields, "任务头", "schemaVersion", "交换契约版本", "string", true,
                "固定为1.0，用于确认任务与结果使用同一版人工交换契约。", "1.0");
        addManualField(fields, "任务头", "taskType", "任务类型", "string", true,
                "固定为DORTMUND_RETURN_COMBINATION，标识回报组合人工分析任务。", "DORTMUND_RETURN_COMBINATION");
        addManualField(fields, "任务头", "generationId", "批次主键", "number", true,
                "系统内部批次主键，仅用于追踪，不得写入分析结论或用于金额计算。", "1001");
        addManualField(fields, "任务头", "taskId", "批次编码", "string", true,
                "当前批次编码，也是导入时必须原样返回的taskId；不得改写或自行生成。", "RET202611180001");
        addManualField(fields, "任务头", "generationMode", "生成模式", "string", true,
                "S表示主观模式，A表示AI辅助模式；决定权重处理规则和证据使用方式。", "S");
        addManualField(fields, "任务头", "generatedAt", "导出时间", "datetime", true,
                "任务JSON生成时间，仅用于审计；分析时以input.dataAsOf为业务数据截止时间。", "2026-11-18 10:00:00");
        addManualField(fields, "任务头", "input", "业务输入", "object", true,
                "冻结的业务输入快照，是第三方AI唯一可信的业务事实源。", "见input字段说明");
        addManualField(fields, "任务头", "instruction", "任务说明", "string", true,
                "后端生成的处理约束摘要，与第三方AI提示词共同使用。", "以input为唯一业务事实源...");
        addManualField(fields, "任务头", "responseContract", "响应契约", "object", true,
                "规定结果必须满足的顶层字段、候选规则、选择类型和示例。", "见responseContract字段说明");

        addManualField(fields, "业务输入", "input.generationMode", "生成模式", "string", true,
                "与任务头generationMode一致；S模式手工权重权威，A模式由AI自主定权。", "S");
        addManualField(fields, "业务输入", "input.dataAsOf", "数据截止时间", "datetime", true,
                "提交受理时冻结的业务数据截止时间；信息点、赔率和证据均不得晚于该时间使用。", "2026-11-18 09:30:00");
        addManualField(fields, "业务输入", "input.inputAmount", "基础金额", "number", true,
                "用户计划投入的基础金额，仅用于理解业务目标，不得由AI自行改写或参与候选金额复算。", "100.00");
        addManualField(fields, "业务输入", "input.targetAmount", "目标金额", "number", true,
                "用户期望达到的目标金额，仅用于理解回报目标；组合倍率与偏差由后端复算。", "500.00");
        addManualField(fields, "业务输入", "input.odds", "赔率快照数组", "array<object>", true,
                "本次可选比赛及其六类赔率和权重快照；每个matchId只能出现一次。", "见赔率快照字段");
        addManualField(fields, "业务输入", "input.businessObjectives", "预定义业务目标", "array<string>", true,
                "后端预定义的强制业务目标，第三方AI必须逐条满足，不得删减、覆盖或弱化。", "[\"优先选择证据充分的比赛\"]");
        addManualField(fields, "业务输入", "input.supplementaryObjectives", "补充业务目标", "array<string>", false,
                "用户临时补充的业务目标，与预定义目标并列生效；不得用它替换预定义目标。", "[\"优先考虑主场状态稳定的球队\"]");
        addManualField(fields, "业务输入", "input.businessContext", "业务上下文", "object", true,
                "比赛、主客队、原始球队标签、赛季球队信息点和比赛临时信息点。", "见业务上下文字段");
        addManualField(fields, "业务输入", "input.evidenceCatalog", "已提交证据目录", "array<object>", false,
                "自动AI批次可能包含由信息点和赔率冻结的证据；人工任务固定为空数组。第三方AI可参考业务上下文，但不得据此编造证据链。", "[]");
        addManualField(fields, "业务输入", "input.retrievedEvidence", "检索证据", "array<object>", false,
                "自动AI模式由平台检索并冻结的证据；人工任务通常为空，第三方AI可在平台外自行补充信息。", "[]");

        addManualField(fields, "赔率快照", "input.odds[].oddsId", "赔率主键", "number", true,
                "赔率记录主键，用于审计快照来源，不参与分析结论。", "9001");
        addManualField(fields, "赔率快照", "input.odds[].matchId", "比赛主键", "number", true,
                "候选selection.matchId必须来自该字段，且同一候选内不得重复。", "501");
        addManualField(fields, "赔率快照", "input.odds[].matchCode", "比赛编码", "string", true,
                "比赛业务编码，用于展示和核对比赛身份。", "M20261118001");
        addManualField(fields, "赔率快照", "input.odds[].matchSummary", "比赛摘要", "string", false,
                "主客队与开赛时间的可读摘要，仅用于理解比赛，不得据此虚构事实。", "主队 vs 客队 · 2026-11-18 20:00");
        addManualField(fields, "赔率快照", "input.odds[].oddsCode", "赔率编码", "string", true,
                "赔率版本编码，用于追踪该场使用的赔率快照。", "ODDS20261118001");
        addManualField(fields, "赔率快照", "input.odds[].sourceTime", "赔率来源时间", "datetime", false,
                "赔率采集时间，不得晚于input.dataAsOf。", "2026-11-18 09:20:00");
        addManualField(fields, "赔率快照", "input.odds[].handicapValue", "让分值", "number", false,
                "让分盘口的让球数值，负值表示主队受让；仅用于理解HH/HD/HA三类选择。", "-1.00");
        addManualField(fields, "赔率快照", "input.odds[].homeWinOdds", "主胜倍率", "number", true,
                "对应selectionType=HW的赔率快照，由后端复算使用，AI不得改写。", "2.10");
        addManualField(fields, "赔率快照", "input.odds[].homeWinWeight", "主胜权重", "number", false,
                "0.0000-1.0000，最多四位小数；S模式为权威手工权重，A模式为参考或空值。", "0.8000");
        addManualField(fields, "赔率快照", "input.odds[].drawOdds", "平局倍率", "number", false,
                "对应selectionType=DR的赔率快照；为空表示该场不可选平局。", "3.40");
        addManualField(fields, "赔率快照", "input.odds[].drawWeight", "平局权重", "number", false,
                "0.0000-1.0000，最多四位小数；语义同主胜权重。", "0.5000");
        addManualField(fields, "赔率快照", "input.odds[].awayWinOdds", "客胜倍率", "number", false,
                "对应selectionType=AW的赔率快照；为空表示该场不可选客胜。", "3.20");
        addManualField(fields, "赔率快照", "input.odds[].awayWinWeight", "客胜权重", "number", false,
                "0.0000-1.0000，最多四位小数；语义同主胜权重。", "0.6000");
        addManualField(fields, "赔率快照", "input.odds[].handicapHomeWinOdds", "让分主胜倍率", "number", false,
                "对应selectionType=HH的赔率快照；为空表示该场不可选让分主胜。", "1.95");
        addManualField(fields, "赔率快照", "input.odds[].handicapHomeWinWeight", "让分主胜权重", "number", false,
                "0.0000-1.0000，最多四位小数；语义同主胜权重。", "0.7000");
        addManualField(fields, "赔率快照", "input.odds[].handicapDrawOdds", "让分平局倍率", "number", false,
                "对应selectionType=HD的赔率快照；为空表示该场不可选让分平局。", "3.60");
        addManualField(fields, "赔率快照", "input.odds[].handicapDrawWeight", "让分平局权重", "number", false,
                "0.0000-1.0000，最多四位小数；语义同主胜权重。", "0.3000");
        addManualField(fields, "赔率快照", "input.odds[].handicapAwayWinOdds", "让分客胜倍率", "number", false,
                "对应selectionType=HA的赔率快照；为空表示该场不可选让分客胜。", "1.88");
        addManualField(fields, "赔率快照", "input.odds[].handicapAwayWinWeight", "让分客胜权重", "number", false,
                "0.0000-1.0000，最多四位小数；语义同主胜权重。", "0.4000");
        addManualField(fields, "业务上下文", "input.businessContext.contextVersion", "上下文版本", "string", true,
                "业务上下文结构版本，当前固定为1.0。", "1.0");
        addManualField(fields, "业务上下文", "input.businessContext.matches", "比赛上下文数组", "array<object>", true,
                "参与分析的比赛、赛季、主客队和信息点；不得新增input.odds之外的比赛。", "见比赛字段");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].matchId", "比赛主键", "number", true,
                "与input.odds[].matchId对应。", "501");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].matchCode", "比赛编码", "string", true,
                "比赛业务编码。", "M20261118001");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].matchTime", "开赛时间", "datetime", true,
                "比赛开赛时间，用于判断信息时效和赛程影响。", "2026-11-18 20:00:00");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].seasonId", "赛季主键", "number", false,
                "比赛所属赛季主键。", "12");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].seasonName", "赛季名称", "string", false,
                "比赛所属赛季名称。", "2026-2027");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].homeTeam", "主队上下文", "object", true,
                "主队身份、原始标签和赛季球队信息点。", "见球队字段");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].awayTeam", "客队上下文", "object", true,
                "客队身份、原始标签和赛季球队信息点。", "见球队字段");
        addManualField(fields, "比赛上下文", "input.businessContext.matches[].matchInfoPoints", "比赛临时信息点", "array<object>", false,
                "M类型比赛临时信息点，如伤停、天气、赛程密集；只作为证据，不得虚构。", "见信息点字段");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].homeTeam.teamId", "球队主键", "number", true,
                "球队主键。", "301");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].homeTeam.teamName", "球队名称", "string", true,
                "球队名称。", "示例主队");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].homeTeam.teamTags", "原始球队标签", "array<string>", false,
                "开放标签原文，不映射固定字典；AI应结合球队和比赛语境自行解释，标签可能继续扩展。", "[\"主场强势\",\"阵容稳定\"]");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].homeTeam.seasonInfoPoints", "赛季球队信息点", "array<object>", false,
                "T类型赛季球队信息点，承载长期球队特征；只作为证据，不得虚构。", "见信息点字段");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].awayTeam.teamId", "球队主键", "number", true,
                "球队主键。", "302");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].awayTeam.teamName", "球队名称", "string", true,
                "球队名称。", "示例客队");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].awayTeam.teamTags", "原始球队标签", "array<string>", false,
                "开放标签原文，不映射固定字典；AI应结合球队和比赛语境自行解释。", "[\"客战保守\",\"防守反击\"]");
        addManualField(fields, "球队上下文", "input.businessContext.matches[].awayTeam.seasonInfoPoints", "赛季球队信息点", "array<object>", false,
                "T类型赛季球队信息点，承载长期球队特征。", "见信息点字段");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].infoPointId 或 matches[].matchInfoPoints[].infoPointId", "信息点主键", "number", true,
                "信息点主键，用于证据追踪。seasonInfoPoints和matchInfoPoints共用该结构。", "7001");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].infoPointType 或 matches[].matchInfoPoints[].infoPointType", "信息点类型", "string", true,
                "T表示赛季球队信息点，M表示比赛临时信息点。", "T");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].pointTitle 或 matches[].matchInfoPoints[].pointTitle", "信息点标题", "string", true,
                "信息点摘要标题。", "主力前锋伤缺");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].pointContent 或 matches[].matchInfoPoints[].pointContent", "信息点内容", "string", true,
                "信息点正文事实；AI只能引用，不得扩写为未提供的事实。", "球队官方确认主力前锋将缺席本场比赛。");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].sourceName 或 matches[].matchInfoPoints[].sourceName", "来源名称", "string", false,
                "信息来源名称，用于判断可信度。", "球队官网");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].sourceUrl 或 matches[].matchInfoPoints[].sourceUrl", "来源地址", "string", false,
                "信息来源URL，仅用于审计。", "https://example.com/news/1");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].sourcePublishTime 或 matches[].matchInfoPoints[].sourcePublishTime", "来源发布时间", "datetime", false,
                "来源发布时间，不得晚于input.dataAsOf。", "2026-11-17 18:00:00");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].collectTime 或 matches[].matchInfoPoints[].collectTime", "采集时间", "datetime", false,
                "系统采集该信息点的时间。", "2026-11-17 18:05:00");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].effectiveStartTime 或 matches[].matchInfoPoints[].effectiveStartTime", "生效开始时间", "datetime", false,
                "信息点有效区间开始时间。", "2026-11-17 00:00:00");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].effectiveEndTime 或 matches[].matchInfoPoints[].effectiveEndTime", "生效结束时间", "datetime", false,
                "信息点有效区间结束时间。", "2026-11-19 00:00:00");
        addManualField(fields, "信息点", "matches[].(homeTeam|awayTeam).seasonInfoPoints[].teamIds 或 matches[].matchInfoPoints[].teamIds", "关联球队主键", "array<number>", false,
                "信息点当前有效关联的球队主键，用于确认信息点归属。", "[301]");
        addManualField(fields, "证据", "input.evidenceCatalog[].evidenceCode", "证据编码", "string", true,
                "系统冻结的证据编码，可作为分析引用；人工契约不强制返回证据链。", "S-I501-7001");
        addManualField(fields, "证据", "input.evidenceCatalog[].matchId", "比赛主键", "number", false,
                "证据所属比赛；球队级证据也归入对应比赛。", "501");
        addManualField(fields, "证据", "input.evidenceCatalog[].evidenceType", "证据类型", "string", false,
                "证据业务类型，如ODDS、SEASON_INFO_POINT、MATCH_INFO_POINT或RETRIEVAL。", "SEASON_INFO_POINT");
        addManualField(fields, "证据", "input.evidenceCatalog[].title", "证据标题", "string", true,
                "证据可读标题。", "信息点：主力前锋伤缺");
        addManualField(fields, "证据", "input.evidenceCatalog[].content", "证据内容", "string", true,
                "证据正文事实，不得改写为更确定的结论。", "infoPointType=T; title=...; content=...");
        addManualField(fields, "证据", "input.evidenceCatalog[].sourceName", "来源名称", "string", false,
                "证据来源名称。", "球队官网");
        addManualField(fields, "证据", "input.evidenceCatalog[].sourceUrl", "来源地址", "string", false,
                "证据来源URL。", "https://example.com/news/1");
        addManualField(fields, "证据", "input.evidenceCatalog[].publishedTime", "发布时间", "datetime", false,
                "证据发布时间。", "2026-11-17 18:00:00");
        addManualField(fields, "证据", "input.evidenceCatalog[].query", "检索查询词", "string", false,
                "自动检索证据对应的查询词；用户证据通常为空。", "主队 伤停");
        addManualField(fields, "证据", "input.evidenceCatalog[].sourceTier", "来源层级", "string", false,
                "OFFICIAL/MAJOR_MEDIA/SPECIALIST/AGGREGATOR/UNKNOWN，由系统归类，AI不得自行改写。", "OFFICIAL");
        addManualField(fields, "证据", "input.evidenceCatalog[].freshnessClass", "新鲜度", "string", false,
                "CURRENT/RECENT/STALE/UNKNOWN，由系统按dataAsOf计算。", "RECENT");

        addManualField(fields, "响应契约", "responseContract.schemaVersion", "结果契约版本", "string", true,
                "导入结果必须返回1.0。", "1.0");
        addManualField(fields, "响应契约", "responseContract.taskType", "结果任务类型", "string", true,
                "导入结果必须返回DORTMUND_RETURN_COMBINATION。", "DORTMUND_RETURN_COMBINATION");
        addManualField(fields, "响应契约", "responseContract.taskId", "当前批次编码", "string", true,
                "导入结果taskId必须与该值完全一致。", "RET202611180001");
        addManualField(fields, "响应契约", "responseContract.requiredTopLevelFields", "顶层必填字段", "array<string>", true,
                "结果JSON必须包含的五个顶层字段。", "[\"schemaVersion\",\"taskType\",\"taskId\",\"analysisContent\",\"candidates\"]");
        addManualField(fields, "响应契约", "responseContract.candidateRules", "候选规则", "array<string>", true,
                "候选数量、选择数量、matchId、selectionType、权重和禁止字段等强制规则。", "candidates必须为1至10条...");
        addManualField(fields, "响应契约", "responseContract.selectionTypes", "选择类型字典", "object", true,
                "HW主胜、DR平局、AW客胜、HH让分主胜、HD让分平局、HA让分客胜。", "{\"HW\":\"主胜\"}");
        addManualField(fields, "响应契约", "responseContract.outputExample", "输出示例", "object", true,
                "仅说明字段形状；实际内容必须是基于input的真实分析结果，不得照抄示例。", "见结果模板");
        return fields;
    }

    /**
     * 构建人工结果导入字段说明。
     *
     * @return 按结果、候选和选择分组的字段清单
     */
    private List<Fx67llDortmundReturnManualTemplateField> buildManualResultFields() {
        List<Fx67llDortmundReturnManualTemplateField> fields = new ArrayList<>();
        addManualField(fields, "结果顶层", "schemaVersion", "契约版本", "string", true,
                "固定为1.0；版本不符时后端拒绝导入。", "1.0");
        addManualField(fields, "结果顶层", "taskType", "任务类型", "string", true,
                "固定为DORTMUND_RETURN_COMBINATION。", "DORTMUND_RETURN_COMBINATION");
        addManualField(fields, "结果顶层", "taskId", "批次编码", "string", true,
                "必须等于导出任务的taskId，即当前批次编码。", "RET202611180001");
        addManualField(fields, "结果顶层", "analysisContent", "整体分析摘要", "string", true,
                "中文分析摘要，说明候选如何同时满足预定义和补充业务目标；不得包含金额计算结果。", "综合球队标签、赛季信息点和赔率结构...");
        addManualField(fields, "结果顶层", "candidates", "候选组合数组", "array<object>", true,
                "1至10条候选；每条候选包含标题、理由、可选复核信息和1至8个比赛选择。", "见候选字段");
        addManualField(fields, "候选", "candidates[].title", "候选标题", "string", true,
                "简短中文标题，突出组合策略，不要求包含金额或倍率。", "主场稳定型两场组合");
        addManualField(fields, "候选", "candidates[].reason", "候选理由", "string", true,
                "说明该组合如何满足业务目标、标签和信息点，不得只写“综合判断”。", "两场均具备主场稳定性证据...");
        addManualField(fields, "候选", "candidates[].confidence", "候选置信度", "number", false,
                "可选，0至100的整数，仅用于复核展示，不参与金额计算；建议填写。", "78");
        addManualField(fields, "候选", "candidates[].riskNotes", "候选风险提示", "string", false,
                "可选，说明信息缺口、冲突证据和不确定性；建议填写，前端会以醒目风险块展示。", "客队近期防守数据样本较小。");
        addManualField(fields, "候选", "candidates[].selections", "比赛选择数组", "array<object>", true,
                "1至8条；同一候选内matchId不得重复，且必须来自input.odds。", "见选择字段");
        addManualField(fields, "选择", "candidates[].selections[].matchId", "比赛主键", "number", true,
                "必须来自input.odds[].matchId。", "501");
        addManualField(fields, "选择", "candidates[].selections[].selectionType", "选择类型", "string", true,
                "只能为HW、DR、AW、HH、HD或HA，且对应赔率必须存在。", "HW");
        addManualField(fields, "选择", "candidates[].selections[].appliedWeight", "AI评估权重", "number", true,
                "0.0000至1.0000，最多四位小数；仅解释评估倾向，不参与倍率、回报或金额计算。S模式必须与权威手工权重一致。", "0.8000");
        addManualField(fields, "选择", "candidates[].selections[].reason", "选择理由", "string", true,
                "逐场说明选择依据，至少覆盖赔率、球队标签、赛季/比赛信息点或业务目标中的相关因素。", "主队主场稳定且客队存在伤停...");
        return fields;
    }

    /**
     * 追加一个字段说明。
     */
    private void addManualField(List<Fx67llDortmundReturnManualTemplateField> fields, String group, String path,
            String label, String type, boolean required, String description, String example) {
        Fx67llDortmundReturnManualTemplateField field = new Fx67llDortmundReturnManualTemplateField();
        field.setGroup(group);
        field.setPath(path);
        field.setLabel(label);
        field.setType(type);
        field.setRequired(required);
        field.setDescription(description);
        field.setExample(example);
        fields.add(field);
    }

    /**
     * 第三方AI处理提示词。提示词只描述通用规则，不包含任何批次业务数据。
     */
    private String buildManualExportPrompt() {
        return "你是 FODCS Dortmund 回报组合的第三方分析助手。"
                + "你会收到一个《回报组合人工AI处理任务》JSON。请严格按以下规则处理：\n"
                + "一、数据边界\n"
                + "1. input 是唯一可信的业务事实源。不得虚构比赛、球队、标签、信息点、赔率或选择类型。\n"
                + "2. generationMode=S 时，input.odds 中的六类权重是用户权威权重，必须原样尊重，不得调整或改写；但仍须综合标签、信息点、赔率和业务目标说明选择理由，不能只依据权重。\n"
                + "3. generationMode=A 时，由你自主选择比赛并给出 0.0000-1.0000 的评估权重；应主动补充必要信息，但不得把未经核实的平台外信息伪装成 input 事实。\n"
                + "4. businessContext 中的 teamTags 是原始开放标签列表，不映射固定字典，请结合球队和比赛语境自行解释；标签体系可能扩展，不得假设固定枚举。\n"
                + "5. T 类型赛季球队信息点和 M 类型比赛临时信息点都是证据，不得扩写为更确定的结论；信息冲突时应降低置信度并写明风险。\n"
                + "二、分析要求\n"
                + "1. 必须同时满足 input.businessObjectives 和 input.supplementaryObjectives；补充目标不得覆盖、替换或弱化预定义目标。\n"
                + "2. 每条候选包含 1-8 场比赛；同一候选内 matchId 不得重复；matchId 必须来自 input.odds。\n"
                + "3. selectionType 只能为 HW、DR、AW、HH、HD 或 HA，且必须存在对应赔率。\n"
                + "4. appliedWeight 使用 0.0000-1.0000，最多四位小数；它只解释评估倾向，不得用于计算组合倍率、预计回报、偏差或任何金额。\n"
                + "5. 输出 1-10 条候选，按你认为的业务质量从高到低排列。\n"
                + "三、输出格式\n"
                + "只返回一个 JSON 对象，不要 Markdown 代码围栏、不要解释文字、不要供应商/模型/Token/费用信息。"
                + "顶层字段必须为 schemaVersion=1.0、taskType=DORTMUND_RETURN_COMBINATION、taskId=导出任务中的 taskId、analysisContent、candidates。"
                + "candidates 每项包含 title、reason、可选 confidence（0-100）、可选 riskNotes、selections；"
                + "selections 每项包含 matchId、selectionType、appliedWeight、reason。"
                + "不得返回组合倍率、预计回报、偏差金额或任何自行计算的金额字段。";
    }

    /**
     * 第三方AI结果修复提示词。只修复格式，不改变真实分析结论。
     */
    private String buildManualImportPrompt() {
        return "你是 FODCS Dortmund 回报组合的 JSON 契约修复器。"
                + "输入是一段第三方AI分析结果和目标结果模板。请只修正结构与格式，保留原有真实分析结论。\n"
                + "修复规则：\n"
                + "1. 只输出一个 JSON 对象，不要 Markdown 代码围栏、前后说明或注释。\n"
                + "2. schemaVersion 必须为 1.0，taskType 必须为 DORTMUND_RETURN_COMBINATION，taskId 必须保持原值。\n"
                + "3. analysisContent 必须保留为中文分析摘要；candidates 必须为 1-10 条。\n"
                + "4. 每条候选保留 title、reason 和 selections；selections 必须为 1-8 条，同一候选内 matchId 不得重复。\n"
                + "5. selectionType 只能为 HW、DR、AW、HH、HD 或 HA；appliedWeight 必须在 0.0000-1.0000 且最多四位小数。\n"
                + "6. 不得新增不存在的比赛或选择，不得编造理由，不得返回组合倍率、预计回报、偏差、金额、供应商、模型、Token 或费用字段。\n"
                + "7. 如果原结果缺少必要字段，只能依据原分析内容补齐结构；无法确定的内容应删除对应候选，而不是编造。\n"
                + "8. 若原结果包含 confidence 或 riskNotes，可原样保留；它们是可选复核字段。";
    }

    /**
     * 导入第三方AI标准化结果。
     * <p>
     * 待人工导入和遗留失败批次可以导入，且在行锁内再次确认状态，防止自动worker或重复请求覆盖结果。
     * 标准化正文先由共用解析器转换为AI业务输出，再复用自动AI同一套候选校验、去重、
     * 金额复算和组合落库逻辑。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Fx67llDortmundReturnManualImportResult importManualResult(Long generationId,
            Fx67llDortmundReturnManualImportRequest request) {
        if (request == null || blank(request.getResponseContent())) {
            throw new ServiceException("人工AI标准化结果不能为空！");
        }
        String responseContent = request.getResponseContent();
        // 与前端 Blob 计算方式保持一致，按 UTF-8 字节数限制，避免中文字符长度判断不一致
        if (responseContent.getBytes(StandardCharsets.UTF_8).length > MAX_MANUAL_RESPONSE_BYTES) {
            throw new ServiceException("人工AI标准化结果超过2MB，请压缩或精简后重试！");
        }
        Fx67llDortmundReturnGeneration locked = fx67llDortmundReturnGenerationMapper
                .selectGenerationForUpdate(generationId);
        assertGenerationScope(locked);
        if (!isManualImportableStatus(locked.getGenerationStatus())) {
            throw new ServiceException("仅待人工导入或遗留失败批次允许导入标准化AI结果！");
        }
        if (fx67llDortmundReturnCombinationMapper.countByGenerationId(locked.getGenerationId()) > 0) {
            throw new ServiceException("批次已存在组合数据，不能再次导入人工AI结果！");
        }
        GenerationSnapshot snapshot = parseGenerationSnapshot(locked.getRequestSnapshot());
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output =
                returnCombinationAiResponseParser.parseManualResponse(
                        responseContent, locked.getGenerationCode());
        String aiResultSnapshot = buildAiResultSnapshot(output, MANUAL_AI_CONTRACT_VERSION);
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput manualInput = buildAiInput(snapshot);
        manualInput.setBusinessObjectives(buildManualBusinessObjectives(snapshot.request));
        String manualInputSnapshot = JSON.toJSONString(manualInput);
        List<PreparedCombination> prepared = prepareCombinations(
                output, snapshot.request, snapshot.odds, null, false);
        if (prepared.isEmpty()) {
            throw new ServiceException("人工AI结果未生成可用回报组合，请检查格式、比赛和选择类型！");
        }
        String username = SecurityUtils.getUsername();
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
        int rows = fx67llDortmundReturnGenerationMapper.completeManualGenerationSuccess(
                locked.getGenerationId(), prepared.size(), "人工AI标准化结果导入成功。", username,
                aiResultSnapshot, manualInputSnapshot);
        if (rows != 1) {
            throw new ServiceException("人工AI处理完成状态写入失败，请刷新后重试！");
        }
        Fx67llDortmundReturnManualImportResult result = new Fx67llDortmundReturnManualImportResult();
        result.setGenerationId(locked.getGenerationId());
        result.setGenerationCode(locked.getGenerationCode());
        result.setGeneratedCount(prepared.size());
        result.setResultSource(RESULT_SOURCE_MANUAL);
        result.setFinishedTime(now);
        return result;
    }

    private Fx67llDortmundReturnGeneration requireManualTaskGeneration(Long generationId) {
        if (generationId == null) {
            throw new ServiceException("生成批次主键不能为空！");
        }
        Fx67llDortmundReturnGeneration generation = fx67llDortmundReturnGenerationMapper
                .selectFx67llDortmundReturnGenerationByGenerationId(generationId);
        assertGenerationScope(generation);
        if (!isManualImportableStatus(generation.getGenerationStatus())) {
            throw new ServiceException("仅待人工导入或遗留失败批次可以导出人工AI任务！");
        }
        if (fx67llDortmundReturnCombinationMapper.countByGenerationId(generation.getGenerationId()) > 0) {
            throw new ServiceException("批次已存在组合数据，不能再次导出人工AI任务！");
        }
        return generation;
    }

    private boolean isManualImportableStatus(String generationStatus) {
        return STATUS_MANUAL_PENDING.equals(generationStatus) || STATUS_FAILED.equals(generationStatus);
    }

    private void requireResultSource(Fx67llDortmundReturnGeneration generation, String expectedSource) {
        if (!expectedSource.equals(generation.getResultSource())) {
            throw new ServiceException(RESULT_SOURCE_MANUAL.equals(expectedSource)
                    ? "该客户端请求号已用于自动AI批次，请调整条件后重试！"
                    : "该客户端请求号已用于人工AI批次，请调整条件后重试！");
        }
    }

    private void assertGenerationScope(Fx67llDortmundReturnGeneration generation) {
        if (generation == null || (!hasAllDataScope()
                && !SecurityUtils.getUserId().equals(generation.getUserId()))) {
            throw new ServiceException("生成批次不存在或没有访问权限！");
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
            EvidenceBundle evidenceBundle = buildSuppliedEvidenceBundle(snapshot);
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput aiInput =
                    buildAiInput(snapshot, evidenceBundle.getAll(), null);
            List<JSONObject> callTraces = new ArrayList<>();
            if ("A".equals(request.getGenerationMode())) {
                // 【通用AI接入点】自动链路只通过端口发起证据规划，通用AI模块不得绕过计划校验。
                if (!returnEvidenceRetrievalPort.isConfigured()) {
                    throw new ServiceException("AI模式需要配置证据检索服务，请设置DORTMUND_AI_RETRIEVAL_URL！");
                }
                if (fx67llDortmundReturnGenerationMapper.startEvidenceWork(
                        generation.getGenerationId(), workerId) != 1) {
                    throw new ServiceException("自动AI证据流程启动失败，生成批次租约已失效！");
                }
                // 【通用AI接入点】规划输入在调用前独立冻结；通用层只返回正文、请求号和治理统计。
                String planInputSnapshot = JSON.toJSONString(aiInput);
                if (fx67llDortmundReturnGenerationMapper.savePlanInputSnapshot(
                        generation.getGenerationId(), workerId, planInputSnapshot) != 1) {
                    throw new ServiceException("自动AI规划输入快照保存失败！");
                }
                ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation planInvocation =
                        buildAiInvocation(generation, AI_OPERATION_PLAN, AI_PLAN_TIMEOUT_MILLIS,
                                true, MAX_RETRIEVAL_QUERIES, MAX_RETRIEVAL_RESULTS_PER_QUERY);
                long planStartedAt = System.currentTimeMillis();
                ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan plan =
                        returnCombinationAiAnalysisPort.planEvidence(aiInput, planInvocation);
                long planDuration = System.currentTimeMillis() - planStartedAt;
                List<ReturnCombinationAiAnalysisPort.EvidenceQuery> queries =
                        validateEvidencePlan(plan, oddsList);
                String planSnapshot = buildEvidencePlanSnapshot(plan, queries);
                JSONObject planTrace = buildAiCallTrace(planInvocation, "SUCCESS", planDuration,
                        plan == null ? null : plan.getAiRunRef());
                callTraces.add(planTrace);
                if (fx67llDortmundReturnGenerationMapper.saveEvidencePlan(
                        generation.getGenerationId(), workerId, planSnapshot,
                        limitRunRef(plan == null ? null : plan.getAiRunRef()),
                        JSON.toJSONString(callTraces)) != 1) {
                    throw new ServiceException("自动AI证据检索计划保存失败！");
                }
                evidenceBundle.addAll(retrieveEvidence(queries));
                if (fx67llDortmundReturnGenerationMapper.updateEvidenceStatus(
                        generation.getGenerationId(), workerId, EVIDENCE_STATUS_READY, null) != 1) {
                    throw new ServiceException("自动AI证据检索状态写入失败！");
                }
                aiInput = buildAiInput(snapshot, evidenceBundle.getAll(), evidenceBundle.getRetrieved());
            }
            // 【通用AI接入点】最终分析输入单独冻结，确保规划与分析两次业务调用的输入均可复核。
            String analysisInputSnapshot = JSON.toJSONString(aiInput);
            if (fx67llDortmundReturnGenerationMapper.saveAnalysisInputSnapshot(
                    generation.getGenerationId(), workerId, analysisInputSnapshot) != 1) {
                throw new ServiceException("自动AI分析输入快照保存失败！");
            }
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation analysisInvocation =
                    buildAiInvocation(generation, AI_OPERATION_ANALYZE, AI_ANALYSIS_TIMEOUT_MILLIS,
                            false, 0, 0);
            long analysisStartedAt = System.currentTimeMillis();
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output =
                    returnCombinationAiAnalysisPort.analyze(aiInput, analysisInvocation);
            long analysisDuration = System.currentTimeMillis() - analysisStartedAt;
            callTraces.add(buildAiCallTrace(analysisInvocation, "SUCCESS", analysisDuration,
                    output == null ? null : output.getAiRunRef()));
            List<PreparedCombination> prepared = prepareCombinations(
                    output, request, oddsList, evidenceBundle, true);
            if (prepared.isEmpty()) {
                throw new ServiceException("AI未生成可用的回报组合！");
            }
            persistSuccess(generation, prepared, output, evidenceBundle, analysisInputSnapshot,
                    JSON.toJSONString(callTraces));
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
        JSONArray matchIdValues = value.getJSONArray("matchIds");
        if (matchIdValues == null) {
            throw new ServiceException("生成批次缺少比赛列表快照！");
        }
        request.setMatchIds(matchIdValues.toJavaList(Long.class));
        request.setSupplementaryObjectives(parseStringList(value.getJSONArray("supplementaryObjectives")));
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
        List<String> businessObjectives = parseStringList(value.getJSONArray("businessObjectives"));
        if (businessObjectives.isEmpty()) {
            // 兼容V2.0之前未冻结预定义目标的快照，仅重建后端目标，不补造历史上下文。
            businessObjectives = buildBusinessObjectives(request);
        }
        ReturnCombinationAiBusinessContext businessContext = new ReturnCombinationAiBusinessContext();
        JSONObject contextValue = value.getJSONObject("businessContext");
        if (contextValue != null && !contextValue.isEmpty()) {
            businessContext = contextValue.toJavaObject(ReturnCombinationAiBusinessContext.class);
            if (businessContext == null) {
                businessContext = new ReturnCombinationAiBusinessContext();
            }
        }
        if (blank(businessContext.getContextVersion())) {
            businessContext.setContextVersion(ReturnCombinationAiBusinessContext.CONTEXT_VERSION);
        }
        Date dataAsOf = parseContextDate(value.getString("dataAsOf"));
        if (dataAsOf == null) {
            throw new ServiceException("生成批次缺少业务快照数据截止时间！");
        }
        return new GenerationSnapshot(request, oddsList, businessObjectives,
                request.getSupplementaryObjectives(), businessContext, dataAsOf);
    }

    private List<String> buildBaseBusinessObjectives() {
        List<String> objectives = new ArrayList<>();
        objectives.add("仅从输入odds中给出的比赛和选择类型生成候选，不得虚构比赛、赔率或选择类型。");
        objectives.add("每条候选必须覆盖1至8场不重复比赛，候选之间要去重；避免对同一风险重复下注。");
        objectives.add("预计回报应尽量接近目标金额，但不得为了贴近目标而违反赔率快照或选择约束。");
        objectives.add("结合matchSummary中的球队、开赛时间，以及独立字段中的让球值和有效odds判断合理性，并输出简短中文理由。");
        objectives.add("将businessContext中的球队标签视为原始开放标签并结合语境解释，利用赛季球队信息点和比赛临时信息点作为判断证据，不得虚构标签、信息点或来源事实。");
        return objectives;
    }

    private List<String> buildBusinessObjectives(Fx67llDortmundReturnGenerateRequest request) {
        List<String> objectives = buildBaseBusinessObjectives();
        if ("S".equals(request.getGenerationMode())) {
            objectives.add("Subjective Mode必须原样尊重输入中的手工权重，高权重代表用户更认可的选择，AI不得调整或改写；"
                    + "但仍须结合标签、信息点、赔率、赛程和业务目标形成完整证据链，禁止只依据权重生成组合。");
        } else {
            objectives.add("AI Mode由AI自主识别论证权重所需的信息缺口，系统按受控计划检索补充证据；"
                    + "AI须结合用户信息与检索证据独立调整每个选择的0至1评估权重（最多四位小数），并输出完整证据链。");
        }
        objectives.add("每条候选和每个选择都必须引用证据链因子说明依据；自动AI结果必须包含逐因子的weightImpact与完整weightRationale。");
        objectives.add("appliedWeight只用于说明AI评估倾向，禁止用它计算组合倍率、预计回报、偏差或任何金额。");
        return objectives;
    }

    /**
     * 人工交换契约1.0不包含证据链字段，因此业务目标不得要求第三方AI返回平台内部证据因子。
     */
    private List<String> buildManualBusinessObjectives(Fx67llDortmundReturnGenerateRequest request) {
        List<String> objectives = buildBaseBusinessObjectives();
        if ("S".equals(request.getGenerationMode())) {
            objectives.add("Subjective Mode必须原样尊重输入中的手工权重，不得调整或改写；"
                    + "仍须综合球队标签、信息点、赔率、赛程和业务目标说明选择理由，禁止只依据权重生成组合。");
        } else {
            objectives.add("AI Mode由第三方AI在平台外完成信息补充和权重判断，须独立确定每个选择的0至1评估权重（最多四位小数）并说明理由。");
        }
        objectives.add("人工任务仅接受1.0结构化结果；不得返回供应商、模型、Token、费用或平台内部证据编码。");
        objectives.add("appliedWeight只用于说明AI评估倾向，禁止用它计算组合倍率、预计回报、偏差或任何金额。");
        return objectives;
    }

    private ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput buildAiInput(GenerationSnapshot snapshot) {
        Fx67llDortmundReturnGenerateRequest request = snapshot.request;
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput aiInput =
                new ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput();
        aiInput.setGenerationMode(request.getGenerationMode());
        aiInput.setDataAsOf(formatContextTime(snapshot.dataAsOf));
        aiInput.setInputAmount(request.getInputAmount());
        aiInput.setTargetAmount(request.getTargetAmount());
        aiInput.setOdds(buildAiOddsList(snapshot.odds, request.getGenerationMode()));
        aiInput.setBusinessObjectives(snapshot.businessObjectives);
        aiInput.setSupplementaryObjectives(snapshot.supplementaryObjectives);
        aiInput.setBusinessContext(snapshot.businessContext);
        return aiInput;
    }

    private ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput buildAiInput(GenerationSnapshot snapshot,
            List<Fx67llDortmundReturnEvidence> suppliedEvidence,
            List<Fx67llDortmundReturnEvidence> retrievedEvidence) {
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput aiInput = buildAiInput(snapshot);
        aiInput.setEvidenceCatalog(toEvidenceContexts(suppliedEvidence, snapshot.dataAsOf));
        aiInput.setRetrievedEvidence(toEvidenceContexts(retrievedEvidence, snapshot.dataAsOf));
        return aiInput;
    }

    private JSONObject buildManualResponseContract(Fx67llDortmundReturnGeneration generation,
            GenerationSnapshot snapshot) {
        JSONObject contract = new JSONObject();
        contract.put("schemaVersion", MANUAL_AI_CONTRACT_VERSION);
        contract.put("taskType", ReturnCombinationAiResponseParser.TASK_TYPE);
        contract.put("taskId", generation.getGenerationCode());

        JSONArray requiredFields = new JSONArray();
        requiredFields.add("schemaVersion");
        requiredFields.add("taskType");
        requiredFields.add("taskId");
        requiredFields.add("analysisContent");
        requiredFields.add("candidates");
        contract.put("requiredTopLevelFields", requiredFields);

        contract.put("candidateRules", buildManualCandidateRules());
        contract.put("selectionTypes", buildManualSelectionTypes());

        if (snapshot.odds == null || snapshot.odds.isEmpty()) {
            throw new ServiceException("生成批次缺少提交时的odds快照！");
        }
        Fx67llDortmundOdds exampleOdds = snapshot.odds.get(0);
        String exampleSelectionType = firstAvailableSelectionType(exampleOdds);
        if (exampleOdds.getMatchId() == null || exampleSelectionType == null) {
            throw new ServiceException("生成批次odds快照没有可用倍率！");
        }

        JSONObject example = new JSONObject();
        example.put("schemaVersion", MANUAL_AI_CONTRACT_VERSION);
        example.put("taskType", ReturnCombinationAiResponseParser.TASK_TYPE);
        example.put("taskId", generation.getGenerationCode());
        example.put("analysisContent", "简要中文分析摘要");
        JSONArray candidates = new JSONArray();
        JSONObject candidate = new JSONObject();
        candidate.put("title", "候选组合标题");
        candidate.put("reason", "组合简要理由");
        candidate.put("confidence", 78);
        candidate.put("riskNotes", "可选：说明信息缺口、冲突证据或不确定性。");
        JSONArray selections = new JSONArray();
        JSONObject selection = new JSONObject();
        selection.put("matchId", exampleOdds.getMatchId());
        selection.put("selectionType", exampleSelectionType);
        selection.put("appliedWeight", 0.8);
        selection.put("reason", "逐场选择理由");
        selections.add(selection);
        candidate.put("selections", selections);
        candidates.add(candidate);
        example.put("candidates", candidates);
        contract.put("outputExample", example);
        return contract;
    }

    /**
     * 构建人工AI结果候选规则，供真实任务与标准模板复用。
     */
    private JSONArray buildManualCandidateRules() {
        JSONArray candidateRules = new JSONArray();
        candidateRules.add("candidates必须为1至" + ReturnCombinationAiResponseParser.MAX_MANUAL_CANDIDATES
                + "条，每条包含title、reason和selections。");
        candidateRules.add("必须同时满足input.businessObjectives和input.supplementaryObjectives，补充目标不得覆盖预定义目标。");
        candidateRules.add("businessContext中的球队标签为原始标签列表，不得假定固定标签体系；信息点与来源事实不得虚构。");
        candidateRules.add("selections必须为1至8条，同一候选内matchId不得重复。");
        candidateRules.add("matchId必须来自input.odds，selectionType只能为HW、DR、AW、HH、HD或HA。");
        candidateRules.add("appliedWeight取值范围为0至1，最多四位小数；AI Mode必填，Subjective Mode仅作参考且不参与金额计算。");
        candidateRules.add("confidence与riskNotes为可选复核字段；confidence如填写必须为0至100整数，riskNotes用于说明信息缺口与冲突，不参与金额计算。");
        candidateRules.add("不得返回组合倍率、预计回报、偏差金额或任何自行计算的金额字段。");
        candidateRules.add("只返回单个JSON对象，不得附加模型说明、Markdown代码围栏或治理元数据。");
        return candidateRules;
    }

    /**
     * 构建人工AI六类选择字典，供真实任务与标准模板复用。
     */
    private JSONObject buildManualSelectionTypes() {
        JSONObject selectionTypes = new JSONObject();
        selectionTypes.put("HW", "主胜");
        selectionTypes.put("DR", "平局");
        selectionTypes.put("AW", "客胜");
        selectionTypes.put("HH", "让分主胜");
        selectionTypes.put("HD", "让分平局");
        selectionTypes.put("HA", "让分客胜");
        return selectionTypes;
    }

    private String firstAvailableSelectionType(Fx67llDortmundOdds odds) {
        if (odds.getHomeWinOdds() != null) {
            return "HW";
        }
        if (odds.getDrawOdds() != null) {
            return "DR";
        }
        if (odds.getAwayWinOdds() != null) {
            return "AW";
        }
        if (odds.getHandicapHomeWinOdds() != null) {
            return "HH";
        }
        if (odds.getHandicapDrawOdds() != null) {
            return "HD";
        }
        if (odds.getHandicapAwayWinOdds() != null) {
            return "HA";
        }
        return null;
    }

    private void handleGenerationFailure(Fx67llDortmundReturnGeneration generation, Exception error) {
        String message = error == null || blank(error.getMessage())
                ? "回报组合异步生成失败，请稍后重试！" : limit(error.getMessage(), 900);
        int attempt = generation.getAttemptCount() == null ? 1 : generation.getAttemptCount();
        int maxAttempts = generation.getMaxAttempts() == null ? generationMaxAttempts : generation.getMaxAttempts();
        try {
            if ("A".equals(generation.getGenerationMode())) {
                fx67llDortmundReturnGenerationMapper.updateEvidenceStatus(generation.getGenerationId(), workerId,
                        EVIDENCE_STATUS_FAILED, limit(message, 1023));
            }
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
            value.setEvidenceChain(null);
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
            value.setEvidenceChain(null);
            if (value.getMatches() != null) {
                for (Fx67llDortmundReturnCombinationItem item : value.getMatches()) {
                    item.setSelectionReason(null);
                    item.setWeightRationale(null);
                    item.setEvidenceRefs(null);
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
            Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> oddsList,
            EvidenceBundle evidenceBundle, boolean evidenceRequired) {
        if (output == null || output.getCandidates() == null) {
            return Collections.emptyList();
        }
        Map<Long, Fx67llDortmundOdds> oddsMap = new HashMap<>();
        for (Fx67llDortmundOdds odds : oddsList) {
            oddsMap.put(odds.getMatchId(), odds);
        }
        Map<String, PreparedCombination> unique = new LinkedHashMap<>();
        for (ReturnCombinationAiAnalysisPort.Candidate candidate : output.getCandidates()) {
            PreparedCombination prepared = prepareOne(candidate, request, oddsMap, evidenceBundle, evidenceRequired);
            if (prepared != null) {
                unique.putIfAbsent(prepared.signature, prepared);
            }
        }
        List<PreparedCombination> result = new ArrayList<>(unique.values());
        result.sort(Comparator.comparing(value -> value.combination.getDeviationAmount().abs()));
        return result.size() > 10 ? new ArrayList<>(result.subList(0, 10)) : result;
    }

    private PreparedCombination prepareOne(ReturnCombinationAiAnalysisPort.Candidate candidate,
            Fx67llDortmundReturnGenerateRequest request, Map<Long, Fx67llDortmundOdds> oddsMap,
            EvidenceBundle evidenceBundle, boolean evidenceRequired) {
        if (candidate == null || blank(candidate.getTitle()) || candidate.getTitle().length() > 233
                || candidate.getSelections() == null || candidate.getSelections().isEmpty()
                || candidate.getSelections().size() > 8
                || (candidate.getConfidence() != null
                && (candidate.getConfidence() < 0 || candidate.getConfidence() > 100))
                || (evidenceRequired && candidate.getConfidence() == null)
                || (!blank(candidate.getRiskNotes())
                && candidate.getRiskNotes().length() > MAX_RISK_NOTES_LENGTH)
                || (evidenceRequired && blank(candidate.getRiskNotes()))) {
            return null;
        }
        Map<String, ReturnCombinationAiAnalysisPort.EvidenceFactor> evidenceFactors = evidenceRequired
                ? validateEvidenceChain(candidate, oddsMap, evidenceBundle) : Collections.emptyMap();
        if (evidenceRequired && evidenceFactors == null) {
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
            // 手工权重快照按0-1保存，空值保持NULL，不能用默认1.0000覆盖用户的“未填写”语义。
            BigDecimal manualWeight = DortmundWeightCodec.normalize(
                    rawManualWeight(odds, selection.getSelectionType()), "手工权重快照");
            // AI评估权重：仅用于解释选择倾向；后续金额计算只使用oddsValue，不读取appliedWeight。
            BigDecimal effectiveManualWeight = effectiveAiWeight(manualWeight, request.getGenerationMode());
            BigDecimal appliedWeight = "S".equals(request.getGenerationMode())
                    ? effectiveManualWeight : selection.getAppliedWeight();
            if (!DortmundWeightCodec.isValid(appliedWeight)) {
                return null;
            }
            appliedWeight = DortmundWeightCodec.normalize(appliedWeight, "AI评估权重");
            if (evidenceRequired && "S".equals(request.getGenerationMode())
                    && (selection.getAppliedWeight() == null
                    || !DortmundWeightCodec.same(selection.getAppliedWeight(), effectiveManualWeight, "AI评估权重"))) {
                // Subjective Mode由后端保证主观权重不被AI改写；契约值也必须是同一冻结权重。
                return null;
            }
            if (evidenceRequired && !validateSelectionEvidence(selection, request.getGenerationMode(), evidenceFactors)) {
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
            if (evidenceRequired) {
                item.setWeightRationale(limit(selection.getWeightRationale(), MAX_WEIGHT_RATIONALE_LENGTH));
                item.setEvidenceRefs(JSON.toJSONString(selection.getEvidenceRefs()));
                item.setCounterEvidenceRefs(JSON.toJSONString(
                        selection.getCounterEvidenceRefs() == null
                                ? Collections.emptyList() : selection.getCounterEvidenceRefs()));
            }
            items.add(item);
            signatureParts.add(selection.getMatchId() + ":" + selection.getSelectionType());
        }
        if (evidenceRequired) {
            for (ReturnCombinationAiAnalysisPort.EvidenceFactor factor : evidenceFactors.values()) {
                if (!seenMatches.contains(factor.getMatchId())) {
                    return null;
                }
            }
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
        combination.setAiConfidence(candidate.getConfidence());
        combination.setRiskNotes(limit(candidate.getRiskNotes(), MAX_RISK_NOTES_LENGTH));
        if (evidenceRequired) {
            combination.setEvidenceChain(JSON.toJSONString(candidate.getEvidenceChain()));
        }
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
     * 校验自动AI候选证据链及来源引用。任何无效因子都会使整个候选失效，避免伪造来源落库。
     */
    private Map<String, ReturnCombinationAiAnalysisPort.EvidenceFactor> validateEvidenceChain(
            ReturnCombinationAiAnalysisPort.Candidate candidate, Map<Long, Fx67llDortmundOdds> oddsMap,
            EvidenceBundle evidenceBundle) {
        List<ReturnCombinationAiAnalysisPort.EvidenceFactor> factors = candidate.getEvidenceChain();
        if (evidenceBundle == null || factors == null || factors.isEmpty()
                || factors.size() > MAX_EVIDENCE_FACTORS) {
            return null;
        }
        Map<String, ReturnCombinationAiAnalysisPort.EvidenceFactor> factorMap = new LinkedHashMap<>();
        for (ReturnCombinationAiAnalysisPort.EvidenceFactor factor : factors) {
            if (factor == null || blank(factor.getFactorCode())
                    || !factor.getFactorCode().equals(factor.getFactorCode().trim())
                    || !factor.getFactorCode().matches("[A-Za-z][A-Za-z0-9_-]{0,31}")
                    || factor.getMatchId() == null || !oddsMap.containsKey(factor.getMatchId())
                    || blank(factor.getFactorType()) || factor.getFactorType().length() > 64
                    || !EVIDENCE_SOURCE_TYPES.contains(factor.getSourceType())
                    || blank(factor.getSourceRef()) || factor.getSourceRef().length() > 64
                    || blank(factor.getObservation())
                    || factor.getObservation().length() > MAX_EVIDENCE_OBSERVATION_LENGTH
                    || blank(factor.getImpact()) || factor.getImpact().length() > 64
                    || factor.getWeightImpact() == null
                    || factor.getWeightImpact().compareTo(new BigDecimal("-100")) < 0
                    || factor.getWeightImpact().compareTo(new BigDecimal("100")) > 0
                    || factor.getConfidence() == null || factor.getConfidence() < 0
                    || factor.getConfidence() > 100) {
                return null;
            }
            if (!blank(factor.getConflictGroup())
                    && (!factor.getConflictGroup().equals(factor.getConflictGroup().trim())
                    || factor.getConflictGroup().length() > MAX_CONFLICT_GROUP_LENGTH
                    || !factor.getConflictGroup().matches("[A-Za-z0-9_-]+"))) {
                return null;
            }
            if (factorMap.put(factor.getFactorCode(), factor) != null) {
                return null;
            }
            Fx67llDortmundReturnEvidence evidence = evidenceBundle.findByCode(factor.getSourceRef());
            if (evidence == null || !factor.getMatchId().equals(evidence.getMatchId())) {
                return null;
            }
            boolean retrieval = EVIDENCE_SOURCE_RETRIEVAL.equals(factor.getSourceType());
            if (retrieval && !EVIDENCE_ORIGIN_RETRIEVED.equals(evidence.getEvidenceOrigin())) {
                return null;
            }
            if (!retrieval && !EVIDENCE_ORIGIN_SUPPLIED.equals(evidence.getEvidenceOrigin())) {
                return null;
            }
            if (EVIDENCE_SOURCE_MANUAL_WEIGHT.equals(factor.getSourceType())
                    && !"ODDS".equals(evidence.getEvidenceType())) {
                return null;
            }
        }
        return factorMap;
    }

    /**
     * 校验选择级证据引用。主观模式禁止仅凭权重论证；AI模式每个选择必须有同场检索证据。
     */
    private boolean validateSelectionEvidence(ReturnCombinationAiAnalysisPort.Selection selection,
            String generationMode, Map<String, ReturnCombinationAiAnalysisPort.EvidenceFactor> factorMap) {
        if (blank(selection.getWeightRationale()) || selection.getWeightRationale().length() < 20
                || selection.getWeightRationale().length() > MAX_WEIGHT_RATIONALE_LENGTH
                || selection.getEvidenceRefs() == null || selection.getEvidenceRefs().isEmpty()
                || selection.getEvidenceRefs().size() > MAX_EVIDENCE_FACTORS) {
            return false;
        }
        if (selection.getCounterEvidenceRefs() != null) {
            if (selection.getCounterEvidenceRefs().size() > MAX_COUNTER_EVIDENCE_REFS) {
                return false;
            }
            Set<String> counterRefs = new HashSet<>();
            for (String counterRef : selection.getCounterEvidenceRefs()) {
                ReturnCombinationAiAnalysisPort.EvidenceFactor factor = factorMap.get(counterRef);
                if (factor == null || !selection.getMatchId().equals(factor.getMatchId())
                        || !counterRefs.add(counterRef)) {
                    return false;
                }
            }
        }
        boolean hasRequiredEvidence = false;
        for (String evidenceRef : selection.getEvidenceRefs()) {
            ReturnCombinationAiAnalysisPort.EvidenceFactor factor = factorMap.get(evidenceRef);
            if (factor == null || !selection.getMatchId().equals(factor.getMatchId())) {
                return false;
            }
            if ("A".equals(generationMode)
                    && EVIDENCE_SOURCE_RETRIEVAL.equals(factor.getSourceType())) {
                hasRequiredEvidence = true;
            }
            if ("S".equals(generationMode)
                    && !EVIDENCE_SOURCE_MANUAL_WEIGHT.equals(factor.getSourceType())) {
                hasRequiredEvidence = true;
            }
        }
        return hasRequiredEvidence;
    }

    /**
     * 固化异步生成结果。
     * <p>
     * worker不依赖SecurityUtils：审计用户取自批次创建人；事务内锁定批次并再次校验worker/status，
     * 防止租约过期后新旧worker同时写入。批次固化不读取、绑定或修改任何组合图片。
     */
    private ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation buildAiInvocation(
            Fx67llDortmundReturnGeneration generation, String operation, long timeoutMillis,
            boolean searchAllowed, int maxQueries, int maxResultsPerQuery) {
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation invocation =
                new ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation();
        invocation.setOperation(operation);
        invocation.setSceneCode(AI_SCENE_CODE);
        invocation.setContractVersion(AUTO_AI_CONTRACT_VERSION);
        invocation.setBusinessKey(generation.getGenerationCode());
        invocation.setRequestId(limit(operation.toLowerCase() + "-" + generation.getGenerationCode()
                + "-" + UUID.randomUUID().toString().replace("-", ""), 128));
        invocation.setTimeoutMillis(timeoutMillis);
        ReturnCombinationAiAnalysisPort.ToolPolicy toolPolicy =
                new ReturnCombinationAiAnalysisPort.ToolPolicy();
        toolPolicy.setSearchAllowed(searchAllowed);
        toolPolicy.setMaxQueries(Math.max(maxQueries, 0));
        toolPolicy.setMaxResultsPerQuery(Math.max(maxResultsPerQuery, 0));
        invocation.setToolPolicy(toolPolicy);
        return invocation;
    }

    /**
     * 构建供应商无关的调用审计。通用AI模块只补充治理统计，不向业务表写入模型、Prompt或费用。
     */
    private JSONObject buildAiCallTrace(
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiInvocation invocation,
            String outcome, long durationMillis, String runRef) {
        JSONObject trace = new JSONObject();
        if (invocation == null) {
            return trace;
        }
        trace.put("operation", invocation.getOperation());
        trace.put("sceneCode", invocation.getSceneCode());
        trace.put("contractVersion", invocation.getContractVersion());
        trace.put("businessKey", invocation.getBusinessKey());
        trace.put("requestId", invocation.getRequestId());
        trace.put("timeoutMillis", invocation.getTimeoutMillis());
        ReturnCombinationAiAnalysisPort.ToolPolicy toolPolicy = invocation.getToolPolicy();
        if (toolPolicy != null) {
            trace.put("searchAllowed", toolPolicy.getSearchAllowed());
            trace.put("maxQueries", toolPolicy.getMaxQueries());
            trace.put("maxResultsPerQuery", toolPolicy.getMaxResultsPerQuery());
        }
        trace.put("outcome", outcome);
        trace.put("durationMillis", durationMillis);
        trace.put("runRef", limitRunRef(runRef));
        return trace;
    }

    private void persistSuccess(Fx67llDortmundReturnGeneration generation, List<PreparedCombination> prepared,
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output, EvidenceBundle evidenceBundle,
            String analysisInputSnapshot, String aiCallRefs) {
        if (evidenceBundle == null || evidenceBundle.getAll().isEmpty()) {
            throw new ServiceException("自动AI证据链为空，拒绝固化回报组合！");
        }
        if (blank(analysisInputSnapshot) || blank(aiCallRefs)) {
            throw new ServiceException("自动AI业务输入或调用审计快照为空，拒绝固化回报组合！");
        }
        String aiResultSnapshot = buildAiResultSnapshot(output, AUTO_AI_CONTRACT_VERSION);
        String aiAnalysisRunRef = limitRunRef(output == null ? null : output.getAiRunRef());
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
            for (Fx67llDortmundReturnEvidence evidence : evidenceBundle.getAll()) {
                evidence.setGenerationId(locked.getGenerationId());
                evidence.setCreateBy(username);
                evidence.setCreateTime(now);
            }
            fx67llDortmundReturnEvidenceMapper.insertBatch(evidenceBundle.getAll());
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
                    locked.getGenerationId(), workerId, prepared.size(), "生成成功。", username,
                    aiResultSnapshot, aiAnalysisRunRef, analysisInputSnapshot, aiCallRefs);
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
        request.setSupplementaryObjectives(normalizeSupplementaryObjectives(request.getSupplementaryObjectives()));
    }

    /**
     * 规范化用户补充业务目标。
     * <p>
     * 空行忽略、重复项去重；每条和总量均设上限，避免用户输入挤占AI上下文。预定义业务目标由
     * buildBusinessObjectives生成，不在此方法中合并或覆盖。
     */
    private List<String> normalizeSupplementaryObjectives(List<String> objectives) {
        List<String> normalized = new ArrayList<>();
        if (objectives == null) {
            return normalized;
        }
        Set<String> seen = new LinkedHashSet<>();
        int totalLength = 0;
        for (String objective : objectives) {
            String value = objective == null ? ""
                    : objective.replace('\r', ' ').replace('\n', ' ').trim();
            if (value.isEmpty() || !seen.add(value)) {
                continue;
            }
            if (value.length() > MAX_SUPPLEMENTARY_OBJECTIVE_LENGTH) {
                throw new ServiceException("每条补充业务目标不能超过"
                        + MAX_SUPPLEMENTARY_OBJECTIVE_LENGTH + "个字符！");
            }
            normalized.add(value);
            totalLength += value.length();
            if (normalized.size() > MAX_SUPPLEMENTARY_OBJECTIVES) {
                throw new ServiceException("补充业务目标最多填写" + MAX_SUPPLEMENTARY_OBJECTIVES + "条！");
            }
        }
        if (totalLength > MAX_SUPPLEMENTARY_OBJECTIVES_TOTAL_LENGTH) {
            throw new ServiceException("补充业务目标合计不能超过"
                    + MAX_SUPPLEMENTARY_OBJECTIVES_TOTAL_LENGTH + "个字符！");
        }
        return normalized;
    }

    private List<String> parseStringList(JSONArray values) {
        Set<String> result = new LinkedHashSet<>();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                String value = values.getString(i);
                if (!blank(value)) {
                    result.add(value.trim());
                }
            }
        }
        return new ArrayList<>(result);
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

    private String buildSnapshot(Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> odds,
            ReturnCombinationAiBusinessContext businessContext, Date dataAsOf) {
        JSONObject value = new JSONObject();
        value.put("snapshotVersion", SNAPSHOT_VERSION);
        value.put("dataAsOf", DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", dataAsOf));
        value.put("generationMode", request.getGenerationMode());
        value.put("inputAmount", request.getInputAmount());
        value.put("targetAmount", request.getTargetAmount());
        value.put("matchIds", request.getMatchIds());
        value.put("businessObjectives", buildBusinessObjectives(request));
        value.put("supplementaryObjectives", request.getSupplementaryObjectives());
        value.put("businessContext", businessContext);
        value.put("odds", copyOddsList(odds));
        return value.toJSONString();
    }

    /**
     * 构建提交时冻结的比赛、球队、标签和信息点上下文。
     * <p>
     * 只纳入有效性查询判定为有效的信息点；T类型按当前有效球队关系拆分到主客队，M类型直接归属比赛。
     * 球队标签保留原始文本，不经过字典映射，后续新增标签无需修改AI契约。
     */
    private ReturnCombinationAiBusinessContext buildBusinessContext(List<Long> matchIds) {
        ReturnCombinationAiBusinessContext context = new ReturnCombinationAiBusinessContext();
        context.setContextVersion(ReturnCombinationAiBusinessContext.CONTEXT_VERSION);
        List<ReturnCombinationAiBusinessContext.MatchContext> matches = new ArrayList<>();
        for (Long matchId : new LinkedHashSet<>(matchIds)) {
            Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchService
                    .selectFx67llDortmundBasicMatchByMatchId(matchId);
            if (match == null) {
                throw new ServiceException("所选比赛不存在，无法构建AI分析上下文！");
            }
            ReturnCombinationAiBusinessContext.MatchContext matchContext =
                    new ReturnCombinationAiBusinessContext.MatchContext();
            matchContext.setMatchId(match.getMatchId());
            matchContext.setMatchCode(match.getMatchCode());
            matchContext.setMatchTime(formatContextTime(match.getMatchTime()));
            matchContext.setSeasonId(match.getSeasonId());
            matchContext.setSeasonName(match.getSeasonName());

            ReturnCombinationAiBusinessContext.TeamContext homeTeam =
                    buildTeamContext(match.getHomeTeamId(), match.getHomeTeamName());
            ReturnCombinationAiBusinessContext.TeamContext awayTeam =
                    buildTeamContext(match.getAwayTeamId(), match.getAwayTeamName());

            List<Fx67llDortmundInfoPoint> infoPoints = fx67llDortmundInfoPointService
                    .selectFx67llDortmundInfoPointValidListByMatchId(matchId);
            if (infoPoints != null) {
                for (Fx67llDortmundInfoPoint infoPoint : infoPoints) {
                    if (infoPoint == null || !Boolean.TRUE.equals(infoPoint.getValid())) {
                        continue;
                    }
                    ReturnCombinationAiBusinessContext.InfoPointContext infoPointContext =
                            buildInfoPointContext(infoPoint);
                    if ("M".equals(infoPoint.getInfoPointType())) {
                        matchContext.getMatchInfoPoints().add(infoPointContext);
                    } else if ("T".equals(infoPoint.getInfoPointType())) {
                        if (relatesToTeam(infoPoint, match.getHomeTeamId())) {
                            homeTeam.getSeasonInfoPoints().add(infoPointContext);
                        }
                        if (relatesToTeam(infoPoint, match.getAwayTeamId())) {
                            awayTeam.getSeasonInfoPoints().add(infoPointContext);
                        }
                    }
                }
            }
            matchContext.setHomeTeam(homeTeam);
            matchContext.setAwayTeam(awayTeam);
            matches.add(matchContext);
        }
        context.setMatches(matches);
        return context;
    }

    private ReturnCombinationAiBusinessContext.TeamContext buildTeamContext(Long teamId, String fallbackTeamName) {
        ReturnCombinationAiBusinessContext.TeamContext teamContext =
                new ReturnCombinationAiBusinessContext.TeamContext();
        teamContext.setTeamId(teamId);
        teamContext.setTeamName(fallbackTeamName);
        if (teamId == null) {
            return teamContext;
        }
        Fx67llDortmundBasicTeam team = fx67llDortmundBasicTeamService
                .selectFx67llDortmundBasicTeamByTeamId(teamId);
        if (team == null) {
            throw new ServiceException("比赛关联球队不存在，无法构建AI分析上下文！");
        }
        if (!blank(team.getTeamName())) {
            teamContext.setTeamName(team.getTeamName());
        }
        teamContext.setTeamTags(splitTeamTags(team.getTeamTag()));
        return teamContext;
    }

    private ReturnCombinationAiBusinessContext.InfoPointContext buildInfoPointContext(
            Fx67llDortmundInfoPoint infoPoint) {
        ReturnCombinationAiBusinessContext.InfoPointContext value =
                new ReturnCombinationAiBusinessContext.InfoPointContext();
        value.setInfoPointId(infoPoint.getInfoPointId());
        value.setInfoPointType(infoPoint.getInfoPointType());
        value.setPointTitle(infoPoint.getPointTitle());
        value.setPointContent(infoPoint.getPointContent());
        value.setSourceName(infoPoint.getSourceName());
        value.setSourceUrl(infoPoint.getSourceUrl());
        value.setSourcePublishTime(formatContextTime(infoPoint.getSourcePublishTime()));
        value.setCollectTime(formatContextTime(infoPoint.getCollectTime()));
        value.setEffectiveStartTime(formatContextTime(infoPoint.getEffectiveStartTime()));
        value.setEffectiveEndTime(formatContextTime(infoPoint.getEffectiveEndTime()));
        value.setTeamIds(activeTeamIds(infoPoint));
        return value;
    }

    private List<Long> activeTeamIds(Fx67llDortmundInfoPoint infoPoint) {
        Set<Long> teamIds = new LinkedHashSet<>();
        if (infoPoint.getTeamRels() == null) {
            return new ArrayList<>(teamIds);
        }
        for (Fx67llDortmundInfoPointTeamRel relation : infoPoint.getTeamRels()) {
            if (relation != null && !"1".equals(relation.getRelationStatus()) && relation.getTeamId() != null) {
                teamIds.add(relation.getTeamId());
            }
        }
        return new ArrayList<>(teamIds);
    }

    private boolean relatesToTeam(Fx67llDortmundInfoPoint infoPoint, Long teamId) {
        return teamId != null && activeTeamIds(infoPoint).contains(teamId);
    }

    private List<String> splitTeamTags(String rawTags) {
        Set<String> tags = new LinkedHashSet<>();
        if (blank(rawTags)) {
            return new ArrayList<>(tags);
        }
        for (String tag : rawTags.split("[,，]")) {
            String value = tag == null ? "" : tag.trim();
            if (!value.isEmpty()) {
                tags.add(value);
            }
        }
        return new ArrayList<>(tags);
    }

    private String formatContextTime(Date value) {
        return value == null ? null : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", value);
    }

    /**
     * 从冻结快照构建已提交证据目录。证据编码与比赛绑定，供AI证据链稳定引用。
     */
    private EvidenceBundle buildSuppliedEvidenceBundle(GenerationSnapshot snapshot) {
        EvidenceBundle bundle = new EvidenceBundle();
        Map<Long, ReturnCombinationAiBusinessContext.MatchContext> contextByMatch = new HashMap<>();
        if (snapshot.businessContext != null && snapshot.businessContext.getMatches() != null) {
            for (ReturnCombinationAiBusinessContext.MatchContext context : snapshot.businessContext.getMatches()) {
                if (context != null && context.getMatchId() != null) {
                    contextByMatch.put(context.getMatchId(), context);
                }
            }
        }
        for (Fx67llDortmundOdds odds : snapshot.odds) {
            Long matchId = odds.getMatchId();
            ReturnCombinationAiBusinessContext.MatchContext matchContext = contextByMatch.get(matchId);
            String matchCode = matchContext == null ? odds.getMatchCode() : matchContext.getMatchCode();
            String matchContent;
            String matchTitle;
            if (matchContext == null) {
                matchTitle = "比赛上下文：" + (blank(matchCode) ? matchId : matchCode);
                matchContent = "matchId=" + matchId + "; matchCode=" + (matchCode == null ? "" : matchCode)
                        + "; matchSummary=" + (odds.getMatchSummary() == null ? "" : odds.getMatchSummary());
            } else {
                matchTitle = "比赛上下文：" + (blank(matchCode) ? matchId : matchCode);
                matchContent = "matchId=" + matchId + "; matchCode=" + (matchCode == null ? "" : matchCode)
                        + "; matchTime=" + (matchContext.getMatchTime() == null ? "" : matchContext.getMatchTime())
                        + "; seasonId=" + (matchContext.getSeasonId() == null ? "" : matchContext.getSeasonId())
                        + "; seasonName=" + (matchContext.getSeasonName() == null ? "" : matchContext.getSeasonName());
            }
            bundle.add(createSuppliedEvidence("S-M" + matchId, matchId, "MATCH_CONTEXT",
                    String.valueOf(matchId), matchTitle, matchContent, "系统提交快照", null, null));
            if (matchContext != null) {
                addTeamEvidence(bundle, matchId, matchContext.getHomeTeam(), "HOME");
                addTeamEvidence(bundle, matchId, matchContext.getAwayTeam(), "AWAY");
                if (matchContext.getMatchInfoPoints() != null) {
                    for (ReturnCombinationAiBusinessContext.InfoPointContext infoPoint
                            : matchContext.getMatchInfoPoints()) {
                        addInfoPointEvidence(bundle, matchId, infoPoint, "MATCH_INFO");
                    }
                }
            }
            String oddsContent = buildOddsEvidenceContent(odds);
            bundle.add(createSuppliedEvidence("S-O" + odds.getOddsId(), matchId, "ODDS",
                    String.valueOf(odds.getOddsId()), "odds快照 " + (blank(odds.getOddsCode()) ? "" : odds.getOddsCode()),
                    oddsContent, "系统提交快照", null, null));
        }
        return bundle;
    }

    private void addTeamEvidence(EvidenceBundle bundle, Long matchId,
            ReturnCombinationAiBusinessContext.TeamContext team, String side) {
        if (team == null || team.getTeamId() == null) {
            return;
        }
        String tags = team.getTeamTags() == null || team.getTeamTags().isEmpty()
                ? "[]" : team.getTeamTags().toString();
        String content = "teamId=" + team.getTeamId() + "; side=" + side + "; teamName="
                + (team.getTeamName() == null ? "" : team.getTeamName()) + "; rawTeamTags=" + tags;
        bundle.add(createSuppliedEvidence("S-T" + matchId + "-" + team.getTeamId(), matchId,
                "TEAM_CONTEXT", String.valueOf(team.getTeamId()),
                "球队上下文：" + (team.getTeamName() == null ? team.getTeamId() : team.getTeamName()),
                content, "系统提交快照", null, null));
        if (team.getSeasonInfoPoints() != null) {
            for (ReturnCombinationAiBusinessContext.InfoPointContext infoPoint : team.getSeasonInfoPoints()) {
                addInfoPointEvidence(bundle, matchId, infoPoint, "SEASON_INFO");
            }
        }
    }

    private void addInfoPointEvidence(EvidenceBundle bundle, Long matchId,
            ReturnCombinationAiBusinessContext.InfoPointContext infoPoint, String evidenceType) {
        if (infoPoint == null || infoPoint.getInfoPointId() == null) {
            return;
        }
        String content = "infoPointType=" + (infoPoint.getInfoPointType() == null ? "" : infoPoint.getInfoPointType())
                + "; title=" + (infoPoint.getPointTitle() == null ? "" : infoPoint.getPointTitle())
                + "; content=" + (infoPoint.getPointContent() == null ? "" : infoPoint.getPointContent())
                + "; sourceName=" + (infoPoint.getSourceName() == null ? "" : infoPoint.getSourceName())
                + "; sourcePublishTime="
                + (infoPoint.getSourcePublishTime() == null ? "" : infoPoint.getSourcePublishTime());
        bundle.add(createSuppliedEvidence("S-I" + matchId + "-" + infoPoint.getInfoPointId(), matchId,
                evidenceType, String.valueOf(infoPoint.getInfoPointId()),
                "信息点：" + (infoPoint.getPointTitle() == null ? infoPoint.getInfoPointId()
                        : infoPoint.getPointTitle()), content, infoPoint.getSourceName(),
                normalizeEvidenceUrl(infoPoint.getSourceUrl()),
                parseContextDate(infoPoint.getSourcePublishTime())));
    }

    private String buildOddsEvidenceContent(Fx67llDortmundOdds odds) {
        JSONObject value = new JSONObject();
        value.put("oddsId", odds.getOddsId());
        value.put("matchId", odds.getMatchId());
        value.put("matchCode", odds.getMatchCode());
        value.put("oddsCode", odds.getOddsCode());
        value.put("handicapValue", odds.getHandicapValue());
        value.put("homeWinOdds", odds.getHomeWinOdds());
        value.put("homeWinWeight", odds.getHomeWinWeight());
        value.put("drawOdds", odds.getDrawOdds());
        value.put("drawWeight", odds.getDrawWeight());
        value.put("awayWinOdds", odds.getAwayWinOdds());
        value.put("awayWinWeight", odds.getAwayWinWeight());
        value.put("handicapHomeWinOdds", odds.getHandicapHomeWinOdds());
        value.put("handicapHomeWinWeight", odds.getHandicapHomeWinWeight());
        value.put("handicapDrawOdds", odds.getHandicapDrawOdds());
        value.put("handicapDrawWeight", odds.getHandicapDrawWeight());
        value.put("handicapAwayWinOdds", odds.getHandicapAwayWinOdds());
        value.put("handicapAwayWinWeight", odds.getHandicapAwayWinWeight());
        return value.toJSONString();
    }

    private Fx67llDortmundReturnEvidence createSuppliedEvidence(String evidenceCode, Long matchId,
            String evidenceType, String sourceRefId, String title, String content, String sourceName,
            String sourceUrl, Date publishedTime) {
        Fx67llDortmundReturnEvidence evidence = new Fx67llDortmundReturnEvidence();
        evidence.setEvidenceCode(evidenceCode);
        evidence.setMatchId(matchId);
        evidence.setEvidenceOrigin(EVIDENCE_ORIGIN_SUPPLIED);
        evidence.setEvidenceType(evidenceType);
        evidence.setSourceRefId(sourceRefId);
        evidence.setTitle(limit(sanitizeEvidenceText(title, 512), 512));
        evidence.setContent(limit(sanitizeEvidenceText(content, 4000), 4000));
        evidence.setSourceName(limit(sourceName, 233));
        evidence.setSourceUrl(limit(sourceUrl, 1023));
        evidence.setPublishedTime(publishedTime);
        evidence.setSourceTier(EvidenceSourceTaxonomy.classifySourceTier(sourceUrl, sourceName));
        evidence.setContentHash(sha256(evidence.getTitle() + "\n" + evidence.getContent()
                + "\n" + (sourceUrl == null ? "" : sourceUrl)));
        return evidence;
    }

    private List<ReturnCombinationAiAnalysisPort.EvidenceContext> toEvidenceContexts(
            List<Fx67llDortmundReturnEvidence> evidenceList, Date dataAsOf) {
        if (evidenceList == null || evidenceList.isEmpty()) {
            return null;
        }
        List<ReturnCombinationAiAnalysisPort.EvidenceContext> result = new ArrayList<>();
        for (Fx67llDortmundReturnEvidence evidence : evidenceList) {
            if (evidence == null) {
                continue;
            }
            ReturnCombinationAiAnalysisPort.EvidenceContext context =
                    new ReturnCombinationAiAnalysisPort.EvidenceContext();
            context.setEvidenceCode(evidence.getEvidenceCode());
            context.setMatchId(evidence.getMatchId());
            context.setEvidenceType(evidence.getEvidenceType());
            context.setTitle(evidence.getTitle());
            context.setContent(evidence.getContent());
            context.setSourceName(evidence.getSourceName());
            context.setSourceUrl(evidence.getSourceUrl());
            context.setPublishedTime(formatContextTime(evidence.getPublishedTime()));
            context.setQuery(evidence.getPlanQuery());
            String sourceTier = EvidenceSourceTaxonomy.isValidTier(evidence.getSourceTier())
                    ? EvidenceSourceTaxonomy.normalizeTier(evidence.getSourceTier())
                    : EvidenceSourceTaxonomy.classifySourceTier(evidence.getSourceUrl(), evidence.getSourceName());
            context.setSourceTier(sourceTier);
            context.setFreshnessClass(EvidenceSourceTaxonomy.classifyFreshness(
                    evidence.getPublishedTime(), evidence.getRetrievedTime(), dataAsOf));
            result.add(context);
        }
        return result;
    }

    private List<ReturnCombinationAiAnalysisPort.EvidenceQuery> validateEvidencePlan(
            ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan plan,
            List<Fx67llDortmundOdds> oddsList) {
        if (plan == null || plan.getQueries() == null || plan.getQueries().isEmpty()) {
            throw new ServiceException("自动AI未规划出可执行的证据检索查询！");
        }
        if (plan.getQueries().size() > MAX_RETRIEVAL_QUERIES) {
            throw new ServiceException("自动AI证据检索查询超过" + MAX_RETRIEVAL_QUERIES + "条限制！");
        }
        Set<Long> allowedMatchIds = new HashSet<>();
        for (Fx67llDortmundOdds odds : oddsList) {
            allowedMatchIds.add(odds.getMatchId());
        }
        Set<Long> plannedMatchIds = new HashSet<>();
        List<ReturnCombinationAiAnalysisPort.EvidenceQuery> result = new ArrayList<>();
        for (ReturnCombinationAiAnalysisPort.EvidenceQuery query : plan.getQueries()) {
            if (query == null || query.getMatchId() == null
                    || !allowedMatchIds.contains(query.getMatchId())
                    || !plannedMatchIds.add(query.getMatchId())) {
                throw new ServiceException("自动AI证据检索计划包含无效或重复的matchId！");
            }
            String queryText = query.getQuery() == null ? null : query.getQuery().trim();
            String purpose = query.getPurpose() == null ? null : query.getPurpose().trim();
            String reason = query.getReason() == null ? null : query.getReason().trim();
            if (blank(queryText) || queryText.length() > 260
                    || blank(purpose) || purpose.length() > 64
                    || reason != null && reason.length() > 500) {
                throw new ServiceException("自动AI证据检索计划字段为空或超过长度限制！");
            }
            Integer recencyDays = query.getRecencyDays();
            if (recencyDays != null && (recencyDays < 1 || recencyDays > 365)) {
                throw new ServiceException("自动AI证据检索recencyDays必须为1至365！");
            }
            String locale = trimToNull(query.getLocale());
            if (locale != null && (locale.length() > 16
                    || !locale.matches("[A-Za-z]{2,3}(?:-[A-Za-z0-9]{2,8}){0,2}"))) {
                throw new ServiceException("自动AI证据检索locale格式不正确！");
            }
            String region = trimToNull(query.getRegion());
            if (region != null && region.length() > 32) {
                throw new ServiceException("自动AI证据检索region超过32个字符！");
            }
            List<String> preferredSourceTiers = new ArrayList<>();
            if (query.getPreferredSourceTiers() != null) {
                Set<String> seenTiers = new LinkedHashSet<>();
                for (String sourceTier : query.getPreferredSourceTiers()) {
                    String normalizedTier = EvidenceSourceTaxonomy.normalizeTier(sourceTier);
                    if (blank(sourceTier) || !EvidenceSourceTaxonomy.isValidTier(sourceTier)
                            || !seenTiers.add(normalizedTier)) {
                        continue;
                    }
                    preferredSourceTiers.add(normalizedTier);
                }
            }
            ReturnCombinationAiAnalysisPort.EvidenceQuery normalized =
                    new ReturnCombinationAiAnalysisPort.EvidenceQuery();
            normalized.setMatchId(query.getMatchId());
            normalized.setQuery(queryText);
            normalized.setPurpose(purpose);
            normalized.setReason(reason);
            normalized.setRecencyDays(recencyDays);
            normalized.setLocale(locale);
            normalized.setRegion(region);
            normalized.setPreferredSourceTiers(preferredSourceTiers);
            result.add(normalized);
        }
        if (!plannedMatchIds.equals(allowedMatchIds)) {
            throw new ServiceException("自动AI证据检索计划必须为每场参与比赛各规划一条查询！");
        }
        return result;
    }

    private String buildEvidencePlanSnapshot(ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan plan,
            List<ReturnCombinationAiAnalysisPort.EvidenceQuery> queries) {
        JSONObject snapshot = new JSONObject();
        snapshot.put("schemaVersion", AUTO_AI_CONTRACT_VERSION);
        snapshot.put("taskType", AI_SCENE_CODE);
        snapshot.put("planningSummary", limit(plan.getPlanningSummary(), 2000));
        snapshot.put("plannerRunRef", limitRunRef(plan.getAiRunRef()));
        snapshot.put("queries", queries);
        return snapshot.toJSONString();
    }

    /**
     * 执行AI规划的受控证据检索，并将外部结果清洗、去重、编码为可冻结证据。
     * <p>
     * 单场查询必须至少产出一条有效证据，否则自动AI无法完成“检索事实支持权重”的完整证据链。
     */
    private List<Fx67llDortmundReturnEvidence> retrieveEvidence(
            List<ReturnCombinationAiAnalysisPort.EvidenceQuery> queries) {
        List<Fx67llDortmundReturnEvidence> result = new ArrayList<>();
        Date retrievedAt = DateUtils.getNowDate();
        for (ReturnCombinationAiAnalysisPort.EvidenceQuery query : queries) {
            List<ReturnEvidenceRetrievalPort.RetrievalResult> retrievalResults =
                    returnEvidenceRetrievalPort.retrieve(query, MAX_RETRIEVAL_RESULTS_PER_QUERY);
            Set<String> contentHashes = new HashSet<>();
            int accepted = 0;
            if (retrievalResults != null) {
                for (ReturnEvidenceRetrievalPort.RetrievalResult raw : retrievalResults) {
                    if (raw == null || accepted >= MAX_RETRIEVAL_RESULTS_PER_QUERY) {
                        continue;
                    }
                    String title = sanitizeEvidenceText(raw.getTitle(), 512);
                    String snippet = sanitizeEvidenceText(raw.getSnippet(), 4000);
                    if (blank(title) && blank(snippet)) {
                        continue;
                    }
                    String purpose = sanitizeEvidenceText(query.getPurpose(), 64);
                    String content = "purpose=" + (purpose == null ? "" : purpose)
                            + "; snippet=" + (snippet == null ? "" : snippet);
                    if (blank(title)) {
                        title = limit(content, 512);
                    }
                    String sourceUrl = normalizeEvidenceUrl(raw.getUrl());
                    String rawSourceName = limit(sanitizeEvidenceText(raw.getSourceName(), 233), 233);
                    String sourceTier = EvidenceSourceTaxonomy.isValidTier(raw.getSourceTier())
                            ? EvidenceSourceTaxonomy.normalizeTier(raw.getSourceTier())
                            : EvidenceSourceTaxonomy.classifySourceTier(sourceUrl, rawSourceName);
                    String contentHash = sha256(title + "\n" + content + "\n"
                            + (sourceUrl == null ? "" : sourceUrl));
                    if (!contentHashes.add(contentHash)) {
                        continue;
                    }
                    accepted++;
                    Fx67llDortmundReturnEvidence evidence = new Fx67llDortmundReturnEvidence();
                    evidence.setEvidenceCode("R-M" + query.getMatchId() + "-" + accepted);
                    evidence.setMatchId(query.getMatchId());
                    evidence.setEvidenceOrigin(EVIDENCE_ORIGIN_RETRIEVED);
                    evidence.setEvidenceType("RETRIEVAL");
                    evidence.setSourceRefId(limit(query.getPurpose() + "-" + accepted, 64));
                    evidence.setPlanQuery(limit(query.getQuery(), 512));
                    evidence.setTitle(title);
                    evidence.setContent(content);
                    evidence.setSourceName(rawSourceName);
                    evidence.setSourceUrl(sourceUrl);
                    evidence.setSourceTier(sourceTier);
                    evidence.setPublishedTime(parseContextDate(raw.getPublishedAt()));
                    evidence.setRetrievedTime(retrievedAt);
                    evidence.setContentHash(contentHash);
                    result.add(evidence);
                }
            }
            if (accepted == 0) {
                throw new ServiceException("自动AI证据检索未返回比赛" + query.getMatchId()
                        + "的可用证据，请稍后重试或改用人工AI任务！");
            }
        }
        return result;
    }

    private String buildAiResultSnapshot(ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output,
            String contractVersion) {
        JSONObject result = new JSONObject();
        result.put("schemaVersion", contractVersion);
        result.put("taskType", AI_SCENE_CODE);
        if (output == null) {
            result.put("analysisContent", null);
            result.put("candidates", new JSONArray());
            return result.toJSONString();
        }
        result.put("analysisContent", output.getAnalysisContent());
        JSONArray candidates = new JSONArray();
        if (output.getCandidates() != null) {
            for (ReturnCombinationAiAnalysisPort.Candidate sourceCandidate : output.getCandidates()) {
                if (sourceCandidate == null) {
                    continue;
                }
                JSONObject candidate = new JSONObject();
                candidate.put("title", sourceCandidate.getTitle());
                candidate.put("reason", sourceCandidate.getReason());
                if (sourceCandidate.getConfidence() != null) {
                    candidate.put("confidence", sourceCandidate.getConfidence());
                }
                if (!blank(sourceCandidate.getRiskNotes())) {
                    candidate.put("riskNotes", sourceCandidate.getRiskNotes());
                }
                if (sourceCandidate.getEvidenceChain() != null
                        && !sourceCandidate.getEvidenceChain().isEmpty()) {
                    candidate.put("evidenceChain", sourceCandidate.getEvidenceChain());
                }
                JSONArray selections = new JSONArray();
                if (sourceCandidate.getSelections() != null) {
                    for (ReturnCombinationAiAnalysisPort.Selection sourceSelection : sourceCandidate.getSelections()) {
                        if (sourceSelection == null) {
                            continue;
                        }
                        JSONObject selection = new JSONObject();
                        selection.put("matchId", sourceSelection.getMatchId());
                        selection.put("selectionType", sourceSelection.getSelectionType());
                        selection.put("appliedWeight", sourceSelection.getAppliedWeight());
                        selection.put("reason", sourceSelection.getReason());
                        if (!blank(sourceSelection.getWeightRationale())) {
                            selection.put("weightRationale", sourceSelection.getWeightRationale());
                        }
                        if (sourceSelection.getEvidenceRefs() != null
                                && !sourceSelection.getEvidenceRefs().isEmpty()) {
                            selection.put("evidenceRefs", sourceSelection.getEvidenceRefs());
                        }
                        if (sourceSelection.getCounterEvidenceRefs() != null
                                && !sourceSelection.getCounterEvidenceRefs().isEmpty()) {
                            selection.put("counterEvidenceRefs", sourceSelection.getCounterEvidenceRefs());
                        }
                        selections.add(selection);
                    }
                }
                candidate.put("selections", selections);
                candidates.add(candidate);
            }
        }
        result.put("candidates", candidates);
        return result.toJSONString();
    }

    private String limitRunRef(String aiRunRef) {
        return blank(aiRunRef) ? null : limit(aiRunRef.trim(), 128);
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
        if (weight == null) {
            return "S".equals(generationMode) ? DortmundWeightCodec.DEFAULT_WEIGHT : null;
        }
        return DortmundWeightCodec.normalize(weight, "手工权重");
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

    /**
     * 清洗外部证据文本：移除HTML/脚本片段和控制字符，保留业务语义并限制长度。
     */
    private String sanitizeEvidenceText(String value, int length) {
        if (blank(value)) {
            return null;
        }
        StringBuilder cleaned = new StringBuilder(value.length());
        boolean inTag = false;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '<') {
                inTag = true;
                continue;
            }
            if (current == '>' && inTag) {
                inTag = false;
                cleaned.append(' ');
                continue;
            }
            if (inTag || current == '\r') {
                continue;
            }
            if (Character.isISOControl(current) && current != '\n' && current != '\t') {
                cleaned.append(' ');
            } else {
                cleaned.append(current);
            }
        }
        String normalized = cleaned.toString().replaceAll("\\s+", " ").trim();
        return normalized.length() > length ? normalized.substring(0, length) : normalized;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            char[] hex = "0123456789abcdef".toCharArray();
            StringBuilder result = new StringBuilder(hash.length * 2);
            for (byte item : hash) {
                result.append(hex[(item >> 4) & 0x0f]);
                result.append(hex[item & 0x0f]);
            }
            return result.toString();
        } catch (Exception e) {
            throw new ServiceException("证据内容哈希计算失败！");
        }
    }

    /**
     * 仅接受后端检索返回的HTTP(S)来源地址，拒绝凭据、非网络协议和超长地址。
     */
    private String normalizeEvidenceUrl(String value) {
        if (blank(value)) {
            return null;
        }
        try {
            URI uri = new URI(value.trim());
            String scheme = uri.getScheme();
            if (scheme == null || uri.getHost() == null || uri.getUserInfo() != null
                    || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
                return null;
            }
            String normalized = uri.toString();
            return normalized.length() > 1023 ? null : normalized;
        } catch (Exception e) {
            return null;
        }
    }

    private Date parseContextDate(String value) {
        return blank(value) ? null : DateUtils.parseDate(value.trim());
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        return blank(value) ? null : value.trim();
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
        private final List<String> businessObjectives;
        private final List<String> supplementaryObjectives;
        private final ReturnCombinationAiBusinessContext businessContext;
        private final Date dataAsOf;

        private GenerationSnapshot(Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> odds,
                List<String> businessObjectives, List<String> supplementaryObjectives,
                ReturnCombinationAiBusinessContext businessContext, Date dataAsOf) {
            this.request = request;
            this.odds = odds;
            this.businessObjectives = businessObjectives;
            this.supplementaryObjectives = supplementaryObjectives;
            this.businessContext = businessContext;
            this.dataAsOf = dataAsOf;
        }
    }

    private static class PreparedCombination {
        private String signature;
        private Fx67llDortmundReturnCombination combination;
        private List<Fx67llDortmundReturnCombinationItem> items;
    }

    /**
     * 当前批次的冻结证据集合，按证据编码去重并区分提交证据与检索证据。
     */
    private static class EvidenceBundle {
        private final Map<String, Fx67llDortmundReturnEvidence> byCode = new LinkedHashMap<>();

        private final List<Fx67llDortmundReturnEvidence> retrieved = new ArrayList<>();

        private void add(Fx67llDortmundReturnEvidence evidence) {
            if (evidence == null || evidence.getEvidenceCode() == null
                    || evidence.getEvidenceCode().trim().isEmpty()
                    || byCode.containsKey(evidence.getEvidenceCode())) {
                return;
            }
            byCode.put(evidence.getEvidenceCode(), evidence);
            if (EVIDENCE_ORIGIN_RETRIEVED.equals(evidence.getEvidenceOrigin())) {
                retrieved.add(evidence);
            }
        }

        private void addAll(List<Fx67llDortmundReturnEvidence> evidenceList) {
            if (evidenceList == null) {
                return;
            }
            for (Fx67llDortmundReturnEvidence evidence : evidenceList) {
                add(evidence);
            }
        }

        private List<Fx67llDortmundReturnEvidence> getAll() {
            return new ArrayList<>(byCode.values());
        }

        private List<Fx67llDortmundReturnEvidence> getRetrieved() {
            return new ArrayList<>(retrieved);
        }

        private Fx67llDortmundReturnEvidence findByCode(String evidenceCode) {
            return evidenceCode == null || evidenceCode.trim().isEmpty() ? null : byCode.get(evidenceCode);
        }
    }
}
