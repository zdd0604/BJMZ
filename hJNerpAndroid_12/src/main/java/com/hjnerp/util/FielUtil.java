package com.hjnerp.util;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hjnerp.activity.im.adapter.FileListAdapter;
import com.hjnerp.model.FileItem;

public class FielUtil {
	public static FileListAdapter adapter;

	public static String currentPath;// 用来存放书籍的路径

	// 设置全屏
	public static void setFullScreen(Activity context) {
		context.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		context.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	// // 获取分辨率
	// public static Display getFeibianlv(Activity context) {
	// Display display = new Display();
	// DisplayMetrics dm = new DisplayMetrics();
	// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
	// display.setHeight(dm.heightPixels);
	// display.setWight(dm.widthPixels);
	// return display;
	// }

	// 扫描文件夹
	public static File[] getFiels() {
		currentPath = "/";
		File[] files = new File("/").listFiles();
		return files;
	}

	public static File[] getFiles(String currentPath) {
		// TODO Auto-generated method stub
		File[] files = new File(currentPath).listFiles();
		return files;
	}

	public static void intoListView(final List<FileItem> lists,
			final ListView lv, final Context context) {
		if (lists != null)// 当文件列表不为空时
		{
			if (lists.size() == 0)// 当前目录为空
			{
				File cf = new File(currentPath);// 获取当前文件列表的路径对应的文件
				cf = cf.getParentFile();// 获取父目录文件
				currentPath = cf.getPath();// 记录当前文件列表路径
				Toast.makeText(context, "该文件夹为空！！", Toast.LENGTH_SHORT).show();
			} else {
				//
				adapter = new FileListAdapter(context, lists);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						FileItem item = adapter.getItem(arg2);
						File f = new File(item.getFile().getPath());// 获得当前点击的文件对象
						if (f.isDirectory())// 存在分支
						{
							currentPath = item.getFile().getPath();// 获取路径
							File[] fs = getFiles(currentPath);// 获取当前路径下所有子文件
							if (fs == null || fs.length == 0) {
								intoListView(null, lv, context);
							} else {

								lists.clear();
								for (File f1 : fs) {
									lists.add(new FileItem(false, f1));
								}
								intoListView(lists, lv, context);// 将子文件列表填入ListView中
							}
						} else {
							// // 是文件
							// /*
							// * 处理思路：首先判断是不是能上传的文件如果是就让字体颜色改变
							// */
							// String path = files[arg2].getAbsolutePath();
							// String bookname = files[arg2].getName();
							// long length = files[arg2].length();
							lists.get(arg2).setSelect(!lists.get(arg2).isSelect());
							adapter.refresh(lists);

						}
					}
				});
			}
		} else {
			File cf = new File(currentPath);// 获取当前文件列表的路径对应的文件
			cf = cf.getParentFile();// 获取父目录文件
			currentPath = cf.getPath();// 记录当前文件列表路径
			Toast.makeText(context, "文件夹为空", Toast.LENGTH_SHORT).show();
		}

	}

}
