package com.ruoyi.fx67ll.lottery.mapper;

import java.util.List;

import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;

/**
 * 每日号码记录Mapper接口
 *
 * @author fx67ll
 * @date 2023-08-07
 */
public interface Fx67llLotteryLogMapper {
    /**
     * 查询每日号码记录
     *
     * @param lotteryId 每日号码记录主键
     * @return 每日号码记录
     */
    public Fx67llLotteryLog selectFx67llLotteryLogByLotteryId(Long lotteryId);

    /**
     * 查询每日号码记录列表
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 每日号码记录集合
     */
    public List<Fx67llLotteryLog> selectFx67llLotteryLogList(Fx67llLotteryLog fx67llLotteryLog);

    /**
     * 通过 UserId 查询每日号码记录列表
     * 暂时不需要
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 每日号码记录集合
     */
    public List<Fx67llLotteryLog> selectFx67llLotteryLogListByUserId(Fx67llLotteryLog fx67llLotteryLog);

    /**
     * 新增每日号码记录
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 结果
     */
    public int insertFx67llLotteryLog(Fx67llLotteryLog fx67llLotteryLog);

    /**
     * 修改每日号码记录
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 结果
     */
    public int updateFx67llLotteryLog(Fx67llLotteryLog fx67llLotteryLog);

    /**
     * 删除每日号码记录
     *
     * @param lotteryId 每日号码记录主键
     * @return 结果
     */
    public int deleteFx67llLotteryLogByLotteryId(Long lotteryId);

    /**
     * 批量删除每日号码记录
     *
     * @param lotteryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llLotteryLogByLotteryIds(Long[] lotteryIds);
}
