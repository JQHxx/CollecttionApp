
package com.huateng.collection.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.huateng.collection.app.Config;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.Outbound;
import com.huateng.collection.utils.OutboundManager;
import com.huateng.collection.utils.map.Constants;
import com.huateng.collection.utils.map.LocationHelper;
import com.huateng.collection.utils.map.MapUtil;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tools.utils.StringUtils;
import com.tools.utils.TimeUtils;

/**
 * Created by shanyong 2018/11/29
 *
 * @author sumincy@163.com
 */
public final class LocationService extends Service {

    private int SERVICE_START_DELAYED = 5;

    LocationHelper mLocationHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cancelAutoStartService(this);
        mLocationHelper = new LocationHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mLocationHelper.isStarted()) {
            LocationFakeService.startForeground(this);
            mLocationHelper.start(new Runnable() {
                @Override
                public void run() {
                    TencentLocation location = mLocationHelper.getLastLocation();

                    Outbound lastOutbound = OutboundManager.obtainLastOutbounds();

                    //保存定位 地址
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Config.setLastLocation(latLng);
                    Config.setLastAddress(location.getAddress());

                    //计算两个点的距离小于50米  或者userid不存在 忽略此次定位
                    if (null != lastOutbound && StringUtils.isEmpty(Perference.getUserId())) {
                        if (MapUtil.getDistance(lastOutbound.getLongitude(), lastOutbound.getLatitude(), location.getLongitude(), location.getLatitude()) < Constants.DENOISE_DISTANCE) {
                            Logger.w("忽略");
                            return;
                        }
                    }

                    //保存定位信息到本地
                    Outbound outbound = new Outbound();
                    outbound.setUserId(Perference.getUserId());
                    outbound.setLatitude(location.getLatitude());
                    outbound.setLongitude(location.getLongitude());
                    outbound.setAddress(location.getAddress());
                    outbound.setDate(TimeUtils.getNowTimeString("yyyyMMdd"));
                    outbound.setTime(System.currentTimeMillis());
                    outbound.setEvent(Constants.TIMING_LOCATE);

                    SugarRecord.save(outbound);
                }
            });
            flags = START_STICKY;
            SERVICE_START_DELAYED = 5;
            return super.onStartCommand(intent, flags, startId);
        } else {
            //TODO   重新登录 重置定位时间?
            int ret = super.onStartCommand(intent, flags, startId);
            stopSelf();
            SERVICE_START_DELAYED += SERVICE_START_DELAYED;
            return ret;
        }
    }

    /**
     * service停掉后自动启动应用
     *
     * @param context
     * @param delayed 延后启动的时间，单位为秒
     */
    private static void startServiceAfterClosed(Context context, int delayed) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayed * 1000, getOperation(context));
    }

    public static void cancelAutoStartService(Context context) {
        AlarmManager alarm = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getOperation(context));
    }

    private static PendingIntent getOperation(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        PendingIntent operation = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return operation;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationHelper.stop();

        if (StringUtils.isNotEmpty(Perference.getUserId())) {
            startServiceAfterClosed(this, SERVICE_START_DELAYED);//5s后重启
        }
    }

}
