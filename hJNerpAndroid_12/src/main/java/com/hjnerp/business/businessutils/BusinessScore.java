package com.hjnerp.business.businessutils;

import java.math.BigDecimal;

/**
 * Created by Admin on 2017/3/9.
 */

public class BusinessScore {
    /**
     * 关键及工作目标都有的情况
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double smark1(double d1, double d2) {
        Double sumNumb = (d1 * d2) * 100 / 12;
        BigDecimal b = new BigDecimal(sumNumb);
        double doubleValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return doubleValue;
    }

    /**
     * 两者其中一个的计算
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double smark2(double d1, double d2) {
        Double sumNumb = (d1 * d2) * 100 / 6;
        BigDecimal b = new BigDecimal(sumNumb);
        double doubleValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return doubleValue;
    }
}
