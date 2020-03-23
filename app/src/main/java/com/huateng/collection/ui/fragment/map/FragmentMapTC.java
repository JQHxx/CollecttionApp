package com.huateng.collection.ui.fragment.map;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.Outbound;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.utils.CommonUtils;
import com.huateng.collection.utils.OutboundManager;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.utils.map.MapUtil;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Address2GeoParam;
import com.tencent.lbssearch.object.param.DrivingParam;
import com.tencent.lbssearch.object.param.RoutePlanningParam;
import com.tencent.lbssearch.object.param.TransitParam;
import com.tencent.lbssearch.object.param.WalkingParam;
import com.tencent.lbssearch.object.result.Address2GeoResultObject;
import com.tencent.lbssearch.object.result.DrivingResultObject;
import com.tencent.lbssearch.object.result.RoutePlanningObject;
import com.tencent.lbssearch.object.result.TransitResultObject;
import com.tencent.lbssearch.object.result.WalkingResultObject;
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
import com.tools.view.RxToast;
import com.tools.view.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.Nullable;

//import com.tencent.tencentmap.mapsdk.map.model.BitmapDescriptorFactory;
//import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
//import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
//import com.tencent.tencentmap.mapsdk.maps.model.Marker;
//import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

/**
 * Created by shanyong on 2018/1/22.
 * 腾讯地图
 */

public class FragmentMapTC extends BaseFragment implements View.OnClickListener, TencentMap.OnMarkerClickListener {

    private static final String TAG = "FragmentMapTC";

    private RxTitle rxTitle;
    private SlidingUpPanelLayout mLayout;
    private ImageView ivArrow;
    private ListView listView;


    /**
     * 地图
     **/
    private MapView mapView;
    private TencentMap tencentMap;
    private CustInfoWindow custInfoWindow;

    //搜索
    private TencentSearch tencentSearch;

    private RoadPlanAdapter roadPlanAdapter;

    private Double lastX = 0.0;
    private int mCurrentDirection = 0;

    private List<Marker> caseMarkers;
    List<LatLng> latLngs = new ArrayList<LatLng>();

    private long startTime = CommonUtils.getStartTime().getTime() / 1000;
    private long endTime = CommonUtils.getEndTime().getTime() / 1000;


    private LatLng currentLocation = new LatLng(31.247241, 121.492696);
//    private TencentLocation mTencentLocation;

    private LatLng desLocation;

    private Handler mHandler = new Handler();

    private TencentLocationManager mLocationManager;

    private Marker locationMarker;


