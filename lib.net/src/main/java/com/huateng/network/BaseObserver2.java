package com.huateng.network;

import android.text.TextUtils;
import android.util.Log;

import com.aes_util.AESUtils;
import com.huateng.network.bean.ErrorMsgBean;
import com.huateng.network.bean.ResponseDataBean;
import com.huateng.network.bean.ResponseStructure;
import com.huateng.network.error.ExceptionHandle;
import com.tools.CommonUtils;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author: yichuan
 * Created on: 2020-05-07 14:17
 * description:
 */

public abstract class BaseObserver2<T> implements Observer<ResponseStructure> {

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
            ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
            if(responeThrowable.code == 302) {
                //token过期了
                EventBus.getDefault().post(new EventBean(BusEvent.TOKEN_OVERDUE));
            }

            onError(String.valueOf(responeThrowable.code), responeThrowable.message);
        } else {
            //将Throwable 和 未知错误的status code返回
            onError("EXP", e.getLocalizedMessage());
        }
    }

    @Override
    public void onNext(ResponseStructure structure) {
        if (structure.getScubeHeader() != null) {
            String code = structure.getScubeHeader().getErrorCode();
            if (code.equals(ApiConstants.ERROR_CODE_SUC)) {
                String respData = structure.getScubeBody().getContextData().getData();
                ResponseDataBean responseDataBean = GsonUtils.fromJson(respData, ResponseDataBean.class);

                if(!TextUtils.isEmpty(responseDataBean.getValue())) {
                    String data = AESUtils.decrypt(responseDataBean.getValue(), "aes-nbcbccms@123");
                    if ("{}".equals(data)) {
                        onNextData(null);
                    } else {
                        if(CommonUtils.isJson(data)) {
                           // Logger.i(data);
                            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                            T t = GsonUtils.fromJson(data, type);
                            if(null != t) {
                                onNextData(t);
                            }else {
                                Log.e("nb","null data");
                            }
                        }else {
                          //  Logger.i(data);
                            onNextData((T) data);
                        }


                    }
                }else if(!TextUtils.isEmpty(responseDataBean.getEncode())) {
                    onNextData((T) responseDataBean.getEncode());
                } else {
                    onNextData((T) responseDataBean.getEncode());
                }

            } else {
                String msg = structure.getScubeHeader().getErrorMsg();
                if (!TextUtils.isEmpty(msg)) {
                    try {

                        ErrorMsgBean errorMsgBean = GsonUtils.fromJson(msg, ErrorMsgBean.class);
                        if (errorMsgBean != null) {
                            onError(code, errorMsgBean.getError_message());
                        }
                    } catch (Exception e) {
                        onError(code, msg);
                    }

                }
                //  onError(new ExceptionHandle.ResponeThrowable(structure.getScubeHeader().getErrorMsg(), 1001));
            }
        }

    }

    public abstract void onError(String code, String msg);

    public abstract void onNextData(T t);

}
