package com.mznerp.business.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.net.HttpClientManager;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.util.ZipUtils;
import com.mznerp.util.myscom.FileUtils;
import com.mznerp.widget.WaitDialogRectangle;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2016/12/2.
 */

public class HjUpload {
    private static final String DATASET_KEY = "key";
    private static final String DATASET_VALUE = "value";
    private Context context;
    private static final int UPLOAD_INFO = 10000;// 上传结果提示
    private WaitDialogRectangle waitDialogRectangle;
    public HjUpload(Context context) {
        this.context = context;
    }

    public void onhjupload(JSONArray args) {
//        int resultint = 0;
        // 上传的代码

        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
            waitDialogRectangle.dismiss();
        }
        waitDialogRectangle = new WaitDialogRectangle(context);
        waitDialogRectangle.setCanceledOnTouchOutside(false);
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在提交");
        JSONObject obj = args.optJSONObject(0);
        if (obj != null) {
            try {
                List<File> files = new ArrayList<>();
                String json = obj.getString(DATASET_KEY);
                String file_list = obj.getString(DATASET_VALUE);
                String uuid = StringUtil.getMyUUID();
                File file = new File(Constant.TEMP_DIR, uuid + ".txt");
                if (!"".equals(file_list)) {
                    for (String name : file_list.split(",")) {
                        files.add(new File("file://"+ Constant.HJPHOTO_CACHE_DIR + name));
                    }
                }
                files.add(file);
                try {
                    FileUtils.writeStringToFile(file, "business" + json, "utf-8");
                    File f = new File(Constant.TEMP_DIR + uuid + ".zip");
                    ZipUtils.zipFiles(files, f);
                    sendZipFile(Constant.TEMP_DIR + uuid + ".zip");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // for (Ctlm1347 ctlm1347 : ctlm1345ListFromCtlm1347) {
        // if (CHECKED.equals(ctlm1347.getFlag_upload())) {
        // billNoList.add(ctlm1347.getVar_billno());
        // idnodeList.add(ctlm1347.getId_node());
        // }
        // }
        // BusinessLua.uploadData(billNoList);
    }

    /**
     * @param name
     */
    private void sendZipFile(String name) {
//        final int[] resultint = {0};
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("download", new FileBody(new File(name)));
        HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS);

        LogUtils.i("上传服务地址：" + EapApplication.URL_SERVER_HOST_HTTP
                + Constant.BUSINESS_SERVICE_ADDRESS);

        httpPost.setEntity(entity);
        HttpClientManager.addTask(new HttpClientManager.HttpResponseHandler() {

            @Override
            public void onResponse(HttpResponse resp) {

                try {
                    String msga = HttpClientManager.toStringContent(resp);

                    try {
                        JSONObject jsonObject = new JSONObject(msga);
                        String result = jsonObject.getString("flag");
                        Log.v("show", "上传返回的数据:" + jsonObject.toString());
                        if ("ok".equalsIgnoreCase(result)) {
                            // 上传成功
//                            ToastUtil.ShowShort(context, "上传成功：" + result);
                            setHandlerInfoNoDialog(UPLOAD_INFO, "提交成功");
                        } else {
                            String message = jsonObject.getString("message");
                            if (message.contains("$")) {
                                int index = message.indexOf("$");
                                String messageSbf = message.substring(0, index);
                                // 上传失败
                                Log.d(context.getPackageName(), "上传失败：" + result
                                        + " 消息：" + messageSbf);
                                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败:" + messageSbf);

                            } else {
                                // 上传失败
                                Log.d(context.getPackageName(), "上传失败：" + result
                                        + " 消息：" + message);
                                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + message);

                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Log.d(context.getPackageName(), "上传失败：" + e.toString());
                        setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d(context.getPackageName(), "上传失败：" + e.toString());
                    setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

                }

            }

            @Override
            public void onException(Exception e) {
                // 上传失败
                Log.d(context.getPackageName(), "上传失败：" + e.toString());
                setHandlerInfoNoDialog(UPLOAD_INFO, "提交失败：" + e.toString());

            }
        }, httpPost);
    }

    private void setHandlerInfoNoDialog(int sginType, Object content) {
        Message msg = new Message();
        msg.what = sginType;
        msg.obj = content;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_INFO:
                    String info = (String) msg.obj;
                    if (StringUtil.isStrTrue(info)) {
                        ToastUtil.ShowLong(context, info);
                        if (waitDialogRectangle != null && waitDialogRectangle.isShowing()) {
                            waitDialogRectangle.dismiss();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public boolean compare(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date a = sdf.parse(time1);
        Date b = sdf.parse(time2);
        if (a.before(b))
            return true;
        else
            return false;
    }

    public boolean compareTime(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date a = sdf.parse(time1);
        Date b = sdf.parse(time2);
        if (a.before(b))
            return true;
        else
            return false;
    }
}
