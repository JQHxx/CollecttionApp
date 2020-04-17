package com.huateng.collection.ui.fragment.casebox.casefill;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.huateng.collection.R;
import com.huateng.collection.app.Constants;
import com.huateng.collection.app.Perference;
import com.huateng.collection.base.BaseActivity;
import com.huateng.collection.base.BasePresenter;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.ui.adapter.GridImageAdapter;
import com.huateng.collection.utils.Utils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.widget.Watermark;
import com.huateng.collection.widget.pictureselector.FullyGridLayoutManager;
import com.luck.picture.lib.PicturePlayAudioFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.bean.EventBean;
import com.tools.utils.FileUtils;
import com.tools.utils.ImageUtils;
import com.tools.view.RxTitle;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;
import com.watermark.androidwm_light.utils.BitmapUtils;
import com.tools.bean.BusEvent;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author：shanyong
 * data：2018/12/27
 */

public class PhotoSelectorActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = PhotoSelectorActivity.class.getSimpleName();
    private View rootView;
    private List<LocalMedia> fileList = new ArrayList<>();
    private RxTitle rxTitle;
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 18;

    private LocalMediaLoader mediaLoader;

    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private int themeId;

    private int chooseMode = PictureMimeType.ofImage();

    private String filePath;
    private String tempFilePath;
    private String currentCaseId;
    private String currentAddrId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        Watermark.getInstance()
                .setTextColor(getResources().getColor(R.color.dialogplus_card_shadow))
                .setTextSize(12.0f)
                .setText("测试文本")
                .show(this);

        themeId = R.style.picture_white_style;

        rxTitle = (RxTitle) findViewById(R.id.rx_title);
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        mediaLoader = new LocalMediaLoader(PhotoSelectorActivity.this, chooseMode, false, 0);

        currentCaseId = Perference.getCurrentCaseId();
        currentAddrId = Perference.getCurrentVisitAddressId();
        AttachmentProcesser processer = AttachmentProcesser.getInstance(PhotoSelectorActivity.this);
        filePath = processer.getPhotoPath(AttachmentProcesser.getProcessId(currentCaseId, currentAddrId));
        tempFilePath = processer.getTempsDir();
//        Logger.i(filePath);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PhotoSelectorActivity.this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, false));

        adapter = new GridImageAdapter(PhotoSelectorActivity.this, onAddPicClickListener);
        adapter.setList(fileList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = fileList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                switch (mediaType) {
                    case 1:
                        // 预览图片
                        PictureSelector.create(PhotoSelectorActivity.this).externalPicturePreview(position, fileList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(PhotoSelectorActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        //PictureSelector.create(PhotoSelectorActivity.this).externalPictureAudio(media.getPath());

                        if (!DoubleUtils.isFastDoubleClick()) {
                            PicturePlayAudioFragment fragment =PicturePlayAudioFragment.newInstance(media.getPath());
                            fragment.show(getSupportFragmentManager(),"PicturePlayAudioActivity");

                        }
                        break;
                }
            }
        });

        mediaLoader.loadAllMedia(new LocalMediaLoader.LocalMediaLoadListener() {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders) {
                if (folders.size() > 0) {
                    Logger.i(String.valueOf(folders.size()));
                    for (LocalMediaFolder folder : folders) {
                        if (null != folder.getPath() && new File(filePath).getAbsolutePath().equals(folder.getPath())) {

                            fileList.clear();
                            fileList.addAll(folder.getImages());
                            adapter.notifyDataSetChanged();

                            List<FileData> fileDatas = SugarRecord.find(
                                    FileData.class, "BIZ_ID=? and USER_ID=? and TYPE=? and EXIST=1", currentAddrId, Perference.getUserId(), FileData.TYPE_PHOTO);
                            //对文件进行同步
                            Utils.mediaFileSync(PhotoSelectorActivity.this, filePath, fileDatas, fileList, FileData.TYPE_PHOTO, currentAddrId, currentCaseId);
                          //  RxBus.get().send();
                            EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_CASE_SUMMARY_REMINDER));

                        }
                    }
                } else {
                    Logger.i("未加载到相关文件");
                }
            }
        });

    }


    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(PhotoSelectorActivity.this)
