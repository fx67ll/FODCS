package com.ruoyi.fx67ll.lottory.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottory.mapper.Fx67llLottoryLogMapper;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryLog;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottoryLogService;

/**
 * 每日号码记录Service业务层处理
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLottoryLogServiceImpl implements IFx67llLottoryLogService 
{
    @Autowired
    private Fx67llLottoryLogMapper fx67llLottoryLogMapper;

    /**
     * 查询每日号码记录
     * 
     * @param lottoryId 每日号码记录主键
     * @return 每日号码记录
     */
    @Override
    public Fx67llLottoryLog selectFx67llLottoryLogByLottoryId(Long lottoryId)
    {
        return fx67llLottoryLogMapper.selectFx67llLottoryLogByLottoryId(lottoryId);
    }

    /**
     * 查询每日号码记录列表
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 每日号码记录
     */
    @Override
    public List<Fx67llLottoryLog> selectFx67llLottoryLogList(Fx67llLottoryLog fx67llLottoryLog)
    {
        return fx67llLottoryLogMapper.selectFx67llLottoryLogList(fx67llLottoryLog);
    }

    /**
     * 新增每日号码记录
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 结果
     */
    @Override
    public int insertFx67llLottoryLog(Fx67llLottoryLog fx67llLottoryLog)
    {
        fx67llLottoryLog.setCreateTime(DateUtils.getNowDate());
        return fx67llLottoryLogMapper.insertFx67llLottoryLog(fx67llLottoryLog);
    }

    /**
     * 修改每日号码记录
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 结果
     */
    @Override
    public int updateFx67llLottoryLog(Fx67llLottoryLog fx67llLottoryLog)
    {
        fx67llLottoryLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llLottoryLogMapper.updateFx67llLottoryLog(fx67llLottoryLog);
    }

    /**
     * 批量删除每日号码记录
     * 
     * @param lottoryIds 需要删除的每日号码记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottoryLogByLottoryIds(Long[] lottoryIds)
    {
        return fx67llLottoryLogMapper.deleteFx67llLottoryLogByLottoryIds(lottoryIds);
    }

    /**
     * 删除每日号码记录信息
     * 
     * @param lottoryId 每日号码记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottoryLogByLottoryId(Long lottoryId)
    {
        return fx67llLottoryLogMapper.deleteFx67llLottoryLogByLottoryId(lottoryId);
    }
}
