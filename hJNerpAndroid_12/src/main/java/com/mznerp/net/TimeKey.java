package com.mznerp.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class TimeKey {

	public static String PAST_TIME = "Ox31PrTS3nliWn/jlsbvkw==";
	public static String KEY = "12345678";

	public static boolean isPasttime() {

		String result2 = "";
		long current, last;
		try {
			result2 = DES.decryptDES(PAST_TIME, KEY);
			last = Long.parseLong(result2);
			current = new Date().getTime();
			Log.i("info", "current= "+current);
			Log.i("info", "last= "+last);
			if (current > last) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("info", e.toString());
			return true;
		}
	}

	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
