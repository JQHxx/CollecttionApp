package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.CreditCardBean;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.widget.Watermark;
import com.orm.SugarRecord;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreditCardDetailActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_credit_card_no)
    TextView mTvCreditCardNo;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.tv_business_line)
    TextView mTvBusinessLine;
    @BindView(R.id.tv_open_acct_date)
    TextView mTvOpenAcctDate;
    @BindView(R.id.tv_open_bank_name)
    TextView mTvOpenBankName;
    @BindView(R.id.tv_statement_date)
    TextView mTvStatementDate;
    @BindView(R.id.tv_ovdu_amts)
    TextView mTvOvduAmts;
    @BindView(R.id.tv_ovdu_principal_amts)
    TextView mTvOvduPrincipalAmts;
    @BindView(R.id.tv_overdraw_amt)
    TextView mTvOverdrawAmt;
    @BindView(R.id.tv_current_loan_cash_amount)
    TextView mTvCurrentLoanCashAmount;
    @BindView(R.id.tv_penal_amt)
    TextView mTvPenalAmt;
    @BindView(R.id.tv_interest_amt)
    TextView mTvInterestAmt;
    @BindView(R.id.tv_period_rest_pri)
    TextView mTvPeriodRestPri;
    @BindView(R.id.tv_period_rest_fee)
    TextView mTvPeriodRestFee;
    @BindView(R.id.tv_overdue_periods)
    TextView mTvOverduePeriods;
    @BindView(R.id.tv_wrof_flag)
    TextView mTvWrofFlag;
    private CreditCardBean.RecordsBean recordsBean;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId()  + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
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
        return R.layout.activity_credit_card_detail;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        recordsBean = getIntent().getParcelableExtra("recordsBean");
        if (recordsBean == null) {
            return;
        }

        initDictData();

        Log.e("nb",recordsBean.toString());
        mTvCreditCardNo.setText(recordsBean.getCreditCardNo());
        //卡种
       // mTvProductName.setText(recordsBean.getProductName());
        if("0000".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("总行业务");
        }else if("0001".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("个人条线业务");
        }else if("0002".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("个人条线业务");
        }else if("0003".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("零售条线业务");
        }else if("0004".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("尊尚卡业务");
        }else if("1000".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("总行业务剥离");
        }else if("1002".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("个人业务剥离");
        }else if("1003".equals(recordsBean.getBusinessLine())) {
            mTvBusinessLine.setText("零售业务剥离");
        }
        if( recordsBean.getOpenAcctDate().length() == 8) {

            mTvOpenAcctDate.setText(recordsBean.getOpenAcctDate().substring(0,4)+"-"+recordsBean.getOpenAcctDate().substring(4,6)+"-"+recordsBean.getOpenAcctDate().substring(6,8));
        }else {
            mTvOpenAcctDate.setText("");
        }

        mTvOpenBankName.setText(recordsBean.getOpenBankName());
        mTvStatementDate.setText(recordsBean.getStatementDate());
        mTvOvduAmts.setText(TextUtils.isEmpty(recordsBean.getAcctBalance())?"0.00":recordsBean.getAcctBalance()+"");
        mTvOvduPrincipalAmts.setText(recordsBean.getOvduPrincipalAmts());
        mTvOverdrawAmt.setText(recordsBean.getOverdrawAmt());
        mTvCurrentLoanCashAmount.setText(recordsBean.getCurrAcctBalance());
        mTvPenalAmt.setText(recordsBean.getPenalAmt());
        mTvInterestAmt.setText(recordsBean.getInterestAmt());
        mTvPeriodRestPri.setText(recordsBean.getPeriodRestPri());
        mTvPeriodRestFee.setText(recordsBean.getPeriodRestFee());
        Log.e("nb",recordsBean.getPeriodRestFee());
        mTvOverduePeriods.setText(recordsBean.getOverPeriodRmb()+"");
        if("0".equals(recordsBean.getWrofFlag())) {
            mTvWrofFlag.setText("未核销");
        }else if("1".equals(recordsBean.getWrofFlag())) {
            mTvWrofFlag.setText("部分核销");
        }else if("2".equals(recordsBean.getWrofFlag())) {
            mTvWrofFlag.setText("核销");
        }else {
            mTvWrofFlag.setText("");
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



    private void initDictData() {
        Observable.create(new ObservableOnSubscribe<HashMap<String, String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<HashMap<String, String>> e) throws Exception {
                //   Log.e(TAG, "Observable thread is : " + Thread.currentThread().getName());
                List<DictItemBean> dictDataBeans = SugarRecord.find(DictItemBean.class, "DICT_CODE=?", "PRODUCTCODE");
                HashMap<String, String> hashMap = new HashMap<>();
                for (DictItemBean dictItemBean : dictDataBeans) {
                    // Log.e("nb", dictItemBean.getDescription() + ":" + dictItemBean.getDataCode());
                    hashMap.put(dictItemBean.getDataVal(), dictItemBean.getDescription());
                }
                e.onNext(hashMap);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HashMap<String, String>>() {
                    @Override
                    public void accept(HashMap<String, String> stringStringHashMap) throws Exception {

                            //  mTvProductType.setText(mRecordsBean.getProductType());
                            if (stringStringHashMap != null) {
                                String cardType = stringStringHashMap.get(recordsBean.getCardType());
                                mTvProductName.setText(TextUtils.isEmpty(cardType) ? recordsBean.getCardType() : cardType);
                            }
                    }
                });
    }



    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
