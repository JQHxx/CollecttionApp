package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.bean.api.RespLog;
import com.huateng.collection.ui.adapter.HistoryActionsAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行动流水
 */
public class FragmentHistoryActions extends BaseFragment implements OnRefreshListener,
        OnLoadMoreListener {

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private List<RespLog> historyActions;
    private HistoryActionsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_history_actions, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        historyActions = (List<RespLog>) getArguments().getSerializable(Constants.CASE_HISTORY_ACTIONS);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        adapter = new HistoryActionsAdapter(R.layout.list_item_history_actions, historyActions);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }


}
