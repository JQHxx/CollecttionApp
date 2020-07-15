package com.huateng.phone.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
@Table
public class RespAddress extends RespBase implements Serializable {

    @Unique
    private Long id;
    private String addrId;
    private String addrType;
    private String address;
    private String city;
    private String latitude;
    private String longitude;
    private String postcode;
    private String relWithCust;
    @Expose(serialize = false, deserialize = false)
    private String caseId;
    @Expose(serialize = false, deserialize = false)
    private String bizId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRelWithCust() {
        return relWithCust;
    }

    public void setRelWithCust(String relWithCust) {
        this.relWithCust = relWithCust;
    }
}
