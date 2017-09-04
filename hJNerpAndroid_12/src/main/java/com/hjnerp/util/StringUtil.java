package com.hjnerp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class 	StringUtil {
	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("") || str.trim().equals("－请选择－")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	/**
	 * 请选择
	 */
	final static String PLEASE_SELECT = "请选择...";

	public static boolean notEmpty(Object o) {
		return o != null && !"".equals(o.toString().trim())
				&& !"null".equalsIgnoreCase(o.toString().trim())
				&& !"undefined".equalsIgnoreCase(o.toString().trim())
				&& !PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean empty(Object o) {
		return o == null || "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim())
				|| PLEASE_SELECT.equals(o.toString().trim());
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean decimal(Object o) {
		double n = 0;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0.0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 给JID返回用户名
	 * 
	 * @param Jid
	 * @return
	 */
	public static String getUserNameByJid(String Jid) {
		if (empty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	/**
	 * 给用户名返回JID
	 * 
	 * @param jidFor
	 *            域名//如ahic.com.cn
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName, String jidFor) {
		if (empty(jidFor) || empty(jidFor)) {
			return null;
		}
		return userName + "@" + jidFor;
	}

	/**
	 * 给用户名返回JID,使用默认域名ahic.com.cn
	 * 
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName) {
		String jidFor = "ahic.com.cn";
		return getJidByName(userName, jidFor);
	}

	/**
	 * 根据给定的时间字符串，返回月 日 时 分 秒
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	/**
	 * 根据给定的时间字符串，返回月 日 时 分 月到分钟
	 * 
	 * @param allDate
	 *            like "yyyy-MM-dd hh:mm:ss SSS"
	 * @return
	 */
	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}

	public static boolean isNullOrEmpty(String str) {		
		 if (str == null || str.length() == 0)
	            return true;
	        else
	            return false;
	}

	public static boolean isStringArrayContains(String[] stringArray, String source) {
		  // 转换为list
		  List<String> tempList = Arrays.asList(stringArray); 
		  // 利用list的包含方法,进行判断
		  if(tempList.contains(source))
		  {
		   return true;
		  } else {
		   return false;
		  }
		 }
	
	/**
	 * 随机生成字符串
	 * 
	 * @param length
	 * @return
	 */
	public static final String randomString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
					+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
			// numbersAndLetters =
			// ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

		}
		return new String(randBuffer);
	}
	
	public static final String randomDataString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789").toCharArray();
			// numbersAndLetters =
			// ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];

		}
		String date = new String(randBuffer);
		return date;
	}
	
	// UUID+设备号序列号 唯一识别码（不可变）
	public static String getMyUUID(Context context) {

		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, tmPhone, androidId;

		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();

		return uniqueId;

	}
	
	// 获取可变UUID
	public static String getMyUUID() {

		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();

		return uniqueId;

	}

	//page是否包含datas
	public static List<String> exist2(String[] page,String[] datas){
        List<String> strs = new ArrayList<String>();
        for (int i = 0; i < datas.length; i++)  
        {
            //需要搞清楚两个数组的顺序，主要分清哪个数组中的值是否存在于哪一个数组，下面判断是datas数组中的值是否存在于page中
            boolean exists = Arrays.asList(page).contains((datas[i]));  
            if (!exists)  
            {  
                strs.add(datas[i]);  
            }  
        }  
        return strs;
    }

	/**
     * 
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName( "com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt( localClass.getField("status_bar_height").get( localObject).toString());
                statusHeight = activity.getResources( ).getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    
    
	//把一个字符串中的大写转为小写，小写转换为大写：思路1
	public static String exChange(String str){
		StringBuffer sb = new StringBuffer();
		if(str!=null){
			for(int i=0;i<str.length();i++){
				char c = str.charAt(i);
				if(Character.isUpperCase(c)){
					sb.append(Character.toLowerCase(c));
				}else if(Character.isLowerCase(c)){
					sb.append(Character.toUpperCase(c)); 
				}
			}
		}
		
		return sb.toString();
	}
	
	//把一个字符串中的大写转为小写
	public static String lowerCaseString(String str){
		return str.toLowerCase();
	}



	/***
	 * 判断字符串是否为空
	 *
	 * @param strContent
	 *            你要判断的字符串
	 * @return 非空返回true
	 */
	public static boolean isStrTrue(String strContent) {
		if (strContent != null && !"".equals(strContent)
				&& !TextUtils.isEmpty(strContent)
				&& !"".equals(strContent.trim())
				&& strContent.length() > 0) {
			return true;
		}
		return false;
	}

}
