package com.huateng.collection.widget;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.huateng.collection.R;


@SuppressLint("NewApi")
public class RecordButton extends AppCompatImageView {

    private String mFilePath = null;
    private OnFinishedRecordListener onFinishedRecordListener;
    private static final int MIN_INTERVAL_TIME = 2000;// 2s
    private long startTime;

    private MediaRecorder recorder;
    public final static int   MAX_TIME =60;//一分钟

    private int state;
    public static final int READY=0,RECORDING=1;


    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setSavePath(String path) {
        mFilePath = path;
    }


    private void init() {
        createAnimation();
    }

    public int getState(){
        return state;
    }

    public void stopRecord() {
        stopRecording();
        long intervalTime = System.currentTimeMillis() - startTime;
        Toast.makeText(getContext(), "已停止录音", Toast.LENGTH_SHORT).show();
        if (onFinishedRecordListener != null){
            onFinishedRecordListener.onFinishedRecord(mFilePath,(long) intervalTime);
        }
    }
    private AnimationDrawable animationDrawable;

    private void createAnimation(){
         animationDrawable = (AnimationDrawable) getResources().getDrawable(
                      R.drawable.animation_record);
        setImageDrawable(animationDrawable);
        animationDrawable.stop();
    }

    private void startAnimation(){
        if (animationDrawable!=null){
            animationDrawable.start();
        }
    }

    private void stopAnimation(){
        if (animationDrawable!=null){
           animationDrawable.stop();
       }
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setAudioChannels(1);
        recorder.setAudioEncodingBitRate(4000);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        //recorder.setVideoFrameRate(4000);
        recorder.setOutputFile(mFilePath);
        try {
            recorder.prepare();
            recorder.start();
            startTime=System.currentTimeMillis();
            Toast.makeText(getContext(), "开始录音", Toast.LENGTH_SHORT).show();
            state=RECORDING;
        } catch (IOException e) {
            e.printStackTrace();
        }
        startAnimation();
//        animateThread = new AnimateThread();
//        animateThread.start();

    }

    public void stopRecording() {
        stopAnimation();
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        state=READY;
    }


    public void setOnFinishedRecordListener(RecordButton.OnFinishedRecordListener listener) {
        onFinishedRecordListener=listener;
    }
    public interface OnFinishedRecordListener {
         void onFinishedRecord(String audioPath, long time);
    }


}
