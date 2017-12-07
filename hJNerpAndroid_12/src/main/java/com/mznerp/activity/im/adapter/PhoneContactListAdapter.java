package com.mznerp.activity.im.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mznerp.model.Contact;
import com.mznerp.R;

public class PhoneContactListAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater mInflater;
	private List<Contact> orginalContactList;
	private List<Contact> contactList;
	private Context mContext;

	public PhoneContactListAdapter(Context context, List<Contact> contactList) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.contactList = contactList;
	}


	@Override
	public int getCount() {
		return contactList.size();
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	@Override
	public Contact getItem(int position) {
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_item, null);

			holder = new ViewHolder();
			holder.userName_tv = (TextView) convertView
					.findViewById(R.id.contact_item_username_tv);
			holder.operation_tv = (TextView) convertView
					.findViewById(R.id.contact_item_operation_tv);
			holder.add_iv = (ImageView) convertView
					.findViewById(R.id.contact_item_operation_add_iv);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		Contact contact = (Contact) getItem(position);
		holder.userName_tv.setText(contact.getDisPlayName());
		holder.operation_tv.setText("添加");
		return convertView;
	}

	public static class ViewHolder {
		TextView userName_tv;
		TextView operation_tv;
		ImageView add_iv;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				List<Contact> filteredArrList = new ArrayList<Contact>();
				if (orginalContactList == null) {
					orginalContactList = new ArrayList<Contact>(contactList);
				}

				if (constraint == null || constraint.length() == 0) {
					filterResults.count = orginalContactList.size();
					filterResults.values = orginalContactList;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < orginalContactList.size(); i++) {
						Contact contact = orginalContactList.get(i);
						if (contact.getDisPlayName().toString()
								.startsWith(constraint.toString())) {
							filteredArrList.add(contact);
						}

					}
					filterResults.count = filteredArrList.size();
					filterResults.values = filteredArrList;
				}
				return filterResults;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence arg0,
					FilterResults results) {
				contactList = (List<Contact>) results.values;
				notifyDataSetChanged();
			}

		};
		return filter;
	}


}
