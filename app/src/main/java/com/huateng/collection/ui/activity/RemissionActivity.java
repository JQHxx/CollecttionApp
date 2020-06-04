package com.huateng.collection.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aes_util.AESUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.bean.BizAcctEventBean;
import com.huateng.collection.bean.BizAcctInfoBean;
import com.huateng.collection.bean.BizAcctItemBean;
import com.huateng.collection.bean.CustInfoBean;
import com.huateng.collection.bean.ReduceRequestBean;
import com.huateng.collection.ui.adapter.BizAcctAdapter;
import com.huateng.collection.ui.adapter.BizAcctCardAdapter;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.ui.remission.contract.RemissionContract;
import com.huateng.collection.ui.remission.presenter.RemissionPresenter;
import com.huateng.collection.utils.DateUtil;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.UploadObserver;
import com.huateng.network.bean.ResponseStructure;
import com.huateng.network.upload.UploadParam;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.ArithUtil;
import com.tools.utils.GsonUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.rosuh.filepicker.config.FilePickerManager;

/**
 * 减免申请
 */
public class RemissionActivity extends BaseActivity<RemissionPresenter> implements RemissionContract.View {
    private boolean isUpload;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.recyclerview_account)
    RecyclerView mRecyclerviewAccount;
    @BindView(R.id.recyclerview_card)
    RecyclerView mRecyclerviewCard;
    @BindView(R.id.btn_save)
    FmButton mBtnSave;
    @BindView(R.id.tv_cust_name)
    TextView mTvCustName;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_cust_id_card)
    TextView mTvCustIdCard;
    @BindView(R.id.edt_overdue_cause)
    EditText mEdtOverdueCause;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_reduce_pri)
    TextView mTvReducePri;
    @BindView(R.id.tv_reduce_int)
    TextView mTvReduceInt;
    @BindView(R.id.tv_reduce_total)
    TextView mTvReduceTotal;
    @BindView(R.id.tv_reduce_pri_loan)
    TextView mTvReducePriLoan;
    @BindView(R.id.tv_reduce_int_loan)
    TextView mTvReduceIntLoan;
    @BindView(R.id.tv_reduce_total_loan)
    TextView mTvReduceTotalLoan;
    @BindView(R.id.tv_reduce_pri_card)
    TextView mTvReducePriCard;
    @BindView(R.id.tv_reduce_int_card)
    TextView mTvReduceIntCard;
    @BindView(R.id.tv_reduce_total_card)
    TextView mTvReduceTotalCard;


    private BizAcctAdapter mAdapter;
    private BizAcctCardAdapter mBizAcctCardAdapter;
    private String custId;
    List<BizAcctItemBean> mList = new ArrayList();
    List<BizAcctItemBean> mAcctCardList = new ArrayList<>();
    private String caseId;
    // private CustInfoBean mCustInfoBean;

    @Override
    protected RemissionPresenter createPresenter() {
        return new RemissionPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initRecyclerview();

        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRxTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerManager.INSTANCE
                        .from(RemissionActivity.this)
                        // .enableSingleChoice()
                        .maxSelectable(6)
                        .forResult(FilePickerManager.REQUEST_CODE);


            }
        });
    }

    private void initRecyclerview() {
        //展示贷款信息列表
        mRecyclerviewAccount.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BizAcctAdapter();
        mRecyclerviewAccount.setAdapter(mAdapter);

        mRecyclerviewCard.setLayoutManager(new LinearLayoutManager(this));
        mBizAcctCardAdapter = new BizAcctCardAdapter();
        mRecyclerviewCard.setAdapter(mBizAcctCardAdapter);

        mBizAcctCardAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_is_ear_settlement) {
                    //
                    List<BottomDialogBean> list = new ArrayList<>();
                    list.add(new BottomDialogBean("是", "Y"));
                    list.add(new BottomDialogBean("否", "N"));
                    BottomDialogFragment.newInstance().setData("是否申请分期提前结清", list, false, true)
                            .setDialogItemClicklistener(new BottomDialogFragment.DialogItemClick() {
                                @Override
                                public void onItemClick(BottomDialogBean bottomDialogBean) {
                                    // mTvIsSelf.setText(bottomDialogBean.getTitle());
                                    mAcctCardList.get(position).setIsEarSettlement(bottomDialogBean.getId());
                                    mBizAcctCardAdapter.notifyItemChanged(position);
                                }
                            })
                            .show(getSupportFragmentManager());


                }
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
        return R.layout.activity_remission;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        mPresenter.loadCustInfo(custId);
        mPresenter.loadData(custId);

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


    @OnClick({R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_save:
                // mPresenter.reliefBatchExcute(custId);
                reliefBatchExcute();
                break;
        }
    }

    private void reliefBatchExcute() {
        String certNo = mTvCustIdCard.getText().toString();
        String applyReason = mEdtOverdueCause.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("custNo", custId);
        map.put("caseId", caseId);
        map.put("certNo", certNo);
        map.put("applyReason", applyReason);
        map.put("applyUser", Perference.getUserId());
        map.put("relaBusiCode", custId + "_" + caseId);
        map.put("reducePriCard", mTvReducePriCard.getText().toString());
        map.put("reduceIntCard", mTvReduceIntCard.getText().toString());
        map.put("reduceTotalCard", mTvReduceTotalCard.getText().toString());

        map.put("reducePriLoan", mTvReducePriLoan.getText().toString());
        map.put("reduceIntLoan", mTvReduceIntLoan.getText().toString());
        map.put("reduceTotalLoan", mTvReduceTotalLoan.getText().toString());

        map.put("reducePri", mTvReducePri.getText().toString());
        map.put("reduceInt", mTvReduceInt.getText().toString());
        map.put("reduceTotal", mTvReduceTotal.getText().toString());
        //贷款
        List<ReduceRequestBean> reduceRequestBeans = new ArrayList<>();
        for (BizAcctItemBean bizAcctItemBean : mList) {
            ReduceRequestBean reduceRequestBean = new ReduceRequestBean();
            reduceRequestBean.setProductNo(bizAcctItemBean.getProductNo());
            reduceRequestBean.setLoanNo(bizAcctItemBean.getLoanNo());
            reduceRequestBean.setLoanAmt(String.valueOf(bizAcctItemBean.getLoanAmt()));
            reduceRequestBean.setEndDate(DateUtil.getDate(bizAcctItemBean.getEndDate()));
            reduceRequestBean.setOverdueDays(bizAcctItemBean.getOverdueDays());
            reduceRequestBean.setLoanPri(String.valueOf(bizAcctItemBean.getLoanPri()));
            reduceRequestBean.setLoanInt(String.valueOf(bizAcctItemBean.getLoanInt()));
            reduceRequestBean.setLoanTotal(String.valueOf(bizAcctItemBean.getLoanTotal()));
            reduceRequestBean.setReducePri(String.valueOf(bizAcctItemBean.getReducePri()));
            reduceRequestBean.setReduceInt(String.valueOf(bizAcctItemBean.getReduceInt()));
            reduceRequestBean.setReduceTotal(String.valueOf(bizAcctItemBean.getReduceTotal()));
            reduceRequestBean.setPlanRepayTotal(bizAcctItemBean.getPlanRepayTotal());
            reduceRequestBean.setBusinessType(bizAcctItemBean.getBusinessType());
            reduceRequestBeans.add(reduceRequestBean);
        }
        //信用卡
        for (BizAcctItemBean bizAcctItemBean : mAcctCardList) {
            ReduceRequestBean reduceRequestBean = new ReduceRequestBean();
            reduceRequestBean.setAcctNo(bizAcctItemBean.getAcctNo());
            reduceRequestBean.setShouldBreach(String.valueOf(bizAcctItemBean.getShouldBreach()));
            reduceRequestBean.setPenalAmt(bizAcctItemBean.getPenalAmt());
            reduceRequestBean.setReduceFee(bizAcctItemBean.getReduceFee());
            reduceRequestBean.setReduceOth(bizAcctItemBean.getReduceOth());
            reduceRequestBean.setIsEarsettlement(bizAcctItemBean.getIsEarSettlement());
            reduceRequestBean.setBusinessType(bizAcctItemBean.getBusinessType());
            reduceRequestBean.setOverdueDays(bizAcctItemBean.getOverdueDays());
            reduceRequestBean.setLoanPri(String.valueOf(bizAcctItemBean.getLoanPri()));
            reduceRequestBean.setLoanInt(String.valueOf(bizAcctItemBean.getLoanInt()));
            reduceRequestBean.setLoanTotal(String.valueOf(bizAcctItemBean.getLoanTotal()));
            reduceRequestBean.setReducePri(String.valueOf(bizAcctItemBean.getReducePri()));
            reduceRequestBean.setReduceInt(String.valueOf(bizAcctItemBean.getReduceInt()));
            reduceRequestBean.setReduceTotal(String.valueOf(bizAcctItemBean.getReduceTotal()));
            reduceRequestBean.setPlanRepayTotal(bizAcctItemBean.getPlanRepayTotal());
            reduceRequestBeans.add(reduceRequestBean);
        }

        map.put("applyLoansList", reduceRequestBeans);

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.APPLICATION_RELIEF_BATCH_EXCURE, map)
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
                        RxToast.showToast("减免申请操作成功");
                        finish();
                    }
                });


    }

    /**
     * 设置减免数据列表
     *
     * @param bizAcctInfoBean
     */
    @Override
    public void setBizAcctInfo(BizAcctInfoBean bizAcctInfoBean) {
        mList.clear();
        //贷款账户信息
        List<BizAcctInfoBean.BizAcctAccountBean> bizAcctAccount = bizAcctInfoBean.getBizAcctAccount();
        //信用卡信息
        List<BizAcctInfoBean.BizAcctCardBean> bizAcctCard = bizAcctInfoBean.getBizAcctCard();

        for (BizAcctInfoBean.BizAcctAccountBean bizAcctAccountBean : bizAcctAccount) {
            BizAcctItemBean bizAcctItemBean = new BizAcctItemBean();
            bizAcctItemBean.setProductName(bizAcctAccountBean.getProductName());
            bizAcctItemBean.setProductNo(bizAcctAccountBean.getProductNo());
            bizAcctItemBean.setLoanNo(bizAcctAccountBean.getBacctNo());
            bizAcctItemBean.setLoanAmt(bizAcctAccountBean.getLoanAmt());
            bizAcctItemBean.setEndDate(bizAcctAccountBean.getPayDueDate());
            bizAcctItemBean.setOverdueDays(bizAcctAccountBean.getOverdueDays());
            bizAcctItemBean.setLoanPri(bizAcctAccountBean.getOdPrincipalAmt());
            bizAcctItemBean.setLoanInt(bizAcctAccountBean.getOdPenIntAmt());
            bizAcctItemBean.setLoanTotal(bizAcctAccountBean.getOvduAmts());
            bizAcctItemBean.setBusinessType(bizAcctAccountBean.getBusinessType());
            bizAcctItemBean.setReducePri(0.0);
            bizAcctItemBean.setReduceInt(0.0);
            bizAcctItemBean.setReduceTotal(0.0);
            bizAcctItemBean.setPlanRepayTotal(0.0);
            mList.add(bizAcctItemBean);

        }


        for (BizAcctInfoBean.BizAcctCardBean bizAcctCardBean : bizAcctCard) {
            BizAcctItemBean bizAcctItemBean = new BizAcctItemBean();
            bizAcctItemBean.setProductName(bizAcctCardBean.getProductName());
            bizAcctItemBean.setCurrency(bizAcctCardBean.getCurrency());
            bizAcctItemBean.setAcctNo(bizAcctCardBean.getCreditCardNo());
            bizAcctItemBean.setLoanPri(bizAcctCardBean.getOvduPrincipalAmts());
            bizAcctItemBean.setOverdueDays(bizAcctCardBean.getOvduMonth());
            bizAcctItemBean.setShouldBreach(bizAcctCardBean.getPenalAmt());
            bizAcctItemBean.setLoanInt(bizAcctCardBean.getInterestAmt());
            bizAcctItemBean.setPenalAmt(bizAcctCardBean.getPeriodRestFee());
            bizAcctItemBean.setLoanTotal(bizAcctCardBean.getCurrAcctBalance());
            bizAcctItemBean.setBusinessType(bizAcctCardBean.getBusinessType());
            bizAcctItemBean.setReducePri(0.0);
            bizAcctItemBean.setReduceInt(0.0);
            bizAcctItemBean.setReduceTotal(0.0);
            bizAcctItemBean.setPlanRepayTotal(0.0);
            bizAcctItemBean.setReduceOth(0.0);
            bizAcctItemBean.setReduceFee(0.0);
            mAcctCardList.add(bizAcctItemBean);

        }

        mAdapter.setNewData(mList);
        mBizAcctCardAdapter.setNewData(mAcctCardList);
    }

    @Override
    public void setCustData(CustInfoBean custInfoBean) {
        if (custInfoBean == null) {
            return;
        }
        // mCustInfoBean = custInfoBean;
        mTvCustName.setText(custInfoBean.getCustName());
        mTvCustIdCard.setText(custInfoBean.getCertNo());
        mTvUserName.setText(Perference.get(Perference.NICK_NAME));

    }

    @Override
    public List<BizAcctItemBean> getBizAcctAccountData() {
        return mList;
    }

    @Override
    public List<BizAcctItemBean> getBizAcctCardData() {
        return mAcctCardList;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.BIZ_ACCT_REDUCE_INFO) {
            BizAcctEventBean bizAcctEventBean = (BizAcctEventBean) bean.getObject();
            if (mHandler.hasMessages(MESSAGE_CHANGE)) {
                mHandler.removeMessages(MESSAGE_CHANGE);
            }
            Message message = new Message();
            message.obj = bizAcctEventBean;
            message.what = MESSAGE_CHANGE;
            mHandler.sendMessageDelayed(message, INTERVAL);

        }

    }

    private void resutReduce(int position, double planRepayTotal, String type) {
        /**
         * 贷款减免的处理
         */
        double reducePri;//申请减免本金
        double reduceInt;//申请减免利罚息/透支利息
        double reduceTotal;//申请减免总额
        double reduceFee;// 申请减免违约金
        double reduceOth;//申请减免分期提前结清手续费
        double loanTotal;//欠款总额
        double loanInt;//欠款利罚息/透支利息
        double shouldBreach;//违约金
        double penalAmt;//分期提前结清手续费

        //计划归还总金额

        //Log.e("nb", position + ":" + bizAcctEventBean.getType() + ":" + bizAcctEventBean.getPlanRepayTotal());
        if ("0".equals(type)) {
            //贷款
            BizAcctItemBean bizAcctItemBean = mList.get(position);


            //欠款利罚息
            loanInt = bizAcctItemBean.getLoanInt();
            //欠款本金
            double loanPri = bizAcctItemBean.getLoanPri();

            //欠款总额 = 欠款利罚息 + 欠款本金
            loanTotal = ArithUtil.add(loanInt, loanPri);

            //申请减免总金额 = 欠款总金额 - 计划归还金额
            reduceTotal = ArithUtil.sub(loanTotal, planRepayTotal);

            if (reduceTotal <= 0) {
                //计划归还总额 大于等于总欠款
                reduceInt = 0.00;
                reducePri = 0.00;
                reduceTotal = 0.00;

            } else {
                //减免总金额大于利罚息 就要减免本金利
                if (reduceTotal > loanInt) {
                    //减免本金
                    reducePri = ArithUtil.sub(reduceTotal, loanInt);
                    reduceInt = loanInt;
                    RxToast.showToast("计划归还金额小于尚欠本金");
                } else {
                    //减免本金 0
                    reducePri = 0.0;
                    reduceInt = reduceTotal;
                }
            }

            mList.get(position).setReducePri(reducePri);//减免本金
            mList.get(position).setReduceInt(reduceInt);//申请减免利罚息
            mList.get(position).setReduceTotal(reduceTotal);//申请减免总额
            mList.get(position).setPlanRepayTotal(planRepayTotal);
            mAdapter.notifyItemChanged(position);
        } else if ("1".equals(type)) {
            //1 信用卡
            BizAcctItemBean bizAcctItemBean = mAcctCardList.get(position);


            loanInt = bizAcctItemBean.getLoanInt();//透支利息
            shouldBreach = bizAcctItemBean.getShouldBreach();//违约金
            penalAmt = bizAcctItemBean.getPenalAmt();//分期提前结清手续费
            double loanPri = bizAcctItemBean.getLoanPri();//欠款本金

            //  欠款总额 = 欠款本金 + 违约金 + 分期提前结清手续费 + 透支利息
           //  loanTotal = loanPri + shouldBreach +penalAmt +loanInt ;//欠款总额
            loanTotal = ArithUtil.add(ArithUtil.add(loanPri,shouldBreach),ArithUtil.add(penalAmt,loanInt));
            //申请减免总金额 = 欠款总金额 - 计划归还金额
            reduceTotal = ArithUtil.sub(loanTotal, planRepayTotal);

            if (reduceTotal <= 0) {
                //计划归还总额 大于等于总欠款
                reduceInt = 0.00;
                reducePri = 0.00;
                reduceTotal = 0.00;
                reduceFee = 0.00;
                reduceOth = 0.00;
            } else {
                //1 减免违约金
                // 申请减免总金额 < 违约金      全是减免违约金
                if (reduceTotal < shouldBreach) {
                    reduceFee = reduceTotal;
                    reduceInt = 0.00;
                    reducePri = 0.00;
                    reduceOth = 0.00;
                } else {
                    reduceFee = shouldBreach;//申请减免违约金
                    //2 减免透支利息
                    double totalSubShould = ArithUtil.sub(reduceTotal, shouldBreach);
                    if (totalSubShould <= loanInt) {
                        reduceInt = ArithUtil.sub(reduceTotal, shouldBreach);
                        reducePri = 0.00;
                        reduceOth = 0.00;

                    } else {
                        //3 减免分期提前结清手续费
                        reduceInt = loanInt;
                        double sub = ArithUtil.sub(totalSubShould, loanInt);
                        if (sub <= penalAmt) {
                            // 分期提前结清手续费
                            reduceOth = sub;
                            reducePri = 0.00;
                        } else {
                            //4 减免本金
                            reduceOth = penalAmt;
                            reducePri = ArithUtil.sub(sub, penalAmt);

                        }

                    }


                }


            }
            bizAcctItemBean.setReduceOth(reduceOth);
            bizAcctItemBean.setReduceFee(reduceFee);
            mAcctCardList.get(position).setReducePri(reducePri);//减免本金
            mAcctCardList.get(position).setReduceInt(reduceInt);//申请减免利罚息
            mAcctCardList.get(position).setReduceTotal(reduceTotal);//申请减免总额
            mAcctCardList.get(position).setPlanRepayTotal(planRepayTotal);
            mBizAcctCardAdapter.notifyItemChanged(position);

        }

        //总的减免计算

        //贷款合计减免本金
        double reducePriLoan = 0.00;
        //贷款合计减免罚息
        double reduceIntLoan = 0.00;
        //贷款合计减免总金额
        double reduceTotalLoan = 0.00;

        //信用卡减免本金
        double reducePriCard = 0.00;
        //信用卡减免利罚息
        double reduceIntCard = 0.00;
        // 信用卡减免总金额
        double reduceTotalCard = 0.00;


        for (BizAcctItemBean bizAcctItemBean : mList) {
            reducePriLoan = ArithUtil.add(reducePriLoan, bizAcctItemBean.getReducePri());

            reduceIntLoan = ArithUtil.add(reduceIntLoan, bizAcctItemBean.getReduceInt());

            reduceTotalLoan = ArithUtil.add(reduceTotalLoan, bizAcctItemBean.getReduceTotal());

        }

        for (BizAcctItemBean bizAcctItemBean : mAcctCardList) {
            reducePriCard = ArithUtil.add(reducePriCard, bizAcctItemBean.getReducePri());
            reduceIntCard = ArithUtil.add(reduceIntCard, bizAcctItemBean.getReduceInt());
            reduceTotalCard = ArithUtil.add(reduceTotalCard, bizAcctItemBean.getReduceTotal());
        }


        mTvReducePriLoan.setText(String.valueOf(reducePriLoan));
        mTvReduceIntLoan.setText(String.valueOf(reduceIntLoan));
        mTvReduceTotalLoan.setText(String.valueOf(reduceTotalLoan));

        mTvReducePriCard.setText(String.valueOf(reducePriCard));
        mTvReduceIntCard.setText(String.valueOf(reduceIntCard));
        mTvReduceTotalCard.setText(String.valueOf(reduceTotalCard));


        mTvReducePri.setText(String.valueOf(ArithUtil.add(reducePriLoan, reducePriCard)));
        mTvReduceInt.setText(String.valueOf(ArithUtil.add(reduceIntLoan, reduceIntCard)));
        mTvReduceTotal.setText(String.valueOf(ArithUtil.add(reduceTotalLoan, reduceTotalCard)));

    }

    private static final int MESSAGE_CHANGE = 0x1;
    private static long INTERVAL = 1500; // 输入变化时间间隔
    MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<RemissionActivity> mActivityReference;

        MyHandler(RemissionActivity fragment) {
            mActivityReference = new WeakReference<RemissionActivity>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            RemissionActivity fragment = mActivityReference.get();
            if (fragment != null) {
                fragment.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message message) {
        if (message.what == MESSAGE_CHANGE) {
            BizAcctEventBean bizAcctEventBean = (BizAcctEventBean) message.obj;
            int position = bizAcctEventBean.getPosition();
            double planRepayTotal = Double.valueOf(bizAcctEventBean.getPlanRepayTotal());
            String type = bizAcctEventBean.getType();
            resutReduce(position, planRepayTotal, type);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerManager.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> list = FilePickerManager.INSTANCE.obtainData();
                if (list != null && list.size() > 0) {
                    startUpLoadFile(list);
                    //  File file = new File(list.get(0));
                    //   Log.e("nb", file.getName() + ":" + file.getAbsolutePath());
                }

            } else {
                // 没有选择任何文件

            }
        }
    }

    private void startUpLoadFile(List<String> list) {

        if (isUpload) {
            RxToast.showToast("附件文件正在上传中，请稍后再试");
            return;
        }
        showLoading();
        ArrayList<UploadParam> uploadParams = new ArrayList<>();
        uploadParams.add(new UploadParam("callback", "mobileAppFileOperServiceImpl/uploadApplyReductionFile"));
        Map<String, String> map = new HashMap<>();
        map.put("tlrNo", Perference.getUserId());
        map.put("relaBusiCode", custId + "_" + caseId);
        map.put("caseId", caseId);
        String appData = GsonUtils.toJson(map);
        uploadParams.add(new UploadParam("appData", AESUtils.encrypt(appData, "aes-nbcbccms@123")));
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(0));
            uploadParams.add(new UploadParam("file", file, file.getName()));
        }

        RetrofitManager.getInstance()
                .uploadFile(NetworkConfig.C.getBaseURL() + "file/upload.htm", uploadParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UploadObserver<ResponseStructure>() {
                    @Override
                    public void _onProgress(Integer percent) {
                        super._onProgress(percent);
                        // Log.e("nb", "percent:" + percent);
                    }

                    @Override
                    public void _onNext(ResponseStructure o) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                        if (o == null) {
                            return;
                        }

                        if (o.getScubeHeader() != null && "EXP".equals(o.getScubeHeader().getErrorCode())) {
                            RxToast.showToast(o.getScubeHeader().getErrorMsg());
                        } else if (o.getScubeHeader() != null && "SUC".equals(o.getScubeHeader().getErrorCode())) {
                            RxToast.showToast("减免附件文件上传成功");

                        }
                        isUpload = false;
                    }

                    @Override
                    public void _onError(Throwable e) {
                        hideLoading();
                        Log.e("nb", e.getLocalizedMessage() + "失败");
                        RxToast.showToast("录音文件上传失败");
                        isUpload = false;
                    }
                });

    }

}
