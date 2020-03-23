package com.huateng.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.ui.adapter.AccountInfoAdapter;
import com.huateng.collection.ui.base.BaseFragment;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账户信息
 */
public class FragmentAccountInfo extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<RespAccount> accounts;
    private AccountInfoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_account_info, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        accounts= (List<RespAccount>) getArguments().getSerializable(Constants.CASE_ACCOUNT_INFO);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AccountInfoAdapter(R.layout.list_item_account_info, accounts,this);
        recyclerView.setAdapter(adapter);
    }

}
