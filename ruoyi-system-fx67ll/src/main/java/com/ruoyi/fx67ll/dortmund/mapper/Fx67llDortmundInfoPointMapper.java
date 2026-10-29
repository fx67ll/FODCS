package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;

/**
 * 统一信息点Mapper接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface Fx67llDortmundInfoPointMapper {
    /**
     * 查询统一信息点
     *
     * @param infoPointId 信息点唯一标识
     * @return 统一信息点
     */
    public Fx67llDortmundInfoPoint selectFx67llDortmundInfoPointByInfoPointId(Long infoPointId);

    /**
     * 查询统一信息点列表
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 统一信息点集合
     */
    public List<Fx67llDortmundInfoPoint> selectFx67llDortmundInfoPointList(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 新增统一信息点
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    public int insertFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 修改统一信息点
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    public int updateFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 条件修改信息点状态（仅当状态发生变化时生效）
     *
     * @param fx67llDortmundInfoPoint 信息点唯一标识与目标状态
     * @return 结果
     */
    public int updateFx67llDortmundInfoPointStatus(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);
}
