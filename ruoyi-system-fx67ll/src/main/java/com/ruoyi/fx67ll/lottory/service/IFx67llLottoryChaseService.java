package com.ruoyi.fx67ll.lottory.service;

import java.util.List;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryChase;

/**
 * 固定追号配置Service接口
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public interface IFx67llLottoryChaseService 
{
    /**
     * 查询固定追号配置
     * 
     * @param chaseId 固定追号配置主键
     * @return 固定追号配置
     */
    public Fx67llLottoryChase selectFx67llLottoryChaseByChaseId(Long chaseId);

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 固定追号配置集合
     */
    public List<Fx67llLottoryChase> selectFx67llLottoryChaseList(Fx67llLottoryChase fx67llLottoryChase);

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 结果
     */
    public int insertFx67llLottoryChase(Fx67llLottoryChase fx67llLottoryChase);

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 结果
     */
    public int updateFx67llLottoryChase(Fx67llLottoryChase fx67llLottoryChase);

    /**
     * 批量删除固定追号配置
     * 
     * @param chaseIds 需要删除的固定追号配置主键集合
     * @return 结果
     */
    public int deleteFx67llLottoryChaseByChaseIds(Long[] chaseIds);

    /**
     * 删除固定追号配置信息
     * 
     * @param chaseId 固定追号配置主键
     * @return 结果
     */
    public int deleteFx67llLottoryChaseByChaseId(Long chaseId);
}
