package com.huateng.phone.collection.network;


import android.text.TextUtils;
import android.util.Log;

import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.huateng.network.update.HttpManager;
import com.orhanobut.logger.Logger;
import com.tools.utils.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateManager implements HttpManager {

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, Object> params, @NonNull final Callback callBack) {
        Logger.i("updateManager : %s   params : %s", url, GsonUtils.toJson(params));

      /*  OkHttpUtils.post()
                .url(url)
                .params(params)
                .addHeader("X-GW-APP-ID","1011")
                .addHeader("authorization", NetworkConfig.C.getAuth())
                .build();
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callBack.onError(validateError(e, response));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.i(response);
                        callBack.onResponse(response);
                    }
                });*/
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, Object> params, @NonNull final Callback callBack) {
        Logger.i("updateManager :%s", url);
       /*OkHttpUtils.postString()
                .url(url)
               // .addHeader("authorization", NetworkConfig.C.getAuth())
                .addHeader("X-GW-APP-ID", "1011")
                .content(GsonUtils.toJson(params))
                .mediaType(MediaType.parse("application/json"))
                .build()
               .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callBack.onError(validateError(e, response));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.i("版本更新信息:%s", response);
                        callBack.onResponse(response);
                    }
                });*/

        params.put("appVersion", "Android-"+ApiConstants.APP_VERSION);
        params.put("versionNum", "1.0.0");

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.METHOD_VERSION_UPDATE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<String>() {
                    @Override
                    public void onError(String code, String msg) {
                        if (TextUtils.isEmpty(msg)) {
                            return;
                        }
                    }

                    @Override
                    public void onNextData(String response) {
                        Logger.i("版本更新信息:%s", response);
                        callBack.onResponse(response);
                    }
                });
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(path, fileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        callback.onProgress(progress, total);
                        Log.e("nb","inProgress ");
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e, int id) {
                        callback.onError(validateError(e, response));
                        Log.e("nb","onError ");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        callback.onResponse(response);
                        Log.e("nb","onResponse ");
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        callback.onBefore();
                        Log.e("nb","onBefore ");
                    }
                });

    }
}