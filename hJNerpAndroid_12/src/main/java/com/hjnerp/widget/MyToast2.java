package com.hjnerp.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjnerpandroid.R;


/**
 * Created by YaoChen on 2017/4/18.
 */

public class MyToast2 {
    private Toast toast;

    public MyToast2(Context context, String text) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_mytoast, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(R.drawable.uc_success);
        imageView.setVisibility(View.GONE);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
