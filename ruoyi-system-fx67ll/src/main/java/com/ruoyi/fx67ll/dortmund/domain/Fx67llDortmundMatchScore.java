package com.ruoyi.fx67ll.dortmund.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 比赛标准化评分对象 fx67ll_dortmund_match_score
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llDortmundMatchScore extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 评分唯一标识（主键）
     */
    private Long scoreId;

    /**
     * 关联分析ID（外键，关联fx67ll_dortmund_match_analysis.analysis_id）
     */
    @Excel(name = "关联分析ID", readConverterExp = "外=键，关联fx67ll_dortmund_match_analysis.analysis_id")
    private Long analysisId;

    /**
     * 关联比赛ID（外键，冗余字段，加速查询）
     */
    @Excel(name = "关联比赛ID", readConverterExp = "外=键，冗余字段，加速查询")
    private Long matchId;

    /**
     * 主队名称（关联查询）
     */
    @Excel(name = "主队名称")
    private String homeTeamName;

    /**
     * 客队名称（关联查询）
     */
    @Excel(name = "客队名称")
    private String awayTeamName;

    /**
     * 主队进攻能力标准化评分（值域：[0,100]，基于近期进攻数据计算）
     */
    @Excel(name = "主队进攻能力标准化评分", readConverterExp = "值=域：[0,100]，基于近期进攻数据计算")
    private BigDecimal homeAttackScore;

    /**
     * 主队防守能力标准化评分（值域：[0,100]，基于近期防守数据计算）
     */
    @Excel(name = "主队防守能力标准化评分", readConverterExp = "值=域：[0,100]，基于近期防守数据计算")
    private BigDecimal homeDefenseScore;

    /**
     * 主队健康状况评分（值域：[0,100]，分值越高表示伤病影响越小）
     */
    @Excel(name = "主队健康状况评分", readConverterExp = "值=域：[0,100]，分值越高表示伤病影响越小")
    private BigDecimal homeInjuryScore;

    /**
     * 主队历史交锋评分（值域：[0,100]，基于对阵客队的历史战绩计算）
     */
    @Excel(name = "主队历史交锋评分", readConverterExp = "值=域：[0,100]，基于对阵客队的历史战绩计算")
    private BigDecimal homeHistoryScore;

    /**
     * 主队综合能力总评分（值域：[0,100]，多维度加权计算）
     */
    @Excel(name = "主队综合能力总评分", readConverterExp = "值=域：[0,100]，多维度加权计算")
    private BigDecimal homeTotalScore;

    /**
     * 客队进攻能力标准化评分（值域：[0,100]，基于近期进攻数据计算）
     */
    @Excel(name = "客队进攻能力标准化评分", readConverterExp = "值=域：[0,100]，基于近期进攻数据计算")
    private BigDecimal awayAttackScore;

    /**
     * 客队防守能力标准化评分（值域：[0,100]，基于近期防守数据计算）
     */
    @Excel(name = "客队防守能力标准化评分", readConverterExp = "值=域：[0,100]，基于近期防守数据计算")
    private BigDecimal awayDefenseScore;

    /**
     * 客队健康状况评分（值域：[0,100]，分值越高表示伤病影响越小）
     */
    @Excel(name = "客队健康状况评分", readConverterExp = "值=域：[0,100]，分值越高表示伤病影响越小")
    private BigDecimal awayInjuryScore;

    /**
     * 客队历史交锋评分（值域：[0,100]，基于对阵主队的历史战绩计算）
     */
    @Excel(name = "客队历史交锋评分", readConverterExp = "值=域：[0,100]，基于对阵主队的历史战绩计算")
    private BigDecimal awayHistoryScore;

    /**
     * 客队综合能力总评分（值域：[0,100]，多维度加权计算）
     */
    @Excel(name = "客队综合能力总评分", readConverterExp = "值=域：[0,100]，多维度加权计算")
    private BigDecimal awayTotalScore;

    /**
     * 比赛预测结果（字典码：0-主队胜，1-平局，2-客队胜）
     */
    @Excel(name = "比赛预测结果", readConverterExp = "字=典码：0-主队胜，1-平局，2-客队胜")
    private String predictedResult;

    /**
     * 预测结果置信度（值域：[0,100]，分值越高表示预测可靠性越强）
     */
    @Excel(name = "预测结果置信度", readConverterExp = "值=域：[0,100]，分值越高表示预测可靠性越强")
    private BigDecimal predictedConfidence;

    /**
     * 评分规则版本号（用于追溯评分逻辑变更）
     */
    @Excel(name = "评分规则版本号", readConverterExp = "用=于追溯评分逻辑变更")
    private String scoreCalcRuleVersion;

    /**
     * 扩展评分数据（JSON格式字符串，存储非标准化评分字段）
     */
    @Excel(name = "扩展评分数据", readConverterExp = "J=SON格式字符串，存储非标准化评分字段")
    private String extraScoreStr;

    /**
     * 评分业务备注
     */
    @Excel(name = "评分业务备注")
    private String scoreRemark;

    /**
     * 逻辑删除标志（字典码：0-存在，2-已删除）
     */
    private String delFlag;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 创建开始时间
     */
    private String beginCreateTime;

    /**
     * 创建结束时间
     */
    private String endCreateTime;

    /**
     * 更新开始时间
     */
    private String beginUpdateTime;

    /**
     * 更新结束时间
     */
    private String endUpdateTime;

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public Long getScoreId() {
        return scoreId;
    }

    public void setAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public Long getAnalysisId() {
        return analysisId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public void setHomeAttackScore(BigDecimal homeAttackScore) {
        this.homeAttackScore = homeAttackScore;
    }

    public BigDecimal getHomeAttackScore() {
        return homeAttackScore;
    }

    public void setHomeDefenseScore(BigDecimal homeDefenseScore) {
        this.homeDefenseScore = homeDefenseScore;
    }

    public BigDecimal getHomeDefenseScore() {
        return homeDefenseScore;
    }

    public void setHomeInjuryScore(BigDecimal homeInjuryScore) {
        this.homeInjuryScore = homeInjuryScore;
    }

    public BigDecimal getHomeInjuryScore() {
        return homeInjuryScore;
    }

    public void setHomeHistoryScore(BigDecimal homeHistoryScore) {
        this.homeHistoryScore = homeHistoryScore;
    }

    public BigDecimal getHomeHistoryScore() {
        return homeHistoryScore;
    }

    public void setHomeTotalScore(BigDecimal homeTotalScore) {
        this.homeTotalScore = homeTotalScore;
    }

    public BigDecimal getHomeTotalScore() {
        return homeTotalScore;
    }

    public void setAwayAttackScore(BigDecimal awayAttackScore) {
        this.awayAttackScore = awayAttackScore;
    }

    public BigDecimal getAwayAttackScore() {
        return awayAttackScore;
    }

    public void setAwayDefenseScore(BigDecimal awayDefenseScore) {
        this.awayDefenseScore = awayDefenseScore;
    }

    public BigDecimal getAwayDefenseScore() {
        return awayDefenseScore;
    }

    public void setAwayInjuryScore(BigDecimal awayInjuryScore) {
        this.awayInjuryScore = awayInjuryScore;
    }

    public BigDecimal getAwayInjuryScore() {
        return awayInjuryScore;
    }

    public void setAwayHistoryScore(BigDecimal awayHistoryScore) {
        this.awayHistoryScore = awayHistoryScore;
    }

    public BigDecimal getAwayHistoryScore() {
        return awayHistoryScore;
    }

    public void setAwayTotalScore(BigDecimal awayTotalScore) {
        this.awayTotalScore = awayTotalScore;
    }

    public BigDecimal getAwayTotalScore() {
        return awayTotalScore;
    }

    public void setPredictedResult(String predictedResult) {
        this.predictedResult = predictedResult;
    }

    public String getPredictedResult() {
        return predictedResult;
    }

    public void setPredictedConfidence(BigDecimal predictedConfidence) {
        this.predictedConfidence = predictedConfidence;
    }

    public BigDecimal getPredictedConfidence() {
        return predictedConfidence;
    }

    public void setScoreCalcRuleVersion(String scoreCalcRuleVersion) {
        this.scoreCalcRuleVersion = scoreCalcRuleVersion;
    }

    public String getScoreCalcRuleVersion() {
        return scoreCalcRuleVersion;
    }

    public void setExtraScoreStr(String extraScoreStr) {
        this.extraScoreStr = extraScoreStr;
    }

    public String getExtraScoreStr() {
        return extraScoreStr;
    }

    public void setScoreRemark(String scoreRemark) {
        this.scoreRemark = scoreRemark;
    }

    public String getScoreRemark() {
        return scoreRemark;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getBeginCreateTime() {
        return beginCreateTime;
    }

    public void setBeginCreateTime(String beginCreateTime) {
        this.beginCreateTime = beginCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getBeginUpdateTime() {
        return beginUpdateTime;
    }

    public void setBeginUpdateTime(String beginUpdateTime) {
        this.beginUpdateTime = beginUpdateTime;
    }

    public String getEndUpdateTime() {
        return endUpdateTime;
    }

    public void setEndUpdateTime(String endUpdateTime) {
        this.endUpdateTime = endUpdateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("scoreId", getScoreId())
                .append("analysisId", getAnalysisId())
                .append("matchId", getMatchId())
                .append("homeTeamName", getHomeTeamName())
                .append("awayTeamName", getAwayTeamName())
                .append("homeAttackScore", getHomeAttackScore())
                .append("homeDefenseScore", getHomeDefenseScore())
                .append("homeInjuryScore", getHomeInjuryScore())
                .append("homeHistoryScore", getHomeHistoryScore())
                .append("homeTotalScore", getHomeTotalScore())
                .append("awayAttackScore", getAwayAttackScore())
                .append("awayDefenseScore", getAwayDefenseScore())
                .append("awayInjuryScore", getAwayInjuryScore())
                .append("awayHistoryScore", getAwayHistoryScore())
                .append("awayTotalScore", getAwayTotalScore())
                .append("predictedResult", getPredictedResult())
                .append("predictedConfidence", getPredictedConfidence())
                .append("scoreCalcRuleVersion", getScoreCalcRuleVersion())
                .append("extraScoreStr", getExtraScoreStr())
                .append("scoreRemark", getScoreRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
