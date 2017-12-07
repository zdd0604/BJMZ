package com.mznerp.business.businessutils;

import android.util.Log;

import com.mznerp.common.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zdd on 2016/11/11.
 * 获取时间的类
 */

public class BusinessTimeUtils {

    /**
     * @param timeStyle
     * @return 时间格式
     */
    public static String getCurrentTime(String timeStyle) {
        // 获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat(timeStyle);
        Date curDate = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(curDate);

        return currentTime;
    }

    /**
     * 两个时间的比较
     * 格式 **:**
     *
     * @param nowTime
     * @param compareTime
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Boolean getSginCompare(String nowTime, String compareTime) {
        String nowT = nowTime.substring(nowTime.length() - 8, nowTime.length() - 3);
        int indexN = nowT.indexOf(":");
        StringBuffer nTimeBuff = new StringBuffer();
        nTimeBuff.append(nowT.substring(0, indexN) + nowT.substring(indexN + 1, nowT.length()));


        int indexC = compareTime.indexOf(":");
        StringBuffer cTimeBuff = new StringBuffer();
        cTimeBuff.append(compareTime.substring(0, indexC) + compareTime.substring(indexC + 1, compareTime.length()));
        Log.v("show", nowT + "----" + compareTime + "      " + nTimeBuff.toString() + "......" + cTimeBuff.toString());
        if (Integer.parseInt(nTimeBuff.toString().trim()) <= Integer.parseInt(cTimeBuff.toString().trim())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 两个时间的比较
     * 格式 **:**
     *
     * @param nowTime
     * @param compareTime
     * @return
     */
    public static Boolean getOutCompare(String nowTime, String compareTime) {
        String nowT = nowTime.substring(nowTime.length() - 8, nowTime.length() - 3);
        int indexN = nowT.indexOf(":");
        StringBuffer nTimeBuff = new StringBuffer();
        nTimeBuff.append(nowT.substring(0, indexN) + nowT.substring(indexN + 1, nowT.length()));


        int indexC = compareTime.indexOf(":");
        StringBuffer cTimeBuff = new StringBuffer();
        cTimeBuff.append(compareTime.substring(0, indexC) + compareTime.substring(indexC + 1, compareTime.length()));
        Log.v("show", nowT + "----" + compareTime + "      " + nTimeBuff.toString() + "......" + cTimeBuff.toString());
        if (Integer.parseInt(nTimeBuff.toString().trim()) >= Integer.parseInt(cTimeBuff.toString().trim())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 两个时间的比较
     * 格式 **:**
     *
     * @param nowTime
     * @return
     */
    public static String getOutCompareAddTime(String nowTime) {

        //上班前偏差的时间
        int var_bonTime = 0;
        //上班后偏差的时间
        int var_lonTime = 0;


        String nowT = nowTime.substring(nowTime.length() - 8, nowTime.length() - 3);
        int indexN = nowT.indexOf(":");
        StringBuffer nTimeBuff = new StringBuffer();
        String houston = nowT.substring(0, indexN).trim();
        String minute = nowT.substring(indexN + 1, nowT.length());
        nTimeBuff.append(houston + minute);
        int nTime = Integer.valueOf(nTimeBuff.toString());

        //上班前偏差
//        String var_on = getIntegerTime(nowTime, Constant.ctlm7161.getVar_on());
//        Log.v("show", " 计划上班时间：" + Constant.ctlm7161.getVar_on());
//        //下班前偏差
       getIntegerTimeOut(nowTime, Constant.ctlm7161.getVar_off());

        return "";
    }


    /**
     * 上班时间计算
     *
     * @param coustomTime
     * @return
     */
    public static void getIntegerTime(String nowTime, String coustomTime) {
        int var_on = 0;
        int var_bon = 0;
        int var_lon = 0;
        int var_lon2 = 0;
        int var_onnow = 0;

        int indexN = coustomTime.indexOf(":");
        String houston = coustomTime.substring(0, indexN);
        String minute = coustomTime.substring(indexN + 1, coustomTime.length());


        //计划上班上班总时长
        var_on = Integer.valueOf(houston) * 60 + Integer.valueOf(minute);
        //上班前偏差
        var_bon = Integer.valueOf(houston) * 60 + Integer.valueOf(minute) - Integer.valueOf(Constant.ctlm7161.getVar_bon().trim());
        //上班后偏差
        var_lon = Integer.valueOf(houston) * 60 + Integer.valueOf(minute) + Integer.valueOf(Constant.ctlm7161.getVar_lon().trim());
        //上班后迟到区间
        var_lon2 = Integer.valueOf(houston) * 60 + Integer.valueOf(minute) + Integer.valueOf(Constant.ctlm7161.getVar_lon2().trim());


        String nowT = nowTime.substring(nowTime.length() - 8, nowTime.length() - 3);
        int indexNow = nowT.indexOf(":");
        String houstonNow = nowT.substring(0, indexNow);
        String minuteNow = nowT.substring(indexN + 1, nowT.length());
        //计划上班上班总时长
        var_onnow = Integer.valueOf(houstonNow) * 60 + Integer.valueOf(minuteNow);
        if (var_bon <= var_onnow && var_onnow <= var_lon) {
            //正常上班
            Log.v("show", "正常上班");
            Constant.sginType = 0;
        } else if (var_lon <= var_onnow && var_onnow <= var_lon2) {
            //迟到
            Log.v("show", "迟到");
            Constant.sginType = 1;
        } else {
            //打卡异常
            Log.v("show", "打卡异常");
            Constant.sginType = 2;
        }

        Log.v("show", nowTime + "   上班计划时间：" + var_on + "    上班前偏差：" + var_bon
                + "   实际上班：" + var_onnow + "   上班后偏差：" + var_lon
                + "   迟到：" + var_lon2 + "   计划上班时间：" + coustomTime);

    }


    /**
     * 上班时间计算
     *
     * @param coustomTime
     * @return
     */
    public static void getIntegerTimeOut(String nowTime, String coustomTime) {
        int var_on = 0;
        int var_boff = 0;
        int var_loff = 0;
        int var_boff2 = 0;
        int var_onnow = 0;

        int indexN = coustomTime.indexOf(":");
        String houston = coustomTime.substring(0, indexN);
        String minute = coustomTime.substring(indexN + 1, coustomTime.length());

        String nowT = nowTime.substring(nowTime.length() - 8, nowTime.length() - 3);
        int indexNow = nowT.indexOf(":");
        String houstonNow = nowT.substring(0, indexNow);
        String minuteNow = nowT.substring(indexN + 1, nowT.length());
        //计划上班上班总时长
        var_onnow = Integer.valueOf(houstonNow) * 60 + Integer.valueOf(minuteNow);


        //计划上班上班总时长
        var_on = Integer.valueOf(houston) * 60 + Integer.valueOf(minute);
        //下班前偏差
        var_boff = Integer.valueOf(houston) * 60 + Integer.valueOf(minute) - Integer.valueOf(Constant.ctlm7161.getVar_boff().trim());
        //下班后偏差
        var_loff = Integer.valueOf(houston) * 60 + Integer.valueOf(minute) + Integer.valueOf(Constant.ctlm7161.getVar_loff().trim());
        //早退期间
        var_boff2 = var_boff - Integer.valueOf(Constant.ctlm7161.getVar_boff2().trim());

        if (var_boff <= var_onnow && var_onnow <= var_loff) {
            //正常下班
            Log.v("show", "正常下班");
            Constant.outType = 0;
        } else if (var_onnow >= var_boff2 && var_onnow <= var_boff) {
            //早退
            Log.v("show", "早退");
            Constant.outType = 1;
        } else {
            //异常
            Log.v("show", "异常");
            Constant.outType = 2;
        }

        Log.v("show", nowTime + "   下班计划时间：" + var_on + "    提前下班：" + var_boff
                + "   实际下班：" + var_onnow + "   下班后偏差：" + var_loff
                + "   早退期间：" + var_boff2 + "   计划下班班时间：" + coustomTime);
    }

}