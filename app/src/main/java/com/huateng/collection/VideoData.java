package com.huateng.collection;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoData {
    private static VideoData sInstance;
    private final ContentResolver mContentResolver;
    private final Context mContext;

    public static VideoData getInstance(Context context) {
        if (sInstance == null)
            sInstance = new VideoData(context);
        return sInstance;
    }


    private VideoData(Context context) {
        mContext = context.getApplicationContext();
        mContentResolver = context.getApplicationContext().getContentResolver();
    }


    public ArrayList<VideoFileInfo> getAllVideo() {
        ArrayList<VideoFileInfo> videos = new ArrayList<VideoFileInfo>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        Cursor cursor = mContentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, MediaStore.Video.Media.DATE_TAKEN+ " DESC");//MediaStore.Video.Media.DATE_TAKEN+ " DESC"

        if (cursor == null) return videos;

        if (cursor.moveToFirst()) {
            do {
                VideoFileInfo fileItem = new VideoFileInfo();
                fileItem.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                File file = new File(fileItem.getFilePath());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                if (duration < 0)
                    duration = 0;
                fileItem.setDuration(duration);

                if (fileItem.getFileName() != null && fileItem.getFileName().endsWith(".mp4")) {
                    videos.add(fileItem);
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }

    public ArrayList<VideoFileInfo> getDraftVideo() {
        ArrayList<VideoFileInfo> videos = new ArrayList<VideoFileInfo>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION
        };
        String outputPath = "/storage/emulated/0/collections/files/093809_20200318000604185948/video";
        String selection = MediaStore.Video.Media.DATA+" like ?";
        String[] selectionArgs = {outputPath + "%"};
        //String[] selectionArgs = {"/storage/emulated/0/draft"+"%"};
        Cursor cursor = mContentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, selection, selectionArgs, MediaStore.Video.Media.DATE_MODIFIED+ " DESC");//MediaStore.Video.Media.DATE_TAKEN+ " DESC"

        if (cursor == null) return videos;

        if (cursor.moveToFirst()) {
            do {
                VideoFileInfo fileItem = new VideoFileInfo();
                fileItem.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                File file = new File(fileItem.getFilePath());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                if (duration < 0)
                    duration = 0;
                fileItem.setDuration(duration);

                if (fileItem.getFileName() != null && fileItem.getFileName().endsWith(".mp4")) {
                    videos.add(fileItem);
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }


    public List<VideoFileInfo> getImages(){
        ArrayList<VideoFileInfo> videos = new ArrayList<VideoFileInfo>();
        String[] mediaColumns = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME
        };
        String outputPath = "/storage/emulated/0/collections/files/093809_20200318000604185948/image";
        String selection = MediaStore.Images.Media.DATA+" like ?";
        String[] selectionArgs = {outputPath + "%"};
        //String[] selectionArgs = {"/storage/emulated/0/draft"+"%"};
        Cursor cursor = mContentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED+ " DESC");//MediaStore.Video.Media.DATE_TAKEN+ " DESC"

        if (cursor == null) return videos;

        if (cursor.moveToFirst()) {
            do {
                VideoFileInfo fileItem = new VideoFileInfo();

                fileItem.setFilePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                File file = new File(fileItem.getFilePath());
                Log.e("nb","path:"+fileItem.getFilePath());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                videos.add(fileItem);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }
}
