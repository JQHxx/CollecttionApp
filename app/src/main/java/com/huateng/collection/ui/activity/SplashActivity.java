package com.huateng.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Flowable.intervalRange(0, 2, 0, 1, TimeUnit.SECONDS)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e("nb", "s--->" + (2 - aLong));
                        if (aLong == 1) {
                            Log.e("nb", "aLong aLong");
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("nb", "accept throwable" + throwable.getMessage());
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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
        return R.layout.activity_splash;
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
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
