package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020/6/22 10:59
 * description:
 */
public class RecorderBean implements Comparable<RecorderBean>{
    //远程录音
    private boolean isLocal;
    private long crtTime;
    private String crtUser;
    private String fileId;
    private String fileName;
    private String filePath;
    private String fileSize;
    private long fileTime;
    private String fileType;
    private String isLongSave;
    private String relaBusiCode;
    private String relaBusiType;
    private String remark;
    private long updTime;
    private String updUser;

    public long getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(long crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIsLongSave() {
        return isLongSave;
    }

    public void setIsLongSave(String isLongSave) {
        this.isLongSave = isLongSave;
    }

    public String getRelaBusiCode() {
        return relaBusiCode;
    }

    public void setRelaBusiCode(String relaBusiCode) {
        this.relaBusiCode = relaBusiCode;
    }

    public String getRelaBusiType() {
        return relaBusiType;
    }

    public void setRelaBusiType(String relaBusiType) {
        this.relaBusiType = relaBusiType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getUpdTime() {
        return updTime;
    }

    public void setUpdTime(long updTime) {
        this.updTime = updTime;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    @Override
    public int compareTo(RecorderBean recordsBean) {
        if(this.getFileTime() < recordsBean.getFileTime()) {
            return 1;
        }

        if(this.getFileTime() == recordsBean.getFileTime()) {
            return 0;
        }

        return -1;
    }
}

