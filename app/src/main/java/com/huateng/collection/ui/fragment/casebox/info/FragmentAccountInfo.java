package com.huateng.collection.ui.fragment.casebox.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.ui.activity.AccountInfoDetailActivity;
import com.huateng.collection.ui.adapter.AccountInfoAdapter;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 账户信息
 */
public class FragmentAccountInfo extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<RespAccount> accounts;
    private AccountInfoAdapter adapter;

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
        return R.layout.fragment_account_info;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        accounts = (List<RespAccount>) getArguments().getSerializable(Constants.CASE_ACCOUNT_INFO);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AccountInfoAdapter(R.layout.list_item_account_info, accounts, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, AccountInfoDetailActivity.class));
            }
        });


    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        List<RespAccount> list = new ArrayList<>();
        for (int i=0;i<3;i++){
            list.add(new RespAccount());
        }
        adapter.setNewData(list);
    }


    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
