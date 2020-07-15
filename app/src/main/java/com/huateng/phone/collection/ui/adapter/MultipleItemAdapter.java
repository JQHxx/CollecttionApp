package com.huateng.phone.collection.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.MultipleEntity;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-04-28 14:17
 * description:
 */
public class MultipleItemAdapter extends BaseMultiItemQuickAdapter<MultipleEntity, BaseViewHolder> {


    public MultipleItemAdapter(List<MultipleEntity> data) {
        super(data);
        addItemType(MultipleEntity.TEXT, R.layout.multiple_item_text);
        addItemType(MultipleEntity.TEXT, R.layout.multiple_item_date);
        addItemType(MultipleEntity.TEXT, R.layout.multiple_item_edit_text);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, MultipleEntity item) {

    }
}
