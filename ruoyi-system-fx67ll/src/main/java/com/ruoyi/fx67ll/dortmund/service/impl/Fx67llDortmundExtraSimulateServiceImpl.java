package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraSimulateMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulate;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundPlan;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundRoundPlanRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundRoundPlanRelMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateVersionService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 外快盈亏数值模拟轮次记录Service业务层处理
 *
 * @author fx67ll
 * @date 2026-09-10
 */
@Service
public class Fx67llDortmundExtraSimulateServiceImpl implements IFx67llDortmundExtraSimulateService {
    @Autowired
    private Fx67llDortmundExtraSimulateMapper fx67llDortmundExtraSimulateMapper;

    @Autowired
    private Fx67llDortmundRoundPlanRelMapper fx67llDortmundRoundPlanRelMapper;

    @Autowired
    private IFx67llDortmundExtraSimulateVersionService fx67llDortmundExtraSimulateVersionService;

    @Autowired
    private IFx67llDortmundPlanService fx67llDortmundPlanService;

    /**
     * 查询外快盈亏数值模拟轮次记录
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    @Override
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulateBySimId(Long simId) {
        return fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateBySimId(simId);
    }

    /**
     * 查询外快盈亏数值模拟轮次记录列表
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录
     */
    @Override
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateList(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        List<Fx67llDortmundExtraSimulate> list = fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateList(fx67llDortmundExtraSimulate);
        for (Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulateItem : list) {
            fillPlanCount(fx67llDortmundExtraSimulateItem);
        }
        return list;
    }

