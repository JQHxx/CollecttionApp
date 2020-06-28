package com.huateng.collection.ui.fragment.casebox.casefill;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.aes_util.AESUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.RemoteAudioBean;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.ui.activity.AudioPlayActivity;
import com.huateng.collection.ui.adapter.RecorderAdapter;
import com.huateng.collection.ui.adapter.RemoteAudioAdapter;
import com.huateng.collection.utils.Utils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.widget.Watermark;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.DownLoadObserver;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.huateng.network.UploadObserver;
import com.huateng.network.bean.ResponseStructure;
import com.huateng.network.upload.UploadParam;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.tools.DoubleUtils;
import com.orm.SugarRecord;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tools.utils.FileUtils;
import com.tools.utils.GsonUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zr.lib_audio.androidaudiorecorder.AndroidAudioRecorder;
import com.zr.lib_audio.androidaudiorecorder.AudioChannel;
import com.zr.lib_audio.androidaudiorecorder.AudioSampleRate;
import com.zr.lib_audio.androidaudiorecorder.AudioSource;
import com.zr.lib_audio.androidaudiorecorder.RecordService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.zr.lib_audio.androidaudiorecorder.AndroidAudioRecorder.EXTRA_FILE_PATH;
import static com.zr.lib_audio.androidaudiorecorder.AndroidAudioRecorder.EXTRA_RECORD_TASK_ID;
import static com.zr.lib_audio.androidaudiorecorder.AndroidAudioRecorder.EXTRA_RECORD_TASK_NAME;

/**
 * author：shanyong
 * data：2018/12/27
 * 录音列表展示
 */
