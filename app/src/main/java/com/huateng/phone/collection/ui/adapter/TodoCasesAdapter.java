package com.huateng.phone.collection.ui.adapter;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.bean.CaseBeanData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.guanaj.easyswipemenulibrary.State.RIGHTOPEN;

/**
 * 待办案件 adapter
 * sumincy
 */

public class TodoCasesAdapter extends BaseQuickAdapter<CaseBeanData.RecordsBean, BaseViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private SparseArray<Boolean> checkStates;
    private HashMap<String, String> map;
    private boolean isInSelectMode;

    public TodoCasesAdapter() {
        super(R.layout.list_item_cases);
    }


    public void setDictData(HashMap<String, String> map) {
        this.map = map;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CaseBeanData.RecordsBean item) {

        helper.getView(R.id.right_menu_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.rightClick(helper.getAdapterPosition());
                }
                EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);
                easySwipeMenuLayout.resetStatus();
            }
        });
        helper.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.es);

                if (easySwipeMenuLayout.getStateCache() == RIGHTOPEN) {
                    easySwipeMenuLayout.resetStatus();
                    return;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.contentClick(helper.getAdapterPosition());
                }

            }
        });
        helper.getView(R.id.iv_call_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.callPhone(helper.getAdapterPosition());
                }
            }
        });

        helper.setText(R.id.tv_hostName, item.getCustName())
                .setText(R.id.tv_id_number, item.getCertNo())
                .setText(R.id.tv_overdue_days, item.getOverdueDays() + "")
                .setText(R.id.tv_case_status, "1".equals(item.getCaseStatus()) ? "已处理" : "未处理")
                .setText(R.id.tv_overdue_amt, item.getOverdueAmt() + "")
                //.setChecked(R.id.iv_check, item.isSelected())
                .setGone(R.id.rl_check, isInSelectMode);


        if (isInSelectMode) {
            helper.setChecked(R.id.iv_check, checkStates.get(helper.getAdapterPosition()));
        }

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
      /*  helper.addOnClickListener(R.id.content);
        helper.addOnClickListener(R.id.iv_call_phone);
        helper.addOnClickListener(R.id.right);*/
        CheckBox checkBox = helper.getView(R.id.iv_check);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStates.setValueAt(helper.getAdapterPosition(), !checkStates.get(helper.getAdapterPosition()));
            }
        });

    }


    private int lastSelectedPosition = -1;

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }


    public void setSelectType(boolean b) {
        isInSelectMode = b;
        initCheckStates();
        notifyDataSetChanged();
    }

    public boolean getSelectType() {
        return isInSelectMode;
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

    public void cancel() {
        for (int i = 0; i < checkStates.size(); i++) {
            checkStates.setValueAt(i, false);
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

    public interface OnItemClickListener {
        void rightClick(int position);

        void contentClick(int position);

        void callPhone(int position);


    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnSwipeMenuListener(OnItemClickListener onSwipeMenuListener) {

        this.mOnItemClickListener = onSwipeMenuListener;
    }


}
