package com.huateng.phone.collection.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-08 17:41
 * description:
 */
public class ReportListBean implements Parcelable {

    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"acctFundInfo":"dfasfasf","carInfo":"csdasfasfaasda","caseId":"20200102000603256318","crtTime":1588929841000,"crtUser":"093809","custName":"李旭辉","custNo":"43068219860303575X_Ind01","debtIndo":"fadfasf","detailedContent":"fsdfasf","houseInfo":"dsfsaf","id":"20200508016900000009","investmentInfo":"fsafsafsaf","maritalStatus":"1","otherAssetsInfo":"fafasfast","otherValidAddress":"sdsaasd","overdueCause":"dadasdasd","unitIncumbency":"0","updTime":1588929841000,"updUser":"093809","visitAddress":"dadsada","visitDate":"2020-05-08","visitName":"sssss"}]
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

    protected ReportListBean(Parcel in) {
        current = in.readInt();
        nextPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        prePage = in.readInt();
        startIndex = in.readInt();
        totalPage = in.readInt();
        totalRecord = in.readInt();
        records = in.createTypedArrayList(RecordsBean.CREATOR);
    }

    public static final Creator<ReportListBean> CREATOR = new Creator<ReportListBean>() {
        @Override
        public ReportListBean createFromParcel(Parcel in) {
            return new ReportListBean(in);
        }

        @Override
        public ReportListBean[] newArray(int size) {
            return new ReportListBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(current);
        parcel.writeInt(nextPage);
        parcel.writeInt(pageNo);
        parcel.writeInt(pageSize);
        parcel.writeInt(prePage);
        parcel.writeInt(startIndex);
        parcel.writeInt(totalPage);
        parcel.writeInt(totalRecord);
        parcel.writeTypedList(records);
    }

    public static class RecordsBean implements Parcelable {
        /**
         * acctFundInfo : dfasfasf
         * carInfo : csdasfasfaasda
         * caseId : 20200102000603256318
         * crtTime : 1588929841000
         * crtUser : 093809
         * custName : 李旭辉
         * custNo : 43068219860303575X_Ind01
         * debtIndo : fadfasf
         * detailedContent : fsdfasf
         * houseInfo : dsfsaf
         * id : 20200508016900000009
         * investmentInfo : fsafsafsaf
         * maritalStatus : 1
         * otherAssetsInfo : fafasfast
         * otherValidAddress : sdsaasd
         * overdueCause : dadasdasd
         * unitIncumbency : 0
         * updTime : 1588929841000
         * updUser : 093809
         * visitAddress : dadsada
         * visitDate : 2020-05-08
         * visitName : sssss
         */

        private String acctFundInfo;
        private String carInfo;
        private String caseId;
        private long crtTime;
        private String crtUser;
        private String custName;
        private String custNo;
        private String debtIndo;
        private String detailedContent;
        private String houseInfo;
        private String id;
        private String investmentInfo;
        private String maritalStatus;
        private String otherAssetsInfo;
        private String otherValidAddress;
        private String overdueCause;
        private String unitIncumbency;
        private long updTime;
        private String updUser;
        private String visitAddress;
        private String visitDate;
        private String visitName;
        private String meetHimselfFlag;

        protected RecordsBean(Parcel in) {
            acctFundInfo = in.readString();
            carInfo = in.readString();
            caseId = in.readString();
            crtTime = in.readLong();
            crtUser = in.readString();
            custName = in.readString();
            custNo = in.readString();
            debtIndo = in.readString();
            detailedContent = in.readString();
            houseInfo = in.readString();
            id = in.readString();
            investmentInfo = in.readString();
            maritalStatus = in.readString();
            otherAssetsInfo = in.readString();
            otherValidAddress = in.readString();
            overdueCause = in.readString();
            unitIncumbency = in.readString();
            updTime = in.readLong();
            updUser = in.readString();
            visitAddress = in.readString();
            visitDate = in.readString();
            visitName = in.readString();
            meetHimselfFlag = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(acctFundInfo);
            dest.writeString(carInfo);
            dest.writeString(caseId);
            dest.writeLong(crtTime);
            dest.writeString(crtUser);
            dest.writeString(custName);
            dest.writeString(custNo);
            dest.writeString(debtIndo);
            dest.writeString(detailedContent);
            dest.writeString(houseInfo);
            dest.writeString(id);
            dest.writeString(investmentInfo);
            dest.writeString(maritalStatus);
            dest.writeString(otherAssetsInfo);
            dest.writeString(otherValidAddress);
            dest.writeString(overdueCause);
            dest.writeString(unitIncumbency);
            dest.writeLong(updTime);
            dest.writeString(updUser);
            dest.writeString(visitAddress);
            dest.writeString(visitDate);
            dest.writeString(visitName);
            dest.writeString(meetHimselfFlag);
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

        public String getAcctFundInfo() {
            return acctFundInfo;
        }

        public void setAcctFundInfo(String acctFundInfo) {
            this.acctFundInfo = acctFundInfo;
        }

        public String getCarInfo() {
            return carInfo;
        }

        public void setCarInfo(String carInfo) {
            this.carInfo = carInfo;
        }

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

        public String getDebtIndo() {
            return debtIndo;
        }

        public void setDebtIndo(String debtIndo) {
            this.debtIndo = debtIndo;
        }

        public String getDetailedContent() {
            return detailedContent;
        }

        public void setDetailedContent(String detailedContent) {
            this.detailedContent = detailedContent;
        }

        public String getHouseInfo() {
            return houseInfo;
        }

        public void setHouseInfo(String houseInfo) {
            this.houseInfo = houseInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvestmentInfo() {
            return investmentInfo;
        }

        public void setInvestmentInfo(String investmentInfo) {
            this.investmentInfo = investmentInfo;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getOtherAssetsInfo() {
            return otherAssetsInfo;
        }

        public void setOtherAssetsInfo(String otherAssetsInfo) {
            this.otherAssetsInfo = otherAssetsInfo;
        }

        public String getOtherValidAddress() {
            return otherValidAddress;
        }

        public void setOtherValidAddress(String otherValidAddress) {
            this.otherValidAddress = otherValidAddress;
        }

        public String getOverdueCause() {
            return overdueCause;
        }

        public void setOverdueCause(String overdueCause) {
            this.overdueCause = overdueCause;
        }

        public String getUnitIncumbency() {
            return unitIncumbency;
        }

        public void setUnitIncumbency(String unitIncumbency) {
            this.unitIncumbency = unitIncumbency;
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

        public String getVisitAddress() {
            return visitAddress;
        }

        public void setVisitAddress(String visitAddress) {
            this.visitAddress = visitAddress;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getVisitName() {
            return visitName;
        }

        public void setVisitName(String visitName) {
            this.visitName = visitName;
        }

        public String getMeetHimselfFlag() {
            return meetHimselfFlag;
        }

        public void setMeetHimselfFlag(String meetHimselfFlag) {
            this.meetHimselfFlag = meetHimselfFlag;
        }

        public static Creator<RecordsBean> getCREATOR() {
            return CREATOR;
        }
    }
}
