package com.huateng.collection.ui.fragment.casebox;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.widget.RecordView;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * author: yichuan
 * Created on: 2020-03-18 09:17
 * description:
 */
public class FragmentRecord extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.csv_record)
    RecordView mCsvRecord;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        SystemBarHelper.immersiveStatusBar(FragmentRecord.this,0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);
        mRxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

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
        return R.layout.fragment_record;
    }



    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {

    }


    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
