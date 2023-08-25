package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraService;

/**
 * 外快盈亏记录Service业务层处理
 *
 * @author fx67ll
 * @date 2023-08-17
 */
@Service
public class Fx67llDortmundExtraServiceImpl implements IFx67llDortmundExtraService {
    @Autowired
    private Fx67llDortmundExtraMapper fx67llDortmundExtraMapper;

    /**
     * 查询外快盈亏记录
     *
     * @param extraId 外快盈亏记录主键
     * @return 外快盈亏记录
     */
    @Override
    public Fx67llDortmundExtra selectFx67llDortmundExtraByExtraId(Long extraId) {
        return fx67llDortmundExtraMapper.selectFx67llDortmundExtraByExtraId(extraId);
    }

    /**
     * 查询外快盈亏记录列表
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 外快盈亏记录
     */
    @Override
    public List<Fx67llDortmundExtra> selectFx67llDortmundExtraList(Fx67llDortmundExtra fx67llDortmundExtra) {
        return fx67llDortmundExtraMapper.selectFx67llDortmundExtraList(fx67llDortmundExtra);
    }

    /**
     * 查询外快盈亏记录列表
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 外快盈亏记录
     */
    @Override
    public List<Fx67llDortmundExtra> selectFx67llDortmundExtraListByUserId(Fx67llDortmundExtra fx67llDortmundExtra) {
        fx67llDortmundExtra.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundExtraMapper.selectFx67llDortmundExtraList(fx67llDortmundExtra);
    }

    /**
     * 新增外快盈亏记录
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra) {
        fx67llDortmundExtra.setUserId(SecurityUtils.getUserId());
        fx67llDortmundExtra.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtra.setCreateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraMapper.insertFx67llDortmundExtra(fx67llDortmundExtra);
    }

    /**
     * 修改外快盈亏记录
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra) {
        fx67llDortmundExtra.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundExtra.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraMapper.updateFx67llDortmundExtra(fx67llDortmundExtra);
    }

    /**
     * 批量删除外快盈亏记录
     *
     * @param extraIds 需要删除的外快盈亏记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundExtraByExtraIds(Long[] extraIds) {
        return fx67llDortmundExtraMapper.deleteFx67llDortmundExtraByExtraIds(extraIds);
    }

    /**
     * 删除外快盈亏记录信息
     *
     * @param extraId 外快盈亏记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundExtraByExtraId(Long extraId) {
        return fx67llDortmundExtraMapper.deleteFx67llDortmundExtraByExtraId(extraId);
    }
}
