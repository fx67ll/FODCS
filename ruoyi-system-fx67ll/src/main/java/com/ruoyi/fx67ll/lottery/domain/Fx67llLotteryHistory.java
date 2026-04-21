package com.ruoyi.fx67ll.lottery.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 彩票历史号码出现频率统计对象
 *
 * @author fx67ll
 * @date 2026-04-21
 */
public class Fx67llLotteryHistory {
    private static final long serialVersionUID = 1L;

    /**
     * 号码类型
     */
    private Integer numberType;

    /**
     * 区域（前区/后区）
     */
    private String zone;

    /**
     * 出现次数
     */
    private Integer occurrenceCount;

    /**
     * 号码列表（逗号分隔）
     */
    private String numbers;

    public Integer getNumberType() {
        return numberType;
    }

    public void setNumberType(Integer numberType) {
        this.numberType = numberType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Integer getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(Integer occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("numberType", getNumberType())
                .append("zone", getZone())
                .append("occurrenceCount", getOccurrenceCount())
                .append("numbers", getNumbers())
                .toString();
    }
}
