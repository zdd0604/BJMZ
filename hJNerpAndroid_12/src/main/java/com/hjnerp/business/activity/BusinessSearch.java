package com.hjnerp.business.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjnerp.business.BusinessAdapter.BusinessSearchAdapter;
import com.hjnerp.business.BusinessQueryDao.BusinessQueryDao;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.Ctlm7502Json;
import com.hjnerp.model.EjMyWProj1345;
import com.hjnerp.net.HttpClientBuilder;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.myscom.StringUtils;
import com.hjnerp.widget.ClearEditText;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.hjnerp.common.Constant.dsaordbaseJsons_new;
import static com.hjnerp.common.Constant.id_terminal_for_address;
import static com.hjnerp.common.Constant.id_terminal_for_item;
import static com.hjnerp.common.Constant.sellDetails;
import static com.hjnerp.common.Constant.sellDetailsPosition;
import static com.hjnerp.common.Constant.user_myid;

public class BusinessSearch extends ActivitySupport implements View.OnClickListener {
    private ClearEditText project_search;
    private RecyclerView project_recy;
    private CharSequence temp;//监听前的文本
    private BusinessSearchAdapter adapter;
    private Context mContext;
    private LinearLayout secah_error;
    private HttpClientManager.HttpResponseHandler responseHandler = new NsyncDataHandler();
    public static final String JSON_VALUE = "values";
    public static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    private List<Ctlm7502Json> travelDatas = new ArrayList<>();
    private WaitDialogRectangle waitDialog;
    private List<DsaordbaseJson> dsaordbaseJsons = new ArrayList<>();
    private TextView text_corr;
    private List<String> terminal_ids = new ArrayList<>();
    private List<String> address_ids = new ArrayList<>();
    private List<String> address_names = new ArrayList<>();
    private List<String> orderTerminal = new ArrayList<>();
    private List<String> overclient_ids = new ArrayList<>();
    private List<String> overclient_names = new ArrayList<>();
    private List<String> dec_acaramt = new ArrayList<>();
    private List<String> var_chkparm = new ArrayList<>();
    private RelativeLayout activity_business_seach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_business_seach);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        switch (Constant.project_type) {
            case 0://项目搜索（工作日志，出差外出）
                mActionBar.setTitle("项目搜索");
                break;
            case 1://终端搜索
                mActionBar.setTitle("终端搜索");
                break;
            case 2://客户搜索
                mActionBar.setTitle("客户搜索");
                break;
            case 3://地址搜索
                mActionBar.setTitle("地址搜索");
                break;
            case 4://产品搜索
                mActionBar.setTitle("产品搜索");
                break;
        }

        mContext = this;
        initView();
    }

    private void initView() {
        project_search = (ClearEditText) findViewById(R.id.project_search);
        project_search.addTextChangedListener(textWatcher);
        project_recy = (RecyclerView) findViewById(R.id.project_recy);
        text_corr = (TextView) findViewById(R.id.text_corr);
        project_recy.setLayoutManager(new LinearLayoutManager(this));
        secah_error = (LinearLayout) findViewById(R.id.secah_error);
//        if (Constant.project_type == 3) {//保存好的地址里拿出来
//            if (orderAddress != null && orderAddress.size() > 0) {
//                for (int i1 = 0; i1 < orderAddress.size(); i1++) {
//                    Ctlm7502Json ctlm7502Json = new Ctlm7502Json();
//                    ctlm7502Json.setName_proj(orderAddress.get(i1));
//                    ctlm7502Json.setId_proj("");
//                    ctlm7502Json.setName_corr("");
//                    ctlm7502Json.setId_corr("");
//                    travelDatas.add(ctlm7502Json);
//                }
//            }
//
//        }
        if (Constant.travel) {//是不是出差外出
            if (travelDatas.size() > 0) {
                setHandlerMsg(1, project_search.getText().toString());

            } else {
                readFrom7502();
            }

        } else {
            setHandlerMsg(0, project_search.getText().toString());

        }


    }

    private void readFrom7502() {
        waitDialog = new WaitDialogRectangle(this);
        waitDialog.show();
        try {
            HttpClientBuilder.HttpClientParam param = HttpClientBuilder
                    .createParam(Constant.NBUSINESS_SERVICE_ADDRESS);
            switch (Constant.project_type) {
                case 0:
                    param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                            .addKeyValue("id_table", StringUtils.join("ctlm7502_corr"))
                            .addKeyValue("condition", "1=1");

                    break;
                case 1:
                case 2:
                    String condition = "";
                    if (!StringUtils.isEmpty(Constant.user_myid)) {
                        String cons = "1=1 and (";
                        String[] users_id = user_myid.split(",");
                        for (int i = 0; i < users_id.length; i++) {
                            if (i == users_id.length - 1) {
                                cons = cons + "id_recorder='" + users_id[i] + "')";
                            } else {
                                cons = cons + "id_recorder='" + users_id[i] + "' or ";

                            }
                        }
                        condition = cons;
                    } else {
                        condition = "1=1 and id_recorder='" + QiXinBaseDao.queryCurrentUserInfo().userID + "'";
                    }
                    param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                            .addKeyValue("id_table", StringUtils.join("terminal"))
                            .addKeyValue("condition", condition);
                    break;
                case 3:
                    if (TextUtils.isEmpty(id_terminal_for_address)) {
                        ToastUtil.ShowShort(this, "请先选择终端");
                        waitDialog.dismiss();
                        return;
                    }
                    param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                            .addKeyValue("id_table", StringUtils.join("terminal"))
                            .addKeyValue("condition", "1=1 and id_column='" + id_terminal_for_address + "'");
                    break;
                case 4:
                    if (TextUtils.isEmpty(id_terminal_for_item)) {
                        ToastUtil.ShowShort(this, "请先选择终端");
                        waitDialog.dismiss();
                        return;
                    }
//                    ToastUtil.ShowShort(this, Constant.id_terminal_for_item);
                    param.addKeyValue(Constant.BM_ACTION_TYPE, "MobileSyncDataDownload")
                            .addKeyValue("id_table", StringUtils.join("item"))
                            .addKeyValue("condition", "1=1 and id_column='" + id_terminal_for_item + "'");
                    break;
            }


            HttpClientManager.addTask(responseHandler, param.getHttpPost());

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (Constant.travel) {
                if (travelDatas != null || travelDatas.size() > 0) {
                    setHandlerMsg(1, s.toString());
                } else {
                    readFrom7502();
                }
            } else {
                setHandlerMsg(0, s.toString());
            }

        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    getWProject(content, 0);
                    break;
                case 1:
                    getWProject(content, 1);

                    break;
                case 2:
//                    ToastUtil.ShowShort(getApplicationContext(), "结果为空");
                    project_recy.setVisibility(View.GONE);
                    secah_error.setVisibility(View.VISIBLE);
                    switch (Constant.project_type) {
                        case 0://项目搜索（工作日志，出差外出）
                            text_corr.setText("找不到相关项目");
                            break;
                        case 1://终端搜索
                            text_corr.setText("找不到相关客户");
                            break;
                        case 2://客户搜索
                            text_corr.setText("找不到相关客户");
                            break;
                        case 3://地址搜索
                            text_corr.setText("找不到相关地址");
                            break;
                        case 4://产品搜索
                            text_corr.setText("找不到相关产品");
                            break;
                    }

                    break;
            }
        }
    };

    private void setHandlerMsg(int numb, Object o) {
        Message message = new Message();
        message.what = numb;
        message.obj = o;
        mHandler.sendMessage(message);
    }

    public void getWProject(final String content, int i) {
        List<EjMyWProj1345> list = null;
        if (i == 0) {
            list = BusinessQueryDao.getMyProj(content, Constant.id_wtype, Constant.MYUSERINFO.userID);

        } else if (i == 1) {
            list = getProject(content);
        }
        if (list.size() > 0) {
            project_recy.setVisibility(View.VISIBLE);
            secah_error.setVisibility(View.GONE);
            adapter = new BusinessSearchAdapter(this, list);
            adapter.notifyDataSetChanged();
            project_recy.setAdapter(adapter);
            adapter.setOnItemClickLitener(new BusinessSearchAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(String item_peoject,
                                        String item_client,
                                        String id_wproj,
                                        String id_corr,
                                        String id_terminal,
                                        String dec_acaramt,
                                        String var_chkparm,
                                        int position) {
                    Constant.item_peoject = item_peoject;
                    Constant.item_client = item_client;
                    Constant.id_wproj = id_wproj;
                    Constant.id_corr = id_corr;
                    Constant.dec_acaramt = dec_acaramt;
                    Log.e("show", "Constant.dec_acaramt:" + Constant.dec_acaramt);

                    switch (Constant.project_type) {
                        case 0:

                            break;
                        case 1:
                            dsaordbaseJsons_new = new ArrayList<DsaordbaseJson>();
                            for (int i = 0; i < dsaordbaseJsons.size(); i++) {
                                if (dsaordbaseJsons.get(i).getId_terminal().equalsIgnoreCase(id_wproj) && dsaordbaseJsons.get(i).getId_corr().equalsIgnoreCase(id_corr)) {
                                    dsaordbaseJsons_new.add(dsaordbaseJsons.get(i));
                                    com.hjnerp.util.Log.d(dsaordbaseJsons_new.toString());
                                }
                            }
                            break;
                        case 2:
                            dsaordbaseJsons_new = new ArrayList<DsaordbaseJson>();
                            for (int i = 0; i < dsaordbaseJsons.size(); i++) {
                                if (dsaordbaseJsons.get(i).getId_corr().equalsIgnoreCase(id_wproj)) {
                                    dsaordbaseJsons_new.add(dsaordbaseJsons.get(i));
                                    com.hjnerp.util.Log.d(dsaordbaseJsons_new.toString());
                                }
                            }
                            break;
                        case 3:
                            break;
                        case 4:
                            sellDetails.get(sellDetailsPosition).setId_item(id_wproj);
                            sellDetails.get(sellDetailsPosition).setName_item(item_peoject);
                            sellDetails.get(sellDetailsPosition).setId_uom(id_corr);
                            sellDetails.get(sellDetailsPosition).setId_tax(dec_acaramt);
                            sellDetails.get(sellDetailsPosition).setVar_chkparm(var_chkparm);
                            sellDetails.get(sellDetailsPosition).setPer_price(Double.valueOf(id_terminal));
                            break;
                    }

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

    private List<EjMyWProj1345> getProject(String content) {
        com.hjnerp.util.Log.d("getProject:" + content);
        List<EjMyWProj1345> list = new ArrayList<>();
        for (int i = 0; i < travelDatas.size(); i++) {
            if (content.isEmpty() || travelDatas.get(i).getName_proj().contains(content) || travelDatas.get(i).getId_proj().contains(content) || travelDatas.get(i).getName_corr().contains(content)) {
                EjMyWProj1345 ejMyWProj1345 = new EjMyWProj1345();
                ejMyWProj1345.setName_wproj(travelDatas.get(i).getName_proj().trim());
                ejMyWProj1345.setId_wproj(travelDatas.get(i).getId_proj().trim());
                ejMyWProj1345.setName_corr(travelDatas.get(i).getName_corr().trim());
                ejMyWProj1345.setId_corr(travelDatas.get(i).getId_corr().trim());
                if (travelDatas.get(i).getId_terminal() != null)
                    ejMyWProj1345.setId_terminal(travelDatas.get(i).getId_terminal().trim());
                if (travelDatas.get(i).getName_terminal() != null)
                    ejMyWProj1345.setName_terminal(travelDatas.get(i).getName_terminal().trim());
                if (travelDatas.get(i).getDec_acaramt() != null)
                    ejMyWProj1345.setDec_acaramt(travelDatas.get(i).getDec_acaramt().trim());
                if (travelDatas.get(i).getId_proj() != null)
                    ejMyWProj1345.setId_wproj(travelDatas.get(i).getId_proj().trim());
                if (travelDatas.get(i).getVar_chkparm() != null)
                    ejMyWProj1345.setVar_chkparm(travelDatas.get(i).getVar_chkparm().trim());
                list.add(ejMyWProj1345);
            }
        }
        waitDialog.dismiss();
        return list;
    }

    private class NsyncDataHandler extends HttpClientManager.HttpResponseHandler {

        @Override
        public void onException(Exception e) {

        }

        @Override
        public void onResponse(HttpResponse resp) {
            // TODO Auto-generated method stub
            try {
                String contentType = resp.getHeaders("Content-Type")[0]
                        .getValue();
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

                    Log.d("value", value);
                    processJsonValue(value);
                } else {

                }

            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析数据，并进行筛选保存
     *
     * @param value 被解析的值
     * @throws JSONException 解析失败的异常
     */
    public void processJsonValue(String value) throws JSONException {
        // TODO Auto-generated method stub
        value = value.trim();
        if (value.equalsIgnoreCase("[]") || value.equalsIgnoreCase(null)) {//如果数据为空
            waitDialog.dismiss();
            mHandler.sendEmptyMessage(2);

            return;
        }
        JSONArray jsonArray = new JSONArray(value);
        for (int i = 0; i < jsonArray.length(); i++) {
            String temp = jsonArray.getString(i);
            Matcher m = p.matcher(temp);
            String subValue = temp.substring(temp.indexOf("{"),
                    temp.indexOf("}") + 1);
            Log.i("subValue", subValue);
            Gson gson = new Gson();
            switch (Constant.project_type) {
                case 0://出差外出的数据解析
                    Ctlm7502Json ctlm7502Json = gson.fromJson(subValue, Ctlm7502Json.class);
                    travelDatas.add(ctlm7502Json);
                    break;
                //销售订单的解析
                case 1:
                case 2:
                case 3:
                case 4:
                    DsaordbaseJson dsaordbaseJson = gson.fromJson(subValue, DsaordbaseJson.class);
                    dsaordbaseJsons.add(dsaordbaseJson);
                    break;
            }

        }
        switch (Constant.project_type) {
            case 1://终端+客户查询，过滤相同终端与客户，过滤后的数据保存
                for (int j = 0; j < dsaordbaseJsons.size(); j++) {
                    if (!overclient_names.contains(dsaordbaseJsons.get(j).getName_corr().trim() + dsaordbaseJsons.get(j).getName_terminal().trim())) {
                        terminal_ids.add(dsaordbaseJsons.get(j).getId_terminal());//终端id
                        address_ids.add(dsaordbaseJsons.get(j).getId_corr());//客户id
                        address_names.add(dsaordbaseJsons.get(j).getName_corr());//客户名
                        orderTerminal.add(dsaordbaseJsons.get(j).getName_terminal());//终端名
                        overclient_names.add(dsaordbaseJsons.get(j).getName_corr().trim() + dsaordbaseJsons.get(j).getName_terminal().trim());//过滤条件
                    }

                }
                travelDatas.clear();//清理数据，用来添加新数据
                for (int i1 = 0; i1 < orderTerminal.size(); i1++) {
                    Ctlm7502Json ctlm7502Json = new Ctlm7502Json();
                    ctlm7502Json.setName_proj(orderTerminal.get(i1));//终端名
                    ctlm7502Json.setId_proj(terminal_ids.get(i1));//终端id
                    ctlm7502Json.setId_corr(address_ids.get(i1));//客户id
                    ctlm7502Json.setName_corr(address_names.get(i1));//客户名
                    travelDatas.add(ctlm7502Json);
                }
                break;
            case 2://查询客户+地址
                for (int j = 0; j < dsaordbaseJsons.size(); j++) {
                    if (!terminal_ids.contains(dsaordbaseJsons.get(j).getId_corr())) {//客户id，过滤条件
                        terminal_ids.add(dsaordbaseJsons.get(j).getId_corr());//客户id
                        address_ids.add(dsaordbaseJsons.get(j).getVar_conplace());//地址
                        address_names.add(dsaordbaseJsons.get(j).getVar_conplace());//地址
                        orderTerminal.add(dsaordbaseJsons.get(j).getName_corr());//客户名

                    }

                }
                travelDatas.clear();
                for (int i1 = 0; i1 < orderTerminal.size(); i1++) {
                    Ctlm7502Json ctlm7502Json = new Ctlm7502Json();
                    ctlm7502Json.setName_proj(orderTerminal.get(i1));//客户名
                    ctlm7502Json.setId_proj(terminal_ids.get(i1));//客户id
                    ctlm7502Json.setId_corr(address_ids.get(i1));//地址
                    ctlm7502Json.setName_corr(address_names.get(i1));//地址
                    travelDatas.add(ctlm7502Json);
                }
                break;
            case 3://查询地址+联系人+电话
                for (int j = 0; j < dsaordbaseJsons.size(); j++) {
                    if (!orderTerminal.contains(dsaordbaseJsons.get(j).getVar_conplace().trim() + dsaordbaseJsons.get(j).getVar_contact().trim())) {
                        terminal_ids.add(dsaordbaseJsons.get(j).getVar_contact());//联系人
                        address_ids.add(dsaordbaseJsons.get(j).getVar_tel());//电话
                        address_names.add(dsaordbaseJsons.get(j).getVar_conplace());//地址
                        orderTerminal.add(dsaordbaseJsons.get(j).getVar_conplace().trim() + dsaordbaseJsons.get(j).getVar_contact().trim());//过滤条件：地址+联系人
                    }

                }
                travelDatas.clear();
                for (int i1 = 0; i1 < address_names.size(); i1++) {
                    Ctlm7502Json ctlm7502Json = new Ctlm7502Json();
                    ctlm7502Json.setName_proj(address_names.get(i1));//地址
                    ctlm7502Json.setId_proj(address_ids.get(i1));//电话
                    ctlm7502Json.setName_corr(terminal_ids.get(i1));//联系人
                    ctlm7502Json.setId_corr("");//adapter限制，这个不传会崩溃
                    travelDatas.add(ctlm7502Json);
                }

                break;
            case 4://查询产品，过滤条件，产品id
                for (int j = 0; j < dsaordbaseJsons.size(); j++) {
                    if (!terminal_ids.contains(dsaordbaseJsons.get(j).getId_item())) {
                        terminal_ids.add(dsaordbaseJsons.get(j).getId_item());//产品id
                        address_ids.add(dsaordbaseJsons.get(j).getId_uom());//型号
                        address_names.add(dsaordbaseJsons.get(j).getVar_spec());//
                        orderTerminal.add(dsaordbaseJsons.get(j).getVar_desc());
                        overclient_ids.add(dsaordbaseJsons.get(j).getDec_price());//单价
                        overclient_names.add(dsaordbaseJsons.get(j).getVar_pattern());
                        dec_acaramt.add(dsaordbaseJsons.get(j).getId_tax());//税种
                        var_chkparm.add(dsaordbaseJsons.get(j).getVar_chkparm());//
                    }

                }
                travelDatas.clear();
                for (int i1 = 0; i1 < terminal_ids.size(); i1++) {
                    Ctlm7502Json ctlm7502Json = new Ctlm7502Json();
                    ctlm7502Json.setName_proj(orderTerminal.get(i1));
                    ctlm7502Json.setId_proj(terminal_ids.get(i1));
                    ctlm7502Json.setId_corr(address_ids.get(i1));
                    ctlm7502Json.setName_corr(address_names.get(i1));
                    ctlm7502Json.setId_terminal(overclient_ids.get(i1));
                    ctlm7502Json.setName_terminal(overclient_names.get(i1));
                    ctlm7502Json.setDec_acaramt(dec_acaramt.get(i1));
                    ctlm7502Json.setVar_chkparm(var_chkparm.get(i1));
                    travelDatas.add(ctlm7502Json);
                }
                break;
        }
        if (travelDatas != null || travelDatas.size() > 0) {
            setHandlerMsg(1, project_search.getText().toString());
        }
//        } else {
//            readFrom7502();
//        }


    }


    private String processBusinessCompress(String fileName) {
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


    @Override
    public void onClick(View v) {

    }
}
