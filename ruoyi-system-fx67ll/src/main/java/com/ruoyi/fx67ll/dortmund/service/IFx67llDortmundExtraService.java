package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;

/**
 * 外快盈亏记录Service接口
 *
 * @author fx67ll
 * @date 2023-08-17
 */
public interface IFx67llDortmundExtraService {
    /**
     * 查询外快盈亏记录
     *
     * @param extraId 外快盈亏记录主键
     * @return 外快盈亏记录
     */
    public Fx67llDortmundExtra selectFx67llDortmundExtraByExtraId(Long extraId);

    /**
     * 查询外快盈亏记录列表
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 外快盈亏记录集合
     */
    public List<Fx67llDortmundExtra> selectFx67llDortmundExtraList(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 新增外快盈亏记录
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    public int insertFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 修改外快盈亏记录
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    public int updateFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 批量删除外快盈亏记录
     *
     * @param extraIds 需要删除的外快盈亏记录主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundExtraByExtraIds(Long[] extraIds);

    /**
     * 删除外快盈亏记录信息
     *
     * @param extraId 外快盈亏记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundExtraByExtraId(Long extraId);
}
