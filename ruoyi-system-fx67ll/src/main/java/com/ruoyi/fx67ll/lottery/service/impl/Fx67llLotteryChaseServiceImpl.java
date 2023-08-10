package com.ruoyi.fx67ll.lottery.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.lottery.mapper.Fx67llLotteryChaseMapper;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryChase;
import com.ruoyi.fx67ll.lottery.service.IFx67llLotteryChaseService;

/**
 * 固定追号配置Service业务层处理
 *
 * @author fx67ll
 * @date 2023-08-07
 */
@Service
public class Fx67llLotteryChaseServiceImpl implements IFx67llLotteryChaseService {
    @Autowired
    private Fx67llLotteryChaseMapper fx67llLotteryChaseMapper;

    /**
     * 查询固定追号配置
     *
     * @param chaseId 固定追号配置主键
     * @return 固定追号配置
     */
    @Override
    public Fx67llLotteryChase selectFx67llLotteryChaseByChaseId(Long chaseId) {
        return fx67llLotteryChaseMapper.selectFx67llLotteryChaseByChaseId(chaseId);
    }

    /**
     * 查询固定追号配置列表
     *
     * @param fx67llLotteryChase 固定追号配置
     * @return 固定追号配置
     */
    @Override
    public List<Fx67llLotteryChase> selectFx67llLotteryChaseList(Fx67llLotteryChase fx67llLotteryChase) {
        return fx67llLotteryChaseMapper.selectFx67llLotteryChaseList(fx67llLotteryChase);
    }

    /**
     * 通过 UserId 查询固定追号配置列表
     *
     * @param userId 用户主键
     * @return 固定追号配置
     */
    @Override
    public List<Fx67llLotteryChase> selectFx67llLotteryChaseListByUserId(Long userId) {
        return fx67llLotteryChaseMapper.selectFx67llLotteryChaseListByUserId(userId);
    }

    /**
     * 新增固定追号配置
     *
     * @param fx67llLotteryChase 固定追号配置
     * @return 结果
     */
    @Override
    public int insertFx67llLotteryChase(Fx67llLotteryChase fx67llLotteryChase) {
        fx67llLotteryChase.setUserId(SecurityUtils.getUserId());
        fx67llLotteryChase.setCreateBy(SecurityUtils.getUsername());
        fx67llLotteryChase.setCreateTime(DateUtils.getNowDate());
        return fx67llLotteryChaseMapper.insertFx67llLotteryChase(fx67llLotteryChase);
    }

    /**
     * 修改固定追号配置
     *
     * @param fx67llLotteryChase 固定追号配置
     * @return 结果
     */
    @Override
    public int updateFx67llLotteryChase(Fx67llLotteryChase fx67llLotteryChase) {
        fx67llLotteryChase.setUserId(SecurityUtils.getUserId());
        fx67llLotteryChase.setUpdateBy(SecurityUtils.getUsername());
        fx67llLotteryChase.setUpdateTime(DateUtils.getNowDate());
        return fx67llLotteryChaseMapper.updateFx67llLotteryChase(fx67llLotteryChase);
    }

    /**
     * 批量删除固定追号配置
     *
     * @param chaseIds 需要删除的固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotteryChaseByChaseIds(Long[] chaseIds) {
        return fx67llLotteryChaseMapper.deleteFx67llLotteryChaseByChaseIds(chaseIds);
    }

    /**
     * 删除固定追号配置信息
     *
     * @param chaseId 固定追号配置主键
     * @return 结果
     */
    @Override
    public int deleteFx67llLotteryChaseByChaseId(Long chaseId) {
        return fx67llLotteryChaseMapper.deleteFx67llLotteryChaseByChaseId(chaseId);
    }
}
