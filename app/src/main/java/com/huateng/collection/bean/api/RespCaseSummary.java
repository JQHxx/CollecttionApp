package com.huateng.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.huateng.collection.app.Constants;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
@Table
public class RespCaseSummary {

    @Unique
    @Expose(serialize = false, deserialize = false)
    private String bizId;
    private String addrId;
    private String caseId;
    private String custName;//客户姓名
    private String oaAmt;
    private String oaDate;
    private String hostName;
    @Expose(serialize = false, deserialize = false)
    private boolean done;
    @Expose(serialize = false, deserialize = false)
    private boolean uploaded;
    @Expose(serialize = false, deserialize = false)
    private String userId;
    private String addrType;
    private String visitAddress;
    //坐标保存
    private double latitude;
    private double longitude;

    //上传文件进度
    @Expose(serialize = false, deserialize = false)
    private int progress;
    //上传文件状态
    @Expose(serialize = false, deserialize = false)
    private String uploadStatus = Constants.CASE_NORMAL;
    //上传文件id
    @Expose(serialize = false, deserialize = false)
    private String uploadId;


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getOaAmt() {
        return oaAmt;
    }

    public void setOaAmt(String oaAmt) {
        this.oaAmt = oaAmt;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getOaDate() {
        return oaDate;
    }

    public void setOaDate(String oaDate) {
        this.oaDate = oaDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getVisitAddress() {
        return visitAddress;
    }

    public void setVisitAddress(String visitAddress) {
        this.visitAddress = visitAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

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

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
