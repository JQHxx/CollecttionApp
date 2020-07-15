package com.huateng.phone.collection.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huateng.phone.collection.R;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.network.UpdateHelper;
import com.huateng.fm.ui.widget.FmButton;
import com.tools.utils.AppUtils;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import butterknife.BindView;

/**
 * Created by sumincy on 2016/12/13.
 * 版本更新
 */

public class CheckVersionActivity extends BaseActivity {

    @BindView(R.id.tv_appVersion)
    TextView tvAppVersion;
    @BindView(R.id.btn_check)
    FmButton btnCheck;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {
//        setFragmentAnimator(new DefaultHorizontalAnimator());
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        tvAppVersion.setText(AppUtils.getAppVersionName());

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHelper.checkupdate(CheckVersionActivity.this, true);
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
        return R.layout.fragment_check_version;
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
