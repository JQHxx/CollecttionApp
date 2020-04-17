package com.huateng.collection.bean;

import com.google.gson.annotations.Expose;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class AddAddress {

    private String addrId;//地址ID
    private String addrType;//地址类型
    private String address1;//地址1
    private String address2;//地址2
    private String address3;//地址3
    private String city;//城市
    private String latitude;//经度
    private String longitude;//纬度
    private String postcode;//请求码
    private String name;//名称
    private String relWithCust;//
    @Expose(serialize = false, deserialize = false)
    private String caseId;
    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
    public String getAddrId() {
        return addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelWithCust() {
        return relWithCust;
    }

    public void setRelWithCust(String relWithCust) {
        this.relWithCust = relWithCust;
    }
}
