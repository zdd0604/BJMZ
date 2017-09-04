package com.hjnerp.activity.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.hjnerp.activity.im.adapter.PhoneContactListAdapter;
import com.hjnerp.business.activity.AddRouteActivity;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.model.Contact;
import com.hjnerp.widget.WaitDialog;
import com.hjnerpandroid.R;

public class PhoneContactListActivity extends ActivitySupport {
	private String TAG = "PhoneContactListActivity";
	private static final int GET_CONTACTLIST_SUCCESS = 1;
	private static final int GET_CONTACTLIST_FAILURE = 2;
	private List<Contact> contacts = new ArrayList<Contact>();
	private PhoneContactListAdapter contactListAdapter;
	private ListView contact_lv;
	private EditText contactlist_search_et;
	
	private WaitDialog waitDialog;   //for waiting 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonenumber_list);

		initWidget();
		mActionBar = getSupportActionBar(); 
		mActionBar.setDisplayHomeAsUpEnabled(true);
		waitDialog.show();
		
		startThreadToLoadContactList();
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {

			default:
				break;
			}
		}
	};

	private void initWidget() {
		waitDialog = new WaitDialog(this);
		contact_lv = (ListView) findViewById(R.id.phonecontactList_lv);
		contactlist_search_et = (EditText) findViewById(R.id.contactlist_search_et);
		contactlist_search_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Call back the Adapter with current character to Filter
				contactListAdapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		contact_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Contact contact = contacts.get(position);
				Log.e(TAG,contact.toString());
				Intent intent = new Intent(PhoneContactListActivity.this, AddRouteActivity.class);
		        intent.putExtra("result", contact.getPhoneNumber());
		        setResult(2001, intent);
		        finish(); 
			}
		});
		closeInput();
	}

	private void startThreadToLoadContactList() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				getUserPhoneContactList();
				for(int i = 0;i < contacts.size();i ++){
					Log.e(TAG,"id " + contacts.get(i).getContactId() + " name " + contacts.get(i).getDisPlayName() + " number " + contacts.get(i).getPhoneNumber() + " photos " + contacts.get(i).getPhotos() );
				}

			}
		});

		thread.start();
	}

	private void getUserPhoneContactList() {
		// 获得所有的联系人
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

			do {

				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				List<String> phoneNumbers = new ArrayList<String>();
				if (phoneCount > 0) {

					// 获得联系人的电话号码
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneNumbers.add(phoneNumber);
						} while (phones.moveToNext());
					}
					phones.close();
				}
				for (int i = 0; i < phoneNumbers.size(); i++) {
					Contact contact = new Contact();
					contact.setContactId(contactId);
					contact.setDisPlayName(disPlayName);
					contact.setPhoneNumber(phoneNumbers.get(i));
					contacts.add(contact);
				}

			} while (cur.moveToNext());

		}
		cur.close();
		sendSuccessMesg();
	}

	private void sendSuccessMesg() {
		Message msg = handler.obtainMessage();
		msg.what = GET_CONTACTLIST_SUCCESS;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			waitDialog.dismiss();
			switch (msg.what) {
			case GET_CONTACTLIST_SUCCESS:				
				for (int i = 0; i < contacts.size(); i++) {
//  	Log.e(TAG, contacts.get(i).getPhoneNumber());
					contactListAdapter = new PhoneContactListAdapter(
							PhoneContactListActivity.this, contacts);
					contact_lv.setAdapter(contactListAdapter);

				}
				break;

			case GET_CONTACTLIST_FAILURE:
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onPause() {
		super.onPause();
		waitDialog.dismiss();
	}

}
