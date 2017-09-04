package com.hjnerp.model;

import java.io.Serializable;
import java.util.Map;

public class ServletMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int version = 1;
	public int flag;
	public String id;
	public String command;
	public String timestamp = String.valueOf(System.currentTimeMillis());
	public String type;
	public Map<String, Object> data;
}