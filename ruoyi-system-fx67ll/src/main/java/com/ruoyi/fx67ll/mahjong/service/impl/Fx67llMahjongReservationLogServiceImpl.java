package com.ruoyi.fx67ll.mahjong.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.mahjong.mapper.Fx67llMahjongReservationLogMapper;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongReservationLogService;

/**
 * 麻将室预约记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class Fx67llMahjongReservationLogServiceImpl implements IFx67llMahjongReservationLogService {
    @Autowired
    private Fx67llMahjongReservationLogMapper fx67llMahjongReservationLogMapper;

    /**
     * 查询麻将室预约记录
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 麻将室预约记录
     */
    @Override
    public Fx67llMahjongReservationLog selectFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId) {
        return fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId);
    }

    /**
     * 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 麻将室预约记录
     */
    @Override
    public List<Fx67llMahjongReservationLog> selectFx67llMahjongReservationLogList(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        return fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
    }

    /**
     * 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 麻将室预约记录
     */
    @Override
    public List<Fx67llMahjongReservationLog> selectFx67llMahjongReservationLogListByUserId(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        fx67llMahjongReservationLog.setUserId(SecurityUtils.getUserId());
        return fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
    }

    /**
     * 新增麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    @Override
    public int insertFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        fx67llMahjongReservationLog.setUserId(SecurityUtils.getUserId());
        fx67llMahjongReservationLog.setCreateBy(SecurityUtils.getUsername());
        fx67llMahjongReservationLog.setCreateTime(DateUtils.getNowDate());
        fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llMahjongReservationLogMapper.insertFx67llMahjongReservationLog(fx67llMahjongReservationLog);
    }

    /**
     * 修改麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    @Override
    public int updateFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llMahjongReservationLogMapper.updateFx67llMahjongReservationLog(fx67llMahjongReservationLog);
    }

    /**
     * 批量删除麻将室预约记录
     *
     * @param mahjongReservationLogIds 需要删除的麻将室预约记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogIds(Long[] mahjongReservationLogIds) {
        return fx67llMahjongReservationLogMapper.deleteFx67llMahjongReservationLogByMahjongReservationLogIds(mahjongReservationLogIds);
    }

    /**
     * 删除麻将室预约记录信息
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId) {
        return fx67llMahjongReservationLogMapper.deleteFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId);
    }
}
