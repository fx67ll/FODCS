package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;

/**
 * 统一信息点Service接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface IFx67llDortmundInfoPointService {
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
     * <p>
     * 类型二选一：T 必须有赛季和至少一支球队、不得提交比赛；M 必须有比赛、不得提交赛季和球队
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    public int insertFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 修改统一信息点
     * <p>
     * 类型、赛季、比赛归属和状态不可通过本接口修改，球队关系按提交集合差异启停
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    public int updateFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 修改信息点状态
     * <p>
     * 条件更新，影响行数不是 1 时按已处理、状态变化或记录不存在返回
     *
     * @param fx67llDortmundInfoPoint 信息点唯一标识与目标状态
     * @return 结果
     */
    public int changeFx67llDortmundInfoPointStatus(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint);

    /**
     * 查询比赛的有效信息点
     * <p>
     * 返回该比赛当前会进入后续分析的信息点与被排除信息点，每行含有效标记与排除原因
     *
     * @param matchId 比赛ID
     * @return 信息点集合
     */
    public List<Fx67llDortmundInfoPoint> selectFx67llDortmundInfoPointValidListByMatchId(Long matchId);
}
