package com.huateng.collection.bean.orm;

import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

/**
 * author: yichuan
 * Created on: 2020-05-18 17:20
 * description:
 */
@Table
public  class DictItemBean {
    /**
     * dataCode : 100
     * dataTrsVal : 无法联系
     * dataVal : 100
     * description : 无法联系
     * dictCode : ZDTAAY
     * dictName : 退案原因
     * id : 201712281114120025000000000692
     * isDel : 1
     * orderby : 1025
     * sysCode : default
     * sysName : 默认
     * versionNum : 1
     */

    private String dataCode;
    private String dataTrsVal;
    private String dataVal;
    private String description;
    private String dictCode;
    private String dictName;
    @SerializedName("id")
    private String idNo;
    private int isDel;
    private int orderby;
    private String sysCode;
    private String sysName;
    private int versionNum;

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataTrsVal() {
        return dataTrsVal;
    }

    public void setDataTrsVal(String dataTrsVal) {
        this.dataTrsVal = dataTrsVal;
    }

    public String getDataVal() {
        return dataVal;
    }

    public void setDataVal(String dataVal) {
        this.dataVal = dataVal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getId() {
        return idNo;
    }

    public void setId(String id) {
        this.idNo = id;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }
}
