package com.huateng.collection.bean.api;

/**
 * Created by shanyong on 2017/1/18.
 */

public class RespVersion extends RespBase{

    /**
     * appUrl : /apps/xxx.apk
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
