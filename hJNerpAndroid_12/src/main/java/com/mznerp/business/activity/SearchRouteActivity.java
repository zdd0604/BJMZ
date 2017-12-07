package com.mznerp.business.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.Ctlm1345;
import com.mznerp.net.HttpClientBuilder;
import com.mznerp.net.HttpClientBuilder.HttpClientParam;
import com.mznerp.net.HttpClientManager;
import com.mznerp.net.HttpClientManager.HttpResponseHandler;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.myscom.StringUtils;
import com.mznerp.widget.WaitDialog;
import com.mznerp.R;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
public class SearchRouteActivity extends ActivitySupport implements
		OnClickListener {
	private RelativeLayout actionbar_back; 
	private ListView listview ; 
	private String flag;
	private Object data;
	private WaitDialog dialog;
	private String idTables;
	private String resultFromServer;  
	private String condition;
	
	private TextView textView;
	private LinearLayout buLinearLayout; 
	private HttpResponseHandler responseHandler = new NsyncDataHandler();

	public static final String BUSINESS_SERVICE_ADDRESS = "/servlet/businessMobileServlet";
	public static final String NBUSINESS_SERVICE_ADDRESS = "/servlet/nbusinessMobileServlet";
	public static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	 
	public static final String JSON_VALUE = "values";
	public static final int  MSG_SHOW_LIST = 1;
	public static final int MSG_FAILED = 2;
 
	
	public SearchRouteAdapter listadapter = null;
	public ArrayList<Ctlm1345> ctlm1345List = new ArrayList<Ctlm1345>() ;
	
	public ArrayList<Ctlm1345> ctlm1345result = new ArrayList<Ctlm1345>();
	public Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();  
	
	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_LIST:
				dialog.dismiss();
				listadapter.refreshList(ctlm1345List, isSelected);
				break;
			case MSG_FAILED:
				dialog.dismiss();
				msg.obj ="";
				showGridLayout(msg);
				ToastUtil.ShowLong(SearchRouteActivity.this, "数据下载出现错误！");
				break;
			default:
				break;
			}
			
		};
	};
	
//	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		getActionBar().hide();
		this.getSupportActionBar().hide();
		setContentView(R.layout.layout_search_route);
		actionbar_back = (RelativeLayout) findViewById(R.id.rl_actionbar_back);
		actionbar_back.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.tv_actionbar_title);
		textView.setText("查询");
		buLinearLayout = (LinearLayout) findViewById(R.id.businessmenu_btn);
		
		TextView  textview =  new TextView(this); 
		textview.setText("确认"); 
		LayoutParams lpd =  new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpd.setMargins(10, 0, 10, 0);
		textview.setLayoutParams(lpd);
		textview.setTextSize(18); 
		textview.setTextColor(Color.parseColor("#303030"));
		
