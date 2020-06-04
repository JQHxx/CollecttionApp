package com.zr.lib_audio.androidaudiorecorder;

import android.media.AudioFormat;

//MONO单声道，STEREO立体声
public enum AudioChannel {
    STEREO,
    MONO;

    public int getChannel(){
        switch (this){
            case MONO:
                return AudioFormat.CHANNEL_IN_MONO;
            default:
                return AudioFormat.CHANNEL_IN_STEREO;
        }
    }
}