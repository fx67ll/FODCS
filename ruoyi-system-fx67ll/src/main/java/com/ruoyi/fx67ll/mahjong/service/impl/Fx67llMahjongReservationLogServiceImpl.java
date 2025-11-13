package com.ruoyi.fx67ll.mahjong.service.impl;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.text.SimpleDateFormat;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.fx67ll.mahjong.mapper.Fx67llMahjongReservationLogMapper;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLog;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongReservationLogExt;
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

    // 日期格式化器（线程安全）
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
     * 时间段重叠校验：判断当前预约时间段是否与已有预约重叠
     *
     * @param log      预约记录对象
     * @param isUpdate 是否为修改操作（修改时排除自身）
     */
    private void checkTimeOverlap(Fx67llMahjongReservationLog log, boolean isUpdate) {
        if (log.getReservationStartTime() == null || log.getReservationEndTime() == null) {
            throw new ServiceException("预约开始时间和结束时间不能为空！");
        }
        if (log.getReservationEndTime().compareTo(log.getReservationStartTime()) <= 0) {
            throw new ServiceException("预约结束时间必须晚于开始时间，请重新选择时间段！");
        }
        // 格式化时间为字符串（统一格式便于数据库比较）
        String startTime = DATE_FORMAT.format(log.getReservationStartTime());
        String endTime = DATE_FORMAT.format(log.getReservationEndTime());
        // 查询重叠记录（修改时排除自身ID）
        Long excludeLogId = isUpdate ? log.getMahjongReservationLogId() : null;
        List<Fx67llMahjongReservationLog> overlapLogs = fx67llMahjongReservationLogMapper.selectOverlapReservationLogs(
                log.getMahjongRoomId(), startTime, endTime, excludeLogId);
        // 存在重叠记录则抛出异常
        if (!overlapLogs.isEmpty()) {
            throw new ServiceException("该时间段已被预约，请选择其他时间段！");
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
     * 提供给 APP 查询麻将室预约记录列表
     *
     * @param fx67llMahjongReservationLogExt 麻将室预约记录Ext
     * @return 预约记录列表（含用户信息）
     */
    @Override
    public List<Fx67llMahjongReservationLogExt> selectReservationLogForApp(Fx67llMahjongReservationLogExt fx67llMahjongReservationLogExt) {
        return fx67llMahjongReservationLogMapper.selectReservationLogByRoomAndDate(fx67llMahjongReservationLogExt);
    }

    /**
     * 新增麻将室预约记录
     *
     * @param fx67llMahjongReservationLog 麻将室预约记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llMahjongReservationLog(Fx67llMahjongReservationLog fx67llMahjongReservationLog) {
        Fx67llMahjongRoom fx67llMahjongRoom = mahjongRoomService.selectFx67llMahjongRoomByMahjongRoomId(fx67llMahjongReservationLog.getMahjongRoomId());
        boolean isFx67ll = "fx67ll".equals(SecurityUtils.getUsername());
        Long nowUserId = fx67llMahjongReservationLog.getUserId() != null
                ? fx67llMahjongReservationLog.getUserId()
                : SecurityUtils.getUserId();
        SysUser sysUser = userService.selectUserById(isFx67ll ? nowUserId : SecurityUtils.getUserId());
        if (sysUser == null) {
            throw new ServiceException("用户不存在，用户ID：" + nowUserId);
        }
        if (fx67llMahjongReservationLog.getMahjongRoomId() == null) {
            throw new ServiceException("预约的麻将室ID为必填参数！");
        }
        if (fx67llMahjongRoom == null) {
            throw new ServiceException("麻将室不存在，麻将室ID：" + fx67llMahjongReservationLog.getMahjongRoomId());
        }

        // 核心修改：新增预约前校验时间段是否重叠（非修改操作）
        checkTimeOverlap(fx67llMahjongReservationLog, false);

        fx67llMahjongReservationLog.setReservationContact(
                sysUser.getContactInfo() != null && !sysUser.getContactInfo().trim().isEmpty()
                        ? sysUser.getContactInfo()
                        : sysUser.getPhonenumber()
        );
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
    @Transactional(rollbackFor = Exception.class)
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
            Long nowMahjongRoomId = fx67llMahjongReservationLog.getMahjongRoomId() != null
                    ? fx67llMahjongReservationLog.getMahjongRoomId()
                    : oldLog.getMahjongRoomId();
            Fx67llMahjongRoom fx67llMahjongRoom = mahjongRoomService.selectFx67llMahjongRoomByMahjongRoomId(nowMahjongRoomId);
            if (sysUser == null) {
                throw new ServiceException("用户不存在，用户ID：" + nowUserId);
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
        } else {
            fx67llMahjongReservationLog.setUpdateBy(SecurityUtils.getUsername());
            fx67llMahjongReservationLog.setUpdateTime(DateUtils.getNowDate());
        }

        // 核心修改：修改预约前校验时间段是否重叠（修改操作，排除自身）
        checkTimeOverlap(fx67llMahjongReservationLog, true);

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