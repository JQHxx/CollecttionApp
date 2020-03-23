package com.huateng.collection.ui.adapter;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.MainApplication;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.fragment.casebox.FragmentCaseDetail;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.CaseFillReminder;
import com.huateng.fm.ui.widget.FmCheckBox;
import com.tools.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static me.yokeyword.fragmentation.ISupportFragment.SINGLETASK;

/**
 * 待办案件 adapter
 * sumincy
 */

public class TodoCasesAdapter extends BaseQuickAdapter<RespCaseSummary, BaseViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private SparseArray<Boolean> checkStates;
    private boolean isInSelectMode;
    private BaseFragment baseFragment;

    public TodoCasesAdapter(@LayoutRes int layoutResId, @Nullable List<RespCaseSummary> dataList, BaseFragment fragment) {
        super(layoutResId, dataList);
        this.baseFragment = fragment;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RespCaseSummary bean) {

        final int position = helper.getPosition();

        if (checkStates == null || checkStates.size() < getData().size()) {
            initCheckStates();
        }

        FmCheckBox checkbox = helper.getView(R.id.checkbox);

        //选择模式
        if (isInSelectMode) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }

        checkbox.setChecked(checkStates.valueAt(helper.getAdapterPosition()));

        //checkbox 选择判断
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("check", position + ":" + isChecked);
                //增加点击判断 避免setChecked触发
                if (buttonView.isPressed()) {
                    checkStates.setValueAt(position, isChecked);
                }
            }
        });

        //案件相关
        String caseId = bean.getCaseId();
        helper.setText(R.id.tv_amt, bean.getOaAmt() + "元");
//        helper.setText(R.id.tv_caseId, caseId);
        helper.setText(R.id.tv_hostName, bean.getHostName());
//        helper.setText(R.id.tv_date, bean.getOaDate());
        helper.setText(R.id.tv_custName, bean.getCustName());
        helper.setText(R.id.tv_addressType, Dic.queryValue(Dic.ADDRESS, bean.getAddrType()));


        //业务id
        String bizId = bean.getAddrId();

        boolean recordCompleted = false;
        boolean takePhotoCompleted = false;
        boolean reportCompleted = false;

        if (StringUtils.isNotEmpty(bizId)) {
            recordCompleted = CaseManager.recordCompleted(bizId);
            takePhotoCompleted = CaseManager.takePhotoCompleted(bizId);
            //判断是否填写报告
            reportCompleted = CaseManager.reportCompleted(bizId);
        }


        //案件填写提示icon  拍照  录音 外访报告
        CaseFillReminder csvFillReminder = helper.getView(R.id.csv_fillReminder);
        csvFillReminder.setCameraState(takePhotoCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);
        csvFillReminder.setVoiceState(recordCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);
        csvFillReminder.setReportState(reportCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);


        //TODO 图标点击处理
        csvFillReminder.setOnActionListener(new CaseFillReminder.OnActionListener() {
            @Override
            public void onCameraPressed(View v) {
//                RxBus.get().post(BusTag.SHOW_CASE_FILL, "SHOW");
//                EventEnv env = new EventEnv(BusEvent.TAKE_PHOTO);
//                RxBus.get().post(BusTag.CASE_FILL, env);
            }

            @Override
            public void onVoicePressed(View v) {
//                RxBus.get().post(BusTag.SHOW_CASE_FILL, "SHOW");
//                EventEnv env = new EventEnv(BusEvent.RECORD);
//                RxBus.get().post(BusTag.CASE_FILL, env);
            }

            @Override
            public void onReportPressed(View v) {
//                RxBus.get().post(BusTag.SHOW_CASE_FILL, "SHOW");
//                EventEnv env = new EventEnv(BusEvent.REPORT);
//                RxBus.get().post(BusTag.CASE_FILL, env);
            }
        });


        //TODO  itemView 点击事件
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainApplication.getApplication().isCurrentCaseOnOperation()) {
                    final CommonContentDM dm = new DialogCenter(mContext).showCurrentCaseOnOperationDialog();
                    dm.setOnFooterButtonClickListener(new CommonContentDM.OnFooterButtonClickListener() {
                        @Override
                        public void onLeftClicked(View v) {
                            dm.getDialog().dismiss();
                        }

                        @Override
                        public void onRightClicked(View v) {
                            dm.getDialog().dismiss();
                        }
                    });
                    return;
                }


                //点击item 跳转
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CASE_ID, bean.getCaseId());
                bundle.putBoolean(Constants.IS_TODO_CASE, true);
                bundle.putString(Constants.ADDRESS_ID, bean.getAddrId());
                bundle.putString(Constants.VISIT_ADDRESS, bean.getVisitAddress());

                BaseFragment fragment = BaseFragment.newInstance(FragmentCaseDetail.class, bundle);
                BaseFragment parent = (BaseFragment) baseFragment.getParentFragment();
                if (parent != null) {
                    parent.startBrotherFragment(fragment, SINGLETASK);
                }

            }
        });


    }


    private int lastSelectedPosition = -1;

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public void goSelectMode() {
        isInSelectMode = true;
        initCheckStates();
        notifyDataSetChanged();
    }

    public void leaveSelectMode() {
        isInSelectMode = false;
        initCheckStates();
        notifyDataSetChanged();
    }

    public boolean inSelectMode() {
        return isInSelectMode;
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
