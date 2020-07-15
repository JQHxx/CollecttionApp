package com.huateng.phone.collection.bean.api;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class RespCaseDetails extends  RespBase{

    private List<RespCaseDetail> caseList;

    public List<RespCaseDetail> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<RespCaseDetail> caseList) {
        this.caseList = caseList;
    }
}
