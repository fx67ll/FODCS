package com.ruoyi.fx67ll.lottery.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.fx67ll.lottery.mapper.Fx67llLotteryLogMapper;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryTotalReward;
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
     * 通过 UserId 查询每日号码记录列表
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

    /**
     * 查询历史号码记录中奖数据统计
     *
     * @param fx67llLotteryLog 每日号码记录
     * @return 历史号码记录中奖统计集合
     */
    @Override
    public List<Fx67llLotteryTotalReward> selectFx67llLotteryTotalReward(Fx67llLotteryLog fx67llLotteryLog) {
        fx67llLotteryLog.setUserId(SecurityUtils.getUserId());
        return fx67llLotteryLogMapper.selectFx67llLotteryTotalReward(fx67llLotteryLog);
    }

    /**
     * 查询历史号码出现频率统计
     *
     * @return 历史号码出现频率统计结果
     */
    @Override
    public List<Fx67llLotteryHistory> selectFx67llLotteryLogHistoryStatistics() {
        return fx67llLotteryLogMapper.selectFx67llLotteryLogHistoryStatistics();
    }

    /**
     * 号码字段的单注最大长度限制（数据库 record_number / chase_number 均为 varchar(1023)）
     */
    private static final int NUMBER_FIELD_MAX_LENGTH = 1023;

    /**
     * 合并后单条记录允许的最大号码注数（固定追号注数 + 每日号码注数）
     */
    private static final int MERGE_MAX_NOTE_COUNT = 5;

    /**
     * 单次合并允许传入的最大记录数量，限制 N+1 查询规模与输入规模
     */
    private static final int MERGE_MAX_INPUT_COUNT = 23;

    /**
     * 合并同期号、同类型的每日号码记录
     * <p>
     * 详见接口注释。该方法在一个事务内完成"读取-校验-合并-新增-删除"全流程，
     * 任一步抛出异常（含 ServiceException 业务校验异常）都会触发回滚，保证不会出现
     * "合并了但没删"或"删了但没合并"的脏数据。
     *
     * @param lotteryIds 需要合并的每日号码记录主键集合
     * @return 合并后新生成记录的主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long mergeFx67llLotteryLogs(Long[] lotteryIds) {
        // ---------- 1. 入参基础校验 ----------
        if (lotteryIds == null || lotteryIds.length < 2) {
            throw new ServiceException("请至少选择两条记录进行合并！");
        }
        // 限制单次合并数量上限，防止恶意传入超大数组造成 N+1 查询与超长字符串拼接
        if (lotteryIds.length > MERGE_MAX_INPUT_COUNT) {
            throw new ServiceException("单次最多合并" + MERGE_MAX_INPUT_COUNT + "条记录！");
        }
        // 对入参主键去重，避免同一 ID 重复传入导致逻辑异常
        Set<Long> idSet = new LinkedHashSet<>(Arrays.asList(lotteryIds));
        if (idSet.size() < 2) {
            throw new ServiceException("请至少选择两条不同的记录进行合并！");
        }

        // ---------- 2. 加载并校验被合并记录 ----------
        List<Fx67llLotteryLog> logList = new ArrayList<>();
        Long currentUserId = SecurityUtils.getUserId();
        for (Long lotteryId : idSet) {
            Fx67llLotteryLog log = fx67llLotteryLogMapper.selectFx67llLotteryLogByLotteryId(lotteryId);
            if (log == null) {
                throw new ServiceException("部分记录不存在或已被删除，请刷新后重试！");
            }
            // App 端合并要求所有记录均为当前登录用户本人创建，防止越权合并他人数据
            if (log.getUserId() == null || !log.getUserId().equals(currentUserId)) {
                throw new ServiceException("存在非本人创建的记录，禁止合并！");
            }
            logList.add(log);
        }

        // 同期号校验：所有记录的 dateCode 必须完全一致
        String baseDateCode = logList.get(0).getDateCode();
        // 同类型校验：所有记录的 numberType 必须完全一致
        Integer baseNumberType = logList.get(0).getNumberType();
        if (baseNumberType == null) {
            // 类型字段为空属于异常数据，显式抛业务异常避免后续 equals 触发 NPE
            throw new ServiceException("待合并记录的彩票类型不能为空！");
        }
        for (Fx67llLotteryLog log : logList) {
            boolean sameDateCode = baseDateCode == null ? log.getDateCode() == null : baseDateCode.equals(log.getDateCode());
            if (!sameDateCode || !baseNumberType.equals(log.getNumberType())) {
                throw new ServiceException("仅支持合并同期号、同类型的记录！");
            }
        }

        // 按创建时间升序排序，取最早创建的那条作为基准记录（保留其期号、类型、星期等基础信息及最早创建时间）
        logList.sort(Comparator.comparing(Fx67llLotteryLog::getCreateTime,
                Comparator.nullsFirst(Comparator.naturalOrder())));
        Fx67llLotteryLog baseLog = logList.get(0);

        // ---------- 3. 合并号码字段 ----------
        // 固定追号去重：跨记录按 "/" 拆分后去重，重复的追号不重复添加
        String mergedChaseNumber = mergeNumberField(logList, true);
        // 每日号码去重：跨记录按 "/" 拆分后去重，重复的号码只保留一注
        String mergedRecordNumber = mergeNumberField(logList, false);

        // 校验合并后总注数不超过上限（固定追号注数 + 每日号码注数）
        int chaseNoteCount = countNotes(mergedChaseNumber);
        int recordNoteCount = countNotes(mergedRecordNumber);
        if (chaseNoteCount + recordNoteCount > MERGE_MAX_NOTE_COUNT) {
            throw new ServiceException("合并后号码注数超过" + MERGE_MAX_NOTE_COUNT + "注上限，请减少合并数量！");
        }

        // 开奖号码：取第一个非空值，若出现多个不同值则视为异常拒绝合并
        String mergedWinningNumber = pickWinningNumber(logList);

        // 是否中奖：任一为 Y 则为 Y，否则为 N
        boolean anyWin = false;
        long totalWinningPrice = 0L;
        for (Fx67llLotteryLog log : logList) {
            if ("Y".equals(log.getIsWin())) {
                anyWin = true;
                // 中奖金额：对所有中奖记录金额求和（字段为字符串，需解析为整数）
                totalWinningPrice += parsePrice(log.getWinningPrice());
            }
        }
        String mergedIsWin = anyWin ? "Y" : "N";
        String mergedWinningPrice = anyWin ? String.valueOf(totalWinningPrice) : null;

        // ---------- 4. 构建并写入新记录 ----------
        Fx67llLotteryLog mergedLog = new Fx67llLotteryLog();
        // 基础信息沿用基准记录（同期号同类型，这些字段各记录本应一致）
        mergedLog.setDateCode(baseLog.getDateCode());
        mergedLog.setNumberType(baseLog.getNumberType());
        mergedLog.setWeekType(baseLog.getWeekType());
        mergedLog.setHasMorePurchases(baseLog.getHasMorePurchases());
        // 合并后的号码与中奖信息
        mergedLog.setRecordNumber(mergedRecordNumber);
        mergedLog.setChaseNumber(mergedChaseNumber);
        mergedLog.setWinningNumber(mergedWinningNumber);
        mergedLog.setIsWin(mergedIsWin);
        mergedLog.setWinningPrice(mergedWinningPrice);
        // 归属用户为当前操作人（被合并记录均属本人，取当前用户即可）
        mergedLog.setUserId(currentUserId);
        // 创建时间取所有记录中最早的一条，创建人取该条原值（保留原始创建痕迹）
        mergedLog.setCreateBy(baseLog.getCreateBy());
        mergedLog.setCreateTime(baseLog.getCreateTime());
        // 更新时间取合并操作的当前时间，更新人取当前操作人
        mergedLog.setUpdateBy(SecurityUtils.getUsername());
        mergedLog.setUpdateTime(DateUtils.getNowDate());

        // 直接走 Mapper 新增，手动设好全部字段（不调用 ServiceImpl.insertFx67llLotteryLog，
        // 因为它会强制覆盖 createBy/createTime 为当前用户与当前时间，不符合"创建时间取最早"的要求）
        // useGeneratedKeys 会将数据库自增主键回填到 mergedLog.lotteryId，得到全新 ID
        fx67llLotteryLogMapper.insertFx67llLotteryLog(mergedLog);

        // ---------- 5. 删除全部被合并的旧记录 ----------
        Long[] oldLotteryIds = idSet.toArray(new Long[0]);
        fx67llLotteryLogMapper.deleteFx67llLotteryLogByLotteryIds(oldLotteryIds);

        // 返回合并后新记录的主键
        return mergedLog.getLotteryId();
    }

    /**
     * 合并号码字段（固定追号或每日号码）
     * <p>
     * 将多条记录的号码字段按 "/" 拆分后跨记录去重，再用 "/" 拼接返回。
     * 使用 LinkedHashSet 保留首次出现的顺序，使合并结果稳定可读。
     *
     * @param logList 被合并记录集合
     * @param isChase true=合并固定追号(chaseNumber)，false=合并每日号码(recordNumber)
     * @return 去重后用 "/" 拼接的号码字符串，无内容时返回 null
     */
    private String mergeNumberField(List<Fx67llLotteryLog> logList, boolean isChase) {
        Set<String> noteSet = new LinkedHashSet<>();
        for (Fx67llLotteryLog log : logList) {
            String raw = isChase ? log.getChaseNumber() : log.getRecordNumber();
            if (raw == null || raw.trim().isEmpty()) {
                continue;
            }
            // 多注号码以 "/" 分隔，按注拆分后逐注加入去重集合
            String[] notes = raw.split("/");
            for (String note : notes) {
                String trimmed = note.trim();
                if (!trimmed.isEmpty()) {
                    noteSet.add(trimmed);
                }
            }
        }
        if (noteSet.isEmpty()) {
            return null;
        }
        String merged = String.join("/", noteSet);
        // 校验合并后字段长度不超过数据库限制
        if (merged.length() > NUMBER_FIELD_MAX_LENGTH) {
            throw new ServiceException("合并后号码长度超过数据库存储上限，请减少合并数量！");
        }
        return merged;
    }

    /**
     * 统计号码字段的注数（按 "/" 分隔）
     *
     * @param numberStr 号码字符串
     * @return 注数，空字符串返回 0
     */
    private int countNotes(String numberStr) {
        if (numberStr == null || numberStr.trim().isEmpty()) {
            return 0;
        }
        return numberStr.split("/").length;
    }

    /**
     * 从多条记录中选取开奖号码
     * <p>
     * 同期号的开奖号码应当一致，取第一个非空值即可。若出现多个不同的非空开奖号码，
     * 视为异常数据拒绝合并，避免合并出脏数据。
     *
     * @param logList 被合并记录集合
     * @return 选取的开奖号码，全为空时返回 null
     */
    private String pickWinningNumber(List<Fx67llLotteryLog> logList) {
        String winner = null;
        for (Fx67llLotteryLog log : logList) {
            String wn = log.getWinningNumber();
            if (wn == null || wn.trim().isEmpty()) {
                continue;
            }
            String trimmed = wn.trim();
            if (winner == null) {
                winner = trimmed;
            } else if (!winner.equals(trimmed)) {
                throw new ServiceException("待合并记录的开奖号码不一致，属于异常数据，禁止合并！");
            }
        }
        return winner;
    }

    /**
     * 将中奖金额字符串解析为 long，空值或非数字按 0 处理
     *
     * @param price 中奖金额字符串
     * @return 解析后的金额
     */
    private long parsePrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(price.trim());
        } catch (NumberFormatException e) {
            // 金额格式异常按 0 计，避免因单条脏数据导致整个合并失败
            return 0L;
        }
    }
}
