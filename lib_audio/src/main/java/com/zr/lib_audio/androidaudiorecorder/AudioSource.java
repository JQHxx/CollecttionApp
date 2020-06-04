package com.zr.lib_audio.androidaudiorecorder;

import android.media.MediaRecorder;

public enum AudioSource {
    MIC,
    CAMCORDER;

    public int getSource(){
        switch (this){
            case CAMCORDER:
                return MediaRecorder.AudioSource.CAMCORDER;
            default:
                return MediaRecorder.AudioSource.MIC;
        }
    }
}