package com.huateng.phone.collection.ui.navigation;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseFragment;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.MapDisplayBean;
import com.huateng.phone.collection.utils.map.Constants;
import com.huateng.phone.collection.utils.map.MapUtil;
import com.huateng.phone.collection.widget.basepopup.popup.MaptypePopup;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.GsonUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yichuan
 * Created on: 2020-05-14 13:46
 * description:
 */
public class MapFragment extends BaseFragment {
    List<MapDisplayBean.DataListBean> list = new ArrayList<>();
    @BindView(R.id.web_view)
    WebView mWebview;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    private MaptypePopup maptypePopup;
    private LatLng currentLocation = new LatLng(31.247241, 121.492696);

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map2;
    }

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        initWebSettings();
        immersiveStatusBar(mRxTitle);
        mWebview.loadUrl("file:///android_asset/map.html");
    }

    private void requestCaseData() {
        Map<String, Object> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("versionNum","1");
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_MAP_DISPLAY, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<List<MapDisplayBean>>() {
                    @Override
                    public void onError(String code, String msg) {

                        //  Log.e("nb",msg);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onNextData(List<MapDisplayBean> mapDisplayBeans) {
                        if (mapDisplayBeans != null && mapDisplayBeans.size() > 0) {
                            mWebview.loadUrl("javascript:clearAllMarker()");
                            for (int i = 0; i < mapDisplayBeans.size(); i++) {
                                MapDisplayBean mapDisplayBean = mapDisplayBeans.get(i);
                                if (mapDisplayBean.getDataList() != null && mapDisplayBean.getDataList().size() > 0) {
                                    for (int j = 0; j < mapDisplayBean.getDataList().size(); j++) {
                                        MapDisplayBean.DataListBean dataListBean = mapDisplayBean.getDataList().get(j);
                                        if ("ok".equals(dataListBean.getResultMsg())) {
                                            dataListBean.setCustNo(mapDisplayBean.getCustNo());
                                            dataListBean.setCustName(mapDisplayBean.getCustName());
                                            if(!TextUtils.isEmpty(dataListBean.getAddress())) {
                                                dataListBean.setAddress(dataListBean.getAddress().replaceAll("\\s*", ""));

                                            }
                                           list.add(dataListBean);
                                        }

                                    }
                                }
                            }
                           /* for (int i = 0; i < list.size(); i++) {
                                Log.e("nb", list.get(i).getCustName() + ":" + list.get(i).getAddress());
                            }*/
                            String s = GsonUtils.toJson(list);
                            mWebview.loadUrl("javascript:setData(" + s + ")");
                        }
                    }
                });

    }


    protected void immersiveStatusBar(View title) {
        SystemBarHelper.immersiveStatusBar((Activity) mContext, 0);
        SystemBarHelper.setHeightAndPadding(mContext, title);
    }

    private void initWebSettings() {
        //去掉横向滚动条

        mWebview.setHorizontalScrollBarEnabled(false);
        //去掉纵向滚动条
        mWebview.setVerticalScrollBarEnabled(false);
        WebSettings webSettings = mWebview.getSettings();

        webSettings.setTextZoom(100);
        // 支持 Js 使用
        webSettings.setJavaScriptEnabled(true);
        // 开启DOM缓存
        webSettings.setDomStorageEnabled(true);
        // 开启数据库缓存
        webSettings.setDatabaseEnabled(true);
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true);
        // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
        webSettings.setAppCacheMaxSize((8 * 1024 * 1024));
        // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
        // webSettings.setAppCachePath();
        // 关闭密码保存提醒功能
        webSettings.setSavePassword(false);
        // 支持缩放
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        //不显示webview缩放按钮
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false);
        }
        // 设置 UserAgent 属性
        webSettings.setUserAgentString("");
        // 允许加载本地 html 文件/false
        webSettings.setAllowFileAccess(true);

        mWebview.addJavascriptInterface(new AndroidJsInterface(), "JsCallNative");
        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder mDialog = new AlertDialog.Builder(mContext);
                mDialog.setTitle("弹窗");
                mDialog.setMessage(message);
                mDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                mDialog.setCancelable(false);
                mDialog.create().show();
                return true;
            }

        });
    }


    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @OnClick(R.id.tv_refresh)
    public void onClick() {
        requestCaseData();
    }


    public class AndroidJsInterface {

        @JavascriptInterface
        public String showToast(String msg) {

            RxToast.showToast(msg);
            Map<String, String> map = new HashMap<>();
            map.put("key", "1234");
            map.put("value", "222");

            return GsonUtils.toJson(map);
        }

        /**
         * 加载地图数据
         *
         * @param msg
         * @return
         */
        @JavascriptInterface
        public String addMark(String msg) {
            requestCaseData();

            return "";
        }

        @JavascriptInterface
        public void jumpAppOtherMap(String lon, String lat, String address) {
            //  Log.e("nb", lon + ":" + lat + ":" + address);
            maptypePopup = new MaptypePopup(getActivity());
            maptypePopup.setOnMaptypeClickListener(new MaptypePopup.onMaptypeClickListener() {
                @Override
                public void onClick(Constants.MapType type) {
                    //    Log.e("nb", "type:" + type);
                    LatLng desLoc = new LatLng(Double.valueOf(lon), Double.valueOf(lat));
                    if (type.equals(Constants.MapType.MAP_TENCENT)) {
                        //  Log.e("nb", "MAP_TENCENT");
                        MapUtil.openTencentMap(mContext, desLoc, address);
                    } else if (type.equals(Constants.MapType.MAP_BAIDU)) {
                        //  Log.e("nb", "MAP_BAIDU");
                        MapUtil.openBaiDuNavi(mContext, currentLocation.getLatitude(), currentLocation.getLongitude(), "", Double.valueOf(lat), Double.valueOf(lon), address);
                    } else if (type.equals(Constants.MapType.MAP_GAODE)) {
                        //   Log.e("nb", "MAP_GAODE");
                        MapUtil.openGaoDeNavi(mContext, 0, 0, null, Double.valueOf(lat), Double.valueOf(lon), address);
                    }
                }
            });

            maptypePopup.showPopupWindow();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        // Log.e("nb", bean.code + "a:c" + bean.getObject());
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.ADD_CASE_TO_WAIT_LIST) {
            requestCaseData();
         //   mPresenter.loadData(com.huateng.collection.app.Constants.REFRESH);
            return;
        }
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

}
