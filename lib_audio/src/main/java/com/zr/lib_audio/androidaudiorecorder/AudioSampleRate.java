package com.zr.lib_audio.androidaudiorecorder;

public enum AudioSampleRate {
   HZ_48000,
    HZ_44100,
    HZ_32000,
    HZ_22050,
    HZ_16000,
    HZ_11025,
    //低采样率
    HZ_8000;

    public int getSampleRate() {
        return Integer.parseInt(name().replace("HZ_", ""));
    }
}