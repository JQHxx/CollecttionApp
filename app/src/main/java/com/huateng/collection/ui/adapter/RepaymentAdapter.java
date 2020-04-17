package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.RepaymentBean;

/**
 * author: yichuan
 * Created on: 2020-03-31 09:48
 * description:
 */
public class RepaymentAdapter extends BaseQuickAdapter<RepaymentBean, BaseViewHolder> {

    public RepaymentAdapter() {
        super(R.layout.item_repayment);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, RepaymentBean item) {

    }
}
