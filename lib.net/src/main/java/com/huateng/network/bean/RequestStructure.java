package com.huateng.network.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shanyong on 2016/10/10.
 * 请求外层结构
 */

public class RequestStructure implements Serializable {

    /**
     * queryTable : {"scubeBody":{"contextData":{"data":{"password":"C1181AACF646B97F0A0A782DB351A405","userId":"fffff"},"domainVilidate":""}},"scubeHeader":{"errorCode":"","errorMsg":"","transCode":"appInteface/login","xsessionToken":"collections"}}
     */

    private QueryTableBean queryTable;

    public QueryTableBean getQueryTable() {
        return queryTable;
    }

    public void setQueryTable(QueryTableBean queryTable) {
        this.queryTable = queryTable;
    }

    public static class QueryTableBean {
        /**
         * scubeBody : {"contextData":{"data":{"password":"C1181AACF646B97F0A0A782DB351A405","userId":"fffff"},"domainVilidate":""}}
         * scubeHeader : {"errorCode":"","errorMsg":"","transCode":"appInteface/login","xsessionToken":"collections"}
         */

        private ScubeBodyBean scubeBody;
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
             * contextData : {"data":{"password":"C1181AACF646B97F0A0A782DB351A405","userId":"fffff"},"domainVilidate":""}
             */

            private ContextDataBean contextData;

            public ContextDataBean getContextData() {
                return contextData;
            }

            public void setContextData(ContextDataBean contextData) {
                this.contextData = contextData;
            }

            public static class ContextDataBean {
                /**
                 * data : {"password":"C1181AACF646B97F0A0A782DB351A405","userId":"fffff"}
                 * domainVilidate :
                 */

                private Object data;
                private String domainVilidate;

                public Object getData() {
                    return data;
                }

                public void setData(Object data) {
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
            /**
             * errorCode :
             * errorMsg :
             * transCode : appInteface/login
             * xsessionToken : collections
             */

            private String errorCode;
            private String errorMsg;
            private String transCode;
            @JSONField(name = "x-session-token")
            @SerializedName("x-session-token")
            private String xsessionToken;

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

            public String getXsessionToken() {
                return xsessionToken;
            }

            public void setXsessionToken(String xsessionToken) {
                this.xsessionToken = xsessionToken;
            }
        }
    }
}
