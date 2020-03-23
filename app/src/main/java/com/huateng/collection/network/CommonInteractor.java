package com.huateng.collection.network;

import com.huateng.collection.bean.api.RespBase;
import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.api.ResponseStructure;
import com.huateng.network.cache.CacheManager;
import com.huateng.network.callback.RequestCallback;
import com.huateng.network.error.ExceptionHandle;
import com.huateng.network.error.NetworkSubscriber;
import com.orhanobut.logger.Logger;
import com.tools.utils.GsonUtils;
import com.tools.utils.NetworkUtils;
import com.tools.utils.StringUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shanyong on 2017/1/12.
 * 用于匹配后端scube协议框架的通用rxjava+retrofit 网络请求与响应处理
 * 请求json报文结构对应:
 *
 * @see com.huateng.network.api.RequestStructure
 * <p>
 * authRequest(RequestStructure)
 * {
 * "queryTable": {
 * "scubeBody": {
 * "contextData": {
 * "data": {
 * <p>
 * },
 * "domainVilidate": ""
 * }
 * },
 * "scubeHeader": {
 * "errorCode": "",
 * "errorMsg": "",
 * "transCode": "",
 * "x-session-token": "collections"
 * }
 * }
 * }
 * <p>
 * 响应json报文结构对应:
 * @see ResponseStructure
 * response(ResponseStructure)
 * {
 * "scubeBody": {
 * "contextData": {
 * "data": "",
 * "domainVilidate": "domainVilidate"
 * }
 * },
 * "scubeHeader": {
 * "errorCode": "SUC",
 * "errorMsg": "",
 * "transCode": "",
 * "x-session-token": "x-session-token"
 * }
 * }
 */

public class CommonInteractor {