//                  .openGallery(chooseMode)
                    .openGallery(chooseMode)
                   // .openCamera(chooseMode)
                    .theme(themeId)
                    .maxSelectNum(maxSelectNum)
                    .minSelectNum(1)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .previewImage(true)
                    .previewVideo(false)
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(true)
                    .compress(true)
                    .compressMode(compressMode)
                    .glideOverride(160, 160)
                    .previewEggs(true)
                    .isGif(false)
                    .openClickSound(false)
                    .selectionMedia(fileList)
                    .setOutputCameraPath(tempFilePath)
                    .setProcessId(AttachmentProcesser.getProcessId(currentCaseId, currentAddrId))
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 将选择的照片文件复制到案件文件下
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        boolean contains = false;
                        LocalMedia selectMedia = selectList.get(i);
                        for (int j = 0; j < fileList.size(); j++) {
                            LocalMedia localMedia = fileList.get(j);
                            //路径一样且名字一样
                            if (localMedia.getPath().equals(selectMedia.getPath()) && localMedia.getName().equals(selectMedia.getName())) {
                                contains = true;
                            }
                        }
                        if (!contains) {
                            File file = new File(selectMedia.getPath());
                            File tempFile = new File(tempFilePath, file.getName());
                            File newFile = new File(filePath, file.getName());

                            //复制选择图片文件
                            boolean isCopy = false;
                            boolean isTempExist = tempFile.exists();
                            if (!isTempExist) {
                                isCopy = FileUtils.copyFile(file, tempFile);
                                Logger.i("%s copy:%s", selectMedia.getPath(), String.valueOf(isCopy));
                            }
                            if (isCopy || isTempExist) {

                                Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
                                int height = bitmap.getHeight();
                                Logger.i(" image height:%s", height);

                                //textsize字体大小 大约对应3高度  2是padding  水印行数加间隔空行 linecount +linecount+1  height是高度
                                // 根据此计算出要设置的字体大小 x
                                //（textsize*3+2）*(linecount+linecount+1)=height
                                //或有偏差
                                int textsize = (height / (5 * 2 + 1) - 2) / 3;

                                Logger.i(" textsize :%s", textsize);

                                //TODO 给文件加上水印
                                WatermarkText watermarkText = new WatermarkText(String.format("%s_%s", Constants.COMPANY_NAME, Perference.getUserId()))
                                        .setPositionX(0.5)
                                        .setPositionY(0.5)
                                        .setTextColor(Color.parseColor("#DBDBDB"))
                                        .setTextFont(R.font.zcool_black)
//                                        .setTextShadow(0.1f, 5, 5, Color.GRAY)
                                        .setTextAlpha(150)
                                        .setRotation(30)
                                        .setTextSize(textsize);

                                //TODO 设置计算的行数
                                Bitmap textBitmap = BitmapUtils.textAsBitmap(PhotoSelectorActivity.this, watermarkText);
                                Logger.i("text height:%s", textBitmap.getHeight());

                                Bitmap waterMakerBitmap = WatermarkBuilder
                                        .create(PhotoSelectorActivity.this, bitmap)
                                        .loadWatermarkText(watermarkText)
                                        .setTileMode(true) // select different drawing mode.
                                        .getWatermark().getOutputImage();

                                boolean saved = ImageUtils.save(waterMakerBitmap, newFile.getPath(), Bitmap.CompressFormat.JPEG);
                                Logger.i("saved :%s", saved);

                                if (saved) {
//                                     删除临时文件
                                    Utils.deleteImage(PhotoSelectorActivity.this, tempFile.getPath());
                                    //刷新媒体库文件
                                    Utils.scanMediaFile(PhotoSelectorActivity.this, newFile);
                                }
                            }
                        }
                    }

                    adapter.setList(fileList);
                    adapter.notifyDataSetChanged();
                    break;
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
        return R.layout.fragment_photo_selector;
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

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
