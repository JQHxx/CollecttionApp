package com.huateng.phone.collection.ui.fragment.casebox.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseFragment;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.CreditCardBean;
import com.huateng.phone.collection.ui.activity.CreditCardDetailActivity;
import com.huateng.phone.collection.ui.adapter.CreditCardAdapter;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020-04-23 18:41
 * description:信用卡信息
 */
public class CreditCardMsgFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String caseId;
    private String custId;
    private int pageSize = 20;
    private int pageNum = 0;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private CreditCardAdapter mAdapter;

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
        return R.layout.fragment_credit_card_msg;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CreditCardAdapter();
        mRecyclerview.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CreditCardBean.RecordsBean recordsBean =  mAdapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("recordsBean",recordsBean);
                Intent intent = new Intent(mContext, CreditCardDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        custId = getArguments().getString(Constants.CUST_ID);
        caseId = getArguments().getString(Constants.CASE_ID);

        loadData();


    }



    private void loadData() {
        Map<String, Object> map = new HashMap<>();

        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("tlrNo", Perference.getUserId());
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_CREDIT_CARD_INFO, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<CreditCardBean>() {


                    @Override
                    public void onError(String code, String msg) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.setEmptyView(R.layout.layout_empty_view,mRecyclerview);

                    }

                    @Override
                    public void onNextData(CreditCardBean creditCardBean) {
                        if (creditCardBean == null) {
                            return;
                        }
                        if(creditCardBean.getRecords().size()>0) {
                            mAdapter.setNewData(creditCardBean.getRecords());

                        }else {
                            mAdapter.setEmptyView(R.layout.layout_empty_view,mRecyclerview);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }


                });
    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
