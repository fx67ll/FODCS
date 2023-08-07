package com.ruoyi.fx67ll.lottory.service;

import java.util.List;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryLog;

/**
 * 每日号码记录Service接口
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public interface IFx67llLottoryLogService 
{
    /**
     * 查询每日号码记录
     * 
     * @param lottoryId 每日号码记录主键
     * @return 每日号码记录
     */
    public Fx67llLottoryLog selectFx67llLottoryLogByLottoryId(Long lottoryId);

    /**
     * 查询每日号码记录列表
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 每日号码记录集合
     */
    public List<Fx67llLottoryLog> selectFx67llLottoryLogList(Fx67llLottoryLog fx67llLottoryLog);

    /**
     * 新增每日号码记录
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 结果
     */
    public int insertFx67llLottoryLog(Fx67llLottoryLog fx67llLottoryLog);

    /**
     * 修改每日号码记录
     * 
     * @param fx67llLottoryLog 每日号码记录
     * @return 结果
     */
    public int updateFx67llLottoryLog(Fx67llLottoryLog fx67llLottoryLog);

    /**
     * 批量删除每日号码记录
     * 
     * @param lottoryIds 需要删除的每日号码记录主键集合
     * @return 结果
     */
    public int deleteFx67llLottoryLogByLottoryIds(Long[] lottoryIds);

    /**
     * 删除每日号码记录信息
     * 
     * @param lottoryId 每日号码记录主键
     * @return 结果
     */
    public int deleteFx67llLottoryLogByLottoryId(Long lottoryId);
}
