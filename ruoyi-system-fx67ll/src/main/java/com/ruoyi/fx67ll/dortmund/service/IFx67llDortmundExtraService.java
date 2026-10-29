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
     * 通过 UserId 查询每日号码记录列表
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 外快盈亏记录集合
     */
    public List<Fx67llDortmundExtra> selectFx67llDortmundExtraListByUserId(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 新增外快盈亏记录
     * <p>
     * 请求可带可选 planIds、newPlans，不传时行为与历史完全相同；有值时同一事务内写外快盈亏、
     * 新建方案计划（及其比赛关系）、写关系行，任一步失败整体回滚
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    public int insertFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 修改外快盈亏记录
     * <p>
     * planIds 和 newPlans 都不传表示关联保持不变；传了任一个（包括空数组）按提交集合替换，
     * 只作用于当前用户可见的关联，当前用户看不到的关联始终保留
     *
     * @param fx67llDortmundExtra 外快盈亏记录
     * @return 结果
     */
    public int updateFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra);

    /**
     * 批量删除外快盈亏记录
     * <p>
     * 同一事务里先删这些记录的方案计划关系行再删记录，方案计划和比赛不动
     *
     * @param extraIds 需要删除的外快盈亏记录主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundExtraByExtraIds(Long[] extraIds);

    /**
     * 删除外快盈亏记录信息
     * <p>
     * 同一事务里先删该记录的方案计划关系行再删记录，方案计划和比赛不动
     *
     * @param extraId 外快盈亏记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundExtraByExtraId(Long extraId);

    /**
     * 合并外快盈亏历史记录
     * <p>
     * 新记录的关联取所选记录关联的并集，按方案计划去重，旧记录的关系行随旧记录删除
     *
     * @param extraIds 需要合并的外快盈亏记录主键集合
     * @param extraRemark 合并后新记录的备注（可不填写）
     * @return 合并后新生成记录的主键
     */
    public Long mergeFx67llDortmundExtras(Long[] extraIds, String extraRemark);

    /**
     * 提供给 APP 合并外快盈亏历史记录
     * <p>
     * 与 Web 端合并逻辑一致，区别在于额外校验所有待合并记录均为当前登录用户本人创建，防止越权合并他人数据
     *
     * @param extraIds 需要合并的外快盈亏记录主键集合
     * @param extraRemark 合并后新记录的备注（可不填写）
     * @return 合并后新生成记录的主键
     */
    public Long mergeFx67llDortmundExtrasForApp(Long[] extraIds, String extraRemark);
}
