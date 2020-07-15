package com.huateng.phone.collection.bean.orm;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-15.
 */
@Table
public class Dic implements Serializable {

    @Ignore
    public static final String WEATHER = "checked";
    //sex
    @Ignore
    public static final String SEX = "ZDXB";
    //telType
    @Ignore
    public static final String TELIPHONE = "ZDDHLX";
    //addrType
    @Ignore
    public static final String ADDRESS = "ZDDZLX";
    @Ignore
    public static final String PROPERTY_COMMITTEE = "propType";
    //currency
    @Ignore
    public static final String CURRENCY = "ZDBZ";
    @Ignore
    public static final String HOUSE_IDENTITY = "neighType";
    //relWithCust
    @Ignore
    public static final String RELATION = "ZDYKHGX";

    //复核时间段   ZDFHSJDDM

    public static List<Dic> dics(String type) {
        List<Dic> dics = SugarRecord.find(Dic.class, "type = ?", type);
        return dics;
    }

    public static List<String> getOptions(String type) {
        List<Dic> dics = dics(type);
        List<String> options = new ArrayList<>();
        for (Dic dic : dics) {
            options.add(dic.getValue());
        }
        return options;
    }

    public static String queryValue(String type, String key) {
        List<Dic> dics = dics(type);
        for (Dic dic : dics) {
            if (dic.getKey().equals(key)) {
                return dic.getValue();
            }
        }
        return null;
    }

    public static String queryKey(String type, String value) {
        List<Dic> dics = dics(type);
        for (Dic dic : dics) {
            if (dic.getValue().equals(value)) {
                return dic.getKey();
            }
        }
        return null;
    }


    public static Dic objectToDic(String key, String value, String type) {
        Dic dic = new Dic();
        dic.setKey(key);
        dic.setType(type);
        dic.setValue(value);
        return dic;
    }


    private String key;
    private String value;
    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
