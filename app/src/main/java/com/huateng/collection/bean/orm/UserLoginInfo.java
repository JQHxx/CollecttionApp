package com.huateng.collection.bean.orm;

import com.orm.dsl.Table;
import com.orm.dsl.Unique;

/**
 * Created by shanyong on 2017/6/15.
 * 用户登录信息表
 */

@Table
public class UserLoginInfo {
    @Unique
    private String userId;//用户地
    @Unique
    private String loginName;//登录名
    private String authorization;//
    private String pwd;//密码
    private String nickName;//昵称
    private long loginTime;//登录时间
    private long firstLoginTime;//首次登录时间
    private int loginErrorCount;//错误次数
    private boolean loginSuccess;//是否登录成功
    private String ordId;//部门所属机构ID
    private String rootOrgId;//一级机构ID

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public long getFirstLoginTime() {
        return firstLoginTime;
    }

    public void setFirstLoginTime(long firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public int getLoginErrorCount() {
        return loginErrorCount;
    }

    public void setLoginErrorCount(int loginErrorCount) {
        this.loginErrorCount = loginErrorCount;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(String rootOrgId) {
        this.rootOrgId = rootOrgId;
    }

    @Override
    public String toString() {
        return "UserLoginInfo{" +
                "userId='" + userId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", authorization='" + authorization + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nickName='" + nickName + '\'' +
                ", loginTime=" + loginTime +
                ", firstLoginTime=" + firstLoginTime +
                ", loginErrorCount=" + loginErrorCount +
                ", loginSuccess=" + loginSuccess +
                ", ordId='" + ordId + '\'' +
                ", rootOrgId='" + rootOrgId + '\'' +
                '}';
    }
}
