package com.huateng.collection.ui.fragment.casebox.casefill;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.aes_util.AESUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.collection.LocalMediaLoader;
import com.huateng.collection.R;
import com.huateng.collection.app.Config;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.UploadImageDataBean;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.ui.adapter.GridImageAdapter;
import com.huateng.collection.ui.adapter.RemoteImageAdapter;
import com.huateng.collection.utils.GlideEngine;
import com.huateng.collection.utils.Utils;
import com.huateng.collection.utils.WatermarkSettings;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.collection.utils.map.LocationHelper;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.pictureselector.FullyGridLayoutManager;
import com.huateng.network.ApiConstants;
import com.huateng.network.BaseObserver2;
import com.huateng.network.DownLoadObserver;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.RxSchedulers;
import com.huateng.network.UploadObserver;
import com.huateng.network.bean.ResponseStructure;
import com.huateng.network.upload.UploadParam;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tools.utils.FileUtils;
import com.tools.utils.GsonUtils;
import com.tools.utils.ImageUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
/**
 * author：shanyong
 * data：2018/12/27
 */
public class PhotoSelectorActivity2 extends BaseActivity {
    private int pageNo = 1;
    private int pageSize = 10;
    private final static String TAG = PhotoSelectorActivity2.class.getSimpleName();
    @BindView(R.id.rx_title)
    RxTitle rxTitle;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_remote)
    RecyclerView mRecyclerRemote;
    private List<LocalMedia> fileList = new ArrayList<>();
    private List<LocalMedia> remoteFileList = new ArrayList<>();
    private GridImageAdapter adapter;
    private RemoteImageAdapter mRemoteImageAdapter;
    private int maxSelectNum = 9;
    private int imageSize = 0;

    // private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private int themeId;
    private String localfilePath;
    private String tempFilePath;
    private String custId;
    private String caseId;
    private String custName;
    private boolean isUpload;//是否在上传文件
    private LocationHelper mLocationHelper;

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextSize(12.0f)
                .setText(Perference.getUserId() + "-" + Perference.get(Perference.NICK_NAME))
                .show(this);
        themeId = R.style.picture_white_style;
        immersiveStatusBar(rxTitle);
        caseId = getIntent().getStringExtra(Constants.CASE_ID);
        custId = getIntent().getStringExtra(Constants.CUST_ID);
        custName = getIntent().getStringExtra(Constants.CUST_NAME);
        localfilePath = AttachmentProcesser.getInstance(PhotoSelectorActivity2.this).getPhotoPath(caseId);
        tempFilePath = AttachmentProcesser.getInstance(PhotoSelectorActivity2.this).getTempsDir();
        initRecyclerview();
        initListener();
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<LocalMedia>>() {

            @Override
            public List<LocalMedia> doInBackground() {
                return new LocalMediaLoader(PhotoSelectorActivity2.this, PictureSelectionConfig.getInstance()).getImages(localfilePath);
            }

            @Override
            public void onSuccess(List<LocalMedia> localMedias) {
                initStandardModel(localMedias);
            }
        });
    }

    private void initStandardModel(List<LocalMedia> images) {


        if (images.size() > 0) {

            List<FileData> fileDatas = CaseManager.obtainPhotoDatas(caseId);
            // List<FileData> fileDatas = SugarRecord.find(
            //     FileData.class, " USER_ID=? and TYPE=? and EXIST=1", Perference.getUserId(), FileData.TYPE_PHOTO);
            fileList.clear();
            remoteFileList.clear();

            for (LocalMedia localMedia : images) {
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
            }
            adapter.notifyDataSetChanged();
            //对文件进行同步
            Utils.mediaFileSync(PhotoSelectorActivity2.this, localfilePath, fileDatas, fileList, FileData.TYPE_PHOTO, caseId, caseId);

        } else {
            Logger.i("未加载到相关文件");
        }

    }

    /**
     * 初始化recycler view
     */
    private void initRecyclerview() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PhotoSelectorActivity2.this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, false));

        adapter = new GridImageAdapter(PhotoSelectorActivity2.this, onAddPicClickListener);
        adapter.setList(fileList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //  LocalMedia media = fileList.get(position);
                // 预览图片
                PictureSelector.create(PhotoSelectorActivity2.this)
                        .themeStyle(R.style.picture_default_style)
                        .isNotPreviewDownload(true)
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .openExternalPreview(position, fileList);
            }
        });

        //远程图片加载
        mRemoteImageAdapter = new RemoteImageAdapter();
        mRecyclerRemote.setLayoutManager(new GridLayoutManager(this, 2));
        // mRecyclerRemote.setLayoutManager(new LinearLayoutManager(PhotoSelectorActivity.this));
        mRecyclerRemote.addItemDecoration(new GridSpacingItemDecoration(2, 40, false));

        mRecyclerRemote.setAdapter(mRemoteImageAdapter);


        mRemoteImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //   PictureSelector.create(PhotoSelectorActivity.this).externalPicturePreview(position, remoteFileList, 0);

                PictureSelector.create(PhotoSelectorActivity2.this)
                        .themeStyle(R.style.picture_default_style)
                        .isNotPreviewDownload(true)
                        .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                        .openExternalPreview(position, remoteFileList);


            }
        });
        mLocationHelper = new LocationHelper(this);
        mLocationHelper.start(new Runnable() {
            @Override
            public void run() {
                TencentLocation location = mLocationHelper.getLastLocation();

                //保存定位 地址
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (latLng != null) {
                    Config.setLastLocation(latLng);
                }
                if (!TextUtils.isEmpty(location.getAddress())) {
                    Config.setLastAddress(location.getAddress());
                }
                //  Log.e("nb",location.getAddress()+":"+latLng.getLatitude()+":"+latLng.getLongitude());
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

        rxTitle.getLlRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpload) {
                    hideLoading();
                    RxToast.showToast("图片正在上传中，请稍后再试");

                    return;
                }
                sendData();
              /*  Intent intent = new Intent();
                intent.putExtra("caseId",caseId);
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) fileList);
                UploadService.enqueueWork(getApplicationContext(),intent);*/
                // 启动后台service上传图片
            }
        });

        mRemoteImageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //加载更多
                loadMoreImage(false);
            }
        }, mRecyclerRemote);


    }

    /**
     * 保存拍照文件到后台
     */
    private void sendData() {
        String fileDate = "";
        isUpload = true;
        showLoading();
        ArrayList<UploadParam> uploadParams = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        //过滤掉已上传的文件
        //数据库查找保存的数据
        List<FileData> fileDatas = CaseManager.obtainPhotoDatas(caseId);
        for (LocalMedia localMedia : fileList) {
            //过滤掉已上传的文件
            boolean b = false;
            for (FileData fileData : fileDatas) {
                //当待上传的图片路径 和数据库中已上传的文件路径相同 说明不需要再次上传
                b = localMedia.getPath().equals(fileData.getRealPath()) && fileData.isUpload();
                if (b) {
                    break;
                }
            }

            if (!b) {
                uploadParams.add(new UploadParam("file", new File(localMedia.getPath()), localMedia.getFileName()));
                //  Log.e("nb", "新增一张图片:");
                long time = new File(localMedia.getPath()).lastModified();
                if (imageSize == 0) {
                    fileDate = localMedia.getFileName() + "/" + time;

                } else {
                    fileDate = fileDate + "," + localMedia.getFileName() + "/" + time;

                }
                imageSize++;
            }
        }


        if (imageSize == 0) {
            RxToast.showToast("本地无待上传图片");
            isUpload = false;
            hideLoading();
            return;
        }
        imageSize = 0;
        map.put("caseId", caseId);
        map.put("fileType", "0");
        map.put("fileDate", fileDate);
        map.put("tlrNo", Perference.getUserId());
        String appData = GsonUtils.toJson(map);
        uploadParams.add(new UploadParam("appData", AESUtils.encrypt(appData, "aes-nbcbccms@123")));
        uploadParams.add(new UploadParam("callback", "mobileAppFileOperServiceImpl/uploadAppFile"));

        RetrofitManager.getInstance()
                .uploadFile(NetworkConfig.C.getBaseURL() + "file/upload.htm", uploadParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UploadObserver<ResponseStructure>() {
                    @Override
                    public void _onProgress(Integer percent) {
                        super._onProgress(percent);
                        // Log.e("nb", "进度：" + percent);
                    }

                    @Override
                    public void _onNext(ResponseStructure o) {
                        if (isFinishing()) {
                            return;
                        }

                        if (o == null) {
                            hideLoading();
                            return;
                        }

                        if (o.getScubeHeader() != null && "EXP".equals(o.getScubeHeader().getErrorCode())) {

                            RxToast.showToast(o.getScubeHeader().getErrorMsg());
                        } else if (o.getScubeHeader() != null && "SUC".equals(o.getScubeHeader().getErrorCode())) {
                            // Log.e("nb", "fileDatas.size():" + fileDatas.size());

                            Iterator<LocalMedia> iterator = fileList.iterator();
                            while (iterator.hasNext()) {
                                LocalMedia localMedia = iterator.next();
                                File file = new File(localMedia.getPath());
                                if (file.isFile()) {
                                    // remoteFileList.add(localMedia);
                                    iterator.remove();
                                }
                            }

                            for (FileData fileData : fileDatas) {
                                fileData.setUpload(true);
                                fileData.setFileType(1);
                                SugarRecord.save(fileData);
                            }


                            RxToast.showToast("照片上传成功了");
                        }

                        //刷新远程图片

                        isUpload = false;
                        adapter.notifyDataSetChanged();
                        loadMoreImage(true);
                        hideLoading();
                    }

                    @Override
                    public void _onError(Throwable e) {
                        hideLoading();
                        RxToast.showToast("照片上传失败");
                        isUpload = false;
                    }
                });

    }


    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            PictureSelector.create(PhotoSelectorActivity2.this)
                    .openCamera(PictureMimeType.ofImage())
                    .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                    .forResult(PictureConfig.REQUEST_CAMERA);


        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.e("nb", requestCode + ":" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    // 将选择的照片文件复制到案件文件下
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    List<LocalMedia> newList = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        boolean contains = false;
                        LocalMedia selectMedia = selectList.get(i);
                        for (int j = 0; j < fileList.size(); j++) {
                            LocalMedia localMedia = fileList.get(j);
                            //路径一样且名字一样
                            if (localMedia.getPath().equals(selectMedia.getPath()) && localMedia.getFileName().equals(selectMedia.getFileName())) {
                                contains = true;
                            }
                        }
                        if (!contains) {
                            File file = new File(selectMedia.getPath());
                            File tempFile = new File(tempFilePath, file.getName());
                            File newFile = new File(localfilePath, file.getName());
                            //复制选择图片文件
                            boolean isCopy = false;
                            boolean isTempExist = tempFile.exists();
                            //  Log.e("nb", "isTempExist:" + isTempExist);
                            if (!isTempExist) {
                                isCopy = FileUtils.copyFile(file, tempFile);
                                Logger.i("%s copy:%s", selectMedia.getPath(), String.valueOf(isCopy));
                            }
                            // Log.e("nb", "isCopy:" + isCopy);
                            if (isCopy || isTempExist) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                String time = formatter.format(new Date());
                                double latitude = 0;
                                double longitude = 0;
                                String lastAddress = Config.getLastAddress();
                                LatLng lastLocation = Config.getLastLocation();
                                if (lastLocation != null) {
                                    latitude = lastLocation.getLatitude();
                                    longitude = lastLocation.getLongitude();
                                }

                                WatermarkSettings.getmInstance(this);

                                Bitmap bitmap2 = WatermarkSettings.createWatermark(tempFile.getPath(), Perference.getUserId(), custName, lastAddress, String.format("( %s,%s)", latitude, longitude), time);
                                boolean saved = ImageUtils.save(bitmap2, newFile.getPath(), Bitmap.CompressFormat.JPEG);
                                //  Log.e("nb", "save:" + saved);
                                if (saved) {
                                    //                                     删除临时文件
                                    Utils.deleteImage(PhotoSelectorActivity2.this, tempFile.getPath());
                                    //刷新媒体库文件
                                    Utils.scanMediaFile(PhotoSelectorActivity2.this, newFile);
                                }
                                LocalMedia localMedia = new LocalMedia();
                                localMedia.setPath(newFile.getPath());
                                localMedia.setFileName(newFile.getName());
                                newList.add(localMedia);
                                FileData fileData = new FileData();
                                fileData.setBizId(caseId);
                                fileData.setCaseId(caseId);
                                fileData.setExist(true);
                                fileData.setType(FileData.TYPE_PHOTO);
                                fileData.setFileType(2);
                                fileData.setRealPath(newFile.getPath());
                                fileData.setFileName(newFile.getName());
                                SugarRecord.save(fileData);
                            }
                        }
                    }
                    if (newList.size() > 0) {
                        fileList.addAll(newList);
                    }

                    // adapter.setList(fileList);
                    adapter.notifyDataSetChanged();
                    break;
            }
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
        return R.layout.fragment_photo_selector;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        loadMoreImage(true);
    }


    private void loadMoreImage(boolean isFresh) {
        if (isFresh) {
            showLoading();
            pageNo = 1;
            remoteFileList.clear();
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("tlrNo", Perference.getUserId());
        map.put("relaBusiCode", caseId);
        map.put("fileType", "0");
        RetrofitManager.getInstance()
                .request(ApiConstants.MOBILE_APP_INTERFACE, ApiConstants.SELECT_BASE_FILE, map)
                .compose(getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver2<UploadImageDataBean>() {
                    @Override
                    public void onError(String code, String msg) {
                        if (isFinishing()) {
                            return;
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNextData(UploadImageDataBean uploadImageDataBean) {
                        if (isFinishing()) {
                            return;
                        }
                        if (uploadImageDataBean == null || uploadImageDataBean.getRecords().size() == 0) {
                            mRemoteImageAdapter.setNewData(remoteFileList);
                            mRemoteImageAdapter.loadMoreEnd();
                            hideLoading();
                            return;
                        }
                        if (TextUtils.isEmpty(localfilePath)) {
                            localfilePath = AttachmentProcesser.getInstance(PhotoSelectorActivity2.this).getPhotoPath(caseId);
                        }
                        for (int i = 0; i < uploadImageDataBean.getRecords().size(); i++) {
                            UploadImageDataBean.RecordsBean recordsBean = uploadImageDataBean.getRecords().get(i);

                            String path = localfilePath + File.separator + recordsBean.getFileName();
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(path);
                            remoteFileList.add(localMedia);
                            if (!FileUtils.isFileExists(path)) {
                                downLoad(recordsBean.getFileName(), recordsBean.getFilePath(),i);
                            }
                        }
                        mRemoteImageAdapter.setNewData(remoteFileList);

                        saveDataToDb();
                        hideLoading();
                        if (isFresh) {
                            //下拉刷新
                            if (uploadImageDataBean.getRecords().size() < 10) {
                                mRemoteImageAdapter.setEnableLoadMore(false);
                            } else {
                                mRemoteImageAdapter.setEnableLoadMore(true);
                            }
                        } else {
                            //加载更多
                            if (uploadImageDataBean.getRecords().size() >= 10) {

                                mRemoteImageAdapter.loadMoreComplete();
                            } else {
                                mRemoteImageAdapter.loadMoreEnd();
                            }

                        }

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

    @Override
    public <T> LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationHelper != null) {
            mLocationHelper.stop();
        }
    }

    private void downLoad(String name, String filePath, int position) {
        //Log.e("nb", name + ":" + filePath);
        RetrofitManager.getInstance()
                .download(name, filePath, "mobileAppFileOperServiceImpl/appDownload", localfilePath)
                .compose(RxSchedulers.io_main())
                .subscribe(new DownLoadObserver() {
                    @Override
                    public void _onNext(String result) {
                        Utils.scanMediaFile(PhotoSelectorActivity2.this, new File(result));
                        mRemoteImageAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void _onError(Throwable e) {
                    }
                });

    }


    private void saveDataToDb() {

        for (LocalMedia localMedia1 : remoteFileList) {
            FileData fileData = new FileData();
            File file = new File(localMedia1.getPath());
            fileData.setFileName(file.getName());
            fileData.setRealPath(file.getAbsolutePath());
            fileData.setFileId(file.getName().substring(0, file.getName().indexOf(".")));
            fileData.setCaseId(caseId);
            fileData.setUpload(true);
            fileData.setBizId(caseId);
            fileData.setFileType(1);
            fileData.setType(FileData.TYPE_PHOTO);
            fileData.setUserId(Perference.getUserId());
            SugarRecord.save(fileData);
        }


    }


/*    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean bean) {
        // Log.e("nb", bean.code + "a:c" + bean.getObject());
        if (bean == null) {
            return;
        }

        if(BusEvent.UPLOAD_STATUS ==bean.getCode() ) {

        }

    }*/
}
