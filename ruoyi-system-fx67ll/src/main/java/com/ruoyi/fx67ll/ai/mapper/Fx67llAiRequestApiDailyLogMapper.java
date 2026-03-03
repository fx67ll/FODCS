package com.ruoyi.fx67ll.ai.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiDailyLog;

/**
 * AI 调用请求日统计日志Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiRequestApiDailyLogMapper {
    /**
     * 查询AI 调用请求日统计日志
     *
     * @param dailyLogDate AI 调用请求日统计日志主键
     * @return AI 调用请求日统计日志
     */
    public Fx67llAiRequestApiDailyLog selectFx67llAiRequestApiDailyLogByDailyLogDate(Date dailyLogDate);

    /**
     * 查询AI 调用请求日统计日志列表
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return AI 调用请求日统计日志集合
     */
    public List<Fx67llAiRequestApiDailyLog> selectFx67llAiRequestApiDailyLogList(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog);

    /**
     * 新增AI 调用请求日统计日志
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return 结果
     */
    public int insertFx67llAiRequestApiDailyLog(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog);

    /**
     * 修改AI 调用请求日统计日志
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return 结果
     */
    public int updateFx67llAiRequestApiDailyLog(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog);

    /**
     * 删除AI 调用请求日统计日志
     *
     * @param dailyLogDate AI 调用请求日统计日志主键
     * @return 结果
     */
    public int deleteFx67llAiRequestApiDailyLogByDailyLogDate(Date dailyLogDate);

    /**
     * 批量删除AI 调用请求日统计日志
     *
     * @param dailyLogDates 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiRequestApiDailyLogByDailyLogDates(Date[] dailyLogDates);
}
