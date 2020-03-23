package com.huateng.fm.core.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author: Devin
 * on: 14-2-10.
 */
public class FmScreenUtil {

   
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
        return (int) ((1f / scale) * FmScreenUtil.getScreenWidth(mContext));
    }

    public static int getRealHeight(Context mContext, int scale) {
        return (int) ((1f / scale) * FmScreenUtil.getScreenHeight(mContext));
    }

    public static int getScaleWidth(Context mContext, float scale) {
        return (int) (scale * FmScreenUtil.getScreenWidth(mContext));
    }

    public static int getScaleHeight(Context mContext, float scale) {
        return (int) (scale * FmScreenUtil.getScreenHeight(mContext));
    }

  
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

 

}
