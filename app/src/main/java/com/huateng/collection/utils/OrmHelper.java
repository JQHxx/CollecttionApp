package com.huateng.collection.utils;

import com.huateng.collection.bean.orm.UserLoginInfo;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by shanyong on 2017/6/15.
 */

public class OrmHelper {

    public static UserLoginInfo getLastLoginUserInfo() {
        List<UserLoginInfo> infos = SugarRecord.find(UserLoginInfo.class, null, null, null, "LOGIN_TIME DESC", null);
        if (infos == null || infos.size() == 0) {
            return new UserLoginInfo();
        }
        return infos.get(0);
    }

    public static boolean isUserExist(String loginName) {
        long count = SugarRecord.count(UserLoginInfo.class, "LOGIN_NAME=?", new String[]{loginName});
        if (count > 0) {
            return true;
        }
        return false;
    }

}