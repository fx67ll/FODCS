package com.ruoyi.fx67ll.dortmund.ai;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import org.springframework.stereotype.Component;

/**
 * 回报组合AI标准化响应解析器。
 * <p>
 * 自动AI使用带完整证据链的2.1契约，并兼容读取2.0；人工第三方AI保持1.0结构不变。后续通用AI模块只需把
 * 编排结果交给本解析器，不应在业务Service中重复解析JSON。
 * <p>
 * 【通用AI接入点】通用AI模块返回的标准化正文仍统一经过本解析器；供应商响应envelope、
 * 模型、Prompt、Token和费用字段不得进入业务结果。
 *
 * @author fx67ll
 * @date 2026-11-16
 */
@Component
public class ReturnCombinationAiResponseParser {
    /** 自动AI完整证据链当前契约版本 */
    public static final String AUTO_SCHEMA_VERSION = "2.1";

    /** 自动AI上一版契约，仅保留读取兼容 */
    public static final String AUTO_SCHEMA_VERSION_LEGACY = "2.0";

    /** 人工AI导入导出契约版本，保持既有业务流不变 */
    public static final String MANUAL_SCHEMA_VERSION = "1.0";

    /** 兼容既有调用方的默认版本别名 */
    public static final String SCHEMA_VERSION = MANUAL_SCHEMA_VERSION;

    /** 自动AI、人工交换任务共用的任务类型 */
    public static final String TASK_TYPE = "DORTMUND_RETURN_COMBINATION";

    /** 人工交换任务限定的候选上限，与导出契约和模块配置保持一致。 */
    public static final int MAX_MANUAL_CANDIDATES = DortmundModuleConfig.Return.MAX_MANUAL_CANDIDATES;

    /**
     * 解析自动AI正文。
     *
     * @param content 模型返回的JSON正文，可带Markdown代码围栏
     * @return 业务可消费的AI分析输出
     */
    public ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput parseAiContent(String content) {
        JSONObject body = parseJson(content);
        requireAutomaticContract(body);
        if (isBlank(body.getString("analysisContent"))) {
            throw new ServiceException("自动AI结果缺少analysisContent！");
        }
        return parseBody(body, true);
    }

    /**
     * 解析自动AI证据检索计划。
     *
     * @param content 模型返回的JSON正文，可带Markdown代码围栏
     * @return 有界检索计划，具体上限由业务层校验
     */
    public ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan parseEvidencePlanContent(String content) {
        JSONObject body = parseJson(content);
        requireAutomaticContract(body);
        ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan plan =
                new ReturnCombinationAiAnalysisPort.ReturnCombinationEvidencePlan();
        plan.setPlanningSummary(body.getString("planningSummary"));
        List<ReturnCombinationAiAnalysisPort.EvidenceQuery> queries = new ArrayList<>();
        JSONArray queryArray = body.getJSONArray("queries");
        if (queryArray != null) {
            for (int i = 0; i < queryArray.size(); i++) {
                JSONObject item = queryArray.getJSONObject(i);
                if (item == null) {
                    continue;
                }
                ReturnCombinationAiAnalysisPort.EvidenceQuery query =
                        new ReturnCombinationAiAnalysisPort.EvidenceQuery();
                query.setMatchId(item.getLong("matchId"));
                query.setQuery(item.getString("query"));
                query.setPurpose(item.getString("purpose"));
                query.setReason(item.getString("reason"));
                query.setRecencyDays(item.getInteger("recencyDays"));
                query.setLocale(item.getString("locale"));
                query.setRegion(item.getString("region"));
                query.setPreferredSourceTiers(parseStringArray(item.getJSONArray("preferredSourceTiers")));
                queries.add(query);
            }
        }
        plan.setQueries(queries);
        return plan;
    }

    /**
     * 解析人工第三方AI标准化响应。
     *
     * @param responseContent 标准化JSON正文，可带Markdown代码围栏
     * @param expectedTaskId  当前生成批次编码
     * @return 与既有自动AI业务输出兼容的结果
     */
    public ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput parseManualResponse(String responseContent,
            String expectedTaskId) {
        JSONObject body = parseJson(responseContent);
        requireContract(body, MANUAL_SCHEMA_VERSION);
        if (isBlank(expectedTaskId) || !expectedTaskId.equals(body.getString("taskId"))) {
            throw new ServiceException("人工AI结果taskId与当前批次不一致！");
        }
        if (isBlank(body.getString("analysisContent"))) {
            throw new ServiceException("人工AI结果缺少analysisContent！");
        }
        JSONArray candidateArray = body.getJSONArray("candidates");
        if (candidateArray != null && candidateArray.size() > MAX_MANUAL_CANDIDATES) {
            throw new ServiceException("人工AI结果候选组合超过" + MAX_MANUAL_CANDIDATES + "条限制！");
        }
        return parseBody(body, false);
    }

    private void requireContract(JSONObject body, String schemaVersion) {
        if (!schemaVersion.equals(body.getString("schemaVersion"))) {
            throw new ServiceException("AI结果schemaVersion必须为" + schemaVersion + "！");
        }
        if (!TASK_TYPE.equals(body.getString("taskType"))) {
            throw new ServiceException("AI结果taskType不正确！");
        }
    }

    private void requireAutomaticContract(JSONObject body) {
        String schemaVersion = body.getString("schemaVersion");
        if (!AUTO_SCHEMA_VERSION.equals(schemaVersion)
                && !AUTO_SCHEMA_VERSION_LEGACY.equals(schemaVersion)) {
            throw new ServiceException("自动AI结果schemaVersion必须为" + AUTO_SCHEMA_VERSION
                    + "或" + AUTO_SCHEMA_VERSION_LEGACY + "！");
        }
        if (!TASK_TYPE.equals(body.getString("taskType"))) {
            throw new ServiceException("AI结果taskType不正确！");
        }
    }

