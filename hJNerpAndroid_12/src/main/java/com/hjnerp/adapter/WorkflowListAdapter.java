package com.hjnerp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.WorkFlowBaseDao;
import com.hjnerp.fragment.WorkFragment;
import com.hjnerp.model.WorkFlowRecorderInfo;
import com.hjnerp.model.WorkflowListInfo;
import com.hjnerp.model.WorkflowRemarkResp;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.service.WebSocketService;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.Log;
import com.hjnerp.util.StringUtil;
import com.hjnerp.widget.MyToast;
import com.hjnerp.widget.MyToast2;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.itheima.roundedimageview.RoundedImageView;

import org.apache.cordova.LOG;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.hjnerp.widget.HJGridListView.TAG;

public class WorkflowListAdapter extends BaseAdapter {
    private Context context;
    private PullToRefreshListView adapterList;
    private ArrayList<WorkflowListInfo> list = new ArrayList<WorkflowListInfo>();
    private Bitmap default_user_pic;
    private WaitDialogRectangle waitDialog;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    waitDialog.dismiss();
                    Intent intent = new Intent();
                    MainActivity.WORK_COUNT = MainActivity.WORK_COUNT - 1;
                    intent.setAction(WebSocketService.ACTION_ON_MSG);
                    context.sendBroadcast(intent);
//                    ToastUtil.ShowShort(context, (String) msg.obj);
                    new MyToast2(context, (String) msg.obj);
                    break;
                case 1:
                    waitDialog.dismiss();
                    new MyToast2(context, (String) msg.obj);
                    break;

                default:
                    waitDialog.dismiss();
                    break;
            }


        }

        ;
    };

    public WorkflowListAdapter(Context context,
                               ArrayList<WorkflowListInfo> list, PullToRefreshListView adapterList) {
        this.context = context;
        if (list == null)
            list = new ArrayList<WorkflowListInfo>();
        this.list = list;
        this.adapterList = adapterList;
        default_user_pic = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.v5_0_1_profile_headphoto);
        waitDialog = new WaitDialogRectangle(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void refreshList(ArrayList<WorkflowListInfo> list) {
        if (list == null)
            list = new ArrayList<WorkflowListInfo>();
        this.list = list;
        this.notifyDataSetChanged();
        // this.adapterList.setSelection(0);
    }

    // @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // WorkflowInfo workflowbean = list.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(
                    R.layout.fragment_workflow_list, parent, false);
            viewHolder.pic = (RoundedImageView) view.findViewById(R.id.workflow_photo_iv);
            viewHolder.type = (TextView) view.findViewById(R.id.workflow_type_tv);
