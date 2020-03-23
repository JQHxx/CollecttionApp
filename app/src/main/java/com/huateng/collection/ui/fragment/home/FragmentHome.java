package com.huateng.collection.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.MainApplication;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.event.EventEnv;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.CommonContentDM;
import com.huateng.collection.ui.fragment.casebox.FragmentDoneCaseChooser;
import com.huateng.collection.ui.fragment.casebox.FragmentTodoCaseChooser;
import com.huateng.collection.ui.fragment.map.FragmentMap;
import com.huateng.collection.ui.fragment.map.FragmentMapTC;
import com.huateng.collection.ui.fragment.setting.FragmentCheckVersion;
import com.huateng.collection.ui.fragment.setting.FragmentClearCache;
import com.huateng.collection.ui.fragment.setting.FragmentModifyPassword;
import com.huateng.collection.ui.fragment.statics.FragmentStatics;
import com.huateng.collection.widget.tab.NavigatorView;
import com.huateng.collection.widget.tab.TabEntity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by sumincy on 2018/11/12.
 */

public class FragmentHome extends BaseFragment implements NavigatorView.OnNavigatorViewItemClickListener {

    @BindView(R.id.tl_home_tab)
    CommonTabLayout tlHomeTab;

    private int mPrePosition = -1;

    private SupportFragment[] mFragments = new SupportFragment[5];
//    private String[] mhomeTabTitles = {"收件箱", "发件箱", "外访统计", "外访轨迹"};
    private String[] mhomeTabTitles = {"收件箱", "发件箱",  "外访轨迹"};

//    private int[] mIconUnselectIds = {
//            R.drawable.ic_inbox, R.drawable.ic_outbox,
//            R.drawable.ic_statics, R.drawable.ic_trace};
//    private int[] mIconSelectIds = {
//            R.drawable.ic_inbox_checked, R.drawable.ic_outbox_checked,
//            R.drawable.ic_statics_checked, R.drawable.ic_trace_checked};

    private int[] mIconUnselectIds = {
            R.drawable.ic_inbox, R.drawable.ic_outbox,
            R.drawable.ic_trace};
    private int[] mIconSelectIds = {
            R.drawable.ic_inbox_checked, R.drawable.ic_outbox_checked,
            R.drawable.ic_trace_checked};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        EventBusActivityScope.getDefault(_mActivity).register(this);
        return view;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        //下方tab
        for (int i = 0; i < mhomeTabTitles.length; i++) {
            mTabEntities.add(new TabEntity(mhomeTabTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tlHomeTab.setTabData(mTabEntities);
        tlHomeTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchContentFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findFragment(FragmentTodoCaseChooser.class);
        if (firstFragment == null) {
            mFragments[0] = BaseFragment.newInstance(FragmentTodoCaseChooser.class);
            mFragments[1] = BaseFragment.newInstance(FragmentDoneCaseChooser.class);
//            mFragments[2] = BaseFragment.newInstance(FragmentStatics.class);
            mFragments[2] = BaseFragment.newInstance(FragmentMap.class);


            loadMultipleRootFragment(R.id.fl_container, 0,
                    mFragments[0], mFragments[1], mFragments[2]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[0] = firstFragment;
            mFragments[1] = findFragment(FragmentDoneCaseChooser.class);
//            mFragments[2] = findFragment(FragmentStatics.class);
            mFragments[2] = findFragment(FragmentMap.class);
        }
    }

    @Override
    public void onNavigatorViewItemClick(int position, View view) {
        if (MainApplication.getApplication().isCurrentCaseOnOperation()) {
            final CommonContentDM dm = new DialogCenter(mContext).showCurrentCaseOnOperationDialog();
            dm.setOnFooterButtonClickListener(new CommonContentDM.OnFooterButtonClickListener() {
                @Override
                public void onLeftClicked(View v) {
                    dm.getDialog().dismiss();
                }

                @Override
                public void onRightClicked(View v) {
                    dm.getDialog().dismiss();
                }
            });
            return;
        }

        switchContentFragment(position);
    }


    /**
     * 替换加载 内容Fragment
     *
     * @param position
     */
    public void switchContentFragment(int position) {
//        Logger.i("p  : %s     c : %s", mPrePosition, position);

        if (null != mFragments && position < mFragments.length) {
            final SupportFragment fragment = mFragments[position];

            if (mPrePosition == -1) {
                showHideFragment(fragment);
            } else {
                showHideFragment(fragment, mFragments[mPrePosition]);
            }

            this.mPrePosition = position;
        }

        SupportFragment topFragment = (SupportFragment) getTopFragment();
        if (null != topFragment && !(topFragment instanceof FragmentHome)) {
            hideSoftInput();
//          点击主页tab  pop到mainfragment
            topFragment.popTo(FragmentHome.class, false);
        }
    }


    @Subscribe
    public void startSettingPage(EventEnv eventEnv) {
        if (BusEvent.START_SETTING_PAGE.equals(eventEnv.obtainEvent())) {
            Integer position = (Integer) eventEnv.get(Constants.POSITION);
            if (position == 0) {

                BaseFragment fragment = findFragment(FragmentModifyPassword.class);
                if (null == fragment) {
                    startBrotherFragment(BaseFragment.newInstance(FragmentModifyPassword.class), SINGLETASK);
                } else {
                    startBrotherFragment(fragment, SINGLETASK);
                }
            } else if (position == 1) {
                BaseFragment fragment = findFragment(FragmentClearCache.class);
                if (null == fragment) {
                    startBrotherFragment(BaseFragment.newInstance(FragmentClearCache.class), SINGLETASK);
                } else {
                    startBrotherFragment(fragment, SINGLETASK);
                }
            } else if (position == 2) {
                BaseFragment fragment = findFragment(FragmentCheckVersion.class);
                if (null == fragment) {
                    startBrotherFragment(BaseFragment.newInstance(FragmentCheckVersion.class), SINGLETASK);
                } else {
                    startBrotherFragment(fragment, SINGLETASK);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }
}
