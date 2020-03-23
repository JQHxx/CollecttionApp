package com.huateng.collection.utils.cases;

import android.text.TextUtils;

import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespAccount;
import com.huateng.collection.bean.api.RespAddress;
import com.huateng.collection.bean.api.RespCaseDetail;
import com.huateng.collection.bean.api.RespCaseSummary;
import com.huateng.collection.bean.api.RespLog;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.bean.orm.FileData;
import com.huateng.collection.bean.orm.PendingReportData;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;
import com.tools.utils.StringUtils;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-20.
 */
public class CaseManager {

    public static boolean reportCompleted(String bizId) {
        long count = SugarRecord.count(PendingReportData.class, "BIZ_ID=? and USER_ID=?", new String[]{bizId, Perference.getUserId()});
        if (count > 30) {
            return true;
        }
        return false;
    }

    public static boolean takePhotoCompleted(String bizId) {
        long count = SugarRecord.count(FileData.class, "BIZ_ID=? and USER_ID=? and TYPE=?", new String[]{bizId, Perference.getUserId(), FileData.TYPE_PHOTO});
        if (count > 0) {
            return true;
        }
        return false;
    }

    public static boolean recordCompleted(String bizId) {
        long count = SugarRecord.count(FileData.class, "BIZ_ID=? and USER_ID=? and TYPE=?", new String[]{bizId, Perference.getUserId(), FileData.TYPE_AUDIO});
        if (count > 0) {
            return true;
        }
        return false;
    }

    public static List<FileData> obtainPhotoDatas(String bizId) {
        List<FileData> fileDatas = SugarRecord.find(FileData.class, "TYPE=? and BIZ_ID=?", FileData.TYPE_PHOTO, bizId);
        return fileDatas;
    }

    public static List<FileData> obtainRecordDatas(String bizId) {
        List<FileData> fileDatas = SugarRecord.find(FileData.class, "TYPE=? and BIZ_ID=?", FileData.TYPE_AUDIO, bizId);
        return fileDatas;
    }

    public static List<FileData> obtainRecordDatas() {
        List<FileData> fileDatas = SugarRecord.find(FileData.class, "TYPE=?", FileData.TYPE_AUDIO);
        return fileDatas;
    }


    public static boolean isCaseCached(String bizId) {
        long count = SugarRecord.count(RespCaseDetail.class, "BIZ_ID=?", new String[]{bizId});
        if (count > 0) {
            return true;
        }
        return false;
    }


    //获取所有案件
    public static List<RespCaseSummary> obtainCaseSummaryByCaseId(String caseId) {
        String userId = Perference.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "USER_ID=? and CASE_ID=? ", userId, caseId);
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries;
    }

    //获取已办案件
    public static List<RespCaseSummary> obtainDoneCaseSummaryForUser() {
        String userId = Perference.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "DONE=1 and USER_ID=? ", userId);
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries;
    }

    //获取待办案件
    public static RespCaseSummary obtainCachedCaseSummary(String bizId) {
        String userId = Perference.getUserId();

        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "BIZ_ID=? and USER_ID=? ", bizId, userId);
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries.get(0);
    }

    //查询出未上传的案件
    public static List<RespCaseSummary> obtainCasesSummaryForUser() {
        if (StringUtils.isEmpty(Perference.getUserId())) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "USER_ID=? AND UPLOADED=0 ", Perference.getUserId());
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries;
    }


    // 本地使用 根据案件id 获取业务id
    public static String obtainBizIdbyCaseId(String caseId) {
        String userId = Perference.getUserId();
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "CASE_ID=? and USER_ID=? ", caseId, userId);
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries.get(0).getBizId();
    }


    public static List<RespCaseSummary> obtainUploadedCaseSummary() {
        String userId = Perference.getUserId();

        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        List<RespCaseSummary> respCaseSummaries = SugarRecord.find(RespCaseSummary.class, "USER_ID=? AND DONE=1 AND UPLOADED=1", userId);
        if (respCaseSummaries == null || respCaseSummaries.size() == 0) {
            return null;
        }
        return respCaseSummaries;
    }

    public static RespCaseDetail obtainCachedCaseDetail(String bizId) {
        List<RespCaseDetail> respCaseDetails = SugarRecord.find(RespCaseDetail.class, "BIZ_ID=?", bizId);
        if (respCaseDetails == null || respCaseDetails.size() == 0) {
            return null;
        }
        return respCaseDetails.get(0);
    }


//    public static List<RespCaseDetail> obtainCachedCaseDetails() {
//        List<RespCaseSummary> summaries = obtainCasesSummaryForUser();
//        List<RespCaseDetail> respCaseDetails = new ArrayList<RespCaseDetail>();
//        for (RespCaseSummary summary : summaries) {
//            RespCaseDetail caseDetail = obtainCachedCaseDetail(summary.getCaseId());
//            respCaseDetails.add(caseDetail);
//        }
//        return respCaseDetails;
//    }


