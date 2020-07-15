package com.huateng.phone.collection.bean.api;

/**
 * @author dengzh
 * @description
 * @time 2016-12-12.
 */
public class RespStaticsItem {
    private String caseId;
    private String custName;
    private String visitDate;
    private String oprateOfficer;
    private String collector;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getOprateOfficer() {
        return oprateOfficer;
    }

    public void setOprateOfficer(String oprateOfficer) {
        this.oprateOfficer = oprateOfficer;
    }
}
