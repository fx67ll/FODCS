package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiRequestApiYearlyLogMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestApiYearlyLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestApiYearlyLogService;

/**
 * AI 调用请求年统计日志Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiRequestApiYearlyLogServiceImpl implements IFx67llAiRequestApiYearlyLogService {
    @Autowired
    private Fx67llAiRequestApiYearlyLogMapper fx67llAiRequestApiYearlyLogMapper;

    /**
     * 查询AI 调用请求年统计日志
     *
     * @param yearlyLogYear AI 调用请求年统计日志主键
     * @return AI 调用请求年统计日志
     */
    @Override
    public Fx67llAiRequestApiYearlyLog selectFx67llAiRequestApiYearlyLogByYearlyLogYear(String yearlyLogYear) {
        return fx67llAiRequestApiYearlyLogMapper.selectFx67llAiRequestApiYearlyLogByYearlyLogYear(yearlyLogYear);
    }

    /**
     * 查询AI 调用请求年统计日志列表
     *
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return AI 调用请求年统计日志
     */
    @Override
    public List<Fx67llAiRequestApiYearlyLog> selectFx67llAiRequestApiYearlyLogList(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        return fx67llAiRequestApiYearlyLogMapper.selectFx67llAiRequestApiYearlyLogList(fx67llAiRequestApiYearlyLog);
    }

    /**
     * 新增AI 调用请求年统计日志
     *
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return 结果
     */
    @Override
    public int insertFx67llAiRequestApiYearlyLog(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        return fx67llAiRequestApiYearlyLogMapper.insertFx67llAiRequestApiYearlyLog(fx67llAiRequestApiYearlyLog);
    }

    /**
     * 修改AI 调用请求年统计日志
     *
     * @param fx67llAiRequestApiYearlyLog AI 调用请求年统计日志
     * @return 结果
     */
    @Override
    public int updateFx67llAiRequestApiYearlyLog(Fx67llAiRequestApiYearlyLog fx67llAiRequestApiYearlyLog) {
        return fx67llAiRequestApiYearlyLogMapper.updateFx67llAiRequestApiYearlyLog(fx67llAiRequestApiYearlyLog);
    }

    /**
     * 批量删除AI 调用请求年统计日志
     *
     * @param yearlyLogYears 需要删除的AI 调用请求年统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiYearlyLogByYearlyLogYears(String[] yearlyLogYears) {
        return fx67llAiRequestApiYearlyLogMapper.deleteFx67llAiRequestApiYearlyLogByYearlyLogYears(yearlyLogYears);
    }

    /**
     * 删除AI 调用请求年统计日志信息
     *
     * @param yearlyLogYear AI 调用请求年统计日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestApiYearlyLogByYearlyLogYear(String yearlyLogYear) {
        return fx67llAiRequestApiYearlyLogMapper.deleteFx67llAiRequestApiYearlyLogByYearlyLogYear(yearlyLogYear);
    }
}
