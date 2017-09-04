package com.hjnerp.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.FileProvider;

import com.google.gson.Gson;
import com.hjnerp.common.EapApplication;
import com.hjnerp.model.ServletMessage;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.HttpClientManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VersionManager {
    public static final String PATH_SERVER_VERSION = "/servlet/versionMobileServlet";
    public static final String PATH_DOWNLOAD_APK = "hjnerpandroid.apk";

    private static final String PARAM_COM_ID = "com_id";
    private static final String PARAM_KEY = "key";
    private static final String PARAM_ACK = "ack";
    private static final String PARAM_VERSION_CODE = "version_code";
    private static final String PARAM_VERSION_NAME = "version_name";
    private static final String PARAM_ACTION_TYPE = "action_type";
    private static final String TYPE_GET_LASTEST_VERSION = "1";
    private static final String TYPE_DOWNLOAD_NEW_APK = "2";

    private long key;
    private ProgressDialog pd;
    private OnUpgradeResultListener l;

    private String currentVersionName = "";
    private String lastestVersionName = "";

    private VersionManager() {
    }

    private static final class VersionManagerHolder {
        private static final VersionManager instance = new VersionManager();
    }

    public static final VersionManager getSharedInstance() {
        return VersionManagerHolder.instance;
    }

    public final void checkVersionUpgrade(final boolean isMain, final Context context, OnUpgradeResultListener l) {
        this.l = l;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Boolean bool = hasNewVersion();
                    if (bool == null) {
                        return;
                    }
                    if (bool) {
                        showUpdateDialog(context);
                    } else {
                        showNotNewDialog(isMain, context);
                    }
                } catch (NameNotFoundException e) {
                    handleUpgradeException("程序包无法找到", new UpgradeException(e));
                    return;
                } catch (UnsupportedEncodingException e) {
                    handleUpgradeException("URL编码失败", new UpgradeException(e));
                    return;
                } catch (ClientProtocolException e) {
                    handleUpgradeException("无法连接到版本管理服务器", new UpgradeException(e));
                    return;
                } catch (IOException e) {
                    handleUpgradeException("网络或文件读写过程出现异常", new UpgradeException(e));
                    return;
                }
            }
        }).start();
    }

    public final Boolean hasNewVersion() throws NameNotFoundException, UnsupportedEncodingException, ClientProtocolException, IOException {
        int version = getCurrentVersion();
        int lastest = getLastestVersion();
        if (version < 0 || lastest < 0)
            return null;
        return lastest > version;
    }

    public final int getCurrentVersion() throws NameNotFoundException {
        EapApplication eap = EapApplication.getApplication();
        PackageManager pm = eap.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(eap.getPackageName(), 0);
        currentVersionName = pi.versionName;
        return pi.versionCode;
    }

    public final int getLastestVersion() throws UnsupportedEncodingException, ClientProtocolException, IOException {

        BasicNameValuePair comIDPair = new BasicNameValuePair(PARAM_COM_ID, SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
        BasicNameValuePair keyPair = new BasicNameValuePair(PARAM_KEY, String.valueOf(key = System.currentTimeMillis()));
        BasicNameValuePair actionPair = new BasicNameValuePair(PARAM_ACTION_TYPE, TYPE_GET_LASTEST_VERSION);
        HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP + PATH_SERVER_VERSION);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(3);
        parameters.add(comIDPair);
        parameters.add(keyPair);
        parameters.add(actionPair);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        HttpClient client = new DefaultHttpClient();
        HttpResponse resp = client.execute(httpPost);
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String content = HttpClientManager.toStringContent(resp);
            Gson gson = new Gson();
            ServletMessage message = gson.fromJson(content, ServletMessage.class);
            if (ChatConstants.iq.TYPE_ERROR.equals(message.type)) {
                String msg = message.data.get(ChatConstants.iq.DATA_KEY_MSG).toString();
                handleUpgradeException("检查不到新版本信息", new UpgradeException(msg));
                return -1;
            } else {
                long lkey = Long.parseLong((String) message.data.get(PARAM_ACK));
                lkey ^= 108108108108L;
                if (lkey == key) {
                    lastestVersionName = (String) message.data.get(PARAM_VERSION_NAME);
                    return ((Double) message.data.get(PARAM_VERSION_CODE)).intValue();
                } else {
                    handleUpgradeException("检查新版本发生异常", new UpgradeException("未知的密码,可能数据包被偷阅"));
                    return -1;
                }
            }
        } else {
            handleUpgradeException("获取最新版本信息失败", new UpgradeException());
        }
        return -1;
    }

    public final void showNotNewDialog(boolean isMain, final Context context) {
        if (isMain)
            return;
        final EapApplication eap = EapApplication.getApplication();
        Handler mainHandler = (Handler) eap.getExtra(EapApplication.EXTRA_MAIN_HANDLER);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.ShowShort(context, "已更新到最新版本");
            }
        });
    }

    public final void showUpdateDialog(final Context context) {
        final EapApplication eap = EapApplication.getApplication();
        Handler mainHandler = (Handler) eap.getExtra(EapApplication.EXTRA_MAIN_HANDLER);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer sb = new StringBuffer();
                    sb.append("当前版本:");
                    sb.append(currentVersionName);
                    sb.append("\n");
                    sb.append("最新版本:");
                    sb.append(lastestVersionName);
                    sb.append("\n");
                    new AlertDialog.Builder(context).setTitle("是否更新")
                            .setMessage(sb.toString())
                            .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showDownloadProgressDialog(context);
                                    try {
                                        execDownload(context);
                                    } catch (UnsupportedEncodingException e) {
                                        dismissProgressDialog();
                                        handleUpgradeException("URL编码失败", new UpgradeException(e));
                                        return;
                                    }
                                }
                            })
                            .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                } catch (Exception e) {
                    handleUpgradeException("版本管理器构建失败", new UpgradeException(e));
                    return;
                }
            }
        });
    }

    private void showDownloadProgressDialog(final Context context) {
        pd = new ProgressDialog(context);
        pd.setTitle("正在下载");
        pd.setMessage("请稍后...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    private void execDownload(final Context context) throws UnsupportedEncodingException {
        EapApplication eap = EapApplication.getApplication();
        BasicNameValuePair comIDPair = new BasicNameValuePair(PARAM_COM_ID, SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());
        BasicNameValuePair keyPair = new BasicNameValuePair(PARAM_KEY, String.valueOf(key));
        BasicNameValuePair actionPair = new BasicNameValuePair(PARAM_ACTION_TYPE, TYPE_DOWNLOAD_NEW_APK);
        HttpPost httpPost = new HttpPost(EapApplication.URL_SERVER_HOST_HTTP + PATH_SERVER_VERSION);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(3);
        parameters.add(comIDPair);
        parameters.add(keyPair);
        parameters.add(actionPair);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        HttpClientManager.addTask(new HttpClientManager.HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    File apkFile = new File(context.getExternalCacheDir(), PATH_DOWNLOAD_APK);
                    HttpEntity entity = resp.getEntity();
                    if (entity.isChunked() || entity.isStreaming()) {
                        String ack = resp.getFirstHeader(PARAM_ACK).getValue();
                        long lkey = Long.parseLong(ack);
                        lkey ^= 108108108108L;
                        if (lkey == key) {
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(apkFile));
                            entity.writeTo(bos);
                            bos.close();
                            Thread.sleep(200);
                            if (apkFile.length() <= 0) {
                                dismissProgressDialog();
                                handleUpgradeException("新版本应用下载失败", new UpgradeException("APK文件是空文件"));
                            } else {
                                showComfirmInstallDialog(context);
                            }
                        } else {
                            dismissProgressDialog();
                            handleUpgradeException("新版本应用下载失败", new UpgradeException("未知的密码,可能数据包被偷阅"));
                            return;
                        }
                    } else {
                        dismissProgressDialog();
                        handleUpgradeException("新版本应用下载失败", new UpgradeException("获得的不是一个流" + HttpClientManager.toStringContent(resp)));
                        return;
                    }
                } catch (Exception e) {
                    onException(e);
                }
            }

            @Override
            public void onException(final Exception e) {
                dismissProgressDialog();
                handleUpgradeException("新版本应用下载失败", new UpgradeException(e));
                return;
            }
        }, httpPost);
    }

    private void showComfirmInstallDialog(final Context context) {
        EapApplication eap = EapApplication.getApplication();
        Handler mainHandler = (Handler) eap.getExtra(EapApplication.EXTRA_MAIN_HANDLER);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                pd.cancel();
                new AlertDialog.Builder(context).setTitle("下载完成").setMessage("是否安装新应用?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                l.onUpgradeResult(true, "");
                                installNewAPK(context);
                                EapApplication.getApplication().exit();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public final void installNewAPK(final Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri downloadUri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", new File(context.getExternalCacheDir(), PATH_DOWNLOAD_APK));
//		intent.setDataAndType(Uri.fromFile(
//				new File(context.getExternalCacheDir(), PATH_DOWNLOAD_APK)
//				), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setDataAndType(downloadUri
                , "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void handleUpgradeException(final String msg, final Throwable tr) {
        Log.e(null, msg, tr);
        final EapApplication eap = EapApplication.getApplication();
        Handler mainHandler = (Handler) eap.getExtra(EapApplication.EXTRA_MAIN_HANDLER);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                l.onUpgradeResult(false, msg);
            }
        });
    }

    private void dismissProgressDialog() {
        EapApplication eap = EapApplication.getApplication();
        Handler mainHandler = (Handler) eap.getExtra(EapApplication.EXTRA_MAIN_HANDLER);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                pd.cancel();
            }
        });
    }

    public interface OnUpgradeResultListener {
        public void onUpgradeResult(boolean success, String msg);
    }

    public static class UpgradeException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public UpgradeException() {
        }

        public UpgradeException(String detailMessage) {
            super(detailMessage);
        }

        public UpgradeException(Throwable throwable) {
            super(throwable);
        }

        public UpgradeException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }
}
