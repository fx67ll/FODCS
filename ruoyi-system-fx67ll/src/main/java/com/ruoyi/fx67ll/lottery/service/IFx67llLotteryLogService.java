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

    /**
     * 合并同期号、同类型的每日号码记录
     * <p>
     * 业务目标：处理同一天内多次生成的同类型、同期号数据，将多条记录合并为一条。
     * <p>
     * 合并规则：
     * 1. 固定追号（chaseNumber）跨记录去重，重复的追号不重复添加；
     * 2. 每日号码（recordNumber）跨记录去重，重复的号码只保留一注；
     * 3. 开奖号码（winningNumber）取第一个非空值（同期号开奖号应一致，不一致视为异常拒绝合并）；
     * 4. 是否中奖（isWin）任一为 Y 则为 Y，否则为 N；
     * 5. 中奖金额（winningPrice）对所有中奖记录金额求和；
     * 6. 合并后总注数（固定追号注数 + 每日号码注数）不超过 5 注；
     * 7. 合并后单字段长度不超过数据库限制（varchar(1023)）。
     * <p>
     * 写库规则：
     * 1. 新建一条全新记录（lotteryId 由数据库自增生成，不沿用任何旧 ID）；
     * 2. 创建时间（createTime）取所有被合并记录中最早的一条，创建人（createBy）取该条原值；
     * 3. 更新时间（updateTime）取合并操作的当前时间，更新人（updateBy）取当前操作人；
     * 4. 删除全部被合并的旧记录。
     * <p>
     * 整个过程在同一个事务中完成（读取-校验-合并-新增-删除），任一步失败全部回滚，
     * 避免前端多次异步调用造成的脏数据错乱问题。
     *
     * @param lotteryIds 需要合并的每日号码记录主键集合
     * @return 合并后新生成记录的主键
     */
    public Long mergeFx67llLotteryLogs(Long[] lotteryIds);
}
