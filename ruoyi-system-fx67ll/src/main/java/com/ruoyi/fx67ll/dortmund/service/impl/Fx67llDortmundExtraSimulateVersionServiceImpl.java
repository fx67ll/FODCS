package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraSimulateVersionMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundRoundPlanRelMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtraSimulateVersion;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraSimulateVersionService;

/**
 * 外快盈亏数值模拟版本记录Service业务层处理
 * <p>
 * 版本号按用户维度单调递增；并发插入由唯一索引兜底，冲突后重读最大值重试。
 * 作废是不可逆业务状态：先清理该版本全部轮次的计划关系，再更新版本状态，避免关系残留。
 *
 * @author fx67ll
 * @date 2026-09-11
 */
@Service
public class Fx67llDortmundExtraSimulateVersionServiceImpl implements IFx67llDortmundExtraSimulateVersionService {
    @Autowired
    private Fx67llDortmundExtraSimulateVersionMapper fx67llDortmundExtraSimulateVersionMapper;

    @Autowired
    private Fx67llDortmundRoundPlanRelMapper fx67llDortmundRoundPlanRelMapper;

    /**
     * 查询外快盈亏数值模拟版本记录列表
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionList(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionList(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 查询外快盈亏数值模拟版本记录列表（仅本人）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 外快盈亏数值模拟版本记录集合
     */
    @Override
    public List<Fx67llDortmundExtraSimulateVersion> selectFx67llDortmundExtraSimulateVersionListByUserId(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        fx67llDortmundExtraSimulateVersion.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionList(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 新增外快盈亏数值模拟版本记录（版本序号按用户自动递增）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        validateVersionFields(fx67llDortmundExtraSimulateVersion, true);
        Long userId = SecurityUtils.getUserId();
        fx67llDortmundExtraSimulateVersion.setUserId(userId);
        fx67llDortmundExtraSimulateVersion.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulateVersion.setCreateTime(DateUtils.getNowDate());
        // 版本号在用户维度固定递增；冲突重试有固定上限，避免并发争用形成无限循环。
        for (int attempt = 0; attempt < DortmundModuleConfig.Simulation.MAX_VERSION_CREATE_ATTEMPTS; attempt++) {
            Long maxVersionNo = fx67llDortmundExtraSimulateVersionMapper
                    .selectMaxVersionNoByUserIdForUpdate(userId);
            fx67llDortmundExtraSimulateVersion.setVersionNo(
                    maxVersionNo == null ? 1L : maxVersionNo + 1);
            try {
                return fx67llDortmundExtraSimulateVersionMapper
                        .insertFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion);
            } catch (DuplicateKeyException e) {
                if (attempt == DortmundModuleConfig.Simulation.MAX_VERSION_CREATE_ATTEMPTS - 1) {
                    throw new ServiceException(
                            "版本序号生成发生并发冲突，请重新提交！");
                }
            }
        }
        throw new ServiceException("版本序号生成失败，请重新提交！");
    }

    /**
     * 修改外快盈亏数值模拟版本记录（用于版本启用停用）
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        Fx67llDortmundExtraSimulateVersion oldVersion = checkVersionAccess(
                fx67llDortmundExtraSimulateVersion.getVersionId());
        if ("1".equals(oldVersion.getInvalidFlag())) {
            throw new ServiceException("数值模拟版本已作废，不能继续修改！");
        }
        validateVersionFields(fx67llDortmundExtraSimulateVersion, false);
        fx67llDortmundExtraSimulateVersion.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulateVersion.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraSimulateVersionMapper.updateFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 作废外快盈亏数值模拟版本记录（软删除，作废后不在列表中展示）
     * <p>
     * 同一事务里先删该版本全部轮次的方案计划关系行再作废版本，方案计划和比赛不动
     *
     * @param fx67llDortmundExtraSimulateVersion 外快盈亏数值模拟版本记录（需携带版本ID）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int invalidateFx67llDortmundExtraSimulateVersion(Fx67llDortmundExtraSimulateVersion fx67llDortmundExtraSimulateVersion) {
        Fx67llDortmundExtraSimulateVersion oldVersion = checkVersionAccess(
                fx67llDortmundExtraSimulateVersion.getVersionId());
        if ("1".equals(oldVersion.getInvalidFlag())) {
            throw new ServiceException("数值模拟版本已作废，请刷新后重试！");
        }
        // 先完成版本存在性与归属校验，再清理轮次关系，避免越权请求先破坏关联数据
        fx67llDortmundRoundPlanRelMapper.deleteFx67llDortmundRoundPlanRelByVersionId(fx67llDortmundExtraSimulateVersion.getVersionId());
        fx67llDortmundExtraSimulateVersion.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundExtraSimulateVersion.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundExtraSimulateVersionMapper.invalidateFx67llDortmundExtraSimulateVersion(fx67llDortmundExtraSimulateVersion);
    }

    /**
     * 根据版本ID查询外快盈亏数值模拟版本记录
     *
     * @param versionId 版本ID
     * @return 外快盈亏数值模拟版本记录
     */
    @Override
    public Fx67llDortmundExtraSimulateVersion selectFx67llDortmundExtraSimulateVersionById(Long versionId) {
        return fx67llDortmundExtraSimulateVersionMapper.selectFx67llDortmundExtraSimulateVersionById(versionId);
    }

