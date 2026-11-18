package com.ruoyi.fx67ll.dortmund.support;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ruoyi.common.exception.ServiceException;

/**
 * Dortmund 权重换算边界。
 * <p>
 * 前端、接口、请求快照和 AI 契约统一使用 0.0000 至 1.0000、最多四位小数；
 * 数据库使用放大 10000 倍的整数保存，业务比较也统一换算为整数单位，避免浮点或小数比较误差。
 * 空权重表示用户未显式填写，有效值按 1.0000 处理。
 *
 * @author fx67ll
 */
public final class DortmundWeightCodec {
    /** 权重小数位 */
    public static final int SCALE = 4;

    /** 权重 1.0000 对应的整数单位 */
    public static final int UNITS_PER_ONE = 10000;

    /** 最小权重单位 */
    public static final int MIN_UNITS = 0;

    /** 最大权重单位 */
    public static final int MAX_UNITS = UNITS_PER_ONE;

    /** 权重最小值 */
    public static final BigDecimal MIN_WEIGHT = new BigDecimal("0.0000");

    /** 空权重参与业务计算时的默认值 */
    public static final BigDecimal DEFAULT_WEIGHT = new BigDecimal("1.0000");

    private DortmundWeightCodec() {
    }

    /**
     * 校验并规范化可选权重。
     *
     * @param value 0 至 1 的权重
     * @param label 字段名称
     * @return 四位小数权重；空值返回 null
     */
    public static BigDecimal normalize(BigDecimal value, String label) {
        if (value == null) {
            return null;
        }
        return fromUnits(toUnits(value, label));
    }

    /**
     * 校验并规范化权重，空值按 1.0000 返回。
     *
     * @param value 0 至 1 的权重
     * @param label 字段名称
     * @return 非空四位小数权重
     */
    public static BigDecimal effective(BigDecimal value, String label) {
        return value == null ? DEFAULT_WEIGHT : normalize(value, label);
    }

    /**
     * 将接口权重换算为数据库整数单位，并完成范围与精度校验。
     *
     * @param value 0 至 1 的权重
     * @param label 字段名称
     * @return 0 至 10000 的整数单位
     */
    public static int toUnits(BigDecimal value, String label) {
        if (value == null) {
            throw new ServiceException(label + "不能为空！");
        }
        DortmundFieldValidator.validateDecimal(value, label, true, false, MIN_WEIGHT, null,
                DEFAULT_WEIGHT, 1, SCALE);
        return value.movePointRight(SCALE).setScale(0, RoundingMode.UNNECESSARY).intValueExact();
    }

    /**
     * 将可选接口权重换算为数据库整数单位，空值按默认权重单位返回。
     *
     * @param value 0 至 1 的权重
     * @param label 字段名称
     * @return 0 至 10000 的整数单位
     */
    public static int effectiveUnits(BigDecimal value, String label) {
        return value == null ? UNITS_PER_ONE : toUnits(value, label);
    }

    /**
     * 将数据库整数单位还原为接口使用的四位小数权重。
     *
     * @param units 0 至 10000 的整数单位
     * @return 0.0000 至 1.0000 的权重
     */
    public static BigDecimal fromUnits(Integer units) {
        if (units == null) {
            return null;
        }
        if (units < MIN_UNITS || units > MAX_UNITS) {
            throw new ServiceException("权重存储值必须在0到10000之间！");
        }
        return BigDecimal.valueOf(units, SCALE);
    }

    /**
     * 判断权重是否满足接口精度与范围约束。
     *
     * @param value 待判断权重
     * @return 合法返回 true
     */
    public static boolean isValid(BigDecimal value) {
        if (value == null || value.signum() < 0 || value.compareTo(DEFAULT_WEIGHT) > 0) {
            return false;
        }
        return Math.max(value.stripTrailingZeros().scale(), 0) <= SCALE;
    }

    /**
     * 按整数单位比较两个权重，避免 BigDecimal 标度差异导致相等判断失败。
     *
     * @param left 左侧权重
     * @param right 右侧权重
     * @param label 字段名称
     * @return 整数单位相等返回 true
     */
    public static boolean same(BigDecimal left, BigDecimal right, String label) {
        return left != null && right != null && toUnits(left, label) == toUnits(right, label);
    }
}
