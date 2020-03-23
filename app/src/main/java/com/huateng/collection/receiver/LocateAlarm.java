package com.huateng.collection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huateng.collection.event.BusTag;
import com.huateng.collection.utils.map.Constants;
import com.huateng.collection.utils.rxbus.RxBus;

/**
 * Created by shanyong on 2017/1/23.
 */

public class LocateAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.LOCATE_ACTION)) {
            RxBus.get().post(BusTag.LOCATE_EVENT, Constants.TIMING_LOCATE);
        }
    }
}
