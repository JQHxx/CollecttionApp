package com.huateng.collection.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespDicts;
import com.huateng.collection.bean.api.RespLoginInfo;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.bean.orm.UserLoginInfo;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.navigation.NavigationActivity;
import com.huateng.collection.utils.OrmHelper;
import com.huateng.collection.utils.StringUtils;
import com.huateng.fm.ui.widget.FmButton;
import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.orm.SugarRecord;
import com.tools.utils.NetworkUtils;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.tv_loginSystem)
    TextView mTvLoginSystem;
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
    private UserLoginInfo loginInfo;
    private long SYSTEM_LOCK_TIME = 1000 * 60 * 5;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        SystemBarHelper.immersiveStatusBar(this, 0.5f);
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

                if (count == 0) {
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void init() {
        Perference.clear();

        loginInfo = OrmHelper.getLastLoginUserInfo();
        String loginName = loginInfo.getLoginName();
        mEtUserName.setText(loginName);
        mEtPwd.setText(loginInfo.getPwd());

        //使用字体
        Typeface typeFace = ResourcesCompat.getFont(this, R.font.zcool_black);
        mTvLoginSystem.setTypeface(typeFace);

        //        ivSetting.setOnLongClickListener(new View.OnLongClickListener() {
        //            @Override
        //            public boolean onLongClick(View v) {
        //                Intent intent = new Intent(ActivityLogin.this, ActivityApiSetting.class);
        //                startActivity(intent);
        //                return false;
        //            }
        //        });

        //账户名改动后
        RxTextView.textChanges(mEtUserName).debounce(600, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                loginInfo.setLoginName(charSequence.toString());
                loginInfo.setUserId(null);
                loginInfo.setAuthorization(null);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   onLogin();
                loginInfo.setLoginName("张三");
                loginInfo.setUserId("b123456789");
                loginInfo.setLoginTime(System.currentTimeMillis());
                loginInfo.setLoginSuccess(true);
                SugarRecord.save(loginInfo);
                Perference.setUserId("b123456789");
                Perference.set(Perference.NICK_NAME, "张三");

                startToMain();
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

        //TODO 测试关闭
        //        long loginDelayTime = System.currentTimeMillis() - loginInfo.getLoginTime();
        //        if (loginInfo.getLoginErrorCount() >= 5 && loginDelayTime < SYSTEM_LOCK_TIME) {
        //            toast(String.format("账号密码连续输入错误超过五次,系统锁定中,剩余时间：%s", CommonUtils.formatTimeMillis(SYSTEM_LOCK_TIME - loginDelayTime)));
        //            return;
        //        }
        //
        //        if(!Validation.pwdCheck(password)){
        //            return;
        //        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", loginName);
        map.put("password", password);

        CommonInteractor.loginRequest(new RequestCallbackImpl<RespLoginInfo>() {

            @Override
            public void beforeRequest() {
                super.beforeRequest();
                showLoading();
            }

            @Override
            public void requestError(String code, String msg) {
                super.requestError(code, msg);
                //                if (code.equals(ApiConstants.CODE_NO_CACHE) || msg.contains("登录失败")) {
                //                    RxToast.showToast("工号或密码不正确,请重新输入,若连续错误5次,系统将锁定5分钟");
                //                    loginInfo.setLoginName(loginName);
                //                    loginInfo.setLoginTime(System.currentTimeMillis());
                //                    loginInfo.setLoginSuccess(false);
                //                    loginInfo.setLoginErrorCount(loginInfo.getLoginErrorCount() + 1);
                //                    SugarRecord.save(loginInfo);
                //                }
                hideLoading();

            }

            @Override
            public void response(RespLoginInfo respLoginInfo) {

                Perference.setUserId(respLoginInfo.getUserId());
                Perference.set(Perference.NICK_NAME, respLoginInfo.getSsouser().getUserName());

                RespLoginInfo.SsouserBean ssouserBean = respLoginInfo.getSsouser();
                //保存登陆auth 信息
                NetworkConfig.C.setAuth(ssouserBean.getToken());

                //用户登录信息
                UserLoginInfo info = new UserLoginInfo();
                info.setUserId(respLoginInfo.getUserId());
                info.setAuthorization(ssouserBean.getToken());
                info.setPwd(password);
                info.setNickName(ssouserBean.getUserName());
                info.setLoginName(loginName);
                info.setLoginTime(System.currentTimeMillis());
                info.setLoginSuccess(true);
                info.setLoginErrorCount(0);


                //第一次登录成功的时间
                if (!OrmHelper.isUserExist(loginName)) {
                    info.setFirstLoginTime(System.currentTimeMillis());
                }

                SugarRecord.save(info);

                //获取业务字典
                requestDicts();
                startToMain();
            }


        }, map);
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


    public void requestDicts() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Perference.getUserId());
        CommonInteractor.request(new RequestCallbackImpl<RespDicts>() {
            @Override
            public void response(RespDicts respDicts) {
                SugarRecord.deleteAll(Dic.class);
                SugarRecord.saveInTx(respDicts.getDataList());

                //请求字典成功后去主页面
                startToMain();
            }

            @Override
            public void end() {
                super.end();
                hideLoading();
            }
        }, ApiConstants.APP_ROOT, ApiConstants.METHOD_QUERY_DICT, map);

    }

    private void startToMain() {
        //网络未连接时  输入正确的账号密码进入离线模式
        if (!NetworkUtils.isConnected()) {
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
        }
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

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
