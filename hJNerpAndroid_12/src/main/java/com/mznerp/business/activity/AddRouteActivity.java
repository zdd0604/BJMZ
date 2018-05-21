package com.mznerp.business.activity;
 
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.mznerp.activity.qrcode.CaptureActivity;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.BusinessActivityFragment;
import com.mznerp.util.BDLocationUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.R;

/**
 * 终端查询界面
 */
public class AddRouteActivity extends ActivitySupport implements
		OnClickListener {
 
	public static final String SEARCH_FLAG = "flag"; // 查询的条件
	public static final String SERACH_BY_INPUT = "inputeditext"; // 输入框内容去查询
	public static final String SEARCH_BY_BDLOCATION = "bdlocation";// 经纬度去查询
	public static final String SEARCH_BY_QWCODE = "qwcode"; // 条形码或者二维码去查询
	public static final String SEARCH_PARAMAS = "data_paramas"; // 传递的参数值
	public static final String SERACH_BY_CONDITION = "condition";
	public static final String DATA_RESOURCE = "data_resource"; 
	public static final int REQUEST_CODE = 1000;
	private RelativeLayout rl_actionbar_back;
	private String id_model;
	
	private TextView title;
	
	private LinearLayout layout_location;
	private LinearLayout layout_scanner;
	private EditText input;
	private String datasource;

	private String condition; 
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 返回定位结果，并且发送给下一个activity
			switch (msg.what) {
			case BDLocationUtil.LOCATION_FAILED:
				ToastUtil.ShowLong(AddRouteActivity.this,
						"定位失败................");
				break;
			case BDLocationUtil.LOCATION_SUCCESS:
				BDLocation location = (BDLocation) msg.obj;
				double[] data = new double[2]; // 存储经纬度的值
				data[0] = location.getLatitude();  
				data[1] = location.getLongitude(); 
				Intent intent = new Intent();
				intent.putExtra("id_model", id_model);
				intent.putExtra(SEARCH_FLAG, SEARCH_BY_BDLOCATION); // 表明通过经纬度去获取终端信息
				intent.putExtra(SEARCH_PARAMAS, data);
				intent.putExtra(DATA_RESOURCE, datasource);
				intent.putExtra(SERACH_BY_CONDITION, condition);
				 
				
				intent.setClass(AddRouteActivity.this,
						SearchRouteActivity.class);
				startActivityForResult(intent,1002);
				break;
			default:
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		mActionBar.hide();
		this.getSupportActionBar().hide();
		
		setContentView(R.layout.layout_add_route);
		input = (EditText) findViewById(R.id.add_route_id);
		id_model = getIntent().getStringExtra("id_model");
		input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// 跳转到其他界面
					Intent intent = new Intent();
					intent.putExtra(SEARCH_FLAG, SERACH_BY_INPUT);
					intent.putExtra("id_model", id_model);
					intent.putExtra(SEARCH_PARAMAS, input.getText().toString());
					intent.putExtra(DATA_RESOURCE, datasource);
					intent.putExtra(SERACH_BY_CONDITION, condition); 
					
					intent.setClass(AddRouteActivity.this, SearchRouteActivity.class);
					startActivityForResult(intent,1002);
					return true;
				}
				return false;
			}
		});
		rl_actionbar_back = (RelativeLayout) findViewById(R.id.rl_actionbar_back);	
		rl_actionbar_back.setOnClickListener(this);
		title = (TextView)findViewById(R.id.tv_actionbar_title); 
		title.setText("查询"); 
		layout_location = (LinearLayout) findViewById(R.id.add_route_location);
		layout_location.setOnClickListener(this);
		layout_scanner = (LinearLayout) findViewById(R.id.add_route_scanner);
		layout_scanner.setOnClickListener(this);
		datasource = getIntent().getStringExtra(DATA_RESOURCE);
		condition = getIntent().getStringExtra(SERACH_BY_CONDITION); 
		closeInput();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_actionbar_back:
			finish();
			break;
		case R.id.add_route_location:
			// 开始执行定位，并且进行跳转
			BDLocationUtil.setActivityHandler(handler);
			BDLocationUtil.getLocation(this);
			break;
		case R.id.add_route_scanner:
			///扫描二维码
			Intent intent = new Intent(); 
			intent.setClass(this,
					CaptureActivity.class);
			startActivityForResult(intent,1001);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub 
		 super.onActivityResult(requestCode, resultCode, data);
		 if(data == null){
			 return;
		 }
			if( requestCode==1001) {
				   Bundle bundle = data.getExtras(); 
				    String str = bundle.getString("result"); 
				    if  (!StringUtil.isNullOrEmpty(str))
				    { Intent intent = new Intent();
				    intent.putExtra("id_model", id_model);
					intent.putExtra(SEARCH_FLAG, SEARCH_BY_QWCODE); // 表明通过经纬度去获取终端信息
					intent.putExtra(SEARCH_PARAMAS, str);
					intent.putExtra(DATA_RESOURCE, datasource);
					intent.putExtra(SERACH_BY_CONDITION, condition); 
					
					intent.setClass(AddRouteActivity.this,
							SearchRouteActivity.class);
					startActivityForResult(intent, 1002);
				    }
			}
			if ( requestCode==1002 && resultCode ==1002) {
				    Bundle bundle = data.getExtras(); 
				    String str = bundle.getString("result"); 
				    if  (!StringUtil.isNullOrEmpty(str))
				    {
				    
				    	Intent intent = new Intent(this,BusinessActivityFragment.class);
						Bundle bundlel = new Bundle();  
						bundlel.putString("result", str);  
						bundlel.putString("id_table", datasource);
						intent.putExtras(bundlel);  
	//					setResult(3, intent);  
						setResult(1002, intent);//传确定信息，和参数
						
						this.finish();
			         }
				
			} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	 
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break; 
		default:
			break;
		}
		return false;
	}
	
}
