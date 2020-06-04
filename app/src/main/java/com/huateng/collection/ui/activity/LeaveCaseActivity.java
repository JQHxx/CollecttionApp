package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 留案
 */
public class LeaveCaseActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.btn_save)
    FmButton mBtnSave;
    @BindView(R.id.tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.ll_end_date)
    LinearLayout mLlEndDate;
    @BindView(R.id.tv_case_id)
    TextView mTvCaseId;
    @BindView(R.id.edt_apply_reason)
    EditText mEdtApplyReason;

    private String caseId;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_case;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        mTvCaseId.setText(caseId);
    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);
    }


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    @OnClick({R.id.ll_end_date, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_end_date:
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mTvEndDate.setText(format.format(date));
                    }
                }).build().show();
                break;
            case R.id.btn_save:

                saveData();

                break;
        }
    }

    private void saveData() {
        String stayEndDate = mTvEndDate.getText().toString();//留案截止日期
        String applyReason = mEdtApplyReason.getText().toString();//留案理由
        if (TextUtils.isEmpty(caseId)) {
            RxToast.showToast("案件ID不能为空");
            return;
        }
        if ("请选择".equals(stayEndDate)) {
            RxToast.showToast("留案截止日期不能为空");
            return;
        }

        if (TextUtils.isEmpty(applyReason)) {
            RxToast.showToast("留案原因说明不能为空");
            return;
        }
        if (applyReason.length()>200) {
            RxToast.showToast("留案原因说明大于200字");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("caseIds", caseId);
        map.put("tlrNo", Perference.getUserId());
        map.put("applyReason", applyReason);
        map.put("stayEndDate", stayEndDate);
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.CASE_RESERVE_BATCH_EXCUTE, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅之前回调回去显示加载动画
                        showLoading();
                    }
                })// 订阅之前操作在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<String>() {

                    @Override
                    public void onError(String code, String msg) {
                        hideLoading();
                        RxToast.showToast(msg);
                    }

                    @Override
                    public void onNextData(String respBase) {
                        hideLoading();
                        RxToast.showToast("留案申请操作成功");
                        finish();
                    }
                });

    }

}
