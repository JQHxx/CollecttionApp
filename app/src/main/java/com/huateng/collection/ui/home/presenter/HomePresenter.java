package com.huateng.collection.ui.home.presenter;
import android.util.Log;

import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.VisitCode;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.home.contract.HomeContract;
import com.huateng.collection.utils.StringUtils;
import com.huateng.network.ApiConstants;
import com.orm.SugarRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: yichuan
 * Created on: 2020-03-26 16:19
 * description:
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private int pageSize = 10;
    private int pageNum = 1;

    public HomePresenter(HomeContract.View view) {
        super(view);
    }


    @Override
    public void loadData(int status) {
        Log.i("nb", "-------loadTodoCases-------");

        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        if (status == Constants.LOAD_MORE) {
            pageNum++;
        }
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageCount", String.valueOf(pageSize));

        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseSummary>>() {

            @Override
            public void response(List<RespCaseSummary> respCaseSummaries) {
                if (mView == null) {
                    return;
                }
                mView.setCaseSummaries(status, respCaseSummaries);
            }

            @Override
            public void end() {
                super.end();

            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map);

    }

    /**
     * 加载
     */
    @Override
    public void loadVisitSunCode() {
        Map<String, String> map2 = new HashMap<>();
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<VisitCode>>() {
            @Override
            public void response(List<VisitCode> t) {
                SugarRecord.deleteAll(VisitCode.class);
                SugarRecord.saveInTx(t);
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_VISIT_SUM_CODE, map2);

    }

    /**
     * 加载搜索案件
     *
     * @param custName
     */
    @Override
    public void loadSearchCase(String custName) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        map.put("pageNum", "1");
        map.put("pageCount", "-1");
        if (StringUtils.isNotEmpty(custName)) {
            map.put("custName", custName);
        }

        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseSummary>>() {

            @Override
            public void beforeRequest() {
                super.beforeRequest();
                //   showLoading();
                mView.showLoading();
            }

            @Override
            public void requestError(String code, String msg) {
                super.requestError(code, msg);
            }

            @Override
            public void response(List<RespCaseSummary> respCaseSummaries) {
                mView.setSearchCase(respCaseSummaries);

            }

            @Override
            public void end() {
                super.end();
                mView.hideLoading();
                // hideLoading();
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map);

    }
}
