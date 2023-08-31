package com.ruoyi.fx67ll.lottery.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottery.mapper.Fx67llLotteryLogMapper;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotteryLogService;

/**
 * 每日号码记录Service业务层处理
 *
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLotteryLogServiceImpl implements IFx67llLotteryLogService {
    @Autowired
    private Fx67llLotteryLogMapper fx67llLotteryLogMapper;

    /**
     * 查询每日号码记录
     *
     * @param lotteryId 每日号码记录主键
     * @return 每日号码记录
     */
    @Override
    public Fx67llLotteryLog selectFx67llLotteryLogByLotteryId(Long lotteryId) {
        return fx67llLotteryLogMapper.selectFx67llLotteryLogByLotteryId(lotteryId);
    }

    /**
     * 查询每日号码记录列表
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 每日号码记录
     */
    @Override
    public List<Fx67llLotteryLog> selectFx67llLotteryLogList(Fx67llLotteryLog fx67llLotteryLog) {
        return fx67llLotteryLogMapper.selectFx67llLotteryLogList(fx67llLotteryLog);
    }

    /**
     * 查询每日号码记录列表
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 每日号码记录
     */
    @Override
    public List<Fx67llLotteryLog> selectFx67llLotteryLogListByUserId(Fx67llLotteryLog fx67llLotteryLog) {
        fx67llLotteryLog.setUserId(SecurityUtils.getUserId());
        return fx67llLotteryLogMapper.selectFx67llLotteryLogList(fx67llLotteryLog);
    }


    /**
     * 新增每日号码记录
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 结果
     */
    @Override
    public int insertFx67llLotteryLog(Fx67llLotteryLog fx67llLotteryLog) {
        fx67llLotteryLog.setUserId(SecurityUtils.getUserId());
        fx67llLotteryLog.setCreateBy(SecurityUtils.getUsername());
        fx67llLotteryLog.setCreateTime(DateUtils.getNowDate());
        return fx67llLotteryLogMapper.insertFx67llLotteryLog(fx67llLotteryLog);
    }

    /**
     * 修改每日号码记录
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 结果
     */
    @Override
    public int updateFx67llLotteryLog(Fx67llLotteryLog fx67llLotteryLog) {
        fx67llLotteryLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llLotteryLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llLotteryLogMapper.updateFx67llLotteryLog(fx67llLotteryLog);
    }

    /**
     * 批量删除每日号码记录
     *
     * @param lotteryIds 需要删除的每日号码记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotteryLogByLotteryIds(Long[] lotteryIds) {
        return fx67llLotteryLogMapper.deleteFx67llLotteryLogByLotteryIds(lotteryIds);
    }

    /**
     * 提供给 APP 删除每日号码记录信息
     *
     * @param lotteryId 需要删除的每日号码记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotteryLogByLotteryIdForApp(Long lotteryId) {
        return fx67llLotteryLogMapper.deleteFx67llLotteryLogByLotteryId(lotteryId);
    }

    /**
     * 删除每日号码记录信息
     *
     * @param lotteryId 每日号码记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotteryLogByLotteryId(Long lotteryId) {
        return fx67llLotteryLogMapper.deleteFx67llLotteryLogByLotteryId(lotteryId);
    }
}
