package com.huateng.collection.ui.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.Outbound;
import com.huateng.collection.utils.CommonUtils;
import com.huateng.collection.utils.OutboundManager;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.utils.map.Constants;
import com.huateng.collection.utils.map.MapUtil;
import com.huateng.collection.widget.basepopup.popup.MaptypePopup;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Address2GeoParam;
import com.tencent.lbssearch.object.result.Address2GeoResultObject;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.mapsdk.raster.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tools.utils.TimeUtils;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//import com.tencent.tencentmap.mapsdk.map.model.BitmapDescriptorFactory;
//import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
//import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
//import com.tencent.tencentmap.mapsdk.maps.model.Marker;
//import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

/**
 * Created by shanyong on 2018/1/22.
 * 腾讯地图
 */

public class FragmentMap extends BaseFragment implements View.OnClickListener, TencentMap.OnMarkerClickListener {

    private static final String TAG = "FragmentMapTC";

    private RxTitle rxTitle;

    /**
     * 地图
     **/
    private MapView mapView;
    private TencentMap tencentMap;
    private CustInfoWindow custInfoWindow;

    //搜索
    private TencentSearch tencentSearch;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;

    private List<Marker> caseMarkers;
    List<LatLng> latLngs = new ArrayList<LatLng>();

    private long startTime = CommonUtils.getStartTime().getTime() / 1000;
    private long endTime = CommonUtils.getEndTime().getTime() / 1000;


    private LatLng currentLocation = new LatLng(31.247241, 121.492696);
//    private TencentLocation mTencentLocation;

    private LatLng desLocation;
    private String desName;

    private Handler mHandler = new Handler();

    private TencentLocationManager mLocationManager;

    private Marker locationMarker;

    private String[] stringItems = {"显示案件地址", "显示轨迹"};
    private ActionSheetDialog dialog;
    private MaptypePopup maptypePopup;


    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        rxTitle = (RxTitle) findViewById(R.id.rx_title);
        immersiveStatusBar(rxTitle);

        mapView = (MapView) findViewById(R.id.tracing_mapView);

        tencentMap = mapView.getMap();

        tencentSearch = new TencentSearch(mContext);
        custInfoWindow = new CustInfoWindow();
        tencentMap.setOnMarkerClickListener(this);
        tencentMap.setInfoWindowAdapter(custInfoWindow);

        //取出上次保存的位置设置地图
        final LatLng lastLoc = Config.getLastLocation();
        if (null != lastLoc) {
            tencentMap.setCenter(lastLoc);
        }

