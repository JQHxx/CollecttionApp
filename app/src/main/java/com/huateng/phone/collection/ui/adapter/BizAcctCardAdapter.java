package com.huateng.phone.collection.ui.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.BizAcctEventBean;
import com.huateng.phone.collection.bean.BizAcctItemBean;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * author: yichuan
 * Created on: 2020-05-14 19:28
 * description:
 */
public class BizAcctCardAdapter extends BaseQuickAdapter<BizAcctItemBean, BaseViewHolder> {

    private HashMap<String, String> map;

    public BizAcctCardAdapter() {
        super(R.layout.item_biz_acct_card);
    }


    @Override
    protected void convert(BaseViewHolder helper, BizAcctItemBean item) {

        helper
                .setText(R.id.tv_account_num, "第" + (helper.getAdapterPosition() + 1) + "条信用卡信息")
                .setText(R.id.tv_credit_card_no, item.getAcctNo())
                .setText(R.id.tv_curr_acct_balance, item.getLoanTotal() + "元")
                .setText(R.id.tv_ovdu_principal_amts, item.getLoanPri() + "元")
                .setText(R.id.tv_ovdu_month, item.getOverdueDays())
                .setText(R.id.tv_should_breach, item.getShouldBreach() + "元")
                .setText(R.id.tv_interest_amt, item.getLoanInt() + "元")
                .setText(R.id.tv_penal_amt, item.getPenalAmt() + "元")
                .setText(R.id.tv_reduce_pri, item.getReducePri() + "元")
                .setText(R.id.tv_reduce_fee, item.getReduceFee() + "元")
                .setText(R.id.tv_reduce_int, item.getReduceAccrual() + "元")
                .setText(R.id.tv_reduce_total, item.getReduceTotal() + "元")
                .setText(R.id.tv_reduce_oth, item.getReduceOth() + "元")
                .setText(R.id.edt_plan_repay_total, "0".equals(item.getPlanRepayTotal()) ? "" : item.getPlanRepayTotal())
                .setText(R.id.tv_is_ear_settlement, "Y".equals(item.getIsEarSettlement()) ? "是" : "否");
        if("Y".equals(item.getIsEarSettlement())) {
            helper.setText(R.id.tv_is_ear_settlement,"是" );
        }else if("N".equals(item.getIsEarSettlement())) {
            helper.setText(R.id.tv_is_ear_settlement,"否" );
        }else{
            helper.setText(R.id.tv_is_ear_settlement,"请选择" );
        }
        helper.setText(R.id.edt_plan_repay_total, item.getPlanRepayTotal());

        if ("156".equals(item.getCurrency())) {
            helper.setText(R.id.tv_currency, "人民币");
        } else if ("840".equals(item.getCurrency())) {
            helper.setText(R.id.tv_currency, "美元");
        } else {
            helper.setText(R.id.tv_currency, item.getCurrency());
        }

        helper.addOnClickListener(R.id.ll_is_ear_settlement);
        EditText editText = helper.getView(R.id.edt_plan_repay_total);

        if (map != null) {
            helper.setText(R.id.tv_product_name, TextUtils.isEmpty(map.get(item.getCardType())) ? item.getCardType() : map.get(item.getCardType()));

        } else {
            helper.setText(R.id.tv_product_name, item.getCardType());
        }

        editText.addTextChangedListener(new TextWatcher() {
            String s = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Log.e("nb","beforeTextChanged：--->"+charSequence.toString());

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (s.equals(editable.toString())) {
                    return;
                }
                s = editable.toString();
                if (TextUtils.isEmpty(s) && TextUtils.isEmpty(item.getPlanRepayTotal())) {
                    return;
                }
                if (item.getPlanRepayTotal().equals(s)) {
                    return;

                }
                if (!item.getPlanRepayTotal().equals(editable.toString())) {
                    EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(helper.getAdapterPosition(), editable.toString(), "1")));

                }


            }
        });


    }


    public void setDictData(HashMap<String, String> map) {
        this.map = map;
    }
}
