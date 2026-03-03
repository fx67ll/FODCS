package com.ruoyi.fx67ll.ai.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.ai.mapper.Fx67llAiRequestLogMapper;
import com.ruoyi.fx67ll.ai.domain.Fx67llAiRequestLog;
import com.ruoyi.fx67ll.ai.service.IFx67llAiRequestLogService;

/**
 * AI 调用请求日志Service业务层处理
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llAiRequestLogServiceImpl implements IFx67llAiRequestLogService {
    @Autowired
    private Fx67llAiRequestLogMapper fx67llAiRequestLogMapper;

    /**
     * 查询AI 调用请求日志
     *
     * @param requestLogId AI 调用请求日志主键
     * @return AI 调用请求日志
     */
    @Override
    public Fx67llAiRequestLog selectFx67llAiRequestLogByRequestLogId(Long requestLogId) {
        return fx67llAiRequestLogMapper.selectFx67llAiRequestLogByRequestLogId(requestLogId);
    }

    /**
     * 查询AI 调用请求日志列表
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return AI 调用请求日志
     */
    @Override
    public List<Fx67llAiRequestLog> selectFx67llAiRequestLogList(Fx67llAiRequestLog fx67llAiRequestLog) {
        return fx67llAiRequestLogMapper.selectFx67llAiRequestLogList(fx67llAiRequestLog);
    }

    /**
     * 通过 UserId 查询AI 调用请求日志列表
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return AI 调用请求日志
     */
    @Override
    public List<Fx67llAiRequestLog> selectFx67llAiRequestLogListByUserId(Fx67llAiRequestLog fx67llAiRequestLog) {
        fx67llAiRequestLog.setUserId(SecurityUtils.getUserId());
        return fx67llAiRequestLogMapper.selectFx67llAiRequestLogList(fx67llAiRequestLog);
    }

    /**
     * 新增AI 调用请求日志
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return 结果
     */
    @Override
    public int insertFx67llAiRequestLog(Fx67llAiRequestLog fx67llAiRequestLog) {
        fx67llAiRequestLog.setUserId(SecurityUtils.getUserId());
        fx67llAiRequestLog.setCreateBy(SecurityUtils.getUsername());
        fx67llAiRequestLog.setCreateTime(DateUtils.getNowDate());
        return fx67llAiRequestLogMapper.insertFx67llAiRequestLog(fx67llAiRequestLog);
    }

    /**
     * 修改AI 调用请求日志
     *
     * @param fx67llAiRequestLog AI 调用请求日志
     * @return 结果
     */
    @Override
    public int updateFx67llAiRequestLog(Fx67llAiRequestLog fx67llAiRequestLog) {
        fx67llAiRequestLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llAiRequestLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llAiRequestLogMapper.updateFx67llAiRequestLog(fx67llAiRequestLog);
    }

    /**
     * 批量删除AI 调用请求日志
     *
     * @param requestLogIds 需要删除的AI 调用请求日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestLogByRequestLogIds(Long[] requestLogIds) {
        return fx67llAiRequestLogMapper.deleteFx67llAiRequestLogByRequestLogIds(requestLogIds);
    }

    /**
     * 删除AI 调用请求日志信息
     *
     * @param requestLogId AI 调用请求日志主键
     * @return 结果
     */
    @Override
    public int deleteFx67llAiRequestLogByRequestLogId(Long requestLogId) {
        return fx67llAiRequestLogMapper.deleteFx67llAiRequestLogByRequestLogId(requestLogId);
    }
}
