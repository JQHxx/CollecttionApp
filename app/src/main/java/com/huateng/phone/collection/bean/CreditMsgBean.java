package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-04-03 14:24
 * description:
 */
public class CreditMsgBean {
    private String name;
    private String  totalAmount;
    private String startDate;
    private String endDate;
    private String userLimit;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(String userLimit) {
        this.userLimit = userLimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