    public static Subscription request(final RequestCallback callback, final String root, final String method, final Object object) {
        return RetrofitManager.getInstance()
                .request(root, method, object).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 订阅之前回调回去显示加载动画
                        callback.beforeRequest();
                    }
                }) // 订阅之前操作在主线程
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Logger.e("错误时处理：" + throwable + " --- " + throwable.getLocalizedMessage());
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new NetworkSubscriber<ResponseStructure>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
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
                    public void onNext(ResponseStructure structure) {

                        ResponseStructure.ScubeBodyBean.ContextDataBean dataBean = structure.getScubeBody().getContextData();
                        String data = null;

                        if (null != dataBean) {
                            data = structure.getScubeBody().getContextData().getData();
                        }

                        if (StringUtils.isNotEmpty(data)) {
                            RespBase respBase = GsonUtils.fromJson(data, RespBase.class);
                            if (ApiConstants.RESULT_CODE_SUC.equals(respBase.getResultCode())) {
                                callback.requestSuccess(data);
                            } else if (ApiConstants.RESULT_CODE_EXP.equals(respBase.getResultCode())) {
                                callback.requestError(respBase.getResultCode(), respBase.getResultDesc());
                            }
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        //TODO 取出离线缓存
                        if (!NetworkUtils.isConnected()) {
                            String url = String.format("%s%s/%s.htm", NetworkConfig.C.getBaseURL(), root, method);
                            String uri = ApiConstants.format(root, method);
                            String request = RetrofitManager.generateRequestJson(uri, object);
                            StringBuilder sb = new StringBuilder();
                            String key = sb.append(url).append(request).toString();

                            //KLog.v(key);

                            String reponse = CacheManager.getInstance().getCache(key);
                            if (StringUtils.isNotEmpty(reponse)) {
                                ResponseStructure structure = GsonUtils.fromJson(reponse, ResponseStructure.class);
                                if (structure != null) {
                                    onNext(structure);
                                } else {
                                    callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                                }
                            } else {
                                callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                            }
                            onCompleted();
                        }

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        //接下来就可以根据状态码进行处理...
                        int statusCode = responeThrowable.code;
                        String errorMessage = responeThrowable.message;

                        Logger.i(errorMessage);

                        callback.requestError(String.valueOf(statusCode), errorMessage);

                        switch (statusCode) {
                            case ExceptionHandle.ERROR.SSL_ERROR:

                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:

                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:

                                break;
                            case ExceptionHandle.ERROR.NETWORK_ERROR:

                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:

                                break;
                            default:
                        }
                    }
                });
    }


    public static Subscription requestEntrys(final RequestCallback callback, final String root, final String method, final Object object) {
        return RetrofitManager.getInstance()
                .request(root, method, object).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 订阅之前回调回去显示加载动画
                        callback.beforeRequest();
                    }
                }) // 订阅之前操作在主线程
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Logger.e("错误时处理：" + throwable + " --- " + throwable.getLocalizedMessage());
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new NetworkSubscriber<ResponseStructure>() {
                    @Override
                    public void onCompleted() {
                        callback.requestComplete();
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
                    public void onNext(ResponseStructure structure) {
                        if (structure.getScubeHeader() != null) {
                            String code = structure.getScubeHeader().getErrorCode();
                            if (code.equals(ApiConstants.ERROR_CODE_SUC)) {
                                String data = structure.getScubeBody().getContextData().getData();
                                callback.requestSuccess(data);
                            } else {
                                callback.requestError(code, structure.getScubeHeader().getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        //TODO 取出离线缓存
                        if (!NetworkUtils.isConnected()) {
                            String url = String.format("%s%s/%s.htm", NetworkConfig.C.getBaseURL(), root, method);
                            String uri = ApiConstants.format(root, method);
                            String request = RetrofitManager.generateRequestJson(uri, object);
                            StringBuilder sb = new StringBuilder();
                            String key = sb.append(url).append(request).toString();

                            //KLog.v(key);

                            String reponse = CacheManager.getInstance().getCache(key);
                            if (StringUtils.isNotEmpty(reponse)) {
                                ResponseStructure structure = GsonUtils.fromJson(reponse, ResponseStructure.class);
                                if (structure != null) {
                                    onNext(structure);
                                } else {
                                    callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                                }
                            } else {
                                callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                            }
                            onCompleted();
                        }

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                        //接下来就可以根据状态码进行处理...
                        int statusCode = responeThrowable.code;
                        String errorMessage = responeThrowable.message;

                        Logger.i(errorMessage);

                        callback.requestError(String.valueOf(statusCode), errorMessage);

                        switch (statusCode) {
                            case ExceptionHandle.ERROR.SSL_ERROR:

                                break;
                            case ExceptionHandle.ERROR.UNKNOWN:

                                break;
                            case ExceptionHandle.ERROR.PARSE_ERROR:

                                break;
                            case ExceptionHandle.ERROR.NETWORK_ERROR:

                                break;
                            case ExceptionHandle.ERROR.HTTP_ERROR:

                                break;
                            default:
                        }
                    }
                });
    }


    public static Subscription loginRequest(final RequestCallback callback, final Object object) {
        return RetrofitManager.getInstance()
                .loginRequest(object).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 订阅之前回调回去显示加载动画
                        callback.beforeRequest();
                    }
                }) // 订阅之前操作在主线程
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Logger.e("错误时处理：" + throwable + " --- " + throwable.getLocalizedMessage());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkSubscriber<ResponseStructure>() {
                               @Override
                               public void onCompleted() {
                                   callback.requestComplete();
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
                               public void onNext(ResponseStructure structure) {
                                   if (structure.getScubeHeader() != null) {
                                       String data = structure.getScubeBody().getContextData().getData();
                                       String code = structure.getScubeHeader().getErrorCode();

                                       if (ApiConstants.ERROR_CODE_SUC.equals(code)) {
                                           callback.requestSuccess(data);
                                       } else {
                                           callback.requestError(code, structure.getScubeHeader().getErrorMsg());
                                       }
                                   }

                               }

                               @Override
                               public void onStart() {
                                   super.onStart();
                                   //TODO 取出离线缓存
                                   if (!NetworkUtils.isConnected()) {
                                       String url = String.format("%s%s/%s.htm", NetworkConfig.C.getBaseURL(), "appInteface", "login");
                                       String uri = ApiConstants.format("appInteface", "login");
                                       String request = RetrofitManager.generateRequestJson(uri, object);
                                       StringBuilder sb = new StringBuilder();
                                       String key = sb.append(url).append(request).toString();

                                       //KLog.v(key);

                                       String reponse = CacheManager.getInstance().getCache(key);
                                       if (StringUtils.isNotEmpty(reponse)) {
                                           ResponseStructure structure = GsonUtils.fromJson(reponse, ResponseStructure.class);
                                           if (structure != null) {
                                               onNext(structure);
                                           } else {
                                               callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                                           }
                                       } else {
                                           callback.requestError(ApiConstants.CODE_NO_CACHE, "无缓存");
                                       }
                                       onCompleted();
                                   }

                               }

                               @Override
                               public void onError(ExceptionHandle.ResponeThrowable responeThrowable) {
                                   //接下来就可以根据状态码进行处理...
                                   int statusCode = responeThrowable.code;
                                   String errorMessage = responeThrowable.message;

                                   Logger.i(errorMessage);

                                   callback.requestError(String.valueOf(statusCode), errorMessage);

                                   switch (statusCode) {
                                       case ExceptionHandle.ERROR.SSL_ERROR:

                                           break;
                                       case ExceptionHandle.ERROR.UNKNOWN:

                                           break;
                                       case ExceptionHandle.ERROR.PARSE_ERROR:

                                           break;
                                       case ExceptionHandle.ERROR.NETWORK_ERROR:

                                           break;
                                       case ExceptionHandle.ERROR.HTTP_ERROR:

                                           break;
                                       default:
                                   }
                               }
                           }

                );
    }

}
