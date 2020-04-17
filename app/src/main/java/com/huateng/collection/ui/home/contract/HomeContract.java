package com.huateng.collection.ui.home.contract;

import com.huateng.collection.base.IBaseView;
import com.huateng.collection.bean.api.RespCaseSummary;

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
        void setCaseSummaries(int status, List<RespCaseSummary> respCaseSummaries);

        /**
         * 展示搜索数据
         * @param respCaseSummaries
         */
        void setSearchCase(List<RespCaseSummary> respCaseSummaries);
    }

    interface Presenter {

        /**
         * 代办任务请求数据
         * @param status
         */
        void loadData(int status);

        /**
         * 加载行动码
         */
        void loadVisitSunCode();

        /**
         * 加载搜索案件
         * @param custName
         */
       void loadSearchCase(String custName);
    }


}
