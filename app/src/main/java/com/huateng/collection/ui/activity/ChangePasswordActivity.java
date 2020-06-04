package com.huateng.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.RetrofitManager;
import com.huateng.network.bean.ResultBean;
import com.tools.utils.CheckPswUtil;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.edt_first_password)
    EditText mEdtFirstPassword;
    @BindView(R.id.iv_show_password1)
    ImageView mIvShowPassword1;
    @BindView(R.id.ll_show_pssword1)
    LinearLayout mLlShowPssword1;
    @BindView(R.id.edt_second_password)
    EditText mEdtSecondPassword;
    @BindView(R.id.iv_show_password2)
    ImageView mIvShowPassword2;
    @BindView(R.id.ll_show_pssword2)
    LinearLayout mLlShowPssword2;
    @BindView(R.id.llpsd)
    LinearLayout mLlpsd;
    @BindView(R.id.btn_login)
    FmButton mBtnLogin;
    @BindView(R.id.edt_old_pass)
    EditText mEdtOldPass;
    @BindView(R.id.iv_old_pass)
    ImageView mIvOldPass;
    @BindView(R.id.ll_old_pass)
    LinearLayout mLlOldPass;

    private boolean isFirst;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBtnLogin.setEnabled(true);

    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        isFirst = getIntent().getBooleanExtra("isFirst", false);
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


    @OnClick({R.id.ll_old_pass, R.id.ll_show_pssword1, R.id.ll_show_pssword2, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_old_pass:
                if (mEdtOldPass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEdtOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvOldPass.setImageResource(R.drawable.icon_eyes_close);
                } else {

                    mEdtOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvOldPass.setImageResource(R.drawable.icon_eyes_open);
                }
                break;
            case R.id.ll_show_pssword1:
                if (mEdtFirstPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEdtFirstPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvShowPassword1.setImageResource(R.drawable.icon_eyes_close);
                } else {

                    mEdtFirstPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvShowPassword1.setImageResource(R.drawable.icon_eyes_open);
                }
                break;
            case R.id.ll_show_pssword2:
                if (mEdtSecondPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEdtSecondPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvShowPassword2.setImageResource(R.drawable.icon_eyes_close);
                } else {

                    mEdtSecondPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvShowPassword2.setImageResource(R.drawable.icon_eyes_open);
                }
                break;
            case R.id.btn_login:

                changePassword();
                break;
        }
    }

    private void changePassword() {
        String firstPass = mEdtFirstPassword.getText().toString();
        String secondPass = mEdtSecondPassword.getText().toString();
        String oldPassword = mEdtOldPass.getText().toString();

        if (TextUtils.isEmpty(oldPassword)) {
            RxToast.showToast("旧密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(firstPass) || TextUtils.isEmpty(secondPass)) {
            RxToast.showToast("新密码不能为空");
            return;
        }

        if (!firstPass.equals(secondPass)) {
            RxToast.showToast("两次输入的密码不一致，请重新输入");
            return;
        }

        if (oldPassword.equals(firstPass)) {
            RxToast.showToast("新密码不能与原密码相同");
            return;
        }

        if (!CheckPswUtil.checkPassword(firstPass) || !CheckPswUtil.checkPassword(secondPass)) {
            RxToast.showToast("密码必须是6-20位数字和大小写字母组合，至少包含两种");
            return;
        }


        Map<String, String> map = new HashMap<>();

        map.put("tlrNo", Perference.getUserId());
        map.put("oldPassword", oldPassword);
        map.put("newPassword", firstPass);


        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_OPER_INTERFACE, ApiConstants.CHANGE_PASSWORD, map)
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
                .subscribe(new BaseObserver2<ResultBean>() {
                    @Override
                    public void onError(String code, String msg) {

                        RxToast.showToast(msg);
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                        RxToast.showToast(msg);
                    }

                    @Override
                    public void onNextData(ResultBean resultBean) {
                        if (isFinishing()) {
                            return;
                        }
                        if (resultBean == null) {
                            return;
                        }
                        RxToast.showToast(resultBean.getResultDesc());
                        hideLoading();
                        if (isFirst) {
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                        }
                        finish();

                    }


                });


    }
}
