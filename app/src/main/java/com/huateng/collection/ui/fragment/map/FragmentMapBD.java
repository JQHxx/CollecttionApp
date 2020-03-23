//package com.huateng.collection.ui.fragment.map;
//
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.core.RouteLine;
//import com.baidu.mapapi.search.core.SearchResult;
//import com.baidu.mapapi.search.route.BikingRoutePlanOption;
//import com.baidu.mapapi.search.route.BikingRouteResult;
//import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
//import com.baidu.mapapi.search.route.DrivingRouteResult;
//import com.baidu.mapapi.search.route.IndoorRouteResult;
//import com.baidu.mapapi.search.route.MassTransitRouteResult;
//import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
//import com.baidu.mapapi.search.route.PlanNode;
//import com.baidu.mapapi.search.route.RoutePlanSearch;
//import com.baidu.mapapi.search.route.TransitRoutePlanOption;
//import com.baidu.mapapi.search.route.TransitRouteResult;
//import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
//import com.baidu.mapapi.search.route.WalkingRouteResult;
//import com.baidu.trace.api.track.DistanceRequest;
//import com.baidu.trace.api.track.DistanceResponse;
//import com.baidu.trace.api.track.HistoryTrackRequest;
//import com.baidu.trace.api.track.HistoryTrackResponse;
//import com.baidu.trace.api.track.OnTrackListener;
//import com.baidu.trace.api.track.SupplementMode;
//import com.baidu.trace.api.track.TrackPoint;
//import com.baidu.trace.model.CoordType;
//import com.baidu.trace.model.ProcessOption;
//import com.baidu.trace.model.SortType;
//import com.baidu.trace.model.StatusCodes;
//import com.baidu.trace.model.TransportMode;
//import com.huateng.collection.R;
//import com.huateng.collection.app.MainApplication;
//import com.huateng.collection.event.BusTag;
//
//import com.huateng.collection.ui.adapter.RouteLineAdapter;
//import com.huateng.collection.ui.base.BaseFragment;
//import com.huateng.collection.ui.fragment.map.overlay.BikingRouteOverlay;
//import com.huateng.collection.ui.fragment.map.overlay.DrivingRouteOverlay;
//import com.huateng.collection.ui.fragment.map.overlay.OverlayManager;
//import com.huateng.collection.ui.fragment.map.overlay.TraceOverlay;
//import com.huateng.collection.ui.fragment.map.overlay.TransitRouteOverlay;
//import com.huateng.collection.ui.fragment.map.overlay.WalkingRouteOverlay;
//import com.huateng.collection.utils.CommonUtils;
//import com.huateng.collection.utils.map.CommonUtil;
//import com.huateng.collection.utils.map.CurrentLocation;
//import com.huateng.collection.utils.map.MapUtil;
//import com.huateng.collection.utils.rxbus.RxBus;
//import com.kyleduo.switchbutton.SwitchButton;
//import com.orhanobut.logger.Logger;
//import com.tools.view.RxToast;
//import com.tools.view.slidinguppanel.SlidingUpPanelLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.content.Context.SENSOR_SERVICE;
//import static com.huateng.collection.ui.adapter.RouteLineAdapter.Type.BIKING_ROUTE;
//import static com.huateng.collection.ui.adapter.RouteLineAdapter.Type.TRANSIT_ROUTE;
//
///**
// * Created by shanyong on 2018/1/22.
// * 百度地图 鹰眼轨迹实现
// */
//
//public class FragmentMapBD extends BaseFragment implements SensorEventListener, OnGetRoutePlanResultListener, View.OnClickListener {
//
//    private static final String TAG = "FragmentMapBD";
//    private MainApplication application;
//    private MapView mapView;
//    private BaiduMap mBaiduMap;
//    private View infoWindow;
//    private LinearLayout layoutBus;
//    private LinearLayout layoutCar;
//    private LinearLayout layoutWalk;
//    private LinearLayout layoutBike;
//    private SlidingUpPanelLayout mLayout;
//
//    private SensorManager mSensorManager;
//    private RoutePlanSearch mSearch = null;
//
//    private WalkingRouteResult nowResultwalk = null;
//    private BikingRouteResult nowResultbike = null;
//    private TransitRouteResult nowResultransit = null;
//    private DrivingRouteResult nowResultdrive = null;
//    private OverlayManager routeOverlay = null;
//    private TraceOverlay traceOverlay;
//
//    private RouteLine route = null;
//    private ListView listView;
//    private RouteLineAdapter mAdapter;
//    private List<? extends RouteLine> mtransitRouteLines;
//
//
//    private Double lastX = 0.0;
//    private int mCurrentDirection = 0;
//
//    private LatLng testPoint = new LatLng(31.204188, 121.433094);
//
//    // 当前进行的检索，供判断浏览节点时结果使用。
//    private int nowSearchType = -1;
//
//    /**
//     * 地图工具
//     */
//    private MapUtil mapUtil = null;
//
//    private ImageView ivArrow;
//
//    /**
//     * 历史轨迹请求
//     */
//    private HistoryTrackRequest historyTrackRequest;
//
//    /**
//     * 轨迹监听器（用于接收历史轨迹回调）
//     */
//    private OnTrackListener mTrackListener = null;
//
//    /**
//     * 轨迹点集合
//     */
//    private List<LatLng> trackPoints = new ArrayList<>();
//
//    private int pageIndex = 1;
//
//    private long startTime = CommonUtils.getStartTime().getTime() / 1000;
//    private long endTime = CommonUtils.getEndTime().getTime() / 1000;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.fragment_map_baidu, container, false);
//        return mContentView;
//    }
//
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        application = MainApplication.getApplication();
//        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
//        initViews(mContentView);
//    }
//
//    private void initViews(View view) {
//        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
//        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
//        mapView = (MapView) view.findViewById(R.id.tracing_mapView);
//        mBaiduMap = mapView.getMap();
//        mapUtil = MapUtil.getInstance();
//        mapUtil.init(mapView);
//        initMap();
//        mapUtil.addCaseMaker(testPoint);
//
//        SwitchButton sbTrace = (SwitchButton) view.findViewById(R.id.sb_trace);
////        RxView.clicks(sbTrace).throttleFirst(2, TimeUnit.SECONDS)
////                .subscribeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Action1<Void>() {
////                    @Override
////                    public void call(Void aVoid) {
////                        RxToast.showToast("轨迹查询频率过高");
////                    }
////                });
//
//        sbTrace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //TODO 获取轨迹
//                    hideRouteLines();
//                    application.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
//                } else {
//                    mBaiduMap.clear();
//                }
//            }
//        });
//
//        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
//                if (slideOffset > 0.5) {
//                    ivArrow.setImageResource(R.drawable.ic_arrow_down);
//                } else {
//                    ivArrow.setImageResource(R.drawable.ic_arrow_up);
//                }
//            }
//
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                Log.i(TAG, "onPanelStateChanged " + newState);
//            }
//        });
//
//        mLayout.setFadeOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });
//
//        TextView tv = (TextView) view.findViewById(R.id.name);
//
//        //初始化 隐藏panel
//        hideRouteLines();
//
//        /**
//         * 上拉panel 里的路线选择
//         */
//        listView = (ListView) view.findViewById(R.id.list);
//        mtransitRouteLines = new ArrayList<>();
//        mAdapter = new RouteLineAdapter(mContext, mtransitRouteLines, RouteLineAdapter.Type.DRIVING_ROUTE);
//        listView.setAdapter(mAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RouteLineAdapter.Type type = mAdapter.getType();
//                if (null != routeOverlay) {
//                    routeOverlay.removeFromMap();
//                }
//
//                OverlayManager overlay = new DrivingRouteOverlay(mBaiduMap);
//                switch (type) {
//                    case WALKING_ROUTE:
//                        route = nowResultwalk.getRouteLines().get(position);
//                        overlay = new WalkingRouteOverlay(mBaiduMap);
//                        ((WalkingRouteOverlay) overlay).setData(nowResultwalk.getRouteLines().get(position));
//                        break;
//                    case BIKING_ROUTE:
//                        route = nowResultbike.getRouteLines().get(position);
//                        overlay = new BikingRouteOverlay(mBaiduMap);
//                        ((BikingRouteOverlay) overlay).setData(nowResultbike.getRouteLines().get(position));
//                        break;
//                    case TRANSIT_ROUTE:
//                        route = nowResultransit.getRouteLines().get(position);
//                        overlay = new TransitRouteOverlay(mBaiduMap);
//                        ((TransitRouteOverlay) overlay).setData(nowResultransit.getRouteLines().get(position));
//                        break;
//                    case DRIVING_ROUTE:
//                    default:
//                        route = nowResultdrive.getRouteLines().get(position);
//                        ((DrivingRouteOverlay) overlay).setData(nowResultdrive.getRouteLines().get(position));
//                }
//                mBaiduMap.setOnMarkerClickListener(overlay);
//                routeOverlay = overlay;
//                overlay.addToMap();
//                overlay.zoomToSpan();
//
//                showAnchor();
//            }
//        });
//
//
//        //外访轨迹
//        traceOverlay = new TraceOverlay(mBaiduMap);
//
//    }
//
//    private void initMap() {
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
//        mBaiduMap.setMapStatus(msu);
//        // 初始化搜索模块，注册事件监听
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(this);
//
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                View view = initInfoWindoView(marker);
//                InfoWindow infoWindow = new InfoWindow(view, marker.getPosition(), -40);
//                if (null != marker.getIcon()) {
//                    mBaiduMap.showInfoWindow(infoWindow);
//                }
//                return true;
//            }
//        });
//
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                mBaiduMap.hideInfoWindow();
//            }
//
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//                return false;
//            }
//        });
//
//        mapUtil.setCenter(mCurrentDirection);
//
//        //历史轨迹查询配置
//        ProcessOption processOption = new ProcessOption();
//        processOption.setNeedDenoise(true);
//        processOption.setNeedVacuate(true);
//        processOption.setNeedMapMatch(true);
//
//        historyTrackRequest = new HistoryTrackRequest(application.getSequence(), application.serviceId, application.entityName);
//
//        historyTrackRequest.setProcessOption(processOption);
//        historyTrackRequest.setProcessed(true);
//
//        historyTrackRequest.setSupplementMode(SupplementMode.no_supplement);
//        historyTrackRequest.setSortType(SortType.asc);
//        historyTrackRequest.setCoordTypeOutput(CoordType.bd09ll);
//        historyTrackRequest.setEntityName(MainApplication.getApplication().entityName);
//        historyTrackRequest.setStartTime(startTime);
//        historyTrackRequest.setEndTime(endTime);
//        historyTrackRequest.setPageIndex(pageIndex);
//        historyTrackRequest.setPageSize(5000);
//
//        //历程查询配置
//        final DistanceRequest distanceRequest = new DistanceRequest(application.getSequence(), application.serviceId, application.entityName);
//        distanceRequest.setStartTime(startTime);// 设置开始时间
//        distanceRequest.setEndTime(endTime);// 设置结束时间
//        distanceRequest.setProcessed(true);// 纠偏
//        ProcessOption processDistance = new ProcessOption();// 创建纠偏选项实例
//        processDistance.setNeedDenoise(true);// 去噪
//        processDistance.setNeedMapMatch(true);// 绑路
//        processDistance.setTransportMode(TransportMode.walking);// 交通方式为步行
//        distanceRequest.setProcessOption(processDistance);// 设置纠偏选项
//        distanceRequest.setSupplementMode(SupplementMode.no_supplement);// 里程填充方式为无
//
//        mTrackListener = new OnTrackListener() {
//            @Override
//            public void onHistoryTrackCallback(HistoryTrackResponse response) {
//                try {
//
//                    List<TrackPoint> points = null;
//
//                    int total = response.getTotal();
//                    if (StatusCodes.SUCCESS != response.getStatus()) {
////                        RxToast.showToast(response.getMessage());
//
//                        Logger.i(response.getMessage());
//                    } else if (0 == total) {
//                        RxToast.showToast("未查询到轨迹");
//                    } else {
//                        points = response.getTrackPoints();
//                    }
//                    //TODO 查找下一页数据
//                    if (total > 5000 * pageIndex) {
//                        historyTrackRequest.setPageIndex(++pageIndex);
//                        application.mClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
//                    } else {
////                        mapUtil.drawHistoryTrack(points, true, 0);//画轨迹
//                        traceOverlay.setData(points);
//                        traceOverlay.addToMap();
//                        traceOverlay.zoomToSpan();
//                    }
//                    // 查询里程
////                    application.mClient.queryDistance(distanceRequest, mTrackListener);
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onDistanceCallback(DistanceResponse response) {
//                RxToast.showToast("里程：" + response.getDistance());
//                super.onDistanceCallback(response);
//            }
//        };
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (null != trackPoints) {
//            trackPoints.clear();
//        }
//        trackPoints = null;
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {
//            mapUtil.onPause();
//            mSensorManager.unregisterListener(this);
//        } else {
//            mapUtil.onResume();
//            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
//            RxBus.get().post(BusTag.LOCATE_EVENT, com.huateng.collection.app.Constants.APP_LOCATE);
//        }
//    }
//
//
//    private View initInfoWindoView(final Marker marker) {
//        if (null == infoWindow) {
//            infoWindow = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.layout_infowindow, null);
//        }
//
//        layoutCar = (LinearLayout) infoWindow.findViewById(R.id.layout_car);
//        layoutBus = (LinearLayout) infoWindow.findViewById(R.id.layout_bus);
//        layoutWalk = (LinearLayout) infoWindow.findViewById(R.id.layout_walk);
//        layoutBike = (LinearLayout) infoWindow.findViewById(R.id.layout_bike);
//
//        layoutCar.setOnClickListener(this);
//        layoutBus.setOnClickListener(this);
//        layoutWalk.setOnClickListener(this);
//        layoutBike.setOnClickListener(this);
//
//        return infoWindow;
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        PlanNode stNode = PlanNode.withLocation(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude));
//        PlanNode enNode = PlanNode.withLocation(testPoint);
//
//        if (null != routeOverlay) {
//            routeOverlay.removeFromMap();
//        }
//
//        switch (view.getId()) {
//            case R.id.layout_car:
//                mSearch.drivingSearch((new DrivingRoutePlanOption())
//                        .from(stNode).to(enNode));
//                nowSearchType = 1;
//                break;
//            case R.id.layout_bus:
//                mSearch.transitSearch((new TransitRoutePlanOption())
//                        .from(stNode).city("").to(enNode));
//                nowSearchType = 2;
//                break;
//            case R.id.layout_walk:
//                mSearch.walkingSearch((new WalkingRoutePlanOption())
//                        .from(stNode).to(enNode));
//                nowSearchType = 3;
//                break;
//            case R.id.layout_bike:
//                mSearch.bikingSearch((new BikingRoutePlanOption())
//                        .from(stNode).to(enNode));
//                nowSearchType = 4;
//                break;
//        }
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        //每次方向改变，重新给地图设置定位数据，用上一次的经纬度
//        double x = sensorEvent.values[SensorManager.DATA_X];
//        if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
//            mCurrentDirection = (int) x;
//            if (!CommonUtil.isZeroPoint(CurrentLocation.latitude, CurrentLocation.longitude)) {
//                mapUtil.updateMapLocation(new LatLng(CurrentLocation.latitude, CurrentLocation.longitude), (float) mCurrentDirection);
//            }
//        }
//        lastX = x;
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    public void onGetWalkingRouteResult(WalkingRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            RxToast.showToast("抱歉，未找到结果");
//            hideRouteLines();
//        }
//
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            // result.getSuggestAddrInfo()
//            RxToast.showToast("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
//            hideRouteLines();
//            return;
//        }
//
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            if (result.getRouteLines().size() > 1) {
//                nowResultwalk = result;
//                mAdapter.updateRouteLines(result.getRouteLines(), RouteLineAdapter.Type.WALKING_ROUTE);
//                mAdapter.notifyDataSetChanged();
//                showRouteLines();
//            } else if (result.getRouteLines().size() == 1) {
//                // 直接显示
//                route = result.getRouteLines().get(0);
//                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
//                mBaiduMap.setOnMarkerClickListener(overlay);
//                routeOverlay = overlay;
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//                hideRouteLines();
//            } else {
//                Log.d("route result", "结果数<0");
//                hideRouteLines();
//            }
//        }
//    }
//
//    @Override
//    public void onGetTransitRouteResult(TransitRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            RxToast.showToast("抱歉，未找到结果");
//            hideRouteLines();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            // result.getSuggestAddrInfo()
//            hideRouteLines();
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            if (result.getRouteLines().size() > 1) {
//                nowResultransit = result;
//                mAdapter.updateRouteLines(result.getRouteLines(), RouteLineAdapter.Type.TRANSIT_ROUTE);
//                mAdapter.notifyDataSetChanged();
//                showRouteLines();
//            } else if (result.getRouteLines().size() == 1) {
//                // 直接显示
//                route = result.getRouteLines().get(0);
//                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
//                mBaiduMap.setOnMarkerClickListener(overlay);
//                routeOverlay = overlay;
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//                hideRouteLines();
//            } else {
//                Log.d("route result", "结果数<0");
//                hideRouteLines();
//            }
//        }
//    }
//
//    @Override
//    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
//
//    }
//
//    @Override
//    public void onGetDrivingRouteResult(DrivingRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            RxToast.showToast("抱歉，未找到结果");
//            hideRouteLines();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            // result.getSuggestAddrInfo()
//            hideRouteLines();
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            if (result.getRouteLines().size() > 1) {
//                showRouteLines();
//                nowResultdrive = result;
//                mAdapter.updateRouteLines(result.getRouteLines(), RouteLineAdapter.Type.DRIVING_ROUTE);
//                mAdapter.notifyDataSetChanged();
//            } else if (result.getRouteLines().size() == 1) {
//                route = result.getRouteLines().get(0);
//                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
//                routeOverlay = overlay;
//                mBaiduMap.setOnMarkerClickListener(overlay);
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//                hideRouteLines();
//            } else {
//                Log.d("route result", "结果数<0");
//                hideRouteLines();
//            }
//        }
//
//    }
//
//
//    @Override
//    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
//
//    }
//
//    @Override
//    public void onGetBikingRouteResult(BikingRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            RxToast.showToast("抱歉，未找到结果");
//            hideRouteLines();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            // result.getSuggestAddrInfo()
//            RxToast.showToast("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
//            hideRouteLines();
//            return;
//        }
//
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            if (result.getRouteLines().size() > 1) {
//                showRouteLines();
//                nowResultbike = result;
//                mAdapter.updateRouteLines(result.getRouteLines(), RouteLineAdapter.Type.TRANSIT_ROUTE);
//                mAdapter.notifyDataSetChanged();
//            } else if (result.getRouteLines().size() == 1) {
//                route = result.getRouteLines().get(0);
//                BikingRouteOverlay overlay = new BikingRouteOverlay(mBaiduMap);
//                routeOverlay = overlay;
//                mBaiduMap.setOnMarkerClickListener(overlay);
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//                hideRouteLines();
//            } else {
//                Log.d("route result", "结果数<0");
//                hideRouteLines();
//            }
//        }
//    }
//
//
//    //隐藏路线面板
//    public void hideRouteLines() {
//        if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//        }
//    }
//
//    //显示路线面板
//    public void showRouteLines() {
//        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }
//    }
//
//    //显示anchor
//    public void showAnchor() {
//        if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }
//    }
//}
