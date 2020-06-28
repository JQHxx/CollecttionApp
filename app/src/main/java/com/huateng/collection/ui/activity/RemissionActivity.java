package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.ui.adapter.BizAcctAdapter;
import com.huateng.collection.ui.adapter.BizAcctCardAdapter;
import com.huateng.collection.ui.dialog.AlertDialogFragment;
import com.huateng.collection.ui.dialog.AlertFragmentUtil;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.ui.remission.contract.RemissionContract;
import com.huateng.collection.ui.remission.presenter.RemissionPresenter;
import com.huateng.collection.utils.DateUtil;
import com.huateng.collection.utils.SoftKeyBoardListener;
import com.huateng.collection.widget.Watermark;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.orm.SugarRecord;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.utils.ArithUtil;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 减免申请
 */
public class RemissionActivity extends BaseActivity<RemissionPresenter> implements RemissionContract.View {
    @BindView(R.id.btn_send2)
    FmButton mBtnSend2;
    @BindView(R.id.ll_send)
    LinearLayout mLlSend;
    private boolean dialogHintShow;
    @BindView(R.id.ll_card)
    LinearLayout mLlCard;
    @BindView(R.id.tv_account_type)
    TextView mTvAccountType;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
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
    private String relaBusiCode = "";


    private BizAcctAdapter mAdapter;
    private BizAcctCardAdapter mBizAcctCardAdapter;
    private String custId;
    private String businessType;
    List<BizAcctItemBean> mList = new ArrayList();
    private String caseId;

