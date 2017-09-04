package com.hjnerp.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.view.HJComboBoxPopWindow;
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
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@SuppressLint("NewApi")
public class HJComboBox extends LinearLayout  implements HJViewInterface{

	private TextView title;
	private Button button;
	private LinearLayout layout;
	private EditText edittext;
	private Context context;
	private BusinessParam businessParam  ; //当前界面上参数
	private WidgetClass items;//<HJList
	private ViewClass currentviewClass;//当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	private View view;
	private HJComboBoxPopWindow boxPopWindow;
	private Ctlm1347  datactlm1347 = new Ctlm1347();
	String value = new String();

	public HJComboBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJComboBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJComboBox(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	public HJComboBox(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo ,BusinessParam param) {
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;  
        this.businessParam = param;
        
        initView();
	}
	
	private void initView() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.layout_hjcombobox, null);

		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		title = (TextView) view.findViewById(R.id.title_hjcombox);
		edittext = (EditText) view.findViewById(R.id.content_hjcombox);
//		button = (Button) view.findViewById(R.id.btn_hjcombox);
		layout = (LinearLayout) view.findViewById(R.id.ll_hjcombobox);
		
		title.setTextSize(18);
		title.setText(items.name);
		setHJTextViewAttribute(items.attribute);
		edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("XX"," filed is " + items.attribute.field);
				showHJComboBoxPopWindow(items.attribute.datasource,items.attribute.field,value);
			}
		});
		addView(view);
		saveDefaultCTLM1347();
	}
	
	public void showHJComboBoxPopWindow(String datasource,String field,String mvalue){
		boxPopWindow = new HJComboBoxPopWindow(context, onCheckedChangeListener,datasource,field,mvalue);
		boxPopWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			View view = boxPopWindow.getContentView();
			int radioButtonId = group.getCheckedRadioButtonId();

			// 根据ID获取RadioButton的实例
			RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rbtn_worktype);
			RadioButton rb = (RadioButton) radioGroup.findViewById(radioButtonId);
			value = rb.getTag().toString();
			
			edittext.setText(value);
			boxPopWindow.dismiss();

			
		}
	};
	
	private void setTitle(String tag) {
		Paint paint = title.getPaint();
		paint.setFakeBoldText(false);
		title.setText(tag);
		title.setTextSize(18);
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
		datactlm1347.setId_nodetype(WidgetName.HJ_COMBOBOX);
//
		datactlm1347.setFlag_upload("N");
		datactlm1347.setVar_data1(value); 
        datactlm1347.setId_parentnode(businessParam.getIdParentNode()); 
        datactlm1347.setId_view(businessParam.getViewId());
// 
        if ( StringUtil.isNullOrEmpty(datactlm1347.getId_node()) )
		{  
		   datactlm1347.setId_node(StringUtil.getMyUUID());
		}

        datactlm1347.setInt_line(0);
        datactlm1347.setId_table(  items.attribute.datasource);
        datactlm1347.setId_srcnode(items.id); 
        datactlm1347.setDate_opr(businessParam.getDataOpr()); 
		BusinessBaseDao.replaceCTLM1347(datactlm1347);

	}
	
	
	private void setHJComboBoxnAttribute(WidgetAttribute attribute) {

	}

	private void setHJComboBoxDataResource(
			List<WidgetClass> hJRadioButtonOption) {
		List<String> list = new ArrayList<String>();
		for(int i = 0;i<3;i++)
			list.add(i+"");
//		content.setAdapter(new MyAdapter(hJRadioButtonOption));
	}

	public void setAttribute(WidgetClass widget) {
		setTitle(widget.name);
		setHJComboBoxnAttribute(widget.attribute);
		setHJComboBoxDataResource(widget.HJRadioButtonOption);
	}
	
	private void setHJTextViewAttribute(WidgetAttribute attribute) {
		Paint paint = edittext.getPaint();
		paint.setFakeBoldText(attribute.bold);
		if ("medium".equalsIgnoreCase(attribute.fontsize)) {
			edittext.setTextSize(18);
		}
		if ("large".equalsIgnoreCase(attribute.fontsize)) {
			edittext.setTextSize(22);
		}
		if ("small".equalsIgnoreCase(attribute.fontsize)) {
			edittext.setTextSize(14);
		}
		edittext.setSingleLine(attribute.singleline);
		if (!attribute.editable) {
			edittext.setFocusable(false);
		}
		
		if ("left".equalsIgnoreCase(attribute.alignment)    )
		{
			edittext.setGravity(Gravity.LEFT);
		}
		if ( "right".equalsIgnoreCase(attribute.alignment) )
		{
			edittext.setGravity(Gravity.RIGHT); 
		}
		if ( "center".equalsIgnoreCase(attribute.alignment) )
		{
			edittext.setGravity(Gravity.CENTER); 
		}
		
		if (  attribute.singleline  &&  attribute.width  > 0   )
		{  int  iPixels ;
			DisplayMetrics dm2 = getResources().getDisplayMetrics();
			iPixels = (int) (dm2.widthPixels  *  (1 -  attribute.width));
			title.setWidth(iPixels);
		}
		
		if ("string".equalsIgnoreCase(attribute.valuetype ))
		{
			edittext.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		if ("phonenumber".equalsIgnoreCase(attribute.valuetype ))
		{
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			
		}
		if ("integer".equalsIgnoreCase(attribute.valuetype ))
		{
			edittext.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);  
		}
		if ("password".equalsIgnoreCase(attribute.valuetype ))
		{
			edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); 
		}
		if ("decimal".equalsIgnoreCase(attribute.valuetype ))
		{
			edittext.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);  
			
	 	} 
		if ("backagroundcolor".equalsIgnoreCase(attribute.backgroundcolor ))
		{
			edittext.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		
		if ("textcolor".equalsIgnoreCase(attribute.textcolor ))
		{
			edittext.setTextColor(Color.parseColor( attribute.textcolor ));
		} 
		  
	}
	



	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		if (StringUtil.isNullOrEmpty( msg) ) 
		{
			msg = "";
		}
		edittext.setText(msg );   
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
		setValue(value);
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return edittext.getText().toString();
	}



	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return "1";
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
		if (flag)
		{  
			 ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao. 
					 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
			 
			  if (ctlm1347List != null && ctlm1347List.size()>0 )
			  {
				  setValue(ctlm1347List.get(0).getVar_data1()); 
			  }
		} else if (ctlm1345List != null  && !flag )
				
		{
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
		return  edittext.getText().toString();
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
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}
