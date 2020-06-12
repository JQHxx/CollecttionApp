package com.huateng.collection.ui.fragment.casebox.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.AccountInfoBean;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.ui.activity.AccountInfoDetailActivity;
import com.huateng.collection.ui.adapter.AccountInfoAdapter;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.orm.SugarRecord;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 账户信息
 */
public class FragmentAccountInfo extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private String custId;
    private String caseId;
    private int pageSize = 20;
    private int pageNum = 0;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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

        //accounts = (List<RespAccount>) getArguments().getSerializable(Constants.CASE_ACCOUNT_INFO);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AccountInfoAdapter(R.layout.list_item_account_info);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               AccountInfoBean.RecordsBean  recordData = (AccountInfoBean.RecordsBean) adapter.getData().get(position);
                Intent intent = new Intent(mContext, AccountInfoDetailActivity.class);
                intent.putExtra("recordData",recordData);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
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

        initDictData();
        Map<String, String> map = new HashMap<>();

        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("tlrNo", Perference.getUserId());
        map.put("pageNo", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECTED_ACCT_ACCOUNT_INFO, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<AccountInfoBean>() {


                    @Override
                    public void onError(String code, String msg) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNextData(AccountInfoBean accountInfoBean) {
                        if(mContext == null) {
                            return;
                        }


                        if(accountInfoBean == null || accountInfoBean.getRecords().size() ==0) {
                            adapter.setEmptyView(R.layout.layout_empty_view,recyclerView);
                        }

                        mSwipeRefreshLayout.setRefreshing(false);

                        adapter.setNewData(accountInfoBean.getRecords());
                    }


                });
    }


    private void initDictData() {
        Observable.create(new ObservableOnSubscribe<HashMap<String, String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<HashMap<String, String>> e) throws Exception {
                //   Log.e(TAG, "Observable thread is : " + Thread.currentThread().getName());
                List<DictItemBean> dictDataBeans = SugarRecord.find(DictItemBean.class, "DICT_CODE=?", "PRODUCTCODE");
                HashMap<String, String> hashMap = new HashMap<>();
                for (DictItemBean dictItemBean : dictDataBeans) {
                    // Log.e("nb", dictItemBean.getDescription() + ":" + dictItemBean.getDataCode());
                    hashMap.put(dictItemBean.getDataVal(), dictItemBean.getDescription());
                }
                e.onNext(hashMap);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HashMap<String, String>>() {
                    @Override
                    public void accept(HashMap<String, String> stringStringHashMap) throws Exception {
                        Log.e("nb", "Dict加载完成" + stringStringHashMap.size());
                        adapter.setDictData(stringStringHashMap);
                        if (adapter.getData() != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }



    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
