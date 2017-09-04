package com.hjnerp.business.activity;
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.hjnerp.model.Ctlm1345;
import com.hjnerp.util.DomJosnParse;
import com.hjnerpandroid.R;

public class SearchRouteAdapter  extends BaseAdapter{
	
	 

	private Context context;
	private ArrayList<Ctlm1345> list  =null;
	 
	public   Map<Integer, Boolean> isSelected =new HashMap<Integer, Boolean>();   
	
	public SearchRouteAdapter(Context context, ArrayList<Ctlm1345> list,   Map<Integer, Boolean> select  ) 
	{
		this.context = context;
		if(list == null)
			list = new ArrayList<Ctlm1345>();
		this.list = list; 
		this.isSelected = select;     
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null) return null;
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void refreshList(ArrayList<Ctlm1345> list , Map<Integer, Boolean> select ) {
		if(list==null)
			list = new ArrayList<Ctlm1345>();
		this.list = list;
		this.isSelected = select;   
		this.notifyDataSetChanged(); 
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
//		if (list == null) return view;
		ViewHolder h = null;
		if (view == null) {
			h = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.layout_search_list_item, parent, false);
			h.linearLeft = (LinearLayout) view.findViewById(R.id.search_listleft);
			h.linearRight = (LinearLayout) view.findViewById(R.id.search_listright);
			h.imageRight = (ImageView) view.findViewById(R.id.search_listright_imageView); 
			view.setTag(h);
		} else {
			h = (ViewHolder) view.getTag();
		}
		////标题  显示列及名称
		h.linearLeft.removeAllViews();
		ArrayList<LinkedHashMap<String, String>>    var_valname = null ;
		try {
			var_valname  = DomJosnParse.Analysis("["+ list.get(position).getVar_valname() +"]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		////内容
		ArrayList<LinkedHashMap<String, String>> var_value= null ; 
		try {
			var_value  = DomJosnParse.Analysis("["+ list.get(position).getVar_value()+"]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<Entry<String, String>> entrySet = var_valname.get(0).entrySet(); 
		for (Entry<String, String> entry  :  entrySet )
		{
			TextView valname =  new TextView(context);			
			LayoutParams lpd =  new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			 
			lpd.setMargins(10, 3, 10, 2);
			valname.setLayoutParams(lpd);
			valname.setTextSize(16);  
			valname.setText( entry.getValue() +": " + var_value.get(0).get(entry.getKey() ) );
			h.linearLeft.addView( valname);
		}
		 
		if (isSelected.containsKey(position)&&isSelected.get(position))
		{
			h.imageRight.setBackgroundResource( R.drawable.round_selector_checked);
		}
		else
		{
			h.imageRight.setBackgroundResource( R.drawable.round_selector_normal);
		}
		return view;
	}
	

	class ViewHolder { 
		LinearLayout linearLeft;
		LinearLayout linearRight; 
		ImageView imageRight;
	}
}