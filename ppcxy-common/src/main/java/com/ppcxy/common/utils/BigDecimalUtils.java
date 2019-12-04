package com.ppcxy.common.utils;

import java.math.BigDecimal;

/**
 * Created by weep on 2016-7-12.
 */
public class BigDecimalUtils {

    private static final int DEFAULT_DEV_SCALE = 10;

    private BigDecimal bigDecimal;

    /**
     * 构造初始值为0的计算对象.
     */
    private BigDecimalUtils() {
        bigDecimal = new BigDecimal("0");
    }

    /**
     * 构造默认初始值的计算对象.
     *
     * @param number
     * @param <N>
     */
    private <N extends Number> BigDecimalUtils(N number) {
        bigDecimal = new BigDecimal(number.toString());
    }

    public static BigDecimalUtils newInstance() {
        return new BigDecimalUtils();
    }

    public static <N extends Number> BigDecimalUtils newInstance(N number) {
        return new BigDecimalUtils(number);
    }

    /**
     * 加法运算,支持链式操作.
     *
     * @param number
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils add(N number) {
        this.bigDecimal = this.bigDecimal.add(new BigDecimal(number.toString()));
        return this;
    }

    /**
     * 减法运算,支持链式操作.
     *
     * @param number
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils sub(N number) {
        this.bigDecimal = this.bigDecimal.subtract(new BigDecimal(number.toString()));
        return this;
    }

    /**
     * 乘法运算,支持链式操作.
     *
     * @param number
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils mult(N number) {
        this.bigDecimal = this.bigDecimal.multiply(new BigDecimal(number.toString()));
        return this;
    }

    /**
     * 乘法运算,保留设定的精度,支持链式操作.
     *
     * @param number
     * @param scale
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils mult(N number, int scale) {
        mult(number).round(scale);
        return this;
    }

    /**
     * 除法运算,使用默认精度,支持链式操作.
     *
     * @param number
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils div(N number) {
        div(number, DEFAULT_DEV_SCALE);
        return this;
    }

    /**
     * 触发运算,使用设定的精度,支持链式操作.
     *
     * @param number
     * @param scale
     * @param <N>
     * @return
     */
    public <N extends Number> BigDecimalUtils div(N number, int scale) {
        this.bigDecimal = this.bigDecimal.divide(new BigDecimal(number.toString()), scale, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    /**
     * 对当前值进行保留精度计算.
     *
     * @param scale
     * @return
     */
    public BigDecimalUtils round(int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        BigDecimal one = new BigDecimal("1");
        this.bigDecimal = this.bigDecimal.divide(one, scale, BigDecimal.ROUND_HALF_UP);
        return this;
    }

    /**
     * 返回计算结果,默认为double
     *
     * @return
     */
    public Double build() {
        return buildDoubleValue();
    }

    /**
     * 返回计算结果,double
     *
     * @return
     */
    public Double buildDoubleValue() {
        return this.bigDecimal.doubleValue();
    }

    /**
     * 返回计算结果,用Long存储,会丢失精度.
     *
     * @return
     */
    public Long buildLongValue() {
        return this.bigDecimal.longValue();
    }

}
