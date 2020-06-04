package com.huateng.collection.bean.api;

/**
 * Created by shanyong on 2017/1/13.
 * 用户登录信息
 */

public class RespLoginInfo extends RespBase {


    /**
     * ssouser : {"token":"4SYfVVNRqFXztEXgAAcF","userId":"201703280929490010000000000354","userName":"M1LCW1"}
     * userId : M1LCW1
     */

    private SsouserBean ssouser;
    private String userId;
    private boolean outsourceFlag;//true 委外 false 行员

    public boolean isOutsourceFlag() {
        return outsourceFlag;
    }

    public void setOutsourceFlag(boolean outsourceFlag) {
        this.outsourceFlag = outsourceFlag;
    }

    public SsouserBean getSsouser() {
        return ssouser;
    }

    public void setSsouser(SsouserBean ssouser) {
        this.ssouser = ssouser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class SsouserBean {
        /**
         * token : 4SYfVVNRqFXztEXgAAcF
         * userId : 201703280929490010000000000354
         * userName : M1LCW1
         */

        private String token;
        private String userId;
        private String userName;
        private String orgId;
        private String rootOrgId;


        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

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

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getRootOrgId() {
            return rootOrgId;
        }

        public void setRootOrgId(String rootOrgId) {
            this.rootOrgId = rootOrgId;
        }

        @Override
        public String toString() {
            return "SsouserBean{" +
                    "token='" + token + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", orgId='" + orgId + '\'' +
                    ", rootOrgId='" + rootOrgId + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "RespLoginInfo{" +
                "ssouser=" + ssouser +
                ", userId='" + userId + '\'' +
                '}';
    }
}
