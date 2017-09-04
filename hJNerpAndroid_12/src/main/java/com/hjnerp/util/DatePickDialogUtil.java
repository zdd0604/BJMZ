package com.hjnerp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import com.hjnerp.model.HJDataPickerInterface;
import com.hjnerpandroid.R;

public class DatePickDialogUtil implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Context context;

	private HJDataPickerInterface dataPickerInterface;  
    public void setCallBack(HJDataPickerInterface dataPickerInterface) {  
        this.dataPickerInterface =dataPickerInterface;  
    }  
    public void finishPicker(String data) {  
    	dataPickerInterface.donePicker(data);  
    }
	
	public DatePickDialogUtil(Context context, String initDateTime) {
		this.context = context;
		this.initDateTime = initDateTime;

	}

	public void init(DatePicker datePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH)  ;
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
	}


	
	
	public AlertDialog dateTimePicKDialog() {
		LinearLayout dateTimeLayout = (LinearLayout) ((Activity) context)
				.getLayoutInflater().inflate(R.layout.common_date_picker, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.data_datepicker);
		init(datePicker);

		ad = new AlertDialog.Builder(context)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finishPicker(dateTime);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finishPicker("");
					}
				}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	@SuppressLint("SimpleDateFormat")
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		dateTime = sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	@SuppressLint("SimpleDateFormat")
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		SimpleDateFormat dd=new SimpleDateFormat("yyyy-mm-dd");
		Date date=null;
		try {
			date = dd.parse(initDateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 if (date !=null)
		 {
			 calendar.setTime(date );
		 }
		return calendar;
	}

	 
}
