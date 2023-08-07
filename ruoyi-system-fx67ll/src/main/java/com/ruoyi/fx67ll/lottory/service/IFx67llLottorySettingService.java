package com.ruoyi.fx67ll.lottory.service;

import java.util.List;
import com.ruoyi.fx67ll.lottory.domain.Fx67llLottorySetting;

/**
 * 固定追号配置Service接口
 * 
 * @author fx67ll
 * @date 2023-08-07
 */
public interface IFx67llLottorySettingService 
{
    /**
     * 查询固定追号配置
     * 
     * @param userId 固定追号配置主键
     * @return 固定追号配置
     */
    public Fx67llLottorySetting selectFx67llLottorySettingByUserId(Long userId);

    /**
     * 查询固定追号配置列表
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 固定追号配置集合
     */
    public List<Fx67llLottorySetting> selectFx67llLottorySettingList(Fx67llLottorySetting fx67llLottorySetting);

    /**
     * 新增固定追号配置
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 结果
     */
    public int insertFx67llLottorySetting(Fx67llLottorySetting fx67llLottorySetting);

    /**
     * 修改固定追号配置
     * 
     * @param fx67llLottorySetting 固定追号配置
     * @return 结果
     */
    public int updateFx67llLottorySetting(Fx67llLottorySetting fx67llLottorySetting);

    /**
     * 批量删除固定追号配置
     * 
     * @param userIds 需要删除的固定追号配置主键集合
     * @return 结果
     */
    public int deleteFx67llLottorySettingByUserIds(Long[] userIds);

    /**
     * 删除固定追号配置信息
     * 
     * @param userId 固定追号配置主键
     * @return 结果
     */
    public int deleteFx67llLottorySettingByUserId(Long userId);
}
