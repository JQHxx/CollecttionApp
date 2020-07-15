package com.huateng.phone.collection.ui.adapter;

import android.text.Editable;
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

/**
 * author: yichuan
 * Created on: 2020-04-21 11:32
 * description:
 */
public class BizAcctAdapter2 extends BaseQuickAdapter<BizAcctItemBean, BaseViewHolder> {
    private final long DELAY = 1000; // in ms
    private long lastTime = 0;

    public BizAcctAdapter2() {
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
                .setText(R.id.edt_plan_repay_total, item.getPlanRepayTotal() + "")// 计划归还总额
                .setText(R.id.tv_end_date, DateUtil.getDate(item.getEndDate()))//结束日期
                .setText(R.id.tv_reduce_total, item.getReduceTotal() + "元")
                .setText(R.id.tv_product_name, item.getProductName());


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
                if(editable.length()>0 && !String.valueOf(item.getPlanRepayTotal()).equals(editable.toString())) {
                    EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(helper.getAdapterPosition(), editable.toString(), "0")));
                }
            }
        });

    }


}
