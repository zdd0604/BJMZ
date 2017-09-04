package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.activity.BusinessActivity;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetAttribute;
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

public class HJGridLayout extends LinearLayout implements HJViewInterface {

	public static enum ListViewSize {
		FULL_SCREEN, ADAPTIVE, SPECIFY
	};

	private static final String TAG = "HJGridLayout";
	private Context context;
	private HJGridScrollView scrollview;
	private HJGridScrollView bottomscrollview;
	public HorizontalScrollView mTouchView;
	private LinearLayout mLayout; // 表头的可滑动的列数
	private HJGridListView mListView;
	private LinearLayout titleLinearLayout;
	private TextView titleTextView;
	private LinearLayout first_row_colum; // 表头固定的列数
	private LinearLayout table_bottom_sum; // 表头固定的列数
	private ImageView table_bottom_line; // 表头固定的列数
	private ScrollView pscrollView;
	private LinearLayout bottom_column; // 表底的可滑动的列数
	private LinearLayout bottom_fixed_column; // 表底的固定的列数
	public static ListViewSize listViewSize;

	private Gson gson = new Gson();
	private int coloumNum; // 固定的列数目
	private List<HJGridScrollView> list = new ArrayList<HJGridScrollView>();
	private SparseArray<LinearLayout> layout_sp = new SparseArray<LinearLayout>();
	// private String nextview;
	private int columSize;
	private WidgetClass items;// <HJGrid
	private ViewClass currentviewClass;// 当前显示的View，<HJModel下面的某一个View
	private StartViewInfo startViewInfo;
	private BusinessParam businessParam = new BusinessParam(); // 当前界面上参数

	public ArrayList<Ctlm1347> ctlm1347List = new ArrayList<Ctlm1347>();

	private boolean sumenable = false;
	// private List<WidgetDataList> gridValue = new ArrayList<WidgetDataList>();

	private int currentrow = 0;
	private HJGridEditText[] bottomsum; // 合计列字段
	// private String[] billNoArray; // list有几个Item就生成几个bill_no

	// 解析后的defaultValue，每个Map内容是 "name": "货架","yesno": "N","qty": "0", "unit":
	// "瓶"

	private MyAdapter myAdapter;
	private int index = 1;
	private int flag = -1;
	private int limitofList = 8;
	private int indexofList = 0;
	private LinearLayout all_table_head;
	private LinearLayout all_table_bottom;

