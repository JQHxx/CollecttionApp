package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.LogActActionBean;
import com.huateng.collection.utils.DateUtil;

/**
 * 行动流水
 */

public class HistoryActionsAdapter extends BaseQuickAdapter<LogActActionBean.RecordsBean, BaseViewHolder> {

    public HistoryActionsAdapter() {
        super(R.layout.list_item_history_actions);
    }

    @Override
    protected void convert(BaseViewHolder helper, LogActActionBean.RecordsBean bean) {

        String data = DateUtil.getDate(bean.getOutBoundDate());
        helper.setText(R.id.tv_actionNote, bean.getOutBoundAddress());
        helper.setText(R.id.tv_actionName, bean.getOutBoundName());
        helper.setText(R.id.tv_actionDate, data);
       // helper.setText(R.id.tv_actionNote, bean.getRemark());
    }

}
