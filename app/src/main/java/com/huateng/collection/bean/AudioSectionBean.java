package com.huateng.collection.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * author: yichuan
 * Created on: 2020/6/22 13:34
 * description:
 */
public class AudioSectionBean extends SectionEntity<RecorderBean> {
    private boolean isAdd;

    public AudioSectionBean(boolean isHeader, String header,boolean isAdd) {
        super(isHeader, header);
        this.isAdd = isAdd;
    }

    public AudioSectionBean(RecorderBean recorderBean) {

        super(recorderBean);
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