    /**
     * 查询外快盈亏数值模拟轮次记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 外快盈亏数值模拟轮次记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulate> selectFx67llDortmundExtraSimulateListByUserId(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        fx67llDortmundExtraSimulate.setUserId(SecurityUtils.getUserId());
        List<Fx67llDortmundExtraSimulate> list = fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateList(fx67llDortmundExtraSimulate);
        for (Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulateItem : list) {
            fillPlanCount(fx67llDortmundExtraSimulateItem);
        }
        return list;
    }

    /**
     * 查询数值轮次的关联方案计划摘要
     * <p>
     * 附带只读关联方案计划信息：关联总数 planCount、不可见关联数 hiddenPlanCount；
     * plans 摘要只对拥有方案计划查询权限的用户返回，否则只给条数，不可见关联不透露内容
     *
     * @param simId 数值模拟轮次记录主键
     * @return 外快盈亏数值模拟轮次记录
     */
    @Override
    public Fx67llDortmundExtraSimulate selectFx67llDortmundExtraSimulatePlansBySimId(Long simId) {
        Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate = fx67llDortmundExtraSimulateMapper.selectFx67llDortmundExtraSimulateBySimId(simId);
        if (fx67llDortmundExtraSimulate != null) {
            fillPlanCountAndSummary(fx67llDortmundExtraSimulate);
        }
        return fx67llDortmundExtraSimulate;
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundExtraSimulate(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        checkPlanFieldsInRequest(fx67llDortmundExtraSimulate);
        List<Long> planIds = resolveRequestPlanIds(fx67llDortmundExtraSimulate);
        fx67llDortmundExtraSimulate.setUserId(SecurityUtils.getUserId());
        fx67llDortmundExtraSimulate.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulate.setCreateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundExtraSimulateMapper.insertFx67llDortmundExtraSimulate(fx67llDortmundExtraSimulate);
        if (rows > 0) {
            insertRoundPlanRels(fx67llDortmundExtraSimulate.getSimId(), planIds);
            if (fx67llDortmundExtraSimulate.getRoundNo() != null && fx67llDortmundExtraSimulate.getRoundNo() < 0) {
                // 轮次为负，强制作废所属版本（软删除，作废后不在列表中展示），
                // 作废同时清理该版本全部轮次的方案计划关系（含本轮刚写入的）
                Fx67llDortmundExtraSimulateVersion invalidateVersion = new Fx67llDortmundExtraSimulateVersion();
                invalidateVersion.setVersionId(fx67llDortmundExtraSimulate.getVersionId());
                fx67llDortmundExtraSimulateVersionService.invalidateFx67llDortmundExtraSimulateVersion(invalidateVersion);
            }
        }
        return rows;
    }

    /**
     * 删除外快盈亏数值模拟轮次记录
     * <p>
     * 同一事务里先删该轮次的方案计划关系行再删轮次，方案计划和比赛不动
     *
     * @param simId 数值模拟轮次记录主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFx67llDortmundExtraSimulateBySimId(Long simId) {
        fx67llDortmundRoundPlanRelMapper.deleteFx67llDortmundRoundPlanRelByRoundId(simId);
        return fx67llDortmundExtraSimulateMapper.deleteFx67llDortmundExtraSimulateBySimId(simId);
    }

    /**
     * 校验请求中的方案计划关联字段：图片对象键一律拒绝，newPlans 元素只能引用已有比赛
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     */
    private void checkPlanFieldsInRequest(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        if (fx67llDortmundExtraSimulate.getPlanImageObjectKeys() != null) {
            throw new ServiceException("数值轮次请求不允许携带图片对象键！");
        }
        List<Fx67llDortmundPlan> newPlans = fx67llDortmundExtraSimulate.getNewPlans();
        if (newPlans != null) {
            for (Fx67llDortmundPlan newPlan : newPlans) {
                if (newPlan.getPlanImageObjectKeys() != null) {
                    throw new ServiceException("数值轮次请求不允许携带图片对象键！");
                }
                if (newPlan.getNewMatches() != null && !newPlan.getNewMatches().isEmpty()) {
                    throw new ServiceException("数值轮次内新建方案计划只能引用已有比赛，不能当场新建比赛！");
                }
                if (newPlan.getMatchIds() == null || newPlan.getMatchIds().length == 0) {
                    throw new ServiceException("数值轮次内新建方案计划至少要关联一场已有比赛！");
                }
            }
        }
    }

    /**
     * 解析请求中的方案计划集合：planIds 需要方案计划查询权限并逐条校验存在与可见，
     * newPlans 需要方案计划新增权限并逐条按方案计划规则新建
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     * @return 方案计划主键集合（去重）
     */
    private List<Long> resolveRequestPlanIds(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        List<Long> planIds = new ArrayList<>();
        Long[] requestPlanIds = fx67llDortmundExtraSimulate.getPlanIds();
        if (requestPlanIds != null) {
            if (!hasPermission("dortmund:plan:query")) {
                throw new ServiceException("没有方案计划查询权限，不能引用已有方案计划！");
            }
            for (Long planId : requestPlanIds) {
                if (!planIds.contains(planId)) {
                    planIds.add(planId);
                }
            }
            for (Long planId : planIds) {
                Fx67llDortmundPlan plan = fx67llDortmundPlanService.selectFx67llDortmundPlanByPlanId(planId);
                if (plan == null || !"0".equals(plan.getDelFlag()) || !checkPlanVisible(plan.getUserId())) {
                    // 不存在和无权限返回同一类受控错误，不区分原因
                    throw new ServiceException("所选方案计划不可用，请刷新后重试！");
                }
            }
        }
        List<Fx67llDortmundPlan> newPlans = fx67llDortmundExtraSimulate.getNewPlans();
        if (newPlans != null) {
            if (!hasPermission("dortmund:plan:add")) {
                throw new ServiceException("没有方案计划新增权限，不能当场新建方案计划！");
            }
            for (Fx67llDortmundPlan newPlan : newPlans) {
                fx67llDortmundPlanService.insertFx67llDortmundPlan(newPlan);
                if (!planIds.contains(newPlan.getPlanId())) {
                    planIds.add(newPlan.getPlanId());
                }
            }
        }
        return planIds;
    }

    /**
     * 批量插入数值轮次与方案计划关系行（同一轮次内同一方案计划只保留一条）
     *
     * @param simId   数值模拟轮次主键
     * @param planIds 方案计划主键集合（已去重）
     */
    private void insertRoundPlanRels(Long simId, List<Long> planIds) {
        if (planIds == null || planIds.isEmpty()) {
            return;
        }
        for (Long planId : planIds) {
            Fx67llDortmundRoundPlanRel rel = new Fx67llDortmundRoundPlanRel();
            rel.setRoundId(simId);
            rel.setPlanId(planId);
            rel.setCreateBy(SecurityUtils.getUsername());
            rel.setCreateTime(DateUtils.getNowDate());
            fx67llDortmundRoundPlanRelMapper.insertFx67llDortmundRoundPlanRel(rel);
        }
    }

    /**
     * 填充数值轮次的关联方案计划数量（列表只读派生值）
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     */
    private void fillPlanCount(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        Fx67llDortmundRoundPlanRel relQuery = new Fx67llDortmundRoundPlanRel();
        relQuery.setRoundId(fx67llDortmundExtraSimulate.getSimId());
        List<Fx67llDortmundRoundPlanRel> relList = fx67llDortmundRoundPlanRelMapper.selectFx67llDortmundRoundPlanRelList(relQuery);
        fx67llDortmundExtraSimulate.setPlanCount(relList == null ? 0 : relList.size());
    }

    /**
     * 填充轮次方案计划查询的关联摘要：可见的按拥有方案计划查询权限与否返回摘要或仅条数，
     * 不可见的只计入 hiddenPlanCount，不透露内容
     *
     * @param fx67llDortmundExtraSimulate 外快盈亏数值模拟轮次记录
     */
    private void fillPlanCountAndSummary(Fx67llDortmundExtraSimulate fx67llDortmundExtraSimulate) {
        fillPlanCount(fx67llDortmundExtraSimulate);
        Fx67llDortmundRoundPlanRel relQuery = new Fx67llDortmundRoundPlanRel();
        relQuery.setRoundId(fx67llDortmundExtraSimulate.getSimId());
        List<Fx67llDortmundRoundPlanRel> relList = fx67llDortmundRoundPlanRelMapper.selectFx67llDortmundRoundPlanRelList(relQuery);
        if (relList == null || relList.isEmpty()) {
            fx67llDortmundExtraSimulate.setHiddenPlanCount(0);
            fx67llDortmundExtraSimulate.setPlans(new ArrayList<>());
            return;
        }
        boolean planQueryPermission = hasPermission("dortmund:plan:query");
        List<Fx67llDortmundPlan> visiblePlans = new ArrayList<>();
        int hiddenCount = 0;
        for (Fx67llDortmundRoundPlanRel rel : relList) {
            Fx67llDortmundPlan plan = fx67llDortmundPlanService.selectFx67llDortmundPlanByPlanId(rel.getPlanId());
            if (plan != null && checkPlanVisible(plan.getUserId())) {
                visiblePlans.add(plan);
            } else {
                hiddenCount++;
            }
        }
        fx67llDortmundExtraSimulate.setHiddenPlanCount(hiddenCount);
        // plans 摘要只对拥有方案计划查询权限的用户返回，否则只给条数
        fx67llDortmundExtraSimulate.setPlans(planQueryPermission ? visiblePlans : null);
    }

    /**
     * 判断当前登录用户是否拥有指定权限标识
     *
     * @param permission 权限标识
     * @return 是否拥有权限
     */
    private boolean hasPermission(String permission) {
        return SecurityUtils.getLoginUser().getPermissions().contains(permission)
                || SecurityUtils.getLoginUser().getPermissions().contains("*:*:*");
    }

    /**
     * 判断当前用户是否拥有"全部数据"权限角色
     *
     * @return 是否全部数据权限
     */
    private boolean hasAllDataScope() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (roles != null) {
            for (SysRole role : roles) {
                if ("1".equals(role.getDataScope())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验当前用户对方案计划的可见性（数据范围口径）
     *
     * @param planUserId 方案计划归属用户ID
     * @return 是否可见
     */
    private boolean checkPlanVisible(Long planUserId) {
        return hasAllDataScope() || SecurityUtils.getUserId().equals(planUserId);
    }
}
