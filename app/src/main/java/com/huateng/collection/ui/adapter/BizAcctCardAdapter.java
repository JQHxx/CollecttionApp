package com.huateng.collection.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.BizAcctEventBean;
import com.huateng.collection.bean.BizAcctItemBean;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

/**
 * author: yichuan
 * Created on: 2020-05-14 19:28
 * description:
 */
public class BizAcctCardAdapter extends BaseQuickAdapter<BizAcctItemBean, BaseViewHolder> {

    public BizAcctCardAdapter() {
        super(R.layout.item_biz_acct_card);
    }


    @Override
    protected void convert(BaseViewHolder helper, BizAcctItemBean item) {

        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_currency, item.getCurrency())
                .setText(R.id.tv_credit_card_no, item.getAcctNo())
                .setText(R.id.tv_curr_acct_balance, item.getLoanTotal() + "元")
                .setText(R.id.tv_ovdu_principal_amts, item.getLoanPri() + "元")
                .setText(R.id.tv_ovdu_month, item.getOverdueDays())
                .setText(R.id.tv_should_breach, item.getShouldBreach() + "元")
                .setText(R.id.tv_interest_amt, item.getLoanInt() + "元")
                .setText(R.id.tv_penal_amt, item.getPenalAmt() + "元")
                .setText(R.id.tv_reduce_pri, item.getReducePri() + "元")
                .setText(R.id.tv_reduce_fee, item.getReduceFee() + "元")
                .setText(R.id.tv_reduce_int, item.getReduceInt() + "元")
                .setText(R.id.tv_reduce_total, item.getReduceTotal() + "元")
                .setText(R.id.tv_reduce_oth, item.getReduceOth() + "元")
                .setText(R.id.edt_plan_repay_total, item.getPlanRepayTotal() + "")
                .setText(R.id.tv_is_ear_settlement,"Y".equals(item.getIsEarSettlement())?"是":"否");

        helper.addOnClickListener(R.id.ll_is_ear_settlement);
        EditText editText = helper.getView(R.id.edt_plan_repay_total);

    editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Log.e("nb","beforeTextChanged：--->"+charSequence.toString());

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

               //  Log.e("nb", "afterTextChanged：--->" + editable.toString());
                if(editable.length()>0 && !String.valueOf(item.getPlanRepayTotal()).equals(editable.toString())) {
                    EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(helper.getAdapterPosition(), editable.toString(), "1")));

                }
            }
        });


    }
}
