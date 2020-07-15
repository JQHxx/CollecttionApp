package com.huateng.phone.collection.utils;

import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.bean.orm.Outbound;
import com.orm.SugarRecord;
import com.tools.utils.*;
import com.tools.utils.StringUtils;

import java.util.List;

/**
 * Created by Lenovo on 2018/11/28.
 * 定位轨迹点查询帮助类
 */

public class OutboundManager {

    public static List<Outbound> obtainOutbounds() {
        String userId = Perference.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            List<Outbound> outbounds = SugarRecord.find(Outbound.class, "USER_ID = ? and DATE= ? ", new String[]{Perference.getUserId(), TimeUtils.getNowTimeString("yyyyMMdd")}, null, "TIME", null);
            return outbounds;
        }
        return null;
    }

    public static Outbound obtainLastOutbounds() {
        String userId = Perference.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            List<Outbound> outbounds = SugarRecord.find(Outbound.class, "USER_ID = ? and DATE= ? ", new String[]{Perference.getUserId(), TimeUtils.getNowTimeString("yyyyMMdd")}, null, "TIME", null);
            if (null != outbounds && outbounds.size() > 0) {
                return outbounds.get(outbounds.size() - 1);
            }
        }
        return null;
    }

}
