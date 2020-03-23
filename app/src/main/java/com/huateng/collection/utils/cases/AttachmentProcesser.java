package com.huateng.collection.utils.cases;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import com.huateng.collection.app.Config;
import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.utils.Utils;
import com.tools.utils.FileUtils;
import com.tools.utils.TimeUtils;

import java.io.File;
import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-19.
 * <p>
 * <p>
 * 案件文件路径处理
 */
public class AttachmentProcesser {

    private Context context;
    private static AttachmentProcesser instance;
    public static String ATTACHMENT_ROOT;

    public static AttachmentProcesser getInstance(Context context) {
        if (instance == null) {
            instance = new AttachmentProcesser(context);
        }
        return instance;
    }

    public AttachmentProcesser(Context context) {
        this.context = context;
    }

    public void initPath() {
        //"/collections/files/" context.getExternalCacheDir().getPath()
        ATTACHMENT_ROOT = String.format("%s%s", Environment.getExternalStorageDirectory().getPath(), "/collections/files/");

        FileUtils.createOrExistsDir(ATTACHMENT_ROOT);

        if (Config.getBoolean(Config.FIRST_OPEN)) {
            Utils.deleteDir(context, new File(ATTACHMENT_ROOT));
        }
    }

    public String getZipsDir() {
        return String.format("%s/zips/", ATTACHMENT_ROOT);
    }

    public String getTempsDir() {
        return String.format("%s/temps/", ATTACHMENT_ROOT);
    }

    public String getCaseRoot(String processId) {
        return String.format("%s%s_%s_%s", ATTACHMENT_ROOT, processId, Perference.getUserId(), TimeUtils.getNowTimeString("yyyyMMdd"));
    }

    public String getZipPath(String processId) {
        return String.format("%s/%s_%s_%s.zip", getZipsDir(), processId, Perference.getUserId(), TimeUtils.getNowTimeString("yyyyMMdd"));
    }

    //普通录音或选择的音频文件
    public String getVoicePath(String processId) {
        return String.format("%s/voice/", getCaseRoot(processId));
    }

    //案件电话录音根目录
    public String getCaseCallRecordRootPath(String processId) {
        return String.format("%s/record/", getCaseRoot(processId));
    }

    //具体案件电话录音目录
    public String getCallRecordPath(String processId, String userName, String caseId) {
        //案件录音文件夹/客户姓名_案件号/生成年月日/
        return String.format("%s%s_%s/%s/", getCaseCallRecordRootPath(processId), userName, caseId, TimeUtils.getNowTimeString("yyyyMMdd"));
    }

    //后面带斜杠与dataloder的匹配不上
    public String getPhotoPath(String processId) {
        return String.format("%s/image", getCaseRoot(processId));
    }

    public String getReportPath(String processId) {
        return String.format("%s/report/", getCaseRoot(processId));
    }

    public String getGpsPath(String processId) {
        return String.format("%s/gps/", getCaseRoot(processId));
    }


    public void checkPath(String bizId, String processId) {
        boolean needMergeOld = false;
        String oldCaseDir = null;
        File rootDir = new File(ATTACHMENT_ROOT);
        if (rootDir.exists() && null != rootDir.list()) {
            for (String dirName : rootDir.list()) {
                //存在操作员与案件号相同文件夹
                if (dirName.contains(processId) && dirName.contains(Perference.getUserId())) {
                    String dir = String.format("%s%s", ATTACHMENT_ROOT, dirName);
                    //如果文件夹名字与当前日期生成的不同
                    if (!dir.equals(getCaseRoot(processId))) {
                        oldCaseDir = dir;
                        needMergeOld = true;
                        break;
                    }
                }
            }
        }
        //案件目录已存在时  每一天更新目录名称为当天
        if (needMergeOld) {
            String newCaseDir = getCaseRoot(processId);
            if (!oldCaseDir.equals(newCaseDir)) {
//                Log.i("oldDir", "------------->" + oldCaseDir);
//                Log.i("newDir", "------------->" + newCaseDir);
                File oldDir = new File(oldCaseDir);
                if (oldDir.exists()) {
                    File newDir = new File(newCaseDir);
                    boolean renamed = oldDir.renameTo(newDir);
                    //扫描案件文件夹
                    MediaScannerConnection.scanFile(context, new String[]{ATTACHMENT_ROOT}, null, null);

                    Log.i("每天重命名案件文件.", "------------->" + renamed);

                    //删除以前生成的外访报告文件
                    FileUtils.deleteFilesInDir(getReportPath(processId));

                    //TODO 变更文件夹名称后 刷新媒体库文件
                    List<FileData> audioDatas = CaseManager.obtainRecordDatas(bizId);
                    //对文件进行同步
                    Utils.mediaFileSync(context, getVoicePath(processId), audioDatas, FileData.TYPE_AUDIO, bizId);

                    List<FileData> photoDatas = CaseManager.obtainPhotoDatas(bizId);
                    Utils.mediaFileSync(context, getPhotoPath(processId), photoDatas, FileData.TYPE_PHOTO, bizId);

                }
                Utils.scanAllMedia(context);
            }
        } else {
            FileUtils.createOrExistsDir(getCaseRoot(processId));
            FileUtils.createOrExistsDir(getVoicePath(processId));
            FileUtils.createOrExistsDir(getPhotoPath(processId));
            FileUtils.createOrExistsDir(getGpsPath(processId));
            FileUtils.createOrExistsDir(getReportPath(processId));
            FileUtils.createOrExistsDir(getCaseCallRecordRootPath(processId));
            FileUtils.createOrExistsDir(getZipsDir());
            FileUtils.createOrExistsDir(getTempsDir());

        }
    }

    /**
     * 返回一个 由案件号和地址编号组成的id
     *
     * @param caseId
     * @param addrId
     * @return
     */
    public static String getProcessId(String caseId, String addrId) {
        return String.format("%s_%s", caseId, addrId);
    }

}
