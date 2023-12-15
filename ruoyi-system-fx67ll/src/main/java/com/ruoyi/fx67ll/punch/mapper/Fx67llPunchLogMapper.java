package com.ruoyi.fx67ll.punch.mapper;

import java.util.List;

import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLog;
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLogLost;
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLogTotal;

/**
 * 打卡记录Mapper接口
 *
 * @author fx67ll
 * @date 2023-11-29
 */
public interface Fx67llPunchLogMapper {
    /**
     * 查询打卡记录
     *
     * @param punchId 打卡记录主键
     * @return 打卡记录
     */
    public Fx67llPunchLog selectFx67llPunchLogByPunchId(Long punchId);

    /**
     * 查询打卡记录列表
     *
     * @param fx67llPunchLog 打卡记录
     * @return 打卡记录集合
     */
    public List<Fx67llPunchLog> selectFx67llPunchLogList(Fx67llPunchLog fx67llPunchLog);

    /**
     * 查询打卡工时统计
     *
     * @param fx67llPunchLog 打卡记录
     * @return 打卡工时统计集合
     */
    public List<Fx67llPunchLogTotal> selectFx67llPunchLogTotalTime(Fx67llPunchLog fx67llPunchLog);

    /**
     * 查询缺卡记录
     *
     * @param fx67llPunchLog 打卡记录
     * @return 缺卡记录集合
     */
    public List<Fx67llPunchLogLost> selectFx67llPunchLostLog(Fx67llPunchLog fx67llPunchLog);

    /**
     * 新增打卡记录
     *
     * @param fx67llPunchLog 打卡记录
     * @return 结果
     */
    public int insertFx67llPunchLog(Fx67llPunchLog fx67llPunchLog);

    /**
     * 修改打卡记录
     *
     * @param fx67llPunchLog 打卡记录
     * @return 结果
     */
    public int updateFx67llPunchLog(Fx67llPunchLog fx67llPunchLog);

    /**
     * 删除打卡记录
     *
     * @param punchId 打卡记录主键
     * @return 结果
     */
    public int deleteFx67llPunchLogByPunchId(Long punchId);

    /**
     * 批量删除打卡记录
     *
     * @param punchIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llPunchLogByPunchIds(Long[] punchIds);
}
