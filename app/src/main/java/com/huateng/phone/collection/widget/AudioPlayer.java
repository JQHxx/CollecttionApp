package com.huateng.phone.collection.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * @author dengzh
 * @description
 * @time 2016-12-08.
 */
public class AudioPlayer {

    private MediaPlayer mMediaPlayer;
    private static Context context;
    private static AudioPlayer audioPlayer;

    public static AudioPlayer get() {
        if (audioPlayer == null) {
            audioPlayer = new AudioPlayer();
        }
        return audioPlayer;
    }


    public void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    //暂停播放
    public void pause() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void play() {
        mMediaPlayer.start();
    }

    public void preparePlayer(Context context,String audioPath,Uri uri){
        try {
            if (uri == null) {
                mMediaPlayer.setDataSource(audioPath);
            }else{
                mMediaPlayer.setDataSource(context, uri);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getAudioDuration(){
        if (mMediaPlayer!=null){
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    public void seekTo(int mesc){
        if (mMediaPlayer!=null){
             mMediaPlayer.seekTo(mesc);
        }
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
//            mMediaPlayer = null;
//            mProgressUpdateHandler = null;
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
//            mMediaPlayer = null;
//            mProgressUpdateHandler = null;
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        if (mMediaPlayer!=null){
            mMediaPlayer.setOnCompletionListener(listener);
        }
    }
}
