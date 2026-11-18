package com.ruoyi.web.controller.fx67ll.dortmund.support;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * Dortmund 接口统一异常处理器
 * <p>
 * 将数据库约束、请求解析和未知运行时异常转换为用户可理解的中文提示，
 * 同时为每次失败生成错误编号并输出包含请求地址、用户和根因的标准日志，便于管理员定位问题。
 *
 * @author fx67ll
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.ruoyi.web.controller.fx67ll.dortmund")
public class DortmundExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DortmundExceptionHandler.class);

    /**
     * 请求参数校验失败
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST, withTraceId(firstBindingMessage(e), traceId));
    }

    /**
     * JSON 请求体校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        String message = e.getBindingResult().getFieldErrors().isEmpty()
                ? "请求参数不符合要求，请检查后重试"
                : e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return AjaxResult.error(HttpStatus.BAD_REQUEST, withTraceId(message, traceId));
    }

    /**
     * 请求体缺失、JSON 格式错误或字段类型无法转换
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("请求内容格式不正确，请检查数字、日期和字段类型是否符合提示", traceId));
    }

    /**
     * 必填请求参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("缺少必填参数「" + e.getParameterName() + "」，请刷新后重试", traceId));
    }

    /**
     * 路径或查询参数类型错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("参数「" + e.getName() + "」格式不正确，请刷新后重新操作", traceId));
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_METHOD,
                withTraceId("当前请求方式不受支持，请刷新页面后重试", traceId));
    }

    /**
     * 请求媒体类型不支持
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public AjaxResult handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.UNSUPPORTED_TYPE,
                withTraceId("请求数据格式不受支持，请刷新页面后重试", traceId));
    }

    /**
     * 上传文件超过系统限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public AjaxResult handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("上传图片超过4MB大小限制，请压缩后重新上传", traceId));
    }

    /**
     * 上传图片扩展名不受支持
     */
    @ExceptionHandler(InvalidExtensionException.class)
    public AjaxResult handleInvalidExtensionException(InvalidExtensionException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("图片格式不受支持，仅支持 JPG、JPEG、PNG、GIF、BMP 格式", traceId));
    }

    /**
     * 上传文件超过服务端业务大小限制
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public AjaxResult handleFileSizeLimitExceededException(FileSizeLimitExceededException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.BAD_REQUEST,
                withTraceId("上传图片超过4MB大小限制，请压缩后重新上传", traceId));
    }

    /**
     * 权限校验失败，保持与 RuoYi 一致的 403 业务码
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        return AjaxResult.error(HttpStatus.FORBIDDEN,
                withTraceId("没有权限执行该操作，请联系管理员授权", traceId));
    }

    /**
     * 数据库完整性约束异常，优先按唯一索引给出可操作提示
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public AjaxResult handleDataIntegrityViolationException(DataIntegrityViolationException e,
            HttpServletRequest request) {
        String traceId = newTraceId();
        logException(traceId, request, e);
        return AjaxResult.error(HttpStatus.ERROR, withTraceId(resolveDataIntegrityMessage(e), traceId));
    }

    /**
     * 其他数据库访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    public AjaxResult handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        String traceId = newTraceId();
        logException(traceId, request, e);
        return AjaxResult.error(HttpStatus.ERROR,
                withTraceId("数据保存或查询失败，请稍后重试；若持续失败请联系管理员", traceId));
    }

    /**
     * 业务校验异常，保留 ServiceException 声明的业务错误码
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        String traceId = newTraceId();
        logBusinessException(traceId, request, e);
        String message = StringUtils.isBlank(e.getMessage()) ? "操作未完成，请检查填写内容后重试" : e.getMessage();
        int code = e.getCode() == null ? HttpStatus.ERROR : e.getCode();
        return AjaxResult.error(code, withTraceId(message, traceId));
    }

    /**
     * 未预期的运行时异常，不向前端暴露 SQL、堆栈或内部类名
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String traceId = newTraceId();
        logException(traceId, request, e);
        return AjaxResult.error(HttpStatus.ERROR,
                withTraceId("系统处理失败，请稍后重试；若持续失败请将错误编号提供给管理员", traceId));
    }

    /**
     * 未预期的受检异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String traceId = newTraceId();
        logException(traceId, request, e);
        return AjaxResult.error(HttpStatus.ERROR,
                withTraceId("系统处理失败，请稍后重试；若持续失败请将错误编号提供给管理员", traceId));
    }

    private String firstBindingMessage(BindException e) {
        if (e.getAllErrors().isEmpty() || StringUtils.isBlank(e.getAllErrors().get(0).getDefaultMessage())) {
            return "请求参数不符合要求，请检查后重试";
        }
        return e.getAllErrors().get(0).getDefaultMessage();
    }

    /**
     * 根据唯一索引或数据库异常关键字生成面向用户的提示，约束名称不返回前端
     */
    private String resolveDataIntegrityMessage(DataIntegrityViolationException e) {
        String message = rootCauseMessage(e).toLowerCase(Locale.ROOT);
        if (message.contains("uk_season_code")) {
            return "赛季编码已存在，请更换编码后重试";
        }
        if (message.contains("uk_team_code")) {
            return "球队编码已存在，请更换编码后重试";
        }
        if (message.contains("uk_match_code")) {
            return "比赛编码已存在，请更换编码后重试";
        }
        if (message.contains("uk_season_home_away_time")) {
            return "同一赛季、主客队和开赛时间的比赛已存在，请直接选择已有比赛";
        }
        if (message.contains("uk_odds_user_code")) {
            return "倍率记录编码已存在，请更换编码后重试";
        }
        if (message.contains("uk_info_point_team")) {
            return "同一信息点不能重复关联同一球队，请刷新后重试";
        }
        if (message.contains("uk_generation_user_code")) {
            return "回报生成批次编码已存在，请刷新后重新生成";
        }
        if (message.contains("uk_combination_user_code")) {
            return "回报组合编码已存在，请刷新后重试";
        }
        if (message.contains("uk_return_image_object_key")) {
            return "回报图片保存冲突，请重新上传图片后重试";
        }
        if (message.contains("uk_combination_match")) {
            return "同一回报组合内不能重复包含同一场比赛，请刷新后重试";
        }
        if (message.contains("uk_plan_return_combination")) {
            return "该回报组合已被其他方案计划使用，请刷新后重新选择";
        }
        if (message.contains("uk_plan_match") || message.contains("uk_extra_plan")
                || message.contains("uk_round_plan")) {
            return "关联关系已存在，请刷新后重试";
        }
        if (message.contains("uk_sim_version_user_no")) {
            return "版本序号已被占用，请刷新后重新提交";
        }
        if (message.contains("uk_sim_round_version_no")) {
            return "当前版本已存在相同轮次序号，请刷新后重新录入";
        }
        if (message.contains("uk_generation_client_request")) {
            return "本次提交已处理，请刷新页面查看结果，勿重复提交";
        }
        if (message.contains("foreign key constraint fails") || message.contains("foreign key")) {
            return "关联数据不存在或仍被其他记录使用，请刷新后重新选择";
        }
        if (message.contains("cannot be null") || message.contains("doesn't have a default value")) {
            return "必填字段不能为空，请检查表单中的必填项";
        }
        if (message.contains("data too long") || message.contains("too long for column")) {
            return "填写内容过长，请按字段提示精简后重试";
        }
        if (message.contains("out of range") || message.contains("incorrect decimal value")
                || message.contains("truncated incorrect")) {
            return "数值格式或范围不符合要求，请按字段提示检查后重试";
        }
        if (message.contains("duplicate entry")) {
            return "数据与现有记录重复，请检查唯一编码或关联关系后重试";
        }
        return "数据保存失败，可能是必填项、唯一性或关联关系冲突，请核对后重试";
    }

    private void logBusinessException(String traceId, HttpServletRequest request, Exception e) {
        log.warn("Dortmund业务异常 traceId={} method={} uri={} user={} exception={} message={}",
                traceId, request.getMethod(), request.getRequestURI(), currentUsername(),
                e.getClass().getName(), e.getMessage());
    }

    private void logException(String traceId, HttpServletRequest request, Exception e) {
        log.error("Dortmund系统异常 traceId={} method={} uri={} user={} exception={} rootCause={} message={}",
                traceId, request.getMethod(), request.getRequestURI(), currentUsername(),
                e.getClass().getName(), rootCause(e).getClass().getName(), rootCauseMessage(e), e);
    }

    private String currentUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "anonymous";
        }
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private String rootCauseMessage(Throwable throwable) {
        String message = rootCause(throwable).getMessage();
        return StringUtils.isBlank(message) ? throwable.getClass().getSimpleName() : message;
    }

    private String withTraceId(String message, String traceId) {
        return message + "（错误编号：" + traceId + "）";
    }

    private String newTraceId() {
        return "DM-" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 8).toUpperCase(Locale.ROOT);
    }
}
