package com.huateng.collection.bean;

import com.huateng.collection.bean.orm.Dic;

import java.io.Serializable;
import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-09.
 */
public class ReportSegmentRecyclerItem implements Serializable{
    private String label;
    private int inputType;
    private List<Dic> dicItems;
    private String content;
    private int position;

    // spinner时非DIC使用
    private boolean isImitate;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Dic> getDicItems() {
        return dicItems;
    }

    public void setDicItems(List<Dic> dicItems) {
        this.dicItems = dicItems;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public boolean isImitate() {
        return isImitate;
    }

    public void setImitate(boolean imitate) {
        isImitate = imitate;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



}
