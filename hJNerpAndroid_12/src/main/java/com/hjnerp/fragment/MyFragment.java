package com.hjnerp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.activity.AboutHJActivity;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.activity.SetActivity;
import com.hjnerp.activity.SetPassWordActivity;
import com.hjnerp.activity.contact.FriendsActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.UserInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import java.io.Serializable;

public class MyFragment extends Fragment implements OnClickListener {
	private RelativeLayout rLayout_setpwd;
	private RelativeLayout rLayout_set;
	private RelativeLayout rLayout_about;
	private RelativeLayout rLayout_logout;
	private LinearLayout linear_myinfo;
	private ImageView myImageView;
	private UserInfo myinfo;
	private TextView user_head_name;
	private TextView user_head_content;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_my, container,
				false);
		rLayout_setpwd = (RelativeLayout) contextView
				.findViewById(R.id.linear_setpwd);
		rLayout_set = (RelativeLayout) contextView
				.findViewById(R.id.linear_set);
		rLayout_about = (RelativeLayout) contextView
				.findViewById(R.id.linear_about);
		rLayout_logout = (RelativeLayout) contextView
				.findViewById(R.id.linear_logout);
		//
		
		myImageView = (ImageView) contextView
				.findViewById(R.id.myphoto);
		linear_myinfo = (LinearLayout) contextView
				.findViewById(R.id.linear_myinfo);
		
		user_head_name = (TextView) contextView
				.findViewById(R.id.user_head_name);
		
		user_head_content = (TextView) contextView
				.findViewById(R.id.user_head_content);
		
		
		rLayout_setpwd.setOnClickListener(this);
		rLayout_set.setOnClickListener(this);
		rLayout_about.setOnClickListener(this);
		rLayout_logout.setOnClickListener(this);
		linear_myinfo.setOnClickListener(this);

		myinfo = QiXinBaseDao.queryCurrentUserInfo();

		if (myinfo != null) {
			String url = myinfo.userImage;
			if (!StringUtil.isNullOrEmpty(url)) {
				ImageLoaderHelper.displayImage(ChatPacketHelper
						.buildImageRequestURL(url,
								ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
						myImageView);
			}
		}

		String user_name = SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()  )
		.getMyUserName();
		String units = SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()  )
				.getComName();
		user_head_name.setText( user_name);
		user_head_content.setText( "单位："+units);
		return contextView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.linear_setpwd:
			Intent intentpas = new Intent(this.getActivity(),
					SetPassWordActivity.class);
			startActivity(intentpas);
			break;
		case R.id.linear_set:
			Intent intent1 = new Intent(this.getActivity(), SetActivity.class);
			startActivity(intent1);
			break;
		case R.id.linear_about:
			startActivity(new Intent(this.getActivity(), AboutHJActivity.class));
			break;
		case R.id.linear_logout:
            ( (MainActivity)  this.getActivity() ).LogOut(getContext());
			break;
		case R.id.linear_myinfo:

			FriendInfo friendinfo = QiXinBaseDao.queryFriendInfo(
					SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()  )
					.getMyUserId());
			if (friendinfo == null)
				return;

			Intent intent = new Intent(this.getActivity(),
					FriendsActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable(Constant.FRIEND_READ,
					(Serializable) friendinfo);
			intent.putExtras(mBundle);
			startActivity(intent);

		default:
			break;
		}
	}
	

	@Override
	 public void onResume() {
		super.onResume();
		UserInfo myinfo = QiXinBaseDao.queryCurrentUserInfo();

		if (myinfo != null && myImageView !=null) {
			String url = myinfo.userImage;
			if (!StringUtil.isNullOrEmpty(url)) {
				ImageLoaderHelper.displayImage(ChatPacketHelper
						.buildImageRequestURL(url,
								ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),
						myImageView);
			}
		}
	};

}