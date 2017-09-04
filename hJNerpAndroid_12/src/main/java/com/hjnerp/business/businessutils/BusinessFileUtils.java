package com.hjnerp.business.businessutils;

import android.content.Context;
import android.util.Log;

import com.hjnerp.common.Constant;
import com.hjnerp.util.ToastUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zdd on 2016/11/11.
 * 对文件进行操作：
 * 新建、压缩、删除、
 */

public class BusinessFileUtils {

    /**
     * 删除指定文件夹的内容
     *
     * @param delUrl
     */
    public static void deleteFiles(String delUrl) {
        // 删除之前某些原因没有删除干净的图片
        File filepath = new File(delUrl);
        if (filepath.exists()) {
            String[] strings = filepath.list();
            for (int i = 0; i < strings.length; i++) {
                File delFile = new File(delUrl + "/" + strings[i]);
                Log.v("show", Constant.SGIN_SAVE_DIR + "/" + strings[i]);
                delFile.delete();
            }
            filepath.delete();
        } else {
            filepath.mkdirs();
        }
    }

    /**
     * 删除指定文件夹的内容
     *
     * @param delUrl
     */
    public static void deleteFile(String delUrl) {
        // 删除之前某些原因没有删除干净的图片
        File filepath = new File(delUrl);
        if (filepath.exists()) {
            filepath.delete();
        } else {
            Log.v("show", "文件不存在");
        }
    }


    /**
     * 删除指定文件夹的内容
     *
     * @param delUrl
     */
    public static Boolean deleteFile(String delUrl, Context context) {
        // 删除之前某些原因没有删除干净的图片
        File filepath = new File(delUrl);
        if (!filepath.exists()) {
            ToastUtil.ShowLong(context, "文件不存在");
        }
        if (filepath.delete()) {
            ToastUtil.ShowLong(context, "删除成功");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成新的文件夹
     *
     * @param fileUrl
     */
    public static void creatFile(String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断文件夹是否存在
     *
     * @return
     */
    public static Boolean isCreate(String fileUrl) {
        File file = new File(fileUrl);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成新的文件夹
     *
     * @param fileUrl
     */
    public static File creatFolder(String fileUrl) {
        File file = new File(fileUrl);
        if (!file.exists()) {
            file.mkdirs();
            return file;
        }
        return file;
    }

    //读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }
}
