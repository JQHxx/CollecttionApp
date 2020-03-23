package com.huateng.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.huateng.collection.app.Constants;
import com.orm.dsl.Ignore;

/**
 * Created by 单勇  on 2018/12/29.
 * 上传案件文件类
 */

public class DoneCaseSummary extends RespCaseSummary {

    //上传文件进度
    @Ignore
    @Expose(serialize = false, deserialize = false)
    private int progress;
    //上传文件状态
    @Ignore
    @Expose(serialize = false, deserialize = false)
    private String uploadStatus = Constants.CASE_NORMAL;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
}
