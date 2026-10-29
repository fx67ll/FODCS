package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;

/**
 * 外快盈亏数值模拟轮次记录Service接口
 *
 * @author fx67ll
 * @date 2026-09-10
 */
public interface IFx67llDortmundExtraSimulateService {
    /**
     * 查询外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulateBySimId(Long simId);

    /**
     * 查询外快盈亏数值模拟轮次记录列表
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录集合
     */
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateList(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate);

    /**
     * 查询外快盈亏数值模拟轮次记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录集合
     */
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateListByUserId(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate);

    /**
     * 查询数值轮次的关联方案计划摘要
     * <p>
     * 附带只读关联方案计划信息：关联总数 planCount、不可见关联数 hiddenPlanCount；
     * plans 摘要只对拥有方案计划查询权限的用户返回，否则只给条数，不可见关联不透露内容
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulatePlansBySimId(Long simId);

    /**
     * 新增外快盈亏数值模拟轮次记录（轮次为负时强制作废所属版本）
     * <p>
     * 请求可带可选 planIds、newPlans，不传时不写方案计划关系，行为与历史完全相同；
     * 有值时同一事务内写轮次、新建方案计划（及其比赛关系）、按新轮次主键写关系行，
     * 同一轮次内同一方案计划只保留一条关系；
     * 轮次为负强制作废版本时，作废会清理该版本全部轮次的关系行（含本轮刚写入的）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 结果
     */
    public int insertFx67llDortmundExtraSimulate(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate);

    /**
     * 删除外快盈亏数值模拟轮次记录
     * <p>
     * 同一事务里先删该轮次的方案计划关系行再删轮次，方案计划和比赛不动
     *
     * @param simId 数值模拟轮次记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundExtraSimulateBySimId(Long simId);
}