public class RecordSelectorActivity extends BaseActivity implements View.OnClickListener {
    private int pageNo = 1;
    private int pageSize = 10;
    private static final int REQUEST_RECORD_AUDIO = 0;
    private String audioUrl;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.recycler_remote)
    RecyclerView mRecyclerRemote;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private boolean isUpload;//是否在上传文件

    private int imageSize = 0;
    private List<LocalMedia> fileList = new ArrayList<>();
    private List<RemoteAudioBean.RecordsBean> removeAudioData = new ArrayList();
    private RecorderAdapter adapter;
    private RemoteAudioAdapter mRemoteAudioAdapter;
    private int maxSelectNum = 20;
    private int themeId;
    private String filePath;
    //private String tempFilePath;
    private String caseId;
    private String custName;

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        custName = getIntent().getStringExtra(Constants.CUST_NAME);
        themeId = R.style.picture_white_style;
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpload) {
                    hideLoading();
                    RxToast.showToast("录音正在上传中，请稍后再试");

                    return;
                }
                upAudioData();
            }
        });
        filePath = AttachmentProcesser.getInstance(RecordSelectorActivity.this).getVoicePath(caseId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        adapter = new RecorderAdapter(this, onAddPicClickListener);
        adapter.setList(fileList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecorderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = fileList.get(position);
                if (!DoubleUtils.isFastDoubleClick()) {

                    if (FileUtils.isFileExists(media.getPath())) {
                        Intent intent = new Intent(RecordSelectorActivity.this, AudioPlayActivity.class);
                        intent.putExtra("filePath", media.getPath());
                        startActivity(intent);
                    } else {
                        RxToast.showToast("录音文件播放失败");
                    }

                }
            }
        });


        mRemoteAudioAdapter = new RemoteAudioAdapter();
        mRecyclerRemote.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerRemote.setAdapter(mRemoteAudioAdapter);
        mRemoteAudioAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RemoteAudioBean.RecordsBean recordsBean = removeAudioData.get(position);
                if (!DoubleUtils.isFastDoubleClick()) {
                    String path = filePath + File.separator + recordsBean.getFileName();

                    if (FileUtils.isFileExists(path)) {
                        Intent intent = new Intent(RecordSelectorActivity.this, AudioPlayActivity.class);
                        intent.putExtra("filePath", path);
                        startActivity(intent);
                    } else {
                        downLoad(recordsBean);
                    }
                }
            }
        });

        mRemoteAudioAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreData(false);
            }
        }, mRecyclerRemote);
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<LocalMedia>>() {

            @Override
            public List<LocalMedia> doInBackground() {
                Log.e("nb", "doInBackground doInBackground doInBackground");
                return new LocalMediaLoader(RecordSelectorActivity.this, PictureSelectionConfig.getInstance()).getAudios(filePath);
            }

            @Override
            public void onSuccess(List<LocalMedia> localMedias) {
                Log.e("nb", "onSuccess onSuccess onSuccess:" + localMedias.size());
                initStandardModel(localMedias);
            }
        });


    }


    private void initStandardModel(List<LocalMedia> localMedias) {

        fileList.clear();
        List<FileData> fileDatas = CaseManager.obtainRecordDatas(caseId);
       // Log.e("nb", "fileDatas size" + fileDatas.size());
        for (LocalMedia localMedia : localMedias) {
            //过滤掉已上传的文件和下载的文件
            boolean b = false;
            for (FileData fileData : fileDatas) {
                b = localMedia.getPath().equals(fileData.getRealPath()) && (fileData.getFileType() == 2);

                if (b) {
                    
                    break;
                }
            }
            if (b) {
                fileList.add(localMedia);
            }
            adapter.notifyDataSetChanged();

        }
        //对文件进行同步
        Utils.mediaFileSync(RecordSelectorActivity.this, filePath, fileDatas, fileList, FileData.TYPE_AUDIO, caseId, caseId);

    }

    private RecorderAdapter.onAddPicClickListener onAddPicClickListener = new RecorderAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //查看是否有录音和存储权限
            //获取权限
            RxPermissions rxPermissions = new RxPermissions(RecordSelectorActivity.this);
            rxPermissions.request(RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (!granted) {
                                RxToast.showToast("录音权限被拒绝，请授权后操作");
                            } else {
                                record();
                            }
                        }
                    });

        }

    };

    private void record() {
        //存在录音任务时判断  如果任务id相同则进入，不同的话提示有任务在进行请结束当前录音任务后再进行新的录音任务

        if (RecordService.isRecording()) {
            Intent taskIntent = RecordService.getTaskIntent();
            Log.e("nb", "id->" + taskIntent.getStringExtra(EXTRA_RECORD_TASK_ID));
            if (!taskIntent.getStringExtra(EXTRA_RECORD_TASK_ID).equals(caseId)) {
                RxToast.showToast(String.format("当前 %s 正在进行录音任务，请在结束该录音任务后再进行录音", taskIntent.getStringExtra(EXTRA_RECORD_TASK_NAME)));
                return;
            } else {
                audioUrl = taskIntent.getStringExtra(EXTRA_FILE_PATH);
            }
        } else {
            audioUrl = String.format("%s%s%s.wav", filePath, Perference.getUserId(), System.currentTimeMillis());

        }
        // 进入相册 以下是例子：不需要的api可以不写
        // Log.e("nb", "tempFilePath:" + tempFilePath);

        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(audioUrl)
                .setColor(ContextCompat.getColor(this, R.color.accent_color))
                .setRequestCode(REQUEST_RECORD_AUDIO)
                // Optional
                .setTaskId(caseId)
                .setTaskName(custName + "外访录音")
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.MONO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)
                // Start recording
                .record();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("nb", requestCode + ":" + resultCode);

        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                int duration = data.getIntExtra("duration", 0);
                Log.e("nb", "time:" + duration);
                File newFile = new File(audioUrl);
                if (newFile.exists()) {
                    File file = new File(audioUrl);
                    if (!file.exists()) {
                        return;
                    }


                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(newFile.getPath());
                    localMedia.setFileName(newFile.getName());
                    localMedia.setDuration(duration * 1000);
                    fileList.add(0, localMedia);

                    FileData fileData = new FileData();
                    fileData.setBizId(caseId);
                    fileData.setCaseId(caseId);
                    fileData.setExist(true);
                    fileData.setType(FileData.TYPE_AUDIO);
                    fileData.setFileType(2);
                    fileData.setRealPath(newFile.getPath());
                    fileData.setFileName(newFile.getName());
                    SugarRecord.save(fileData);

                    Utils.scanMediaFile(RecordSelectorActivity.this, newFile);
                }
                adapter.setList(fileList);
                adapter.notifyDataSetChanged();
                // Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                //   Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_back:
                finish();
                break;
        }
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
        return R.layout.fragment_selector;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        loadMoreData(true);
    }


    public void loadMoreData(boolean isFresh) {

        if (isFresh) {
            showLoading();
            pageNo = 1;
            removeAudioData.clear();
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        map.put("tlrNo", Perference.getUserId());
        map.put("relaBusiCode", caseId);
        map.put("fileType", "1");
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_BASE_FILE, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<RemoteAudioBean>() {
                    @Override
                    public void onError(String code, String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNextData(RemoteAudioBean remoteAudioBean) {
                        if (isFinishing()) {
                            return;
                        }
                        if (remoteAudioBean == null || remoteAudioBean.getRecords().size() == 0) {
                            // mRemoteAudioAdapter.setNewData(remoteFileList);
                            mRemoteAudioAdapter.loadMoreEnd();
                            hideLoading();
                            return;
                        }
                        Log.e("nb","sixe--->"+remoteAudioBean.getRecords().size());
                        if (isFresh) {
                            removeAudioData.clear();
                        }

                        if (isFresh) {
                            //下拉刷新
                            if (remoteAudioBean.getRecords().size() < 10) {
                                mRemoteAudioAdapter.setEnableLoadMore(false);
                            } else {
                                mRemoteAudioAdapter.setEnableLoadMore(true);
                            }
                            hideLoading();
                        } else {
                            //加载更多
                            if (remoteAudioBean.getRecords().size() >= 10) {

                                mRemoteAudioAdapter.loadMoreComplete();
                            } else {
                                mRemoteAudioAdapter.loadMoreEnd();
                            }

                        }
                        removeAudioData.addAll(remoteAudioBean.getRecords());

                        Collections.sort(removeAudioData);


                        mRemoteAudioAdapter.setNewData(removeAudioData);

                        pageNo++;
                    }

                });


    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {

    }

    private void upAudioData() {
        String fileDuration = "";
        String fileDate = "";
        isUpload = true;
        showLoading();
        List<FileData> fileDatas = CaseManager.obtainRecordDatas(caseId);
        ArrayList<UploadParam> uploadParams = new ArrayList<>();

        Map<String, String> map = new HashMap<>();

        uploadParams.add(new UploadParam("callback", "mobileAppFileOperServiceImpl/uploadAppFile"));
        //过滤掉已上传的文件
        //数据库查找保存的数据

        for (LocalMedia localMedia : fileList) {
            boolean b = false;
            for (FileData fileData : fileDatas) {
                //当待上传的图片路径 和数据库中已上传的文件路径相同 说明不需要再次上传
                b = localMedia.getPath().equals(fileData.getRealPath()) && fileData.isUpload();
                //当找到已上传文件 退出本次循环
                if (b) {
                    break;
                }

            }

            if (!b) {
                uploadParams.add(new UploadParam("file", new File(localMedia.getPath()), localMedia.getFileName()));
                Log.e("nb", "新增一条录音:");
                long time = new File(localMedia.getPath()).lastModified();
                if (imageSize == 0) {
                    fileDuration = localMedia.getFileName() + "/" + localMedia.getDuration() / 1000 + "/" + time;
                    fileDate = localMedia.getFileName() + "/" + time;

                } else {
                    fileDuration = fileDuration + "," + localMedia.getFileName() + "/" + localMedia.getDuration() / 1000 + "/" + time;
                    fileDate = fileDate + "," + localMedia.getFileName() + "/" + time;

                }
                imageSize++;
            }

        }

        if (imageSize == 0) {
            RxToast.showToast("本地无待上传录音文件");
            isUpload = false;
            hideLoading();
            return;
        }
        imageSize = 0;
        Log.e("nb", "fileDate:" + fileDate);
        map.put("caseId", caseId);
        map.put("tlrNo", Perference.getUserId());
        map.put("fileType", "1");
        map.put("fileDate", fileDate);
        map.put("fileDuration", fileDuration);
        String appData = GsonUtils.toJson(map);
        uploadParams.add(new UploadParam("appData", AESUtils.encrypt(appData, "aes-nbcbccms@123")));


        RetrofitManager.getInstance()
                .uploadFile(NetworkConfig.C.getBaseURL() + "file/upload.htm", uploadParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UploadObserver<ResponseStructure>() {
                    @Override
                    public void _onProgress(Integer percent) {
                        super._onProgress(percent);
                        // Log.e("nb", "percent:" + percent);
                    }

                    @Override
                    public void _onNext(ResponseStructure o) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                        if (o == null) {
                            return;
                        }
                        if (o.getScubeHeader() != null && "EXP".equals(o.getScubeHeader().getErrorCode())) {

                            RxToast.showToast(o.getScubeHeader().getErrorMsg());
                        } else if (o.getScubeHeader() != null && "SUC".equals(o.getScubeHeader().getErrorCode())) {

                            Iterator<LocalMedia> iterator = fileList.iterator();
                            while (iterator.hasNext()) {
                                LocalMedia localMedia = iterator.next();
                                File file = new File(localMedia.getPath());
                                if (file.isFile()) {
                                    /*RemoteAudioBean.RecordsBean recordsBean = new RemoteAudioBean.RecordsBean();
                                    recordsBean.setCrtUser(Perference.getUserId());
                                    recordsBean.setFileName(localMedia.getFileName());
                                    recordsBean.setFilePath(localMedia.getPath());
                                    recordsBean.setFileSize(localMedia.getDuration()/1000 + "");
                                    recordsBean.setFileType(FileData.TYPE_AUDIO);
                                    recordsBean.setFileTime( file.lastModified());
                                    removeAudioData.add(recordsBean);*/
                                    iterator.remove();
                                    Log.e("nb", "设置为已上传");
                                }
                            }

                            for (FileData fileData : fileDatas) {
                                fileData.setUpload(true);
                                fileData.setFileType(1);
                                SugarRecord.save(fileData);
                            }


                            RxToast.showToast("录音文件上传成功了");
                        }

                        //刷新远程图片

                        if (mRemoteAudioAdapter.getData().size() == 0) {
                            mRemoteAudioAdapter.setNewData(removeAudioData);
                        } else {
                            mRemoteAudioAdapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();


                        Log.e("nb", "录音文件上传成功了");

                        isUpload = false;
                        hideLoading();
                        loadMoreData(true);
                    }

                    @Override
                    public void _onError(Throwable e) {
                        hideLoading();
                        Log.e("nb", e.getLocalizedMessage() + "失败");
                        RxToast.showToast("录音文件上传失败");
                        isUpload = false;
                    }
                });
    }

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    /**
     * xia
     *
     * @param recordsBean
     */
    private void downLoad(RemoteAudioBean.RecordsBean recordsBean) {
        showLoading();
        Log.e("nb", recordsBean.getFileName() + ":" + recordsBean.getFilePath());
        RetrofitManager.getInstance()
                .download(recordsBean.getFileName(), recordsBean.getFilePath(), "mobileAppFileOperServiceImpl/appDownload", filePath)
                .compose(RxSchedulers.io_main())
                .subscribe(new DownLoadObserver() {
                    @Override
                    public void _onNext(String result) {
                        //去播放音频
                        Log.e("nb", "_onNext _onNext _onNext");
                        if (FileUtils.isFileExists(result)) {
                            Intent intent = new Intent(RecordSelectorActivity.this, AudioPlayActivity.class);
                            intent.putExtra("filePath", result);
                            startActivity(intent);
                        } else {
                            RxToast.showToast("录音文件播放失败");
                        }

                        // PicturePlayAudioFragment fragment = PicturePlayAudioFragment.newInstance(result);
                        // fragment.show(getSupportFragmentManager(), "PicturePlayAudioActivity");

                        File file = new File(result);
                        if (file.exists()) {
                            FileData fileData = new FileData();
                            fileData.setBizId(caseId);
                            fileData.setCaseId(caseId);
                            fileData.setExist(true);
                            fileData.setType(FileData.TYPE_AUDIO);
                            fileData.setFileType(1);
                            fileData.setRealPath(file.getPath());
                            fileData.setFileName(file.getName());
                            SugarRecord.save(fileData);
                        }
                        hideLoading();

                    }

                    @Override
                    public void _onError(Throwable e) {
                        Log.e("nb", "_onError _onError _onError");
                        hideLoading();

                    }
                });
    }
}
