package com.huateng.network.upload;

import android.os.Handler;


import com.huateng.network.ApiConstants;
import com.huateng.network.CommonApiService;
import com.huateng.network.NetworkConfig;
import com.huateng.network.StringConverterFactory;
import com.huateng.network.callback.RequestCallback;
import com.huateng.network.error.ExceptionHandle;
import com.huateng.network.error.NetworkSubscriber;
import com.orhanobut.logger.Logger;
import com.tools.utils.NetworkUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RetrofitUtil {

    public static Retrofit retrofit;
    private static OkHttpClient okHttpClient = new OkHttpClient();

    private static RetrofitUtil instance;

    private CommonApiService commonApiService;

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            instance = new RetrofitUtil();
        }
        return instance;
    }

    private RetrofitUtil() {
        okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        commonApiService = createService(CommonApiService.class);
    }

    public <T> T createService(Class<T> clazz) {
        if (retrofit == null) {
            synchronized (RetrofitUtil.class) {
                Retrofit.Builder builder = new Retrofit.Builder();
                String baseUrl = NetworkConfig.C.getBaseURL();
                retrofit = builder.baseUrl(baseUrl)
                        .client(okHttpClient)
                        .addConverterFactory(StringConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit.create(clazz);
    }

    public void uploadFile(final RequestCallback callback, String path, int position, Map<String, String> paramMap, Handler mHandler) {
        File file = new File(path);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(file, new DefaultProgressListener(mHandler, position));
        requestBodyMap.put("callback", RequestBody.create(MediaType.parse("text"), "AppFileOperateServiceImpl/upload"));
        requestBodyMap.put("fileName", RequestBody.create(MediaType.parse("text"), file.getName()));
        requestBodyMap.put("file\"; filename=\"" + file.getName(), fileRequestBody);

        //将参数写入请求体
        for (String key : paramMap.keySet()) {
            String value = paramMap.get(key);
            requestBodyMap.put(key, RequestBody.create(MediaType.parse("text"), value));
        }

        commonApiService.uploadFile(NetworkConfig.C.getAuth(), requestBodyMap).
                subscribeOn(Schedulers.io())
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
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new NetworkSubscriber<ResponseBody>() {
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
                    public void onNext(ResponseBody responseBody) {
                        callback.requestSuccess(responseBody.toString());
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

    public static void clearInstance() {
        instance = null;
    }

}