package com.zr.lib_audio.androidaudiorecorder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.zr.lib_audio.R;

import java.lang.ref.WeakReference;

import androidx.core.app.NotificationCompat;


/**
 * Created by shanyong on 2019/1/29.
 * 在service中进行录音
 * email：sumincy@163.com
 */

public class RecordService extends Service {

    private static final String LOG_TAG = "RecordingService";
    private static final String CHANNEL_ID = "record_service";
    private static final String NAMESPACE = ".recorder.recordservice";
    private static final String ACTION_RECORD_SUFFIX = ".recorder.recordservice.action.record";

    private long notificationCreationTimeMillis;
    //通知相关
    private NotificationManager notificationManager;
    private static int NOTIFICATION__ID = 1235;

    //录音任务id
    private static Intent taskIntent;
    public static RecordTask recordTask;

    private static WeakReference<RecordTask.RecordStatusDelegate> recordStatusDelegate;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();

        if (recordStatusDelegate != null) {
            recordTask = getTask(intent, recordStatusDelegate.get());
        } else {
            recordTask = getTask(intent, null);
        }

        recordTask.resumeRecording();

        return START_STICKY;
    }

    private void init() {
        //初始化notifycation
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null == notificationManager.getNotificationChannel(CHANNEL_ID)) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Record Service channel", NotificationManager.IMPORTANCE_LOW);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //录音任务
    RecordTask getTask(Intent intent, RecordTask.RecordStatusDelegate delegate) {
        RecordTask recordTask = new RecordTask();
        taskIntent = intent;
        intent.putExtra(AndroidAudioRecorder.EXTRA_SOURCE,AudioSource.MIC);
        intent.putExtra(AndroidAudioRecorder.EXTRA_CHANNEL,AudioChannel.MONO);
        intent.putExtra(AndroidAudioRecorder.EXTRA_SAMPLE_RATE, AudioSampleRate.HZ_8000);
        recordTask.init(this, intent, delegate);

        return recordTask;
    }

    @Override
    public void onDestroy() {
        if (recordTask != null) {
            recordTask.stopRecording(this);
        }
        super.onDestroy();
    }

    //创建通知
    protected void crateNotification() {
        notificationCreationTimeMillis = System.currentTimeMillis();

        //点击通知进来activity
        Intent pendIntent = new Intent(this, AudioRecordServiceActivity.class);
//        pendIntent.setAction()

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setWhen(notificationCreationTimeMillis)
                .setContentTitle(String.format("%s：%s", getString(R.string.notification_recording), taskIntent.getStringExtra(AndroidAudioRecorder.EXTRA_RECORD_TASK_NAME)))
                .setContentText(Util.formatSeconds(0))
                .setContentIntent(PendingIntent.getBroadcast(this, 0, pendIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_mic_white_36dp)
                .setColor(NotificationCompat.COLOR_DEFAULT)
                .setGroup(CHANNEL_ID)
                .setOngoing(true);

        Notification builtNotification = notification.build();

        notificationManager.notify(NOTIFICATION__ID, builtNotification);
    }

    //更新通知
    protected void updateNotification(int elapsedSeconds) {
        notificationCreationTimeMillis = System.currentTimeMillis();

        //点击通知进来activity
        Intent pendIntent = new Intent(this, AudioRecordServiceActivity.class);
//        pendIntent.setAction()

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setWhen(notificationCreationTimeMillis)
                .setContentTitle(String.format("%s：%s", getString(R.string.notification_recording), taskIntent.getStringExtra(AndroidAudioRecorder.EXTRA_RECORD_TASK_NAME)))
                .setContentText(Util.formatSeconds(elapsedSeconds))
                .setContentIntent(PendingIntent.getBroadcast(this, 0, pendIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_mic_white_36dp)
                .setColor(NotificationCompat.COLOR_DEFAULT)
                .setGroup(CHANNEL_ID)
                .setOngoing(true);

        Notification builtNotification = notification.build();

        notificationManager.notify(NOTIFICATION__ID, builtNotification);
    }

    public static void cancelNotification(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION__ID);
    }

    protected static String getActionRecord() {
        return NAMESPACE + ACTION_RECORD_SUFFIX;
    }

    /**
     * 录音状态
     *
     * @param delegate the delegate instance
     */
    public static void setRecordStatusDelegate(RecordTask.RecordStatusDelegate delegate) {
        if (delegate == null) {
            return;
        }
        recordStatusDelegate = new WeakReference<>(delegate);

        //当录音任务存在时 重设录音监听
        if (null != recordTask) {
            recordTask.setRecordStatusDelegate(recordStatusDelegate.get());
        }
    }

    //是否在录音
    public static boolean isRecording() {
        return recordTask != null && recordTask.isRecording;
    }

    //是否初始化
    public static boolean isInit() {
        return null != recordTask && null != recordTask.recorder;
    }

    //开始录音
    public static void startRecording(Context context, Intent bundleIntent, RecordTask.RecordStatusDelegate delegate) {
        //保存录音状态回调
        RecordService.setRecordStatusDelegate(delegate);

        final Intent intent = new Intent(context, RecordService.class);
        //录音的一些设置
        Bundle extras = bundleIntent.getExtras();
        if (null != extras) {
            intent.putExtras(extras);
        }

        intent.setAction(RecordService.getActionRecord());

        context.startService(intent);
    }

    public static void stopRecording(Context context) {
        if (isInit()) {
            recordTask.stopRecording(context);
            recordTask.mElapsedSeconds = 0;
            cancelNotification(context);
        }
    }

    public static void cancelRecording(Context context) {
        if (isInit()) {
            recordTask.stopRecording(context);
            recordTask.mElapsedSeconds = 0;
            cancelNotification(context);
            recordTask = null;
        }
    }

    public static void restart(Context context) {
        if (isInit()) {
            cancelNotification(context);
            try {
                recordTask.recorder.stopRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }
            recordTask.mElapsedSeconds = 0;
        }
    }

    public static void resumeRecording() {
        recordTask.resumeRecording();
    }

    public static void startRecording() {
        recordTask.resumeRecording();
    }

    public static void pauseRecording() {
        if (isRecording()) {
            recordTask.pauseRecording();
        }
    }

    public static Intent getTaskIntent() {
        return taskIntent;
    }
}
