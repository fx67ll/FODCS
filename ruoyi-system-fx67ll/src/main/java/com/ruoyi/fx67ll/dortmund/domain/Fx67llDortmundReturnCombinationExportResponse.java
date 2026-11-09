package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.ruoyi.common.annotation.Excel;

/**
 * Dortmund回报组合导出响应对象
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public class Fx67llDortmundReturnCombinationExportResponse {
    @Excel(name = "回报组合编码")
    private String combinationCode;

    @Excel(name = "生成批次编码")
    private String generationCode;

    @Excel(name = "生成模式")
    private String generationMode;

    @Excel(name = "组合标题")
    private String combinationTitle;

    @Excel(name = "组合倍率")
    private BigDecimal combinedOdds;

    @Excel(name = "预计回报")
    private BigDecimal estimatedReturn;

    @Excel(name = "目标偏差")
    private BigDecimal deviationAmount;

    @Excel(name = "是否采用")
    private String adoptedFlag;

    @Excel(name = "比赛数量")
    private Integer matchCount;

    @Excel(name = "审核人")
    private String reviewBy;

    @Excel(name = "审核时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    @Excel(name = "创建时间", width = 20, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Fx67llDortmundReturnCombinationExportResponse(Fx67llDortmundReturnCombination source) {
        this.combinationCode = source.getCombinationCode();
        this.generationCode = source.getGenerationCode();
        this.generationMode = source.getGenerationMode();
        this.combinationTitle = source.getCombinationTitle();
        this.combinedOdds = source.getCombinedOdds();
        this.estimatedReturn = source.getEstimatedReturn();
        this.deviationAmount = source.getDeviationAmount();
        this.adoptedFlag = source.getAdoptedFlag();
        this.matchCount = source.getMatchCount();
        this.reviewBy = source.getReviewBy();
        this.reviewTime = source.getReviewTime();
        this.createTime = source.getCreateTime();
    }
}
