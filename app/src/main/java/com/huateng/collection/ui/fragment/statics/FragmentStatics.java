package com.huateng.collection.ui.fragment.statics;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespStaticsItem;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.adapter.StaticsAdapter;
import com.huateng.collection.widget.DividerItemDecoration;
import com.huateng.network.ApiConstants;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 外访统计
 */
public class FragmentStatics extends BaseFragment implements OnRefreshListener {

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    private List<RespStaticsItem> respStaticsItems = new ArrayList<>();
    private StaticsAdapter adapter;

    private View emptyView;

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
        return R.layout.fragment_statics;
    }



    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        immersiveStatusBar(rxTitle);

//        vSearch = (LinearLayout) mContentView.findViewById(R.id.layout_search);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        emptyView = inflater.inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        TextView tvTip = (TextView) emptyView.findViewById(R.id.tv_tip);

        //使用字体
        Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.zcool_black);
        tvTip.setTypeface(typeFace);
        tvTip.setText("无外访记录");

        requestStatics();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setLoadMoreEnabled(false);
        adapter = new StaticsAdapter(R.layout.list_item_statics_2, respStaticsItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        requestStatics();
    }

    public void requestStatics() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespStaticsItem>>() {

            @Override
            public void response(List<RespStaticsItem> t) {
                respStaticsItems.clear();
                if (null != t && t.size() > 0) {
                    respStaticsItems.addAll(t);
                } else {
                    adapter.setEmptyView(emptyView);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void end() {
                super.end();
                swipeToLoadLayout.setRefreshing(false);
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_GET_STATICS, map);
    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
