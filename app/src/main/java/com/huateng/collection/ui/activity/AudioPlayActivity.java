package com.huateng.collection.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.tools.DateUtils;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zr.lib_audio.androidaudiorecorder.RecordService;
import com.zr.lib_audio.androidaudiorecorder.Util;
import com.zr.lib_audio.androidaudiorecorder.VisualizerHandler;

import org.apache.poi.ss.formula.functions.T;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

public class AudioPlayActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.timer)
    TextView mTimer;
    @BindView(R.id.restart)
    ImageButton mRestart;
    @BindView(R.id.record)
    ImageButton mRecord;
    @BindView(R.id.play)
    ImageButton mPlay;
    @BindView(R.id.content)
    RelativeLayout mContentLayout;
    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.tv_musicTime)
    TextView mTvMusicTime;
    @BindView(R.id.musicSeekBar)
    SeekBar mMusicSeekBar;
    @BindView(R.id.tv_musicTotal)
    TextView mTvMusicTotal;
    private GLAudioVisualizationView visualizerView;
    private MediaPlayer player;
    private String filePath;
    private VisualizerHandler visualizerHandler;
    private Timer timer;
    private int playerSecondsElapsed;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(com.zr.lib_audio.R.dimen.aar_wave_height)
                .setWavesFooterHeight(com.zr.lib_audio.R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(com.zr.lib_audio.R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(ContextCompat.getColor(this, R.color.accent_color)))
                .setLayerColors(new int[]{ContextCompat.getColor(this, R.color.accent_color)})
                .build();

        mContentLayout.setBackgroundColor(Util.getDarkerColor(ContextCompat.getColor(this, R.color.accent_color)));
        mContentLayout.addView(visualizerView, 0);

        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                finish();
            }
        });

        mMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }


    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio_play;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        filePath = getIntent().getStringExtra("filePath");
    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);
    }


    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            visualizerView.onResume();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        try {
            visualizerView.onPause();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        stopPlaying();

        try {
            visualizerView.release();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private void stopPlaying() {
        mStatus.setText("");
        mStatus.setVisibility(View.INVISIBLE);
        mRecord.setImageResource(com.zr.lib_audio.R.drawable.iv_audio_stop);
        //   playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (player != null) {
            try {
                player.stop();
                player.reset();
            } catch (Exception e) {
            }
        }

        stopTimer();
    }


    private void startPlaying() {
        mRecord.setImageResource(com.zr.lib_audio.R.drawable.iv_audio_start);
        try {
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player));
            visualizerView.post(new Runnable() {
                @Override
                public void run() {
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            stopPlaying();
                        }
                    });
                }
            });

            mTimer.setText("00:00");
            mStatus.setText(com.zr.lib_audio.R.string.aar_playing);
            mStatus.setVisibility(View.VISIBLE);
            // playView.setImageResource(R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying() && !RecordService.isRecording();
        } catch (Exception e) {
            return false;
        }
    }


    public void toggleRecording(View v) {

        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }


    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }


    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    playerSecondsElapsed++;
                    if (player != null) {
                        String s = DateUtils.timeParse(player.getCurrentPosition());
                        mTimer.setText(s);
                        mTvMusicTime.setText(s);
                        mMusicSeekBar.setProgress(player.getCurrentPosition());
                        mMusicSeekBar.setMax(player.getDuration());
                        mTvMusicTotal.setText(DateUtils.timeParse(player.getDuration()));
                    }
                }
            }
        });
    }
}
