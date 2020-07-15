package com.huateng.phone.collection.ui.fragment.casebox.info;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.base.BaseFragment;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.CreditMsgBean;
import com.huateng.phone.collection.ui.adapter.CreditMsgAdapter;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * author: yichuan
 * Created on: 2020-04-01 14:19
 * description:
 */
public class CreditMsgFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private CreditMsgAdapter mCreditMsgAdapter;

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
        return R.layout.fragment_credit_msg;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mCreditMsgAdapter = new CreditMsgAdapter();
        mRecyclerview.setAdapter(mCreditMsgAdapter);
        mCreditMsgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               // startActivity(new Intent(mContext, AccountInfoDetailActivity.class));
            }
        });

    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        List<CreditMsgBean> list = new ArrayList<>();

        for (int i=0;i<3;i++){
            list.add(new CreditMsgBean());
        }
        mCreditMsgAdapter.setNewData(list);

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
