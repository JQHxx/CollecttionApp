package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.CreditCardBean;
import com.huateng.collection.utils.DictUtils;

/**
 * author: yichuan
 * Created on: 2020-04-23 19:04
 * description:
 */
public class CreditCardAdapter extends BaseQuickAdapter<CreditCardBean.RecordsBean, BaseViewHolder> {

    public CreditCardAdapter() {
        super(R.layout.list_item_credit_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditCardBean.RecordsBean item) {

        helper.setText(R.id.tv_credit_card_no,item.getCreditCardNo())
                .setText(R.id.tv_ovdu_amts,item.getOvduAmts()+"元")
                .setText(R.id.tv_overdue_periods,item. getOverduePeriods()+"期")
                .setText(R.id.tv_wrof_flag, DictUtils.getWrofFlag(item.getWrofFlag()));
    }
}
