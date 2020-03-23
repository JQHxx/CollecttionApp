package com.huateng.collection.utils;

import android.os.Handler;
import android.widget.EditText;

import com.tools.utils.KeyboardUtils;


/**
 * Created by 大灯泡 on 2016/1/14.
 * 显示键盘d工具类
 */
public class InputMethodUtils {
    /** 显示软键盘 */
    public static void showInputMethod(EditText view) {
        KeyboardUtils.showSoftInput(view);
    }


    /** 多少时间后显示软键盘 */
    public static void showInputMethod(final EditText view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtils.showInputMethod(view);
            }
        }, delayMillis);
    }
}
