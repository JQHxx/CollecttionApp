package com.huateng.collection.ui.fragment.casebox;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.systembar.SystemBarHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.api.RespLog;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.event.EventEnv;
import com.huateng.collection.network.CommonInteractor;
import com.huateng.collection.network.RequestCallbackImpl;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.ui.dialog.DialogCenter;
import com.huateng.collection.ui.dialog.dm.BaseDM;
import com.huateng.collection.ui.dialog.dm.CorrentEndCaseDM;
import com.huateng.collection.ui.dialog.dm.WrongEndCaseDM;
import com.huateng.collection.ui.fragment.casebox.casefill.FragmentPhotoSelector;
import com.huateng.collection.ui.fragment.casebox.casefill.FragmentRecordSelector;
import com.huateng.collection.ui.fragment.casebox.casefill.FragmentReport;
import com.huateng.collection.ui.fragment.casebox.info.FragmentAccountInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentBaseInfo;
import com.huateng.collection.ui.fragment.casebox.info.FragmentContactsAddress;
import com.huateng.collection.ui.fragment.casebox.info.FragmentContactsBook;
import com.huateng.collection.ui.fragment.casebox.info.FragmentHistoryActions;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.tab.TabEntity;
import com.huateng.network.ApiConstants;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tools.utils.GsonUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;
import rx.functions.Action1;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

/**
 * 案件详情fragment
 */

