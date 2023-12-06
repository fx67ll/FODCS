package com.ruoyi.fx67ll.punch.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.punch.mapper.Fx67llPunchLogMapper;
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLog;
import com.ruoyi.fx67ll.punch.service.IFx67llPunchLogService;

/**
 * 打卡记录Service业务层处理
 *
 * @author fx67ll
 * @date 2023-11-29
 */
@Service
public class Fx67llPunchLogServiceImpl implements IFx67llPunchLogService {
    @Autowired
    private Fx67llPunchLogMapper fx67llPunchLogMapper;

    /**
     * 查询打卡记录
     *
     * @param punchId 打卡记录主键
     * @return 打卡记录
     */
    @Override
    public Fx67llPunchLog selectFx67llPunchLogByPunchId(Long punchId) {
        return fx67llPunchLogMapper.selectFx67llPunchLogByPunchId(punchId);
    }

    /**
     * 查询打卡记录列表
     *
     * @param fx67llPunchLog 打卡记录
     * @return 打卡记录
     */
    @Override
    public List<Fx67llPunchLog> selectFx67llPunchLogList(Fx67llPunchLog fx67llPunchLog) {
        return fx67llPunchLogMapper.selectFx67llPunchLogList(fx67llPunchLog);
    }

    /**
     * 查询打卡记录列表
     *
     * @param fx67llPunchLog 打卡记录
     * @return 打卡记录
     */
    @Override
    public List<Fx67llPunchLog> selectFx67llPunchLogListByUserId(Fx67llPunchLog fx67llPunchLog) {
        fx67llPunchLog.setUserId(SecurityUtils.getUserId());
        return fx67llPunchLogMapper.selectFx67llPunchLogList(fx67llPunchLog);
    }

    /**
     * 新增打卡记录
     *
     * @param fx67llPunchLog 打卡记录
     * @return 结果
     */
    @Override
    public int insertFx67llPunchLog(Fx67llPunchLog fx67llPunchLog) {
        fx67llPunchLog.setUserId(SecurityUtils.getUserId());
        fx67llPunchLog.setCreateBy(SecurityUtils.getUsername());
        fx67llPunchLog.setCreateTime(DateUtils.getNowDate());
        fx67llPunchLog.setUpdateBy(SecurityUtils.getUsername());
        System.out.println("111111111111111111111111111111111111:" + DateUtils.getNowDate());
        return fx67llPunchLogMapper.insertFx67llPunchLog(fx67llPunchLog);
    }

    /**
     * 修改打卡记录
     *
     * @param fx67llPunchLog 打卡记录
     * @return 结果
     */
    @Override
    public int updateFx67llPunchLog(Fx67llPunchLog fx67llPunchLog) {
        fx67llPunchLog.setUpdateBy(SecurityUtils.getUsername());
        return fx67llPunchLogMapper.updateFx67llPunchLog(fx67llPunchLog);
    }

    /**
     * 批量删除打卡记录
     *
     * @param punchIds 需要删除的打卡记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llPunchLogByPunchIds(Long[] punchIds) {
        return fx67llPunchLogMapper.deleteFx67llPunchLogByPunchIds(punchIds);
    }

    /**
     * 提供给 APP 删除打卡记录
     *
     * @param punchId 需要删除的打卡记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llPunchLogByPunchIdForApp(Long punchId) {
        return fx67llPunchLogMapper.deleteFx67llPunchLogByPunchId(punchId);
    }

    /**
     * 删除打卡记录信息
     *
     * @param punchId 打卡记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llPunchLogByPunchId(Long punchId) {
        return fx67llPunchLogMapper.deleteFx67llPunchLogByPunchId(punchId);
    }
}
