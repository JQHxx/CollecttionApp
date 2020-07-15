package com.huateng.phone.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.ProductMsgBean;

/**
 * author: yichuan
 * Created on: 2020-03-30 14:07
 * description:
 */
public class ProductMsgAdater extends BaseQuickAdapter<ProductMsgBean, BaseViewHolder> {

    public ProductMsgAdater() {
        super(R.layout.item_product_msg);
    }

    @Override
 protected void convert(BaseViewHolder helper, ProductMsgBean item) {

    }
}
