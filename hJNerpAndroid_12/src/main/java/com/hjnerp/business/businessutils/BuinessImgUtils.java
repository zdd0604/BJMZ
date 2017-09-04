package com.hjnerp.business.businessutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by Admin on 2016/11/11.
 */

public class BuinessImgUtils {

    /**
     *压缩图片
     * @param file
     * @return
     */
    public static Bitmap decodeBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        // 缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 800);
        if (scale <= 0) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        return bitmap;
    }

}
