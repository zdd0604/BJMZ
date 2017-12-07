package com.mznerp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

//@SuppressLint("NewApi")
public class HJTextView extends LinearLayout  implements HJViewInterface{
 
	private TextView title;
	private EditText content,content_vertical;
	private String ID; 
	private WidgetClass items;// <HJList 或者<HJTextView
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;   
	private BusinessParam businessParam  ; //当前界面上参数
	private Ctlm1347  datactlm1347 = new Ctlm1347();
	
	String data =""; 
	 	
	public HJTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		initView();
	}

	public HJTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		.initView();
	}

	public HJTextView(Context context) {
		super(context);
//		initView();
//		setValueDefault();
	}

	public HJTextView(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param ) {
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
	    this.businessParam = param;
 
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
		datactlm1347.setId_nodetype(WidgetName.HJ_TEXTVIEW);
//
		datactlm1347.setFlag_upload("N");
		datactlm1347.setVar_data1(data); 
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
			    map.put(items.attribute.field , data); 
			    datactlm1347.setvar_Json(gson.toJson(map ));
			     
            }
            else
            {
            	datactlm1347.setvar_Json(  "{\""+items.attribute.field+"\":\""+data+"\"}"); 
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

	private void initView() {
//		View view = LayoutInflater.from(getContext()).inflate(
//				R.layout.layout_hjtextview, null);
//		addView(view);
//		title = (TextView) view.findViewById(R.id.hjtv_et_title);
//		content = (EditText) view.findViewById(R.id.hjtv_et_content);
	}

	public String getHJId() {
		return ID;
	}

	/**
	 * set the textview show
	 * 
	 * @param tags
	 *            the textview title
	 */
	private void setTitle(String tags) {
		title.setText("");
		title.append(Html.fromHtml(tags  ));
		Paint paint = title.getPaint();
		paint.setFakeBoldText(false);
		title.setTextSize(18);
		title.setSingleLine(false);
	}

	private void setHJTextViewAttribute(WidgetAttribute attribute) {
		Paint paint = content.getPaint();
		paint.setFakeBoldText(attribute.bold);
		if ("medium".equalsIgnoreCase(attribute.fontsize)) {
			content.setTextSize(18);
		}
		if ("large".equalsIgnoreCase(attribute.fontsize)) {
			content.setTextSize(22);
		}
		if ("small".equalsIgnoreCase(attribute.fontsize)) {
			content.setTextSize(14);
		}
//		content.setSingleLine(attribute.singleline);
		if (!attribute.editable) {
			content.setFocusable(false);
		}
		
		if ("left".equalsIgnoreCase(attribute.alignment)    )
		{
			content.setGravity(Gravity.LEFT);
		}
		if ( "right".equalsIgnoreCase(attribute.alignment) )
		{
			content.setGravity(Gravity.RIGHT); 
		}
		if ( "center".equalsIgnoreCase(attribute.alignment) )
		{
			content.setGravity(Gravity.CENTER); 
		}
		
		if (  attribute.singleline  &&  attribute.width  > 0   )
		{  int  iPixels ;
			DisplayMetrics dm2 = getResources().getDisplayMetrics(); 
			iPixels = (int) (dm2.widthPixels  *  (1 -  attribute.width)); 
			title.setWidth(iPixels);
		}
		
		if ("string".equalsIgnoreCase(attribute.valuetype ))
		{
			content.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		}
		if ("phonenumber".equalsIgnoreCase(attribute.valuetype ))
		{
			content.setInputType(InputType.TYPE_CLASS_PHONE|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			
		}
		if ("integer".equalsIgnoreCase(attribute.valuetype ))
		{
			content.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL|InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
		}
		if ("password".equalsIgnoreCase(attribute.valuetype ))
		{
			content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_TEXT_FLAG_MULTI_LINE); 
		}
		if ("decimal".equalsIgnoreCase(attribute.valuetype ))
		{
			content.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
			
	 	} 
		if ("backagroundcolor".equalsIgnoreCase(attribute.backgroundcolor ))
		{
			content.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		
		if ("textcolor".equalsIgnoreCase(attribute.textcolor ))
		{
			content.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		  
		if (attribute.visible)
		{
			this.setVisibility(View.VISIBLE);
		}
		else
		{
			this.setVisibility(View.GONE);
		}
	}
 

	public String getText() {
		return content.getText().toString();
	}
	
	@SuppressLint("InflateParams") public void setAttribute(WidgetClass widget) {
		int  lengthlimit = 0; 
		String   format = "";
		String valuetype = ""; 
		if(widget.attribute.valuetype != null)
			valuetype = widget.attribute.valuetype.toString();
		
		if(widget.attribute.format != null)
			format = widget.attribute.format.toString();
		
		lengthlimit = widget.attribute.lengthlimit ;
		
		if(StringUtil.isNullOrEmpty(format)  ) format = "";
		if(StringUtil.isNullOrEmpty(valuetype)  ) valuetype = "";
		
		if (!widget.attribute.singleline) {
		 
			View view = LayoutInflater.from(getContext()).inflate(
					R.layout.layout_hjtext_vertical, null);
			addView(view);   
			title = (TextView) view.findViewById(R.id.hjtv_ve_title);
			content = (EditText) view.findViewById(R.id.hjtv_ve_content); 
			
			if (lengthlimit ==0){
				lengthlimit =500;
			} 
			 content.addTextChangedListener( new  HJEditTextWatcher( content ,lengthlimit ,"", "",callHandler ));
			 content.setOnFocusChangeListener(new OnFocusChangeListenerImp()) ;
		}
		else
		{
			View view = LayoutInflater.from(getContext()).inflate(
					R.layout.layout_hjtextview, null);
			addView(view);
			title = (TextView) view.findViewById(R.id.hjtv_et_title);
			content = (EditText) view.findViewById(R.id.hjtv_et_content);
			if (lengthlimit ==0){
				if  (  "phoneNumber".equalsIgnoreCase(valuetype) ) lengthlimit = 11; 	 
				if  ( "integer".equalsIgnoreCase(valuetype)  ) lengthlimit = 10;
				if  ( "decimal".equalsIgnoreCase(valuetype)  ) lengthlimit = 18;  
				if  ( !"decimal".equalsIgnoreCase(valuetype) &&  !"phoneNumber".equalsIgnoreCase(valuetype) && !"integer".equalsIgnoreCase(valuetype) ) lengthlimit = 120;  
			} 
			content.addTextChangedListener( new  HJEditTextWatcher( content ,lengthlimit ,valuetype, format,callHandler ));
			content.setOnFocusChangeListener(new OnFocusChangeListenerImp()) ;
		}
		 
		if  ( widget.attribute.required )
		   {
			setTitle("<font size=1 color =#000000>"+widget.name+":</font>"+"<font color =#ff0000>*</font>" );
			} 
		else
			{setTitle (widget.name+":");
			} 
			
		setHJTextViewAttribute(widget.attribute); 
		ID = widget.id;
	}
//
  
	@SuppressLint("HandlerLeak")
	private Handler callHandler = new Handler(){
		
	     public void handleMessage(android.os.Message msg) { 
	    	 data = content.getText().toString();
//	         setValue(data); 
			};
			
		};
		
	@Override
	public void setValue(String msg) {
		
		if (StringUtil.isNullOrEmpty( msg) ) 
		{
			msg = "";
		}
		// TODO Auto-generated method stub
		String   format = "";
		String valuetype = "";
		String formatted = "";
		 
		if(items.attribute.valuetype != null)
		valuetype = items.attribute.valuetype.toString();
		
		if(items.attribute.format != null)
		format = items.attribute.format.toString();
		  
		if ( "decimal".equalsIgnoreCase(valuetype) && !StringUtil.isNullOrEmpty( format) && !StringUtil.isNullOrEmpty( msg)  ) 
		{  
			DecimalFormat dformat = new DecimalFormat(format);
		    formatted = dformat.format(  Double.parseDouble(msg )  );  
		}
		else
		{
			formatted = msg;
		}
		data= msg; 
		content.setText( formatted );   
		if (currentviewClass.presave )
		 { 
			saveDefaultCTLM1347();
		 }
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

	@SuppressWarnings("unchecked")
	@Override
	public void setJesonValue(String jsonValue) {
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
	
	public void setctlm1347json( String json  )
	{
		String ls_json ;
		ls_json =  datactlm1347.getvar_Json();
		
	}

	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub v
		return items.attribute.datasource ;
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

	private class OnFocusChangeListenerImp implements OnFocusChangeListener{

		@Override
		public void onFocusChange(View arg0, boolean hasFocus) {
			// TODO Auto-generated method stub 
				data =content.getText().toString();
				setValue(data); 
		}
		
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		setValue(value); 
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return "";
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
		  if(   flag ){ 
			  
			  ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao. 
							 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
					 
			  if (ctlm1347List != null && ctlm1347List.size() > 0 )
			  {
				  setValue(ctlm1347List); 
			  }
			 
			}else  { 
				//setctlm1345(ctlm1345List.getVar_values(),"N");
				setValue( ctlm1345List ) ;
			}
//		
//		if(ctlm1347List != null && ctlm1347List.size() > 0){ 
//			setValue(ctlm1347List); 
//		}else if(ctlm1345List != null &&(ctlm1347List == null || ctlm1347List.size() < 1)){ 
//			//setctlm1345(ctlm1345List.getVar_values(),"N");
//			setValue( ctlm1345List ) ;
//		}
	}
	
	public void setValue(BusinessData ctlm1345List) {
		// TODO Auto-generated method stub 
		if  (ctlm1345List.getCtlm1345().size() <=0 ) return ;
		datactlm1347.setvar_Json(  ctlm1345List.getCtlm1345().get(0).getVar_value());
		datactlm1347.setInt_line(0);
		if    ( StringUtil.isNullOrEmpty(datactlm1347.getId_node()) )
		{
		datactlm1347.setId_node( StringUtil.getMyUUID()); 
		}
		datactlm1347.setId_table(items.attribute.datasource);  
		///设置显示 值
		setJesonValue( ctlm1345List.getCtlm1345().get(0).getVar_value());
		 
		} 
		
	public void setValue(ArrayList<Ctlm1347> ctlm1347List) {
		// TODO Auto-generated method stub  
		if (ctlm1347List == null) return ;
		datactlm1347 = ctlm1347List.get(0);
		setValue(datactlm1347.getVar_data1());
		
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		items.attribute.datasource = Data;
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
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		
		if (items.attribute.required)
		{  
			 if (StringUtil.isNullOrEmpty(data))
			 {
				 ToastUtil.ShowShort(getContext(),  items.name + "不能为空,请输入.");
				 return false;
			 }
		}
		
		return true;
	}
 
}