	/**
	 * 删除对话框
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	private Dialog noticeDialog;

	// @SuppressLint("NewApi")
	public HJGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public HJGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public HJGridLayout(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public HJGridLayout(Context context, WidgetClass items,
			ViewClass currentviewClass, StartViewInfo startViewInfo,
			ScrollView sview, BusinessParam param) {
		super(context);
		this.context = context;
		this.items = items;
		this.currentviewClass = currentviewClass;
		this.startViewInfo = startViewInfo;
		this.pscrollView = sview;
		this.businessParam = param;
		// 第一次创建完成后只显示表头

		initView();

	}

	public void saveDefaultCTLM1347() {
		if (currentviewClass.presave) {
			saveCTLM1347();
		}
	}

	public void saveCTLM1347() {
		int i = 0;
		for (Ctlm1347 ctlm1347 : ctlm1347List) {

			ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getMyId());
			ctlm1347.setId_com(SharePreferenceUtil.getInstance(
					EapApplication.getApplication().getApplicationContext())
					.getComid());

			if (StringUtil.isNullOrEmpty(ctlm1347.getId_node())) {
				ctlm1347.setId_node(StringUtil.getMyUUID());
			}
			ctlm1347.setName_node(items.name);
			ctlm1347.setVar_billno(businessParam.getBillNo());
			ctlm1347.setId_model(businessParam.getModelId());
			ctlm1347.setId_nodetype(WidgetName.HJ_GRID);
			ctlm1347.setFlag_upload("N");
			ctlm1347.setId_parentnode(businessParam.getIdParentNode());
			ctlm1347.setId_view(businessParam.getViewId());
			for (int j = 0; j < items.HJRadioButtonOption.size(); j++) {
				WidgetClass widget = items.HJRadioButtonOption.get(j);
				if ("HJTitle".equalsIgnoreCase(widget.widgetType))
					continue;
				JSONObject json = null;
				try {
					json = new JSONObject(ctlm1347.getvar_Json());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String text = "";
				try {
					if (json != null) {
						text = json.getString(widget.attribute.field);
						ctlm1347.setVar_data(widget.attribute.dbfield, text);
					} else {
						text = ctlm1347.getVar_data(widget.attribute.field);
						if (text == null) {
							text = "";
						}
						json = new JSONObject();
						json.put(widget.attribute.field, text);
						ctlm1347.setvar_Json(json.toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			ctlm1347.setId_table(items.attribute.datasource);

			ctlm1347.setId_srcnode(items.id);
			ctlm1347.setDate_opr(businessParam.getDataOpr());
			BusinessBaseDao.replaceCTLM1347(ctlm1347);
			i = i++;
		}
	}

	public void addHViews(final HJGridScrollView hScrollView) {
		if (!list.isEmpty()) {
			int size = list.size();
			HJGridScrollView scrollView = list.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			if (scrollX != 0) {
				mListView.post(new Runnable() {
					public void run() {
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		list.add(hScrollView);
	}

	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		for (HJGridScrollView scrollView : list) {
			if (mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}

	private void setListViewHeightBasedOnChildren() {
		Adapter adapter = mListView.getAdapter();
		if (adapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = adapter.getCount() < limitofList ? adapter.getCount()
				: limitofList;
		for (int i = 0; i < count; i++) {
			View view = adapter.getView(i, null, mListView);
			view.measure(0, 0);
			totalHeight += view.getMeasuredHeight();
		}
		android.view.ViewGroup.LayoutParams ll = mListView.getLayoutParams();
		ll.height = totalHeight + mListView.getDividerHeight()
				* (adapter.getCount() - 1);
		mListView.setLayoutParams(ll);
	}

	private void setListViewHeightBasedAdapter() {
		Adapter adapter = mListView.getAdapter();
		if (adapter == null) {
			return;
		}
		int count = adapter.getCount();
		int totalHeight = 0;
		for (int i = 0; i < count; i++) {
			View view = adapter.getView(i, null, mListView);
			view.measure(0, 0);
			totalHeight += view.getMeasuredHeight();
		}
		android.view.ViewGroup.LayoutParams ll = mListView.getLayoutParams();
		ll.height = totalHeight + mListView.getDividerHeight()
				* (adapter.getCount() - 1);
		mListView.setLayoutParams(ll);
	}

	// /设置表体里固定列
	private void setLinearLayoutContent(final LinearLayout layout,
			final Ctlm1347 ctlm1347a, int beginIndex, int endIndex,
			final int pos) {
		layout.removeAllViews();
		for (int i = beginIndex; i < endIndex; i++) {
			if ("HJTitle"
					.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType))
				continue;

			int[] mIntArray = { pos, i };// 行，列
			final HJGridTextView tv = new HJGridTextView(getContext(),
					mIntArray);
			tv.setId(i);
			WidgetClass item = items.HJRadioButtonOption.get(i);
			Log.i("info", "item "+i+"属性：" + item.attribute.toString());
			tv.setAttribute(items.HJRadioButtonOption.get(i));

			JSONObject json = null;
			try {
				json = new JSONObject(ctlm1347a.getvar_Json());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String text = "0";
			try {
				if (json != null) {
					text = json
							.getString(items.HJRadioButtonOption.get(i).attribute.field);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (text == null) {
				text = "";
			}
			tv.setValue(text);
			/**
			 * 添加长按删除事件
			 */
			if (items.attribute.delrow) {

				tv.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						// showNoticeDialog("操作", "确定删除");

						noticeDialog = new Dialog(getContext(),
								R.style.noticeDialogStyle);
						noticeDialog
								.setContentView(R.layout.dialog_notice_simple);

						RelativeLayout dialog_confirm_rl;
						TextView notice = (TextView) noticeDialog
								.findViewById(R.id.dialog_simple_title);
						notice.setText("操作");
						TextView confirm = (TextView) noticeDialog
								.findViewById(R.id.dialog_simple_confirm_tv);
						confirm.setText("确定删除");
						dialog_confirm_rl = (RelativeLayout) noticeDialog
								.findViewById(R.id.dialog_simple__nc_confirm_rl);
						dialog_confirm_rl
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 删除数据库里的数据
										BusinessBaseDao
												.deleteContactById_node(ctlm1347a
														.getId_node());
										// 删除这一行数据
										layout.removeView(tv);
										// 删除右侧的一行数据
										myAdapter.getList().remove(pos);
										myAdapter.setData(myAdapter.getList());
										// 删除第flag行
										noticeDialog.cancel();
									}
								});

						noticeDialog.show();

						return true;
					}
				});
			}
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (StringUtil.isNullOrEmpty(items.attribute.onclick))
						return;

					int[] tv_position = ((HJGridTextView) v).getPosition();

					try {
						String ls_column = "";
						int cut = 0;
						for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {
							if ("HJTitle"
									.equalsIgnoreCase(items.HJRadioButtonOption
											.get(i).widgetType)) {
								continue;
							}
							cut++;
							if (cut == tv_position[1]) {
								ls_column = items.HJRadioButtonOption.get(cut).id;
								break;
							}
						}

						Map<String, String> mMap = new HashMap<String, String>();
						mMap.put(HJSender.COL, String.valueOf(tv_position[0]));
						mMap.put(HJSender.COLID, ls_column);
						mMap.put(HJSender.ROW, String.valueOf(tv_position[1]));
						mMap.put(HJSender.VALUES,ctlm1347List.get(tv_position[0]).getvar_Json());

						LuaLoadScript.LoadScript();
						LuaLoadScript.runScript(items.attribute.onclick, mMap);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

			android.view.ViewGroup.LayoutParams ll = tv.getLayoutParams();
			ll.height = LayoutParams.MATCH_PARENT;
			tv.setBackgroundResource(R.drawable.gridtext_back_normal);
			layout.addView(tv);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);

	}

	// 初值创建控件
	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjgrid, null);
		scrollview = (HJGridScrollView) view.findViewById(R.id.grid_scroll);
		bottomscrollview = (HJGridScrollView) view
				.findViewById(R.id.grid_bottom_scroll);
		all_table_bottom = (LinearLayout) view
				.findViewById(R.id.all_table_bottom);
		mLayout = (LinearLayout) view.findViewById(R.id.grid_title);
		all_table_head = (LinearLayout) view.findViewById(R.id.all_table_head);
		mListView = (HJGridListView) view.findViewById(R.id.scroll_list);
		myAdapter = new MyAdapter(getLocalCtlm1347());
		mListView.setAdapter(myAdapter);
		setListViewHeightBasedOnChildren();
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount != ctlm1347List.size()) {
					myAdapter.refreshList(getLocalCtlm1347());
					setListViewHeightBasedOnChildren();
					myAdapter.notifyDataSetChanged();
					mListView.setSelection(firstVisibleItem + visibleItemCount);
				}

			}
		});

		bottom_fixed_column = (LinearLayout) view
				.findViewById(R.id.bottom_fixed_colum);
		bottom_column = (LinearLayout) view.findViewById(R.id.bottom_row_colum);
		first_row_colum = (LinearLayout) view
				.findViewById(R.id.first_row_colum);
		table_bottom_sum = (LinearLayout) view
				.findViewById(R.id.all_table_bottom);
		table_bottom_line = (ImageView) view
				.findViewById(R.id.grid_bottom_line);

		for (int i = 0; i < items.HJRadioButtonOption.size(); i++) {
			if ("HJTitle"
					.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType)) {
				titleLinearLayout = (LinearLayout) view
						.findViewById(R.id.hj_grid_title_layout);
				titleTextView = (TextView) view
						.findViewById(R.id.hj_grid_title);
				titleLinearLayout.setVisibility(View.VISIBLE);
				titleTextView.setVisibility(View.VISIBLE);
				titleTextView.setText(items.HJRadioButtonOption.get(i).name);
				settitleAttribute(items.HJRadioButtonOption.get(i));

			}
		}

		// //加入滚动
		addHViews(scrollview);

		addView(view);
		// //加入标题 显示 内容

		coloumNum = items.attribute.locking; // 锁定的列数
		columSize = items.HJRadioButtonOption.size();
		setGridHeader();
		if (sumenable) {
			table_bottom_sum.setVisibility(VISIBLE);
			table_bottom_line.setVisibility(VISIBLE);
			setGridBottomSum();
			addHViews(bottomscrollview);
		}

		mListView.setParentScrollView(pscrollView);

		setheight();

		if (items.attribute.visible) {
			this.setVisibility(View.VISIBLE);
		} else {
			this.setVisibility(View.GONE);
		}
	}

	private List<Ctlm1347> getLocalCtlm1347() {
		int lastItem = indexofList + index * limitofList <= ctlm1347List.size() ? indexofList
				+ index * limitofList
				: ctlm1347List.size();
		index++;
		// Log.e(TAG,"lastItem is "+lastItem);
		return ctlm1347List.subList(indexofList, lastItem);
	}

	private void setheight() {
		if ("fullscreen".equalsIgnoreCase(items.attribute.layouttype)) // 全屏显示
		{
			listViewSize = ListViewSize.FULL_SCREEN;
			WindowManager windowManager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			int height = windowManager.getDefaultDisplay().getHeight();
			int titleHeight = scrollview.getHeight();// TODO 表头高度
			int bottomHeight = 0;
			if (View.VISIBLE == all_table_bottom.getVisibility()) {
				bottomHeight = all_table_bottom.getHeight();
			}
			Rect frame = new Rect();
			((BusinessActivity) context).getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;// 手机状态栏高度

			int actionbar_height = (int) getResources().getDimension(
					R.dimen.abc_action_bar_default_height);// 自定义actionbar高度
			LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT,
					height - actionbar_height - statusBarHeight - bottomHeight);

			this.setLayoutParams(ll);

		}
		if ("adaptive".equalsIgnoreCase(items.attribute.layouttype)) {// 显示所有数据，listview不可滑动（scrollview可滑动）
			listViewSize = ListViewSize.ADAPTIVE;
			setListViewHeightBasedAdapter();
		}
		if ("specify".equalsIgnoreCase(items.attribute.layouttype)) {// 显示10个数据
			listViewSize = ListViewSize.SPECIFY;
			setListViewHeightBasedOnChildren();
		}

	}

	// /设置表格头
	private void setGridHeader() {
		// 设置固定列数目的头内容
		setGridHeaderLinearLayoutContent(first_row_colum, 0, coloumNum);
		// 设置滑动的表格内容
		setGridHeaderLinearLayoutContent(mLayout, coloumNum, columSize);
	}

	private void setGridHeaderLinearLayoutContent(LinearLayout layout,
			int beginIndex, int endIndex) {

		layout.removeAllViews();
		WidgetAttribute headerattri = new WidgetAttribute();
		for (int i = beginIndex; i < endIndex; i++) {

			if ("HJTitle"
					.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType))
				continue;

			if (!sumenable
					&& items.HJRadioButtonOption.get(i).attribute.sumenable) {
				sumenable = true;
			}

			HJGridHeadLabel tv = new HJGridHeadLabel(getContext());
			headerattri = items.attribute;
			headerattri.width = items.HJRadioButtonOption.get(i).attribute.width;
			tv.setAttribute(headerattri);
			android.view.ViewGroup.LayoutParams ll = tv.getLayoutParams();
			ll.height = LayoutParams.MATCH_PARENT;
			tv.setBackgroundResource(R.drawable.text_back);

			tv.setText(items.HJRadioButtonOption.get(i).name.toString());

			layout.addView(tv);
		}
	}

	private void setGridBottomSum() {
		bottomsum = new HJGridEditText[columSize];
		// 设置固定列数目的头内容
		setGridBottomLinearLayoutContent(bottom_fixed_column, 0, coloumNum);
		// 设置滑动的表格内容
		setGridBottomLinearLayoutContent(bottom_column, coloumNum, columSize);
	}

	private void setGridBottomLinearLayoutContent(LinearLayout layout,
			int beginIndex, int endIndex) {
		layout.removeAllViews();
		for (int i = beginIndex; i < endIndex; i++) {
			if ("HJTitle"
					.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType))
				continue;
			HJGridEditText tv = new HJGridEditText(getContext());
			tv.setAttribute(items.HJRadioButtonOption.get(i));
			tv.setValue("");
			tv.setTag(items.HJRadioButtonOption.get(i).id);
			tv.setBackgroundResource(R.drawable.gridtext_back_normal);
			tv.setKeyListener(null);
			bottomsum[i] = tv;
			layout.addView(tv);
		}
	}

	static class ViewHolder {
		public LinearLayout item_row_colum; // listView中固定的列数
		public HJGridScrollView scroll;
		public LinearLayout mLayout;

	}

	private class MyAdapter extends BaseAdapter {

		private List<Ctlm1347> list;

		public MyAdapter(List<Ctlm1347> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// Log.e(TAG,"the size is "+list.size());
			return list.size();
		}

		public List<Ctlm1347> getList() {
			return this.list;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setData(List<Ctlm1347> list) {
			if (list == null)
				list = new ArrayList<Ctlm1347>();
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.layout_hjgrid_item, null);
				holder.item_row_colum = (LinearLayout) convertView
						.findViewById(R.id.item_title);
				holder.scroll = (HJGridScrollView) convertView
						.findViewById(R.id.grid_scroll_item);
				holder.mLayout = (LinearLayout) convertView
						.findViewById(R.id.item_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.scroll.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// v.requestFocusFromTouch();
			// return false;
			// }
			// });
			holder.item_row_colum.removeAllViews();
			// /先设置固定列 list.get(position).getMap()
			setLinearLayoutContent(holder.item_row_colum, list.get(position),
					0, coloumNum, position);
			// /可滑动的列
			holder.mLayout.removeAllViews();
			addLinearLayout(holder.mLayout, list.get(position), position,
					coloumNum, columSize);

			// 设置滑动的可以滑动
			addHViews(holder.scroll);
			return convertView;
		}

		private void addLinearLayout(LinearLayout mLayout, Ctlm1347 ctlm1347a,
				final int pos, int ibegin, int iend) {
			// pos:表示第几行的数据 ibegin：表示开始的列
			for (int i = ibegin; i < iend; i++) {
				if ("HJTitle"
						.equalsIgnoreCase(items.HJRadioButtonOption.get(i).widgetType))
					continue;

				String jsontxt = ctlm1347a.getvar_Json();
				String text = "";
				String dbtext = "";
				dbtext = ctlm1347a
						.getVar_data(items.HJRadioButtonOption.get(i).attribute.dbfield);

				if (!StringUtil.isNullOrEmpty(dbtext)) {
					Map<String, Object> map = null;
					text = dbtext;
					map = (Map<String, Object>) gson.fromJson(jsontxt,
							Object.class);
					map.put(items.HJRadioButtonOption.get(i).attribute.field,
							dbtext);
					ctlm1347a.setvar_Json(gson.toJson(map));
				} else {
					if (!StringUtil.isNullOrEmpty(jsontxt)) {
						Map<String, Object> map = null;
						map = (Map<String, Object>) gson.fromJson(jsontxt,
								Object.class);
						if (map != null && !map.isEmpty()) {
							text = (String) map.get(items.HJRadioButtonOption
									.get(i).attribute.field);
						}
					}
				}

				if ("checkbox".equalsIgnoreCase(items.HJRadioButtonOption
						.get(i).attribute.columntype)) {
					HjGridCheckBox tv = new HjGridCheckBox(getContext());
					tv.setAttribute(items.HJRadioButtonOption.get(i));
					tv.setCtlm1347(ctlm1347a);
					if (pos % 2 == 1) {
						tv.setBackgroundResource(R.drawable.gridcell);
					} else {
						tv.setBackgroundResource(R.drawable.gridcell2);
					}

					mLayout.addView(tv);
				} else if ("date".equalsIgnoreCase(items.HJRadioButtonOption
						.get(i).attribute.columntype)) {
					/**
					 * @author haijian 增加date控件
					 */
					HjGridDate tv = new HjGridDate(getContext());
					tv.setAttribute(items.HJRadioButtonOption.get(i));
					if (pos % 2 == 1) {
						tv.setBackgroundResource(R.drawable.gridcell);
					} else {
						tv.setBackgroundResource(R.drawable.gridcell2);
					}
					// tv.setCtlm1347(ctlm1347a);
					mLayout.addView(tv);
				} else {
					if (items.HJRadioButtonOption.get(i).attribute.editable) {
						int[] mIntArray = { pos, i };// 行，列
						HJGridEditText tv = new HJGridEditText(getContext(),
								mIntArray);
						// tv.setId(i);
						tv.setAttribute(items.HJRadioButtonOption.get(i));
						tv.setValue(text);
						tv.setPadding(10, 10, 10, 10);
						tv.setCtlm1347(ctlm1347a);
						if (pos % 2 == 1) {
							tv.setBackgroundResource(R.drawable.gridcell);
						} else {
							tv.setBackgroundResource(R.drawable.gridcell2);
						}

						mLayout.addView(tv);
					} else {
						int[] mIntArray = { pos, i };// 行，列
						HJGridTextView tv = new HJGridTextView(getContext(),
								mIntArray);
						// tv.setId(i);
						tv.setAttribute(items.HJRadioButtonOption.get(i));
						tv.setValue(text);
						tv.setPadding(10, 10, 10, 10);

						tv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (items.attribute.onclick != null) {
									int[] tv_position = ((HJGridTextView) v)
											.getPosition();

									try {
										String ls_column = "";
										int cut = 0;
										for (int i = 0; i < items.HJRadioButtonOption
												.size(); i++) {
											if ("HJTitle"
													.equalsIgnoreCase(items.HJRadioButtonOption
															.get(i).widgetType)) {
												continue;
											}
											cut++;
											if (cut == tv_position[1]) {
												ls_column = items.HJRadioButtonOption
														.get(cut).id;
												break;
											}
										}
										Map<String, String> mMap = new HashMap<String, String>();
										mMap.put(HJSender.COL,
												String.valueOf(tv_position[1]));

										mMap.put(HJSender.COLID, ls_column);
										mMap.put(HJSender.ROW,
												String.valueOf(tv_position[0]));
										mMap.put(HJSender.VALUES, ctlm1347List
												.get(tv_position[0])
												.getvar_Json());
										mMap.put(HJSender.BILLNO, ctlm1347List
												.get(tv_position[0])
												.getVar_billno());
										mMap.put(HJSender.NODEID, ctlm1347List
												.get(tv_position[0])
												.getId_node());

										LuaLoadScript.LoadScript();
										LuaLoadScript.runScript(
												items.attribute.onclick, mMap);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						});
						if (pos % 2 == 1) {
							tv.setBackgroundResource(R.drawable.gridcell);
						} else {
							tv.setBackgroundResource(R.drawable.gridcell2);
						}

						mLayout.addView(tv);
					}

				}

			}
			layout_sp.put(pos, mLayout);
		}

		public void refreshList(List<Ctlm1347> dataList) {
			this.list = dataList;
			this.notifyDataSetChanged();
		}

	}

	private void sumall() {
		for (int i = 0; i < columSize; i++) {
			// /如果当前列是合计列，则合计出来数据
			if (items.HJRadioButtonOption.get(i).attribute.sumenable) {
				sumcolumn(items.HJRadioButtonOption.get(i).id,
						items.HJRadioButtonOption.get(i).attribute.field);
			}
		}
	}

	private void sumcolumn(String columnid, String field) {
		// bottomsum
		double dec = 0;

		for (int i = 0; i < ctlm1347List.size(); i++) {
			JSONObject json = null;
			try {
				json = new JSONObject(ctlm1347List.get(i).getvar_Json());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String text = "0";
			try {
				text = json.getString(field);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (text == null)
				text = "0";

			dec = dec + Double.parseDouble(text);
		}

		for (int i = 0; i < bottomsum.length; i++) {
			if (columnid.equals(bottomsum[i].getTag())) {
				bottomsum[i].setValue(String.valueOf(dec));
				break;
			}
		}

	}

	@Override
	public void setValue(String msg) {
		// TODO Auto-generated method stub
		// /表格此功能不做，
	}

	// /初始值调用
	@Override
	public void setValueDefault() {
		// TODO Auto-generated method stub
		String jsonstring = items.defaultValue;
		if (TextUtils.isEmpty(jsonstring))
			return;
		try {
			JSONArray jsonArray = new JSONArray(jsonstring);
			for (int i = 0; i < jsonArray.length(); i++) {
				// 处理每一行的数据
				Ctlm1347 ctlm1347 = new Ctlm1347();
				ctlm1347.setvar_Json(jsonArray.getJSONObject(i).toString());
				ctlm1347.setId_node(StringUtil.getMyUUID());
				ctlm1347.setInt_line(ctlm1347List.size() + 1);
				ctlm1347List.add(ctlm1347);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// saveCTLM1347();
		saveDefaultCTLM1347();
		// /更新ctlm1347 合界面上的数据值
		switch (listViewSize) {
		case FULL_SCREEN:
		case ADAPTIVE:
			refreshList(ctlm1347List);
			break;
		case SPECIFY:
			refreshList(getLocalCtlm1347());
			break;
		}
	}

	private void refreshList(List<Ctlm1347> dataList) {
		myAdapter.refreshList(dataList);
		// myAdapter.notifyDataSetChanged();
		sumall();
		setheight();
	}

	@Override
	public void setJesonValue(String msg) {
		// TODO Auto-generated method stub
		// /
		if (TextUtils.isEmpty(msg))
			return;
		try {
			JSONArray jsonArray = new JSONArray(msg);
			for (int i = 0; i < jsonArray.length(); i++) {
				// 处理每一行的数据
				Ctlm1347 ctlm1347 = new Ctlm1347();
				ctlm1347.setvar_Json(jsonArray.getJSONObject(i).toString());
				ctlm1347.setId_node(StringUtil.getMyUUID());
				ctlm1347List.add(ctlm1347);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		switch (listViewSize) {
		case FULL_SCREEN:
		case ADAPTIVE:
			refreshList(ctlm1347List);
			break;
		case SPECIFY:
			refreshList(getLocalCtlm1347());
			break;
		}
		// saveCTLM1347();
		saveDefaultCTLM1347();
		mListView.setSelection(0);
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
		return items.attribute.editable;
	}

	@Override
	public void setValue(String row, String column, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getValue(String row, String column) {

		int irow;
		if (Integer.valueOf(row) >= ctlm1347List.size())
			return "";

		irow = Integer.parseInt(row);
		if (irow == 0) {
			irow = currentrow;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(ctlm1347List.get(irow).getvar_Json());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int ii = 0; ii < items.HJRadioButtonOption.size(); ii++) {
			WidgetClass widget = items.HJRadioButtonOption.get(ii);
			if (column.equalsIgnoreCase(widget.id)) {
				column = widget.attribute.field;
				break;
			}
		}
		String text = "";
		try {
			text = (String) json.get(column);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (text == null) {
			text = "";
		}
		return text.trim();
	}

	@Override
	public String getRowCount() {
		// TODO Auto-generated method stub
		if (ctlm1347List.size() == 0)
			return "0";
		return String.valueOf(ctlm1347List.size() - 1);
	}

	@Override
	public String getCurrentRow() {
		// TODO Auto-generated method stub
		return String.valueOf(currentrow);
	}

	@Override
	public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {
		// TODO Auto-generated method stub

		if (flag) {
			ctlm1347List.clear();
			ctlm1347List = BusinessBaseDao.getCtlm1347List(startViewInfo.id,
					currentviewClass.id, businessParam.getIdParentNode(),
					businessParam.getBillNo(), items.id);
			if (ctlm1347List.size() > 0) {
				setValue(ctlm1347List);

			}
		} else if (ctlm1345List != null && !flag) {
			// /删除本地数据
			BusinessBaseDao.deletesrcctlm1347(businessParam.getBillNo(),
					businessParam.getViewId(), businessParam.getIdParentNode(),
					items.id);
			// /界面上的数据要清空
			setValue(ctlm1345List); // ;setctlm1345(ctlm1345List.getVar_values(),"N");
		}
	}

	private void setValue(BusinessData ctlm1345List) {
		ctlm1347List.clear();

		for (int i = 0; i < ctlm1345List.getCtlm1345().size(); i++) {
			Ctlm1347 ctlm1347 = new Ctlm1347();
			ctlm1347.setvar_Json(ctlm1345List.getCtlm1345().get(i)
					.getVar_value());
			ctlm1347.setInt_line(ctlm1347List.size() + 1);
			ctlm1347.setId_node(StringUtil.getMyUUID());
			ctlm1347.setId_table(ctlm1345List.getId_table());
			ctlm1347List.add(ctlm1347);
		}
		switch (listViewSize) {
		case FULL_SCREEN:
		case ADAPTIVE:
			refreshList(ctlm1347List);
			break;
		case SPECIFY:
			refreshList(getLocalCtlm1347());
			break;
		}
		// saveCTLM1347();
		saveDefaultCTLM1347();
	}

	private void setValue(ArrayList<Ctlm1347> ctlm1347Lista) {

		switch (listViewSize) {
		case FULL_SCREEN:
		case ADAPTIVE:
			refreshList(ctlm1347List);
			break;
		case SPECIFY:
			refreshList(getLocalCtlm1347());
			break;
		}

		mListView.setSelection(0);

	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setDataSource(String Data) {
		// TODO Auto-generated method stub
		items.attribute.datasource = Data;
		ctlm1347List.clear();
		refreshList(ctlm1347List);
	}

	public void settitleAttribute(WidgetClass item) {
		// 设置字体大小
		if (getContext().getResources().getString(R.string.text_size_large)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeLarge));
		} else if (getContext().getResources()
				.getString(R.string.text_size_medium)
				.equalsIgnoreCase(item.attribute.fontsize)) {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeMedium));
		} else {
			titleTextView.setTextSize(getContext().getResources().getInteger(
					R.integer.TextSizeSmall));
		}
		// 设置居中

		if ("left".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		if ("center".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		if ("right".equalsIgnoreCase(item.attribute.alignment)) {
			titleTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		}
		// 设置加粗
		Paint paint = titleTextView.getPaint();
		paint.setFakeBoldText(item.attribute.bold);
		// 设置是否换行
		titleTextView.setSingleLine(item.attribute.singleline);
		if (item.attribute.backgroundcolor != null
				&& !"".equalsIgnoreCase(item.attribute.backgroundcolor)) {
			titleLinearLayout.setBackgroundColor(Color
					.parseColor(item.attribute.backgroundcolor));
		}
		if (item.attribute.textcolor != null
				&& !"".equalsIgnoreCase(item.attribute.textcolor)) {
			titleTextView.setTextColor(Color
					.parseColor(item.attribute.textcolor));
		}
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
	public void addItem(String billno, String nodeid, String vlues) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
}
