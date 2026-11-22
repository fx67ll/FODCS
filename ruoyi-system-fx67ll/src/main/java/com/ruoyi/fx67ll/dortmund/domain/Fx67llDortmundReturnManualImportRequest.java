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

    /**
     * 导入模式。
     * <p>REPLACE-覆盖本批次现有未采用候选；APPEND-保留现有候选并追加不同AI结论。
     * 为空时按 REPLACE 处理，兼容旧客户端。</p>
     */
    private String importMode;

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getImportMode() {
        return importMode;
    }

    public void setImportMode(String importMode) {
        this.importMode = importMode;
    }
}
