package com.hjnerp.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hjnerp.net.HttpClientManager;
import com.hjnerp.widget.MyToast;
import com.hjnerp.widget.MyToast2;
import com.hjnerp.widget.WaitDialogRectangle;

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
    protected Context mContext;
    //弹框
    protected WaitDialogRectangle waitDialog;
    protected String JSON_VALUE = "values";
    protected Calendar calendar = Calendar.getInstance();
    protected WaitDialogRectangle waitDialogRectangle;
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
        waitDialog = new WaitDialogRectangle(mContext);
        waitDialogRectangle = new WaitDialogRectangle(mContext);
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
     * 短toast
     *
     * @param content
     */
    public void LogShow(String content) {
        Log.e("MZ", content);
    }


    /**
     * bundle
     *
     * @param from
     * @param to
     */
    public void intentActivity(Context from, Class to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 提交成功提示框
     *
     * @param content
     */
    public void showSuccessToast(String content) {
        new MyToast(mContext, content);
    }

    /**
     * 提交失败提示框
     *
     * @param content
     */
    public void showFailToast(String content) {
        new MyToast2(mContext, content);
    }


    //网络获取的方法
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
                    String json = processBusinessCompress(fileName);
                    JSONObject jsonObject = new JSONObject(json);
                    String value = jsonObject.getString(JSON_VALUE);

                    if (nsyncDataConnector != null) {
                        nsyncDataConnector.processJsonValue(value);
                    }
                } else {
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //解压缩下载的zip包
    public String processBusinessCompress(String fileName) {
        // TODO Auto-generated method stub
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (zis != null) {
                    zis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
        editText.setCompoundDrawables(null, null, null, null);
    }

    public interface NsyncDataConnector {
        void processJsonValue(String value);
    }
}
