package com.huateng.phone.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.DictDataBean;
import com.huateng.phone.collection.bean.api.RespLoginInfo;
import com.huateng.phone.collection.bean.orm.DictItemBean;
import com.huateng.phone.collection.bean.orm.UserLoginInfo;
import com.huateng.phone.collection.ui.navigation.NavigationActivity;
import com.huateng.phone.collection.utils.OrmHelper;
import com.huateng.phone.collection.utils.StringUtils;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orm.SugarRecord;
import com.tools.ActivityUtils;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by shanyong on 2016/12/13.
 */


public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.et_userName)
    EditText mEtUserName;
    @BindView(R.id.ll_user_id)
    LinearLayout mLlUserId;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.llpsd)
    LinearLayout mLlpsd;
    @BindView(R.id.btn_login)
    FmButton mBtnLogin;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.ll_clear_password)
    LinearLayout mLlClearPassword;
    @BindView(R.id.iv_show_password)
    ImageView mIvShowPassword;
    @BindView(R.id.ll_show_pssword)
    LinearLayout mLlShowPssword;
    private UserLoginInfo loginInfo;
    private long SYSTEM_LOCK_TIME = 1000 * 60 * 5;
    private boolean isFirst;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        ActivityUtils.getAppManager().finishAllActivity();
        init();
        initListener();
    }

    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        isFirst = getIntent().getBooleanExtra(Constants.IS_FIRST, false);
        if (isFirst) {
            mRxTitle.setLeftIconVisibility(true);
            mRxTitle.setLeftTextVisibility(true);
        }

        initPermissions();


    }

    private void initPermissions() {
        XXPermissions.with(this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                //定位 存取
                .permission(Permission.Group.LOCATION,Permission.Group.STORAGE)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        Log.e("nb", denied + ":" + quick);
                        // RxToast.showToast("读写存储卡权限被禁止，应用将无法正常使用");
                        //        mBtnLogin.setEnabled(false);
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


    private void initListener() {
        mEtPwd.setOnEditorActionListener(this);
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRxTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void init() {

        Perference.clear();
        loginInfo = OrmHelper.getLastLoginUserInfo();
        String loginName = loginInfo.getLoginName();
        if (!TextUtils.isEmpty(loginName)) {
            mEtUserName.setText(loginName);
            mEtUserName.setSelection(loginName.length());
        }
        mBtnLogin.setEnabled(false);
        Perference.setBoolean(Constants.CHECK_IS_LOGIN, false);

        //账户名改动后
        RxTextView.textChanges(mEtUserName).debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                // .subscribeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length() > 0) {
                            if (mLlClearPassword != null) {
                                mLlClearPassword.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (mLlClearPassword != null) {
                                mLlClearPassword.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

    }


    public void onLogin() {

        final String loginName = mEtUserName.getText().toString();
        final String password = mEtPwd.getText().toString();

        if (StringUtils.isEmpty(loginName, password)) {
            RxToast.showToast("用户名或密码不能为空");
            return;
        }

        loginInfo = OrmHelper.getLoginUserIInfo(loginName);


        showLoading();
        loginInfo.setLoginName(loginName);
        loginInfo.setUserId(null);
        loginInfo.setAuthorization(null);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", loginName);
        map.put("password", password);
        RetrofitManager.getInstance()
                .loginRequest(map)
                .compose(bindToLifecycle())
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver2<RespLoginInfo>() {
                    @Override
                    public void onError(String code, String msg) {
                        Log.e("nb", code + ":" + msg);
                        if (code.equals(ApiConstants.ERROR_CODE_EXP)) {
                            loginInfo.setLoginName(loginName);
                            loginInfo.setLoginSuccess(false);
                            SugarRecord.save(loginInfo);
                        }

                        if (!TextUtils.isEmpty(msg)) {
                            RxToast.showToast(msg);
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNextData(RespLoginInfo respLoginInfo) {
                        if (isFinishing()) {
                            return;
                        }
                        if (respLoginInfo == null) {
                            hideLoading();
                            return;
                        }

                        if ("0000".equals(respLoginInfo.getResultCode())) {

                            RespLoginInfo.SsouserBean ssouserBean = respLoginInfo.getSsouser();
                            //保存登陆auth 信息
                            NetworkConfig.C.setAuth(ssouserBean.getToken());

                            if ("Y".equals(respLoginInfo.getInitialPassword())) {
                                //强制修改密码
                                hideLoading();
                                mEtPwd.setText("");
                                RxToast.showToast("首次登录，请修改初始密码");
                                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                intent.putExtra("tlrNo", loginName);
                                intent.putExtra("isFirst", true);
                                startActivity(intent);

                            } else {

                                RespLoginInfo.SsouserBean ssouser = respLoginInfo.getSsouser();
                                Perference.setUserId(ssouser.getUserId());
                                Perference.set(Perference.NICK_NAME, ssouser.getUserName());
                                Perference.set(Perference.ROOT_ORG_ID, ssouser.getRootOrgId());
                                Perference.set(Perference.ORG_ID, ssouser.getOrgId());
                                Perference.setBoolean(Perference.OUT_SOURCE_FLAG, respLoginInfo.isOutsourceFlag());
                                Perference.set(Perference.PASSWORD_OVERDUE_INFO,respLoginInfo.getPasswordOverdueInfo());

                                //用户登录信息
                                UserLoginInfo info = new UserLoginInfo();
                                info.setAuthorization(ssouserBean.getToken());
                                info.setOrdId(ssouserBean.getOrgId());
                                info.setRootOrgId(ssouserBean.getRootOrgId());
                                info.setNickName(ssouserBean.getUserName());
                                info.setLoginName(loginName);
                                info.setLoginTime(System.currentTimeMillis());
                                info.setLoginSuccess(true);
                                info.setUserId(ssouserBean.getUserId());
                                info.setLoginErrorCount(0);
                                Perference.setBoolean(Perference.IS_LOGIN, true);
                                //第一次登录成功的时间
                                if (!OrmHelper.isUserExist(loginName)) {
                                    info.setFirstLoginTime(System.currentTimeMillis());
                                }
                                Perference.setBoolean(Constants.CHECK_IS_LOGIN, true);
                                EventBus.getDefault().post(new EventBean(BusEvent.LOGIN_SUCESS, info));

                                SugarRecord.save(info);
                                //  hideLoading();
                                //获取业务字典
                                // requestDicts();
                                requestDictData();
                            }


                        } else if ("9999".equals(respLoginInfo.getResultCode())) {
                            RxToast.showToast(respLoginInfo.getResultDesc());
                            hideLoading();
                        }
                    }
                });
    }




    private void startToMain() {
        //网络未连接时  输入正确的账号密码进入离线模式

        startActivity(new Intent(this, NavigationActivity.class));
        finish();
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:// 点击键盘回车触发
                onLogin();
                break;
            default:
                break;
        }
        return false;
    }


    @OnClick({R.id.ll_clear_password, R.id.ll_show_pssword, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_clear_password:
                mEtUserName.setText("");
                mEtPwd.setText("");

                break;
            case R.id.ll_show_pssword:
                Log.e("nb", "input:" + mEtPwd.getInputType());
                if (mEtPwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIvShowPassword.setImageResource(R.drawable.icon_eyes_close);
                } else {

                    mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIvShowPassword.setImageResource(R.drawable.icon_eyes_open);
                }
                //移动光标到末尾
                String pwd = mEtPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    mEtPwd.setSelection(pwd.length());
                }

                break;

            case R.id.btn_login:
                onLogin();
                break;
        }
    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    public void requestDictData() {
        SugarRecord.deleteAll(DictItemBean.class);
        Map<String, Object> map = new HashMap<>();
        map.put("dictCode", "");
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_DATA_BY_DICT_CODE, map)
                .compose(getRxlifecycle())
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver2<DictDataBean>() {
                    @Override
                    public void onError(String code, String msg) {
                        startToMain();
                    }

                    @Override
                    public void onNextData(DictDataBean dictDataBean) {
                        if (isFinishing()) {
                            return;
                        }
                        if (dictDataBean != null) {
                            SugarRecord.deleteAll(DictItemBean.class);
                        }
                        // mDictItemBeanList = dictDataBean.getZdtaayList();

                        if (dictDataBean.getEducationList() != null && dictDataBean.getEducationList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getEducationList());

                        }

                        if (dictDataBean.getLoanstatusList() != null && dictDataBean.getLoanstatusList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getLoanstatusList());
                            Log.e("nb", "getLoanstatusList");
                        }

                        if (dictDataBean.getNationalityList() != null && dictDataBean.getNationalityList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getNationalityList());
                        }

                        if (dictDataBean.getWorknatureList() != null && dictDataBean.getWorknatureList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getWorknatureList());
                        }

                        if (dictDataBean.getProductcodeList() != null && dictDataBean.getProductcodeList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getProductcodeList());
                        }

                        if (dictDataBean.getZdtaayList() != null && dictDataBean.getZdtaayList().size() > 0) {
                            SugarRecord.saveInTx(dictDataBean.getZdtaayList());
                        }

                        startToMain();
                    }
                });

    }
}
