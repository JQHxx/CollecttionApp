package com.huateng.collection.bean;

/**
 * author: yichuan
 * Created on: 2020/6/17 17:44
 * description:
 */
public class UploadEventBean {

    private String  type ; // 0 图片 1 录音文件

    private String status; //0开始上传  1 上传成功  -1 上传失败


     public UploadEventBean(String  type,String status){

         this.type = type;
         this.status =status;

     }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
