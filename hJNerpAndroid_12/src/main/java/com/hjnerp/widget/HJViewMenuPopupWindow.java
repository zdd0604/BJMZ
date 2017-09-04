package com.hjnerp.widget;

  
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.HJSender;
import com.hjnerp.util.LuaLoadScript;
import com.hjnerpandroid.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HJViewMenuPopupWindow extends PopupWindow {
	 
	private Context context;
	private LinearLayout mLayout;
	private List<WidgetClass> items; 
	private ViewClass currentviewClass;
	private StartViewInfo startViewInfo;
	private BusinessParam businessParam  ; //当前界面上参数
	
	public HJViewMenuPopupWindow(Context context, List<WidgetClass> hJRadioButtonOption) {
		super(context);
		this.context = context;
		this.items = hJRadioButtonOption;
		initView();
	
	}
	
	public HJViewMenuPopupWindow(Context context, List<WidgetClass> hJRadioButtonOption,
			ViewClass currentviewClass, StartViewInfo startViewInfo  ,BusinessParam param){
		super(context);
		this.context = context;
		this.items = hJRadioButtonOption;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;  
		businessParam = param;
		initView();
		
	}
	
//	@SuppressLint("NewApi")

	private void initView(){
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_menu_poupwindow, null);
 
		setContentView(view);
		mLayout = (LinearLayout) view.findViewById(R.id.hj_menu_container);
//		mLayout.setBackgroundDrawable( getContentView().getResources().getDrawable(R.color.actionbar_bg_color)  );  
		Point outSize = new Point();
		((Activity)context).getWindowManager().getDefaultDisplay().getSize(outSize);
		setHeight(LayoutParams.WRAP_CONTENT); 
		setWidth(outSize.x/3); 
		setFocusable(true);
//		
//		TextView  linetop =  new TextView(context); 
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 50);  
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);  
		
		lp3.setMargins(20, 0, 0, 0);
//		linetop.setBackground( getContentView().getResources().getDrawable(R.color.black)  );  
//		linetop.setBackgroundColor(Color.parseColor("#FFFFFF"));//Drawable( getContentView().getResources().getDrawable(R.color.white)  );  
//		linetop.setHeight(10);
//		mLayout.addView(linetop,lp1);   
		for(int i = 0;i<items.size();i++) { 
			final LinearLayout mLayoutg = new LinearLayout(context )  ; 
			
			mLayoutg.setOrientation(LinearLayout.HORIZONTAL); 
			mLayoutg.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.activity_child_item_bkg));
			 
			mLayoutg.setTag(items.get(i).id); 
		    TextView label = new TextView(context); 
		    label.setTextSize(16); 
			label.setText(items.get(i).name);
			label.setTextColor(Color.parseColor("#303030"));
		 	label.setGravity(Gravity.LEFT);
		 	label.setGravity(Gravity.CENTER);
		 
		 	
		 	mLayoutg.setOnClickListener(new OnClickListener() { 
				@Override
				public void onClick(View v) {
					HJViewMenuPopupWindow.this.dismiss();
					
					final String MenuID = (String)mLayoutg.getTag(); 
					for(int i=0 ; i <items.size();i++)
					{
						if  (MenuID == items.get(i).id)
						{ 
							try {
								if (!TextUtils.isEmpty(items.get(i).attribute.onclick)) {
									Ctlm1347 ctlm1347 =  BusinessBaseDao.getSrcCtlm1347(businessParam.getIdParentNode(), businessParam.getBillNo(), businessParam.getModelId(),items.get(i).id);
									 if (ctlm1347== null)
									  {
										  ctlm1347 = new Ctlm1347();
									  }
									Map<String,String> mMap = new HashMap<String, String>();
									mMap.put(HJSender.COL, "");
									mMap.put(HJSender.COLID, "");
									mMap.put(HJSender.ROW, "1");
									mMap.put(HJSender.VALUES, "");
									mMap.put(HJSender.BILLNO, businessParam.getBillNo()); 
									mMap.put(HJSender.NODEID,  ctlm1347.getId_node()); 
									
									LuaLoadScript.LoadScript( );
									LuaLoadScript.runScript(items.get(i).attribute.onclick ,mMap) ; 
								}
				 				} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}  
							
							return ;
						}
					}	 
				}
			});
		
		 	
			mLayoutg.addView(label,lp3);
			 
			int count = mLayoutg.getChildCount();
			int height = 0;
			for(int index = 0;index<count;index++){
				View childView = mLayoutg.getChildAt(index);
				childView.measure(0, 0);
				height += childView.getMeasuredHeight();
			}
			 
			 float scale = context.getResources().getDisplayMetrics().density;  
			lp2.height =(int) (48 * scale + 0.5f);  
			mLayout.addView(mLayoutg,lp2);  
			
			TextView  line =  new TextView(context);  
			line.setHeight(1); 
			line.setBackgroundColor(Color.parseColor("#303030"));
			mLayout.addView(line,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
			
		}
		 
		
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.hj_menu_container)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
		
	}
	 
}
