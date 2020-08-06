package com.huateng.phone.collection.network;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.huateng.network.update.UpdateAppBean;
import com.huateng.network.update.UpdateAppManager;
import com.huateng.network.update.UpdateCallback;
import com.huateng.network.update.listener.ExceptionHandler;
import com.huateng.network.update.listener.IUpdateDialogFragmentListener;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.bean.api.RespVersion;
import com.huateng.phone.collection.utils.DateUtil;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.AppUtils;
import com.tools.utils.GsonUtils;
import com.tools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sumincy on 2018/8/26.
 * 更新帮助类
 */
public class UpdateHelper {
    /**
     * @param context
     * @param isActive true主动调用 显示是否有更新  显示进度条   false 不显示进度条 不显示查询信息
     */
    public static void checkupdate(final Activity context, final boolean isActive) {

        String path;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }

        final String code = AppUtils.getAppVersionName();

        Map<String, Object> params = new HashMap<>();
        params.put("appVersion", "Android-" + ApiConstants.APP_VERSION);
        params.put("versionNum", "1.0.0");


        String updateUrl = String.format("%s%s/%s", NetworkConfig.C.getBaseURL(), ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.METHOD_VERSION_UPDATE);

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity((Activity) context)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateManager())
                //必须设置，更新地址
                .setUpdateUrl(updateUrl)
                //全局异常捕获
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //不显示通知栏进度条
                //                .dismissNotificationProgress()
                //是否忽略版本
                //                .showIgnoreVersion()
                //添加自定义参数，默认version=1.0.0（app的versionName）；
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度，如果是强制更新，则设置无效
                //                .hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.top_3)
                //为按钮，进度条设置颜色。
                .setThemeColor(0xFF39C0E8)
                //设置apk下载路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                    @Override
                    public void onUpdateNotifyDialogCancel(UpdateAppBean updateApp) {
                        //用户点击关闭按钮，取消了更新，如果是下载完，用户取消了安装，则可以在 onActivityResult 监听到。
                    //关闭更新页面 每天提醒一次
                        Perference.set("update_data", DateUtil.getDate(System.currentTimeMillis()));
                    }
                })
                //忽略 参数appkey
                .setIgnoreDefParams(true)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        Log.e("nb", "json-->" + json);
                        RespVersion version = GsonUtils.fromJson(json, RespVersion.class);

                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {

                            String title;

                            String update = "No";
                            if (null != version) {
                                //当前版本
                                if ("-1".equals(code) || TextUtils.isEmpty(version.getVersionCode())) {
                                    update = "No";
                                } else {
                                    String[] oldVersion = code.split(".");

                                    String[] newVersion = version.getVersionCode().split(".");

                                    try {
                                        if (oldVersion.length == 3 && newVersion.length == 3) {
                                            if (Integer.valueOf(oldVersion[0]) < Integer.valueOf(newVersion[0])) {
                                                update = "Yes";
                                            } else if (Integer.valueOf(oldVersion[1]) < Integer.valueOf(newVersion[1])) {
                                                update = "Yes";
                                            } else if (Integer.valueOf(oldVersion[2]) < Integer.valueOf(newVersion[2])) {
                                                update = "Yes";
                                            } else {
                                                update = "No";
                                            }

                                        } else {
                                            update = "No";
                                        }


                                    } catch (Exception e) {
                                        update = "No";
                                    }

                                }
                                //     update = code < Integer.valueOf() ? "Yes" : "No";
                            }
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(update)
                                    //（必须）新版本号，
                                    .setNewVersion(version.getVersionCode())
                                    //（必须）下载地址
                                    //测试下载路径是重定向路径
                                    .setApkFileUrl(version.getAppUrl())

                                    .setUpdateDefDialogTitle("有新版本,下载更新?")
                                    //（必须）更新内容
                                    //测试内容
                                    //                                    .setUpdateLog(dataBean.getDEF2())
                                    //大小，不设置不显示大小，可以不设置
                                    // .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint("0");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                        // EventBusActivityScope.getDefault(context).post(eventEnv);
                        EventBus.getDefault().post(new EventBean(BusEvent.VERSION_UPDATE));
                        // RxBus.get().send();
                        Log.e("nb", "hasNewApp hasNewApp");
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        if (isActive) {
                            // context.showLoading();
                        }
                        Log.e("nb", "onBefore onBefore");
                    }

                    /**
                     * 网络请求之后
                     */
                    @Override
                    public void onAfter() {
                        if (isActive) {
                            //  context.hideLoading();
                        }
                        Log.e("nb", "onAfter onAfter");
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp(String error) {
                         if (isActive) {
                           RxToast.showToast("没有新版本");
                          }
                    }
                });

    }


}
