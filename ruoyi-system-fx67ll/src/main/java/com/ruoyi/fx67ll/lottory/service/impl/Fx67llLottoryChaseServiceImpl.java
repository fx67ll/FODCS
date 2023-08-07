package com.ruoyi.fx67ll.lottory.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottory.mapper.Fx67llLottoryChaseMapper;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottoryChase;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottoryChaseService;

/**
 * 固定追号配置Service业务层处理
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLottoryChaseServiceImpl implements IFx67llLottoryChaseService 
{
    @Autowired
    private Fx67llLottoryChaseMapper fx67llLottoryChaseMapper;

    /**
     * 查询固定追号配置
     * 
     * @param chaseId 固定追号配置主键
     * @return 固定追号配置
     */
    @Override
    public Fx67llLottoryChase selectFx67llLottoryChaseByChaseId(Long chaseId)
    {
        return fx67llLottoryChaseMapper.selectFx67llLottoryChaseByChaseId(chaseId);
    }

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 固定追号配置
     */
    @Override
    public List<Fx67llLottoryChase> selectFx67llLottoryChaseList(Fx67llLottoryChase fx67llLottoryChase)
    {
        return fx67llLottoryChaseMapper.selectFx67llLottoryChaseList(fx67llLottoryChase);
    }

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 结果
     */
    @Override
    public int insertFx67llLottoryChase(Fx67llLottoryChase fx67llLottoryChase)
    {
        fx67llLottoryChase.setCreateTime(DateUtils.getNowDate());
        return fx67llLottoryChaseMapper.insertFx67llLottoryChase(fx67llLottoryChase);
    }

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLottoryChase 固定追号配置
     * @return 结果
     */
    @Override
    public int updateFx67llLottoryChase(Fx67llLottoryChase fx67llLottoryChase)
    {
        fx67llLottoryChase.setUpdateTime(DateUtils.getNowDate());
        return fx67llLottoryChaseMapper.updateFx67llLottoryChase(fx67llLottoryChase);
    }

    /**
     * 批量删除固定追号配置
     * 
     * @param chaseIds 需要删除的固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottoryChaseByChaseIds(Long[] chaseIds)
    {
        return fx67llLottoryChaseMapper.deleteFx67llLottoryChaseByChaseIds(chaseIds);
    }

    /**
     * 删除固定追号配置信息
     * 
     * @param chaseId 固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottoryChaseByChaseId(Long chaseId)
    {
        return fx67llLottoryChaseMapper.deleteFx67llLottoryChaseByChaseId(chaseId);
    }
}
