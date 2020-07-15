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
public class RespPhone  extends RespBase implements Serializable{

    private String name;
    private String relWithCust;
    private String telId;
    private String telNo;
    private String telType;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelWithCust() {
        return relWithCust;
    }

    public void setRelWithCust(String relWithCust) {
        this.relWithCust = relWithCust;
    }

    public String getTelId() {
        return telId;
    }

    public void setTelId(String telId) {
        this.telId = telId;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }
}
