package com.huateng.collection.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.huateng.collection.BuildConfig;
import com.huateng.fm.app.FmAttributeValues;
import com.huateng.fm.core.app.FmApplication;
import com.huateng.network.NetworkConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;
import com.squareup.leakcanary.RefWatcher;
import com.tools.utils.SDCardUtils;
import com.tools.utils.Utils;
import com.zhy.http.okhttp.intercepter.HttpLoggingInterceptor;

import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.facebook.stetho.Stetho.newInitializerBuilder;

public class MainApplication extends FmApplication {

    private RefWatcher mRefWatcher;
    private static MainApplication application;
    public static String cacheDir;
    public Context mContext = null;

    private static boolean currentCaseOnOperation = false;
    //上传案件操作
    private static boolean onUploadCasesOperation = false;


    @Override
    public void onCreate(Context context) throws Exception {
        application = this;
        mContext = getApplicationContext();

        FmAttributeValues.init(this);
        //工具类库
        Utils.init(this);
        // 如果检测到某个 activity 有内存泄露，LeakCanary 就是自动地显示一个通知
//        mRefWatcher = LeakCanary.install(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        SugarContext.init(this);

        ARouter.init(this);

        Perference.init(this);
        Config.init(this);

        NetworkConfig.C.init(this);


        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }


        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Set up the Http Stack to use. If you omit this or comment it, HurlStack will be
        // used by default
        UploadService.HTTP_STACK = new OkHttpStack(getOkHttpClient());

        // setup backoff multiplier
        UploadService.BACKOFF_MULTIPLIER = 2;


        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内部存储
         */
        if (getApplicationContext().getExternalCacheDir() != null && SDCardUtils.isSDCardEnable()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }

        //fragment
//        Fragmentation.builder()
//                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
//                .stackViewMode(Fragmentation.BUBBLE)
//                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
//                /**
//                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
//                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
//                 */
//                .handleException(new ExceptionHandler() {
//                    @Override
//                    public void onException(Exception e) {
//                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
//                        // Bugtags.sendException(e);
//                    }
//                })
//                .install();

      //  CrashReport.initCrashReport(getApplicationContext(), "144541f06a", true);

    }

    public boolean isCurrentCaseOnOperation() {
        return currentCaseOnOperation;
    }

    public void setCurrentCaseOnOperation(boolean currentCaseOnOperation) {
        MainApplication.currentCaseOnOperation = currentCaseOnOperation;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


//    /**
//     * 获取内存监控
//     *
//     * @param context
//     * @return
//     */
//    public static RefWatcher getRefWatcher(Context context) {
//        MainApplication application = (MainApplication) context.getApplicationContext();
//        return application.mRefWatcher;
//    }


    // 获取ApplicationContext
    public static MainApplication getApplication() {
        return application;
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }


    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

                // you can add your own request interceptors to add authorization headers.
                // do not modify the body or the http method here, as they are set and managed
                // internally by Upload Service, and tinkering with them will result in strange,
                // erroneous and unpredicted behaviors
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder request = chain.request().newBuilder();

                        return chain.proceed(request.build());
                    }
                })

                // open up your Chrome and go to: chrome://inspect
                .addNetworkInterceptor(new StethoInterceptor())

                // if you use HttpLoggingInterceptor, be sure to put it always as the last interceptor
                // in the chain and to not use BODY level logging, otherwise you will get all your
                // file contents in the log. Logging body is suitable only for small requests.
                .addInterceptor(new HttpLoggingInterceptor("Okhttp"))
                .cache(null)
                .build();
    }

}