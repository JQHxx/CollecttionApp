package com.huateng.collection.widget;

/**
 * Created by Lenovo on 2018/11/20.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huateng.collection.R;

/**
 * 案件选择器
 *
 * @author dengzh
 * @time 2016-11-22.
 */
public class CaseFillReminder extends LinearLayout implements View.OnClickListener {

    private Context context;
    private ViewGroup root;
    private ImageView iv_camera, iv_voice, iv_report;

    public CaseFillReminder(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CaseFillReminder(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CaseFillReminder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.csv_case_fill_reminder, null);
        iv_camera = (ImageView) root.findViewById(R.id.iv_camera);
        iv_voice = (ImageView) root.findViewById(R.id.iv_voice);
        iv_report = (ImageView) root.findViewById(R.id.iv_report);
        iv_camera.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        iv_report.setOnClickListener(this);

        addView(root);
    }

    public void setMindState(State state) {
        if (state == State.DONE) {
            iv_camera.setImageResource(R.drawable.camera_checked);
            iv_voice.setImageResource(R.drawable.voice_checked);
            iv_report.setImageResource(R.drawable.report_checked);
        } else if (state == State.TODO) {
            iv_camera.setImageResource(R.drawable.camera_unchecked);
            iv_voice.setImageResource(R.drawable.voice_unchecked);
            iv_report.setImageResource(R.drawable.report_unchecked);
        }
    }

    public void setCameraState(State state) {
        if (state == State.DONE) {
            iv_camera.setImageResource(R.drawable.camera_checked);
        } else if (state == State.TODO) {
            iv_camera.setImageResource(R.drawable.camera_unchecked);
        }
    }

    public void setVoiceState(State state) {
        if (state == State.DONE) {
            iv_voice.setImageResource(R.drawable.voice_checked);
        } else if (state == State.TODO) {
            iv_voice.setImageResource(R.drawable.voice_unchecked);
        }
    }

    public void setReportState(State state) {
        if (state == State.DONE) {
            iv_report.setImageResource(R.drawable.report_checked);
        } else if (state == State.TODO) {
            iv_report.setImageResource(R.drawable.report_unchecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                if (listener != null) {
                    listener.onCameraPressed(v);
                }
                break;
            case R.id.iv_voice:
                if (listener != null) {
                    listener.onVoicePressed(v);
                }
                break;
            case R.id.iv_report:
                if (listener != null) {
                    listener.onReportPressed(v);
                }
                break;
        }
    }

    public enum State {
        DONE, TODO
    }

    private OnActionListener listener;

    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }

    public interface OnActionListener {
        public void onCameraPressed(View v);

        public void onVoicePressed(View v);

        public void onReportPressed(View v);
    }

}