package com.huateng.collection.widget;

import android.content.Context;

import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by shanyong on 2018/12/26.
 */

//@GlideModule
//public final class MyAppGlideModule extends AppGlideModule {
//    @Override
//    public void registerComponents(Context context, Registry registry) {
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
//    }
//
//}