package com.mznerp.business;

import com.google.gson.Gson;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.dao.BusinessBaseDao;
import com.mznerp.db.SQLiteWorker;
import com.mznerp.db.SQLiteWorker.AbstractSQLable;
import com.mznerp.model.Ctlm1347JsonPrototype;
import com.mznerp.net.HttpClientBuilder;
import com.mznerp.net.HttpClientManager;
import com.mznerp.net.HttpClientManager.HttpResponseHandler;
import com.mznerp.util.Command;
import com.mznerp.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Ctlm1347UpdateAgain implements Command {
    private HttpResponseHandler responseHandler;
    private OnResultListener l;

    public Ctlm1347UpdateAgain(OnResultListener l) {
        this.l = l;
        responseHandler = new NCtlm1347RespHandler();
    }

    @Override
    public void action() {
        try {
            HttpPost post = HttpClientBuilder.createParam(Constant.NBUSINESS_SERVICE_ADDRESS).addKeyValue(Constant.BM_ACTION_TYPE, Constant.BMTYPE_CTML1347_DOWNLOAD_Again).getHttpPost();
            HttpClientManager.addTask(responseHandler, post);
        } catch (UnsupportedEncodingException e) {
            com.mznerp.util.Log.e("", e);
            if (l != null)
                l.onResult(false);
        }
    }

    String processBusinessCompress(String fileName) throws Exception {//解压缩下载的同步数据
        File file = new File(EapApplication.getApplication().getExternalCacheDir(), fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry = zis.getNextEntry();
        if (entry == null) {
            Log.e("数据文件已损坏");
        }
        int len = -1;
        byte[] bytes = new byte[512];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = zis.read(bytes, 0, bytes.length)) > 0) {
            baos.write(bytes, 0, len);
        }
        String json = new String(baos.toByteArray(), HTTP.UTF_8);
        zis.close();
        return json;
    }

    void procCompressJson(String json) {//处理压缩包中的文本为json
        Gson gson = new Gson();
        final Ctlm1347JsonPrototype proto = gson.fromJson(json, Ctlm1347JsonPrototype.class);
        SQLiteWorker.getSharedInstance().postDML(new AbstractSQLable() {
            @Override
            public void onCompleted(Object event) {
                if (!(event instanceof Throwable)) {
                    if (l != null)
                        l.onResult(true);
                } else {
                    Log.e((Throwable) event);
                    if (l != null)
                        l.onResult(false);
                }
            }

            @Override
            public Object doAysncSQL() {
                BusinessBaseDao.opCtlm1347Prototype(proto);
                return null;
            }
        });
    }

    class NCtlm1347RespHandler extends HttpClientManager.HttpResponseHandler {
        @Override
        public void onException(Exception e) {
            Log.e(e);
            if (l != null)
                l.onResult(false);
        }

        @Override
        public void onResponse(HttpResponse resp) {
            try {
                String contentType = resp.getHeaders("Content-Type")[0].getValue();
                if ("application/octet-stream".equals(contentType)) {
                    String contentDiscreption = resp.getHeaders("Content-Disposition")[0].getValue();
                    String fileName = contentDiscreption.substring(contentDiscreption.indexOf("=") + 1);
                    FileOutputStream fos = new FileOutputStream(new File(EapApplication.getApplication().getExternalCacheDir(), fileName));
                    resp.getEntity().writeTo(fos);
                    fos.close();
                    String json = processBusinessCompress(fileName);
                    procCompressJson(json);
                } else /*if("text/json".equals(contentType))*/ {
                    Log.w(Ctlm1347UpdateAgain.class.getSimpleName(), HttpClientManager.toStringContent(resp));
                    if (l != null)
                        l.onResult(false);
                }
            } catch (Exception e) {
                onException(e);
            }
        }
    }
}
