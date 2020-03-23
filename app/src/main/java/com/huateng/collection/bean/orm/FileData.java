package com.huateng.collection.bean.orm;

import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * @author dengzh
 * @description
 * @time 2016-12-19.
 */
@Table
public class FileData {

    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_PHOTO = "PHOTO";

    @Unique
    private String fileName;
    //bizId 业务id
    private String bizId;
    private String caseId;
    private String type;
    private String realPath;
    private String userId;
    private String fileId;
    private long createTime;
    private long duration;
    private boolean exist;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
