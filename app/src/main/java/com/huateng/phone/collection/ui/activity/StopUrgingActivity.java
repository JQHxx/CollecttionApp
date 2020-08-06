package com.huateng.phone.collection.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.ui.dialog.BottomDialogFragment;
import com.huateng.phone.collection.utils.DateUtil;
import com.huateng.phone.collection.utils.SoftKeyBoardListener;
import com.huateng.phone.collection.widget.Watermark;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tools.SystemUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * 停催管理
 */
public class StopUrgingActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
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
    @BindView(R.id.ll_for_ever_stop)
    LinearLayout mLlForEverStop;
    @BindView(R.id.edt_stop_call_reason)
    EditText mEdtStopCallReason;
    @BindView(R.id.btn_send)
    FmButton mBtnSend;
    @BindView(R.id.tv_forever_stop)
    TextView mTvForeverStop;
    @BindView(R.id.btn_send2)
    FmButton mBtnSend2;
    @BindView(R.id.ll_send)
    LinearLayout mLlSend;

    private String caseId;

    private String stopCallReason;//停催原因

    private boolean stopUrging = false;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RxTextView.textChanges(mEdtPaymentAcctNo).debounce(1500, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                // .subscribeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        // Log.e("nb", "12345");
                        if (charSequence.length() > 0) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            mTvForeverStop.setText("否");
                            try {
                                Date date2 = format.parse(mTvStopCallStartDate.getText().toString());

                                if (!"0".equals(charSequence)) {
                                    //  Log.e("nb",date2.getTime()+":"+Long.valueOf(charSequence.toString()) * 24 * 60 * 60 * 1000+":"+charSequence.toString());
                                    long l = date2.getTime() + Long.valueOf(charSequence.toString()) * 24 * 60 * 60 * 1000;
                                    mTvStopCallEndDate.setText(DateUtil.getDate(l));

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });

        mEdtStopCallReason.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() > 200) {
                    RxToast.showToast("停催原因不能超过200字");
                    mEdtStopCallReason.setText(s.toString().substring(0, 200));
                }
            }
        });


        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

            @Override
            public void keyBoardShow(int height) {
                Log.e("nb", "height" + height);
                mLlSend.setVisibility(View.GONE);
                mBtnSend.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.e("nb", "height" + height);
                mLlSend.setVisibility(View.VISIBLE);
                mBtnSend.setVisibility(View.INVISIBLE);
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

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

    }


    @OnClick({R.id.ll_stop_call_reason, R.id.ll_stop_call_end_date, R.id.btn_send, R.id.btn_send2, R.id.ll_for_ever_stop})
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
                        String format1 = format.format(date);
                        String format2 = format.format(new Date(System.currentTimeMillis()));
                        if (format1.compareTo(format2) == -1) {
                            RxToast.showToast("留案截止日期不能小于当前日期");
                            return;
                        }
                        mTvStopCallEndDate.setText(format.format(date));
                        mTvForeverStop.setText("否");
                        try {
                            Date date2 = format.parse(mTvStopCallStartDate.getText().toString());
                            long days = (date.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24;
                            mEdtPaymentAcctNo.setText(String.valueOf(days));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }).build().show();

                break;
            case R.id.btn_send:
            case R.id.btn_send2:
                if (!stopUrging) {
                    stopUrging = true;
                    sendData();
                }


                break;

            case R.id.ll_for_ever_stop:

                list.clear();
                list.add(new BottomDialogBean("是", "0"));
                list.add(new BottomDialogBean("否", "1"));
                BottomDialogFragment.newInstance().setData("是否永久停催", list, false, true)
                        .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                            @Override
                            public void onItemClick(BottomDialogBean bottomDialogBean) {
                                if ("0".equals(bottomDialogBean.getId())) {
                                    mEdtPaymentAcctNo.setFocusable(false);
                                    mEdtPaymentAcctNo.setFocusableInTouchMode(false);
                                    mEdtPaymentAcctNo.setText("");
                                    mTvForeverStop.setText("是");
                                    mTvStopCallEndDate.setText("请选择");
                                    mLlStopCallEndDate.setClickable(false);
                                } else if ("1".equals(bottomDialogBean.getId())) {
                                    mEdtPaymentAcctNo.setFocusableInTouchMode(true);
                                    mEdtPaymentAcctNo.setFocusable(true);
                                    mEdtPaymentAcctNo.requestFocus();
                                    mLlStopCallEndDate.setClickable(true);
                                    mTvForeverStop.setText("否");
                                }
                            }
                        })
                        .show(getSupportFragmentManager());
                break;
        }
    }

    //
    private void sendData() {
        String applyReason = mEdtStopCallReason.getText().toString();
        String stopCallStartDate = mTvStopCallStartDate.getText().toString();
        String stopCallEndDate = mTvStopCallEndDate.getText().toString();
        String foreverStopCall = mTvForeverStop.getText().toString();

        if ("是".equals(foreverStopCall)) {
            foreverStopCall = "1";
        } else {
            foreverStopCall = "0";
        }
        if (TextUtils.isEmpty(stopCallReason)) {
            RxToast.showToast("停催原因不能为空");
            stopUrging = false;
            return;
        }

        if (TextUtils.isEmpty(applyReason)) {
            RxToast.showToast("停催原因说明不能为空");
            stopUrging = false;
            return;
        }

        if (!TextUtils.isEmpty(applyReason) && applyReason.length() > 200) {
            RxToast.showToast("停催原因说明不能大于200字");
            stopUrging = false;
            return;
        }

        if ("0".equals(foreverStopCall) && "请选择".equals(stopCallEndDate)) {
            //不是永久停催
            RxToast.showToast("案件结束日期不能为空");
            stopUrging = false;
            return;
        }

        Map<String, Object> map = new HashMap<>();
        if ("0".equals(foreverStopCall)) {
            map.put("stopCollEndDate", stopCallEndDate);
        }
        map.put("foreverStopCall", foreverStopCall);
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
                        stopUrging = false;
                    }

                    @Override
                    public void onNextData(String s) {
                        hideLoading();
                        stopUrging = false;
                        RxToast.showToast("停催申请操作成功");
                        finish();
                    }
                });

    }


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
