package com.huateng.phone.collection.bean.orm;

import com.orm.dsl.Table;

/**
 * Created by shanyong on 2017/1/22.
 */
@Table
public class Outbound {
    //纬度
    private double latitude;
    //经度
    private double longitude;
    //用户ID
    private String userId;
    //地址
    private String address;
    //时间
    private long time;
    //日期
    private String date;
    //事件
    private String event;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }


}
