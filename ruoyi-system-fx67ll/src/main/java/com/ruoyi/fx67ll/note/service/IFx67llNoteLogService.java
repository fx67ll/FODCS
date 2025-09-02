package com.ruoyi.fx67ll.note.service;

import java.util.List;

import com.ruoyi.fx67ll.note.domain.Fx67llNoteLog;

/**
 * 备忘记录Service接口
 *
 * @author ruoyi
 * @date 2025-08-26
 */
public interface IFx67llNoteLogService {
    /**
     * 查询备忘记录
     *
     * @param noteId 备忘记录主键
     * @return 备忘记录
     */
    public Fx67llNoteLog selectFx67llNoteLogByNoteId(Long noteId);

    /**
     * 查询备忘记录列表
     *
     * @param fx67llNoteLog 备忘记录
     * @return 备忘记录集合
     */
    public List<Fx67llNoteLog> selectFx67llNoteLogList(Fx67llNoteLog fx67llNoteLog);

    /**
     * 查询备忘记录列表
     *
     * @param fx67llNoteLog 备忘记录
     * @return 备忘记录集合
     */
    public List<Fx67llNoteLog> selectFx67llNoteLogListByUserId(Fx67llNoteLog fx67llNoteLog);

    /**
     * 新增备忘记录
     *
     * @param fx67llNoteLog 备忘记录
     * @return 结果
     */
    public int insertFx67llNoteLog(Fx67llNoteLog fx67llNoteLog);

    /**
     * 修改备忘记录
     *
     * @param fx67llNoteLog 备忘记录
     * @return 结果
     */
    public int updateFx67llNoteLog(Fx67llNoteLog fx67llNoteLog);

    /**
     * 批量删除备忘记录
     *
     * @param noteIds 需要删除的备忘记录主键集合
     * @return 结果
     */
    public int deleteFx67llNoteLogByNoteIds(Long[] noteIds);

    /**
     * 删除备忘记录信息
     *
     * @param noteId 备忘记录主键
     * @return 结果
     */
    public int deleteFx67llNoteLogByNoteId(Long noteId);
}
