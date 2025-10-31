package com.ruoyi.fx67ll.mahjong.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.fx67ll.mahjong.mapper.Fx67llMahjongRoomMapper;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongRoom;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongRoomService;

/**
 * 麻将室Service业务层处理
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class Fx67llMahjongRoomServiceImpl implements IFx67llMahjongRoomService {
    @Autowired
    private Fx67llMahjongRoomMapper fx67llMahjongRoomMapper;

    @Autowired
    private ISysUserService userService;

    // 管理员用户名
    private static final String ADMIN_USERNAME = "fx67ll";

    /**
     * 权限校验：非管理员只能操作自己创建的数据
     *
     * @param room 麻将室对象
     */
    private void checkPermission(Fx67llMahjongRoom room) {
        String currentUsername = SecurityUtils.getUsername();
        // 管理员直接通过校验
        if (ADMIN_USERNAME.equals(currentUsername)) {
            return;
        }
        // 非管理员需校验创建者是否为当前用户
        if (room == null) {
            throw new ServiceException("数据不存在，无法操作！");
        }
        if (!currentUsername.equals(room.getCreateBy())) {
            throw new ServiceException("您没有权限操作该数据，请立刻停止违规操作！");
        }
    }

    /**
     * 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    @Override
    public Fx67llMahjongRoom selectFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId) {
        Fx67llMahjongRoom room = fx67llMahjongRoomMapper.selectFx67llMahjongRoomByMahjongRoomId(mahjongRoomId);
        // 校验权限
        checkPermission(room);
        return room;
    }

    /**
     * 提供给 APP 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    @Override
    public Fx67llMahjongRoom selectFx67llMahjongRoomInfoForApp(Long mahjongRoomId) {
        return fx67llMahjongRoomMapper.selectFx67llMahjongRoomDescriptionByMahjongRoomId(mahjongRoomId);
    }

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室
     */
    @Override
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomList(Fx67llMahjongRoom fx67llMahjongRoom) {
        String currentUsername = SecurityUtils.getUsername();
        // 非管理员只能查询自己创建的数据
        if (!ADMIN_USERNAME.equals(currentUsername)) {
            fx67llMahjongRoom.setCreateBy(currentUsername);
        }
        return fx67llMahjongRoomMapper.selectFx67llMahjongRoomList(fx67llMahjongRoom);
    }

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室
     */
    @Override
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomListByUserId(Fx67llMahjongRoom fx67llMahjongRoom) {
        String currentUsername = SecurityUtils.getUsername();
        fx67llMahjongRoom.setUserId(SecurityUtils.getUserId());
        // 非管理员只能查询自己创建的数据
        if (!ADMIN_USERNAME.equals(currentUsername)) {
            fx67llMahjongRoom.setCreateBy(currentUsername);
        }
        return fx67llMahjongRoomMapper.selectFx67llMahjongRoomList(fx67llMahjongRoom);
    }

    /**
     * 新增麻将室
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 结果
     */
    @Override
    public int insertFx67llMahjongRoom(Fx67llMahjongRoom fx67llMahjongRoom) {
        SysUser sysUser = userService.selectUserById(fx67llMahjongRoom.getUserId());
        if (sysUser == null) {
            throw new ServiceException("用户不存在，用户ID：" + fx67llMahjongRoom.getUserId());
        }
        // 新增数据
        fx67llMahjongRoom.setUserName(sysUser.getUserName());
        fx67llMahjongRoom.setCreateBy(SecurityUtils.getUsername());
        fx67llMahjongRoom.setCreateTime(DateUtils.getNowDate());
        fx67llMahjongRoom.setUpdateBy(SecurityUtils.getUsername());
        fx67llMahjongRoom.setUpdateTime(DateUtils.getNowDate());
        return fx67llMahjongRoomMapper.insertFx67llMahjongRoom(fx67llMahjongRoom);
    }

    /**
     * 修改麻将室
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 结果
     */
    @Override
    public int updateFx67llMahjongRoom(Fx67llMahjongRoom fx67llMahjongRoom) {
        // 先查询原数据
        Fx67llMahjongRoom oldRoom = fx67llMahjongRoomMapper.selectFx67llMahjongRoomByMahjongRoomId(
                fx67llMahjongRoom.getMahjongRoomId());
        // 校验权限
        checkPermission(oldRoom);
        // 更新信息
        fx67llMahjongRoom.setUpdateBy(SecurityUtils.getUsername());
        fx67llMahjongRoom.setUpdateTime(DateUtils.getNowDate());
        return fx67llMahjongRoomMapper.updateFx67llMahjongRoom(fx67llMahjongRoom);
    }

    /**
     * 批量删除麻将室
     *
     * @param mahjongRoomIds 需要删除的麻将室主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongRoomByMahjongRoomIds(Long[] mahjongRoomIds) {
        String currentUsername = SecurityUtils.getUsername();
        // 非管理员需要逐个校验权限
        if (!ADMIN_USERNAME.equals(currentUsername)) {
            for (Long roomId : mahjongRoomIds) {
                Fx67llMahjongRoom room = fx67llMahjongRoomMapper.selectFx67llMahjongRoomByMahjongRoomId(roomId);
                // 校验权限
                checkPermission(room);
            }
        }
        return fx67llMahjongRoomMapper.deleteFx67llMahjongRoomByMahjongRoomIds(mahjongRoomIds);
    }

    /**
     * 删除麻将室信息
     *
     * @param mahjongRoomId 麻将室主键
     * @return 结果
     */
    @Override
    public int deleteFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId) {
        Fx67llMahjongRoom room = fx67llMahjongRoomMapper.selectFx67llMahjongRoomByMahjongRoomId(mahjongRoomId);
        // 校验权限
        checkPermission(room);
        return fx67llMahjongRoomMapper.deleteFx67llMahjongRoomByMahjongRoomId(mahjongRoomId);
    }
}
