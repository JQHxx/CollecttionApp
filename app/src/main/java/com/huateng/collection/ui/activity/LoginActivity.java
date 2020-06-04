package com.huateng.collection.ui.activity;

import android.content.DialogInterface;
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
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.DictDataBean;
import com.huateng.collection.bean.api.RespLoginInfo;
import com.huateng.collection.bean.orm.DictItemBean;
import com.huateng.collection.bean.orm.UserLoginInfo;
import com.huateng.collection.ui.navigation.NavigationActivity;
import com.huateng.collection.utils.CommonUtils;
import com.huateng.collection.utils.OrmHelper;
import com.huateng.collection.utils.StringUtils;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orm.SugarRecord;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.functions.Action1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by shanyong on 2016/12/13.
 */

@RuntimePermissions
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
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, mRxTitle);

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
        if(isFirst) {
            mRxTitle.setLeftIconVisibility(true);
            mRxTitle.setLeftTextVisibility(true);
        }
    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {

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
       /* mIvSetting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ActivityApiSetting.class);
                startActivity(intent);
                return false;
            }
        });*/
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
        //String password = loginInfo.getPwd();
        // Log.e("nb", loginName + ":" + password);
        mEtUserName.setText(loginName);
       // mEtPwd.setText(password);
        mBtnLogin.setEnabled(false);
       /* if (StringUtils.isEmpty(loginName, password)) {
            mBtnLogin.setEnabled(false);
        } else {
            mBtnLogin.setEnabled(true);
        }*/
        Perference.setBoolean(Constants.CHECK_IS_LOGIN, false);


        //        ivSetting.setOnLongClickListener(new View.OnLongClickListener() {
        //            @Override
        //            public boolean onLongClick(View v) {
        //                Intent intent = new Intent(ActivityLogin.this, ActivityApiSetting.class);
        //                startActivity(intent);
        //                return false;
        //            }
        //        });

        //账户名改动后
        RxTextView.textChanges(mEtUserName).debounce(600, TimeUnit.MILLISECONDS)
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                // .subscribeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        Log.e("nb", "12345");
                        if (charSequence.length() > 0) {
                            if(mLlClearPassword!= null) {
                                mLlClearPassword.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if(mLlClearPassword!= null) {
                                mLlClearPassword.setVisibility(View.INVISIBLE);
                            }
                        }

                        loginInfo.setLoginName(charSequence.toString());
                        loginInfo.setUserId(null);
                        loginInfo.setAuthorization(null);
                    }
                });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        LoginActivityPermissionsDispatcher.initSDcardPermissionsWithPermissionCheck(this);
    }


    public void onLogin() {

        final String loginName = mEtUserName.getText().toString();
        final String password = mEtPwd.getText().toString();

        if (StringUtils.isEmpty(loginName, password)) {
            RxToast.showToast("用户名或密码不能为空");
            return;
        }

        long loginDelayTime = System.currentTimeMillis() - loginInfo.getLoginTime();
        if (loginInfo.getLoginErrorCount() >= 5 && loginDelayTime < SYSTEM_LOCK_TIME) {
            RxToast.showToast(String.format("账号密码连续输入错误超过五次,系统锁定中,剩余时间：%s", CommonUtils.formatTimeMillis(SYSTEM_LOCK_TIME - loginDelayTime)));
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", loginName);
        map.put("password", password);
        RetrofitManager.getInstance()
                .loginRequest(map)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 订阅之前回调回去显示加载动画
                        showLoading();
                    }
                })// 订阅之前操作在主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<RespLoginInfo>() {
                    @Override
                    public void onError(String code, String msg) {
                        Log.e("nb", msg + ":" + code);
                        if (code.equals(ApiConstants.ERROR_CODE_EXP) && "登录失败,请检查账户名和密码！！".equals(msg)) {
                            RxToast.showToast("登录失败,请重新输入,若连续错误5次,系统将锁定5分钟");
                            loginInfo.setLoginName(loginName);
                            loginInfo.setLoginTime(System.currentTimeMillis());
                            loginInfo.setLoginSuccess(false);
                            loginInfo.setLoginErrorCount(loginInfo.getLoginErrorCount() + 1);
                            SugarRecord.save(loginInfo);
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                RxToast.showToast(msg);
                            }

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
                        RespLoginInfo.SsouserBean ssouser = respLoginInfo.getSsouser();
                        Perference.setUserId(ssouser.getUserId());
                        Perference.set(Perference.NICK_NAME, ssouser.getUserName());
                        Perference.set(Perference.ROOT_ORG_ID, ssouser.getRootOrgId());
                        Perference.set(Perference.ORG_ID, ssouser.getOrgId());
                        Perference.setBoolean(Perference.OUT_SOURCE_FLAG,respLoginInfo.isOutsourceFlag());
                        RespLoginInfo.SsouserBean ssouserBean = respLoginInfo.getSsouser();
                        //保存登陆auth 信息
                        NetworkConfig.C.setAuth(ssouserBean.getToken());

                        //用户登录信息
                        Log.e("nb", "orgId" + ssouserBean.getOrgId());
                        UserLoginInfo info = new UserLoginInfo();
                        // info.setUserId(respLoginInfo.getUserId());
                        info.setAuthorization(ssouserBean.getToken());
                       // info.setPwd(password);
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
                });
    }


    //权限设置
    @NeedsPermission({READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE})
    public void initSDcardPermissions() {
        // btnLogin.setEnabled(true);
        LoginActivityPermissionsDispatcher.initLocationPermissionsWithPermissionCheck(this);
    }

    @OnPermissionDenied({READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE})
    public void onPermissionsDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        RxToast.showToast("读写存储卡权限被禁止，应用将无法正常使用");
        mBtnLogin.setEnabled(false);
    }


    @OnShowRationale({READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE})
    public void showRationaleForPermission(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("应用需要读写储存卡权限才能正常运行，是否申请？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                }).show();
    }

    @NeedsPermission({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE})
    public void initLocationPermissions() {

    }

    @OnPermissionDenied({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE})
    public void onLocationPermissionsDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        RxToast.showToast("定位权限被禁止，将无法获取定位信息");
    }

    @OnShowRationale({ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_PHONE_STATE})
    public void showLocationRationaleForPermission(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("外访轨迹需要定位权限，是否申请？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                }).show();
    }


    private void startToMain() {
        //网络未连接时  输入正确的账号密码进入离线模式
       /* if (!NetworkUtils.isConnected()) {
            final CommonContentDM dm = new DialogCenter(LoginActivity.this).showOfflineModeDialog();
            dm.setOnFooterButtonClickListener(new CommonContentDM.OnFooterButtonClickListener() {
                @Override
                public void onLeftClicked(View v) {
                    dm.getDialog().dismiss();
                }

                @Override
                public void onRightClicked(View v) {
                    dm.getDialog().dismiss();
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(this, NavigationActivity.class));
            finish();
        }*/
        if (!isFirst) {
            startActivity(new Intent(this, NavigationActivity.class));
        }
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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


    @OnClick({R.id.ll_clear_password, R.id.ll_show_pssword,})
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

                break;
        }
    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }


    public void requestDictData() {
        SugarRecord.deleteAll(DictItemBean.class);
        Map<String, String> map = new HashMap<>();
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
