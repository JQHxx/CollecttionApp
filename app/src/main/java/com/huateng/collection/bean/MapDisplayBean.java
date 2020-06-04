package com.huateng.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-21 15:23
 * description:
 */
public class MapDisplayBean {


    /**
     * custName : 王晨岭
     * custNo : 330225197305060036_Ind01
     * dataList : [{"address":"象山县丹东街道汇振路18幢28梯202室","city":"宁波市","district":"象山县","geohash":"wtnxtsdh10kd","lat":"29.468767","lon":"121.88512","provider":"gaode","province":"浙江省","resultMsg":"ok","status":"0"},{"address":"0","city":"","district":"","geohash":"s00000000000","lat":"0.0","lon":"0.0","netStatus":"0","provider":"gaode","province":"","resultMsg":"fail","status":"1","validStatus":"1"}]
     */

    private String custName;
    private String custNo;
    private List<DataListBean> dataList;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * address : 象山县丹东街道汇振路18幢28梯202室
         * city : 宁波市
         * district : 象山县
         * geohash : wtnxtsdh10kd
         * lat : 29.468767
         * lon : 121.88512
         * provider : gaode
         * province : 浙江省
         * resultMsg : ok
         * status : 0
         * netStatus : 0
         * validStatus : 1
         */
        private String custName;
        private String custNo;
        private String address;
        private String city;
        private String district;
        private String geohash;
        private String lat;
        private String lon;
        private String provider;
        private String province;
        private String resultMsg;
        private String status;
        private String netStatus;
        private String validStatus;
        private String addressType;//地址类型 0家庭 1单位 2户籍
        private String caseStatus;//0 未处理 1已处理


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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getGeohash() {
            return geohash;
        }

        public void setGeohash(String geohash) {
            this.geohash = geohash;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNetStatus() {
            return netStatus;
        }

        public void setNetStatus(String netStatus) {
            this.netStatus = netStatus;
        }

        public String getValidStatus() {
            return validStatus;
        }

        public void setValidStatus(String validStatus) {
            this.validStatus = validStatus;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getAddressType() {
            return addressType;
        }

        public void setAddressType(String addressType) {
            this.addressType = addressType;
        }

        public String getCaseStatus() {
            return caseStatus;
        }

        public void setCaseStatus(String caseStatus) {
            this.caseStatus = caseStatus;
        }
    }
}
