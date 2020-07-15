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
import com.huateng.phone.collection.utils.DateUtil;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * author: yichuan
 * Created on: 2020-04-21 11:32
 * description:
 */
public class BizAcctAdapter extends BaseQuickAdapter<BizAcctItemBean, BaseViewHolder> {

    private HashMap<String, String> map;

    public BizAcctAdapter() {
        super(R.layout.item_resale_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, BizAcctItemBean item) {

        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_loan_no, item.getLoanNo())
                .setText(R.id.tv_loan_amt, item.getLoanAmt() + "元")
                .setText(R.id.tv_loan_int, item.getLoanInt() + "元")
                .setText(R.id.tv_loan_total, item.getLoanTotal() + "元")
                .setText(R.id.tv_overdue_days, item.getOverdueDays() + "天")//逾期天数
                .setText(R.id.tv_reduce_pri, item.getReducePri() + "元")//申请减免本金
                .setText(R.id.tv_reduce_int, item.getReduceInt() + "元")//申请减免利罚息
                .setText(R.id.tv_loan_pri, item.getLoanPri() + "元")//逾期本金
                .setText(R.id.tv_end_date, DateUtil.getDate(item.getEndDate()))//结束日期
                .setText(R.id.tv_reduce_total, item.getReduceTotal() + "元");
        helper .setText(R.id.tv_account_num, "第" + (helper.getAdapterPosition() + 1) + "条零售个人贷款");
        helper.setText(R.id.edt_plan_repay_total, item.getPlanRepayTotal());
        if (map != null) {
            helper.setText(R.id.tv_product_name, TextUtils.isEmpty(map.get(item.getProductNo())) ? item.getProductNo() : map.get(item.getProductNo()));
        } else {
            helper.setText(R.id.tv_product_name, item.getCardType());
        }

        EditText editText = helper.getView(R.id.edt_plan_repay_total);


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
                    EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(helper.getAdapterPosition(), editable.toString(), "0")));

                }

                s = editable.toString();
            }
        });

    }


    public void setDictData(HashMap<String, String> map) {
        this.map = map;
    }
}
