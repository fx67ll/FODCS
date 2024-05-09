package com.ruoyi.fx67ll.lottery.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 历史号码记录中奖统计对象
 *
 * @author fx67ll
 * @date 2024-05-08
 */
public class Fx67llLotteryTotalReward {
    private static final long serialVersionUID = 1L;

    /**
     * 统计类型
     */
    private String lotteryType;

    /**
     * 总购买期数统计
     */
    private String totalTickets;

    /**
     * 总购买号码数统计
     */
    private String totalNumbers;

    /**
     * 中奖期数统计
     */
    private String winningTickets;

    /**
     * 中奖金额统计
     */
    private String totalWinningAmount;

    public String getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType;
    }

    public String getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(String totalTickets) {
        this.totalTickets = totalTickets;
    }

    public String getTotalNumbers() {
        return totalNumbers;
    }

    public void setTotalNumbers(String totalNumbers) {
        this.totalNumbers = totalNumbers;
    }

    public String getWinningTickets() {
        return winningTickets;
    }

    public void setWinningTickets(String winningTickets) {
        this.winningTickets = winningTickets;
    }

    public String getTotalWinningAmount() {
        return totalWinningAmount;
    }

    public void setTotalWinningAmount(String totalWinningAmount) {
        this.totalWinningAmount = totalWinningAmount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("lotteryType", getLotteryType())
                .append("totalTickets", getTotalTickets())
                .append("totalNumbers", getTotalNumbers())
                .append("winningTickets", getWinningTickets())
                .append("totalWinningAmount", getTotalWinningAmount())
                .toString();
    }
}
