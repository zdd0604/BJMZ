package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetAttribute;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.HJDataPickerInterface;
import com.hjnerp.util.DatePickDialogUtil;
import com.hjnerp.util.DateTimePickDialogUtil;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HJDatePicker extends LinearLayout  implements HJViewInterface {
	private String TAG = "HJDatePicker";
	private String ID; 
	private WidgetClass items;// <HJList 或者<HJTextView
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;   
	private BusinessParam businessParam  ; //当前界面上参数
	private Ctlm1347  datactlm1347 = new Ctlm1347();
	private View view;
	private Context context;
	private EditText editText_time;
	TextView title;
	private String datetime = "";
	public HJDatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJDatePicker(Context context, AttributeSet attrs ) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public HJDatePicker(Context context ) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public HJDatePicker(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param ) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
	    this.businessParam = param;
	    this.context = context;
	    initView();
	    setHJTextViewAttribute(items.attribute); 
//	    saveDefaultCTLM1347();
 
	    
	}
	private void initView() {
	    view = LayoutInflater.from(getContext()).inflate(R.layout.layout_hjdatepickerview, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		title = (TextView)view.findViewById(R.id.hjtv_datepicker_title);
		editText_time = (EditText)view.findViewById(R.id.hjtv_et_datepicker_content);
		
		title.setText(items.name);
		editText_time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("date".equalsIgnoreCase(items.attribute.valuetype)) {
					DatePickDialogUtil dateTimePicKDialog = new DatePickDialogUtil(context, "");
					dateTimePicKDialog.dateTimePicKDialog();

					dateTimePicKDialog.setCallBack(new HJDataPickerInterface() {

						@Override
						public void donePicker(String data) {
							 
							datetime = data;
							editText_time.setText(data);
//							saveCTLM1347();
						}
					});
				} else if ("datetime"
						.equalsIgnoreCase(items.attribute.valuetype)) {
					DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
							context, "");
					dateTimePicKDialog.dateTimePicKDialog();

					dateTimePicKDialog.setCallBack(new HJDataPickerInterface() {

						@Override
						public void donePicker(String data) {
							Log.e(TAG, ">>>>>>>>> data is " + data);
							datetime = data;
							editText_time.setText(data);
//							saveDefaultCTLM1347();
						}
					});
				}

			}
		});
		addView(view); 
	}
	
	private void setTitle(String tags) {
		title.setText("");
		title.append(Html.fromHtml(tags  ));
		Paint paint = title.getPaint();
		paint.setFakeBoldText(false);
		title.setTextSize(18);
		title.setSingleLine(false);
	}
	private void setHJTextViewAttribute(WidgetAttribute attribute) {
		Paint paint = editText_time.getPaint();
		paint.setFakeBoldText(attribute.bold);
		if ("medium".equalsIgnoreCase(attribute.fontsize)) {
			editText_time.setTextSize(18);
		}
		if ("large".equalsIgnoreCase(attribute.fontsize)) {
			editText_time.setTextSize(22);
		}
		if ("small".equalsIgnoreCase(attribute.fontsize)) {
			editText_time.setTextSize(14);
		}
		editText_time.setSingleLine(attribute.singleline);
		if (!attribute.editable) {
			editText_time.setFocusable(false);
		}
		
		if ("left".equalsIgnoreCase(attribute.alignment)    )
		{
			editText_time.setGravity(Gravity.LEFT);
		}
		if ( "right".equalsIgnoreCase(attribute.alignment) )
		{
			editText_time.setGravity(Gravity.RIGHT); 
		}
		if ( "center".equalsIgnoreCase(attribute.alignment) )
		{
			editText_time.setGravity(Gravity.CENTER); 
		}
		
		if (  attribute.singleline  &&  attribute.width  > 0   )
		{  int  iPixels ;
			DisplayMetrics dm2 = getResources().getDisplayMetrics(); 
			iPixels = (int) (dm2.widthPixels  *  (1 -  attribute.width)); 
			title.setWidth(iPixels);
		}
		
		if ("string".equalsIgnoreCase(attribute.valuetype ))
		{
			editText_time.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		if ("phonenumber".equalsIgnoreCase(attribute.valuetype ))
		{
			editText_time.setInputType(InputType.TYPE_CLASS_PHONE);
			
		}
		if ("integer".equalsIgnoreCase(attribute.valuetype ))
		{
			editText_time.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);  
		}
		if ("password".equalsIgnoreCase(attribute.valuetype ))
		{
			editText_time.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
		}
		if ("decimal".equalsIgnoreCase(attribute.valuetype ))
		{
			editText_time.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);  
			
	 	} 
		if ("backagroundcolor".equalsIgnoreCase(attribute.backgroundcolor ))
		{
			editText_time.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		
		if ("textcolor".equalsIgnoreCase(attribute.textcolor ))
		{
			editText_time.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		  
		if (attribute.visible)
		{
			this.setVisibility(View.VISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
		
		if  ( items.attribute.required )
		   {
			setTitle("<font size=1 color =#000000>"+items.name+":</font>"+"<font color =#ff0000>*</font>" );
			} 
		else
			{setTitle (items.name+":");
			} 
	}
	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}

	public void saveCTLM1347() { 
		datactlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		datactlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
 	   datactlm1347.setName_node(items.name);
		datactlm1347.setVar_billno(businessParam.getBillNo()  );
		datactlm1347.setId_model(startViewInfo.id);
		datactlm1347.setId_nodetype(WidgetName.HJ_DATE_PICKER);
//
		datactlm1347.setFlag_upload("N");
		datactlm1347.setVar_data1(datetime); 
        datactlm1347.setId_parentnode(businessParam.getIdParentNode()); 
        datactlm1347.setId_view(businessParam.getViewId());
// 
        if ( StringUtil.isNullOrEmpty(datactlm1347.getId_node()) )
		{  
		   datactlm1347.setId_node(StringUtil.getMyUUID());
		}
      ///保存JSOn值  
        if ( !StringUtil.isNullOrEmpty(items.attribute.datasource ))
        {
        	String jsonvalues;
            jsonvalues = datactlm1347.getvar_Json();
            if ( !StringUtil.isNullOrEmpty(jsonvalues) ) 
            { 
            	Map<String, Object> map = null;  
			    Gson gson = new Gson(); 
			    map = (Map<String, Object>)gson.fromJson(jsonvalues, Object.class); 
			    map.put(items.attribute.field , datetime); 
			    datactlm1347.setvar_Json(gson.toJson(map ));
			     
            }
            else
            {
            	datactlm1347.setvar_Json(  "{\""+items.attribute.field+"\":\""+datetime+"\"}"); 
            }
            
        }
        else
        {
        	datactlm1347.setvar_Json("");
        }
        datactlm1347.setInt_line(0);
        datactlm1347.setId_table(  items.attribute.datasource);
        datactlm1347.setId_srcnode(items.id); 
        datactlm1347.setDate_opr(businessParam.getDataOpr()); 
		BusinessBaseDao.replaceCTLM1347(datactlm1347);

	}
	
	
	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty( msg) ) 
		{
			msg = "";
		}
		datetime = msg;
		editText_time.setText(msg);
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		Object  defaultValue ;
		defaultValue = items.attribute.defaultvalue; 
		///如果默认值不为空，则直接加默认值
		if (defaultValue != null && !StringUtil.isNullOrEmpty(defaultValue.toString()))
 		   {
			setValue( defaultValue.toString() );
 		 } 
	}

 
	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return items.attribute.datasource;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return items.id;
	}

	@Override
	public boolean getEditable() {
		// TODO Auto-generated method stub
		return items.attribute.editable;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return editText_time.getText().toString();
	}

	 

	 

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return  "0" ;
	}

	@Override
	public void setDataBuild(Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		 if(   flag ){ 
			  
			  ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao. 
							 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
					 
			  if (ctlm1347List != null  && ctlm1347List.size() >0 )
			  {
					if (ctlm1347List == null) return ;
					datactlm1347 = ctlm1347List.get(0); 
					setValue(datactlm1347.getVar_data1());
			  }
			 
			}else  { 
				//setctlm1345(ctlm1345List.getVar_values(),"N");
				if  (ctlm1345List.getCtlm1345().size() <=0 ) return ; 
				datactlm1347.setInt_line(0);
				if ( StringUtil.isNullOrEmpty(datactlm1347.getId_node()) )
				{
				datactlm1347.setId_node( StringUtil.getMyUUID()); 
				}
				datactlm1347.setId_table(items.attribute.datasource);  
				///设置显示 值
				setJesonValue( ctlm1345List.getCtlm1345().get(0).getVar_value());
			}
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return editText_time.getText().toString();
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setLocation() {
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
		
		return 0; 
	}

 

	@Override
	public void setJesonValue(String jsonValue) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Map<String, Object> map = null;  
			    Gson gson = new Gson();
			    map = (Map<String, Object>)gson.fromJson(jsonValue, Object.class); 
				if (map != null && !map.isEmpty() )
				{
					datactlm1347.setvar_Json( jsonValue);  
					setValue(map.get( items.attribute.field).toString());//   content.setText( list.get(0).get( items.attribute.field)); 
					
				}
	}

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validate() {
		if (items.attribute.required)
		{  
			 if (StringUtil.isNullOrEmpty(datetime))
			 {
				 ToastUtil.ShowShort(getContext(),  items.name + "不能为空,请输入.");
				 return false;
			 }
		}
		
		return true;
	}
}
