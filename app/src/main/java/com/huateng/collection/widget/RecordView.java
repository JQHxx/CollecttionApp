package com.huateng.collection.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author dengzh
 * @description
 * @time 2016-12-07.
 */
public class RecordView extends LinearLayout{

    @BindView(R.id.iv_recordState) ImageView ivRecordState;
    @BindView(R.id.chronometer) Chronometer chronometer;
    @BindView(R.id.v_state) LinearLayout vState;
    @BindView(R.id.iv_record) RecordButton ivRecord;
    @BindView(R.id.tv_tip) TextView tvTip;
    private Context context;
    private View root;

    public RecordView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        root = LayoutInflater.from(context).inflate(R.layout.csv_record_view, null);
        ButterKnife.bind(this, root);
        addView(root);
        ivRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ivRecord.getState()==RecordButton.READY){
                      /*  if (onRecordClickListener!=null){
                            onRecordClickListener.onRecordClicked(RecordButton.RECORDING);
                        }
                        ivRecord.startRecording();
                        tvTip.setText("正在录音..");
                        MainApplication.getApplication().setCurrentCaseOnOperation(true);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();*/
                    }else if (ivRecord.getState()==RecordButton.RECORDING){
                      /*  if (onRecordClickListener!=null){
                            onRecordClickListener.onRecordClicked(RecordButton.READY);
                        }
                        MainApplication.getApplication().setCurrentCaseOnOperation(false);
                        ivRecord.stopRecord();
                        chronometer.stop();
                        chronometer.setText("00:00");
                        tvTip.setText("点击录音");*/
                    }
                }
        });
    }

/*    private OnRecordClickListener onRecordClickListener;

    public void setOnRecordClickListener(OnRecordClickListener onRecordClickListener){
        this.onRecordClickListener=onRecordClickListener;
    }*/

  /*  public interface OnRecordClickListener{
        public void onRecordClicked(int state);
    }


    public void setSavePath(String path) {
        ivRecord.setSavePath(path);
    }*/

/*    public void setOnFinishedRecordListener(RecordButton.OnFinishedRecordListener listener) {
        ivRecord.setOnFinishedRecordListener(listener);
    }*/

}
