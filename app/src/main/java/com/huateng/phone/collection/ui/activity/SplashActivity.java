package com.huateng.phone.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huateng.phone.collection.R;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends RxAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //避免每次启动都走splash界面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        initData();
    }

    protected void initData() {
        Flowable.intervalRange(0, 2, 0, 1, TimeUnit.SECONDS)
                .compose(bindToLifecycle())
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

}
