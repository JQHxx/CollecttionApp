package com.huateng.collection.utils;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Devin
 * on: 14-2-10.
 */
public class CommonUtils {
    private static double EARTH_RADIUS = 6378137.0;

    public static ProgressDialog myDialog;

    public static String formatTimeMillis(Long duration) {
        int minute = 0;
        int second = 0;
        second = duration.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
            if (second <= 9) {
                return minute + "'" + "0" + second + "\"";
            } else {
                return minute + "'" + second + "\"";
            }
        } else {
            return second + "\"";
        }
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static void showProgressDialog(Context mContext, String title) {

        myDialog = new ProgressDialog(mContext);
        myDialog.setTitle("请稍后");
        myDialog.setMessage(title);
        myDialog.show();
    }

    public static void hideProgressDialog() {
        myDialog.cancel();
    }

    public static String getDate(String format) {
        SimpleDateFormat formatBuilder = new SimpleDateFormat(format);
        return formatBuilder.format(new Date());
    }

    public static String getDate() {
        return getDate("MMddhhmmss");
    }


    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int[] sizes = {width, height};

        return sizes;
    }

    public static int getScreenWidth(Context context) {
        int[] sizes = getScreenSize(context);
        return sizes[0];
    }

    public static int getScreenHeight(Context context) {
        int[] sizes = getScreenSize(context);
        return sizes[1];
    }

    public static int getRealWidth(Context mContext, int scale) {
        return (int) ((1f / scale) * CommonUtils.getScreenWidth(mContext));
    }

    public static int getRealHeight(Context mContext, int scale) {
        return (int) ((1f / scale) * CommonUtils.getScreenHeight(mContext));
    }

    public static int getScaleWidth(Context mContext, float scale) {
        return (int) (scale * CommonUtils.getScreenWidth(mContext));
    }

    public static int getScaleHeight(Context mContext, float scale) {
        return (int) (scale * CommonUtils.getScreenHeight(mContext));
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 判断网络是否连接
    public static boolean hasAvailNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取所有网络连接信息
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {//逐一查找状态为已连接的网络
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    // 打印所有的 intent extra 数据
    public static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
        }
        return sb.toString();
    }

    public static void clearCache() {
        FileUtil.clearDir(Environment.getExternalStorageDirectory()
                .getPath() + "/tourGuide/image");
    }

    public static void clearPhoneCache(Context context) {
        FileUtil.deleteAll(context.getCacheDir());
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static boolean checkAppIsRunning(Context context) {
        //判断应用是否在运行
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.devin.tourguide";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * V4.5.0起，保证数据不溢出，使用long型保存数据包大小结果
     */
    public static String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

}
