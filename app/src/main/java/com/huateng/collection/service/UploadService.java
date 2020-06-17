package com.huateng.collection.service;

import android.content.Context;
import android.content.Intent;

import com.aes_util.AESUtils;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.utils.cases.CaseManager;
import com.huateng.network.NetworkConfig;
import com.huateng.network.RetrofitManager;
import com.huateng.network.UploadObserver;
import com.huateng.network.bean.ResponseStructure;
import com.huateng.network.upload.UploadParam;
import com.luck.picture.lib.entity.LocalMedia;
import com.orm.SugarRecord;
import com.tools.utils.GsonUtils;
import com.tools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * author: yichuan
 * Created on: 2020/6/15 10:58
 * description:
 */
public class UploadService extends JobIntentService {
    private String caseId;
    int imageSize;

    private static final int JOB_ID = 1005;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, UploadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
      //  EventBus.getDefault().post(new EventBean(BusEvent.BIZ_ACCT_REDUCE_INFO, new UploadEventBean());

        List<LocalMedia> fileList = intent.getParcelableArrayListExtra("list");
        caseId = intent.getStringExtra("caseId");
        //获取传递过来的参数
        String fileDate = "";
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
                .subscribe(new UploadObserver<ResponseStructure>() {
                    @Override
                    public void _onProgress(Integer percent) {
                        super._onProgress(percent);
                        // Log.e("nb", "进度：" + percent);
                    }

                    @Override
                    public void _onNext(ResponseStructure o) {

                        if (o.getScubeHeader() != null && "EXP".equals(o.getScubeHeader().getErrorCode())) {

                            RxToast.showToast(o.getScubeHeader().getErrorMsg());
                        } else if (o.getScubeHeader() != null && "SUC".equals(o.getScubeHeader().getErrorCode())) {
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
                    }

                    @Override
                    public void _onError(Throwable e) {
                    }
                });
    }
}
