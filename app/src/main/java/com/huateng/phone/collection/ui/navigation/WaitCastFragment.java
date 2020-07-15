package com.huateng.phone.collection.ui.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseFragment;
import com.huateng.phone.collection.bean.CaseBeanData;
import com.huateng.phone.collection.bean.CaseIdBean;
import com.huateng.phone.collection.bean.orm.DictItemBean;
import com.huateng.phone.collection.ui.activity.CaseDetailActivity;
import com.huateng.phone.collection.ui.adapter.TodoCasesAdapter;
import com.huateng.phone.collection.ui.adapter.WaitCasesAdapter;
import com.huateng.phone.collection.ui.wait.contract.WaitCaseContract;
import com.huateng.phone.collection.ui.wait.presenter.WaitCasePresenter;
import com.huateng.phone.collection.widget.DividerItemDecoration;
import com.orm.SugarRecord;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
 * author: yichuan
 * Created on: 2020/7/7 15:02
 * description:
 */
public class WaitCastFragment extends BaseFragment<WaitCasePresenter> implements WaitCaseContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private WaitCasesAdapter mAdapter;
    private View emptyView;
    private String[] stringItems = {"拨号"};
    private ActionSheetDialog dialog;

    @Override
    protected WaitCasePresenter createPresenter() {
        return new WaitCasePresenter(this);
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wait_cast;
    }


    @Override
    protected void initData() {
        //刷新时禁用下拉
        mAdapter.setEnableLoadMore(false);
        initDictData();
        mPresenter.loadData(Constants.REFRESH);
    }


    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        immersiveStatusBar(mRxTitle);
        initRecyclerview();
        initListener();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initRecyclerview() {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        emptyView = inflater.inflate(R.layout.layout_empty_view, (ViewGroup) mRecyclerview.getParent(), false);

        mAdapter = new WaitCasesAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
    }

    private void initListener() {

        //加载数据监听
        mAdapter.setOnLoadMoreListener(this, mRecyclerview);

        mAdapter.setOnSwipeMenuListener(new TodoCasesAdapter.OnItemClickListener() {
            @Override
            public void rightClick(int position) {
                CaseBeanData.RecordsBean recordsBean = mAdapter.getData().get(position);

                List<CaseIdBean> caseIds = new ArrayList<>();
                caseIds.add(new CaseIdBean(recordsBean.getCaseId()));
                  mPresenter.removeWaitCast(caseIds,position);
            }

            @Override
            public void contentClick(int position) {
                CaseBeanData.RecordsBean bean = mAdapter.getData().get(position);

                Intent intent = new Intent(mContext, CaseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUSINESS_TYPE, bean.getBusinessType());
                bundle.putString(Constants.CASE_ID, bean.getCaseId());
                bundle.putString(Constants.CUST_ID, bean.getCustNo());
                bundle.putString(Constants.CUST_NAME, bean.getCustName());
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void callPhone(int position) {
                CaseBeanData.RecordsBean bean = mAdapter.getData().get(position);

                if (TextUtils.isEmpty(bean.getPhoneNo())) {
                    RxToast.showToast("手机号码不能未空");
                    return;
                }
                String tiltle = String.format("拨打%s电话\r\n%s", bean.getCustName(), bean.getPhoneNo());
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
            }
        });


      /*  mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                Log.e("nb",view.getId()+":");
                if (view.getId() == R.id.content) {

                    Intent intent = new Intent(mContext, CaseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BUSINESS_TYPE, bean.getBusinessType());
                    bundle.putString(Constants.CASE_ID, bean.getCaseId());
                    bundle.putString(Constants.CUST_ID, bean.getCustNo());
                    bundle.putString(Constants.CUST_NAME, bean.getCustName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    if (stateCache != null && stateCache == RIGHTOPEN) {
                        easySwipeMenuLayout.resetStatus();
                    }

                } else if (view.getId() == R.id.iv_call_phone) {

                    if (TextUtils.isEmpty(bean.getPhoneNo())) {
                        RxToast.showToast("手机号码不能未空");
                        return;
                    }
                    String tiltle = String.format("拨打%s电话\r\n%s", bean.getCustName(), bean.getPhoneNo());
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
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getPhoneNo()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else if (view.getId() == R.id.right) {

                }


            }
        });
*/

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @Override
    public void onError(String msg) {

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
            if (respCaseSummaries.size() < 10) {
                mAdapter.loadMoreEnd();

            } else {
                mAdapter.loadMoreComplete();
            }


        }

        //停止刷新 并恢复下拉刷新功能
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }


    }


    @Override
    public void showEmptyView() {
        if (mAdapter != null) {
            mAdapter.getData().clear();
            mAdapter.setEmptyView(emptyView);
            mAdapter.notifyDataSetChanged();
        }


        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }

    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void removeWaitCaseSucess(int position) {

        if (mAdapter == null) {
            return;
        }
        RxToast.showToast("待清收案件删除成功");
        mAdapter.remove(position);

    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        //刷新时禁用下拉
        mAdapter.setEnableLoadMore(false);
        mPresenter.loadData(Constants.REFRESH);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载数据时禁用刷新
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.loadData(Constants.LOAD_MORE);
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
                        // Log.e("nb", "Dict加载完成" + stringStringHashMap.size());
                        mAdapter.setDictData(stringStringHashMap);
                        if (mAdapter.getData() != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        // Log.e("nb", bean.code + "a:c" + bean.getObject());
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.ADD_CASE_TO_WAIT_LIST) {
            mPresenter.loadData(Constants.REFRESH);
            return;
        }
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }
}