//            viewHolder.title = (TextView) view
//                    .findViewById(R.id.workflow_title_tv);
            viewHolder.time = (TextView) view.findViewById(R.id.workflow_time_tv);
            viewHolder.name = (TextView) view.findViewById(R.id.workflow_name_tv);
            viewHolder.content = (TextView) view.findViewById(R.id.workflow_content_tv);
            viewHolder.background = (LinearLayout) view.findViewById(R.id.workflow_bg);
            viewHolder.ll_attach = (LinearLayout) view.findViewById(R.id.ll_attach);
            viewHolder.iv_attach = (ImageView) view.findViewById(R.id.iv_attach_adapter);
            viewHolder.agree = (TextView) view.findViewById(R.id.work_button_agree);
            viewHolder.workView = view.findViewById(R.id.workView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final WorkflowListInfo info = (WorkflowListInfo) getItem(position);
        WorkFlowRecorderInfo user = info.getUser();
        String title1 = info.getTitle();
        title1 = title1.replaceAll("\\d+", "");
//        viewHolder.title.setText(title1);
        final boolean isVisible = info.isVisible();
        String time = info.getDate();
        //判断是不是今天
        Calendar c = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String calendarToday = f.format(c.getTime());
        String timeToday = time.replaceAll(" \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", "");
        if (calendarToday.equalsIgnoreCase(timeToday)) {
            time = time.replaceAll(timeToday, "今天");
            // 服务器传过来的时间有毫秒，去掉毫秒
            if (time.indexOf(".") != -1) {
                time = time.replaceAll(":\\d{2}\\.\\d{3}", "");
            }
        } else {
            // 服务器传过来的时间有毫秒，去掉毫秒
            if (time.indexOf(".") != -1) {
                //布局需要，去掉时分秒
                time = time.replaceAll(" \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", "");
            }
        }

        viewHolder.time.setText(time);
        String aName = user.getUserName().toString().trim();
        String aTitle = title1.toString().trim();
        if (StringUtil.isStrTrue(aTitle))
            viewHolder.name.setText(aTitle);
//        Log.e("show",info.toString());
        String content = info.getContent();
        Log.e(TAG, "原始：" + content);
        content = content.replace("\\n", " \n");
//        content = content.replaceAll("##", " \n");
        content = content.replace(" ", "  ");
        content = content.replaceAll(" +:", ":");

        content = content.replaceAll("#N##T#", "\n"); //"#N#"代表换行
        content = content.trim();
        Log.e(TAG, "替换后  is " + content);
        viewHolder.content.setText(content);

//        String[] a = content.split("\n");
//        android.util.Log.d("lines", a.length + "");
//
//        if (a.length <= 2) {
//            viewHolder.content.setMaxLines(a.length);
//
//        }


        // 设置头像，此头像地址取自WorkflowInfo而不是联系人表
        if (!StringUtil.isNullOrEmpty(user.getAvatar().trim())) {

            String url = ChatPacketHelper.buildImageRequestURL(
                    user.getAvatar(),
                    ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH);

            ImageLoaderHelper.displayImage(url, viewHolder.pic);
        } else {
            viewHolder.pic.setImageBitmap(default_user_pic);
        }

        // 加载附件名字和图标
        viewHolder.ll_attach.removeAllViews();
        if (info.getAttach() != null
                && !StringUtil.isNullOrEmpty(info.getAttach().trim())) {

            viewHolder.iv_attach.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_attach.setVisibility(View.GONE);
        }
        if (info.getFlagDeal().equals("Y")) {
            viewHolder.agree.setVisibility(view.GONE);
            viewHolder.workView.setVisibility(view.GONE);
            viewHolder.type.setBackgroundResource(R.drawable.work_type_text_bkg_green);
        } else {
            viewHolder.agree.setVisibility(view.VISIBLE);
            viewHolder.workView.setVisibility(view.VISIBLE);
//            viewHolder.type.setBackgroundResource(R.drawable.work_type_text_bkg_red);
            if ("R".equals(info.getOptType())) {// 审阅单
                viewHolder.type.setText("阅");
                viewHolder.type.setBackgroundResource(R.drawable.work_type_text_bkg);
            } else if ("Y".equals(info.getOptType())) {// 审批单
                viewHolder.type.setText("批");
                viewHolder.type.setBackgroundResource(R.drawable.work_type_text_bkg_red);
            } else if ("A".equals(info.getOptType())) {// 会签
                viewHolder.type.setText("签");
                viewHolder.type.setBackgroundResource(R.drawable.work_type_text_bkg_red);
            }
        }

		/*
         * if(info.getFlagDeal().equalsIgnoreCase("N"))
		 * viewHolder.background.setBackground
		 * (context.getResources().getDrawable(R.drawable.voice_rcd_btn_nor_n));
		 * else
		 * viewHolder.background.setBackgroundResource(R.drawable.voice_rcd_btn_nor
		 * );
		 */

        viewHolder.agree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isVisible) {
                    waitDialog.show();
                    HttpPost post = WorkFragment.postWorkflow(
                            Constant.WF_TYPE_OPERATE, info.getUser().getComID(),
                            info.getBillNo(), info.getBillType(), null, null, null,
                            "send", "同意", null);
                    if (post == null)
                        return;
                    HttpClientManager.addTask(new HttpClientManager.HttpResponseHandler() {
                        @Override
                        public void onResponse(HttpResponse resp) {
                            try {
                                String msg = HttpClientManager.toStringContent(resp);

                                Gson gson = new Gson();
                                WorkflowRemarkResp workflowResp = gson.fromJson(msg,
                                        WorkflowRemarkResp.class);

                                if (workflowResp == null) {
                                    ((Handler) EapApplication.getApplication().getExtra(
                                            EapApplication.EXTRA_MAIN_HANDLER))
                                            .post(new Runnable() {
                                                @Override
                                                public void run() {
//											waitDialog.dismiss();
                                                    Message message = Message.obtain();
                                                    message.obj = "网络错误，请重试！";
                                                    message.what = 1;
                                                    handler.sendMessage(message);
//                                                handler.sendEmptyMessage(1);
                                                }
                                            });
                                    Message message = Message.obtain();
                                    message.obj = "网络错误，请重试！";
                                    message.what = 1;
                                    handler.sendMessage(message);
                                    return;
                                } else {
                                    if ("result".equalsIgnoreCase(workflowResp.type)) {
                                        //成功
                                        WorkFlowBaseDao.replaceWorkFlowInfo(info);
                                        list.remove(position);
                                        Message message = Message.obtain();
                                        message.obj = "审核成功";
                                        message.what = 0;
                                        handler.sendMessage(message);
                                    } else {
                                        //失败
                                        Message message = Message.obtain();
                                        message.obj = "操作失败：" + workflowResp.data.msg;
                                        message.what = 1;
                                        handler.sendMessage(message);
                                    }
                                }

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                LOG.i("info", "同意异常" + e.toString());
                            }
                        }

                        @Override
                        public void onException(Exception e) {

                        }
                    }, post);
                } else {
//                    ToastUtil.ShowShort(context, "未获得该单据的使用权，请与软件供应商联系");
                }

            }
        });

        return view;
    }

    public static class ViewHolder {
        RoundedImageView pic; // 单据申请人头像
        ImageView iv_attach;// 附件标志
        TextView type; // 单据类型
        TextView name; // 单据申请人名称
        //        TextView title; // 单据名称
        TextView time;
        TextView content; // 单据内容
        LinearLayout ll_attach; // 附件
        LinearLayout background;
        /**
         * @author haijian 同意按钮
         */
        TextView agree;// 同意
        View workView;
    }
}
