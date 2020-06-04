package com.huateng.network;


import com.huateng.network.bean.ResponseStructure;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface CommonApiService {

    @Headers({"Content-Type: application/json", "Accept: application/json","X-GW-APP-ID:1011"})//需要添加头
    @POST("{root}/{method}.htm")
    Observable<ResponseStructure> mockRequest(@Header("authorization") String auth, @Path("root") String root, @Path("method") String method, @Body RequestBody body);

    @Headers({"Content-Type: application/json", "Accept: application/json","X-GW-APP-ID:1011"})//需要添加头
    @POST("service/request/call.htm")
    Observable<ResponseStructure> authRequest(@Header("authorization") String auth, @Body RequestBody body);


    @Headers({"Content-Type: application/json", "Accept: application/json","X-GW-APP-ID:1011"})//需要添加头
    @POST("service/request/call2.htm")
    Observable<ResponseStructure> request(@Body RequestBody body);


    //上传文件单独实例化了一个Retrofit @see com.huateng.network.upload.RetrofitUtil
    @Multipart
    @POST("file/upload.htm")
    Observable<ResponseBody> uploadFile(@Header("authorization") String auth, @PartMap Map<String, RequestBody> params);

    @Headers({"X-GW-APP-ID:1011"})
    @Multipart
    @POST
    Observable<String> upload(@Url String url,@Header("authorization") String auth, @Part List<MultipartBody.Part> params);


    @Streaming
    @Multipart
    @Headers({"X-GW-APP-ID:1011"})
    @POST("file/appDownload.htm")
    Observable<ResponseBody> startDownLoad(@Header("authorization") String auth,@PartMap Map<String, RequestBody> requestBodyMap);


}
