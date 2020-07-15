package com.huateng.phone.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 * 案件详情
 */
@Table
public class RespCaseDetail implements Serializable {

    @Unique
    @Expose(serialize = false, deserialize = false)
    private String bizId;
    @Ignore
    private List<RespAccount> acctList;
    @Ignore
    private List<RespAddress> addrList;
    @Ignore
    private List<RespLog> actLogList;
    @Ignore
    private List<RespPhone> telList;

    private int age;
    private float caseAmt;
    private String caseId;
    private String certNo;
    private String compName;
    private String custId;
    private String deptOffice;
    private String gender;
    private String incollDate;
    private String name;
    @Expose(serialize = false, deserialize = false)
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<RespAccount> getAcctList() {
        return acctList;
    }

    public void setAcctList(List<RespAccount> acctList) {
        this.acctList = acctList;
    }

    public List<RespAddress> getAddrList() {
        return addrList;
    }

    public void setAddrList(List<RespAddress> addrList) {
        this.addrList = addrList;
    }

    public List<RespLog> getActLogList() {
        return actLogList;
    }

    public void setActLogList(List<RespLog> actLogList) {
        this.actLogList = actLogList;
    }

    public List<RespPhone> getTelList() {
        return telList;
    }

    public void setTelList(List<RespPhone> telList) {
        this.telList = telList;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getCaseAmt() {
        return caseAmt;
    }

    public void setCaseAmt(float caseAmt) {
        this.caseAmt = caseAmt;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDeptOffice() {
        return deptOffice;
    }

    public void setDeptOffice(String deptOffice) {
        this.deptOffice = deptOffice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIncollDate() {
        return incollDate;
    }

    public void setIncollDate(String incollDate) {
        this.incollDate = incollDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }
}
