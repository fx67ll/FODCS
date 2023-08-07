package com.ruoyi.fx67ll.lottery.service;

import java.util.List;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotterySetting;

/**
 * 固定追号配置Service接口
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public interface IFx67llLotterySettingService 
{
    /**
     * 查询固定追号配置
     * 
     * @param settingId 固定追号配置主键
     * @return 固定追号配置
     */
    public Fx67llLotterySetting selectFx67llLotterySettingBySettingId(Long settingId);

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 固定追号配置集合
     */
    public List<Fx67llLotterySetting> selectFx67llLotterySettingList(Fx67llLotterySetting fx67llLotterySetting);

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 结果
     */
    public int insertFx67llLotterySetting(Fx67llLotterySetting fx67llLotterySetting);

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLotterySetting 固定追号配置
     * @return 结果
     */
    public int updateFx67llLotterySetting(Fx67llLotterySetting fx67llLotterySetting);

    /**
     * 批量删除固定追号配置
     * 
     * @param settingIds 需要删除的固定追号配置主键集合
     * @return 结果
     */
    public int deleteFx67llLotterySettingBySettingIds(Long[] settingIds);

    /**
     * 删除固定追号配置信息
     * 
     * @param settingId 固定追号配置主键
     * @return 结果
     */
    public int deleteFx67llLotterySettingBySettingId(Long settingId);
}
