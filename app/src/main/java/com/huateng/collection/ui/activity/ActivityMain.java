package com.huateng.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespBase;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.event.EventEnv;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.service.LocationService;
import com.huateng.collection.service.PhoneListenerService;
import com.huateng.collection.ui.base.BaseActivity;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.fragment.home.FragmentHome;
import com.huateng.collection.utils.DataCleanManager;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.tools.utils.RxActivityUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;


/**
 * Created by sumincy on 2018/11/12.
 */

public class ActivityMain extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.layout_modifyPassword)
    LinearLayout layoutModifyPassword;
    @BindView(R.id.tv_clearCache)
    TextView tvClearCache;
    @BindView(R.id.tv_cacheSize)
    TextView tvCacheSize;
    @BindView(R.id.layout_clearCache)
    LinearLayout layoutClearCache;
    @BindView(R.id.ic_updateDot)
    ImageView icUpdateDot;
    @BindView(R.id.layout_checkVersion)
    LinearLayout layoutCheckVersion;
    @BindView(R.id.layout_logout)
    LinearLayout layoutLogout;

    private FragmentHome homeFragment;

    private EventEnv eventEnv;

    private CommonContentDM contentDM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SystemBarHelper.immersiveStatusBar(this, 0.5f);
        init();
    }

    private void init() {
        //重置案件详情里设置的全局信息
        Perference.setCurrentCaseId(null);
        Perference.setCurrentVisitAddressId(null);
        Perference.setCurrentVisitAddress(null);
        Perference.setPrepareCallRecording(false);
        Perference.setPrepareRecordingPhoneNumber(null);

        tvName.setText(Perference.get(Perference.NICK_NAME));
        //切换
        eventEnv = new EventEnv(BusEvent.START_SETTING_PAGE);

        layoutModifyPassword.setOnClickListener(this);
        layoutClearCache.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);
        layoutCheckVersion.setOnClickListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();


        if (findFragment(FragmentHome.class) == null) {
            homeFragment = (FragmentHome) BaseFragment.newInstance(FragmentHome.class);
            loadRootFragment(R.id.fl_container, homeFragment);
        }


        //创建案件文件目录
        AttachmentProcesser.getInstance(getApplicationContext()).initPath();
//        //设置不是第一次登录
        Config.setBoolean(Config.FIRST_OPEN, false);

        //加载图片数据
//        dataSource = new ImageDataSource(ActivityMain.this, AttachmentProcesser.ATTACHMENT_ROOT, ActivityMain.this);
        //获取缓存大小
        updateCacheSize();
        startService(new Intent(this, LocationService.class));

        //通话监听
        Intent intent = new Intent(this, PhoneListenerService.class);
        startService(intent);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.layout_modifyPassword:
                eventEnv.put(Constants.POSITION, 0);
                EventBusActivityScope.getDefault(this).post(eventEnv);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_clearCache:
                eventEnv.put(Constants.POSITION, 1);
                EventBusActivityScope.getDefault(this).post(eventEnv);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_checkVersion:
                eventEnv.put(Constants.POSITION, 2);
                EventBusActivityScope.getDefault(this).post(eventEnv);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.layout_logout:
                contentDM = new DialogCenter(this).showLogoutDialog();
                contentDM.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
                    @Override
                    public void onLeftClicked(View v) {
                        Map<String, String> map = new HashMap<>();
                        map.put("token", NetworkConfig.C.getAuth());
                        map.put("url", "http://170.252.199.174:18088/ccms-app/service/request/call2.htm");

                        CommonInteractor.request(new RequestCallbackImpl<RespBase>() {

                            @Override
                            public void beforeRequest() {
                                super.beforeRequest();
                                showLoading();
                            }

                            @Override
                            public void response(RespBase resp) {

                            }

                            @Override
                            public void end() {
                                super.end();

                                stopService(new Intent(ActivityMain.this, LocationService.class));

                                drawerLayout.closeDrawer(Gravity.START);
                                contentDM.getDialog().dismiss();
                                NetworkConfig.C.setAuth(null);

//                                Perference.clear();

//                                SugarRecord.deleteAll(UserLoginInfo.class);

                                RxActivityUtils.skipActivityAndFinishAll(ActivityMain.this, ActivityLogin.class);

                            }
                        }, ApiConstants.APP_ROOT, ApiConstants.METHOD_LOGOUT, map);

                    }

                    @Override
                    public void onRightClicked(View v) {
                        contentDM.getDialog().dismiss();
                    }
                });
                break;
            default:
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void updateCacheSize() {
        tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
    }

    @Subscribe
    public void showUpdateDot(EventEnv eventEnv) {
        if (BusEvent.VERSION_UPDATE.equals(eventEnv.obtainEvent())) {
            icUpdateDot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressedSupport() {
//        //将app切换到后台
        moveTaskToBack(true);
    }

}
