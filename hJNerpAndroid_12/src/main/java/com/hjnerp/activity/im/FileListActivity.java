package com.hjnerp.activity.im;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.model.FileItem;
import com.hjnerp.util.FielUtil;
import com.hjnerpandroid.R;

public class FileListActivity extends ActivitySupport {
	private ListView lv;
	private File[] files;
	private List<FileItem> lists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("文件");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.file_list);
		setupView();
	}

	private void setupView() {
		lv = (ListView) findViewById(R.id.allApps);
		files = FielUtil.getFiels();
		lists = new ArrayList<FileItem>();
		for (File f : files) {
			lists.add(new FileItem(false,f));
		}
		FielUtil.intoListView(lists, lv, this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (FielUtil.currentPath.equals("/")) {
				finish();
			} else {
				File cf = new File(FielUtil.currentPath);// 获取当前文件列表的路径对应的文件
				cf = cf.getParentFile();// 获取父目录文件
				FielUtil.currentPath = cf.getPath();// 记录当前文件列表路径
				lists.clear();
				for (File f : FielUtil.getFiles(FielUtil.currentPath)) {
					lists.add(new FileItem(false,f));
				}
				FielUtil.intoListView(lists,
						lv, this);
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat_send, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_send:
			// 发送文件
			sendFile();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);

	}

	private void sendFile() {
		//发送然后关闭当前界面
		finish();
	}
}
