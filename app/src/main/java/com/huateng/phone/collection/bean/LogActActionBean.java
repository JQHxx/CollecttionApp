package com.huateng.phone.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-08 16:27
 * description:行动流水
 */
public class LogActActionBean {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"caseId":"20200102000603256318","crtTime":1588922941000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"sdfsafsaf","outBoundDate":1588867200000,"outBoundId":"20200508017000000001","outBoundName":"fasdfasfsafs","outBoundSummary":"sfsadfasfasf","remark":"外访","seeHimseif":"Y","updTime":1588922941000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588923047000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"sdfsafsaf","outBoundDate":1588867200000,"outBoundId":"20200508017000000002","outBoundName":"fasdfasfsafs","outBoundSummary":"sfsadfasfasf","remark":"外访","seeHimseif":"Y","updTime":1588923047000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588923570000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"dsffasdfsafa","outBoundDate":1588867200000,"outBoundId":"20200508017000000004","outBoundName":"csdasfasfsaf","outBoundSummary":"fdsfasfsafsafasf","remark":"外访","seeHimseif":"Y","updTime":1588923570000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588923985000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"sdfsafsafasf","outBoundDate":1588867200000,"outBoundId":"20200508017000000006","outBoundName":"fasdfsafas","outBoundSummary":"dafasfasdfasf","remark":"外访","seeHimseif":"Y","updTime":1588923985000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588924361000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"safsadfasdfsa","outBoundDate":1588867200000,"outBoundId":"20200508017000000007","outBoundName":"dsfsfsafsaf","outBoundSummary":"dfasfsafdsa","remark":"外访","seeHimseif":"Y","updTime":1588924361000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588924573000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"afdasfsafs","outBoundDate":1588867200000,"outBoundId":"20200508017000000008","outBoundName":"dasfasfasf","outBoundSummary":"dfasfasfa","remark":"外访","seeHimseif":"Y","updTime":1588924573000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588925135000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"czCZCZC","outBoundDate":1588867200000,"outBoundId":"20200508017000000009","outBoundName":"xccczzcZCZCZC","outBoundSummary":"ZCZCZCZCZCz","remark":"外访","seeHimseif":"N","updTime":1588925135000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588925536000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"dsdsdsd","outBoundDate":1588867200000,"outBoundId":"20200508017000000010","outBoundName":"dsdsd","outBoundSummary":"dsfdsfsdfsdfs","remark":"外访","seeHimseif":"Y","updTime":1588925536000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588925680000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"fsfsafas","outBoundDate":1588867200000,"outBoundId":"20200508017000000011","outBoundName":"dfsafsf","outBoundSummary":"dsfsfs","remark":"外访","seeHimseif":"Y","updTime":1588925680000,"updUser":"093809"},{"caseId":"20200102000603256318","crtTime":1588926128000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","outBoundAddress":"fdsfsf","outBoundDate":1588867200000,"outBoundId":"20200508017000000012","outBoundName":"dsafasf","outBoundSummary":"fafasf","remark":"外访","seeHimseif":"Y","updTime":1588926128000,"updUser":"093809"}]
     * startIndex : -9
     * totalPage : 2
     * totalRecord : 14
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
         * caseId : 20200102000603256318
         * crtTime : 1588922941000
         * crtUser : 093809
         * custName : 李旭辉
         * custNo : 43068219860303575X_Ind01
         * outBoundAddress : sdfsafsaf
         * outBoundDate : 1588867200000
         * outBoundId : 20200508017000000001
         * outBoundName : fasdfasfsafs
         * outBoundSummary : sfsadfasfasf
         * remark : 外访
         * seeHimseif : Y
         * updTime : 1588922941000
         * updUser : 093809
         */

        private String caseId;
        private long crtTime;
        private String crtUser;
        private String custName;
        private String custNo;
        private String outBoundAddress;
        private long outBoundDate;
        private String outBoundId;
        private String outBoundName;
        private String outBoundSummary;
        private String remark;
        private String seeHimseif;
        private long updTime;
        private String updUser;

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
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

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getOutBoundAddress() {
            return outBoundAddress;
        }

        public void setOutBoundAddress(String outBoundAddress) {
            this.outBoundAddress = outBoundAddress;
        }

        public long getOutBoundDate() {
            return outBoundDate;
        }

        public void setOutBoundDate(long outBoundDate) {
            this.outBoundDate = outBoundDate;
        }

        public String getOutBoundId() {
            return outBoundId;
        }

        public void setOutBoundId(String outBoundId) {
            this.outBoundId = outBoundId;
        }

        public String getOutBoundName() {
            return outBoundName;
        }

        public void setOutBoundName(String outBoundName) {
            this.outBoundName = outBoundName;
        }

        public String getOutBoundSummary() {
            return outBoundSummary;
        }

        public void setOutBoundSummary(String outBoundSummary) {
            this.outBoundSummary = outBoundSummary;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSeeHimseif() {
            return seeHimseif;
        }

        public void setSeeHimseif(String seeHimseif) {
            this.seeHimseif = seeHimseif;
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
