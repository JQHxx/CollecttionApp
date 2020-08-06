package com.huateng.phone.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-13 20:50
 * description:
 */
public class BizAcctInfoBean {


    private List<BizAcctAccountBean> bizAcctAccount;
    private List<BizAcctCardBean> bizAcctCard;

    public List<BizAcctAccountBean> getBizAcctAccount() {
        return bizAcctAccount;
    }

    public void setBizAcctAccount(List<BizAcctAccountBean> bizAcctAccount) {
        this.bizAcctAccount = bizAcctAccount;
    }

    public List<BizAcctCardBean> getBizAcctCard() {
        return bizAcctCard;
    }

    public void setBizAcctCard(List<BizAcctCardBean> bizAcctCard) {
        this.bizAcctCard = bizAcctCard;
    }

    public static class BizAcctAccountBean {
        /**
         * bacctNo : 77010110000685055
         * loanAmt : 100000.0
         * odPenIntAmt : 967.44
         * odPrincipalAmt : 0.0
         * ovduAmts : 967.44
         * overdueDays : 0
         * payDueDate : 1397059200000
         * productName : 白领通授信额度
         */

        private String bacctNo;//项目编号
        private Double loanAmt;//业务本金
        private Double loanInt;//尚欠利罚息
        private Double loanPri;//尚欠本金
        private Double loanTotal;//尚欠总额
        private String overdueDays;//逾期天数
        private long payDueDate;//到期日
        private String productName;//产品名称
        private String productNo;//项目类型
        private String businessType;//

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getProductNo() {
            return productNo;
        }

        public void setProductNo(String productNo) {
            this.productNo = productNo;
        }

        public String getBacctNo() {
            return bacctNo;
        }

        public void setBacctNo(String bacctNo) {
            this.bacctNo = bacctNo;
        }

        public Double getLoanAmt() {
            return loanAmt;
        }

        public void setLoanAmt(Double loanAmt) {
            this.loanAmt = loanAmt;
        }

        public Double getOdPenIntAmt() {
            return loanInt;
        }

        public void setOdPenIntAmt(Double odPenIntAmt) {
            this.loanInt = odPenIntAmt;
        }

        public Double getOdPrincipalAmt() {
            return loanPri;
        }

        public void setOdPrincipalAmt(Double odPrincipalAmt) {
            this.loanPri = odPrincipalAmt;
        }

        public Double getOvduAmts() {
            return loanTotal;
        }

        public void setOvduAmts(Double ovduAmts) {
            this.loanTotal = ovduAmts;
        }

        public String getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(String overdueDays) {
            this.overdueDays = overdueDays;
        }

        public long getPayDueDate() {
            return payDueDate;
        }

        public void setPayDueDate(long payDueDate) {
            this.payDueDate = payDueDate;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }


    public static class BizAcctCardBean {
        private String productName;//卡种
        private String currency;//币种
        private String creditCardNo;//卡号
        private double acctBalance;//逾期金额
        private double ovduPrincipalAmts;//逾期本金
        private String ovduMonth;//逾期期数
        private double penalAmt;//违约金
        private double interestAmt;//投资利息
        private double periodRestFee;//分期提前结清手续费
        private String businessType;
        private String cardType;
        private double periodRestPri;//分期剩余本金

        public double getPeriodRestPri() {
            return periodRestPri;
        }

        public void setPeriodRestPri(double periodRestPri) {
            this.periodRestPri = periodRestPri;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public double getAcctBalance() {
            return acctBalance;
        }

        public void setAcctBalance(double acctBalance) {
            this.acctBalance = acctBalance;
        }

        public void setCurrAcctBalance(double currAcctBalance) {
            this.acctBalance = currAcctBalance;
        }

        public void setOvduPrincipalAmts(double ovduPrincipalAmts) {
            this.ovduPrincipalAmts = ovduPrincipalAmts;
        }

        public void setPenalAmt(double penalAmt) {
            this.penalAmt = penalAmt;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCreditCardNo() {
            return creditCardNo;
        }

        public void setCreditCardNo(String creditCardNo) {
            this.creditCardNo = creditCardNo;
        }

        public Double getCurrAcctBalance() {
            return acctBalance;
        }

        public void setCurrAcctBalance(Double currAcctBalance) {
            this.acctBalance = currAcctBalance;
        }

        public Double getOvduPrincipalAmts() {
            return ovduPrincipalAmts;
        }

        public void setOvduPrincipalAmts(Double ovduPrincipalAmts) {
            this.ovduPrincipalAmts = ovduPrincipalAmts;
        }

        public String getOvduMonth() {
            return ovduMonth;
        }

        public void setOvduMonth(String ovduMonth) {
            this.ovduMonth = ovduMonth;
        }

        public Double getPenalAmt() {
            return penalAmt;
        }

        public void setPenalAmt(Double penalAmt) {
            this.penalAmt = penalAmt;
        }

        public double getInterestAmt() {
            return interestAmt;
        }

        public void setInterestAmt(double interestAmt) {
            this.interestAmt = interestAmt;
        }

        public double getPeriodRestFee() {
            return periodRestFee;
        }

        public void setPeriodRestFee(double periodRestFee) {
            this.periodRestFee = periodRestFee;
        }
    }
}
