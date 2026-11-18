package com.ruoyi.fx67ll.dortmund.domain;

/**
 * 回报组合人工AI结果导入请求。
 *
 * @author fx67ll
 * @date 2026-11-16
 */
public class Fx67llDortmundReturnManualImportRequest {
    /** 第三方AI返回的标准化JSON正文 */
    private String responseContent;

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}
