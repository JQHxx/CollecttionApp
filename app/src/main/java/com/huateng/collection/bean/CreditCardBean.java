package com.huateng.collection.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-08 08:52
 * description:
 */
public class CreditCardBean {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"businessLine":"0001","creditCardNo":"4815470000007106","currentLoanCashAmount":0,"interestAmt":0,"openAcctDate":"20150814","openBankName":"总行信用卡中心","ovduAmts":0,"ovduPrincipalAmts":0,"overdrawAmt":0,"penalAmt":0,"periodRestFee":0,"periodRestPri":0,"productName":"汇通环球卡","statementDate":"08","wrofFlag":"0"}]
     * startIndex : -9
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

    public static class RecordsBean  implements Parcelable {
        /**
         * businessLine : 0001
         * creditCardNo : 4815470000007106
         * currentLoanCashAmount : 0.0
         * interestAmt : 0.0
         * openAcctDate : 20150814
         * openBankName : 总行信用卡中心
         * ovduAmts : 0.0
         * ovduPrincipalAmts : 0.0
         * overdrawAmt : 0.0
         * penalAmt : 0.0
         * periodRestFee : 0.0
         * periodRestPri : 0.0
         * productName : 汇通环球卡
         * statementDate : 08
         * wrofFlag : 0
         */

        private String businessLine;
        private String creditCardNo;
        private String currentLoanCashAmount;
        private String interestAmt;
        private String openAcctDate;
        private String openBankName;
        private String ovduAmts;
        private String ovduPrincipalAmts;
        private String overdrawAmt;
        private String penalAmt;
        private String periodRestFee;
        private String periodRestPri;
        private String productName;
        private String statementDate;
        private String wrofFlag;
        private int overduePeriods;



        protected RecordsBean(Parcel in) {
            businessLine = in.readString();
            creditCardNo = in.readString();
            currentLoanCashAmount = in.readString();
            interestAmt = in.readString();
            openAcctDate = in.readString();
            openBankName = in.readString();
            ovduAmts = in.readString();
            ovduPrincipalAmts = in.readString();
            overdrawAmt = in.readString();
            penalAmt = in.readString();
            periodRestFee = in.readString();
            periodRestPri = in.readString();
            productName = in.readString();
            statementDate = in.readString();
            wrofFlag = in.readString();
            overduePeriods = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(businessLine);
            dest.writeString(creditCardNo);
            dest.writeString(currentLoanCashAmount);
            dest.writeString(interestAmt);
            dest.writeString(openAcctDate);
            dest.writeString(openBankName);
            dest.writeString(ovduAmts);
            dest.writeString(ovduPrincipalAmts);
            dest.writeString(overdrawAmt);
            dest.writeString(penalAmt);
            dest.writeString(periodRestFee);
            dest.writeString(periodRestPri);
            dest.writeString(productName);
            dest.writeString(statementDate);
            dest.writeString(wrofFlag);
            dest.writeInt(overduePeriods);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<RecordsBean> CREATOR = new Creator<RecordsBean>() {
            @Override
            public RecordsBean createFromParcel(Parcel in) {
                return new RecordsBean(in);
            }

            @Override
            public RecordsBean[] newArray(int size) {
                return new RecordsBean[size];
            }
        };

        public String getBusinessLine() {
            return businessLine;
        }

        public void setBusinessLine(String businessLine) {
            this.businessLine = businessLine;
        }

        public String getCreditCardNo() {
            return creditCardNo;
        }

        public void setCreditCardNo(String creditCardNo) {
            this.creditCardNo = creditCardNo;
        }

        public String getCurrentLoanCashAmount() {
            return currentLoanCashAmount;
        }

        public void setCurrentLoanCashAmount(String currentLoanCashAmount) {
            this.currentLoanCashAmount = currentLoanCashAmount;
        }

        public String getInterestAmt() {
            return interestAmt;
        }

        public void setInterestAmt(String interestAmt) {
            this.interestAmt = interestAmt;
        }

        public String getOpenAcctDate() {
            return openAcctDate;
        }

        public void setOpenAcctDate(String openAcctDate) {
            this.openAcctDate = openAcctDate;
        }

        public String getOpenBankName() {
            return openBankName;
        }

        public void setOpenBankName(String openBankName) {
            this.openBankName = openBankName;
        }

        public String getOvduAmts() {
            return ovduAmts;
        }

        public void setOvduAmts(String ovduAmts) {
            this.ovduAmts = ovduAmts;
        }

        public String getOvduPrincipalAmts() {
            return ovduPrincipalAmts;
        }

        public void setOvduPrincipalAmts(String ovduPrincipalAmts) {
            this.ovduPrincipalAmts = ovduPrincipalAmts;
        }

        public String getOverdrawAmt() {
            return overdrawAmt;
        }

        public void setOverdrawAmt(String overdrawAmt) {
            this.overdrawAmt = overdrawAmt;
        }

        public String getPenalAmt() {
            return penalAmt;
        }

        public void setPenalAmt(String penalAmt) {
            this.penalAmt = penalAmt;
        }

        public String getPeriodRestFee() {
            return periodRestFee;
        }

        public void setPeriodRestFee(String periodRestFee) {
            this.periodRestFee = periodRestFee;
        }

        public String getPeriodRestPri() {
            return periodRestPri;
        }

        public void setPeriodRestPri(String periodRestPri) {
            this.periodRestPri = periodRestPri;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getStatementDate() {
            return statementDate;
        }

        public void setStatementDate(String statementDate) {
            this.statementDate = statementDate;
        }

        public String getWrofFlag() {
            return wrofFlag;
        }

        public void setWrofFlag(String wrofFlag) {
            this.wrofFlag = wrofFlag;
        }

        public int getOverduePeriods() {
            return overduePeriods;
        }

        public void setOverduePeriods(int overduePeriods) {
            this.overduePeriods = overduePeriods;
        }

        public static Creator<RecordsBean> getCREATOR() {
            return CREATOR;
        }
    }
}
