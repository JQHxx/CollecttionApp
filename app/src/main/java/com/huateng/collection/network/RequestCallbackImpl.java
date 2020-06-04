package com.huateng.collection.network;

import android.text.TextUtils;

import com.huateng.collection.app.MainApplication;
import com.huateng.collection.bean.ErrorMsgBean;
import com.huateng.network.callback.RequestCallback;
import com.huateng.network.error.ExceptionHandle;
import com.tools.utils.GsonUtils;
import com.tools.utils.RxActivityUtils;

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
        if (!TextUtils.isEmpty(msg)) {
            try {
                ErrorMsgBean errorMsgBean = GsonUtils.fromJson(msg, ErrorMsgBean.class);
                if (errorMsgBean != null) {
                    error(code, errorMsgBean.getError_message());
                }
            } catch (Exception e) {
                error(code, msg);
            }

        }

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
        if (TextUtils.isEmpty(resp)) {
            response(null);
            return;
        }
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T t = GsonUtils.fromJson(resp, type);
        end();
        response(t);
    }

    public abstract void response(T resp);

    public abstract void error(String code, String msg);

    public void end() {

    }

}
