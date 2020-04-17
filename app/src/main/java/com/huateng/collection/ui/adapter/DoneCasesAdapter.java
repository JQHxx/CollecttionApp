package com.huateng.collection.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.CaseFillReminder;
import com.huateng.fm.ui.view.FmNumericProgressBar;
import com.huateng.fm.ui.widget.FmCheckBox;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;


/**
 * 发件箱adapter
 */
public class DoneCasesAdapter extends BaseQuickAdapter<RespCaseSummary, BaseViewHolder> {

    private SparseArray<Boolean> checkStates;
    private boolean isInSelectMode;

    private Context mContext;

    public DoneCasesAdapter(@LayoutRes int layoutResId, @Nullable List<RespCaseSummary> dataList, Context context) {
        super(layoutResId, dataList);
        this.mContext = context;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final RespCaseSummary bean) {

        final int position = helper.getPosition();


        if (checkStates == null || checkStates.size() < getData().size()) {
            initCheckStates();
        }

        FmCheckBox checkBox = helper.getView(R.id.checkbox);

        if (isInSelectMode) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        //设置是否选中
        checkBox.setChecked(checkStates.valueAt(helper.getPosition()));

        //checkbox 选择判断
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("check", position + ":" + isChecked);
                //增加点击判断 避免setChecked触发
                if (buttonView.isPressed()) {
                    checkStates.setValueAt(position, isChecked);
                }
            }
        });

        String caseId = bean.getCaseId();
        String addrId = bean.getAddrId();

        helper.setText(R.id.tv_amt, bean.getOaAmt() + "元");
//        helper.setText(R.id.tv_caseId, caseId);
//        helper.setText(R.id.tv_date, bean.getOaDate());
        helper.setText(R.id.tv_hostName, bean.getHostName());
        helper.setText(R.id.tv_custName, bean.getCustName());

        helper.setText(R.id.tv_addressType, Dic.queryValue(Dic.ADDRESS, bean.getAddrType()));

        boolean recordCompleted = CaseManager.recordCompleted(addrId);
        boolean takePhotoCompleted = CaseManager.takePhotoCompleted(addrId);
        boolean reportCompleted = CaseManager.reportCompleted(addrId);


        //案件填写提示icon  拍照  录音 外访报告
        CaseFillReminder csvFillReminder = helper.getView(R.id.csv_fillReminder);
        csvFillReminder.setCameraState(takePhotoCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);
        csvFillReminder.setVoiceState(recordCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);
        csvFillReminder.setReportState(reportCompleted ? CaseFillReminder.State.DONE : CaseFillReminder.State.TODO);


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectedPosition = helper.getPosition();

                //点击item 跳转

                Bundle bundle = new Bundle();
                bundle.putString(Constants.CASE_ID, bean.getCaseId());
                bundle.putBoolean(Constants.IS_TODO_CASE, false);
                bundle.putString(Constants.ADDRESS_ID, bean.getAddrId());
                bundle.putString(Constants.VISIT_ADDRESS, bean.getVisitAddress());


             /*   BaseFragment fragment = BaseFragment.newInstance(FragmentCaseDetail.class, bundle);
                BaseFragment parent = (BaseFragment) baseFragment.getParentFragment();
                if (parent != null) {
                    parent.startBrotherFragment(fragment, SINGLETASK);
                }*/

            }
        });

        //上传相关布局
        View vUpload = helper.getView(R.id.v_uploadState);

        String status = bean.getUploadStatus();
        if (Constants.CASE_NORMAL.equals(status)) {
            vUpload.setVisibility(View.GONE);
        } else {
            vUpload.setVisibility(View.VISIBLE);

            //上传进度条
            FmNumericProgressBar numericProgressBar = helper.getView(R.id.csv_uploadProgress);
            numericProgressBar.setProgress(bean.getProgress());
            //上传状态
            TextView tvUploadState = helper.getView(R.id.tv_uploadState);
            tvUploadState.setText(Constants.getCaseStatusName(status));

            //上传进度与上传状态颜色
            if (Constants.CASE_UPLOAD_ERROR.equals(status)) {
                tvUploadState.setTextColor(mContext.getResources().getColor(R.color.upload_error));
                numericProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.upload_error));
            } else if (Constants.CASE_FILE_TOO_LARGE.equals(status)) {
                tvUploadState.setTextColor(mContext.getResources().getColor(R.color.upload_warn));
                numericProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.upload_warn));
            } else {
                tvUploadState.setTextColor(mContext.getResources().getColor(R.color.uploading));
                numericProgressBar.setReachedBarColor(mContext.getResources().getColor(R.color.uploading));
            }
        }
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

    public List<Integer> getSelectedPositions() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < checkStates.size(); i++) {
//            Log.i("checkstate  size", "------------->" + checkStates.size());
//            Log.i("i", "------------->" + i);
            boolean b = checkStates.get(i);
            if (b) {
                positions.add(i);
            }
        }
        return positions;
    }


    //TODO  模拟进度 采用timeTask时  快速切换TAbBUG
    public void mockProgress(final int position) {
        final RespCaseSummary summary = getItem(position);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int progress = summary.getProgress();
                        if (progress < 100) {
                            summary.setProgress(summary.getProgress() + 5);
                            summary.setUploadStatus(Constants.CASE_UPLOADING);
                        } else if (progress >= 100) {
                            summary.setProgress(100);
                            summary.setUploadStatus(Constants.CASE_UPLOADED);
                            summary.setUploaded(true);
                            SugarRecord.save(summary);
                            timer.cancel();
                           // ((FragmentDoneCaseChooser) baseFragment).doneUploadCase();
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }, 1000, 100);
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


    //    初始化
    public void initCheckStates() {
        checkStates = new SparseArray<>();
        for (int i = 0; i < getData().size(); i++) {
            checkStates.put(i, false);
        }
    }

}
