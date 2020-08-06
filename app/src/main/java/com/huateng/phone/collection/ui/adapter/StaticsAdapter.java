package com.huateng.phone.collection.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.api.RespStaticsItem;
import com.tools.utils.TimeUtils;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * 外访统计
 */

public class StaticsAdapter extends BaseQuickAdapter<RespStaticsItem, BaseViewHolder> {

    public StaticsAdapter(@LayoutRes int layoutResId, @Nullable List<RespStaticsItem> dataList) {
        super(layoutResId, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, RespStaticsItem bean) {
        helper.setText(R.id.tv_custNo, bean.getCaseId());
        helper.setText(R.id.tv_custName, bean.getCustName());
        helper.setText(R.id.tv_date, TimeUtils.millis2String(Long.valueOf(bean.getVisitDate())));
        helper.setText(R.id.tv_oprateOfficer, String.format("操作员：%s", bean.getCollector()));
    }

}