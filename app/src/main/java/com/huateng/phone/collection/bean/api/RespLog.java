package com.huateng.phone.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
@Table
public class RespLog implements Serializable {

    private String actCode;
    private String actDate;
    private String actName;
    private String actRemark;
    @Expose(serialize = false, deserialize = false)
    private String caseId;
    @Expose(serialize = false, deserialize = false)
    private String bizId;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActRemark() {
        return actRemark;
    }

    public void setActRemark(String actRemark) {
        this.actRemark = actRemark;
    }

    @Override
    public String toString() {
        return "RespLog{" +
                "actCode='" + actCode + '\'' +
                ", actDate='" + actDate + '\'' +
                ", actName='" + actName + '\'' +
                ", actRemark='" + actRemark + '\'' +
                ", caseId='" + caseId + '\'' +
                ", bizId='" + bizId + '\'' +
                '}';
    }
}
