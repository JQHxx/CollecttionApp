package com.huateng.phone.collection.ui.report.presenter;

import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.ui.report.contract.ReportContract;

/**
 * author: yichuan
 * Created on: 2020-04-01 18:14
 * description:
 */
public class ReportPresenter extends BasePresenter<ReportContract.View> implements ReportContract.Presenter {

    public ReportPresenter(ReportContract.View view) {
        super(view);
    }
}