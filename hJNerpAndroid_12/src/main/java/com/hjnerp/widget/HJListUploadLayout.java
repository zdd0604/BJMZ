package com.hjnerp.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hjnerp.business.view.WidgetAttribute;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.model.HjListData;
import com.hjnerp.util.DomJosnParse;
import com.hjnerpandroid.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HJListUploadLayout extends LinearLayout {

	public static final String TAG = "HJListUploadLayout";

	private ListView mListView;
	private MyListViewAdapter adapter;
	private ArrayList<HjListData> dataList = new ArrayList<HjListData>(); // adpter显示的数据
	private ArrayList<Ctlm1347> ctlm1347List; // 传入的1347的值
	private WidgetClass items = new WidgetClass();
	
	public void setItems(WidgetClass items){
		this.items = items;
	}
	
	public ListView getListView(){
		return this.mListView;
	}

	public HJListUploadLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public HJListUploadLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HJListUploadLayout(Context context) {
		super(context);
		initView();
	}

	public MyListViewAdapter getMyListViewAdapter() {
		return this.adapter;
	}
	
	private void makeDataFromList(ArrayList<Ctlm1347> ctlm1347List){
		dataList.clear();
		for(int datasize = 0;datasize < ctlm1347List.size(); datasize ++){
			HashMap<String, String> map = new HashMap<String, String>();
			HjListData  listdata =  new HjListData();
			try {
				if(!TextUtils.isEmpty(ctlm1347List.get(datasize).getvar_Json())){
					map = DomJosnParse.AnalysisMap( "["+ctlm1347List.get(datasize).getvar_Json() +"]") ;
					listdata.setId(ctlm1347List.get(datasize ).getId_node().toString()  ); 
					listdata.setMap(map );
					dataList.add(listdata);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} 	
//		for(int i =0;i<dataList.size();i++){
//			Log.e(TAG,"dataList >>>> " + i + "  "+ dataList.get(i).toString());
//			
//		}
	}
	
	public void setValue(ArrayList<Ctlm1347> ctlm1347List){
		makeDataFromList(ctlm1347List); 
//		adapter.refreshList(items,dataList);
		adapter = new MyListViewAdapter(items, dataList);
		mListView.setAdapter(adapter);
	}

	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.layout_hjlist, null);

		mListView = (ListView) view.findViewById(R.id.listView);
		adapter = new MyListViewAdapter(items, dataList);
		mListView.setAdapter(adapter);
		addView(view);
	}

	public class MyListViewAdapter extends BaseAdapter {
		WidgetClass adapterItems;
		ArrayList<HjListData> adapterList;

		public MyListViewAdapter(WidgetClass items,
				ArrayList<HjListData> adapterlist) {
			this.adapterItems = items;
			this.adapterList = adapterlist;
		}

		@Override
		public int getCount() {
//			Log.e(TAG,"the size is "+adapterList.size());
			return adapterList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return adapterList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void refreshList(ArrayList<HjListData> adapterList) {
			this.adapterList = adapterList;
			this.notifyDataSetChanged();
		}

		public void refreshList(WidgetClass items,
				ArrayList<HjListData> adapterList) {
			this.adapterList = adapterList;
			this.adapterItems = items;
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.layout_hjlist_item, parent, false);
				viewHolder.imageLeft = (ImageView) view
						.findViewById(R.id.imageViewLeft);
				viewHolder.imageRight = (ImageView) view
						.findViewById(R.id.imageViewRight);
				viewHolder.linearMiddle = (LinearLayout) view
						.findViewById(R.id.linear_middle);
				viewHolder.linearLeft = (LinearLayout) view
						.findViewById(R.id.linear_left);
				viewHolder.linearRight = (LinearLayout) view
						.findViewById(R.id.linear_right);

				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			Map<String, String> amap = adapterList.get(position).getMap();
			viewHolder.linearMiddle.removeAllViews();
			if (adapterItems.HJRadioButtonOption.size() > 0) {
				for (int i = 0; i < adapterItems.HJRadioButtonOption.size(); i++) {
					WidgetClass widget = adapterItems.HJRadioButtonOption
							.get(i);
					WidgetAttribute attribute = widget.attribute;
					if (adapterItems.attribute.visibledisclosure) {
						viewHolder.imageRight.setVisibility(View.VISIBLE);
					} else {
						viewHolder.imageRight.setVisibility(View.INVISIBLE);
					}
					 
					String text = amap.get(attribute.field);
					viewHolder.imageLeft.setVisibility(VISIBLE);
					viewHolder.imageLeft
					.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.round_selector_normal));

						HJListLabel label = new HJListLabel(getContext());
						label.setAttribute(widget);
						if (!"".equalsIgnoreCase(widget.name)) {
							label.setDataResource(widget.name + " : " + text);
						} else {
							label.setDataResource(text);
						}
						viewHolder.linearMiddle.addView(label);
//					}
				}
			}
			return view;
		}

		class ViewHolder {
			LinearLayout linearMiddle;
			LinearLayout linearLeft;
			LinearLayout linearRight;
			ImageView imageLeft;
			ImageView imageRight;
		}
	}

}
