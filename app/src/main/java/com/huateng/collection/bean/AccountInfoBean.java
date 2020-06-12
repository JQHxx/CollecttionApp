package com.huateng.collection.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-07 18:13
 * description:
 */
public class AccountInfoBean {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 10
     * prePage : 1
     * records : [{"acctBusinessType":"02","acctClass":"","acctClassType":"2019121700000002","acctLimit":0,"acctNo":"6600200118012257","acctStatus":"S8","acctType":"02","amountNo":"LI2018120261747741","bacctNo":"BD2018120362171636","baserialno":"","belongFirstTeam":"岳阳团队","belongSecondTeam":"岳阳团队","borrowerLocation":"430600","btDate":1589558400000,"businessrate":12,"caseId":"20200102000603256318","collStrategy":"A0002","collStrength":"A0002","credLimit":0,"crtTime":1577963129000,"crtUser":"SYSTEM","curCashAmt":0,"curCycBal":0,"curCycStmtBal":0,"curInsSpendBal":0,"curSpendBal":0,"currency":"156","custNo":"43068219860303575X_Ind01","debitInterestAmts":0,"deductionCardNo":"6223162056683119","deductionCardStatus":"","disputeAmt":0,"exceedLmtAmt":0,"insPoundageBal":0,"insuranceCompany":"343","interestAmt":0,"isremote":"","lastCycDueAmt":0,"lastCycStmtBal":0,"lastDebitTransAmt":0,"lastLoanTransAmt":0,"loanAmt":200000,"loanDate":1543766400000,"m1Amt":0,"m2Amt":0,"m3Amt":0,"m4Amt":0,"m5Amt":0,"m6Amt":0,"m7Amt":0,"m7PlusAmt":0,"mainAcctFlag":"Y","manager":"160664","managerOrg":"1101901","odInterestAmt":24200,"odPenIntAmt":0,"odPrincipalAmt":200000,"outstandInt":24200,"outstandPri":200000,"ovduAmts":224200,"ovduMonth":0,"ovduPrincipalAmts":0,"overdrawAmt":0,"overdrawLimit":0,"overdueAmt":224200,"overdueDays":19,"overdueStage":"","overdueTimes":0,"paidAmt":0,"paidFlag":"N","payDueDate":1575043200000,"penalAmt":0,"productType":"03","projectNo":"6600200118012257","repayDay":"","repayType":"D_P1","repaymentInt":0,"repaymentPri":0,"riskLevel":"","rootOrgId":"11019","satisfyFlag":"N","secOrgId":"1101901","sortRuleNo":"000","sourceCode":"3","spendIniAmt":0,"straAiId":"","straEmailId":"","straMailId":"","straSmsId":"","straUpdDate":1583769600000,"straVoiceId":"","subChannel":"ZXMB","tenLevelClass":"","updTime":1586756583000,"updUser":"SYSTEM"}]
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

