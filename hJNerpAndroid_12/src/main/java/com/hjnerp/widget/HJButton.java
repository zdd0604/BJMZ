package com.hjnerp.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetAttribute;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.BusinessData;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

public class HJButton extends Button implements OnClickListener , HJViewInterface{
	private static String TAG = "HJButton";
	
	private String nextaction;
	private String backview;
	
	private WidgetClass items;//<HJButton
	private ViewClass currentviewClass;//当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	private String billNo; //构造方法传过来bill_no，或者自己生成

	public HJButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	public HJButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public HJButton(Context context) {
		super(context);
		initViews();
	}
	
	public HJButton(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo){
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		billNo = StringUtil.getMyUUID();
		
		initViews();
		
	}
	public HJButton(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,String billNo){
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.billNo = billNo;
		
		initViews();
		
	}

	private void initViews(){
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		setOnClickListener(this);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
		setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_style));
	}
	
	public void setHJButtonBackgroudDrawable(Drawable drawable){
		setBackgroundDrawable(drawable);
	}
	
	public void setAttribute(WidgetClass widget){
		setText(widget.name);
		setWidgetDetailAttribute(widget.attribute);
	}

	private void setWidgetDetailAttribute(WidgetAttribute attribute) {
		Paint paint = getPaint();
		paint.setFakeBoldText(attribute.bold);
		if("large".equalsIgnoreCase(attribute.fontsize)) {
			setTextSize(22);
		} else if("medium".equalsIgnoreCase(attribute.fontsize)) {
			setTextSize(18);
		} else {
			setTextSize(14);
		}
		if(attribute.height>0) {
			setHeight((int) attribute.height);
		}
		if(attribute.width>0) {
			setWidth((int) attribute.width);
		}
		nextaction = attribute.nextview;
		backview = attribute.backview; 
	}
	
	public void setlistner(OnClickListener l){
		setOnClickListener(l);
	}

	@Override
	public void onClick(View v) {
//		if(nextaction != null) {
//			Log.e(TAG,"next pager is "+nextaction);
//			//表明需要存在跳转
//		
//			//step1:处理MainActivity.billInfoListMap
//			HashMap<String,String> billInfoMap = new HashMap<String, String>();
//			if(BusinessActivity.billInfoListMap.size() > 0){
//				HashMap<String, String> fatherMap = BusinessActivity.billInfoListMap.get(BusinessActivity.currentPageNumber - 2);
//				
//				billInfoMap.put("view_id",currentviewClass.id);
//				billInfoMap.put("id_parentnode",fatherMap.get("id_node"));
//				billInfoMap.put("id_node",billNo);
////				billInfoMap.put("id_srcnode",currentviewClass.id);
//				billInfoMap.put("var_billno", BusinessActivity.BillNo);
//			}else{
//				if(currentviewClass == null){
//					Log.e(TAG,"null                null");
//				}
//				Log.e(TAG,currentviewClass.id);
//				billInfoMap.put("view_id",currentviewClass.id);
//				billInfoMap.put("id_parentnode",null);
//				billInfoMap.put("id_node",billNo);
////				billInfoMap.put("id_srcnode",currentviewClass.id);
//				billInfoMap.put("var_billno", BusinessActivity.BillNo);
//			}
//
//			BusinessActivity.billInfoListMap.add(billInfoMap);
//			
//			
//			
//			
//			((com.hjnerp.business.activity.BusinessActivity)getContext()).setViewId(nextaction);
//			((com.hjnerp.business.activity.BusinessActivity)getContext()).refresh();
//		}
//		if(backview != null) {
//			((com.hjnerp.business.activity.BusinessActivity)getContext()).removeViews(backview);
//			((com.hjnerp.business.activity.BusinessActivity)getContext()).setViewId(backview);
//			((com.hjnerp.business.activity.BusinessActivity)getContext()).refresh();
//		}
		
	}


	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
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
		return column;
		// TODO Auto-generated method stub
		
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
	public void setDataBuild( Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
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
		return 0; 
	}

 
 
	@Override
	public void setJesonValue(String values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}
