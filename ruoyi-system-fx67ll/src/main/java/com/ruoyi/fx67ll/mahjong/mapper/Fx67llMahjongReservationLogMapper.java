package com.ruoyi.fx67ll.mahjong.mapper;

import java.util.List;

import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLogExt;

/**
 * 麻将室预约记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-16
 */
public interface Fx67llMahjongReservationLogMapper {
    /**
     * 查询麻将室预约记录
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 麻将室预约记录
     */
    public Fx67llMahjongReservationLog selectFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId);

    /**
     * 提供给 APP 查询指定麻将室某天的所有预约记录（含用户名、用户联系方式）
     *
     * @param fx67llMahjongReservationLogExt 麻将室预约记录Ext
     * @return 预约记录列表（含用户信息）
     */
    public List<Fx67llMahjongReservationLogExt> selectReservationLogByRoomAndDate(Fx67llMahjongReservationLogExt fx67llMahjongReservationLogExt);

    /**
     * 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 麻将室预约记录集合
     */
    public List<Fx67llMahjongReservationLog> selectFx67llMahjongReservationLogList(Fx67llMahjongReservationLog fx67llMahjongReservationLog);

    /**
     * 新增麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    public int insertFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog);

    /**
     * 修改麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    public int updateFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog);

    /**
     * 删除麻将室预约记录
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 结果
     */
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId);

    /**
     * 批量删除麻将室预约记录
     *
     * @param mahjongReservationLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogIds(Long[] mahjongReservationLogIds);
}
