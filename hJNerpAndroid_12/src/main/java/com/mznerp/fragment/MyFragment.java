package com.mznerp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.activity.AboutHJActivity;
import com.mznerp.activity.MainActivity;
import com.mznerp.activity.SetActivity;
import com.mznerp.activity.SetPassWordActivity;
import com.mznerp.activity.contact.FriendsActivity;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.FriendInfo;
import com.mznerp.model.UserInfo;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.util.StringUtil;
import com.mznerp.widget.BubbleImageView;
import com.mznerp.R;

import java.io.Serializable;

public class MyFragment extends Fragment implements OnClickListener {
	private RelativeLayout rLayout_setpwd;
	private RelativeLayout rLayout_set;
	private RelativeLayout rLayout_about;
	private RelativeLayout rLayout_logout;
	private LinearLayout linear_myinfo;
	private BubbleImageView myImageView;
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
		
		myImageView = (BubbleImageView) contextView
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