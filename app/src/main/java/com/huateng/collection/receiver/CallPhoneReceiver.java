package com.huateng.collection.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huateng.collection.event.BusEvent;
import com.huateng.collection.event.EventEnv;
import com.huateng.collection.utils.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by shanyong on 2019/4/9.
 */

public class CallPhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //去电监听
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            Logger.i("call OUT:" + phoneNumber);
            EventEnv eventEnv=new EventEnv(BusEvent.START_CALL_RECORD);
            eventEnv.put("phoneNumber",phoneNumber);
            EventBus.getDefault().post(eventEnv);
        }
    }
}