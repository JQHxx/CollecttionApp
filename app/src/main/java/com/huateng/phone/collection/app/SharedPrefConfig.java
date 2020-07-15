package com.huateng.phone.collection.app;

import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;


/**
 * Created by shanyong on 2016/10/12.
 * 保存全局数据到sharepreference
 */

public class SharedPrefConfig {

    private static SharedPreferences preferences;

    //public static final String SP_KEY_PASSWORD = "PASSWORD";// 密码
    private static final String SP_KEY_FIRST_OPEN = "FIRST_OPEN";

    private static SharedPrefConfig INSTANCE;

    public static SharedPrefConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (SharedPrefConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SharedPrefConfig();
                }
            }
        }
        return INSTANCE;
    }

    private SharedPrefConfig() {
        preferences = new SecurePreferences(MainApplication.getApplication(), "", "preferences");
    }

    public void setFirstOpen(boolean open) {
        putBoolean(SP_KEY_FIRST_OPEN, open);
    }

    public boolean isFirstOpen() {
        return getBoolean(SP_KEY_FIRST_OPEN, true);
    }

    public String get(String key) {
        return getString(key, "");
    }

    public void set(String key, String value) {
        putString(key, value);
    }

    public void setBoolean(String key, boolean value) {
        putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public void setLong(String key, long value) {
        putLong(key, value);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    private static void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    private void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    private long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public boolean contains(String key) {
        return preferences.contains(key);
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

}
