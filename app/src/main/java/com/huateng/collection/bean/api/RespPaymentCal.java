package com.huateng.collection.bean.api;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-06.
 */
public class RespPaymentCal extends RespBase{

    private List<RespPaymentCalItem> calResult;

    public List<RespPaymentCalItem> getCalResult() {
        return calResult;
    }

    public void setData(List<RespPaymentCalItem> calResult) {
        this.calResult = calResult;
    }
}
