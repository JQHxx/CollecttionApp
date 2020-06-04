package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.ReportListBean;

/**
 * author: yichuan
 * Created on: 2020-05-08 17:42
 * description:
 */
public class ReportListadapter extends BaseQuickAdapter<ReportListBean.RecordsBean, BaseViewHolder> {

    public ReportListadapter() {
        super(R.layout.list_item_report);
    }


    @Override
    protected void convert(BaseViewHolder helper, ReportListBean.RecordsBean item) {

        helper.setText(R.id.tv_case_id, item.getCaseId())
                .setText(R.id.tv_cust_name, item.getCustName())
                .setText(R.id.tv_visit_name, item.getVisitName())
                .setText(R.id.tv_visit_date, item.getVisitDate());
    }
}