    /**
     * 校验版本字段，新增时全部必填，修改时只校验请求中实际携带的字段
     * <p>
     * 数值文本规则与前端数值模拟表单一致：普通十进制、集中配置的整数位/小数位/总长度；
     * 初始数值和目标数值必须大于0，每轮系数必须大于1。
     *
     * @param version  版本记录
     * @param creating 是否为新增
     */
    private void validateVersionFields(Fx67llDortmundExtraSimulateVersion version, boolean creating) {
        version.setVersionName(creating
                ? DortmundFieldValidator.requireText(version.getVersionName(), "版本名称",
                        DortmundModuleConfig.Field.SIMULATE_VERSION_NAME)
                : DortmundFieldValidator.optionalText(version.getVersionName(), "版本名称",
                        DortmundModuleConfig.Field.SIMULATE_VERSION_NAME));
        version.setSimMode(DortmundFieldValidator.validateEnum(
                version.getSimMode(), "模拟模式", creating, "full", "half"));
        version.setInitialValue(DortmundFieldValidator.validateDecimalText(
                version.getInitialValue(), "初始数值", creating, false,
                DortmundModuleConfig.Numeric.SIMULATION_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_FRACTION_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_TEXT_LENGTH,
                DortmundModuleConfig.Simulation.MIN_VALUE, null, null));
        version.setCoefficient(DortmundFieldValidator.validateDecimalText(
                version.getCoefficient(), "每轮系数", creating, false,
                DortmundModuleConfig.Numeric.SIMULATION_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_FRACTION_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_TEXT_LENGTH,
                null, DortmundModuleConfig.Simulation.COEFFICIENT_EXCLUSIVE_MIN, null));
        version.setTargetValue(DortmundFieldValidator.validateDecimalText(
                version.getTargetValue(), "目标数值", creating, false,
                DortmundModuleConfig.Numeric.SIMULATION_INTEGER_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_FRACTION_DIGITS,
                DortmundModuleConfig.Numeric.SIMULATION_TEXT_LENGTH,
                DortmundModuleConfig.Simulation.MIN_VALUE, null, null));
        if (version.getDelFlag() != null) {
            version.setDelFlag(DortmundFieldValidator.validateEnum(
                    version.getDelFlag(), "版本启用状态", true, "0", "2"));
        }
    }

    /**
     * 校验版本存在性与数据范围，返回数据库中的原版本用于后续状态判断
     *
     * @param versionId 版本主键
     * @return 原版本记录
     */
    private Fx67llDortmundExtraSimulateVersion checkVersionAccess(Long versionId) {
        if (versionId == null) {
            throw new ServiceException("数值模拟版本主键不能为空！");
        }
        Fx67llDortmundExtraSimulateVersion version = fx67llDortmundExtraSimulateVersionMapper
                .selectFx67llDortmundExtraSimulateVersionById(versionId);
        if (version == null) {
            throw new ServiceException("数值模拟版本不存在或已被删除，请刷新后重试！");
        }
        if (!hasAllDataScope() && !SecurityUtils.getUserId().equals(version.getUserId())) {
            throw new ServiceException("没有权限操作该数值模拟版本！");
        }
        return version;
    }

    /**
     * 判断当前用户是否拥有全部数据权限
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
}
