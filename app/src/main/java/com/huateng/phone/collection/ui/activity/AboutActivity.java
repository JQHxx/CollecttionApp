package com.huateng.phone.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.tools.utils.AppUtils;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return R.layout.activity_about;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        String appVersionName = AppUtils.getAppVersionName();
        if(!TextUtils.isEmpty(appVersionName)) {
            mTvVersion.setText("V"+appVersionName);
        }
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