    @Override
    protected RemissionPresenter createPresenter() {
        return new RemissionPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        businessType = getIntent().getStringExtra(Constants.BUSINESS_TYPE);

        initRecyclerview();

        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEdtOverdueCause.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() > 200) {
                    mEdtOverdueCause.setText(s.toString().substring(0, 200));
                    RxToast.showToast("减免原因不能超过200字");
                }
            }
        });

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

            @Override
            public void keyBoardShow(int height) {
                Log.e("nb", "height" + height);
                mLlSend.setVisibility(View.GONE);
                mBtnSave.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                Log.e("nb", "height" + height);
                mLlSend.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void initRecyclerview() {

        mRecyclerviewCard.setLayoutManager(new LinearLayoutManager(this));
        if ("03".equals(businessType)) {
            //信用卡
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
                                        mList.get(position).setIsEarSettlement(bottomDialogBean.getId());
                                        mBizAcctCardAdapter.notifyItemChanged(position);
                                    }
                                })
                                .show(getSupportFragmentManager());


                    }
                }
            });

        } else {
            //展示贷款信息列表
            mAdapter = new BizAcctAdapter();
            mRecyclerviewCard.setAdapter(mAdapter);
        }
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

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        mTvDate.setText(formatter.format(new Date()));

        initDictData();
        mPresenter.loadCustInfo(custId);
        mPresenter.loadData(custId,caseId);

        if ("03".equals(businessType)) {
            //信用卡
            mTvAccountType.setText("信用卡");
        } else {
            //贷款
            mTvAccountType.setText("贷款");

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


    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    @OnClick({R.id.btn_save,R.id.btn_send2})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_save:
            case R.id.btn_send2:
                reliefBatchExcute();
                break;
        }
    }

    private void reliefBatchExcute() {
        String certNo = mTvCustIdCard.getText().toString();
        String applyReason = mEdtOverdueCause.getText().toString();
        if (TextUtils.isEmpty(applyReason)) {
            RxToast.showToast("减免原因不能为空");
            return;
        }

        if (applyReason.length() > 200) {
            RxToast.showToast("减免原因不能超过200字");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("custNo", custId);
        map.put("caseId", caseId);
        map.put("certNo", certNo);
        map.put("applyReason", applyReason);
        map.put("applyUser", Perference.getUserId());
        map.put("relaBusiCode", relaBusiCode);
        String reducePri = mTvReducePri.getText().toString();
        String reduceInt = mTvReduceInt.getText().toString();
        String reduceTotal = mTvReduceTotal.getText().toString();

        map.put("reducePri", reducePri);
        map.put("reduceInt", reduceInt);
        map.put("reduceTotal", reduceTotal);
        if ("03".equals(businessType)) {
            //信用卡
            map.put("reducePriCard", reducePri);
            map.put("reduceIntCard", reduceInt);
            map.put("reduceTotalCard", reduceTotal);

            map.put("reducePriLoan", "0");
            map.put("reduceIntLoan", "0");
            map.put("reduceTotalLoan", "0");
        } else {
            //贷款
            map.put("reducePriCard", "0");
            map.put("reduceIntCard", "0");
            map.put("reduceTotalCard", "0");

            map.put("reducePriLoan", reducePri);
            map.put("reduceIntLoan", reduceInt);
            map.put("reduceTotalLoan", reduceTotal);
        }
        //贷款
        List<ReduceRequestBean> reduceRequestBeans = new ArrayList<>();
        if ("03".equals(businessType)) {
            //信用卡
            for (BizAcctItemBean bizAcctItemBean : mList) {
                ReduceRequestBean reduceRequestBean = new ReduceRequestBean();
                String planRepayTotal = bizAcctItemBean.getPlanRepayTotal();
                if (TextUtils.isEmpty(planRepayTotal)) {
                    RxToast.showToast("计划归还金额不能为空");
                    return;
                }
                if (TextUtils.isEmpty(bizAcctItemBean.getIsEarSettlement()) || "请选择".equals(bizAcctItemBean.getIsEarSettlement())) {
                    RxToast.showToast("是否申请分期提前结清不能为空");
                    return;
                }

                reduceRequestBean.setIsEarsettlement(bizAcctItemBean.getIsEarSettlement());
                reduceRequestBean.setPlanRepayTotal(planRepayTotal);
                reduceRequestBean.setAcctNo(bizAcctItemBean.getAcctNo());
                reduceRequestBean.setShouldBreach(String.valueOf(bizAcctItemBean.getShouldBreach()));
                reduceRequestBean.setPenalAmt(bizAcctItemBean.getPenalAmt());
                reduceRequestBean.setReduceFee(bizAcctItemBean.getReduceFee());
                reduceRequestBean.setReduceOth(bizAcctItemBean.getReduceOth());

                reduceRequestBean.setBusinessType(bizAcctItemBean.getBusinessType());
                reduceRequestBean.setOverdueDays(bizAcctItemBean.getOverdueDays());
                reduceRequestBean.setLoanPri(String.valueOf(bizAcctItemBean.getLoanPri()));
                reduceRequestBean.setLoanInt(String.valueOf(bizAcctItemBean.getLoanInt()));
                reduceRequestBean.setLoanTotal(String.valueOf(bizAcctItemBean.getLoanTotal()));
                reduceRequestBean.setReducePri(String.valueOf(bizAcctItemBean.getReducePri()));
                reduceRequestBean.setReduceInt(String.valueOf(bizAcctItemBean.getReduceInt()));
                reduceRequestBean.setReduceTotal(String.valueOf(bizAcctItemBean.getReduceTotal()));

                reduceRequestBeans.add(reduceRequestBean);
            }
        } else {

            for (BizAcctItemBean bizAcctItemBean : mList) {
                ReduceRequestBean reduceRequestBean = new ReduceRequestBean();
                String planRepayTotal = bizAcctItemBean.getPlanRepayTotal();
                if (TextUtils.isEmpty(planRepayTotal)) {
                    RxToast.showToast("计划归还金额不能为空");
                    return;
                }

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
                reduceRequestBean.setPlanRepayTotal(planRepayTotal);
                reduceRequestBean.setBusinessType(bizAcctItemBean.getBusinessType());
                reduceRequestBeans.add(reduceRequestBean);
            }
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
        if ("03".equals(businessType)) {
            //信用卡信息
            List<BizAcctInfoBean.BizAcctCardBean> bizAcctCard = bizAcctInfoBean.getBizAcctCard();
            for (BizAcctInfoBean.BizAcctCardBean bizAcctCardBean : bizAcctCard) {
                BizAcctItemBean bizAcctItemBean = new BizAcctItemBean();
                bizAcctItemBean.setProductName(bizAcctCardBean.getProductName());//
                bizAcctItemBean.setCurrency(bizAcctCardBean.getCurrency());
                bizAcctItemBean.setAcctNo(bizAcctCardBean.getCreditCardNo());
                bizAcctItemBean.setLoanPri(bizAcctCardBean.getOvduPrincipalAmts());
                bizAcctItemBean.setOverdueDays(bizAcctCardBean.getOvduMonth());
                bizAcctItemBean.setShouldBreach(bizAcctCardBean.getPenalAmt());
                bizAcctItemBean.setLoanInt(bizAcctCardBean.getInterestAmt());
                bizAcctItemBean.setPenalAmt(bizAcctCardBean.getPeriodRestFee());
                bizAcctItemBean.setLoanTotal(bizAcctCardBean.getCurrAcctBalance());
                bizAcctItemBean.setBusinessType(bizAcctCardBean.getBusinessType());//
                bizAcctItemBean.setCardType(bizAcctCardBean.getCardType());
                bizAcctItemBean.setReducePri(0.0);//
                bizAcctItemBean.setReduceInt(0.0);//
                bizAcctItemBean.setReduceTotal(0.0);//
                bizAcctItemBean.setPlanRepayTotal("");//
                bizAcctItemBean.setReduceOth(0.0);
                bizAcctItemBean.setReduceFee(0.0);
                mList.add(bizAcctItemBean);
                mBizAcctCardAdapter.setNewData(mList);
            }

        } else {
            //贷款账户信息
            List<BizAcctInfoBean.BizAcctAccountBean> bizAcctAccount = bizAcctInfoBean.getBizAcctAccount();
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
                bizAcctItemBean.setPlanRepayTotal("");
                mList.add(bizAcctItemBean);
                mAdapter.setNewData(mList);
            }

        }


    }

    @Override
    public void setCustData(CustInfoBean custInfoBean) {
        if (custInfoBean == null) {
            return;
        }
        mTvCustName.setText(custInfoBean.getCustName());
        mTvCustIdCard.setText(custInfoBean.getCertNo());
        mTvUserName.setText(Perference.get(Perference.NICK_NAME));

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

    private void resutReduce(int position, String planRepayTotal, String type) {
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

        if ("0".equals(type)) {
            //贷款
            BizAcctItemBean bizAcctItemBean = mList.get(position);

            if (TextUtils.isEmpty(planRepayTotal)) {
                reducePri = 0;
                reduceInt = 0;
                reduceTotal = 0;

            } else {

                //欠款利罚息
                loanInt = bizAcctItemBean.getLoanInt();
                //欠款本金
                double loanPri = bizAcctItemBean.getLoanPri();

                //欠款总额 = 欠款利罚息 + 欠款本金
                loanTotal = ArithUtil.add(loanInt, loanPri);

                //申请减免总金额 = 欠款总金额 - 计划归还金额

                reduceTotal = ArithUtil.sub(loanTotal, Double.valueOf(planRepayTotal));

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
                        //   RxToast.showToast("计划归还金额小于尚欠本金");

                    } else {
                        //减免本金 0
                        reducePri = 0.0;
                        reduceInt = reduceTotal;
                    }
                }
            }
            // double reduceFee;// 申请减免违约金
            // double reduceOth;//申请减免分期提前结清手续费
            // double loanInt;//欠款利罚息/透支利息
            mList.get(position).setReducePri(reducePri);//减免本金
            mList.get(position).setReduceInt(reduceInt);//申请减免利罚息
            mList.get(position).setReduceTotal(reduceTotal);//申请减免总额
            mList.get(position).setPlanRepayTotal(planRepayTotal);
            mAdapter.notifyItemChanged(position);
        } else if ("1".equals(type)) {
            //1 信用卡
            BizAcctItemBean bizAcctItemBean = mList.get(position);
            if (TextUtils.isEmpty(planRepayTotal)) {
                reduceOth = 0;
                reduceFee = 0;
                reducePri = 0;
                reduceInt = 0;
                reduceTotal = 0;
            } else {
                loanInt = bizAcctItemBean.getLoanInt();//透支利息
                shouldBreach = bizAcctItemBean.getShouldBreach();//违约金
                penalAmt = bizAcctItemBean.getPenalAmt();//分期提前结清手续费
                double loanPri = bizAcctItemBean.getLoanPri();//欠款本金

                //  欠款总额 = 欠款本金 + 违约金 + 分期提前结清手续费 + 透支利息
                //  loanTotal = loanPri + shouldBreach +penalAmt +loanInt ;//欠款总额
                loanTotal = ArithUtil.add(ArithUtil.add(loanPri, shouldBreach), ArithUtil.add(penalAmt, loanInt));
                //申请减免总金额 = 欠款总金额 - 计划归还金额
                reduceTotal = ArithUtil.sub(loanTotal, Double.valueOf(TextUtils.isEmpty(planRepayTotal) ? "0" : planRepayTotal));

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
            }
            double mainReduceInt = ArithUtil.add(ArithUtil.add(reduceInt, reduceFee), reduceOth);
            bizAcctItemBean.setReduceOth(reduceOth);
            bizAcctItemBean.setReduceFee(reduceFee);
            mList.get(position).setReducePri(reducePri);//减免本金
            mList.get(position).setReduceInt(mainReduceInt);//申请减免利罚息
            mList.get(position).setReduceTotal(reduceTotal);//申请减免总额

            mList.get(position).setPlanRepayTotal(planRepayTotal);
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

        mTvReducePri.setText(String.valueOf(ArithUtil.add(reducePriLoan, reducePriCard)));
        mTvReduceInt.setText(String.valueOf(ArithUtil.add(reduceIntLoan, reduceIntCard)));
        mTvReduceTotal.setText(String.valueOf(ArithUtil.add(reduceTotalLoan, reduceTotalCard)));

    }


    private static final int MESSAGE_CHANGE = 0x1;
    private static long INTERVAL = 2000; // 输入变化时间间隔
    MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


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
        if (isFinishing()) {
            return;
        }
        if (message.what == MESSAGE_CHANGE) {
            BizAcctEventBean bizAcctEventBean = (BizAcctEventBean) message.obj;
            int position = bizAcctEventBean.getPosition();
            String planRepayTotal = bizAcctEventBean.getPlanRepayTotal();
            String type = bizAcctEventBean.getType();
            BizAcctItemBean bizAcctItemBean = mList.get(position);
            if (TextUtils.isEmpty(planRepayTotal)) {
                dialogHintShow = false;

                double reducePri = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReducePri.getText().toString()) ? "0" : mTvReducePri.getText().toString()), mList.get(position).getReducePri());
                double reduceInt = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReduceInt.getText().toString()) ? "0" : mTvReduceInt.getText().toString()), mList.get(position).getReduceInt());
                double reduceTotal = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReduceTotal.getText().toString()) ? "0" : mTvReduceTotal.getText().toString()), mList.get(position).getReduceTotal());

                mList.get(position).setReducePri(0);//减免本金
                mList.get(position).setReduceInt(0);//申请减免利罚息
                mList.get(position).setReduceTotal(0);//申请减免总额
                mList.get(position).setPenalAmt(0);//分期提前结清手续费
                mList.get(position).setReduceFee(0);//申请减免违约金
                mList.get(position).setReduceOth(0);//申请减免分期提前结清手续费

                mList.get(position).setPlanRepayTotal("");
                mTvReducePri.setText(String.valueOf(reducePri));
                mTvReduceInt.setText(String.valueOf(reduceInt));
                mTvReduceTotal.setText(String.valueOf(reduceTotal));

                if ("03".equals(businessType)) {
                    mBizAcctCardAdapter.notifyItemChanged(position);
                } else {

                    mAdapter.notifyItemChanged(position);
                }


            } else if (Double.valueOf(planRepayTotal) < bizAcctItemBean.getLoanPri()) {
                if (!dialogHintShow) {
                    dialogHintShow = true;
                    AlertFragmentUtil.showAlertDialog(RemissionActivity.this, "计划归还金额小于尚欠本金，是否继续申请减免？", new AlertDialogFragment.OnDialogButtonClickListener() {
                        @Override
                        public void onClickLeft() {
                            dialogHintShow = false;
                            double reducePri = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReducePri.getText().toString()) ? "0" : mTvReducePri.getText().toString()), mList.get(position).getReducePri());
                            double reduceInt = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReduceInt.getText().toString()) ? "0" : mTvReduceInt.getText().toString()), mList.get(position).getReduceInt());
                            double reduceTotal = ArithUtil.sub(Double.valueOf(TextUtils.isEmpty(mTvReduceTotal.getText().toString()) ? "0" : mTvReduceTotal.getText().toString()), mList.get(position).getReduceTotal());

                            mList.get(position).setReducePri(0);//减免本金
                            mList.get(position).setReduceInt(0);//申请减免利罚息
                            mList.get(position).setReduceTotal(0);//申请减免总额
                            mList.get(position).setPenalAmt(0);//分期提前结清手续费
                            mList.get(position).setReduceFee(0);//申请减免违约金
                            mList.get(position).setReduceOth(0);//申请减免分期提前结清手续费
                            mList.get(position).setPlanRepayTotal("");

                            mTvReducePri.setText(String.valueOf(reducePri));
                            mTvReduceInt.setText(String.valueOf(reduceInt));
                            mTvReduceTotal.setText(String.valueOf(reduceTotal));
                            if ("03".equals(businessType)) {
                                mBizAcctCardAdapter.notifyItemChanged(position);
                            } else {

                                mAdapter.notifyItemChanged(position);
                            }
                        }

                        @Override
                        public void onClickRight() {
                            resutReduce(position, planRepayTotal, type);
                            dialogHintShow = false;
                        }
                    });
                }

            } else {

                resutReduce(position, planRepayTotal, type);
            }


        }
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
                        if ("03".equals(businessType)) {

                            mBizAcctCardAdapter.setDictData(stringStringHashMap);
                            if (mBizAcctCardAdapter.getData() != null) {
                                mBizAcctCardAdapter.notifyDataSetChanged();
                            }
                        } else {
                            mAdapter.setDictData(stringStringHashMap);
                            if (mAdapter.getData() != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }


                    }
                });
    }

}
