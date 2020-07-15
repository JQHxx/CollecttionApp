package com.huateng.network.bean;

/**
 * author: yichuan
 * Created on: 2020/7/13 17:42
 * description:
 */
public class UpdateBean {


    /**
     * appType : Android
     * appUrl : http://cdn.nbcb.com.cn/waifangqingshouapp/外访清收-v1.0.0.apk
     * isConstraint : 1
     * isNewVersion : 1
     * versionCode : 2.0.0
     */

    private String appType;
    private String appUrl;
    private String isConstraint;
    private String isNewVersion;
    private String versionCode;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getIsConstraint() {
        return isConstraint;
    }

    public void setIsConstraint(String isConstraint) {
        this.isConstraint = isConstraint;
    }

    public String getIsNewVersion() {
        return isNewVersion;
    }

    public void setIsNewVersion(String isNewVersion) {
        this.isNewVersion = isNewVersion;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "appType='" + appType + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", isConstraint='" + isConstraint + '\'' +
                ", isNewVersion='" + isNewVersion + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }
}
