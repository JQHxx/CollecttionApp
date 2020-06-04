package com.huateng.collection.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.huateng.collection.app.Perference;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.luck.picture.lib.config.PictureConfig;
import com.tools.utils.FileUtils;
import com.zr.lib_audio.androidaudiorecorder.AndroidAudioRecorder;
import com.zr.lib_audio.androidaudiorecorder.AudioChannel;
import com.zr.lib_audio.androidaudiorecorder.AudioSampleRate;
import com.zr.lib_audio.androidaudiorecorder.AudioSource;
import com.zr.lib_audio.androidaudiorecorder.RecordService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.Nullable;



/**
 * Created by mk on 2017/3/14.
 */
public class PhoneListenerService extends Service {
    //电话管理
    private TelephonyManager tm;
    private myListener listener;
    private String tempFilePath;
    private String audioPath;

    private String currentCustName;
    private String currentCaseId;

    private String phoneNumber;

    private String processId;
    private AttachmentProcesser processer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        listener = new myListener(this);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//第一个参数是监听器！第二个是监听呼叫的状态
        processer = AttachmentProcesser.getInstance(this);
        tempFilePath = processer.getTempsDir();

        super.onCreate();
    }

    private class myListener extends PhoneStateListener {
        private Context mContext;

        public myListener(Context context) {
            this.mContext = context;
        }

        //监听手机状态的变化
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.i("电话状态输出", state + "");
            phoneNumber = incomingNumber;
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态
                    Log.i("电话状态输出", "空闲状态");
                    //停止录音
                    if (Perference.isPrepareCallRecording()) {
                        RecordService.cancelRecording(mContext);
                        //将对应临时文件复制到具体的电话录音文件夹下
                        if (StringUtils.isNotEmpty(processId)) {
                            String caseCallRecordpath = processer.getCallRecordPath(processId, currentCustName, currentCaseId);
                            //创建文件夹
                            FileUtils.createOrExistsDir(caseCallRecordpath);

                            File tempFile = new File(audioPath);
                            File newFile = new File(caseCallRecordpath, tempFile.getName());
                            FileUtils.copyFile(tempFile, newFile);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    Log.i("电话状态输出", "通话状态");

                    Log.i("电话号码", incomingNumber);

                    //准备录音状态下，且通话号码与app内设置的通话号码相同
                    if (Perference.isPrepareCallRecording() && incomingNumber.equals(Perference.getPrepareRecordingPhoneNumber())) {
                        Log.i("电话状态输出", "开始录音");
                        if (!RecordService.isRecording()) {
                            callRecording();
                        } else {
                            RecordService.cancelRecording(mContext);
                            callRecording();
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态

                    break;
            }
        }
    }

    private void callRecording() {
        currentCaseId = Perference.getCurrentCaseId();
        currentCustName = Perference.getCurrentCustName();

        String fileName;
        String processName;

        if (StringUtils.isNotEmpty(currentCaseId, currentCustName)) {
            processId = currentCaseId;
            fileName = String.format("%s_%s_%s", currentCaseId, phoneNumber, Perference.getUserId());
            processName = String.format("%s (%s)", currentCustName, currentCaseId);
        } else {
            //test
            processId = String.valueOf(UUID.randomUUID());
            fileName = String.format("%s_%s", phoneNumber, Perference.getUserId());
            processName = fileName;
        }

        File audioFile = createMediaFile(tempFilePath, fileName);
        audioPath = audioFile.getAbsolutePath();

        //@see Util 设置 PCM
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(audioPath)
//                //taskId
                .setTaskId(processId)
                .setTaskName(processName)
                .setRequestCode(PictureConfig.REQUEST_CAMERA)
                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.MONO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)
                // Start recording
                .backgroundRecord();
    }

    public File createMediaFile(String parentPath, String tempFileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = String.format("%s_%s.mp3", tempFileName, timeStamp);

        File folderDir = new File(parentPath);
        FileUtils.createOrExistsDir(parentPath);

        return new File(folderDir, fileName);
    }

}

