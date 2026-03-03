package com.ruoyi.fx67ll.ai.mapper;

import java.util.List;

import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiMonthlyLog;

/**
 * AI 调用请求月统计日志Mapper接口
 *
 * @author ruoyi
 * @date 2026-03-03
 */
public interface Fx67llAiRequestApiMonthlyLogMapper {
    /**
     * 查询AI 调用请求月统计日志
     *
     * @param monthlyLogMonth AI 调用请求月统计日志主键
     * @return AI 调用请求月统计日志
     */
    public Fx67llAiRequestApiMonthlyLog selectFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(String monthlyLogMonth);

    /**
     * 查询AI 调用请求月统计日志列表
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return AI 调用请求月统计日志集合
     */
    public List<Fx67llAiRequestApiMonthlyLog> selectFx67llAiRequestApiMonthlyLogList(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog);

    /**
     * 新增AI 调用请求月统计日志
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return 结果
     */
    public int insertFx67llAiRequestApiMonthlyLog(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog);

    /**
     * 修改AI 调用请求月统计日志
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return 结果
     */
    public int updateFx67llAiRequestApiMonthlyLog(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog);

    /**
     * 删除AI 调用请求月统计日志
     *
     * @param monthlyLogMonth AI 调用请求月统计日志主键
     * @return 结果
     */
    public int deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(String monthlyLogMonth);

    /**
     * 批量删除AI 调用请求月统计日志
     *
     * @param monthlyLogMonths 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonths(String[] monthlyLogMonths);
}
