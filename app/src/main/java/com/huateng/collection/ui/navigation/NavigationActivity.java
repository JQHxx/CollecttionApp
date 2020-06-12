package com.huateng.collection.ui.navigation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.ui.activity.LoginActivity;
import com.huateng.collection.ui.dialog.AlertDialogFragment;
import com.huateng.collection.ui.dialog.AlertFragmentUtil;
import com.huateng.collection.ui.home.view.HomeFragment;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.gradientuilibrary.GradientIconView;
import com.huateng.collection.widget.gradientuilibrary.GradientTextView;
import com.tools.ActivityUtils;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * author: yichuan
 * Created on: 2020-03-25 15:46
 * description:
 */
public class NavigationActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.id_iconfont_mine)
    GradientIconView mIdIconfontMine;
    @BindView(R.id.id_about_mine_tv)
    GradientTextView mIdAboutMineTv;
    @BindView(R.id.ll_tab_mine)
    LinearLayout mLlTabMine;
    @BindView(R.id.id_iconfont_faxian)
    GradientIconView mIdIconfontFaxian;
    @BindView(R.id.id_discover_tv)
    GradientTextView mIdDiscoverTv;
    @BindView(R.id.ll_tab_customer)
    LinearLayout mLlTabCustomer;
    private FragmentPagerAdapter mAdapter;
    private List<GradientIconView> mTabIconIndicator = new ArrayList<GradientIconView>();
    private List<GradientTextView> mTabTextIndicator = new ArrayList<GradientTextView>();
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    @BindView(R.id.id_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.id_iconfont_chat)
    GradientIconView mIdIconfontChat;
    @BindView(R.id.id_chats_tv)
    GradientTextView mIdChatsTv;
    @BindView(R.id.ll_tab_main)
    LinearLayout mLlTabMain;
    @BindView(R.id.id_iconfont_me)
    GradientIconView mIdIconfontMe;
    @BindView(R.id.id_about_me_tv)
    GradientTextView mIdAboutMeTv;
    @BindView(R.id.ll_tab_more)
    LinearLayout mLlTabMore;
    @BindView(R.id.ll_bottom_menu)
    LinearLayout mLlBottomMenu;
    @BindView(R.id.drawerlayout)
    LinearLayout mDrawerlayout;
    private long TOUCH_TIME = 0;
    private static final long WAIT_TIME = 2000L;
    private boolean dialogHintShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("nb", "onCreate");
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        SystemBarHelper.immersiveStatusBar(this, 0.5f);
        ActivityUtils.getAppManager().addActivity(this);
        EventBus.getDefault().register(this);

        initFragments();
        initView();
        initData();

    }

    private void initView() {

        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
    }


    private void initData() {

        //重置案件详情里设置的全局信息
        // Perference.setCurrentCaseId(null);
        Perference.setPrepareCallRecording(false);
        Perference.setPrepareRecordingPhoneNumber(null);
        //切换
        // eventEnv = new EventEnv(BusEvent.START_SETTING_PAGE);

        //创建案件文件目录
        AttachmentProcesser.getInstance(getApplicationContext()).initPath();
        //        //设置不是第一次登录
        Config.setBoolean(Config.FIRST_OPEN, false);

        //加载图片数据
        //        dataSource = new ImageDataSource(ActivityMain.this, AttachmentProcesser.ATTACHMENT_ROOT, ActivityMain.this);
        //开启定位信息
        //  startService(new Intent(this, LocationService.class));

        //通话监听
        /*Intent intent = new Intent(this, PhoneListenerService.class);
        startService(intent);*/
    }


    private void checkTab(int position) {
        switch (position) {
            case 0:
                mLlTabMain.getBackground().setLevel(10);
                break;
            case 1:
                mLlTabCustomer.getBackground().setLevel(10);
                break;
            case 2:
                mLlTabMore.getBackground().setLevel(10);
                break;
            case 3:
                mLlTabMine.getBackground().setLevel(10);
                break;
            default:

        }
    }

    private void initFragments() {

        if (!Perference.getBoolean(Perference.OUT_SOURCE_FLAG)) {
            mLlTabCustomer.setVisibility(View.VISIBLE);
            mLlTabMore.setVisibility(View.VISIBLE);
        } else {
            mLlTabCustomer.setVisibility(View.GONE);
            mLlTabMore.setVisibility(View.GONE);
        }


        Log.e("nb", "initFragments");
        //待处理案件
        mTabs.add(BaseFragment.newInstance(HomeFragment.class));
        //已处理案件
        mTabs.add(BaseFragment.newInstance(StatisticsFragment.class));
        //回访轨迹
        mTabs.add(BaseFragment.newInstance(FragmentMap2.class));
        //我的
        mTabs.add(BaseFragment.newInstance(MineFragment.class));
        checkTab(0);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {

                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(4);


        mTabIconIndicator.add(mIdIconfontChat);
        mTabIconIndicator.add(mIdIconfontFaxian);
        mTabIconIndicator.add(mIdIconfontMe);
        mTabIconIndicator.add(mIdIconfontMine);

        mTabTextIndicator.add(mIdChatsTv);
        mTabTextIndicator.add(mIdDiscoverTv);
        mTabTextIndicator.add(mIdAboutMeTv);
        mTabTextIndicator.add(mIdAboutMineTv);

        mIdIconfontChat.setIconAlpha(1.0f);
        mIdChatsTv.setTextViewAlpha(1.0f);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //左->右 positionOffset：0--->1     右->左 positionOffset： 1--->0
        if (positionOffset > 0) {
            GradientIconView iconLeft = mTabIconIndicator.get(position);
            GradientIconView iconRight = mTabIconIndicator.get(position + 1);

            GradientTextView textLeft = mTabTextIndicator.get(position);
            GradientTextView textRight = mTabTextIndicator.get(position + 1);

            iconLeft.setIconAlpha(1 - positionOffset);
            textLeft.setTextViewAlpha(1 - positionOffset);

            iconRight.setIconAlpha(positionOffset);
            textRight.setTextViewAlpha(positionOffset);
        }
    }


    @Override
    public void onPageSelected(int position) {
        checkTab(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        // 滑动完毕也要重置其他栏的颜色,0表示什么都没做1表示正在滑动2表示滑动完毕.
        if (state == ViewPager.SCROLL_STATE_SETTLING) {
            resetTabs();
        }
    }

    /**
     * 重置Tab背景
     */
    private void resetTabs() {
        for (int i = 0; i < mTabIconIndicator.size(); i++) {
            mTabIconIndicator.get(i).setIconAlpha(0);
            mTabTextIndicator.get(i).setTextViewAlpha(0);
        }
        mLlTabMain.getBackground().setLevel(5);
        mLlTabCustomer.getBackground().setLevel(5);
        mLlTabMore.getBackground().setLevel(5);

        mLlTabMine.getBackground().setLevel(5);
    }

    //R.id.id_iconfont_faxian,
    @OnClick({R.id.ll_tab_main, R.id.ll_tab_customer, R.id.ll_tab_more, R.id.ll_tab_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab_main:
                resetTabs();
                mTabIconIndicator.get(0).setIconAlpha(1.0f);
                mTabTextIndicator.get(0).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                checkTab(0);
                break;

            case R.id.ll_tab_customer:
                resetTabs();
                mTabIconIndicator.get(1).setIconAlpha(1.0f);
                mTabTextIndicator.get(1).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                checkTab(1);
                break;
            case R.id.ll_tab_more:
                resetTabs();
                mTabIconIndicator.get(2).setIconAlpha(1.0f);
                mTabTextIndicator.get(2).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                checkTab(2);
                break;
            case R.id.ll_tab_mine:
                resetTabs();
                mTabIconIndicator.get(3).setIconAlpha(1.0f);
                mTabTextIndicator.get(3).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                checkTab(3);

                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(NavigationActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        if (bean == null) {
            return;
        }

        Log.e("nb",bean.code+":");

        if (bean.getCode() == BusEvent.TOKEN_OVERDUE) {
            Activity activity = ActivityUtils.getAppManager().currentActivity();
            if(activity == null) {
                return;
            }

            if (!dialogHintShow) {
                dialogHintShow = true;
                AlertFragmentUtil.showAlertDialog((FragmentActivity) activity, "登录过期，请重新登录", new AlertDialogFragment.OnDialogButtonClickListener() {
                    @Override
                    public void onClickLeft() {
                        dialogHintShow = false;
                    }

                    @Override
                    public void onClickRight() {
                       startActivity(new Intent(activity, LoginActivity.class));
                        dialogHintShow = false;
                       finish();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
