package com.ruoyi.fx67ll.dortmund.support;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * Dortmund 模块统一数据范围判断。
 * <p>
 * 球队、比赛和赛季等业务对象以 {@code user_id} 标记归属：普通用户只能读取和维护自己的数据，
 * 角色数据范围为“全部数据”（{@code data_scope = '1'}）的用户可以访问全量数据。
 * 该规则只控制记录可见性与维护边界，不参与业务编码唯一性判断；球队编码、赛季编码和比赛编码
 * 始终由数据库全局唯一索引校验，不能按用户拆分。
 * </p>
 */
public final class DortmundDataScope {
    /** RuoYi 数据范围字典中代表“全部数据”的值。 */
    private static final String ALL_DATA_SCOPE = "1";

    private DortmundDataScope() {
    }

    /**
     * 获取当前登录用户主键。
     *
     * @return 当前用户主键
     */
    public static Long currentUserId() {
        return SecurityUtils.getUserId();
    }

    /**
     * 获取列表查询应附加的归属用户条件。
     * <p>全部数据权限返回 {@code null}，表示不强制收窄数据范围；普通用户返回自身用户主键。</p>
     *
     * @return 归属用户过滤值
     */
    public static Long ownerFilterForCurrentUser() {
        return hasAllDataScope() ? null : currentUserId();
    }

    /**
     * 判断当前用户是否拥有“全部数据”权限角色。
     *
     * @return 是否可访问全量数据
     */
    public static boolean hasAllDataScope() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (roles != null) {
            for (SysRole role : roles) {
                if (ALL_DATA_SCOPE.equals(role.getDataScope())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否可以访问指定归属用户的业务记录。
     *
     * @param ownerUserId 业务记录归属用户主键
     * @return 是否可访问
     */
    public static boolean canAccess(Long ownerUserId) {
        return hasAllDataScope() || (ownerUserId != null && ownerUserId.equals(currentUserId()));
    }

    /**
     * 校验当前用户是否可以访问指定归属用户的业务记录。
     * <p>无权限与记录不存在统一返回相同提示，避免通过接口响应推断他人数据是否存在。</p>
     *
     * @param ownerUserId 业务记录归属用户主键
     * @param message 统一的受控业务提示
     */
    public static void requireAccessible(Long ownerUserId, String message) {
        if (!canAccess(ownerUserId)) {
            throw new ServiceException(message);
        }
    }
}
