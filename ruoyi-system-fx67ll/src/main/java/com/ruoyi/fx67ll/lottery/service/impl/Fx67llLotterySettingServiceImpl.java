package com.ruoyi.fx67ll.lottery.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottery.mapper.Fx67llLotterySettingMapper;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotterySetting;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotterySettingService;

/**
 * 固定追号配置Service业务层处理
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLotterySettingServiceImpl implements IFx67llLotterySettingService 
{
    @Autowired
    private Fx67llLotterySettingMapper fx67LlLotterySettingMapper;

    /**
     * 查询固定追号配置
     * 
     * @param settingId 固定追号配置主键
     * @return 固定追号配置
     */
    @Override
    public Fx67llLotterySetting selectFx67llLotterySettingBySettingId(Long settingId)
    {
        return fx67LlLotterySettingMapper.selectFx67llLotterySettingBySettingId(settingId);
    }

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 固定追号配置
     */
    @Override
    public List<Fx67llLotterySetting> selectFx67llLotterySettingList(Fx67llLotterySetting fx67llLotterySetting)
    {
        return fx67LlLotterySettingMapper.selectFx67llLotterySettingList(fx67llLotterySetting);
    }

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 结果
     */
    @Override
    public int insertFx67llLotterySetting(Fx67llLotterySetting fx67llLotterySetting)
    {
        fx67llLotterySetting.setCreateTime(DateUtils.getNowDate());
        return fx67LlLotterySettingMapper.insertFx67llLotterySetting(fx67llLotterySetting);
    }

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 结果
     */
    @Override
    public int updateFx67llLotterySetting(Fx67llLotterySetting fx67llLotterySetting)
    {
        fx67llLotterySetting.setUpdateTime(DateUtils.getNowDate());
        return fx67LlLotterySettingMapper.updateFx67llLotterySetting(fx67llLotterySetting);
    }

    /**
     * 批量删除固定追号配置
     * 
     * @param settingIds 需要删除的固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotterySettingBySettingIds(Long[] settingIds)
    {
        return fx67LlLotterySettingMapper.deleteFx67llLotterySettingBySettingIds(settingIds);
    }

    /**
     * 删除固定追号配置信息
     * 
     * @param settingId 固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotterySettingBySettingId(Long settingId)
    {
        return fx67LlLotterySettingMapper.deleteFx67llLotterySettingBySettingId(settingId);
    }
}
