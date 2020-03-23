package com.huateng.collection.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.FileData;
import com.luck.picture.lib.entity.LocalMedia;
import com.orm.SugarRecord;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class Utils {


    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列，并获取Item宽度
     */
    public static int getImageItemWidth(Activity activity) {
        //int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenWidth = (int) activity.getResources().getDimension(R.dimen.reference_width);
        int densityDpi = activity.getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        cols = cols < 3 ? 3 : cols;
        int columnSpace = (int) (2 * activity.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }


    /**
     * 获取手机大小（分辨率）
     */
    public static DisplayMetrics getScreenPix(Activity activity) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }

    /**
     * 刷新媒体文件
     */
    public static void scanMediaFile(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * galleryScan
     */
    public static void scanAllMedia(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * 扫描媒体文件
     */
    public static void scanDirMedia(Context context, File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            for (File child : children) {
                scanDirMedia(context, child);
            }
            return;
        }
        String type = "";
        type = Utils.getMimeType(file);
        if (type.startsWith("image/") || type.startsWith("audio/")) {
            scanMediaFile(context, file);
        }
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            Log.i("mimeType", "---->" + type);
            return type;
        }
        return "file/*";
    }


    /**
     * 删除目录
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteDir(Context context, File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteMediaFile(context, file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(context, file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除android 媒体文件
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean deleteMediaFile(Context context, File file) {
        boolean deleted = false;
        if (file.isFile()) {
            String filePath = file.getPath();
            String name = file.getName();

            //删除多媒体数据库中的数据
            if (name.endsWith(".mp3") || name.endsWith(".amr") || name.endsWith(".m4a")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac") || name.endsWith(".wav")) {
                //音频文件
                int res = context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);

                if (res > 0) {
                    deleted = file.delete();
                } else {
//                    deleted = false;
                    //强制删除
                    deleted = file.delete();
                    scanMediaFile(context, file);
                }

            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                    || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")) {
                //图片文件
                deleted = deleteImage(context, filePath);
            } else if (name.endsWith(".mp4") || name.endsWith(".avi")
                    || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")) {
                //视频文件
                int res = context.getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);
                if (res > 0) {
                    deleted = file.delete();
                } else {
//                    deleted = false;
                    //强制删除
                    deleted = file.delete();
                    scanMediaFile(context, file);
                }

            } else {
                deleted = file.delete();
            }
        }
        return deleted;
    }


    /**
     * 删除android 媒体文件
     *
     * @param context
     * @param file
     * @return
     */
    public static boolean deleteAudioFile(Context context, File file) {
        boolean deleted = false;
        if (file.isFile()) {
            String filePath = file.getPath();
            String name = file.getName();

            //删除多媒体数据库中的数据
            if (name.endsWith(".mp3") || name.endsWith(".amr") || name.endsWith(".m4a")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac") || name.endsWith(".mp4") || name.endsWith(".wav")) {
                //音频文件
                int res = context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Media.DATA + "= \"" + filePath + "\"",
                        null);

                if (res > 0) {
                    deleted = file.delete();
                } else {
                    deleted = false;
                }

            } else {
                deleted = file.delete();
            }
        }
        return deleted;
    }


    public static boolean deleteImage(Context context, String imgPath) {
        ContentResolver resolver = context.getContentResolver();
        boolean result;

        Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?",
                new String[]{imgPath}, null);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            int count = context.getContentResolver().delete(uri, null, null);
            result = count == 1;
        } else {
            File file = new File(imgPath);
            result = file.delete();
        }
        return result;
    }


    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 因为android 中存在文件被删除,
     * 媒体库未刷新导致已删除文件还可以在媒体库看到预览的情况
     * 做此操作对文件进行同步处理
     */
    public static void mediaFileSync(Context context, String dir, List<FileData> fileDatas, List<LocalMedia> localMedias, String fileType, String currentAddrId, String currentCaseId) {
        //对文件系统存在 数据库不存在  媒体库不存在文件进行同步
        File audioDir = new File(dir);
        File[] files = audioDir.listFiles();
        if (null != files && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                boolean isDBExist = false;
                File file = files[i];

                //处理文件与数据库间的同步
                for (int j = 0; j < fileDatas.size(); j++) {
                    FileData fileData = fileDatas.get(j);
                    String fileName = fileData.getFileName();
                    if (fileName.equals(file.getName())) {
                        isDBExist = true;
                    }
                }
                //文件在数据库中不存在则删除
                if (!isDBExist) {
                    //保存数据到数据库
                    String fileName = file.getName();
                    FileData fileData = new FileData();
                    fileData.setFileName(fileName);
                    fileData.setBizId(currentAddrId);
                    fileData.setCaseId(currentCaseId);
                    fileData.setRealPath(file.getAbsolutePath());
                    fileData.setType(fileType);
                    fileData.setFileId(fileName.substring(0, fileName.indexOf(".")));
                    fileData.setCreateTime(file.lastModified());
                    fileData.setUserId(Perference.getUserId());
                    fileData.setExist(true);
                    SugarRecord.save(fileData);
                }


                boolean isMediaExist = false;
                //TODO 处理文件与媒体库间的同步
                //处理文件与数据库间的同步
                for (int j = 0; j < localMedias.size(); j++) {
                    LocalMedia localMedia = localMedias.get(j);
                    String path = localMedia.getPath();
                    if (path.equals(file.getAbsolutePath())) {
                        isMediaExist = true;
                    }
                }

                if (!isMediaExist) {
                    Utils.scanMediaFile(context, file);
                }

            }
        }

        //对媒体库存在 文件系统不存在的 媒体库缓存进行处理
        if (null != localMedias && localMedias.size() > 0) {
            for (int j = 0; j < localMedias.size(); j++) {
                LocalMedia localMedia = localMedias.get(j);
                String path = localMedia.getPath();
                if (!TextUtils.isEmpty(path)) {
                    boolean isFileExist = false;
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (path.equals(file.getAbsolutePath())) {
                            isFileExist = true;
                        }
                    }
                    //文件不存在 媒体缩略存在 刷新媒体库
                    if (!isFileExist) {
                        MediaScannerConnection.scanFile(context, new String[]{Environment
                                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
                    }
                }
            }
        }
    }


    /**
     * 因为android 中存在文件被删除,
     * 媒体库未刷新导致已删除文件还可以在媒体库看到预览的情况
     * 做此操作对文件进行同步处理
     */
    public static void mediaFileSync(Context context, String dir, List<FileData> fileDatas, String fileType, String bizId) {

        //对文件系统存在 数据库不存在  媒体库不存在文件进行同步
        File audioDir = new File(dir);
        File[] files = audioDir.listFiles();
        if (null != files && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                boolean isDBExist = false;
                File file = files[i];

                //处理文件与数据库间的同步
                for (int j = 0; j < fileDatas.size(); j++) {
                    FileData fileData = fileDatas.get(j);
                    String fileName = fileData.getFileName();
                    if (fileName.equals(file.getName())) {
                        isDBExist = true;
                    }
                }
                //文件在数据库中不存在则删除
                if (!isDBExist) {
                    //保存数据到数据库
                    String fileName = file.getName();
                    FileData fileData = new FileData();
                    fileData.setFileName(fileName);
                    fileData.setBizId(bizId);
                    fileData.setRealPath(file.getAbsolutePath());
                    fileData.setType(fileType);
                    fileData.setFileId(fileName.substring(0, fileName.indexOf(".")));
                    fileData.setCreateTime(file.lastModified());
                    fileData.setUserId(Perference.getUserId());
                    fileData.setExist(true);
                    SugarRecord.save(fileData);
                }

                Utils.scanMediaFile(context, file);
            }
        }

    }


    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    public static Uri parUri(Context context, File cameraFile) {
        Uri imageUri;
        String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(context, authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        return imageUri;
    }


}
