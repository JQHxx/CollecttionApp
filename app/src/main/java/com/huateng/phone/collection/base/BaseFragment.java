package com.huateng.phone.collection.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.phone.collection.widget.LoadingDialog;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @param <P>
 */

public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements IBaseView {
    public View view;
    private Unbinder unbinder;

    protected P mPresenter;

    private LoadingDialog dialog;

    protected abstract P createPresenter();

    private boolean isUseEventBus;

    public Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        mPresenter = createPresenter();
        unbinder = ButterKnife.bind(this, view);
        isUseEventBus = isUseEventBus();
        if (isUseEventBus) {
            // RxBus.get().register(this);
            EventBus.getDefault().register(this);

        }
        this.initView(savedInstanceState);
        return view;
    }

    protected View findViewById(@IdRes int id) {
        return view.findViewById(id);
    }


    protected void immersiveStatusBar(View title) {
        SystemBarHelper.immersiveStatusBar((Activity) mContext, 0);
        SystemBarHelper.setHeightAndPadding(mContext, title);
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);


    /**
     * 数据初始化操作
     */
    protected abstract void initData();


    @Override
    public void showLoading() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new LoadingDialog(getActivity());
        dialog.show();
    }

    @Override
    public void hideLoading() {

        if (dialog == null) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        //  ToastUtil.showShortToast(Utils.getApp(), message);
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
        intent.setClass(getActivity(), clz);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initData();

    }

    public static BaseFragment newInstance(Class<?> clazz) {
        Bundle args = new Bundle();
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) clazz.newInstance();
            fragment.setArguments(args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public static BaseFragment newInstance(Class<?> clazz, Bundle args) {
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) clazz.newInstance();
            fragment.setArguments(args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }


    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        if (isUseEventBus) {
            // xBus.get().unRegister(this);
            EventBus.getDefault().unregister(this);
        }
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
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public boolean isUseEventBus() {
        return false;
    }
}

