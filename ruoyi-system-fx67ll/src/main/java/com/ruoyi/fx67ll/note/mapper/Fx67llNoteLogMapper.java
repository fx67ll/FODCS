package com.ruoyi.fx67ll.note.mapper;

import java.util.List;

import com.ruoyi.fx67ll.note.domain.Fx67llNoteLog;

/**
 * 富文本记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-08-26
 */
public interface Fx67llNoteLogMapper {
    /**
     * 查询富文本记录
     *
     * @param noteId 富文本记录主键
     * @return 富文本记录
     */
    public Fx67llNoteLog selectFx67llNoteLogByNoteId(Long noteId);

    /**
     * 查询富文本记录列表
     *
     * @param fx67llNoteLog 富文本记录
     * @return 富文本记录集合
     */
    public List<Fx67llNoteLog> selectFx67llNoteLogList(Fx67llNoteLog fx67llNoteLog);

    /**
     * 新增富文本记录
     *
     * @param fx67llNoteLog 富文本记录
     * @return 结果
     */
    public int insertFx67llNoteLog(Fx67llNoteLog fx67llNoteLog);

    /**
     * 修改富文本记录
     *
     * @param fx67llNoteLog 富文本记录
     * @return 结果
     */
    public int updateFx67llNoteLog(Fx67llNoteLog fx67llNoteLog);

    /**
     * 删除富文本记录
     *
     * @param noteId 富文本记录主键
     * @return 结果
     */
    public int deleteFx67llNoteLogByNoteId(Long noteId);

    /**
     * 批量删除富文本记录
     *
     * @param noteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFx67llNoteLogByNoteIds(Long[] noteIds);
}
