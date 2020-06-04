package com.huateng.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-25 16:09
 * description:
 */
public class UploadImageDataBean {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 20
     * prePage : 1
     * records : [{"crtTime":1590373566000,"crtUser":"093809","fileId":"20200525000100001246","fileName":"0938091590373559.png","filePath":"4da3/2e/c387b14ed28b69d72102cdb596.png","fileSize":"5036987","fileTime":1590336000000,"fileType":"png","isLongSave":"Y","relaBusiCode":"20200521000604261679","relaBusiType":"APP","remark":"照片","updTime":1590373566000,"updUser":"093809"}]
     * startIndex : -19
     * totalPage : 1
     * totalRecord : 1
     */

    private int current;
    private int nextPage;
    private int pageNo;
    private int pageSize;
    private int prePage;
    private int startIndex;
    private int totalPage;
    private int totalRecord;
    private List<RecordsBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
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
    }
}
