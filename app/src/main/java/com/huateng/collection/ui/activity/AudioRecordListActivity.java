package com.huateng.collection.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.ui.adapter.RecorderAdapter;
import com.huateng.collection.utils.Utils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.widget.RecordButton;
import com.luck.picture.lib.PictureBaseActivity;
import com.luck.picture.lib.PicturePlayAudioFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tools.bean.EventBean;
import com.tools.utils.FileUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.tools.bean.BusEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cafe.adriel.androidaudiorecorder.AudioChannel;
import cafe.adriel.androidaudiorecorder.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.AudioSource;
import cafe.adriel.androidaudiorecorder.RecordService;
import cafe.adriel.androidaudiorecorder.RecordTask;
import cafe.adriel.androidaudiorecorder.Util;
import io.reactivex.functions.Consumer;

import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_AUTO_START;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_CHANNEL;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_COLOR;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_FILE_PATH;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_RECORD_TASK_ID;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_RECORD_TASK_NAME;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_SAMPLE_RATE;
import static cafe.adriel.androidaudiorecorder.AndroidAudioRecorder.EXTRA_SOURCE;

public class AudioRecordListActivity extends PictureBaseActivity {
    private List<LocalMedia> fileList = new ArrayList<>();
    private RecorderAdapter adapter;
    private LocalMediaLoader mediaLoader;
    private int chooseMode = PictureMimeType.ofAudio();

