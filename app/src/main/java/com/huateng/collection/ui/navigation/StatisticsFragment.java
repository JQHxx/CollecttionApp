package com.huateng.collection.ui.navigation;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.CaseSunTotalBean;
import com.huateng.collection.utils.DateUtil;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020/6/3 17:17
 * description:
 */
public class StatisticsFragment extends BaseFragment {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_case_sum_total)
    TextView mTvCaseSumTotal;
    @BindView(R.id.tv_case_count)
    TextView mTvCaseCount;
    @BindView(R.id.tv_record_time)
    TextView mTvRecordTime;
    @BindView(R.id.tv_report_integrity)
    TextView mTvReportIntegrity;
    @BindView(R.id.tv_crt_time)
    TextView mTvCrtTime;
    @BindView(R.id.tv_out_bound_name)
    TextView mTvOutBoundName;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_statistics;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        immersiveStatusBar(mRxTitle);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setEnabled(false);
                initData();
            }
        });
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

        Map<String, String> map = new HashMap<>();

        map.put("tlrNo", Perference.getUserId());

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_OUT_BOUND_RECORD, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅之前回调回去显示加载动画
                        showLoading();
                    }
                })// 订阅之前操作在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CaseSunTotalBean>() {
                    @Override
                    public void onError(String code, String msg) {
                        hideLoading();
                        RxToast.showToast(msg);
                        Log.e("nb", code + ":" + msg);
                        mSwipeRefresh.setEnabled(true);
                    }

                    @Override
                    public void onNextData(CaseSunTotalBean caseSunTotalBean) {

                        mSwipeRefresh.setEnabled(true);
                        mSwipeRefresh.setRefreshing(false);
                        if (caseSunTotalBean == null) {
                            hideLoading();
                            return;
                        }

                        mTvCaseSumTotal.setText(String.valueOf(caseSunTotalBean.getCaseSumTotal()));
                        mTvCaseCount.setText(String.valueOf(caseSunTotalBean.getCaseCount()));
                        mTvRecordTime.setText(DateUtil.getTwoDecimalsValue(caseSunTotalBean.getRecordTime()/60)+"分"+DateUtil.getTwoDecimalsValue(caseSunTotalBean.getRecordTime()%60)+"秒");

                        mTvReportIntegrity.setText(caseSunTotalBean.getReportIntegrity());
                        if (caseSunTotalBean.getCrtTime() != 0) {
                            mTvCrtTime.setText(DateUtil.getDate(caseSunTotalBean.getCrtTime()));
                        }

                        mTvOutBoundName.setText(caseSunTotalBean.getOutBoundName());
                        hideLoading();

                    }
                });


    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
