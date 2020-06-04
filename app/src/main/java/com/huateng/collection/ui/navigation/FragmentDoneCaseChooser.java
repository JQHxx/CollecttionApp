/*
package com.huateng.collection.ui.navigation;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseFragment;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.orm.Outbound;
import com.huateng.collection.network.events.NotificationActions;
import com.huateng.collection.ui.adapter.DoneCasesAdapter;
import com.huateng.collection.utils.OutboundManager;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.utils.sqltoexcel.SQLiteToExcel;
import com.huateng.collection.widget.DividerItemDecoration;
import com.huateng.network.ApiConstants;
import com.huateng.network.NetworkConfig;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.bean.EventBean;
import com.tools.utils.FileIOUtils;
import com.tools.utils.FileUtils;
import com.tools.utils.GsonUtils;
import com.tools.utils.TimeUtils;
import com.tools.utils.ZipUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.tools.bean.BusEvent;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

*
 * 发件箱


public class FragmentDoneCaseChooser extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    @BindView(R.id.tv_selAll)
    TextView tvSelAll;
    @BindView(R.id.tv_selNone)
    TextView tvSelNone;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.v_selectOptions)
    LinearLayout vSelectOptions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    private DoneCasesAdapter adapter;

    private View emptyView;

    private List<RespCaseSummary> caseSummaries;

    private UploadReceiver uploadReceiver;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


*
     * 获取布局ID
     *
     * @return


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_done_case_chooser;
    }


*
     * 数据初始化操作


    @Override
    protected void initData() {
        //  Log.e("nb",NetworkConfig.C.getBaseURL() + "file/upload.htm");
    }


*
     * 处理顶部title
     *
     * @param savedInstanceState


    @Override
    protected void initView(Bundle savedInstanceState) {
        immersiveStatusBar(rxTitle);
        rxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vSelectOptions.getVisibility() == View.VISIBLE && caseSummaries.size() > 0 && !uploadMode) {
                    List<Integer> selectPositions = adapter.getSelectedPositions();
                    needUploadSize = selectPositions.size();

                    Logger.i(String.valueOf(needUploadSize));

                    if (adapter.inSelectMode() && needUploadSize > 0) {
                        uploadMode = true;
                        uploadCases(selectPositions);
                    } else {
                        RxToast.showToast("请至少选中一个案件进行上传");
                    }

                } else if (uploadMode) {
                    RxToast.showToast("请耐心等待当前案件上传成功");
                } else {
                    vSelectOptions.setVisibility(View.VISIBLE);
                    adapter.goSelectMode();
                }
            }
        });

        //        vSearch.setOnClickListener(this);
        //        ivUpload.setOnClickListener(this);
        //        tvCancel.setOnClickListener(this);
        //        tvSelAll.setOnClickListener(this);
        //        tvSelNone.setOnClickListener(this);

        // TODO recyclerview 截获了父布局的点击事件
        tvCancel.setOnTouchListener(this);
        tvSelAll.setOnTouchListener(this);
        tvSelNone.setOnTouchListener(this);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        emptyView = inflater.inflate(R.layout.layout_empty_view, (ViewGroup) recyclerView.getParent(), false);
        TextView tvTip = (TextView) emptyView.findViewById(R.id.tv_tip);

        //使用字体
        Typeface typeFace = ResourcesCompat.getFont(mContext, R.font.zcool_black);
        tvTip.setTypeface(typeFace);
        tvTip.setText("无处理完成的案件");

        caseSummaries = CaseManager.obtainDoneCaseSummaryForUser();
        if (null == caseSummaries) {
            caseSummaries = new ArrayList<>();
        }

        adapter = new DoneCasesAdapter(R.layout.list_item_done_cases, caseSummaries, mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        if (null == caseSummaries || caseSummaries.size() <= 0) {
            adapter.setEmptyView(emptyView);
        }

        //注册上传文件广播
        uploadReceiver = new UploadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huateng.phone.collection.uploadservice.broadcast.status");
        mContext.registerReceiver(uploadReceiver, intentFilter);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        if (bean == null) {
            return;
        }
        if (bean.getCode() == BusEvent.REFRESH_DONE_CASES) {
            Log.e("nb","收到home通知");
            List<RespCaseSummary> summaries = CaseManager.obtainDoneCaseSummaryForUser();
            if (null != summaries && summaries.size() > 0) {
                this.caseSummaries.clear();
                this.caseSummaries.addAll(summaries);
                adapter.notifyDataSetChanged();
            } else {
                this.caseSummaries.clear();
                adapter.notifyDataSetChanged();
                vSelectOptions.setVisibility(View.GONE);
                adapter.leaveSelectMode();
                adapter.setEmptyView(emptyView);
            }
        }

    }

    //上传预操作
    private void preUpload(final int position) {
        Logger.i("preload position", "------------>" + position);

        final RespCaseSummary summary = caseSummaries.get(position);

        //TODO 测试关闭
        if (Constants.CASE_UPLOADING.equals(summary.getUploadStatus())) {
            Logger.i("id为:%s的案件正在上传", summary.getBizId());
            return;
        }

        RxToast.showToast("开始处理");
        summary.setUploadStatus(Constants.CASE_PRE_UPLOAD);
        adapter.notifyDataSetChanged();

        final String addrId = summary.getAddrId();
        final String caseId = summary.getCaseId();

        final String processId = caseId;

        //上传一些案件信息
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("caseId", summary.getCaseId());
        paramMap.put("custName", summary.getCustName());
        paramMap.put("addrId", summary.getAddrId());
        paramMap.put("addrType", summary.getAddrType());
        paramMap.put("visitAddress", summary.getVisitAddress());
        paramMap.put("userId", Perference.getUserId());
        paramMap.put("date", TimeUtils.getNowTimeString("yyyyMMdd"));

        generateReportFile(processId, addrId);
        generateGpsFile(processId);

        final String zipsFilePath = AttachmentProcesser.getInstance(getActivity()).getZipPath(processId);
        //删除已存在文件
        boolean del = FileUtils.deleteFile(zipsFilePath);
        Logger.i(String.valueOf(del));

        //使用Rxjava 对压缩文件后 上传文件做线程调度
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                //压缩文件
                boolean ziped = false;
                try {
                    ziped = ZipUtils.zipFile(AttachmentProcesser.getInstance(getActivity())
                                    .getCaseRoot(processId),
                            zipsFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean toUpload = false;
                if (ziped) {
                    File file = new File(zipsFilePath);
                    if (file.length() <= 1024 * 1024 * 100) {
                        toUpload = true;
                    }
                }
                subscriber.onNext(toUpload);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean toUpload) {
                        if (toUpload) {
                            if (NetworkConfig.C.getApiMode().equals(ApiConstants.API_MODE_CUSTOM)) {
                                adapter.mockProgress(position);
                            } else {
                                //上传文件
                                final String uploadId = UUID.randomUUID().toString();
                                Logger.i("upload id %s", uploadId);

                                try {
                                    final MultipartUploadRequest request =
                                            new MultipartUploadRequest(mContext, uploadId, NetworkConfig.C.getBaseURL() + "file/upload.htm")
                                                    .setMethod("POST")
                                                    .setUtf8Charset()
                                                    .setNotificationConfig(getNotificationConfig(uploadId, R.string.multipart_upload))
                                                    .setMaxRetries(3)
                                                    //.setCustomUserAgent(getUserAgent())
                                                    .setUsesFixedLengthStreamingMode(true);

                                    String fileName = new File(zipsFilePath).getName();
                                    request.addHeader("authorization", NetworkConfig.C.getAuth());
                                    request.addParameter("callback", "AppFileOperateServiceImpl/upload");
                                    request.addParameter("fileName", fileName);
                                    request.addParameter(paramMap);

                                    request.addFileToUpload(zipsFilePath, "file\"; filename=\"" + fileName);
                                    //TODO 电话录音文件处理
                                    //                                  request.addFileToUpload(zipsFilePath, "file\"; filename=\"" + fileName);

                                    Log.e("nb", request.toString());
                                    RxToast.showToast("开始上传");
                                    summary.setUploadStatus(Constants.CASE_UPLOADING);
                                    summary.setUploadId(uploadId);
                                    adapter.notifyDataSetChanged();
                                    request.startUpload();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            summary.setUploadStatus(Constants.CASE_FILE_TOO_LARGE);
                            adapter.notifyDataSetChanged();
                            RxToast.showToast("单个案件上传附件大小不能超过100M");
                            doneUploadCase();
                        }
                    }
                });

    }

    private void generateGpsFile(String processId) {
        //RxBus.get().post(BusTag.GENERATE_OUTBOUND_ROUTE, caseId);
        //TODO 图片 生成gps 截图文件   切换到地图页面才能生成 暂时传坐标点点
        String gpsPath = AttachmentProcesser.getInstance(getActivity()).getGpsPath(processId);
        File floder = new File(gpsPath);
        File gpsFile = new File(floder, String.format("gps_%s_%s_%s.json", processId, TimeUtils.getNowTimeString("yyyyMMdd"), "01"));

        List<Outbound> outbounds = OutboundManager.obtainOutbounds();

        String data = GsonUtils.toJson(outbounds);
        //将数据写入文件
        FileIOUtils.writeFileFromString(gpsFile, data, false);
    }


    private void uploadCases(List<Integer> positions) {
        for (int i = 0; i < positions.size(); i++) {
            preUpload(positions.get(i));
        }
    }

    //生成外访文件
    private void generateReportFile(String processId, String addrId) {

        String dbPath = getActivity().getDatabasePath("collection.db").getAbsolutePath();

        new SQLiteToExcel
                .Builder(mContext)
                .setDataBase(dbPath)
                //                .setTables("user")
                .setOutputPath(AttachmentProcesser.getInstance(getActivity()).getReportPath(processId))
                .setOutputFileName(String.format("rpt_%s_%s.xls", processId, TimeUtils.getNowTimeString("yyyyMMdd")))
                .setSQL("外访报告", String.format("SELECT KEY as '名称' , CONTENT as '内容' FROM PENDING_REPORT_DATA WHERE BIZ_ID ='%s' ORDER BY  POSITION ", addrId))
                //                .setEncryptKey("1234567")
                //                .setProtectKey("9876543")
                .start(new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                        //                        Logger.i("Export importTables--->");
                    }

                    @Override
                    public void onCompleted(String filePath) {
                        //                        Logger.i("Export completed--->" + filePath);
                    }

                    @Override
                    public void onError(Exception e) {
                        //                        Logger.e("Export error--->" + e.toString());
                    }
                });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_cancel:
                vSelectOptions.setVisibility(View.GONE);
                adapter.leaveSelectMode();
                break;
            case R.id.tv_selAll:
                adapter.selectAll();
                break;
            case R.id.tv_selNone:*/
