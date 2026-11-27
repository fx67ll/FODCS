package com.ruoyi.fx67ll.dortmund.ai;

/**
 * DeepSeek API 状态码到业务错误提示的统一映射。
 * <p>
 * 原始响应正文只进入受控日志；返回给调用方的信息不包含供应商响应、密钥或请求头。
 *
 * @author fx67ll
 * @date 2026-11-27
 */
final class DeepSeekApiErrorMessages {
    private DeepSeekApiErrorMessages() {
    }

    /**
     * 根据调用阶段和HTTP状态码生成可操作的中文错误。
     *
     * @param operationLabel 调用阶段名称，例如“DeepSeek分析”或“DeepSeek内置检索”
     * @param statusCode     DeepSeek返回的HTTP状态码
     * @return 不含供应商敏感信息的业务错误
     */
    static String forRequest(String operationLabel, int statusCode) {
        if (statusCode == 400) {
            return operationLabel + "请求格式错误，请检查请求体配置！";
        }
        if (statusCode == 401) {
            return operationLabel + "鉴权失败，请检查接口密钥配置！";
        }
        if (statusCode == 402) {
            return operationLabel + "账户余额不足，请充值后重试！";
        }
        if (statusCode == 403) {
            return operationLabel + "鉴权失败或无权访问当前接口，请检查密钥与权限配置！";
        }
        if (statusCode == 408) {
            return operationLabel + "请求超时，请稍后重试！";
        }
        if (statusCode == 413) {
            return operationLabel + "请求体过大，请减少上下文或文件大小！";
        }
        if (statusCode == 422) {
            return operationLabel + "请求参数错误，请检查模型与参数配置！";
        }
        if (statusCode == 429) {
            return operationLabel + "请求过于频繁，请稍后重试！";
        }
        if (statusCode == 500) {
            return operationLabel + "服务内部故障，请稍后重试！";
        }
        if (statusCode == 503) {
            return operationLabel + "服务繁忙，请稍后重试！";
        }
        if (statusCode == 504) {
            return operationLabel + "网关超时，请稍后重试！";
        }
        return operationLabel + "请求失败，HTTP状态码：" + statusCode + "！";
    }
}
