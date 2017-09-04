package com.hjnerp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.activity.work.ApprovalActivity;
import com.hjnerp.activity.work.WorkListBillTypeWindow;
import com.hjnerp.adapter.WorkflowListAdapter;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.OtherBaseDao;
import com.hjnerp.model.IDComConfig;
import com.hjnerp.model.WorkflowBillTypeData;
import com.hjnerp.model.WorkflowBillTypeInfo;
import com.hjnerp.model.WorkflowBillTypeResp;
import com.hjnerp.model.WorkflowListInfo;
import com.hjnerp.model.WorkflowListResp;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerpandroid.R;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据接口类使用WorkflowInfo
 */
@SuppressLint("HandlerLeak")
public class WorkFragment extends Fragment {
    private WorkflowListAdapter listItemAdapter;
    public static PullToRefreshListView listview;
    //    private TextView tviewType;
    private TextView btnLeft;
    private TextView btnRight;
    private WorkListBillTypeWindow workTypeWindow;
    protected static SharePreferenceUtil sputil;
    private ArrayList<WorkflowListInfo> listYN = new ArrayList<WorkflowListInfo>();

    private final int MY_GET_WORKLIST_FLAG_FIRST = 1; // 第一次从服务器刷新数据
    private final int MY_GET_WORKLIST_FLAG_SCROLL = 2; // 上拉刷新数据
    private final int MY_GET_BILLTYPE = 6; // 获取工单类型

    private final int FOOT_TAB_NUMBER_N = 1;
    private final int FOOT_TAB_NUMBER_Y = 2;
    private int foot_tab_number = 1;// 默认未处理单据

    private List<WorkflowBillTypeInfo> selectedBillTypeList = new ArrayList<WorkflowBillTypeInfo>();// 用户选中的工单类型
    private List<WorkflowBillTypeInfo> allBillTypeList = new ArrayList<WorkflowBillTypeInfo>();// 全部工单类型

    private View contextView;
    private View rightview;
    private View leftview;
    private int myGetWorkListFlag;
    private Context context;
    private int index;
    public static WorkFragment workFragment = null;
    private int j = 0;
    private int num;
    private String[] buss_types;//注册获取能使用的类型
    private List<String> bu_types = new ArrayList<>();//注册获取能使用的类型
    private boolean isAllType;//判断是否为全部类型

    @SuppressWarnings("static-access")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = this.getActivity();
        contextView = inflater.inflate(R.layout.fragment_workflow, container,
                false);
        workFragment = this;
        sputil = SharePreferenceUtil.getInstance(getActivity());
        IDComConfig idconfig = OtherBaseDao.queryReginfo(sputil.getComid());
        if (idconfig != null) {
            if (idconfig.getVar_worktable().contains(",")) {
                buss_types = idconfig.getVar_worktable().split(",");
                for (int i = 0; i < buss_types.length; i++) {
                    Log.i("buss_type", buss_types[i]);
                    bu_types.add(buss_types[i]);
                }
            } else {
                bu_types.add(idconfig.getVar_worktable());
            }

            isAllType = false;
        } else {
            isAllType = true;
        }

//        tviewType = (TextView) contextView
//                .findViewById(R.id.fragment_work_type);
        btnLeft = (TextView) contextView.findViewById(R.id.work_button_left);
        rightview = contextView.findViewById(R.id.rightview);
        leftview = contextView.findViewById(R.id.leftview);
        btnRight = (TextView) contextView.findViewById(R.id.work_button_right);
        btnLeft.setTextColor(new Color().rgb(39, 164, 227));
        btnRight.setTextColor(new Color().rgb(89, 89, 89));
        btnRight.setOnClickListener(onClickListener);
        btnLeft.setOnClickListener(onClickListener);

        listview = (PullToRefreshListView) contextView.findViewById(R.id.pull_refresh_list);
        listview.setMode(Mode.BOTH);//上下拉刷新
//        listview.setMode(Mode.PULL_FROM_START);// 仅下拉刷新

        // 如果有网络加载网络数据，并在加载成功后删除之前保留的本地数据，没有网络加载本地数据
        if (((MainActivity) getActivity()).hasInternetConnected()) {
            // 从server获取工作流类型
            getBillType(MY_GET_BILLTYPE, null, null, null, null, null);
            // 从server获取工作流列表
            getWorkFlowListFromNetFirsrTime();
        } else {
            // 获取用户选择的想要展示的工作流类型
            getBillTypeList();
        }

        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new WorkflowListAdapter(this.getActivity(), listYN, listview);
        listview.setAdapter(listItemAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                /** 获得当前的workflowInfo信息 */
                WorkflowListInfo wfInfo = getClickWorkInfo(arg2 - 1);
                if (wfInfo.isVisible()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ApprovalActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(Constant.WORK_NEWS, wfInfo);
                    intent.putExtras(mBundle);
//				startActivity(intent);
                    startActivityForResult(intent, 11);
                } else {
//                    ToastUtil.ShowShort(context, "未获得该单据的使用权，请与软件供应商联系");
                }


            }

        });
