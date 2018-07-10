package com.mznerp.common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mznerp.net.HttpClientManager;
import com.mznerp.widget.MyToast;
import com.mznerp.widget.MyToast2;
import com.mznerp.widget.WaitDialogRectangle;
import com.sdyy.utils.XPermissionListener;
import com.sdyy.utils.XPermissions;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Admin on 2017/8/31.
 */

public class ActionBarWidgetActivity extends ActivitySupport {

    public static Context mContext;
    //是否显示LOG 打包是可以设置false
    private boolean isShowLog = true;
    //是否授权
    public boolean isPsions = false;
    //弹框
    protected WaitDialogRectangle waitDialog;

    protected String JSON_VALUE = "values";

    protected Gson mGson;
    protected PopupWindow popupWindow;
    protected Calendar calendar = Calendar.getInstance();

    //数据请求成功后的回调事件
    public static NsyncDataConnector nsyncDataConnector;
    public HttpClientManager.HttpResponseHandler responseHandler = new NsyncDataHandler();

    public static void setNsyncDataConnector(NsyncDataConnector nsyncDataConnector) {
        ActionBarWidgetActivity.nsyncDataConnector = nsyncDataConnector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 创建部分实体
     */
    private void initView() {
        mContext = this;
        mGson = new Gson();
        waitDialog = new WaitDialogRectangle(mContext);
    }

    /**
     * 判断是否授权
     *
     * @param psions
     * @return
     */
    public boolean isPermissions(String[] psions) {
        XPermissions.getPermissions(psions, (Activity) context, new XPermissionListener() {
            @Override
            public void onAcceptAccredit() {
                isPsions = true;
            }

            @Override
            public void onRefuseAccredit(String[] results) {
                isPsions = false;
            }
        });
        return isPsions;
    }


    /**
     * 长toast
     *
     * @param content
     */
    public void toastLONG(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 短toast
     *
     * @param content
     */
    public void toastSHORT(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 打印日志
     *
     * @param content
     */
    public void LogShow(String content) {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        if (isShowLog)
            Log.e("MZ", "文件名称："+getClass().getSimpleName()
                    + "，行号：" + ste.getLineNumber()
                    + "，内容：" + content);
    }

    /**
     * 带数据跳转
     * @param to
     * @param bundle
     */
    public void intentActivity(Class to, Bundle bundle) {
        Intent intent = new Intent(mContext, to);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     *
     * @param to
     */
    public void intentActivity(Class to) {
        Intent intent = new Intent(mContext, to);
        startActivity(intent);
    }

    /**
     * 提交成功提示框
     *
     * @param content
     */
    public static void showSuccessToast(String content) {
        new MyToast(mContext, content);
    }

    /**
     * 提交失败提示框
     *
     * @param content
     */
    public static void showFailToast(String content) {
        new MyToast2(mContext, content);
    }

    public static int getStringEidth(String string) {
        TextPaint newPaint = new TextPaint();
        return (int) newPaint.measureText(string);
    }

    /**
     * 网络获取的方法
     */
    public class NsyncDataHandler extends HttpClientManager.HttpResponseHandler {
        @Override
        public void onException(Exception e) {
        }

        @Override
        public void onResponse(HttpResponse resp) {
            // TODO Auto-generated method stub
            try {

                String contentType = resp.getHeaders("Content-Type")[0].getValue();
                // if ("application/octet-stream".equals(contentType) ) {
                if (contentType.indexOf("application/octet-stream") != -1) {
                    String contentDiscreption = resp
                            .getHeaders("Content-Disposition")[0].getValue();
                    String fileName = contentDiscreption
                            .substring(contentDiscreption.indexOf("=") + 1);
                    FileOutputStream fos = new FileOutputStream(new File(
                            getExternalCacheDir(), fileName));
                    resp.getEntity().writeTo(fos);
                    fos.close();
                    LogShow("开始解压文件");
                    String json = processBusinessCompress(fileName);
                    LogShow("开始解析文件");
                    JSONObject jsonObject = new JSONObject(json);
                    String value = jsonObject.getString(JSON_VALUE);
                    LogShow("后台返回：" + value);

                    if (nsyncDataConnector != null) {
                        nsyncDataConnector.processJsonValue(value);
                    } else {
                        LogShow("回调设置失败！");
                    }
                } else {
                    if (waitDialog != null) {
                        waitDialog.dismiss();
                        showFailToast("错误!");
                    }
                    LogShow("出错了");
                }
            } catch (IllegalStateException e) {
                com.mznerp.util.Log.d(e.getMessage());
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                com.mznerp.util.Log.d(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                com.mznerp.util.Log.d(e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                com.mznerp.util.Log.d(e.getMessage());
                e.printStackTrace();
            }
        }


    }

    /**
     * 解压缩下载的zip包
     */
    public String processBusinessCompress(String fileName) {
        ZipInputStream zis = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            File f = new File(getExternalCacheDir(), fileName);
            FileInputStream fis = new FileInputStream(f);
            zis = new ZipInputStream(fis);
            ZipEntry zip = zis.getNextEntry();
            int len = 0;
            while ((len = zis.read(data)) != -1) {
                baos.write(data, 0, len);
            }
            String json = new String(baos.toByteArray(), HTTP.UTF_8);
            return json;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 选择时间
     *
     * @param editText
     */
    public void showCalendar(final TextView editText) {
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        if (month < 10 && dayOfMonth < 10) {
                            editText.setText(year + "-0" + month
                                    + "-0" + dayOfMonth);
                        } else if (month < 10 && dayOfMonth >= 10) {
                            editText.setText(year + "-0" + month
                                    + "-" + dayOfMonth);
                        } else if (month >= 10 && dayOfMonth < 10) {
                            editText.setText(year + "-" + month
                                    + "-0" + dayOfMonth);
                        } else {
                            editText.setText(year + "-" + month
                                    + "-" + dayOfMonth);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 网络数据回调接口
     */
    public interface NsyncDataConnector {
        void processJsonValue(String value);
    }

    /**
     * 发送消息
     *
     * @param mHandler
     * @param numb
     * @param o
     */
    public void setHandlerMsg(Handler mHandler, int numb, Object o) {
        Message message = new Message();
        message.what = numb;
        message.obj = o;
        mHandler.sendMessage(message);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        waitDialog.dismiss();
    }
}
