package com.huateng.collection.bean.api;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-06.
 */
public class RespStatics extends RespBase{

    private List<RespStaticsItem> datalist;

    public List<RespStaticsItem> getDatalist() {
        return datalist;
    }

    public void setData(List<RespStaticsItem> datalist) {
        this.datalist = datalist;
    }
}
