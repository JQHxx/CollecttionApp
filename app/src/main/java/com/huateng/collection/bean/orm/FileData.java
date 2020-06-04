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
    private String fileName;//文件名
    //bizId 业务id
    private String bizId;
    private String caseId;//案件ID
    private String type;//类型
    private String realPath;//真实地址
    private String userId;//用户id
    private String fileId;//文件ID
    private long createTime;//创建时间
    private long duration;//时长
    private boolean exist;//是否存在
    private boolean isUpload;//是否已上传到后台
    private int  fileType;//新增文件类型 1已上传 2 未上传

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

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

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "fileName='" + fileName + '\'' +
                ", bizId='" + bizId + '\'' +
                ", caseId='" + caseId + '\'' +
                ", type='" + type + '\'' +
                ", realPath='" + realPath + '\'' +
                ", userId='" + userId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", createTime=" + createTime +
                ", duration=" + duration +
                ", exist=" + exist +
                ", isUpload=" + isUpload +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
