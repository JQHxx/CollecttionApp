package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.bean.LogActActionBean;
import com.huateng.collection.ui.adapter.HistoryActionsAdapter;
import com.huateng.collection.ui.caseInfo.contract.HistoryActionContract;
import com.huateng.collection.ui.caseInfo.presenter.HistoryActionsPresenter;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * 行动流水
 */
public class FragmentHistoryActions extends BaseFragment<HistoryActionsPresenter> implements HistoryActionContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeToLoadLayout;
    private HistoryActionsAdapter adapter;
    private String custId;
    private String caseId;

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryActionsAdapter();
        recyclerView.setAdapter(adapter);

        mSwipeToLoadLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                adapter.setEnableLoadMore(false);
                mPresenter.loadData(Constants.REFRESH, caseId, custId);
                //刷新时禁用下拉

            }
        });

        adapter.setOnLoadMoreListener(this, recyclerView);
    }


    @Override
    protected HistoryActionsPresenter createPresenter() {
        return new HistoryActionsPresenter(this);
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_actions;
    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        caseId = getArguments().getString(Constants.CASE_ID);
        custId = getArguments().getString(Constants.CUST_ID);
        // loadData();
        mPresenter.loadData(Constants.REFRESH, caseId, custId);


    }


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void showEmptyView() {

        if (adapter != null) {
            adapter.setEmptyView(R.layout.layout_empty_view, recyclerView);
        }

        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setEnabled(true);
        }
    }

    @Override
    public void setLogActionData(int status, List<LogActActionBean.RecordsBean> records) {
        if (status == Constants.REFRESH) {
            //下拉刷新
            adapter.setNewData(records);
            if (records.size() < 10) {
                adapter.setEnableLoadMore(false);
            } else {
                adapter.setEnableLoadMore(true);
            }

        } else {
            //加载更多
            adapter.addData(records);
            if (records.size() >= 10) {

                adapter.loadMoreComplete();
            } else {
                adapter.loadMoreEnd();
            }
        }

        //停止刷新 并恢复下拉刷新功能
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(false);
            mSwipeToLoadLayout.setEnabled(true);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        //加载数据时禁用刷新
        mSwipeToLoadLayout.setEnabled(false);
        mPresenter.loadData(Constants.LOAD_MORE, caseId, custId);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        // Log.e("nb", bean.code + "a:c" + bean.getObject());
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.REFRESH_HISTORY_ACTIONS) {
            mPresenter.loadData(Constants.REFRESH, caseId, custId);

        }

    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }
}
