package com.ruoyi.fx67ll.mahjong.service.impl;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.fx67ll.mahjong.mapper.Fx67llMahjongReservationLogMapper;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongReservationLogService;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongRoom;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongRoomService;
import com.ruoyi.common.exception.ServiceException;

/**
 * 麻将室预约记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class Fx67llMahjongReservationLogServiceImpl implements IFx67llMahjongReservationLogService {
    @Autowired
    private Fx67llMahjongReservationLogMapper fx67llMahjongReservationLogMapper;

    @Autowired
    private IFx67llMahjongRoomService mahjongRoomService;

    @Autowired
    private ISysUserService userService;

    // 允许操作所有数据的管理员用户名集合（兼容Java 8及以下版本）
    private static final Set<String> ADMIN_USERNAMES;

    static {
        Set<String> adminSet = new HashSet<>();
        adminSet.add("fx67ll");
        adminSet.add("chaoshen");
        // 包装为不可修改集合，防止意外修改
        ADMIN_USERNAMES = Collections.unmodifiableSet(adminSet);
    }

    /**
     * 权限校验：管理员可操作所有数据，非管理员只能操作自己创建的数据
     *
     * @param log 麻将室预约记录对象
     */
    private void checkPermission(Fx67llMahjongReservationLog log) {
        String currentUsername = SecurityUtils.getUsername();
        // 管理员直接通过校验
        if (ADMIN_USERNAMES.contains(currentUsername)) {
            return;
        }
        // 非管理员需校验创建者是否为当前用户
        if (log == null) {
            throw new ServiceException("数据不存在，无法操作！");
        }
        if (!currentUsername.equals(log.getCreateBy())) {
            throw new ServiceException("您没有权限操作该数据，请立刻停止违规操作！");
        }
    }

    /**
     * 查询麻将室预约记录
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 麻将室预约记录
     */
    @Override
    public Fx67llMahjongReservationLog selectFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId) {
        Fx67llMahjongReservationLog log = fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId);
        // 校验权限
        checkPermission(log);
        return log;
    }

    /**
     * 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 麻将室预约记录
     */
    @Override
    public List<Fx67llMahjongReservationLog> selectFx67llMahjongReservationLogList(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        String currentUsername = SecurityUtils.getUsername();
        // 非管理员只能查询自己创建的记录
        if (!ADMIN_USERNAMES.contains(currentUsername)) {
            fx67llMahjongReservationLog.setCreateBy(currentUsername);
        }
        return fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
    }

    /**
     * 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 麻将室预约记录
     */
    @Override
    public List<Fx67llMahjongReservationLog> selectFx67llMahjongReservationLogListByUserId(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        fx67llMahjongReservationLog.setUserId(SecurityUtils.getUserId());
        return fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogList(fx67llMahjongReservationLog);
    }

    /**
     * 新增麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    @Override
    public int insertFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        Fx67llMahjongRoom fx67llMahjongRoom = mahjongRoomService.selectFx67llMahjongRoomByMahjongRoomId(fx67llMahjongReservationLog.getMahjongRoomId());
        boolean isFx67ll = "fx67ll".equals(SecurityUtils.getUsername());
        Long nowUserId = fx67llMahjongReservationLog.getUserId() != null
                ? fx67llMahjongReservationLog.getUserId()
                : SecurityUtils.getUserId();
        SysUser sysUser = userService.selectUserById(isFx67ll ? nowUserId : SecurityUtils.getUserId());
        if (sysUser == null) {
            throw new ServiceException("用户不存在，用户ID：" + fx67llMahjongRoom.getUserId());
        }
        if (fx67llMahjongReservationLog.getMahjongRoomId() == null) {
            throw new ServiceException("预约的麻将室ID为必填参数！");
        }
        if (fx67llMahjongRoom == null) {
            throw new ServiceException("麻将室不存在，麻将室ID：" + fx67llMahjongReservationLog.getMahjongRoomId());
        }
        fx67llMahjongReservationLog.setReservationContact(sysUser.getPhonenumber());
        fx67llMahjongReservationLog.setMahjongRoomName(fx67llMahjongRoom.getMahjongRoomName());
        fx67llMahjongReservationLog.setUserId(isFx67ll ? nowUserId : SecurityUtils.getUserId());
        fx67llMahjongReservationLog.setCreateBy(SecurityUtils.getUsername());
        fx67llMahjongReservationLog.setCreateTime(DateUtils.getNowDate());
        fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llMahjongReservationLogMapper.insertFx67llMahjongReservationLog(fx67llMahjongReservationLog);
    }

    /**
     * 修改麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    @Override
    public int updateFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        // 先查询原数据
        Fx67llMahjongReservationLog oldLog = fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogByMahjongReservationLogId(
                fx67llMahjongReservationLog.getMahjongReservationLogId());
        // 校验权限
        checkPermission(oldLog);
        // 更新信息
        boolean isFx67ll = "fx67ll".equals(SecurityUtils.getUsername());
        if (isFx67ll) {
            Long nowUserId = fx67llMahjongReservationLog.getUserId() != null
                    ? fx67llMahjongReservationLog.getUserId()
                    : oldLog.getUserId();
            SysUser sysUser = userService.selectUserById(nowUserId);
            Long nowMahjongRoomId= fx67llMahjongReservationLog.getMahjongRoomId() != null
                    ? fx67llMahjongReservationLog.getMahjongRoomId()
                    : oldLog.getMahjongRoomId();
            Fx67llMahjongRoom fx67llMahjongRoom = mahjongRoomService.selectFx67llMahjongRoomByMahjongRoomId(nowMahjongRoomId);
            if (sysUser == null) {
                throw new ServiceException("用户不存在，用户ID：" + fx67llMahjongRoom.getUserId());
            }
            if (fx67llMahjongRoom == null) {
                throw new ServiceException("麻将室不存在，麻将室ID：" + nowMahjongRoomId);
            }
            fx67llMahjongReservationLog.setUserId(nowUserId);
            fx67llMahjongReservationLog.setCreateBy(sysUser.getUserName());
            fx67llMahjongReservationLog.setMahjongRoomId(nowMahjongRoomId);
            fx67llMahjongReservationLog.setMahjongRoomName(fx67llMahjongRoom.getMahjongRoomName());
            fx67llMahjongReservationLog.setReservationContact(sysUser.getPhonenumber());
            fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
            fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        }else{
            fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
            fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        }
        return fx67llMahjongReservationLogMapper.updateFx67llMahjongReservationLog(fx67llMahjongReservationLog);
    }

    /**
     * 批量删除麻将室预约记录
     *
     * @param mahjongReservationLogIds 需要删除的麻将室预约记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogIds(Long[] mahjongReservationLogIds) {
        String currentUsername = SecurityUtils.getUsername();
        // 非管理员需要逐个校验权限
        if (!ADMIN_USERNAMES.contains(currentUsername)) {
            for (Long logId : mahjongReservationLogIds) {
                Fx67llMahjongReservationLog log = fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogByMahjongReservationLogId(logId);
                // 校验权限
                checkPermission(log);
            }
        }
        return fx67llMahjongReservationLogMapper.deleteFx67llMahjongReservationLogByMahjongReservationLogIds(mahjongReservationLogIds);
    }

    /**
     * 删除麻将室预约记录信息
     *
     * @param mahjongReservationLogId 麻将室预约记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongReservationLogByMahjongReservationLogId(Long mahjongReservationLogId) {
        Fx67llMahjongReservationLog log = fx67llMahjongReservationLogMapper.selectFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId);
        // 校验权限
        checkPermission(log);
        return fx67llMahjongReservationLogMapper.deleteFx67llMahjongReservationLogByMahjongReservationLogId(mahjongReservationLogId);
    }
}
