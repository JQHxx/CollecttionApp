package com.huateng.collection.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author: yichuan
 * Created on: 2020-04-28 14:27
 * description:
 */
public class MultipleEntity implements MultiItemEntity {

    private int itemType;

    private String content;//内容
    private String title;//标题



    public static final  int TEXT = 1;//反显文字展示
    public static final  int DATE = 2;//日期选则
    public static final  int INPUT = 3;//输入框
    public static final  int COMBO_BOX = 4;//下拉框
    public static final  int CHEXK_BOX = 5;//选择框
    public static final  int DETAIL_INPUT = 5;//选择框
    @Override
    public int getItemType() {
        return itemType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
