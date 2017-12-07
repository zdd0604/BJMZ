package com.mznerp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BaseData implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final String FLAG_OK = "ok";
	public static final String FLAG_ERROR = "error";

	public String flag;
	public String message;
	public String passId;
//	public String registtime;//注册时间
//	public String alertTime;//警告时间
//	public String stopTime;//停止时间
	public Object result;

	public boolean isSuccess()
	{
		return FLAG_OK.equalsIgnoreCase(this.flag);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDataList()
	{
		return (List<Map<String, Object>>)result;
	}
}
