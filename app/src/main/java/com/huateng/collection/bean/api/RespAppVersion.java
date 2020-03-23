package com.huateng.collection.bean.api;

/**
 * Created by shanyong on 2016/12/21.
 */

public class RespAppVersion extends RespBase {

    /**
     * apkUrl : www.12345.com/collection.apk
     * versionCode : 1.0
     */

    private String appUrl;
    private String versionCode;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
