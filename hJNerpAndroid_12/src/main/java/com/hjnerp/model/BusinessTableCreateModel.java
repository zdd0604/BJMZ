package com.hjnerp.model;

import java.util.HashMap;

import com.hjnerp.util.myscom.StringUtils;

public class BusinessTableCreateModel
{
	public static final String DIVIDER_STRING = "#&#";
	public String table;
	public String param;
	public String cols;
	public String values;
	
	public HashMap<String, String> data;
	public String insertSqls;
	public String deleteSql;
	
	public void build()
	{
		data = new HashMap<String, String>();
		String[] colNames = cols.split(",");
		String[] colValues = null;
		if(StringUtils.isBlank(values))
		{
			colValues = new String[colNames.length];
			for(int i = 0; i < colNames.length; ++i)
				colValues[i] = " ";
		}else{
			colValues = values.split(",");
		}
		for(int i = 0; i < colNames.length; ++i)
			data.put(colNames[i], colValues[i]);
	}
	
	public void create()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(table).append(" where ").append(param);
		deleteSql = sb.toString();
		
		if(!values.isEmpty())
		{
			sb = new StringBuffer();
			String[] strs = values.split(DIVIDER_STRING);
			for(String str : strs)
			{
				sb.append("insert into ").append(table).append("(").append(cols).append(") values (").append(str).append(") ").append(DIVIDER_STRING);
			}
			insertSqls = sb.toString();
		}
	}
}
