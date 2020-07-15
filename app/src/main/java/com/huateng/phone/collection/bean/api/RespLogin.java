package com.huateng.phone.collection.bean.api;

/**
 * @author dengzh
 * @description
 * @time 2016-12-21.
 */
public class RespLogin extends RespBase{
    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
