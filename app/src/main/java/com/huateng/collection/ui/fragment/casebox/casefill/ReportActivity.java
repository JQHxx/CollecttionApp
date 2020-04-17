package com.huateng.collection.ui.fragment.casebox.casefill;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.huateng.collection.R;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.ReportSegmentRecyclerItem;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.widget.UniversalInputType;
import com.huateng.collection.widget.tab.IconTextTabAdapter;
import com.huateng.collection.widget.tab.IconTextTabEntity;
import com.huateng.collection.widget.tab.RecyclerTabLayout;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.tools.bean.BusEvent;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 外访报告
 */
public class ReportActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.recycler_tab_layout)
    RecyclerTabLayout recyclerTabLayout;
    @BindView(R.id.fl_container)
    ViewPager mFlContainer;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private String[] titles = {"基本情况", "应门情况", "电话情况", "邻居反应", "物业反应", "门卫反应"
            , "同事反应", "沟通情况", "其他"};
    private int[] titleIconResIds = {R.drawable.title_base_info, R.drawable.title_answer_door_info, R.drawable.title_call_info, R.drawable.title_neighbor_info, R.drawable.title_estate_info, R.drawable.title_doorkeeper_info, R.drawable.title_workmate_info
            , R.drawable.title_communication_info, R.drawable.title_other_info};
    private List<List<ReportSegmentRecyclerItem>> datalist = new ArrayList<>();

    //下方tab
    private int[] tabIconRes = {R.drawable.base_info, R.drawable.answer_door_info, R.drawable.call_info,
            R.drawable.neighbor_info, R.drawable.estate_info, R.drawable.doorkeeper_info, R.drawable.workmate_info,
            R.drawable.communication_info, R.drawable.other_info};

    private List<IconTextTabEntity> tabEntities;

    private int mPrePosition = -1;

    private FragmentPagerAdapter mAdapter;

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
        return R.layout.fragment_report;
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

    /**
     * 处理顶部title
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBean(BusEvent.SAVE_REPORT));
                RxToast.showToast("保存成功");
            }
        });

        initFragments();
    }


    private void initFragments() {
        Log.e(TAG, "initFragments");
        initRecyclerItems();

        tabEntities = new ArrayList<>();
        //初始化
        for (int i = 0; i < titles.length; i++) {
            mFragments.add(FragmentReportSegment.newInstance(titles[i], titleIconResIds[i], datalist.get(i)));
            IconTextTabEntity tab = new IconTextTabEntity();
            tab.setText(titles[i]);
            tab.setIconRes(tabIconRes[i]);
            tabEntities.add(tab);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {

                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

        };

        mFlContainer.setAdapter(mAdapter);
       // mFlContainer.addOnPageChangeListener(this);
        mFlContainer.setOffscreenPageLimit(9);
        recyclerTabLayout.setUpWithAdapter(new IconTextTabAdapter(recyclerTabLayout, tabEntities));

        //选中
        recyclerTabLayout.setOnTabSelectListener(new RecyclerTabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFlContainer.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }


    private ReportSegmentRecyclerItem getRecyclerItem(String label, int inputType, List<Dic> dicItems, int index) {
        ReportSegmentRecyclerItem item = new ReportSegmentRecyclerItem();
        item.setLabel(label);
        item.setInputType(inputType);
        item.setDicItems(dicItems);
        item.setPosition(index);
        return item;
    }

    private ReportSegmentRecyclerItem getRecyclerItem(String label, int inputType, int index) {
        return getRecyclerItem(label, inputType, null, index);
    }

    private void initRecyclerItems() {
        initRecyclerItemsBaseInfo();
        initRecyclerItemsAnswerDoorInfo();
        initRecyclerItemsCallInfo();
        initRecyclerItemsNeighborInfo();
        initRecyclerItemsEstateInfo();
        initRecyclerItemsDoorKepperInfo();
        initRecyclerItemsWorkMateInfo();
        initRecyclerItemsCommunicationInfo();
        initRecyclerItemsOtherInfo();
    }


    private void initRecyclerItemsBaseInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("案件号", UniversalInputType.TEXT, 1));
        items.add(getRecyclerItem("客户姓名", UniversalInputType.TEXT, 2));
        items.add(getRecyclerItem("地址", UniversalInputType.IMAGE, 3));


        items.add(getRecyclerItem("外访人员", UniversalInputType.EDIT.TEXT, 4));
        items.add(getRecyclerItem("外访日期", UniversalInputType.DATE.DATE_SINGEL, 5));

        //        items.add(getRecyclerItem("录音打开时间", UniversalInput.TIME, 6));
        //        items.add(getRecyclerItem("录音结束时间", UniversalInput.TIME, 7));
        //        items.add(getRecyclerItem("录音编号", UniversalInput.SPINNER, 8));
        //        items.add(getRecyclerItem("照片编号", UniversalInput.SPINNER, 9));

        items.add(getRecyclerItem("拍照人", UniversalInputType.EDIT.TEXT, 10));
        items.add(getRecyclerItem("入照人", UniversalInputType.EDIT.TEXT, 11));
        items.add(getRecyclerItem("每层户数", UniversalInputType.EDIT.NUMBER, 12));
        items.add(getRecyclerItem("门的颜色", UniversalInputType.EDIT.TEXT, 13));
        items.add(getRecyclerItem("邮箱检查", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 14));
        items.add(getRecyclerItem("信件类别", UniversalInputType.EDIT.TEXT, 15));
        items.add(getRecyclerItem("收件人姓名", UniversalInputType.EDIT.TEXT, 16));
        items.add(getRecyclerItem("邮寄日期", UniversalInputType.DATE.DATE_SINGEL, 17));
        datalist.add(items);
    }

    private void initRecyclerItemsAnswerDoorInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("应门", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 18));
        items.add(getRecyclerItem("应门人身份", UniversalInputType.SPINNER, Dic.dics(Dic.RELATION), 19));
        items.add(getRecyclerItem("应门人姓氏", UniversalInputType.EDIT.TEXT, 20));
        items.add(getRecyclerItem("应门人性别", UniversalInputType.SPINNER, Dic.dics(Dic.SEX), 21));
        items.add(getRecyclerItem("装修情况", UniversalInputType.EDIT.TEXT, 22));
        datalist.add(items);
    }

    private void initRecyclerItemsCallInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("电话类型", UniversalInputType.SPINNER, Dic.dics(Dic.TELIPHONE), 23));
        items.add(getRecyclerItem("电话号码", UniversalInputType.EDIT.NUMBER, 24));
        items.add(getRecyclerItem("拨打次数", UniversalInputType.EDIT.NUMBER, 25));
        items.add(getRecyclerItem("拨打时间", UniversalInputType.DATE.TIME, 26));
        items.add(getRecyclerItem("室内是否有铃音", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 27));
        datalist.add(items);
    }


    private void initRecyclerItemsNeighborInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("姓氏", UniversalInputType.EDIT.TEXT, 28));
        items.add(getRecyclerItem("性别", UniversalInputType.SPINNER, Dic.dics(Dic.SEX), 29));
        items.add(getRecyclerItem("屋主/房客", UniversalInputType.SPINNER, Dic.dics(Dic.HOUSE_IDENTITY), 30));
        items.add(getRecyclerItem("是否有人居住", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 31));
        items.add(getRecyclerItem("居住日期/空置日期", UniversalInputType.DATE.DATE_SINGEL, 32));
        datalist.add(items);
    }


    private void initRecyclerItemsEstateInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("姓氏", UniversalInputType.EDIT.TEXT, 33));
        items.add(getRecyclerItem("性别", UniversalInputType.SPINNER, Dic.dics(Dic.SEX), 34));
        items.add(getRecyclerItem("物业/居委", UniversalInputType.SPINNER, Dic.dics(Dic.PROPERTY_COMMITTEE), 35));
        items.add(getRecyclerItem("是否有人居住", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 36));
        items.add(getRecyclerItem("居住日期/空置日期", UniversalInputType.DATE.DATE_SINGEL, 37));
        datalist.add(items);
    }

    private void initRecyclerItemsDoorKepperInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("姓氏", UniversalInputType.EDIT.TEXT, 38));
        items.add(getRecyclerItem("性别", UniversalInputType.SPINNER, Dic.dics(Dic.SEX), 39));
        items.add(getRecyclerItem("门卫/前台", UniversalInputType.EDIT.TEXT, 40));
        items.add(getRecyclerItem("客户是否在职", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 41));
        items.add(getRecyclerItem("居住日期/空置日期", UniversalInputType.DATE.DATE_SINGEL, 42));
        datalist.add(items);
    }

    private void initRecyclerItemsWorkMateInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("姓氏", UniversalInputType.EDIT.TEXT, 43));
        items.add(getRecyclerItem("性别", UniversalInputType.SPINNER, Dic.dics(Dic.SEX), 44));
        items.add(getRecyclerItem("HR/其他同事", UniversalInputType.EDIT.TEXT, 45));
        items.add(getRecyclerItem("客户是否在职", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 46));
        items.add(getRecyclerItem("在职日期/离职日期", UniversalInputType.DATE.DATE_SINGEL, 47));
        datalist.add(items);
    }

    private void initRecyclerItemsCommunicationInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("沟通情况", UniversalInputType.EDIT.TEXT, 48));
        datalist.add(items);
    }

    private void initRecyclerItemsOtherInfo() {
        List<ReportSegmentRecyclerItem> items = new ArrayList<>();
        items.add(getRecyclerItem("催款通知书编号", UniversalInputType.EDIT.TEXT, 49));
        items.add(getRecyclerItem("客户新地址", UniversalInputType.EDIT.TEXT, 53));
        items.add(getRecyclerItem("签收人", UniversalInputType.SPINNER, Dic.dics(Dic.RELATION), 50));
        items.add(getRecyclerItem("客户新电话", UniversalInputType.EDIT.NUMBER, 52));
        items.add(getRecyclerItem("法律告知书留置", UniversalInputType.SPINNER, Dic.dics(Dic.WEATHER), 51));
        items.add(getRecyclerItem("新联系人", UniversalInputType.EDIT.TEXT, 54));
        items.add(getRecyclerItem("新联系人联系方式", UniversalInputType.EDIT.NUMBER, 55));
        datalist.add(items);
    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

}
