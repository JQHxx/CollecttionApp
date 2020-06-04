/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.huateng.collection.utils;

import android.content.Context;
import android.os.Environment;

import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.utils.cases.AttachmentProcesser;
import com.huateng.collection.utils.cases.CaseManager;

import java.io.File;
import java.util.List;

/**
 * @author 咖枯
 * @version 1.0 2016/2/7_除夕
 */
public class DataCleanManager {

    public static String getTotalCacheSize(Context context) {
        AttachmentProcesser processer = AttachmentProcesser.getInstance(context);
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
            //案件相关文件
            cacheSize += getFolderSize(new File(AttachmentProcesser.ATTACHMENT_ROOT));
        }
        return FileUtil.formatFileSize(cacheSize, "0");
    }


    public static void clearAllCache(Context context) {
        //删除cache文件
        deleteDir(context.getCacheDir());

        AttachmentProcesser processer = AttachmentProcesser.getInstance(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //删除zip文件
            String zipPath = processer.getZipsDir();
            File zipDir = new File(zipPath);

            String[] fileNames = zipDir.list();
            if (null != fileNames && fileNames.length > 0) {
                for (String child : fileNames) {
                    deleteDir(new File(zipDir, child));
                }
            }

            //删除已上传案件外访文件夹
            List<RespCaseSummary> summarys = CaseManager.obtainUploadedCaseSummary();

            if (null != summarys) {
                for (RespCaseSummary summary : summarys) {
                    //清除已上传案件
                    String bizId = summary.getBizId();
                    //删除案件相关业务信息
                    CaseManager.removeBizDatas(bizId);

                    String processId = summary.getCaseId();

                    Utils.deleteDir(context, new File(processer.getCaseRoot(processId)));
                }
            }

            //删除无用案件文件夹
            File rootDir = new File(AttachmentProcesser.ATTACHMENT_ROOT);
            File[] processDirs = rootDir.listFiles();

            List<RespCaseSummary> caseSummaries = CaseManager.obtainCasesSummaryForUser();
            for (int i = 0; i < processDirs.length; i++) {
                boolean isValid = false;
                File processDir = processDirs[i];
                String dirName = processDir.getName();

                if (null != caseSummaries) {
                    //判断文件夹 在缓存案件的数据是否对应
                    for (int j = 0; j < caseSummaries.size(); j++) {
                        RespCaseSummary summary = caseSummaries.get(j);
                        String processId = summary.getCaseId();
                        if (dirName.contains(processId)) {
                            isValid = true;
                        }
                    }
                }

//                Logger.i(processDir.getName());
                //删除无效案件文件夹
                if (!isValid && !dirName.equals("zips")) {
                    boolean isDel = Utils.deleteDir(context, processDir);
//                    Logger.i(String.valueOf(isDel));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

/*    *//**
     * 格式化单位
     *
     * @param size
     * @return
     *//*
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }*/


}
