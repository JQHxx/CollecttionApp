package com.huateng.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-07 14:15
 * description:
 */
public class CaseBeanData {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"caseId":"20200102000603256318","certNo":"43068219860303575X","certType":"Ind01","custName":"李旭辉","custNo":"43068219860303575X_Ind01","overdueAmt":224200,"overdueDays":19,"phoneNo":"15700848055","productName":"直销表外类贷款案件","productType":"2019121700000002"},{"caseId":"20200102000603206303","certNo":"320511197911012081","certType":"Ind01","custName":"丁文艳","custNo":"320511197911012081_Ind01","overdueAmt":25568.99,"overdueDays":13,"phoneNo":"13913519626","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603205713","certNo":"110101198902083516","certType":"Ind01","custName":"耿宝峄","custNo":"110101198902083516_Ind01","overdueAmt":793.31,"overdueDays":0,"phoneNo":"15911061608","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603202945","certNo":"110101196104170536","certType":"Ind01","custName":"魏建东","custNo":"110101196104170536_Ind01","overdueAmt":534.48,"overdueDays":0,"phoneNo":"18811261720","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199475","certNo":"330227196812027538","certType":"Ind01","custName":"顾成华","custNo":"330227196812027538_Ind01","overdueAmt":297.36,"overdueDays":0,"phoneNo":"13857887784","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199215","certNo":"330226197103103671","certType":"Ind01","custName":"任旦","custNo":"330226197103103671_Ind01","overdueAmt":2817.78,"overdueDays":0,"phoneNo":"13777296060","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199207","certNo":"320113196612229519","certType":"Ind01","custName":"郁超","custNo":"320113196612229519_Ind01","overdueAmt":3408.87,"overdueDays":0,"phoneNo":"13805172836","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199150","certNo":"330124198208081815","certType":"Ind01","custName":"陈斌","custNo":"330124198208081815_Ind01","overdueAmt":207.12,"overdueDays":0,"phoneNo":"13486150808","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199147","certNo":"330183198206220316","certType":"Ind01","custName":"唐星明","custNo":"330183198206220316_Ind01","overdueAmt":263.55,"overdueDays":0,"phoneNo":"13805767372","productName":"白领通授信额度","productType":"2012082300000074"},{"caseId":"20200102000603199108","certNo":"320524196304228013","certType":"Ind01","custName":"周淳","custNo":"320524196304228013_Ind01","overdueAmt":786.78,"overdueDays":0,"phoneNo":"13906216963","productName":"白领通授信额度","productType":"2012082300000074"}]
     * startIndex : -9
     * totalPage : 3
     * totalRecord : 21
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
         * certNo : 43068219860303575X
         * certType : Ind01
         * custName : 李旭辉
         * custNo : 43068219860303575X_Ind01
         * overdueAmt : 224200.0
         * overdueDays : 19
         * phoneNo : 15700848055
         * productName : 直销表外类贷款案件
         * productType : 2019121700000002
         */

        private String caseId;
        private String certNo;
        private String certType;
        private String custName;
        private String custNo;
        private double overdueTotalRmb;
        private int overdueDays;
        private String phoneNo;
        private String productType;
        private String businessType;
        private String caseStatus;
        private String productName;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getCertNo() {
            return certNo;
        }

        public void setCertNo(String certNo) {
            this.certNo = certNo;
        }

        public String getCertType() {
            return certType;
        }

        public void setCertType(String certType) {
            this.certType = certType;
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

        public double getOverdueAmt() {
            return overdueTotalRmb;
        }

        public void setOverdueAmt(double overdueAmt) {
            this.overdueTotalRmb = overdueAmt;
        }

        public int getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(int overdueDays) {
            this.overdueDays = overdueDays;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getCaseStatus() {
            return caseStatus;
        }

        public void setCaseStatus(String caseStatus) {
            this.caseStatus = caseStatus;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }
    }
}
