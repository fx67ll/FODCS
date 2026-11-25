package com.ruoyi.fx67ll.dortmund.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 统一信息点业务交换对象。
 * <p>
 * T 类型通过赛季名称与球队编码建立关系，M 类型通过比赛编码归属比赛；
 * 同时承载标准 JSON 导入和 Excel 导出：所有引用在写入前由服务层按当前用户数据范围解析并校验，
 * 交换文件中的展示名称不直接落库。Excel 仅用于导出查看，信息点导入只接受 JSON。
 *
 * @author fx67ll
 * @date 2026-11-25
 */
public class Fx67llDortmundInfoPointExcel {
    /** 信息点类型：T-赛季球队，M-比赛临时 */
    @Excel(name = "信息点类型", readConverterExp = "T=赛季球队信息点,M=比赛临时信息点",
            prompt = "必填；T 或 M")
    private String infoPointType;

    /** T 类型所属赛季名称 */
    @Excel(name = "赛季名称", width = 30, prompt = "T 类型必填；M 类型留空")
    private String seasonName;

    /** M 类型所属比赛编码 */
    @Excel(name = "比赛编码", width = 24, prompt = "M 类型必填；T 类型留空")
    private String matchCode;

    /** T 类型关联球队编码，多个值使用中文顿号、逗号或分号分隔 */
    @Excel(name = "关联球队编码", width = 34,
            prompt = "T 类型必填；多个球队编码用逗号、分号或顿号分隔")
    private String teamCodes;

    /** 信息点标题 */
    @Excel(name = "信息点标题", width = 28, prompt = "必填")
    private String pointTitle;

    /** 客观信息内容 */
    @Excel(name = "客观信息内容", width = 50, prompt = "必填；只填写可核验的客观事实")
    private String pointContent;

    /** 信息来源名称 */
    @Excel(name = "信息来源名称", width = 26, prompt = "必填")
    private String sourceName;

    /** 来源地址 */
    @Excel(name = "来源地址", width = 42)
    private String sourceUrl;

    /** 来源发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "来源发布时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sourcePublishTime;

    /** 采集或录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "采集或录入时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss",
            prompt = "选填；留空时使用导入时间")
    private Date collectTime;

    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "生效时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss",
            prompt = "选填；留空表示该端不限制")
    private Date effectiveStartTime;

    /** 失效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "失效时间", width = 22, dateFormat = "yyyy-MM-dd HH:mm:ss",
            prompt = "选填；留空表示该端不限制")
    private Date effectiveEndTime;

    /** 信息点状态：0-启用，1-停用 */
    @Excel(name = "信息点状态", readConverterExp = "0=启用,1=停用", prompt = "选填；默认启用")
    private String pointStatus;

    /** 是否可公开：0-不可公开，1-可公开 */
    @Excel(name = "是否可公开", readConverterExp = "0=不可公开,1=可公开", prompt = "选填；默认不可公开")
    private String allowPublic;

    /** 备注 */
    @Excel(name = "备注", width = 36)
    private String pointRemark;

    public Fx67llDortmundInfoPointExcel() {
    }

    /**
     * 从信息点列表行构建导出对象。
     *
     * @param source 信息点列表行
     */
    public Fx67llDortmundInfoPointExcel(Fx67llDortmundInfoPoint source) {
        if (source == null) {
            return;
        }
        this.infoPointType = source.getInfoPointType();
        this.seasonName = source.getSeasonName();
        this.matchCode = source.getMatchCode();
        if (source.getTeamRels() != null) {
            StringBuilder codes = new StringBuilder();
            for (Fx67llDortmundInfoPointTeamRel rel : source.getTeamRels()) {
                if (rel == null || rel.getTeamCode() == null || rel.getTeamCode().trim().isEmpty()) {
                    continue;
                }
                if (codes.length() > 0) {
                    codes.append(',');
                }
                codes.append(rel.getTeamCode().trim());
            }
            this.teamCodes = codes.toString();
        }
        this.pointTitle = source.getPointTitle();
        this.pointContent = source.getPointContent();
        this.sourceName = source.getSourceName();
        this.sourceUrl = source.getSourceUrl();
        this.sourcePublishTime = source.getSourcePublishTime();
        this.collectTime = source.getCollectTime();
        this.effectiveStartTime = source.getEffectiveStartTime();
        this.effectiveEndTime = source.getEffectiveEndTime();
        this.pointStatus = source.getPointStatus();
        this.allowPublic = source.getAllowPublic();
        this.pointRemark = source.getPointRemark();
    }

    public String getInfoPointType() {
        return infoPointType;
    }

    public void setInfoPointType(String infoPointType) {
        this.infoPointType = infoPointType;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getTeamCodes() {
        return teamCodes;
    }

    public void setTeamCodes(String teamCodes) {
        this.teamCodes = teamCodes;
    }

    public String getPointTitle() {
        return pointTitle;
    }

    public void setPointTitle(String pointTitle) {
        this.pointTitle = pointTitle;
    }

    public String getPointContent() {
        return pointContent;
    }

    public void setPointContent(String pointContent) {
        this.pointContent = pointContent;
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

    public Date getSourcePublishTime() {
        return sourcePublishTime;
    }

    public void setSourcePublishTime(Date sourcePublishTime) {
        this.sourcePublishTime = sourcePublishTime;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Date getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Date effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Date getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Date effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public String getPointStatus() {
        return pointStatus;
    }

    public void setPointStatus(String pointStatus) {
        this.pointStatus = pointStatus;
    }

    public String getAllowPublic() {
        return allowPublic;
    }

    public void setAllowPublic(String allowPublic) {
        this.allowPublic = allowPublic;
    }

    public String getPointRemark() {
        return pointRemark;
    }

    public void setPointRemark(String pointRemark) {
        this.pointRemark = pointRemark;
    }
}
