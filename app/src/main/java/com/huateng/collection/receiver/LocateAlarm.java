package com.huateng.collection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huateng.collection.utils.map.Constants;
import com.tools.bean.EventBean;
import com.tools.bean.BusEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by shanyong on 2017/1/23.
 */

public class LocateAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.LOCATE_ACTION)) {
            EventBus.getDefault().post(new EventBean(BusEvent.LOCATE_EVENT, Constants.TIMING_LOCATE));
          //  RxBus.get().send();
        }
    }
}
