package com.huateng.network.error;


import rx.Subscriber;

/**
 * Created by Sumincy on 2018/8/25.
 * 网络错误处理
 */
public abstract class NetworkSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

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

    @Override
    public void onStart() {
        super.onStart();
    }

    public abstract void onError(ExceptionHandle.ResponeThrowable responeThrowable);

}
