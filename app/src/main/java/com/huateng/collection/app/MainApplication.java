package com.huateng.collection.app;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.huateng.collection.BuildConfig;
import com.huateng.fm.app.FmAttributeValues;
import com.huateng.fm.core.app.FmApplication;
import com.huateng.network.NetworkConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;
import com.tools.utils.SDCardUtils;
import com.tools.utils.Utils;

import androidx.multidex.MultiDex;

import static com.facebook.stetho.Stetho.newInitializerBuilder;

public class MainApplication extends FmApplication {

   // private RefWatcher mRefWatcher;
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
      //  mRefWatcher = LeakCanary.install(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        SugarContext.init(this);

        //ARouter.init(this);
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
      //  CrashAppUtil.getInstance().init(this, "com.huateng.phone.collection", "1.0.0", 0);//捕获全局异常闪退信息

        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内部存储
         */
        if (getApplicationContext().getExternalCacheDir() != null && SDCardUtils.isSDCardEnable()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }



    // 获取ApplicationContext
    public static MainApplication getApplication() {
        return application;
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

}
