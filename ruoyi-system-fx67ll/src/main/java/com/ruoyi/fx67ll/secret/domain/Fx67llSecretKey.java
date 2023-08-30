package com.ruoyi.fx67ll.secret.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 秘钥配置对象 fx67ll_secret_key
 *
 * @author fx67ll
 * @date 2023-08-30
 */
public class Fx67llSecretKey extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 秘钥主键
     */
    private Long secretId;

    /**
     * 秘钥键
     */
    @Excel(name = "秘钥键")
    private String secretKey;

    /**
     * 秘钥值
     */
    @Excel(name = "秘钥值")
    private String secretValue;

    public void setSecretId(Long secretId) {
        this.secretId = secretId;
    }

    public Long getSecretId() {
        return secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretValue(String secretValue) {
        this.secretValue = secretValue;
    }

    public String getSecretValue() {
        return secretValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("secretId", getSecretId())
                .append("secretKey", getSecretKey())
                .append("secretValue", getSecretValue())
                .toString();
    }
}
