package com.huateng.collection.ui.caseInfo.presenter;

import android.text.TextUtils;

import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.LogActActionBean;
import com.huateng.collection.ui.caseInfo.contract.HistoryActionContract;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020-05-12 11:17
 * description:
 */
public class HistoryActionsPresenter extends BasePresenter<HistoryActionContract.View> implements HistoryActionContract.Presenter {
    private int pageSize = 10;
    private int pageNum = 1;

    public HistoryActionsPresenter(HistoryActionContract.View view) {
        super(view);
    }

    /**
     * 加载数据
     *
     * @param status 类型 刷新 还是加载更多
     */
    @Override
    public void loadData(int status, String caseId, String custId) {

        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("tlrNo", Perference.getUserId());
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_LOG_ACT_ACTION, map)
                .compose(mView.getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<LogActActionBean>() {


                    @Override
                    public void onError(String code, String msg) {
                        if (mView == null) {
                            return;
                        }

                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                        mView.showEmptyView();
                        mView.hideLoading();

                        mView.showToast(msg);
                    }

                    @Override
                    public void onNextData(LogActActionBean logActActionBean) {

                        if (mView == null) {
                            return;
                        }
                        if (logActActionBean == null || logActActionBean.getRecords().size() == 0) {
                            if (status == Constants.REFRESH) {
                                mView.showEmptyView();
                            }
                            return;
                        }
                        pageNum = logActActionBean.getNextPage();
                        mView.hideLoading();
                        mView.setLogActionData(status, logActActionBean.getRecords());
                    }


                });

    }
}
