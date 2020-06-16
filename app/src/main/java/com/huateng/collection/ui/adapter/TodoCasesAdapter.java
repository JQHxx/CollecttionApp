package com.huateng.collection.ui.adapter;

import android.text.TextUtils;
import android.util.SparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.bean.CaseBeanData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.LayoutRes;

/**
 * 待办案件 adapter
 * sumincy
 */

public class TodoCasesAdapter extends BaseQuickAdapter<CaseBeanData.RecordsBean, BaseViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private SparseArray<Boolean> checkStates;
    private HashMap<String, String> map;
    private boolean isInSelectMode;

    public TodoCasesAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void setDictData(HashMap<String, String> map) {
        this.map = map;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CaseBeanData.RecordsBean item) {
        helper.setText(R.id.tv_hostName, item.getCustName())
                .setText(R.id.tv_id_number, item.getCertNo())
                .setText(R.id.tv_overdue_days, item.getOverdueDays() + "")
                .setText(R.id.tv_case_status, "1".equals(item.getCaseStatus()) ? "已处理" : "未处理")
                .setText(R.id.tv_overdue_amt, item.getOverdueAmt() + "");

        //
        if (map != null && map.size() > 0) {
            String productName = map.get(item.getProductType());
            if (TextUtils.isEmpty(productName)) {
                helper.setText(R.id.tv_product_name, item.getProductType());
            } else {
                helper.setText(R.id.tv_product_name, productName);
            }
        } else {
            helper.setText(R.id.tv_product_name, item.getProductType());
        }
        helper.addOnClickListener(R.id.ll_layout);
        helper.addOnClickListener(R.id.iv_call_phone);

        if (TextUtils.isEmpty(item.getPhoneNo())) {
            helper.setVisible(R.id.iv_call_phone, true);
        } else {
            helper.setVisible(R.id.iv_call_phone, false);

        }

    }


    private int lastSelectedPosition = -1;

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }


    public void leaveSelectMode() {
        isInSelectMode = false;
        initCheckStates();
        notifyDataSetChanged();
    }


    //    初始化
    public void initCheckStates() {
        checkStates = new SparseArray<>();
        for (int i = 0; i < getData().size(); i++) {
            checkStates.put(i, false);
        }
    }

    //    反选
    public void reverse() {
        for (int i = 0; i < checkStates.size(); i++) {
            if (checkStates.valueAt(i)) {
                checkStates.setValueAt(i, false);
            } else {
                checkStates.setValueAt(i, true);
            }
        }
        notifyDataSetChanged();
    }

    //    全选
    public void selectAll() {
        for (int i = 0; i < checkStates.size(); i++) {
            checkStates.setValueAt(i, true);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedPositions() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < checkStates.size(); i++) {
            boolean b = checkStates.get(i);
            if (b) {
                positions.add(i);
            }
        }
        return positions;
    }


}
