package com.huateng.collection.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.api.RespLog;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * 行动流水
 */

public class HistoryActionsAdapter  extends BaseQuickAdapter<RespLog, BaseViewHolder> {

    public HistoryActionsAdapter(@LayoutRes int layoutResId, @Nullable List<RespLog> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, RespLog bean) {

       helper.setText(R.id.tv_actionCode,bean.getActCode());
        helper.setText(R.id.tv_actionName,bean.getActName());
        helper.setText(R.id.tv_actionDate,bean.getActDate());
        helper.setText(R.id.tv_actionNote,bean.getActRemark());
    }

}
