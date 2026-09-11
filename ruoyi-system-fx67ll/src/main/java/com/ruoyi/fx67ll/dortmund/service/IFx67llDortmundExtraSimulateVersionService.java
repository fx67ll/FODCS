package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;

/**
 * 外快盈亏数值模拟版本记录Service接口
 *
 * @author fx67ll
 * @date 2026-09-11
 */
public interface IFx67llDortmundExtraSimulateVersionService {
    /**
     * 查询外快盈亏数值模拟版本记录列表
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionList(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion);

    /**
     * 查询外快盈亏数值模拟版本记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionListByUserId(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion);

    /**
     * 新增外快盈亏数值模拟版本记录（版本序号按用户自动递增）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    public int insertFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion);

    /**
     * 修改外快盈亏数值模拟版本记录（用于版本启用停用）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    public int updateFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion);
}
