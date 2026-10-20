package com.ruoyi.fx67ll.dortmund.domain;

/**
 * 外快盈亏历史记录合并请求对象
 * <p>
 * 前端合并确认弹窗提交的请求体：待合并记录主键集合 + 合并后新记录的备注（可不填写）
 *
 * @author fx67ll
 * @date 2026-10-20
 */
public class Fx67llDortmundExtraMergeBody {
    /**
     * 需要合并的外快盈亏记录主键集合
     */
    private Long[] extraIds;

    /**
     * 合并后新记录的备注（可不填写）
     */
    private String extraRemark;

    public Long[] getExtraIds() {
        return extraIds;
    }

    public void setExtraIds(Long[] extraIds) {
        this.extraIds = extraIds;
    }

    public String getExtraRemark() {
        return extraRemark;
    }

    public void setExtraRemark(String extraRemark) {
        this.extraRemark = extraRemark;
    }
}
