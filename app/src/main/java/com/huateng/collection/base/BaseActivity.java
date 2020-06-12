package com.huateng.collection.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.widget.LoadingDialog;
import com.tools.ActivityUtils;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * File descripition: activity基类
 * <p>
 *
 * @author lp
 * @date 2018/5/16
 */
public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity implements IBaseView {
    // protected final String TAG = getClass().getSimpleName();
    private Unbinder unbinder;
    protected P mPresenter;
    private boolean isUseEventBus;
    private LoadingDialog dialog;

    protected abstract P createPresenter();


    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 数据初始化操作
     */
    protected abstract void initData();

    /**
     * 此处设置沉浸式地方
     */
    protected abstract void setStatusBar();

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
        }
        if (!dialog.isShowing()) {

            dialog.show();
            Log.e("NBCB", "dialog show");
        }
    }

    @Override
    public void hideLoading() {
        if (dialog == null) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }


    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }


    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        mPresenter = createPresenter();
        unbinder = ButterKnife.bind(this);
        setStatusBar();

        isUseEventBus = isUseEventBus();
        initView(savedInstanceState);
        // 把actvity放到application栈中管理
        ActivityUtils.getAppManager().addActivity(this);

        if (isUseEventBus) {
            EventBus.getDefault().register(this);
        }
        // initListener();
        initData();
    }


    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        if (isUseEventBus) {
            EventBus.getDefault().unregister(this);
        }

        // 把actvity放到application栈中管理
        ActivityUtils.getAppManager().removeActivity(this);
    }

    public boolean isUseEventBus() {
        return false;
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        RxToast.showToast(message);
        //  ToastUtil.showShortToast(Utils.getApp(),message);
    }

    protected void immersiveStatusBar(View title) {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, title);
    }
}

