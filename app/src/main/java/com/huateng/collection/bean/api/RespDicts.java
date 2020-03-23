package com.huateng.collection.bean.api;

import com.huateng.collection.bean.orm.Dic;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-15.
 */
public class RespDicts extends  RespBase{
    private List<Dic> dataList;

    public List<Dic> getDataList() {
        return dataList;
    }

    public void setDataList(List<Dic> dataList) {
        this.dataList = dataList;
    }
}
