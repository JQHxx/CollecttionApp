package com.huateng.phone.collection.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.huateng.phone.collection.app.MainApplication;
import com.huateng.fm.util.FmValueUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Locale;

public class FileUtil extends BaseFileUtil {


    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    public static String uploadFile(String uploadUrl, String filePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
            // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"IMAGEFILE\"; filename=\""
                    + filePath.substring(filePath.lastIndexOf("/") + 1)
                    + "\""
                    + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            dos.close();
            is.close();


            return result;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    public static void compressSaveImage(String filePath, File tempFile, Context context) throws IOException {
        File file = new File(filePath);
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "照片保存失败!",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        Bitmap bitmap = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = new FileInputStream(tempFile.getAbsolutePath());
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();
        final int REQUIRED_SIZE = 100;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 3;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = scale;
        fis = new FileInputStream(tempFile.getAbsolutePath());
        bitmap = BitmapFactory.decodeStream(fis, null, op);
        fis.close();
        // 保存压缩图片 替换临时图片
        FileOutputStream out = new FileOutputStream(file);
        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
            out.flush();
            out.close();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }


    /**
     * @param context
     * @param fileName
     * @return
     * @Title: readContentFromAssets
     * @Description: 从Assets中获取文件内容
     * @author scofieldandroid@gmail.com
     * @returnType String
     */
    public static String readContentFromAssets(Context context, String fileName) {

        String content = null;

        InputStream inStream = null;
        try {
            inStream = context.getAssets().open(fileName);
            content = new String(FileUtil.readStream(inStream));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * @param fileName
     * @param context
     * @return
     * @throws Exception
     * @Title: readFileFromPhone
     * @Description: 读取手机自带存储设备上的文件内容
     * @author scofieldandroid@gmail.com
     * @returnType byte[]
     */
    public static String readContentFromPhone(String fileName, Context context) throws Exception {

        File file = new File(context.getFilesDir(), fileName);

        if (file.exists()) {
            FileInputStream inStream = context.openFileInput(file.getName());
            return new String(FileUtil.readStream(inStream));
        } else {
            return null;
        }
    }

    /**
     * @param filename
     * @param context
     * @return
     * @Title: readFile
     * @Description: 如果SDCard存在，则在SDCard中读取文件，如果读取不到，则到手机中读取
     * 如果SDCard不存在，则在手机自带存储中读取文件
     * @author scofieldandroid@gmail.com
     * @returnType byte[]
     */
    public static byte[] readFile(String filename, Context context) {
        try {
            if (isRemovedSDCard) {
                return readFileFromPhone(filename, context);
            } else {
                byte[] byteContent = getFileFromSDCard(filename);
                return byteContent == null ? readFileFromPhone(filename, context) : byteContent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName
     * @param byteContent
     * @param context
     * @param mode
     * @throws Exception
     * @Title: saveFile
     * @Description: <pre>
     * 1-如果SDCard存在，则在SDCard中存储文件，如果存储不成功，则在手机自带存储中存储文件。
     * 2-如果SDCard不存在，则在手机自带存储中存储文件
     * </pre>
     * @author scofieldandroid@gmail.com
     * @returnType void
     */
    public static void saveFile(String fileName, byte[] byteContent, Context context, int mode) {
        try {
            if (isRemovedSDCard) {
                saveToPhone(fileName, byteContent, context, mode);
            } else {
                boolean isSuccess = saveToSDCard(fileName, byteContent);
                if (!isSuccess) {
                    saveToPhone(fileName, byteContent, context, mode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取临时文件夹的路径
     */
    public String getTempFolderPath() {
        return "";
    }

    /**
     * 根据文件名删除文件
     *
     * @param fileName
     * @author carlos carlosk@163.com
     * @version 创建时间：2012-8-7 下午2:13:52
     */
    public static void deleteFile(String fileName) {
        if (FmValueUtil.isStrEmpty(fileName)) {
            return;
        }
        File file = new File(getFilePath(), fileName);
        if (null != file) {
            file.delete();
        }
    }

    /**
     * 该文件是否存在
     *
     * @param fileName
     * @return
     * @author carlos carlosk@163.com
     * @version 创建时间：2012-8-10 下午2:38:16
     */
    public static boolean isFileExist(String fileName) {
        if (FmValueUtil.isStrEmpty(fileName)) {
            return false;
        }
        File file = new File(getFilePath(), fileName);
        return null != file && file.exists() && file.isFile();
    }

    /**
     * 获取完整的路径
     *
     * @return
     * @author carlos carlosk@163.com
     * @version 创建时间：2012-8-10 上午11:27:02
     */
    public static String getFilePath() {
        // 如果存储卡存在,则使用存储卡.不然就用系统的
        return getBasePath() + File.separator + "download";
    }

    /**
     * 获取存储的根目录
     *
     * @return
     * @author carlos carlosk@163.com
     * @version 创建时间：2012-8-15 下午2:15:28
     */
    public static String getBasePath() {
//		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
//			//去掉包的SDCARD存储路径
//			return Environment.getExternalStorageDirectory() + "";
//		}
        return MainApplication.getInstance().getCacheDir().getPath();
    }

    /**
     * 根据完整路径删除文件
     *
     * @param filePath
     * @author carlos carlosk@163.com
     * @version 创建时间：2013-3-19 下午4:16:53
     */
    public static void deleteFileWithPath(String filePath) {
        if (FmValueUtil.isStrEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (null != file) {
            file.delete();
        }
    }

    /**
     * @param tempFilePath
     * @author carlos carlosk@163.com
     * @version 创建时间：2013-3-20 下午4:20:27
     */
    public static void clearDir(String tempFilePath) {
        File file = new File(tempFilePath);
        // file.delete();
        deleteDir(file);
    }

    /**
     * 删除指定文件夹下的所有文件和文件夹
     *
     * @author carlos carlosk@163.com
     * @version 创建时间：2012-8-13 上午9:47:03
     */
    private static void deleteDir(File dirFile) {
        if (null == dirFile) {
            return;
        }
        File[] files = dirFile.listFiles();
        if (null == files) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDir(file);
                file.delete();
            } else if (file.isFile()) {
                file.delete();
            }
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        //关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long getFileSize(File file) {
        return file.length();
    }

    /**
     * 转换文件大小
     *
     * @param fileLength file
     * @param pattern    匹配模板 "#.00","0.0"...
     * @return 格式化后的大小
     */
    public static String formatFileSize(long fileLength, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        String fileSizeString;
        if (fileLength < 1024) {
//            fileSizeString = df.format((double) fileLength) + "B";
            fileSizeString = "0KB";
        } else if (fileLength < 1048576) {
            fileSizeString = df.format((double) fileLength / 1024) + "KB";
        } else if (fileLength < 1073741824) {
            fileSizeString = df.format((double) fileLength / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileLength / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