//        image_refresh_workflow.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listview.setRefreshing();
//            }
//        });

        // //上下拉刷新
        listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                num = 10;
                new GetDataTask().execute(num);
            }


            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                num += 10;
                new GetDataTask().execute(num);
            }

        });
//        listview.onRefreshComplete();

        return contextView;
    }


    private class GetDataTask extends AsyncTask<Integer, Void, String[]> {
        @Override
        protected void onPreExecute() {
//            MainActivity.newMain.unabledrefresh();
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(Integer... params) {

            switch (foot_tab_number) {
                case FOOT_TAB_NUMBER_N:
                    getWorkFlowListN(params[0]);
                    break;
                case FOOT_TAB_NUMBER_Y:
                    getWorkFlowListY(params[0]);
                    break;
                default:
                    break;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            MainActivity.newMain.enabledrefresh();
            super.onPostExecute(result);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 从本地获取全部工单类型
    private void getBillTypeList() {
        Gson gson = new Gson();
        if ("".equalsIgnoreCase(sputil.getWorkListBillType()))
            return;

        WorkflowBillTypeData workflowData = gson.fromJson(
                sputil.getWorkListBillType(), WorkflowBillTypeData.class);
        if (workflowData != null) {
            if (workflowData.items != null) {
                allBillTypeList = workflowData.items;
            }
        }
    }

    // 获取用户选中的要展示的工单类型
    private void getSelectedBillTypeList() {
        if (allBillTypeList != null) {
            selectedBillTypeList = new ArrayList<WorkflowBillTypeInfo>();
            for (int i = 0; i < allBillTypeList.size(); i++) {
                if (allBillTypeList.get(i).getIsSelected()) {
                    selectedBillTypeList.add(allBillTypeList.get(i));
                }
            }
        }
    }

    /**
     * 刷新listview中的数据
     */
    private void refreshList() {
        listItemAdapter.refreshList(listYN);
        // listItemAdapter.notifyDataSetChanged();
        listview.onRefreshComplete();
    }

    public void search(ImageView imageView) {
        getSelectedBillTypeList();
        workTypeWindow = new WorkListBillTypeWindow(getActivity(),
                onCheckedChangeListener);
        workTypeWindow.setFocusable(true);
        workTypeWindow.showAsDropDown(imageView);
    }

    // 底部tab监听
    private OnClickListener onClickListener = new OnClickListener() {
        // TODO
        @SuppressWarnings("static-access")
        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.work_button_right:
                    btnRight.setTextColor(new Color().rgb(39, 164, 227));
                    btnLeft.setTextColor(new Color().rgb(89, 89, 89));
                    foot_tab_number = FOOT_TAB_NUMBER_Y;
                    listYN.clear();
                    listItemAdapter.notifyDataSetChanged();
                    // new GetDataTask().execute();
                    listview.setRefreshing();
                    leftview.setVisibility(View.GONE);
                    rightview.setVisibility(View.VISIBLE);
                    break;
                case R.id.work_button_left:
                    btnLeft.setTextColor(new Color().rgb(39, 164, 227));
                    btnRight.setTextColor(new Color().rgb(89, 89, 89));
                    foot_tab_number = FOOT_TAB_NUMBER_N;
                    listYN.clear();
                    listItemAdapter.notifyDataSetChanged();
                    rightview.setVisibility(View.GONE);
                    leftview.setVisibility(View.VISIBLE);

                    // new GetDataTask().execute();
                    listview.setRefreshing();
                    // listview.setRefreshing(true);
                    break;
                default:
                    break;
            }

        }
    };

    // 工作流类型弹出窗口监听
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View view = workTypeWindow.getContentView();
            int radioButtonId = group.getCheckedRadioButtonId();

            // 根据ID获取RadioButton的实例
            RadioGroup radioGroup = (RadioGroup) view
                    .findViewById(R.id.rbtn_worktype);
            RadioButton rb = (RadioButton) radioGroup
                    .findViewById(radioButtonId);
            String typeId = rb.getTag().toString();

            if ("allType".equalsIgnoreCase(typeId)) {
                // TODO
                for (int i = 0; i < allBillTypeList.size(); i++) {
                    allBillTypeList.get(i).setIsSelected(false);
                    allBillTypeList.get(i).setIsChecked(false);
                }
            } else {
//                int j ;
                for (int i = 0; i < allBillTypeList.size(); i++) {
                    j++;
                    if (allBillTypeList.get(i).getId().equalsIgnoreCase(typeId)) {
                        allBillTypeList.get(i).setIsSelected(true);
                        allBillTypeList.get(i).setIsChecked(true);
                        allBillTypeList.get(i).setFrequence(j);
                    } else {
                        allBillTypeList.get(i).setIsSelected(false);
                        allBillTypeList.get(i).setIsChecked(false);
//                        allBillTypeList.get(i).setFrequence(0);
                    }
                }
            }

            WorkflowBillTypeData mbilltypelist = new WorkflowBillTypeData();
            mbilltypelist.items = allBillTypeList;
            sputil.setWorkListBillType(new Gson().toJson(mbilltypelist));

            getBillTypeList();
            getSelectedBillTypeList();
            workTypeWindow.dismiss();
            listYN.clear();
            listItemAdapter.notifyDataSetChanged();
            listview.setRefreshing();

        }
    };

    // 第一次从网络获取全部未处理单据
    private void getWorkFlowListFromNetFirsrTime() {
        getWorkFlowList(MY_GET_WORKLIST_FLAG_FIRST, null, null, "N", null, "10");
    }

    private void getWorkFlowListN(Integer integer) {

        String type = null;
//        if (listYN.size() > 0&&integer<=10) {
//            // 请求10个已处理
//            if (selectedBillTypeList.size() > 0) {
//                for (int i = 0; i < selectedBillTypeList.size(); i++) {
//                    type = selectedBillTypeList.get(i).getId();
//                }
//                getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, type, "N",
//                        listYN.get(0).getDate(), integer+"");
//            } else {
//                getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, null, "N",
//                        listYN.get(0).getDate(), integer+"");
//            }
//        } else {
        listYN.clear();
        // 请求10个已处理
        if (selectedBillTypeList.size() > 0) {
            for (int i = 0; i < selectedBillTypeList.size(); i++) {
                if (selectedBillTypeList.get(i).getIsSelected()) {
                    type = selectedBillTypeList.get(i).getId();
                }
            }
            getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, type, "N",
                    null, integer + "");
        } else {
            getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, null, "N",
                    null, integer + "");
        }