/**//*

                adapter.reverse();*/
/**//*

                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    vSelectOptions.setVisibility(View.GONE);
                    if (!uploadMode) {
                        initUploadStatus();
                    }
                    adapter.leaveSelectMode();
                    break;
                case R.id.tv_selAll:
                    adapter.selectAll();
                    break;
                case R.id.tv_selNone:
                    adapter.reverse();
                    break;
            }
        }
        return false;
    }


    //上传通知栏
    protected UploadNotificationConfig getNotificationConfig(final String uploadId,
                                                             @StringRes int title) {
        UploadNotificationConfig config = new UploadNotificationConfig();

        PendingIntent clickIntent = PendingIntent.getActivity(
                mContext, 1, new Intent(mContext, NavigationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        config.setTitleForAllStatuses(getString(title))
                .setRingToneEnabled(true)
                .setClickIntentForAllStatuses(clickIntent)
                .setClearOnActionForAllStatuses(true);

        config.getProgress().message = getString(R.string.uploading);
        config.getProgress().iconResourceID = R.drawable.ic_upload;
        config.getProgress().iconColorResourceID = Color.BLUE;
        config.getProgress().actions.add(new UploadNotificationAction(
                R.drawable.ic_cancelled,
                getString(R.string.cancel_upload),
                NotificationActions.getCancelUploadAction(mContext, 1, uploadId)));

        config.getCompleted().message = getString(R.string.upload_success);
        config.getCompleted().iconResourceID = R.drawable.ic_upload_success;
        config.getCompleted().iconColorResourceID = Color.GREEN;

        config.getError().message = getString(R.string.upload_error);
        config.getError().iconResourceID = R.drawable.ic_upload_error;
        config.getError().iconColorResourceID = Color.RED;

        config.getCancelled().message = getString(R.string.upload_cancelled);
        config.getCancelled().iconResourceID = R.drawable.ic_cancelled;
        config.getCancelled().iconColorResourceID = Color.YELLOW;

        return config;
    }


    private int needUploadSize = -1;
    private int uploadedCaseSize = 0;
    private boolean uploadMode = false;

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return null;
    }

    public class UploadReceiver extends UploadServiceBroadcastReceiver {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {
            super.onProgress(context, uploadInfo);
            Logger.i("%s %s", uploadInfo.getUploadId(), uploadInfo.getProgressPercent());
            updateCaseStatus(uploadInfo, Constants.CASE_UPLOADING);
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
            super.onError(context, uploadInfo, serverResponse, exception);
            updateCaseStatus(uploadInfo, Constants.CASE_UPLOAD_ERROR);
        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
            super.onCompleted(context, uploadInfo, serverResponse);
            updateCaseStatus(uploadInfo, Constants.CASE_UPLOADED);
        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {
            super.onCancelled(context, uploadInfo);
            updateCaseStatus(uploadInfo, Constants.CASE_UPLOAD_CANCEL);
        }
    }

    //根据案件上传信息刷新数据状态
    public void updateCaseStatus(UploadInfo uploadInfo, String status) {

        String uploadId = uploadInfo.getUploadId();
        for (RespCaseSummary caseSummary : caseSummaries) {

            //找出正在对应的案件进行处理
            if (null != uploadId && uploadId.equals(caseSummary.getUploadId())) {
                int processPercent = uploadInfo.getProgressPercent();

                //状态变才刷新列表
                if (caseSummary.getProgress() < processPercent || !caseSummary.getUploadStatus().equals(status)) {
                    caseSummary.setProgress(uploadInfo.getProgressPercent());
                    caseSummary.setUploadStatus(status);
                    adapter.notifyDataSetChanged();

                    //安装状态是成功或者失败保存状态
                    if (Constants.CASE_UPLOADED.equals(status) || Constants.CASE_UPLOAD_ERROR.equals(status) || Constants.CASE_UPLOAD_CANCEL.equals(status)) {
                        if (Constants.CASE_UPLOADED.equals(status)) {
                            caseSummary.setUploaded(true);
                            SugarRecord.save(caseSummary);

                            //                            //更新案件信息
                            //                            final String addrId = caseSummary.getAddrId();
                            //                            Map<String, String> map = new HashMap<>();
                            //                            map.put("caseId", caseSummary.getCaseId());
                            //                            map.put("addrId", addrId);
                            //                            CommonInteractor.request(new RequestCallbackImpl<RespBase>() {
                            //                                @Override
                            //                                public void response(RespBase respBase) {
                            //                                    Logger.i("%s  ---- 案件状态更新成功", addrId);
                            //                                }
                            //                            }, ApiConstants.APP_ROOT, ApiConstants.METHOD_UPDATE_VISIT_STATUS, map);
                        }
                        Logger.i("%s %s", status, uploadedCaseSize);
                        doneUploadCase();
                    }
                    break;
                }
            }
        }
    }

    //上传完成或失败一个案件进入此方法处理
    public void doneUploadCase() {
        uploadedCaseSize++;
        if (uploadedCaseSize == needUploadSize) {
            uploadMode = false;
            uploadedCaseSize = 0;
            needUploadSize = -1;
            //全部上传成功
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
  showConfirmMessage("现在清除上传成功案件?", new com.huateng.collection.ui.base.BaseFragment.AppMsgConfirmListener() {
                        @Override
                        public void OnConfirm() {
                            clearUploadedCases();
                        }

                        @Override
                        public void OnCancel() {
                        }
                    });

                }
            }, 200);
        }
    }


    private void initUploadStatus() {
        if (null != caseSummaries && caseSummaries.size() > 0) {
            for (RespCaseSummary caseSummary : caseSummaries) {
                caseSummary.setUploadStatus(Constants.CASE_NORMAL);
                caseSummary.setUploadId(null);
                caseSummary.setProgress(0);
                SugarRecord.saveInTx(caseSummaries);
            }
        }
    }

    //清除已上传的案件
    public void clearUploadedCases() {

        if (caseSummaries.size() > 0) {
            //初始化进度条

            List<RespCaseSummary> uploadedSummary = new ArrayList<>();

            for (int i = 0; i < caseSummaries.size(); i++) {
                RespCaseSummary caseSummary = caseSummaries.get(i);
                boolean uploaded = caseSummary.isUploaded();

                if (uploaded) {
                    CaseManager.removeCachedCaseSummary(caseSummary.getBizId());
                    CaseManager.removeBizDatas(caseSummary.getBizId());
                    uploadedSummary.add(caseSummary);
                }
            }
            caseSummaries.removeAll(uploadedSummary);
            adapter.notifyDataSetChanged();

            if (caseSummaries.size() == 0) {
                //刷新发件箱
                EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_DONE_CASES));

            }

            adapter.initCheckStates();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != uploadReceiver && null != mContext) {
            mContext.unregisterReceiver(uploadReceiver);
        }
    }


    @Override
    public boolean isUseEventBus() {
        return true;
    }
}
*/
