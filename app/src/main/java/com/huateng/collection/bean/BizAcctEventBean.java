package com.huateng.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-05-14 12:27
 * description:
 */
public class BizAcctEventBean {
    private int position;//
    private String planRepayTotal;//计划归还总额
    private String type ; // 0 贷款 1 信用卡

    public BizAcctEventBean(int position,String planRepayTotal,String type){
        this.position = position;
        this.planRepayTotal = planRepayTotal;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPlanRepayTotal() {
        return planRepayTotal;
    }

    public void setPlanRepayTotal(String planRepayTotal) {
        this.planRepayTotal = planRepayTotal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
