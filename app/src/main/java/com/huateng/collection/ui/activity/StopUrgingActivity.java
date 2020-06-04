package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.SystemUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 停催管理
 */
public class StopUrgingActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_case_id)
    TextView mTvCaseId;
    @BindView(R.id.tv_stop_call_reason)
    TextView mTvStopCallReason;
    @BindView(R.id.ll_stop_call_reason)
    LinearLayout mLlStopCallReason;
    @BindView(R.id.tv_stop_call_start_date)
    TextView mTvStopCallStartDate;
    @BindView(R.id.edt_payment_acct_no)
    EditText mEdtPaymentAcctNo;
    @BindView(R.id.tv_stop_call_end_date)
    TextView mTvStopCallEndDate;
    @BindView(R.id.ll_stop_call_end_date)
    LinearLayout mLlStopCallEndDate;
    @BindView(R.id.check_forever_stop)
    CheckBox mCheckForeverStop;
    @BindView(R.id.ll_for_ever_stop)
    LinearLayout mLlForEverStop;
    @BindView(R.id.edt_stop_call_reason)
    EditText mEdtStopCallReason;
    @BindView(R.id.btn_send)
    FmButton mBtnSend;

    private String caseId;

    private String stopCallReason;//停催原因

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
        return R.layout.activity_stop_urging;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        mTvStopCallStartDate.setText(formatter.format(new Date()));
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


    @OnClick({R.id.ll_stop_call_reason, R.id.ll_stop_call_end_date, R.id.btn_send})
    public void onClick(View view) {
        List<BottomDialogBean> list = new ArrayList<>();
        switch (view.getId()) {

            case R.id.ll_stop_call_reason:
                list.clear();
                list.add(new BottomDialogBean("争议调查", "C01N"));
                list.add(new BottomDialogBean("卡人死亡", "C02N"));
                list.add(new BottomDialogBean("其他", "C04N"));
                list.add(new BottomDialogBean("欺诈调查", "C05N"));
                list.add(new BottomDialogBean("投诉处理", "C06N"));
                list.add(new BottomDialogBean("第三人要求", "C03N"));
                BottomDialogFragment.newInstance().setData("停催原因", list, false, true)
                        .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                            @Override
                            public void onItemClick(BottomDialogBean bottomDialogBean) {
                                mTvStopCallReason.setText(bottomDialogBean.getTitle());
                                stopCallReason = bottomDialogBean.getId();
                            }
                        })
                        .show(getSupportFragmentManager());
                break;
            case R.id.ll_stop_call_end_date:
                SystemUtils.hideInputmethod(mEdtPaymentAcctNo);
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mTvStopCallEndDate.setText(format.format(date));
                    }
                }).build().show();

                break;
            case R.id.btn_send:
                sendData();
                //finish();
                break;
        }
    }

    //
    private void sendData() {
        boolean checked = mCheckForeverStop.isChecked();
        String applyReason = mEdtStopCallReason.getText().toString();
        String stopCallStartDate = mTvStopCallStartDate.getText().toString();
        String stopCallEndDate = mTvStopCallEndDate.getText().toString();
        String foreverStopCall = mCheckForeverStop.isChecked() ? "1" : "0";
        if (TextUtils.isEmpty(stopCallReason)) {
            RxToast.showToast("停催原因不能为空");
            return;
        }

        if (TextUtils.isEmpty(applyReason)) {
            RxToast.showToast("停催原因说明为空");
            return;
        }

        if(!TextUtils.isEmpty(applyReason) && applyReason.length()>200) {
            RxToast.showToast("停催原因说明不能大于200字");
            return;
        }

        if ("0".equals(foreverStopCall) && "请选择".equals(stopCallEndDate)) {
            //不是永久停催
            RxToast.showToast("案件结束日期不能为空");
            return;
        }

        Map<String, String> map = new HashMap<>();
        if (!checked) {
            map.put("stopCollEndDate", stopCallEndDate);
        }
        map.put("foreverStopCall", checked ? "1" : "0");
        map.put("caseIds", caseId);
        map.put("tlrNo", Perference.getUserId());
        map.put("applyReason", applyReason);
        map.put("stopCollBegDate", stopCallStartDate);
        map.put("stopCollReason", stopCallReason);
        map.put("foreverStopCall", foreverStopCall);

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.STOP_CALL_BATCH_EXCUTE, map)
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
                        Log.e("nb", code + ":" + msg);
                    }

                    @Override
                    public void onNextData(String s) {
                        hideLoading();
                        RxToast.showToast("停催申请操作成功");
                        finish();
                    }
                });

    }


    private Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
