package com.mznerp.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.EapApplication;
import com.mznerp.manager.HJWebSocketManager;
import com.mznerp.util.SharePreferenceUtil;
import com.mznerp.widget.ClearEditText;
import com.mznerp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPassWordActivity extends ActionBarWidgetActivity implements OnClickListener {
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.et_pwd_first)
    ClearEditText et_input_first;
    @BindView(R.id.et_pwd_second)
    ClearEditText et_input_second;
    @BindView(R.id.et_pwd_old)
    ClearEditText et_input_old;
    @BindView(R.id.setting_password_eye)
    CheckBox setting_password_eye;
    @BindView(R.id.btn_confirm)
    Button confirm;

    private String first_input, second_input, old_input;
    private Dialog noticeDialog;
    private Thread mThread;
    private boolean iseye = false;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String mmsg = b.getString("flag");
            if (mmsg.equals("setpwd_ok")) {
                setResult(RESULT_OK);
                showFailToast("密码修改成功。");
                finish();
            } else if (mmsg.equals("setpwd_error")) {
                showFailToast("密码修改失败，请稍后重试");
            }
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onPause() {
        if (noticeDialog != null) {
            noticeDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        first_input = null;
        second_input = null;
    }

    private void initView() {
        actionCenterTv.setText(getString(R.string.setPWord_Title_TlActivity));
        actionRightTv.setVisibility(View.GONE);
        actionLeftTv.setOnClickListener(this);
        confirm.setOnClickListener(this);
        tv_id.setText(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
        et_input_old.addTextChangedListener(textWatcher);
        et_input_first.addTextChangedListener(textWatcher);
        et_input_second.addTextChangedListener(textWatcher);

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
                    showFailToast("设置失败");
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
        noticedialog_confirm_rl.setOnClickListener(this);
        noticeDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_left_tv:
                finish();
                break;
            case R.id.btn_confirm:
                if (validateInternet()) {
                    if (first_input.equals(second_input)) {
                        setPasswordThread();
                    } else {
                        showFailToast("两次输入的密码不一致");
                    }
                } else {
                    showFailToast(getResources()
                            .getString(R.string.net_connect_error));
                }
                break;
            case R.id.dialog_nc_confirm_rl:
                noticeDialog.dismiss();
                break;
        }
    }
}
