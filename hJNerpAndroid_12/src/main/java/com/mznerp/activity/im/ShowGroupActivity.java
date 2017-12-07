package com.mznerp.activity.im;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.activity.im.adapter.ShowGroupListAdapter;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.Constant;
import com.mznerp.dao.QiXinBaseDao;
import com.mznerp.model.GroupInfo;
import com.mznerp.R;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowGroupActivity extends ActionBarWidgetActivity implements View.OnClickListener {
    private String TAG = "ShowGroupActivity";
    private ListView listView;
    private ShowGroupListAdapter adapter;
    private ArrayList<GroupInfo> groupInfoList;
    private RelativeLayout rl_actionbar;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		mActionBar = getSupportActionBar();
////		mActionBar.hide();
//		mActionBar.setTitle("选择群组");// 设置左上角标题
//		mActionBar.setDisplayHomeAsUpEnabled(true);
//		mActionBar.setHomeButtonEnabled(true);

        setContentView(R.layout.show_group);
        ButterKnife.bind(this);
        actionCenterTv.setText("选择群组");
        actionLeftTv.setOnClickListener(this);
        actionRightTv.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listview_showgroup);
        //		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//		TextView abTitle = (TextView) findViewById(titleId);
//		abTitle.setTextColor(getResources().getColor(R.color.actionbar_bkg_black));
//		rl_actionbar = (RelativeLayout) findViewById(R.id.actionbar_back_rl_showgroup);
//		rl_actionbar.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});

        groupInfoList = QiXinBaseDao.queryAllGroupInfo();

        adapter = new ShowGroupListAdapter(this, groupInfoList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GroupInfo groupInfo = groupInfoList.get(position);
                if ("sys".equalsIgnoreCase(groupInfo.groupType)) {
                    Intent intent = new Intent(ShowGroupActivity.this, NoticeActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constant.IM_GOUP_NEWS,
                            (Serializable) groupInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    ShowGroupActivity.this.finish();
                } else {
                    Intent intent = new Intent(ShowGroupActivity.this, ChatActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constant.IM_GOUP_NEWS,
                            (Serializable) groupInfo);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    ShowGroupActivity.this.finish();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_left_tv:
                finish();
                break;
        }
    }
}
