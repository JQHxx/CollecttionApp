package com.huateng.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

/**
 * 网络配置
 * Created by shanyong on 2017/9/22.
 * TODO :Do not place Android context classes in static fields
 */

public final class NetworkConfig {
    private static final String SP_KEY_API_MODE = "api_mode";
    private static final String SP_KEY_DOMAIN = "domain";
    private static final String SP_KEY_CUSTOM_HOST = "custom_host";
    private static final String SP_KEY_CUSTOM_PORT = "custom_port";
    private static final String SP_KEY_CUSTOM_ROOT = "custom_root";
    private static final String SP_KEY_CUSOMT_URL = "custom_url";
    private static final String SP_KEY_COOKIE = "cookie";
    private static final String SP_KEY_AUTH = "auth";
    public static NetworkConfig C = new NetworkConfig();

    public static String mpushKey;
    public static String PUB_KEY;
    public static String CLIENT_BKS;
    public static String B_PWD;
    public static String SERVER_CER;

    private Context ctx;
    private SharedPreferences sp;

    public void init(Context context) {
        ctx = context.getApplicationContext();
        sp = new SecurePreferences(ctx, "collect", "net_config.xml");

        String apiMode = getApiMode();
        String host = getCustomHost();
        String port = getCustomPort();
        String root = getCustomRoot();
        if (apiMode.equals(ApiConstants.API_MODE_DEVELOP)) {
            ApiConstants.DEVELOP_BASE_URL = String.format("http://%s:%s/%s/", host, port, root);
        }

//        Logger.i(apiMode);
    }

    public NetworkConfig checkInit(Context context) {
        if (ctx == null) {
            init(context);
        }
        return this;
    }

    public void setApiMode(String appMode) {
        putString(SP_KEY_API_MODE, appMode);
    }

    public void setCustomRoot(String root) {
        putString(SP_KEY_CUSTOM_ROOT, root);
    }

    public void setCustomHost(String host) {
        putString(SP_KEY_CUSTOM_HOST, host);
    }

    public void setCustomPort(String port) {
        putString(SP_KEY_CUSTOM_PORT, port);
    }

    public void setCustomURL(String url) {
        putString(SP_KEY_CUSOMT_URL, url);
    }

    public void setDomain(String domain) {
        putString(SP_KEY_DOMAIN, domain);
    }

    public void setCookie(String cookie) {
        putString(SP_KEY_COOKIE, cookie);
    }

    public void setAuth(String auth) {
        putString(SP_KEY_AUTH, auth);
    }

    public String getApiMode() {
        return sp.getString(SP_KEY_API_MODE, ApiConstants.API_MODE_RELEASE);
    }

    public String getCustomHost() {
        return sp.getString(SP_KEY_CUSTOM_HOST, "127.0.0.1");
    }

    public String getCustomPort() {
        return sp.getString(SP_KEY_CUSTOM_PORT, "8080");
    }

    public String getCustomRoot() {
        return sp.getString(SP_KEY_CUSTOM_ROOT, "ccms-app");
    }


    public String getDomain() {
        return sp.getString(SP_KEY_DOMAIN, ApiConstants.DOMAIN);
    }

    public String getCookie() {
        return sp.getString(SP_KEY_COOKIE, "");
    }

    public String getAuth() {
        return sp.getString(SP_KEY_AUTH, null);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //根据mode 获取BaseUrl
    public String getBaseURL() {
       /* String apiMode = getApiMode();
        String baseUrl = ApiConstants.RELEASE_BASE_URL;
        if (apiMode.equals(ApiConstants.API_MODE_DEVELOP)) {
            baseUrl = ApiConstants.DEVELOP_BASE_URL;
        } else if (apiMode.equals(ApiConstants.API_MODE_RELEASE)) {
            baseUrl = ApiConstants.RELEASE_BASE_URL;
        } else if (apiMode.equals(ApiConstants.API_MODE_CUSTOM)) {
            baseUrl = getCustomURL();
        }*/
       return ApiConstants.DEVELOP_BASE_URL;
    }

    public Context getCtx() {
        return ctx;
    }
}
