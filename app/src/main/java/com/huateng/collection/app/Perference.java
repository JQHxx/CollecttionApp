package com.huateng.collection.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

/**
 * Created by shanyong on 2017/9/1.
 * 商户资料
 */

public class Perference {

    private static SharedPreferences preferences;

    public static final String NICK_NAME = "nick_name";
    //用户id
    private static final String USER_ID = "user_id";
    private static final String CURRENT_CASE_ID = "current_case_id";//案件ID
    private static final String CURRENT_CUST_ID = "current_cust_id";//当前案件客户ID
    private static final String CURRENT_CUST_NAME = "current_cust_name";//客户名称
    private static final String CURRENT_VISIT_ADDRESS = "current_visit_address";//案件行动地址
    private static final String CURRENT_VISIT_ADDRESS_ID = "current_visit_address_id";//案件行动地址ID

    //通话录音相关
    private static final String PREPARE_CALL_RECORDING = "prepare_call_recording";
    private static String PREPARE_RECORDING_PHONE_NUMBER = "prepare_recording_phone_number";

    public static void init(Context context) {
        preferences = new SecurePreferences(context, "collectionApp", "preferences");
    }

    public static String get(String key) {
        return getString(key, null);
    }

    public static void set(String key, String value) {
        putString(key, value);
    }

    public static void setBoolean(String key, boolean value) {
        putBoolean(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static void setLong(String key, long value) {
        putLong(key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }


    public static void clear() {
        preferences.edit().clear().apply();
    }

    private static void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private static String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    private static void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private static void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    private static long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public static String getUserId() {
        return get(USER_ID);
    }

    public static void setUserId(String userId) {
        set(USER_ID, userId);
    }

    /**
     * 案件相关参数
     */

    public static void setCurrentVisitAddressId(String visitaddrssId) {
        set(CURRENT_VISIT_ADDRESS_ID, visitaddrssId);
    }

    public static String getCurrentVisitAddressId() {
        return get(CURRENT_VISIT_ADDRESS_ID);
    }

    public static void setCurrentVisitAddress(String currentVisitAddress) {
        set(CURRENT_VISIT_ADDRESS, currentVisitAddress);
    }

    public static String getCurrentVisitAddress() {
        return get(CURRENT_VISIT_ADDRESS);
    }

    //获取当前案件id
    public static String getCurrentCaseId() {
        return get(CURRENT_CASE_ID);
    }

    public static void setCurrentCaseId(String currentCaseId) {
        set(CURRENT_CASE_ID, currentCaseId);
    }

    //获取当前催收客户id
    public static String getCurrentCustId() {
        return get(CURRENT_CUST_ID);
    }

    public static void setCurrentCustId(String currentCustId) {
        set(CURRENT_CUST_ID, currentCustId);
    }

    public static String getCurrentCustName() {
        return get(CURRENT_CUST_NAME);
    }

    public static void setCurrentCustName(String currentCustName) {
        set(CURRENT_CUST_NAME, currentCustName);
    }

    public static Boolean isPrepareCallRecording() {
        return getBoolean(PREPARE_CALL_RECORDING);
    }

    public static void setPrepareCallRecording(boolean prepareCallRecording) {
        setBoolean(PREPARE_CALL_RECORDING, prepareCallRecording);
    }

    public static String getPrepareRecordingPhoneNumber() {
        return get(PREPARE_RECORDING_PHONE_NUMBER);
    }

    public static void setPrepareRecordingPhoneNumber(String prepareRecordingPhoneNumber) {
        set(PREPARE_RECORDING_PHONE_NUMBER, prepareRecordingPhoneNumber);
    }
}
