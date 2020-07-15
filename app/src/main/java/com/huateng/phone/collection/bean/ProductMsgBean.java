package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-03-30 14:20
 * description:
 */
public class ProductMsgBean {
    private String productName;//产品名称
    private String endDate;//结束时间
    private String startDate;//开始时间
    private String debitCardNumber;//借记卡卡号
    private String type;//额度状态
    private String creditLimitAmt;//总额度
    private String creditlimitBal;//可用额度

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreditLimitAmt() {
        return creditLimitAmt;
    }

    public void setCreditLimitAmt(String creditLimitAmt) {
        this.creditLimitAmt = creditLimitAmt;
    }

    public String getCreditlimitBal() {
        return creditlimitBal;
    }

    public void setCreditlimitBal(String creditlimitBal) {
        this.creditlimitBal = creditlimitBal;
    }
}
