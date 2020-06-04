package com.huateng.collection.ui.caseInfo.contract;

import com.huateng.collection.base.IBaseView;

/**
 * author: yichuan
 * Created on: 2020-05-12 09:26
 * description:
 */
public interface CaseDetailContract {

    interface View extends IBaseView{

        void finishPage();

        void toCaseAction(String type);
    }

    interface Presenter {
        void stopDealWithCase(String caseId,String custNo);


        void getCaseStatus(String caseId,String type);
    }

}
