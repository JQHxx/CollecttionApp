package com.huateng.collection.ui.caseInfo.presenter;

import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.CaseStatusBean;
import com.huateng.collection.ui.caseInfo.contract.CaseDetailContract;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * author: yichuan
 * Created on: 2020-05-12 09:19
 * description:
 */
public class CaseDetailPresenter extends BasePresenter<CaseDetailContract.View> implements CaseDetailContract.Presenter {


    public CaseDetailPresenter(CaseDetailContract.View view) {
        super(view);
    }

    @Override
    public void stopDealWithCase(String caseId, String custNo) {
        mView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", custNo);//
        map.put("tlrNo", Perference.getUserId());

        request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.STOP_DEAL_WITH_CASE, map)
                .subscribe(new BaseObserver2<String>() {

                    @Override
                    public void onError(String code, String msg) {
                      //  Log.e("nb", code + ":" + msg);
                        RxToast.showToast(msg);
                        mView.hideLoading();

                    }

                    @Override
                    public void onNextData(String s) {
                        // Log.e("nb",s);
                        if(mView == null) {
                            return;
                        }
                        EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_CASE_SUMMARY_REMINDER));

                        mView.finishPage();
                        mView.hideLoading();
                    }
                });
    }


    @Override
    public void getCaseStatus(String caseId) {
        Map<String, String> map = new HashMap<>();
        map.put("caseIds", caseId);

        request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_OPER_APPR_STOP, map)
                .subscribe(new BaseObserver2<CaseStatusBean>() {

                    @Override
                    public void onError(String code, String msg) {
                        if(mView == null) {
                            return;
                        }
                        mView.hideLoading();
                        mView.toCaseAction(false);
                    }

                    @Override
                    public void onNextData(CaseStatusBean caseStatusBean) {
                        if(mView == null) {
                            return;
                        }
                        mView.hideLoading();
                        if(caseStatusBean == null) {
                            mView.toCaseAction(false);
                            return;
                        }

                        if ("N".equals(caseStatusBean.getResultCode())) {
                           // RxToast.showToast("");
                            mView.toCaseAction(false);

                        } else if ("Y".equals(caseStatusBean.getResultCode())) {
                            mView.toCaseAction(true);
                        }
                    }
                });
    }
}
