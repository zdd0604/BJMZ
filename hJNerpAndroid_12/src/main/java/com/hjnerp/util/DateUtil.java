/**   
 * 用一句话描述该文件做什么.
 * @title DateUtil.java
 * @package com.sinsoft.android.util
 * @author 李庆义  
 * @update 2012-6-26 上午9:57:56  
 */
package com.hjnerp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;
import android.text.format.Time;

/**
 * 日期操作工具类.
 * 
 * @author 李庆义
 */

public class DateUtil {

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String TAG = "DateUtil";

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	// "2014-11-21 12:12:10" 转为"1432256346"
	public static String StringDataToMillons(String format, String timesource) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date dt2 = null;
		try {
			dt2 = (Date) sdf.parse(timesource);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 继续转换得到秒数的long型
		long lTime = dt2.getTime();
		String time = String.valueOf(lTime);
		return time;

	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}

		if (str.trim().length() == 10) {
			str = str.trim() + " 00:00:00";
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + " "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String year = String.valueOf(c.get(Calendar.YEAR));

		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (c.get(Calendar.MONTH) + 1 < 10) {
			month = "0" + String.valueOf(c.get(Calendar.MONTH) + 1);
		}

		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if (c.get(Calendar.DAY_OF_MONTH) < 10) {
			day = "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		}

		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		if (c.get(Calendar.HOUR_OF_DAY) < 10) {
			hour = "0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		}

		String minute = String.valueOf(c.get(Calendar.MINUTE));
		if (c.get(Calendar.MINUTE) < 10) {
			minute = "0" + String.valueOf(c.get(Calendar.MINUTE));
		}

		String second = String.valueOf(c.get(Calendar.SECOND));
		if (c.get(Calendar.SECOND) < 10) {
			second = "0" + String.valueOf(c.get(Calendar.SECOND));
		}

		String data = year + month + day + hour + minute + second;

		return data;
	}

	// 毫秒
	public static String getCurrentTimeM() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String year = String.valueOf(c.get(Calendar.YEAR));

		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (Integer.parseInt(month) < 10) {
			month = "0" + month;
		}

		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if (Integer.parseInt(day) < 10) {
			day = "0" + day;
		}

		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		if (Integer.parseInt(hour) < 10) {
			hour = "0" + hour;
		}

		String minute = String.valueOf(c.get(Calendar.MINUTE));
		if (Integer.parseInt(minute) < 10) {
			minute = "0" + minute;
		}

		String second = String.valueOf(c.get(Calendar.SECOND));
		if (Integer.parseInt(second) < 10) {
			second = "0" + second;
		}
		String msecond = String.valueOf(c.get(Calendar.MILLISECOND));
		if (Integer.parseInt(msecond) < 10) {
			second = "0" + msecond;
		}
		String data = year + month + day + hour + minute + second + msecond;

		return data;
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	// 格式到秒
	public static String getMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

	}

	// 格式到天
	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}

	public static int getCurrentDay(){
		Calendar c = Calendar.getInstance();  
		int day = c.get(Calendar.DAY_OF_MONTH);  
		return day;
	}

