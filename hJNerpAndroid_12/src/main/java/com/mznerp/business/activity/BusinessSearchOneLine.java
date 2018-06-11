package com.mznerp.business.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.business.BusinessAdapter.BusinessSearchAdapterOne;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.model.BusinessOneLine;
import com.mznerp.widget.ClearEditText;
import com.mznerp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mznerp.common.Constant.buss_key;
import static com.mznerp.common.Constant.buss_value;
import static com.mznerp.common.Constant.datas;

/**
 * 搜索
 */
public class BusinessSearchOneLine extends ActionBarWidgetActivity implements View.OnClickListener {
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.project_search)
    ClearEditText project_search;
    @BindView(R.id.project_recy)
    RecyclerView project_recy;
    @BindView(R.id.secah_error)
    LinearLayout secah_error;
    @BindView(R.id.text_corr)
    TextView text_corr;
    @BindView(R.id.activity_business_seach)
    RelativeLayout activity_business_seach;

    CharSequence temp;//监听前的文本
    private BusinessSearchAdapterOne adapter;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            switch (msg.what) {
                //没有关键词
                case 0:
                    getList(content, 0);
                    break;
                //有搜索词
                case 1:
                    getList(content, 1);
                    break;
                //数据为空
                case 2:
                    project_recy.setVisibility(View.GONE);
                    secah_error.setVisibility(View.VISIBLE);
                    text_corr.setText("数据为空");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_seach);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        actionCenterTv.setText("搜索");//可根据传递过来的数据进行改变
        project_recy.setLayoutManager(new LinearLayoutManager(this));
        project_search.addTextChangedListener(textWatcher);
        actionLeftTv.setOnClickListener(this);
        actionRightTv.setVisibility(View.GONE);
        setHandlerMsg(0, project_search.getText().toString());
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            setHandlerMsg(1, s.toString());

        }
    };

    private void setHandlerMsg(int numb, Object o) {
        Message message = new Message();
        message.what = numb;
        message.obj = o;
        mHandler.sendMessage(message);
    }

    public void getList(final String content, int i) {
        List<BusinessOneLine> list = null;
        if (i == 0) {
            list = datas;
        } else if (i == 1) {
            list = getProject(content);
        }
        if (list.size() > 0) {
            project_recy.setVisibility(View.VISIBLE);
            secah_error.setVisibility(View.GONE);
            adapter = new BusinessSearchAdapterOne(this, list);
            adapter.notifyDataSetChanged();
            project_recy.setAdapter(adapter);
            adapter.setOnItemClickLitener(new BusinessSearchAdapterOne.OnItemClickLitener() {
                @Override
                public void onItemClick(String item_key,
                                        String item_value) {
                    buss_key = item_key;
                    buss_value = item_value;
                    datas.clear();
                    setResult(22);
                    finish();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        } else {
            project_recy.setVisibility(View.GONE);
            secah_error.setVisibility(View.VISIBLE);
        }

    }

    private List<BusinessOneLine> getProject(String content) {
        LogShow(content);
        List<BusinessOneLine> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (content.isEmpty() || datas.get(i).getKey().contains(content) || datas.get(i).getValue().contains(content)) {
                list.add(datas.get(i));
            }
        }
        return list;
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
