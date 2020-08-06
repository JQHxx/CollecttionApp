package com.huateng.phone.collection.ui.home.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.CaseBeanData;
import com.huateng.phone.collection.bean.CaseIdBean;
import com.huateng.phone.collection.ui.home.contract.HomeContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: yichuan
 * Created on: 2020-03-26 16:19
 * description:
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private int pageSize =10;
    private int pageNum = 0;

    public HomePresenter(HomeContract.View view) {
        super(view);
    }

    @Override
    public void loadData(int status) {
        Map<String, Object> map = new HashMap<>();

        if (status == Constants.REFRESH) {
            pageNum = 0;
        }
        map.put("tlrNo", Perference.getUserId());//
        map.put("orgId", Perference.get(Perference.ORG_ID));
        map.put("operFlag","1");
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map)
                .compose(mView.getRxlifecycle())
                .compose(RxSchedulers.io_main())
               .subscribe(new BaseObserver2<CaseBeanData>() {
                    @Override
                    public void onError(String code, String msg) {
                        if (mView == null) {
                            return;
                        }
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }

                        mView.showEmpty();
                        mView.showToast(msg);

                    }

                    @Override
                    public void onNextData(CaseBeanData caseBeanData) {
                        if (mView == null) {
                            return;
                        }
                        if (caseBeanData == null || caseBeanData.getRecords().size() ==0) {
                            if (status == Constants.REFRESH) {
                                mView.showEmpty();
                            }else {
                                Log.e("nb","setLoadMoreEnd");
                                mView.setLoadMoreEnd();
                            }
                            return;
                        }

                        pageNum = caseBeanData.getNextPage();
                        mView.setCaseSummaries(status, caseBeanData.getRecords());
                    }
                });
    }


    /**
     * 加载搜索案件
     *
     * @param custName
     */
    @Override
    public void loadSearchCase(String custName) {

        Map<String, Object> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("orgId", Perference.get(Perference.ORG_ID));
        map.put("operFlag","1");
        map.put("pageNo", "0");
        map.put("pageSize", "20");
        map.put("condition", custName);

        request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map)
                .subscribe(new BaseObserver2<CaseBeanData>() {


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
                    public void onNextData(CaseBeanData caseBeanData) {
                        if (mView == null) {
                            return;
                        }
                        pageNum = caseBeanData.getNextPage();
                        mView.setSearchCase(caseBeanData.getRecords());
                    }
                });


    }

    @Override
    public void addToWaitCast(List<CaseIdBean> caseIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("pendingList",caseIds);
        map.put("operFlag","1");

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
                        mView.addToWaitCaseSucess();
                    }
                });


    }
}
