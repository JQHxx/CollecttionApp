package com.huateng.network.error;


import io.reactivex.Observer;

/**
 * Created by Sumincy on 2018/8/25.
 * 网络错误处理
 */
public abstract class NetworkSubscriber<T> implements Observer<T> {


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
