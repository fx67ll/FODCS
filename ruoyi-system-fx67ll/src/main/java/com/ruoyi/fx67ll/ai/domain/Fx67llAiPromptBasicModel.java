package com.ruoyi.fx67ll.ai.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * AI Prompt模型配置对象 fx67ll_ai_prompt_model
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public class Fx67llAiPromptBasicModel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模型唯一标识（主键）
     */
    private Long modelId;

    /**
     * 模型业务编码（唯一，如deepseek-v3）
     */
    @Excel(name = "模型业务编码", readConverterExp = "唯=一，如deepseek-v3")
    private String modelCode;

    /**
     * 模型业务名称（用于前端展示）
     */
    @Excel(name = "模型业务名称", readConverterExp = "用=于前端展示")
    private String modelName;

    /**
     * 模型厂商标识（如Deepseek，doubao）
     */
    @Excel(name = "模型厂商标识", readConverterExp = "如=Deepseek，doubao")
    private String modelVendor;

    /**
     * API密钥ID（外键，关联fx67ll_secret_key.secret_id，存储API Key的加密引用）
     */
    @Excel(name = "API密钥ID", readConverterExp = "外=键，关联fx67ll_secret_key.secret_id，存储API,K=ey的加密引用")
    private Long modelApiKey;

    /**
     * Secret密钥ID（外键，关联fx67ll_secret_key.secret_id，部分厂商需要，可为空）
     */
    @Excel(name = "Secret密钥ID", readConverterExp = "外=键，关联fx67ll_secret_key.secret_id，部分厂商需要，可为空")
    private Long modelSecretKey;

    /**
     * 模型API调用地址（完整URL）
     */
    @Excel(name = "模型API调用地址", readConverterExp = "完=整URL")
    private String modelApiUrl;

    /**
     * API版本号（如v1）
     */
    @Excel(name = "API版本号", readConverterExp = "如=v1")
    private String modelApiVersion;

    /**
     * 模型默认调用参数（JSON格式，如temperature、max_tokens等）
     */
    @Excel(name = "模型默认调用参数", readConverterExp = "J=SON格式，如temperature、max_tokens等")
    private String modelConfigParams;

    /**
     * API请求头扩展配置（JSON格式，用于特殊鉴权或自定义头）
     */
    @Excel(name = "API请求头扩展配置", readConverterExp = "J=SON格式，用于特殊鉴权或自定义头")
    private String modelRequestHeader;

    /**
     * 模型计费单价（元/千Token，用于成本估算）
     */
    @Excel(name = "模型计费单价", readConverterExp = "元=/千Token，用于成本估算")
    private BigDecimal modelTokenPrice;

    /**
     * 计价货币类型（ISO 4217货币码，如CNY、USD）
     */
    @Excel(name = "计价货币类型", readConverterExp = "I=SO,4=217货币码，如CNY、USD")
    private String modelTokenCurrency;

    /**
     * 模型启用状态（字典码：0-启用，1-停用）
     */
    @Excel(name = "模型启用状态", readConverterExp = "字=典码：0-启用，1-停用")
    private String modelStatus;

    /**
     * 模型展示排序（升序排列，数值越小越靠前）
     */
    @Excel(name = "模型展示排序", readConverterExp = "升=序排列，数值越小越靠前")
    private Integer modelSort;

    /**
     * 模型业务备注（说明模型特点、使用限制等）
     */
    @Excel(name = "模型业务备注", readConverterExp = "说=明模型特点、使用限制等")
    private String modelRemark;

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
     * 是否需要排序标志（Y-按sort排序，其他情况按create_time倒序）
     */
    private String isNeedSort;

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

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelVendor(String modelVendor) {
        this.modelVendor = modelVendor;
    }

    public String getModelVendor() {
        return modelVendor;
    }

    public void setModelApiKey(Long modelApiKey) {
        this.modelApiKey = modelApiKey;
    }

    public Long getModelApiKey() {
        return modelApiKey;
    }

    public void setModelSecretKey(Long modelSecretKey) {
        this.modelSecretKey = modelSecretKey;
    }

    public Long getModelSecretKey() {
        return modelSecretKey;
    }

    public void setModelApiUrl(String modelApiUrl) {
        this.modelApiUrl = modelApiUrl;
    }

    public String getModelApiUrl() {
        return modelApiUrl;
    }

    public void setModelApiVersion(String modelApiVersion) {
        this.modelApiVersion = modelApiVersion;
    }

    public String getModelApiVersion() {
        return modelApiVersion;
    }

    public void setModelConfigParams(String modelConfigParams) {
        this.modelConfigParams = modelConfigParams;
    }

    public String getModelConfigParams() {
        return modelConfigParams;
    }

    public void setModelRequestHeader(String modelRequestHeader) {
        this.modelRequestHeader = modelRequestHeader;
    }

    public String getModelRequestHeader() {
        return modelRequestHeader;
    }

    public void setModelTokenPrice(BigDecimal modelTokenPrice) {
        this.modelTokenPrice = modelTokenPrice;
    }

    public BigDecimal getModelTokenPrice() {
        return modelTokenPrice;
    }

    public void setModelTokenCurrency(String modelTokenCurrency) {
        this.modelTokenCurrency = modelTokenCurrency;
    }

    public String getModelTokenCurrency() {
        return modelTokenCurrency;
    }

    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    public void setModelSort(Integer modelSort) {
        this.modelSort = modelSort;
    }

    public Integer getModelSort() {
        return modelSort;
    }

    public void setModelRemark(String modelRemark) {
        this.modelRemark = modelRemark;
    }

    public String getModelRemark() {
        return modelRemark;
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

    public String getIsNeedSort() {
        return isNeedSort;
    }

    public void setIsNeedSort(String isNeedSort) {
        this.isNeedSort = isNeedSort;
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
                .append("modelId", getModelId())
                .append("modelCode", getModelCode())
                .append("modelName", getModelName())
                .append("modelVendor", getModelVendor())
                .append("modelApiKey", getModelApiKey())
                .append("modelSecretKey", getModelSecretKey())
                .append("modelApiUrl", getModelApiUrl())
                .append("modelApiVersion", getModelApiVersion())
                .append("modelConfigParams", getModelConfigParams())
                .append("modelRequestHeader", getModelRequestHeader())
                .append("modelTokenPrice", getModelTokenPrice())
                .append("modelTokenCurrency", getModelTokenCurrency())
                .append("modelStatus", getModelStatus())
                .append("modelSort", getModelSort())
                .append("modelRemark", getModelRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("isNeedSort", getIsNeedSort())
                .append("beginCreateTime", getBeginCreateTime())
                .append("endCreateTime", getEndCreateTime())
                .append("beginUpdateTime", getBeginUpdateTime())
                .append("endUpdateTime", getEndUpdateTime())
                .toString();
    }
}
