package com.huateng.phone.collection.app;

//import com.amap.api.maps2d.model.LatLng;


/**
 * Created by shanyong on 2016/12/14.
 */

public class Constants {


    public static final int REQUEST_CODE_TAKE_PIC = 666;
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int RESULT_CODE_BACK = 200;

    public static final int REFRESH = 0;
    public static final int LOAD_MORE = 1;
    public static final String IS_FIRST = "is_first";

    public static final String POSITION = "position";

    public static final String CHECK_IS_LOGIN = "check_is_login";
    public static final String BUSINESS_TYPE = "business_type";
    public static final String CASE_ID = "case_id";
    public static final String CUST_NAME = "cust_name";
    public static final String ADDRESS_ID = "address_id";
    public static final String VISIT_ADDRESS = "visit_address";
    public static final String CASE_DETAIL = "case_detail";
    public static final String CASE_ACCOUNT_INFO = "case_account_info";
    public static final String CASE_CONTACT_ADDRESS = "case_contact_address";
    public static final String CASE_CONTACT_BOOK = "case_contact_book";
    public static final String CASE_HISTORY_ACTIONS = "case_history_actions";
    public static final String CUST_ID = "cust_id";
    //发件箱案件的几种状态
    public static final String CASE_NORMAL = "case_normal";
    public static final String CASE_PRE_UPLOAD = "case_pre_upload";
    //文件过大
    public static final String CASE_FILE_TOO_LARGE = "case_file_too_large";
    public static final String CASE_UPLOADING = "case_upoloading";
    public static final String CASE_UPLOADED = "case_uploaded";
    public static final String CASE_UPLOAD_ERROR = "case_upload_error";
    public static final String CASE_UPLOAD_CANCEL = "case_upload_cancel";

    public static String getCaseStatusName(String status) {
        String name = "";
        switch (status) {
            case CASE_NORMAL:
                name = "";
                break;
            case CASE_PRE_UPLOAD:
                name = "等待上传";
                break;
            case CASE_FILE_TOO_LARGE:
                name = "附件过大";
                break;
            case CASE_UPLOADING:
                name = "正在上传";
                break;
            case CASE_UPLOADED:
                name = "上传成功";
                break;
            case CASE_UPLOAD_ERROR:
                name = "上传失败";
                break;
            case CASE_UPLOAD_CANCEL:
                name = "上传取消";
                break;
            default:
        }
        return name;
    }


}
