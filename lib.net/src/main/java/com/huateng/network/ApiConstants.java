package com.huateng.network;

/**
 * ClassName: NewsApi<p>
 * Fuction: 请求接口<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class ApiConstants {
    //当前app版本号
    public static final String APP_VERSION = "1.0.1";
    /**
     * RELEASE 发布版API
     * DEVELOP  开发版API
     * CUSTOM   自定义
     */
    public static final String API_MODE_RELEASE = "MODE_RELEASE";
    public static final String API_MODE_DEVELOP = "MODE_DEVELOP";
    public static final String API_MODE_CUSTOM = "MODE_CUSTOM";

    public static final String CODE_NETWORK_ERROR = "100";
    public static final String CODE_SERVER_ERROR = "500";

    public static final String ERROR_CODE_SUC = "SUC";

    public static final String ERROR_CODE_EXP = "EXP";

    public static final String CODE_NO_CACHE = "98";
    public static final String CODE_ILLEGAL_JSON = "99";

    public static String DOMAIN = "huiyicc.test001.hjzddata.com";

    public static String DEVELOP_BASE_URL = "http://12.99.114.235:8080/apms/api/ccms-app/";//uat
    // public static String DEVELOP_BASE_URL = "http://12.99.129.113:18088/ccms-app/";//本地开发
    // public static String DEVELOP_BASE_URL = "http://12.99.123.180:18088/ccms-app/";
   // public static String DEVELOP_BASE_URL = "http://12.99.114.208:8080/apms/api/ccms-app/";//sit

    //  public static String DEVELOP_BASE_URL = "https://aaph.nbcb.com.cn/apms/api/ccms-app/";//生产环境

    public static String MOBILE_APP_INTERFACE = "mobileAppInterface";
    public static String MOBILE_APP_OPER_INTERFACE = "mobileAppOperInterface";

    public static final String WEB_LOGIN_URL = "/ccms-app/SSO/toLogin.htm";

    //修改密码
    public static String METHOD_CHANGE_PWD = "changePasswd";
    //销付卡清算
    public static String METHOD_PAYMENT_CALCULATE = "paymentCalculate";
    //获取外访统计
    public static String METHOD_GET_STATICS = "getStatics";
    //查询待办案件
    public static String METHOD_QUERY_TODO_CASE_LIST = "selectWaitingTask";
    //版本更新
    public static String METHOD_VERSION_UPDATE = "versionUpdate";

    //外访记录录入
    public static String INSERT_OUT_BOUND_RECORD = "insertOutBoundRecord";
    //退案
    public static String CASE_RETIRE_BATCH_EXCUTE = "caseRetireBatchExcute";
    //留案
    public static String CASE_RESERVE_BATCH_EXCUTE = "caseReserveBatchExcute";
    //停催
    public static String STOP_CALL_BATCH_EXCUTE = "stopCallBatchExcute";
    // 案件结束处理
    public static String STOP_DEAL_WITH_CASE = "stopDealWithCase";
    //查询是否可以停催 留案操作
    public static String SELECT_OPER_APPR_STOP = "selectOperApprStop";

    //查询用户基础信息
    public static String SELECTED_CUSTOM_INFO = "selectCustomInfo";
    //查询客户账户信息
    public static String SELECTED_ACCT_ACCOUNT_INFO = "selectAcctAccountInfo";
    //查询信用卡信息
    public static String SELECT_CREDIT_CARD_INFO = "selectCreditCardInfo";

    //查询电话信息
    public static String SELECT_CUST_TEL_INFO = "selectCustTelInfo";

    //外访流水
    public static String SELECT_LOG_ACT_ACTION = "selectLogActAction";

    //调查报告新增
    public static String INDERT_OR_UPD_CUST_REPORT_INFO = "indertOrUpdCustReportInfo";

    //调查报告查询
    public static String SELEDT_CUST_REPORT_INFO = "selectCustReportInfo";
    //查询映射字段数据
    public static String SELECT_DATA_BY_DICT_CODE = "selectDataByDictCode";
    //减免申请查询信用卡信息
    public static String SELECT_APPLICATION_RELIEF = "selectApplicationRelief";
    //减免申请
    public static String APPLICATION_RELIEF_BATCH_EXCURE = "applicationReliefBatchExcute";
    //查询已上传的文件
    public static String SELECT_BASE_FILE = "selectBaseFile";

    public static String SELECT_MAP_DISPLAY = "selectMapDisplay";

    //下载

    //外访统计
    public static String SELECT_OUT_BOUND_RECORD = "selectOutBoundRecord";

    //修改密码
    public static String CHANGE_PASSWORD = "changePasswd";
    //新增或删除待外访列表
    public static String INSERT_OR_DEL_CASE_PENDING = "insertOrDelCasePending";

    public static String format(String root, String uri) {
        return String.format("%s/%s", root, uri);
    }

}
