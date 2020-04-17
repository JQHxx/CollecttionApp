package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.ChannelItem;
import com.huateng.collection.ui.adapter.GridMenuAdapter;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseManagerActivity extends BaseActivity {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private GridMenuAdapter mGridMenuAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_manager;
    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mGridMenuAdapter = new GridMenuAdapter(null);
        mRecyclerview.setAdapter(mGridMenuAdapter);
        List<ChannelItem> list = new ArrayList<>();

        String[] titles = {"客户信息", "账户信息", "电话簿", "地址簿", "行动流水", "录音", "拍照", "外访报告"};
        String[] titles2 = {"录音","拍照","外访报告"};

        list.add(new ChannelItem(true,"案件信息"));

        for (int i = 0; i < titles.length; i++) {
          list.add( new ChannelItem(false,titles[i]));
        }

        list.add(new ChannelItem(true,"案件处理"));
        for (int i = 0; i < titles2.length; i++) {
            list.add( new ChannelItem(false,titles2[i]));
        }
        mGridMenuAdapter.setNewData(list);

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

    }


    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
