package com.huateng.phone.collection.bean;

import com.huateng.phone.collection.bean.orm.DictItemBean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-13 18:32
 * description:
 */
public class DictDataBean {


    private List<DictItemBean> educationList;
    private List<DictItemBean> loanstatusList;
    private List<DictItemBean> nationalityList;
    private List<DictItemBean> worknatureList;
    private List<DictItemBean> zdtaayList;
    private List<DictItemBean> productcodeList;


    public List<DictItemBean> getProductcodeList() {
        return productcodeList;
    }

    public void setProductcodeList(List<DictItemBean> productcodeList) {
        this.productcodeList = productcodeList;
    }

    public List<DictItemBean> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<DictItemBean> educationList) {
        this.educationList = educationList;
    }

    public List<DictItemBean> getLoanstatusList() {
        return loanstatusList;
    }

    public void setLoanstatusList(List<DictItemBean> loanstatusList) {
        this.loanstatusList = loanstatusList;
    }

    public List<DictItemBean> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<DictItemBean> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<DictItemBean> getWorknatureList() {
        return worknatureList;
    }

    public void setWorknatureList(List<DictItemBean> worknatureList) {
        this.worknatureList = worknatureList;
    }

    public List<DictItemBean> getZdtaayList() {
        return zdtaayList;
    }

    public void setZdtaayList(List<DictItemBean> zdtaayList) {
        this.zdtaayList = zdtaayList;
    }


}
