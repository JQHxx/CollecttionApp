package com.huateng.collection.ui.fragment.casebox;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.api.RespCustSummary;
import com.huateng.collection.bean.orm.VisitCode;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.event.EventEnv;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.activity.ActivityMain;
import com.huateng.collection.ui.adapter.TodoCasesAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.SearchDM;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.DividerItemDecoration;
import com.huateng.network.ApiConstants;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.utils.GsonUtils;
import com.tools.utils.NetworkUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * 收件箱 待办案件列表
 */
public class FragmentTodoCaseChooser extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        View.OnClickListener, View.OnTouchListener {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.tv_selAll)
    TextView tvSelAll;
    @BindView(R.id.tv_selNone)
    TextView tvSelNone;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.v_selectOptions)
    LinearLayout vSelectOptions;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;

    LinearLayout vSearch;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TodoCasesAdapter mAdapter;
    //填充适配器的数据
    private List<RespCaseSummary> datalist = new ArrayList<>();

    private int pageSize = 10;
    private int pageNum = 1;

    private String custName;

    List<String> names;
    List<RespCustSummary> summaries;

    private View emptyView;
    //    //当前界面 进行操作的案件数据
    //    List<RespCaseSummary> caseSummaries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_todo_case_chooser, container, false);
        ButterKnife.bind(this, mContentView);
        EventBusActivityScope.getDefault(_mActivity).register(this);
        return mContentView;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        immersiveStatusBar(rxTitle);

        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityMain) getActivity()).getDrawerLayout().openDrawer(GravityCompat.START);

            }
        });

        rxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CASE_ID, "a123");
                bundle.putBoolean(Constants.IS_TODO_CASE, true);
                bundle.putString(Constants.ADDRESS_ID, "b123");
                bundle.putString(Constants.VISIT_ADDRESS, "c123");

                BaseFragment fragment = BaseFragment.newInstance(FragmentCaseDetail.class, bundle);

                BaseFragment parent = (BaseFragment)getParentFragment();
                if (parent != null) {
                    parent.startBrotherFragment(fragment, SINGLETASK);
                }
               /* if (vSelectOptions.getVisibility() == View.VISIBLE) {
                    if (mAdapter.inSelectMode() && mAdapter.getSelectedPositions().size() > 0) {
                        onDownloadClicked(v, mAdapter.getSelectedPositions());
                    }
                } else {
                    vSelectOptions.setVisibility(View.VISIBLE);
                    mAdapter.goSelectMode();
                    mAdapter.setEnableLoadMore(false);
                    swipeRefreshLayout.setEnabled(false);
                }*/
            }
        });


        vSearch = (LinearLayout) mContentView.findViewById(R.id.layout_search);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        emptyView = inflater.inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        TextView tvTip = (TextView) emptyView.findViewById(R.id.tv_tip);

        //使用字体
        Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.zcool_black);
        tvTip.setTypeface(typeFace);
        tvTip.setText("无待办案件");

        //        vSearch.setOnClickListener(this);
        //        ivDownLoad.setOnClickListener(this);
        //        tvCancel.setOnClickListener(this);
        //        tvSelAll.setOnClickListener(this);
        //        tvSelNone.setOnClickListener(this);

        // TODO recyclerview 截获了父布局的点击事件
        vSearch.setOnTouchListener(this);
        tvCancel.setOnTouchListener(this);
        tvSelAll.setOnTouchListener(this);
        tvSelNone.setOnTouchListener(this);

        mAdapter = new TodoCasesAdapter(R.layout.list_item_cases, datalist, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        //        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
        //加载数据监听
        mAdapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
        //查询行动码
        Map<String, String> map2 = new HashMap<>();
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<VisitCode>>() {
            @Override
            public void response(List<VisitCode> t) {
                SugarRecord.deleteAll(VisitCode.class);
                SugarRecord.saveInTx(t);
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_VISIT_SUM_CODE, map2);
    }


    @Subscribe
    public void pickCasesToOutBox(EventEnv env) {
        String key = env.obtainEvent();
        if (BusEvent.PICK_CASES_TO_OUTBOX.equals(key)) {
            for (int i = 0; i < datalist.size(); i++) {
                RespCaseSummary caseSummary = datalist.get(i);
                if (caseSummary.getAddrId().equals(env.get(Constants.BIZ_ID))) {
                    //移除移入发件箱的案件
                    datalist.remove(caseSummary);
                    mAdapter.notifyDataSetChanged();
                    if (this.datalist.size() == 0) {
                        showEmptyView();
                    }
                    //刷新发件箱
                    EventBusActivityScope.getDefault(_mActivity).post(BusEvent.REFRESH_DONE_CASES);
                }
            }
        }
    }

    public void refresh(List<RespCaseSummary> datas) {
        this.datalist.clear();
        this.datalist.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void refreshCaseSummaries(String event) {
        if (event.equals(BusEvent.REFRESH_CASE_SUMMARY_REMINDER)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载待办案件
     *
     * @param status
     */
    public void loadTodoCases(final int status) {

        Log.i("todocases", "-------loadTodoCases-------");

        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        if (status == Constants.LOAD_MORE) {
            pageNum++;
        }
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageCount", String.valueOf(pageSize));
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseSummary>>() {

            @Override
            public void response(List<RespCaseSummary> respCaseSummaries) {

                if (respCaseSummaries.size() <= 0) {
                    if (status == Constants.REFRESH) {
                        datalist.clear();
                        mAdapter.notifyDataSetChanged();
                        SugarRecord.deleteAll(RespCaseSummary.class);
                    } else if (status == Constants.LOAD_MORE) {
                        mAdapter.loadMoreComplete();
                        mAdapter.loadMoreEnd(false);
                    }
                } else {
                    layoutHead.setVisibility(View.VISIBLE);
                    //检查案件文件存储路径
                    for (RespCaseSummary summary : respCaseSummaries) {
                        //为所有案件添加UserID
                        summary.setUserId(Perference.getUserId());

                        //地址id 设置为业务id
                        summary.setBizId(summary.getAddrId());

                        String processId = AttachmentProcesser.getProcessId(summary.getCaseId(), summary.getAddrId());

                        //                        Logger.i("processId : %s", processId);
                        AttachmentProcesser.getInstance(getActivity()).checkPath(summary.getBizId(), processId);
                    }

                    //将获取到的案件 分离出已在发件箱的案件 状态为DONE的案件
                    List<RespCaseSummary> caseSummaries = new ArrayList<RespCaseSummary>();
                    caseSummaries.addAll(respCaseSummaries);

                    List<RespCaseSummary> respCaseSummariesDone = CaseManager.obtainDoneCaseSummaryForUser();

                    if (respCaseSummariesDone != null) {
                        for (int i = caseSummaries.size() - 1; i >= 0; i--) {
                            RespCaseSummary respCaseSummary = caseSummaries.get(i);
                            for (RespCaseSummary caseSummaryDone : respCaseSummariesDone) {
                                if (caseSummaryDone.getAddrId().equals(respCaseSummary.getAddrId())) {
                                    caseSummaries.remove(respCaseSummary);
                                }
                            }
                        }
                    }

                    //                    if (caseSummaries.size() <= 0) {
                    //                        //当请求到的案件 已有并全转入收件箱时
                    //                        showEmptyView();
                    //                    }

                    if (status == Constants.REFRESH) {
                        refresh(caseSummaries);

                        // 移除缓存案件详情：刷新案件详情
                        CaseManager.removeNotDoneCaseSummaries();
                        CaseManager.removeCachedCaseDetail();

                        //                        requestTodoCusts();
                    } else if (status == Constants.LOAD_MORE) {
                        load(caseSummaries);
                        mAdapter.loadMoreComplete();
                    }

                    //                    //缓存案件概要
                    SugarRecord.saveInTx(caseSummaries);

                }
            }

            @Override
            public void end() {
                super.end();
                if (status == Constants.REFRESH) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (datalist.size() <= 0) {
                        showEmptyView();
                    }
                }
                //重设
                swipeRefreshLayout.setEnabled(true);
                mAdapter.setEnableLoadMore(true);
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map);

    }

    public void load(List<RespCaseSummary> datalist) {
        this.datalist.addAll(datalist);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 加载搜索案件
     */
    public void loadSearchCase(String custName) {
        boolean isConnected = NetworkUtils.isConnected();
        if (!isConnected) {
            List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "DONE=?", "0");
            refresh(respCaseSummaries);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        map.put("pageNum", "1");
        map.put("pageCount", "-1");
        if (StringUtils.isNotEmpty(custName)) {
            map.put("custName", custName);
        }

        //        if (StringUtils.isNotEmpty(caseId)) {
        //            map.put("caseId", caseId);
        //        }

        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseSummary>>() {

            @Override
            public void beforeRequest() {
                super.beforeRequest();
                showLoading();
            }

            @Override
            public void requestError(String code, String msg) {
                super.requestError(code, msg);
            }

            @Override
            public void response(List<RespCaseSummary> respCaseSummaries) {
                Logger.i("size------>" + respCaseSummaries.size());
                List<RespCaseSummary> caseSummaries = new ArrayList<RespCaseSummary>();
                caseSummaries.addAll(respCaseSummaries);
                List<RespCaseSummary> respCaseSummariesDone = SugarRecord.find(RespCaseSummary.class, "DONE=?", "1");
                for (int i = caseSummaries.size() - 1; i >= 0; i--) {
                    RespCaseSummary respCaseSummary = caseSummaries.get(i);
                    for (RespCaseSummary caseSummaryDone : respCaseSummariesDone) {
                        if (caseSummaryDone.getBizId().equals(respCaseSummary.getAddrId()))
                            caseSummaries.remove(respCaseSummary);
                    }
                }
                if (caseSummaries.size() > 0) {
                    refresh(caseSummaries);
                } else {
                    RxToast.showToast("未查询到案件");
                }
            }

            @Override
            public void end() {
                super.end();
                hideLoading();
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_QUERY_TODO_CASE_LIST, map);
    }

    /**
     * 点击搜索按钮
     */
    public void onSearchClicked(View v) {
        Logger.i("onSearchClicked");

        final SearchDM dm = new DialogCenter(getActivity()).showSearchCaseDialog();
        //        dm.refreshCustNames(names);

        //        dm.setCustNamesSelectedListener(new UniversalInput.SpinnerItemSelectedListener() {
        //            @Override
        //            public void onItemSelected(int position) {
        //            }
        //        });
        //
        //        dm.setCustNamesTouchListener(new UniversalInput.OnHtOptionTouchListener() {
        //            @Override
        //            public void onHtOptionTouched() {
        //                //TODO
        //            }
        //        });


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

    /**
     * 下载
     *
     * @param v
     * @param positions
     */
    public void onDownloadClicked(View v, List<Integer> positions) {
        List<String> caseIds = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            RespCaseSummary caseSummary = datalist.get(positions.get(i));
            CaseManager.removeCachedCaseDetail(caseSummary.getAddrId());
            caseIds.add(caseSummary.getCaseId());
        }
        requestAndCacheCases(caseIds);
    }


    @Override
    public void onRefresh() {
        //刷新时禁用下拉
        mAdapter.setEnableLoadMore(false);
        pageNum = 1;
        loadTodoCases(Constants.REFRESH);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_downLoad:
                if (vSelectOptions.getVisibility() == View.VISIBLE) {
                    if (mAdapter.inSelectMode() && mAdapter.getSelectedPositions().size() > 0) {
                        onDownloadClicked(view, mAdapter.getSelectedPositions());
                    }
                } else {
                    vSelectOptions.setVisibility(View.VISIBLE);
                    mAdapter.goSelectMode();
                    swipeRefreshLayout.setEnabled(false);
                    mAdapter.setEnableLoadMore(false);
                }
                break;
            case R.id.layout_search:
                onSearchClicked(view);
                break;
            case R.id.tv_cancel:
                vSelectOptions.setVisibility(View.GONE);
                mAdapter.leaveSelectMode();
                swipeRefreshLayout.setEnabled(true);
                mAdapter.setEnableLoadMore(true);
                break;
            case R.id.tv_selAll:
                mAdapter.selectAll();
                break;
            case R.id.tv_selNone:
                mAdapter.reverse();
                break;
        }
    }

    public void requestTodoCusts() {
        names = new ArrayList<String>();
        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());

        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCustSummary>>() {
            @Override
            public void response(final List<RespCustSummary> t) {
                summaries = t;
                names.clear();
                for (RespCustSummary custSummary : t) {
                    names.add(custSummary.getCustName());
                }
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_QUERY_TODO_CUSTS, map);

    }

    /**
     * 批量请求及缓存案件
     *
     * @param caseIds
     */
    public void requestAndCacheCases(final List<String> caseIds) {

        String caseIdStr = GsonUtils.toJson(caseIds);

        Map<String, String> map = new HashMap<>();
        map.put("caseList", caseIdStr);
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseDetail>>() {
            @Override
            public void beforeRequest() {
                super.beforeRequest();
                showLoading();
            }

            @Override
            public void response(List<RespCaseDetail> caseDetails) {
                CaseManager.cacheCaseDetail(caseDetails);
                Toast.makeText(getActivity(), "缓存成功", Toast.LENGTH_LONG).show();
                leaveSelectMode();
            }

            @Override
            public void end() {
                super.end();
                hideLoading();
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_GET_CASE_DETAIL_BATCH, map);
    }


    public void leaveSelectMode() {
        vSelectOptions.setVisibility(View.GONE);
        mAdapter.leaveSelectMode();
        mAdapter.setEnableLoadMore(true);
        swipeRefreshLayout.setEnabled(true);
    }


    public void showEmptyView() {
        Logger.d("展示emptyView");
        layoutHead.setVisibility(View.GONE);
        mAdapter.setEmptyView(emptyView);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.layout_search:
                    onSearchClicked(v);
                    break;
                case R.id.tv_cancel:
                    vSelectOptions.setVisibility(View.GONE);
                    mAdapter.leaveSelectMode();
                    swipeRefreshLayout.setEnabled(true);
                    mAdapter.setEnableLoadMore(true);
                    break;
                case R.id.tv_selAll:
                    mAdapter.selectAll();
                    break;
                case R.id.tv_selNone:
                    mAdapter.reverse();
                    break;
            }
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载数据时禁用刷新
        swipeRefreshLayout.setEnabled(false);
        loadTodoCases(Constants.LOAD_MORE);
    }
}