	// 格式到毫秒
	public static String getSMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

	}

	public static String getDatediff(String fdate, String tdate) {

		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long between = 0;
		try {
			java.util.Date begin = dfs.parse(fdate);
			java.util.Date end = dfs.parse(tdate);
			between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		String retvalue = "";

		if (day != 0) {
			retvalue = retvalue + day + "天";
		}
		if (hour != 0) {
			retvalue = retvalue + hour + "小时";
		}
		if (min != 0) {
			retvalue = retvalue + min + "分";
		}
		if (s != 0) {
			retvalue = retvalue + s + "秒";
		}
		return retvalue;

	}

	/**
	 * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
	 * 
	 * @param context
	 * @param when
	 * @return String
	 */
	public static String formatTimeString(String datetime) {
		Time then = new Time();
		Date date = null;
		date = str2Date(datetime);

		then.set(date.getTime());
		Time now = new Time();
		now.setToNow();

		String formatStr;
		if (then.year != now.year) {
			formatStr = "yyyy/MM/dd";
		} else if (then.yearDay != now.yearDay) {
			// If it is from a different day than today, show only the date.
			formatStr = "MM月dd日";
		} else {
			// Otherwise, if the message is from today, show the time.
			formatStr = "HH:MM";
		}

		if (then.year == now.year && then.yearDay == now.yearDay) {

			SimpleDateFormat sdfd1 = new SimpleDateFormat("HH:MM");
			String time1 = sdfd1.format(date.getTime());
			if (time1 != null && time1.length() == 5
					&& time1.substring(0, 1).equals("0")) {
				time1 = time1.substring(1);
			}
			return "今天" + time1;
		} else if ((then.year == now.year)
				&& ((now.yearDay - then.yearDay) == 1)) {
			SimpleDateFormat sdfd1 = new SimpleDateFormat("HH:MM");
			String time1 = sdfd1.format(date.getTime());
			if (time1 != null && time1.length() == 5
					&& time1.substring(0, 1).equals("0")) {
				time1 = time1.substring(1);
			}
			return "昨天" + time1;
		} else {
			SimpleDateFormat sdfd = new SimpleDateFormat(formatStr);
			String time = sdfd.format(date.getTime());
			if (time != null && time.length() == 5
					&& time.substring(0, 1).equals("0")) {
				time = time.substring(1);
			}
			return time;
		}
	}

	public static final SimpleDateFormat yyyy_MM_dd__HHimm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.getDefault());
	public static final SimpleDateFormat timestamp = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

	/** 当逻辑语义上或者参数本身存在问题时返回"", 否则返回人可阅读的日期字符串 */
	public static final String msgToHumanReadableTime(String time) {
		if (TextUtils.isEmpty(time))
			return "";

		String yesterday = "昨天";
		String[] labels = new String[] { " 晚上 ", " 下午 ", " 中午 ", " 上午 ", " 清晨 " };
		long ctime = Long.parseLong(time);
		Calendar ct = Calendar.getInstance();
		Calendar calc = (Calendar) ct.clone();

		if (ctime > calc.getTimeInMillis())
			return "";

		ct.setTimeInMillis(ctime);
		calc.set(Calendar.HOUR_OF_DAY, 23);
		calc.set(Calendar.MINUTE, 59);
		calc.set(Calendar.SECOND, 59);
		calc.set(Calendar.MILLISECOND, 999);
		long t = calc.getTimeInMillis();
		if (ctime > t)
			return "";

		calc.add(Calendar.HOUR_OF_DAY, -6);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return labels[0] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -5);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return labels[1] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -1);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return labels[2] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -5);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return labels[3] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -7);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return labels[4] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -6);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return yesterday + labels[0] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -5);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return yesterday + labels[1] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -1);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return yesterday + labels[2] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -5);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return yesterday + labels[3] + packageHourMinute(ct);

		calc.add(Calendar.HOUR_OF_DAY, -7);
		t = calc.getTimeInMillis();
		if (ctime > t)
			return yesterday + labels[4] + packageHourMinute(ct);

		int hour = ct.get(Calendar.HOUR_OF_DAY);
		String noon;
		if (hour > 17)
			noon = labels[0];
		else if (hour > 12)
			noon = labels[1];
		else if (hour == 12)
			noon = labels[2];
		else if (hour > 6)
			noon = labels[3];
		else
			noon = labels[4];
		return (ct.get(Calendar.MONTH) + 1) + "月"
				+ ct.get(Calendar.DAY_OF_MONTH) + "日" + noon
				+ packageHourMinute(ct);
	}

	private static final String packageHourMinute(Calendar ct) {
		String dot = ":";
		String hour = String.valueOf(ct.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minite = String.valueOf(ct.get(Calendar.MINUTE));
		if (minite.length() == 1)
			minite = "0" + minite;
		return hour + dot + minite;
	}
	
	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
