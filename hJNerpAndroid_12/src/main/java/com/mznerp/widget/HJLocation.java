package com.mznerp.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mznerp.business.view.StartViewInfo;
import com.mznerp.business.view.ViewClass;
import com.mznerp.business.view.WidgetAttribute;
import com.mznerp.business.view.WidgetClass;
import com.mznerp.business.view.WidgetName;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.BusinessData;
import com.mznerp.model.BusinessParam;
import com.mznerp.model.Ctlm1347;
import com.mznerp.util.BDLocationUtil;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.R;

//@SuppressLint("newapi")
public class HJLocation extends LinearLayout  implements HJViewInterface{
	private static String TAG = "HJLocation";
	
	 
	private WidgetClass items;
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	public Ctlm1347 ctlm1347 = new Ctlm1347() ;
	public BusinessData ctlm1345List;
	private BusinessParam businessParam =  new BusinessParam(); //当前界面上参数
	private String id_node;
	private TextView tv_locatin;
	private ImageView imageView;
	private ProgressBar progressBar; 
	 
	private String locationString;
	
 
	private Handler handler = new Handler(){
		
     public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BDLocationUtil.LOCATION_SUCCESS:
				progressBar.setVisibility(View.INVISIBLE);
				locationString =  msg.obj.toString();  
//				locationString = location.getLatitude() + ":" + location.getLongitude();
				tv_locatin.setText(locationString); 
//				saveCTLM1347();
				saveDefaultCTLM1347(); 
			
			default:
				 
				break;
			}
			
		};
		
	};
	
	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}
	
	public void saveCTLM1347() {

		ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		ctlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
 		if  (StringUtil.isNullOrEmpty(ctlm1347.getId_node()))
		{
			ctlm1347.setId_node(StringUtil.getMyUUID())	;
		}
		ctlm1347.setName_node(items.name);
		ctlm1347.setVar_billno(businessParam.getBillNo());
		ctlm1347.setId_model(businessParam.getModelId());
		ctlm1347.setId_nodetype(WidgetName.HJ_LOCATION);
		ctlm1347.setFlag_upload("N");
		ctlm1347.setId_parentnode(businessParam.getIdParentNode()); 
		ctlm1347.setId_view(businessParam.getViewId() );
		ctlm1347.setInt_line(0);
		ctlm1347.setId_table(items.attribute.datasource); 
		ctlm1347.setId_srcnode(items.id);
		ctlm1347.setDate_opr(businessParam.getDataOpr());  
		ctlm1347.setVar_data1(locationString);  
		
		BusinessBaseDao.replaceCTLM1347(ctlm1347);

	}
	
	
	public HJLocation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJLocation(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJLocation(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public HJLocation(Context context,WidgetClass items,ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param){
		super(context);
		
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.businessParam = param;
		
		initView();
		ctlm1347  = BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(),businessParam.getBillNo(),businessParam.getModelId(),items.id);
		if(ctlm1347 == null){
			ctlm1347 = new Ctlm1347();
		}else{
			locationString = ctlm1347.getVar_data1();
			tv_locatin.setText(locationString);
		}
		
	}
	
	
	private void initView() {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_hjlocation, null);
		progressBar = (ProgressBar) view.findViewById(R.id.pb_waitding);
		progressBar.setVisibility(View.INVISIBLE);
		imageView = (ImageView) view.findViewById(R.id.iv_getlocation);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				int i = progressBar.INVISIBLE;  
				BDLocationUtil.setActivityHandler(handler);
				BDLocationUtil.getLocation(getContext());
				
			}
		});
		view.setLayoutParams(lp);
		addView(view);
		tv_locatin = (TextView) view.findViewById(R.id.tv_location);
		setHJLocationAttribute(items.attribute);
		
	}
	
	/*
	 * 设置edittext属性
	 */
	private void setHJLocationAttribute(WidgetAttribute attribute){	
		Paint paint = tv_locatin.getPaint();
		paint.setFakeBoldText(attribute.bold);
		
//		设置字体大小
		if(getResources().getString(R.string.text_size_small).equalsIgnoreCase(attribute.fontsize)){
			tv_locatin.setTextSize(getResources().getInteger(R.integer.TextSizeSmall));
		}
		if(getResources().getString(R.string.text_size_medium).equalsIgnoreCase(attribute.fontsize)){
			tv_locatin.setTextSize(getResources().getInteger(R.integer.TextSizeMedium));
		}
		if(getResources().getString(R.string.text_size_large).equalsIgnoreCase(attribute.fontsize)){
			tv_locatin.setTextSize(getResources().getInteger(R.integer.TextSizeLarge));
		}
		
		
		if(attribute.visible){
			this.setVisibility(View.VISIBLE);
		}else{
			this.setVisibility(View.GONE);
		}
		
		//是否多行
//		input.setSingleLine(attribute.singleline);
//		input.setSingleLine(true);
		
		
		
		//设置edittext高度和宽度
//		input.setHeight(Integer.parseInt(new java.text.DecimalFormat("0").format(attribute.height)));
//		input.setWidth(Integer.parseInt(new java.text.DecimalFormat("0").format(attribute.width)));
		
	}
	
	
	public void setValue(String msg){
		locationString = msg;
		tv_locatin.setText(msg);
	}
	
	public String getValue(){
		return tv_locatin.getText().toString();
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
//		saveCTLM1347();
//		saveDefaultCTLM1347();
		locationString = items.defaultValue;
		tv_locatin.setText( items.defaultValue);
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return items.id;
	}

	@Override
	public boolean getEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return locationString;
	}

 
 

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public void setDataBuild(Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		if (flag)
		{  
			 ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao. 
					 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
			 
			 if (ctlm1347List != null  && ctlm1347List.size()>0 )
			 {
				 ctlm1347 = ctlm1347List.get(0); 
			     locationString = ctlm1347.getVar_data1();
			     tv_locatin.setText(  ctlm1347.getVar_data1());
			 }
			  
		} 
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String setLocation( ) {
		// TODO Auto-generated method stub 
		 
		return "";  
		
	}


	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub
		return "";
	}


	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		saveCTLM1347();
		return 1; 
	}


 

 
	@Override
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}
