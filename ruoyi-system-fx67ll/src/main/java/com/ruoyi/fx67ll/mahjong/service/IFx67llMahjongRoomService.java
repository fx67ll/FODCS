package com.ruoyi.fx67ll.mahjong.service;

import java.util.List;

import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongRoom;

/**
 * 麻将室Service接口
 *
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IFx67llMahjongRoomService {
    /**
     * 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    public Fx67llMahjongRoom selectFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId);

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室集合
     */
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomList(Fx67llMahjongRoom fx67llMahjongRoom);

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室集合
     */
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomListByUserId(Fx67llMahjongRoom fx67llMahjongRoom);

    /**
     * 新增麻将室
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 结果
     */
    public int insertFx67llMahjongRoom(Fx67llMahjongRoom fx67llMahjongRoom);

    /**
     * 修改麻将室
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 结果
     */
    public int updateFx67llMahjongRoom(Fx67llMahjongRoom fx67llMahjongRoom);

    /**
     * 批量删除麻将室
     *
     * @param mahjongRoomIds 需要删除的麻将室主键集合
     * @return 结果
     */
    public int deleteFx67llMahjongRoomByMahjongRoomIds(Long[] mahjongRoomIds);

    /**
     * 删除麻将室信息
     *
     * @param mahjongRoomId 麻将室主键
     * @return 结果
     */
    public int deleteFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId);
}
