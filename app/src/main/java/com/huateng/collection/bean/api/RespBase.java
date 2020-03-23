package com.huateng.collection.bean.api;

import com.orm.dsl.Ignore;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class RespBase {
    @Ignore
    private String resultCode;
    @Ignore
    private String resultDesc;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
