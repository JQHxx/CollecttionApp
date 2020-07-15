package com.huateng.phone.collection.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * author: yichuan
 * Created on: 2020-03-30 15:40
 * description:
 */

public class ChannelItem extends SectionEntity {//implements Serializable

    /**
     * 栏目对应NAME
     */
    public String name;

    public Class activity;

    public String ActionId;
    /**
     * 栏目是否选中
     */
    public Integer selected;


    public ChannelItem(boolean isHeader, String name) {
        super(isHeader,name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public String getActionId() {
        return ActionId;
    }

    public void setActionId(String actionId) {
        ActionId = actionId;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

}
