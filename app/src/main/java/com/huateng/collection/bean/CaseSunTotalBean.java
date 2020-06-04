package com.huateng.collection.bean;

/**
 * author: yichuan
 * Created on: 2020/6/3 20:14
 * description:
 */
public class CaseSunTotalBean {
    private int caseSumTotal;
    private int caseCount;
    private int recordTime;
    private long crtTime;
    private String outBoundName;
    private String reportIntegrity;

    public int getCaseSumTotal() {
        return caseSumTotal;
    }

    public void setCaseSumTotal(int caseSumTotal) {
        this.caseSumTotal = caseSumTotal;
    }

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }

    public long getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(long crtTime) {
        this.crtTime = crtTime;
    }

    public String getOutBoundName() {
        return outBoundName;
    }

    public void setOutBoundName(String outBoundName) {
        this.outBoundName = outBoundName;
    }

    public String getReportIntegrity() {
        return reportIntegrity;
    }

    public void setReportIntegrity(String reportIntegrity) {
        this.reportIntegrity = reportIntegrity;
    }
}
