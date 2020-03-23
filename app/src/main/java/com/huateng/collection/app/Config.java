package com.huateng.collection.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.securepreferences.SecurePreferences;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tools.utils.StringUtils;

/**
 * Created by shanyong on 2017/11/17.
 * 配置
 */

public class Config {
    private static SharedPreferences config;
    public static final String FIRST_OPEN = "first_open";
    //存储卡权限是否允许
    public static final String STORAGE_PERMISSION_GRANTED = "storage_permission_granted";

    private static final String LAST_LOCATION = "last_location";
    private static final String LAST_ADDRESS = "last_address";

    public static void init(Context context) {
        config = new SecurePreferences(context, "cfg", "config");
    }

    public static String get(String key) {
        return getString(key, "");
    }

    public static void set(String key, String value) {
        putString(key, value);
    }

    public static void setBoolean(String key, boolean value) {
        putBoolean(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, true);
    }

    private static void putString(String key, String value) {
        config.edit().putString(key, value).apply();
    }

    private static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    private static void putBoolean(String key, boolean value) {
        config.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static void clear() {
        config.edit().clear().apply();
    }


    public static LatLng getLastLocation() {
        String locString = get(LAST_LOCATION);
//        Logger.i(" get lastloc ：%s", locString);

        if (StringUtils.isNotEmpty(locString)) {
            String[] strs = locString.split(",");
            return new LatLng(Double.valueOf(strs[0]), Double.valueOf(strs[1]));
        }
        return null;
    }

    public static void setLastLocation(LatLng latLng) {
        String lastLoc = String.format("%s,%s", latLng.getLatitude(), latLng.getLongitude());
//        Logger.i("save lastloc %s", lastLoc);
        putString(LAST_LOCATION, lastLoc);
    }

    public static String getLastAddress() {
        return get(LAST_ADDRESS);
    }

    public static void setLastAddress(String lastAddress) {
        set(LAST_ADDRESS, lastAddress);
    }
}
