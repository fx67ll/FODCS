package com.ruoyi.fx67ll.ai.service;

import java.util.List;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiYearlyLog;

/**
 * AI 调用请求年统计日志Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llAiRequestApiYearlyLogService 
{
    /**
     * 查询AI 调用请求年统计日志
     * 
     * @param yearlyLogYear AI 调用请求年统计日志主键
     * @return AI 调用请求年统计日志
     */
    public Fx67llAiRequestApiYearlyLog selectFx67llAiRequestApiYearlyLogByYearlyLogYear(String yearlyLogYear);

    /**
     * 查询AI 调用请求年统计日志列表
     * 
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return AI 调用请求年统计日志集合
     */
    public List<Fx67llAiRequestApiYearlyLog> selectFx67llAiRequestApiYearlyLogList(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog);

    /**
     * 新增AI 调用请求年统计日志
     * 
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return 结果
     */
    public int insertFx67llAiRequestApiYearlyLog(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog);

    /**
     * 修改AI 调用请求年统计日志
     * 
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return 结果
     */
    public int updateFx67llAiRequestApiYearlyLog(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog);

    /**
     * 批量删除AI 调用请求年统计日志
     * 
     * @param yearlyLogYears 需要删除的AI 调用请求年统计日志主键集合
     * @return 结果
     */
    public int deleteFx67llAiRequestApiYearlyLogByYearlyLogYears(String[] yearlyLogYears);

    /**
     * 删除AI 调用请求年统计日志信息
     * 
     * @param yearlyLogYear AI 调用请求年统计日志主键
     * @return 结果
     */
    public int deleteFx67llAiRequestApiYearlyLogByYearlyLogYear(String yearlyLogYear);
}
