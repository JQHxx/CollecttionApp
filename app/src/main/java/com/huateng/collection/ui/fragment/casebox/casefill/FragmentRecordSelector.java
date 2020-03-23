package com.huateng.collection.ui.fragment.casebox.casefill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huateng.collection.R;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.ui.adapter.RecorderAdapter;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.utils.Utils;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.utils.FileUtils;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.RecordService;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;


/**
 * author：shanyong
 * data：2018/12/27
 */


public class FragmentRecordSelector extends BaseFragment implements View.OnClickListener {
    private final static String TAG = FragmentRecordSelector.class.getSimpleName();
    private View rootView;
    private List<LocalMedia> fileList = new ArrayList<>();
    private RxTitle rxTitle;
    private RecyclerView recyclerView;
    private RecorderAdapter adapter;
    private int maxSelectNum = 12;

    private LocalMediaLoader mediaLoader;

    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private int themeId;

    private int chooseMode = PictureMimeType.ofAudio();

    private String filePath;
    private String currentCaseId;
    private String currentAddrId;
    private String currentCustName;
    private String currentAddr;

    private String tempFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_selector, container, false);
        }
        return rootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        themeId = R.style.picture_white_style;

        rxTitle = (RxTitle) rootView.findViewById(R.id.rx_title);
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mediaLoader = new LocalMediaLoader(getActivity(), chooseMode, false, 0);

        currentCaseId = Perference.getCurrentCaseId();
        currentAddrId = Perference.getCurrentVisitAddressId();
        currentAddr = Perference.getCurrentVisitAddress();
        currentCustName = Perference.getCurrentCustName();

        AttachmentProcesser processer = AttachmentProcesser.getInstance(getActivity());
        filePath = processer.getVoicePath(AttachmentProcesser.getProcessId(currentCaseId, currentAddrId));
        tempFilePath = processer.getTempsDir();

        //        Logger.i(filePath);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
//        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        adapter = new RecorderAdapter(getActivity(), onAddPicClickListener);
        adapter.setList(fileList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecorderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = fileList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                Log.e("nb","mediaType:"+mediaType);
                PictureSelector.create(FragmentRecordSelector.this).externalPictureAudio(media.getPath());

                /*switch (mediaType) {

                    case 1:
                        // 预览图片
                        PictureSelector.create(FragmentRecordSelector.this).externalPicturePreview(position, fileList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(FragmentRecordSelector.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(FragmentRecordSelector.this).externalPictureAudio(media.getPath());
                        break;
                }*/
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

                            List<FileData> audios = SugarRecord.find(
                                    FileData.class, "BIZ_ID=? and USER_ID=? and TYPE=? and EXIST=1", currentAddrId, Perference.getUserId(), FileData.TYPE_AUDIO);
                            //对文件进行同步
                            Utils.mediaFileSync(mContext, filePath, audios, fileList, FileData.TYPE_AUDIO, currentAddrId, currentCaseId);
                            EventBusActivityScope.getDefault(_mActivity).post(BusEvent.REFRESH_CASE_SUMMARY_REMINDER);
                        }
                    }
                } else {
                    Logger.i("未加载到相关文件");
                }
            }
        });
    }


    private RecorderAdapter.onAddPicClickListener onAddPicClickListener = new RecorderAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //存在录音任务时判断  如果任务id相同则进入，不同的话提示有任务在进行请结束当前录音任务后再进行新的录音任务

            String processId = AttachmentProcesser.getProcessId(currentCaseId, currentAddrId);

            if (RecordService.isRecording()) {
                Intent taskIntent = RecordService.getTaskIntent();
                if (!taskIntent.getStringExtra(AndroidAudioRecorder.EXTRA_RECORD_TASK_ID).equals(processId)) {
                    RxToast.showToast(String.format("当前 %s 正在进行录音任务，请在结束该录音任务后再进行录音", taskIntent.getStringExtra(AndroidAudioRecorder.EXTRA_RECORD_TASK_NAME)));
                    return;
                }
            }

        /*    String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
            Intent intent = new Intent(mContext, AudioRecordServiceActivity.class);
            intent.putExtra(AndroidAudioRecorder.EXTRA_FILE_PATH, filePath);
            intent.putExtra(AndroidAudioRecorder.EXTRA_COLOR, getResources().getColor(R.color.colorPrimaryDark));
            startActivity(intent);*/

            // 进入相册 以下是例子：不需要的api可以不写
           PictureSelector.create(FragmentRecordSelector.this)
                    .openGallery(chooseMode)
                 //   .openCamera(chooseMode)
                    .theme(themeId)
                    .maxSelectNum(maxSelectNum)
                    .minSelectNum(1)
                    .selectionMode(PictureConfig.MULTIPLE)
                    .previewImage(true)
                    .previewVideo(false)
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(true)
                    .compress(false)
                    .compressMode(compressMode)
                    .glideOverride(160, 160)
                    .previewEggs(true)
                    .isGif(false)
                    .openClickSound(false)
                    .selectionMedia(fileList)
                    .setOutputCameraPath(tempFilePath)
                    .setProcessId(processId)
                    .setProcessName(String.format("%s (%s)", currentCustName, currentAddr))
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    Logger.i("selelct size :%s", selectList.size());

                    for (int i = 0; i < selectList.size(); i++) {
                        boolean contains = false;
                        LocalMedia selectMedia = selectList.get(i);
                        for (int j = 0; j < fileList.size(); j++) {
                            LocalMedia localMedia = fileList.get(j);
                            //localmedia 和selectmedia 路径一样且名字一样 跳过

//                            Logger.i("localMedia path %s  name %s", localMedia.getPath(), localMedia.getName());
//                            Logger.i("selectMedia path %s  name %s", selectMedia.getPath(), selectMedia.getName());

                            //表示在文件夹中存在
                            if (localMedia.getPath().equals(selectMedia.getPath()) && localMedia.getName().equals(selectMedia.getName())) {
                                contains = true;
                            }
                        }

                        //案件文件夹中不存在 从缓存文件夹去拷贝过来
                        if (!contains) {
                            File file = new File(selectMedia.getPath());
                            File tempFile = new File(tempFilePath, file.getName());
                            File newFile = new File(filePath, file.getName());

                            boolean isCopy = false;

                            if (!file.getPath().equals(tempFilePath)) {

                                //文件地址与临时文件地址不一样 表示选择文件
                                isCopy = FileUtils.copyFile(file, newFile);
                            } else {
                                //录音到临时文件
                                boolean isTempExist = tempFile.exists();
                                if (isTempExist) {
                                    //将临时文件复制到文件
                                    isCopy = FileUtils.copyFile(tempFile, newFile);
                                }
                            }

                            Logger.i(String.valueOf(isCopy));
                            if (isCopy) {
                                //删除临时文件
                                Utils.deleteMediaFile(mContext, tempFile);
                                //将文件刷新到媒体库
                                Utils.scanMediaFile(mContext, newFile);
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
                getActivity().finish();
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        pop();
        return true;
    }

}
