package com.ruoyi.fx67ll.lottory.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottory.mapper.Fx67llLottorySettingMapper;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottorySetting;
import com.ruoyi.fx67ll.lottory.service.IFx67llLottorySettingService;

/**
 * 固定追号配置Service业务层处理
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLottorySettingServiceImpl implements IFx67llLottorySettingService 
{
    @Autowired
    private Fx67llLottorySettingMapper fx67llLottorySettingMapper;

    /**
     * 查询固定追号配置
     * 
     * @param userId 固定追号配置主键
     * @return 固定追号配置
     */
    @Override
    public Fx67llLottorySetting selectFx67llLottorySettingByUserId(Long userId)
    {
        return fx67llLottorySettingMapper.selectFx67llLottorySettingByUserId(userId);
    }

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 固定追号配置
     */
    @Override
    public List<Fx67llLottorySetting> selectFx67llLottorySettingList(Fx67llLottorySetting fx67llLottorySetting)
    {
        return fx67llLottorySettingMapper.selectFx67llLottorySettingList(fx67llLottorySetting);
    }

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 结果
     */
    @Override
    public int insertFx67llLottorySetting(Fx67llLottorySetting fx67llLottorySetting)
    {
        fx67llLottorySetting.setCreateTime(DateUtils.getNowDate());
        return fx67llLottorySettingMapper.insertFx67llLottorySetting(fx67llLottorySetting);
    }

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 结果
     */
    @Override
    public int updateFx67llLottorySetting(Fx67llLottorySetting fx67llLottorySetting)
    {
        fx67llLottorySetting.setUpdateTime(DateUtils.getNowDate());
        return fx67llLottorySettingMapper.updateFx67llLottorySetting(fx67llLottorySetting);
    }

    /**
     * 批量删除固定追号配置
     * 
     * @param userIds 需要删除的固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottorySettingByUserIds(Long[] userIds)
    {
        return fx67llLottorySettingMapper.deleteFx67llLottorySettingByUserIds(userIds);
    }

    /**
     * 删除固定追号配置信息
     * 
     * @param userId 固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLottorySettingByUserId(Long userId)
    {
        return fx67llLottorySettingMapper.deleteFx67llLottorySettingByUserId(userId);
    }
}