    private JSONObject parseJson(String content) {
        if (isBlank(content)) {
            throw new ServiceException("AI分析响应不能为空！");
        }
        String value = stripCodeFence(content);
        try {
            JSONObject body = JSON.parseObject(value);
            if (body == null) {
                throw new ServiceException("AI分析响应不是有效JSON对象！");
            }
            return body;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("AI分析响应格式不正确！");
        }
    }

    private ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput parseBody(JSONObject responseBody,
            boolean automatic) {
        JSONArray candidateArray = responseBody.getJSONArray("candidates");
        if (candidateArray == null || candidateArray.isEmpty()) {
            throw new ServiceException("AI未返回候选组合内容！");
        }
        int maxCandidates = automatic
                ? DortmundModuleConfig.Return.MAX_AUTO_PARSED_CANDIDATES
                : DortmundModuleConfig.Return.MAX_MANUAL_CANDIDATES;
        if (candidateArray.size() > maxCandidates) {
            throw new ServiceException("AI返回候选组合超过" + maxCandidates + "条限制！");
        }
        ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput output =
                new ReturnCombinationAiAnalysisPort.ReturnCombinationAiOutput();
        output.setAnalysisContent(responseBody.getString("analysisContent"));
        List<ReturnCombinationAiAnalysisPort.Candidate> candidates = new ArrayList<>();
        for (int i = 0; i < candidateArray.size(); i++) {
            JSONObject item = candidateArray.getJSONObject(i);
            if (item == null) {
                continue;
            }
            ReturnCombinationAiAnalysisPort.Candidate candidate =
                    new ReturnCombinationAiAnalysisPort.Candidate();
            candidate.setTitle(item.getString("title"));
            candidate.setReason(item.getString("reason"));
            candidate.setConfidence(item.getInteger("confidence"));
            candidate.setRiskNotes(item.getString("riskNotes"));
            candidate.setAnalysisContent(item.toJSONString());
            candidate.setEvidenceChain(automatic ? parseEvidenceChain(item.getJSONArray("evidenceChain"))
                    : new ArrayList<>());
            candidate.setSelections(parseSelections(item.getJSONArray("selections"), automatic));
            candidates.add(candidate);
        }
        output.setCandidates(candidates);
        return output;
    }

    private List<ReturnCombinationAiAnalysisPort.EvidenceFactor> parseEvidenceChain(JSONArray factorArray) {
        List<ReturnCombinationAiAnalysisPort.EvidenceFactor> factors = new ArrayList<>();
        if (factorArray == null) {
            return factors;
        }
        for (int i = 0; i < factorArray.size(); i++) {
            JSONObject value = factorArray.getJSONObject(i);
            if (value == null) {
                continue;
            }
            ReturnCombinationAiAnalysisPort.EvidenceFactor factor =
                    new ReturnCombinationAiAnalysisPort.EvidenceFactor();
            factor.setFactorCode(value.getString("factorCode"));
            factor.setMatchId(value.getLong("matchId"));
            factor.setFactorType(value.getString("factorType"));
            factor.setSourceType(value.getString("sourceType"));
            factor.setSourceRef(value.getString("sourceRef"));
            factor.setObservation(value.getString("observation"));
            factor.setImpact(value.getString("impact"));
            factor.setConflictGroup(value.getString("conflictGroup"));
            factor.setWeightImpact(value.getBigDecimal("weightImpact"));
            factor.setConfidence(value.getInteger("confidence"));
            factors.add(factor);
        }
        return factors;
    }

    private List<ReturnCombinationAiAnalysisPort.Selection> parseSelections(JSONArray selectionArray,
            boolean automatic) {
        List<ReturnCombinationAiAnalysisPort.Selection> selections = new ArrayList<>();
        if (selectionArray == null) {
            return selections;
        }
        for (int i = 0; i < selectionArray.size(); i++) {
            JSONObject selectionValue = selectionArray.getJSONObject(i);
            if (selectionValue == null) {
                continue;
            }
            ReturnCombinationAiAnalysisPort.Selection selection =
                    new ReturnCombinationAiAnalysisPort.Selection();
            selection.setMatchId(selectionValue.getLong("matchId"));
            selection.setSelectionType(selectionValue.getString("selectionType"));
            // appliedWeight仅用于解释与复核，不参与任何金额计算。
            selection.setAppliedWeight(selectionValue.getBigDecimal("appliedWeight"));
            selection.setReason(selectionValue.getString("reason"));
            if (automatic) {
                selection.setWeightRationale(selectionValue.getString("weightRationale"));
                selection.setEvidenceRefs(parseStringArray(selectionValue.getJSONArray("evidenceRefs")));
                selection.setCounterEvidenceRefs(
                        parseStringArray(selectionValue.getJSONArray("counterEvidenceRefs")));
            } else {
                selection.setEvidenceRefs(new ArrayList<>());
                selection.setCounterEvidenceRefs(new ArrayList<>());
            }
            selections.add(selection);
        }
        return selections;
    }

    private List<String> parseStringArray(JSONArray values) {
        List<String> result = new ArrayList<>();
        if (values == null) {
            return result;
        }
        for (int i = 0; i < values.size(); i++) {
            String value = values.getString(i);
            if (!isBlank(value)) {
                result.add(value.trim());
            }
        }
        return result;
    }

    private String stripCodeFence(String content) {
        String value = content.trim();
        if (value.startsWith("```")) {
            int firstBreak = value.indexOf('\n');
            int lastFence = value.lastIndexOf("```");
            if (firstBreak > 0 && lastFence > firstBreak) {
                value = value.substring(firstBreak + 1, lastFence).trim();
            }
        }
        return value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
