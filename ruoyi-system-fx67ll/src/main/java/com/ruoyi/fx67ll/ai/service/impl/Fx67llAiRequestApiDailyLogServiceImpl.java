package com.ruoyi.fx67ll.ai.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiRequestApiDailyLogMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiDailyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiDailyLogService;

/**
 * AI 调用请求日统计日志Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiRequestApiDailyLogServiceImpl implements IFx67llAiRequestApiDailyLogService {
    @Autowired
    private Fx67llAiRequestApiDailyLogMapper fx67llAiRequestApiDailyLogMapper;

    /**
     * 查询AI 调用请求日统计日志
     *
     * @param dailyLogDate AI 调用请求日统计日志主键
     * @return AI 调用请求日统计日志
     */
    @Override
    public Fx67llAiRequestApiDailyLog selectFx67llAiRequestApiDailyLogByDailyLogDate(Date dailyLogDate) {
        return fx67llAiRequestApiDailyLogMapper.selectFx67llAiRequestApiDailyLogByDailyLogDate(dailyLogDate);
    }

    /**
     * 查询AI 调用请求日统计日志列表
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return AI 调用请求日统计日志
     */
    @Override
    public List<Fx67llAiRequestApiDailyLog> selectFx67llAiRequestApiDailyLogList(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        return fx67llAiRequestApiDailyLogMapper.selectFx67llAiRequestApiDailyLogList(fx67llAiRequestApiDailyLog);
    }

    /**
     * 新增AI 调用请求日统计日志
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return 结果
     */
    @Override
    public int insertFx67llAiRequestApiDailyLog(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        return fx67llAiRequestApiDailyLogMapper.insertFx67llAiRequestApiDailyLog(fx67llAiRequestApiDailyLog);
    }

    /**
     * 修改AI 调用请求日统计日志
     *
     * @param fx67llAiRequestApiDailyLog AI 调用请求日统计日志
     * @return 结果
     */
    @Override
    public int updateFx67llAiRequestApiDailyLog(Fx67llAiRequestApiDailyLog fx67llAiRequestApiDailyLog) {
        return fx67llAiRequestApiDailyLogMapper.updateFx67llAiRequestApiDailyLog(fx67llAiRequestApiDailyLog);
    }

    /**
     * 批量删除AI 调用请求日统计日志
     *
     * @param dailyLogDates 需要删除的AI 调用请求日统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiDailyLogByDailyLogDates(Date[] dailyLogDates) {
        return fx67llAiRequestApiDailyLogMapper.deleteFx67llAiRequestApiDailyLogByDailyLogDates(dailyLogDates);
    }

    /**
     * 删除AI 调用请求日统计日志信息
     *
     * @param dailyLogDate AI 调用请求日统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiDailyLogByDailyLogDate(Date dailyLogDate) {
        return fx67llAiRequestApiDailyLogMapper.deleteFx67llAiRequestApiDailyLogByDailyLogDate(dailyLogDate);
    }
}
