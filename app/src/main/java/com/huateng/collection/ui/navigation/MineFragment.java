package com.huateng.collection.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.ui.activity.AboutActivity;
import com.huateng.collection.ui.activity.LoginActivity;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author: yichuan
 * Created on: 2020-04-02 16:14
 * description:
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView mIvHeader;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.ll_clear_cache)
    LinearLayout mLlClearCache;
    @BindView(R.id.ll_change_account)
    LinearLayout mLlChangeAccount;
    @BindView(R.id.ll_update_version)
    LinearLayout mLlUpdateVersion;
    @BindView(R.id.ll_chage_password)
    LinearLayout mLlChagePassword;
    @BindView(R.id.ll_about)
    LinearLayout mLlAbout;

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
        return R.layout.fragment_mine;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @OnClick({R.id.iv_header, R.id.ll_clear_cache, R.id.ll_change_account, R.id.ll_update_version, R.id.ll_chage_password, R.id.ll_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.ll_clear_cache:
                break;
            case R.id.ll_change_account:
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.ll_update_version:
                //检查版本更新
                break;
            case R.id.ll_chage_password:
                //修改密码
                break;
            case R.id.ll_about:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
        }
    }
}
