package com.huateng.phone.collection.ui.fragment.casebox.casefill;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.aes_util.AESUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.huateng.phone.collection.LocalMediaLoader;
import com.huateng.phone.collection.R;
import com.huateng.phone.collection.app.Config;
import com.huateng.phone.collection.app.Constants;
import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.bean.ImageRecordsBean;
import com.huateng.phone.collection.bean.ImageSectionBean;
import com.huateng.phone.collection.bean.UploadImageDataBean;
import com.huateng.phone.collection.bean.orm.FileData;
import com.huateng.phone.collection.ui.adapter.GridImageAdapter;
import com.huateng.phone.collection.ui.adapter.RemoteImageAdapter2;
import com.huateng.phone.collection.utils.DateUtil;
import com.huateng.phone.collection.utils.GlideEngine;
import com.huateng.phone.collection.utils.Utils;
import com.huateng.phone.collection.utils.WatermarkSettings;
import com.huateng.phone.collection.utils.cases.AttachmentProcesser;
import com.huateng.phone.collection.utils.cases.CaseManager;
import com.huateng.phone.collection.utils.map.LocationHelper;
import com.huateng.phone.collection.widget.Watermark;
import com.huateng.phone.collection.widget.pictureselector.FullyGridLayoutManager;
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
import com.luck.picture.lib.tools.SdkVersionUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
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
    //@BindView(R.id.recycler)
    // RecyclerView recyclerView;
    @BindView(R.id.recycler_remote)
    RecyclerView mRecyclerRemote;
    private List<ImageSectionBean> dataList = new ArrayList<>();
    private List<LocalMedia> localData = new ArrayList<>();


    // private GridImageAdapter adapter;
    private RemoteImageAdapter2 mRemoteImageAdapter;
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
        dataList.clear();
        dataList.add(new ImageSectionBean(true, "未上传图片", false));
        List<FileData> fileDatas = CaseManager.obtainPhotoDatas(caseId);
        if (images.size() > 0) {
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
                    dataList.add(new ImageSectionBean(localMedia));
                    localData.add(localMedia);
                    localData.add(localMedia);

                }
            }
            //对文件进行同步
            Utils.mediaFileSync(PhotoSelectorActivity2.this, localfilePath, fileDatas, localData, FileData.TYPE_PHOTO, caseId, caseId);

        }
        dataList.add(new ImageSectionBean(false, "", true));
        dataList.add(new ImageSectionBean(true, "已上传图片", false));
        loadInitImage();

    }

    /**
     * 初始化recycler view
     */
    private void initRecyclerview() {
        //远程图片加载
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PhotoSelectorActivity2.this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerRemote.setLayoutManager(manager);
        mRecyclerRemote.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));
        mRemoteImageAdapter = new RemoteImageAdapter2();
        mRecyclerRemote.setAdapter(mRemoteImageAdapter);

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
            }
        });

        mRemoteImageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //加载更多
                loadMoreImage(false);
            }
        }, mRecyclerRemote);


        mRemoteImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageSectionBean imageSectionBean = (ImageSectionBean) adapter.getData().get(position);
                if (imageSectionBean.isAdd()) {
                    //添加图片
                    //获取权限
                    PictureSelector.create(PhotoSelectorActivity2.this)
                            .openCamera(PictureMimeType.ofImage())
                            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                            .forResult(PictureConfig.REQUEST_CAMERA);

                } else if (!imageSectionBean.isHeader && !imageSectionBean.isAdd()) {
                    //查看图片
                    // 预览图片
                    PictureSelector.create(PhotoSelectorActivity2.this)
                            .themeStyle(R.style.picture_default_style)
                            .isNotPreviewDownload(true)
                            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                            .openExternalPreview(position, localData);
                }
            }
        });


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
        for (LocalMedia localMedia : localData) {
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
                            int addNum = 1;
                            for (int i = 0; i < dataList.size(); i++) {
                                if (dataList.get(i).isAdd()) {
                                    addNum = i;
                                    break;
                                }
                            }

                            for (int i = addNum - 1; i >= 1; i--) {
                                dataList.add(addNum + 2, dataList.get(i));

                            }

                            for (; 1 < addNum; ) {

                                dataList.remove(1);
                                addNum--;
                            }


                            for (FileData fileData : fileDatas) {
                                fileData.setUpload(true);
                                fileData.setFileType(1);
                                SugarRecord.save(fileData);
                            }
                          /*  Iterator<LocalMedia> iterator = fileList.iterator();
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
                            }*/


                            RxToast.showToast("照片上传成功了");
                        }

                        //刷新远程图片
                        isUpload = false;
                        mRemoteImageAdapter.notifyDataSetChanged();
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

            //获取权限
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
                    // List<LocalMedia> newList = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        boolean contains = false;
                        LocalMedia selectMedia = selectList.get(i);
                        String path = "";
                        if (SdkVersionUtils.checkedAndroid_Q()) {
                            path = selectMedia.getAndroidQToPath();
                        } else {
                            path = selectMedia.getPath();
                        }
                        for (int j = 0; j < dataList.size(); j++) {
                            if (dataList.get(j).t != null) {
                                LocalMedia localMedia = dataList.get(j).t;
                                //路径一样且名字一样
                                //   Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());

                                if (localMedia.getPath().equals(path) && localMedia.getFileName().equals(selectMedia.getFileName())) {
                                    contains = true;
                                }
                            }

                        }
                        if (!contains) {
                            File file = new File(path);
                            File tempFile = new File(tempFilePath, file.getName());
                            File newFile = new File(localfilePath, file.getName());
                            //复制选择图片文件
                            boolean isCopy = false;
                            boolean isTempExist = tempFile.exists();
                            //  Log.e("nb", "isTempExist:" + isTempExist);
                            if (!isTempExist) {
                                isCopy = FileUtils.copyFile(file, tempFile);
                                //   Logger.i("%s copy:%s", path, String.valueOf(isCopy));
                            }
                            // Log.e("nb", "isCopy:" + isCopy);
                            if (isCopy || isTempExist) {
                                double latitude = 0;
                                double longitude = 0;
                                String lastAddress = Config.getLastAddress();
                                LatLng lastLocation = Config.getLastLocation();
                                if (lastLocation != null) {
                                    latitude = lastLocation.getLatitude();
                                    longitude = lastLocation.getLongitude();
                                }

                                WatermarkSettings.getmInstance(this);

                                Bitmap bitmap2 = WatermarkSettings.createWatermark(tempFile.getPath(), Perference.getUserId(), custName, lastAddress, String.format("( %s,%s)", latitude, longitude), DateUtil.getDate2(System.currentTimeMillis()));
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
                                localData.add(localMedia);
                                dataList.add(1, new ImageSectionBean(localMedia));
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
                    mRemoteImageAdapter.notifyDataSetChanged();
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
        return R.layout.fragment_photo_selector2;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {
        loadMoreImage(true);
    }


    private void loadInitImage() {
        showLoading();
        pageNo = 1;
        Map<String, Object> map = new HashMap<>();
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
                            mRemoteImageAdapter.setNewData(dataList);
                            mRemoteImageAdapter.loadMoreEnd();
                            hideLoading();
                            return;
                        }
                        if (TextUtils.isEmpty(localfilePath)) {
                            localfilePath = AttachmentProcesser.getInstance(PhotoSelectorActivity2.this).getPhotoPath(caseId);
                        }
                        for (int i = 0; i < uploadImageDataBean.getRecords().size(); i++) {
                            ImageRecordsBean recordsBean = uploadImageDataBean.getRecords().get(i);

                            String path = localfilePath + File.separator + recordsBean.getFileName();
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(path);
                            dataList.add(new ImageSectionBean(localMedia));
                            if (!FileUtils.isFileExists(path)) {
                                downLoad(recordsBean.getFileName(), recordsBean.getFilePath(), i);
                            }
                        }

                        mRemoteImageAdapter.setNewData(dataList);
                        saveDataToDb();
                        hideLoading();
                        pageNo++;

                    }

                });
    }


    private void loadMoreImage(boolean isFresh) {
        Map<String, Object> map = new HashMap<>();
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
                            //  mRemoteImageAdapter.setNewData(remoteFileList);
                            mRemoteImageAdapter.loadMoreEnd();
                            hideLoading();
                            return;
                        }
                        if (TextUtils.isEmpty(localfilePath)) {
                            localfilePath = AttachmentProcesser.getInstance(PhotoSelectorActivity2.this).getPhotoPath(caseId);
                        }
                        for (int i = 0; i < uploadImageDataBean.getRecords().size(); i++) {
                            ImageRecordsBean recordsBean = uploadImageDataBean.getRecords().get(i);

                            String path = localfilePath + File.separator + recordsBean.getFileName();
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(path);
                            dataList.add(new ImageSectionBean(localMedia));
                            if (!FileUtils.isFileExists(path)) {
                                downLoad(recordsBean.getFileName(), recordsBean.getFilePath(), i);
                            }
                        }
                        mRemoteImageAdapter.notifyDataSetChanged();

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

        for (ImageSectionBean imageSectionBean : dataList) {
            FileData fileData = new FileData();
            if (!imageSectionBean.isHeader && !imageSectionBean.isAdd()) {
                File file = new File(imageSectionBean.t.getPath());
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


    }

}