    public static class RecordsBean implements Parcelable {
        /**
         * acctBusinessType : 02
         * acctClass :
         * acctClassType : 2019121700000002
         * acctLimit : 0.0
         * acctNo : 6600200118012257
         * acctStatus : S8
         * acctType : 02
         * amountNo : LI2018120261747741
         * bacctNo : BD2018120362171636
         * baserialno :
         * belongFirstTeam : 岳阳团队
         * belongSecondTeam : 岳阳团队
         * borrowerLocation : 430600
         * btDate : 1589558400000
         * businessrate : 12.0
         * caseId : 20200102000603256318
         * collStrategy : A0002
         * collStrength : A0002
         * credLimit : 0.0
         * crtTime : 1577963129000
         * crtUser : SYSTEM
         * curCashAmt : 0.0
         * curCycBal : 0.0
         * curCycStmtBal : 0.0
         * curInsSpendBal : 0.0
         * curSpendBal : 0.0
         * currency : 156
         * custNo : 43068219860303575X_Ind01
         * debitInterestAmts : 0.0
         * deductionCardNo : 6223162056683119
         * deductionCardStatus :
         * disputeAmt : 0.0
         * exceedLmtAmt : 0.0
         * insPoundageBal : 0.0
         * insuranceCompany : 343
         * interestAmt : 0.0
         * isremote :
         * lastCycDueAmt : 0.0
         * lastCycStmtBal : 0.0
         * lastDebitTransAmt : 0.0
         * lastLoanTransAmt : 0.0
         * loanAmt : 200000.0
         * loanDate : 1543766400000
         * m1Amt : 0.0
         * m2Amt : 0.0
         * m3Amt : 0.0
         * m4Amt : 0.0
         * m5Amt : 0.0
         * m6Amt : 0.0
         * m7Amt : 0.0
         * m7PlusAmt : 0.0
         * mainAcctFlag : Y
         * manager : 160664
         * managerOrg : 1101901
         * odInterestAmt : 24200.0
         * odPenIntAmt : 0.0
         * odPrincipalAmt : 200000.0
         * outstandInt : 24200.0
         * outstandPri : 200000.0
         * ovduAmts : 224200.0
         * ovduMonth : 0
         * ovduPrincipalAmts : 0.0
         * overdrawAmt : 0.0
         * overdrawLimit : 0.0
         * overdueAmt : 224200.0
         * overdueDays : 19
         * overdueStage :
         * overdueTimes : 0
         * paidAmt : 0.0
         * paidFlag : N
         * payDueDate : 1575043200000
         * penalAmt : 0.0
         * productType : 03
         * projectNo : 6600200118012257
         * repayDay :
         * repayType : D_P1
         * repaymentInt : 0.0
         * repaymentPri : 0.0
         * riskLevel :
         * rootOrgId : 11019
         * satisfyFlag : N
         * secOrgId : 1101901
         * sortRuleNo : 000
         * sourceCode : 3
         * spendIniAmt : 0.0
         * straAiId :
         * straEmailId :
         * straMailId :
         * straSmsId :
         * straUpdDate : 1583769600000
         * straVoiceId :
         * subChannel : ZXMB
         * tenLevelClass :
         * updTime : 1586756583000
         * updUser : SYSTEM
         */

        private String acctBusinessType;
        private String acctClass;
        private String acctClassType;
        private String acctLimit;
        private String acctNo;
        private String acctStatus;
        private String acctType;
        private String amountNo;
        private String bacctNo;
        private String baserialno;
        private String belongFirstTeam;
        private String belongSecondTeam;
        private String borrowerLocation;
        private long btDate;
        private String businessrate;
        private String caseId;
        private String collStrategy;
        private String collStrength;
        private double credLimit;
        private long crtTime;
        private String crtUser;
        private String curCashAmt;
        private String curCycBal;
        private String curCycStmtBal;
        private String curInsSpendBal;
        private String curSpendBal;
        private String currency;
        private String custNo;
        private String debitInterestAmts;
        private String deductionCardNo;
        private String deductionCardStatus;
        private String disputeAmt;
        private String exceedLmtAmt;
        private String insPoundageBal;
        private String insuranceCompany;
        private String interestAmt;
        private String isremote;
        private String lastCycDueAmt;
        private String lastCycStmtBal;
        private String lastDebitTransAmt;
        private String lastLoanTransAmt;
        private String loanAmt;
        private long loanDate;
        private String m1Amt;
        private String m2Amt;
        private String m3Amt;
        private String m4Amt;
        private String m5Amt;
        private String m6Amt;
        private String m7Amt;
        private String m7PlusAmt;
        private String mainAcctFlag;
        private String manager;
        private String managerOrg;
        private String odInterestAmt;
        private String odPenIntAmt;
        private String odPrincipalAmt;
        private String outstandInt;
        private String outstandPri;
        private String ovduAmts;
        private int ovduMonth;
        private String ovduPrincipalAmts;
        private String overdrawAmt;
        private String overdrawLimit;
        private String overdueAmt;
        private int overdueDays;
        private String overdueStage;
        private int overdueTimes;
        private String paidAmt;
        private String paidFlag;
        private long payDueDate;
        private String penalAmt;
        private String productType;
        private String projectNo;
        private String repayDay;
        private String repayType;
        private String repaymentInt;
        private String repaymentPri;
        private String riskLevel;
        private String rootOrgId;
        private String satisfyFlag;
        private String secOrgId;
        private String sortRuleNo;
        private String sourceCode;
        private String spendIniAmt;
        private String straAiId;
        private String straEmailId;
        private String straMailId;
        private String straSmsId;
        private long straUpdDate;
        private String straVoiceId;
        private String subChannel;
        private String tenLevelClass;
        private long updTime;
        private String updUser;
        private long overdueDate;
        private String businessType;

