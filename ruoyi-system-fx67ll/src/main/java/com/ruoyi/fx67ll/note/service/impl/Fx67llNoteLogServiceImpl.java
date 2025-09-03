package com.ruoyi.fx67ll.note.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.punch.domain.Fx67llPunchLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.note.mapper.Fx67llNoteLogMapper;
import com.ruoyi.fx67ll.note.domain.Fx67llNoteLog;
import com.ruoyi.fx67ll.note.service.IFx67llNoteLogService;

/**
 * 富文本记录Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-26
 */
@Service
public class Fx67llNoteLogServiceImpl implements IFx67llNoteLogService {
    @Autowired
    private Fx67llNoteLogMapper fx67llNoteLogMapper;

    /**
     * 查询富文本记录
     *
     * @param noteId 富文本记录主键
     * @return 富文本记录
     */
    @Override
    public Fx67llNoteLog selectFx67llNoteLogByNoteId(Long noteId) {
        return fx67llNoteLogMapper.selectFx67llNoteLogByNoteId(noteId);
    }

    /**
     * 查询富文本记录列表
     *
     * @param fx67llNoteLog 富文本记录
     * @return 富文本记录
     */
    @Override
    public List<Fx67llNoteLog> selectFx67llNoteLogList(Fx67llNoteLog fx67llNoteLog) {
        return fx67llNoteLogMapper.selectFx67llNoteLogList(fx67llNoteLog);
    }

    /**
     * 查询打卡记录列表
     *
     * @param fx67llNoteLog 富文本记录
     * @return 打卡记录
     */
    @Override
    public List<Fx67llNoteLog> selectFx67llNoteLogListByUserId(Fx67llNoteLog fx67llNoteLog) {
        fx67llNoteLog.setUserId(SecurityUtils.getUserId());
        return fx67llNoteLogMapper.selectFx67llNoteLogList(fx67llNoteLog);
    }


    /**
     * 新增富文本记录
     *
     * @param fx67llNoteLog 富文本记录
     * @return 结果
     */
    @Override
    public int insertFx67llNoteLog(Fx67llNoteLog fx67llNoteLog) {
        fx67llNoteLog.setUserId(SecurityUtils.getUserId());
        fx67llNoteLog.setCreateBy(SecurityUtils.getUsername());
        fx67llNoteLog.setCreateTime(DateUtils.getNowDate());
        fx67llNoteLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llNoteLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llNoteLogMapper.insertFx67llNoteLog(fx67llNoteLog);
    }

    /**
     * 修改富文本记录
     *
     * @param fx67llNoteLog 富文本记录
     * @return 结果
     */
    @Override
    public int updateFx67llNoteLog(Fx67llNoteLog fx67llNoteLog) {
        fx67llNoteLog.setUpdateBy(SecurityUtils.getUsername());
        fx67llNoteLog.setUpdateTime(DateUtils.getNowDate());
        return fx67llNoteLogMapper.updateFx67llNoteLog(fx67llNoteLog);
    }

    /**
     * 批量删除富文本记录
     *
     * @param noteIds 需要删除的富文本记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llNoteLogByNoteIds(Long[] noteIds) {
        return fx67llNoteLogMapper.deleteFx67llNoteLogByNoteIds(noteIds);
    }

    /**
     * 删除富文本记录信息
     *
     * @param noteId 富文本记录主键
     * @return 结果
     */
    @Override
    public int deleteFx67llNoteLogByNoteId(Long noteId) {
        return fx67llNoteLogMapper.deleteFx67llNoteLogByNoteId(noteId);
    }
}
