package com.ruoyi.fx67ll.mahjong.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    @Override
    public Fx67llMahjongRoom selectFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId) {
        return fx67llMahjongRoomMapper.selectFx67llMahjongRoomByMahjongRoomId(mahjongRoomId);
    }

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室
     */
    @Override
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomList(Fx67llMahjongRoom fx67llMahjongRoom) {
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
        fx67llMahjongRoom.setUserId(SecurityUtils.getUserId());
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
        fx67llMahjongRoom.setUserId(SecurityUtils.getUserId());
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
        return fx67llMahjongRoomMapper.deleteFx67llMahjongRoomByMahjongRoomId(mahjongRoomId);
    }
}
