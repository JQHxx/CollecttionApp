package com.huateng.collection.bean;

/**
 * author: yichuan
 * Created on: 2020/7/2 15:41
 * description:
 */
public class ImageRecordsBean implements Comparable<RecorderBean>{



    /**
     * crtTime : 1590373566000
     * crtUser : 093809
     * fileId : 20200525000100001246
     * fileName : 0938091590373559.png
     * filePath : 4da3/2e/c387b14ed28b69d72102cdb596.png
     * fileSize : 5036987
     * fileTime : 1590336000000
     * fileType : png
     * isLongSave : Y
     * relaBusiCode : 20200521000604261679
     * relaBusiType : APP
     * remark : 照片
     * updTime : 1590373566000
     * updUser : 093809
     */
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

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

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

    @Override
    public String toString() {
        return "ImageRecordsBean{" +
                "isLocal=" + isLocal +
                ", crtTime=" + crtTime +
                ", crtUser='" + crtUser + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileTime=" + fileTime +
                ", fileType='" + fileType + '\'' +
                ", isLongSave='" + isLongSave + '\'' +
                ", relaBusiCode='" + relaBusiCode + '\'' +
                ", relaBusiType='" + relaBusiType + '\'' +
                ", remark='" + remark + '\'' +
                ", updTime=" + updTime +
                ", updUser='" + updUser + '\'' +
                '}';
    }
}
