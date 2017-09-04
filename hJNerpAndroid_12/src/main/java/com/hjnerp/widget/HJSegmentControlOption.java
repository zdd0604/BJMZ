package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.LuaLoadScript;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HJSegmentControlOption extends LinearLayout  implements HJViewInterface{
	
	private static final String TAG = "HJSegmentControlOption";
	private Context context;
	private WidgetClass items;//<HJRadioButton
	private ViewClass currentviewClass;//当前显示的View，<HJModel下面的某一个View
    private List<Button> buttonList =  new ArrayList<Button>();
    private int width; // 每个按钮的宽度\
    private String searchCondition;//查询条件：本周/本月等
    private BusinessParam businessParam =  new BusinessParam(); //当前界面上参数
    int mcolor;
    int defaultselected = 0;
    private Ctlm1347 ctlm1347 = new Ctlm1347(); 
    
	public HJSegmentControlOption(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	public HJSegmentControlOption(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public HJSegmentControlOption(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	public HJSegmentControlOption(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,BusinessParam param) {
		super(context);
		// TODO Auto-generated constructor stuba
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.businessParam = param;
		//如果可见，则创建此控件
		if(items.attribute.visible){
			if(!TextUtils.isEmpty(items.attribute.backgroundcolor)){
				mcolor = Color.parseColor(items.attribute.backgroundcolor);
			}else{
				mcolor = getResources().getColor(R.color.blue);
			}
			initView();
		}
		
	}

//	@SuppressLint("NewApi")
	@SuppressLint("NewApi")
	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_linearlayout_horizontal, null);
		addView(view);
		int num = items.HJRadioButtonOption.size();
		Point outSize = new Point();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getSize(outSize);
		width = (outSize.x - 40*3) / num;
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.linearLayout_h);
		// ll.removeAllViews();
		if (items.HJRadioButtonOption.size() > 0) {
			
			for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {

				WidgetClass widget = items.HJRadioButtonOption.get(i);
				View view1 = new View(context);
				view1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_hjsegment, null);
				view1.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				android.view.ViewGroup.LayoutParams lp = view1
						.getLayoutParams();
				lp.width = width;//设置宽度 
//				lp.height = 200;
				
				final Button button = (Button) view1.findViewById(R.id.button_segment);
				button.setTag(i);
								
				
				if(i == 0){
					button.setBackground(getResources().getDrawable(R.drawable.hjsegment_leftbutton));
//					setButtonBkg(button, 0);
				}
				if(0 < i && i < items.HJRadioButtonOption.size() - 1){
					button.setBackground(getResources().getDrawable(R.drawable.hjsegment_middlebutton));
//					setButtonBkg(button, 0);
				}
				if(i == items.HJRadioButtonOption.size() - 1){
					button.setBackground(getResources().getDrawable(R.drawable.hjsegment_rightbutton));
//					setButtonBkg(button, 0);
				}

				//设置按钮默认选中
				if(items.defaultValue != null){
					if(items.defaultValue.equalsIgnoreCase(items.HJRadioButtonOption.get(i).defaultValue)){
						defaultselected = i;
						searchCondition = items.defaultValue;
					}
				}else{//如果xml没有设默认选中，则默认选中第一个
					if(i == 0){
						searchCondition = items.HJRadioButtonOption.get(0).defaultValue;
					}
				}

				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int number = (Integer) button.getTag();
						searchCondition = items.HJRadioButtonOption.get(number).defaultValue;
						setButtonBackGround((Integer)button.getTag());
//						saveCtlm1347();
						saveDefaultCTLM1347();
						setSearchValue(number);
					}
				});
				
				button.setText(widget.name); 
				setButtonTextSize(button,widget);
				ll.addView(view1);
				buttonList.add(button);
                
			}
			setButtonBackGround(defaultselected);
		}
		
		//如果1347有数据，则设置下默认选项,并展示此选项下的数据
		
		Ctlm1347 ctlm13472  = BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(),businessParam.getBillNo(),businessParam.getModelId(),items.id);
		
		System.out.println(ctlm13472);
		if (ctlm13472 != null )
		{
			ctlm1347 = ctlm13472;
			for(int i = 0;i < items.HJRadioButtonOption.size();i++){
				if(ctlm1347.getVar_data1().trim().equalsIgnoreCase(items.HJRadioButtonOption.get(i).defaultValue.trim())){
					defaultselected = i;
					searchCondition = items.HJRadioButtonOption.get(i).defaultValue;
				}
			}
			setButtonBackGround(defaultselected);

		}
		
