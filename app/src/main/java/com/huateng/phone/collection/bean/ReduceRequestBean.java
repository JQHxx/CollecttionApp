package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-05-16 15:29
 * description:
 */
public class ReduceRequestBean {
    private String productNo;
    private String loanNo;
    private String loanAmt;
    private String endDate;
    private String overdueDays;
    private String loanPri;
    private String loanInt;
    private String loanTotal;
    private String reducePri;
    private String reduceInt;//申请减免透支利息
    private String reduceTotal;
    private String planRepayTotal;
    private String acctNo;
    private String shouldBreach;
    private double penalAmt;
    private double reduceFee;
    private double reduceOth;
    private String isEarsettlement;
    private String businessType;

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(String loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getLoanPri() {
        return loanPri;
    }

    public void setLoanPri(String loanPri) {
        this.loanPri = loanPri;
    }

    public String getLoanInt() {
        return loanInt;
    }

    public void setLoanInt(String loanInt) {
        this.loanInt = loanInt;
    }

    public String getLoanTotal() {
        return loanTotal;
    }

    public void setLoanTotal(String loanTotal) {
        this.loanTotal = loanTotal;
    }

    public String getReducePri() {
        return reducePri;
    }

    public void setReducePri(String reducePri) {
        this.reducePri = reducePri;
    }

    public String getReduceInt() {
        return reduceInt;
    }

    public void setReduceInt(String reduceInt) {
        this.reduceInt = reduceInt;
    }

    public String getReduceTotal() {
        return reduceTotal;
    }

    public void setReduceTotal(String reduceTotal) {
        this.reduceTotal = reduceTotal;
    }

    public String getPlanRepayTotal() {
        return planRepayTotal;
    }

    public void setPlanRepayTotal(String planRepayTotal) {
        this.planRepayTotal = planRepayTotal;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getShouldBreach() {
        return shouldBreach;
    }

    public void setShouldBreach(String shouldBreach) {
        this.shouldBreach = shouldBreach;
    }

    public double getPenalAmt() {
        return penalAmt;
    }

    public void setPenalAmt(double penalAmt) {
        this.penalAmt = penalAmt;
    }

    public double getReduceFee() {
        return reduceFee;
    }

    public void setReduceFee(double reduceFee) {
        this.reduceFee = reduceFee;
    }

    public double getReduceOth() {
        return reduceOth;
    }

    public void setReduceOth(double reduceOth) {
        this.reduceOth = reduceOth;
    }

    public String getIsEarsettlement() {
        return isEarsettlement;
    }

    public void setIsEarsettlement(String isEarsettlement) {
        this.isEarsettlement = isEarsettlement;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
