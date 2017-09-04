package com.hjnerp.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.EapApplication;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

public class SetPassWordActivity extends ActivitySupport {
    private EditText et_input_old, et_input_first, et_input_second;
    private Button confirm;
    private String first_input, second_input, old_input;
    private Dialog noticeDialog;
    private Thread mThread;
    private CheckBox setting_password_eye;
    private boolean iseye = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("修改密码");
        setContentView(R.layout.setpassword);
        TextView tv = (TextView) findViewById(R.id.tv_id);
        tv.setText(SharePreferenceUtil.getInstance(
                EapApplication.getApplication().getApplicationContext())
                .getMyId());
        findView();
    }

    private void findView() {
        et_input_first = (EditText) findViewById(R.id.et_pwd_first);
        et_input_second = (EditText) findViewById(R.id.et_pwd_second);
        et_input_old = (EditText) findViewById(R.id.et_pwd_old);
        confirm = (Button) findViewById(R.id.btn_confirm);
        setting_password_eye = (CheckBox) findViewById(R.id.setting_password_eye);
        et_input_old.addTextChangedListener(textWatcher);
        et_input_first.addTextChangedListener(textWatcher);
        et_input_second.addTextChangedListener(textWatcher);
        confirm.setOnClickListener(onClickListener);
        setting_password_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 显示为普通文本
                    et_input_first.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_input_second.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_input_old.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_input_first.setSelection(first_input.length());
                    et_input_second.setSelection(second_input.length());
                    et_input_old.setSelection(old_input.length());
                } else {
                    // 显示为普通文本
                    et_input_first.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_input_second.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_input_old.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_input_first.setSelection(first_input.length());
                    et_input_second.setSelection(second_input.length());
                    et_input_old.setSelection(old_input.length());
                }
            }
        });

    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    if (validateInternet()) {
                        if (first_input.equals(second_input)) {
                            setPasswordThread();
                        } else {
                            showNoticeDialog("两次输入的密码不一致");
                        }
                    } else {
                        ToastUtil.ShowLong(SetPassWordActivity.this, getResources()
                                .getString(R.string.net_connect_error));
                    }

                    break;
                case R.id.dialog_nc_confirm_rl:
                    noticeDialog.dismiss();
                    break;
                default:
                    break;
            }

        }
    };


    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            first_input = et_input_first.getText().toString().trim();
            second_input = et_input_second.getText().toString().trim();
            old_input = et_input_old.getText().toString();
            if (first_input.length() > 0 && second_input.length() > 0
                    && old_input.length() > 0) {
                confirm.setEnabled(true);
            } else {
                confirm.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setPasswordThread() {
        mThread = new Thread() {
            @Override
            public void run() {
                String temp = et_input_old.getText().toString();
                String temp2 = et_input_first.getText().toString();
                String respond = HJWebSocketManager.getInstance()
                        .requestChangePasswd(temp.toCharArray(),
                                temp2.toCharArray());
                if (respond == null) {
                    Toast.makeText(SetPassWordActivity.this, "设置失败", Toast.LENGTH_LONG).show();
                    return;
                }
                if (respond.contains("error")) {
                    sendToHandler("setpwd_error");
                } else {
                    sendToHandler("setpwd_ok");
                }
            }
        };
        mThread.start();
    }

    private void sendToHandler(String msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putString("flag", msg);
        Msg.setData(b);

        myHandler.sendMessage(Msg);
    }

    final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            Bundle b = msg.getData();
            String mmsg = b.getString("flag");
            if (mmsg.equals("setpwd_ok")) {
                setResult(RESULT_OK);
                ToastUtil.ShowShort(context, "密码修改成功。");
                finish();
            } else if (mmsg.equals("setpwd_error")) {
                showNoticeDialog("密码修改失败，请稍后重试");
            }
        }

        ;
    };

    @Override
    protected void onPause() {
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
        super.onPause();
    }

    ;

    @Override
    protected void onStop() {
        super.onStop();
        first_input = null;
        second_input = null;
    }

    private void showNoticeDialog(String msg) {
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_nocancel);
        noticeDialog.setCancelable(false);
        noticeDialog.setCanceledOnTouchOutside(false);
        RelativeLayout noticedialog_confirm_rl;
        TextView notice = (TextView) noticeDialog.findViewById(R.id.nc_notice);
        notice.setText(msg);
        noticedialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_nc_confirm_rl);
        noticedialog_confirm_rl.setOnClickListener(onClickListener);

        noticeDialog.show();
    }
}
