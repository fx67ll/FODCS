package com.ruoyi.fx67ll.mahjong.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 麻将室对象 fx67ll_mahjong_room
 *
 * @author ruoyi
 * @date 2025-10-16
 */
public class Fx67llMahjongRoom extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 麻将室主键
     */
    private Long mahjongRoomId;

    /**
     * 创建者用户主键
     */
    @Excel(name = "创建者用户主键")
    private Long userId;

    /**
     * 麻将室名称
     */
    @Excel(name = "麻将室名称")
    private String mahjongRoomName;

    /**
     * 麻将室描述
     */
    @Excel(name = "麻将室描述")
    private String mahjongRoomDescription;

    /**
     * 容纳人数（默认4人）
     */
    @Excel(name = "容纳人数", readConverterExp = "默=认4人")
    private Integer mahjongRoomCapacity;

    /**
     * 预留：计费配置（未来存储分时段/包夜规则等JSON数据）
     */
    @Excel(name = "预留：计费配置", readConverterExp = "未=来存储分时段/包夜规则等JSON数据")
    private String mahjongRoomPriceConfig;

    /**
     * 状态（0开放 1关闭）
     */
    @Excel(name = "状态", readConverterExp = "0=开放,1=关闭")
    private String mahjongRoomStatus;

    /**
     * 麻将室备注
     */
    @Excel(name = "麻将室备注")
    private String mahjongRoomRemark;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setMahjongRoomId(Long mahjongRoomId) {
        this.mahjongRoomId = mahjongRoomId;
    }

    public Long getMahjongRoomId() {
        return mahjongRoomId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setMahjongRoomName(String mahjongRoomName) {
        this.mahjongRoomName = mahjongRoomName;
    }

    public String getMahjongRoomName() {
        return mahjongRoomName;
    }

    public void setMahjongRoomDescription(String mahjongRoomDescription) {
        this.mahjongRoomDescription = mahjongRoomDescription;
    }

    public String getMahjongRoomDescription() {
        return mahjongRoomDescription;
    }

    public void setMahjongRoomCapacity(Integer mahjongRoomCapacity) {
        this.mahjongRoomCapacity = mahjongRoomCapacity;
    }

    public Integer getMahjongRoomCapacity() {
        return mahjongRoomCapacity;
    }

    public void setMahjongRoomPriceConfig(String mahjongRoomPriceConfig) {
        this.mahjongRoomPriceConfig = mahjongRoomPriceConfig;
    }

    public String getMahjongRoomPriceConfig() {
        return mahjongRoomPriceConfig;
    }

    public void setMahjongRoomStatus(String mahjongRoomStatus) {
        this.mahjongRoomStatus = mahjongRoomStatus;
    }

    public String getMahjongRoomStatus() {
        return mahjongRoomStatus;
    }

    public void setMahjongRoomRemark(String mahjongRoomRemark) {
        this.mahjongRoomRemark = mahjongRoomRemark;
    }

    public String getMahjongRoomRemark() {
        return mahjongRoomRemark;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("mahjongRoomId", getMahjongRoomId())
                .append("userId", getUserId())
                .append("mahjongRoomName", getMahjongRoomName())
                .append("mahjongRoomDescription", getMahjongRoomDescription())
                .append("mahjongRoomCapacity", getMahjongRoomCapacity())
                .append("mahjongRoomPriceConfig", getMahjongRoomPriceConfig())
                .append("mahjongRoomStatus", getMahjongRoomStatus())
                .append("mahjongRoomRemark", getMahjongRoomRemark())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