        protected RecordsBean(Parcel in) {
            acctBusinessType = in.readString();
            acctClass = in.readString();
            acctClassType = in.readString();
            acctLimit = in.readString();
            acctNo = in.readString();
            acctStatus = in.readString();
            acctType = in.readString();
            amountNo = in.readString();
            bacctNo = in.readString();
            baserialno = in.readString();
            belongFirstTeam = in.readString();
            belongSecondTeam = in.readString();
            borrowerLocation = in.readString();
            btDate = in.readLong();
            businessrate = in.readString();
            caseId = in.readString();
            collStrategy = in.readString();
            collStrength = in.readString();
            credLimit = in.readDouble();
            crtTime = in.readLong();
            crtUser = in.readString();
            curCashAmt = in.readString();
            curCycBal = in.readString();
            curCycStmtBal = in.readString();
            curInsSpendBal = in.readString();
            curSpendBal = in.readString();
            currency = in.readString();
            custNo = in.readString();
            debitInterestAmts = in.readString();
            deductionCardNo = in.readString();
            deductionCardStatus = in.readString();
            disputeAmt = in.readString();
            exceedLmtAmt = in.readString();
            insPoundageBal = in.readString();
            insuranceCompany = in.readString();
            interestAmt = in.readString();
            isremote = in.readString();
            lastCycDueAmt = in.readString();
            lastCycStmtBal = in.readString();
            lastDebitTransAmt = in.readString();
            lastLoanTransAmt = in.readString();
            loanAmt = in.readString();
            loanDate = in.readLong();
            m1Amt = in.readString();
            m2Amt = in.readString();
            m3Amt = in.readString();
            m4Amt = in.readString();
            m5Amt = in.readString();
            m6Amt = in.readString();
            m7Amt = in.readString();
            m7PlusAmt = in.readString();
            mainAcctFlag = in.readString();
            manager = in.readString();
            managerOrg = in.readString();
            odInterestAmt = in.readString();
            odPenIntAmt = in.readString();
            odPrincipalAmt = in.readString();
            outstandInt = in.readString();
            outstandPri = in.readString();
            ovduAmts = in.readString();
            ovduMonth = in.readInt();
            ovduPrincipalAmts = in.readString();
            overdrawAmt = in.readString();
            overdrawLimit = in.readString();
            overdueAmt = in.readString();
            overdueDays = in.readInt();
            overdueStage = in.readString();
            overdueTimes = in.readInt();
            paidAmt = in.readString();
            paidFlag = in.readString();
            payDueDate = in.readLong();
            penalAmt = in.readString();
            productType = in.readString();
            projectNo = in.readString();
            repayDay = in.readString();
            repayType = in.readString();
            repaymentInt = in.readString();
            repaymentPri = in.readString();
            riskLevel = in.readString();
            rootOrgId = in.readString();
            satisfyFlag = in.readString();
            secOrgId = in.readString();
            sortRuleNo = in.readString();
            sourceCode = in.readString();
            spendIniAmt = in.readString();
            straAiId = in.readString();
            straEmailId = in.readString();
            straMailId = in.readString();
            straSmsId = in.readString();
            straUpdDate = in.readLong();
            straVoiceId = in.readString();
            subChannel = in.readString();
            tenLevelClass = in.readString();
            updTime = in.readLong();
            updUser = in.readString();
            overdueDate = in.readLong();
            businessType = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(acctBusinessType);
            dest.writeString(acctClass);
            dest.writeString(acctClassType);
            dest.writeString(acctLimit);
            dest.writeString(acctNo);
            dest.writeString(acctStatus);
            dest.writeString(acctType);
            dest.writeString(amountNo);
            dest.writeString(bacctNo);
            dest.writeString(baserialno);
            dest.writeString(belongFirstTeam);
            dest.writeString(belongSecondTeam);
            dest.writeString(borrowerLocation);
            dest.writeLong(btDate);
            dest.writeString(businessrate);
            dest.writeString(caseId);
            dest.writeString(collStrategy);
            dest.writeString(collStrength);
            dest.writeDouble(credLimit);
            dest.writeLong(crtTime);
            dest.writeString(crtUser);
            dest.writeString(curCashAmt);
            dest.writeString(curCycBal);
            dest.writeString(curCycStmtBal);
            dest.writeString(curInsSpendBal);
            dest.writeString(curSpendBal);
            dest.writeString(currency);
            dest.writeString(custNo);
            dest.writeString(debitInterestAmts);
            dest.writeString(deductionCardNo);
            dest.writeString(deductionCardStatus);
            dest.writeString(disputeAmt);
            dest.writeString(exceedLmtAmt);
            dest.writeString(insPoundageBal);
            dest.writeString(insuranceCompany);
            dest.writeString(interestAmt);
            dest.writeString(isremote);
            dest.writeString(lastCycDueAmt);
            dest.writeString(lastCycStmtBal);
            dest.writeString(lastDebitTransAmt);
            dest.writeString(lastLoanTransAmt);
            dest.writeString(loanAmt);
            dest.writeLong(loanDate);
            dest.writeString(m1Amt);
            dest.writeString(m2Amt);
            dest.writeString(m3Amt);
            dest.writeString(m4Amt);
            dest.writeString(m5Amt);
            dest.writeString(m6Amt);
            dest.writeString(m7Amt);
            dest.writeString(m7PlusAmt);
            dest.writeString(mainAcctFlag);
            dest.writeString(manager);
            dest.writeString(managerOrg);
            dest.writeString(odInterestAmt);
            dest.writeString(odPenIntAmt);
            dest.writeString(odPrincipalAmt);
            dest.writeString(outstandInt);
            dest.writeString(outstandPri);
            dest.writeString(ovduAmts);
            dest.writeInt(ovduMonth);
            dest.writeString(ovduPrincipalAmts);
            dest.writeString(overdrawAmt);
            dest.writeString(overdrawLimit);
            dest.writeString(overdueAmt);
            dest.writeInt(overdueDays);
            dest.writeString(overdueStage);
            dest.writeInt(overdueTimes);
            dest.writeString(paidAmt);
            dest.writeString(paidFlag);
            dest.writeLong(payDueDate);
            dest.writeString(penalAmt);
            dest.writeString(productType);
            dest.writeString(projectNo);
            dest.writeString(repayDay);
            dest.writeString(repayType);
            dest.writeString(repaymentInt);
            dest.writeString(repaymentPri);
            dest.writeString(riskLevel);
            dest.writeString(rootOrgId);
            dest.writeString(satisfyFlag);
            dest.writeString(secOrgId);
            dest.writeString(sortRuleNo);
            dest.writeString(sourceCode);
            dest.writeString(spendIniAmt);
            dest.writeString(straAiId);
            dest.writeString(straEmailId);
            dest.writeString(straMailId);
            dest.writeString(straSmsId);
            dest.writeLong(straUpdDate);
            dest.writeString(straVoiceId);
            dest.writeString(subChannel);
            dest.writeString(tenLevelClass);
            dest.writeLong(updTime);
            dest.writeString(updUser);
            dest.writeLong(overdueDate);
            dest.writeString(businessType);
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

        public String getAcctBusinessType() {
            return acctBusinessType;
        }

        public void setAcctBusinessType(String acctBusinessType) {
            this.acctBusinessType = acctBusinessType;
        }

        public String getAcctClass() {
            return acctClass;
        }

        public void setAcctClass(String acctClass) {
            this.acctClass = acctClass;
        }

        public String getAcctClassType() {
            return acctClassType;
        }

        public void setAcctClassType(String acctClassType) {
            this.acctClassType = acctClassType;
        }

        public String getAcctLimit() {
            return acctLimit;
        }

        public void setAcctLimit(String acctLimit) {
            this.acctLimit = acctLimit;
        }

        public String getAcctNo() {
            return acctNo;
        }

        public void setAcctNo(String acctNo) {
            this.acctNo = acctNo;
        }

        public String getAcctStatus() {
            return acctStatus;
        }

        public void setAcctStatus(String acctStatus) {
            this.acctStatus = acctStatus;
        }

        public String getAcctType() {
            return acctType;
        }

        public void setAcctType(String acctType) {
            this.acctType = acctType;
        }

        public String getAmountNo() {
            return amountNo;
        }

        public void setAmountNo(String amountNo) {
            this.amountNo = amountNo;
        }

        public String getBacctNo() {
            return bacctNo;
        }

        public void setBacctNo(String bacctNo) {
            this.bacctNo = bacctNo;
        }

        public String getBaserialno() {
            return baserialno;
        }

        public void setBaserialno(String baserialno) {
            this.baserialno = baserialno;
        }

        public String getBelongFirstTeam() {
            return belongFirstTeam;
        }

        public void setBelongFirstTeam(String belongFirstTeam) {
            this.belongFirstTeam = belongFirstTeam;
        }

        public String getBelongSecondTeam() {
            return belongSecondTeam;
        }

        public void setBelongSecondTeam(String belongSecondTeam) {
            this.belongSecondTeam = belongSecondTeam;
        }

        public String getBorrowerLocation() {
            return borrowerLocation;
        }

        public void setBorrowerLocation(String borrowerLocation) {
            this.borrowerLocation = borrowerLocation;
        }

        public long getBtDate() {
            return btDate;
        }

        public void setBtDate(long btDate) {
            this.btDate = btDate;
        }

        public String getBusinessrate() {
            return businessrate;
        }

        public void setBusinessrate(String businessrate) {
            this.businessrate = businessrate;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getCollStrategy() {
            return collStrategy;
        }

        public void setCollStrategy(String collStrategy) {
            this.collStrategy = collStrategy;
        }

        public String getCollStrength() {
            return collStrength;
        }

        public void setCollStrength(String collStrength) {
            this.collStrength = collStrength;
        }

        public double getCredLimit() {
            return credLimit;
        }

        public void setCredLimit(double credLimit) {
            this.credLimit = credLimit;
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

        public String getCurCashAmt() {
            return curCashAmt;
        }

        public void setCurCashAmt(String curCashAmt) {
            this.curCashAmt = curCashAmt;
        }

        public String getCurCycBal() {
            return curCycBal;
        }

        public void setCurCycBal(String curCycBal) {
            this.curCycBal = curCycBal;
        }

        public String getCurCycStmtBal() {
            return curCycStmtBal;
        }

        public void setCurCycStmtBal(String curCycStmtBal) {
            this.curCycStmtBal = curCycStmtBal;
        }

        public String getCurInsSpendBal() {
            return curInsSpendBal;
        }

        public void setCurInsSpendBal(String curInsSpendBal) {
            this.curInsSpendBal = curInsSpendBal;
        }

        public String getCurSpendBal() {
            return curSpendBal;
        }

        public void setCurSpendBal(String curSpendBal) {
            this.curSpendBal = curSpendBal;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getDebitInterestAmts() {
            return debitInterestAmts;
        }

        public void setDebitInterestAmts(String debitInterestAmts) {
            this.debitInterestAmts = debitInterestAmts;
        }

        public String getDeductionCardNo() {
            return deductionCardNo;
        }

        public void setDeductionCardNo(String deductionCardNo) {
            this.deductionCardNo = deductionCardNo;
        }

        public String getDeductionCardStatus() {
            return deductionCardStatus;
        }

        public void setDeductionCardStatus(String deductionCardStatus) {
            this.deductionCardStatus = deductionCardStatus;
        }

        public String getDisputeAmt() {
            return disputeAmt;
        }

        public void setDisputeAmt(String disputeAmt) {
            this.disputeAmt = disputeAmt;
        }

        public String getExceedLmtAmt() {
            return exceedLmtAmt;
        }

        public void setExceedLmtAmt(String exceedLmtAmt) {
            this.exceedLmtAmt = exceedLmtAmt;
        }

        public String getInsPoundageBal() {
            return insPoundageBal;
        }

        public void setInsPoundageBal(String insPoundageBal) {
            this.insPoundageBal = insPoundageBal;
        }

        public String getInsuranceCompany() {
            return insuranceCompany;
        }

        public void setInsuranceCompany(String insuranceCompany) {
            this.insuranceCompany = insuranceCompany;
        }

        public String getInterestAmt() {
            return interestAmt;
        }

        public void setInterestAmt(String interestAmt) {
            this.interestAmt = interestAmt;
        }

        public String getIsremote() {
            return isremote;
        }

        public void setIsremote(String isremote) {
            this.isremote = isremote;
        }

        public String getLastCycDueAmt() {
            return lastCycDueAmt;
        }

        public void setLastCycDueAmt(String lastCycDueAmt) {
            this.lastCycDueAmt = lastCycDueAmt;
        }

        public String getLastCycStmtBal() {
            return lastCycStmtBal;
        }

        public void setLastCycStmtBal(String lastCycStmtBal) {
            this.lastCycStmtBal = lastCycStmtBal;
        }

        public String getLastDebitTransAmt() {
            return lastDebitTransAmt;
        }

        public void setLastDebitTransAmt(String lastDebitTransAmt) {
            this.lastDebitTransAmt = lastDebitTransAmt;
        }

        public String getLastLoanTransAmt() {
            return lastLoanTransAmt;
        }

        public void setLastLoanTransAmt(String lastLoanTransAmt) {
            this.lastLoanTransAmt = lastLoanTransAmt;
        }

        public String getLoanAmt() {
            return loanAmt;
        }

        public void setLoanAmt(String loanAmt) {
            this.loanAmt = loanAmt;
        }

        public long getLoanDate() {
            return loanDate;
        }

        public void setLoanDate(long loanDate) {
            this.loanDate = loanDate;
        }

        public String getM1Amt() {
            return m1Amt;
        }

        public void setM1Amt(String m1Amt) {
            this.m1Amt = m1Amt;
        }

        public String getM2Amt() {
            return m2Amt;
        }

        public void setM2Amt(String m2Amt) {
            this.m2Amt = m2Amt;
        }

        public String getM3Amt() {
            return m3Amt;
        }

        public void setM3Amt(String m3Amt) {
            this.m3Amt = m3Amt;
        }

        public String getM4Amt() {
            return m4Amt;
        }

        public void setM4Amt(String m4Amt) {
            this.m4Amt = m4Amt;
        }

        public String getM5Amt() {
            return m5Amt;
        }

        public void setM5Amt(String m5Amt) {
            this.m5Amt = m5Amt;
        }

        public String getM6Amt() {
            return m6Amt;
        }

        public void setM6Amt(String m6Amt) {
            this.m6Amt = m6Amt;
        }

        public String getM7Amt() {
            return m7Amt;
        }

        public void setM7Amt(String m7Amt) {
            this.m7Amt = m7Amt;
        }

        public String getM7PlusAmt() {
            return m7PlusAmt;
        }

        public void setM7PlusAmt(String m7PlusAmt) {
            this.m7PlusAmt = m7PlusAmt;
        }

        public String getMainAcctFlag() {
            return mainAcctFlag;
        }

        public void setMainAcctFlag(String mainAcctFlag) {
            this.mainAcctFlag = mainAcctFlag;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getManagerOrg() {
            return managerOrg;
        }

        public void setManagerOrg(String managerOrg) {
            this.managerOrg = managerOrg;
        }

        public String getOdInterestAmt() {
            return odInterestAmt;
        }

        public void setOdInterestAmt(String odInterestAmt) {
            this.odInterestAmt = odInterestAmt;
        }

        public String getOdPenIntAmt() {
            return odPenIntAmt;
        }

        public void setOdPenIntAmt(String odPenIntAmt) {
            this.odPenIntAmt = odPenIntAmt;
        }

        public String getOdPrincipalAmt() {
            return odPrincipalAmt;
        }

        public void setOdPrincipalAmt(String odPrincipalAmt) {
            this.odPrincipalAmt = odPrincipalAmt;
        }

        public String getOutstandInt() {
            return outstandInt;
        }

        public void setOutstandInt(String outstandInt) {
            this.outstandInt = outstandInt;
        }

        public String getOutstandPri() {
            return outstandPri;
        }

        public void setOutstandPri(String outstandPri) {
            this.outstandPri = outstandPri;
        }

        public String getOvduAmts() {
            return ovduAmts;
        }

        public void setOvduAmts(String ovduAmts) {
            this.ovduAmts = ovduAmts;
        }

        public int getOvduMonth() {
            return ovduMonth;
        }

        public void setOvduMonth(int ovduMonth) {
            this.ovduMonth = ovduMonth;
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

        public String getOverdrawLimit() {
            return overdrawLimit;
        }

        public void setOverdrawLimit(String overdrawLimit) {
            this.overdrawLimit = overdrawLimit;
        }

        public String getOverdueAmt() {
            return overdueAmt;
        }

        public void setOverdueAmt(String overdueAmt) {
            this.overdueAmt = overdueAmt;
        }

        public int getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(int overdueDays) {
            this.overdueDays = overdueDays;
        }

        public String getOverdueStage() {
            return overdueStage;
        }

        public void setOverdueStage(String overdueStage) {
            this.overdueStage = overdueStage;
        }

        public int getOverdueTimes() {
            return overdueTimes;
        }

        public void setOverdueTimes(int overdueTimes) {
            this.overdueTimes = overdueTimes;
        }

        public String getPaidAmt() {
            return paidAmt;
        }

        public void setPaidAmt(String paidAmt) {
            this.paidAmt = paidAmt;
        }

        public String getPaidFlag() {
            return paidFlag;
        }

        public void setPaidFlag(String paidFlag) {
            this.paidFlag = paidFlag;
        }

        public long getPayDueDate() {
            return payDueDate;
        }

        public void setPayDueDate(long payDueDate) {
            this.payDueDate = payDueDate;
        }

        public String getPenalAmt() {
            return penalAmt;
        }

        public void setPenalAmt(String penalAmt) {
            this.penalAmt = penalAmt;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public String getRepayDay() {
            return repayDay;
        }

        public void setRepayDay(String repayDay) {
            this.repayDay = repayDay;
        }

        public String getRepayType() {
            return repayType;
        }

        public void setRepayType(String repayType) {
            this.repayType = repayType;
        }

        public String getRepaymentInt() {
            return repaymentInt;
        }

        public void setRepaymentInt(String repaymentInt) {
            this.repaymentInt = repaymentInt;
        }

        public String getRepaymentPri() {
            return repaymentPri;
        }

        public void setRepaymentPri(String repaymentPri) {
            this.repaymentPri = repaymentPri;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getRootOrgId() {
            return rootOrgId;
        }

        public void setRootOrgId(String rootOrgId) {
            this.rootOrgId = rootOrgId;
        }

        public String getSatisfyFlag() {
            return satisfyFlag;
        }

        public void setSatisfyFlag(String satisfyFlag) {
            this.satisfyFlag = satisfyFlag;
        }

        public String getSecOrgId() {
            return secOrgId;
        }

        public void setSecOrgId(String secOrgId) {
            this.secOrgId = secOrgId;
        }

        public String getSortRuleNo() {
            return sortRuleNo;
        }

        public void setSortRuleNo(String sortRuleNo) {
            this.sortRuleNo = sortRuleNo;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String getSpendIniAmt() {
            return spendIniAmt;
        }

        public void setSpendIniAmt(String spendIniAmt) {
            this.spendIniAmt = spendIniAmt;
        }

        public String getStraAiId() {
            return straAiId;
        }

        public void setStraAiId(String straAiId) {
            this.straAiId = straAiId;
        }

        public String getStraEmailId() {
            return straEmailId;
        }

        public void setStraEmailId(String straEmailId) {
            this.straEmailId = straEmailId;
        }

        public String getStraMailId() {
            return straMailId;
        }

        public void setStraMailId(String straMailId) {
            this.straMailId = straMailId;
        }

        public String getStraSmsId() {
            return straSmsId;
        }

        public void setStraSmsId(String straSmsId) {
            this.straSmsId = straSmsId;
        }

        public long getStraUpdDate() {
            return straUpdDate;
        }

        public void setStraUpdDate(long straUpdDate) {
            this.straUpdDate = straUpdDate;
        }

        public String getStraVoiceId() {
            return straVoiceId;
        }

        public void setStraVoiceId(String straVoiceId) {
            this.straVoiceId = straVoiceId;
        }

        public String getSubChannel() {
            return subChannel;
        }

        public void setSubChannel(String subChannel) {
            this.subChannel = subChannel;
        }

        public String getTenLevelClass() {
            return tenLevelClass;
        }

        public void setTenLevelClass(String tenLevelClass) {
            this.tenLevelClass = tenLevelClass;
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

        public long getOverdueDate() {
            return overdueDate;
        }

        public void setOverdueDate(long overdueDate) {
            this.overdueDate = overdueDate;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }
    }
}
