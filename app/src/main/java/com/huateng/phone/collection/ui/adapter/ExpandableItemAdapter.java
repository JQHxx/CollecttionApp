package com.huateng.phone.collection.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.BizAcctEventBean;
import com.huateng.phone.collection.bean.Level0Item;
import com.huateng.phone.collection.bean.Level1Item;
import com.huateng.phone.collection.utils.DateUtil;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020-05-19 11:23
 * description:
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final  int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public ExpandableItemAdapter(List<MultiItemEntity> data) {

        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_resale_view);
        addItemType(TYPE_LEVEL_2, R.layout.item_biz_acct_card);
    }


    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {

       switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(R.id.tv_product_name,lv0.getTitle());
                //set view contentS
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                      if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                //1 贷款
                Level1Item level1Item = (Level1Item) item;
                holder.setText(R.id.tv_product_name, level1Item.getProductName())
                        .setText(R.id.tv_loan_no, level1Item.getLoanNo())
                        .setText(R.id.tv_loan_amt, level1Item.getLoanAmt() + "元")
                        .setText(R.id.tv_loan_int, level1Item.getLoanInt() + "元")
                        .setText(R.id.tv_loan_total, level1Item.getLoanTotal() + "元")
                        .setText(R.id.tv_overdue_days, level1Item.getOverdueDays() + "天")//逾期天数
                        .setText(R.id.tv_reduce_pri, level1Item.getReducePri() + "元")//申请减免本金
                        .setText(R.id.tv_reduce_int, level1Item.getReduceInt() + "元")//申请减免利罚息
                        .setText(R.id.tv_loan_pri, level1Item.getLoanPri() + "元")//逾期本金
                        .setText(R.id.edt_plan_repay_total, level1Item.getPlanRepayTotal() + "")// 计划归还总额
                        .setText(R.id.tv_end_date, DateUtil.getDate(level1Item.getEndDate()))//结束日期
                        .setText(R.id.tv_reduce_total, level1Item.getReduceTotal() + "元")
                        .setText(R.id.tv_product_name, level1Item.getProductName());


                EditText editText = holder.getView(R.id.edt_plan_repay_total);


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


                        if(editable.length()>0 && !String.valueOf(level1Item.getPlanRepayTotal()).equals(editable.toString())) {
                            Log.e("nb", "afterTextChanged：--->level1Item "+level1Item.getPosition() );
                            EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(level1Item.getPosition(), editable.toString(), "0")));
                        }
                    }
                });

                break;

           case TYPE_LEVEL_2:
               Level1Item level1Item2 = (Level1Item) item;
               holder.setText(R.id.tv_product_name, level1Item2.getProductName())
                       .setText(R.id.tv_currency, level1Item2.getCurrency())
                       .setText(R.id.tv_credit_card_no, level1Item2.getAcctNo())
                       .setText(R.id.tv_curr_acct_balance, level1Item2.getLoanTotal() + "元")
                       .setText(R.id.tv_ovdu_principal_amts, level1Item2.getLoanPri() + "元")
                       .setText(R.id.tv_ovdu_month, level1Item2.getOverdueDays())
                       .setText(R.id.tv_should_breach, level1Item2.getShouldBreach() + "元")
                       .setText(R.id.tv_interest_amt, level1Item2.getLoanInt() + "元")
                       .setText(R.id.tv_penal_amt, level1Item2.getPenalAmt() + "元")
                       .setText(R.id.tv_reduce_pri, level1Item2.getReducePri() + "元")
                       .setText(R.id.tv_reduce_fee, level1Item2.getReduceFee() + "元")
                       .setText(R.id.tv_reduce_int, level1Item2.getReduceInt() + "元")
                       .setText(R.id.tv_reduce_total, level1Item2.getReduceTotal() + "元")
                       .setText(R.id.tv_reduce_oth, level1Item2.getReduceOth() + "元")
                       .setText(R.id.edt_plan_repay_total, level1Item2.getPlanRepayTotal() + "")
                       .setText(R.id.tv_is_ear_settlement,"Y".equals(level1Item2.getIsEarSettlement())?"是":"否");

               holder.addOnClickListener(R.id.ll_is_ear_settlement);
               EditText editText2 = holder.getView(R.id.edt_plan_repay_total);

               editText2.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                       // Log.e("nb","beforeTextChanged：--->"+charSequence.toString());

                   }

                   @Override
                   public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                   }

                   @Override
                   public void afterTextChanged(Editable editable) {


                       if(editable.length()>0 && !String.valueOf(level1Item2.getPlanRepayTotal()).equals(editable.toString())) {
                           Log.e("nb", "afterTextChanged：--->level1Item2 " + editable.toString());
                           EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new BizAcctEventBean(level1Item2.getPosition(), editable.toString(), "1")));

                       }
                   }
               });


               break;
        }
    }

}
