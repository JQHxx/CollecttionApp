package com.huateng.network.update;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 版本信息
 */
public class UpdateAppBean implements Serializable {
    private static final long serialVersionUID = 1L;
    //   {"appType":"Android","appUrl":"http://cdn.nbcb.com.cn/waifangqingshouapp/外访清收-v1.0.0.apk","versionCode":"1.0.0"}
    private String appType;//ios or android

    //是否有新版本
    private String isNewVersion;
    //新版本号
    private String versionCode;
    //新app下载地址
    private String appUrl;
    //更新日志
    private String update_log;
    //配置默认更新dialog 的title
    private String update_def_dialog_title;
    //新app大小
    private String target_size;
    //是否强制更新
    private String isConstraint;
    //md5
    private String new_md5;
    //是否增量 暂时不用
    private boolean delta;
    //服务器端的原生返回数据（json）,方便使用者在hasNewApp自定义渲染dialog的时候可以有别的控制，比如：#issues/59
    private String origin_res;
    /**********以下是内部使用的数据**********/

    //网络工具，内部使用
    private HttpManager httpManager;
    private String targetPath;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;

    //是否隐藏对话框下载进度条,内部使用
    public boolean isHideDialog() {
        return mHideDialog;
    }

    public void setHideDialog(boolean hideDialog) {
        mHideDialog = hideDialog;
    }

    public boolean isUpdate() {
        return !TextUtils.isEmpty(this.isNewVersion) && "1".equals(this.isNewVersion);
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isConstraint() {
        //return ;
        return !TextUtils.isEmpty(this.isConstraint) && "1".equals(this.isConstraint);

    }

    public UpdateAppBean setConstraint(String constraint) {
        this.isConstraint = constraint;
        return this;
    }

    public String getUpdate() {
        return isNewVersion;
    }

    public UpdateAppBean setUpdate(String update) {
        this.isNewVersion = update;
        return this;
    }

    public String getNewVersion() {
        return versionCode;
    }

    public UpdateAppBean setNewVersion(String new_version) {
        this.versionCode = new_version;
        return this;
    }

    public String getApkFileUrl() {
        return appUrl;
    }


    public UpdateAppBean setApkFileUrl(String apk_file_url) {
        this.appUrl = apk_file_url;
        return this;
    }

    public String getUpdateLog() {
        return update_log;
    }

    public UpdateAppBean setUpdateLog(String update_log) {
        this.update_log = update_log;
        return this;
    }

    public String getUpdateDefDialogTitle() {
        return update_def_dialog_title;
    }

    public UpdateAppBean setUpdateDefDialogTitle(String updateDefDialogTitle) {
        this.update_def_dialog_title = updateDefDialogTitle;
        return this;
    }

    public boolean isDelta() {
        return delta;
    }

    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    public String getNewMd5() {
        return new_md5;
    }

    public UpdateAppBean setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public UpdateAppBean setNewMd5(String new_md5) {
        this.new_md5 = new_md5;
        return this;
    }


    public String getTargetSize() {
        return target_size;
    }

    public UpdateAppBean setTargetSize(String target_size) {
        this.target_size = target_size;
        return this;
    }

    public boolean isShowIgnoreVersion() {
        return mShowIgnoreVersion;
    }

    public void showIgnoreVersion(boolean showIgnoreVersion) {
        mShowIgnoreVersion = showIgnoreVersion;
    }

    public void dismissNotificationProgress(boolean dismissNotificationProgress) {
        mDismissNotificationProgress = dismissNotificationProgress;
    }

    public boolean isDismissNotificationProgress() {
        return mDismissNotificationProgress;
    }

    public boolean isOnlyWifi() {
        return mOnlyWifi;
    }

    public void setOnlyWifi(boolean onlyWifi) {
        mOnlyWifi = onlyWifi;
    }

    public String getOriginRes() {
        return origin_res;
    }

    public UpdateAppBean setOriginRes(String originRes) {
        this.origin_res = originRes;
        return this;
    }

}
