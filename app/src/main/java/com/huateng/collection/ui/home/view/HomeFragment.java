package com.huateng.collection.ui.home.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.bean.CaseBeanData;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.ui.activity.CaseDetailActivity;
import com.huateng.collection.ui.adapter.TodoCasesAdapter;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.SearchDM;
import com.huateng.collection.ui.home.contract.HomeContract;
import com.huateng.collection.ui.home.presenter.HomePresenter;
import com.huateng.collection.widget.DividerItemDecoration;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.NetworkUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
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
 * 收件箱 待办案件列表
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, HomeContract.View {
    private final String TAG = getClass().getSimpleName();
    private String[] stringItems = {"拨号"};
    private ActionSheetDialog dialog;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layout_search)
    RelativeLayout mLayoutSearch;
    @BindView(R.id.layout_head)
    LinearLayout mLayoutHead;

    private TodoCasesAdapter mAdapter;

    private String custName;

    private View emptyView;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_todo_case_chooser;
    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        //刷新时禁用下拉
        mAdapter.setEnableLoadMore(false);
        initDictData();
        mPresenter.loadData(Constants.REFRESH);

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
                        mAdapter.setDictData(stringStringHashMap);
                        if (mAdapter.getData() != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        immersiveStatusBar(rxTitle);
        initRecyclerview();
        initListener();
        swipeRefreshLayout.setRefreshing(true);
    }

    private void initRecyclerview() {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        emptyView = inflater.inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        TextView tvTip = (TextView) emptyView.findViewById(R.id.tv_tip);

        //使用字体
       // Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.zcool_black);
      //  tvTip.setTypeface(typeFace);
       // tvTip.setText("无待办案件");

        mAdapter = new TodoCasesAdapter(R.layout.list_item_cases);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        recyclerView.setAdapter(mAdapter);
      //  mAdapter.setEmptyView(emptyView);
    }

    private void initListener() {


        //加载数据监听
        mAdapter.setOnLoadMoreListener(this, recyclerView);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CaseBeanData.RecordsBean bean = (CaseBeanData.RecordsBean) adapter.getData().get(position);
                if (view.getId() == R.id.ll_layout) {
                    Intent intent = new Intent(mContext, CaseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BUSINESS_TYPE, bean.getBusinessType());
                    bundle.putString(Constants.CASE_ID, bean.getCaseId());
                    bundle.putString(Constants.CUST_ID, bean.getCustNo());
                    bundle.putString(Constants.CUST_NAME, bean.getCustName());
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else if (view.getId() == R.id.iv_call_phone) {

                    if(TextUtils.isEmpty(bean.getPhoneNo())) {
                        RxToast.showToast("手机号码不能未空");
                        return;
                    }
                    //bean.getContactPnhone()
                    String tiltle = String.format("拨打%s电话\r\n%s", bean.getCustName(),"13082961783" );
                    dialog = new ActionSheetDialog(mContext, stringItems, null);
                    dialog.title(tiltle)
                            .titleTextSize_SP(14.5f)
                            .show();

                    dialog.setOnOperItemClickL(new OnOperItemClickL() {
                        @Override
                        public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String phoneNumber = bean.getPhoneNo();

                            Perference.setPrepareCallRecording(true);
                            Perference.setPrepareRecordingPhoneNumber(phoneNumber);

                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);

                            dialog.dismiss();
                        }
                    });
                  /*  Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getPhoneNo()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);*/
                }
            }
        });


        mLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClicked();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        // Log.e("nb", bean.code + "a:c" + bean.getObject());
        if (bean == null) {
            return;
        } else if (bean.getCode() == BusEvent.REFRESH_CASE_SUMMARY_REMINDER) {
            mPresenter.loadData(Constants.REFRESH);
        } else if (bean.getCode() == BusEvent.LOGIN_SUCESS) {

            mPresenter.loadData(Constants.REFRESH);
        }

    }

    /**
     * 加载搜索案件
     */
    public void loadSearchCase(String custName) {
        boolean isConnected = NetworkUtils.isConnected();
        if (!isConnected) {
            //  List<CaseBeanData.RecordsBean> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "DONE=?", "0");
            //  refresh(respCaseSummaries);
            return;
        }

        if (TextUtils.isEmpty(custName)) {
            RxToast.showToast("查询条件不能为空");
            return;
        }

        mPresenter.loadSearchCase(custName);

    }

    /**
     * 点击搜索按钮
     */
    public void onSearchClicked() {

        final SearchDM dm = new DialogCenter(getActivity()).showSearchCaseDialog();

        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
            @Override
            public void onLeftClicked(View v) {
                custName = dm.getCustName();

                loadSearchCase(custName);
                dm.getDialog().dismiss();
            }

            @Override
            public void onRightClicked(View v) {
                dm.getDialog().dismiss();
            }
        });
    }


    @Override
    public void onRefresh() {
        //刷新时禁用下拉
        mAdapter.setEnableLoadMore(false);
        mPresenter.loadData(Constants.REFRESH);
    }


    public void showEmptyView() {
        if(!Perference.getBoolean(Perference.OUT_SOURCE_FLAG)) {
            Logger.d("展示emptyView");

            if (mAdapter != null) {
                mAdapter.setEmptyView(emptyView);
            }

            mAdapter.getData().clear();

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
            }
        }

    }


    @Override
    public void onLoadMoreRequested() {
        //加载数据时禁用刷新
        swipeRefreshLayout.setEnabled(false);
        mPresenter.loadData(Constants.LOAD_MORE);
    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    /**
     * 加载代办任务
     *
     * @param status
     */
    @Override
    public void setCaseSummaries(int status, List<CaseBeanData.RecordsBean> respCaseSummaries) {


        if (status == Constants.REFRESH) {
            //下拉刷新
            mAdapter.setNewData(respCaseSummaries);
            if (respCaseSummaries.size() < 10) {
                mAdapter.setEnableLoadMore(false);
            } else {
                mAdapter.setEnableLoadMore(true);
            }
        } else {
            //加载更多
            mAdapter.addData(respCaseSummaries);
            if (respCaseSummaries.size() >= 10) {

                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd();
            }

        }

        //停止刷新 并恢复下拉刷新功能
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(true);
        }


    }

    /**
     * 展示搜索数据
     *
     * @param respCaseSummaries
     */
    @Override
    public void setSearchCase(List<CaseBeanData.RecordsBean> respCaseSummaries) {
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            RxToast.showToast("没有查询到相关案件信息");
            return;
        }
        mAdapter.setNewData(respCaseSummaries);
        if (respCaseSummaries.size() >= 10) {

            mAdapter.loadMoreComplete();
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onError(String msg) {
        showEmptyView();

        showToast(msg);
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }


}
