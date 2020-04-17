package com.tools.bean;

/**
 * author: yichuan
 * Created on: 2020-04-15 16:26
 * description:
 */
public class EventBean {
    public int code;
    public Object mObject;


    public EventBean(int code){
        this.code = code;
    }

    public EventBean(int code,Object object){
        this.code = code;
        this.mObject = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }
}
