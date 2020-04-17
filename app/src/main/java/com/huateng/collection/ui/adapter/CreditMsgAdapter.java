package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.CreditMsgBean;

/**
 * author: yichuan
 * Created on: 2020-04-03 14:22
 * description:
 */
public class CreditMsgAdapter extends BaseQuickAdapter<CreditMsgBean, BaseViewHolder> {


    public CreditMsgAdapter() {
        super(R.layout.item_credit_msg);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, CreditMsgBean item) {

    }
}
