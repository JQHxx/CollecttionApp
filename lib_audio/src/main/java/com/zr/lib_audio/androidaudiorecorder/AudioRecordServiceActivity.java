package com.zr.lib_audio.androidaudiorecorder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.flyco.systembar.SystemBarHelper;
import com.orhanobut.logger.Logger;
import com.tools.view.RxToast;
import com.zr.lib_audio.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * Created by shanyong on 2019/1/29.
 * email：sumincy@163.com
 */

public class AudioRecordServiceActivity extends AppCompatActivity {

    private String filePath;
    private int color;
    private boolean autoStart;
    private boolean keepDisplayOn;

    private MediaPlayer player;
    private VisualizerHandler visualizerHandler;

    private Timer timer;
    private MenuItem saveMenuItem;
    private int playerSecondsElapsed;

    private RelativeLayout contentLayout;
    private GLAudioVisualizationView visualizerView;
    private TextView statusView;
    private TextView timerView;
   // private ImageButton restartView;
    private ImageButton recordView;
  //  private ImageButton playView;

    private int recordLength;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aar_activity_audio_recorder2);
        SystemBarHelper.immersiveStatusBar(this, 0);

        if (savedInstanceState != null) {
            filePath = savedInstanceState.getString(AndroidAudioRecorder.EXTRA_FILE_PATH);
            color = savedInstanceState.getInt(AndroidAudioRecorder.EXTRA_COLOR);
            autoStart = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_AUTO_START);
            keepDisplayOn = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON);
        } else {
            intent = getIntent();
            filePath = intent.getStringExtra(AndroidAudioRecorder.EXTRA_FILE_PATH);
            color = intent.getIntExtra(AndroidAudioRecorder.EXTRA_COLOR, Color.BLACK);
            autoStart = intent.getBooleanExtra(AndroidAudioRecorder.EXTRA_AUTO_START, false);
            keepDisplayOn = intent.getBooleanExtra(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON, false);
        }

        if (keepDisplayOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Util.getDarkerColor(color)));
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(this, R.drawable.aar_ic_clear));
        }

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(R.dimen.aar_wave_height)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        contentLayout = (RelativeLayout) findViewById(R.id.content);
        statusView = (TextView) findViewById(R.id.status);
        timerView = (TextView) findViewById(R.id.timer);
        //restartView = (ImageButton) findViewById(R.id.restart);
        recordView = (ImageButton) findViewById(R.id.record);
       // playView = (ImageButton) findViewById(R.id.play);

        //当在有录音任务在进行时 使用旧路径
        if (RecordService.isRecording()) {
            filePath = RecordService.recordTask.getFilePath();
            timerView.setText(Util.formatSeconds(RecordService.recordTask.getElapsedSeconds()));
        }

        contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        contentLayout.addView(visualizerView, 0);
      //  restartView.setVisibility(View.INVISIBLE);
       // playView.setVisibility(View.INVISIBLE);

        if (Util.isBrightColor(color)) {
            ContextCompat.getDrawable(this, R.drawable.aar_ic_clear)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            ContextCompat.getDrawable(this, R.drawable.aar_ic_check)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
        //    restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
         //   playView.setColorFilter(Color.BLACK);
        }
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

        //没在录音时
        if (!RecordService.isRecording()) {
            restartRecording(null);
            setResult(RESULT_CANCELED);
        }

        try {
            visualizerView.release();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(AndroidAudioRecorder.EXTRA_FILE_PATH, filePath);
        outState.putInt(AndroidAudioRecorder.EXTRA_COLOR, color);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aar_audio_recorder, menu);
        saveMenuItem = menu.findItem(R.id.action_save);
        saveMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.aar_ic_check));

        //当servie中在录音时 重新打开activity时 恢复UI状态
        if (RecordService.isRecording()) {
            resumeRecording();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            if (!RecordService.isRecording()) {
                stopRecording();
                finish();
            }

        } else if (i == R.id.action_save) {
            selectAudio();
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectAudio() {
        if(recordLength < 10) {
          RxToast.showToast("录音时长不能小于10秒");
          return;
        }
        stopRecording();
        Intent intent = new Intent();
        Log.e("nb","recordLength:"+recordLength);
        intent.putExtra("duration",recordLength);
        intent.putExtra("path",filePath);
        intent.setData(parUri(new File(filePath)));
        //录音时长
        setResult(RESULT_OK, intent);
        finish();
    }

    public void toggleRecording(View v) {
        stopPlaying();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (RecordService.isRecording()) {
                    pauseRecording();
                } else {

                  resumeRecording();
                }
            }
        });
    }

    /**
     * 超过30分钟通知录音
     * @param delegate
     */




    //启动录音
    public void startRecord(RecordTask.RecordStatusDelegate delegate) {

        RecordService.startRecording(this, intent, delegate);
    }

    //切换录音状态
    public void togglePlaying(View v) {
        pauseRecording();
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

    //更新UI状态 重启录音
    public void restartRecording(View v) {

        if (RecordService.isRecording()) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        }

        visualizerHandler = new VisualizerHandler();
        visualizerView.linkTo(visualizerHandler);
        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        saveMenuItem.setVisible(false);
        statusView.setVisibility(View.INVISIBLE);
      //  restartView.setVisibility(View.INVISIBLE);
      //  playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        playerSecondsElapsed = 0;
        //取消通知
        RecordService.restart(this);
    }

    //恢复录音UI状态
    private void resumeRecording() {

        saveMenuItem.setVisible(false);
        statusView.setText(R.string.aar_recording);
        statusView.setVisibility(View.VISIBLE);
       // restartView.setVisibility(View.INVISIBLE);
       // playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_pause);
       // playView.setImageResource(R.drawable.aar_ic_play);

        visualizerHandler = new VisualizerHandler();
        visualizerView.linkTo(visualizerHandler);

        //录音任务不存在时
        if (!RecordService.isInit()) {
            Logger.i("start new record task");
            timerView.setText("00:00:00");
            startRecord(recordStatusDelegate);
        } else {

            //判断后台是否在录音  在录音重新设置监听，否者恢复录音
            if (RecordService.isRecording()) {
                Logger.i("is recording, reset record delegate");
                RecordService.setRecordStatusDelegate(recordStatusDelegate);
            } else {
                Logger.i("resume record form paused task");
                RecordService.resumeRecording();
            }
        }

    }

    //录音状态回调
    public RecordTask.RecordStatusDelegate recordStatusDelegate = new RecordTask.RecordStatusDelegate() {
        @Override
        public void onStarted() {

        }

        @Override
        public void onResumed() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onAudioChunkPull(float amplitude) {
            visualizerHandler.onDataReceived(amplitude);
        }

        @Override
        public void onTimerChanged(final int seconds) {
            recordLength = seconds;
            Log.e("nb","playerSecondsElapsed:"+recordLength);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerView.setText(Util.formatSeconds(seconds));

                    /**
                     * 录音超过30分钟停止录音
                     */

                    if(playerSecondsElapsed > 30*60) {
                        toggleRecording(null);
                        RxToast.showToast("录音时长不能超过30分钟");
                    }
                }
            });
        }
    };

    private void pauseRecording() {
        if (!isFinishing()) {
            saveMenuItem.setVisible(true);
        }
        statusView.setText(R.string.aar_paused);
        statusView.setVisibility(View.VISIBLE);
       // restartView.setVisibility(View.VISIBLE);
      //  playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
      //  playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        RecordService.pauseRecording();
    }

    //停止录音
    private void stopRecording() {
        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        RecordService.stopRecording(this);
    }

    private void startPlaying() {
        try {
            stopRecording();
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

            timerView.setText("00:00:00");
            statusView.setText(R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
           // playView.setImageResource(R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
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

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying() && !RecordService.isRecording();
        } catch (Exception e) {
            return false;
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

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));

                }
            }
        });
    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(this, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }
}
