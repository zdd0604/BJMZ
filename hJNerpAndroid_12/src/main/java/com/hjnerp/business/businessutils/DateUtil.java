package com.hjnerp.business.businessutils;

import com.hjnerp.common.Constant;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.SGIN_FORMART);
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

}
