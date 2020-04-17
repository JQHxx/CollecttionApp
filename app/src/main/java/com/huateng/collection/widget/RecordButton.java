package com.huateng.collection.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import com.huateng.collection.R;

import androidx.appcompat.widget.AppCompatImageView;


@SuppressLint("NewApi")
public class RecordButton extends AppCompatImageView {

    private String mFilePath = null;
   // private RecordListener onFinishedRecordListener;
    private static final int MIN_INTERVAL_TIME = 2000;// 2s
    private long startTime;

    public final static int MAX_TIME = 60;//一分钟

    private int state;
    public static final int READY = 0, RECORDING = 1;


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

    private void init() {
        createAnimation();
    }

    public int getState() {
        return state;
    }

    private AnimationDrawable animationDrawable;

    private void createAnimation() {
        animationDrawable = (AnimationDrawable) getResources().getDrawable(
                R.drawable.animation_record);
        setImageDrawable(animationDrawable);
        animationDrawable.stop();
    }

    private void startAnimation() {
        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    private void stopAnimation() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }

    public void startRecording() {

        startAnimation();
        state = RECORDING;

    }

    public void stopRecording() {
        stopAnimation();
        state = READY;

    }

}
