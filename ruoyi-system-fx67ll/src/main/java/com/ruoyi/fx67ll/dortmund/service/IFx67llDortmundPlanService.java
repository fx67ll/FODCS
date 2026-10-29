package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlan;

/**
 * 方案计划Service接口
 *
 * @author fx67ll
 * @date 2026-10-29
 */
public interface IFx67llDortmundPlanService {
    /**
     * 查询方案计划
     *
     * @param planId 方案计划主键
     * @return 方案计划
     */
    public Fx67llDortmundPlan selectFx67llDortmundPlanByPlanId(Long planId);

    /**
     * 查询方案计划列表
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 方案计划集合
     */
    public List<Fx67llDortmundPlan> selectFx67llDortmundPlanList(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 新增方案计划
     * <p>
     * 文字、比赛关系、本次新建比赛、允许保存的对象键同一事务写入
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int insertFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 修改方案计划
     * <p>
     * 保存后至少仍有一场比赛，已关联、后来被关闭的比赛保持原关联不动
     *
     * @param fx67llDortmundPlan 方案计划
     * @return 结果
     */
    public int updateFx67llDortmundPlan(Fx67llDortmundPlan fx67llDortmundPlan);

    /**
     * 批量删除方案计划
     * <p>
     * 仍被未删除的外快盈亏或数值轮次关系引用时拒绝；删除时同事务删除比赛关系行并清理只属于它的图片文件
     *
     * @param planIds 需要删除的方案计划主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundPlanByPlanIds(Long[] planIds);

    /**
     * 删除方案计划信息
     *
     * @param planId 方案计划主键
     * @return 结果
     */
    public int deleteFx67llDortmundPlanByPlanId(Long planId);

    /**
     * 上传方案计划图片
     * <p>
     * 复用 RuoYi 现有上传实现（随机文件名、现有存储路径、大小/扩展名/MIME/文件头校验），返回相对对象键；
     * 上传成功后写入 24 小时签发记录，保存方案计划时校验归属并一次性消费
     *
     * @param file 上传的图片文件
     * @return 相对对象键
     */
    public String uploadPlanImage(MultipartFile file) throws Exception;
}
