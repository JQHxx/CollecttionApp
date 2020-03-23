package com.tools.model.scaner;

import android.os.Handler;

/**
 * Created by Lenovo on 2017/8/2.
 */

public interface CaptureHanlder {
    void handleDecode(int type, String result);

    Handler getHandler();

    int getX();

    int getY();

    int getCropWidth();

    int getCropHeight();
}
