package com.huateng.phone.collection.ui.home.contract;

import com.huateng.phone.collection.base.IBaseView;
import com.huateng.phone.collection.bean.CaseBeanData;
import com.huateng.phone.collection.bean.CaseIdBean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-03-26 16:19
 * description:
 */
public interface HomeContract {

    interface View extends IBaseView {
        /**
         * 代办任务数据页面展示
         * @param status
         */
        void setCaseSummaries(int status, List<CaseBeanData.RecordsBean> respCaseSummaries);


        void showEmpty();
        /**
         * 展示搜索数据
         * @param respCaseSummaries
         */
        void setSearchCase(List<CaseBeanData.RecordsBean> respCaseSummaries);

        void onError(String msg);

        void addToWaitCaseSucess();
    }

    interface Presenter {

        /**
         * 代办任务请求数据
         * @param status
         */
        void loadData(int status);



        /**
         * 加载搜索案件
         * @param custName
         */
       void loadSearchCase(String custName);


       void addToWaitCast(List<CaseIdBean> caseIds);

    }


}
