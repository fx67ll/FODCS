package com.ruoyi.fx67ll.dortmund.ai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * DeepSeek回报组合AI分析实现
 * <p>
 * 本实现只负责第1.5阶段固定业务调用和结构化输出转换，后续可由通用AI架构替换
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@Service
public class DeepSeekReturnCombinationAnalyzer implements ReturnCombinationAiAnalysisPort {
    private static final int CONNECT_TIMEOUT_SECONDS = 10;

    private static final int READ_TIMEOUT_SECONDS = 45;

    @Value("${dortmund.ai.deepseek.url:}")
    private String deepSeekEndpoint;

    @Value("${dortmund.ai.deepseek.api-key:}")
    private String deepSeekApiKey;

    @Value("${dortmund.ai.deepseek.model:}")
    private String deepSeekModel;

    private final RestTemplate restTemplate = createRestTemplate();

    /**
     * 调用DeepSeek并转换为业务输出
     *
     * @param input 已校验的业务输入
     * @return 业务可消费的AI分析输出
     */
    @Override
    public ReturnCombinationAiOutput analyze(ReturnCombinationAiInput input) {
        if (isBlank(deepSeekEndpoint) || isBlank(deepSeekApiKey) || isBlank(deepSeekModel)) {
            throw new ServiceException("DeepSeek分析配置不完整！");
        }
        JSONObject request = new JSONObject();
        request.put("model", deepSeekModel);
        request.put("temperature", 0.2);
        request.put("messages", buildMessages(input));
        request.put("response_format", JSONObject.of("type", "json_object"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepSeekApiKey);
        try {
            String rawResponse = restTemplate.postForObject(deepSeekEndpoint,
                    new HttpEntity<>(request.toJSONString(), headers), String.class);
            return parseResponse(rawResponse);
        } catch (RestClientException e) {
            throw new ServiceException("DeepSeek分析请求失败，请稍后重试！");
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("DeepSeek分析响应格式不正确！");
        }
    }

    private List<JSONObject> buildMessages(ReturnCombinationAiInput input) {
        List<JSONObject> messages = new ArrayList<>();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Return JSON only: {candidates:[{title,reason,selections:[{matchId,selectionType,appliedWeight,reason}]}],analysisContent}. selectionType must be HW, DR, AW, HH, HD, or HA. Each candidate contains 1 to 8 unique matches and no more than 10 candidates. Do not return database odds values or amounts. S uses input weights; A returns a weight from 0 to 100.");
        messages.add(systemMessage);
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", JSON.toJSONString(input));
        messages.add(userMessage);
        return messages;
    }

    private ReturnCombinationAiOutput parseResponse(String rawResponse) {
        if (isBlank(rawResponse)) {
            throw new ServiceException("DeepSeek未返回分析响应！");
        }
        JSONObject responseEnvelope = JSON.parseObject(rawResponse);
        JSONArray choices = responseEnvelope.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new ServiceException("DeepSeek未返回候选组合内容！");
        }
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String content = message == null ? null : message.getString("content");
        if (isBlank(content)) {
            throw new ServiceException("DeepSeek未返回分析正文！");
        }
        content = stripCodeFence(content);
        JSONObject responseBody = JSON.parseObject(content);
        ReturnCombinationAiOutput output = new ReturnCombinationAiOutput();
        output.setAnalysisContent(content);
        List<ReturnCombinationAiAnalysisPort.Candidate> candidates = new ArrayList<>();
        JSONArray candidateArray = responseBody.getJSONArray("candidates");
        if (candidateArray != null) {
            for (int i = 0; i < candidateArray.size(); i++) {
                JSONObject item = candidateArray.getJSONObject(i);
                if (item == null) {
                    continue;
                }
                ReturnCombinationAiAnalysisPort.Candidate candidate = new ReturnCombinationAiAnalysisPort.Candidate();
                candidate.setTitle(item.getString("title"));
                candidate.setReason(item.getString("reason"));
                candidate.setAnalysisContent(item.toJSONString());
                candidate.setSelections(parseSelections(item.getJSONArray("selections")));
                candidates.add(candidate);
            }
        }
        output.setCandidates(candidates);
        return output;
    }

    private List<ReturnCombinationAiAnalysisPort.Selection> parseSelections(JSONArray selectionArray) {
        List<ReturnCombinationAiAnalysisPort.Selection> selections = new ArrayList<>();
        if (selectionArray == null) {
            return selections;
        }
        for (int i = 0; i < selectionArray.size(); i++) {
            JSONObject selectionValue = selectionArray.getJSONObject(i);
            if (selectionValue == null) {
                continue;
            }
            ReturnCombinationAiAnalysisPort.Selection selection = new ReturnCombinationAiAnalysisPort.Selection();
            selection.setMatchId(selectionValue.getLong("matchId"));
            selection.setSelectionType(selectionValue.getString("selectionType"));
            selection.setAppliedWeight(selectionValue.getBigDecimal("appliedWeight"));
            selection.setReason(selectionValue.getString("reason"));
            selections.add(selection);
        }
        return selections;
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(READ_TIMEOUT_SECONDS).toMillis());
        return new RestTemplate(factory);
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
