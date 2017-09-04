package com.hjnerp.model;

import com.hjnerp.util.StringUtil;

import java.util.Map;

public class HjListData {
	
  public String id;
  
  public  Map<String, String>  maplist ;
  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, String> getMap() {
		return maplist;
	}
	public void setMap(Map<String, String> maplist) {
		this.maplist = maplist;
	}
   
	public void setIds()
	{
		id = StringUtil.getMyUUID();
	}
   
}
