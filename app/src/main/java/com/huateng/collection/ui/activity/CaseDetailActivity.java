package com.huateng.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flyco.systembar.SystemBarHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.ui.caseInfo.contract.CaseDetailContract;
import com.huateng.collection.ui.caseInfo.presenter.CaseDetailPresenter;
import com.huateng.collection.ui.dialog.BottomDialogView;
import com.huateng.collection.ui.fragment.casebox.casefill.PhotoSelectorActivity2;
import com.huateng.collection.ui.fragment.casebox.casefill.RecordSelectorActivity2;
import com.huateng.collection.ui.fragment.casebox.info.CreditCardMsgFragment;
import com.huateng.collection.ui.fragment.casebox.info.FragmentAccountInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentBaseInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentContactsBook;
import com.huateng.collection.ui.report.view.ReportListActivity;
import com.huateng.collection.widget.NoScrollViewPager;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.tab.TabEntity;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
/**
 * 案件详情fragment
 */

public class CaseDetailActivity extends BaseActivity<CaseDetailPresenter> implements CaseDetailContract.View {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.iv_load_more)
    ImageView mIvLoadMore;
    @BindView(R.id.tl_caseFill)
    CommonTabLayout mTlCaseFill;
    @BindView(R.id.bottom_dialog)
    BottomDialogView mBottomDialog;
    private FragmentPagerAdapter mAdapter;
    @BindView(R.id.container_root)
    View container_root;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.fl_container)
    NoScrollViewPager mFlContainer;
    private String[] mDoneCaseFillTitles;
    private int[] mIconUnselectIds;
    private int[] mIconSelectIds;
    private String[] mInfoTitles;

    private String[] mCaseMoreFillTitles = {"录音", "拍照", "调查报告", "结束处理", "停催", "留案", "退案", "申请减免"};
    private int[] mCaseMoreFillIcons = {R.drawable.icon_voice, R.drawable.icon_photo,
            R.drawable.icon_report_list, R.drawable.icon_report_end, R.drawable.icon_stop_urging
            , R.drawable.icon_leave_case, R.drawable.icon_case_back, R.drawable.icon_remission};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private String caseId;
    private String custId;
    private String custName;
    private String businessType;
   // private boolean caseStatus;

    @Override
    protected void initView(Bundle savedInstanceState) {

        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
        initListener();

        Intent intent = getIntent();
        caseId = intent.getStringExtra(Constants.CASE_ID);
        custId = intent.getStringExtra(Constants.CUST_ID);
        custName = intent.getStringExtra(Constants.CUST_NAME);
        businessType = intent.getStringExtra(Constants.BUSINESS_TYPE);
        rxTitle.setTitle("客户信息");

        boolean outSourceFlag = Perference.getBoolean(Perference.OUT_SOURCE_FLAG);
        if (outSourceFlag) {
            mCaseMoreFillTitles = new String[]{"录音", "拍照", "调查报告", "结束处理", "停催", "留案", "退案", "申请减免"};
        } else {
            mCaseMoreFillTitles = new String[]{"录音", "拍照", "调查报告", "结束处理"};
        }
        //  mCaseMoreFillTitles = new String[]{"录音", "拍照", "调查报告", "结束处理", "停催", "留案", "退案", "申请减免"};

        //删除临时文件夹里的文件
        //        FileUtils.deleteFilesInDir(AttachmentProcesser.getInstance(mContext).getTempsDir());
        if ("03".equals(businessType)) {
            mDoneCaseFillTitles = new String[]{"客户信息", "信用卡列表", "联系人列表"};
            mIconUnselectIds = new int[]{
                    R.drawable.icon_cust_msg, R.drawable.icon_card_info,
                    R.drawable.icon_contact_book};
            mIconSelectIds = new int[]{
                    R.drawable.icon_cust_msg_selected, R.drawable.icon_card_info_selected,
                    R.drawable.icon_contact_book_selected};
            mInfoTitles = new String[]{"客户信息", "信用卡列表", "联系人列表"};
        } else {
            mIconUnselectIds = new int[]{
                    R.drawable.icon_cust_msg, R.drawable.icon_account_info,
                    R.drawable.icon_contact_book};
            mIconSelectIds = new int[]{
                    R.drawable.icon_cust_msg_selected, R.drawable.icon_account_info_selected,
                    R.drawable.icon_contact_book_selected};
            mDoneCaseFillTitles = new String[]{"客户信息", "账户列表", "联系人列表"};
            mInfoTitles = new String[]{"客户信息", "账户列表", "联系人列表"};
        }

        //下方tab
        for (int i = 0; i < mDoneCaseFillTitles.length; i++) {
            mTabEntities.add(new TabEntity(mDoneCaseFillTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }


        mTlCaseFill.setTabData(mTabEntities);
        mTlCaseFill.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                rxTitle.setTitle(mInfoTitles[position]);
                mFlContainer.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });



    }

    private void initListener() {

        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   showLoadMore();
                mPresenter.getCaseStatus(caseId);
            }
        });


    }


    private void toReport() {
        Intent intent6 = new Intent(CaseDetailActivity.this, ReportListActivity.class);
        intent6.putExtra(Constants.CASE_ID, caseId);
        intent6.putExtra(Constants.CUST_ID, custId);
        intent6.putExtra(Constants.CUST_NAME, custName);
        startActivity(intent6);
    }

    /**
     * 跳转到录音页面
     */
    private void toAudio() {
        Intent intent = new Intent(CaseDetailActivity.this, RecordSelectorActivity2.class);
        intent.putExtra(Constants.CASE_ID, caseId);
        intent.putExtra(Constants.CUST_ID, custId);
        intent.putExtra(Constants.CUST_NAME, custName);
        startActivity(intent);
    }

    private void toPhoto() {
        Intent intent1 = new Intent(CaseDetailActivity.this, PhotoSelectorActivity2.class);
        intent1.putExtra(Constants.CASE_ID, caseId);
        intent1.putExtra(Constants.CUST_ID, custId);
        intent1.putExtra(Constants.CUST_NAME, custName);
        startActivity(intent1);
    }


    public void initFragments() {
        // Log.e("nb", "initFragments initFragments initFragments");
        //客户信息
        Bundle baseInfo = new Bundle();
        baseInfo.putString(Constants.CUST_ID, custId);
        baseInfo.putString(Constants.CASE_ID, caseId);
        mFragments.add(BaseFragment.newInstance(FragmentBaseInfo.class, baseInfo));

        if ("03".equals(businessType)) {
            //信用卡信息
            Bundle cardInfo = new Bundle();
            cardInfo.putString(Constants.CUST_ID, custId);
            cardInfo.putString(Constants.CASE_ID, caseId);
            mFragments.add(BaseFragment.newInstance(CreditCardMsgFragment.class, cardInfo));

        } else {
            //账户信息
            Bundle accountInfo = new Bundle();
            accountInfo.putString(Constants.CUST_ID, custId);
            accountInfo.putString(Constants.CASE_ID, caseId);
            mFragments.add(BaseFragment.newInstance(FragmentAccountInfo.class, accountInfo));

        }

        //联系人
        Bundle contactBook = new Bundle();
        contactBook.putString(Constants.CUST_ID, custId);
        contactBook.putString(Constants.CASE_ID, caseId);
        mFragments.add(BaseFragment.newInstance(FragmentContactsBook.class, contactBook));

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
        mFlContainer.setOffscreenPageLimit(3);


    }


    @Override
    protected CaseDetailPresenter createPresenter() {
        return new CaseDetailPresenter(this);
    }


    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_case_detail_2;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

        initFragments();
    }


    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, rxTitle);

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空当前案件信息
        Perference.setPrepareCallRecording(false);
        Perference.setPrepareRecordingPhoneNumber(null);

    }

    @Override
    public void finishPage() {
        finish();
    }

    @Override
    public void toCaseAction(boolean isProcess) {
     //   caseStatus = isProcess;
        List<BottomDialogBean> list = new ArrayList<>();
        for (int i = 0; i < mCaseMoreFillTitles.length; i++) {

            list.add(new BottomDialogBean(mCaseMoreFillIcons[i], mCaseMoreFillTitles[i], true));
        }
        mBottomDialog.initData(isProcess, list);
        mBottomDialog.setOnItemClickListener(new BottomDialogView.OnItemClickListener() {
            @Override
            public void onItemClick(BottomDialogBean bean) {
                String title = bean.getTitle();
                switch (title) {
                    case "录音":
                        //录音
                        toAudio();
                        break;
                    case "拍照":
                        //拍照
                        toPhoto();
                        break;
                    case "调查报告":
                        toReport();
                        break;
                    case "结束处理":
                        //结束案件
                        mPresenter.stopDealWithCase(caseId, custId);
                        break;
                    case "申请减免":
                    case "停催":
                    case "留案":
                    case "退案":
                        if (!isProcess) {
                            toCaseAction(title);
                        } else {
                            RxToast.showToast("已存在相同审核节点，不允许此操作");
                        }


                        break;
                }
            }
        });
        mBottomDialog.showView();
    }

    /**
     * 校验完成 跳转操作页面
     *
     * @param type
     */
    public void toCaseAction(String type) {
        switch (type) {
            case "申请减免":
                if (!"02".equals(businessType)) {
                    //减免申请
                    Intent intent1 = new Intent(CaseDetailActivity.this, RemissionActivity.class);
                    intent1.putExtra(Constants.CUST_ID, custId);
                    intent1.putExtra(Constants.CASE_ID, caseId);
                    intent1.putExtra(Constants.BUSINESS_TYPE, businessType);

                    startActivity(intent1);
                } else {
                    RxToast.showToast("直销案件不可以申请减免");
                }

                break;
            case "停催":
                //停催
                Intent intent2 = new Intent(CaseDetailActivity.this, StopUrgingActivity.class);
                intent2.putExtra(Constants.CASE_ID, caseId);
                startActivity(intent2);
                break;
            case "留案":
                //留案
                Intent intent3 = new Intent(CaseDetailActivity.this, LeaveCaseActivity.class);
                intent3.putExtra(Constants.CASE_ID, caseId);
                startActivity(intent3);
                break;
            case "退案":
                //退案
                Intent intent4 = new Intent(CaseDetailActivity.this, CaseBackActivity.class);
                intent4.putExtra(Constants.CASE_ID, caseId);
                startActivity(intent4);
                break;
        }
    }
}
