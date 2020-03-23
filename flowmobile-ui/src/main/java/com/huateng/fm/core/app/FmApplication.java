package com.huateng.fm.core.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public abstract class FmApplication extends Application {
	
    private static  FmApplication instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
        instance=this;
		try {
			// 加载用户自定义
			onCreate(this);
		} catch (Exception e) {
			Log.e("START", e.getMessage(), e);
		}
	}
	

    public static FmApplication getInstance(){
    	return instance;
    }

	
	

	public abstract void onCreate(Context context) throws Exception;

}
