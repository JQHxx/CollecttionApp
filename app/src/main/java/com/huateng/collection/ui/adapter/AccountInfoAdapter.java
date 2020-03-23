package com.huateng.collection.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.bean.api.RespPaymentCalItem;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.dm.PaymentCalculateDM;
import com.huateng.network.ApiConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;


/**
 *
 */

public class AccountInfoAdapter extends BaseQuickAdapter<RespAccount, BaseViewHolder> {


    private BaseFragment baseFragment;
    private int[] dots = {R.drawable.dot1, R.drawable.dot2, R.drawable.dot3, R.drawable.dot4};
    private PaymentCalculateDM dm;

    public AccountInfoAdapter(@LayoutRes int layoutResId, @Nullable List<RespAccount> data, BaseFragment fragment) {
        super(layoutResId, data);
        this.baseFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, RespAccount bean) {

        helper.setText(R.id.tv_cardNo, bean.getAcctNo());
        helper.setImageResource(R.id.iv_dot, dots[new Random().nextInt(3)]);

        helper.setText(R.id.tv_bal, String.format("%s元", bean.getLastCycStmtBal()));
        helper.setImageResource(R.id.iv_dot2, dots[new Random().nextInt(3)]);

        if (helper.getAdapterPosition() == 0) {
            View view = helper.getView(R.id.v_line);
            view.setVisibility(View.INVISIBLE);
        }
        final List<String> list = new ArrayList<>();
        list.add(bean.getAcctNo());


//        helper.getView(R.id.btn_operation).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dm = new DialogCenter(mContext).showPaymentCalculateDialog();
//                dm.setAccountDataSource(list);
//                dm.setOnQueryListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        requestPamentCalculate(dm.collectData());
//                    }
//                });
//            }
//        });
    }

    /**
     * 请求销卡付清计算数据
     *
     * @param map
     */
    public void requestPamentCalculate(HashMap<String, String> map) {
        CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespPaymentCalItem>>() {

            @Override
            public void beforeRequest() {
                super.beforeRequest();
                baseFragment.showLoading();
            }

            @Override
            public void response(List<RespPaymentCalItem> respPaymentCalItems) {
                if (dm != null) {
                    PaymentCalculateAdapter paymentCalculateAdapter = new PaymentCalculateAdapter(mContext, respPaymentCalItems);
                    dm.setRecyclerAdapter(paymentCalculateAdapter);
                }
            }

            @Override
            public void end() {
                super.end();
                baseFragment.hideLoading();
            }
        }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_PAYMENT_CALCULATE, map);
    }
}
