package com.huateng.network.upload;

import android.os.Handler;

import com.huateng.network.BaseObserver;
import com.huateng.network.CommonApiService;
import com.huateng.network.NetworkConfig;
import com.huateng.network.StringConverterFactory;
import com.huateng.network.callback.RequestCallback;
import com.huateng.network.error.ExceptionHandle;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

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
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅之前回调回去显示加载动画
                         callback.beforeRequest();
                    }
                }) // 订阅之前操作在主线程
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        throwable.printStackTrace();
                        Logger.e("错误时处理：" + throwable + " --- " + throwable.getLocalizedMessage());

                    }
                })
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new BaseObserver<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        super.onNext(responseBody);
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