package com.hjnerp.util;

/**
 * The proxy of the class android.util.Log
 * 
 * @author John Kenrinus Lee
 * @date 2014-9-22
 */
public class Log {
	/** the default TAG for null TAG */
	public static final String TAG = "System.out";
	/** if the log level used bigger than this value cause logging */
	public static final int START_LOG_LEVEL = 1;

	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static final int VERBOSE = android.util.Log.VERBOSE;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static final int DEBUG = android.util.Log.DEBUG;

	/**
	 * Priority constant for the println method; use Log.i.
	 */
	public static final int INFO = android.util.Log.INFO;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static final int WARN = android.util.Log.WARN;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static final int ERROR = android.util.Log.ERROR;

	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = android.util.Log.ASSERT;
	/**
	 * @author haijian 控制是否打印日志
	 */
	public static boolean IsDebug = true;

	private Log() {
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String msg)

	{
		if (IsDebug) {

			v(TAG, msg);
		}
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= VERBOSE) {
				android.util.Log.v(tag, msg);
			}
		}
		handleError(VERBOSE, tag, msg, null);
	}

	/**
	 * Send a {@link #VERBOSE} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void v(String tag, String msg, Throwable tr) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= VERBOSE) {
				android.util.Log.v(tag, msg, tr);
			}
		}
		handleError(VERBOSE, tag, msg, tr);
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void d(String msg) {
		if (IsDebug) {

			d(TAG, msg);
		}
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void d(String tag, String msg) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= DEBUG) {
				android.util.Log.d(tag, msg);
			}
		}
		handleError(DEBUG, tag, msg, null);
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= DEBUG) {
				android.util.Log.d(tag, msg, tr);
			}
		}
		handleError(DEBUG, tag, msg, tr);
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String msg) {
		if (IsDebug) {

			i(TAG, msg);
		}
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= INFO) {
				android.util.Log.i(tag, msg);
			}
		}
		handleError(INFO, tag, msg, null);
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= INFO) {
				android.util.Log.i(tag, msg, tr);
			}
		}
		handleError(INFO, tag, msg, tr);
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String msg) {
		if (IsDebug) {

			w(TAG, msg);
		}
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String tag, String msg) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= WARN) {
				android.util.Log.w(tag, msg);
			}
		}
		handleError(WARN, tag, msg, null);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= WARN) {
				android.util.Log.w(tag, msg, tr);
			}
		}
		handleError(WARN, tag, msg, tr);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tr
	 *            An exception to log
	 */
	public static void w(Throwable tr) {
		if (IsDebug) {

			w(TAG, tr);
		}
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param tr
	 *            An exception to log
	 */
	public static void w(String tag, Throwable tr) {
		if (IsDebug) {

			w(tag, "", tr);
		}
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String msg) {
		if (IsDebug) {

			e(TAG, msg);
		}
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tr
	 *            An exception to log
	 */
	public static void e(Throwable tr) {
		if (IsDebug) {

			e(TAG, "", tr);
		}
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= ERROR) {
				android.util.Log.e(tag, msg);
			}
		}
		handleError(ERROR, tag, msg, null);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (IsDebug) {

			if (START_LOG_LEVEL <= ERROR) {
				android.util.Log.e(tag, msg, tr);
			}
		}
		handleError(ERROR, tag, msg, tr);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, Throwable tr) {
		if (IsDebug) {

			e(tag, "", tr);
		}
	}

	private static void handleError(int level, String tag, String msg,
			Throwable tr) {
		// TODO
	}
}