        mLocationManager = TencentLocationManager.getInstance(getContext());
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);

        if (null == dialog) {
            dialog = new ActionSheetDialog(mContext, stringItems, null);
        }

        maptypePopup = new MaptypePopup(getActivity());
        maptypePopup.setOnMaptypeClickListener(new MaptypePopup.onMaptypeClickListener() {
            @Override
            public void onClick(Constants.MapType type) {
                Log.e("nb","type:"+type);
                if (type.equals(Constants.MapType.MAP_TENCENT)) {
                    Log.e("nb","MAP_TENCENT");
                    MapUtil.openTencentMap(mContext, desLocation, desName);
                } else if (type.equals(Constants.MapType.MAP_BAIDU)) {
                    Log.e("nb","MAP_BAIDU");
                    MapUtil.openBaiDuNavi(mContext, currentLocation.getLatitude(), currentLocation.getLongitude(), "", desLocation.getLatitude(), desLocation.getLongitude(), desName);
                } else if (type.equals(Constants.MapType.MAP_GAODE)) {
                    Log.e("nb","MAP_GAODE");
                    MapUtil.openGaoDeNavi(mContext, currentLocation.getLatitude(), currentLocation.getLongitude(), "", desLocation.getLatitude(), desLocation.getLongitude(), desName);
                }
            }
        });

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        tencentMap.clearAllOverlays();
                        locationMarker = null;
                        addCaseAddressMarkers();
                        showLocationMarker();

                        break;
                    case 1:
                        tencentMap.clearAllOverlays();
                        locationMarker = null;
                        //展示轨迹图
                        List<Outbound> outbounds = OutboundManager.obtainOutbounds();
                        latLngs.clear();

                        for (int i = 0; i < outbounds.size(); i++) {

                            Outbound outbound = outbounds.get(i);

//                            Logger.i("position  %s time %s", i, outbound.getTime());

                            LatLng latLng = new LatLng(outbound.getLatitude(), outbound.getLongitude());
                            latLngs.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng).title(String.format("%s(%s)", outbound.getAddress(), TimeUtils.millis2String(outbound.getTime())));

                            if (i == 0) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start));
                            } else if (i == outbounds.size() - 1) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_end));
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_route));
                            }
                            tencentMap.addMarker(markerOptions);
                        }

                        tencentMap.addPolyline(new PolylineOptions().width(16).edgeWidth(2).
                                addAll(latLngs).
                                color(0xff09bc4a));

                        break;
                    default:
                }
                dialog.hide();
            }
        });


        ImageView ivMore = (ImageView) findViewById(R.id.iv_more);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.title("地图操作")
                        .titleTextSize_SP(14.5f)
                        .show();
            }
        });

        //案件地点集合
        caseMarkers = new ArrayList<>();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.layout_nav:
                maptypePopup.showPopupWindow();
                break;
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        //点击的是案件marker时
        if (custInfoWindow.map.containsKey(marker)) {
            desLocation = marker.getPosition();
            desName = marker.getSnippet().split(",")[3];
            custInfoWindow.hideAll();
        }

        //infowindow 展示的时候关闭  关闭的时候展示
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }

        return false;
    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    class CustInfoWindow implements TencentMap.InfoWindowAdapter {

        private HashMap<Marker, InfoWindowWrapper> map;

        public CustInfoWindow() {
            // TODO Auto-generated constructor stub
            map = new HashMap<Marker, InfoWindowWrapper>();
        }

        public void addMarker(Marker marker, int type) {
            InfoWindowWrapper wrapper = new InfoWindowWrapper();
            wrapper.type = type;
            map.put(marker, wrapper);
        }

        public void removeMarker(Marker marker) {
            map.remove(marker);
        }

        public void removeAll() {
            map.clear();
        }

        public void hideAll() {
            for (Marker marker : map.keySet()) {
                marker.hideInfoWindow();
            }
        }

        class InfoWindowWrapper {
            View infoWindow;
            View infoContent;
            Object holder;
            int type;
        }

        class InfoViewHolder {
            private View infoWindow;

            private LinearLayout layoutNav;

            private TextView tvName;
            private TextView tvAmt;
            private TextView tvAddr;
            private TextView tvCaseId;
            private TextView tvAddrId;


            public InfoViewHolder() {
                infoWindow = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_infowindow_content_nav, null);
                layoutNav = (LinearLayout) infoWindow.findViewById(R.id.layout_nav);

                tvName = (TextView) infoWindow.findViewById(R.id.tv_name);
                tvAmt = (TextView) infoWindow.findViewById(R.id.tv_amt);
                tvAddr = (TextView) infoWindow.findViewById(R.id.tv_addr);
                tvCaseId = (TextView) infoWindow.findViewById(R.id.tv_caseId);
                tvAddrId = (TextView) infoWindow.findViewById(R.id.tv_addrId);

                layoutNav.setOnClickListener(FragmentMap.this);

            }
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            // TODO Auto-generated method stub
            final InfoWindowWrapper wrapper = map.get(marker);
            if (wrapper == null) {
                return null;
            }
            if (wrapper.type == 1) {
                if (wrapper.infoWindow == null) {
                    wrapper.holder = new InfoViewHolder();

                    //名称放在title里
                    String agentName = marker.getTitle();
                    ((InfoViewHolder) wrapper.holder).tvName.setText(String.format(mContext.getString(R.string.agent_name), agentName));
                    //其他信息保存在Snippet中
                    String[] baseInfo = marker.getSnippet().split(",");
                    ((InfoViewHolder) wrapper.holder).tvAmt.setText(String.format(mContext.getString(R.string.agent_amt), baseInfo[0]));
                    ((InfoViewHolder) wrapper.holder).tvCaseId.setText(String.format(mContext.getString(R.string.agent_caseId), baseInfo[1]));
                    ((InfoViewHolder) wrapper.holder).tvAddrId.setText(String.format(mContext.getString(R.string.agent_addrId), baseInfo[2]));

                    String address = baseInfo[3];
                    StringBuilder sb = new StringBuilder(address);
                    if (address.length() > 20) {
                        sb.insert(20, "\r\n");
                    }

                    ((InfoViewHolder) wrapper.holder).tvAddr.setText(String.format(mContext.getString(R.string.agent_addr), sb.toString()));
                    wrapper.infoWindow = ((InfoViewHolder) wrapper.holder).infoWindow;
                }
                return wrapper.infoWindow;
            } else if (wrapper.type == 2) {

            }
            return null;
        }

        @Override
        public void onInfoWindowDettached(Marker arg0, View arg1) {
            // TODO Auto-generated method stub

        }
    }

    private ConcurrentHashMap<String, Integer> geoCountMap = new ConcurrentHashMap<>();

    /**
     * 地理编码
     */

    protected synchronized void geocoder(final RespCaseSummary summary, final MarkerOptions options, final String address) {
        final Address2GeoParam address2GeoParam =
                new Address2GeoParam().address(address);

        tencentSearch.address2geo(address2GeoParam, new HttpResponseListener() {

            @Override
            public void onSuccess(int arg0, BaseObject arg1) {
                if (arg1 == null) {
                    return;
                }
                Address2GeoResultObject obj = (Address2GeoResultObject) arg1;
                Location location = obj.result.location;
                LatLng latLng = MapUtil.locationToLatlng(location);

                summary.setLatitude(location.lat);
                summary.setLongitude(location.lng);
                long id = SugarRecord.save(summary);
                Logger.i("Saved address id :%s ", id);

                addMarkerToMap(options, latLng);
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                Logger.e("address: %s 地理编码失败 error : %s  CustName:%s", address, arg1, summary.getCustName());

                String bizId = summary.getBizId();
                Integer count = geoCountMap.get(summary.getBizId());

                if (null == count) {
                    count = 0;
                }

                if (count <= 5) {
                    Logger.i("%s 再次进行定位, 次数：%s", address, count);

                    //TODO 获取地址失败的再重新地理编码
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            geocoder(summary, options, address);
                        }
                    }, 3000);
                } else {
                    Logger.e("%s 定位失败次数超过限制,不在重试", address);
                }

                geoCountMap.put(bizId, ++count);
            }
        });
    }

    //添加 案件marker
    private void addCaseAddressMarkers() {

        caseMarkers.clear();
        geoCountMap.clear();

        List<RespCaseSummary> caseSummaries = CaseManager.obtainCasesSummaryForUser();

        if (null == caseSummaries) {
            return;
        }

        Logger.i("addCaseAddressMarkers   Summaries size :%s", caseSummaries.size());

        //将案件添加到地图上
        for (int i = 0; i < caseSummaries.size(); i++) {
            final RespCaseSummary summary = caseSummaries.get(i);

            String caseId = summary.getCaseId();

            final String addr = summary.getVisitAddress();
            if (StringUtils.isEmpty(addr)) {
                continue;
            }

            String baseInfo = String.format("%s,%s,%s,%s", summary.getOaAmt(), caseId, summary.getAddrId(), addr);

            final MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_address))
                    .title(summary.getCustName())
                    .snippet(baseInfo);

            if (summary.getLatitude() > 0 && summary.getLongitude() > 0) {
                Logger.i("(index:%s  address id:%s)  Saved address  :  %s    CustName :%s", i, summary.getAddrId(), addr, summary.getCustName());

                LatLng latLng = new LatLng(summary.getLatitude(), summary.getLongitude());
                addMarkerToMap(markerOptions, latLng);
            } else {

                Logger.i("(index:%s  address id:%s)  Geocoder address : %s   CustName :%s", i, summary.getAddrId(), addr, summary.getCustName());
                geocoder(summary, markerOptions, addr);
            }
        }
    }

    //添加case marker到地图
    public void addMarkerToMap(MarkerOptions options, LatLng latLng) {
        options.position(latLng);
        Marker caseMaker = tencentMap.addMarker(options);
        custInfoWindow.addMarker(caseMaker, 1);
        caseMarkers.add(caseMaker);
    }

    //定位maker
    public void addLocationMarker(String address, LatLng latLng) {
        if (null == locationMarker) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_anchor_marker))
                    .title(address);
            locationMarker = tencentMap.addMarker(markerOptions);
        } else {
            locationMarker.setPosition(latLng);
        }

    }



    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mapView.onStop();
    }

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
        return R.layout.fragment_map;
    }



    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        mapView = null;
        tencentMap = null;
        //停止定位
    }


/*    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mapView.onPause();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mapView.onResume();

        showLocationMarker();

        if (custInfoWindow.map.isEmpty()) {
            addCaseAddressMarkers();
        }
    }*/


    public void showLocationMarker() {

        currentLocation = Config.getLastLocation();

        if (locationMarker == null && currentLocation != null) {
            tencentMap.animateTo(currentLocation);
        }

        addLocationMarker(Config.getLastAddress(), currentLocation);
    }

}
