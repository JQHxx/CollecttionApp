package com.huateng.collection.ui.remission.presenter;

import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.BizAcctInfoBean;
import com.huateng.collection.bean.CustInfoBean;
import com.huateng.collection.ui.remission.contract.RemissionContract;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020-05-13 20:09
 * description:
 */
public class RemissionPresenter extends BasePresenter<RemissionContract .View> implements RemissionContract.Presenter{

    public RemissionPresenter(RemissionContract.View view) {
        super(view);
    }

    /**
     * 查询减免申请信用卡贷款信息账号
     * @param custId
     */
    @Override
    public void loadData(String custId,String caseId) {
        Map<String,String> map = new HashMap<>();
        map.put("custNo",custId);
        map.put("caseId",caseId);
        request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_APPLICATION_RELIEF, map)
                .subscribe(new BaseObserver2<BizAcctInfoBean>() {
                    @Override
                    public void onError(String code, String msg) {

                    }

                    @Override
                    public void onNextData(BizAcctInfoBean bizAcctInfoBean) {
                        if(mView == null) {
                            return;
                        }

                        if(bizAcctInfoBean == null) {
                            return;
                        }
                        mView.setBizAcctInfo(bizAcctInfoBean);
                    }
                });
    }

    @Override
    public void loadCustInfo(String custId) {
        Map<String, String> map = new HashMap<>();

        map.put("custNo", custId);
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECTED_CUSTOM_INFO, map)
                .compose(mView.getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CustInfoBean>() {


                    @Override
                    public void onError(String code, String msg) {

                    }

                    @Override
                    public void onNextData(CustInfoBean custInfoBean) {
                        if (custInfoBean == null) {
                            return;
                        }
                        mView.setCustData(custInfoBean);
                    }


                });
    }

    /**
     * 减免申请
     * @param custId
     */
    @Override
    public void reliefBatchExcute(String custId) {
    }
}
