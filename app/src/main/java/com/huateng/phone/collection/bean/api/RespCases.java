package com.huateng.phone.collection.bean.api;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class RespCases extends  RespBase{

    private List<RespCaseSummary> caseList;

    public List<RespCaseSummary> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<RespCaseSummary> caseList) {
        this.caseList = caseList;
    }
}
