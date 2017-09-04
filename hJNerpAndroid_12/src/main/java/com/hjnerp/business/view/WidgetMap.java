package com.hjnerp.business.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class WidgetMap {
	public static Map<String, Class<?>> widgetMap;
	public static Context context;

	static {
		if(context != null) {
			widgetMap = new HashMap<String, Class<?>>();
			widgetMap.put(WidgetName.HJ_LABEL,TextView.class);
			widgetMap.put(WidgetName.HJ_TEXTVIEW,EditText.class);
			widgetMap.put("list", ListView.class);
		}
	}

}
