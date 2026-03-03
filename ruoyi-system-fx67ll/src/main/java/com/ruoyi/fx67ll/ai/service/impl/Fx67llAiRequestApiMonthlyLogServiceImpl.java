package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiRequestApiMonthlyLogMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiMonthlyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiMonthlyLogService;

/**
 * AI 调用请求月统计日志Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiRequestApiMonthlyLogServiceImpl implements IFx67llAiRequestApiMonthlyLogService {
    @Autowired
    private Fx67llAiRequestApiMonthlyLogMapper fx67llAiRequestApiMonthlyLogMapper;

    /**
     * 查询AI 调用请求月统计日志
     *
     * @param monthlyLogMonth AI 调用请求月统计日志主键
     * @return AI 调用请求月统计日志
     */
    @Override
    public Fx67llAiRequestApiMonthlyLog selectFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(String monthlyLogMonth) {
        return fx67llAiRequestApiMonthlyLogMapper.selectFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(monthlyLogMonth);
    }

    /**
     * 查询AI 调用请求月统计日志列表
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return AI 调用请求月统计日志
     */
    @Override
    public List<Fx67llAiRequestApiMonthlyLog> selectFx67llAiRequestApiMonthlyLogList(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        return fx67llAiRequestApiMonthlyLogMapper.selectFx67llAiRequestApiMonthlyLogList(fx67llAiRequestApiMonthlyLog);
    }

    /**
     * 新增AI 调用请求月统计日志
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return 结果
     */
    @Override
    public int insertFx67llAiRequestApiMonthlyLog(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        return fx67llAiRequestApiMonthlyLogMapper.insertFx67llAiRequestApiMonthlyLog(fx67llAiRequestApiMonthlyLog);
    }

    /**
     * 修改AI 调用请求月统计日志
     *
     * @param fx67llAiRequestApiMonthlyLog AI 调用请求月统计日志
     * @return 结果
     */
    @Override
    public int updateFx67llAiRequestApiMonthlyLog(Fx67llAiRequestApiMonthlyLog fx67llAiRequestApiMonthlyLog) {
        return fx67llAiRequestApiMonthlyLogMapper.updateFx67llAiRequestApiMonthlyLog(fx67llAiRequestApiMonthlyLog);
    }

    /**
     * 批量删除AI 调用请求月统计日志
     *
     * @param monthlyLogMonths 需要删除的AI 调用请求月统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonths(String[] monthlyLogMonths) {
        return fx67llAiRequestApiMonthlyLogMapper.deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonths(monthlyLogMonths);
    }

    /**
     * 删除AI 调用请求月统计日志信息
     *
     * @param monthlyLogMonth AI 调用请求月统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(String monthlyLogMonth) {
        return fx67llAiRequestApiMonthlyLogMapper.deleteFx67llAiRequestApiMonthlyLogByMonthlyLogMonth(monthlyLogMonth);
    }
}
