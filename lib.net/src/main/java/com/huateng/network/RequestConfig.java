package com.huateng.network;

import com.huateng.network.bean.RequestStructure;
import com.huateng.network.bean.RequestStructure.QueryTableBean;
import com.huateng.network.bean.RequestStructure.QueryTableBean.ScubeBodyBean;
import com.huateng.network.bean.RequestStructure.QueryTableBean.ScubeBodyBean.ContextDataBean;
import com.huateng.network.bean.RequestStructure.QueryTableBean.ScubeHeaderBean;


/**
 * Created by shanyong on 2016/10/10.
 * <p>
 * 配置请求体
 */

public class RequestConfig {

    static volatile RequestStructure INSTANCE;

    public static RequestStructure getStructureInstance() {
        if (INSTANCE == null) {
            synchronized (RequestStructure.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RequestStructure();
                    //1
                    QueryTableBean queryTable = new QueryTableBean();

                    //2
                    ScubeHeaderBean header = new ScubeHeaderBean();

                    header.setErrorCode("");
                    header.setErrorMsg("");
                    header.setXsessionToken("collections_android_app");

                    ScubeBodyBean body = new ScubeBodyBean();

                    //3
                    ContextDataBean contextData = new ContextDataBean();

                    //4
                    Object dataBean = new Object();

                    contextData.setDomainVilidate("");
                    contextData.setData(dataBean);

                    body.setContextData(contextData);

                    queryTable.setScubeHeader(header);
                    queryTable.setScubeBody(body);

                    INSTANCE.setQueryTable(queryTable);
                }
            }
        }
        return INSTANCE;
    }

//    public static Call<ResponseBody> uploadFileCall(String descriptionString, File file) {
//
//        // 创建 RequestBody，用于封装 请求RequestBody
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        // MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//        // 添加描述
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);
//
//        ApiService service = RetrofitHelper.getInstance();
//        Call<ResponseBody> call = service.uploadImage(description, body);
//        return call;
//    }


}
