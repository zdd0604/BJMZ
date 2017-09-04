package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.DomJosnParse;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

//@SuppressLint("NewApi")
public class HJCheckBox extends LinearLayout  implements HJViewInterface{
	 
	private Ctlm1347  datactlm1347 = new Ctlm1347();
	private Context context;
	private WidgetClass items;
	ViewClass currentviewClass;
	StartViewInfo startViewInfo;
	private BusinessParam businessParam  ; //当前界面上参数
	
	List<CheckBox> checkBoxs = new ArrayList<CheckBox>(); 
 
	public HJCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public HJCheckBox(Context context,WidgetClass items){
		super(context);		
	}
	
	public HJCheckBox(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param){
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
		 this.businessParam = param;
		initView(items); 
	}

	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}
	public void saveCTLM1347() {

		String val = "" ;
		
		for ( int i=0 ; i<checkBoxs.size() ;i++)
		{
			if (checkBoxs.get(i).isChecked() )
			{
				if (val=="")
				{val =checkBoxs.get(i).getTag().toString();}
				else
				{ val =val +","+ checkBoxs.get(i).getTag().toString();}
			}
		}
////		if (StringUtil.isNullOrEmpty(val) ) return ;
 
//
		datactlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		datactlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
		//ctlm1347.setId_node( dataList.get(i).getId()  ); // 自己生成id_node,当前时间+随机数
		datactlm1347.setName_node(items.name);
		datactlm1347.setVar_billno(businessParam.getBillNo());
		datactlm1347.setId_model(businessParam.getModelId() );
		datactlm1347.setId_nodetype(WidgetName.HJ_CHECKBOX); 
		datactlm1347.setFlag_upload("N");  
		datactlm1347.setId_view( businessParam.getViewId());
		datactlm1347.setId_parentnode(businessParam.getIdParentNode());
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
	               JSONObject jsonObject =  null;
					try {
						jsonObject = new  JSONObject(jsonvalues);
						jsonObject.putOpt(items.attribute.field, val);
						datactlm1347.setvar_Json(jsonObject.toString());
					} catch (JSONException e) { 
						e.printStackTrace();
					}
	            }
	            else
	            {
	            	datactlm1347.setvar_Json(  "{\""+items.attribute.field+"\":\""+val+"\"}"); 
	            }
	            
	        }
	        else
	        {
	        	datactlm1347.setvar_Json("");
	        }
	        datactlm1347.setInt_line(0);
	        datactlm1347.setId_table(  items.attribute.datasource);
		datactlm1347.setVar_data1(val); 
		datactlm1347.setId_srcnode(items.id); 
		datactlm1347.setDate_opr(businessParam.getDataOpr()); 
		BusinessBaseDao.replaceCTLM1347(datactlm1347);

	}
	
	
	private void initView(WidgetClass items) {
 
		View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_linearlayout_vertical, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(view);
		
		
		TextView content = (TextView) view.findViewById(R.id.textView);
		content.setText(items.name); 
		
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.hj_linearLayout); 
		ll.removeAllViews();
		if(items.HJRadioButtonOption.size() > 0){
			for(int i = 0; i < items.HJRadioButtonOption.size(); i++){

				View view1 = LayoutInflater.from(getContext()).inflate(
						R.layout.layout_hjcheckbox, null);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			 
				view1.setLayoutParams(lp); 
				CheckBox checkBox = (CheckBox)view1.findViewById(R.id.checkBox1); 
				checkBox.setText(items.HJRadioButtonOption.get(i).name);
				checkBox.setChecked(false); 
				checkBox.setTag(items.HJRadioButtonOption.get(i).defaultValue ); 
				setCheckBoxAttribute(i,checkBox,content);
				ll.addView(view1);
				checkBoxs.add( checkBox);
			}
		
		}
	

	} 
	
	private void setCheckBoxAttribute(int i, CheckBox button ,TextView title) {
		 
		// /加粗
		if ("medium"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(18);
			button.setTextSize(18); 
		}

		// /加粗
		if ("large"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(22);
			button.setTextSize(22); 
		}

		// /加粗
		if ("small"
				.equalsIgnoreCase(items.attribute.fontsize)) {
			title.setTextSize(14);
			button.setTextSize(14); 
		}
		title.getPaint().setFakeBoldText(
				items.attribute.bold);
		
		// /加粗
		if (items.HJRadioButtonOption.get(i).attribute.bold) {
			
			button.getPaint().setFakeBoldText(
					items.attribute.bold);
		}
		// /加粗
		if (!StringUtil
				.isNullOrEmpty(items.attribute.textcolor)) {
			button.getPaint()
					.setColor(
							Color.parseColor(items.attribute.textcolor));
		}

		 
	}
	
	///////////////////对外公开接口
