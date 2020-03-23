package com.huateng.network.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shanyong on 2016/10/9.
 * 响应的外层结构
 */

public class ResponseStructure implements Serializable {


    /**
     * contextData : {"data":"{\"body\":[{\"productId\":\"TEST001\",\"productName\":\"TEST001\",\"productType\":\"01\"},{\"productId\":\"product_8888\",\"productName\":\"消费信贷111\",\"productType\":\"01\"},{\"productId\":\"Test008\",\"productName\":\"Test008\",\"productType\":\"01\"},{\"productId\":\"TEST110\",\"productName\":\"TEST110\",\"productType\":\"01\"},{\"productId\":\"TEST112\",\"productName\":\"TEST112\",\"productType\":\"01\"},{\"productId\":\"TEST003\",\"productName\":\"TEST003\",\"productType\":\"01\"}],\"code\":\"000000\",\"msg\":\"SUCCESS\",\"pageNo\":1,\"pageSize\":6,\"totle\":6}","domainVilidate":"domainVilidate"}
     */

    private ScubeBodyBean scubeBody;
    /**
     * errorCode : SUC
     * errorMsg :
     * transCode :
     * xSessionToken : x-session-token
     */

    private ScubeHeaderBean scubeHeader;

    public ScubeBodyBean getScubeBody() {
        return scubeBody;
    }

    public void setScubeBody(ScubeBodyBean scubeBody) {
        this.scubeBody = scubeBody;
    }

    public ScubeHeaderBean getScubeHeader() {
        return scubeHeader;
    }

    public void setScubeHeader(ScubeHeaderBean scubeHeader) {
        this.scubeHeader = scubeHeader;
    }

    public static class ScubeBodyBean {
        /**
         * data : {"body":[{"productId":"TEST001","productName":"TEST001","productType":"01"},{"productId":"product_8888","productName":"消费信贷111","productType":"01"},{"productId":"Test008","productName":"Test008","productType":"01"},{"productId":"TEST110","productName":"TEST110","productType":"01"},{"productId":"TEST112","productName":"TEST112","productType":"01"},{"productId":"TEST003","productName":"TEST003","productType":"01"}],"code":"000000","msg":"SUCCESS","pageNo":1,"pageSize":6,"totle":6}
         * domainVilidate : domainVilidate
         */

        private ContextDataBean contextData;

        public ContextDataBean getContextData() {
            return contextData;
        }

        public void setContextData(ContextDataBean contextData) {
            this.contextData = contextData;
        }

        public static class ContextDataBean {
            private String data;
            private String domainVilidate;

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDomainVilidate() {
                return domainVilidate;
            }

            public void setDomainVilidate(String domainVilidate) {
                this.domainVilidate = domainVilidate;
            }
        }
    }

    public static class ScubeHeaderBean {
        private String errorCode;
        private String errorMsg;
        private String transCode;
        @JSONField(name = "x-session-token")
        @SerializedName("x-session-token")
        private String xSessionToken;

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getTransCode() {
            return transCode;
        }

        public void setTransCode(String transCode) {
            this.transCode = transCode;
        }

        public String getXSessionToken() {
            return xSessionToken;
        }

        public void setXSessionToken(String xSessionToken) {
            this.xSessionToken = xSessionToken;
        }
    }
}