    private String[] stringItems = {"显示案件地址", "显示轨迹"};
    private ActionSheetDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_map_tencent, container, false);
        return mContentView;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        rxTitle = (RxTitle) findViewById(R.id.rx_title);
        immersiveStatusBar(rxTitle);

        ivArrow = (ImageView) findViewById(R.id.iv_arrow);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mapView = (MapView) findViewById(R.id.tracing_mapView);

        listView = (ListView) findViewById(R.id.list);

        roadPlanAdapter = new RoadPlanAdapter(getContext());
        listView.setAdapter(roadPlanAdapter);

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

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        hideRouteLines();
                        tencentMap.clearAllOverlays();
                        locationMarker = null;
                        addCaseAddressMarkers();
                        showLocationMarker();

                        break;
                    case 1:
                        hideRouteLines();
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

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset > 0.5) {
                    ivArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    ivArrow.setImageResource(R.drawable.ic_arrow_up);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //初始化 隐藏panel
        hideRouteLines();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tencentMap.clearAllOverlays();
                addCaseAddressMarkers();
                locationMarker = null;

                if (currentLocation != null) {
                    addLocationMarker(Config.getLastAddress(), currentLocation);
                }

                if (roadPlanAdapter.getWalkRoutes() != null) {
                    WalkingResultObject.Route route = roadPlanAdapter.getWalkRoutes().get(position);
                    drawSolidLine(roadPlanAdapter.getWalkRoutes().get(position).polyline);

                    drawRouteMarker(route.steps, route.polyline);
                }
                if (roadPlanAdapter.getDriveRoutes() != null) {
                    DrivingResultObject.Route route = roadPlanAdapter.getDriveRoutes().get(position);
                    drawSolidLine(route.polyline);
                    drawRouteMarker(route.steps, route.polyline);
                }
                if (roadPlanAdapter.getTransitRoutes() != null) {
                    List<TransitResultObject.Segment> segments =
                            roadPlanAdapter.getTransitRoutes().get(position).steps;

                    for (TransitResultObject.Segment segment : segments) {
                        if (segment instanceof TransitResultObject.Walking) {
                            //画公交路线中的步行
                            if (null != ((TransitResultObject.Walking) segment).polyline) {
                                drawDotLine(((TransitResultObject.Walking) segment).polyline);
                                if (null != ((TransitResultObject.Walking) segment).steps) {
                                    drawRouteMarker(((TransitResultObject.Walking) segment).steps, ((TransitResultObject.Walking) segment).polyline);
                                }
                            }
                        }
                        if (segment instanceof TransitResultObject.Transit) {
                            //画公交路线
                            drawSolidLine(((TransitResultObject.Transit) segment).lines.get(0).polyline);
                            drawBusMarker(((TransitResultObject.Transit) segment).lines.get(0));
                        }
                    }
                }

                showAnchor();
            }
        });

        //案件地点集合
        caseMarkers = new ArrayList<>();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.layout_car:
                getDrivePlan();
                break;
            case R.id.layout_bus:
                getTransitPlan();
                break;
            case R.id.layout_walk:
                getWalkPlan();
                break;
        }
    }


    //隐藏路线面板
    public void hideRouteLines() {
        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    //显示路线面板
    public void showRouteLines() {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    //显示anchor
    public void showAnchor() {
        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }


    /**
     * 步行规划，只能设置起点和终点
     */
    protected void getWalkPlan() {
        WalkingParam walkingParam = new WalkingParam();
        walkingParam.from(MapUtil.latLngToLocation(currentLocation));
        walkingParam.to(MapUtil.latLngToLocation(desLocation));
        tencentSearch.getDirection(walkingParam, directionResponseListener);
    }

    /**
     * 驾车规划，支持途经点和策略设置，具体信息见文档
     */
    protected void getDrivePlan() {
        TencentSearch tencentSearch = new TencentSearch(mContext);
        DrivingParam drivingParam = new DrivingParam();
        drivingParam.from(MapUtil.latLngToLocation(currentLocation));
        drivingParam.to(MapUtil.latLngToLocation(desLocation));
        //策略
        drivingParam.policy(RoutePlanningParam.DrivingPolicy.LEAST_DISTANCE);
        //途经点
//		drivingParam.addWayPoint(new Location(39.898938f, 116.348648f));
        tencentSearch.getDirection(drivingParam, directionResponseListener);
    }

    /**
     * 公交换乘，支持策略，具体信息见文档
     */
    protected void getTransitPlan() {
        TencentSearch tencentSearch = new TencentSearch(mContext);
        TransitParam transitParam = new TransitParam();
        transitParam.from(MapUtil.latLngToLocation(currentLocation));
        transitParam.to(MapUtil.latLngToLocation(desLocation));
        //策略
        transitParam.policy(RoutePlanningParam.TransitPolicy.LEAST_TIME);
        tencentSearch.getDirection(transitParam, directionResponseListener);
    }

    //路线规划 监听
    HttpResponseListener directionResponseListener =
            new HttpResponseListener() {

                @Override
                public void onSuccess(int arg0, BaseObject reponse) {
                    // TODO Auto-generated method stub
                    if (reponse == null) {
                        return;
                    }

                    Logger.i("tencent map RoadPlan search success");

                    RoutePlanningObject obj = (RoutePlanningObject) reponse;
                    roadPlanAdapter.setPlanObject(obj);
                    roadPlanAdapter.notifyDataSetChanged();
                    showRouteLines();
                }

                @Override
                public void onFailure(int arg0, String message, Throwable arg2) {
                    // TODO Auto-generated method stub
                    RxToast.showToast(message);
                }
            };

    @Override
    public boolean onMarkerClick(Marker marker) {

        //点击的是案件marker时
        if (custInfoWindow.map.containsKey(marker)) {
            desLocation = marker.getPosition();
            hideRouteLines();
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

            private LinearLayout layoutCar;
            private LinearLayout layoutBus;
            private LinearLayout layoutWalk;

            private TextView tvName;
            private TextView tvAmt;
            private TextView tvAddr;
            private TextView tvCaseId;
            private TextView tvAddrId;


            public InfoViewHolder() {
                infoWindow = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_infowindow_content, null);
                layoutCar = (LinearLayout) infoWindow.findViewById(R.id.layout_car);
                layoutBus = (LinearLayout) infoWindow.findViewById(R.id.layout_bus);
                layoutWalk = (LinearLayout) infoWindow.findViewById(R.id.layout_walk);

                tvName = (TextView) infoWindow.findViewById(R.id.tv_name);
                tvAmt = (TextView) infoWindow.findViewById(R.id.tv_amt);
                tvAddr = (TextView) infoWindow.findViewById(R.id.tv_addr);
                tvCaseId = (TextView) infoWindow.findViewById(R.id.tv_caseId);
                tvAddrId = (TextView) infoWindow.findViewById(R.id.tv_addrId);

                layoutCar.setOnClickListener(FragmentMapTC.this);
                layoutBus.setOnClickListener(FragmentMapTC.this);
                layoutWalk.setOnClickListener(FragmentMapTC.this);

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


    private AtomicInteger count = new AtomicInteger();


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
                count.decrementAndGet();
            }

            @Override
            public void onFailure(int arg0, String arg1, Throwable arg2) {
                Logger.i("address %s geo  %s  ", address, arg1);
                count.decrementAndGet();
            }
        });
    }

    //添加 案件marker
    private void addCaseAddressMarkers() {


        caseMarkers.clear();

        List<RespCaseSummary> caseSummaries = CaseManager.obtainCasesSummaryForUser();

        if (null == caseSummaries) {
            return;
        }
//        Logger.i("addCaseAddressMarkers   Summaries size :%s", caseSummaries.size());

        //将案件添加到地图上
        for (int i = 0; i < caseSummaries.size(); i++) {
            final RespCaseSummary summary = caseSummaries.get(i);

            Logger.i("address id:%s", summary.getAddrId());


            String caseId = summary.getCaseId();

            final String addr = summary.getVisitAddress();

            String baseInfo = String.format("%s,%s,%s,%s", summary.getOaAmt(), caseId, summary.getAddrId(), addr);

            final MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_address))
                    .title(summary.getCustName())
                    .snippet(baseInfo);

            if (summary.getLatitude() > 0 && summary.getLongitude() > 0) {
                LatLng latLng = new LatLng(summary.getLatitude(), summary.getLongitude());
                addMarkerToMap(markerOptions, latLng);
            } else {
                if (count.getAndIncrement() >= 4) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            geocoder(summary, markerOptions, addr);
                        }
                    }, 2000);
                } else {
                    geocoder(summary, markerOptions, addr);
                }
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


    /**
     * 将路线以实线画到地图上
     *
     * @param locations
     */
    protected void drawSolidLine(List<Location> locations) {
        tencentMap.addPolyline(new PolylineOptions().width(20).edgeWidth(2).
                addAll(MapUtil.getLatLngs(locations)).
                color(0xff09bc4a));
    }


    //添加路线marker
    protected void drawRouteMarker(List<RoutePlanningObject.Step> steps, List<Location> polyline) {
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_line_node));

        for (int i = 0; i < steps.size(); i++) {
            RoutePlanningObject.Step step = steps.get(i);
            options.title(step.instruction);

            LatLng startLatLng = MapUtil.locationToLatlng(polyline.get(step.polyline_idx.get(0)));
            LatLng toLatLng = MapUtil.locationToLatlng(polyline.get(step.polyline_idx.get(1)));

            //旋转图标
            double angle = MapUtil.getAngle(startLatLng, toLatLng);
            options.rotation(360 - (float) angle);
            options.position(startLatLng);

            tencentMap.addMarker(options);
        }
    }

    //公交路线marker
    public void drawBusMarker(TransitResultObject.Line lines) {
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_bus));

        options.title(String.format("%s站 搭乘 %s", lines.geton.title, lines.title)).position(MapUtil.locationToLatlng(lines.geton.location));

        tencentMap.addMarker(options);

        options.title(String.format("%s站 下车", lines.getoff.title)).position(MapUtil.locationToLatlng(lines.getoff.location));

        tencentMap.addMarker(options);
    }


    /**
     * 将路线以虚线画到地图上，用于公交中的步行
     *
     * @param locations
     */

    protected void drawDotLine(List<Location> locations) {
        tencentMap.addPolyline(new PolylineOptions().width(16).edgeWidth(1).
                addAll(MapUtil.getLatLngs(locations)).
                color(0xff348af8).
                setDottedLine(true));
    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

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
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
        mapView = null;
        tencentMap = null;
        //停止定位
    }


    @Override
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
    }


    public void showLocationMarker() {

        currentLocation = Config.getLastLocation();

        if (locationMarker == null && currentLocation != null) {
            tencentMap.animateTo(currentLocation);
        }

        addLocationMarker(Config.getLastAddress(), currentLocation);
    }

}