public class FragmentCaseDetail extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.container_root)
    View container_root;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.tl_detailInfo)
    SegmentTabLayout tlDetailInfo;
    @BindView(R.id.tl_caseFill)
    CommonTabLayout tlCaseFill;


    private String[] mInfoTitles = {"基础信息", "账户信息", "电话簿", "地址簿", "行动流水"};
    private String[] mCaseFillTitles = {"录音", "拍照", "外访报告", "结束处理"};


    private String[] mDoneCaseFillTitles = {"录音", "拍照", "外访报告"};

    private int[] mIconUnselectIds = {
            R.drawable.voice_bottom_un, R.drawable.camera_bottom_un,
            R.drawable.report_bottom_un, R.drawable.finish_bottom_un};
    private int[] mIconSelectIds = {
            R.drawable.voice_bottom, R.drawable.camera_bottom,
            R.drawable.report_bottom, R.drawable.finish_bottom};


    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int mPrePosition = -1;

    private SupportFragment[] mFragments = new SupportFragment[5];


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_case_detail_2, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        SystemBarHelper.immersiveStatusBar((Activity) mContext, 0);
        SystemBarHelper.setHeightAndPadding(mContext, rxTitle);

        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });


        Bundle bundle = getArguments();

        addrId = bundle.getString(Constants.ADDRESS_ID);
        visitAddress = bundle.getString(Constants.VISIT_ADDRESS);
        caseId = bundle.getString(Constants.CASE_ID);
        isTodoCase = bundle.getBoolean(Constants.IS_TODO_CASE);

        //保存当前案件信息
        Perference.setCurrentCaseId(caseId);
        Perference.setCurrentVisitAddressId(addrId);
        Perference.setCurrentVisitAddress(visitAddress);

        //删除临时文件夹里的文件
        //        FileUtils.deleteFilesInDir(AttachmentProcesser.getInstance(mContext).getTempsDir());

        tlDetailInfo.setTabData(mInfoTitles);

        tlDetailInfo.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchContentFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        if (isTodoCase) {
            //下方tab
            for (int i = 0; i < mCaseFillTitles.length; i++) {
                mTabEntities.add(new TabEntity(mCaseFillTitles[i], mIconUnselectIds[i], mIconUnselectIds[i]));
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
        RxPermissions rxPermissions = new RxPermissions(getActivity());

        rxPermissions.request(CAMERA, RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (!granted) {
                            RxToast.showToast("录音权限被禁止,请在设置页面开启");
                        }
                    }
                });

    }


    public void casefillSelect(int position) {
        switch (position) {
            case 0:
                Logger.i("To FragmentRecord");//FragmentRecordSelector
                //   BaseFragment fragmentRecord = BaseFragment.newInstance(FragmentRecord.class);
                //  startBrotherFragment(fragmentRecord);
               /* String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
                Intent intent = new Intent(mContext, AudioRecordServiceActivity.class);
                intent.putExtra(AndroidAudioRecorder.EXTRA_FILE_PATH, filePath);
                intent.putExtra(AndroidAudioRecorder.EXTRA_COLOR, getResources().getColor(R.color.colorPrimaryDark));
                startActivity(intent);*/

                BaseFragment framentRecordSelector = BaseFragment.newInstance(FragmentRecordSelector.class);
                startBrotherFragment(framentRecordSelector);

                break;
            case 1:
                Logger.i("To FragmentTakePhoto");

                BaseFragment fragmentTakePhoto = BaseFragment.newInstance(FragmentPhotoSelector.class);
                startBrotherFragment(fragmentTakePhoto);
                break;
            case 2:
                Logger.i("To FragmentReport");
                BaseFragment fragmentReport = BaseFragment.newInstance(FragmentReport.class);
                startBrotherFragment(fragmentReport);
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

                final CorrentEndCaseDM dm = new DialogCenter(getActivity()).showCorrentEndCaseDialog();
                dm.setContent("确定案件处理结束？");
                dm.setDescription("结束处理后，案件将转移至发件箱。");
                dm.setOnFooterButtonClickListener(new BaseDM.OnFooterButtonClickListener() {
                    @Override
                    public void onLeftClicked(View v) {
                        dm.getDialog().dismiss();
                        RespCaseSummary caseSummary = CaseManager.obtainCachedCaseSummary(addrId);

                        //设置done
                        if (caseSummary != null) {
                            caseSummary.setDone(true);
                            SugarRecord.save(caseSummary);
                        } else {
                            Logger.w("RespCaseSummary : biz id %s 查询失败", addrId);
                        }

                        EventEnv eventEnv = new EventEnv(BusEvent.PICK_CASES_TO_OUTBOX);
                        eventEnv.put(Constants.BIZ_ID, addrId);

                        EventBusActivityScope.getDefault(_mActivity).post(eventEnv);
                        pop();
                    }

                    @Override
                    public void onRightClicked(View v) {
                        dm.getDialog().dismiss();
                    }
                });
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findFragment(FragmentBaseInfo.class);
        if (firstFragment == null) {
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

                final List<String> caseIds = new ArrayList<>();
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

                        //初始化
                        initFragments();

                        CaseManager.cacheCaseDetail(respCaseDetail, addrId);
                    }

                    @Override
                    public void end() {
                        super.end();
                        hideLoading();
                    }
                }, ApiConstants.BATCH_ROOT, ApiConstants.METHOD_GET_CASE_DETAIL_BATCH, map);

            }

            tlDetailInfo.setCurrentTab(0);

        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[0] = firstFragment;
            mFragments[1] = findFragment(FragmentAccountInfo.class);
            mFragments[2] = findFragment(FragmentContactsBook.class);
            mFragments[3] = findFragment(FragmentContactsAddress.class);
            mFragments[4] = findFragment(FragmentHistoryActions.class);
        }
    }

    public void initFragments() {
        //基础信息
        Bundle baseInfo = new Bundle();
        baseInfo.putSerializable(Constants.CASE_DETAIL, caseDetail);
        mFragments[0] = BaseFragment.newInstance(FragmentBaseInfo.class, baseInfo);
        //清算账号
        Bundle accountInfo = new Bundle();
        accountInfo.putSerializable(Constants.CASE_ACCOUNT_INFO, respAccounts);
        mFragments[1] = BaseFragment.newInstance(FragmentAccountInfo.class, accountInfo);
        //联系电话
        Bundle contactBook = new Bundle();
        contactBook.putSerializable(Constants.CASE_CONTACT_BOOK, respPhones);
        mFragments[2] = BaseFragment.newInstance(FragmentContactsBook.class, contactBook);
        //联系地址
        Bundle contactAddress = new Bundle();
        contactAddress.putSerializable(Constants.CASE_CONTACT_ADDRESS, respAddresses);
        mFragments[3] = BaseFragment.newInstance(FragmentContactsAddress.class, contactAddress);
        //外出流水
        Bundle historyActions = new Bundle();
        historyActions.putSerializable(Constants.CASE_HISTORY_ACTIONS, respLogs);
        mFragments[4] = BaseFragment.newInstance(FragmentHistoryActions.class, historyActions);

        loadMultipleRootFragment(R.id.fl_container, 0,
                mFragments[0], mFragments[1], mFragments[2], mFragments[3], mFragments[4]);

    }


    private void showWrongTodoDialog(String wrongMsg) {
        final WrongEndCaseDM dm = new DialogCenter(getActivity()).showWrongEndCaseDialog();
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
        if (null != topFragment && !(topFragment instanceof FragmentCaseDetail)) {
            hideSoftInput();
            topFragment.popTo(FragmentCaseDetail.class, false);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        //清空当前案件信息
        Perference.setCurrentCaseId(null);
        Perference.setCurrentVisitAddressId(null);
        Perference.setCurrentVisitAddress(null);
        Perference.setPrepareCallRecording(false);
        Perference.setPrepareRecordingPhoneNumber(null);

        pop();
        return true;
    }
}
