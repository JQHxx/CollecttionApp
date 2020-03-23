package com.huateng.network.api;


/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
public class RespBase {

    /**
     * data : {"userInfo":{"brId":"201612161705490005000000000424","brManagerId":"201612161705490005000000000424","id":"eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdHktYXBwIjoie1wiYnJJZFwiOlwiMjAxNjEyMTYxNzA1NDkwMDA1MDAwMDAwMDAwNDI0XCIsXCJick1hbmFnZXJJZFwiOlwiMjAxNjEyMTYxNzA1NDkwMDA1MDAwMDAwMDAwNDI0XCIsXCJpZFwiOlwiMjAxNjEyMTYxNzA4NTEwMDEwMDAwMDAwMDAwMjc4XCIsXCJsb2dpbkluZm9LZXlcIjpcIkFwcDA2NGZiMDEzMDUyODRiNjU4NTA5ZjE3Mjg1YzQzYThlXCIsXCJzdGF0dXNcIjpcIjFcIixcInRsck5hbWVcIjpcIueUteWCrOWCrOaUtuWRmDFcIixcInRsck5vXCI6XCJBNTAxXCJ9IiwiZXhwIjoyNTMzNzA3MzYwMDB9.Jt0ta-dmmc5M4CL4vyNoiKFLQPO31e5LfIdvbjC58mI","loginInfoKey":"App064fb01305284b658509f17285c43a8e","status":"1","tlrName":"电催催收员1","tlrNo":"A501"}}
     * resultCode : SUC
     * resultDesc : 登陆成功
     * userId : 52236644
     */

    private String resultCode;
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
