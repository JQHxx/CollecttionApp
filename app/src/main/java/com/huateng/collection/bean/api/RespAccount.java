package com.huateng.collection.bean.api;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * @author dengzh
 * @description
 * @time 2016-11-24.
 */
@Table
public class RespAccount implements Serializable {
    private String acctNo;
    private String cardNo;
    private String currency;
    private String lastCycStmtBal;
    @Expose(serialize = false, deserialize = false)
    private String caseId;
    @Expose(serialize = false, deserialize = false)
    private String bizId;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastCycStmtBal() {
        return lastCycStmtBal;
    }

    public void setLastCycStmtBal(String lastCycStmtBal) {
        this.lastCycStmtBal = lastCycStmtBal;
    }
}
