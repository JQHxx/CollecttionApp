package com.huateng.collection.bean.api;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class RespCusts extends RespBase{

    private List<RespCustSummary> custList;

    public List<RespCustSummary> getCustList() {
        return custList;
    }

    public void setCustList(List<RespCustSummary> custList) {
        this.custList = custList;
    }
}
