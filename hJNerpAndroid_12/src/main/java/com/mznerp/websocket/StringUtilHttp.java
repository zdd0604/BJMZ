package com.mznerp.websocket;

import android.util.Log;

/**
 *
 * @author Lance Chen
 */
public class StringUtilHttp {

	private static final String TAG = "HTTP";
	
    private StringUtilHttp() {
    }

    /**
     * Determine a string is an integer.
     * @param str The string
     * @return Return true if is integer
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Determine a string is empty.
     * @param str The given string
     * @return Return true if is empty
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() <= 0 || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * Determine a string is email.
     * @param email The given string
     * @return Return true if is email
     */
    public static boolean isEmail(String email) {
        if (StringUtilHttp.isEmpty(email)) {
            return false;
        }
        String regex = "^[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+$";
        return email.matches(regex);
    }

    /**
     * Convert string to lower and the first character to upper.
     * @param str The string
     * @return Return string
     */
    public static String firstToUpper(String str) {
        if (isEmpty(str)) {
            return str;
        }
        str = str.trim().toLowerCase();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }
    
    public static void log(String str)
    {
    	if(isEmpty(str))
    	{
    		return;
    	}
		int count = str.length();
		int lineCount = 120;
		for (int i = 0; i < count; i = i + lineCount)
		{
			if (i + lineCount <= count)
			{
				Log.v(TAG, str.substring(i, i + lineCount));
			} else
			{
				Log.v(TAG, str.substring(i, count));
			}
		}
    }
}
