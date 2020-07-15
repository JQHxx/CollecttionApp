package com.huateng.phone.collection.ui.wait.contract;

import com.huateng.phone.collection.base.IBaseView;
import com.huateng.phone.collection.bean.CaseBeanData;
import com.huateng.phone.collection.bean.CaseIdBean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020/7/8 17:20
 * description:
 */
public interface WaitCaseContract {

    interface View extends IBaseView {
        void onError(String msg);

        void setCaseSummaries(int status, List<CaseBeanData.RecordsBean> respCaseSummaries);

        void showEmptyView();

        void removeWaitCaseSucess(int position);
    }

    interface Presenter {

        /**
         * 代办任务请求数据
         *
         * @param status
         */
        void loadData(int status);


        void removeWaitCast(List<CaseIdBean> caseIds,int position);
    }

}
