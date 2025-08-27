package com.ruoyi.fx67ll.note.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 备忘记录对象 fx67ll_note_log
 *
 * @author ruoyi
 * @date 2025-08-26
 */
public class Fx67llNoteLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 备忘记录主键
     */
    private Long noteId;

    /**
     * 备忘内容
     */
    @Excel(name = "备忘内容")
    private String noteContent;

    /**
     * 备忘记录备注
     */
    @Excel(name = "备忘记录备注")
    private String noteRemark;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteRemark(String noteRemark) {
        this.noteRemark = noteRemark;
    }

    public String getNoteRemark() {
        return noteRemark;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("noteId", getNoteId())
                .append("noteContent", getNoteContent())
                .append("noteRemark", getNoteRemark())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
