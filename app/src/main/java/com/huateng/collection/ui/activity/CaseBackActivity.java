package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.DictDataBean;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.widget.Watermark;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import java.util.ArrayList;
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
 * 退案
 */
public class CaseBackActivity extends BaseActivity {
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.btn_save)
    FmButton mBtnSave;
    @BindView(R.id.tv_cause)
    TextView mTvCause;
    @BindView(R.id.ll_cause)
    LinearLayout mLlCause;
    @BindView(R.id.edt_apply_reason_desc)
    EditText mEdtApplyReasonDesc;
    private String caseId;
    private String applyReason;
    private List<DictItemBean> mDictItemBeanList;
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


        mEdtApplyReasonDesc.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() > 200) {
                    RxToast.showToast("退案原因说明不能超过200字");
                    mEdtApplyReasonDesc.setText(s.toString().substring(0, 200));
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
        return R.layout.activity_case_back;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
       requestDictData();
    }

    private void requestDictData() {
        Map<String, String> map = new HashMap<>();
        map.put("dictCode", "ZDTAAY");
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_DATA_BY_DICT_CODE, map)
                .compose(getRxlifecycle())
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver2<DictDataBean>() {
                    @Override
                    public void onError(String code, String msg) {

                    }

                    @Override
                    public void onNextData(DictDataBean dictDataBean) {
                        mDictItemBeanList = dictDataBean.getZdtaayList();
                    }
                });

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


    @OnClick({R.id.ll_cause, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cause:
                if(mDictItemBeanList == null || mDictItemBeanList.size() ==0) {
                    RxToast.showToast("退案原因数据为加载成功");
                    return;
                }
                List<BottomDialogBean> list3 = new ArrayList<>();
                for (DictItemBean itemBean: mDictItemBeanList){
                    list3.add(new BottomDialogBean(itemBean.getDescription(), itemBean.getDataVal()));
                }

/*
                list3.add(new BottomDialogBean(" 无法联系", "100"));
                list3.add(new BottomDialogBean("无能力还款（上门）", "101"));
                list3.add(new BottomDialogBean("无能力还款（固话）", "102"));
                list3.add(new BottomDialogBean("无能力还款（手机）", "103"));
                list3.add(new BottomDialogBean("无诚意还款（上门）", "104"));
                list3.add(new BottomDialogBean("无诚意还款（固话）", "105"));
                list3.add(new BottomDialogBean("无诚意还款（手机）", "106"));
                list3.add(new BottomDialogBean("分行要求报公安", "107"));
                list3.add(new BottomDialogBean("分行要求司法诉讼", "108"));*/

                BottomDialogFragment.newInstance()
                        .setData("退案原因", list3, false, true)
                        .setDialogItemClicklistener(bottomDialogBean -> {
                            mTvCause.setText(bottomDialogBean.getTitle());
                            applyReason = bottomDialogBean.getId();
                        }).show(getSupportFragmentManager());
                break;
            case R.id.btn_save:
                //提交
                sendRequest();
                break;

        }
    }

    private void sendRequest() {
        String applyReasonDesc = mEdtApplyReasonDesc.getText().toString();//退案原因说明
        if (TextUtils.isEmpty(caseId)) {
            RxToast.showToast("案件ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(applyReason)) {
            RxToast.showToast("退案原因不能为空");
            return;
        }
        if (TextUtils.isEmpty(applyReasonDesc)) {
            RxToast.showToast("退案原因说明不能为空");
            return;
        }

        if (applyReasonDesc.length()>200) {
            RxToast.showToast("退案原因说明不大于200字");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("caseIds", caseId);
        map.put("applyReason", applyReason);
        map.put("applyReasonDesc", applyReasonDesc);
        map.put("tlrNo", Perference.getUserId());

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.CASE_RETIRE_BATCH_EXCUTE, map)
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
                        RxToast.showToast("退案申请操作成功");
                        finish();
                    }
                });


    }

}