//		Button btn = new Button(this);
//		btn.setText("确认"); 
		
		buLinearLayout.setOnClickListener(this);
		buLinearLayout.addView(textview);
		///得到返回的List
		listview = (ListView) findViewById(R.id.business_search_list);
		
		listadapter =  new SearchRouteAdapter( this,null ,null); 
		
		listview.setAdapter(listadapter );
		listview.setOnItemClickListener(new OnItemClickListener(){ 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (isSelected.containsKey(arg2)&&isSelected.get(arg2))
				 { isSelected.put(arg2 , false);}
				else
				{ isSelected.put(arg2 , true); }
				 listadapter.refreshList(ctlm1345List,isSelected);
			}
			
		});
		
		actionbar_back.setOnClickListener(this); 
		Intent intent = getIntent();
		idTables = getIntent().getStringExtra(AddRouteActivity.DATA_RESOURCE);   
		condition= getIntent().getStringExtra(AddRouteActivity.SERACH_BY_CONDITION);
		processData(intent);
	}
	
	 protected void showGridLayout(Message msg) {
//		try {
//			String data = (String) msg.obj;
//			data = "[{\"id\":\"代码\",\"name\":\"名称\"},{\"id\":\"001\",\"name\":\"小吃店\",\"id_type\":\"小型店\"}," +
//					"{\"id\":\"002\",\"name\":\"小吃店\",\"id_type\":\"小型店\"}]";
////			WidgetClass widgetItems = new WidgetClass();
//			widgetItems.widgetType = WidgetName.HJ_GRID;
//			widgetItems.attribute = new WidgetAttribute();
//			widgetItems.attribute.fontsize="medium";
//			widgetItems.attribute.locking=1;
//			//grid.setCheckBoxShowing(true);
//			//解析json数据获取列的属性
//			List<WidgetClass> columProperties = new ArrayList<WidgetClass>();
//			JSONArray jsonArray = new JSONArray(data);
//			//获取列的数目和属性
//			JSONObject jsonObject = jsonArray.getJSONObject(0);
//			Iterator<String> iter = jsonObject.keys();
//			while(iter.hasNext()) {
//				String text = iter.next();
//				WidgetClass subWidgetClass = new WidgetClass();
//				subWidgetClass.attribute = new WidgetAttribute();
//				subWidgetClass.attribute.editable = false;
//				subWidgetClass.attribute.field = text;
//				subWidgetClass.attribute.fontsize = "medium";
//				subWidgetClass.attribute.width = 1/(float)jsonObject.length();
//				subWidgetClass.name = jsonObject.getString(text);
//				columProperties.add(subWidgetClass);
//			}
//			widgetItems.HJRadioButtonOption = columProperties;
////			grid.setAttribute(widgetItems);
//			
//			result4List = data.substring(data.indexOf("}")+2);
//			grid.setDataResource("["+result4List);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

 

	private void processData(Intent intent) {
		flag = intent.getStringExtra(AddRouteActivity.SEARCH_FLAG);
		if (AddRouteActivity.SEARCH_BY_BDLOCATION.equals(flag)) {
			// 百度定位扫描查找
			data = intent.getDoubleArrayExtra(AddRouteActivity.SEARCH_PARAMAS);
			searchRouteByBDLocation((double[]) data);
			return;
		}
		if (AddRouteActivity.SERACH_BY_INPUT.equals(flag)) {
			// 根据输入框内容查找
			data = intent.getStringExtra(AddRouteActivity.SEARCH_PARAMAS);
			searchRouteByInputText((String) data);
			return;
		}
		if (AddRouteActivity.SEARCH_BY_QWCODE.equals(flag)) {
			// 根据扫描二维码查找
			data = intent.getStringExtra(AddRouteActivity.SEARCH_PARAMAS);
//			searchRouteByQWCode("id_column='"+(String) data +"'");
			searchRouteByInputText((String) data);
			return;
		}
	}

	private void searchRouteByQWCode(Object data2) {
		// TODO Auto-generated method stub

		dialog = new WaitDialog(this);
		dialog.show();
		
		if (TextUtils.isEmpty(data2.toString())) { 
			dialog.dismiss();
			this.finish();
			return ;
		}
		getSyncMenus(" "+condition + " and  var_value like '%"+data2.toString()+"%'"+" and id_table = '"+idTables+"'" );
	}

	private void searchRouteByInputText(String data2) {
		dialog = new WaitDialog(this);
		dialog.show();
		if (TextUtils.isEmpty(data2)) { 
			dialog.dismiss();
			this.finish();
			return ;
		} 
		getSyncMenus(" "+condition + " and  var_value like '%"+data2.toString()+"%'"+" and id_table = '"+idTables+"'" ); 
	}
	
	private void searchRouteByBDLocation(double[] data2) {
		dialog = new WaitDialog(this);
		dialog.show();
		String var_rlati = String.valueOf(data2[0]);
		String var_rlongi = String.valueOf(data2[1]);

		if (TextUtils.isEmpty(var_rlati)||TextUtils.isEmpty(var_rlongi)) { 
			dialog.dismiss();
			this.finish();
			return ;
		} 
		StringBuffer bf = new StringBuffer();
		bf.append(" "+ condition + " and   " );
		bf.append("var_rlati = '"+var_rlati+"'" );
		bf.append(" and ");
		bf.append("var_rlongi = '"+var_rlongi+"'");
		bf.append(" and ");
		bf.append("id_table = '"+idTables+"' ");
		 
		getSyncMenus(bf.toString());
	}
	
	private void getSyncMenus( String parm ) {
		try {
//			HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
//							+ EapApplication.PATH_HTTP_SERVER_PROJECT_ROOT
//							+ NBUSINESS_SERVICE_ADDRESS);
//			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//			parameters.add(new BasicNameValuePair(Constant.BM_ACTION_TYPE,
//					"MobileSyncDataDownload")); 
//			parameters.add(new BasicNameValuePair("id_table",   idTables
//					));
//			parameters.add(new BasicNameValuePair("condition", parm));
//			parameters.add(new BasicNameValuePair(
//					ChatConstants.iq.DATA_KEY_USER_ID, EapApplication.getApplication().getExtra(EapApplication.EXTRA_USER_ID).toString()   ));
//			parameters.add(new BasicNameValuePair(
//					ChatConstants.iq.DATA_KEY_COM_ID, EapApplication.getApplication().getExtra(EapApplication.EXTRA_COM_ID).toString()));
//			parameters.add(new BasicNameValuePair(
//					ChatConstants.iq.DATA_KEY_SESSION_ID,
//					 ActivitySupport.sputil.getMySessionId()  ));
//			httpPost.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
//			 
			HttpClientParam  param = HttpClientBuilder.createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
			param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
			.addKeyValue("id_table", StringUtils.join(idTables, ":")).addKeyValue("condition",parm);
	 
			HttpClientManager.addTask(responseHandler, param.getHttpPost());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_actionbar_back:
			finish();
			break;
		case  R.id.businessmenu_btn:
			//进行数据库的插入 
			ctlm1345result.clear();
			for(Map.Entry<Integer, Boolean> entry:isSelected.entrySet()){
				if(entry.getValue()){
					ctlm1345result.add(ctlm1345List.get(entry.getKey()));
				}
			}
		

			if (ctlm1345result.size()==0){
				return ;
			}
			
			if(ctlm1345result.size()!=0){
				BusinessBaseDao.replaceCTLM1345s(ctlm1345result);
			} 
			
			String  result = "";
//			
			for (Ctlm1345 ctlm1345: ctlm1345result)
			{
				result = result + ctlm1345.getVar_value()  +"," ;
			}
			result = result.substring(0,result.length() -1 ) ;
			result = "[" + result +"]"; 
			Intent intent = new Intent(this,AddRouteActivity.class); 
			Bundle bundle = new Bundle();  
			bundle.putString("result", result);  
			intent.putExtras(bundle);  

			setResult(1002, intent);//传确定信息，和参数
			
			this.finish();
			break;
		default:
			
		}

	}
	
	private String processBusinessCompress(String fileName) {
		// TODO Auto-generated method stub
		ZipInputStream zis = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			File f = new File(getExternalCacheDir(), fileName);
			FileInputStream fis = new FileInputStream(f);
			zis = new ZipInputStream(fis);
			ZipEntry zip = zis.getNextEntry();
			int len = 0;
			while ((len = zis.read(data)) != -1) {
				baos.write(data, 0, len);
			}
			String json = new String(baos.toByteArray(), HTTP.UTF_8);
			return json;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (zis != null) {
					zis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private class NsyncDataHandler extends HttpResponseHandler {

		@Override
		public void onException(Exception e) {
			handler.sendEmptyMessage(MSG_SHOW_LIST);
		}

		@Override
		public void onResponse(HttpResponse resp) {
			// TODO Auto-generated method stub
			try {
				String contentType = resp.getHeaders("Content-Type")[0]
						.getValue();
//				if ("application/octet-stream".equals(contentType)    ) {
			if (contentType.indexOf("application/octet-stream" )  != -1  ) {
					String contentDiscreption = resp
							.getHeaders("Content-Disposition")[0].getValue();
					String fileName = contentDiscreption
							.substring(contentDiscreption.indexOf("=") + 1);
					FileOutputStream fos = new FileOutputStream(new File(
							getExternalCacheDir(), fileName));
					resp.getEntity().writeTo(fos);
					fos.close();
					String json = processBusinessCompress(fileName);
//					Log.e(TAG,"the json value is "+json);
					//开始生成表格
					JSONObject jsonObject = new JSONObject(json);
					String value = jsonObject.getString(JSON_VALUE);
				    processJsonValue(value);
					//开始生成表格
					Message msg = handler.obtainMessage(MSG_SHOW_LIST);
					msg.obj = resultFromServer;
					handler.sendMessage(msg);
				} else {
//					Log.e(TAG,"the json is "+ HttpClientManager.toStringContent(resp));
					handler.sendEmptyMessage(MSG_FAILED);
				}

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void processJsonValue(String value) throws JSONException {
		JSONArray jsonArray = new JSONArray(value);
		//添加相关数据,一行只有一个数据
		for(int i = 0;i<jsonArray.length();i++) {
			Ctlm1345 ctlm1345 = new Ctlm1345();
			//添加表头
			String temp = jsonArray.getString(i);
			
			Matcher m = p.matcher(temp);
			temp = m.replaceAll("");
			ctlm1345.setVar_valname(temp.substring(temp.lastIndexOf("{"), temp.lastIndexOf("}")+1));
		 	//添加值
			String subValue = temp.substring(temp.indexOf("{"), temp.indexOf("}")+1);
			ctlm1345.setVar_value(subValue);
		 	temp = temp.replace(ctlm1345.getVar_valname(), "");
			temp = temp.replace(ctlm1345.getVar_value(), "");
			String []deStrings = temp.split(",");
			for(int k = 0;k<deStrings.length;k++){
				getCtlm1345ByStringsArray(ctlm1345,k,deStrings[k]);
			}
			ctlm1345List.add(ctlm1345);
		}
//		handler.sendEmptyMessage(MSG_SHOW_LIST);
	}
   
	private void getCtlm1345ByStringsArray(Ctlm1345 ctlm1345, int k,String s) {
		// 对应的字符串
		// "id_table, id_recorder, id_com, line_no, id_column, name_column, var_value, var_image, var_condition, flag_download, var_rlati, var_rlongi, var_valname",
		s=s.replaceAll("'", "");
		switch (k) {
		case 0: // id_table
			ctlm1345.setId_table(s);
			break;
		case 1: // id_recorder
			ctlm1345.setId_recorder(s);
			break;
		case 2: // id_com
			ctlm1345.setId_com(s);
			break;
		case 3: // line_no
			ctlm1345.setLine_no(s);
			break;
		case 4: // id_column
			ctlm1345.setId_column(s);
			break;
		case 5: // name_column
			ctlm1345.setName_column(s);
			break;
		case 6: //var_value
			break;
		case 7: // var_image
			ctlm1345.setVar_image(s);
			break;
		case 8: // var_condition
			ctlm1345.setVar_condition(s);
			break;
		case 9: // flag_download
			ctlm1345.setFlag_download(s);
			break;
		case 10: // var_rlati
			ctlm1345.setVar_lati(s);
			break;
		case 11:// var_rlongi
			ctlm1345.setVar_longi(s);
			break;
		case 12: //var_valname
			break;
		default:
			break;
		}
		
	}
	
	
	
	
	 

}
