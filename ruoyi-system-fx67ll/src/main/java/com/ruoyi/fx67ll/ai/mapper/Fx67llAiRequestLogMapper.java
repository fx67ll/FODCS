package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLog;

/**
 * AI 调用请求日志Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiRequestLogMapper {
    /**
     * 查询AI 调用请求日志
     *
     * @param requestLogId AI 调用请求日志主键
     * @return AI 调用请求日志
     */
    public Fx67llAiRequestLog selectFx67llAiRequestLogByRequestLogId(Long requestLogId);

    /**
     * 查询AI 调用请求日志列表
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return AI 调用请求日志集合
     */
    public List<Fx67llAiRequestLog> selectFx67llAiRequestLogList(Fx67llAiRequestLog fx67llAiRequestLog);

    /**
     * 新增AI 调用请求日志
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return 结果
     */
    public int insertFx67llAiRequestLog(Fx67llAiRequestLog fx67llAiRequestLog);

    /**
     * 修改AI 调用请求日志
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return 结果
     */
    public int updateFx67llAiRequestLog(Fx67llAiRequestLog fx67llAiRequestLog);

    /**
     * 删除AI 调用请求日志
     *
     * @param requestLogId AI 调用请求日志主键
     * @return 结果
     */
    public int deleteFx67llAiRequestLogByRequestLogId(Long requestLogId);

    /**
     * 批量删除AI 调用请求日志
     *
     * @param requestLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiRequestLogByRequestLogIds(Long[] requestLogIds);
}
