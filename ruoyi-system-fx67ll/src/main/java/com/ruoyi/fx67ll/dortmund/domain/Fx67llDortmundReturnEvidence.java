package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回报组合证据对象 fx67ll_dortmund_return_evidence
 * <p>
 * 仅保存自动AI模式下冻结的已提交证据与系统检索证据。人工AI交换契约不读取本表。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public class Fx67llDortmundReturnEvidence {
    /** 证据主键 */
    private Long evidenceId;

    /** 生成批次主键 */
    private Long generationId;

    /** AI可引用的稳定证据编码 */
    private String evidenceCode;

    /** 关联比赛主键 */
    private Long matchId;

    /** 证据来源（S-已提交业务证据，R-系统检索证据） */
    private String evidenceOrigin;

    /** 证据类型 */
    private String evidenceType;

    /** 原始业务主键或检索条目编码 */
    private String sourceRefId;

    /** 触发该检索的系统查询 */
    private String planQuery;

    /** 证据标题 */
    private String title;

    /** 冻结后的证据正文 */
    private String content;

    /** 来源名称 */
    private String sourceName;

    /** 来源地址，仅来自后端记录 */
    private String sourceUrl;

    /** 来源发布时间 */
    private Date publishedTime;

    /** 系统检索时间 */
    private Date retrievedTime;

    /** 证据正文规范化哈希 */
    private String contentHash;

    /** 供应商无关来源层级（OFFICIAL/MAJOR_MEDIA/SPECIALIST/AGGREGATOR/UNKNOWN） */
    private String sourceTier;

    /** 来源可信度或系统置信度 */
    private BigDecimal confidence;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    public Long getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(Long evidenceId) {
        this.evidenceId = evidenceId;
    }

    public Long getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Long generationId) {
        this.generationId = generationId;
    }

    public String getEvidenceCode() {
        return evidenceCode;
    }

    public void setEvidenceCode(String evidenceCode) {
        this.evidenceCode = evidenceCode;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getEvidenceOrigin() {
        return evidenceOrigin;
    }

    public void setEvidenceOrigin(String evidenceOrigin) {
        this.evidenceOrigin = evidenceOrigin;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getSourceRefId() {
        return sourceRefId;
    }

    public void setSourceRefId(String sourceRefId) {
        this.sourceRefId = sourceRefId;
    }

    public String getPlanQuery() {
        return planQuery;
    }

    public void setPlanQuery(String planQuery) {
        this.planQuery = planQuery;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public Date getRetrievedTime() {
        return retrievedTime;
    }

    public void setRetrievedTime(Date retrievedTime) {
        this.retrievedTime = retrievedTime;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getSourceTier() {
        return sourceTier;
    }

    public void setSourceTier(String sourceTier) {
        this.sourceTier = sourceTier;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
