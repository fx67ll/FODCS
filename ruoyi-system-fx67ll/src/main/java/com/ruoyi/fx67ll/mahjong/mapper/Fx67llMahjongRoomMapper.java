package com.ruoyi.fx67ll.mahjong.mapper;

import java.util.List;

import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongRoom;

/**
 * 麻将室Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-16
 */
public interface Fx67llMahjongRoomMapper {
    /**
     * 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    public Fx67llMahjongRoom selectFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId);

    /**
     * 提供给 APP 查询麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 麻将室
     */
    public Fx67llMahjongRoom selectFx67llMahjongRoomDescriptionByMahjongRoomId(Long mahjongRoomId);

    /**
     * 查询麻将室列表
     *
     * @param fx67llMahjongRoom 麻将室
     * @return 麻将室集合
     */
    public List<Fx67llMahjongRoom> selectFx67llMahjongRoomList(Fx67llMahjongRoom fx67llMahjongRoom);

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
     * 删除麻将室
     *
     * @param mahjongRoomId 麻将室主键
     * @return 结果
     */
    public int deleteFx67llMahjongRoomByMahjongRoomId(Long mahjongRoomId);

    /**
     * 批量删除麻将室
     *
     * @param mahjongRoomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llMahjongRoomByMahjongRoomIds(Long[] mahjongRoomIds);
}