    /**********************/
    Intent intent;
    private RxPermissions rxPermissions;
    private String filePath;//录音文件目录
    private int color;
    private boolean autoStart;
    private boolean keepDisplayOn;
    private MediaPlayer player;
    private Timer timer;
    private int playerSecondsElapsed;
    private TextView statusView;
    private TextView timerView;
    private ImageButton restartView;
    private ImageButton recordView;
    private ImageButton playView;
    private RxTitle rxTitle;
    private RecyclerView mRecyclerView;
    private String currentAddrId;
    private String currentCaseId;
    private ImageView mIvImage;
    String tempFilePath;
    private long lastTime = 0;
    private RecordButton mRecordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_audio_recorder);
        initView();
        record();
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString(EXTRA_FILE_PATH);
            color = savedInstanceState.getInt(EXTRA_COLOR);
            autoStart = savedInstanceState.getBoolean(EXTRA_AUTO_START);
            keepDisplayOn = savedInstanceState.getBoolean(EXTRA_KEEP_DISPLAY_ON);
        } else {
            // 录音 初始化录音信息
            startOpenAudio();
        }

        if (keepDisplayOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        //当在有录音任务在进行时 使用旧路径
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, rxTitle);


        if (RecordService.isRecording()) {
            cameraPath = RecordService.recordTask.getFilePath();
            timerView.setText(Util.formatSeconds(RecordService.recordTask.getElapsedSeconds()));


            resumeRecording();
        }

        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);

        initRecyclerview();

        initListener();
    }

    private void initView() {
        statusView = (TextView) findViewById(R.id.status);
        timerView = (TextView) findViewById(R.id.timer);
        restartView = (ImageButton) findViewById(R.id.restart);
        recordView = (ImageButton) findViewById(R.id.record);
        playView = (ImageButton) findViewById(R.id.play);
        rxTitle = findViewById(R.id.rx_title);
        mRecyclerView = findViewById(R.id.recyclerview);
        mIvImage = findViewById(R.id.iv_image);
        mRecordButton = findViewById(R.id.record_button);
        rxPermissions = new RxPermissions(this);
    }

    private void initRecyclerview() {

        mediaLoader = new LocalMediaLoader(this, chooseMode, false, 0);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        adapter = new RecorderAdapter(this, null);
        adapter.setList(fileList);
        adapter.setSelectMax(maxSelectNum);
        mRecyclerView.setAdapter(adapter);


        mediaLoader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                if (folders.size() > 0) {
                    for (LocalMediaFolder folder : folders) {
                        if (null != folder.getPath() && new File(filePath).getAbsolutePath().equals(folder.getPath())) {
                            fileList.clear();
                            fileList.addAll(folder.getImages());
                            adapter.notifyDataSetChanged();

                            List<FileData> audios = SugarRecord.find(
                                    FileData.class, "BIZ_ID=? and USER_ID=? and TYPE=? and EXIST=1", currentAddrId, Perference.getUserId(), FileData.TYPE_AUDIO);
                            //对文件进行同步
                            Utils.mediaFileSync(AudioRecordListActivity.this, filePath, audios, fileList, FileData.TYPE_AUDIO, currentAddrId, currentCaseId);
                            EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_CASE_SUMMARY_REMINDER));

                        }
                    }
                }
            }
        });

    }

    private void initListener() {
        rxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        adapter.setOnItemClickListener(new RecorderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = fileList.get(position);
                if (isPlaying()) {
                    stopPlaying();
                }

                if (!DoubleUtils.isFastDoubleClick()) {
                    PicturePlayAudioFragment fragment = PicturePlayAudioFragment.newInstance(media.getPath());
                    fragment.show(getSupportFragmentManager(), "PicturePlayAudioActivity");

                }

            }
        });


        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopPlaying();
                mRecordButton.stopRecording();
                Util.wait(100, new Runnable() {
                    @Override
                    public void run() {
                        if (RecordService.isRecording()) {
                            //暂停
                            mRecordButton.stopRecording();
                            pauseRecording();
                            selectAudio();
                        } else {
                            //开始录音
                            resumeRecording();
                            mRecordButton.startRecording();
                        }
                    }
                });
            }
        });
    }

    /**
     * start to camera audio
     */
    public void startOpenAudio() {
        rxPermissions.request(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            color = ContextCompat.getColor(AudioRecordListActivity.this, R.color.accent_color);
                            autoStart = false;
                            keepDisplayOn = true;

                            intent = new Intent();
                            intent.putExtra(EXTRA_COLOR, color);
                            intent.putExtra(EXTRA_SOURCE, AudioSource.MIC);
                            intent.putExtra(EXTRA_CHANNEL, AudioChannel.MONO);
                            intent.putExtra(EXTRA_SAMPLE_RATE, AudioSampleRate.HZ_8000);
                            intent.putExtra(EXTRA_AUTO_START, autoStart);
                            intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
                            intent.putExtra(EXTRA_RECORD_TASK_ID, processId);
                            intent.putExtra(EXTRA_RECORD_TASK_NAME, processName);

                        } else {
                            showToast(getString(com.luck.picture.lib.R.string.picture_audio));
                        }
                    }
                });
    }

    private void record() {
        currentCaseId = Perference.getCurrentCaseId();
        currentAddrId = Perference.getCurrentVisitAddressId();
        String currentAddr = Perference.getCurrentVisitAddress();
        String currentCustName = Perference.getCurrentCustName();
        AttachmentProcesser processer = AttachmentProcesser.getInstance(this);
        //录音文件目录
        filePath = processer.getVoicePath(AttachmentProcesser.getProcessId(currentCaseId, currentAddrId));
        tempFilePath = processer.getTempsDir();
        //存在录音任务时判断  如果任务id相同则进入，不同的话提示有任务在进行请结束当前录音任务后再进行新的录音任务
        processId = AttachmentProcesser.getProcessId(currentCaseId, currentAddrId);
        if (RecordService.isRecording()) {
            Intent taskIntent = RecordService.getTaskIntent();
            if (!taskIntent.getStringExtra(EXTRA_RECORD_TASK_ID).equals(processId)) {
                RxToast.showToast(String.format("当前 %s 正在进行录音任务，请在结束该录音任务后再进行录音", taskIntent.getStringExtra(EXTRA_RECORD_TASK_NAME)));
                return;
            }
        }
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(AudioRecordListActivity.this)
                .openCamera(PictureMimeType.ofAudio())
                .theme(R.style.picture_white_style)
                .selectionMode(PictureConfig.MULTIPLE)
                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)
                .compress(false)
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                .previewEggs(true)
                .setOutputCameraPath(tempFilePath)
                .setProcessId(processId)
                .setProcessName(String.format("%s (%s)", currentCustName, currentAddr));
    }


    @Override
    protected void onDestroy() {

        //没在录音时
        if (!RecordService.isRecording()) {
            restartRecording(null);
            setResult(RESULT_CANCELED);
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FILE_PATH, cameraPath);
        outState.putInt(EXTRA_COLOR, color);
        super.onSaveInstanceState(outState);
    }

    /**
     * 录音完成发送消息刷新页面
     */
    private void selectAudio() {

        stopRecording();

        final File file = new File(cameraPath);
        // 生成新拍照片或视频对象
        LocalMedia media = new LocalMedia();
        // media.setPath(cameraPath);
        int duration = PictureMimeType.getLocalVideoDuration(cameraPath);
        // File tempFile = new File(tempFilePath, file.getName());
        File newFile = new File(filePath, file.getName());
        boolean isCopy = false;
        //录音到临时文件
        boolean isTempExist = file.exists();
        if (isTempExist) {
            //将临时文件复制到文件
            isCopy = FileUtils.copyFile(file, newFile);
        }
        media.setPictureType("audio/mpeg");
        media.setDuration(duration);
        media.setMimeType(mimeType);
        media.setName(newFile.getName());
        media.setPath(newFile.getPath());
        if (isCopy) {
            //删除临时文件
            Log.e("nb", "删除本地录音文件");
            Utils.deleteMediaFile(AudioRecordListActivity.this, file);
            //将文件刷新到媒体库
            Utils.scanMediaFile(AudioRecordListActivity.this, newFile);
        }
        fileList.add(media);
        adapter.notifyItemChanged(fileList.size() - 1);
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

    //启动录音
    public void startRecord(RecordTask.RecordStatusDelegate delegate) {
        //启动录音时生成录音文件名称
        File cameraFile = PictureFileUtils.createCameraFile
                (AudioRecordListActivity.this, mimeType,
                        outputCameraPath, processId);

        cameraPath = cameraFile.getAbsolutePath();

        if (RecordService.isRecording()) {
            cameraPath = RecordService.recordTask.getFilePath();
        }

        intent.putExtra(EXTRA_FILE_PATH, cameraPath);
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
                    startPlaying(cameraPath);
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

        // saveMenuItem.setVisible(false);
        statusView.setVisibility(View.INVISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        playerSecondsElapsed = 0;
        //取消通知
        RecordService.restart(this);
    }

    //恢复录音UI状态
    private void resumeRecording() {

        //saveMenuItem.setVisible(false);
        statusView.setText(com.luck.picture.lib.R.string.aar_recording);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_pause);
        playView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_play);

        if (mIvImage != null) {
            mRecordButton.startRecording();
        }

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
            long time = System.currentTimeMillis();
            if (time - lastTime < 300) {
                return;
            }
            Log.e("nb", "amplitude:--->" + amplitude);
            if (amplitude < 30) {
                mIvImage.getDrawable().setLevel(0);
                //   mIvImage.getResources().se
            } else if (amplitude < 35) {
                mIvImage.getDrawable().setLevel(1);
            } else if (amplitude < 40) {
                mIvImage.getDrawable().setLevel(3);
            } else if (amplitude < 45) {
                mIvImage.getDrawable().setLevel(4);
            } else if (amplitude < 50) {
                mIvImage.getDrawable().setLevel(5);
            } else if (amplitude < 55) {
                mIvImage.getDrawable().setLevel(7);
            } else if (amplitude < 60) {
                mIvImage.getDrawable().setLevel(9);
            } else if (amplitude < 65) {
                mIvImage.getDrawable().setLevel(11);
            } else if (amplitude < 70) {
                mIvImage.getDrawable().setLevel(13);
            }
            lastTime = time;
        }

        @Override
        public void onTimerChanged(final int seconds) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerView.setText(Util.formatSeconds(seconds));
                }
            });
        }
    };

    private void pauseRecording() {
        statusView.setText(com.luck.picture.lib.R.string.aar_paused);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_rec);
        playView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_play);

        RecordService.pauseRecording();
    }

    //停止录音
    private void stopRecording() {

        RecordService.stopRecording(this);
    }

    private void startPlaying(String audioPath) {
        try {
            stopRecording();
            player = new MediaPlayer();
            player.setDataSource(audioPath);
            player.prepare();
            player.start();
            timerView.setText("00:00:00");
            statusView.setText(com.luck.picture.lib.R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(com.luck.picture.lib.R.drawable.aar_ic_play);


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

}