//		saveCtlm1347();
		saveDefaultCTLM1347();
		setSearchValue(defaultselected);
	}
	
	private void setButtonBkgNormal(Button button){
		
		LayerDrawable background = (LayerDrawable) button.getBackground();
		GradientDrawable drawable = (GradientDrawable) background.getDrawable(0);
		drawable.mutate();
		drawable.setColor(getResources().getColor(R.color.business_bkg_grey));
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		float dpi = outMetrics.density;
		drawable.setStroke((int) (2 * dpi), mcolor);
		GradientDrawable drawable2 = (GradientDrawable) background.getDrawable(1);
		drawable2.mutate();
		drawable2.setColor(getResources().getColor(R.color.business_bkg_grey));
		button.setTextColor(getResources().getColor(R.color.black));
	}
	
	private void setButtonBkgSelected(Button button){
		
		LayerDrawable background = (LayerDrawable) button.getBackground();
		GradientDrawable drawable = (GradientDrawable) background.getDrawable(0);
		drawable.mutate();
		drawable.setColor(mcolor);
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		float dpi = outMetrics.density;
		drawable.setStroke((int) (2 * dpi), mcolor);
		GradientDrawable drawable2 = (GradientDrawable) background.getDrawable(1);
		drawable2.mutate();
		drawable2.setColor(mcolor);
		button.setTextColor(getResources().getColor(R.color.business_bkg_grey));
	}
	
	public void saveDefaultCTLM1347(){
		if(currentviewClass.presave){
			saveCTLM1347();
		}
	}
	
	private void saveCTLM1347()
	{   	
	 
		ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		ctlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
 	 
		if ( StringUtil.isNullOrEmpty(ctlm1347.getId_node()))
		{ctlm1347.setId_node(StringUtil.getMyUUID()); }
		 
		ctlm1347.setName_node(items.name);
		ctlm1347.setVar_billno(businessParam.getBillNo());
		ctlm1347.setId_model(businessParam.getModelId() );
		ctlm1347.setId_nodetype(WidgetName.HJ_SEGMENTCONTROL); 
		ctlm1347.setFlag_upload("N");  
		ctlm1347.setId_parentnode(businessParam.getIdParentNode());  
		ctlm1347.setVar_data1(searchCondition);
		ctlm1347.setInt_line(0); 
		ctlm1347.setId_srcnode(items.id);
		ctlm1347.setDate_opr(businessParam.getDataOpr()); 
		BusinessBaseDao.replaceCTLM1347(ctlm1347);
	}
	// 设置字体大小
	public void setButtonTextSize(Button button,WidgetClass item){
		if (getContext().getResources().getString(R.string.text_size_large).equalsIgnoreCase(item.attribute.fontsize)) {
			button.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeLarge));
		} else if (getContext().getResources().getString(R.string.text_size_medium).equalsIgnoreCase(item.attribute.fontsize)) {
			button.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeMedium));
		} else {
			button.setTextSize(getContext().getResources().getInteger(R.integer.TextSizeSmall));
		}
	}
	
	private void setButtonBackGround(int tag){
		for(int i = 0;i < buttonList.size();i ++){
			Button btnTemp = buttonList.get(i);
			if((Integer)btnTemp.getTag() == tag){
//				btnTemp.setBackgroundDrawable(getResources().getDrawable(R.drawable.hjsegment_button_selected));
//				setButtonBkg(btnTemp,getResources().getColor(R.color.blue));//选中
				setButtonBkgSelected(btnTemp);
				
			}else{
//				btnTemp.setBackgroundDrawable(getResources().getDrawable(R.drawable.hjsegment_button_normal));
				setButtonBkgNormal(btnTemp);//选中
			}
		}
	}

	//根据查询条件设置展示数据
	private void setSearchValue(int number){
		
	    final int index ;
	    index = number;
	    ((Activity) context).runOnUiThread(new Runnable() { 
			@Override
			public void run() { 
				
				if (!StringUtil.isNullOrEmpty( items.HJRadioButtonOption.get(index).attribute.onclick ) )
				{
					try {
						LuaLoadScript.LoadScript(   );
						LuaLoadScript.runScript(items.HJRadioButtonOption.get(index).attribute.onclick );
					} catch (Exception e) {
				// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 
				}
			}
		}); 
		
	}

	 
 
//	public void setValueDefault() {
//		// TODO Auto-generated method stub
//		if (StringUtil.isNullOrEmpty(items.defaultValue) ) return ; 
//		Ctlm1347 ctlm13472  = BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(),businessParam.getBillNo(),businessParam.getModelId(),businessParam.getIdSrcnode());
//	 
//		if (ctlm13472 != null)
//		{ for (int i =0 ; i < items.HJRadioButtonOption.size();i++)
//		 { 
//			if (  ctlm13472.getVar_data1().equalsIgnoreCase(items.HJRadioButtonOption.get(i).defaultValue ) )
//			{	searchCondition = items.HJRadioButtonOption.get(i).defaultValue;
//				setButtonBackGround( i);
//		        setSearchValue(searchCondition);
//			}
//		 }}
//	   
//	}

	@Override
	public void setValue(String msg) {
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
	public void setValueDefault() {
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
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub
		
	}

	 
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
	 
	
}
