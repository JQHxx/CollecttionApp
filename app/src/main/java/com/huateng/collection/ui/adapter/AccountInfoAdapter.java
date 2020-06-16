package com.huateng.collection.ui.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.AccountInfoBean;
import com.huateng.collection.bean.api.RespPaymentCalItem;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.dialog.dm.PaymentCalculateDM;
import com.huateng.collection.utils.DictUtils;
import com.huateng.network.ApiConstants;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.LayoutRes;


/**
 *
 */

public class AccountInfoAdapter extends BaseQuickAdapter<AccountInfoBean.RecordsBean, BaseViewHolder> {

    private PaymentCalculateDM dm;
    private HashMap<String, String> map;

    public AccountInfoAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountInfoBean.RecordsBean bean) {
        helper.setText(R.id.tv_acct_status, DictUtils.getAcctStatus(bean.getAcctStatus()))
                .setText(R.id.tv_overdue_days, String.valueOf(bean.getOverdueDays()))
                .setText(R.id.tv_ovdu_amts, bean.getOvduAmts());

        TextView mTvBaserialNo = helper.getView(R.id.tv_baserial_no);
        if ("01".equals(bean.getBusinessType())) {
            mTvBaserialNo.setText(bean.getAcctNo());

        } else if ("02".equals(bean.getBusinessType())) {
            mTvBaserialNo.setText(bean.getBaserialno());

        } else if ("03".equals(bean.getBusinessType())) {
            mTvBaserialNo.setText(bean.getBaserialno());

        } else if ("04".equals(bean.getBusinessType())) {
            mTvBaserialNo.setText(bean.getBaserialno());

        }

        if (map != null && map.size() > 0) {
            String productName = map.get(bean.getProductType());
            if (TextUtils.isEmpty(productName)) {
                helper.setText(R.id.tv_product_type, bean.getProductType());
            } else {
                helper.setText(R.id.tv_product_type, productName);
            }
        } else {
            helper.setText(R.id.tv_product_type, bean.getProductType());
        }

    }


    public void setDictData(HashMap<String, String> map) {
        this.map = map;
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
                //  baseFragment.showLoading();
            }

            @Override
            public void response(List<RespPaymentCalItem> respPaymentCalItems) {
                if (dm != null) {
                    PaymentCalculateAdapter paymentCalculateAdapter = new PaymentCalculateAdapter(mContext, respPaymentCalItems);
                    dm.setRecyclerAdapter(paymentCalculateAdapter);
                }
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void end() {
                super.end();
                // baseFragment.hideLoading();
            }
        }, ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.METHOD_PAYMENT_CALCULATE, map);
    }
}
