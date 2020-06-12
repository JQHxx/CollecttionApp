package com.huateng.collection.utils;

/**
 * author: yichuan
 * Created on: 2020/6/10 16:03
 * description:
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tools.utils.FileUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @since 创建时间  2017/1/10.
 * <br><b>捕获异常信息</b></br>
 */
public class CrashAppUtil implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
    @SuppressLint("StaticFieldLeak")
    private static CrashAppUtil INSTANCE;// CrashHandler实例
    private Context mContext;// 程序的Context对象
    private Map<String, String> info = new HashMap<>();// 用来存储设备信息和异常信息

    private String name = "";
    private String versionCode = "";
    //    private int publishProcess = 0;//发布进程
    //    private int loginProcess = 0;//登录进程
    //    private int settingProcess = 0;//设置进程

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashAppUtil() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashAppUtil getInstance() {

        if (INSTANCE == null) {
            synchronized (CrashAppUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashAppUtil();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context, String name, String versionCode, int process) {
        mContext = context;
        this.name = name;
        this.versionCode = versionCode;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
        //        if ("com.circle.youyu:loginandregister".equals(name)) {
        //            loginProcess = process;
        //        } else if ("com.circle.youyu:publishimage".equals(name)) {
        //            publishProcess = process;
        //        } else if ("com.circle.youyu:setinfo".equals(name)) {
        //            settingProcess = process;
        //        }
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    public void uncaughtException(Thread thread, final Throwable ex) {
        try {
            if (!handleException(ex) && mDefaultHandler != null) {
                // 如果自定义的没有处理则让系统默认的异常处理器来处理

                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                Log.i("nb", "错误未处理");
            }
        } catch (Exception e) {
                       e.printStackTrace();
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    @SuppressLint("MissingPermission")
    private boolean handleException(Throwable ex) {
        Log.i("nb","获取到异常:"+ex.getMessage());

        if (ex == null)
            return false;
        //        new Thread() {
        //            public void run() {
        //                Looper.prepare();
        //                ToastUtil.showShortToast(mContext, "出了点小问题，正在加紧修复！");
        //                Looper.loop();
        //            }
        //        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext, versionCode);
        // 发送堆栈信息
        sendMessage(ex);

        if ("com.huateng.phone.collection".equals(name)) {

            //            mContext.sendBroadcast(new Intent("mainThreadClose"));
            //            LogUtil.info("Yang", "闪退");

            //            if (0 != publishProcess) {//关闭发布进程
            //                android.os.Process.killProcess(publishProcess);
            //            }
            //            if (0 != loginProcess) {//关闭登录进程
            //                android.os.Process.killProcess(loginProcess);
            //            }
            //            if (0 != settingProcess) {//关闭设置进程
            //                android.os.Process.killProcess(settingProcess);
            //            }
            android.os.Process.killProcess(Process.myPid());
            System.exit(0);
        } else {
            if (mContext != null) {
                SystemClock.sleep(3000);

                ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                if (activityManager != null){
                    activityManager.killBackgroundProcesses(name);
                    System.exit(0);
                }

            }
        }

        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param context     上下文
     * @param version_num 当前的版本号
     */
    private void collectDeviceInfo(Context context, String version_num) {

        if (context == null) {
            return;
        }

        String phone_type = android.os.Build.MODEL; // 手机型号
        String system_version = android.os.Build.VERSION.RELEASE;//系统版本
        String sdk_version = android.os.Build.VERSION.SDK;//sdk版本

        if (phone_type == null){
            phone_type = "";
        }

        if (system_version == null){
            system_version = "";
        }
        if (sdk_version == null){
            sdk_version = "";
        }

        info.put("phone_type", phone_type);
        info.put("system_version", system_version);
        info.put("sdk_version", sdk_version);
        info.put("code_num", version_num);

        TelephonyManager mTm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (mTm != null) {
            @SuppressLint({"HardwareIds", "MissingPermission"})
            String telPhone = mTm.getLine1Number();
            if (telPhone == null){
                telPhone = "";
            }
            info.put("tel_phone", telPhone);
        }

    }

    /**
     * 将堆栈信息发送到服务器
     *
     * @param ex Throwable
     */
    private void sendMessage(Throwable ex) {

        if (info != null){

            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> entry : info.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key).append("=").append(value).append("\r\n");
            }

            Writer writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            ex.printStackTrace(pw);
            Throwable cause = ex.getCause();
            // 循环着把所有的异常信息写入writer中
            while (cause != null) {
                cause.printStackTrace(pw);
                cause = cause.getCause();
            }
            pw.close();// 记得关闭
            String result = writer.toString();
            sb.append(result);

            String errorMsg = sb.toString();

            //将文件写入到本地
            String filePath = FileUtils.createFilePath("collection_app_crash", System.currentTimeMillis() + "_log.txt");

            boolean flag = FileUtils.writeFile(filePath, errorMsg, false);

            Log.i("nb","flag = " + flag);

            SystemClock.sleep(3000);

        }




    }



}

