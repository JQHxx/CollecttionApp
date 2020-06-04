package com.huateng.collection.ui.report.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.huateng.collection.bean.ReportListBean;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.ui.report.contract.ReportContract;
import com.huateng.collection.ui.report.presenter.ReportPresenter;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.tools.SystemUtils;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 * author: yichuan
 * Created on: 2020-04-01 18:03
 * description:
 */
public class ReportActivity2 extends BaseActivity<ReportPresenter> implements ReportContract.View {

    @BindView(R.id.tv_cust_name)
    TextView mTvCustName;
    @BindView(R.id.edt_visit_name)
    EditText mEdtVisitName;
    @BindView(R.id.tv_visit_date)
    TextView mTvVisitDate;
    @BindView(R.id.ll_visit_date)
    LinearLayout mLlVisitDate;
    @BindView(R.id.edt_visit_address)
    EditText mEdtVisitAddress;
    @BindView(R.id.tv_is_self)
    TextView mTvIsSelf;
    @BindView(R.id.tv_is_work)
    TextView mTvIsWork;
    @BindView(R.id.ll_is_work)
    LinearLayout mLlIsWork;
    @BindView(R.id.tv_is_marriage)
    TextView mTvIsMarriage;
    @BindView(R.id.ll_is_marriage)
    LinearLayout mLlIsMarriage;
    @BindView(R.id.edt_overdue_cause)
    EditText mEdtOverdueCause;
    @BindView(R.id.edt_other_valid_address)
    EditText mEdtOtherValidAddress;
    @BindView(R.id.edt_car_msg)
    EditText mEdtCarMsg;
    @BindView(R.id.edt_home_msg)
    EditText mEdtHomeMsg;
    @BindView(R.id.edt_provident_fund)
    EditText mEdtProvidentFund;
    @BindView(R.id.edt_investment_msg)
    EditText mEdtInvestmentMsg;
    @BindView(R.id.edt_debt_msg)
    EditText mEdtDebtMsg;
    @BindView(R.id.edt_other_assets_info)
    EditText mEdtOtherAssetsInfo;
    @BindView(R.id.edt_detail_msg)
    EditText mEdtDetailMsg;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    private String custId;
    private String caseId;
    private String custName;
    private boolean isAdd;
    private ReportListBean.RecordsBean recordsBean;

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
       /* Watermark.getInstance()
                .setTextColor(getResources().getColor(R.color.dialogplus_card_shadow))
                .setTextSize(12.0f)
                .setText("测试文本")
                .show(this);*/

