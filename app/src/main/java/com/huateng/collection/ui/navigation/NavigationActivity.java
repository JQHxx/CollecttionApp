package com.huateng.collection.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.service.LocationService;
import com.huateng.collection.ui.home.view.HomeFragment;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.gradientuilibrary.GradientIconView;
import com.huateng.collection.widget.gradientuilibrary.GradientTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    private FragmentPagerAdapter mAdapter;
   // private EventEnv eventEnv;
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
    @BindView(R.id.id_iconfont_faxian)
    GradientIconView mIdIconfontFaxian;
    @BindView(R.id.id_discover_tv)
    GradientTextView mIdDiscoverTv;
    @BindView(R.id.ll_tab_customer)
    LinearLayout mLlTabCustomer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        SystemBarHelper.immersiveStatusBar(this, 0.5f);
        initFragments();
        initView();
        initData();

    }

    private void initView() {

        Watermark.getInstance()
                .setTextColor(getResources().getColor(R.color.dialogplus_card_shadow))
                .setTextSize(12.0f)
                .setText("测试文本")
                .show(this);
    }

    private void initData() {
        //重置案件详情里设置的全局信息
        Perference.setCurrentCaseId(null);
        Perference.setCurrentVisitAddressId(null);
        Perference.setCurrentVisitAddress(null);
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
        startService(new Intent(this, LocationService.class));

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
        //待处理案件
        mTabs.add(BaseFragment.newInstance(HomeFragment.class));
        //已处理案件
        mTabs.add(BaseFragment.newInstance(FragmentDoneCaseChooser.class));
        //回访轨迹
        mTabs.add(BaseFragment.newInstance(FragmentMap.class));
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
    }

    @OnClick({R.id.ll_tab_main, R.id.ll_tab_customer, R.id.ll_tab_more,R.id.ll_tab_mine})
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

}
