package com.huateng.network;

/**
 * ClassName: NewsApi<p>
 * Fuction: 请求接口<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class ApiConstants {

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

    public static final String RESULT_CODE_SUC = "0000";
    public static final String RESULT_CODE_EXP = "1111";

    public static final String CODE_NO_CACHE = "98";
    public static final String CODE_ILLEGAL_JSON = "99";

    public static String DOMAIN = "huiyicc.test001.hjzddata.com";

    public static final String ENDPOINT = "http://360cool.net:8021/";
    public static final String ROUTER = "mockjsdata/2/";

//    public static final String RELEASE_BASE_URL = "http://huiyicc.test001.hjzddata.com/ccms-app/";
    public static final String RELEASE_BASE_URL = "http://10.252.0.23:18088/ccms-app/";

    public static String MOCKDATA_BASE_URL = "http://rap2api.taobao.org/app/mock/87147/";
    public static String DEVELOP_BASE_URL = "http://170.252.199.177:18088/ccms-app/";

    public static String APP_ROOT = "appInteface";
    public static String BATCH_ROOT = "caseDetailBatch";

    public static final int UPLOAD_CASIES = 9527;

    public static final String URL_REGISTER = ENDPOINT + "register";
    public static final String URL_GET_TODO_CASES = ENDPOINT + ROUTER + "queryTodoCaseList";

    public static final String WEB_LOGIN_URL = "/ccms-app/SSO/toLogin.htm";

    //登录
    public static String METHOD_LOGIN = "login";
    //
    public static String METHOD_LOGOUT = "logout";
    //修改密码
    public static String METHOD_CHANGE_PWD = "changePasswd";
    //销付卡清算
    public static String METHOD_PAYMENT_CALCULATE = "paymentCalculate";
    //批量获取案件详情
    public static String METHOD_GET_CASE_DETAIL_BATCH = "getCaseDetailBatch";
    //获取外访统计
    public static String METHOD_GET_STATICS = "getStatics";
    //添加外访日志
    public static String METHOD_ADD_VISIT_LOG = "addVisitLog";
    //添加地址
    public static String METHOD_ADD_ADDRESS = "addAddr";
    //添加电话
    public static String METHOD_ADD_TEL = "addTel";
    //查询字典
    public static String METHOD_QUERY_DICT = "queryAllDict";
    //查询待办案件
    public static String METHOD_QUERY_TODO_CASE_LIST = "queryTodoCaseList";
    //查询代办案件客户
    public static String METHOD_QUERY_TODO_CUSTS = "queryTodoCusts";
    //
    public static String METHOD_SEND_CASE_INFO = "sendCaseInfo";
    //版本更新
    public static String METHOD_VERSION_UPDATE = "versionUpdate";
    //外访行动码
    public static String METHOD_VISIT_SUM_CODE = "queryVisitCode";
    //更新外访状态
    public static String METHOD_UPDATE_VISIT_STATUS = "updateVisitStatus";

    public static String format(String root, String uri) {
        return String.format("%s/%s", root, uri);
    }

}
