package com.ruoyi.fx67ll.dortmund.service.impl;

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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ruoyi.common.core.redis.RedisCache;
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
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationItemMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnCombinationMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundReturnGenerationMapper;
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
    private static final String IMAGE_ISSUED_PREFIX = "dortmund:return:image:issued:";
    private static final Set<String> SELECTION_TYPES = new HashSet<>();
    static {
        Collections.addAll(SELECTION_TYPES, "HW", "DR", "AW", "HH", "HD", "HA");
    }

    @Autowired
    private Fx67llDortmundReturnGenerationMapper fx67llDortmundReturnGenerationMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationMapper fx67llDortmundReturnCombinationMapper;

    @Autowired
    private Fx67llDortmundReturnCombinationItemMapper fx67llDortmundReturnCombinationItemMapper;

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    @Autowired
    private IFx67llDortmundOddsService fx67llDortmundOddsService;

    @Autowired
    private ReturnCombinationAiAnalysisPort returnCombinationAiAnalysisPort;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 生成回报组合
     *
     * @param request 回报组合生成请求
     * @return 生成批次及候选组合
     */
    @Override
    public Fx67llDortmundReturnGeneration generate(Fx67llDortmundReturnGenerateRequest request) {
        validateRequest(request);
        validateImageKeys(request.getImageObjectKeys());
        List<Fx67llDortmundOdds> oddsList = loadRequestOdds(request.getMatchIds());
        Fx67llDortmundReturnGeneration generation = new Fx67llDortmundReturnGeneration();
        Date now = DateUtils.getNowDate();
        generation.setGenerationCode(createCode("G"));
        generation.setGenerationMode(request.getGenerationMode());
        generation.setInputAmount(request.getInputAmount());
        generation.setTargetAmount(request.getTargetAmount());
        generation.setGenerationStatus(STATUS_PROCESSING);
        generation.setGeneratedCount(0);
        generation.setStartedTime(now);
        generation.setUserId(SecurityUtils.getUserId());
        generation.setCreateBy(SecurityUtils.getUsername());
        generation.setUpdateBy(SecurityUtils.getUsername());
        generation.setCreateTime(now);
        generation.setUpdateTime(now);
        generation.setRequestSnapshot(buildSnapshot(request, oddsList));
        fx67llDortmundReturnGenerationMapper.insertFx67llDortmundReturnGeneration(generation);
        try {
            ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput aiInput = new ReturnCombinationAiAnalysisPort.ReturnCombinationAiInput();
            aiInput.setGenerationMode(request.getGenerationMode());
            aiInput.setInputAmount(request.getInputAmount());
            aiInput.setTargetAmount(request.getTargetAmount());
            aiInput.setOdds(buildAiOddsList(oddsList, request.getGenerationMode()));
            List<PreparedCombination> prepared = prepareCombinations(
                    returnCombinationAiAnalysisPort.analyze(aiInput), request, oddsList);
            if (prepared.isEmpty()) {
                throw new ServiceException("AI未生成可用的回报组合！");
            }
            persistSuccess(generation, prepared, request.getImageObjectKeys());
            generation.setGenerationStatus(STATUS_SUCCESS);
            generation.setGeneratedCount(prepared.size());
            generation.setFinishedTime(DateUtils.getNowDate());
            List<Fx67llDortmundReturnCombination> combinations = new ArrayList<>();
            for (PreparedCombination value : prepared) {
                combinations.add(value.combination);
            }
            generation.setCombinations(combinations);
            return generation;
        } catch (ServiceException e) {
            markFailed(generation, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("回报组合生成失败，生成批次ID：{}", generation.getGenerationId(), e);
            markFailed(generation, "回报组合生成失败，请稍后重试！");
            throw new ServiceException("回报组合生成失败，请稍后重试！");
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
    public Fx67llDortmundReturnGeneration selectGenerationById(Long generationId) {
        Fx67llDortmundReturnGeneration value = fx67llDortmundReturnGenerationMapper
                .selectFx67llDortmundReturnGenerationByGenerationId(generationId);
        if (value == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(value.getUserId()))) {
            return null;
        }
        value.setCombinations(selectCombinationListByGeneration(generationId));
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
        if (!hasAllDataScope()) {
            query.setUserId(SecurityUtils.getUserId());
        }
        List<Fx67llDortmundReturnCombination> result = fx67llDortmundReturnCombinationMapper
                .selectFx67llDortmundReturnCombinationList(query);
        for (Fx67llDortmundReturnCombination value : result) {
            value.setCombinationRemark(null);
            value.setCombinationImageObjectKeys(null);
            value.setImageObjectKeys(null);
            value.setMatches(null);
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
        if (hasPermission("dortmund:return:image:query")) {
            value.setImageObjectKeys(splitImageObjectKeys(value.getCombinationImageObjectKeys()));
        } else {
            value.setCombinationImageObjectKeys(null);
            value.setImageObjectKeys(null);
        }
        value.setMatches(fx67llDortmundReturnCombinationItemMapper.selectItemsByCombinationId(combinationId));
        fillSelectionReasons(combinationId, value.getCombinationRemark(), value.getMatches());
        return value;
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
        value.setAdoptedFlag(adoptedFlag);
        value.setReviewBy(SecurityUtils.getUsername());
        value.setReviewTime(DateUtils.getNowDate());
        value.setUpdateBy(SecurityUtils.getUsername());
        value.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundReturnCombinationMapper.updateAdoptedFlag(value);
    }

    /**
     * 上传回报组合图片
     *
     * @param file 上传的图片文件
     * @return 相对对象键
     */
    @Override
    public String uploadReturnImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("上传图片不能为空！");
        }
        String objectKey = FileUploadUtils.upload(RuoYiConfig.getUploadPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
        redisCache.setCacheObject(IMAGE_ISSUED_PREFIX + SecurityUtils.getUserId() + ":" + objectKey,
                SecurityUtils.getUsername(), 24, TimeUnit.HOURS);
        return objectKey;
    }

    /**
     * 删除尚未固化的回报组合图片
     *
     * @param objectKey 图片相对对象键
     * @return 结果
     */
    @Override
    public int removeTemporaryReturnImage(String objectKey) {
        if (blank(objectKey)) {
            throw new ServiceException("图片对象键不能为空！");
        }
        String issuedKey = IMAGE_ISSUED_PREFIX + SecurityUtils.getUserId() + ":" + objectKey;
        if (redisCache.getCacheObject(issuedKey) == null) {
            throw new ServiceException("图片对象键不存在或已过期！");
        }
        if (!objectKey.startsWith(Constants.RESOURCE_PREFIX + "/")) {
            throw new ServiceException("图片对象键格式不正确！");
        }
        redisCache.deleteObject(issuedKey);
        try {
            FileUtils.deleteFile(RuoYiConfig.getProfile() + objectKey.substring(Constants.RESOURCE_PREFIX.length()));
        } catch (Exception e) {
            log.warn("删除临时回报组合图片失败，对象键：{}", objectKey, e);
        }
        return 1;
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
        StringBuilder signature = new StringBuilder();
        int sort = 0;
        for (ReturnCombinationAiAnalysisPort.Selection selection : candidate.getSelections()) {
            if (selection == null || selection.getMatchId() == null || !seenMatches.add(selection.getMatchId())
                    || !oddsMap.containsKey(selection.getMatchId())
                    || !SELECTION_TYPES.contains(selection.getSelectionType())) {
                return null;
            }
            Fx67llDortmundOdds odds = oddsMap.get(selection.getMatchId());
            BigDecimal manualWeight = manualWeight(odds, selection.getSelectionType());
            BigDecimal appliedWeight = "S".equals(request.getGenerationMode()) ? manualWeight : selection.getAppliedWeight();
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
            item.setAppliedWeight(appliedWeight);
            item.setItemSort(sort++);
            item.setSelectionReason(limit(selection.getReason(), 1023));
            items.add(item);
            signature.append(selection.getMatchId()).append(':').append(selection.getSelectionType()).append(';');
        }
        BigDecimal estimated = request.getInputAmount().multiply(combined).setScale(2, RoundingMode.HALF_UP);
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
        result.signature = signature.toString();
        result.combination = combination;
        result.items = items;
        return result;
    }

    private void persistSuccess(Fx67llDortmundReturnGeneration generation, List<PreparedCombination> prepared,
            List<String> imageKeys) {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        transaction.execute(status -> {
            for (PreparedCombination value : prepared) {
                Fx67llDortmundReturnCombination combination = value.combination;
                combination.setGenerationId(generation.getGenerationId());
                combination.setUserId(generation.getUserId());
                combination.setCombinationImageObjectKeys(
                        imageKeys == null || imageKeys.isEmpty() ? null : String.join(",", imageKeys));
                if (hasPermission("dortmund:return:image:query") && imageKeys != null && !imageKeys.isEmpty()) {
                    combination.setImageObjectKeys(imageKeys.toArray(new String[0]));
                }
                combination.setCreateBy(SecurityUtils.getUsername());
                combination.setUpdateBy(SecurityUtils.getUsername());
                combination.setCreateTime(DateUtils.getNowDate());
                combination.setUpdateTime(DateUtils.getNowDate());
                fx67llDortmundReturnCombinationMapper.insertFx67llDortmundReturnCombination(combination);
                for (Fx67llDortmundReturnCombinationItem item : value.items) {
                    item.setCombinationId(combination.getCombinationId());
                    item.setCreateBy(SecurityUtils.getUsername());
                    item.setCreateTime(DateUtils.getNowDate());
                    fx67llDortmundReturnCombinationItemMapper.insertFx67llDortmundReturnCombinationItem(item);
                }
            }
            generation.setGenerationStatus(STATUS_SUCCESS);
            generation.setGeneratedCount(prepared.size());
            generation.setFinishedTime(DateUtils.getNowDate());
            generation.setGenerationRemark("生成成功。");
            generation.setUpdateBy(SecurityUtils.getUsername());
            generation.setUpdateTime(DateUtils.getNowDate());
            fx67llDortmundReturnGenerationMapper.updateFx67llDortmundReturnGeneration(generation);
            return null;
        });
        if (imageKeys != null) {
            for (String key : imageKeys) {
                try {
                    redisCache.deleteObject(IMAGE_ISSUED_PREFIX + SecurityUtils.getUserId() + ":" + key);
                } catch (Exception e) {
                    log.warn("清理已固化图片签发记录失败，对象键：{}", key, e);
                }
            }
        }
    }

    private void markFailed(Fx67llDortmundReturnGeneration generation, String remark) {
        try {
            generation.setGenerationStatus(STATUS_FAILED);
            generation.setGenerationRemark(limit(remark, 1023));
            generation.setFinishedTime(DateUtils.getNowDate());
            generation.setUpdateBy(SecurityUtils.getUsername());
            generation.setUpdateTime(DateUtils.getNowDate());
            fx67llDortmundReturnGenerationMapper.updateFx67llDortmundReturnGeneration(generation);
        } catch (Exception e) {
            log.error("更新失败生成批次状态异常，生成批次ID：{}", generation.getGenerationId(), e);
        }
    }

    private List<Fx67llDortmundReturnCombination> selectCombinationListByGeneration(Long generationId) {
        Fx67llDortmundReturnCombination query = new Fx67llDortmundReturnCombination();
        query.setGenerationId(generationId);
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
        if (request.getMatchIds() == null || request.getMatchIds().size() < 1
                || request.getMatchIds().size() > 8 || request.getMatchIds().contains(null)
                || new HashSet<>(request.getMatchIds()).size() != request.getMatchIds().size()) {
            throw new ServiceException("比赛列表必须包含1至8场不重复的比赛！");
        }
    }

    private void validateImageKeys(List<String> keys) {
        if (keys == null) {
            return;
        }
        if (keys.size() > 3 || new LinkedHashSet<>(keys).size() != keys.size()) {
            throw new ServiceException("最多只能提交三张不重复的图片！");
        }
        for (String key : keys) {
            if (blank(key) || key.length() > 1023 || !key.startsWith(Constants.RESOURCE_PREFIX + "/")
                    || redisCache.getCacheObject(IMAGE_ISSUED_PREFIX + SecurityUtils.getUserId() + ":" + key) == null) {
                throw new ServiceException("图片对象键格式不正确、不存在或已过期！");
            }
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

    private BigDecimal manualWeight(Fx67llDortmundOdds odds, String type) {
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
        return value == null ? new BigDecimal("100") : value;
    }

    private String buildSnapshot(Fx67llDortmundReturnGenerateRequest request, List<Fx67llDortmundOdds> odds) {
        JSONObject value = new JSONObject();
        value.put("generationMode", request.getGenerationMode());
        value.put("inputAmount", request.getInputAmount());
        value.put("targetAmount", request.getTargetAmount());
        value.put("matchIds", request.getMatchIds());
        value.put("odds", buildAiOddsList(odds, request.getGenerationMode()));
        return value.toJSONString();
    }

    private List<Fx67llDortmundOdds> buildAiOddsList(List<Fx67llDortmundOdds> oddsList, String generationMode) {
        List<Fx67llDortmundOdds> result = new ArrayList<>();
        for (Fx67llDortmundOdds source : oddsList) {
            Fx67llDortmundOdds target = new Fx67llDortmundOdds();
            target.setOddsId(source.getOddsId());
            target.setMatchId(source.getMatchId());
            target.setMatchCode(source.getMatchCode());
            target.setOddsCode(source.getOddsCode());
            target.setSourceTime(source.getSourceTime());
            target.setHandicapValue(source.getHandicapValue());
            target.setHomeWinOdds(source.getHomeWinOdds());
            target.setDrawOdds(source.getDrawOdds());
            target.setAwayWinOdds(source.getAwayWinOdds());
            target.setHandicapHomeWinOdds(source.getHandicapHomeWinOdds());
            target.setHandicapDrawOdds(source.getHandicapDrawOdds());
            target.setHandicapAwayWinOdds(source.getHandicapAwayWinOdds());
            target.setHomeWinWeight(effectiveAiWeight(source.getHomeWinWeight(), generationMode));
            target.setDrawWeight(effectiveAiWeight(source.getDrawWeight(), generationMode));
            target.setAwayWinWeight(effectiveAiWeight(source.getAwayWinWeight(), generationMode));
            target.setHandicapHomeWinWeight(effectiveAiWeight(source.getHandicapHomeWinWeight(), generationMode));
            target.setHandicapDrawWeight(effectiveAiWeight(source.getHandicapDrawWeight(), generationMode));
            target.setHandicapAwayWinWeight(effectiveAiWeight(source.getHandicapAwayWinWeight(), generationMode));
            result.add(target);
        }
        return result;
    }

    private BigDecimal effectiveAiWeight(BigDecimal weight, String generationMode) {
        return "S".equals(generationMode) && weight == null ? new BigDecimal("100") : weight;
    }

    private String[] splitImageObjectKeys(String objectKeys) {
        return blank(objectKeys) ? null : objectKeys.split(",");
    }

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
                if (selection != null && selection.getLong("matchId") != null) {
                    reasons.put(selection.getLong("matchId") + ":" + selection.getString("selectionType"),
                            selection.getString("reason"));
                }
            }
            for (Fx67llDortmundReturnCombinationItem item : items) {
                item.setSelectionReason(reasons.get(item.getMatchId() + ":" + item.getSelectionType()));
            }
        } catch (Exception e) {
            log.warn("回报组合明细理由解析失败，组合ID：{}", combinationId);
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

    private static class PreparedCombination {
        private String signature;
        private Fx67llDortmundReturnCombination combination;
        private List<Fx67llDortmundReturnCombinationItem> items;
    }
}