        initListener();
    }

    private void initListener() {
        mRxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectData();
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
        return R.layout.activity_report2;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        isAdd = getIntent().getBooleanExtra("isAdd",true);
        Log.e("nb","isAdd--->"+isAdd);
        if (!isAdd) {
            recordsBean = getIntent().getParcelableExtra("reportData");
        }
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        custName = getIntent().getStringExtra(Constants.CUST_NAME);
        mTvCustName.setText(custName);
        if (recordsBean != null) {
            mTvCustName.setText(recordsBean.getCustName());
            mEdtVisitName.setText(recordsBean.getVisitName());
            mTvVisitDate.setText(recordsBean.getVisitDate());
            mEdtVisitAddress.setText(recordsBean.getVisitAddress());
            mTvIsSelf.setText("1".equals(recordsBean.getMeetHimselfFlag()) ? "是" : "否");
            mTvIsWork.setText("1".equals(recordsBean.getUnitIncumbency()) ? "已入职" : "未入职");
            mTvIsMarriage.setText("1".equals(recordsBean.getMaritalStatus()) ? "已婚" : "未婚");
            mEdtOverdueCause.setText(recordsBean.getOverdueCause());
            mEdtOtherValidAddress.setText(recordsBean.getOtherValidAddress());
            mEdtCarMsg.setText(recordsBean.getCarInfo());
            mEdtHomeMsg.setText(recordsBean.getHouseInfo());
            mEdtProvidentFund.setText(recordsBean.getAcctFundInfo());
            mEdtInvestmentMsg.setText(recordsBean.getInvestmentInfo());
            mEdtDebtMsg.setText(recordsBean.getDebtIndo());
            mEdtOtherAssetsInfo.setText(recordsBean.getOtherAssetsInfo());
            mEdtDetailMsg.setText(recordsBean.getDetailedContent());
        }
    }



    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

    }


    //保存已填信息
    private void collectData() {
        String visitName = mEdtVisitName.getText().toString().trim();//外访人员名称
        String visitDate = mTvVisitDate.getText().toString();//外访日期
        String visitAddress = mEdtVisitAddress.getText().toString();//外访地址
        String meetHimselfFlag = mTvIsSelf.getText().toString();//是否本人
        String incumbencyFlag = mTvIsWork.getText().toString();//是否在职
        String maritalStatus = mTvIsMarriage.getText().toString();//婚姻状况
        String otherValidAddress = mEdtOtherValidAddress.getText().toString();//其他有限地址
        String carInfo = mEdtCarMsg.getText().toString();//车辆信息
        String houseInfo = mEdtHomeMsg.getText().toString();//房屋信息
        String acctFundInfo = mEdtProvidentFund.getText().toString();//公积金信息
        String investmentInfo = mEdtInvestmentMsg.getText().toString();//投资信息
        String debtInfo = mEdtDebtMsg.getText().toString();//外债信息
        String otherAssetsInfo = mEdtOtherAssetsInfo.getText().toString();//其他资产
        String overdueCause = mEdtOverdueCause.getText().toString().trim();//逾期原因
        String detailInfo = mEdtDetailMsg.getText().toString().trim();//详细信息

        if (TextUtils.isEmpty(caseId)) {
            RxToast.showToast("案件ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(custName)) {
            RxToast.showToast("客户名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(custId)) {
            RxToast.showToast("客户ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(visitName)) {
            RxToast.showToast("外访人员名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(visitDate)) {
            RxToast.showToast("外访时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(visitAddress)) {
            RxToast.showToast("外访地址不能为空");
            return;
        }



        if(!TextUtils.isEmpty(otherValidAddress) && otherValidAddress.length()>200) {
            RxToast.showToast("其他有效地址内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(overdueCause) && overdueCause.length()>200) {
            RxToast.showToast("逾期原因内容不能超过200字");
            return;
        }

       if(!TextUtils.isEmpty(carInfo) && carInfo.length()>200) {
           RxToast.showToast("车辆信息内容不能超过200字");
           return;
       }
        if(!TextUtils.isEmpty(houseInfo) && houseInfo.length()>200) {
            RxToast.showToast("房屋信息内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(acctFundInfo) && acctFundInfo.length()>200) {
            RxToast.showToast("公积金信息内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(investmentInfo) && investmentInfo.length()>200) {
            RxToast.showToast("投资信息内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(debtInfo) && debtInfo.length()>200) {
            RxToast.showToast("外债信息内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(otherAssetsInfo) && otherAssetsInfo.length()>200) {
            RxToast.showToast("其他资产信息内容不能超过200字");
            return;
        }
        if(!TextUtils.isEmpty(detailInfo) && detailInfo.length()>200) {
            RxToast.showToast("详细信息内容不能超过200字");
            return;
        }


        Map<String, String> map = new HashMap<>();
        Log.e("nb", "isadd:" + isAdd);
        if (!isAdd) {
             map.put("id", recordsBean.getId());
        }
        map.put("caseId", caseId);
        map.put("custName", custName);
        map.put("custNo", custId);
        map.put("visitName", visitName);
        map.put("visitDate", visitDate);
        map.put("visitAddress", visitAddress);
        map.put("tlrNo", Perference.getUserId());
        map.put("overdueCause", overdueCause);
        map.put("meetHimselfFlag", "是".equals(meetHimselfFlag) ? "0" : "1");
        map.put("unitIncumbency", "已入职".equals(incumbencyFlag) ? "1" : "0");
        map.put("maritalStatus", "未婚".equals(maritalStatus) ? "0" : "1");
        map.put("otherValidAddress", otherValidAddress);
        map.put("carInfo", carInfo);
        map.put("houseInfo", houseInfo);
        map.put("acctFundInfo", acctFundInfo);
        map.put("investmentInfo", investmentInfo);
        map.put("debtIndo", debtInfo);
        map.put("otherAssetsInfo", otherAssetsInfo);
        map.put("detailedContent", detailInfo);
        map.put("updateFlag", "N");


        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.INDERT_OR_UPD_CUST_REPORT_INFO, map)
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
                        if (isFinishing()) {
                            return;
                        }

                        if (TextUtils.isEmpty(msg)) {
                            return;

                        }
                        hideLoading();
                        RxToast.showToast(msg);
                    }

                    @Override
                    public void onNextData(String s) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                        EventBus.getDefault().post(new EventBean(BusEvent.REPORT_REFRESH));
                        if (isAdd) {
                            RxToast.showToast("客户调查报告录入完成");
                        } else {
                            RxToast.showToast("客户调查报告修改完成");
                        }

                        finish();



                    }
                });
    }

    public String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    @OnClick({R.id.ll_is_self, R.id.ll_is_work, R.id.ll_is_marriage, R.id.ll_visit_date})
    public void onClick(View view) {
        List<BottomDialogBean> list = new ArrayList<>();
        switch (view.getId()) {
            case R.id.ll_is_self:
                list.clear();
                list.add(new BottomDialogBean("是", "0"));
                list.add(new BottomDialogBean("否", "1"));
                BottomDialogFragment.newInstance().setData("是否见到本人", list, false, true)
                        .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                            @Override
                            public void onItemClick(BottomDialogBean bottomDialogBean) {
                                mTvIsSelf.setText(bottomDialogBean.getTitle());
                            }
                        })
                        .show(getSupportFragmentManager());


                break;
            case R.id.ll_is_work:
                list.clear();
                list.add(new BottomDialogBean("已入职", "1"));
                list.add(new BottomDialogBean("未入职", "0"));
                BottomDialogFragment.newInstance().setData("在职情况", list, false, true)
                        .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                            @Override
                            public void onItemClick(BottomDialogBean bottomDialogBean) {
                                mTvIsWork.setText(bottomDialogBean.getTitle());
                            }
                        })
                        .show(getSupportFragmentManager());


                break;
            case R.id.ll_is_marriage:
                list.clear();
                list.add(new BottomDialogBean("已婚", "1"));
                list.add(new BottomDialogBean("未婚", "0"));
                BottomDialogFragment.newInstance().setData("婚姻状况", list, false, true)
                        .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                            @Override
                            public void onItemClick(BottomDialogBean bottomDialogBean) {
                                mTvIsMarriage.setText(bottomDialogBean.getTitle());
                            }
                        })
                        .show(getSupportFragmentManager());

                break;
            case R.id.ll_visit_date:
                SystemUtils.hideInputmethod(view);
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        mTvVisitDate.setText(format.format(date));
                    }
                }).build().show();
                break;
        }
    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
