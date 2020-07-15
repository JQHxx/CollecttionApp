package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-05-14 09:34
 * description:
 */
public class BizAcctItemBean {

    private String productName;//产品名称
    private String productNo;//产品类型
    private String loanNo;//项目编号
    private double loanAmt;//业务本金
    private long endDate;//到期日
    private String overdueDays;//逾期天数
    private double loanPri;//尚欠本金
    private double loanInt;//尚欠利罚息
    private double loanTotal;//尚欠总额
    private double reducePri;//申请减免本金
    private double reduceInt;//申请减免利罚息
    private double reduceAccrual;//申请减免利息
    private double reduceTotal;//申请减免总额
    private String planRepayTotal;//计划归还总额
    private String acctNo;//卡号
    private double shouldBreach;//违约金
    private double penalAmt;// 分期提前结清手续费
    private double reduceFee;//申请减免违约金
    private double reduceOth;//申请减免分期提前结清手续费
    private String isEarSettlement;//是否申请分期提前结清
    private String currency;//币种
    private String businessType;//业务类型
    private String cardType;//卡种

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

    public double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public double getLoanPri() {
        return loanPri;
    }

    public void setLoanPri(double loanPri) {
        this.loanPri = loanPri;
    }

    public double getLoanInt() {
        return loanInt;
    }

    public void setLoanInt(double loanInt) {
        this.loanInt = loanInt;
    }

    public double getLoanTotal() {
        return loanTotal;
    }

    public void setLoanTotal(double loanTotal) {
        this.loanTotal = loanTotal;
    }

    public double getReducePri() {
        return reducePri;
    }

    public void setReducePri(double reducePri) {
        this.reducePri = reducePri;
    }

    public double getReduceInt() {
        return reduceInt;
    }

    public void setReduceInt(double reduceInt) {
        this.reduceInt = reduceInt;
    }

    public double getReduceTotal() {
        return reduceTotal;
    }

    public void setReduceTotal(double reduceTotal) {
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

    public double getShouldBreach() {
        return shouldBreach;
    }

    public void setShouldBreach(double shouldBreach) {
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

    public String getIsEarSettlement() {
        return isEarSettlement;
    }

    public void setIsEarSettlement(String isEarSettlement) {
        this.isEarSettlement = isEarSettlement;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getReduceAccrual() {
        return reduceAccrual;
    }

    public void setReduceAccrual(double reduceAccrual) {
        this.reduceAccrual = reduceAccrual;
    }
}
