package com.huateng.collection.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.orm.UserLoginInfo;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.utils.OrmHelper;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 外访记录录入
 */
public class OutboundEntryActivity extends BaseActivity {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_is_self)
    TextView mTvIsSelf;
    @BindView(R.id.ll_is_self)
    LinearLayout mLlIsSelf;
    @BindView(R.id.tv_cust_name)
    TextView mTvCustName;
    @BindView(R.id.out_bound_name)
    TextView mOutBoundName;
    @BindView(R.id.edt_out_bound_address)
    EditText mEdtOutBoundAddress;
    @BindView(R.id.edt_out_bound_summary)
    EditText mEdtOutBoundSummary;
    @BindView(R.id.btn_send)
    FmButton mBtnSend;

    private String  custId;
    private String caseId;
    private String custName;
    private UserLoginInfo userInfo;
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
        return R.layout.activity_outbound_entry;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
         userInfo = OrmHelper.getLastLoginUserInfo();
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        custName = getIntent().getStringExtra(Constants.CUST_NAME);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        mTvDate.setText(formatter.format(new Date()));
        mTvCustName.setText(custName);
       // mOutBoundName.setText(userInfo.getNickName());


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


    @OnClick({R.id.ll_is_self, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_is_self:

                List<BottomDialogBean> list = new ArrayList<>();
                list.add(new BottomDialogBean("是", "0"));
                list.add(new BottomDialogBean("否", "1"));
                BottomDialogFragment.newInstance()
                        .setData("是否见到本人", list, false, true)
                        .setDialogItemClicklistener(bottomDialogBean -> {
                            mTvIsSelf.setText(bottomDialogBean.getTitle());
                        }).show(getSupportFragmentManager());

                break;
            case R.id.btn_send:
                //保存外访记录录入
                saveData();
                //
                break;
        }
    }

    private void saveData() {
        String outBoundDate = mTvDate.getText().toString().trim();
        String outBoundSummary = mEdtOutBoundSummary.getText().toString().trim();
        String outBoundAddress = mEdtOutBoundAddress.getText().toString().trim();
        String isSelf = mTvIsSelf.getText().toString().trim();
        String outBoundName = mOutBoundName.getText().toString().trim();
        if(TextUtils.isEmpty(caseId)) {
            RxToast.showToast("案件ID不能为空");
            return;
        }

        if(TextUtils.isEmpty(custId)) {
            RxToast.showToast("客户ID不能为空");
            return;
        }
        if(TextUtils.isEmpty(custName)) {
            RxToast.showToast("客户姓名不能为空");
            return;
        }
        if(TextUtils.isEmpty(outBoundDate)) {
            RxToast.showToast("外访日期不能为空");
            return;
        }


        if(TextUtils.isEmpty(outBoundName)) {
            RxToast.showToast("外访人员不能为空");
            return;
        }

        if(TextUtils.isEmpty(outBoundAddress)) {
            RxToast.showToast("外访地址不能为空");
            return;
        }
        if(TextUtils.isEmpty(outBoundAddress)) {
            RxToast.showToast("外访地址不能为空");
            return;
        }

        if(TextUtils.isEmpty(outBoundSummary)) {
            RxToast.showToast("外访小结不能为空");
            return;
        }


        Map<String, String> map = new HashMap<>();
        map.put("caseId", caseId);
        map.put("custNo", custId);
        map.put("custName", custName);
        map.put("outBoundName",outBoundName);
        map.put("outBoundDate",outBoundDate);
        map.put("outBoundAddress", outBoundAddress);
        map.put("seeHimseif", "是".equals(isSelf)?"Y":"N");
        map.put("outBoundSummary",outBoundSummary );
        map.put("tlrNo",userInfo.getUserId());
        showLoading();

        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.INSERT_OUT_BOUND_RECORD, map)
                .compose(getRxlifecycle())
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver2<String>() {


                    @Override
                    public void onError(String code, String msg) {
                        Log.e("nb","onError onError");

                        hideLoading();

                        if(TextUtils.isEmpty(msg)) {
                            return;
                        }
                        RxToast.showToast(msg);
                    }

                    @Override
                    public void onNextData(String s) {

                        hideLoading();
                        Log.e("nb","onNextData onNextData");
                        RxToast.showToast("外访录入录入成功");
                        EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_HISTORY_ACTIONS));
                        finish();
                    }
                });

    }

}