//	@Override
//	public void setValue(ArrayList<Ctlm1347> ctlm1347List) {
//		// TODO Auto-generated method stub
//		for (int datasize = 0; datasize < ctlm1347List.size(); datasize++) {
//			if (items.id.equalsIgnoreCase(ctlm1347List.get(datasize)
//					.getId_srcnode())
//					&& startViewInfo.id.equalsIgnoreCase(ctlm1347List.get(
//							datasize).getId_model())) { 
//				setValue( ctlm1347List.get(datasize ).getVar_data1() ); 
//				  break;
//			}
//		} 
//	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		 
		if (StringUtil.isNullOrEmpty(msg )) return ; 
		String[] val =   msg.split(","); 
		for (int i =0;i<val.length ;i++)
		{
			for (int j=0 ;j<checkBoxs.size() ;j++ )
			{
				 if  (val[i].equalsIgnoreCase(  checkBoxs.get(j).getTag().toString())     )
				 {
					 checkBoxs.get(j).setChecked(true);
				 }
			}
		} 
	}

	@Override
	public void setValueDefault() {
		///
		//判断是否保存过
		// TODO Auto-generated method stub
		String DfeaultValue ;
		DfeaultValue = items.defaultValue ;  
		if (StringUtil.isNullOrEmpty(DfeaultValue )) return ; 
		setValue(DfeaultValue);
	}

	@Override
	public void setJesonValue(String values ) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> list = null; 
		try {
			list = DomJosnParse.AnalysisLine(values) ;
		} catch (JSONException e) { 
			e.printStackTrace();
		}  
		 
		if (list.size() >0 )
		{
			setValue(list.get(0).get( items.attribute.field));//   content.setText( list.get(0).get( items.attribute.field)); 
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
		return false;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return "";
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
	public void setDataBuild(  Boolean flag ,
			BusinessData ctlm1345List) 
	{
		if (flag)
		{
			ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao. 
					 getCtlm1347List( startViewInfo.id,currentviewClass.id,businessParam.getIdParentNode(),businessParam.getBillNo(),items.id) ;
			 
			  if (ctlm1347List != null && ctlm1347List.size() >0 )
			  {
				  setValue(ctlm1347List); 
			  }
		} else if (ctlm1345List != null
				&& !flag)
		{
			setValue(ctlm1345List);
		}
	}
	
	public void setValue(ArrayList<Ctlm1347> ctlm1347List)
	{
		for (int datasize = 0; datasize < ctlm1347List.size(); datasize++)
		{
			if (items.id.equalsIgnoreCase(ctlm1347List.get(datasize)
					.getId_srcnode()))
			{
				datactlm1347 = ctlm1347List.get(datasize);
				for (CheckBox cb : checkBoxs)
				{
					if (cb.getTag().toString()
							.equalsIgnoreCase(datactlm1347.getVar_data1()))
					{
						cb.setChecked(true);
					}
				}
				break;
			}
		}
	}
	
	public void setValue(BusinessData ctlm1345List) 
	{
		if (ctlm1345List.getCtlm1345().size() <= 0)
			return;
		datactlm1347.setvar_Json(ctlm1345List.getCtlm1345().get(0)
				.getVar_value());
		datactlm1347.setInt_line(0);
		datactlm1347.setId_node(StringUtil.getMyUUID());
		datactlm1347.setId_table(ctlm1345List.getId_table());
		// /设置显示值
		setJesonValue(ctlm1345List.getCtlm1345().get(0).getVar_value());
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
      String val = "" ;
		
		for ( int i=0 ; i<checkBoxs.size() ;i++)
		{
			if (checkBoxs.get(i).isChecked() )
			{
				if (val=="")
				{val =checkBoxs.get(i).getTag().toString();}
				else
				{ val =val +","+ checkBoxs.get(i).getTag().toString();}
			}
		}
		return val;
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
	public void addItem(String billno,String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		 String val = "" ;
			
			for ( int i=0 ; i<checkBoxs.size() ;i++)
			{
				if (checkBoxs.get(i).isChecked() )
				{
					if (val=="")
					{val =checkBoxs.get(i).getTag().toString();}
					else
					{ val =val +","+ checkBoxs.get(i).getTag().toString();}
				}
			}
			if (items.attribute.required)
			{
				if (StringUtil.isNullOrEmpty(val)) 
				{
					 ToastUtil.ShowShort(getContext(),  items.name + "不能为空,请选择.");
					return false;
				}
			}
		return true;
	}
}
