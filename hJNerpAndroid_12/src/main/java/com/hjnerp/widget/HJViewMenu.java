package com.hjnerp.widget;
 
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
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
import com.hjnerp.model.HJSender;
import com.hjnerp.util.LuaLoadScript;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.util.HashMap;
import java.util.Map;

//@SuppressLint("NewApi")
public class HJViewMenu extends LinearLayout implements HJViewInterface{

	protected static final String TAG = "HJViewMenu"; 
	private BusinessParam businessParam  ; //当前界面上参数
	private WidgetClass items;//<HJViewMenu
	private ViewClass currentviewClass;//当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	private String[] billNoArray; //ToolBar有几个Button就生成几个bill_no
    private Context context;
    private View contextFragment;
	public HJViewMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HJViewMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HJViewMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HJViewMenu(Context context, WidgetClass items) {
		super(context);
		this.items = items;
		initView(items);
	}
	
	public HJViewMenu(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo){
		super(context);
		this.context  = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
		initView(items); 
	}

	public HJViewMenu(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param ,View fragment){
		super(context);
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo; 
		businessParam = param;
		contextFragment = fragment;
		initView(items); 
	}
	
	
 

	private void initView(final WidgetClass items) {
		final LinearLayout ll_btn2 = (LinearLayout) contextFragment.findViewById(R.id.businessmenu_btn);
		final LinearLayout ll_btn = new   LinearLayout (getContext()) ;
 
		ll_btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
 
		TextView  textview =  new TextView(getContext()); 
		if (items.HJRadioButtonOption.size() == 1) { 
			textview.setText(items.HJRadioButtonOption.get(0).name);  
			LayoutParams lpd =  new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			lpd.gravity=Gravity.CENTER_VERTICAL   ;
			lpd.setMargins(10, 0, 10, 0);
			textview.setLayoutParams(lpd);
			textview.setTextSize(18);
			textview.setTextColor(getResources().getColor(R.color.actionbar_bg_color));
			textview.setFocusable(false);
			ll_btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					WidgetClass widgetClass = items.HJRadioButtonOption.get(0);
					 
					  Ctlm1347 ctlm1347 =  BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(), 
							  businessParam.getBillNo(), 
							  businessParam.getModelId(),
							  widgetClass.id);
						
					  if (ctlm1347== null)
					  {
						  ctlm1347 = new Ctlm1347();
					  }
					  
					if (!StringUtil.isNullOrEmpty( widgetClass.attribute.onclick ) )
					{  
					Map<String,String> mMap = new HashMap<String, String>();
					mMap.put(HJSender.COL, "");
					mMap.put(HJSender.COLID, "");
					mMap.put(HJSender.ROW, "1");
					mMap.put(HJSender.VALUES, "");
					if (StringUtil.isNullOrEmpty(businessParam.getBillNo()))
					{
						mMap.put(HJSender.BILLNO, ctlm1347.getVar_billno()); 
					}
					else
					{
						mMap.put(HJSender.BILLNO, businessParam.getBillNo()); 	
					} 
					mMap.put(HJSender.NODEID,  ctlm1347.getId_node());
					
					try {
						LuaLoadScript.LoadScript( );
						LuaLoadScript.runScript(widgetClass.attribute.onclick ,mMap) ; 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
 
					} 
				}
			});
		}
		else { 
		   
			LayoutParams lpd =  new LayoutParams(  LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//			lpd.weight =(int) (48 * scale + 0.5f);
			lpd.setMargins(30, 0, 10, 0);
			lpd.gravity=Gravity.CENTER_VERTICAL   ;
			textview.setLayoutParams(lpd); 
			textview.setFocusable(false);
//			textview.setBackground(getResources().getDrawable(R.drawable.actionbar_more_icon)); 
			textview.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_actbar_more));
			ll_btn2.setTag("hjpop");
//			
			ll_btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HJViewMenuPopupWindow viewwinodw = new HJViewMenuPopupWindow(
							getContext(), items.HJRadioButtonOption,
							currentviewClass, startViewInfo, businessParam);
					viewwinodw.setBackgroundDrawable( getResources().getDrawable(R.color.transparent) );
				 
					viewwinodw.showAsDropDown(ll_btn2.findViewWithTag("hjpop"));
				}
			});
		}
 
		ll_btn.addView(textview);
		////保存菜单 
	   addView(ll_btn);
	}

	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}
	
	public void saveCTLM1347()
	{
		////先读出菜单是否保存过，如果保存过ID_NODE 就不本变化，否则生成新的ID 
		for  ( int i=0 ; i< items.HJRadioButtonOption.size() ;i++) 
		{ 
		   	Ctlm1347 ctlm1347 = BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(), businessParam.getBillNo(), businessParam.getModelId(), items.HJRadioButtonOption.get(i).id );   
			if (ctlm1347 == null)
			{
				ctlm1347 =  new Ctlm1347();
			}
		   	ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
			ctlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
			if (StringUtil.isNullOrEmpty(ctlm1347.getId_node()))
			 {
				ctlm1347.setId_node(  StringUtil.getMyUUID() ); // 自己生成id_node,当前时间+随机数  
			 }
			ctlm1347.setName_node(items.HJRadioButtonOption.get(i).name);
			ctlm1347.setId_parentnode(businessParam.getIdParentNode());
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_model(businessParam.getModelId());
			ctlm1347.setId_nodetype(WidgetName.HJ_VIEWMENU);  
			ctlm1347.setFlag_upload("N");
			ctlm1347.setId_view( businessParam.getViewId() );
			ctlm1347.setId_srcnode(items.HJRadioButtonOption.get(i).id); 
			ctlm1347.setDate_opr(businessParam.getDataOpr());
			BusinessBaseDao.replaceCTLM1347(ctlm1347);
			
		}
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
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(String row, String column) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setPhoto() {
		// TODO Auto-generated method stub
		return null;
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
		return false;
	}

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return null;
	}

 
	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDataBuild(Boolean flag ,
			BusinessData ctlm1345List) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int saveData(Boolean required) {
		// TODO Auto-generated method stub
		
		if(currentviewClass.presave){
			saveCTLM1347();
		}
		return 0;
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
