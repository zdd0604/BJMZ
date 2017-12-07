package com.mznerp.business.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.business.BusinessLua;
import com.mznerp.business.view.WidgetClass;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.model.BusinessParam;
import com.mznerp.model.Ctlm1347;
import com.mznerp.util.ToastUtil;
import com.mznerp.widget.HJListUploadLayout;
import com.mznerp.widget.WaitDialog;
import com.mznerp.R;

import java.util.ArrayList;
import java.util.List;

//@SuppressLint("NewApi")
public class BussinessUploadActivity extends ActivitySupport {
	public static final String TAG = "BussinessUploadActivity";

	public static final String CHECKED = "Y";
	public static final String UNCHECKED ="N";

	private static String controlID;
	private static BusinessParam para;
	private WidgetClass items;
	private static Activity mActivity = null;

	private TextView actionbar_title;
	private RelativeLayout actionbar_content;
	private static HJListUploadLayout listLayout;
	private LinearLayout businessmenu_btn;
	private ListView listview;
	private static ArrayList<Ctlm1347> ctlm1345ListFromCtlm1347 ;
	private List<String> billNoList = new ArrayList<String>() ;
	private List<String> idnodeList = new ArrayList<String>();
	private static WaitDialog dialog = null;
    public static Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    	   if(dialog == null||mActivity == null){
    		   return;
    	   }
    		String text =  (String) msg.obj;
    		switch (msg.what) {
			case Constant.MSG_SHOW:
				dialog.show();
				dialog.setTextView(text );
				break;
			case Constant.MSG_UPLOAD_SUCCESS:
				dialog.dismiss();
				ToastUtil.ShowLong(mActivity, text);
				// 修改数据库中的值
				BusinessBaseDao.replaceCTLM1347s(ctlm1345ListFromCtlm1347);
				
				ctlm1345ListFromCtlm1347.clear();
				ctlm1345ListFromCtlm1347 = BusinessBaseDao
						.getCtlm1347ListUpload(para.getIdParentNode(), para.getBillNo(),
								para.getModelId(), controlID);

				listLayout.setValue(ctlm1345ListFromCtlm1347);
				
				break;
			case Constant.MSG_UPLOAD_FAILED:
				dialog.dismiss();
				ToastUtil.ShowLong(mActivity, text);
				BusinessBaseDao.updateCtlm1347s(ctlm1345ListFromCtlm1347,"N");
				break;
			case Constant.MSG_UPDATE_TEXT:
				if(dialog != null && dialog.isShowing()){
					dialog.setTextView(text);
				}
				break;
			default:
				break;
			}
    		
    	};
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getActionBar().hide();
		this.getSupportActionBar().hide();
		setContentView(R.layout.layout_hjlist_upload);
		dialog = new WaitDialog(this);
		mActivity =  this;
		actionbar_title = (TextView) findViewById(R.id.tv_actionbar_title);
		listLayout = (HJListUploadLayout) findViewById(R.id.HJListUploadLayout);
		businessmenu_btn = (LinearLayout) findViewById(R.id.businessmenu_btn);
		
		TextView  textview =  new TextView(getContext()); 
		textview.setText("上传"); 
		LayoutParams lpd =  new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lpd.setMargins(10, 0, 10, 0);
		textview.setLayoutParams(lpd);
		textview.setTextSize(18); 
		textview.setTextColor(Color.parseColor("#303030"));
	 
		businessmenu_btn.addView(textview);
		businessmenu_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ctlm1345ListFromCtlm1347 != null
						&& ctlm1345ListFromCtlm1347.size() > 0) {
					billNoList.clear();
					idnodeList.clear();
					for (Ctlm1347 ctlm1347 : ctlm1345ListFromCtlm1347) {
						if (CHECKED.equals(ctlm1347.getFlag_upload())) {
							billNoList.add(ctlm1347.getVar_billno());
							idnodeList.add(ctlm1347.getId_node());
						}
					}
					BusinessLua.uploadData(billNoList);
					String text = "开始上传";
					Message msg = handler.obtainMessage(Constant.MSG_SHOW);
					msg.obj = text;
					handler.sendMessage(msg);
				}
			}
			
		});
		
		
		actionbar_content = (RelativeLayout) findViewById(R.id.tl_actionbar_content);
		actionbar_content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				return;
			}
		});
		controlID = getIntent().getStringExtra("control_id");
		para = (BusinessParam) getIntent().getSerializableExtra("data");
		items = (WidgetClass) getIntent().getSerializableExtra("items");
		listLayout.setItems(items);
		listview = listLayout.getListView();
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView imageView = (ImageView) view.findViewById(R.id.imageViewLeft);
				String var_data = ctlm1345ListFromCtlm1347.get(position).getFlag_upload();
				if(CHECKED.equalsIgnoreCase(var_data)){
					imageView.setImageResource(R.drawable.round_selector_normal);
					ctlm1345ListFromCtlm1347.get(position).setFlag_upload(UNCHECKED);
				} else {
					imageView.setImageResource(R.drawable.round_selector_checked);
					ctlm1345ListFromCtlm1347.get(position).setFlag_upload(CHECKED);
				}
			}
		});
		actionbar_title.setText("数据上传");

		ctlm1345ListFromCtlm1347 = BusinessBaseDao
				.getCtlm1347ListUpload(para.getIdParentNode(), para.getBillNo(),
						para.getModelId(), controlID);

		listLayout.setValue(ctlm1345ListFromCtlm1347);
	}
}
