package com.huateng.collection.ui.fragment.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespBase;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.utils.StringUtils;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by shanyong on 2016/12/13.
 */

public class FragmentModifyPassword extends BaseActivity {

    @BindView(R.id.et_earlyPwd)
    EditText etEarlyPwd;
    @BindView(R.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R.id.et_newPwdP)
    EditText etNewPwdP;
    @BindView(R.id.btn_confirm)
    FmButton btnConfirm;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    @Override
    protected void initView(Bundle savedInstanceState) {

//        setFragmentAnimator(new DefaultHorizontalAnimator());
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // pop();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String earlyPwd = etEarlyPwd.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                String newPwdp = etNewPwdP.getText().toString();

                if (StringUtils.isEmpty(earlyPwd, newPwd, newPwdp)) {
                    RxToast.showToast("密码不能为空");
                    return;
                }
//
//        if (!Validation.pwdCheck(newPwd)) {
//            toast("密码格式不正确 ！ 格式为:5-17位已字母开头并包含数字组合.");
//            return;
//        }

                if (!newPwd.equals(newPwdp)) {
                    RxToast.showToast("新密码和确认密码不一致");
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("userId", Perference.getUserId());
                map.put("oldPassword", earlyPwd);
                map.put("newPassword", newPwd);

                CommonInteractor.request(new RequestCallbackImpl<RespBase>() {

                    @Override
                    public void beforeRequest() {
                        super.beforeRequest();
                        showLoading();
                    }

                    @Override
                    public void requestError(String code, String msg) {
                        super.requestError(code, msg);
                        RxToast.showToast(msg);
                    }

                    @Override
                    public void response(RespBase respBase) {
                        RxToast.showToast(respBase.getResultDesc());
                        etEarlyPwd.setText(null);
                        etNewPwd.setText(null);
                        etNewPwdP.setText(null);
                    }

                    @Override
                    public void end() {
                        super.end();
                        hideLoading();
                    }
                }, ApiConstants.APP_ROOT, ApiConstants.METHOD_CHANGE_PWD, map);
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }



    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_modify_password;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
