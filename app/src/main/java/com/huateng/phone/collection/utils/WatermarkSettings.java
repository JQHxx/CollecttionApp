package com.huateng.phone.collection.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: baipenggui
 * Date: 2019/1/23 16:06
 * Email: 1528354213@qq.com
 * Description: 形成水印工具类
 */
public class WatermarkSettings {


    public static WatermarkSettings mInstance;
    public static Context mContext;
    public static String mPath;
    private static String watermarkText;
    private static String mTrlUser;
    private static String mCustName;
    private static String mAddress;
    private static String mtime;
    private static String mLatLng;
    private static String TAG = "";

    /*
     *@Description: 图片添加水印的信息
     *@Params:
     *@Author: baipenggui
     *@Date: 2019/1/23
     */
    public static WatermarkSettings getmInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new WatermarkSettings();
        }
        TAG = mContext.getClass().getName();
        return mInstance;
    }

    /**
     * @param path           图片地址
     * @param trlUser        清收人
     * @param custName       客户
     * @param address         地址
     * @param latLng     经纬度
     * @param time     时间
     * @return
     * @Description 创建水印文件, 以下是水印上添加的文本信息
     */
    public static Bitmap createWatermark(String path, String trlUser, String custName,
                                         String address, String latLng, String time) {
        mPath = path;
        mTrlUser = trlUser;
        mCustName = custName;
        mAddress = address;
        mLatLng = latLng;
        mtime = time;

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        // 获取图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 创建一个和图片一样大的背景图
        Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        // 画背景图
        canvas.drawBitmap(bitmap,0, 0, null);
        watermarkText = "清收人:" + trlUser + "\n客户姓名:" + custName + "\n地址:" + mAddress + "\n经纬度:" + latLng + "\n时间:" + mtime ;

        //-------------开始绘制文字--------------
        if (!TextUtils.isEmpty(watermarkText)) {
            int screenWidth = getScreenWidth();

            float textSize = dp2px(mContext, 16) * bitmapWidth / screenWidth;
            // 创建画笔
            TextPaint mPaint = new TextPaint();
            // 文字矩阵区域
            Rect textBounds = new Rect();
            // 水印的字体大小
            mPaint.setTextSize(textSize);
            // 文字阴影
            mPaint.setShadowLayer(0.5f, 0f, 1f, Color.GRAY);
            // 抗锯齿
            mPaint.setAntiAlias(true);
            // 水印的区域
            mPaint.getTextBounds(watermarkText, 0, watermarkText.length(), textBounds);
            // 水印的颜色
            mPaint.setColor(Color.parseColor("#DBDBDB"));
            StaticLayout layout = new StaticLayout(watermarkText, 0, watermarkText.length(), mPaint, (int) (bitmapWidth - textSize),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.5F, true);
            // 文字开始的坐标
            float textX = dp2px(mContext, 8) * bitmapWidth / screenWidth;
            //float textY = bitmapHeight / 2;//图片的中间
            float textY = dp2px(mContext, 8) * bitmapHeight / screenWidth;
            // 画文字
            Log.e("nb",bitmapHeight+":"+ watermarkText.length());
            canvas.translate(textX, bitmapHeight - textSize*8);
            layout.draw(canvas);
        }
        //保存所有元素
        canvas.save();
        canvas.restore();
        return bmp;
    }

    /**
     * @param saveWatermarkPath 保存路径
     * @Description 保存水印图片
     */
    public static boolean savaWaterparkFile(String saveWatermarkPath) {

        Bitmap watermark = createWatermark(mPath, mTrlUser, mCustName, mAddress, mLatLng, mtime);
        File watermarkfile = new File(saveWatermarkPath);
        if (!watermarkfile.exists()) {
            watermarkfile.mkdir();
        }
        // 创建媒体文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
      //  File file = new File(watermarkfile.getPath() + File.separator + timeStamp + ".jpg");
      /*  if (file != null) {
            Log.e(TAG, "savaWaterparkFile: success ");
        } else {
            Log.e(TAG, "savaWaterparkFile: failure ");
        }*/
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(watermarkfile));
            if (watermark != null) {
                watermark.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  true;

    }


    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
