package com.huateng.collection.ui.home.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.CaseBeanData;
import com.huateng.collection.ui.home.contract.HomeContract;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020-03-26 16:19
 * description:
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private int pageSize = 10;
    private int pageNum = 0;

    public HomePresenter(HomeContract.View view) {
        super(view);
    }

    @Override
    public void loadData(int status) {
        Log.i("nb", "-------loadTodoCases-------");
        Map<String, String> map = new HashMap<>();

        if (status == Constants.REFRESH) {
            pageNum = 0;
        }
        map.put("tlrNo", Perference.getUserId());//
        map.put("orgId", Perference.get(Perference.ORG_ID));
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
                        if (caseBeanData == null ) {
                            if (status == Constants.REFRESH) {
                                mView.showEmptyView();
                            }
                            return;
                        }

                        pageNum = caseBeanData.getNextPage();
                        Log.e("nb","案件信息加载完成");

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

        Map<String, String> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("orgId", Perference.get(Perference.ORG_ID));
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
}
