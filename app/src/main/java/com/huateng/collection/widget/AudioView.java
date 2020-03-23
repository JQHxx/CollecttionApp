package com.huateng.collection.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huateng.collection.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioView extends LinearLayout implements View.OnClickListener {
    @BindView(R.id.iv_option)
    ImageView ivOption;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tv_currentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tv_totalTime)
    TextView tvTotalTime;
    @BindView(R.id.player_layout)
    LinearLayout playerLayout;

    private static final String TAG = AudioView.class.getSimpleName();
    private static final int AUDIO_PROGRESS_UPDATE_TIME = 100;
    private Handler progressUpdateHandler;
    private int state;
    private final int INIT = 0, READY = 1, PLAYING = 2, PAUSED = 3;
    private Context context;
    private View root;
    private AudioPlayer player;
    private String audioPath;
    private Uri uri;

    public AudioView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private AudioView init() {
        root = LayoutInflater.from(context).inflate(R.layout.csv_audio_view, null);
        setOrientation(VERTICAL);
        addView(root);
        ButterKnife.bind(this, root);
        ivOption.setOnClickListener(this);
        ivStop.setOnClickListener(this);
        progressUpdateHandler = new Handler();
        return this;
    }

    public void useAudioPlayer(AudioPlayer player, String audioPath, Uri uri) {
        this.audioPath = audioPath;
        this.uri = uri;
        this.player = player;
        state = INIT;
    }

    public void useAudioPlayer(AudioPlayer player, String audioPath) {
        if (audioPath == null || TextUtils.isEmpty(audioPath)) {
            throw new IllegalArgumentException("audioPath cannot be empty or empty");
        }
        useAudioPlayer(player, audioPath, null);
    }

    public void useAudioPlayer(AudioPlayer player, Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        useAudioPlayer(player, null, uri);
    }

    private boolean enable;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

//
//    @OnClick(R.id.iv_option)
//    public void onOptionClicked(){
//
//    }

    @Override
    public void onClick(View v) {
        setEnable(true);
        if (!enable) {
            return;
        }
        if (v.getId() == R.id.iv_option) {
            if (playListener != null) {
                playListener.onPlayStarted();
            }
            switch (state) {
                case INIT:
                    completionHandle();
                    toReadyState();
                    toPlayingState();
                    break;
                case READY:
                    break;
                case PLAYING:
                    toPausedState();
                    break;
                case PAUSED:
                    toPlayingState();
                    break;
            }
        } else if (v.getId() == R.id.iv_stop) {
            switch (state) {
                case INIT:
                    break;
                case READY:
                    break;
                case PLAYING:
                case PAUSED:
                    toInitState();
                    break;
            }
        }
    }

    private void toReadyState() {
        player.preparePlayer(context, audioPath, uri);
        initSeekBar();
        state = READY;
    }

    private void toPlayingState() {
        tvCurrentTime.setVisibility(View.VISIBLE);
        ivOption.setImageResource(R.drawable.pause);
        ivStop.setVisibility(View.VISIBLE);
        player.play();
        progressUpdateHandler.postDelayed(progressUpdateRunnable, AUDIO_PROGRESS_UPDATE_TIME);
        state = PLAYING;
    }

    private void toPausedState() {
        tvCurrentTime.setVisibility(View.VISIBLE);
        ivOption.setImageResource(R.drawable.play);
        ivStop.setVisibility(View.VISIBLE);
        player.pause();
        state = PAUSED;
    }

    public void toInitState() {
        state = INIT;
        sbProgress.setProgress(0);
        progressUpdateHandler.removeCallbacks(progressUpdateRunnable);
        updateCurrentTime(0);
        ivOption.setImageResource(R.drawable.play);
        ivStop.setVisibility(View.GONE);
        player.stop();
        setEnable(false);
        tvCurrentTime.setVisibility(View.GONE);
    }


    public interface OnPlayListener {
        public void onPlayStarted();
    }

    private OnPlayListener playListener;

    public void setOnPlayListener(OnPlayListener playListener) {
        this.playListener = playListener;
    }


    private Runnable progressUpdateRunnable = new Runnable() {
        public void run() {
            if (sbProgress == null) {
                return;
            }
            if (progressUpdateHandler != null && player.isPlaying()) {
                sbProgress.setProgress((int) player.getCurrentPosition());
                int currentTime = player.getCurrentPosition();
                updateCurrentTime(currentTime);
                progressUpdateHandler.postDelayed(this, AUDIO_PROGRESS_UPDATE_TIME);
            } else {
            }
        }
    };

    private void updateCurrentTime(int currentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));
        tvCurrentTime.setText(sb);
    }

    private void completionHandle() {
        player.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                toInitState();
            }
        });
    }

    private void initSeekBar() {
        long duration = player.getAudioDuration();
        sbProgress.setMax((int) duration);
        sbProgress.setProgress(0);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                updateCurrentTime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });
    }


}
