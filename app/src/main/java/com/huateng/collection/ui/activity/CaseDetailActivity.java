package com.huateng.collection.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.flyco.systembar.SystemBarHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.api.RespLog;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.ui.dialog.BottomDialogFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.CorrentEndCaseDM;
import com.huateng.collection.ui.dialog.dm.WrongEndCaseDM;
import com.huateng.collection.ui.fragment.casebox.casefill.PhotoSelectorActivity;
import com.huateng.collection.ui.fragment.casebox.info.CreditMsgFragment;
import com.huateng.collection.ui.fragment.casebox.info.FragmentAccountInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentBaseInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentContactsBook;
import com.huateng.collection.ui.report.view.ReportActivity;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.NoScrollViewPager;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.tab.TabEntity;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tools.bean.BusEvent;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zrht.common.bean.BottomDialogBean;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

/**
 * 案件详情fragment
 */

public class CaseDetailActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.iv_load_more)
    ImageView mIvLoadMore;
    private FragmentPagerAdapter mAdapter;
    @BindView(R.id.container_root)
    View container_root;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.tl_detailInfo)
    SegmentTabLayout tlDetailInfo;
    @BindView(R.id.tl_caseFill)
    CommonTabLayout tlCaseFill;
    @BindView(R.id.fl_container)
    NoScrollViewPager mFlContainer;


    private String[] mInfoTitles = {"客户信息", "账户信息", "授信信息", "电话簿"};
    private String[] mCaseFillTitles = {"录音", "拍照", "外访报告", "结束处理"};


    private String[] mDoneCaseFillTitles = {"录音", "拍照", "外访报告"};

    private int[] mIconUnselectIds = {
            R.drawable.voice_bottom, R.drawable.camera_bottom,
            R.drawable.report_bottom, R.drawable.finish_bottom};
    private int[] mIconSelectIds = {
            R.drawable.voice_bottom, R.drawable.camera_bottom,
            R.drawable.report_bottom, R.drawable.finish_bottom};


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int mPrePosition = -1;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private String caseId;
    private String addrId;
    private String visitAddress;


    private RespCaseDetail caseDetail;

    private ArrayList<RespAccount> respAccounts;
    private ArrayList<RespPhone> respPhones;
    private ArrayList<RespAddress> respAddresses;
    private ArrayList<RespLog> respLogs;

    private boolean isTodoCase;


    @Override
    protected void initView(Bundle savedInstanceState) {
        SystemBarHelper.immersiveStatusBar(this, 0);
        SystemBarHelper.setHeightAndPadding(this, rxTitle);

        Watermark.getInstance()
                .setTextColor(getResources().getColor(R.color.dialogplus_card_shadow))
                .setTextSize(12.0f)
                .setText("测试文本")
                .show(this);

        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                List<BottomDialogBean> list = new ArrayList<>();
                for (int i=0;i<mCaseFillTitles.length;i++){

                    list.add(new BottomDialogBean(mIconUnselectIds[i],mCaseFillTitles[i]));
                }
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                BottomDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),"bottom_dialog");
            }
        });


        Intent intent = getIntent();

        addrId = intent.getStringExtra(Constants.ADDRESS_ID);
        visitAddress = intent.getStringExtra(Constants.VISIT_ADDRESS);
        caseId = intent.getStringExtra(Constants.CASE_ID);
        isTodoCase = intent.getBooleanExtra(Constants.IS_TODO_CASE, false);

        //保存当前案件信息
        Perference.setCurrentCaseId(caseId);
        Perference.setCurrentVisitAddressId(addrId);
        Perference.setCurrentVisitAddress(visitAddress);

        Log.e("nb", caseId + ":" + addrId + ":" + visitAddress);

        //删除临时文件夹里的文件
        //        FileUtils.deleteFilesInDir(AttachmentProcesser.getInstance(mContext).getTempsDir());

        tlDetailInfo.setTabData(mInfoTitles);

        tlDetailInfo.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFlContainer.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        if (isTodoCase) {
            //下方tab
            for (int i = 0; i < mCaseFillTitles.length; i++) {
                mTabEntities.add(new TabEntity(mCaseFillTitles[i], mIconUnselectIds[i], mIconSelectIds[i]));
            }
        } else {
            //下方tab
            for (int i = 0; i < mDoneCaseFillTitles.length; i++) {
                mTabEntities.add(new TabEntity(mDoneCaseFillTitles[i], mIconUnselectIds[i], mIconUnselectIds[i]));
            }
        }


        tlCaseFill.setTabData(mTabEntities);
        tlCaseFill.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                casefillSelect(position);
            }

            @Override
            public void onTabReselect(int position) {
                casefillSelect(position);
            }
        });


        //获取权限
        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(CAMERA, RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            RxToast.showToast("录音权限被禁止,请在设置页面开启");
                        }
                    }
                });
    }


    public void casefillSelect(int position) {
        switch (position) {
            case 0:
                // Logger.i("To FragmentRecord");//
                startActivity(new Intent(CaseDetailActivity.this, AudioRecordListActivity.class));

                break;
            case 1:
                // Logger.i("To FragmentTakePhoto");
                startActivity(new Intent(CaseDetailActivity.this, PhotoSelectorActivity.class));


                break;
            case 2:
                //Logger.i("To FragmentReport");
                Intent intent = new Intent(CaseDetailActivity.this, ReportActivity.class);
                intent.putExtra("title", "外访报告");
                startActivity(intent);

                break;
            case 3:

                boolean hasAudio = CaseManager.recordCompleted(addrId);

                boolean hasPhoto = CaseManager.takePhotoCompleted(addrId);

                if (!hasAudio) {
                    showWrongTodoDialog("当前案件未进行录音,不能结束处理!");
                    return;
                }

                if (!hasPhoto) {
                    showWrongTodoDialog("当前案件未进行拍照,不能结束处理!");
                    return;
                }

                final CorrentEndCaseDM dm = new DialogCenter(this).showCorrentEndCaseDialog();
                dm.setContent("确定案件处理结束？");
                dm.setDescription("结束处理后，案件将转移至发件箱。");
                dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
                    @Override
                    public void onLeftClicked(View v) {
                        dm.getDialog().dismiss();
                        Log.e("nb", "addrId:" + addrId);
                        RespCaseSummary caseSummary = CaseManager.obtainCachedCaseSummary(addrId);

                        //设置done
                        if (caseSummary != null) {
                            caseSummary.setDone(true);
                            SugarRecord.save(caseSummary);
                        } else {
                            Logger.w("RespCaseSummary : biz id %s 查询失败", addrId);
                        }
                        //刷新收件箱
                        EventBus.getDefault().post(new EventBean(BusEvent.PICK_CASES_TO_OUTBOX, addrId));
                        finish();
                    }

                    @Override
                    public void onRightClicked(View v) {
                        dm.getDialog().dismiss();
                    }
                });

                break;
        }
    }


    public void initFragments() {
        Log.e("nb", "initFragments initFragments initFragments");
        //客户信息
        Bundle baseInfo = new Bundle();
        baseInfo.putSerializable(Constants.CASE_DETAIL, caseDetail);//
        mFragments.add(BaseFragment.newInstance(FragmentBaseInfo.class, baseInfo));
        //账户信息
        Bundle accountInfo = new Bundle();
        accountInfo.putSerializable(Constants.CASE_ACCOUNT_INFO, respAccounts);
        mFragments.add(BaseFragment.newInstance(FragmentAccountInfo.class, accountInfo));
        //授信信息
        Bundle creditInfo = new Bundle();
        creditInfo.putString("msg", "");
        mFragments.add(BaseFragment.newInstance(CreditMsgFragment.class, creditInfo));

        //联系人
        Bundle contactBook = new Bundle();
        contactBook.putSerializable(Constants.CASE_CONTACT_BOOK, respPhones);
        mFragments.add(BaseFragment.newInstance(FragmentContactsBook.class, contactBook));
        //外出流水
     /*   Bundle historyActions = new Bundle();
        historyActions.putSerializable(Constants.CASE_HISTORY_ACTIONS, respLogs);
        mFragments.add(BaseFragment.newInstance(FragmentHistoryActions.class, historyActions));
*/

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
        mFlContainer.setOffscreenPageLimit(5);

        //选中
        tlDetailInfo.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFlContainer.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }


    private void showWrongTodoDialog(String wrongMsg) {
        final WrongEndCaseDM dm = new DialogCenter(this).showWrongEndCaseDialog();
        dm.setContent(wrongMsg);
        dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
            @Override
            public void onLeftClicked(View v) {
                dm.getDialog().dismiss();
            }

            @Override
            public void onRightClicked(View v) {
                dm.getDialog().dismiss();
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
        return R.layout.fragment_case_detail_2;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

        //有缓存
        if (CaseManager.isCaseCached(addrId)) {

            caseDetail = CaseManager.obtainCachedCaseDetail(addrId);

            respAccounts = (ArrayList<RespAccount>) CaseManager.obtainCachedAccounts(addrId);
            respAddresses = (ArrayList<RespAddress>) CaseManager.obtainCachedAddresses(addrId);
            respLogs = (ArrayList<RespLog>) CaseManager.obtainCachedLogs(addrId);
            respPhones = (ArrayList<RespPhone>) CaseManager.obtainCachedPhones(addrId);

            //设置当前用户名称
            Perference.setCurrentCustId(caseDetail.getCustId());
            Perference.setCurrentCustName(caseDetail.getName());
            //初始化
            initFragments();
        } else {
            initFragments();
        /*    final List<String> caseIds = new ArrayList<>();
            caseIds.add(caseId);
            String caseIdStr = GsonUtils.toJson(caseIds);

            Map<String, String> map = new HashMap<>();
            map.put("caseList", caseIdStr);

            CommonInteractor.requestEntrys(new RequestCallbackImpl<List<RespCaseDetail>>() {
                @Override
                public void beforeRequest() {
                    super.beforeRequest();
                    showLoading();
                }

                @Override
                public void response(List<RespCaseDetail> caseDetails) {
                    RespCaseDetail respCaseDetail = caseDetails.get(0);

                    Perference.setCurrentCustId(respCaseDetail.getCustId());
                    //设置当前用户名称
                    Perference.setCurrentCustName(respCaseDetail.getName());

                    caseDetail = respCaseDetail;

                    respAccounts = (ArrayList) respCaseDetail.getAcctList();
                    respPhones = (ArrayList) respCaseDetail.getTelList();
                    respAddresses = (ArrayList) respCaseDetail.getAddrList();
                    respLogs = (ArrayList) respCaseDetail.getActLogList();

                    CaseManager.cacheCaseDetail(respCaseDetail, addrId);

                }

                @Override
                public void end() {
                    super.end();
                    //初始化
                    initFragments();
                    hideLoading();
                }
            }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_GET_CASE_DETAIL_BATCH, map);
*/
        }

        tlDetailInfo.setCurrentTab(0);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空当前案件信息
        Perference.setCurrentCaseId(null);
        Perference.setCurrentVisitAddressId(null);
        Perference.setCurrentVisitAddress(null);
        Perference.setPrepareCallRecording(false);
        Perference.setPrepareRecordingPhoneNumber(null);

    }

}
