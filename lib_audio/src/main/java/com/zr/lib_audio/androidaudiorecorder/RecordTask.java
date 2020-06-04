package com.zr.lib_audio.androidaudiorecorder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

/**
 * Created by shanyong on 2019/1/31.
 * 处理录音任务
 */

public class RecordTask implements PullTransport.OnAudioChunkPulledListener {

    protected int mElapsedSeconds = 0;

    //录音
    protected Recorder recorder;
    //是否在录音
    protected boolean isRecording;

    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;

    private static Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;

    private RecordService mContext;
    private RecordStatusDelegate mDelegate;


    public void init(RecordService service, Intent intent, RecordStatusDelegate delegate) {
        this.mContext = service;
        this.mDelegate = delegate;
        filePath = intent.getStringExtra(AndroidAudioRecorder.EXTRA_FILE_PATH);
        source = (AudioSource) intent.getSerializableExtra(AndroidAudioRecorder.EXTRA_SOURCE);
        channel = (AudioChannel) intent.getSerializableExtra(AndroidAudioRecorder.EXTRA_CHANNEL);
        sampleRate = (AudioSampleRate) intent.getSerializableExtra(AndroidAudioRecorder.EXTRA_SAMPLE_RATE);

        Log.e("nb",source+":"+channel+":"+sampleRate+":"+filePath);
    }

    //开始录音或者恢复录音
    protected void resumeRecording() {
        isRecording = true;
        Log.e("nb","filePath:"+filePath);
        if (recorder == null) {
            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), this),
                    new File(filePath));
        }
        recorder.resumeRecording();

        startTimer();
    }

    //启动计时器
    protected void startTimer() {
        mTimer = new Timer();
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {
                mElapsedSeconds++;
                if (mDelegate != null) {
                    mDelegate.onTimerChanged(mElapsedSeconds);
                }
                mContext.updateNotification(mElapsedSeconds);
            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 1000, 1000);
    }

    //结束录音
    protected void stopRecording(Context context) {
        if (recorder != null) {
            recorder.stopRecording();
            if (mDelegate != null) {
                mDelegate.onStop();
            }
            recorder = null;
        }
        stopTimer();
        context.stopService(new Intent(context, RecordService.class));
    }

    //结束计时器
    protected static void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    //暂停录音
    protected void pauseRecording() {
        isRecording = false;
        if (recorder != null) {
            recorder.pauseRecording();
            if (mDelegate != null) {
                mDelegate.onPaused();
            }
        }
        stopTimer();
    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        if (mDelegate != null) {
            mDelegate.onAudioChunkPull(amplitude);
        }
    }

    //录音状态监听
    public interface RecordStatusDelegate {
        //开始录音
        void onStarted();

        //恢复录音
        void onResumed();

        //暂停录音
        void onPaused();

        //结束录音
        void onStop();

        void onAudioChunkPull(float amplitude);

        //录音时间变化
        void onTimerChanged(int seconds);
    }

    public void setRecordStatusDelegate(RecordStatusDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getElapsedSeconds() {
        return mElapsedSeconds;
    }
}