//    public static List<RespAccount> obtainCachedAccounts() {
//        List<RespAccount> records = SugarRecord.find(RespAccount.class, "CASE_ID=?", Perference.getCurrentCaseId());
//        return records;
//    }
//
//    public static List<RespAddress> obtainCachedAddresses() {
//        List<RespAddress> records = SugarRecord.find(RespAddress.class, "CASE_ID=?", Perference.getCurrentCaseId());
//        return records;
//    }
//
//    public static List<RespPhone> obtainCachedPhones() {
//        List<RespPhone> records = SugarRecord.find(RespPhone.class, "CASE_ID=?", Perference.getCurrentCaseId());
//        return records;
//    }
//
//    public static List<RespLog> obtainCachedLogs() {
//        List<RespLog> records = SugarRecord.find(RespLog.class, "CASE_ID=?", Perference.getCurrentCaseId());
//        return records;
//    }

    public static List<RespAccount> obtainCachedAccounts(String bizId) {
        List<RespAccount> records = SugarRecord.find(RespAccount.class, "BIZ_ID=?", bizId);
        return records;
    }

    public static List<RespAddress> obtainCachedAddresses(String bizId) {
        List<RespAddress> records = SugarRecord.find(RespAddress.class, "BIZ_ID=?", bizId);
        return records;
    }

    public static RespAddress obtainCachedAddress(String addrId) {
        List<RespAddress> records = SugarRecord.find(RespAddress.class, "ADDR_ID=?", addrId);
        if (null != records && records.size() > 0) {
            return records.get(0);
        }
        return null;
    }

    public static List<RespPhone> obtainCachedPhones(String bizId) {
        List<RespPhone> records = SugarRecord.find(RespPhone.class, "BIZ_ID=?", bizId);
        return records;
    }

    public static List<RespLog> obtainCachedLogs(String bizId) {
        List<RespLog> records = SugarRecord.find(RespLog.class, "BIZ_ID=?", bizId);
        return records;
    }

    public static boolean removeCachedCaseSummary(String bizId) {
        int i = SugarRecord.deleteAll(RespCaseSummary.class, "BIZ_ID=? and USER_ID=? ", bizId, Perference.getUserId());
        if (i > 0) {
            return true;
        }
        return false;
    }

    public static boolean removeCachedCaseSummaries() {
        int i = SugarRecord.deleteAll(RespCaseSummary.class, "USER_ID=?", Perference.getUserId());
        if (i > 0) {
            return true;
        }
        return false;
    }

    public static boolean removeNotDoneCaseSummaries() {
        int i = SugarRecord.deleteAll(RespCaseSummary.class, "USER_ID=? and DONE=0 ", Perference.getUserId());
        if (i > 0) {
            return true;
        }
        return false;
    }


    public static void removeCachedCaseDetail(String bizId) {
        //SugarRecord.deleteAll(RespCaseSummary.class, "CASE_ID=?", caseId);
        SugarRecord.deleteAll(RespCaseDetail.class, "BIZ_ID=?", bizId);
        SugarRecord.deleteAll(RespAccount.class, "BIZ_ID=?", bizId);
        SugarRecord.deleteAll(RespAddress.class, "BIZ_ID=?", bizId);
        SugarRecord.deleteAll(RespPhone.class, "BIZ_ID=?", bizId);
        SugarRecord.deleteAll(RespLog.class, "BIZ_ID=?", bizId);

    }

    //删除外访报告
    public static void removeReportDatas(String bizId) {
        SugarRecord.deleteAll(PendingReportData.class, "BIZ_ID=?", bizId);
    }

    public static void removeFileData(String bizId) {
        //删除文件标志
        SugarRecord.deleteAll(FileData.class, "BIZ_ID=?", bizId);
    }

    //删除与业务相关的所有数据
    public static void removeBizDatas(String bizId) {
        CaseManager.removeReportDatas(bizId);
        CaseManager.removeCachedCaseDetail(bizId);
        CaseManager.removeFileData(bizId);
        CaseManager.removeCachedCaseSummary(bizId);
    }

    public static void removeCachedCaseDetail() {
        SugarRecord.deleteAll(RespCaseDetail.class);
        SugarRecord.deleteAll(RespAccount.class);
        SugarRecord.deleteAll(RespAddress.class);
        SugarRecord.deleteAll(RespPhone.class);
        SugarRecord.deleteAll(RespLog.class);
    }


    /**
     * 缓存案件详情
     *
     * @param caseDetails
     */
    public static void cacheCaseDetail(List<RespCaseDetail> caseDetails) {
        for (RespCaseDetail respCaseDetail : caseDetails) {
            String caseId = respCaseDetail.getCaseId();

            List<RespCaseSummary> summaries = obtainCaseSummaryByCaseId(caseId);
            if (null != summaries) {
                for (RespCaseSummary summary : summaries) {
                    //业务id
                    String bizId = summary.getBizId();
                    Logger.i(bizId);
                    cacheCaseDetail(respCaseDetail, bizId);
                }
            }
        }
    }

    /**
     * 缓存单个案件详情
     */
    public static void cacheCaseDetail(RespCaseDetail caseDetail, String bizId) {
        //删除旧数据
        removeCachedCaseDetail(bizId);

        String caseId = caseDetail.getCaseId();

        List<RespAccount> respAccounts = caseDetail.getAcctList();
        for (RespAccount respAccount : respAccounts) {
            respAccount.setCaseId(caseId);
            respAccount.setBizId(bizId);
        }
        SugarRecord.saveInTx(respAccounts);

        List<RespAddress> respAddresses = caseDetail.getAddrList();
        for (RespAddress respAddress : respAddresses) {
            respAddress.setCaseId(caseId);
            respAddress.setBizId(bizId);
        }

        SugarRecord.saveInTx(respAddresses);
        List<RespLog> respLogs = caseDetail.getActLogList();
        for (RespLog respLog : respLogs) {
            respLog.setCaseId(caseId);
            respLog.setBizId(bizId);
        }

        SugarRecord.saveInTx(respLogs);
        List<RespPhone> respPhones = caseDetail.getTelList();
        for (RespPhone respPhone : respPhones) {
            respPhone.setCaseId(caseId);
            respPhone.setBizId(bizId);
        }

        SugarRecord.saveInTx(respPhones);

        //给案件详情设置业务id
        caseDetail.setBizId(bizId);

        SugarRecord.save(caseDetail);
    }

}
