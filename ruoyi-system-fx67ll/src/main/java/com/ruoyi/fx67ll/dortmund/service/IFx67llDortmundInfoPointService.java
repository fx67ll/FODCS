package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointExcel;

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
     * 批量删除统一信息点
     * <p>
     * 同一事务删除信息点及其球队关联，逐条执行数据范围校验
     *
     * @param infoPointIds 需要删除的统一信息点主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundInfoPointByInfoPointIds(Long[] infoPointIds);

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

    /**
     * 查询信息点导出数据。
     * <p>
     * 在标准列表结果上为 T 类型补齐当前有效球队关系，保证导出文件可直接携带球队编码。
     *
     * @param query 信息点查询条件
     * @return 带球队关系的信息点集合
     */
    public List<Fx67llDortmundInfoPoint> selectFx67llDortmundInfoPointExportList(Fx67llDortmundInfoPoint query);

    /**
     * 批量导入前端预览并确认的标准 JSON 信息点。
     * <p>
     * T 类型按赛季名称和球队编码解析关系，M 类型按比赛编码定位比赛；
     * 所有条目先完成校验再统一写入，任一条错误都会给出带条目序号的原因并回滚整批。
     *
     * @param rows 标准 JSON 信息点条目
     * @return 导入成功条数
     */
    public int importFx67llDortmundInfoPointJson(List<Fx67llDortmundInfoPointExcel> rows);
}
