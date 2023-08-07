package com.ruoyi.fx67ll.lottery.mapper;

import java.util.List;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryChase;

/**
 * 固定追号配置Mapper接口
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public interface Fx67llLotteryChaseMapper 
{
    /**
     * 查询固定追号配置
     * 
     * @param chaseId 固定追号配置主键
     * @return 固定追号配置
     */
    public Fx67llLotteryChase selectFx67llLotteryChaseByChaseId(Long chaseId);

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLotteryChase 固定追号配置
     * @return 固定追号配置集合
     */
    public List<Fx67llLotteryChase> selectFx67llLotteryChaseList(Fx67llLotteryChase fx67llLotteryChase);

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLotteryChase 固定追号配置
     * @return 结果
     */
    public int insertFx67llLotteryChase(Fx67llLotteryChase fx67llLotteryChase);

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLotteryChase 固定追号配置
     * @return 结果
     */
    public int updateFx67llLotteryChase(Fx67llLotteryChase fx67llLotteryChase);

    /**
     * 删除固定追号配置
     * 
     * @param chaseId 固定追号配置主键
     * @return 结果
     */
    public int deleteFx67llLotteryChaseByChaseId(Long chaseId);

    /**
     * 批量删除固定追号配置
     * 
     * @param chaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llLotteryChaseByChaseIds(Long[] chaseIds);
}
