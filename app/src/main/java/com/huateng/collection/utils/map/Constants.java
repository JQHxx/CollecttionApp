package com.huateng.collection.utils.map;


public final class Constants {

    public enum MapType {
        MAP_TENCENT,
        MAP_BAIDU,
        MAP_GAODE
    }

    /**
     * 实时定位间隔(单位:秒)
     */
    //30 * 60 * 1000
    public static final int LOCATE_INTERVAL = 15 * 60 * 1000;

    public static final int DENOISE_DISTANCE = 50;

    public static final String TIMING_LOCATE = "TIME_LOCATE";

    public static final String LOCATE_ACTION = "LOCATION";


}
