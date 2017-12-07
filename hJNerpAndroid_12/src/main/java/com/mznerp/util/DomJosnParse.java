package com.mznerp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class DomJosnParse {

	/**
     * ~{=bNv~}
     * 
     * @throws JSONException
     */
    public static ArrayList<LinkedHashMap<String, String>> Analysis(String jsonStr)
            throws JSONException {
        /******************* 解析***********************/
        JSONArray jsonArray = null;
        // 初始化list数组对象
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i); 
         // 初始化map数组对象 
            Iterator<?> it = jsonObject.keys();  
              String aa2 = "";  
              String bb2 = null; 
            
             LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
             while(it.hasNext()){//遍历JSONObject 
                 bb2 = (String) it.next().toString();  
                 aa2 =   jsonObject.getString(bb2);   
                
                 map.put(bb2, aa2);
                 
              }    
             list.add(map);
        }
        return list;
    }

    
    
    ////~{V;=bNvR;PP~}
    public static HashMap<String, String>  AnalysisMap(String jsonStr)
            throws JSONException {
        /******************* ~{=bNv~} ***********************/
        JSONArray jsonArray = null;
        // ~{3uJ<;/~}list~{J}Wi6TOs~}
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i); 
            // ~{3uJ<;/~}map~{J}Wi6TOs~}  
            Iterator<?> it = jsonObject.keys();  
              String aa2 = "";  
              String bb2 = null; 
            
            HashMap<String, String> map = new HashMap<String, String>();
             while(it.hasNext()){//~{1i@z~}JSONObject  
                 bb2 = (String) it.next().toString();  
                 aa2 =   jsonObject.getString(bb2);   
                
                 map.put(bb2, aa2);
                 
              }    
             list.add(map);
        }
        if(list.size()>0){
        	return list.get(0);
        }
        return new HashMap<String, String>();
    }
    public static String  AnalyMap(Map<String, String> mList) throws JSONException{
    	String jsonString = null;
    
    
    		JSONObject jsonObject = new JSONObject();
    		Set<Entry<String, String>> entry = mList.entrySet(); 
    		Iterator< Entry<String, String>> iter = entry.iterator();
    		while(iter.hasNext()){
    			Entry<String, String> next = iter.next();
    			String key = next.getKey();
    			String value = next.getValue();
    			jsonObject.put(key,value);
//    			Log.e("&&&&&&&&&&&&&&&","key:" + key + " value:" + value );
    		}
  
    	
    	
    	return jsonObject.toString();
    }
    
    ////~{V;=bNvR;PP~}
    public static ArrayList<HashMap<String, String>> AnalysisLine(String jsonStr)
            throws JSONException {
        /******************* ~{=bNv~} ***********************/
        JSONArray jsonArray = null;
        // ~{3uJ<;/~}list~{J}Wi6TOs~}
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < 1; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i); 
            // ~{3uJ<;/~}map~{J}Wi6TOs~}  
            Iterator<?> it = jsonObject.keys();  
              String aa2 = "";  
              String bb2 = null; 
            
            HashMap<String, String> map = new HashMap<String, String>();
             while(it.hasNext()){//~{1i@z~}JSONObject  
                 bb2 = (String) it.next().toString();  
                 aa2 =   jsonObject.getString(bb2);   
                
                 map.put(bb2, aa2);
                 
              }    
             list.add(map);
        }
        return list;
    }
}
