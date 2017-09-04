package com.hjnerp.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerp.business.activity.BusinessActivityFragment;
import com.hjnerp.business.view.StartViewInfo;
import com.hjnerp.business.view.ViewClass;
import com.hjnerp.business.view.WidgetClass;
import com.hjnerp.business.view.WidgetName;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.db.SQLiteWorker;
import com.hjnerp.manager.HjActivityManager;
import com.hjnerp.model.BusinessData;
import com.hjnerp.model.BusinessParam;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.util.ImageFileHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerpandroid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//@SuppressLint("NewApi")
public class HJPhotoView extends LinearLayout implements View.OnClickListener,
        HJViewInterface {
    private static String TAG = "HJPhotoView";
    public List<String> array = new ArrayList<String>();
    private File out;
    private Uri uri;
    private TextView title_textview;

    // private TextView number;
    public String strImage;
    private HorizontalListView gallery;//
    private Context context;
    private WidgetClass items;
    private ViewClass currentviewClass;
    private StartViewInfo startViewInfo;
    private BusinessParam businessParam = new BusinessParam(); // 当前界面上参数

    public Ctlm1347 ctlm1347 = new Ctlm1347();
    public Ctlm1347 ctlm1347Show = null;// 展示照片使用，构造方法HJPhotoView(Context
    // context, Ctlm1347 ctlm1347)
    public boolean istakingphoto = false;
    private int mCacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
    private int screenWidth;

    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
            mCacheSize) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

    };

    public Boolean flag_curr = false;

    private void addBitmap2LruCache(String uri, Bitmap bitmap) {
        if (getBitmapFromLruCache(uri) != null && bitmap != null) {
            cache.put(uri, bitmap);
        }
    }

    private Bitmap getBitmapFromLruCache(String uri) {
        return cache.get(uri);
    }

    public HJPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HJPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HJPhotoView(Context context) {
        super(context);
        initView();
    }

    public HJPhotoView(Context context, Ctlm1347 ctlm1347) {
        super(context);
        this.ctlm1347Show = ctlm1347;
        initView();
    }

    public HJPhotoView(Context context, WidgetClass items) {
        super(context);
        initView();
    }

    public HJPhotoView(Context context, WidgetClass items,
                       ViewClass currentviewClass, StartViewInfo startViewInfo,
                       BusinessParam param) {
        super(context);
        this.context = context;
        this.items = items;
        this.currentviewClass = currentviewClass;
        this.startViewInfo = startViewInfo;
        this.businessParam = param;
        initView();
    }

    public void compressBmpToFile(File filetemp) {
        Bitmap bmp = null;
        File f = filetemp;
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inSampleSize = 8;
        FileInputStream file1 = null;
        try {
            file1 = new FileInputStream(f);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        bmp = BitmapFactory.decodeStream(file1, null, options1);

        // File f = new File(Constant.CHAT_CACHE_DIR, fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        if (baos.toByteArray().length / 1024 > 40) {
            options = 4000 / (baos.toByteArray().length / 1024);
            baos.reset();
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp.isRecycled()) {
            bmp.recycle();
            System.gc();
        }
    }

    public void activityRefreshGallery() {
        // String[] ss = strImage.split(":");
//		File f = new File(Constant.HJPHOTO_CACHE_DIR + strImage);
//		// //压缩文件
//		compressBmpToFile(f);

        android.view.ViewGroup.LayoutParams layoutParams = gallery
                .getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new android.view.ViewGroup.LayoutParams(
                    layoutParams.MATCH_PARENT, (int) (screenWidth / 3));
            gallery.setLayoutParams(layoutParams);

        } else {
            layoutParams.height = (int) (screenWidth / 3);
        }

        ((BaseAdapter) gallery.getAdapter()).notifyDataSetChanged();

        saveCTLM1347();

    }

    public void refreshGallery() {
        android.view.ViewGroup.LayoutParams layoutParams = gallery
                .getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new android.view.ViewGroup.LayoutParams(
                    layoutParams.MATCH_PARENT, (int) (screenWidth / 3));
            gallery.setLayoutParams(layoutParams);

        } else {
            layoutParams.height = (int) (screenWidth / 3);
        }

        ((BaseAdapter) gallery.getAdapter()).notifyDataSetChanged();

        saveCTLM1347();

    }

    public void setAdapterArray(List<String> list) {
        this.array = list;
        ((BaseAdapter) gallery.getAdapter()).notifyDataSetChanged();
        refreshGallery();
    }

    public void saveDefaultCTLM1347() {
        if (currentviewClass.presave) {
            saveCTLM1347();
        }
    }

    public void saveCTLM1347() {

        if (ctlm1347Show != null) {
            ctlm1347 = ctlm1347Show;
        } else {
            ctlm1347.setId_recorder(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
            ctlm1347.setId_com(SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getComid());


            if (StringUtil.isNullOrEmpty(ctlm1347.getId_node())) {
                // id_node = StringUtil.getMyUUID();
                ctlm1347.setId_node(StringUtil.getMyUUID());
            }
            ctlm1347.setName_node(items.name);
            ctlm1347.setVar_billno(businessParam.getBillNo());
            ctlm1347.setId_model(businessParam.getModelId());
            ctlm1347.setId_nodetype(WidgetName.HJ_PHOTOVIEW);
            ctlm1347.setFlag_upload("N");
            ctlm1347.setId_parentnode(businessParam.getIdParentNode());
            ctlm1347.setInt_line(0);
            ctlm1347.setId_table(items.attribute.datasource);
            ctlm1347.setId_srcnode(items.id);
            ctlm1347.setDate_opr(businessParam.getDataOpr());
            ctlm1347.setId_view(businessParam.getViewId());
        }

        if (array.size() == 0) {
            ctlm1347.setVar_data1("");
        } else {
            String allphotourl = array.get(0);
            for (int i = 0; i < array.size() - 1; i++) {
                allphotourl = allphotourl + ";" + array.get(i + 1);
            }
            ctlm1347.setVar_data1(allphotourl);
        }

        BusinessBaseDao.replaceCTLM1347(ctlm1347);

    }

    private void initView() {
        Point outSize = new Point();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay()
                .getSize(outSize);
        screenWidth = outSize.x;
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay()
                .getMetrics(outMetrics);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_hjphotoview, null);
        view.setLayoutParams(lp);

        title_textview = (TextView) view.findViewById(R.id.imageView_title);

        if (ctlm1347Show != null) {

            if (items.attribute.required) {
                title_textview.setText("");
                title_textview.append(Html
                        .fromHtml("<font size=1 color =#000000>"
                                + ctlm1347Show.getName_node() + "</font>"
                                + "<font color =#ff0000>*</font>"));

            } else {
                title_textview.setText(ctlm1347Show.getName_node());
            }
        } else {

            if (items.attribute.required) {
                title_textview.setText("");
                title_textview.append(Html
                        .fromHtml("<font size=1 color =#000000>" + items.name
                                + "</font>" + "<font color =#ff0000>*</font>"));
            } else {
                title_textview.setText(items.name);
            }

//			Paint paint = title_textview.getPaint();
//			paint.setFakeBoldText(true);
        }

        gallery = (HorizontalListView) view.findViewById(R.id.hj_hjphotoview);
        gallery.setAdapter(new GalleryAdapter());
        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                final int tempposition = position;
                if (array == null || array.size() < 1) {
                    ToastUtil.ShowShort(getContext(), "没有照片可以删除");
                } else if (position == array.size()) {
                    ToastUtil.ShowShort(getContext(), "不可以删除");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            getContext());
                    android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            // Log.e(TAG, "the which is " + which);
                            switch (which) {
                                case 0:
                                    final String fileName = array.get(tempposition);
                                    // Log.e(TAG, ">>>>>> " + tempposition);
                                    array.remove(tempposition);
                                    ((BaseAdapter) gallery.getAdapter())
                                            .notifyDataSetChanged();
                                    // 删除数据库
                                    SQLiteWorker.getSharedInstance().postDML(
                                            new SQLiteWorker.AbstractSQLable() {
                                                @Override
                                                public Object doAysncSQL() {
                                                    BusinessBaseDao
                                                            .replaceCtlm1347ByKey(
                                                                    Ctlm1347.VARDATA1,
                                                                    fileName);
                                                    return null;
                                                }

                                                @Override
                                                public void onCompleted(Object event) {
                                                    if (event instanceof Throwable) {
                                                        // Log.e(TAG,"onCompleted>>>>>>>>>>");
                                                    }
                                                }
                                            });

                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                    builder.setItems(new CharSequence[]{"删除"}, listener);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });

        android.view.ViewGroup.LayoutParams layoutParams = gallery
                .getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (screenWidth / 3));
            gallery.setLayoutParams(layoutParams);

        } else {
            /**
             * @author haijian
             * 修改图片大小
             *
             */
            layoutParams.height = (int) (screenWidth / 3);
        }

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == array.size()) {
                    boolean sdState = Environment.getExternalStorageState()
                            .equals(Environment.MEDIA_MOUNTED);
                    if (!sdState) {
                        ToastUtil.ShowLong(getContext(), "sd卡不存在");
                        return;
                    }
                    if (ctlm1347Show == null) {
                        startPhoto();
                    } else {
                        startPhotoInSearchBusinessPicture();
                    }
                }

            }
        });
        addView(view);
        if (!items.attribute.visible) {
            this.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void startPhoto() {
        Random random = new Random();
        String rand = String.valueOf(random.nextInt());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId()
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + rand + ".jpg";
        fileName = fileName.replaceAll("-", "");

        strImage = fileName;
        out = new File(Constant.HJPHOTO_CACHE_DIR);
        if (!out.exists()) {
            out.mkdirs();
        }
        ImageFileHelper.getInstance().setFileName(fileName);
        out = new File(Constant.HJPHOTO_CACHE_DIR, fileName);
//        ContentValues contentValues = new ContentValues(1);
//        contentValues.put(MediaStore.Images.Media.DATA, out.getAbsolutePath());
//        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//		uri = Uri.fromFile(out);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", out);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(out);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        checkPath(intent, Constant.HJPHOTO_CACHE_DIR + fileName);
        flag_curr = true;
        BusinessActivityFragment bfragment = (BusinessActivityFragment) HjActivityManager
                .getInstance().peek().getSupportFragmentManager()
                .findFragmentByTag(businessParam.getViewId());
        if (bfragment != null) {
            bfragment.startActivityForResult(intent, 1001);
        }
        // ((Activity) getContext()).startActivityForResult(intent, 1001);

    }

    private void startPhotoInSearchBusinessPicture() {
        istakingphoto = true;
        Random random = new Random();
        String rand = String.valueOf(random.nextInt());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId()
                + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                .format(new Date()) + rand + ".jpg";

        strImage = fileName;

        out = new File(Constant.HJPHOTO_CACHE_DIR);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(Constant.HJPHOTO_CACHE_DIR, fileName);
//        ContentValues contentValues = new ContentValues(1);
//        contentValues.put(MediaStore.Images.Media.DATA, out.getAbsolutePath());
//        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        uri = Uri.fromFile(out);
        uri = FileProvider.getUriForFile(context, "com.hjnerp.takephoto.fileprovider", out);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        checkPath(intent, Constant.HJPHOTO_CACHE_DIR + fileName);
        flag_curr = true;
        ((Activity) getContext()).startActivityForResult(intent, 1001);

    }

    private void checkPath(Intent intent, String picpath) {
        if (intent != null) {
            Uri uri_DCIM = null;
            if (intent.getData() != null) {
                uri_DCIM = intent.getData();
            } else {
                uri_DCIM = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            String DCIMPath = "";
            Cursor cr = getContext().getContentResolver().query(uri_DCIM,
                    new String[]{MediaStore.Images.Media.DATA}, null, null,
                    MediaStore.Images.Media.DATE_MODIFIED + " desc");
            if (cr.moveToNext()) {
                DCIMPath = cr.getString(cr
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cr.close();
            if (DCIMPath.equals(picpath)) {
                ToastUtil.ShowLong(getContext(), "路径错误！");
            }
        }
    }

    private class GalleryAdapter extends BaseAdapter {

        class ViewHolder {
            public ImageView iv;
            public TextView tv;
            public LinearLayout ll;
        }

        @Override
        public int getCount() {
            return array.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            String paString = null;
            if (position <= array.size() - 1) {
                paString = array.get(position);
            }
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.hjphotoview_item, parent, false);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv = (TextView) convertView
                        .findViewById(R.id.tv_takephoto);
                holder.ll = (LinearLayout) convertView
                        .findViewById(R.id.ll_all);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (paString != null) {
                holder.tv.setVisibility(View.GONE);
                Bitmap bitmap = cache.get(paString);
                if (bitmap != null) {
                    holder.iv.setImageBitmap(bitmap);
                } else {
                    try {

                        String picname = paString;
                        FileInputStream in = new FileInputStream(
                                Constant.HJPHOTO_CACHE_DIR + picname);
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inJustDecodeBounds = true;
                        bitmap = BitmapFactory.decodeFile(
                                Constant.HJPHOTO_CACHE_DIR + picname, opts);
                        int scale = 1;
                        while (opts.outHeight / scale > (screenWidth / 3)
                                || opts.outWidth / scale > (screenWidth / 3)) {
                            scale = scale * 3;
                        }

                        // Log.e(TAG, "getWidth " + screenWidth);
                        opts.inPreferredConfig = Bitmap.Config.RGB_565;
                        opts.inPurgeable = true;
                        opts.inInputShareable = true;
                        opts.inSampleSize = scale;
                        opts.inJustDecodeBounds = false;
                        bitmap = BitmapFactory.decodeStream(in, null, opts);
                        // Log.e(TAG,bitmap.getHeight()+"");

                        Bitmap newBitmap = ThumbnailUtils.extractThumbnail(
                                bitmap, screenWidth / 4, screenWidth / (3));
                        if (newBitmap == null) {
                            holder.iv.setImageDrawable(getContext()
                                    .getResources().getDrawable(
                                            R.drawable.deleted_photo));
                        } else {

                            addBitmap2LruCache(paString, newBitmap);
                            holder.iv.setImageBitmap(newBitmap);
                            holder.iv.measure(0, 0);

                        }

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            } else {
                holder.tv.setVisibility(View.VISIBLE);

                // convertView.setBackgroundColor(getResources().getColor(R.color.red));
                Bitmap bitmptemp = Bitmap
                        .createBitmap(20, 50, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmptemp);

                canvas.drawColor(R.color.red);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        screenWidth / 4, (int) (screenWidth / 3));
                holder.ll.setLayoutParams(layoutParams);
                holder.iv.setImageBitmap(bitmptemp);
                holder.iv.setLayoutParams(layoutParams);
            }
            return convertView;
        }

    }

    @Override
    public void setValue(String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValueDefault() {
        // TODO Auto-generated method stub
        // saveCTLM1347();
        // saveDefaultCTLM1347();
    }

    @Override
    public void setJesonValue(String msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDataSource() {
        // TODO Auto-generated method stub
        return items.attribute.datasource;
    }

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return items.id;
    }

    @Override
    public boolean getEditable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setValue(String row, String column, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getValue(String row, String column) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String getRowCount() {
        // TODO Auto-generated method stub
        return "0";
    }

    @Override
    public String getCurrentRow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDataBuild(Boolean flag, BusinessData ctlm1345List) {

        // this.ctlm1347List = ctlm1347List;
        if (flag) {
            ArrayList<Ctlm1347> ctlm1347List = BusinessBaseDao.getCtlm1347List(
                    startViewInfo.id, currentviewClass.id,
                    businessParam.getIdParentNode(), businessParam.getBillNo(),
                    items.id);

            if (ctlm1347List != null && ctlm1347List.size() > 0) {
                setValue(ctlm1347List.get(0));
            }
        }
    }

    public void setValue(Ctlm1347 ctlm1347Lista) {

        ctlm1347 = ctlm1347Lista;
        if (ctlm1347 == null) {
            ctlm1347 = new Ctlm1347();
        } else {
            String allpicname = ctlm1347.getVar_data1();
            if (TextUtils.isEmpty(allpicname)) {
                return;
            }
            array.clear();
            String[] imagers = allpicname.split(";");
            for (String s : imagers) {
                array.add(s);
            }
//			array = Arrays.asList(allpicname.split(";"));
            if (array != null && array.size() > 0) {
                refreshGallery();
            }
        }

    }

    @Override
    public String getValue() {
        /**
         * @author haijian
         * 返回照片路径
         */
        String allphotourl = "";
        if (array != null && array.size() > 0) {//返回照片路径
            allphotourl = array.get(0);
            for (int i = 0; i < array.size() - 1; i++) {
                allphotourl = allphotourl + "," + array.get(i + 1);
            }
        }
        return allphotourl;
    }

    @Override
    public void setDataSource(String Data) {
        // TODO Auto-generated method stub

    }

    @Override
    public String setLocation() {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String setPhoto() {
        // TODO Auto-generated method stub
        boolean sdState = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdState) {
            ToastUtil.ShowLong(getContext(), "sd卡不存在");
            return "";
        }

        startPhoto();
        return "";
    }

    @Override
    public int saveData(Boolean required) {
        // TODO Auto-generated method stub
        saveCTLM1347();
        return 0;
    }

    @Override
    public void addItem(String billno, String nodeid, String vlues) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean validate() {
        // TODO Auto-generated method stub

        if (items.attribute.required) {
            if (array.size() <= 0) {
                ToastUtil.ShowShort(getContext(), items.name + "不能为空,请拍照.");
                return false;
            }
        }

        return true;

    }
}
