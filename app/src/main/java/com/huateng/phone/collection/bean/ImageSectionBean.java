package com.huateng.phone.collection.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.luck.picture.lib.entity.LocalMedia;

/**
 * author: yichuan
 * Created on: 2020/7/2 15:08
 * description:
 */

public class ImageSectionBean extends SectionEntity<LocalMedia> {
    private boolean isAdd;

    public ImageSectionBean(boolean isHeader, String header,boolean isAdd) {
        super(isHeader, header);
        this.isAdd = isAdd;
    }

    public ImageSectionBean(LocalMedia recorderBean) {

        super(recorderBean);
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}

