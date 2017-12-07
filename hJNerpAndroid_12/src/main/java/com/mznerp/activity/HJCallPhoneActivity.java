package com.mznerp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HJCallPhoneActivity extends ActionBarWidgetActivity implements View.OnClickListener {
    private TextView tellPhone;
    private int title;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hjcall_phone);
        ButterKnife.bind(this);
        actionRightTv.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getInt("title");
        actionCenterTv.setText(title);
        tellPhone = (TextView) findViewById(R.id.tellPhone);
        tellPhone.setOnClickListener(this);
        actionLeftTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tellPhone:
//                ShowCallDialog();
                break;
            case R.id.action_left_tv:
                finish();
                break;
        }
    }


    private void ShowCallDialog() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("是否联系和佳?")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CallPhone();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
    }

    private void CallPhone() {
        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-82870888"));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent1);
    }
}
