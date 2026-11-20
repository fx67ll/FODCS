package com.ruoyi.fx67ll.dortmund.ai;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.config.DortmundRuntimeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 供应商无关的HTTP证据检索适配器。
 * <p>
 * 部署方只需提供返回标准results数组的检索网关；更换搜索供应商时不修改回报组合业务服务。
 * <p>
 * 【通用AI接入点】接入通用AI搜索工具时，应由通用适配器实现ReturnEvidenceRetrievalPort；
 * 本类可保留为兼容实现，但不得把检索供应商字段写入回报组合业务表。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
@Service
public class HttpReturnEvidenceRetrievalAdapter implements ReturnEvidenceRetrievalPort {
    private static final Logger log = LoggerFactory.getLogger(HttpReturnEvidenceRetrievalAdapter.class);

    @Value("${dortmund.ai.retrieval.url:}")
    private String retrievalUrl;

    @Value("${dortmund.ai.retrieval.api-key:}")
    private String retrievalApiKey;

    /** 检索数值边界统一从模块类型化配置读取。 */
    @Autowired
    private DortmundRuntimeProperties dortmundRuntimeProperties;

    @Override
    public boolean isConfigured() {
        return !isBlank(retrievalUrl);
    }

    /** 执行单条有界检索；查询由业务层预先校验，本方法只负责传输和受控解析。 */
    @Override
    public List<RetrievalResult> retrieve(ReturnCombinationAiAnalysisPort.EvidenceQuery query, int maxResults) {
        if (!isConfigured()) {
            throw new ServiceException("自动AI证据检索服务未配置，请设置DORTMUND_AI_RETRIEVAL_URL！");
        }
        if (query == null || isBlank(query.getQuery())) {
            throw new ServiceException("证据检索查询不能为空！");
        }
        int boundedResults = Math.max(1, Math.min(maxResults,
                dortmundRuntimeProperties.getAi().getRetrieval().getMaxResults()));
        JSONObject request = new JSONObject();
        request.put("query", query.getQuery());
        request.put("maxResults", boundedResults);
        request.put("language", isBlank(query.getLocale()) ? "zh-CN" : query.getLocale());
        if (query.getRecencyDays() != null) {
            request.put("recencyDays", query.getRecencyDays());
        }
        if (!isBlank(query.getRegion())) {
            request.put("region", query.getRegion());
        }
        if (query.getPreferredSourceTiers() != null && !query.getPreferredSourceTiers().isEmpty()) {
            request.put("preferredSourceTiers", query.getPreferredSourceTiers());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        if (!isBlank(retrievalApiKey)) {
            headers.setBearerAuth(retrievalApiKey.trim());
        }
        try {
            RestTemplate restTemplate = createRestTemplate();
            String rawResponse = restTemplate.postForObject(normalizeEndpoint(retrievalUrl),
                    new HttpEntity<>(request.toJSONString(), headers), String.class);
            return parseResponse(rawResponse, boundedResults);
        } catch (HttpStatusCodeException e) {
            int statusCode = e.getRawStatusCode();
            log.error("证据检索请求失败，HTTP状态码：{}，响应：{}", statusCode, limitLog(e.getResponseBodyAsString()));
            if (statusCode == 401 || statusCode == 403) {
                throw new ServiceException("证据检索鉴权失败，请检查检索服务密钥配置！");
            }
            if (statusCode == 429) {
                throw new ServiceException("证据检索请求过于频繁，请稍后重试！");
            }
            throw new ServiceException("证据检索请求失败，HTTP状态码：" + statusCode + "！");
        } catch (RestClientException e) {
            log.error("证据检索网络请求失败", e);
            throw new ServiceException("证据检索请求失败，请稍后重试！");
        }
    }

    /** 解析供应商无关结果并截断到上限；来源层级始终由平台重新分类。 */
    private List<RetrievalResult> parseResponse(String rawResponse, int maxResults) {
        if (isBlank(rawResponse)) {
            throw new ServiceException("证据检索服务未返回内容！");
        }
        if (rawResponse.length() > DortmundModuleConfig.Retrieval.MAX_RESPONSE_CHARS) {
            throw new ServiceException("证据检索响应超过允许大小！");
        }
        JSONObject body;
        try {
            body = JSON.parseObject(rawResponse);
        } catch (Exception e) {
            throw new ServiceException("证据检索响应格式不正确！");
        }
        JSONArray values = body == null ? null : body.getJSONArray("results");
        if (values == null && body != null) {
            JSONObject data = body.getJSONObject("data");
            values = data == null ? null : data.getJSONArray("results");
        }
        List<RetrievalResult> results = new ArrayList<>();
        if (values == null) {
            return results;
        }
        for (int i = 0; i < values.size() && results.size() < maxResults; i++) {
            JSONObject item = values.getJSONObject(i);
            if (item == null) {
                continue;
            }
            RetrievalResult result = new RetrievalResult();
            result.setTitle(firstNonBlank(item.getString("title"), item.getString("name")));
            result.setUrl(item.getString("url"));
            result.setSnippet(firstNonBlank(item.getString("snippet"), item.getString("content")));
            result.setSourceName(firstNonBlank(item.getString("sourceName"), item.getString("source")));
            result.setPublishedAt(firstNonBlank(item.getString("publishedAt"), item.getString("publishedTime")));
            // 供应商返回的sourceTier只视为普通外部数据，来源层级始终由平台按URL和来源名称重新分类。
            result.setSourceTier(EvidenceSourceTaxonomy.classifySourceTier(result.getUrl(), result.getSourceName()));
            results.add(result);
        }
        return results;
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(
                DortmundModuleConfig.Retrieval.CONNECT_TIMEOUT_SECONDS).toMillis());
        int readTimeout = dortmundRuntimeProperties.getAi().getRetrieval().getTimeoutSeconds();
        factory.setReadTimeout((int) Duration.ofSeconds(
                Math.max(readTimeout, DortmundModuleConfig.Retrieval.MIN_TIMEOUT_SECONDS)).toMillis());
        return new RestTemplate(factory);
    }

    private String normalizeEndpoint(String endpoint) {
        String value = endpoint.trim();
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            return "https://" + value;
        }
        return value;
    }

    private String firstNonBlank(String first, String second) {
        return isBlank(first) ? second : first;
    }

    private String limitLog(String value) {
        if (isBlank(value)) {
            return "";
        }
        return value.length() > DortmundModuleConfig.Ai.MAX_LOG_RESPONSE_CHARS
                ? value.substring(0, DortmundModuleConfig.Ai.MAX_LOG_RESPONSE_CHARS) : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
