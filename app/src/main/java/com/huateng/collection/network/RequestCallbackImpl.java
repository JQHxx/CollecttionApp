package com.huateng.collection.network;

import com.huateng.collection.app.MainApplication;
import com.huateng.network.callback.RequestCallback;
import com.huateng.network.error.ExceptionHandle;
import com.orhanobut.logger.Logger;
import com.tools.utils.GsonUtils;
import com.tools.utils.RxActivityUtils;
import com.tools.view.RxToast;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by shanyong on 2017/1/13.
 */

public abstract class RequestCallbackImpl<T> implements RequestCallback<String> {

    @Override
    public void beforeRequest() {

    }

    @Override
    public void requestError(String code, String msg) {
        end();
        Logger.e(code);
        
        RxToast.showToast(msg);
        if (String.valueOf(ExceptionHandle.ERROR.TIMEOUT).equals(code)) {
            RxActivityUtils.restartApp(MainApplication.getApplication());
        }
    }

    @Override
    public void requestComplete() {
        end();
    }

    @Override
    public void requestSuccess(String resp) {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T t = GsonUtils.fromJson(resp, type);
        end();
        response(t);
    }

    public abstract void response(T resp);

    public void end() {

    }
}
