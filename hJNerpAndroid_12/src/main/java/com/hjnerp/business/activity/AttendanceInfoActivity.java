package com.hjnerp.business.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerp.adapter.BussnessPhotoAdapter;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.business.view.uploadDialog;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.NearBuild;
import com.hjnerp.util.DateUtil;
import com.hjnerp.util.Log;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.imageCompressUtil;
import com.hjnerp.widget.HorizontalListView;
import com.hjnerp.widget.PositionSelectPopupWindow;
import com.hjnerp.widget.PositionSelectText;
import com.hjnerpandroid.R;

public class AttendanceInfoActivity extends ActivitySupport implements
		OnClickListener {

	private Intent intent;
	private ArrayList<NearBuild> lists;

	private PositionSelectText tvPosition;
	private String position = "";
	private TextView tvPoto;
	private ImageView imPhoto;
	public BussnessPhotoAdapter horizontalAdapter;
	private HorizontalListView horizontalListView;
	public String strImage;
	private File out;
	private Uri uri;
	private ArrayList<String> photoLists;
	
	private List<String> bills;
	private int index;

	/**
	 * @author haijian 添加edittext控件 添加图片路径字符串
	 */
	private BusinessParam businessParam;
	private EditText etNode;
	private String photoPath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getSupportActionBar().setTitle("详细信息");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			setContentView(R.layout.layout_hjattendance_info_activity);
			setupView();
			setupListener();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("info", "签到详情异常：" + e.toString());
		}
	}

	private void setupView() {
		intent = getIntent();
		lists = (ArrayList<NearBuild>) intent.getSerializableExtra("pois");
		businessParam = (BusinessParam) intent.getSerializableExtra("params");
		Log.i("infoq", "经纬度坐标：" + lists.get(0).point.x + "");
		tvPosition = (PositionSelectText) findViewById(R.id.ui_position_input);
		tvPoto = (TextView) findViewById(R.id.imageView_title);
		imPhoto = (ImageView) findViewById(R.id.imageView_takePhoto);
		tvPosition.setText(lists.get(0).name);
		tvPosition.addTextChangedListener(textWatcher);
		tvPosition.setTextColor(getResources().getColor(
				R.color.login_input_normal));
		horizontalListView = (HorizontalListView) findViewById(R.id.photo1);
		photoLists = new ArrayList<String>();
		horizontalAdapter = new BussnessPhotoAdapter(this, photoLists);
		horizontalListView.setAdapter(horizontalAdapter);
		etNode = (EditText) findViewById(R.id.editText1);
		bills = new ArrayList<String>();
	}

	private void setupListener() {
		tvPosition.setPopup(new PositionSelectPopupWindow(
				AttendanceInfoActivity.this, lists,
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// sputil.setComid(lists.get(position)
						// .name);
						index = position;
						tvPosition.setText(lists.get(position).name);
						tvPosition.getPopup().dismiss();
					}
				}));
		tvPoto.setOnClickListener(this);
		imPhoto.setOnClickListener(this);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			position = tvPosition.getText().toString();

			if (position.length() > 0) {
				tvPosition.setTextColor(getResources().getColor(
						R.color.login_input_normal));
			} else {
				tvPosition.setTextColor(getResources().getColor(
						R.color.login_input_notice));
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private String makePicFileName() {
		return Constant.FILE_TYPE_PIC + sputil.getMyId()
				+ DateUtil.getCurrentTime() + ".jpg";
	}

	private void startPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = makePicFileName();
		strImage = fileName;

		out = new File(Constant.CHAT_CACHE_DIR);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(Constant.CHAT_CACHE_DIR, fileName);
		uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		checkPath(intent, Constant.CHAT_CACHE_DIR + fileName);
		((Activity) getContext()).startActivityForResult(intent, 1);
	}

	private void checkPath(Intent intent, String picpath) {
		if (intent != null) {
			Uri uri_DCIM = null;
			if (intent.getData() != null) {
				uri_DCIM = intent.getData();
			} else {
				uri_DCIM = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			}
			String DCIMPath = "";
			Cursor cr = getContext().getContentResolver().query(uri_DCIM,
					new String[] { MediaStore.Images.Media.DATA }, null, null,
					MediaStore.Images.Media.DATE_MODIFIED + " desc");
			if (cr.moveToNext()) {
				DCIMPath = cr.getString(cr
						.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cr.close();
			if (DCIMPath.equals(picpath)) {
				ToastUtil.ShowLong(getContext(), "");
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (requestCode) {
		case 1:// 拍照
			if (resultCode == RESULT_OK) {

				String filePath = Constant.CHAT_CACHE_DIR + strImage;
				// //压缩成功后直接显示到聊天记录中
				Bitmap bitmap = imageCompressUtil.getCompressImage(filePath);
				if (bitmap != null) {
					try {

						imageCompressUtil.saveImage(bitmap, filePath);
						System.gc();
						photoLists.add(filePath);
						horizontalAdapter.updateListView(photoLists);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("发送图片异常：" + e.toString());
					}
				}

			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_takePhoto:
		case R.id.imageView_title:

			boolean sdState = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
			if (!sdState) {
				ToastUtil.ShowLong(getContext(), "sd卡不存在");
				return;
			}
			startPhoto();

			break;

		default:
			break;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.qiandao_upload, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 点击上传按钮的监听
		switch (item.getItemId()) {
		case R.id.menu_upload:
			for (int i = 0; i < photoLists.size(); i++) {
				if (i == photoLists.size() - 1) {
					photoPath = photoPath
							+ photoLists.get(i).split("/")[photoLists.get(i)
									.split("/").length - 1];
				} else {

					photoPath = photoPath
							+ photoLists.get(i).split("/")[photoLists.get(i)
									.split("/").length - 1] + ",";
				}
			}
			// 保存数据到1347
			Ctlm1347 ctlm1347 = new Ctlm1347();
			ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId());
			ctlm1347.setId_com(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getComid());
			ctlm1347.setFlag_upload("N");
			ctlm1347.setDate_opr(DateUtil.getCurDateStr());
			ctlm1347.setVar_data(Ctlm1347.VARDATA1, lists.get(index).point.x+","+lists.get(index).point.y);
			ctlm1347.setVar_data(Ctlm1347.VARDATA2, tvPosition.getText()
					.toString());
			ctlm1347.setVar_data(Ctlm1347.VARDATA3, etNode.getText() + "");
			ctlm1347.setVar_data(Ctlm1347.VARDATA4, photoPath);
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_node(businessParam.getBillNo());
			ctlm1347.setId_nodetype(WidgetName.HJ_ATTENDANCE);
			ctlm1347.setId_model(businessParam.getModelId() );
			ctlm1347.setId_view( businessParam.getViewId());
			
			
			BusinessBaseDao.replaceCTLM1347(ctlm1347);
			//存储完成，上传到服务端
			bills.clear();
			bills.add(ctlm1347.getVar_billno());
//			BusinessLua.uploadData(bills);//jhjdb_nodeuplod
//			BusinessLua.jhjdb_nodeuplod(ctlm1347.getVar_billno());
			uploadDialog dlg = new uploadDialog(this, bills);
			dlg.showDialog();

			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
