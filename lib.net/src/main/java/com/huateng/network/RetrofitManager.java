package com.huateng.network;


import android.support.annotation.NonNull;

import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.huateng.network.api.RequestStructure;
import com.huateng.network.api.ResponseStructure;
import com.huateng.network.cache.CacheManager;
import com.huateng.network.utils.SignUtil;
import com.orhanobut.logger.Logger;
import com.tools.CommonUtils;
import com.tools.utils.GsonUtils;
import com.tools.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * api接口相关的网络请求
 * <p>
 * 请求 响应数据都是json
 **/
public class RetrofitManager {

    //设缓存有效期为两天
    private static final int CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
    private static final String CACHE_CONTROL_NETWORK = "max-age=60";

    private static volatile OkHttpClient sOkHttpClient;

    private CommonApiService commonApiService;

    private static RetrofitManager instance;

    public static RetrofitManager getInstance() {
        if (instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }


    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //网络可用 强制从网络获取数据
            request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
            Response response = chain.proceed(request);

            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置 CACHE_CONTROL_NETWORK
            response.newBuilder().
                    removeHeader("Pragma").
                    removeHeader("Cache-Control")
                    .header("Cache-Control", "public," + CACHE_CONTROL_NETWORK).build();

            return response;

        }
    };

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //请求部分
            Request request = chain.request();

            //headers

            Headers headers = request.headers();
//            Logger.i("headers: %s", headers.toString());

            RequestBody requestBody = request.body();

            Charset charset = Charset.forName("UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append(request.url());
            sb.append("\r\n");

            //将请求体写入sb中
            if (request.method().equals("POST")) {
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                Buffer buf = new Buffer();
                try {
                    requestBody.writeTo(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sb.append(buf.readString(charset));
                buf.close();
            }

            Logger.v(sb.toString());

            //响应部分
            final Response response = chain.proceed(request);
            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                } catch (UnsupportedCharsetException e) {
                    Logger.e("");
                    Logger.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                String key = sb.toString();
                String data = buffer.clone().readString(charset);
                if (CommonUtils.isJson(data)) {
                    Logger.json(data);
                    CacheManager.getInstance().putCache(key, data);
                } else {
                    Logger.w(data);
                    //登录超时判断
                    if (data.contains(ApiConstants.WEB_LOGIN_URL))
                        throw new RuntimeException("Timeout");
                }

            }

            return response;
        }
    };


    private RetrofitManager() {
        String baseURL = NetworkConfig.C.getBaseURL();
        Retrofit retrofit = null;
        try {
            retrofit = build(baseURL);
        } catch (Exception e) {
            e.printStackTrace();
            //报错 将其初始化
            NetworkConfig.C.setApiMode(ApiConstants.API_MODE_RELEASE);
            baseURL = ApiConstants.RELEASE_BASE_URL;
            retrofit = build(baseURL);
        }
        commonApiService = retrofit.create(CommonApiService.class);

    }

    public Retrofit build(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient()).addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit;
    }


    //带token 请求
    public Observable<ResponseStructure> request(String root, String method, Object obj) {
        String uri = ApiConstants.format(root, method);
        String request = generateRequestJson(uri, obj);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request);
        return commonApiService.authRequest(NetworkConfig.C.getAuth(), body);
    }

    //不带token请求
    public Observable<ResponseStructure> loginRequest(Object obj) {
        String uri = ApiConstants.format("appInteface", "login");
        String request = generateRequestJson(uri, obj);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request);
        return commonApiService.request(body);
    }

//    //带token 请求  mock
//    public Observable<ResponseStructure> request(String root, String method, Object obj) {
//        String uri = ApiConstants.format(root, method);
//        String request = generateRequestJson(uri, obj);
//        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request);
//        return commonApiService.mockRequest(NetworkConfig.C.getAuth(), root, method, body);
//    }
//
//    //不带token请求  mock
//    public Observable<ResponseStructure> loginRequest(Object obj) {
//        String uri = ApiConstants.format("appInteface", "login");
//        String request = generateRequestJson(uri, obj);
//        okhttp3.RequestBody body = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), request);
//        return commonApiService.mockRequest(NetworkConfig.C.getAuth(), "appInteface", "login", body);
//    }

    public static String generateRequestJson(String uri, Object obj) {
        RequestStructure baseRequest = RequestConfig.getStructureInstance();
        baseRequest.getQueryTable().getScubeHeader().setTransCode(uri);
        baseRequest.getQueryTable().getScubeBody().getContextData().setData(obj);
        return GsonUtils.toJson(baseRequest);
    }

    // 配置OkHttpClient
    public OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (sOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(NetworkConfig.C.getCtx().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
//                    InputStream[] serverCerStream = new InputStream[]{new Buffer().writeUtf8(NetworkConfig.SERVER_CER).inputStream()};
////                    InputStream cerStream;
////                    InputStream[] serverCerStream = null;
////                    //从文件读取服务端证书
////                    try {
////                        cerStream = MainApplication.getApplication().getAssets().open("server.cer");
////                        serverCerStream = new InputStream[]{cerStream};
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//                    //读取客户端BKS
//                    InputStream bksStream = null;
//                    try {
//                        bksStream = new Buffer().write(new BASE64Decoder().decodeBuffer(NetworkConfig.CLIENT_BKS)).inputStream();
////                      bksStream = MainApplication.getApplication().getAssets().open("client.bks");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    sOkHttpClient = new OkHttpClient.Builder()
//                            .hostnameVerifier(OkHttpsUtils.getHostnameVerifier(new String[]{"202.100.208.194"}))
//                            .sslSocketFactory(OkHttpsUtils.getSSLSocketFactory(serverCerStream, bksStream, NetworkConfig.B_PWD))
//                            .hostnameVerifier(OkHttpsUtils.getAllHostnameVerify())
                            .cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mLoggingInterceptor)
//                            .addInterceptor(new ReceivedCookiesInterceptor())
//                            .addInterceptor(new AddCookiesInterceptor())
//                            .addInterceptor(new AddSignInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS).build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 根据网络状况获取缓存的策略
     *
     * @return
     */
    @NonNull
    private String getCacheControl() {
        return NetworkUtils.isConnected() ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }

}