package com.huateng.collection.bean.orm;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * @author dengzh
 * @description
 * @time 2016-12-15.
 */
@Table
public class PendingReportData implements Serializable {
    @Unique
    @Expose(serialize = false, deserialize = false)
    private String tag;
    @Expose(serialize = false, deserialize = false)
    private String bizId;
    @Expose(serialize = false, deserialize = false)
    private String caseId;
    private String userId;
    private String content;
    private String key;
    private String value;
    private boolean isDic;
    private int position;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDic() {
        return isDic;
    }

    public void setDic(boolean dic) {
        isDic = dic;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //    public static String reflect(Object obj) {
//        if (obj == null) return null;
//        StringBuilder builder = new StringBuilder();
//
//        Field[] fields = obj.getClass().getDeclaredFields();
//        for (int j = 0; j < fields.length; j++) {
//            fields[j].setAccessible(true);
//            // 字段名
//            String name = fields[j].getName();
//            if (name.equals("isDic")) {
//                continue;
//            }
//            if (name.equals("caseId")) {
//                continue;
//            }
//            if (name.equals("tag")) {
//                continue;
//            }
//            if (name.equals("key")) {
//                continue;
//            }
//            if(name.equals("position")){
//                continue;
//            }
//            System.out.println(name);
//            // 字段值
//            if (fields[j].getType().getName().equals(
//                    java.lang.String.class.getName())) {
//                // String type
//                try {
//                    builder.append(fields[j].get(obj));
//                    System.out.println(fields[j].get(obj));
//                } catch (IllegalArgumentException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            } else if (fields[j].getType().getName().equals(
//                    java.lang.Integer.class.getName())
//                    || fields[j].getType().getName().equals("int")) {
//                // Integer type
//                try {
//                    builder.append(fields[j].getInt(obj));
//                    System.out.println(fields[j].getInt(obj));
//                } catch (IllegalArgumentException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            // 其他类型。。。
//        }
//        return builder.toString();
//    }

}
