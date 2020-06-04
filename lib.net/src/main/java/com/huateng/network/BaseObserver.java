package com.huateng.network;

/**
 * author: yichuan
 * Created on: 2020-03-25 09:49
 * description:
 */

import com.huateng.network.error.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * File descripition:   数据处理基类
 *
 * @author lp
 * @date 2018/6/19
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onComplete() {

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof Exception) {
            //访问获得对应的Exception
            onError(ExceptionHandle.handleException(e));
        } else {
            //将Throwable 和 未知错误的status code返回
            onError(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onNext(T t) {

    }

    public abstract void onError(ExceptionHandle.ResponeThrowable responeThrowable);

}