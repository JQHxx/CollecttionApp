package com.huateng.phone.collection.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.CreditCardBean;
import com.huateng.phone.collection.utils.DictUtils;

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
                .setText(R.id.tv_ovdu_amts, TextUtils.isEmpty(item.getAcctBalance())?"0.00":item.getAcctBalance())
                .setText(R.id.tv_overdue_periods,String.valueOf(item. getOverPeriodRmb()))
                .setText(R.id.tv_wrof_flag, DictUtils.getWrofFlag(item.getWrofFlag()))
                .setText(R.id.tv_ovdu_amts_type,"156".equals(item.getCurrency())?"人民币逾期总金额:":"美元逾期总金额：")
                .setText(R.id.tv_overdue_periods_type,"156".equals(item.getCurrency())?"人民币逾期期数:":"美元逾期期数：");

    }
}
