package com.huateng.collection.ui.report.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.ReportListBean;
import com.huateng.collection.ui.adapter.ReportListadapter;
import com.huateng.collection.widget.Watermark;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReportListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private String custId;
    private String caseId;
    private String custName;
    private int pageSize = 20;
    private int pageNum = 0;
    private ReportListadapter mAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + Perference.get(Perference.NICK_NAME))
                .show(this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReportListadapter();
        mRecyclerview.setAdapter(mAdapter);

        initListener();

    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ReportListBean.RecordsBean data = (ReportListBean.RecordsBean) adapter.getData().get(position);
                Intent intent = new Intent(ReportListActivity.this, ReportActivity.class);
                intent.putExtra(Constants.CASE_ID, caseId);
                intent.putExtra(Constants.CUST_ID, custId);
                intent.putExtra(Constants.CUST_NAME, custName);
                intent.putExtra("reportData", data);
                intent.putExtra("isAdd", false);
                startActivity(intent);
            }
        });


        mRxTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportListActivity.this, ReportActivity.class);
                intent.putExtra(Constants.CASE_ID, caseId);
                intent.putExtra(Constants.CUST_ID, custId);
                intent.putExtra(Constants.CUST_NAME, custName);
                intent.putExtra("isAdd", true);
                startActivity(intent);
            }
        });

        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreData(true);
            }
        });

        mAdapter.setOnLoadMoreListener(this, mRecyclerview);
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_report_list;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        custName = getIntent().getStringExtra(Constants.CUST_NAME);

        loadMoreData(true);

    }


    private void loadMoreData(boolean isFresh) {
        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("tlrNo", Perference.getUserId());
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELEDT_CUST_REPORT_INFO, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅之前回调回去显示加载动画
                        if (isFresh) {
                          //  showLoading();
                            mSwipeRefresh.setRefreshing(true);
                        }

                    }
                })// 订阅之前操作在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<ReportListBean>() {


                    @Override
                    public void onError(String code, String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                        mSwipeRefresh.setRefreshing(false);
                        if (TextUtils.isEmpty(msg)) {
                            return;

                        }

                        RxToast.showToast(msg);

                    }

                    @Override
                    public void onNextData(ReportListBean bean) {

                        if (isFresh) {
                            //下拉刷新
                            if (bean == null || bean.getRecords().size() == 0) {
                                mAdapter.setEmptyView(R.layout.layout_empty_view, mRecyclerview);
                                mSwipeRefresh.setRefreshing(false);
                                return;
                            }
                            mAdapter.setNewData(bean.getRecords());
                            if (bean.getRecords().size() < 10) {
                                mAdapter.setEnableLoadMore(false);
                            } else {
                                mAdapter.setEnableLoadMore(true);
                            }
                          //  hideLoading();
                        } else {
                            //加载更多
                            mAdapter.addData(bean.getRecords());
                            if (bean.getRecords().size() >= 10) {

                                mAdapter.loadMoreComplete();
                            } else {
                                mAdapter.loadMoreEnd();
                            }

                        }

                        //停止刷新 并恢复下拉刷新功能
                        if (mSwipeRefresh != null) {
                            mSwipeRefresh.setRefreshing(false);
                            mSwipeRefresh.setEnabled(true);
                        }

                    }
                });

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

    }


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.REPORT_REFRESH) {
            loadMoreData(true);
        }


    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    public void onLoadMoreRequested() {
        loadMoreData(false);
    }
}
