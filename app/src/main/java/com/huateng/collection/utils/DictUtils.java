package com.huateng.collection.utils;

/**
 * author: yichuan
 * Created on: 2020-05-12 13:53
 * description:
 */
public class DictUtils {

    /**
     * 单位编制映射
     *
     * @param key
     * @return
     */
    public static final String getUnitsCompiled(String key) {
        String unitsCompild = "";
        switch (key) {
            case "01":
                unitsCompild = "国有";
                break;
            case "02":
                unitsCompild = "独资";
                break;
            case "03":
                unitsCompild = "合资";
                break;
            case "04":
                unitsCompild = "股份制";
                break;
            case "05":
                unitsCompild = "私营";
                break;
            case "06":
            case "10":
                unitsCompild = "其他";
                break;
            case "1":
                unitsCompild = "国有企业";
                break;
            case "2":
                unitsCompild = "私营独资企业";
                break;
            case "3":
                unitsCompild = "私营合伙企业";
                break;
            case "4":
                unitsCompild = "股份有限公司";
                break;
            case "5":
                unitsCompild = "私营企业";
                break;
            case "6":
                unitsCompild = "其他内资企业";
                break;
            case "7":
                unitsCompild = "公务员";
                break;
            case "8":
                unitsCompild = "事业单位";
                break;
            case "9":
                unitsCompild = "企业员工";
                break;


        }

        return unitsCompild;
    }

    /**
     * 学历关系映射
     *
     * @param key
     * @return
     */
    public static final String getEducation(String key) {
        String education = "";
        switch (key) {
            case "010":
                education = "博士研究生教育及以上";
                break;
            case "020":
                education = "硕士研究生教育";
                break;
            case "030":
                education = "大学本科教育";
                break;
            case "040":
                education = "大学专科教育";
                break;
            case "050":
                education = "普通高级中学教育";
                break;
            case "060":
            case "070":
                education = "初级中学教育及以下";
                break;
            case "080":
                education = "中等职业教育";
                break;
            case "090":
                education = "其他";
                break;
            case "C_1":
            case "L_6":
                education = "初中";
                break;
            case "C_2":
                education = "高中";
                break;
            case "C_3":
            case "L_4":
                education = "大专";
                break;
            case "C_4":
            case "L_3":
                education = "本科";
                break;
            case "C_5":
            case "L_2":
                education = "硕士";
                break;
            case "C_6":
            case "L_1":
                education = "博士";
                break;
            case "L_5":
                education = "高中，职高，中专";
                break;
            case "L_7":
                education = "初中以下";
                break;
        }


        return education;
    }

    /**
     * 核销状态
     *
     * @param key
     * @return
     */

    public static final String getAcctStatus(String key) {
        String acctStatus = "";
        switch (key) {
            case "01":
                acctStatus = "正常";
                break;
            case "02":
                acctStatus = "逾期";
                break;
            case "03":
                acctStatus = "部分逾期";
                break;
            case "04":
                acctStatus = "呆滞";
                break;
            case "05":
                acctStatus = "部分呆滞";
                break;
            case "06":
                acctStatus = "呆帐";
                break;
            case "07":
                acctStatus = "核销";
                break;
            case "08":
                acctStatus = "销户";
                break;
            case "A0":
                acctStatus = "申请失败";
                break;
            case "A1":
                acctStatus = "待发布";
                break;
            case "A2":
                acctStatus = "拒绝";
                break;
            case "A3":
                acctStatus = "发布申请中";
                break;
            case "A4":
                acctStatus = "分行一审";
                break;
            case "A5":
                acctStatus = "总行一审";
                break;
            case "A6":
                acctStatus = "总行二审";
                break;
            case "C0":
                acctStatus = "撤销";
                break;
            case "S0":
                acctStatus = "融资中";
                break;
            case "S1":
                acctStatus = "融资失败";
                break;
            case "S2":
                acctStatus = "融资完成";
                break;
            case "S5":
                acctStatus = "放款中";
                break;
            case "S6":
                acctStatus = "待还款";
                break;
            case "S7":
                acctStatus = "还款中";
                break;
            case "S8":
                acctStatus = "到期未还款";
                break;
            case "S9":
                acctStatus = "结清";
                break;
            case "S10":
                acctStatus = "已还款";
                break;
            case "S11":
                acctStatus = "提前还款";
                break;
            case "S12":
                acctStatus = "已结清（非足额）";
                break;


        }

        return acctStatus;
    }


    public static final String getWrofFlag(String key) {
        String wrofFlag = "";
        switch (key) {
            case "0":
                wrofFlag = "未核销";
                break;

            case "1":
                wrofFlag = "部分核销";
                break;
            case "2":
                wrofFlag = "核销";
                break;
        }
        return wrofFlag;
    }

}
