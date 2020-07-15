package com.huateng.phone.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-08 09:51
 * description:
 */
public class CustTelInfoBean {

    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"btDate":1582992000000,"contactCompany":"宁波","contactIdno":"11111111111","contactPnhone":"15612345689","contactSex":"M","crtTime":1588844490000,"crtUser":"093809","custNo":"310111195007150467_Ind01","dataSource":"HAND","effectiveFlag":"Y","familyFlag":"Y","name":"张三","relWithCust":"兄弟","telId":"20200507000700000497","updTime":1588844490000,"updUser":"093809"},{"btDate":1583769600000,"contactCompany":"上海志攀贸易有限公司","contactIdno":"","contactPnhone":"","contactSex":"","crtTime":1577963057000,"crtUser":"SYSTEM","curDDialCnt":0,"curDNoRespDialCnt":0,"curDRespDialCnt":0,"custNo":"310111195007150467_Ind01","dataSource":"HOST","effectiveFlag":"","familyFlag":"","name":"俞哲彦/50691981","relWithCust":"","telId":"310111195007150467","updTime":1583758987000,"updUser":"SYSTEM"}]
     * startIndex : -9
     * totalPage : 1
     * totalRecord : 2
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
         * btDate : 1582992000000
         * contactCompany : 宁波
         * contactIdno : 11111111111
         * contactPnhone : 15612345689
         * contactSex : M
         * crtTime : 1588844490000
         * crtUser : 093809
         * custNo : 310111195007150467_Ind01
         * dataSource : HAND
         * effectiveFlag : Y
         * familyFlag : Y
         * name : 张三
         * relWithCust : 兄弟
         * telId : 20200507000700000497
         * updTime : 1588844490000
         * updUser : 093809
         * curDDialCnt : 0
         * curDNoRespDialCnt : 0
         * curDRespDialCnt : 0
         */

        private long btDate;
        private String contactCompany;
        private String contactIdno;
        private String contactPnhone;
        private String contactSex;
        private long crtTime;
        private String crtUser;
        private String custNo;
        private String dataSource;
        private String effectiveFlag;
        private String familyFlag;
        private String name;
        private String relWithCust;
        private String telId;
        private long updTime;
        private String updUser;
        private int curDDialCnt;
        private int curDNoRespDialCnt;
        private int curDRespDialCnt;

        public long getBtDate() {
            return btDate;
        }

        public void setBtDate(long btDate) {
            this.btDate = btDate;
        }

        public String getContactCompany() {
            return contactCompany;
        }

        public void setContactCompany(String contactCompany) {
            this.contactCompany = contactCompany;
        }

        public String getContactIdno() {
            return contactIdno;
        }

        public void setContactIdno(String contactIdno) {
            this.contactIdno = contactIdno;
        }

        public String getContactPnhone() {
            return contactPnhone;
        }

        public void setContactPnhone(String contactPnhone) {
            this.contactPnhone = contactPnhone;
        }

        public String getContactSex() {
            return contactSex;
        }

        public void setContactSex(String contactSex) {
            this.contactSex = contactSex;
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

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getEffectiveFlag() {
            return effectiveFlag;
        }

        public void setEffectiveFlag(String effectiveFlag) {
            this.effectiveFlag = effectiveFlag;
        }

        public String getFamilyFlag() {
            return familyFlag;
        }

        public void setFamilyFlag(String familyFlag) {
            this.familyFlag = familyFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelWithCust() {
            return relWithCust;
        }

        public void setRelWithCust(String relWithCust) {
            this.relWithCust = relWithCust;
        }

        public String getTelId() {
            return telId;
        }

        public void setTelId(String telId) {
            this.telId = telId;
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

        public int getCurDDialCnt() {
            return curDDialCnt;
        }

        public void setCurDDialCnt(int curDDialCnt) {
            this.curDDialCnt = curDDialCnt;
        }

        public int getCurDNoRespDialCnt() {
            return curDNoRespDialCnt;
        }

        public void setCurDNoRespDialCnt(int curDNoRespDialCnt) {
            this.curDNoRespDialCnt = curDNoRespDialCnt;
        }

        public int getCurDRespDialCnt() {
            return curDRespDialCnt;
        }

        public void setCurDRespDialCnt(int curDRespDialCnt) {
            this.curDRespDialCnt = curDRespDialCnt;
        }
    }
}
