package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.ChannelItem;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-03-30 15:39
 * description:
 */

public class GridMenuAdapter extends BaseSectionQuickAdapter<ChannelItem, BaseViewHolder> {


    public GridMenuAdapter(List<ChannelItem> data) {
        super(R.layout.gridview_item, R.layout.item_grid_head, data);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, ChannelItem item) {
        helper.setText(R.id.tv_menu_name,item.getName());
    }


    @Override
    protected void convertHead(BaseViewHolder helper, ChannelItem item) {
        helper.setText(R.id.tv_title,item.getName());
    }
}

