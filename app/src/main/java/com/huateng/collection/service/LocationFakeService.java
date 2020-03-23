

package com.huateng.collection.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.huateng.collection.R;

import androidx.core.app.NotificationCompat;

/**
 * 双Service提高进程优先级,降低被系统杀死机率
 * <p>
 * Created by shanyong 2018/11/29
 *
 * @author sumincy@163.com
 */
public final class LocationFakeService extends Service {
    public static final int NOTIFICATION_ID = 1002;

    public static void startForeground(Service service) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            setForegroundService(service);
            service.startForegroundService(new Intent(service, LocationFakeService.class));
            setForegroundService(service);
        } else {
            service.startService(new Intent(service, LocationFakeService.class));
            service.startForeground(NOTIFICATION_ID, new Notification());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService(this);
        } else {
            startForeground(NOTIFICATION_ID, new Notification());
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final String CHANNEL_ID = "com.huateng.collections.location.channel";

    /**
     * 通过通知启动服务
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static void setForegroundService(Service context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //设定的通知渠道名称
            String channelName = "Location service";
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_LOW;
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription("location service");
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher) //设置通知图标
                .setContentText("正在记录外访位置信息")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        context.startForeground(NOTIFICATION_ID, builder.build());
    }
}
