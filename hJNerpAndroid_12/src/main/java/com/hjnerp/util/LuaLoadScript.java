package com.hjnerp.util;

import com.hjnerp.business.BusinessLua;
import com.hjnerp.model.HJSender;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public  class LuaLoadScript {

	public static LuaState L;   
	public static BusinessLua lua;
	  
	///打开加载文件
	public static void  LoadScript(  ) throws Exception
	{
	
		 
		if (L == null)
		{ 
			L = LuaStateFactory.newLuaState(); 
	    	L.openLibs();    
	    	InputStream in =   LuaLoadScript.class.getResourceAsStream( "androidlua.lua");
          // cass.getResources().getAssets().open("androidlua.lua");  
	    	String ls_item ; 
	    	ls_item = readAll(in);
	        L.LdoString(ls_item); 
		}
	}   
	
	public static void runScript(String Script){
		L.LdoString(Script);
	}

	//传入lua Table对象
	public static void runScript(String Script,Map<String,String> mMap){
		L.newTable();
		
		L.pushString(HJSender.ROW);//键
		L.pushString(mMap.get(HJSender.ROW)); 
		L.setTable(-3);
		
		L.pushString(HJSender.COL);
		L.pushString(mMap.get(HJSender.COL)); 
		L.setTable(-3);
		
		L.pushString(HJSender.COLID);
		L.pushString(mMap.get(HJSender.COLID)); 
		L.setTable(-3);
		
		L.pushString(HJSender.VALUES);
		L.pushString(mMap.get(HJSender.VALUES)); 
		L.setTable(-3);
		
		L.pushString(HJSender.BILLNO);
		L.pushString(mMap.get(HJSender.BILLNO)); 
		L.setTable(-3);
		
		L.pushString(HJSender.NODEID);
		L.pushString(mMap.get(HJSender.NODEID));  
		L.setTable(-3);
		
		L.setGlobal( "sender");
	
		L.LdoString(Script);
	}
	
	//传入lua Table对象
		public static void runScriptSearch(String Script,ArrayList<String> mMap){
			L.newTable();
			
			for ( int i = 0  ; i <mMap.size();i++   )
			{
			   L.pushString(  String.valueOf(i));
			   L.pushString(mMap.get(i).toString());
			   
			   L.setTable(-3); 
			}
			L.setGlobal( "searchresult"); 
			L.LdoString(Script);
		}
	
	///行运函数
	public static String  runScriptreturn(String Script )
	{ 
		String reust ;
		L.LdoString("androidvalues = " + Script);
		 L.getGlobal("androidvalues");
		reust = L.toString(-1); 
		 
		return reust; 
	 }
		
	///行运函数
	void runScriptFunction(String functionName,Object obj)
	{  L.getGlobal(functionName);  
	   L.pushJavaObject(obj);  
	   L.call(1,0);  
	 } 
	
	///关闭
	public static void closeScript(){  //L.close();  
	}  
	
	private static  String readAll(InputStream input) throws Exception { 
		  ByteArrayOutputStream bo = new ByteArrayOutputStream(); 
		  int i = input.read(); 
		  while (i != -1) { 
			  bo.write(i); 
			  i = input.read(); 
		  }
		  return bo.toString(); 
	} 

}
