package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.AccountInfoBean;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.utils.DateUtil;
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

/**
 * 账户详细信息
 */
public class AccountInfoDetailActivity extends BaseActivity {
    HashMap<String, String> hashMap = new HashMap<>();
    @BindView(R.id.tv_baserial_no)
    TextView mTvBaserialNo;
    @BindView(R.id.tv_project_no)
    TextView mTvProjectNo;
    @BindView(R.id.tv_product_type)
    TextView mTvProductType;
    @BindView(R.id.tv_ovdu_amts)
    TextView mTvOvduAmts;
    @BindView(R.id.tv_od_principal_amt)
    TextView mTvOdPrincipalAmt;
    @BindView(R.id.tv_od_interest_amt)
    TextView mTvOdInterestAmt;
    @BindView(R.id.tv_od_pen_int_amt)
    TextView mTvOdPenIntAmt;
    @BindView(R.id.tv_pay_due_date)
    TextView mTvPayDueDate;
    @BindView(R.id.tv_acct_status)
    TextView mTvAcctStatus;
    @BindView(R.id.tv_overdue_date)
    TextView mTvOverdueDate;
    @BindView(R.id.tv_overdue_days)
    TextView mTvOverdueDays;


    @BindView(R.id.tv_businessrate)
    TextView mTvBusinessrate;
    private AccountInfoBean.RecordsBean mRecordsBean;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

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
        return R.layout.activity_account_info_detail;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        mRecordsBean = getIntent().getParcelableExtra("recordData");
        initDictData();
        if (mRecordsBean == null) {
            return;
        }

        mTvBaserialNo.setText(mRecordsBean.getBaserialno());
        mTvProjectNo.setText(mRecordsBean.getBacctNo());
        mTvProductType.setText(mRecordsBean.getProductType());
        mTvOvduAmts.setText(mRecordsBean.getOvduAmts());
        mTvOdPrincipalAmt.setText(mRecordsBean.getOdPrincipalAmt());
        mTvOdInterestAmt.setText(mRecordsBean.getOdInterestAmt());
        mTvOdPenIntAmt.setText(mRecordsBean.getOdPenIntAmt());
        if (mRecordsBean.getPayDueDate() > 0) {
            mTvPayDueDate.setText(DateUtil.getDate(mRecordsBean.getPayDueDate()));
        }

        mTvOverdueDays.setText(mRecordsBean.getOverdueDays() + "天");
        mTvOverdueDate.setText(mRecordsBean.getOverdueDate());
        if (hashMap != null) {
            String acctStatus = hashMap.get(mRecordsBean.getAcctStatus());
            mTvAcctStatus.setText(TextUtils.isEmpty(hashMap.get(mRecordsBean.getAcctStatus())) ? mRecordsBean.getAcctStatus() : acctStatus);
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

                    }
                });
    }


    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
