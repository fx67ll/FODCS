package com.ruoyi.fx67ll.lottery.service;

import java.util.List;

import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryHistory;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryTotalReward;

/**
 * 每日号码记录Service接口
 *
 * @author fx67ll
 * @date 2023-08-07
 */
public interface IFx67llLotteryLogService {
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
     * 批量删除每日号码记录
     *
     * @param lotteryIds 需要删除的每日号码记录主键集合
     * @return 结果
     */
    public int deleteFx67llLotteryLogByLotteryIds(Long[] lotteryIds);

    /**
     * 删除每日号码记录信息
     *
     * @param lotteryId 每日号码记录主键
     * @return 结果
     */
    public int deleteFx67llLotteryLogByLotteryIdForApp(Long lotteryId);

    /**
     * 删除每日号码记录信息
     *
     * @param lotteryId 每日号码记录主键
     * @return 结果
     */
    public int deleteFx67llLotteryLogByLotteryId(Long lotteryId);

    /**
     * 查询历史号码记录中奖数据统计
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 历史号码记录中奖统计集合
     */
    public List<Fx67llLotteryTotalReward> selectFx67llLotteryTotalReward(Fx67llLotteryLog fx67llLotteryLog);

    /**
     * 查询历史号码出现频率统计
     *
     * @return 历史号码出现频率统计结果
     */
    public List<Fx67llLotteryHistory> selectFx67llLotteryLogHistoryStatistics();

    public Long mergeFx67llLotteryLogs(Long[] lotteryIds);

    /**
     * 批量新增每日号码记录（一个事务内按入参顺序依次写入）
     * <p>
     * 业务目标：用于"周五一键三连"等需要一次性生成多条不同类型记录的场景，
     * 替代前端多次异步调用 addLog 造成的顺序不可控与原子性缺失问题。
     * <p>
     * 写库规则：
     * 1. 按入参 List 顺序依次 insert，保证保存顺序与传入顺序一致（如排列三→排列五→七星彩）；
     * 2. 每条记录的 createTime 在基准时间上按序号递增 1 秒，入参靠前的记录时间最新，
     * 确保列表按 create_time 倒序时排列三在最上、七星彩在最下，顺序稳定可读；
     * 3. createBy / updateBy 取当前操作人，userId 取当前登录用户；
     * 4. 整个过程在同一个事务中完成，任一条插入失败全部回滚，避免出现"三连只成功两条"的脏数据。
     *
     * @param logList 待新增的每日号码记录集合（顺序即保存顺序）
     * @return 新增记录的主键集合（与入参顺序一致）
     */
    public List<Long> batchInsertFx67llLotteryLogs(List<Fx67llLotteryLog> logList);
}
