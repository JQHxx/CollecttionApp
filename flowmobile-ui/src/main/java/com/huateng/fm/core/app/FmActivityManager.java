package com.huateng.fm.core.app;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理
 *
 * @author Decheewar
 */
public class FmActivityManager {
    private static Stack<Activity> activityStack;
    private static FmActivityManager instance;

    public FmActivityManager() {
    }

    public static FmActivityManager getInstance() {
        if (instance == null) {
            instance = new FmActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //移除
    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取栈中最近的activity
     *
     * @return
     */
    public Activity getCurrenActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈中最近activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定activity
     *
     * @param activity
     */
    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;

        }
    }

    /**
     * 结束指定类名activity所有实例
     *
     * @param cls
     */
    public void finishActivityClass(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }

    }

    /**
     * 结束所有activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    public void exitApplication() {

        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
