package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 超神用户管理（复用用户模块路径，通过子路径区分）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user/chaoshen")
public class SysUserChaoshenController extends BaseController {

    public static final String USER_KEY_CHAOSHEN = "chaoshen";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取超神用户列表（仅查询user_key=chaoshen的用户）
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        // 使用常量筛选超神用户
        user.setUserKey(USER_KEY_CHAOSHEN);
        List<SysUser> list = userService.selectChaoshenUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据超神用户ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream()
                .filter(r -> !r.isAdmin())
                .collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());

        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            SysUser sysUserWithSensitiveField = userService.selectChaoshenUserSensitiveFieldById(userId);
            // 使用常量校验超神用户标识
            if (!USER_KEY_CHAOSHEN.equals(sysUserWithSensitiveField.getUserKey())) {
                return error("查询失败，该用户不是超神用户");
            }
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream()
                    .map(SysRole::getRoleId)
                    .collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 修改超神用户信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:edit')")
    @Log(title = "超神用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        Long userId = user.getUserId();
        SysUser originalUser = userService.selectUserById(userId);
        SysUser sysUserWithSensitiveField = userService.selectChaoshenUserSensitiveFieldById(userId);
        if (originalUser == null || !USER_KEY_CHAOSHEN.equals(sysUserWithSensitiveField.getUserKey())) {
            return error("修改失败，该用户不是超神用户");
        }

        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());

        if (!userService.checkUserNameUnique(user)) {
            return error("修改失败，用户名'" + user.getUserName() + "'已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("修改失败，手机号'" + user.getPhonenumber() + "'已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("修改失败，邮箱'" + user.getEmail() + "'已存在");
        }

        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除超神用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:remove')")
    @Log(title = "超神用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }

        if (ArrayUtils.contains(userIds, 1000001)) {
            return error("麻将馆超级管理员禁止删除");
        }

        for (Long userId : userIds) {
            SysUser user = userService.selectUserById(userId);
            SysUser sysUserWithSensitiveField = userService.selectChaoshenUserSensitiveFieldById(userId);
            if (user == null || !USER_KEY_CHAOSHEN.equals(sysUserWithSensitiveField.getUserKey())) {
                return error("删除失败，包含非超神用户数据");
            }
        }

        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 超神用户重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:resetPwd')")
    @Log(title = "超神用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        Long userId = user.getUserId();
        SysUser originalUser = userService.selectUserById(userId);
        SysUser sysUserWithSensitiveField = userService.selectChaoshenUserSensitiveFieldById(userId);
        if (originalUser == null || !USER_KEY_CHAOSHEN.equals(sysUserWithSensitiveField.getUserKey())) {
            return error("重置失败，该用户不是超神用户");
        }

        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());

        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 超神用户状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:chaoshen:edit')")
    @Log(title = "超神用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        Long userId = user.getUserId();
        SysUser originalUser = userService.selectUserById(userId);
        SysUser originalUserWithSensitiveField = userService.selectChaoshenUserSensitiveFieldById(userId);
        if (originalUser == null || !USER_KEY_CHAOSHEN.equals(originalUserWithSensitiveField.getUserKey())) {
            return error("状态修改失败，该用户不是超神用户");
        }

        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());

        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}