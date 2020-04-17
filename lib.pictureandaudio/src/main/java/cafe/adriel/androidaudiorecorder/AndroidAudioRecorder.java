package cafe.adriel.androidaudiorecorder;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;

import androidx.fragment.app.Fragment;

public class AndroidAudioRecorder {

    public static final String EXTRA_COLOR = "color";
    public static final String EXTRA_SOURCE = "source";
    public static final String EXTRA_CHANNEL = "channel";
    public static final String EXTRA_SAMPLE_RATE = "sampleRate";
    public static final String EXTRA_AUTO_START = "autoStart";
    public static final String EXTRA_KEEP_DISPLAY_ON = "keepDisplayOn";
    public static final String EXTRA_FILE_PATH = "filePath";
    public static final String EXTRA_RECORD_TASK_ID = "recordTaskId";
    public static final String EXTRA_RECORD_TASK_NAME = "recordTaskName";


    private Activity activity;
    private Fragment fragment;
    private Service service;

    private String taskId = "recorder";
    private String taskName = "TaskName";
    private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
    private AudioSource source = AudioSource.MIC;
    private AudioChannel channel = AudioChannel.STEREO;
    private AudioSampleRate sampleRate = AudioSampleRate.HZ_8000;
    private int color = Color.parseColor("#546E7A");
    private int requestCode = 0;
    private boolean autoStart = false;
    private boolean keepDisplayOn = false;

    private AndroidAudioRecorder(Activity activity) {
        this.activity = activity;
    }

    private AndroidAudioRecorder(Fragment fragment) {
        this.fragment = fragment;
    }

    private AndroidAudioRecorder(Service service) {
        this.service = service;
    }


    public static AndroidAudioRecorder with(Activity activity) {
        return new AndroidAudioRecorder(activity);
    }

    public static AndroidAudioRecorder with(Fragment fragment) {
        return new AndroidAudioRecorder(fragment);
    }

    public static AndroidAudioRecorder with(Service service) {
        return new AndroidAudioRecorder(service);
    }

    public AndroidAudioRecorder setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public AndroidAudioRecorder setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public AndroidAudioRecorder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public AndroidAudioRecorder setColor(int color) {
        this.color = color;
        return this;
    }

    public AndroidAudioRecorder setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public AndroidAudioRecorder setSource(AudioSource source) {
        this.source = source;
        return this;
    }

    public AndroidAudioRecorder setChannel(AudioChannel channel) {
        this.channel = channel;
        return this;
    }

    public AndroidAudioRecorder setSampleRate(AudioSampleRate sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public AndroidAudioRecorder setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    public AndroidAudioRecorder setKeepDisplayOn(boolean keepDisplayOn) {
        this.keepDisplayOn = keepDisplayOn;
        return this;
    }

    public void record() {
       Intent intent = new Intent(activity, AudioRecordServiceActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        intent.putExtra(EXTRA_RECORD_TASK_ID, taskId);
        intent.putExtra(EXTRA_RECORD_TASK_NAME, taskName);
        activity.startActivityForResult(intent, requestCode);
    }




    public void recordFromFragment() {
        Intent intent = new Intent(fragment.getActivity(), AudioRecorderActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        fragment.startActivityForResult(intent, requestCode);
    }

    public void backgroundRecord() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        intent.putExtra(EXTRA_RECORD_TASK_ID, taskId);
        intent.putExtra(EXTRA_RECORD_TASK_NAME, taskName);

        RecordService.startRecording(service, intent, null);
    }

}
