package com.mznerp.widget;

import java.text.NumberFormat;

/**
 * Created by zhangdongdong on 2018/3/20.
 * 计算工具
 */

public class CountUtils {

    /**
     * 将double转换成百分比
     * @param percent
     * @param digit
     * @return
     */
    public static String countNumberPercentage(Double percent,int digit){
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(digit);
        return nt.format(percent);
    }
}
