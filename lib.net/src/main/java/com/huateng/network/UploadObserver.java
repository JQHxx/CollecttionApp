package com.huateng.network;

import android.util.Log;
import android.util.Pair;

import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by wuzhiming on 2019/2/21
 */
public abstract class UploadObserver<T> implements Observer<Object> {

    private int mPercent = 0;
    private Type mType;

    public UploadObserver() {
        mType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(Object o) {
        Log.e("nb",o.toString());
        if (o instanceof Pair) {
            long uploaded = (long)(((Pair) o).first);
            long sumLength = (long)(((Pair) o).second);
            _onProgress(uploaded, sumLength);
            int percent = (int) (uploaded*100f / sumLength);
            if (percent < 0) {
                percent = 0;
            }
            if (percent > 100) {
                percent = 100;
            }
            if (percent == mPercent) {
                return;
            }
            mPercent = percent;
            _onProgress(mPercent);
            return;
        }else {
            try {
                Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                T t = GsonUtils.fromJson(o.toString(), type);
                Log.e("nb","_onNext _onNext 111");
                _onNext(t);
            } catch (Exception e) {
                Log.e("nb","_onError _onError 111");
                _onError(e);
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e("nb",e.getLocalizedMessage()+":"+e.getMessage());
        if("tokenOverdue".equals(e.getMessage())) {
            //token过期了
            EventBus.getDefault().post(new EventBean(BusEvent.TOKEN_OVERDUE));
            return;
        }
        _onError(e);
    }

    @Override
    public void onComplete() {
    }

    public abstract void _onNext(T t);

    public void _onProgress(Integer percent) {}

    public void _onProgress(long uploaded, long sumLength) {}

    public abstract void _onError(Throwable e);

}