//        }

    }

    private void getWorkFlowListY(Integer integer) {
        String type = null;
//        if (listYN.size() > 0&&integer<=10) {
//            // 请求10个已处理
//            if (selectedBillTypeList.size() > 0) {
//                for (int i = 0; i < selectedBillTypeList.size(); i++) {
//                    type = selectedBillTypeList.get(i).getId();
//
//                }
//                getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, type, "Y",
//                        listYN.get(0).getDate(), integer+"");
//            } else {
//                getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, null, "Y",
//                        listYN.get(0).getDate(), integer+"");
//            }
//        } else {
        listYN.clear();
        // 请求10个已处理
        if (selectedBillTypeList.size() > 0) {
            for (int i = 0; i < selectedBillTypeList.size(); i++) {
                if (selectedBillTypeList.get(i).getIsSelected()) {
                    type = selectedBillTypeList.get(i).getId();
                }
            }
            getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, type, "Y",
                    null, integer + "");
        } else {
            getWorkFlowList(MY_GET_WORKLIST_FLAG_SCROLL, null, null, "Y",
                    null, integer + "");
        }
//        }
    }

    // TODO 获取工单类型
    private void getBillType(int myFlag, String comID, String billType,
                             String dealFlag, String markDate, String pageSize) {

        HttpPost post = postWorkflow(Constant.WF_TYPE_CATEGORY, comID, null,
                null, null, null, null, null, null, null);
        if (post == null)
            return;

        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        return;
                    }
                    String msg = HttpClientManager.toStringContent(resp);
                    Gson gson = new Gson();
                    WorkflowBillTypeResp workflowResp = gson.fromJson(msg, WorkflowBillTypeResp.class);
                    if ("result".equalsIgnoreCase(workflowResp.type)
                            && workflowResp.data != null
                            && workflowResp.data.items.size() > 0) {

                        WorkflowBillTypeData data = new WorkflowBillTypeData();
                        data.items = workflowResp.data.items;
                        sputil.setWorkListBillType(new Gson().toJson(data));
                        allBillTypeList = workflowResp.data.items;

                    }
                } catch (IOException e) {
                    onException(e);

                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }, post);
    }

    /**
     * 获得后台的工作流数据
     */
    private void getWorkFlowList(int myFlag, String comID, String billType,
                                 String dealFlag, String markDate, String pageSize) {
        myGetWorkListFlag = myFlag;
        HttpPost post = postWorkflow(Constant.WF_TYPE_LIST, comID, null,
                billType, dealFlag, markDate, pageSize, null, null, null);
        if (post == null)
            return;
        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    String msg = HttpClientManager.toStringContent(resp);
                    Gson gson = new Gson();

                    final WorkflowListResp workflowResp = gson.fromJson(msg, WorkflowListResp.class);
                    if ("result".equalsIgnoreCase(workflowResp.type)
                            && workflowResp.data != null
                            && workflowResp.data.items.size() > 0) {
                        int iSize = workflowResp.data.items.size() - 1;
                        for (int i = iSize; i >= 0; i--)// WorkflowListInfo info
                        // :
                        // workflowResp.data.items)
                        // {
                        {
                            WorkflowListInfo info = workflowResp.data.items.get(i);
                            if (isAllType || bu_types.contains(info.getBillType())) {//不再判断是否可操作全部
//                            if (bu_types.contains(info.getBillType())) {
                                info.setVisible(true);
                            } else {
                                info.setVisible(false);
                            }
                            listYN.add(0, info);
                        }
                        sendToMyHandler(1);
                    } else {
                        if (myGetWorkListFlag == 1) {
                            sendToMyHandler(2);
                        } else {
                            sendToMyHandler(999);
                        }

                    }

                } catch (IOException e) {
                    onException(e);
                    sendToMyHandler(999);

                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
                // //
                sendToMyHandler(999);

            }
        }, post);
    }

    private void sendToMyHandler(int msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putInt("flag", msg);
        Msg.setData(b);

        myHandler.sendMessage(Msg);
    }

    final Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            int mmsg = b.getInt("flag");

            switch (mmsg) {
                case 1:
                    refreshList();
                    break;
                case 2:
                    listview.onRefreshComplete();
                    break;
                case 999:
                    listview.onRefreshComplete();
                    ToastUtil.ShowShort(context, "当前没有您需要处理的单据");
                    break;
                case 4:
                    listYN.remove(index);
                    refreshList();
                    break;
                default:
                    break;
            }

        }

        ;
    };

    // pageSize 为null时，请求全部，dealFlag是已处理未处理Y/N dealType-审批结果 remark-附加审批意见
    public static final HttpPost postWorkflow(String type, String comID,
                                              String billNo, String billType, String dealFlag, String markDate,
                                              String pageSize, String dealType, String remark, String fileID) {
        try {
            HttpPost httpPost = new HttpPost(
                    EapApplication.URL_SERVER_HOST_HTTP
                            + "/servlet/nworkflowMobileServlet");
            // httpPost.addHeader("Accept-Encoding",
            // "gzip; q=1.0, identity; q=0.5, *; q=0");
            ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("com_id", comID));
            params.add(new BasicNameValuePair("session_id", sputil
                    .getMySessionId()));
            params.add(new BasicNameValuePair("user_id", sputil.getMyId()));

            params.add(new BasicNameValuePair("type", type));
            if (StringUtils.isNotBlank(billNo))
                params.add(new BasicNameValuePair("bill_no", billNo));
            if (StringUtils.isNotBlank(billType))
                params.add(new BasicNameValuePair("bill_type", billType));
            if (StringUtils.isNotBlank(dealFlag))
                params.add(new BasicNameValuePair("flag_deal", dealFlag));
            if (StringUtils.isNotBlank(markDate))
                params.add(new BasicNameValuePair("mark_date", markDate));
            if (StringUtils.isNotBlank(pageSize))
                params.add(new BasicNameValuePair("page_size", pageSize));
            if (StringUtils.isNotBlank(dealType))
                params.add(new BasicNameValuePair("deal_op", dealType));
            if (StringUtils.isNotBlank(remark))
                params.add(new BasicNameValuePair("remark", remark));
            if (StringUtils.isNotBlank(fileID))
                params.add(new BasicNameValuePair("file_id", fileID));
            LogUtils.i("params== " + params.toString());
            Log.i("info", "工作流：" + params.toString());
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(entity);
//            HttpClient client = new DefaultHttpClient();
//            // 请求超时
//            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
//            // 读取超时
//            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
            return httpPost;
//            HttpResponse response = client.execute(httpPost);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                return httpPost;
//            }else {
//                ToastUtil.ShowShort(workFragment.context,"网络超时");
//                return null;
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    // 获得WorkInfo

    private WorkflowListInfo getClickWorkInfo(int position) {
        WorkflowListInfo info = null;
        index = position;
        info = listYN.get(position);
        return info;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // //界面返回值
        /**
         * @author haijian
         * 收到返回的值判断是否成功，如果同意就将数据移除刷新列表
         */
        if (requestCode == 11 && resultCode == 22) {
            sendToMyHandler(4);
            MainActivity.WORK_COUNT = MainActivity.WORK_COUNT - 1;
        } else if (requestCode == 11 && resultCode == 33) {
            sendToMyHandler(4);
            MainActivity.WORK_COUNT = MainActivity.WORK_COUNT + 1;
        }
    }
}