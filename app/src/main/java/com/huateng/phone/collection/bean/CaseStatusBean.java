package com.huateng.phone.collection.bean;

/**
 * author: yichuan
 * Created on: 2020-05-13 09:57
 * description:
 */
public class CaseStatusBean {
    private String resultCode;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "CaseStatusBean{" +
                "resultCode='" + resultCode + '\'' +
                '}';
    }
}
