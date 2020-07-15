package com.huateng.phone.collection.ui.wait.presenter;

import android.text.TextUtils;

import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.CaseBeanData;
import com.huateng.phone.collection.bean.CaseIdBean;
import com.huateng.phone.collection.ui.wait.contract.WaitCaseContract;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020/7/8 17:19
 * description:
 */
public class WaitCasePresenter extends BasePresenter<WaitCaseContract.View> implements WaitCaseContract.Presenter  {
    private int pageSize =10;
    private int pageNum = 0;
    public WaitCasePresenter(WaitCaseContract.View view) {
        super(view);
    }


    /**
     * 代办任务请求数据
     *
     * @param status
     */
    @Override
    public void loadData(int status) {
        Map<String, Object> map = new HashMap<>();

        if (status == Constants.REFRESH) {
            pageNum = 0;
        }
        map.put("tlrNo", Perference.getUserId());//
        map.put("orgId", Perference.get(Perference.ORG_ID));
        map.put("operFlag","0");
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map)
                .compose(mView.getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CaseBeanData>() {
                    @Override
                    public void onError(String code, String msg) {
                        if (mView == null) {
                            return;
                        }
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }

                        mView.showEmptyView();
                        mView.showToast(msg);

                    }

                    @Override
                    public void onNextData(CaseBeanData caseBeanData) {
                        if (mView == null) {
                            return;
                        }
                        if (caseBeanData == null ||caseBeanData.getRecords().size() ==0) {
                            if (status == Constants.REFRESH) {
                                mView.showEmptyView();
                            }
                            return;
                        }

                        pageNum = caseBeanData.getNextPage();
                        mView.setCaseSummaries(status, caseBeanData.getRecords());
                    }
                });
    }


    @Override
    public void removeWaitCast(List<CaseIdBean> caseIds,int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("pendingList",caseIds);
        map.put("operFlag","0");

        request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.INSERT_OR_DEL_CASE_PENDING, map)
                .subscribe(new BaseObserver2<String>() {


                    @Override
                    public void onError(String code, String msg) {
                        if (mView == null) {
                            return;
                        }

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }

                        mView.showToast(msg);
                    }

                    @Override
                    public void onNextData(String caseBeanData) {
                        //  Log.e("nb",caseBeanData);
                        if (mView == null) {
                            return;
                        }
                        mView.removeWaitCaseSucess(position);
                    }
                });


    }
}
