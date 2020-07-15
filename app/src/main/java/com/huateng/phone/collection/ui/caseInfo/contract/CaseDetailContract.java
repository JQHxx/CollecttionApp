package com.huateng.phone.collection.ui.caseInfo.contract;

import com.huateng.phone.collection.base.IBaseView;

/**
 * author: yichuan
 * Created on: 2020-05-12 09:26
 * description:
 */
public interface CaseDetailContract {

    interface View extends IBaseView{

        void finishPage();

        void toCaseAction(boolean isProcess);
    }

    interface Presenter {
        void stopDealWithCase(String caseId,String custNo);

        void getCaseStatus(String caseId);
    }

}
